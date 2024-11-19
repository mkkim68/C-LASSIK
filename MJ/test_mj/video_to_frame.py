import cv2
import os

# opencv version확인
print(cv2.__version__)

# 비디오 가져오기
filepath = "./datasets/test.mp4"
video = cv2.VideoCapture(filepath)

if not video.isOpened():
    print("Could not Open :", filepath)
    exit(0)

#불러온 비디오 파일의 정보 출력
length = int(video.get(cv2.CAP_PROP_FRAME_COUNT))
width = int(video.get(cv2.CAP_PROP_FRAME_WIDTH))
height = int(video.get(cv2.CAP_PROP_FRAME_HEIGHT))
fps = video.get(cv2.CAP_PROP_FPS)

print("length :", length)
print("width :", width)
print("height :", height)
print("fps :", fps)

#프레임을 저장할 디렉토리를 생성

try:
    if not os.path.exists(filepath[:-4]):
        os.makedirs(filepath[:-4])
except OSError:
    print ('Error: Creating directory. ' +  filepath[:-4])

count = 0

while video.isOpened():
    ret, image = video.read()
    if not ret:  # 비디오가 끝나면 종료
        break

    # 현재 프레임 번호를 가져와 fps로 나누어 떨어지는 프레임마다 저장
    frame_id = int(video.get(cv2.CAP_PROP_POS_FRAMES))
    if frame_id % (video.get(cv2.CAP_PROP_FPS) // fps) == 0:
        cv2.imwrite(filepath[:-4] + "/frame%d.jpg" % count, image)
        print('Saved frame number:', str(frame_id))
        count += 1
# 비디오 메모리 해제
video.release()
