# 🌱 UMC 7th FarmON BackEnd

## 🌾 Introduce
**농업의 연결 고리** **FarmON**은 농업의 혁신을 이끄는 디지털 솔루션으로, <br>
디지털 커뮤니티를 통한 공동 농업, 플랫폼을 활용한 전국의 전문가 연결, 농업 데이터를 기반으로 한 체계적인 농업 서비스를 제공합니다.

&nbsp;
## 🔧 Tech Stack
<p>
  <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> 
  <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white">
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/STOMP-6E4C13?style=for-the-badge&logo=apachekafka&logoColor=white">
  <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white">
</p>

<p>
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
  <img src="https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=nginx&logoColor=white">
  <img src="https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white">
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white">
</p>

&nbsp;
## ✨ Main Feature

### 🔹 농업인 & 전문가 등록
- 농업인 회원가입
- 전문가 등록을 통해 **전문 분야 및 컨설팅 가능 지역 설정**
- 농업인 - 전문가 역할 전환

### 🔹 전문가 - 농업인 매칭 시스템
- **전문가 매칭 시스템**
- **전문가 추천 기능**
- **전문가 프로필 열람 및 상세 정보 확인**
- **전문가 - 농업인 견적서 매칭 시스템**

### 🔹 전문가 프로필 등록
- 자유 형식의 **포트폴리오 등록 기능**
- 전문가의 **경력 관리**

### 🔹 농업 견적서
- **지역, 작물, 예산 기반 견적 등록 기능**
- 작물 카테고리별 견적 검색 및 필터링
- 상세 견적 확인

### 🔹 실시간 채팅
- **실시간 채팅**을 통한 신뢰 기반의 견적 거래 시스템 제공

### 🔹 작물 검색 최적화
- **견적 데이터**를 활용한 **작물 검색어 추천** 기능
- **작물 카테고리 기반 자동 완성 검색** 기능 제공

### 🔹 커뮤니티
- 인기 게시판
- 전체 게시판
- 전문가 칼럼
- Q&A
- 자유 게시판

&nbsp;
## 👩‍💻👨‍💻 Backend Developer

<table>
    <tr height="200px">
        <td align="center" width="200px">
            <a href="https://github.com/LEEYOENN">
                <img height="150px" width="150px" src="https://avatars.githubusercontent.com/LEEYOENN"/>
            </a>
            <br />
            <a href="https://github.com/LEEYOENN">데이/이연</a>
        </td>
        <td align="center" width="200px">
            <a href="https://github.com/hyunji0348">
                <img height="150px" width="150px" src="https://avatars.githubusercontent.com/hyunji0348"/>
            </a>
            <br />
            <a href="https://github.com/hyunji0348">로컬/김현지</a>
        </td>
        <td align="center" width="200px">
            <a href="https://github.com/mmije0ng">
                <img height="150px" width="150px" src="https://avatars.githubusercontent.com/mmije0ng"/>
            </a>
            <br />
            <a href="https://github.com/mmije0ng">엠제이/박미정</a>
        </td>
        <td align="center" width="200px">
            <a href="https://github.com/Hanjun2022">
                <img height="150px" width="150px" src="https://avatars.githubusercontent.com/Hanjun2022"/>
            </a>
            <br />
            <a href="https://github.com/Hanjun2022">준/전한준</a>
        </td>
    </tr>
    <tr>
      <td align="center">견적서 등록, 필터링 기반 견적 조회 등 견적 관련 기능 구현</td>
      <td align="center">유저&전문가 관련 기능 구현<br>로그인&회원가입<br>JWT 기반 인증&인가</td>
      <td align="center">채팅 및 검색 관련 기능 구현<br>CI/CD 구축</td>
      <td align="center">커뮤니티 관련 기능 구현<br> s3 이미지 업로드 구현</td>
    </tr>
</table>

&nbsp;
## 🗂 ERD
<img width="972" alt="Image" src="https://github.com/user-attachments/assets/f6805244-44b5-45b1-9e47-c47521d8d53a" />

&nbsp;
## 🛠 Backend Architecture
<p align="center">
  <img width="500" alt="image" src="https://github.com/user-attachments/assets/e02ac54f-30da-4500-b662-8679bb52f42a">
</p>

&nbsp;
## 🚀 git flow
- `main`
  - 프로젝트 최종 merge
  - 기본 프로젝트 세팅, 배포 가능한 브랜치, 항상 배포 가능한 상태를 유지
- `develop`
  - 데모데이 전까지 완성한 기능들을 계속해서 merge
  - 배포 가능한 브랜치, 항상 배포 가능한 상태를 유지
- `{type}/{description}`: 개발 브랜치
  - 예: `feat/login`, `fix/login-token`

&nbsp;
## 💻 Commit Message Convention

| Type                 | Description                                                  |
| -------------------- | ------------------------------------------------------------ |
| **Feat**             | 새로운 기능 추가                                             |
| **Fix**              | 버그 수정                                                    |
| **Docs**             | 문서 수정                                                    |
| **Style**            | 코드 formatting, 세미콜론 누락, 코드 자체의 변경이 없는 경우 |
| **Refactor**         | 코드 리팩토링                                                |
| **Test**             | 테스트 코드, 리팩토링 테스트 코드 추가                       |
| **Chore**            | 패키지 매니저 수정, 그 외 기타 수정 (예: .gitignore)         |
| **Design**           | CSS 등 사용자 UI 디자인 변경                                 |
| **Comment**          | 필요한 주석 추가 및 변경                                     |
| **Rename**           | 파일 또는 폴더 명을 수정하거나 옮기는 작업만인 경우          |
| **Remove**           | 파일을 삭제하는 작업만 수행한 경우                           |
| **!BREAKING CHANGE** | 커다란 API 변경의 경우                                       |
| **!HOTFIX**          | 급하게 치명적인 버그를 고쳐야 하는 경우                      |

ex. `Feat : 새로운 기능 추가`
