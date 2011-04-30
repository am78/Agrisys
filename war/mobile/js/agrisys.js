//var baseUrl = 'http://agri-sys.appspot.com';
//var baseUrl = 'http://192.168.178.26:8888';
var baseUrl = '';
var schlagData;
var actListData;
var stammdaten;
var currentSchlagErntejahrId;
var initialized = false;
var longitude = -1;
var latitude = -1;

//init
function init() {
	blockUI();
	//load stammdaten
	loadStammdaten();

	loadAndDisplaySchlagListData();
	
	/* add tap handlers */
	
	//called when Schlagliste is about to open
	$('#schlagliste').live('pagebeforeshow',function(event, ui){
		loadAndDisplaySchlagListData();
	});		

	//listener for tap/click on schlagliste item 
	$('#schlagliste li a').live('tap',function(event, ui){
		loadAndDisplayAktivitaetListData($(this).attr('id'));
	});
	$('#schlagliste li a').live('click',function(event, ui){
		loadAndDisplayAktivitaetListData($(this).attr('id'));
	});

	//listener for tap/click on aktivitaet liste item 
	$('#aktivitaetListe li a').live('tap',function(event, ui){
		loadAndDisplayActEntry($(this).attr('id'));
	});
	$('#aktivitaetListe li a').live('click',function(event, ui){
		loadAndDisplayActEntry($(this).attr('id'));
	});


	//add save button handlers
	$('#saveNewBoden').live('tap',function(event, ui){			
		onSaveNewBodenbearbeitung($('#newBodenForm'));
	});
	$('#saveNewBoden').live('click',function(event, ui){
		onSaveNewBodenbearbeitung($('#newBodenForm'));
	});

	$('#saveNewAussaat').live('tap',function(event, ui){			
		onSaveNewAussaat($('#newAussaatForm'));
	});
	$('#saveNewAussaat').live('click',function(event, ui){
		onSaveNewAussaat($('#newAussaatForm'));
	});

	$('#saveNewDuengung').live('tap',function(event, ui){			
		onSaveNewDuengung($('#newDuengungForm'));
	});
	$('#saveNewDuengung').live('click',function(event, ui){
		onSaveNewDuengung($('#newDuengungForm'));
	});

	$('#saveNewPflanzenschutz').live('tap',function(event, ui){			
		onSaveNewPflanzenschutz($('#newPflanzenschutzForm'));
	});
	$('#saveNewPflanzenschutz').live('click',function(event, ui){
		onSaveNewPflanzenschutz($('#newPflanzenschutzForm'));
	});

	$('#saveNewErnte').live('tap',function(event, ui){			
		onSaveNewErnte($('#newErnteForm'));
	});
	$('#saveNewErnte').live('click',function(event, ui){
		onSaveNewErnte($('#newErnteForm'));
	});
	
	$('#mainBtn a').live('click',function(event, ui){
		refreshGeoPosition();
	});
	

	//refresh the geo location
	refreshGeoPosition();
	
	unblockUI();
}

function loadStammdaten() {
	//the stammdaten URL
	var url = baseUrl + '/service/stammdaten?media=json';
	
	//load all schlagdata 
	//loadAndDisplaySchlagListData();
	
	blockUI();

	//load stammdaten
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
			stammdaten = data;
			unblockUI();
			//perform some post init task after the markup and the initial data has been loaded
			postInit();
		},
		error: function(error) { 
			unblockUI();
			alert("Fehler beim Laden der Stammdaten:\n" + error.status + " : " + error.statusText);
		}
	});
}

function refreshGeoPosition() {
	//get geo location (if feature is available)
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(geoSuccess, geoError, { enableHighAccuracy: true });
	} else {
		//console.log("Geo location Feature nicht unterst√ºtzt!");
	}
}

function geoSuccess(position) {
	latitude = position.coords.latitude;
	longitude = position.coords.longitude;
	//alert(position);
}
 
function geoError(msg) {
	//alert(typeof msg == 'string' ? msg : "error");
}

