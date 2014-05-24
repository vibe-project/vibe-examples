if (typeof exports === "object") {
    // Only in Node.js
    var vibe = require("vibe-client");
}

var global = this;
vibe.open("http://localhost:8080/vibe", {reconnect: false})
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