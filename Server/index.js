var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var util = require("util");

var idClientIpAddress = {}; 

app.get('/',function (req, res) {
	res.sendFile(__dirname+'/index.html');
})

io.on('connection',function(socket){
	
	socket.on("init_client",function(androidId){
		idClientIpAddress[socket.id] = socket.request.connection.remoteAddress;
		console.log("init_client>>>");
		console.log(androidId);
		prettyJSON(idClientIpAddress);
	})

	socket.on('message',function(data){
		var sockets = io.sockets.sockets;
		sockets.forEach(function(sock){
			if(sock.id != socket.id){
				sock.emit('message',{message:data});
			}
		})
	})

	socket.on('disconnect',function(){
		console.log('one user disconnected '+socket.id);
		delete(idClientIpAddress[socket.id]);
		prettyJSON(idClientIpAddress);
	})
})

http.listen(3000,function(){
	console.log('server listening on port 3000');
});

function prettyJSON(obj) {
    console.log(util.inspect(obj, false, null));
}