//perform some post init tasks after markup and initial data has been loaded
function postInit() {
	//add bodenbearbeitung types to input form
	var btlst = stammdaten.bodenbearbeitungTypList;
	if (btlst != null) {
		for (var i=0; i<btlst.length; i++) {
			var id = btlst[i].id;
			var name = btlst[i].name;
			var option = '<option value="' + id + '">' + name + '</option>';
			$('select[name=bodenbearbeitungTypInput]').append(option);
		}
	}
	
	//add duengerart items to input form
	var dlst = stammdaten.duengerartList;
	if (dlst != null) {
		for (var i=0; i<dlst.length; i++) {
			var id = dlst[i].id;
			var name = dlst[i].name;
			var option = '<option value="' + id + '">' + name + '</option>';
			$('select[name=duengerartInput]').append(option);
		}
	}
	
	//add Pflanzenschutzmittel item to input form
	var pslst = stammdaten.psMittelList;
	if (pslst != null) {
		for (var i=0; i<pslst.length; i++) {
			var id = pslst[i].id;
			var name = pslst[i].name;
			var option = '<option value="' + id + '">' + name + '</option>';
			$('select[name=psMittelInput]').append(option);
		}
	}
}

//add schlag entries to input forms
function postInit2() {
	if (!initialized && schlagData != null) {
		initialized = true;
		
		//add schlag items to all Aktivit√§t input forms
		for (var i=0; i<schlagData.length; i++) {
			var id = schlagData[i].schlagErntejahr.id;
			var name = schlagData[i].flurstueck.name;
			var option = '<option value="' + id + '">' + name + '</option>';
			$('select[name=schlagInput]').append(option);
		}
	}
}

function getDateString(date) {
	var day = date.getDate();
	var month = date.getMonth() + 1;
	var year = date.getFullYear();
	return day + '.' + month + '.' + year;
}

function getDouble(value) {
	if (value != null) {
		return value.replace(",", ".");
	}
	return value;
}

function onSaveNewBodenbearbeitung(form) {
	var type = 0;
	var fl = getDouble($('#newBodenForm input[name=flaeche]').val());
	var date = $('#newBodenForm input[name=dateInput]').val();
	var remark = $('#newBodenForm textarea[name=remark]').val();
	var bodenbearbeitungTyp = $('#newBodenForm select[name=bodenbearbeitungTypInput]').val();
	var schlagErntejahrId = $('#newBodenForm select[name=schlagInput]').val();;
	
	var data = '{"datum":"' + date + 
		'","type":"' + type + 
		'","flaeche":"' + fl + 
		'","schlagErntejahrId":"' + schlagErntejahrId +   
		'","bemerkung":"' + remark +
		'","longitude":"' + longitude +
		'","latitude":"' + latitude +
		'","bodenbearbeitungTyp":"' + bodenbearbeitungTyp + '"}';
	
	save(data);
}

function onSaveNewDuengung(form) {
	var type = 2;
	var fl = getDouble($('#newDuengungForm input[name=flaeche]').val());
	var date = $('#newDuengungForm input[name=dateInput]').val();
	var remark = $('#newBodenForm textarea[name=remark]').val();
	var duengerart = $('#newDuengungForm select[name=duengerartInput]').val();
	var schlagErntejahrId = $('#newDuengungForm select[name=schlagInput]').val();
	var kgProHa = getDouble($('#newDuengungForm input[name=kgProHa]').val());
	var ec = getDouble($('#newDuengungForm input[name=ec]').val());
	
	var data = '{"datum":"' + date + 
		'","type":"' + type + 
		'","flaeche":"' + fl + 
		'","schlagErntejahrId":"' + schlagErntejahrId +   
		'","bemerkung":"' + remark +
		'","ec":"' + ec +
		'","kgProHa":"' + kgProHa +
		'","longitude":"' + longitude +
		'","latitude":"' + latitude +
		'","duengerart":"' + duengerart + '"}';
	
	save(data);
}

