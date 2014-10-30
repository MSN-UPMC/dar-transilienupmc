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
		
		// Evenement pour affichage de linfo bulle
		marker.on('click', function(e){

			// recuperation de la gare sur laquelle on a cliquer
			gare=null;	
			for(j in environnement.gares){
				gare = environnement.gares[j];
				if(gare.nom === e.target.label._content){
					break;
				}
			}
			
			// ouverture de la popup pour chargement user
			environnement.map.markers[gare.codeUIC].bindPopup('<b>Chargement des informations ...</b>').openPopup();
			
			//Creation du html a injecter dans l'info bulle
			$.get(environnement.routes["prochainDepart"]+"?codeUIC="+gare.codeUIC, function(data) {

			// creation du msg a afficher dans linfo bulle
			var htmlGareInfoBulle = "";		
			htmlGareInfoBulle += "<b>Information sur "+gare.nom+"</b>\
									</br></br>\
								  <table class='table table-striped'>\
									  <tbody>\
										<tr>\
											<td>Correspondance(s)</td>\
											<td>"+getLignesOfAGareToHTML(getLignesOfAGare(gare.codeUIC))+"</td>\
										</tr>\
										</tbody>\
									</table>\
									<b>Prochain départs</b>";
							
			// Si présence des prochains depart
			if(data.items && data.items.length > 0){					
														
				htmlGareInfoBulle += "</br></br>\
										<table class='table table-striped'>\
										  <thead>\
											<tr>\
											  <th>Horaire</th>\
											  <th>Direction</th>\
											  <th>Train</th>\
											  <th>Etat</th>\
											</tr>\
										  </thead>\
										  <tbody>";	

				var cpt=0;
				for(i in data.items){
					item = data.items[i];
					if(environnement.gares[item.terminus]){
						htmlGareInfoBulle += "<tr>\
												<td>"+item.date.split(" ")[1]+"</td>\
												<td>"+environnement.gares[item.terminus].nom+"</td>\
												<td>"+item.codeMission+"</td>";
												if(item.etat==0){ htmlGareInfoBulle += "<td>A l'heure</td>"; }
												else{  htmlGareInfoBulle += "<td>"+item.etat+"</td>"; }
												
						htmlGareInfoBulle += "</tr>";
						cpt++;
					}
					if(cpt==5){
						break;
					}
				
				}
				
				htmlGareInfoBulle += "</tbody>\
									</table>";
			}
			else{
				htmlGareInfoBulle += "<p>Aucune information disponible</p>";
			}

			// ouverture de la popup avec htmlgareinfobulle
			environnement.map.markers[gare.codeUIC].bindPopup(htmlGareInfoBulle).openPopup();

						
			});

		});
			
			
	}
/*
	for(j in environnement.map.markers){
	
		marker = environnement.map.markers[j];
		console.log(marker);
	
		marker.on('click', function(e){

			gare=null;
			
			for(j in environnement.gares){
				gare = environnement.gares[j];
				if(gare.nom === e.target.label._content){
					break;
				}
			}
			
			//Creation du html a injecter dans l'info bulle
			$.get(environnement.routes["prochainDepart"]+"?codeUIC="+gare.codeUIC, function(data) {

			var htmlGareInfoBulle = "";		
			htmlGareInfoBulle += "<b>Information sur "+gare.nom+"</b>\
								  </br></br>\
									"+getLignesOfAGareToHTML(lignes)+"</br></br>\
									<b>Prochain départs</b>\
									<table class='table table-striped'>\
									  <thead>\
										<tr>\
										  <th>Horaire</th>\
										  <th>Direction</th>\
										  <th>Train</th>\
										  <th>Etat</th>\
										</tr>\
									  </thead>\
									  <tbody>";	

			for(i in data.items){
				item = data.items[i];
				htmlGareInfoBulle += "<tr>\
										<td>"+item.date+"</td>\
										<td>"+environnement.gares[item.terminus].nom+"</td>\
										<td>"+item.codeMission+"</td>\
										<td>"+item.etat+"</td>\
									  </tr>"
				if(i==5){
					break;
				}
			
			}
			
			htmlGareInfoBulle += "</tbody>\
								</table>";

			marker.bindPopup(htmlGareInfoBulle).openPopup();

						
			});

		});
		
	}*/
	


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

	// ajax pour recuperer les lignes
	$.get(environnement.routes["getLignes"], function(data) {
	
		// ensemble du layer qui sera affiche au dessus de la map
		var overlayMaps = {};
		var ligneLayerAll = [];
		
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
					// enregistrement pour un layer qui prend toute les lignes
					ligneLayerAll.push(L.polyline(polyline, {color: environnement.lignes.couleurs[ligne.nom], opacity : '1',weight: '2'}));
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

		// layer pour toutes les tracer de ligne
		overlayMaps["Toutes les lignes"] = L.layerGroup(ligneLayerAll);
		
		// on rajouter tout les controller des ligne sur la map
		L.control.layers(baseMapControl,overlayMaps).setPosition('bottomleft').addTo(environnement.map.instance);

		
		
		});

}