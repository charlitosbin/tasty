var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var util = require("util");
var idIpAddress = {}; 
app.get('/',function (req, res) {
	res.sendFile(__dirname+'/index.html');
})
io.on('connection',function(socket){
	socket.on("init",function(androidId){
		idIpAddress[socket.id] = socket.request.connection.remoteAddress;
		console.log("init>>>");
		console.log(androidId);
		prettyJSON(idIpAddress);
	})
	socket.on('message',function(data){
		var sockets = io.sockets.sockets;
		sockets.forEach(function(sock){
			console.log("Object: %j", sock);
			if(sock.id != socket.id){
				sock.emit('message',{message:data});
			}
		})
	})
	socket.on('disconnect',function(){
		console.log('one user disconnected '+socket.id);
		delete(idIpAddress[socket.id]);
		prettyJSON(idIpAddress);
	})
})
http.listen(3000,function(){
	console.log('server listening on port 3000');
});

function prettyJSON(obj) {
    console.log(util.inspect(obj, false, null));
}