function onSaveNewAussaat(form) {
	var type = 1;
	var fl = getDouble($('#newAussaatForm input[name=flaeche]').val());
	var date = $('#newAussaatForm input[name=dateInput]').val();
	var remark = $('#newBodenForm textarea[name=remark]').val();
	var schlagErntejahrId = $('#newAussaatForm select[name=schlagInput]').val();
	var kgProHa = getDouble($('#newAussaatForm input[name=kgProHa]').val());
	var beize = $('#newAussaatForm input[name=beize]').val();
	
	var data = '{"datum":"' + date + 
		'","type":"' + type + 
		'","flaeche":"' + fl + 
		'","schlagErntejahrId":"' + schlagErntejahrId +   
		'","bemerkung":"' + remark +
		'","kgProHa":"' + kgProHa +
		'","longitude":"' + longitude +
		'","latitude":"' + latitude +
		'","beize":"' + beize + '"}';
	
	save(data);
}

function onSaveNewPflanzenschutz(form) {
	var type = 4;
	var fl = getDouble($('#newPflanzenschutzForm input[name=flaeche]').val());
	var date = $('#newPflanzenschutzForm input[name=dateInput]').val();
	var remark = $('#newBodenForm textarea[name=remark]').val();
	var psMittel = $('#newPflanzenschutzForm select[name=psMittelInput]').val();
	var schlagErntejahrId = $('#newPflanzenschutzForm select[name=schlagInput]').val();
	var kgProHa = getDouble($('#newPflanzenschutzForm input[name=kgProHa]').val());
	var ec = getDouble($('#newPflanzenschutzForm input[name=ec]').val());
	var indikation = $('#newPflanzenschutzForm input[name=indikation]').val();
	
	var data = '{"datum":"' + date + 
		'","type":"' + type + 
		'","flaeche":"' + fl + 
		'","schlagErntejahrId":"' + schlagErntejahrId +   
		'","bemerkung":"' + remark +
		'","ec":"' + ec +
		'","kgProHa":"' + kgProHa +
		'","indikation":"' + indikation +
		'","longitude":"' + longitude +
		'","latitude":"' + latitude +
		'","psMittel":"' + psMittel + '"}';
	
	save(data);
}

function onSaveNewErnte(form) {
	var type = 3;
	var fl = getDouble($('#newErnteForm input[name=flaeche]').val());
	var date = $('#newErnteForm input[name=dateInput]').val();
	var remark = $('#newBodenForm textarea[name=remark]').val();
	var schlagErntejahrId = $('#newErnteForm select[name=schlagInput]').val();
	var dtProHa = getDouble($('#newErnteForm input[name=dtProHa]').val());
	var gesamtmenge = getDouble($('#newErnteForm input[name=gesamtmenge]').val());
	var anlieferung = $('#newErnteForm input[name=anlieferung]').val();
	
	var data = '{"datum":"' + date + 
		'","type":"' + type + 
		'","flaeche":"' + fl + 
		'","schlagErntejahrId":"' + schlagErntejahrId +   
		'","bemerkung":"' + remark +
		'","dtProHa":"' + dtProHa +
		'","gesamtmenge":"' + gesamtmenge +
		'","longitude":"' + longitude +
		'","latitude":"' + latitude +
		'","anlieferung":"' + anlieferung + '"}';
	
	save(data);
}

//saves the ontained data (using server) 
function save(data) {
	blockUI();
	$.ajax({
		url: baseUrl + '/service/aktivitaet/0?media=json',
		type:'PUT',
		contentType: "application/json",
		data: data,
		success : function() { 
			alert("Daten gespeichert!");
			unblockUI();
		},
    	error: function(error) {
			unblockUI();
			alert("Fehler beim Speichern der Daten:\n" + error.status + " : " + error.statusText);
		}
	});
}

//display current position
function displayPosition(pos) {
    var lat = pos.coords.latitude;
    var lon = pos.coords.longitude;
    var alt = (pos.coords.altitude != null ? pos.coords.altitude : 0);
	var outputStr = "Position: lat:"+ lat + " long:"+ lon + " alt:"+ alt;

	document.getElementById("info").innerHTML=outputStr;;
	
}

//log error message
function error(msg) {
  alert(typeof msg == 'string' ? msg : "error");
}

