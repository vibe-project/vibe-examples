Type:

```
mvn jetty:run
```

Then, open the [client](http://jsbin.com/pocet/12/edit) in your browser.

**Note**

* Java WebSocket API 1 has no support of HTTP. You have to use only WebSocket transport.
* To use it together with Servelt 3, you should declare `Server` as `static` or use vendor-specific API.