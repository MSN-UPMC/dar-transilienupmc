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
	var baseMap = L.tileLayer('https://{s}.tiles.mapbox.com/v3/magstellon.joaljn60/{z}/{x}/{y}.png', {
		maxZoom: 18,
		minZoom: 9
	}).addTo(map);
	
	var baseMapControl = {
		"Map": baseMap
	};

	environnement.map = {};
	environnement.map.instance = map;
	environnement.map.markers = new Array();
	
	initGareOnMap();
	initLigneOnMap(baseMapControl);
	
}


function initGareOnMap(){

	// Message d'attente pour le client - placement des gares sur carte
	$("#modalAlertDiv div[class='modal-body']").text("Génération des gares sur la carte");
	for(i in environnement.gares){
		/* Maj de la MAP */
		// Creation dune variable temporaire pour stocker une gare
		gare = environnement.gares[i];
		// tableau des noms de lignes d'une gare
		lignes = getLignesOfAGare(gare.codeUIC);
		
		// On crée un point sur la carte
		// Si il y a plusieurs lignes sur une gare => rond noir
		if(lignes.length>1){
			marker = L.circleMarker([gare.latitude,gare.longitude],{opacity :'1',color: '#000000', fillColor :'#000000'}).setRadius(8).bindLabel(gare.nom, {direction :'auto'}).addTo(environnement.map.instance);
		}
		// Si il y a qu'une seule ligne => couleur de la ligne 
		else{
			couleur = environnement.lignes.couleurs[lignes[0]];
			marker = L.circleMarker([gare.latitude,gare.longitude],{opacity :'1', fillOpacity:'0.5',color: couleur, fillColor :couleur}).setRadius(8).bindLabel(gare.nom, {direction :'auto'} ).addTo(environnement.map.instance);		
		}

		
		// on ajouter notre objet de point dans lenvironnement
		environnement.map.markers[gare.codeUIC] = marker; 
		// On ajoute les informations info bulle à cette gare
		marker.bindPopup("<b>"+gare.nom+"</b>\
						  </br></br>\
						  <div style='text-align:center;'>\
							"+getLignesOfAGareToHTML(lignes)+"\
						  </div>\
						");
			
	}


}

function getLignesOfAGare(codeUICInput){

	if( (typeof codeUICInput != "number") ){
		lancerException("Les paramètres ne correspondent pas au typage de la fonction getLignesOfAGare");
	}

	var output = [];
		
	for(i in environnement.lignes){	
		ligne = environnement.lignes[i];
		for(j in ligne.gares){
			codeUIC = ligne.gares[j];
			if(codeUIC === codeUICInput){
				output.push(ligne.nom);
			}
		}
	}
	
	if( output.length == 0){
		lancerException("La gare avec le codeUIC "+codeUICInput+" n'est liée à aucune ligne dans la fonction getLignesOfAGare");
	}
	
	return output;

}


function getLignesOfAGareToHTML(lignesOfAGareArray){

	if( !(lignesOfAGareArray instanceof Array) ){
		lancerException("Les paramètres ne correspondent pas au typage de la fonction getLignesOfAGare");
	}
	if( lignesOfAGareArray.length == 0){
		lancerException("Le paramètre de la fonction getLignesOfAGareToHTML ne contient aucun élement");
	}

	var output = '';
		
	for(i in lignesOfAGareArray){
		output += "<img src='"+environnement.lignes.icones[lignesOfAGareArray[i]]+"'/>&nbsp;";
	}	
		
	return output;

}


function initLigneOnMap(baseMapControl){

	/*
	// Recup toutes les lignes
	for(i in environnement.lignes){
		
		// recup la ligne i de la liste
		ligne = environnement.lignes[i];

		// appell ajax vers les ligne oriente
		$.get(environnement.routes["getLignes"]+"?nom+de+la+ligne="+ligne.nom, function(data) {
		
		
			// Liste des points 2d quil faut relier par une ligne
			polyline = [];

			// pour toute les gare de la ligne
			for(i in data.items){
				gare = data.items[i];
				// recuperation des coordone de la gare a relier par la ligne
				polyline.push(L.latLng(gare.latitude,gare.longitude));		
			}
			// ajout à la map de la ligne
			L.polyline(polyline, {color: environnement.lignes.couleurs[ligne.nom], opacity : '1',weight: '2'}).addTo(environnement.map.instance);
		});		
		
	}

	*/
	
	$.get(environnement.routes["getLignes"], function(data) {
	
		// ensemble du layer qui sera affiche au dessus de la map
		var overlayMaps = {};
		
		// fetch de la ligne de la req. ajax
		for(i in data.items){
		
			// ligne courante
			ligne = data.items[i];
			// objet qui simule un ensemble de point
			polyline = [];
			// objet qui simule le layer ligne 
			layerGroup = [];
			
			for( j in ligne.gares){
				
				codeUIC = ligne.gares[j];
				
				// Si jarrive a la fin du tracer de la ligne en cours
				if(codeUIC === null){
					// je creer un ligne en reliant tout les points
					var ligneTracer = L.polyline(polyline, {color: environnement.lignes.couleurs[ligne.nom], opacity : '1',weight: '2'});
					// enregistrement de cette ligne dans le layer au dessus de la map
					layerGroup.push(ligneTracer);
					// reset de la liste de point
					polyline = [];
					continue;
				}
				else{
					// recupere la structure de gare egal a UIC
					gare = environnement.gares[codeUIC];
					// on rajoute la gare dans la liste de point a relier
					polyline.push(L.latLng(gare.latitude,gare.longitude));						
				}
			}
			
			// On regroupe toutes les lignes créer dans un objet regroupe la ligne courante
			var ligneLayer = L.layerGroup(layerGroup);
			// on créer un controler sur la map
			overlayMaps["Ligne "+ligne.nom.toUpperCase()] = ligneLayer ;

		}

		// on rajouter tout les controller des ligne sur la map
		L.control.layers(baseMapControl,overlayMaps).setPosition('bottomleft').addTo(environnement.map.instance);

		
		});

}