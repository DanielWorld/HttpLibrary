# HttpLibrary
HTTP connection library which is based on OkHttp 3.2 library

## Gradle build
build.gradle
<pre>
buildscript {
		repositories {
			...
			jcenter()
		}
	}

dependencies {
  ...
  compile 'com.danielworld:http-connection-library:1.1.3'
  // If current project is dependent on parent project (Indeed, there is parent project)
  // You should write jcenter() in parent's repositories
}
</pre>

### Asynchronous GET
Excute on the main thread!
<pre>
HttpRequest request = new HttpRequest();
request.setURL("url");                            // Set URL
request.setMethod(HttpRequest.Method.GET);        // Http GET method
request.setRequestType(RequestType.Type.STRING);  // String request type

request.addHeader("header_key", "header_value");  // add Header

request.addParameter("Locale", "enUS");           // add Parameter
request.addParameter("Id", 134);

// Start Async Http connection 
AsyncHttpConnection
  .getInstance(android.content.Context)
    .start(request, new JsonHttpResponseHandler(), new okhttp3.Intercepter);

</pre>

### Synchronous GET
<pre>
...
SyncHttpConnection
  .getInstance(android.content.Context)
    .start(request, new JsonHttpResponseHandler(), new okhttp3.Intercepter);
</pre>

### POST Json request
<pre>
HttpRequest request = new HttpRequest();
request.setURL("url");                            // Set URL
request.setMethod(HttpRequest.Method.POST);        // Http GET method
request.setRequestType(RequestType.Type.STRING);  // String request type

request.addHeader("header_key", "header_value");  // add Header

JsonObject jsonObject = new JsonObject();
jsonObject.addProperty("Text", "example1");
jsonObject.addProperty("Id", 11324);

request.setContentType(ContentType.getApplicationJson());   // Content-type (required)
request.addBody(jsonObject.toString);                       // add body	(required)

AsyncHttpConnection
  .getInstance(android.content.Context)
    .start(request, new JsonHttpResponseHandler(), new okhttp3.Intercepter);
</pre>

### POST Form request
<pre>
HttpRequest request = new HttpRequest();
request.setURL("url");                            // Set URL
request.setMethod(HttpRequest.Method.POST);        // Http POST method
request.setRequestType(RequestType.Type.STRING);  // String request type

request.addHeader("header_key", "header_value");  // add Header

request.addParameter("text","uiok");              // Form body
request.addParameter("Id", 22242);

request.setContentType(ContentType.getApplicationXWwwFormUrlencoded());   // Content-type (required)

AsyncHttpConnection
  .getInstance(android.content.Context)
    .start(request, new JsonHttpResponseHandler(), new okhttp3.Intercepter);
</pre>

### DELETE
<pre>
HttpRequest request = new HttpRequest();
request.setURL("url");                            // Set URL
request.setMethod(HttpRequest.Method.DELETE);        // Http DELETE method
request.setRequestType(RequestType.Type.STRING);  // String request 타입

request.addHeader("header_key", "header_value");  // String request type

AsyncHttpConnection
  .getInstance(android.content.Context)
    .start(request, new JsonHttpResponseHandler(), new okhttp3.Intercepter);
</pre>

### DELETE Json request
<pre>
HttpRequest request = new HttpRequest();
request.setURL("url");                            // Set URL
request.setMethod(HttpRequest.Method.DELETE);        // Http DELETE method
request.setRequestType(RequestType.Type.STRING);  // String request 타입

request.addHeader("header_key", "header_value");  // String request type

JsonObject jsonObject = new JsonObject();
jsonObject.addProperty("Text", "example1");
jsonObject.addProperty("Id", 11324);

request.setContentType(ContentType.getApplicationJson());   // Content-type 
request.addBody(jsonObject.toString);                       // add body

AsyncHttpConnection
  .getInstance(android.content.Context)
    .start(request, new JsonHttpResponseHandler(), new okhttp3.Intercepter);
</pre>

### DELETE Query string
<pre>
HttpRequest request = new HttpRequest();
request.setURL("url");                            // Set URL
request.setMethod(HttpRequest.Method.DELETE);        // Http DELETE method
request.setRequestType(RequestType.Type.STRING);  // String request 타입

request.addHeader("header_key", "header_value");  // String request type

request.addParameter("abc", "sfd");
request.addParameter("23sd", 22344);

AsyncHttpConnection
  .getInstance(android.content.Context)
    .start(request, new JsonHttpResponseHandler(), new okhttp3.Intercepter);
</pre>

### PUT Json request
<pre>
HttpRequest request = new HttpRequest();
request.setURL("url");                            // Set URL
request.setMethod(HttpRequest.Method.PUT);        // Http PUT method
request.setRequestType(RequestType.Type.STRING);  // String request 타입

request.addHeader("header_key", "header_value");  // add Header

JsonObject jsonObject = new JsonObject();
jsonObject.addProperty("Text", "example1");
jsonObject.addProperty("Id", 11324);

request.setContentType(ContentType.getApplicationJson());   // Content-type
request.addBody(jsonObject.toString);                       // add body

AsyncHttpConnection
  .getInstance(android.content.Context)
    .start(request, new JsonHttpResponseHandler(), new okhttp3.Intercepter);
</pre>

### Multipart/form-data POST request
<pre>
HttpRequest request = new HttpRequest();
request.setURL("url");                            // Set URL
request.setMethod(HttpRequest.Method.POST);        // Http POST method
request.setRequestType(RequestType.Type.MULTI_PART);  // Multi-part request type

request.addHeader("header_key", "header_value");  // add Header

// Add Image file (file path, file name, parameter name, image content-type)
request.addFile(new File("aa.png").getAbsolutePath(), new File("aa.png").getName(), "parameterName", ContentType.getApplicationOctetStream());

// add parameters
// add to body using multipart/form-data format (Content-Type = text/plain) in multipart/form-data task
request.addParameter("Locale", "enUS"); 
request.addParameter("Id", 134);

AsyncHttpConnection
  .getInstance(android.content.Context)
    .start(request, new JsonHttpResponseHandler(), new okhttp3.Intercepter);
</pre>

### JsonHttpResponseHandler
<pre>
public void onSuccess(int statusCode, Headers headers, JSONObject response) {
	// JSONObject response (there should be contents)
}

public void onSuccess(int statusCode, Headers headers, JSONArray response) {
        // JSONArray response (there should be contents)
}

public void onSuccess(int statusCode, Headers headers, String response) {
       // String response (it could be empty)
}

public void onFailure(int statusCode, Headers headers, ResponseBody responseBody){
	// Failure response
}
</pre>

### AsyncHttpResponseHandler
<pre>
public void onSuccess(int statusCode, Headers headers, ResponseBody responseBody) {
       
}

public void onFailure(int statusCode, Headers headers, ResponseBody responseBody){
	
}
</pre>
