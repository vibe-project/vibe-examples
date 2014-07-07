// This try-catch statement is to load the module only in Node.js and not needed in browser
try {
    var vibe = require("vibe-client");
} catch (e) {}

vibe.open("http://localhost:8080/vibe", {reconnect: false})
.on("open", function() {
    this.send("echo", "An echo message").send("chat", "A chat message");
})
.on("chat", function(data) {
    console.log("on chat event: " + data);
})
.on("echo", function(data) {
    console.log("on echo event: " + data);
});