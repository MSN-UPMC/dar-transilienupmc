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
		
		// Lister les noms des gares dans un tableau
		var gareTabs = [];
		for(i in environnement.gares){
			gareTabs.push(environnement.gares[i].nom);
		}

		// moteur autocompletion : robust, flexible, and offers advanced functionalities 
		// such as prefetching, intelligent caching, fast lookups, and backfilling with remote data
		var bloodhound = new Bloodhound({
		  datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
		  queryTokenizer: Bloodhound.tokenizers.whitespace,
		  local: $.map(gareTabs, function(nom) { return { value: nom }; })
		});
		bloodhound.initialize();

		// bind les inputs de l'ui avec bloodhound
		$('#rechercherGare').typeahead({
		  hint: true,
		  highlight: true,
		  minLength: 1
		},
		{
		  name: 'bloodhound',
		  displayKey: 'value',
		  source: bloodhound.ttAdapter()
		 
		});
		
		$('#adresseDepart').typeahead({
		  hint: true,
		  highlight: true,
		  minLength: 1
		},
		{
		  name: 'bloodhound',
		  displayKey: 'value',
		  source: bloodhound.ttAdapter()
		 
		});
		
		$('#adresseDestination').typeahead({
		  hint: true,
		  highlight: true,
		  minLength: 1
		},
		{
		  name: 'bloodhound',
		  displayKey: 'value',
		  source: bloodhound.ttAdapter()
		 
		});
		
		// Evenement sur la recherche d'info sur une gare - deplace la map sur la gare et ouvre la popup d'info
		$('#collapseTwo form button[type="submit"]').click(function(e) {
		  e.preventDefault();
		  e.stopPropagation();
		  
		  var codeUIC;
		  for(i in environnement.gares){
			if(environnement.gares[i].nom === $('#rechercherGare').typeahead('val')){
				codeUIC = environnement.gares[i].codeUIC;
				break;
			}
		  }

		  
		  environnement.map.instance.setView(environnement.map.markers[codeUIC].getLatLng());
		  environnement.map.instance.setZoom(15);
		  environnement.map.markers[codeUIC].fire('click');
		});			
	});

}