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
		var isRestaurant = isSocketRestaurant(socket.id);

		if(isRestaurant){
			console.log("Restaurant sending message >>>");
			var toSocket = idRestaurantIpAddress[socket.id].clientSocketId;
			sendingMessage(data,sockets, toSocket);
		}else{
			console.log("Client sending message >>>");
			var toSocket = idClientIpAddress[socket.id].restaurantSocketId;
			sendingMessage(data, sockets, toSocket);
		}
		
	})

	socket.on('disconnect',function(){
		console.log('one user disconnected '+socket.id);

		var restaurant = idRestaurantIpAddress[socket.id];
		var client = idClientIpAddress[socket.id];
		var isRestaurant = isSocketRestaurant(socket.id);

		var sockets = io.sockets.sockets;
		
		try{
			if(!isRestaurant){
				console.log("client");
				var restaurantSocketIdAndIpAddress = removeRestaurantFromClient(client.clientSocketId);
				console.log(restaurantSocketIdAndIpAddress.clientIpAddress);
				sendRestaurantConfirmation(sockets, restaurantSocketIdAndIpAddress.restaurantSocketId, restaurantSocketIdAndIpAddress.clientIpAddress);
			}
			if(isRestaurant){
				console.log('restaurant');
				var clientSocketAndIpAddress = removeClientFromRestaurant(socket.id);
				console.log(clientSocketAndIpAddress.clientSocketId);
				sendClientConfirmation(sockets, clientSocketAndIpAddress.clientSocketId, clientSocketAndIpAddress.restaurantIpAddress);
			}
		}catch(err){
			console.log(err);
		}

		delete idRestaurantIpAddress[socket.id];
		delete idClientIpAddress[socket.id];
	})
})

http.listen(3000,function(){
	console.log('server listening on port 3000');
});


function sendingMessage(data, sockets, socketId){
	sockets.forEach(function(sock){
		if(sock.id === socketId){
			sock.emit('message',{message:data});
			return;
		}
	});
}


function isSocketRestaurant(socketId){
	var result = false;

	var restaurant = idRestaurantIpAddress[socketId];
	var client = idClientIpAddress[socketId];

	if(typeof client !== 'undefined')
		result = false;
	else if(typeof restaurant !== 'undefined')
		result = true;

	return result;
}



function sendRestaurantConfirmation(sockets, restaurantSocketId, clientIpAddress){
	console.log("entro a confirmacion");
	sockets.forEach(function(sock){
			if(sock.id == restaurantSocketId){
				console.log("confirmacion>>>" +  restaurantSocketId);
				sock.emit('client_logout',{"client_logout" : clientIpAddress});
			}
		})
}

function sendClientConfirmation(sockets, clientSocketId, restaurantIpAddress){
	console.log("entro a confirmacion");
	sockets.forEach(function(sock){
		if(sock.id == clientSocketId){
			console.log("confirmacion>>>" + clientSocketId);
			sock.emit("restaurant_logout", {"restaurant_logout" : restaurantIpAddress});
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
	var clientIpAddress = client.ipAddress;
	var restaurantSocketId = restaurant.restaurantSocketId;

	console.log("rs>>> " + restaurantSocketId);
	restaurant.clientSocketId = "";
	restaurant.ipClientAddress = "";
	restaurant.restaurantName = "";
	restaurant.isFree = true;

	console.log("ip>>>" + clientIpAddress);
	return {"restaurantSocketId" : restaurantSocketId, "clientIpAddress" : clientIpAddress};

}


function removeClientFromRestaurant(restaurantSocketId){
	console.log("restaurantSocketId" + restaurantSocketId);
	var restaurant = idRestaurantIpAddress[restaurantSocketId];
	var restaurantIpAddress = restaurant.ipAddress;

	var client = idClientIpAddress[restaurant.clientSocketId];

	client.restaurantSocketId = "";
	client.restaurantName = "";
	client.restaurantIpAddress = "";
	client.isFree = true;

	return {"clientSocketId" : client.clientSocketId, "restaurantIpAddress" : restaurantIpAddress};
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
