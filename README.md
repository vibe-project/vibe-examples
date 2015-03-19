**Notice:** Vibe has [renamed](https://groups.google.com/d/msg/vibe-project/kmURxoLC85I/UO7ECEdOYs4J) to Cettia and is no longer maintained. Use [Cettia](http://cettia.io/).

## Vibe Examples
This project contains various working examples demonstrating how to use Vibe. To run examples, either download or clone the repository and consult each example README. As several projects are involved, this repository doesn't use tag. Every example uses the latest versions. 

### Archetype
An archetype example is a port of [echo and chat example](http://vibe-project.github.io/projects/vibe-protocol/3.0.0-Alpha12/reference/#example) written in reference implementation and an bare-bones application which you can use as the starting point to write your own application.

* A client
    * should connect to `http://localhost:8080/vibe`.
    * should fire `echo` event with some text data on `open` event.
    * should fire `chat` event with some text data on `open` event.
* A server
    * should accept `http://localhost:8080/vibe`.
    * should send `echo` event back to the client that sent the event.
    * should broadcast `chat` event to every client that connected to the server.

#### Vibe Java Server
##### By platform
* [Atmosphere 2](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/platform/atmosphere2)
* [Grizzly 2](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/platform/grizzly2)
* [Netty 4](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/platform/netty4)
* [Play 2](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/platform/play2)
* [Servlet 3 and Java WebSocket API 1](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/platform/servlet3-jwa1)
* [Vert.x 2](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/platform/vertx2)

##### By platform on platform
* [JAX-RS 2 on Atmosphere 2](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/platform-on-platform/jaxrs2-atmosphere2)

##### By clustering
* [AMQP 1](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/clustering/amqp1)
* [Hazelcast 3](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/clustering/hazelcast3)
* [jGroups 3](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/clustering/jgroups3)
* [JMS 2](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/clustering/jms2)
* [Redis 2](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/clustering/redis2)
* [Vert.x 2](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/clustering/vertx2)

##### By dependency injection
* [CDI 1](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/dependency-injection/cdi1)
* [Dagger 1](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/dependency-injection/dagger1)
* [Guice 3](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/dependency-injection/guice3)
* [HK 2](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/dependency-injection/hk2)
* [PicoContainer 2](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/dependency-injection/picocontainer2)
* [Spring 4](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/dependency-injection/spring4)
* [Tapestry 5](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-java-server/dependency-injection/tapestry5)

#### [Vibe JavaScript Client](https://github.com/vibe-project/vibe-examples/tree/master/archetype/vibe-javascript-client)

### Migration
A migration example is a port of other project's basic example or a simple application to help migrate into Vibe.

* [Atmosphere 2](https://github.com/vibe-project/vibe-examples/tree/master/migration/atmosphere2)