function getSchlag(schlagErntejahrId) {
	var s = null;
	var data = schlagData;
	var count = data.length;
	
	for (var i=0; i<count; i++) {
		var schlag = data[i];
		if (schlag.schlagErntejahr.id == schlagErntejahrId) {
			s = schlag;
			break;
		}
	}
	
	return s;
}

function loadAndDisplayAktivitaetListData(schlagErntejahrId) {
	//show busy indicator
	blockUI();
	
	//empty this list
	$('#aktivitaetListe ul li').hide();

	var url = baseUrl + '/service/aktivitaetList/' + schlagErntejahrId + '?media=json';

	
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
			onActListDataLoaded(data, schlagErntejahrId);
			unblockUI();
		},
		error: function(error) { 
			unblockUI();
			alert("Fehler beim Laden der Daten:\n" + error.status + " : " + error.statusText); 
		}
	});
}


function onActListDataLoaded(data, schlagErntejahrId) {
	actListData = data;
	
	var s = getSchlag(schlagErntejahrId);
	$('#aktivitaetListe h1').text(s.flurstueck.name);
	
	$('#aktivitaetListe ul').append('<li data-role="list-divider">Bodenbearbeitung</li>');
	$.each(data, function(akt) {
		if (this.type == 0){
			var newEntryRow = createEntry(this);
			$('#aktivitaetListe ul').append(newEntryRow);
		}
	});
	
	$('#aktivitaetListe ul').append('<li data-role="list-divider">Aussaat</li>');
	$.each(data, function(akt) {
		if (this.type == 1){
			var newEntryRow = createEntry(this);
			$('#aktivitaetListe ul').append(newEntryRow);
		}
	});
	
	$('#aktivitaetListe ul').append('<li data-role="list-divider">D√ºngung</li>');
	$.each(data, function(akt) {
		if (this.type == 2){
			var newEntryRow = createEntry(this);
			$('#aktivitaetListe ul').append(newEntryRow);
		}
	});
	
	$('#aktivitaetListe ul').append('<li data-role="list-divider">Pflanzenschutz</li>');
	$.each(data, function(akt) {
		if (this.type == 4){
			var newEntryRow = createEntry(this);
			$('#aktivitaetListe ul').append(newEntryRow);
		}
	});
	
	$('#aktivitaetListe ul').append('<li data-role="list-divider">Ernte</li>');
	$.each(data, function(akt) {
		if (this.type == 3){
			var newEntryRow = createEntry(this);
			$('#aktivitaetListe ul').append(newEntryRow);
		}
	});
	
	//refresh the list
	$('#aktivitaetListe ul').listview('refresh');
}

function createEntry(data) {
	var id = data.id;
	var datum = data.datum;
	if (data.datum != null && data.datum.length > 9) {
		datum = data.datum.substring(0, 10);
	}
	var flaeche = data.flaeche;
	//var typ = getTypeString(data.type);
	
	var newEntryRow = $('#actEntryTemplate').clone();
	newEntryRow.removeAttr('id');
	newEntryRow.removeAttr('style');

	newEntryRow.data('entryId', id);
	newEntryRow.find('.label').text(datum);
	newEntryRow.find('.flaeche').text(flaeche + ' ha');
	newEntryRow.find('a').attr('id', id);
	
	return newEntryRow;
}

function getTypeString(type) {
	var t = '';
	if (type == 0) {
		t = 'Bodenbearbeitung';
	} else if (type == 1) {
		t = 'Aussaat';
	} else if (type == 2) {
		t = 'D√ºngung';
	} else if (type == 3) {
		t = 'Ernte';
	} else if (type == 4) {
		t = 'Pflanzenschutz';
	}
	return t;
}

function getSorteString(id) {
	var sorte = "";
	if (stammdaten != null && stammdaten.sorteList != null) {
		for (var i=0; i<stammdaten.sorteList.length; i++) {
			var s = stammdaten.sorteList[i];
			if (s.id == id) {
				sorte = s.name;
				break;
			}
		}
	}
	return sorte;
}

