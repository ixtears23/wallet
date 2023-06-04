# CryptoWallet

## 환경

### 개발환경
- gradle : 8.1.1
- java : openjdk20
- spring-boot : 3.1.0
- spring : 6.0.9
> 참고 : 이더리움이 부족해서 wei 단위로 테스트해서 개발했습니다. 

### Server 구동 방법
- docker-compose 파일 실행
  - Dockerfile 빌드
  - 빌드된 Dockerfile 로 Server 구동
> 코드 변경 후에는 `docker-compose up --build` 로 실행해야 변경된 내용 적용

### Swagger-UI
```
http://localhost:8080/swagger-ui/index.htm
```

### 테스트 환경
- DB : h2
> 이유 : Dockerfile 빌드 시 Postgresql에 의존적이기 때문에, 테스트 실패. 그렇다고 테스트를 스킵하는 것 보다, h2에서라도 성공한 후 빌드되록 함


## 기능 특이 사항
### 입금 모니터링
- 입금의 경우 동일한 이더노드 엔드포인트의 경우만 추적
