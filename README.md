# thePiratesCodingTest

<br>

## 환경 설정 및 실행 방법
>
> Java 버젼: 1.8 설치
> 
> Gradle기반의 SpringBoot 프레임워크
>
> 개발 DB : H2(In-Memory 모드 사용)
>
>
> **실행 순서**
>
> 1. 프로젝트 다운로드
> 
> 2. 인텔리제이 IDE에서 실행
> 
> 3. http://localhost:8080/swagger-ui.html 접속 후 API 테스트로 과제 확인
> 

<br>

## ERD 다이어그램

![drawSQL-export-2022-03-16_00_38](https://user-images.githubusercontent.com/22443546/158415156-172a8eda-46d8-40c6-9725-82a1d8f8dfa2.png)

<br>

## API 설계

[\[API 명세서 바로가기\]](https://www.notion.so/44d230b0c8164084ae97d31882c84253?v=a198bb84237043baa0ffa3848a6d36f4)

<br>

## 과제 수행 중 고려 사항

>**1. API 사용 방법**
>
>swagger로 API 문서 자동화를 해주어서 위 실행 방법에 표시된 swagger링크에서 테스트 가능 합니다.
>API 명세서에 있는 sample data를 복사 붙여넣기해서 Test하시면 될것 같습니다.
>
>**2. Exception Handling**
>
>요구사항에는 없었지만 post 요청시 데이터가 빈값으로 넘어오면 다른 api 요청 시 에러가 발생하기 때문에 NotBlank validation 처리는 해주었습니다.
>ex) 마감시간이 null로 들어오면 수령 가능일 조회 api 요청시 nullpointerexception 발생
>
>**3. 수령일 조회 API**
>
>공휴일, 대체 공휴일, 주말을 모두 고려하여 수령가능일이 정확히 조회 가능하게끔 로직을 구현했습니다.
>
>**4. 테스트**
>
>테스트 케이스를 작성해 수령가능일 조회 api 비즈니스 로직을 검증 했습니다.
>restTemplate을 사용한 springboot test를 작성해 모듈이 올바르게 연계되어 동작하고 있는지를 확인했습니다.
>
