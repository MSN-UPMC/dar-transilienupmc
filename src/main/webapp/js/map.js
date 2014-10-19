// ############################
// ############################
// ########### MAP ############
// ############################
// ############################


function initMap() {
	// Si l'environnement n'est pas crée alors lancé une exception
	if(environnement === undefined){
		lancerException("L'environnement n'est pas initialisé.");
	}
	
	// Message d'attente pour le client - creation de la carte
	$("#modalAlertDiv div[class='modal-body']").text("Création de la carte");

	
	// Mettre le map a la coordonnee au demarrage 
	// Sur Chatelet Paris en position 48.858755;48.858755
	// Avec un zoom de niveau 13
	var map = L.map('map').setView([48.858755, 2.347531], 13);
	

	// Utilisation dun title(cartographie) mapbox avec ma cle session 
	// Zoom de niveau 18 maximum autoriser
	L.tileLayer('https://{s}.tiles.mapbox.com/v3/magstellon.joaljn60/{z}/{x}/{y}.png', {
		maxZoom: 18,
		minZoom: 9
	}).addTo(map);
	

	environnement.map = {};
	environnement.map.instance = map;
	environnement.map.markers = new Array();
	

	// Message d'attente pour le client - placement des gares sur carte
	$("#modalAlertDiv div[class='modal-body']").text("Génération de la carte");
	for(i in environnement.gares){
		/* Maj de la MAP */
		// Creation dune variable temporaire pour stocker une gare
		gare = environnement.gares[i];
		// On crée un point sur la carte
		marker = L.circleMarker([gare.latitude,gare.longitude],{opacity :'1'}).setRadius(8).addTo(environnement.map.instance);
		environnement.map.markers[gare.codeUIC] = marker; 
		// On ajoute les informations info bulle à cette gare
		marker.bindPopup("<b>"+gare.nom+"</b><br>Code UIC : "+gare.codeUIC+".");
			
		/* Maj du select dans la section 'Informations sur une gare' */
		$("#collapseTwo select").append("<option data-uic="+gare.codeUIC+">"+gare.nom+"</option>");
	}
}