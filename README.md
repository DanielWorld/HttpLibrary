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

