# 1.1.7
2016-11-16

- Allow duplicate key parameter in HTTP GET (StringTask.class)
- okhttp 3.4.2 updated (https://github.com/square/okhttp/blob/master/CHANGELOG.md)
- okio 1.11.0 updated (https://github.com/square/okio/blob/master/CHANGELOG.md)

# 1.1.6
2016-08-08

- Enable interceptor callback
- Removed http response cache mode (unstable)
- Change target / compile sdk to 24
- okhttp 3.4.1 / okio 1.9.0 updated

# 1.1.5
2016-07-09

- change timeout 20 to 30 seconds
- add response() cache mode

# 1.1.3
2016-06-23

- update okhttp 3.3.1 / okio 1.8.0 / gradle 2.1.2
- remove static Log object
- maintain try - catch with Exception
- if no content-length from response, then call body.close()
