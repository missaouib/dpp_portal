# Data Portal Platform
---
DPP Web은 Web Portal 통합서비스입니다.
AWS Web Console 를 쉽게 다를 수 있습니다.
Dpp Web은 [Spring Boot](https://spring.io/guides/gs/spring-boot) 기반 [Gradle](https://spring.io/guides/gs/gradle) 빌드를 사용했습니다.

* Spring Boot
* Spring Lombok
* AWS SDK

## 솔루션 주요 기능
---
* EC2 인스턴스 제어
* S3 목록
* Meta 조회

## 폴더 구조 및 설명


```bash

             
├── src/java/mzc/data/portal/       - Java Backend
│  ├── agent/                       - AWS SDK cofig,dto,service
│  ├── config/                      - Web 공통 설정
│  ├── controller/                  - Url 맵핑 및 제어
│  ├── dto/                         - database Data
│  ├── enums/                       - Data Type Enums
│  ├── exception/                   - exception 공통 설정
│  ├── mapper/                      - Mybatis SQL Query Class 
│  └── service/                     - database 가공 및 처리
├── src/resources/                  - Frontend
│  ├── mapper/                      - Mybatis SQL Query Xml File 
│  ├── static/                      - assets, js, css, plugin 
│  ├── templeates/                  - html 코드 
└── application-xxx.yaml            - spring boot apllication 설정 파일 (배포에따라 설정)
```



---
# License

- Spring Boot is Open Source software released under the Apache 2.0 license.
- Lombok is Open Source software released under the MIT license.
- AWS SDK is distributed under the Apache License, Version 2.0.