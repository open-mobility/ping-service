var stompClient = null;
var currentUsername = "No name";

function setConnected(connected) {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('response').innerHTML = '';
}

function _id(identifier) {
    return document.getElementById(identifier);
}

function _idv(identifier) {
    return _id(identifier).value;
}
/**
Create a new connection
**/
function connect() {

    currentUsername = _idv('name'); //read name from text box
    stompClient = Stomp.client('ws://localhost:8080/ws-connect');

    /* These headers are not accessible at the moment,
    header at server side is does not contains these keys,
    Could be bug in the library of JS */

    var headers = {'username': currentUsername, 'password': 'somePassword'};
    stompClient.connect(headers, onConnectSuccess, onConnectFailure);
}

function onConnectSuccess(frame) {

        setConnected(true);
        console.log('Connected: ' + frame);

        stompClient.subscribe('/user/queue/greetings', function(greeting){
            console.log("***************** QUEUE");
            console.table(greeting.body);
            showGreeting(JSON.parse(greeting.body).message);
        });

        stompClient.subscribe('/topic/greetings', function(greeting){
            console.table("***************** TOPIC");
            console.log(greeting.body);
            showBroadCast(JSON.parse(greeting.body));
        });

        stompClient.subscribe("/user/queue/reply", function(message) {
            console.log("Querrrrrrrrrrrrrrrrrrrrrrrr Reply");
            console.log(message)
        });

}

function onConnectFailure(error) {
  console.log("Conn NOT OK " + url + ": " + JSON.stringify(error));
}


function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    var chat = _idv('chat');
    var person = _idv('person');
    var data = JSON.stringify({ 'senderName': currentUsername, 'chat':chat, 'person': person});
    console.log(data);
    stompClient.send("/app/chat", {}, data);
}

function sendMessage() {
}

function showGreeting(message) {
    var response = document.getElementById('response');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(message));
    response.appendChild(p);
}

function showBroadCast(packet) {

    var response = document.getElementById('broadcast');

    if (packet.action == 'USER_DISCONNECTED') {
        p = document.getElementById("online_"+packet.message);
        p.remove();
        return;
    }

    var p = document.createElement('p');
    p.setAttribute("id", "online_"+packet.message)
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(packet.message));
    response.appendChild(p);
}