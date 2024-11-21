# C-LASSIK (클라식)

## 📌 프로젝트 소개

<img width="500" src="./readme/gifs/splash.gif">

C-LASSIK은 공감각적 클래식 음악 감상을 위한 VR 음악 스트리밍 서비스입니다.

청각, 시각, 촉각을 사용하여 비장애인뿐만 아니라 청각장애인도 음악을 즐길 수 있도록 유니버셜 컨텐츠를 제공합니다.

[🎞 UCC 보러가기 →](https://www.youtube.com/watch?v=NCk-aZTwF6Y)

[📲 Apk 파일 다운받기 →](https://drive.google.com/file/d/1gL74tvTDfhlvfF8H5q-r5zsSZZ4ZwIL1/view?usp=sharing)

## 💻 프로젝트 주요 기능

### 💯 음악을 100% 즐기기

클래식 음악은 다른 장르에 비해 음악을 듣는 동시에 정보를 즐기기가 어렵습니다. 클라식은 클래식 음악의 세부 정보를 함께 제공하여 감상을 더욱 풍부하게 합니다.

<img width="500" src="./readme/gifs/playmusic.gif">

### 👀 음악을 시각으로 즐기기

Skybox AI를 이용해 해당 클래식 음악에 맞는 정보로 VR 이미지를 생성하여 제공합니다.

<img width="500" src="./readme/gifs/vropen.gif">
<img width="500" src="./readme/gifs/vrwatch.gif">

### 👋 음악을 촉각으로 즐기기

librosa 라이브러리를 사용하여 음악의 frequency를 수치화하고 셈여림 데이터를 haptic으로 표현하여 제공합니다.

### 🧡 음악을 취향대로 즐기기

사용자가 재생한 시청 기록 기반으로 취향에 맞는 추천 플레이리스트를 제공합니다.

<img width="500" src="./readme/gifs/recommend.gif">

### 😎 음악을 원하는대로 즐기기

사용자가 생성한 플레이리스트에 원하는 음악을 저장하고 해당 플레이리스트에 저장된 곡만 즐길 수 있도록 커스텀 플레이리스트를 제공합니다.

<img width="500" src="./readme/gifs/create_playlist.gif">
<img width="500" src="./readme/gifs/save_playlist.gif">
<img width="500" src="./readme/gifs/mypage_playlist.gif">

## 🎼 주요 기술 소개

### 🔎 검색

Track 데이터를 벡터화하여 ChromaDB에 저장하고, 검색어를 벡터화해 유사 키워드 상위 20개를 추출할 수 있도록 설계했습니다. 또한, GPT-4o 모델을 통해 검색어의 오탈자를 보정하여 최적화된 검색 결과를 제공합니다.

<img width="500" src="./readme/search.PNG">

<img width="500" src="./readme/gifs/search_betho.gif">
<img width="500" src="./readme/gifs/search_moz.gif">

### 👁‍🗨 음악 시각화

음악 제목을 기반으로 OpenAI 모델에서 생성된 프롬프트를 활용해 Skybox API로 파노라마 이미지를 생성하고, 이를 S3에 저장한 뒤 관련 메타데이터를 데이터베이스에 연동합니다. OpenAI, 이미지 생성 기술, 클라우드 스토리지를 통합해 효율적인 데이터 관리와 이미지 제공을 구현했습니다.

<img width="500" src="./readme/createimage.png">

#### 파노라마 asset 결과

<img width="500" src="./readme/example.jpg">

### 🎯 추천 플레이리스트

사용자의 Track 재생 기록 데이터를 기반으로 10개의 플레이리스트를 추천합니다. 만약 데이터가 부족하다면 무작위 작곡가에 대한 플레이 리스트를 반환합니다.

#### 추천 기능 동작 과정

1. Redis에 저장된 사용자의 재생 기록 Data를 가져온다.
2. Track 작곡가 & Tag 정보와 HashMap을 활용해 순위를 구한다.
3. 상위 10개에 대해 알맞은 Track List를 반환한다.

## 🛠 기술 스택

### 🖥️ Client

|                 |                                                                                                                                                                                                                                     |
| :-------------- | :---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Framework       | <img src="https://img.shields.io/badge/jetpackcompose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white">                                                                                                              |
| Language        | <img src="https://img.shields.io/badge/kotlin-7F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white"/> <img src="https://img.shields.io/badge/c%23-A179DC.svg?style=for-the-badge&logo=c&logoColor=white"/>                    |
| Version Control | <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"/> <img src="https://img.shields.io/badge/gitLAB-fc6d26?style=for-the-badge&logo=gitlab&logoColor=white"/>                           |
| IDE             | <img src="https://img.shields.io/badge/androidstudio-3DDC84.svg?style=for-the-badge&logo=androidstudio&logoColor=white"/> <img src="https://img.shields.io/badge/unity-FFFFFF.svg?style=for-the-badge&logo=unity&logoColor=black"/> |

### 🖥️ Server

|                 |                                                                                                                                                                                                                                                                                                                                                            |
| :-------------: | :--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
|    Framework    | <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white"/> <img src="https://img.shields.io/badge/fastapi-009688?style=for-the-badge&logo=fastapi&logoColor=white"/>                                                                                                                                  |
|    Language     | <img src="https://img.shields.io/badge/kotlin-7F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white"/> <img src="https://img.shields.io/badge/python-3776AB.svg?style=for-the-badge&logo=python&logoColor=white"/> <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white"/>                      |
|    Database     | <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MariaDB"/> <img src="https://img.shields.io/badge/redis-FF4438?style=for-the-badge&logo=redis&logoColor=white" alt="Redis"/> <img src="https://img.shields.io/badge/chroma-CC6A73?style=for-the-badge&logo=chroma&logoColor=white" alt="chroma"/> |
|      Cloud      | <img src="https://img.shields.io/badge/Amazon%20EC2-FF9900?style=for-the-badge&logo=Amazon%20EC2&logoColor=white"> <img src="https://img.shields.io/badge/Amazon%20S3-569A31?style=for-the-badge&logo=Amazon%20S3&logoColor=white">                                                                                                                        |
|     DevOps      | <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white" alt="Docker"> <img src="https://img.shields.io/badge/jenkins-D24939?style=for-the-badge&logo=jenkins&logoColor=white" alt="jenkins">                                                                                                                 |
|       AI        | <img src="https://img.shields.io/badge/openai-412991.svg?style=for-the-badge&logo=openai&logoColor=white"/>                                                                                                                                                                                                                                                |
|    Crawling     | <img src="https://img.shields.io/badge/selenium-43B02A?style=for-the-badge&logo=selenium&logoColor=white"/>                                                                                                                                                                                                                                                |
| Version Control | <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"/> <img src="https://img.shields.io/badge/gitLAB-fc6d26?style=for-the-badge&logo=gitlab&logoColor=white"/>                                                                                                                                                  |
|       IDE       | <img src="https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white"/>                                                                                                                                                                                                                                   |

### 🖥️ Common

|               |                                                                                                                                                                                                                                                                                                                                                         |
| :------------ | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Collaboration | <img src="https://img.shields.io/badge/jira-0052CC?style=for-the-badge&logo=jira&logoColor=white" alt="Notion"/> <img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white" alt="Notion"/> <img src="https://img.shields.io/badge/figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white" alt="Figma"/> |

## 👩‍👩‍👦‍👦 우리 팀 소개

<table>
  <tr>
    <th colspan="2" style="text-align: center;">Frontend</th>
    <th colspan="3" style="text-align: center;">Backend</th>
    <th colspan="1" style="text-align: center;">Infra</th>
  </tr>
  <tr>
  <td style="text-align: center;">팀장</td>
  <td></td>
  <td style="text-align: center;">BE 팀장</td>
  <td></td>
  <td style="text-align: center;">AI</td>
  <td></td>
  </tr>
  <tr>
    <td align="center"><a href="https://github.com/mkkim68"><img src="https://avatars.githubusercontent.com/mkkim68" width="130px;" alt=""><br /><sub><b>김민경</b></sub></a></td>
    <td align="center"><a href="https://github.com/Junyoung-Park-jyp"><img src="https://avatars.githubusercontent.com/Junyoung-Park-jyp" width="130px;" alt=""><br /><sub><b>박준영</b></sub></a></td>
    <td align="center"><a href="https://github.com/ParkDH0809"><img src="https://avatars.githubusercontent.com/ParkDH0809" width="130px;" alt=""><br /><sub><b>박동환</b></sub></a></td>
    <td align="center"><a href="https://github.com/ttaeram"><img src="https://avatars.githubusercontent.com/ttaeram" width="130px;" alt=""><br /><sub><b>유태람</b></sub></a></td>
    <td align="center"><a href="https://github.com/songminj"><img src="https://avatars.githubusercontent.com/songminj" width="130px;" alt=""><br /><sub><b>송민정</b></sub></a></td>
    <td align="center"><a href="https://github.com/SungjuYoo530"><img src="https://avatars.githubusercontent.com/SungjuYoo530" width="120px;" alt=""><br /><sub><b>유성주</b></sub></a></td>
  </tr>
</table>

## 📚 산출물

|              |                                                            |
| :----------: | ---------------------------------------------------------: |
| Architecture | <img width="700" src="./readme/C-LASSIK_Architecture.png"> |
|     ERD      |                   <img width="700" src="./readme/erd.PNG"> |
