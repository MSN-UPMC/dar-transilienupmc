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
		  $('.alert').remove();
		  
		  
		  
		  if($('#adresseDepart').typeahead('val')=="" || $('#adresseDestination').typeahead('val')==""){
				$('#collapseOne form').after('<div class="alert alert-danger" style="margin-top:40px" role="alert">La gare départ ou de destination n\'est pas renseignée.</div>');	
				return;
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
				$('#collapseOne form').after('<div class="alert alert-danger" style="margin-top:40px" role="alert">La gare de départ ou de destination n\'est pas connue.</div>');	
				return;
		  }
		  
		  
		  $('#collapseOne form').after('<div class="alert alert-info" style="margin-top:40px" role="alert">Calcul de l\'itinéraire en cours.</div>');	

			
			$.get(environnement.routes["itineraire"]+"?departCodeUIC="+codeUICDepart+"&destinationCodeUIC="+codeUICDestination, function(data) {
				if(data.lignes){
			
					// objet qui simule le layer ligne 
					polylineTab = [];
					
					var gareFirst = environnement.gares[data.gares[0].codeUIC];
					var gareLast;
					for(i in data.lignes){
					
						// gare courante 1 et 2
						gare1 = environnement.gares[data.gares[i].codeUIC];
						gare2 = environnement.gares[data.gares[parseInt(i)+1].codeUIC];
						gareLast = gare2;
						
						// objet ligne qui lie la gare 1 et 2
						var polyline = new Array();
						polyline.push(L.latLng(gare1.latitude,gare1.longitude));
						polyline.push(L.latLng(gare2.latitude,gare2.longitude));
						// je creer un ligne en reliant tout les points
						var ligneTracer = L.polyline(polyline, {color: environnement.lignes.couleurs[data.lignes[i]], opacity : '1',weight: '2'}).addTo(environnement.map.instance);						
						
						
						
						polylineTab.push(ligneTracer);
						
					}
					
					var polyline = new Array();
					polyline.push(L.latLng(gareFirst.latitude,gareFirst.longitude));
					polyline.push(L.latLng(gareLast.latitude,gareLast.longitude));
					
					environnement.map.instance.fitBounds(L.polyline(polyline).getBounds());
					
					
					environnement.map.itineraire.isDisplay=true;
					environnement.map.itineraire.polylineArray = polylineTab;
					
					
					
					$('.alert').remove();
					
					var htmlGareAPrendre="";
					for(i in data.gares){
						htmlGareAPrendre += "<strong> "+data.gares[i].nom+"</strong></br>" ;
						if(i < (parseInt(data.gares.length)-1)){
							htmlGareAPrendre += "&#9660;</br>";
						}
					}
					
					
					// creation du msg a afficher dans linfo bulle
					var htmlGareInfoBulle = "";		
					htmlGareInfoBulle += "<b>Itinéraire de "+$('#adresseDepart').typeahead('val')+" à "+$('#adresseDestination').typeahead('val')+"</b>\
											</br></br>\
										  <table class='table table-bordered'>\
											  <tbody>\
												<tr>\
													<td>Ligne(s)</td>\
												</tr>\
												<tr>\
													<td>"+getLignesOfAGareToHTML(data.lignes)+"</td>\
												</tr>\
												<tr>\
													<td>Correspondance(s)</td>\
												</tr>\
												<tr>\
													<td>"+htmlGareAPrendre+"</td>\
												</tr>\
												</tbody>\
											</table>";					
					
					
					
					$('#collapseOne form').after('<div class="alert alert-success" style="margin-top:40px" role="alert">'+htmlGareInfoBulle+'</div>');

					
					
				}
				else{
					$('#collapseOne form').after('<div class="alert alert-danger" style="margin-top:40px" role="alert">Aucun itinéraire est disponible de '+$('#adresseDepart').typeahead('val')+' à '+$('#adresseDestination').typeahead('val')+'</div>');
					return;
				}
			
			
			});
	
		});	

		
		// Evenement sur la recherche d'info sur une gare - deplace la map sur la gare et ouvre la popup d'info
		$('#collapseTwo form button[type="submit"]').click(function(e) {
		  e.preventDefault();
		  e.stopPropagation();
		  $('.alert').remove();
		  
		  
		  if(environnement.map.itineraire.isDisplay){
			for(j in polylineTab){
				environnement.map.instance.removeLayer(environnement.map.itineraire.polylineArray[j]);
			}
			environnement.map.itineraire.isDisplay=false;
		  }
		  
		  
		  
		  
		  
		  var codeUIC=-1;
		  for(i in environnement.gares){
			if(environnement.gares[i].nom === $('#rechercherGare').typeahead('val')){
				codeUIC = environnement.gares[i].codeUIC;
				break;
			}
		  }
		  
		  
		  
		  
		  if(codeUIC==-1){
				$('#collapseTwo form').after('<div class="alert alert-danger" style="margin-top:10px" role="alert">La gare n\'est pas connue.</div>');	
				return;
		  }

		  
		  environnement.map.instance.setView(environnement.map.markers[codeUIC].getLatLng());
		  environnement.map.instance.setZoom(15);
		  environnement.map.markers[codeUIC].fire('click');
		});			
	});
	
}


