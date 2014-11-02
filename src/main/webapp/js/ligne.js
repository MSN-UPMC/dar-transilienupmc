// ############################
// ############################
// ########### LIGNE ##########
// ############################
// ############################


function initLigne(){


	// Linker les lignes à leurs images respectifs
	environnement.lignes.icones = [];
	environnement.lignes.icones["a"]="img/rera.png";
	environnement.lignes.icones["b"]="img/rerb.png";
	environnement.lignes.icones["c"]="img/rerc.png";
	environnement.lignes.icones["d"]="img/rerd.png";
	environnement.lignes.icones["e"]="img/rere.png";
	environnement.lignes.icones["u"]="img/trainu.png";
	environnement.lignes.icones["r"]="img/trainr.png";
	environnement.lignes.icones["p"]="img/trainp.png";
	environnement.lignes.icones["n"]="img/trainn.png";
	environnement.lignes.icones["l"]="img/trainl.png";
	environnement.lignes.icones["k"]="img/traink.png";
	environnement.lignes.icones["j"]="img/trainj.png";
	environnement.lignes.icones["h"]="img/trainh.png";

	// Linker les lignes à leurs couleurs respectifs
	environnement.lignes.couleurs = [];
	environnement.lignes.couleurs["a"]="#D1302F";
	environnement.lignes.couleurs["b"]="#427DBD";
	environnement.lignes.couleurs["c"]="#FCD946";
	environnement.lignes.couleurs["d"]="#5E9620";
	environnement.lignes.couleurs["e"]="#BD76A1";
	environnement.lignes.couleurs["u"]="#D60058";
	environnement.lignes.couleurs["r"]="#E4B4D1";
	environnement.lignes.couleurs["p"]="#F0B600";
	environnement.lignes.couleurs["n"]="#00A092";
	environnement.lignes.couleurs["l"]="#7584BC";
	environnement.lignes.couleurs["k"]="#C7B300";
	environnement.lignes.couleurs["j"]="#CDCD00";
	environnement.lignes.couleurs["h"]="#7B4339";	
	
	
	// Message d'attente pour le client - chargement des données des lignes
	$("#modalAlertDiv div[class='modal-body']").text("Chargement des lignes");

	// appel ajax pour recuperer les lignes
	$.ajaxSetup({async: false});
	$.get(environnement.routes["getLignes"], function(data) {
		for(i in data.items){
			ligne = data.items[i];
			// Ajoute les gares null - A verifier
			environnement.lignes[ligne.nom] = new Ligne(ligne.nom,ligne.gares);			
		}
	});
	
	$.get(environnement.routes["statistique"], function(data) {

		environnement.statistique = {}

		environnement.statistique.alheure = parseFloat(data.items[0]);
		environnement.statistique.retard = parseFloat(data.items[1]);
		environnement.statistique.annule = parseFloat(data.items[2]);

	});	
	
	$.ajaxSetup({async: true});


}


