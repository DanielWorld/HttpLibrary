# HttpLibrary
OkHttp 3.0 라이브러리를 이용한 http connection 라이브러리

## Gradle build
build.gradle
<pre>
repositories {
  ...
  maven { url 'https://jitpack.io' }  // Github 에서 바로 gradle depenency compile 처리하기 위함.
}

dependencies {
  ...
  compile 'com.github.DanielWorld:HttpLibrary:-SNAPSHOT'
  // 만약 해당 library project 에 dependencies 를 넣은 경우 
  // 최상위 프로젝트에도 repositories 에 maven { url 'https://jitpack.io' } 처리를 해줘야한다.
}
</pre>

### Asynchronous GET
Example
<pre>
HttpRequest request = new HttpRequest();
request.setURL("url");                            // URL 설정
request.setMethod(HttpRequest.Method.GET);        // http GET 방식
request.setRequestType(RequestType.Type.STRING);  // String request 타입
request.addHeader("header_key", "header_value");  // 헤더 추가

request.addParameter("Locale", "enUS");           // 파라미터들 추가
request.addParameter("Id", 134);

// Async Http connection 시작
AsyncHttpConnection.getInstance(android.content.Context).start(request, new JsonHttpResponseHandler(), new okhttp3.Intercepter);
// JsonHttpResponseHandler() 는 response body 가 JSON 형태일 경우, JSON 으로 받을 수 있음
// AsyncHttpReponseHandler() 도 사용 가능한데, header 와 response body를 그대로 반환 (http status code 포함)
</pre>
