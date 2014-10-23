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
	
	initGareOnMap();
	
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
			marker = L.circleMarker([gare.latitude,gare.longitude],{opacity :'1',color: '#000000', fillColor :'#000000'}).setRadius(8).addTo(environnement.map.instance);
		}
		// Si il y a qu'une seule ligne => couleur de la ligne 
		else{
			couleur = environnement.lignes.couleurs[lignes[0]];
			marker = L.circleMarker([gare.latitude,gare.longitude],{opacity :'1', fillOpacity:'0.5',color: couleur, fillColor :couleur}).setRadius(8).addTo(environnement.map.instance);		
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
		//lancerException("La gare avec le codeUIC "+codeUICInput+" n'est liée à aucune ligne dans la fonction getLignesOfAGare");
	}
	
	return output;

}


function getLignesOfAGareToHTML(lignesOfAGareArray){

	if( !(lignesOfAGareArray instanceof Array) ){
		//lancerException("Les paramètres ne correspondent pas au typage de la fonction getLignesOfAGare");
	}
	if( lignesOfAGareArray.length == 0){
		//lancerException("Le paramètre de la fonction getLignesOfAGareToHTML ne contient aucun élement");
	}

	var output = '';
		
	for(i in lignesOfAGareArray){
		output += "<img src='"+environnement.lignes.icones[lignesOfAGareArray[i]]+"'/>&nbsp;";
	}	
		
	return output;

}


function initLigneOnMap(){

// Faire tracage des lignes 

}