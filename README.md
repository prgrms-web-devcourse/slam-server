# slam-server

![image](https://user-images.githubusercontent.com/88185304/147046643-fd5c660e-1040-491c-87a9-2bf0d76e2495.png)


## ✏️ 프로젝트 설명 
<I>같이 농구할 사람이 없다고? 나랑 슬램하러 가자</I> <br><br>
길거리 농구를 하러 나가고 싶어도 현재 가려는 농구장에 사람이 몇명인지 알 수 없다는 기존 문제점을 해결하기 위해 ‘슬램’ 프로젝트를 시작했습니다. <br>
지도뷰에서 가려는 농구장을 찾아 원하는 시간대에 예약을 하고, 예약한 시간대에 본인말고 몇명이 더 예약을 했는지 현황을 알 수 있습니다. 여기서 만난 사람들과 팔로우를 걸며 친목을 다질 수 있고 프로필에서 본인의 간단한 정보(숙련도, 선호 포지션 등)를 기입해 본인을 나타낼 수 있습니다. 다가오는 시합에 사람이 6명이상 모이지 않는다면 확성기버튼을 눌러 다른사람들에게 알림을 보내 홍보를 할 수 도 있습니다.<br>
<br> 그럼 우리 다같이 슬램하러 가볼까요? 


<br>

## 👨‍👨‍👧‍👦 팀소개

|![image 31](https://user-images.githubusercontent.com/88185304/147052066-7c229d7a-1c5d-4c73-9e62-5098d14935b6.png)|![yunyun 1 (1)](https://user-images.githubusercontent.com/88185304/147052427-54500fe5-ff22-4617-9751-bbef433fe828.png)|![Frame 42 (1)](https://user-images.githubusercontent.com/88185304/147052306-172673e9-8f09-428b-9de8-d50f8cdb6324.png)|
|:---|:---|:---|
|Hey(서동성)|Flora(류윤정)|Jely(권예경)|
|- 백앤드 팀장 <br> - 예약도메인 <br> - 즐겨찾기 도메인 <br> - 농구장도메인|- 공지 도메인 <br> - 채팅도메인 <br> - CICD 구축 <br> - SSL 적용|- 카카오로그인 <br> - 스프링스큐리티 <br> - 유저 및 팔로우 도메인 <br> - 관리자 도메인|


<br>

## ⚒ 개발환경

### 어플리케이션
- Java 11
- Spring Framework(Spring MVC) 2.6.1
- JPA/Hibernate
- Maven

### 아키텍처
![image](https://user-images.githubusercontent.com/88185304/147044361-b98c14b3-5597-439d-8170-abbaa3041e14.png)
- MySql
- Jenkins & Gibhub Webhook

### 협업방식
- Notion
- Discord
- Slack
- Jira


<br>

## 📊 다이어그램
### 시퀀스 다이어그램

- 카카오 로그인 시퀀스 다이어그램

![image](https://user-images.githubusercontent.com/88185304/147052616-44ece9a4-d83f-4d40-93d6-6e8224815662.png)

- 도메인 시퀀스 다이어그램 (RestAPI & WebSocket)

![image](https://user-images.githubusercontent.com/88185304/147050928-bb43ac86-5d18-45af-ac13-8e2608cdedb4.png)

### ERD

![image](https://user-images.githubusercontent.com/88185304/147046038-3ab6cd1e-a468-4cba-a27b-c6b185a1af25.png)

<br>

## 🔖 API 명세서
[API 명세서](https://www.notion.so/backend-devcourse/API-d742f25552b448fb870d80d7afcc6913)

<br>

## 🔗 Client Git Repo
[Slam-cliet](https://github.com/prgrms-web-devcourse/slam-client)
