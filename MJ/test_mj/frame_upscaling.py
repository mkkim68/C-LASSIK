import torch
import os
import cv2
import subprocess

# GPU가 사용 가능한지 확인
assert torch.cuda.is_available(), "GPU not detected.. Please change runtime to GPU"

# 비디오 경로 및 결과 저장 경로 설정
video_path = "datasets/test.mp4"  # 입력 비디오 경로
output_dir = "datasets/"  # 결과 비디오 저장 경로
output_video_name = "testresult.mp4"  # 결과 비디오 파일 이름
resolution = "FHD (1920 x 1080)"  # 해상도 설정
model = "RealESRGAN_x4plus"  # 사용할 모델

# 비디오 파일이 존재하는지 확인
assert os.path.exists(video_path), "Video file does not exist"

# 비디오 캡처 객체 생성
video_capture = cv2.VideoCapture(video_path)
video_width = int(video_capture.get(cv2.CAP_PROP_FRAME_WIDTH))
video_height = int(video_capture.get(cv2.CAP_PROP_FRAME_HEIGHT))

final_width = None
final_height = None
aspect_ratio = float(video_width / video_height)

# 출력 해상도 설정
match resolution:
    case "FHD (1920 x 1080)":
        final_width = 1920
        final_height = 1080
    case "2k (2560 x 1440)":
        final_width = 2560
        final_height = 1440
    case "4k (3840 x 2160)":
        final_width = 3840
        final_height = 2160
    case "2 x original":
        final_width = 2 * video_width
        final_height = 2 * video_height
    case "3 x original":
        final_width = 3 * video_width
        final_height = 3 * video_height
    case "4 x original":
        final_width = 4 * video_width
        final_height = 4 * video_height

# 가로 세로 비율에 따라 최종 높이 조정
if aspect_ratio == 1.0 and "original" not in resolution:
    final_height = final_width

if aspect_ratio < 1.0 and "original" not in resolution:
    temp = final_width
    final_width = final_height
    final_height = temp

scale_factor = max(final_width / video_width, final_height / video_height)
isEven = int(video_width * scale_factor) % 2 == 0 and int(video_height * scale_factor) % 2 == 0

# scale_factor가 짝수가 되도록 조정
while not isEven:
    scale_factor += 0.01
    isEven = int(video_width * scale_factor) % 2 == 0 and int(video_height * scale_factor) % 2 == 0

print(f"Upscaling from {video_width}x{video_height} to {final_width}x{final_height}, scale_factor={scale_factor}")

# Real-ESRGAN 비디오 인퍼런스 실행
os.system(f"python inference_realesrgan_video.py -n {model} -i '{video_path}' -o '{output_dir}' --outscale {scale_factor}")

# 업스케일된 비디오 경로 설정
upscaled_video_path = os.path.join(output_dir, "test_out.mp4")  # 업스케일된 비디오 경로
final_video_path = os.path.join(output_dir, output_video_name)  # 최종 결과 비디오 경로

# 크롭하여 맞추기 (해상도에 따라)
if "original" not in resolution:
    print("Cropping to fit...")
    command = f"ffmpeg -loglevel error -y -i '{upscaled_video_path}' -c:v h264_nvenc -filter:v 'crop={final_width}:{final_height}:(in_w-{final_width})/2:(in_h-{final_height})/2' -c:v libx264 -pix_fmt yuv420p '{final_video_path}'"
    subprocess.run(command, shell=True)

print(f"Upscaled video saved to: {final_video_path}")

# 임시 업스케일된 비디오 파일 삭제
# os.remove(upscaled_video_path)
