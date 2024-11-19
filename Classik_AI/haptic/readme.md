
## venv 설정 

```bash

python -m venv venv

source ./venv/Scripts/activate

pip install -r requirements

```

## How To make Vibrator in Android Studio

### [API 31이상](https://www.google.com/search?q=android+studio+vibratormanager&oq=android+studio+vibratormanager&gs_lcrp=EgZjaHJvbWUyBggAEEUYOdIBCDYxMTRqMGo3qAIAsAIA&sourceid=chrome&ie=UTF-8) 


```kt
public abstract class VibratorManager
extends Object
```

```kt
val vibrator = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
```

VibrationEffect에 이어서 CombinedVibration 이 새로 등장하고 다음과 같이 설명한다.

이전 버전의 VibrationEffect는 하나의 진동 객체에 대해서 적용이 가능했다면 CombinedVibration는 하나 이상의 객체에 적용이 가능하게 하였다.

CombinedVibration 가 제공하는 createParallel()의 기능을 공식문서에서는 다음과 같이 말하고 있다.
CombinedVibration 은 여러 진동 객체에 대해 같은 VibrationEffect를 줄 수 있도록 하는 것이다.


<br>
그럼 이제 사용 방법에 대해 알아보자. 사실 큰 차이는 없다.

1. OneShot
이전 버전에 VibrationEffect를 생성하는 것까지 동일하고 이 VibrationEffect를 CombinedVibration.createParallel 함수를 통해 CombinedVibration를 생성하고 vibrate() 함수를 실행한다.

```kt
val vibrationEffect = VibrationEffect.createOneShot(100L, VibrationEffect.DEFAULT_AMPLITUDE)
val combinedVibration = CombinedVibration.createParallel(vibrationEffect)
vibrator.vibrate(combinedVibration)
```
<br>

2. WaveForm

```kt
val timings = longArrayOf(100, 200, 100, 200, 100, 200)
val amplitudes = intArrayOf(0, 50, 0, 100, 0, 200)
val vibrationEffect = VibrationEffect.createWaveform(timings, amplitudes, 0)

val combinedVibration = CombinedVibration.createParallel(vibrationEffect)
vibrator.vibrate(combinedVibration)
```
