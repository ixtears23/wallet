# CryptoWallet

## 환경

### 개발환경
- gradle : 8.1.1
- java : openjdk20
- spring-boot : 3.1.0
- spring : 6.0.9

### Server 구동 방법
- docker-compose 파일 실행
  - Dockerfile 빌드
  - 빌드된 Dockerfile 로 Server 구동
  - intellij 에서 빌드해서 코드를 확인하려면  
  gradle jvm version을 openjdk20으로 변경
> 코드 변경 후에는 `docker-compose up --build` 로 실행해야 변경된 내용 적용

```shell script
$ docker-compose -f docker-compose.yml up -d --build
```
### Swagger-UI
```
http://localhost:8080/swagger-ui/index.htm
```

### 테스트 환경
- DB : h2