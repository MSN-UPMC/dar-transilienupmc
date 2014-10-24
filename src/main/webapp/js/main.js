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
	
	environnement.host = "https://transilien-upmc.appspot.com/_ah/api";
	environnement.routes = new Array();
	environnement.routes["getGaresList"] = environnement.host+"/gares/v1/getGares";
	environnement.routes["getLignes"] = environnement.host+"/lignes/v1/getLignes";
	environnement.routes["ligneOriente"] = environnement.host+"/lignes/v1/ligneOriente";

	
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
	if (typeof environnement.lignes === "undefined"){
		lancerException("L'environnement Ligne n'est pas initialisé");
	}

	this.nom = nom;
	this.gares = gares;

	// Si la ligne n'existe pas dans l'environnement Ligne
	if (typeof environnement.lignes[nom] === "undefined")
		environnement.lignes[nom] = this;
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


