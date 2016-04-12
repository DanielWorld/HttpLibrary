#http://proguard.sourceforge.net/manual/examples.html
#http://proguard.sourceforge.net/manual/usage.html

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-keepclasseswithmembers class * {
    native <methods>;
}
-keepclasseswithmembers class com.danielpark.httpconnection.AsyncHttpConnection {
    public final static synchronized com.danielpark.httpconnection.AsyncHttpConnection getInstance(android.content.Context);
    public void start(com.danielpark.httpconnection.request.HttpRequest, com.danielpark.httpconnection.handler.JsonHttpResponseHandler, okhttp3.Interceptor);
    public void start(com.danielpark.httpconnection.request.HttpRequest, com.danielpark.httpconnection.handler.AsyncHttpResponseHandler, okhttp3.Interceptor);
}

-keepclasseswithmembers class com.danielpark.httpconnection.SyncHttpConnection {
    public final static synchronized com.danielpark.httpconnection.SyncHttpConnection getInstance(android.content.Context);
    public void start(com.danielpark.httpconnection.request.HttpRequest, com.danielpark.httpconnection.handler.JsonHttpResponseHandler, okhttp3.Interceptor);
    public void start(com.danielpark.httpconnection.request.HttpRequest, com.danielpark.httpconnection.handler.AsyncHttpResponseHandler, okhttp3.Interceptor);
}

-keepclasseswithmembers class com.danielpark.httpconnection.HttpConnectionTask {
    public <methods>;
}
-keepclasseswithmembers class com.danielpark.httpconnection.SyncHttpConnection {
    public <methods>;
}
-keepclasseswithmembers class com.danielpark.httpconnection.request.HttpRequest {
    public <methods>;
}
-keepclasseswithmembers class com.danielpark.httpconnection.type.RequestType {
    public <methods>;
}
-keepclasseswithmembers class com.danielpark.httpconnection.handler.JsonHttpResponseHandler {
    public <methods>;
}
-keepclasseswithmembers class com.danielpark.httpconnection.handler.AsyncHttpResponseHandler {
    public <methods>;
}
-keepclasseswithmembers interface com.danielpark.httpconnection.listener.ResponseHandlerInterface {
    <methods>;
}
-keepclasseswithmembers class com.danielpark.httpconnection.type.ContentType {
    public <methods>;
}

# enum Method in HttpRequest class
-keepclasseswithmembers class com.danielpark.httpconnection.request.HttpRequest$Method {
    *;
}
-keepclasseswithmembers class com.danielpark.httpconnection.type.RequestType$Type {
    *;
}
-keepclasseswithmembers class com.danielpark.httpconnection.HttpConnection$SyncType {
    *;
}
-dontnote android.net.http.**
-dontnote org.apache.http.conn.scheme.**
-dontnote org.apache.http.conn.**
-dontnote org.apache.http.params.**

# Protect members of enum field in RequestType class
#-keep public enum com.danielpark.httpconnection.type.RequestType$** {
#    public *;
#}
#-keep public enum com.danielpark.httpconnection.request.HttpRequest$** {
#    public *;
#}

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

##---------------End: proguard configuration for Gson  ----------