function initStatistique(){


$(function(){

  $("#pieChart").drawPieChart([
    { title: "sont à l'heure", value : environnement.statistique.alheure,  color: "#04374E" },
    { title: "sont en retard", value:  environnement.statistique.retard,   color: "#EB0D42" },
    { title: "sont annulés",   value : environnement.statistique.annule,   color: "#FFEC62" }
  ]);

  
});

;(function($, undefined) {
  $.fn.drawPieChart = function(data, options) {
    var $this = this,
      W = 250,
      H = 250,
      centerX = W/2,
      centerY = H/2,
      cos = Math.cos,
      sin = Math.sin,
      PI = Math.PI,
      settings = $.extend({
        segmentShowStroke : true,
        segmentStrokeColor : "#fff",
        segmentStrokeWidth : 1,
        baseColor: "#fff",
        baseOffset: 15,
        edgeOffset: 30,
        pieSegmentGroupClass: "pieSegmentGroup",
        pieSegmentClass: "pieSegment",
        lightPiesOffset: 12,
        lightPiesOpacity: .3,
        lightPieClass: "lightPie",
        animation : true,
        animationSteps : 90,
        animationEasing : "easeInOutExpo",
        tipOffsetX: -15,
        tipOffsetY: -45,
        tipClass: "pieTip",
        beforeDraw: function(){  },
        afterDrawed : function(){  },
        onPieMouseenter : function(e,data){  },
        onPieMouseleave : function(e,data){  },
        onPieClick : function(e,data){  }
      }, options),
      animationOptions = {
        linear : function (t){
          return t;
        },
        easeInOutExpo: function (t) {
          var v = t<.5 ? 8*t*t*t*t : 1-8*(--t)*t*t*t;
          return (v>1) ? 1 : v;
        }
      },
      requestAnimFrame = function(){
        return window.requestAnimationFrame ||
          window.webkitRequestAnimationFrame ||
          window.mozRequestAnimationFrame ||
          window.oRequestAnimationFrame ||
          window.msRequestAnimationFrame ||
          function(callback) {
            window.setTimeout(callback, 1000 / 60);
          };
      }();

    var $wrapper = $('<svg width="' + W + '" height="' + H + '" viewBox="0 0 ' + W + ' ' + H + '" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"></svg>').appendTo($this);
    var $groups = [],
        $pies = [],
        $lightPies = [],
        easingFunction = animationOptions[settings.animationEasing],
        pieRadius = Min([H/2,W/2]) - settings.edgeOffset,
        segmentTotal = 0;

    var drawBasePie = function(){
      var base = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
      var $base = $(base).appendTo($wrapper);
      base.setAttribute("cx", centerX);
      base.setAttribute("cy", centerY);
      base.setAttribute("r", pieRadius+settings.baseOffset);
      base.setAttribute("fill", settings.baseColor);
    }();

    var pathGroup = document.createElementNS('http://www.w3.org/2000/svg', 'g');
    var $pathGroup = $(pathGroup).appendTo($wrapper);
    $pathGroup[0].setAttribute("opacity",0);

    var $tip = $('<div class="' + settings.tipClass + '" />').appendTo('body').hide(),
      tipW = $tip.width(),
      tipH = $tip.height();

    for (var i = 0, len = data.length; i < len; i++){
      segmentTotal += data[i].value;
      var g = document.createElementNS('http://www.w3.org/2000/svg', 'g');
      g.setAttribute("data-order", i);
      g.setAttribute("class", settings.pieSegmentGroupClass);
      $groups[i] = $(g).appendTo($pathGroup);
      $groups[i]
        .on("mouseenter", pathMouseEnter)
        .on("mouseleave", pathMouseLeave)
        .on("mousemove", pathMouseMove)
        .on("click", pathClick);

      var p = document.createElementNS('http://www.w3.org/2000/svg', 'path');
      p.setAttribute("stroke-width", settings.segmentStrokeWidth);
      p.setAttribute("stroke", settings.segmentStrokeColor);
      p.setAttribute("stroke-miterlimit", 2);
      p.setAttribute("fill", data[i].color);
      p.setAttribute("class", settings.pieSegmentClass);
      $pies[i] = $(p).appendTo($groups[i]);

      var lp = document.createElementNS('http://www.w3.org/2000/svg', 'path');
      lp.setAttribute("stroke-width", settings.segmentStrokeWidth);
      lp.setAttribute("stroke", settings.segmentStrokeColor);
      lp.setAttribute("stroke-miterlimit", 2);
      lp.setAttribute("fill", data[i].color);
      lp.setAttribute("opacity", settings.lightPiesOpacity);
      lp.setAttribute("class", settings.lightPieClass);
      $lightPies[i] = $(lp).appendTo($groups[i]);
    }

    settings.beforeDraw.call($this);
    triggerAnimation();

    function pathMouseEnter(e){
      var index = $(this).data().order;
      $tip.text(data[index].value + "% des trains "+data[index].title  ).fadeIn(200);
      if ($groups[index][0].getAttribute("data-active") !== "active"){
        $lightPies[index].animate({opacity: .8}, 180);
      }
      settings.onPieMouseenter.apply($(this),[e,data]);
    }
    function pathMouseLeave(e){
      var index = $(this).data().order;
      $tip.hide();
      if ($groups[index][0].getAttribute("data-active") !== "active"){
        $lightPies[index].animate({opacity: settings.lightPiesOpacity}, 100);
      }
      settings.onPieMouseleave.apply($(this),[e,data]);
    }
    function pathMouseMove(e){
      $tip.css({
        top: e.pageY + settings.tipOffsetY,
        left: e.pageX - $tip.width() / 2 + settings.tipOffsetX
      });
    }
    function pathClick(e){
      var index = $(this).data().order;
      var targetGroup = $groups[index][0];
      for (var i = 0, len = data.length; i < len; i++){
        if (i === index) continue;
        $groups[i][0].setAttribute("data-active","");
        $lightPies[i].css({opacity: settings.lightPiesOpacity});
      }
      if (targetGroup.getAttribute("data-active") === "active"){
        targetGroup.setAttribute("data-active","");
        $lightPies[index].css({opacity: .8});
      } else {
        targetGroup.setAttribute("data-active","active");
        $lightPies[index].css({opacity: 1});
      }
      settings.onPieClick.apply($(this),[e,data]);
    }
    function drawPieSegments (animationDecimal){
      var startRadius = -PI/2,
          rotateAnimation = 1;
      if (settings.animation) {
        rotateAnimation = animationDecimal;
      }

      $pathGroup[0].setAttribute("opacity",animationDecimal);

      for (var i = 0, len = data.length; i < len; i++){
        var segmentAngle = rotateAnimation * ((data[i].value/segmentTotal) * (PI*2)),
            endRadius = startRadius + segmentAngle,
            largeArc = ((endRadius - startRadius) % (PI * 2)) > PI ? 1 : 0,
            startX = centerX + cos(startRadius) * pieRadius,
            startY = centerY + sin(startRadius) * pieRadius,
            endX = centerX + cos(endRadius) * pieRadius,
            endY = centerY + sin(endRadius) * pieRadius,
            startX2 = centerX + cos(startRadius) * (pieRadius + settings.lightPiesOffset),
            startY2 = centerY + sin(startRadius) * (pieRadius + settings.lightPiesOffset),
            endX2 = centerX + cos(endRadius) * (pieRadius + settings.lightPiesOffset),
            endY2 = centerY + sin(endRadius) * (pieRadius + settings.lightPiesOffset);
        var cmd = [
          'M', startX, startY,
          'A', pieRadius, pieRadius, 0, largeArc, 1, endX, endY,
          'L', centerX, centerY,
          'Z'
        ];
        var cmd2 = [
          'M', startX2, startY2,
          'A', pieRadius + settings.lightPiesOffset, pieRadius + settings.lightPiesOffset, 0, largeArc, 1, endX2, endY2,
          'L', centerX, centerY,
          'Z'
        ];
        $pies[i][0].setAttribute("d",cmd.join(' '));
        $lightPies[i][0].setAttribute("d", cmd2.join(' '));
        startRadius += segmentAngle;
      }
    }

    var animFrameAmount = (settings.animation)? 1/settings.animationSteps : 1,
        animCount =(settings.animation)? 0 : 1;
    function triggerAnimation(){
      if (settings.animation) {
        requestAnimFrame(animationLoop);
      } else {
        drawPieSegments(1);
      }
    }
    function animationLoop(){
      animCount += animFrameAmount;
      drawPieSegments(easingFunction(animCount));
      if (animCount < 1){
        requestAnimFrame(arguments.callee);
      } else {
        settings.afterDrawed.call($this);
      }
    }
    function Max(arr){
      return Math.max.apply(null, arr);
    }
    function Min(arr){
      return Math.min.apply(null, arr);
    }
    return $this;
  };
})(jQuery);



}