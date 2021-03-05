# mysql 8.0에서 load data local infile 사용하기

Mysql 8.0 에서는 보안상의 이슈로 load data local infile 이 전용 폴더에 존재하지 않으면 불가능하다.

이러한 경우에는 여러가지 방법이 있다.
- FileReader로 읽어 Insert 구문을 통해서 데이터 집어 넣기
- infile 폴더로 파일 카피 하기
- option 을 설정하여 기존 코드 그대로 가능하게 하기

위의 방법 중 가장 마지막 방법을 소개하면 우선 접속 시 url 에 아래와 같이 `allowLoadLocalInfile=true` 옵션을 추가해준다.
```java
"jdbc:mysql://localhost:3306/computer?serverTimezone=UTC&allowLoadLocalInfile=true"
```
그다음 Java 코드로 아래의 SQL 구문을 날려주면 기존에 사용하던 LOAD DATA LOCAL 을 사용 할 수 있다.
```sql
SET GLOBAL local_infile = true;
```