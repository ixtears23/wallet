# Wallet

## 환경
- gradle : 8.1.1
- java : openjdk20
- spring-boot : 3.1.0
- spring : 6.0.9


### 세부 기능 요구 사항

- 트랜잭션
  - 트랜잭션 거래 생성(출금)
  - 트랜잭션 영수증 조회
  - 트랜잭션 해시 조회
  - block Confirmation 확인
  

- 출금 API



### gas 가격과, gas limit
- gas limit : Ether를 전송하는 트랜잭션의 경우 대략적으로 21,000 사용
- gas price
  - 단위 gas당 지불할 Ether의 양, 보통 Gwei(1e9 wei) 단위로 표시
  - gas price가 낮으면 트랜잭션 처리가 지연될 수 있음
  - 0으로 설정하면 블록에 포함되지 않을 수 있음

- gas가 gasLimit을 초과하는 경우 트랜잭션 실패 모든 상태 변화 롤백

- gas price 0으로 설정 X
  - 트랜잭션 실패 가능성
  - 블록에 포함되지 않을 확률 증가
  - 실패한 트랜잭션의 비용 환불 X
  - 출금하려는 이더의 양은 없어짐