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
  // 만약 해당 project 가 상위 프로젝트에 종속되는 경우 (즉 부모 프로젝트가 존재하는 경우) 
  // 최상위 프로젝트에도 repositories 에 maven { url 'https://jitpack.io' } 처리를 해줘야한다.
}
</pre>

### Asynchronous GET
main thread 에서 실행 필수!
<pre>
HttpRequest request = new HttpRequest();
request.setURL("url");                            // URL 설정
request.setMethod(HttpRequest.Method.GET);        // http GET 방식
request.setRequestType(RequestType.Type.STRING);  // String request 타입

request.addHeader("header_key", "header_value");  // 헤더 추가

request.addParameter("Locale", "enUS");           // 파라미터들 추가
request.addParameter("Id", 134);

// Async Http connection 시작
AsyncHttpConnection
  .getInstance(android.content.Context)
    .start(request, new JsonHttpResponseHandler(), new okhttp3.Intercepter);
// JsonHttpResponseHandler() 는 response body 가 JSON 형태일 경우, JSON 으로 받을 수 있음, String response 는 Json 형태가 아닐경우 받음
// (만약 ResponseBody 그대로를 받고 싶다면 AsyncHttpResponseHandler() 를 대신 사용!)
// new okhttp3.Intercepter 는 null 해도 상관없음.
</pre>

### Synchronous GET
Asynchronous GET 과 동일 다만 Start 만 다름 (main thread 에서 실행불가 X, Service 나 thread 내에서 처리!)
<pre>
// AsyncHttpConnection 에서 SyncHttpConnection 만 변경됨...
SyncHttpConnection
  .getInstance(android.content.Context)
    .start(request, new JsonHttpResponseHandler(), new okhttp3.Intercepter);
</pre>
그 외 POST, DELETE, PUT 그리고 Multi-part request 등등 Async 와 Sync http connection 의 차이는 이와 동일

### POST
<pre>
HttpRequest request = new HttpRequest();
request.setURL("url");                            // URL 설정
request.setMethod(HttpRequest.Method.POST);        // http GET 방식
request.setRequestType(RequestType.Type.STRING);  // String request 타입

request.addHeader("header_key", "header_value");  // 헤더 추가

JsonObject jsonObject = new JsonObject();
jsonObject.addProperty("Text", "example1");
jsonObject.addProperty("Id", 11324);

request.setContentType(ContentType.getApplicationJson());   // Content-type 설정
request.addBody(jsonObject.toString);                       // body 추가 (String 형태이며 설정한 Content-type 과 유효해야 한다.)

// Async Http connection 시작
AsyncHttpConnection
  .getInstance(android.content.Context)
    .start(request, new JsonHttpResponseHandler(), new okhttp3.Intercepter);
</pre>

### DELETE
<pre>
HttpRequest request = new HttpRequest();
request.setURL("url");                            // URL 설정
request.setMethod(HttpRequest.Method.DELETE);        // http GET 방식
request.setRequestType(RequestType.Type.STRING);  // String request 타입

request.addHeader("header_key", "header_value");  // 헤더 추가

//----- 추가할 body가 없다면 안넣어도 좋다.
JsonObject jsonObject = new JsonObject();
jsonObject.addProperty("Text", "example1");
jsonObject.addProperty("Id", 11324);

request.setContentType(ContentType.getApplicationJson());   // Content-type 설정
request.addBody(jsonObject.toString);                       // body 추가 (String 형태이며 설정한 Content-type 과 유효해야 한다.)
//------

// Async Http connection 시작
AsyncHttpConnection
  .getInstance(android.content.Context)
    .start(request, new JsonHttpResponseHandler(), new okhttp3.Intercepter);
</pre>

### PUT
<pre>
HttpRequest request = new HttpRequest();
request.setURL("url");                            // URL 설정
request.setMethod(HttpRequest.Method.DELETE);        // http GET 방식
request.setRequestType(RequestType.Type.STRING);  // String request 타입

request.addHeader("header_key", "header_value");  // 헤더 추가

JsonObject jsonObject = new JsonObject();
jsonObject.addProperty("Text", "example1");
jsonObject.addProperty("Id", 11324);

request.setContentType(ContentType.getApplicationJson());   // Content-type 설정
request.addBody(jsonObject.toString);                       // body 추가 (String 형태이며 설정한 Content-type 과 유효해야 한다.)
//------

// Async Http connection 시작
AsyncHttpConnection
  .getInstance(android.content.Context)
    .start(request, new JsonHttpResponseHandler(), new okhttp3.Intercepter);
</pre>

### Multipart/form-data POST request
<pre>
HttpRequest request = new HttpRequest();
request.setURL("url");                            // URL 설정
request.setMethod(HttpRequest.Method.POST);        // http POST 방식
request.setRequestType(RequestType.Type.MULTI_PART);  // Multi-part request 타입

request.addHeader("header_key", "header_value");  // 헤더 추가

// 이미지 파일 추가 (순서대로 파일 경로, 파일 이름, 파라미터 이름, 이미지 Content-type)
request.addFile(new File("aa.png").getAbsolutePath(), new File("aa.png").getName(), "parameterName", ContentType.getApplicationOctetStream());

// 파라미터들 추가
// 해당 파라미터들 역시 multipart/form-data 형식으로 body에 붙어서 들어간다. (Content-Type = text/plain)
request.addParameter("Locale", "enUS"); 
request.addParameter("Id", 134);

// Async Http connection 시작
AsyncHttpConnection
  .getInstance(android.content.Context)
    .start(request, new JsonHttpResponseHandler(), new okhttp3.Intercepter);
</pre>
