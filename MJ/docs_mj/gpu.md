## 가상환경 생성

<aside>
  💡

  A604  jupyter계정 - jupyter01 http://70.12.130.101

  ID : k11a605     PW : Device8

  https://server.ssafy.com

  ID : ssafy     PW : trainee#0114#

</aside>


```powershell
# 가상환경생성 
python -m venv {가상환경이름}

# 가상환경 활성화
.\venv\Scripts\Activate

```

* 참고한 자료 

https://discuss.pytorch.kr/t/real-esrgan-4k/4281

https://discuss.pytorch.kr/t/cuda/1277

## Anaconda

```powershell
# 가상환경 생성
conda create -n a604 python=3.9

# 가상환경 활성화 
conda activate a604

# ipykernel을 이용해 jupyternotebook과 연결
python -m ipykernel install --user --name a604 --display-name a604

# GPU 사용률 확인 
nvidia-smi

# CUDA 컴파일러 버전 확인
nvcc -V

# torch를 cuda 버전에 맞춰서 설치 
pip install torch==1.12.1+cu116 torchvision==0.13.1+cu116 torchaudio==0.12.1 --extra-index-url https://download.pytorch.org/whl/cu116
```

### NVIDIA 버전

NVIDIA-SMI 535.183.01   |   Driver Version: 535.183.01   |   CUDA Version: 12.2