
var schlagData;
var actListData;
var stammdaten;
var currentSchlagErntejahrId;

//init
function init() {
	var url = '/service/stammdaten?media=json';
	//load stammdaten
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
			console.log("stammdaten loaded: " + data);
			stammdaten = data;
			
			//load and display schlag list
			loadAndDisplaySchlagListData();
			
			//perform some post init task after the markup and the initial data has been loaded
			postInit();
			
		}
	});
	
	//add tap handlers
	$("#schlagliste ul li a").tap(function(event) {
		var id = $(event.target).attr('entryid');
		if (id == null) {
			id = this.id;
		}
		console.log("tapped on : " + id);
		//set current schlagerntejahrId
		currentSchlagErntejahrId = id;
		//load and display data
		loadAndDisplayAktivitaetListData(id);
		
	    return true;
	}); 

	$("#actList ul li a").tap(function(event) {
		var id = $(event.target).attr('entryid');
		if (id == null) {
			id = this.id;
		}
		console.log("tapped on : " + id);
		loadAndDisplayActEntry(id);
	    return true;
	});

	$('#actInputForm').submit(function() {
		
		onSaveNewAktivitaet(this);

	  return false;
	});

	//set current date in dateInput form input
	var date = getDateString(new Date());
	
	$('#dateInput').attr('value', date);

	/*
	//get geo location
	if (navigator.geolocation) {
	  navigator.geolocation.getCurrentPosition(displayPosition, error);
	} else {
	  alert("Geo location Feature nicht unterstützt!");
	}
	*/

}

//perform some post init tasks after markup and initial data has been loaded
function postInit() {
	//add bodenbearbeitung types
	var btlst = stammdaten.bodenbearbeitungTypList;
	for (var i=0; i<btlst.length; i++) {
		var id = btlst[i].id;
		var name = btlst[i].name;
		var option = '<option value="' + id + '">' + name + '</option>';
		$('select[name=bodenbearbeitungTypInput]').append(option);
	}
}

function getDateString(date) {
	var day = date.getDate();
	var month = date.getMonth() + 1;
	var year = date.getFullYear();
	return day + '.' + month + '.' + year;
}

function onSaveNewAktivitaet(form) {
	console.log(form);
	var type = 0;
	var fl = $('input[name=flaeche]').val();
	var date = $('input[name=dateInput]').val();
	var remark = $('input[name=remark]').val();
	var bodenbearbeitungTyp = $('select[name=bodenbearbeitungTypInput]').val();
	var schlagErntejahrId = currentSchlagErntejahrId;
	
	var data = '{"datum":"' + date + '","type":"' + type + '","flaeche":"' + fl + '","schlagErntejahrId":"' + schlagErntejahrId +   
		'","bemerkung":"' + remark +'","bodenbearbeitungTyp":"' + bodenbearbeitungTyp + '"}';
	
	console.log(data);

	$.ajax({
		url: '/service/aktivitaet/0?media=json',
		type:'PUT',
		contentType: "application/json",
		data: data,
		success : function() { console.log("Data stored!"); },
    	error: function(error) { 
			alert("Error while storing data.\nError " + error.status + " : " + error.statusText); 
		}
	});
}

//display current position
function displayPosition(pos) {
    var lat = pos.coords.latitude;
    var lon = pos.coords.longitude;
    var alt = (pos.coords.altitude != null ? pos.coords.altitude : 0);
	var outputStr = "Position: lat:"+ lat + " long:"+ lon + " alt:"+ alt;

	console.log(outputStr);
		
	document.getElementById("info").innerHTML=outputStr;;
	
}

//log error message
function error(msg) {
  console.log(typeof msg == 'string' ? msg : "error");
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
	$('body').append('<div id="progress" class="current">Bitte warten...</div>'); 
	
	//empty this list
	$('#actList ul li').hide();

	console.log('load and dispaly aktivitaet list data for flurstueck with id ' + schlagErntejahrId + " ...");
	var url = '/service/aktivitaetList/' + schlagErntejahrId + '?media=json';

	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
			onActListDataLoaded(data, schlagErntejahrId);
		}
	});
}


function onActListDataLoaded(data, schlagErntejahrId) {
	actListData = data;
	$('#progress').remove();
	
	console.log('Load was performed.');
	console.log(data);
	
	var s = getSchlag(schlagErntejahrId);
	console.log('Schlag: ' + s);
	$('#actList h1').text(s.flurstueck.name);

	$.each(data, function(akt) {
		var id = this.id;
		var datum = this.datum.substring(0, 10);
		var flaeche = this.flaeche;
		var typ = getTypeString(this.type);
		
		
		var newEntryRow = $('#actEntryTemplate').clone();
		newEntryRow.removeAttr('id');
		newEntryRow.removeAttr('style');

		newEntryRow.data('entryId', id);
		newEntryRow.appendTo('#actList ul');
		newEntryRow.find('.label').text(datum);
		newEntryRow.find('.flaeche').text(flaeche + ' ha - ' + typ);
		newEntryRow.find('a').attr('id', id);
	}); 
}

function getTypeString(type) {
	var t = '';
	if (type == 0) {
		t = 'Bodenbearbeitung';
	} else if (type == 1) {
		t = 'Aussaat';
	} else if (type == 2) {
		t = 'Düngung';
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
	console.log('load and display schlaglist data...');
	var url = '/service/schlagList?media=json';

	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
			onSchlagDataLoaded(data);
		}
	});
}

function onSchlagDataLoaded(data) {
	console.log('Load was performed.');
	console.log(data);
	
	schlagData = data;

	$.each(data, function(schlag) {
		var fs = this.flurstueck;
		var name = fs.name;
		var flaeche = fs.flaeche;
		var id = this.schlagErntejahr.id;
		var kultur = getKulturString(this.schlagErntejahr.anbauKultur.id);
		var sorte = getSorteString(this.schlagErntejahr.anbauSorte.id);
		
		console.log(id);

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
	
	console.log("display entry: " + act.id);
	
	//empty this list
	$('#actDetails ul li').hide();
	
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
	row.find('.label').text("Fläche: ");
	row.find('.value').text(flaeche + " ha");
	row.appendTo('#actDetails ul');
	
	//add bemerkung row
	var row = $('#actDetailsEntryTemplate').clone();
	row.removeAttr('id');
	row.removeAttr('style');
	row.find('.label').text("Bemerkung: ");
	row.find('.value').text(bem);
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
		//TODO
	}
	
	//pflanzenschutz
	else if (act.type == 4) {
		//TODO
	}
	
	//ernte
	else if (act.type == 3) {
		//TODO
	}
}
