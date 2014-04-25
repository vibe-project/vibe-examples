A simple echo and chat server running on Java WebSocket API 1. 

Type:

```
mvn jetty:run
```

Then, open an [echo client](http://jsbin.com/pocet/8/watch?js,console) in your browser.

**Note**

* Java WebSocket API 1 has no support of HTTP. You have to use only WebSocket transport.
* To use it together with Servelt 3, you should declare `Server` as `static` or use vendor-specific API.