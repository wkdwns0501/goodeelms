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

📚 GoodeeLMS Project – 학사 관리 시스템 (LMS)

수강 신청, 강의 관리, 성적 관리, 게시판 등 대학 학사 업무 전반을 웹으로 구현한 LMS(학사 관리 시스템) 입니다.
실제 대학 행정 흐름을 기반으로 학생·교수·관리자 권한을 분리하여 설계하였으며,
요구사항 정의 → ERD → UI 설계 → 구현 → 발표까지 실무형 협업 프로세스로 진행한 프로젝트입니다.

설계/ERD/요구정의서 등 모든 산출물 완비
GitHub 브랜치 전략 기반 협업 프로젝트

🎬 시연 영상 : (추후 YouTube 링크 삽입 예정)
📁 GitHub 저장소 : https://github.com/wkdwns0501/goodeelms.git

🧩 프로젝트 개요
항목	내용
프로젝트명	GoodeeLMS Project
개발 기간	2026.01.12 ~ 2026.01.30
팀 구성	윤재훈, 임 욱, 장 준, 최준희 (4인 협업 프로젝트)
개발 도구	Eclipse / Apache Tomcat 9.x / MySQL 8.x
기술 스택	JSP, Servlet, JSTL, JDBC, MVC Pattern
주요 기능	회원가입(학생·교수), 수강신청, 장바구니, 강의 관리, 성적 관리, 게시판, 관리자 기능
산출물	ERD, 화면설계서, 요구정의서, 테이블 정의서, 발표 PPT
🧠 역할 분담 및 기여도
이름	주요 기능	비고
윤재훈	수강 신청, 장바구니, 학사 일정 스케줄러, 필터 기능	수강 흐름 로직 담당
임 욱	로그인, 회원가입, 학사 경고, 우등 이력 조회, 등록금 납입, 장학 정보 조회, 학과 정보, 학적 변경 이력, 현 학기 강의 조회	인증·학적 관리
장 준	마이페이지 정보 조회/수정(학생), 비밀번호 변경, 강의 목록 조회(학생), 성적 이력 조회, 강의 등록/조회(교수), 성적 등록/조회(교수), ERD 설계	학생·교수 핵심 기능
최준희	게시판 등록/조회, 학적 관리, 장학 관리, 교원 변동, 계정 등록, 학사 일정 등록, 강의 평가	관리자 영역
팀원 전체	요구사항 정의서, 테이블 정의서 등 전체 산출물 작성	협업 문서화
🧰 프로젝트 폴더 구조
goodeelms/
├── docs/                 # ERD, 화면설계서, 요구정의서, PPT 등 산출물
├── src/
│   └── main/
│       ├── java/
│       │   ├── controller/
│       │   ├── dao/
│       │   ├── dto/
│       │   ├── filter/
│       │   ├── listener/
│       │   ├── scheduler/
│       │   ├── service/
│       │   └── util/
│       └── webapp/
│           ├── resources/
│           └── WEB-INF/
│               ├── lib/
│               └── views/

🛠️ Tech Stack
구분	기술	사용 목적
Backend	Java, JSP, Servlet, JSTL, JDBC	MVC 기반 서버 로직, 세션 및 권한 처리
Frontend	HTML5, CSS3, JavaScript(ES6), Bootstrap 5	UI 구성 및 반응형 화면 구현
Async	Ajax	비동기 데이터 처리 (수강신청, 필터 등)
Database	MySQL 8.x	학사 데이터 저장 및 조회
Server	Apache Tomcat 9.x	웹 애플리케이션 실행 환경
Collaboration	GitHub (main/develop/feature)	브랜치 전략 기반 협업
Docs	ERD, 요구사항 정의서, 테이블 정의서, 화면설계서, 발표자료	설계 및 문서화
🖼️ 화면 설계서 (UI 설계 기반)

화면설계서 PDF
https://github.com/wkdwns0501/goodeelms/tree/main/docs

학생: 마이페이지, 강의 목록, 수강 신청, 성적 조회

교수: 강의 등록/조회, 수강생 성적 관리

관리자: 학적/장학/계정/학사 일정 관리

🗃️ 데이터베이스 설계 (ERD + 정의서)

ERD 이미지 / 요구사항 정의서 / 테이블 정의서
https://github.com/wkdwns0501/goodeelms/tree/main/docs

정규화된 16개 테이블 기반 ERD 설계

학생, 교수, 강의, 학과, 수강 장바구니, 학사 일정, 게시판 등 포함

🚀 주요 기능 요약
영역	기능
인증	학생/교수 회원가입, 로그인
수강	강의 조회, 수강 신청, 장바구니
강의	강의 등록 및 조회 (교수)
성적	성적 등록/조회 (교수), 성적 이력 조회 (학생)
학적	학적 변경, 학사 경고, 우등 이력
학사 일정	학사 일정 등록 및 자동 반영
게시판	게시글 등록 및 조회
관리자	학적/장학/계정/교원 관리
🎬 프로젝트 발표자료

발표용 PDF
https://github.com/wkdwns0501/goodeelms/tree/main/docs

발표 구성

프로젝트 개요

요구사항 정의서

ERD

기술 스택

WBS

버전 관리 및 협업 환경

주요 기능

프로젝트 시연 영상

에필로그

🧪 테스트 및 협업 전략
항목	내용
테스트 플로우	(추후 테스트 시나리오 반영 예정)
GitHub 전략	main + develop + feature 브랜치 운영
📄 라이선스

MIT License

본 프로젝트는 자유롭게 사용, 수정, 배포 가능합니다.
단, 반드시 저작자 표기 및 GitHub 출처 링크 명시가 필요합니다.

🔗 산출물
항목	링크
GitHub 저장소	https://github.com/wkdwns0501/goodeelms.git

시연 영상	(추후 YouTube 링크 삽입 예정)
전체 산출물	https://github.com/wkdwns0501/goodeelms/tree/main/docs

시연 영상	(추후 YouTube 링크 삽입 예정)
전체 산출물 보기	https://github.com/wkdwns0501/goodeelms/tree/main/docs
GitHub 저장소	https://github.com/wkdwns0501/goodeelms.git
시연 영상	(추후 YouTube 링크 삽입 예정)
전체 산출물 보기	https://github.com/wkdwns0501/goodeelms/tree/main/docs
