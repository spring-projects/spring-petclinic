//
//    Main script of DevOOPS v1.0 Bootstrap Theme
//
"use strict";
/*-------------------------------------------
	Dynamically load plugin scripts
---------------------------------------------*/
//
// Dynamically load Fullcalendar Plugin Script
// homepage: http://arshaw.com/fullcalendar
// require moment.js
//
function LoadCalendarScript(callback){
	function LoadFullCalendarScript(){
		if(!$.fn.fullCalendar){
			$.getScript('plugins/fullcalendar/fullcalendar.js', callback);
		}
		else {
			if (callback && typeof(callback) === "function") {
				callback();
			}
		}
	}
	if (!$.fn.moment){
		$.getScript('plugins/moment/moment.min.js', LoadFullCalendarScript);
	}
	else {
		LoadFullCalendarScript();
	}
}
//
// Dynamically load  OpenStreetMap Plugin
// homepage: http://openlayers.org
//
function LoadOpenLayersScript(callback){
	if (!$.fn.OpenLayers){
		$.getScript('http://www.openlayers.org/api/OpenLayers.js', callback);
	}
	else {
		if (callback && typeof(callback) === "function") {
			callback();
		}
	}
}
//
//  Dynamically load  jQuery Timepicker plugin
//  homepage: http://trentrichardson.com/examples/timepicker/
//
function LoadTimePickerScript(callback){
	if (!$.fn.timepicker){
		$.getScript('plugins/jquery-ui-timepicker-addon/jquery-ui-timepicker-addon.min.js', callback);
	}
	else {
		if (callback && typeof(callback) === "function") {
			callback();
		}
	}
}
//
//  Dynamically load Bootstrap Validator Plugin
//  homepage: https://github.com/nghuuphuoc/bootstrapvalidator
//
function LoadBootstrapValidatorScript(callback){
	if (!$.fn.bootstrapValidator){
		$.getScript('plugins/bootstrapvalidator/bootstrapValidator.min.js', callback);
	}
	else {
		if (callback && typeof(callback) === "function") {
			callback();
		}
	}
}
//
//  Dynamically load jQuery Select2 plugin
//  homepage: https://github.com/ivaynberg/select2  v3.4.5  license - GPL2
//
function LoadSelect2Script(callback){
	if (!$.fn.select2){
		$.getScript('plugins/select2/select2.min.js', callback);
	}
	else {
		if (callback && typeof(callback) === "function") {
			callback();
		}
	}
}
//
//  Dynamically load DataTables plugin
//  homepage: http://datatables.net v1.9.4 license - GPL or BSD
//
function LoadDataTablesScripts(callback){
	function LoadDatatables(){
		$.getScript('plugins/datatables/jquery.dataTables.js', function(){
			$.getScript('plugins/datatables/ZeroClipboard.js', function(){
				$.getScript('plugins/datatables/TableTools.js', function(){
					$.getScript('plugins/datatables/dataTables.bootstrap.js', callback);
				});
			});
		});
	}
	if (!$.fn.dataTables){
		LoadDatatables();
	}
	else {
		if (callback && typeof(callback) === "function") {
			callback();
		}
	}
}
//
//  Dynamically load Widen FineUploader
//  homepage: https://github.com/Widen/fine-uploader  v5.0.1 license - GPL3
//
function LoadFineUploader(callback){
	if (!$.fn.fineuploader){
		$.getScript('plugins/fineuploader/jquery.fineuploader-5.0.1.min.js', callback);
	}
	else {
		if (callback && typeof(callback) === "function") {
			callback();
		}
	}
}
//
//  Dynamically load xCharts plugin
//  homepage: http://tenxer.github.io/xcharts/ v0.3.0 license - MIT
//  Required D3 plugin http://d3js.org/ v3.4.1 license - MIT
//
function LoadXChartScript(callback){
	function LoadXChart(){
		$.getScript('plugins/xcharts/xcharts.min.js', callback);
	}
	function LoadD3Script(){
		if (!$.fn.d3){
			$.getScript('plugins/d3/d3.v3.min.js', LoadXChart)
		}
		else {
			LoadXChart();
		}
	}
	if (!$.fn.xcharts){
		LoadD3Script();
	}
	else {
		if (callback && typeof(callback) === "function") {
			callback();
		}
	}
}
//
//  Dynamically load Flot plugin
//  homepage: http://www.flotcharts.org  v0.8.2 license- MIT
//
function LoadFlotScripts(callback){
	function LoadFlotScript(){
		$.getScript('plugins/flot/jquery.flot.js', LoadFlotResizeScript);
	}
	function LoadFlotResizeScript(){
		$.getScript('plugins/flot/jquery.flot.resize.js', LoadFlotTimeScript);
	}
	function LoadFlotTimeScript(){
		$.getScript('plugins/flot/jquery.flot.time.js', callback);
	}
	if (!$.fn.flot){
		LoadFlotScript();
	}
	else {
		if (callback && typeof(callback) === "function") {
			callback();
		}
	}
}
//
//  Dynamically load Morris Charts plugin
//  homepage: http://www.oesmith.co.uk/morris.js/ v0.4.3 License - MIT
//  require Raphael http://raphael.js
//
function LoadMorrisScripts(callback){
	function LoadMorrisScript(){
		if(!$.fn.Morris){
			$.getScript('plugins/morris/morris.min.js', callback);
		}
		else {
			if (callback && typeof(callback) === "function") {
				callback();
			}
		}
	}
	if (!$.fn.raphael){
		$.getScript('plugins/raphael/raphael-min.js', LoadMorrisScript);
	}
	else {
		LoadMorrisScript();
	}
}
//
//  Dynamically load Fancybox 2 plugin
//  homepage: http://fancyapps.com/fancybox/ v2.1.5 License - MIT
//
function LoadFancyboxScript(callback){
	if (!$.fn.fancybox){
		$.getScript('plugins/fancybox/jquery.fancybox.js', callback);
	}
	else {
		if (callback && typeof(callback) === "function") {
			callback();
		}
	}
}
//
//  Dynamically load jQuery-Knob plugin
//  homepage: http://anthonyterrien.com/knob/  v1.2.5 License- MIT or GPL
//
function LoadKnobScripts(callback){
	if(!$.fn.knob){
		$.getScript('plugins/jQuery-Knob/jquery.knob.js', callback);
	}
	else {
		if (callback && typeof(callback) === "function") {
			callback();
		}
	}
}
//
//  Dynamically load Sparkline plugin
//  homepage: http://omnipotent.net/jquery.sparkline v2.1.2  License - BSD
//
function LoadSparkLineScript(callback){
	if(!$.fn.sparkline){
		$.getScript('plugins/sparkline/jquery.sparkline.min.js', callback);
	}
	else {
		if (callback && typeof(callback) === "function") {
			callback();
		}
	}
}
/*-------------------------------------------
	Main scripts used by theme
---------------------------------------------*/
//
//  Function for load content from url and put in $('.ajax-content') block
//
function LoadAjaxContent(url){
	$('.preloader').show();
	$.ajax({
		mimeType: 'text/html; charset=utf-8', // ! Need set mimeType only when run from local file
		url: url,
		type: 'GET',
		success: function(data) {
			$('#ajax-content').html(data);
			$('.preloader').hide();
		},
		error: function (jqXHR, textStatus, errorThrown) {
			alert(errorThrown);
		},
		dataType: "html",
		async: false
	});
}
//
//  Function maked all .box selector is draggable, to disable for concrete element add class .no-drop
//
function WinMove(){
	$( "div.box").not('.no-drop')
		.draggable({
			revert: true,
			zIndex: 2000,
			cursor: "crosshair",
			handle: '.box-name',
			opacity: 0.8
		})
		.droppable({
			tolerance: 'pointer',
			drop: function( event, ui ) {
				var draggable = ui.draggable;
				var droppable = $(this);
				var dragPos = draggable.position();
				var dropPos = droppable.position();
				draggable.swap(droppable);
				setTimeout(function() {
					var dropmap = droppable.find('[id^=map-]');
					var dragmap = draggable.find('[id^=map-]');
					if (dragmap.length > 0 || dropmap.length > 0){
						dragmap.resize();
						dropmap.resize();
					}
					else {
						draggable.resize();
						droppable.resize();
					}
				}, 50);
				setTimeout(function() {
					draggable.find('[id^=map-]').resize();
					droppable.find('[id^=map-]').resize();
				}, 250);
			}
		});
}
//
// Swap 2 elements on page. Used by WinMove function
//
jQuery.fn.swap = function(b){
	b = jQuery(b)[0];
	var a = this[0];
	var t = a.parentNode.insertBefore(document.createTextNode(''), a);
	b.parentNode.insertBefore(a, b);
	t.parentNode.insertBefore(b, t);
	t.parentNode.removeChild(t);
	return this;
};
//
//  Screensaver function
//  used on locked screen, and write content to element with id - canvas
//
function ScreenSaver(){
	var canvas = document.getElementById("canvas");
	var ctx = canvas.getContext("2d");
	// Size of canvas set to fullscreen of browser
	var W = window.innerWidth;
	var H = window.innerHeight;
	canvas.width = W;
	canvas.height = H;
	// Create array of particles for screensaver
	var particles = [];
	for (var i = 0; i < 25; i++) {
		particles.push(new Particle());
	}
	function Particle(){
		// location on the canvas
		this.location = {x: Math.random()*W, y: Math.random()*H};
		// radius - lets make this 0
		this.radius = 0;
		// speed
		this.speed = 3;
		// random angle in degrees range = 0 to 360
		this.angle = Math.random()*360;
		// colors
		var r = Math.round(Math.random()*255);
		var g = Math.round(Math.random()*255);
		var b = Math.round(Math.random()*255);
		var a = Math.random();
		this.rgba = "rgba("+r+", "+g+", "+b+", "+a+")";
	}
	// Draw the particles
	function draw() {
		// re-paint the BG
		// Lets fill the canvas black
		// reduce opacity of bg fill.
		// blending time
		ctx.globalCompositeOperation = "source-over";
		ctx.fillStyle = "rgba(0, 0, 0, 0.02)";
		ctx.fillRect(0, 0, W, H);
		ctx.globalCompositeOperation = "lighter";
		for(var i = 0; i < particles.length; i++){
			var p = particles[i];
			ctx.fillStyle = "white";
			ctx.fillRect(p.location.x, p.location.y, p.radius, p.radius);
			// Lets move the particles
			// So we basically created a set of particles moving in random direction
			// at the same speed
			// Time to add ribbon effect
			for(var n = 0; n < particles.length; n++){
				var p2 = particles[n];
				// calculating distance of particle with all other particles
				var yd = p2.location.y - p.location.y;
				var xd = p2.location.x - p.location.x;
				var distance = Math.sqrt(xd*xd + yd*yd);
				// draw a line between both particles if they are in 200px range
				if(distance < 200){
					ctx.beginPath();
					ctx.lineWidth = 1;
					ctx.moveTo(p.location.x, p.location.y);
					ctx.lineTo(p2.location.x, p2.location.y);
					ctx.strokeStyle = p.rgba;
					ctx.stroke();
					//The ribbons appear now.
				}
			}
			// We are using simple vectors here
			// New x = old x + speed * cos(angle)
			p.location.x = p.location.x + p.speed*Math.cos(p.angle*Math.PI/180);
			// New y = old y + speed * sin(angle)
			p.location.y = p.location.y + p.speed*Math.sin(p.angle*Math.PI/180);
			// You can read about vectors here:
			// http://physics.about.com/od/mathematics/a/VectorMath.htm
			if(p.location.x < 0) p.location.x = W;
			if(p.location.x > W) p.location.x = 0;
			if(p.location.y < 0) p.location.y = H;
			if(p.location.y > H) p.location.y = 0;
		}
	}
	setInterval(draw, 30);
}
//
// Helper for draw Google Chart
//
function drawGoogleChart(chart_data, chart_options, element, chart_type) {
	// Function for visualize Google Chart
	var data = google.visualization.arrayToDataTable(chart_data);
	var chart = new chart_type(document.getElementById(element));
	chart.draw(data, chart_options);
}
//
//  Function for Draw Knob Charts
//
function DrawKnob(elem){
	elem.knob({
		change : function (value) {
			//console.log("change : " + value);
		},
		release : function (value) {
			//console.log(this.$.attr('value'));
			console.log("release : " + value);
		},
		cancel : function () {
			console.log("cancel : ", this);
		},
		draw : function () {
			// "tron" case
			if(this.$.data('skin') == 'tron') {
				var a = this.angle(this.cv);  // Angle
				var sa = this.startAngle;          // Previous start angle
				var sat = this.startAngle;         // Start angle
				var ea;                            // Previous end angle
				var eat = sat + a;                 // End angle
				var r = 1;
				this.g.lineWidth = this.lineWidth;
				this.o.cursor
					&& (sat = eat - 0.3)
					&& (eat = eat + 0.3);
				if (this.o.displayPrevious) {
					ea = this.startAngle + this.angle(this.v);
					this.o.cursor
						&& (sa = ea - 0.3)
						&& (ea = ea + 0.3);
					this.g.beginPath();
					this.g.strokeStyle = this.pColor;
					this.g.arc(this.xy, this.xy, this.radius - this.lineWidth, sa, ea, false);
					this.g.stroke();
				}
				this.g.beginPath();
				this.g.strokeStyle = r ? this.o.fgColor : this.fgColor ;
				this.g.arc(this.xy, this.xy, this.radius - this.lineWidth, sat, eat, false);
				this.g.stroke();
				this.g.lineWidth = 2;
				this.g.beginPath();
				this.g.strokeStyle = this.o.fgColor;
				this.g.arc( this.xy, this.xy, this.radius - this.lineWidth + 1 + this.lineWidth * 2 / 3, 0, 2 * Math.PI, false);
				this.g.stroke();
				return false;
			}
		}
	});
	// Example of infinite knob, iPod click wheel
	var v;
	var up = 0;
	var down=0;
	var i=0;
	var $idir = $("div.idir");
	var $ival = $("div.ival");
	var incr = function() { i++; $idir.show().html("+").fadeOut(); $ival.html(i); }
	var decr = function() { i--; $idir.show().html("-").fadeOut(); $ival.html(i); };
	$("input.infinite").knob(
		{
			min : 0,
			max : 20,
			stopper : false,
			change : function () {
				if(v > this.cv){
					if(up){
						decr();
						up=0;
					} else {
						up=1;down=0;
					}
				} else {
					if(v < this.cv){
						if(down){
							incr();
							down=0;
						} else {
							down=1;up=0;
						}
					}
				}
				v = this.cv;
			}
		});
}
//
// Create OpenLayers map with required options and return map as object
//
function drawMap(lon, lat, elem, layers) {
	var LayersArray = [];
	// Map initialization
	var map = new OpenLayers.Map(elem);
	// Add layers on map
	map.addLayers(layers);
	// WGS 1984 projection
	var epsg4326 =  new OpenLayers.Projection("EPSG:4326");
	//The map projection (Spherical Mercator)
	var projectTo = map.getProjectionObject();
	// Max zoom = 17
	var zoom=10;
	map.zoomToMaxExtent();
	// Set longitude/latitude
	var lonlat = new OpenLayers.LonLat(lon, lat);
	map.setCenter(lonlat.transform(epsg4326, projectTo), zoom);
	var layerGuest = new OpenLayers.Layer.Vector("You are here");
	// Define markers as "features" of the vector layer:
	var guestMarker = new OpenLayers.Feature.Vector(
		new OpenLayers.Geometry.Point(lon, lat).transform(epsg4326, projectTo)
	);
	layerGuest.addFeatures(guestMarker);
	LayersArray.push(layerGuest);
	map.addLayers(LayersArray);
	// If map layers > 1 then show checker
	if (layers.length > 1){
		map.addControl(new OpenLayers.Control.LayerSwitcher({'ascending':true}));
	}
	// Link to current position
	map.addControl(new OpenLayers.Control.Permalink());
	// Show current mouse coords
	map.addControl(new OpenLayers.Control.MousePosition({ displayProjection: epsg4326 }));
	return map
}
//
//  Function for create 2 dates in human-readable format (with leading zero)
//
function PrettyDates(){
	var currDate = new Date();
	var year = currDate.getFullYear();
	var month = currDate.getMonth() + 1;
	var startmonth = 1;
	if (month > 3){
		startmonth = month -2;
	}
	if (startmonth <=9){
		startmonth = '0'+startmonth;
	}
	if (month <= 9) {
		month = '0'+month;
	}
	var day= currDate.getDate();
	if (day <= 9) {
		day = '0'+day;
	}
	var startdate = year +'-'+ startmonth +'-01';
	var enddate = year +'-'+ month +'-'+ day;
	return [startdate, enddate];
}
//
//  Function set min-height of window (required for this theme)
//
function SetMinBlockHeight(elem){
	elem.css('min-height', window.innerHeight - 49)
}
//
//  Helper for correct size of Messages page
//
function MessagesMenuWidth(){
	var W = window.innerWidth;
	var W_menu = $('#sidebar-left').outerWidth();
	var w_messages = (W-W_menu)*16.666666666666664/100;
	$('#messages-menu').width(w_messages);
}
//
// Function for change panels of Dashboard
//
function DashboardTabChecker(){
	$('#content').on('click', 'a.tab-link', function(e){
		e.preventDefault();
		$('div#dashboard_tabs').find('div[id^=dashboard]').each(function(){
			$(this).css('visibility', 'hidden').css('position', 'absolute');
		});
		var attr = $(this).attr('id');
		$('#'+'dashboard-'+attr).css('visibility', 'visible').css('position', 'relative');
		$(this).closest('.nav').find('li').removeClass('active');
		$(this).closest('li').addClass('active');
	});
}
//
// Helper for run TinyMCE editor with textarea's
//
function TinyMCEStart(elem, mode){
	var plugins = [];
	if (mode == 'extreme'){
		plugins = [ "advlist anchor autolink autoresize autosave bbcode charmap code contextmenu directionality ",
			"emoticons fullpage fullscreen hr image insertdatetime layer legacyoutput",
			"link lists media nonbreaking noneditable pagebreak paste preview print save searchreplace",
			"tabfocus table template textcolor visualblocks visualchars wordcount"]
	}
	tinymce.init({selector: elem,
		theme: "modern",
		plugins: plugins,
		//content_css: "css/style.css",
		toolbar: "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image | print preview media fullpage | forecolor backcolor emoticons",
		style_formats: [
			{title: 'Header 2', block: 'h2', classes: 'page-header'},
			{title: 'Header 3', block: 'h3', classes: 'page-header'},
			{title: 'Header 4', block: 'h4', classes: 'page-header'},
			{title: 'Header 5', block: 'h5', classes: 'page-header'},
			{title: 'Header 6', block: 'h6', classes: 'page-header'},
			{title: 'Bold text', inline: 'b'},
			{title: 'Red text', inline: 'span', styles: {color: '#ff0000'}},
			{title: 'Red header', block: 'h1', styles: {color: '#ff0000'}},
			{title: 'Example 1', inline: 'span', classes: 'example1'},
			{title: 'Example 2', inline: 'span', classes: 'example2'},
			{title: 'Table styles'},
			{title: 'Table row 1', selector: 'tr', classes: 'tablerow1'}
		]
	});
}
//
// Helper for draw Sparkline plots on Dashboard page
//
function SparkLineDrawBarGraph(elem, arr, color){
	if (color) {
		var stacked_color = color;
	}
	else {
		stacked_color = '#6AA6D6'
	}
	elem.sparkline(arr, { type: 'bar', barWidth: 7, highlightColor: '#000', barSpacing: 2, height: 40, stackedBarColor: stacked_color});
}
//
//  Helper for open ModalBox with requested header, content and bottom
//
//
function OpenModalBox(header, inner, bottom){
	var modalbox = $('#modalbox');
	modalbox.find('.modal-header-name span').html(header);
	modalbox.find('.devoops-modal-inner').html(inner);
	modalbox.find('.devoops-modal-bottom').html(bottom);
	modalbox.fadeIn('fast');
	$('body').addClass("body-expanded");
}
//
//  Close modalbox
//
//
function CloseModalBox(){
	var modalbox = $('#modalbox');
	modalbox.fadeOut('fast', function(){
		modalbox.find('.modal-header-name span').children().remove();
		modalbox.find('.devoops-modal-inner').children().remove();
		modalbox.find('.devoops-modal-bottom').children().remove();
		$('body').removeClass("body-expanded");
	});
}
//
//  Beauty tables plugin (navigation in tables with inputs in cell)
//  Created by DevOOPS.
//
(function( $ ){
	$.fn.beautyTables = function() {
		var table = this;
		var string_fill = false;
		this.on('keydown', function(event) {
		var target = event.target;
		var tr = $(target).closest("tr");
		var col = $(target).closest("td");
		if (target.tagName.toUpperCase() == 'INPUT'){
			if (event.shiftKey === true){
				switch(event.keyCode) {
					case 37: // left arrow
						col.prev().children("input[type=text]").focus();
						break;
					case 39: // right arrow
						col.next().children("input[type=text]").focus();
						break;
					case 40: // down arrow
						if (string_fill==false){
							tr.next().find('td:eq('+col.index()+') input[type=text]').focus();
						}
						break;
					case 38: // up arrow
						if (string_fill==false){
							tr.prev().find('td:eq('+col.index()+') input[type=text]').focus();
						}
						break;
				}
			}
			if (event.ctrlKey === true){
				switch(event.keyCode) {
					case 37: // left arrow
						tr.find('td:eq(1)').find("input[type=text]").focus();
						break;
					case 39: // right arrow
						tr.find('td:last-child').find("input[type=text]").focus();
						break;
				case 40: // down arrow
					if (string_fill==false){
						table.find('tr:last-child td:eq('+col.index()+') input[type=text]').focus();
					}
					break;
				case 38: // up arrow
					if (string_fill==false){
						table.find('tr:eq(1) td:eq('+col.index()+') input[type=text]').focus();
					}
						break;
				}
			}
			if (event.keyCode == 13 || event.keyCode == 9 ) {
				event.preventDefault();
				col.next().find("input[type=text]").focus();
			}
			if (string_fill==false){
				if (event.keyCode == 34) {
					event.preventDefault();
					table.find('tr:last-child td:last-child').find("input[type=text]").focus();}
				if (event.keyCode == 33) {
					event.preventDefault();
					table.find('tr:eq(1) td:eq(1)').find("input[type=text]").focus();}
			}
		}
		});
		table.find("input[type=text]").each(function(){
			$(this).on('blur', function(event){
			var target = event.target;
			var col = $(target).parents("td");
			if(table.find("input[name=string-fill]").prop("checked")==true) {
				col.nextAll().find("input[type=text]").each(function() {
					$(this).val($(target).val());
				});
			}
		});
	})
};
})( jQuery );
//
// Beauty Hover Plugin (backlight row and col when cell in mouseover)
//
//
(function( $ ){
	$.fn.beautyHover = function() {
		var table = this;
		table.on('mouseover','td', function() {
			var idx = $(this).index();
			var rows = $(this).closest('table').find('tr');
			rows.each(function(){
				$(this).find('td:eq('+idx+')').addClass('beauty-hover');
			});
		})
		.on('mouseleave','td', function(e) {
			var idx = $(this).index();
			var rows = $(this).closest('table').find('tr');
			rows.each(function(){
				$(this).find('td:eq('+idx+')').removeClass('beauty-hover');
			});
		});
	};
})( jQuery );
//
//  Function convert values of inputs in table to JSON data
//
//
function Table2Json(table) {
	var result = {};
	table.find("tr").each(function () {
		var oneRow = [];
		var varname = $(this).index();
		$("td", this).each(function (index) { if (index != 0) {oneRow.push($("input", this).val());}});
		result[varname] = oneRow;
	});
	var result_json = JSON.stringify(result);
	OpenModalBox('Table to JSON values', result_json);
}
/*-------------------------------------------
	Demo graphs for Flot Chart page (charts_flot.html)
---------------------------------------------*/
//
// Graph1 created in element with id = box-one-content
//
function FlotGraph1(){
	// We use an inline data source in the example, usually data would
	// be fetched from a server
	var data = [],
	totalPoints = 300;
	function getRandomData() {
		if (data.length > 0)
			data = data.slice(1);
		// Do a random walk
		while (data.length < totalPoints) {
			var prev = data.length > 0 ? data[data.length - 1] : 50,
			y = prev + Math.random() * 10 - 5;
			if (y < 0) {
				y = 0;
			} else if (y > 100) {
				y = 100;
			}
			data.push(y);
		}
		// Zip the generated y values with the x values
		var res = [];
		for (var i = 0; i < data.length; ++i) {
			res.push([i, data[i]])
		}
		return res;
	}
	var updateInterval = 30;
	var plot = $.plot("#box-one-content", [ getRandomData() ], {
		series: {
			shadowSize: 0	// Drawing is faster without shadows
		},
		yaxis: {min: 0,	max: 100},
		xaxis: {show: false	}
	});
	function update() {
		plot.setData([getRandomData()]);
		// Since the axes don't change, we don't need to call plot.setupGrid()
		plot.draw();
		setTimeout(update, updateInterval);
	}
	update();
}
//
// Graph2 created in element with id = box-two-content
//
function FlotGraph2(){
	var sin = [];
	var cos = [];
	var tan = [];
	for (var i = 0; i < 14; i += 0.1) {
		sin.push([i, Math.sin(i)]);
		cos.push([i, Math.cos(i)]);
		tan.push([i, Math.tan(i)/4]);
	}
	var plot = $.plot("#box-two-content", [
		{ data: sin, label: "sin(x) = -0.00"},
		{ data: cos, label: "cos(x) = -0.00" },
		{ data: tan, label: "tan(x)/4 = -0.00" }
	], {
		series: {
			lines: {
				show: true
			}
		},
		crosshair: {
			mode: "x"
		},
		grid: {
			hoverable: true,
			autoHighlight: false
		},
		yaxis: {
			min: -5.2,
			max: 5.2
		}
	});
	var legends = $("#box-two-content .legendLabel");
	legends.each(function () {
		// fix the widths so they don't jump around
		$(this).css('width', $(this).width());
	});
	var updateLegendTimeout = null;
	var latestPosition = null;
	function updateLegend() {
		updateLegendTimeout = null;
		var pos = latestPosition;
		var axes = plot.getAxes();
		if (pos.x < axes.xaxis.min || pos.x > axes.xaxis.max ||
			pos.y < axes.yaxis.min || pos.y > axes.yaxis.max) {
			return;
		}
		var i, j, dataset = plot.getData();
		for (i = 0; i < dataset.length; ++i) {
			var series = dataset[i];
			// Find the nearest points, x-wise
			for (j = 0; j < series.data.length; ++j) {
				if (series.data[j][0] > pos.x) {
					break;
				}
			}
		// Now Interpolate
		var y, p1 = series.data[j - 1],	p2 = series.data[j];
			if (p1 == null) {
				y = p2[1];
			} else if (p2 == null) {
				y = p1[1];
			} else {
				y = p1[1] + (p2[1] - p1[1]) * (pos.x - p1[0]) / (p2[0] - p1[0]);
			}
			legends.eq(i).text(series.label.replace(/=.*/, "= " + y.toFixed(2)));
		}
	}
	$("#box-two-content").bind("plothover",  function (event, pos, item) {
		latestPosition = pos;
		if (!updateLegendTimeout) {
			updateLegendTimeout = setTimeout(updateLegend, 50);
		}
	});
}
//
// Graph3 created in element with id = box-three-content
//
function FlotGraph3(){
	var d1 = [];
	for (var i = 0; i <= 60; i += 1) {
		d1.push([i, parseInt(Math.random() * 30 - 10)]);
	}
	function plotWithOptions(t) {
		$.plot("#box-three-content", [{
			data: d1,
			color: "rgb(30, 180, 20)",
			threshold: {
				below: t,
				color: "rgb(200, 20, 30)"
			},
			lines: {
				steps: true
			}
		}]);
	}
	plotWithOptions(0);
}
//
// Graph4 created in element with id = box-four-content
//
function FlotGraph4(){
	var d1 = [];
	for (var i = 0; i < 14; i += 0.5) {
		d1.push([i, Math.sin(i)]);
	}
	var d2 = [[0, 3], [4, 8], [8, 5], [9, 13]];
	var d3 = [];
	for (var i = 0; i < 14; i += 0.5) {
		d3.push([i, Math.cos(i)]);
	}
	var d4 = [];
	for (var i = 0; i < 14; i += 0.1) {
		d4.push([i, Math.sqrt(i * 10)]);
	}
	var d5 = [];
	for (var i = 0; i < 14; i += 0.5) {
		d5.push([i, Math.sqrt(i)]);
	}
	var d6 = [];
	for (var i = 0; i < 14; i += 0.5 + Math.random()) {
		d6.push([i, Math.sqrt(2*i + Math.sin(i) + 5)]);
	}
	$.plot("#box-four-content", [{
		data: d1,
			lines: { show: true, fill: true }
		}, {
			data: d2,
			bars: { show: true }
		}, {
			data: d3,
			points: { show: true }
		}, {
			data: d4,
			lines: { show: true }
		}, {
			data: d5,
			lines: { show: true },
			points: { show: true }
		}, {
			data: d6,
			lines: { show: true, steps: true }
		}]);
}
/*-------------------------------------------
	Demo graphs for Morris Chart page (charts_morris.html)
---------------------------------------------*/
//
// Graph1 created in element with id = morris-chart-1
//
function MorrisChart1(){
	var day_data = [
		{"period": "2013-10-01", "licensed": 3407, "sorned": 660},
		{"period": "2013-09-30", "licensed": 3351, "sorned": 629},
		{"period": "2013-09-29", "licensed": 3269, "sorned": 618},
		{"period": "2013-09-20", "licensed": 3246, "sorned": 661},
		{"period": "2013-09-19", "licensed": 3257, "sorned": 667},
		{"period": "2013-09-18", "licensed": 3248, "sorned": 627},
		{"period": "2013-09-17", "licensed": 3171, "sorned": 660},
		{"period": "2013-09-16", "licensed": 3171, "sorned": 676},
		{"period": "2013-09-15", "licensed": 3201, "sorned": 656},
		{"period": "2013-09-10", "licensed": 3215, "sorned": 622}
	];
	Morris.Bar({
		element: 'morris-chart-1',
		data: day_data,
		xkey: 'period',
		ykeys: ['licensed', 'sorned'],
		labels: ['Licensed', 'SORN'],
		xLabelAngle: 60
	});
}
//
// Graph2 created in element with id = morris-chart-2
//
function MorrisChart2(){
	// Use Morris.Area instead of Morris.Line
	Morris.Area({
		element: 'morris-chart-2',
		data: [
			{x: '2011 Q1', y: 3, z: 3, m: 1},
			{x: '2011 Q2', y: 2, z: 0, m: 7},
			{x: '2011 Q3', y: 2, z: 5, m: 2},
			{x: '2011 Q4', y: 4, z: 4, m: 5},
			{x: '2012 Q1', y: 6, z: 1, m: 11},
			{x: '2012 Q2', y: 4, z: 4, m: 3},
			{x: '2012 Q3', y: 4, z: 4, m: 7},
			{x: '2012 Q4', y: 4, z: 4, m: 9}
		],
		xkey: 'x',
		ykeys: ['y', 'z', 'm'],
		labels: ['Y', 'Z', 'M']
		})
		.on('click', function(i, row){
			console.log(i, row);
		});
}
//
// Graph3 created in element with id = morris-chart-3
//
function MorrisChart3(){
	var decimal_data = [];
	for (var x = 0; x <= 360; x += 10) {
		decimal_data.push({ x: x, y: Math.sin(Math.PI * x / 180).toFixed(4), z: Math.cos(Math.PI * x / 180).toFixed(4) });
	}
	Morris.Line({
		element: 'morris-chart-3',
		data: decimal_data,
		xkey: 'x',
		ykeys: ['y', 'z'],
		labels: ['sin(x)', 'cos(x)'],
		parseTime: false,
		goals: [-1, 0, 1]
	});
}
//
// Graph4 created in element with id = morris-chart-4
//
function MorrisChart4(){
	// Use Morris.Bar
	Morris.Bar({
		element: 'morris-chart-4',
		data: [
			{x: '2011 Q1', y: 0},
			{x: '2011 Q2', y: 1},
			{x: '2011 Q3', y: 2},
			{x: '2011 Q4', y: 3},
			{x: '2012 Q1', y: 4},
			{x: '2012 Q2', y: 5},
			{x: '2012 Q3', y: 6},
			{x: '2012 Q4', y: 7},
			{x: '2013 Q1', y: 8},
			{x: '2013 Q2', y: 7},
			{x: '2013 Q3', y: 6},
			{x: '2013 Q4', y: 5},
			{x: '2014 Q1', y: 9}
		],
		xkey: 'x',
		ykeys: ['y'],
		labels: ['Y'],
		barColors: function (row, series, type) {
			if (type === 'bar') {
				var red = Math.ceil(255 * row.y / this.ymax);
				return 'rgb(' + red + ',0,0)';
			}
			else {
				return '#000';
			}
		}
	});
}
//
// Graph5 created in element with id = morris-chart-5
//
function MorrisChart5(){
	Morris.Area({
		element: 'morris-chart-5',
		data: [
			{period: '2010 Q1', iphone: 2666, ipad: null, itouch: 2647},
			{period: '2010 Q2', iphone: 2778, ipad: 2294, itouch: 2441},
			{period: '2010 Q3', iphone: 4912, ipad: 1969, itouch: 2501},
			{period: '2010 Q4', iphone: 3767, ipad: 3597, itouch: 5689},
			{period: '2011 Q1', iphone: 6810, ipad: 1914, itouch: 2293},
			{period: '2011 Q2', iphone: 5670, ipad: 4293, itouch: 1881},
			{period: '2011 Q3', iphone: 4820, ipad: 3795, itouch: 1588},
			{period: '2011 Q4', iphone: 15073, ipad: 5967, itouch: 5175},
			{period: '2012 Q1', iphone: 10687, ipad: 4460, itouch: 2028},
			{period: '2012 Q2', iphone: 8432, ipad: 5713, itouch: 1791}
		],
		xkey: 'period',
		ykeys: ['iphone', 'ipad', 'itouch'],
		labels: ['iPhone', 'iPad', 'iPod Touch'],
		pointSize: 2,
		hideHover: 'auto'
	});
}
/*-------------------------------------------
	Demo graphs for Google Chart page (charts_google.html)
---------------------------------------------*/
//
// One function for create all graphs on Google Chart page
//
function DrawAllCharts(){
	//  Chart 1
	var chart1_data = [
		['Smartphones', 'PC', 'Notebooks', 'Monitors','Routers', 'Switches' ],
		['01.01.2014',  1234, 2342, 344, 232,131],
		['02.01.2014',  1254, 232, 314, 232, 331],
		['03.01.2014',  2234, 342, 298, 232, 665],
		['04.01.2014',  2234, 42, 559, 232, 321],
		['05.01.2014',  1999, 82, 116, 232, 334],
		['06.01.2014',  1634, 834, 884, 232, 191],
		['07.01.2014',  321, 342, 383, 232, 556],
		['08.01.2014',  845, 112, 499, 232, 731]
	];
	var chart1_options = {
		title: 'Sales of company',
		hAxis: {title: 'Date', titleTextStyle: {color: 'red'}},
		backgroundColor: '#fcfcfc',
		vAxis: {title: 'Quantity', titleTextStyle: {color: 'blue'}}
	};
	var chart1_element = 'google-chart-1';
	var chart1_type = google.visualization.ColumnChart;
	drawGoogleChart(chart1_data, chart1_options, chart1_element, chart1_type);
	//  Chart 2
	var chart2_data = [
		['Height', 'Width'],
		['Samsung',  74.5],
		['Apple',  31.24],
		['LG',  12.10],
		['Huawei',  11.14],
		['Sony',  8.3],
		['Nokia',  7.4],
		['Blackberry',  6.8],
		['HTC',  6.63],
		['Motorola',  3.5],
		['Other',  43.15]
	];
	var chart2_options = {
		title: 'Smartphone marketshare 2Q 2013',
		backgroundColor: '#fcfcfc'
	};
	var chart2_element = 'google-chart-2';
	var chart2_type = google.visualization.PieChart;
	drawGoogleChart(chart2_data, chart2_options, chart2_element, chart2_type);
	//  Chart 3
	var chart3_data = [
		['Age', 'Weight'],
		[ 8, 12],
		[ 4, 5.5],
		[ 11, 14],
		[ 4, 5],
		[ 3, 3.5],
		[ 6.5, 7]
	];
	var chart3_options = {
		title: 'Age vs. Weight comparison',
		hAxis: {title: 'Age', minValue: 0, maxValue: 15},
		vAxis: {title: 'Weight', minValue: 0, maxValue: 15},
		legend: 'none',
		backgroundColor: '#fcfcfc'
	};
	var chart3_element = 'google-chart-3';
	var chart3_type = google.visualization.ScatterChart;
	drawGoogleChart(chart3_data, chart3_options, chart3_element, chart3_type);
	//  Chart 4
	var chart4_data = [
		['ID', 'Life Expectancy', 'Fertility Rate', 'Region',     'Population'],
		['CAN',    80.66,              1.67,      'North America',  33739900],
		['DEU',    79.84,              1.36,      'Europe',         81902307],
		['DNK',    78.6,               1.84,      'Europe',         5523095],
		['EGY',    72.73,              2.78,      'Middle East',    79716203],
		['GBR',    80.05,              2,         'Europe',         61801570],
		['IRN',    72.49,              1.7,       'Middle East',    73137148],
		['IRQ',    68.09,              4.77,      'Middle East',    31090763],
		['ISR',    81.55,              2.96,      'Middle East',    7485600],
		['RUS',    68.6,               1.54,      'Europe',         141850000],
		['USA',    78.09,              2.05,      'North America',  307007000]
	];
	var chart4_options = {
		title: 'Correlation between life expectancy, fertility rate and population of some world countries (2010)',
		hAxis: {title: 'Life Expectancy'},
		vAxis: {title: 'Fertility Rate'},
		backgroundColor: '#fcfcfc',
		bubble: {textStyle: {fontSize: 11}}
	};
	var chart4_element = 'google-chart-4';
	var chart4_type = google.visualization.BubbleChart;
	drawGoogleChart(chart4_data, chart4_options, chart4_element, chart4_type);
	//  Chart 5
	var chart5_data = [
		['Country', 'Popularity'],
		['Germany', 200],
		['United States', 300],
		['Brazil', 400],
		['Canada', 500],
		['France', 600],
		['RU', 700]
	];
	var chart5_options = {
		backgroundColor: '#fcfcfc',
		enableRegionInteractivity: true
	};
	var chart5_element = 'google-chart-5';
	var chart5_type = google.visualization.GeoChart;
	drawGoogleChart(chart5_data, chart5_options, chart5_element, chart5_type);
	//  Chart 6
	var chart6_data = [
	['Year', 'Sales', 'Expenses'],
		['2004',  1000,      400],
		['2005',  1170,      460],
		['2006',  660,       1120],
		['2007',  1030,      540],
		['2008',  2080,      740],
		['2009',  1949,      690],
		['2010',  2334,      820]
	];
	var chart6_options = {
		backgroundColor: '#fcfcfc',
		title: 'Company Performance'
	};
	var chart6_element = 'google-chart-6';
	var chart6_type = google.visualization.LineChart;
	drawGoogleChart(chart6_data, chart6_options, chart6_element, chart6_type);
	//  Chart 7
	var chart7_data = [
	['Task', 'Hours per Day'],
		['Work',     11],
		['Eat',      2],
		['Commute',  2],
		['Watch TV', 2],
		['Sleep',    7]
	];
	var chart7_options = {
		backgroundColor: '#fcfcfc',
		title: 'My Daily Activities',
		pieHole: 0.4
	};
	var chart7_element = 'google-chart-7';
	var chart7_type = google.visualization.PieChart;
	drawGoogleChart(chart7_data, chart7_options, chart7_element, chart7_type);
	//  Chart 8
	var chart8_data = [
		['Generation', 'Descendants'],
		[0, 1], [1, 33], [2, 269], [3, 2013]
	];
	var chart8_options = {
		backgroundColor: '#fcfcfc',
		title: 'Descendants by Generation',
		hAxis: {title: 'Generation', minValue: 0, maxValue: 3},
		vAxis: {title: 'Descendants', minValue: 0, maxValue: 2100},
		trendlines: {
			0: {
				type: 'exponential',
				visibleInLegend: true
			}
		}
	};
	var chart8_element = 'google-chart-8';
	var chart8_type = google.visualization.ScatterChart;
	drawGoogleChart(chart8_data, chart8_options, chart8_element, chart8_type);
}
/*-------------------------------------------
	Demo graphs for xCharts page (charts_xcharts.html)
---------------------------------------------*/
//
// Graph1 created in element with id = xchart-1
//
function xGraph1(){
	var tt = document.createElement('div'),
	leftOffset = -(~~$('html').css('padding-left').replace('px', '') + ~~$('body').css('margin-left').replace('px', '')),
	topOffset = -32;
	tt.className = 'ex-tooltip';
	document.body.appendChild(tt);
	var data = {
		"xScale": "time",
		"yScale": "linear",
		"main": [
			{
			"className": ".xchart-class-1",
			"data": [
				{
				  "x": "2012-11-05",
				  "y": 6
				},
				{
				  "x": "2012-11-06",
				  "y": 6
				},
				{
				  "x": "2012-11-07",
				  "y": 8
				},
				{
				  "x": "2012-11-08",
				  "y": 3
				},
				{
				  "x": "2012-11-09",
				  "y": 4
				},
				{
				  "x": "2012-11-10",
				  "y": 9
				},
				{
				  "x": "2012-11-11",
				  "y": 6
				},
				{
				  "x": "2012-11-12",
				  "y": 16
				},
				{
				  "x": "2012-11-13",
				  "y": 4
				},
				{
				  "x": "2012-11-14",
				  "y": 9
				},
				{
				  "x": "2012-11-15",
				  "y": 2
				}
			]
			}
		]
	};
	var opts = {
		"dataFormatX": function (x) { return d3.time.format('%Y-%m-%d').parse(x); },
		"tickFormatX": function (x) { return d3.time.format('%A')(x); },
		"mouseover": function (d, i) {
			var pos = $(this).offset();
			$(tt).text(d3.time.format('%A')(d.x) + ': ' + d.y)
				.css({top: topOffset + pos.top, left: pos.left + leftOffset})
				.show();
		},
		"mouseout": function (x) {
			$(tt).hide();
		}
	};
	var myChart = new xChart('line-dotted', data, '#xchart-1', opts);
}
//
// Graph2 created in element with id = xchart-2
//
function xGraph2(){
	var data = {
	"xScale": "ordinal",
	"yScale": "linear",
	"main": [
		{
		"className": ".xchart-class-2",
		"data": [
			{
			  "x": "Apple",
			  "y": 575
			},
			{
			  "x": "Facebook",
			  "y": 163
			},
			{
			  "x": "Microsoft",
			  "y": 303
			},
			{
			  "x": "Cisco",
			  "y": 121
			},
			{
			  "x": "Google",
			  "y": 393
			}
		]
		}
		]
	};
	var myChart = new xChart('bar', data, '#xchart-2');
}
//
// Graph3 created in element with id = xchart-3
//
function xGraph3(){
	var data = {
		"xScale": "time",
		"yScale": "linear",
		"type": "line",
		"main": [
		{
			"className": ".xchart-class-3",
			"data": [
				{
				  "x": "2012-11-05",
				  "y": 1
				},
				{
				  "x": "2012-11-06",
				  "y": 6
				},
				{
				  "x": "2012-11-07",
				  "y": 13
				},
				{
				  "x": "2012-11-08",
				  "y": -3
				},
				{
				  "x": "2012-11-09",
				  "y": -4
				},
				{
				  "x": "2012-11-10",
				  "y": 9
				},
				{
				  "x": "2012-11-11",
				  "y": 6
				},
				{
				  "x": "2012-11-12",
				  "y": 7
				},
				{
				  "x": "2012-11-13",
				  "y": -2
				},
				{
				  "x": "2012-11-14",
				  "y": -7
				}
			]
			}
		]
	};
	var opts = {
		"dataFormatX": function (x) { return d3.time.format('%Y-%m-%d').parse(x); },
		"tickFormatX": function (x) { return d3.time.format('%A')(x); }
	};
	var myChart = new xChart('line', data, '#xchart-3', opts);
}
/*-------------------------------------------
	Demo graphs for CoinDesk page (charts_coindesk.html)
---------------------------------------------*/
//
// Main function for CoinDesk API Page
// (we get JSON data and make 4 graph from this)
//
function CoinDeskGraph(){
	var dates = PrettyDates();
	var startdate = dates[0];
	var enddate = dates[1];
	// Load JSON data from CoinDesk API
	var jsonURL = 'http://api.coindesk.com/v1/bpi/historical/close.json?start='+startdate+'&end='+enddate;
	$.getJSON(jsonURL, function(result){
		// Create array of data for xChart
		$.each(result.bpi, function(key, val){
			xchart_data.push({'x': key,'y':val});
		});
		// Set handler for resize and create xChart plot
		var graphXChartResize;
		$('#coindesk-xchart').resize(function(){
			clearTimeout(graphXChartResize);
			graphXChartResize = setTimeout(DrawCoinDeskXCharts, 500);
		});
		DrawCoinDeskXCharts();
		// Create array of data for Google Chart
			$.each(result.bpi, function(key, val){
				google_data.push([key,val]);
			});
		// Set handler for resize and create Google Chart plot
		var graphGChartResize;
		$('#coindesk-google-chart').resize(function(){
			clearTimeout(graphGChartResize);
			graphGChartResize = setTimeout(DrawCoinDeskGoogleCharts, 500);
		});
		DrawCoinDeskGoogleCharts();
		// Create array of data for Flot and Sparkline
		$.each(result.bpi, function(key, val){
			var parseDate=key;
			parseDate=parseDate.split("-");
			var newDate=parseDate[1]+"/"+parseDate[2]+"/"+parseDate[0];
			var new_date = new Date(newDate).getTime();
			exchange_rate.push([new_date,val]);
		});
		// Create Flot plot (not need bind to resize, cause Flot use plugin 'resize')
		DrawCoinDeskFlot();
		// Set handler for resize and create Sparkline plot
		var graphSparklineResize;
		$('#coindesk-sparklines').resize(function(){
			clearTimeout(graphSparklineResize);
			graphSparklineResize = setTimeout(DrawCoinDeskSparkLine, 500);
		});
		DrawCoinDeskSparkLine();
	});
}
//
// Draw Sparkline Graph on Coindesk page
//
function DrawCoinDeskSparkLine(){
	$('#coindesk-sparklines').sparkline(exchange_rate, { height: '100%', width: '100%' });
}
//
// Draw xChart Graph on Coindesk page
//
function DrawCoinDeskXCharts(){
	var data = {
		"xScale": "ordinal",
		"yScale": "linear",
		"main": [
			{
			  "className": ".pizza",
			  "data": xchart_data
			}
		  ]
		};
	var myChart = new xChart('line-dotted', data, '#coindesk-xchart');
}
//
// Draw Flot Graph on Coindesk page
//
function DrawCoinDeskFlot(){
	var data1 = [
		{ data: exchange_rate, label: "Bitcoin exchange rate ($)" }
	];
	var options = {
		canvas: true,
		xaxes: [
			{ mode: "time" }
		],
		yaxes: [
			{ min: 0 },
			{
				position: "right",
				alignTicksWithAxis: 1,
				tickFormatter: function (value, axis) {
					return value.toFixed(axis.tickDecimals) + "€";
				}
			}
		],
		legend: { position: "sw" }
	};
	$.plot("#coindesk-flot", data1, options);
}
//
// Draw Google Chart Graph on Coindesk page
//
function DrawCoinDeskGoogleCharts(){
	var google_options = {
		backgroundColor: '#fcfcfc',
		title: 'Coindesk Exchange Rate'
	};
	var google_element = 'coindesk-google-chart';
	var google_type = google.visualization.LineChart;
	drawGoogleChart(google_data, google_options, google_element, google_type);
}
/*-------------------------------------------
	Scripts for DataTables page (tables_datatables.html)
---------------------------------------------*/
//
// Function for table, located in element with id = datatable-1
//
function TestTable1(){
	$('#datatable-1').dataTable( {
		"aaSorting": [[ 0, "asc" ]],
		"sDom": "<'box-content'<'col-sm-6'f><'col-sm-6 text-right'l><'clearfix'>>rt<'box-content'<'col-sm-6'i><'col-sm-6 text-right'p><'clearfix'>>",
		"sPaginationType": "bootstrap",
		"oLanguage": {
			"sSearch": "",
			"sLengthMenu": '_MENU_'
		}
	});
}
//
// Function for table, located in element with id = datatable-2
//
function TestTable2(){
	var asInitVals = [];
	var oTable = $('#datatable-2').dataTable( {
		"aaSorting": [[ 0, "asc" ]],
		"sDom": "<'box-content'<'col-sm-6'f><'col-sm-6 text-right'l><'clearfix'>>rt<'box-content'<'col-sm-6'i><'col-sm-6 text-right'p><'clearfix'>>",
		"sPaginationType": "bootstrap",
		"oLanguage": {
			"sSearch": "",
			"sLengthMenu": '_MENU_'
		},
		bAutoWidth: false
	});
	var header_inputs = $("#datatable-2 thead input");
	header_inputs.on('keyup', function(){
		/* Filter on the column (the index) of this element */
		oTable.fnFilter( this.value, header_inputs.index(this) );
	})
	.on('focus', function(){
		if ( this.className == "search_init" ){
			this.className = "";
			this.value = "";
		}
	})
	.on('blur', function (i) {
		if ( this.value == "" ){
			this.className = "search_init";
			this.value = asInitVals[header_inputs.index(this)];
		}
	});
	header_inputs.each( function (i) {
		asInitVals[i] = this.value;
	});
}
//
// Function for table, located in element with id = datatable-3
//
function TestTable3(){
	$('#datatable-3').dataTable( {
		"aaSorting": [[ 0, "asc" ]],
		"sDom": "T<'box-content'<'col-sm-6'f><'col-sm-6 text-right'l><'clearfix'>>rt<'box-content'<'col-sm-6'i><'col-sm-6 text-right'p><'clearfix'>>",
		"sPaginationType": "bootstrap",
		"oLanguage": {
			"sSearch": "",
			"sLengthMenu": '_MENU_'
		},
		"oTableTools": {
			"sSwfPath": "plugins/datatables/copy_csv_xls_pdf.swf",
			"aButtons": [
				"copy",
				"print",
				{
					"sExtends":    "collection",
					"sButtonText": 'Save <span class="caret" />',
					"aButtons":    [ "csv", "xls", "pdf" ]
				}
			]
		}
	});
}
/*-------------------------------------------
	Functions for Dashboard page (dashboard.html)
---------------------------------------------*/
//
// Helper for random change data (only test data for Sparkline plots)
//
function SmallChangeVal(val) {
	var new_val = Math.floor(100*Math.random());
	var plusOrMinus = Math.random() < 0.5 ? -1 : 1;
	var result = val[0]+new_val*plusOrMinus;
	if (parseInt(result) > 1000){
		return [val[0] - new_val]
	}
	if (parseInt(result) < 0){
		return [val[0] + new_val]
	}
	return [result];
}
//
// Make array of random data
//
function SparklineTestData(){
	var arr = [];
	for (var i=1; i<9; i++){
		arr.push([Math.floor(1000*Math.random())])
	}
	return arr;
}
//
// Redraw Knob charts on Dashboard (panel- servers)
//
function RedrawKnob(elem){
	elem.animate({
		value: Math.floor(100*Math.random())
	},{
		duration: 3000,
		easing:'swing',
		progress: function()
		{
			$(this).val(parseInt(Math.ceil(elem.val()))).trigger('change');
		}
	});
}
//
// Draw 3 Sparkline plot in Dashboard header
//
function SparklineLoop(){
	SparkLineDrawBarGraph($('#sparkline-1'), sparkline_arr_1.map(SmallChangeVal));
	SparkLineDrawBarGraph($('#sparkline-2'), sparkline_arr_2.map(SmallChangeVal), '#7BC5D3');
	SparkLineDrawBarGraph($('#sparkline-3'), sparkline_arr_3.map(SmallChangeVal), '#B25050');
}
//
// Draw Morris charts on Dashboard (panel- Statistics + 3 donut)
//
function MorrisDashboard(){
	Morris.Line({
		element: 'stat-graph',
		data: [
			{"period": "2014-01", "Win8": 13.4, "Win7": 55.3, 'Vista': 1.5, 'NT': 0.3, 'XP':11, 'Linux': 4.9, 'Mac': 9.6 , 'Mobile':4},
			{"period": "2013-12", "Win8": 10, "Win7": 55.9, 'Vista': 1.5, 'NT': 3.1, 'XP':11.6, 'Linux': 4.8, 'Mac': 9.2 , 'Mobile':3.8},
			{"period": "2013-11", "Win8": 8.6, "Win7": 56.4, 'Vista': 1.6, 'NT': 3.7, 'XP':11.7, 'Linux': 4.8, 'Mac': 9.6 , 'Mobile':3.7},
			{"period": "2013-10", "Win8": 9.9, "Win7": 56.7, 'Vista': 1.6, 'NT': 1.4, 'XP':12.4, 'Linux': 4.9, 'Mac': 9.6 , 'Mobile':3.3},
			{"period": "2013-09", "Win8": 10.2, "Win7": 56.8, 'Vista': 1.6, 'NT': 0.4, 'XP':13.5, 'Linux': 4.8, 'Mac': 9.3 , 'Mobile':3.3},
			{"period": "2013-08", "Win8": 9.6, "Win7": 55.9, 'Vista': 1.7, 'NT': 0.4, 'XP':14.7, 'Linux': 5, 'Mac': 9.2 , 'Mobile':3.4},
			{"period": "2013-07", "Win8": 9, "Win7": 56.2, 'Vista': 1.8, 'NT': 0.4, 'XP':15.8, 'Linux': 4.9, 'Mac': 8.7 , 'Mobile':3.2},
			{"period": "2013-06", "Win8": 8.6, "Win7": 56.3, 'Vista': 2, 'NT': 0.4, 'XP':15.4, 'Linux': 4.9, 'Mac': 9.1 , 'Mobile':3.2},
			{"period": "2013-05", "Win8": 7.9, "Win7": 56.4, 'Vista': 2.1, 'NT': 0.4, 'XP':15.7, 'Linux': 4.9, 'Mac': 9.7 , 'Mobile':2.6},
			{"period": "2013-04", "Win8": 7.3, "Win7": 56.4, 'Vista': 2.2, 'NT': 0.4, 'XP':16.4, 'Linux': 4.8, 'Mac': 9.7 , 'Mobile':2.2},
			{"period": "2013-03", "Win8": 6.7, "Win7": 55.9, 'Vista': 2.4, 'NT': 0.4, 'XP':17.6, 'Linux': 4.7, 'Mac': 9.5 , 'Mobile':2.3},
			{"period": "2013-02", "Win8": 5.7, "Win7": 55.3, 'Vista': 2.4, 'NT': 0.4, 'XP':19.1, 'Linux': 4.8, 'Mac': 9.6 , 'Mobile':2.2},
			{"period": "2013-01", "Win8": 4.8, "Win7": 55.3, 'Vista': 2.6, 'NT': 0.5, 'XP':19.9, 'Linux': 4.8, 'Mac': 9.3 , 'Mobile':2.2}
		],
		xkey: 'period',
		ykeys: ['Win8', 'Win7','Vista','NT','XP', 'Linux', 'Mac', 'Mobile'],
		labels: ['Win8', 'Win7','Vista','NT','XP', 'Linux', 'Mac', 'Mobile']
	});
	Morris.Donut({
		element: 'morris_donut_1',
		data: [
			{value: 70, label: 'pay', formatted: 'at least 70%' },
			{value: 15, label: 'client', formatted: 'approx. 15%' },
			{value: 10, label: 'buy', formatted: 'approx. 10%' },
			{value: 5, label: 'hosted', formatted: 'at most 5%' }
		],
		formatter: function (x, data) { return data.formatted; }
	});
	Morris.Donut({
		element: 'morris_donut_2',
		data: [
			{value: 20, label: 'office', formatted: 'current' },
			{value: 35, label: 'store', formatted: 'approx. 35%' },
			{value: 20, label: 'shop', formatted: 'approx. 20%' },
			{value: 25, label: 'cars', formatted: 'at most 25%' }
		],
		formatter: function (x, data) { return data.formatted; }
	});
	Morris.Donut({
		element: 'morris_donut_3',
		data: [
			{value: 17, label: 'current', formatted: 'current' },
			{value: 22, label: 'week', formatted: 'last week' },
			{value: 10, label: 'month', formatted: 'last month' },
			{value: 25, label: 'period', formatted: 'period' },
			{value: 25, label: 'year', formatted: 'this year' }
		],
		formatter: function (x, data) { return data.formatted; }
	});
}
//
// Draw SparkLine example Charts for Dashboard (table- Tickers)
//
function DrawSparklineDashboard(){
	SparklineLoop();
	setInterval(SparklineLoop, 1000);
	var sparkline_clients = [[309],[223], [343], [652], [455], [18], [912],[15]];
	$('.bar').each(function(){
		$(this).sparkline(sparkline_clients.map(SmallChangeVal), {type: 'bar', barWidth: 5, highlightColor: '#000', barSpacing: 2, height: 30, stackedBarColor: '#6AA6D6'});
	});
	var sparkline_table = [ [1,341], [2,464], [4,564], [5,235], [6,335], [7,535], [8,642], [9,342], [10,765] ];
	$('.td-graph').each(function(){
		var arr = $.map( sparkline_table, function(val, index) {
			return [[val[0], SmallChangeVal([val[1]])]];
		});
		$(this).sparkline( arr ,
			{defaultPixelsPerValue: 10, minSpotColor: null, maxSpotColor: null, spotColor: null,
			fillColor: false, lineWidth: 2, lineColor: '#5A8DB6'});
		});
}
//
// Draw Knob Charts for Dashboard (for servers)
//
function DrawKnobDashboard(){
	var srv_monitoring_selectors = [
		$("#knob-srv-1"),$("#knob-srv-2"),$("#knob-srv-3"),
		$("#knob-srv-4"),$("#knob-srv-5"),$("#knob-srv-6")
	];
	srv_monitoring_selectors.forEach(DrawKnob);
	setInterval(function(){
		srv_monitoring_selectors.forEach(RedrawKnob);
	}, 3000);
}
/*-------------------------------------------
	Function for File upload page (form_file_uploader.html)
---------------------------------------------*/
function FileUpload(){
	$('#bootstrapped-fine-uploader').fineUploader({
		template: 'qq-template-bootstrap',
		classes: {
			success: 'alert alert-success',
			fail: 'alert alert-error'
		},
		thumbnails: {
			placeholders: {
				waitingPath: "assets/waiting-generic.png",
				notAvailablePath: "assets/not_available-generic.png"
			}
		},
		request: {
			endpoint: 'server/handleUploads'
		},
		validation: {
			allowedExtensions: ['jpeg', 'jpg', 'gif', 'png']
		}
	});
}
/*-------------------------------------------
	Function for OpenStreetMap page (maps.html)
---------------------------------------------*/
//
// Load GeoIP JSON data and draw 3 maps
//
function LoadTestMap(){
	$.getJSON("http://www.telize.com/geoip?callback=?",
		function(json) {
			var osmap = new OpenLayers.Layer.OSM("OpenStreetMap");//создание слоя карты
			var googlestreets = new OpenLayers.Layer.Google("Google Streets", {numZoomLevels: 22,visibility: false});
			var googlesattelite = new OpenLayers.Layer.Google( "Google Sattelite", {type: google.maps.MapTypeId.SATELLITE, numZoomLevels: 22});
			var map1_layers = [googlestreets,osmap, googlesattelite];
			// Create map in element with ID - map-1
			var map1 = drawMap(json.longitude, json.latitude, "map-1", map1_layers);
			$("#map-1").resize(function(){ setTimeout(map1.updateSize(), 500); });
			// Create map in element with ID - map-2
			var osmap1 = new OpenLayers.Layer.OSM("OpenStreetMap");//создание слоя карты
			var map2_layers = [osmap1];
			var map2 = drawMap(json.longitude, json.latitude, "map-2", map2_layers);
			$("#map-2").resize(function(){ setTimeout(map2.updateSize(), 500); });
			// Create map in element with ID - map-3
			var sattelite = new OpenLayers.Layer.Google( "Google Sattelite", {type: google.maps.MapTypeId.SATELLITE, numZoomLevels: 22});
			var map3_layers = [sattelite];
			var map3 = drawMap(json.longitude, json.latitude, "map-3", map3_layers);
			$("#map-3").resize(function(){ setTimeout(map3.updateSize(), 500); });
		}
	);
}
/*-------------------------------------------
	Function for Fullscreen Map page (map_fullscreen.html)
---------------------------------------------*/
//
// Create Fullscreen Map
//
function FullScreenMap(){
	$.getJSON("http://www.telize.com/geoip?callback=?",
		function(json) {
			var osmap = new OpenLayers.Layer.OSM("OpenStreetMap");//создание слоя карты
			var googlestreets = new OpenLayers.Layer.Google("Google Streets", {numZoomLevels: 22,visibility: false});
			var googlesattelite = new OpenLayers.Layer.Google( "Google Sattelite", {type: google.maps.MapTypeId.SATELLITE, numZoomLevels: 22});
			var map1_layers = [googlestreets,osmap, googlesattelite];
			var map_fs = drawMap(json.longitude, json.latitude, "full-map", map1_layers);
		}
	);
}
/*-------------------------------------------
	Function for Flickr Gallery page (gallery_flickr.html)
---------------------------------------------*/
//
// Load data from Flicks, parse and create gallery
//
function displayFlickrImages(data){
	var res;
	$.each(data.items, function(i,item){
		if (i >11) { return false;}
		res = "<a href=" + item.link + " title=" + item.title + " target=\"_blank\"><img alt=" + item.title + " src=" + item.media.m + " /></a>";
		$('#box-one-content').append(res);
		});
		setTimeout(function(){
			$("#box-one-content").justifiedGallery({
				'usedSuffix':'lt240',
				'justifyLastRow':true,
				'rowHeight':150,
				'fixedHeight':false,
				'captions':true,
				'margins':1
				});
			$('#box-one-content').fadeIn('slow');
		}, 100);
}
/*-------------------------------------------
	Function for Form Layout page (form layouts.html)
---------------------------------------------*/
//
// Example form validator function
//
function DemoFormValidator(){
	$('#defaultForm').bootstrapValidator({
		message: 'This value is not valid',
		fields: {
			username: {
				message: 'The username is not valid',
				validators: {
					notEmpty: {
						message: 'The username is required and can\'t be empty'
					},
					stringLength: {
						min: 6,
						max: 30,
						message: 'The username must be more than 6 and less than 30 characters long'
					},
					regexp: {
						regexp: /^[a-zA-Z0-9_\.]+$/,
						message: 'The username can only consist of alphabetical, number, dot and underscore'
					}
				}
			},
			country: {
				validators: {
					notEmpty: {
						message: 'The country is required and can\'t be empty'
					}
				}
			},
			acceptTerms: {
				validators: {
					notEmpty: {
						message: 'You have to accept the terms and policies'
					}
				}
			},
			email: {
				validators: {
					notEmpty: {
						message: 'The email address is required and can\'t be empty'
					},
					emailAddress: {
						message: 'The input is not a valid email address'
					}
				}
			},
			website: {
				validators: {
					uri: {
						message: 'The input is not a valid URL'
					}
				}
			},
			phoneNumber: {
				validators: {
					digits: {
						message: 'The value can contain only digits'
					}
				}
			},
			color: {
				validators: {
					hexColor: {
						message: 'The input is not a valid hex color'
					}
				}
			},
			zipCode: {
				validators: {
					usZipCode: {
						message: 'The input is not a valid US zip code'
					}
				}
			},
			password: {
				validators: {
					notEmpty: {
						message: 'The password is required and can\'t be empty'
					},
					identical: {
						field: 'confirmPassword',
						message: 'The password and its confirm are not the same'
					}
				}
			},
			confirmPassword: {
				validators: {
					notEmpty: {
						message: 'The confirm password is required and can\'t be empty'
					},
					identical: {
						field: 'password',
						message: 'The password and its confirm are not the same'
					}
				}
			},
			ages: {
				validators: {
					lessThan: {
						value: 100,
						inclusive: true,
						message: 'The ages has to be less than 100'
					},
					greaterThan: {
						value: 10,
						inclusive: false,
						message: 'The ages has to be greater than or equals to 10'
					}
				}
			}
		}
	});
}
//
// Function for Dynamically Change input size on Form Layout page
//
function FormLayoutExampleInputLength(selector){
	var steps = [
		"col-sm-1",
		"col-sm-2",
		"col-sm-3",
		"col-sm-4",
		"col-sm-5",
		"col-sm-6",
		"col-sm-7",
		"col-sm-8",
		"col-sm-9",
		"col-sm-10",
		"col-sm-11",
		"col-sm-12"
	];
	selector.slider({
	   range: 'min',
		value: 1,
		min: 0,
		max: 11,
		step: 1,
		slide: function(event, ui) {
			if (ui.value < 1) {
				return false;
			}
			var input = $("#form-styles");
			var f = input.parent();
			f.removeClass();
			f.addClass(steps[ui.value]);
			input.attr("placeholder",'.'+steps[ui.value]);
		}
	});
}
/*-------------------------------------------
	Functions for Progressbar page (ui_progressbars.html)
---------------------------------------------*/
//
// Function for Knob clock
//
function RunClock() {
	var second = $(".second");
	var minute = $(".minute");
	var hour = $(".hour");
	var d = new Date();
	var s = d.getSeconds();
	var m = d.getMinutes();
	var h = d.getHours();
	if (h > 11) {h = h-12;}
		$('#knob-clock-value').html(h+':'+m+':'+s);
		second.val(s).trigger("change");
		minute.val(m).trigger("change");
		hour.val(h).trigger("change");
}
//
// Function for create test sliders on Progressbar page
//
function CreateAllSliders(){
	$(".slider-default").slider();
	var slider_range_min_amount = $(".slider-range-min-amount");
	var slider_range_min = $(".slider-range-min");
	var slider_range_max = $(".slider-range-max");
	var slider_range_max_amount = $(".slider-range-max-amount");
	var slider_range = $(".slider-range");
	var slider_range_amount = $(".slider-range-amount");
	slider_range_min.slider({
		range: "min",
		value: 37,
		min: 1,
		max: 700,
		slide: function( event, ui ) {
			slider_range_min_amount.val( "$" + ui.value );
		}
	});
	slider_range_min_amount.val("$" + slider_range_min.slider( "value" ));
	slider_range_max.slider({
		range: "max",
		min: 1,
		max: 100,
		value: 2,
		slide: function( event, ui ) {
			slider_range_max_amount.val( ui.value );
		}
	});
	slider_range_max_amount.val(slider_range_max.slider( "value" ));
	slider_range.slider({
		range: true,
		min: 0,
		max: 500,
		values: [ 75, 300 ],
		slide: function( event, ui ) {
			slider_range_amount.val( "$" + ui.values[ 0 ] + " - $" + ui.values[ 1 ] );
		}
	});
	slider_range_amount.val( "$" + slider_range.slider( "values", 0 ) +
	  " - $" + slider_range.slider( "values", 1 ) );
	$( "#equalizer > div.progress > div" ).each(function() {
		// read initial values from markup and remove that
		var value = parseInt( $( this ).text(), 10 );
		$( this ).empty().slider({
			value: value,
			range: "min",
			animate: true,
			orientation: "vertical"
		});
	});
}
/*-------------------------------------------
	Function for jQuery-UI page (ui_jquery-ui.html)
---------------------------------------------*/
//
// Function for make all Date-Time pickers on page
//
function AllTimePickers(){
	$('#datetime_example').datetimepicker({});
	$('#time_example').timepicker({
		hourGrid: 4,
		minuteGrid: 10,
		timeFormat: 'hh:mm tt'
	});
	$('#date3_example').datepicker({ numberOfMonths: 3, showButtonPanel: true});
	$('#date3-1_example').datepicker({ numberOfMonths: 3, showButtonPanel: true});
	$('#date_example').datepicker({});
}
/*-------------------------------------------
	Function for Calendar page (calendar.html)
---------------------------------------------*/
//
// Example form validator function
//
function DrawCalendar(){
	/* initialize the external events
	-----------------------------------------------------------------*/
	$('#external-events div.external-event').each(function() {
		// create an Event Object (http://arshaw.com/fullcalendar/docs/event_data/Event_Object/)
		var eventObject = {
			title: $.trim($(this).text()) // use the element's text as the event title
		};
		// store the Event Object in the DOM element so we can get to it later
		$(this).data('eventObject', eventObject);
		// make the event draggable using jQuery UI
		$(this).draggable({
			zIndex: 999,
			revert: true,      // will cause the event to go back to its
			revertDuration: 0  //  original position after the drag
		});
	});
	/* initialize the calendar
	-----------------------------------------------------------------*/
	var calendar = $('#calendar').fullCalendar({
		header: {
			left: 'prev,next today',
			center: 'title',
			right: 'month,agendaWeek,agendaDay'
		},
		selectable: true,
		selectHelper: true,
		select: function(start, end, allDay) {
			var form = $('<form id="event_form">'+
				'<div class="form-group has-success has-feedback">'+
				'<label">Event name</label>'+
				'<div>'+
				'<input type="text" id="newevent_name" class="form-control" placeholder="Name of event">'+
				'</div>'+
				'<label>Description</label>'+
				'<div>'+
				'<textarea rows="3" id="newevent_desc" class="form-control" placeholder="Description"></textarea>'+
				'</div>'+
				'</div>'+
				'</form>');
			var buttons = $('<button id="event_cancel" type="cancel" class="btn btn-default btn-label-left">'+
							'<span><i class="fa fa-clock-o txt-danger"></i></span>'+
							'Cancel'+
							'</button>'+
							'<button type="submit" id="event_submit" class="btn btn-primary btn-label-left pull-right">'+
							'<span><i class="fa fa-clock-o"></i></span>'+
							'Add'+
							'</button>');
			OpenModalBox('Add event', form, buttons);
			$('#event_cancel').on('click', function(){
				CloseModalBox();
			});
			$('#event_submit').on('click', function(){
				var new_event_name = $('#newevent_name').val();
				if (new_event_name != ''){
					calendar.fullCalendar('renderEvent',
						{
							title: new_event_name,
							description: $('#newevent_desc').val(),
							start: start,
							end: end,
							allDay: allDay
						},
						true // make the event "stick"
					);
				}
				CloseModalBox();
			});
			calendar.fullCalendar('unselect');
		},
		editable: true,
		droppable: true, // this allows things to be dropped onto the calendar !!!
		drop: function(date, allDay) { // this function is called when something is dropped
			// retrieve the dropped element's stored Event Object
			var originalEventObject = $(this).data('eventObject');
			// we need to copy it, so that multiple events don't have a reference to the same object
			var copiedEventObject = $.extend({}, originalEventObject);
			// assign it the date that was reported
			copiedEventObject.start = date;
			copiedEventObject.allDay = allDay;
			// render the event on the calendar
			// the last `true` argument determines if the event "sticks" (http://arshaw.com/fullcalendar/docs/event_rendering/renderEvent/)
			$('#calendar').fullCalendar('renderEvent', copiedEventObject, true);
			// is the "remove after drop" checkbox checked?
			if ($('#drop-remove').is(':checked')) {
				// if so, remove the element from the "Draggable Events" list
				$(this).remove();
			}
		},
		eventRender: function (event, element, icon) {
			if (event.description != "") {
				element.attr('title', event.description);
			}
		},
		eventClick: function(calEvent, jsEvent, view) {
			var form = $('<form id="event_form">'+
				'<div class="form-group has-success has-feedback">'+
				'<label">Event name</label>'+
				'<div>'+
				'<input type="text" id="newevent_name" value="'+ calEvent.title +'" class="form-control" placeholder="Name of event">'+
				'</div>'+
				'<label>Description</label>'+
				'<div>'+
				'<textarea rows="3" id="newevent_desc" class="form-control" placeholder="Description">'+ calEvent.description +'</textarea>'+
				'</div>'+
				'</div>'+
				'</form>');
			var buttons = $('<button id="event_cancel" type="cancel" class="btn btn-default btn-label-left">'+
							'<span><i class="fa fa-clock-o txt-danger"></i></span>'+
							'Cancel'+
							'</button>'+
							'<button id="event_delete" type="cancel" class="btn btn-danger btn-label-left">'+
							'<span><i class="fa fa-clock-o txt-danger"></i></span>'+
							'Delete'+
							'</button>'+
							'<button type="submit" id="event_change" class="btn btn-primary btn-label-left pull-right">'+
							'<span><i class="fa fa-clock-o"></i></span>'+
							'Save changes'+
							'</button>');
			OpenModalBox('Change event', form, buttons);
			$('#event_cancel').on('click', function(){
				CloseModalBox();
			});
			$('#event_delete').on('click', function(){
				calendar.fullCalendar('removeEvents' , function(ev){
					return (ev._id == calEvent._id);
				});
				CloseModalBox();
			});
			$('#event_change').on('click', function(){
				calEvent.title = $('#newevent_name').val();
				calEvent.description = $('#newevent_desc').val();
				calendar.fullCalendar('updateEvent', calEvent);
				CloseModalBox()
			});
		}
		});
		$('#new-event-add').on('click', function(event){
			event.preventDefault();
			var event_name = $('#new-event-title').val();
			var event_description = $('#new-event-desc').val();
			if (event_name != ''){
			var event_template = $('<div class="external-event" data-description="'+event_description+'">'+event_name+'</div>');
			$('#events-templates-header').after(event_template);
			var eventObject = {
				title: event_name,
				description: event_description
			};
			// store the Event Object in the DOM element so we can get to it later
			event_template.data('eventObject', eventObject);
			event_template.draggable({
				zIndex: 999,
				revert: true,
				revertDuration: 0
			});
			}
		});
}
//
// Load scripts and draw Calendar
//
function DrawFullCalendar(){
	LoadCalendarScript(DrawCalendar);
}
//////////////////////////////////////////////////////
//////////////////////////////////////////////////////
//
//      MAIN DOCUMENT READY SCRIPT OF DEVOOPS THEME
//
//      In this script main logic of theme
//
//////////////////////////////////////////////////////
//////////////////////////////////////////////////////
$(document).ready(function () {
	$('.show-sidebar').on('click', function (e) {
		e.preventDefault();
		$('div#main').toggleClass('sidebar-show');
		setTimeout(MessagesMenuWidth, 250);
	});
	$('#locked-screen').on('click', function (e) {
		e.preventDefault();
		$('body').addClass('body-screensaver');
		$('#screensaver').addClass("show");
		ScreenSaver();
	});
	$('body').on('click', 'a.close-link', function(e){
		e.preventDefault();
		CloseModalBox();
	});
});


