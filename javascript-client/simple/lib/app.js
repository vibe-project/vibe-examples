if (typeof exports === "object") {
    // Only in Node.js
    var react = require("react-client");
}

var global = this;
react.open("http://localhost:8080/react", {reconnect: false})
.on("open", function() {
    global.socket = this;
    for (var i = 0; i < 10; i++) {
        this.send("echo", "Hello to me " + i).send("chat", "Hello everyone " + i);
    }
})
.on("chat", function(data) {
    console.log("on chat event: " + data);
})
.on("echo", function(data) {
    console.log("on echo event: " + data);
});