# Webkit640_backend Refactoring V1
<p align="center">
<img src="https://user-images.githubusercontent.com/40915234/234068224-7de4c9d2-2795-4b5e-955b-b7425daf822b.png" width="350" height="350"/>
</p>

<p align="center">
<a href="https://hits.seeyoufarm.com"><img src="https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2FSongMinGyu0506%2FWebkit640_back_refactoring&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false"/></a>
</p>

---
# 프로젝트 소개
금오공과대학교 Webkit640 교육 프로그램 홍보 및 교육신청 웹사이트 프로젝트

## 개발기간
* 2023.04.24 ~ 2023.05.28

## 개발환경
<img src="https://img.shields.io/badge/Springboot-6DB33F?style=for-the-badge&logo=Springboot&logoColor=white"><img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white">
<img src="https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=java&logoColor=white"><img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white"><img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=for-the-badge&logo=IntelliJ IDEA&logoColor=white">
<img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white"><img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white">
* Java 16
* Springboot 2.7.4
* MySQL 8.0.33 (Docker latest version)
* github
* Docker
* IntelliJ IDEA 2021
* Gradle

## 개발 진행 기록
https://songmingyu0506.notion.site/Webkit640-Project-d0346a97bea7478da25d393774acfec6

## 리팩토링 내용
### 코드구조 개선
* Spring Aspect를 이용하여 공통 코드 최대한 통합
* Restcontroller Advice를 이용하여 발생하는 예외사항 대응
* MVC 패턴 준수 및 기존 코드에 노출되어있는 서비스 로직 분리
* 다양한 Http Status를 이용하여 status 코드로 상태 구분
* 느슨한 연결 구조를 위해 서비스로직을 인터페이스로 분리

### 문서화 방법 변경
* 기존 프로젝트에서 Postman을 이용한 문서화에서 Swagger를 이용하여 문서화 수행
* 그 외 프로젝트 진행사항은 Notion, Github Project를 이용
* 프로젝트 개발 필요사항을 기록하기 위해 Issue 활용

### TDD 적용
* 테스트코드 수행 위주로 개발 진행

### 배포 방법 변경
* 기존 방법에서 사용하던 파일 전송 대신 Docker를 활용

## 프로젝트 실행 및 빌드 방법
* 설치되어있는 IDE에 Clone하여 수행
* 해당 Repository에는 프로젝트 설정파일인 **application.yml** 또는 **application.properties**가 보안상 포함되어있지 않습니다.
* 빌드는 Gradle을 이용하여 수행, 배포 목적으로는 "build -x test" 권장
