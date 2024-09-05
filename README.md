# 1. 앱에 대한 설명

캘린더 애플리케이션은 사용자가 일정을 생성, 조회, 수정, 삭제할 수 있는 기능을 제공하며,
특정 이벤트가 시작되기 전에 알림을 이메일로 발송하는 기능을 갖추고 있습니다.<br>
이 애플리케이션은 반복 일정(일일, 주간, 월간)을 설정할 수 있으며,
각 일정에 대한 알림 시간을 설정하여 일정 시작 전에 미리 알림을 받도록 설계되었습니다. <br>
또한, Swagger UI를 사용하여 API 문서를 제공하며, OpenAPI 3.0 규격에 따라 문서화되었습니다.

### 주요 기능

	1.	일정 생성 (생성 시 알림 스케줄링 포함)
	•	사용자는 새로운 일정을 생성할 수 있으며, 반복 일정(매일, 매주, 매월)도 설정할 수 있습니다.
	•	일정 생성 시 알림 기능을 활성화하면 지정된 시간 전에 이메일 알림을 받을 수 있습니다.
	2.	일정 조회
	•	사용자는 모든 일정을 조회하거나 특정 ID에 해당하는 일정을 조회할 수 있습니다.
	3.	일정 수정
	•	사용자는 기존 일정을 수정할 수 있으며, 수정된 정보에 따라 새로운 알림 스케줄링이 가능하게 됩니다.
	4.	일정 삭제
	•	사용자는 생성된 일정을 삭제할 수 있으며, 삭제된 일정에 대한 알림 스케줄도 제거됩니다.
	5.	이메일 알림 기능
	•	사용자는 일정이 시작되기 전에 이메일로 알림을 받을 수 있습니다. 일정 생성 시 알림 설정과 알림 시간(얼마나 일찍 받을지)을 설정할 수 있습니다.
	•	이메일 알림은 JavaMailSender를 사용하여 SMTP 서버를 통해 발송됩니다.
	6.	반복 일정 생성
	•	매일, 매주, 매월 반복되는 일정을 설정할 수 있으며, 각각의 반복되는 일정에 대해 알림을 받을 수 있습니다.

# 2. 소스 빌드 및 실행 방법 메뉴얼(DB 스키마 포함)

	•	Java 17 이상
	•	Maven
	•	MySQL 데이터베이스 서버 설치 및 구동

### 2.1 MySQL 데이터베이스 설정
```
CREATE DATABASE calendar_app;
```

### 2.2 소스 빌드 및 실행 방법

#### 2.2.1 소스코드 다운로드 및 빌드 
프로젝트를 클론하거나 다운로드 후, 프로젝트의 루트 디렉터리로 이동합니다.
Maven을 사용하여 의존성을 설치하고 애플리케이션을 빌드합니다.
```
mvn clean install
```

#### 2.2.2 설정 파일 수정
src/main/resources/application.properties 파일을 열어 데이터베이스 설정 및 Swagger 설정을 수정합니다.
```
# DB Server Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/calendar_app
spring.datasource.username=user
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# JWT Configuration
springdoc.api-docs.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true

# Email Configuration(임의 작성 상태 실제 사용시 SMTP 서버 정보를 입력해야 합니다.)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=user@gmail.com
spring.mail.password=your-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000
```

#### 2.2.3 애플리케이션 실행
다음 명령어를 실행하여 애플리케이션을 시작합니다.
```
mvn spring-boot:run
```
애플리케이션 실행 확인은 브라우저에서 URL : localhost:8080 로 이동하여 화면에 Calendar App 텍스트가 확인된다면 애플리케이션 정상 실행중 입니다.

#### 2.2.4 Swagger UI 확인
애플리케이션이 실행되면, 브라우저에서 Swagger UI로 이동하여 API를 테스트할 수 있습니다.
URL : http://localhost:8080/swagger-ui.html


