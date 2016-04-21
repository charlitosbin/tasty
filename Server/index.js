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
		var restaurant = addRestaurantToClient(socket);
		var client = addClientToRestaurant(socket.id, restaurant);

		socket.emit('restaurant',{"restaurant" : restaurant.ipAddress});

		var sockets = io.sockets.sockets;
		sockets.forEach(function(sock){
			if(sock.id == client.restaurantSocketId){
				sock.emit('client',{"client" : client.ipAddress+","+restaurant.restaurantName});
			}
		})
		
		prettyJSON(idClientIpAddress);
		prettyJSON(idRestaurantIpAddress);


	})

	socket.on("init_restaurant",function(androidId){
		console.log("init_restaurant>>>");
		addObjectToRestaurantIpAddress(androidId, socket);
		prettyJSON(idRestaurantIpAddress);
	})

	socket.on('message',function(data){
		var sockets = io.sockets.sockets;
		console.log("sendingMessage>>>");
	})

	socket.on('disconnect',function(){
		console.log('one user disconnected '+socket.id);

		var restaurant = idRestaurantIpAddress[socket.id];
		var client = idClientIpAddress[socket.id];
		var sockets = io.sockets.sockets;
		
		if(typeof client !== 'undefined'){
			console.log("client");
			console.log("hola " + client.clientSocketId);
			var restaurantSocketId = removeRestaurantFromClient(client.clientSocketId);
			console.log(restaurantSocketId);
			sendRestaurantConfirmation(sockets, restaurantSocketId);

		}

		if(typeof restaurant !== 'undefined'){
			console.log('restaurant');
			var clientSocketId = removeClientFromRestaurant(socket.id);
			console.log(clientSocketId);
			sendClientConfirmation(sockets, clientSocketId);
		}

		delete idRestaurantIpAddress[socket.id];
		delete idClientIpAddress[socket.id];
	})
})

http.listen(3000,function(){
	console.log('server listening on port 3000');
});


function sendRestaurantConfirmation(sockets, restaurantSocketId){
	console.log("entro a confirmacion");
	sockets.forEach(function(sock){
			if(sock.id == restaurantSocketId){
				console.log("confirmacion>>>" +  restaurantSocketId);
				sock.emit('client_logout',{"client_logout" : getIpAddressFromSocket(sock)});
			}
		})
}

function sendClientConfirmation(sockets, clientSocketId){
	console.log("entro a confirmacion");
	sockets.forEach(function(sock){
		if(sock.id == clientSocketId){
			console.log("confirmacion>>>" + clientSocketId);
			sock.emit("restaurant_logout", {"restaurant_logout" : getIpAddressFromSocket(sock)});
		}
	})
}

function addObjectToClientIpAddress(data, socket){
	var restaurantNameClientId = data.split(",");
	var restaurantName = restaurantNameClientId[0].split(":")[1];
	var clientId = restaurantNameClientId[1].split(":")[1];
	idClientIpAddress[socket.id] = 
	{
		clientSocketId : socket.id,
		restaurantSocketId : "",
		restaurantName :restaurantName,
		clientId : clientId,
	 	ipAddress : getIpAddressFromSocket(socket),
	 	restaurantIpAddress : "",
	 	isFree : true
	};
}

function addObjectToRestaurantIpAddress(data,socket){
	idRestaurantIpAddress[socket.id] = 
	{
		restaurantId : data,
		restaurantSocketId : socket.id,
		clientSocketId : "",
		ipAddress : getIpAddressFromSocket(socket),
		ipClientAddress : "",
		restaurantName : "",
		isFree : true
	};
}

function removeRestaurantFromClient(clientSocketId){
	console.log("clientSocketId >>>> " + clientSocketId);
	var client = idClientIpAddress[clientSocketId];

	var restaurant = idRestaurantIpAddress[client.restaurantSocketId];
	console.log("rs>>> " + restaurant.restaurantSocketId);
	restaurant.clientSocketId = "";
	restaurant.ipClientAddress = "";
	restaurant.restaurantName = "";
	restaurant.isFree = true;

	return restaurant.restaurantSocketId;

}


function removeClientFromRestaurant(restaurantSocketId){
	console.log("restaurantSocketId" + restaurantSocketId);
	var restaurant = idRestaurantIpAddress[restaurantSocketId];
	var client = idClientIpAddress[restaurant.clientSocketId];

	client.restaurantSocketId = "";
	client.restaurantName = "";
	client.restaurantIpAddress = "";
	client.isFree = true;

	return client.clientSocketId;
}

function addRestaurantToClient(clientSocket){
	var client = idClientIpAddress[clientSocket.id];
	if(client !== undefined || client !== null){
		for(var restaurantSocketId in idRestaurantIpAddress){
			var restaurant = idRestaurantIpAddress[restaurantSocketId];
			if(restaurant.isFree){
				restaurant.restaurantName = client.restaurantName;
				restaurant.clientSocketId = clientSocket.id;
				restaurant.ipClientAddress = getIpAddressFromSocket(clientSocket);
				client.restaurantIpAddress = restaurant.ipAddress;

				return restaurant;
			}
		}
	}

	return "";
}

function addClientToRestaurant(clientSocketId, restaurant){

	var client = idClientIpAddress[clientSocketId];
	client.restaurantSocketId = restaurant.restaurantSocketId;
	restaurant.clientSocketId = clientSocketId;
	restaurant.ipClientAddress = client.ipAddress;
	restaurant.restaurantName = client.restaurantName;
	
	return client;
}

function prettyJSON(obj) {
    console.log(util.inspect(obj, false, null));
}

function getIpAddressFromSocket(socket){
	return socket.request.connection.remoteAddress;
}
