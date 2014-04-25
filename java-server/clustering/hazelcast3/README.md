A simple echo and chat server running on Atmosphere 2 clustered through hazelcast. 

Type:

```
mvn jetty:run
```

And in another console

```
mvn jetty:run -Djetty.port=8090
```

Then, open the [corresponding client](http://jsbin.com/pocet/8/watch?js,console) connecting to server at 8080 and 8090 respectively in your browser and see that chat event from server at port 8080 is propagated to server at port 8090.