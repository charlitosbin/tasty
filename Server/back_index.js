var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var util = require("util");

var idClientIpAddress = {}; 
var idRestaurantIpAddress = {};

app.get('/',function (req, res) {
	res.sendFile(__dirname+'/index.html');
})

io.on('connection',function(socket){
	
	socket.on("init_client",function(androidId){
		console.log("init_client>>>");
		addObjectToClientIpAddress(androidId, socket);
		prettyJSON(idClientIpAddress);
	})

	socket.on("init_restaurant",function(androidId){
		console.log("init_restaurant>>>");
		addObjectToRestaurantIpAddress(androidId, socket);
		var client = addClientToRestaurant(socket);
		prettyJSON(idRestaurantIpAddress);
		socket.emit('client',{"client" : client.ipAddress});

		var sockets = io.sockets.sockets;

		sockets.forEach(function(sock){
			if(sock.request.connection.remoteAddress == client.ipAddress){
				sock.emit('restaurant',{"restaurant" : client.restaurantIpAddress});
			}
		});
	})

	socket.on('message',function(data){
		var sockets = io.sockets.sockets;
		console.log("sendingMessage>>>");
		if(idClientIpAddress[socket.id] != null && idClientIpAddress[socket.id] !== undefined){
			idClientIpAddress[socket.id].restaurantSocket.emit("message", {message:data});
		}else if(idRestaurantIpAddress[socket.id]!= null && idRestaurantIpAddress[socket.id] !== undefined){
			idRestaurantIpAddress[socket.id].clientSocket.emit("message", {message:data});
		}
		//sockets.forEach(function(sock){

			//if(sock.id != socket.id){
			//	sock.emit('message',{message:data});
		//	}
		//})
	})

	socket.on('disconnect',function(){
		console.log('one user disconnected '+socket.id);
		delete(idClientIpAddress[socket.id]);
		delete(idRestaurantIpAddress[socket.id]);
	})
})

http.listen(3000,function(){
	console.log('server listening on port 3000');
});

function prettyJSON(obj) {
    console.log(util.inspect(obj, false, null));
}

function addObjectToClientIpAddress(data, socket){
	var restaurantNameClientId = data.split(",");
	var restaurantName = restaurantNameClientId[0].split(":")[1];
	var clientId = restaurantNameClientId[1].split(":")[1];
	idClientIpAddress[socket.id] = 
	{
		restaurantSocket : "",
		restaurantName :restaurantName,
		clientSocket : socket,
		clientId : clientId,
	 	ipAddress : socket.request.connection.remoteAddress,
	 	restaurantIpAddress : "",
	 	isFree : true
	};
}

function addObjectToRestaurantIpAddress(data,socket){
	idRestaurantIpAddress[socket.id] = 
	{
		restaurantId : data,
		restaurantSocket : socket,
		clientSocket : "",
		ipAddress : socket.request.connection.remoteAddress,
		ipClientAddress : "",
		restaurantName : "",
		isFree : true
	};
}

function addClientToRestaurant(socket){
	for(var client in idClientIpAddress){
		if(idClientIpAddress[client].isFree){
			idRestaurantIpAddress[socket.id].ipClientAddress = idClientIpAddress[client].ipAddress;
			idRestaurantIpAddress[socket.id].isFree = true;

			idClientIpAddress[client].restaurantIpAddress = socket.request.connection.remoteAddress;

			idClientIpAddress[client].restaurantSocket = socket;
			idRestaurantIpAddress[socket.id].clientSocket = idClientIpAddress[client].clientSocket;
			return idClientIpAddress[client];
		}
	}

	return "";
}