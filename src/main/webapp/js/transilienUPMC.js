/**
 * Import de bibliotèque; externes
 */
<script src="js/mustache.js"></script>

/**
 * Création de l'environnement
 */
function main() {
	environnement = {};
	environnement.gare = new Array();
	environnement.ligne = new Array();
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
	this.nom = nom;
	this.codeUIC = codeUIC;
	this.longitude = longitude;
	this.latitude = latitude;

	if (environnement.gare != undefined)
		environnement.gare[codeUIC] = this;
}

/**
 * Obtient le code HTML d'une gare
 * 
 * @param gare
 *            la gare a afficher en HTML
 * @returns {String} le HTML correspondant à la gare
 */
Gare.prototype.getHTML = function(gare) {

	var gareHTML = {
		html : {
			balise : "div",
			nomClasse : "gare"
		}
		gare : {
			nom : this.nom,
			codeUIC : this.codeUIC,
			latitude : this.latitude,
			longitude : this.longitude
		}
	};
	var template = "<{{html.balise}} class='{{html.nomClasse}}'>{{gare.nom}} ({{gare.codeUIC}}) &#8594; [{{gare.latitude}};{{gare.longitude}}]</{{html.balise}}>";
	return Mustache.to_html(template, gare);
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
	this.nom = nom;
	this.gares = gares;

	if (environnement.ligne != undefined)
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