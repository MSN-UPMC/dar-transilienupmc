// ############################
// ############################
// ########### Tools ##########
// ############################
// ############################

/**
 * Création de l'environnement
 */
function initDataModel() {
	environnement = {};
	environnement.gares = new Array();
	environnement.lignes = new Array();
	
	environnement.host = "https://supple-flux-704.appspot.com/_ah/api/gares";
	environnement.routes = new Array();
	environnement.routes["getGaresList"] = environnement.host+"/v1/list";
	
	// Message d'attente pour le client - chargement des données des gares 
	$("#modalAlertDiv div[class='modal-body']").text("Chargement des gares");
	// Appel Ajax synchrone - données critique
	$.ajaxSetup({async: false});
	$.get(environnement.routes["getGaresList"], function(data) {
		for(i in data.items){	
			gare = data.items[i];
			environnement.gares[gare.codeUIC]= new Gare(gare.nom,parseInt(gare.codeUIC),parseFloat(gare.longitude),parseFloat(gare.lattitude));			
		}
	});
	$.ajaxSetup({async: true});
}

/**
 * Gestion des erreurs
 */
function lancerException(message){
    throw new Error(message);

} 
 
// ############################
// ############################
// ########### GARE ###########
// ############################
// ############################
/**
 * Créer un objet Gare
 * 
 * @param nom
 *            le nom de la gare
 * @param codeUIC
 *            le codeUIC de la gare
 * @param longitude
 *            la longitude de la gare
 * @param latitude
 *            la latitude de la gare
 * @return un objet gare
 */
function Gare(nom, codeUIC, longitude, latitude) {

	// Si l'environnement n'est pas crée alors lancé une exception
	if(environnement === undefined){
		lancerException("L'environnement n'est pas initialisé.");
	}
	// Ajout du typage des parametres sinon déclanchement d'une exception
	if( (typeof nom != "string") || 
		(typeof codeUIC != "number") ||  
		(typeof longitude != "number") ||
		(typeof latitude != "number") ){
		lancerException("Les paramètres ne correspondent pas au typage de la fonction Gare");
	}
	// Si environnement gare n'est pas initialisé, on déclanche une exception
	if (typeof environnement.gares === "undefined"){
		lancerException("L'environnement Gare n'est pas initialisé");
	}

	this.nom = nom;
	this.codeUIC = codeUIC;
	this.longitude = longitude;
	this.latitude = latitude;

	// Si la gare n'existe pas dans l'environnement Gare
	if ( typeof environnement.gares[codeUIC] === "undefined")
		environnement.gares[codeUIC] = this;
}

/**
 * Obtient le code HTML d'une gare
 * 
 * @param gare
 *            la gare a afficher en HTML
 * @returns {String} le HTML correspondant à la gare
 */
Gare.prototype.getHTML = function(gare) {

	var s = '<div class="gare">';
	// Encodage HTML : &#8594; equivalent à  =>
	s +=  this.nom + " (" +this.codeUIC+") &#8594; "+" ["+this.latitude+";"+this.longitude+"]";
	s += '</div>';

	return s;
}

// TODO modifier / supprimer
/**
 * Un exemple de traitement de réponse
 */
Gare.traiteReponse = function(json) {
	var s = "";
	for (var i = 0; i < json.length; i++) {
		var obj = new Gare(json[i].nom, json[i].codeUIC, json[i].longitude,
				json[i].latitude);
		s += obj.getHTML(obj);
	}
	$("#listeGare").html(s);
}

// ############################
// ############################
// ########### LIGNE ##########
// ############################
// ############################
/**
 * Créer une ligne
 * 
 * @param nom
 *            le nom de la ligne
 * @param gares
 *            le tableau des gares de la ligne
 * @returns un objet ligne
 */
function Ligne(nom, gares) {

	// Si l'environnement n'est pas crée alors lancé une exception
	if(environnement === undefined){
		lancerException("Environnement non initialisé.");
	}
	// Ajout du typage des parametres sinon déclanchement d'une exception
	if( (typeof nom != "string") || 
		(typeof gares != "object") ){
		lancerException("Les paramètres ne correspondent pas au typage de la fonction Ligne");
	}
	// Si environnement ligne n'est pas initialisé, on déclanche une exception
	if (typeof environnement.ligne === "undefined"){
		lancerException("L'environnement Ligne n'est pas initialisé");
	}

	this.nom = nom;
	this.gares = gares;

	// Si la ligne n'existe pas dans l'environnement Ligne
	if (typeof environnement.ligne[nom] === "undefined")
		environnement.ligne[nom] = this;
}

/**
 * Obtient le code HTML d'une ligne
 * 
 * @param ligne
 *            la ligne en question
 * @returns {String} le code HTML de la ligne
 */
Ligne.prototype.getHTML = function(ligne) {
	var s = '<div class="ligne">';
	s += '<h3>' + this.nom + '</h3>';
	s += '<ul>';
	for (var i = 0; i < this.gares.length; i++) {
		s += '<li>' + gares[i].getHTML(gares[i]) + '</li>';
	}
	s += '</ul>';
	s += '</div>';
	return s;
}

// TODO modifier / supprimer
/**
 * Un exemple de traitement de réponse
 */
Ligne.traiteReponse = function(json) {
	var s = "";
	for (var i = 0; i < json.length; i++) {
		var gares = new Array();
		for (var j = 0; j < json[i].gares.length; j++) {
			gares[j] = new Gare(json[i].gares[j].nom, json[i].gares[j].codeUIC,
					json[i].gares[j].longitude, json[i].gares[j].latitude);
		}

		var obj = new Ligne(json[i].nom, gares);
		s += obj.getHTML(obj);
	}
	$("#listeLigne").html(s);
}


// ############################
// ############################
// ########### MAP ############
// ############################
// ############################

/**
 * Création de l'environnement
 */
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
		marker = L.circleMarker([gare.latitude,gare.longitude],{opacity :'1'}).addTo(environnement.map.instance);
		environnement.map.markers[gare.codeUIC] = marker; 
		// On ajoute les informations info bulle à cette gare
		marker.bindPopup("<b>"+gare.nom+"</b><br>Code UIC : "+gare.codeUIC+".");
			
		/* Maj du select dans la section 'Informations sur une gare' */
		$("#collapseTwo select").append("<option data-uic="+gare.codeUIC+">"+gare.nom+"</option>");
	}
}


function initUIEvent(){
	// Attendre la fin du charement de la page et creation du DOM
	$(function() {
		// Message d'attente pour le client - initiation evenmenet ui + injection donnée
		$("#modalAlertDiv div[class='modal-body']").text("Initialisation de l'interface graphique");
		for(i in environnement.gares){
			/* Maj du select dans la section 'Informations sur une gare' */
			$("#collapseTwo select").append("<option data-uic="+gare.codeUIC+">"+gare.nom+"</option>");
		}
		// Evenement sur la recherche d'info sur une gare - deplace la map sur la gare et ouvre la popup d'info
		$('#collapseTwo form button[type="submit"]').click(function(e) {
		  e.preventDefault();
		  e.stopPropagation();
		  
		  codeUIC = $('#collapseTwo form option:selected').attr('data-uic');
		  environnement.map.instance.setView(environnement.map.markers[codeUIC].getLatLng());
		  environnement.map.instance.setZoom(15);
		  environnement.map.markers[codeUIC].openPopup();
		});			
	});

}