#### 2.2.5 DB 스키마
```
CREATE TABLE `calendar` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `description` varchar(255),
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `is_recurring` bit NOT NULL,
  `recurrence_type` varchar(255),
  `recurrence_interval` int,
  `notify_before_event` bit NOT NULL,
  `notification_time` bigint,
  PRIMARY KEY (`id`)
);
```

# 3. 주력으로 사용한 컴포넌트에 대한 설명 및 사용 이유

1️⃣ Spring Boot

Spring Boot는 애플리케이션을 빠르고 쉽게 설정할 수 있도록 해줍니다. 내장 서버 제공, 자동 설정, 의존성 관리와 같은 기능 덕분에 REST API를 구축하는 데 있어 매우 적합한 선택입니다.

2️⃣ Spring Data JPA

Spring Data JPA는 데이터베이스 접근을 위한 간단하고 일관된 추상화를 제공합니다. 이 애플리케이션에서는 MySQL 데이터베이스와 상호작용하기 위해 사용했습니다. JPA의 CRUD 메서드 제공, 관계 매핑 등 다양한 기능을 통해 코드의 양을 크게 줄였습니다.

3️⃣ MySQL

애플리케이션의 데이터를 안정적으로 저장하고 관리하기 위해 관계형 데이터베이스인 MySQL을 선택했습니다. 데이터 정합성 유지와 복잡한 관계형 데이터 관리를 쉽게 처리할 수 있기 때문에 선택했습니다.

4️⃣ Swagger

API 문서화를 위해 Swagger를 사용했습니다. Swagger는 RESTful API의 명세를 자동으로 생성하고, 이를 통해 쉽게 API를 테스트할 수 있는 UI도 제공합니다. 이를 통해 개발자는 자신이 작성한 API를 보다 직관적으로 이해하고 사용할 수 있습니다.

5️⃣ JavaMailSender

사용자가 설정한 알림을 이메일로 전송하기 위해 JavaMailSender를 사용하였습니다.

6️⃣ Scheduler (Spring @Scheduled)

일정 시작 전에 알림을 자동으로 확인하고 전송하기 위해 1분 간격으로 스케줄러를 사용하였습니다.

# 4. API 명세(Swagger 사용)

Swagger UI는 애플리케이션이 실행될 때 자동으로 생성됩니다. 모든 API 엔드포인트는 http://localhost:8080/swagger-ui.html에서 확인할 수 있습니다. 아래는 주요 API와 설명입니다.

1️⃣ POST /api/calendars: 새로운 일정 생성<br>
Request Body (JSON)
```
{
    "title": "회의", // 일정 이름
    "description": "주간 회의", // 일정 내용
    "startTime": "2024-09-06T10:00:00", // 일정 시작 시간
    "endTime": "2024-09-06T11:00:00", // 일정 종료 시간
    "isRecurring": false, // 반복여부
    "recurrenceType": null, // 반복형태
    "recurrenceInterval": 0, // 반복 간격 (Daily, Weekly, Monthly)
    "notifyBeforeEvent": true, // 알림 여부
    "notificationTime": "PT30M"  // 30분 전 알림
}
```

2️⃣ GET /api/calendars : 모든 일정 조회<br>
Response 
```
[
    {
        "id": 1,
        "title": "회의",
        "description": "주간 회의",
        "startTime": "2024-09-06T10:00:00",
        "endTime": "2024-09-06T11:00:00",
        "isRecurring": false,
        "recurrenceType": null,
        "recurrenceInterval": 0,
        "notifyBeforeEvent": true,
        "notificationTime": "PT30M"
    }
]
```

3️⃣ GET /api/calendars/{id} : 특정 일정 조회<br>
Request Parameters <br>
id : 일정 ID

4️⃣ PUT /api/calendars/{id} : 일정 수정 <br>
Request Body (JSON)<br>
일정 생성 시와 동일

5️⃣ DELETE /api/calendars/{id} : 일정 삭제<br>

