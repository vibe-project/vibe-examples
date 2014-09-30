// This try-catch statement is to load the module only in Node.js and not needed in browser
try {
    var vibe = require("vibe-client");
} catch (e) {}

var socket = vibe.open("http://localhost:8080/vibe", {reconnect: false});
socket.on("open", function() {
    console.log("on open event");
    socket.send("echo", "An echo message").send("chat", "A chat message");
})
.on("close", function(reason) {
    console.log("on close event: " + reason);
})
.on("chat", function(data) {
    console.log("on chat event: " + data);
})
.on("echo", function(data) {
    console.log("on echo event: " + data);
});