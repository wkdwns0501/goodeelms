![Java](https://img.shields.io/badge/Java-11-blue)
![JSP](https://img.shields.io/badge/JSP-Servlet-orange)
![JavaScript](https://img.shields.io/badge/JavaScript-ES6-yellow)
![HTML](https://img.shields.io/badge/HTML5-markup-red)
![CSS](https://img.shields.io/badge/CSS3-style-blue)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5-purple)

![Ajax](https://img.shields.io/badge/Ajax-Async-green)
![MySQL](https://img.shields.io/badge/DB-MySQL-blue)
![Tomcat](https://img.shields.io/badge/Server-Tomcat%209-red)
![MVC](https://img.shields.io/badge/Pattern-MVC-brightgreen)
![License](https://img.shields.io/badge/License-MIT-brightgreen)
![Team](https://img.shields.io/badge/Team-4%20Members-purple)

# 📚 GoodeeLMS Project – 학사 관리 시스템 (Academic Management System)

수강 신청, 강의 관리, 성적 관리, 게시판 등 대학 학사 운영을 위한 웹 플랫폼 프로젝트입니다.<br>
설계/ERD/요구정의서 등 산출물 완비 및 GitHub 브랜치 전략을 기반으로 한 **실무형 협업 프로젝트**입니다.

🎬 [프로젝트 시연 영상 보러가기](유튜브링크) | 📁 [GitHub 저장소 바로가기](https://github.com/wkdwns0501/goodeelms.git)

---

## 🧩 프로젝트 개요

| 항목 | 내용 |
| --- | --- |
| **프로젝트명** | GoodeeLMS Project |
| **개발 기간** | 2026.01.12 ~ 2026.01.30 (19일간) |
| **팀 구성** | 윤재훈, 임 욱, 장 준, 최준희 (4인 프로젝트) |
| **개발 도구** | Eclipse, Apache Tomcat 9.x, MySQL 8.x |
| **기술 스택** | Java, JSP, Servlet, HTML/CSS/JS, JSTL |
| **주요 기능** | 회원가입(학생/교수), 수강신청, 장바구니, 강의/성적 관리, 게시판, 관리자 기능 등 |
| **산출물** | 💡 ERD, 화면설계서, 요구정의서, PPT 포함 모든 산출물 완비 |

<br>

## 🧠 역할 분담 및 기여도

| 이름 | 주요 기능 구현 | 비고 |
| :---: | --- | --- |
| **윤재훈** | **[수강/일정]** 수강 신청, 장바구니, 학사 일정 스케줄러, 검색 필터 구현 | 수강 흐름 로직 담당 |
| **임 욱** | **[회원/재정]** 로그인, 회원가입, 학사 경고, 우등/장학/등록금 조회, 학과 정보 | 인증·학적 관리 |
| **장 준** | **[강의/성적]** 마이페이지, 강의 목록, 성적 이력 조회(학생), 강의/성적 관리(교수), ERD | 학생, 교수 기능 |
| **최준희** | **[관리/게시판]** 게시판, 학적/장학/교원 관리, 계정 등록, 강의 평가 | 관리자 영역 |
| **팀 전체** | 요구사항 정의서, 테이블 정의서, WBS 등 모든 산출물 공동 작성 | 협업 |

<br>

## 🧰 프로젝트 폴더 구조

```text
goodeelms/
├── docs/                     # 설계서, ERD, 요구정의서, PPT 등 산출물 폴더
└── src/
    └── main/
        ├── java/
        │   ├── controller/   # 요청 처리 Controller
        │   ├── dao/          # DB 데이터 접근 객체
        │   ├── dto/          # 데이터 전송 객체
        │   ├── filter/       # 권한 및 인코딩 필터
        │   ├── listener/     # 웹 애플리케이션 리스너
        │   ├── scheduler/    # 학사 일정 자동화 스케줄러
        │   ├── service/      # 비즈니스 로직 처리
        │   └── util/         # 공통 유틸리티
        └── webapp/
            ├── resources/    # CSS, JS, Images
            └── WEB-INF/
                ├── lib/      # 라이브러리
                └── views/    # JSP 화면
```

🖼️ 화면 설계서 (UI 설계 기반)👉 📂 화면 설계서 PDF 보기 (docs 폴더)위 링크를 클릭하면 GitHub 내 docs/ 폴더로 이동하여 상세 설계서를 확인할 수 있습니다.주요 화면기능 설명로그인/메인사용자 유형(학생/교수/관리자)별 대시보드 및 메뉴 분기수강 신청강의 목록 조회, 필터링, 장바구니 담기 및 신청 처리강의/성적실(교수) 강의 개설 및 성적 입력 / (학생) 강의 평가 및 성적 조회게시판공지사항, 자유게시판 글쓰기/수정/삭제 및 조회관리자 페이지학적 변동 승인, 장학금 지급 관리, 교원 및 계정 관리🗃️ 데이터베이스 설계 (ERD + 정의서)👉 📂 ERD 및 DB 정의서 보기 (docs 폴더)정규화된 16개의 테이블을 기반으로 데이터 무결성을 고려한 실무형 ERD 작성주요 테이블: 학생, 교수, 강의, 학과, 수강 내역, 장바구니, 학사일정, 게시판, 첨부파일 등🚀 주요 기능 요약영역기능 상세🔐 인증/계정로그인, 회원가입, 비밀번호 변경, 마이페이지(정보 수정)🎓 학사 관리학적 사항 변경(휴/복학), 등록금 납부 확인, 장학 이력 조회📅 수강/일정학사 일정 조회(스케줄러), 강의 검색 필터, 장바구니, 수강 신청📊 성적/평가(학생) 성적 이력 조회, 강의 평가 / (교수) 성적 등록 및 수정🏫 교수 기능강의 등록 및 조회, 수강생 출석부 조회, 성적 산출⚙️ 관리자사용자 계정 생성, 학사 일정 등록, 게시판 관리, 교원 변동 관리🎬 프로젝트 발표자료👉 📂 발표용 PPT 및 산출물 전체 보기 (docs 폴더)발표 순서 및 구성:개요: 프로젝트 목표 및 팀 소개요구사항 정의: 사용자별 기능 명세ERD: 데이터베이스 구조 설계기능 스택: 사용 기술 및 환경WBS: 일정 관리 및 진행표협업 환경: Git 브랜치 전략 및 버전 관리주요 기능: 핵심 로직 설명시연: 프로젝트 시연 동영상에필로그: 프로젝트 회고🧪 테스트 및 협업 전략항목내용테스트 플로우추후 업데이트 예정 (테스트 시나리오 확정 후 기재)GitHub 전략Gitflow 전략 차용 (main + develop + feature 브랜치 운영)📄 라이선스MIT License본 프로젝트는 자유롭게 사용/수정/배포 가능합니다.단, 반드시 저작자 표기 및 GitHub 출처 링크 명시가 필요합니다.🔗 산출물 링크 모음항목링크📁 GitHub 저장소https://github.com/wkdwns0501/goodeelms.git🎥 시연 영상YouTube 영상 보러가기📂 전체 산출물docs/ 폴더 바로가기
