// ############################
// ############################
// ########### UI ############
// ############################
// ############################


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