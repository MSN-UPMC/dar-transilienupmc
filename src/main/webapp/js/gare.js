// ############################
// ############################
// ########### Gare ##########
// ############################
// ############################

function initGare(){

	// Message d'attente pour le client - chargement des données des gares 
	$("#modalAlertDiv div[class='modal-body']").text("Chargement des gares");
	// Appel Ajax synchrone - données critique
	$.ajaxSetup({async: false});
	$.get(environnement.routes["getGaresList"], function(data) {
		for(i in data.items){	
			gare = data.items[i];
			
			if(gare.nom == "PARIS MONTPARNASSE"){
				console.log(gare.codeUIC);
			}
			environnement.gares[gare.codeUIC]= new Gare(gare.nom,parseInt(gare.codeUIC),parseFloat(gare.longitude),parseFloat(gare.latitude));			
		}
	});
	$.ajaxSetup({async: true});
	
}