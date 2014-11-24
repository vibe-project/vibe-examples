$(function () {
    "use strict";

    var content = $('#content');
    var input = $('#input');
    var status = $('#status');
    var myName = false;
    var author = null;
    var logged = false;

    var socket = vibe.open("chat");
    socket.on("connecting", function() {
        content.html($('<p>', {text: 'Connecting to the server'}));
    });
    socket.on("open", function() {
        content.html($('<p>', {text: 'Connection opened'}));
        input.removeAttr('disabled').focus();
        status.text('Choose name:');
    });
    socket.on("error", function(error) {
        content.html($('<p>', {text: 'There was an error in connection ' + error.message}));
        logged = false;
    });
    socket.on("waiting", function(delay, attempts) {
        content.html($('<p>', {text: 'Trying to reconnect ' + delay + '. Reconnection attempts ' + attempts}));
    });
    socket.on("message", function(data) {
        if (!logged && myName) {
            logged = true;
            status.text(myName + ': ').css('color', 'blue');
        } else {
            var me = data.author == author;
            var date = typeof(data.time) == 'string' ? parseInt(data.time) : data.time;
            addMessage(data.author, data.message, me ? 'blue' : 'black', new Date(date));
        }
    });
    socket.on("close", function() {
        content.html($('<p>', {text: 'Connection closed'}));
        input.attr('disabled', 'disabled');
    });

    input.keydown(function(e) {
        if (e.keyCode === 13) {
            var msg = $(this).val();
            if (author == null) {
                author = msg;
            }
            socket.send("message", {author: author, message: msg});
            $(this).val('');
            if (myName === false) {
                myName = msg;
            }
        }
    });

    function addMessage(author, message, color, datetime) {
        content.append('<p><span style="color:' + color + '">' + author + '</span> @ ' +
            + (datetime.getHours() < 10 ? '0' + datetime.getHours() : datetime.getHours()) + ':'
            + (datetime.getMinutes() < 10 ? '0' + datetime.getMinutes() : datetime.getMinutes())
            + ': ' + message + '</p>');
    }
});
