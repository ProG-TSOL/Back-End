# 개발 환경
java : 17
spring boot : 3.2.2
redis : 3.0.504
kafka : lastst
react : 28.2.0
zustand : 4.5.0
react-query : 5.17.19
tailwindcss : 3.4.1
yarn : 1.2.21
typeScript : 5.3.3
vite : 5.0.8
nodejs : lastst

# git ignore

.env
```
VITE_API_URL=https://localhost:8080
```

application.yml
```
sever:
  port: 3306

spring:
  config:
    import:
      - application-datasource.yml
      - application-s3.yml
      - application-jwt.yml
      - application-email.yml
      - application-oauth.yml
```

application-datasource.yml
```
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/prog?serverTimezone=UTC&characterEncoding=UTF-8
    username: root
    password: ssafy
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        format_sql: true
  data:
    redis:
      port: 6379
      host: localhost

logging:
  level:
    org:
      springframework:
        security: DEBUG
```

application-s3.yml
```
cloud:
  aws:
    credentials:
      access-key: AKIAQ3EGVVAXRQUNJAJ4
      secret-key: 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

    s3:
      bucket: prog-bucket-ssafy
    region:
      static: ap-northeast-2
    stack:
      auto: false
```

application-jwt.yml
```
jwt:
  secretKey: NOo5YIVm5yDDSublQMW3M0ZAMnwDSRdXMVkE58Uwfjhk411aJ9o6h2AL1tE8gkfX

  access:
    expiration: 1800
    header: accessToken

  refresh:
    expiration: 1209600 #  (1000L(ms -> s) * 60L(s -> m) * 60L(m -> h) * 24L(h -> 하루) * 14(2주))
    header: refreshToken
  authorization:
    header: Authorization
    prefix: Bearer
```

application-email.yml
```
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: csj9912@gmail.com
    password: zanrqnoamcxiacwr
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
          connectiontimeout: 5000
          timeout: 5000
          writetimeout: 5000
    auth-code-expiration-millis: 180000
    default-encoding: UTF-8
```

application-oauth.yml
```
oauth2:
  prefix: Bearer
  type: application/json
  github:
    client-id: 6ed445436a1a391574c4
    client-secret: 9ea0190cb2dea6b746b0dcdaa69aee00bc9b683d
    base-url: https://api.github.com
    access-token-url: https://github.com/login/oauth/access_token
    information-url: https://api.github.com/user
    email-url: https://api.github.com/user/emails
```

# EC2 설정

1. GitLab runner 설치
2. Docker 설치
3. nginx 설치
4. mysql 설치