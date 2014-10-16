## Vibe Examples
This project contains various working examples demonstrating how to use Vibe. To run examples, either download or clone the repository and consult each example README.

### Archetype
An archetype example is port of [basic echo and chat example](http://vibe-project.github.io/projects/vibe-protocol/3.0.0-Alpha2/api/#module--vibe-protocol-) written in reference implementation and an bare-bones application which you can use as the starting point to write your own application.

* A client
    * should connect to `http://localhost:8080/vibe`.
    * should fire `echo` event with some text data on `open` event.
    * should fire `chat` event with some text data on `open` event.
* A server
    * should accept `http://localhost:8080/vibe`.
    * should send `echo` event back to the client that sent the event.
    * should broadcast `chat` event to every client that connected to the server.

#### Examples
* Vibe Java Server
    * By platform.
        * [Atmosphere 2](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/platform/atmosphere2)
        * [Java WebSocket API 1](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/platform/jwa1)
        * [Play 2](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/platform/play2)
        * [Servlet 3](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/platform/servlet3)
        * [Vert.x 2](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/platform/vertx2)
    * By clustering.
        * [Hazelcast 3](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/clustering/hazelcast3)
        * [Vert.x 2](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/clustering/vertx2)
    * By dependency injection.
        * [Spring](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/dependency-injection/spring4)
* [Vibe JavaScript Client](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-javascript-client)
