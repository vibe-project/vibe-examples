Type:

```
mvn jetty:run
```

Then, open the [client](http://jsbin.com/ditewo/1/watch?js,console) in your browser.

**Note**

* This example uses Servlet 3 as well as Java webSocket API 1 and runs on Jetty but you can run it on other platform like Tomcat implementing Servlet 3 and Java WebSocket API 1.
* To share `Server` between Servlet platform and JWA platform, it is declared as `static`.