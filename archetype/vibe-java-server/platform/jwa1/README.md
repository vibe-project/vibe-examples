**TODO** As the handshaking is landed to the Vibe protocol, now platform is required to support both HTTP and WebSocket resource. However, Java WebSocket API supports only WebSocket so that it doesn't work. Later, this example will be rewritten in conjunction with Servlet 3.

Type:

```
mvn jetty:run
```

Then, open the [client](http://jsbin.com/ditewo/1/watch?js,console) in your browser.

**Note**

* This example runs on Jetty but you can run it on other Java WebSocket API implementations.
* Java WebSocket API 1 has no support of HTTP. You have to use only WebSocket transport.
* To use it together with Servlet 3, you should declare `Server` as `static`, use vendor-specific API or introduce CDI.