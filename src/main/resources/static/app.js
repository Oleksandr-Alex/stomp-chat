let stompClient = null;

function setConnected(connected) {
    $("#name").prop("disabled", connected);
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
        $("#messenger").show();
    } else {
        $("#conversation").hide();
        $("#messenger").hide();
    }
    $("#messages").html("");
}

function connect() {
    let socket = new SockJS('/alexb-chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);

        stompClient.subscribe("/app/chat/00000000-0000-0000-0000-000000000032/old-messages", function (messages) {
            let msgArr = JSON.parse(messages.body);

            for (let index in msgArr) {
                if (msgArr.hasOwnProperty(index)) {
                    showMessage(msgArr[index]);
                }
            }
        });

        stompClient.subscribe('/chat/00000000-0000-0000-0000-000000000032/messages', function (message) {
            showMessage(JSON.parse(message.body));
        });

        stompClient.subscribe('/chat/errors', function (message) {
            showMessage(JSON.parse(message.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    stompClient.send("/app/chat/00000000-0000-0000-0000-000000000032/send-message", {},
        JSON.stringify({
            'author': $("#name").val(),
            'content': $("#message").val()
        }));
}

function showMessage(message) {
    $("#messages").append("<tr><td>" + message.author + ": " + message.content + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendMessage();
        $("#message").val("");
    });
});

var logout = function() {
    $.post("/logout", function() {
        $("#user").html('');
        $(".unauthenticated").show();
        $(".authenticated").hide();
    })
    return true;
}
