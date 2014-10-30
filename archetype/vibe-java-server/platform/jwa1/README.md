Type:

```
mvn jetty:run
```

Then, open the [client](http://jsbin.com/ditewo/1/watch?js,console) in your browser.

**Note**

* This example runs on Jetty but you can run it on other Java WebSocket API implementations.
* Java WebSocket API 1 has no support of HTTP. You have to use only WebSocket transport.
* To use it together with Servelt 3, you should declare `Server` as `static` or use vendor-specific API.