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

		// Evenement sur la recherche d'itineraire - trace les lignes sur la map
		$('#collapseOne form button[type="submit"]').click(function(e) {
		  e.preventDefault();
		  e.stopPropagation();
		  
		  
		  if($('#adresseDepart').typeahead('val')=="" || $('#adresseDestination').typeahead('val')==""){
				$('div[class="alert"]').remove();
				$('#collapseOne form').after('<div class="alert alert-danger" role="alert">La gare départ ou de destination n\'est pas renseignée.</div>');		
		  }
		  
		  
		  if(environnement.map.itineraire.isDisplay){
			for(j in polylineTab){
				environnement.map.instance.removeLayer(environnement.map.itineraire.polylineArray[j]);
			}
			environnement.map.itineraire.isDisplay=false;
		  }
		  
		  var codeUICDepart=-1, codeUICDestination=-1;
		  var fini = 0;
		  
		  for(i in environnement.gares){
			if(environnement.gares[i].nom === $('#adresseDepart').typeahead('val')){
				codeUICDepart = environnement.gares[i].codeUIC;
				fini++;
			}
			if(environnement.gares[i].nom === $('#adresseDestination').typeahead('val')){
				codeUICDestination = environnement.gares[i].codeUIC;			
				fini++;
			}
			if(fini>1){
				break;
			}
		  }

		  
		  if(codeUICDepart==-1 || codeUICDestination==-1){
				$('div[class="alert"]').remove();
				$('#collapseOne form').after('<div class="alert alert-danger" role="alert">La gare de départ ou de detination n\'est pas connue.</div>');		
		  }
			
			$.get(environnement.routes["itineraire"]+"?departCodeUIC="+codeUICDepart+"&destinationCodeUIC="+codeUICDestination, function(data) {
				if(data.lignes){
					// objet qui simule le layer ligne 
					polylineTab = [];
					// objet qui simule un ensemble de point
					polyline = [];
					// Tracer de la ligne
					var ligneTracer;
						
					for(i in data.lignes){
					
						// gare courante 1 et 2
						gare1 = environnement.gares[data.gares[i].codeUIC];
						gare2 = environnement.gares[data.gares[parseInt(i)+1].codeUIC];
						
						// objet ligne qui lie la gare 1 et 2
						polyline.push(L.latLng(gare1.latitude,gare1.longitude),L.latLng(gare2.latitude,gare2.longitude));
						// je creer un ligne en reliant tout les points
						ligneTracer = L.polyline(polyline, {color: environnement.lignes.couleurs[data.lignes[i]], opacity : '1',weight: '2'});						
						
						polyline = [];
						polylineTab.push(ligneTracer);
						console.log(polylineTab);
						
					}
					
					
					environnement.map.itineraire.isDisplay=true;
					environnement.map.itineraire.polylineArray = polylineTab;
					
					for(j in polylineTab){
						polylineTab[j].addTo(environnement.map.instance);
					}
							
				}
				else{
					$('div[class="alert"]').remove();
					$('#collapseOne form').after('<div class="alert alert-danger" role="alert">Aucun itinéraire est disponible de '+$('#adresseDepart').typeahead('val')+' à '+$('#adresseDestination').typeahead('val')+'</div>');
				}
			
			
			});
	
		});	

		
		// Evenement sur la recherche d'info sur une gare - deplace la map sur la gare et ouvre la popup d'info
		$('#collapseTwo form button[type="submit"]').click(function(e) {
		  e.preventDefault();
		  e.stopPropagation();
		  
		  
		  if(environnement.map.itineraire.isDisplay){
			for(j in polylineTab){
				environnement.map.instance.removeLayer(environnement.map.itineraire.polylineArray[j]);
			}
			environnement.map.itineraire.isDisplay=false;
		  }
		  
		  
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