function getKulturString(id) {
	var kultur = "";
	
	if (stammdaten != null && stammdaten.kulturList != null) {
		for (var i=0; i<stammdaten.kulturList.length; i++) {
			var k = stammdaten.kulturList[i];
			if (k.id == id) {
				kultur = k.name;
				break;
			}
		}
	}
	return kultur;
}

function loadAndDisplaySchlagListData() {
	if (initialized) {
		return;
	}
	
	//show busy indocator
	blockUI();

	var url = baseUrl + '/service/schlagList?media=json';
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
			onSchlagDataLoaded(data);
			postInit2();
			unblockUI();
		},
		error: function(error) { 
			unblockUI();
			alert("Fehler beim Laden der Daten:\n" + error.status + " : " + error.statusText); 
		}
	});
}

function onSchlagDataLoaded(data) {
	schlagData = data;

	$.each(data, function(schlag) {
		var fs = this.flurstueck;
		var name = fs.name;
		var flaeche = this.schlagErntejahr.flaeche; 
		var id = this.schlagErntejahr.id;
		var kultur = getKulturString(this.schlagErntejahr.anbauKultur.id);
		var sorte = getSorteString(this.schlagErntejahr.anbauSorte.id);
		
		var newEntryRow = $('#schlagEntryTemplate').clone();
		newEntryRow.removeAttr('id');
		newEntryRow.removeAttr('style');

		newEntryRow.attr('entryid', id);
		newEntryRow.find('a').attr('id', id);
		newEntryRow.find('.label').text(name);
		newEntryRow.find('.flaeche').text(flaeche + " ha");
		newEntryRow.find('.kultur').text(kultur);
		newEntryRow.find('.sorte').text(sorte);

		newEntryRow.appendTo('#schlagliste ul');

	});
}


