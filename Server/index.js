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
		var clientIpAddress = addClientToRestaurant(socket);
		prettyJSON(idRestaurantIpAddress);
		socket.emit('client',{"client": clientIpAddress});
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
		delete(idRestaurantIpAddress[socket.id]);
		prettyJSON(idClientIpAddress);
		prettyJSON(idRestaurantIpAddress);
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
		restaurantName :restaurantName,
		clientId : clientId,
	 	ipAddress : socket.request.connection.remoteAddress,
	 	isFree : true
	};
}

function addObjectToRestaurantIpAddress(data,socket){
	idRestaurantIpAddress[socket.id] = 
	{
		restaurantId : data,
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
			idRestaurantIpAddress[socket.id].isFree = false;

			return idClientIpAddress[client].ipAddress;
		}
	}

	return "";
}