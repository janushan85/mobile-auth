# Social Authentication for Mobile App
백엔드에서 소셜 토큰을 검증하여 로그인/가입을 수행하는 예제이다.

## 사전 조건
애플 로그인을 테스트하기 위해서는 localhost가 아닌 도메인으로 접근이 가능해야 한다. 로컬의 hosts 파일을 수정하여 도메인으로 접근이 가능하도록 설정한다.

예제)
$ sudo vi /etc/hosts
127.0.0.1   mobile-auth.com

## 실행 방법
$ mvn spring-boot:run -Dspring-boot.run.arguments=--signin.google.clientId={your_info},--signin.apple.clientId={your_info},--signin.facebook.clientId={your_info}

## 접근 방법
https://localhost:8443/
https://mobile-app:8443/

## 주의사항
* 구글에 로그인이 되어있으면 바로 소셜 로그인이 수행된다. 구글이 아닌 다른 것으로 테스트하려면 구글 로그아웃을 하거나 시크릿 모드로 실행해야 한다.
* 애플 로그인을 수행하려면 반드시 반드시 https로 구동해야 한다. 
* 페이스북 로그인을 수행하려면 localhost로 접근해야 한다.

### 크롬에서 신뢰할수 없는 인증서에서 더이상 진행할수 없는 경우 
* https://medium.com/@dblazeski/chrome-bypass-net-err-cert-invalid-for-development-daefae43eb12