//load activitaet details entry data
function loadAndDisplayActEntry(id) {
	var act = null;
	for (var i=0; i<actListData.length; i++) {
		if (actListData[i].id == id) {
			act = actListData[i];
			break;
		}
	}
	
	if (act == null) {
		return;
	}
	
	//empty this list
	$('#actDetails ul li').hide();
	$('#actDetails ul h1').hide();
	
	var datum = act.datum.substring(0, 10);
	var flaeche = act.flaeche;
	var typ = getTypeString(act.type);
	var bem = act.bemerkung;
	if (bem == null) bem = '';
	
	$('#actDetails h1').text(typ);

	//add date row
	var row = $('#actDetailsEntryTemplate').clone();
	row.removeAttr('id');
	row.removeAttr('style');
	row.find('.label').text("Datum: ");
	row.find('.value').text(datum);
	row.appendTo('#actDetails ul');
	
	//add flaeche row
	var row = $('#actDetailsEntryTemplate').clone();
	row.removeAttr('id');
	row.removeAttr('style');
	row.find('.label').text("Fl√§che: ");
	row.find('.value').text(flaeche + " ha");
	row.appendTo('#actDetails ul');
	
	//bodenbearbeitung
	if (act.type == 0) {
		//add bodenbearbeitung typ
		var typName = act.bodenbearbeitungTyp.name;
		
		var row = $('#actDetailsEntryTemplate').clone();
		row.removeAttr('id');
		row.removeAttr('style');
		row.find('.label').text("");
		row.find('.value').text(typName);
		row.appendTo('#actDetails ul');
	}
	//aussaat
	else if (act.type == 1) {
		var kgProHa = act.kgProHa;
		var beize = act.beize;
		
		//kg/ha
		var row = $('#actDetailsEntryTemplate').clone();
		row.removeAttr('id');
		row.removeAttr('style');
		row.find('.label').text("kg/ha: ");
		row.find('.value').text(kgProHa);
		row.appendTo('#actDetails ul');
		
		//beize
		var row = $('#actDetailsEntryTemplate').clone();
		row.removeAttr('id');
		row.removeAttr('style');
		row.find('.label').text("Beize: ");
		row.find('.value').text(beize);
		row.appendTo('#actDetails ul');
	}
	//duengung
	else if (act.type == 2) {
		var kgProHa = act.kgProHa;
		var ec = act.ec;
		var duengerart = act.duengerart.name;
		
		//duengerart
		var row = $('#actDetailsEntryTemplate').clone();
		row.removeAttr('id');
		row.removeAttr('style');
		row.find('.label').text("D√ºnger: ");
		row.find('.value').text(duengerart);
		row.appendTo('#actDetails ul');
		
		//kg/ha
		var row = $('#actDetailsEntryTemplate').clone();
		row.removeAttr('id');
		row.removeAttr('style');
		row.find('.label').text("kg/ha: ");
		row.find('.value').text(kgProHa);
		row.appendTo('#actDetails ul');
		
		//ec
		var row = $('#actDetailsEntryTemplate').clone();
		row.removeAttr('id');
		row.removeAttr('style');
		row.find('.label').text("EC: ");
		row.find('.value').text(ec);
		row.appendTo('#actDetails ul');
	}
	//pflanzenschutz
	else if (act.type == 4) {
		var kgProHa = act.kgProHa;
		var ec = act.ec;
		var indikation = act.indikation;
		var psMittel = act.psMittel.name;
		
		//PS-Mittel
		var row = $('#actDetailsEntryTemplate').clone();
		row.removeAttr('id');
		row.removeAttr('style');
		row.find('.label').text("PS-Mittel: ");
		row.find('.value').text(psMittel);
		row.appendTo('#actDetails ul');
		
		//kg/ha
		var row = $('#actDetailsEntryTemplate').clone();
		row.removeAttr('id');
		row.removeAttr('style');
		row.find('.label').text("kg/ha: ");
		row.find('.value').text(kgProHa);
		row.appendTo('#actDetails ul');
		
		//ec
		var row = $('#actDetailsEntryTemplate').clone();
		row.removeAttr('id');
		row.removeAttr('style');
		row.find('.label').text("EC: ");
		row.find('.value').text(ec);
		row.appendTo('#actDetails ul');
		
		//indikation
		var row = $('#actDetailsEntryTemplate').clone();
		row.removeAttr('id');
		row.removeAttr('style');
		row.find('.label').text("Indikation: ");
		row.find('.value').text(indikation);
		row.appendTo('#actDetails ul');

	}
	//ernte
	else if (act.type == 3) {
		var dtProHa = act.dtProHa;
		var menge = act.gesamtmenge;
		var anlieferung = act.anlieferung;
		
		//dt/ha
		var row = $('#actDetailsEntryTemplate').clone();
		row.removeAttr('id');
		row.removeAttr('style');
		row.find('.label').text("dt/ha: ");
		row.find('.value').text(dtProHa);
		row.appendTo('#actDetails ul');
		
		//gesamtmenge
		var row = $('#actDetailsEntryTemplate').clone();
		row.removeAttr('id');
		row.removeAttr('style');
		row.find('.label').text("Gesamtmenge: ");
		row.find('.value').text(menge);
		row.appendTo('#actDetails ul');
		
		//Anlieferung
		var row = $('#actDetailsEntryTemplate').clone();
		row.removeAttr('id');
		row.removeAttr('style');
		row.find('.label').text("Anlieferung: ");
		row.find('.value').text(anlieferung);
		row.appendTo('#actDetails ul');
	}
	
	//add bemerkung row
	var row = $('#actDetailsEntryTemplate').clone();
	row.removeAttr('id');
	row.removeAttr('style');
	row.find('.label').text("Bemerkung: ");
	row.find('.value').text(bem);
	row.appendTo('#actDetails ul');
	
	
	//display link to map (if location for record is set)
	var longitude = act.longitude;
	var latitude  = act.latitude;
	
	if (longitude > 0 && latitude > 0) {
		$('#actDetails ul').append('<li><a target="_blank" href="map.html?long=' 
				+ longitude + '&lat=' + latitude + '">Karte</a></li>');
	}
	
	//don't forget to refresh the list
	$('#actDetails ul').listview('refresh');
}


function blockUI() {
//	window.AGRISYS.showBusyIndicator();	
}

function unblockUI() {
//	window.AGRISYS.hideBusyIndicator();	
}