# 패션 가이드

🏛 USG 공유대학 스마트제조ICT학과 패션피플팀
💻 캡스톤 종합설계경진대회 출품작  
</br>

## 프로젝트 소개  

```bash
• 개인 의류 데이터를 기반으로 날씨별 패션 코디를 추천해주는 어플리케이션
• 사용자가 옷장을 관리하고, 개인 맞춤형 코디를 제공함으로써 패스트 패션 문제를 해결하고 지속 가능한 패션 문화를 조성하는 것을 목표
```

</br>
  
## 프로젝트 구조

![전체 구조](https://github.com/Fashion-People-GNU/FashionGuide/assets/98737142/13f39c1c-3c60-4196-89ef-f9e3fbf51916)


![프로젝트 시스템 구조도](https://github.com/Fashion-People-GNU/FashionGuide/assets/98737142/52e942e9-6416-4fd7-95b9-8e17a7ffdd78)

```bash
데이터 수집: 무신사 웹사이트에서 의류 데이터 크롤링.
데이터 전처리: 크롤링된 데이터를 정제하고 모델 학습을 위한 데이터셋 구축.
모델 학습: 의류 속성 판별 모델과 추천 모델 학습.
서버 구축: Flask와 Firebase를 사용하여 서버 및 데이터베이스 구축.
앱 개발: Kotlin과 Android SDK를 사용하여 사용자 친화적인 애플리케이션 개발.
```

</br>

## 주요 기능  
</br>
<div align="center">
  <img src="https://github.com/Fashion-People-GNU/FashionGuide/assets/98737142/8ae61251-97aa-4db9-929d-e81dac54f6a6" alt="Image 1" width="400"/>
  <img src="https://github.com/Fashion-People-GNU/FashionGuide/assets/98737142/07f76e2c-9a20-4797-a301-cf3851176c9d" alt="Image 2" width="400"/>
</div>
</br>
**1. 날씨 기반 옷 추천 기능**

  ```bash
• 전체 추천과 부분 추천을 받을 수 있습니다.
• 사용자가 선택한 옷 아이디를 서버로 전송하여 이미지 감지 학습 인공지능 모델을 실행합니다.
• 미리 분류된 카테고리와 속성을 바탕으로 옷을 판별합니다.
• 판별된 옷의 속성과 현재 날씨 정보를 기반으로 가장 적합한 옷을 추천합니다.
• 서버로부터 반환된 추천 결과를 사용자에게 알기 쉽게 제공합니다.
  ```

</br>
<div align="center">
  <img src="https://github.com/Fashion-People-GNU/FashionGuide/assets/98737142/beb8134d-0dfa-4a5b-a37e-7b031072cdbc" alt="Image 1" width="400"/>
  <img src="https://github.com/Fashion-People-GNU/FashionGuide/assets/98737142/8117709b-d6e7-4d2c-9b18-eefa8bc9c64a" alt="Image 2" width="400"/>
</div>
</br>
**2. 가상 옷장 관리 기능**  

  ```bash
• 사용자가 소유한 옷을 등록하고 관리할 수 있습니다.
• 옷 사진을 촬영하여 앱에 추가하고, 카테고리, 색상, 소재 등의 정보를 입력합니다.
• 옷장을 통해 등록된 옷을 조회, 수정, 삭제할 수 있습니다.
• 옷장에 저장된 옷을 바탕으로 추천받은 코디를 확인할 수 있습니다.
• 사용자가 옷장을 효율적으로 정리하고 관리할 수 있도록 도와줍니다.
  ```
    
</br>

## 사용 방법

```bash
• 앱을 실행하고 구글 로그인을 통해 로그인합니다.
• 옷장 화면에서 옷을 추가하거나 삭제할 수 있습니다.
• 홈 화면에서 날씨 정보를 확인하고, 전체 추천이나 부분 추천을 받을 수 있습니다.
• 설정 화면에서 위치 정보나 데이터 출처 등을 확인하고 로그아웃할 수 있습니다.
```

## 개발 환경

```bash
Android Studio Iguana 2023.2.1., Kotlin 1.9.0, Compose 1.6.6, Dagger Hilt 2.48, KSP, 
Restrofit 2.9.0, Google Play Services Location 21.2.0, Firebase 23.0.0, Firestore 25.0.0

안드로이드 : Kotlin, Android SDK, Jetpack Compose
서버: Python, Flask, Firebase Firestore
모델: YOLOv5, K-Prototypes
```

</br>
