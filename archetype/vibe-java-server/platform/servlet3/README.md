Type:

```
mvn jetty:run
```

Then, open the [client](http://jsbin.com/loqika/1/watch?js,console) in your browser.

**Note**

* Servlet 3 has no support of WebSocket. You have to use other transports based on HTTP.
* To use it together with Java WebSocket 1, you should declare `Server` as `static` or use vendor-specific API.