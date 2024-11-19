import torch
import os
import cv2
import subprocess
from pathlib import Path
from typing import Tuple, Optional
import logging


class VideoUpscaler:
    def __init__(self):
        # GPU 사용 가능 여부 확인
        self.has_gpu = torch.cuda.is_available()
        if not self.has_gpu:
            logging.warning("GPU not detected. Processing might be slow.")

        # 로깅 설정
        logging.basicConfig(level=logging.INFO)
        self.logger = logging.getLogger(__name__)

    def validate_paths(self, video_path: str, output_dir: str) -> Tuple[Path, Path]:
        """입력 및 출력 경로 유효성 검사"""
        video_path = Path(video_path)
        output_dir = Path(output_dir)

        if not video_path.exists():
            raise FileNotFoundError(f"Video file not found: {video_path}")

        output_dir.mkdir(parents=True, exist_ok=True)
        return video_path, output_dir

    def get_video_info(self, video_path: Path) -> Tuple[int, int, float, float]:
        """비디오 정보 추출"""
        cap = cv2.VideoCapture(str(video_path))
        if not cap.isOpened():
            raise ValueError("Unable to open video file")

        width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
        height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
        fps = cap.get(cv2.CAP_PROP_FPS)
        total_frames = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))
        cap.release()

        return width, height, fps, total_frames

    def calculate_dimensions(self,
                             current_width: int,
                             current_height: int,
                             target_resolution: str) -> Tuple[int, int, float]:
        """목표 해상도에 따른 크기 계산"""
        resolutions = {
            "4K": (3840, 2160),
            "2K": (2560, 1440),
            "FHD": (1920, 1080)
        }

        if target_resolution not in resolutions:
            raise ValueError(f"Unsupported resolution: {target_resolution}")

        target_width, target_height = resolutions[target_resolution]
        aspect_ratio = current_width / current_height

        # 원본 비율 유지하면서 타겟 해상도에 맞추기
        if aspect_ratio > target_width / target_height:
            final_width = target_width
            final_height = int(target_width / aspect_ratio)
        else:
            final_height = target_height
            final_width = int(target_height * aspect_ratio)

        # 짝수로 맞추기
        final_width = final_width + (final_width % 2)
        final_height = final_height + (final_height % 2)

        scale_factor = max(final_width / current_width, final_height / current_height)

        return final_width, final_height, scale_factor

    def upscale_video(self,
                      video_path: str,
                      output_dir: str,
                      target_resolution: str = "4K",
                      model: str = "RealESRGAN_x4plus",
                      output_filename: Optional[str] = None) -> str:
        """비디오 업스케일링 실행"""
        try:
            # 경로 검증
            video_path, output_dir = self.validate_paths(video_path, output_dir)

            # 출력 파일명 설정
            if output_filename is None:
                output_filename = f"{video_path.stem}_upscaled_{target_resolution}.mp4"

            # 비디오 정보 가져오기
            width, height, fps, total_frames = self.get_video_info(video_path)
            self.logger.info(f"Processing video: {width}x{height} at {fps}fps, {total_frames} frames")

            # 타겟 크기 계산
            final_width, final_height, scale_factor = self.calculate_dimensions(
                width, height, target_resolution
            )

            self.logger.info(f"Target dimensions: {final_width}x{final_height}")

            # 중간 출력 파일
            temp_output = output_dir / f"temp_{output_filename}"
            final_output = output_dir / output_filename

            # Real-ESRGAN 실행
            self.logger.info("Running Real-ESRGAN upscaling...")
            os.system(
                f"python inference_realesrgan_video.py -n {model} "
                f"-i '{video_path}' -o '{temp_output}' --outscale {scale_factor}"
            )

            # FFmpeg로 최종 크기 조정 및 인코딩
            self.logger.info("Post-processing with FFmpeg...")
            encoder = "h264_nvenc" if self.has_gpu else "libx264"
            command = (
                f"ffmpeg -loglevel error -y -i '{temp_output}' "
                f"-c:v {encoder} "
                f"-filter:v 'scale={final_width}:{final_height}:flags=lanczos' "
                f"-c:a copy -pix_fmt yuv420p '{final_output}'"
            )
            subprocess.run(command, shell=True, check=True)

            # 임시 파일 정리
            if temp_output.exists():
                temp_output.unlink()

            self.logger.info(f"Successfully saved upscaled video to: {final_output}")
            return str(final_output)

        except Exception as e:
            self.logger.error(f"Error during video upscaling: {str(e)}")
            raise


if __name__ == "__main__":
    # 사용 예시
    upscaler = VideoUpscaler()
    try:
        result = upscaler.upscale_video(
            video_path="datasets/test.mp4",
            output_dir="datasets/",
            target_resolution="4K",
            output_filename="testresult.mp4"
        )
        print(f"Upscaling completed: {result}")
    except Exception as e:
        print(f"Error: {str(e)}")
