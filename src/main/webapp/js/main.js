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
	
	//environnement.host = "https://transilien-upmc.appspot.com/_ah/api";
	
	environnement.host = "http://localhost:8080/_ah/api";	
	environnement.routes = new Array();
	environnement.routes["getGaresList"] = environnement.host+"/gares/v1/getGares";
	environnement.routes["getLignes"] = environnement.host+"/lignes/v1/getLignes";
	environnement.routes["ligneOriente"] = environnement.host+"/lignes/v1/ligneOriente";
	environnement.routes["prochainDepart"] = environnement.host+"/itineraire/v1/prochainDepart";
	environnement.routes["itineraire"] = environnement.host+"/itineraire/v1/itineraire";


	
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



/** Init Statistique **/

$(function(){
  $("#pieChart").drawPieChart([
    { title: "sont à l'heure", value : 80,  color: "#04374E" },
    { title: "sont en retard", value:  10,   color: "#EB0D42" },
    { title: "sont annulés",   value : 10,   color: "#FFEC62" }
  ]);
});

;(function($, undefined) {
  $.fn.drawPieChart = function(data, options) {
	console.log(this);
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


