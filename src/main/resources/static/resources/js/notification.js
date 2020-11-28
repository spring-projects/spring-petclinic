var stompClient = null;

function displayMessage() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/public', function (socketMessage) {

            noty({
                text: JSON.parse(socketMessage.body).content,
                type: JSON.parse(socketMessage.body).type,
                layout: "topRight",
                timeout: 4000,
                animation: {
                    open: 'animated bounceInRight',
                    close: 'animated bounceOutRight',
                    easing: 'swing',
                    speed: 500
                }
            });
        });
    });
}

