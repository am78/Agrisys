function agrisys(){
  var $wnd_0 = window, $doc_0 = document, $stats = $wnd_0.__gwtStatsEvent?function(a){
    return $wnd_0.__gwtStatsEvent(a);
  }
  :null, $sessionId_0 = $wnd_0.__gwtStatsSessionId?$wnd_0.__gwtStatsSessionId:null, scriptsDone, loadDone, bodyDone, base = '', metaProps = {}, values = [], providers = [], answers = [], softPermutationId = 0, onLoadErrorFunc, propertyErrorFunc;
  $stats && $stats({moduleName:'agrisys', sessionId:$sessionId_0, subSystem:'startup', evtGroup:'bootstrap', millis:(new Date).getTime(), type:'begin'});
  if (!$wnd_0.__gwt_stylesLoaded) {
    $wnd_0.__gwt_stylesLoaded = {};
  }
  if (!$wnd_0.__gwt_scriptsLoaded) {
    $wnd_0.__gwt_scriptsLoaded = {};
  }
  function isHostedMode(){
    var result = false;
    try {
      var query = $wnd_0.location.search;
      return (query.indexOf('gwt.codesvr=') != -1 || (query.indexOf('gwt.hosted=') != -1 || $wnd_0.external && $wnd_0.external.gwtOnLoad)) && query.indexOf('gwt.hybrid') == -1;
    }
     catch (e) {
    }
    isHostedMode = function(){
      return result;
    }
    ;
    return result;
  }

  function maybeStartModule(){
    if (scriptsDone && loadDone) {
      var iframe = $doc_0.getElementById('agrisys');
      var frameWnd = iframe.contentWindow;
      if (isHostedMode()) {
        frameWnd.__gwt_getProperty = function(name_0){
          return computePropValue(name_0);
        }
        ;
      }
      agrisys = null;
      frameWnd.gwtOnLoad(onLoadErrorFunc, 'agrisys', base, softPermutationId);
      $stats && $stats({moduleName:'agrisys', sessionId:$sessionId_0, subSystem:'startup', evtGroup:'moduleStartup', millis:(new Date).getTime(), type:'end'});
    }
  }

  function computeScriptBase(){
    function getDirectoryOfFile(path){
      var hashIndex = path.lastIndexOf('#');
      if (hashIndex == -1) {
        hashIndex = path.length;
      }
      var queryIndex = path.indexOf('?');
      if (queryIndex == -1) {
        queryIndex = path.length;
      }
      var slashIndex = path.lastIndexOf('/', Math.min(queryIndex, hashIndex));
      return slashIndex >= 0?path.substring(0, slashIndex + 1):'';
    }

    function ensureAbsoluteUrl(url){
      if (url.match(/^\w+:\/\//)) {
      }
       else {
        var img = $doc_0.createElement('img');
        img.src = url + 'clear.cache.gif';
        url = getDirectoryOfFile(img.src);
      }
      return url;
    }

    function tryMetaTag(){
      var metaVal = __gwt_getMetaProperty('baseUrl');
      if (metaVal != null) {
        return metaVal;
      }
      return '';
    }

    function tryNocacheJsTag(){
      var scriptTags = $doc_0.getElementsByTagName('script');
      for (var i = 0; i < scriptTags.length; ++i) {
        if (scriptTags[i].src.indexOf('agrisys.nocache.js') != -1) {
          return getDirectoryOfFile(scriptTags[i].src);
        }
      }
      return '';
    }

    function tryMarkerScript(){
      var thisScript;
      if (typeof isBodyLoaded == 'undefined' || !isBodyLoaded()) {
        var markerId = '__gwt_marker_agrisys';
        var markerScript;
        $doc_0.write('<script id="' + markerId + '"><\/script>');
        markerScript = $doc_0.getElementById(markerId);
        thisScript = markerScript && markerScript.previousSibling;
        while (thisScript && thisScript.tagName != 'SCRIPT') {
          thisScript = thisScript.previousSibling;
        }
        if (markerScript) {
          markerScript.parentNode.removeChild(markerScript);
        }
        if (thisScript && thisScript.src) {
          return getDirectoryOfFile(thisScript.src);
        }
      }
      return '';
    }

    function tryBaseTag(){
      var baseElements = $doc_0.getElementsByTagName('base');
      if (baseElements.length > 0) {
        return baseElements[baseElements.length - 1].href;
      }
      return '';
    }

    var tempBase = tryMetaTag();
    if (tempBase == '') {
      tempBase = tryNocacheJsTag();
    }
    if (tempBase == '') {
      tempBase = tryMarkerScript();
    }
    if (tempBase == '') {
      tempBase = tryBaseTag();
    }
    if (tempBase == '') {
      tempBase = getDirectoryOfFile($doc_0.location.href);
    }
    tempBase = ensureAbsoluteUrl(tempBase);
    base = tempBase;
    return tempBase;
  }

  function processMetas(){
    var metas = document.getElementsByTagName('meta');
    for (var i = 0, n = metas.length; i < n; ++i) {
      var meta = metas[i], name_0 = meta.getAttribute('name'), content_0;
      if (name_0) {
        name_0 = name_0.replace('agrisys::', '');
        if (name_0.indexOf('::') >= 0) {
          continue;
        }
        if (name_0 == 'gwt:property') {
          content_0 = meta.getAttribute('content');
          if (content_0) {
            var value, eq = content_0.indexOf('=');
            if (eq >= 0) {
              name_0 = content_0.substring(0, eq);
              value = content_0.substring(eq + 1);
            }
             else {
              name_0 = content_0;
              value = '';
            }
            metaProps[name_0] = value;
          }
        }
         else if (name_0 == 'gwt:onPropertyErrorFn') {
          content_0 = meta.getAttribute('content');
          if (content_0) {
            try {
              propertyErrorFunc = eval(content_0);
            }
             catch (e) {
              alert('Bad handler "' + content_0 + '" for "gwt:onPropertyErrorFn"');
            }
          }
        }
         else if (name_0 == 'gwt:onLoadErrorFn') {
          content_0 = meta.getAttribute('content');
          if (content_0) {
            try {
              onLoadErrorFunc = eval(content_0);
            }
             catch (e) {
              alert('Bad handler "' + content_0 + '" for "gwt:onLoadErrorFn"');
            }
          }
        }
      }
    }
  }

  function __gwt_isKnownPropertyValue(propName, propValue){
    return propValue in values[propName];
  }

  function __gwt_getMetaProperty(name_0){
    var value = metaProps[name_0];
    return value == null?null:value;
  }

  function unflattenKeylistIntoAnswers(propValArray, value){
    var answer = answers;
    for (var i = 0, n = propValArray.length - 1; i < n; ++i) {
      answer = answer[propValArray[i]] || (answer[propValArray[i]] = []);
    }
    answer[propValArray[n]] = value;
  }

  function computePropValue(propName){
    var value = providers[propName](), allowedValuesMap = values[propName];
    if (value in allowedValuesMap) {
      return value;
    }
    var allowedValuesList = [];
    for (var k in allowedValuesMap) {
      allowedValuesList[allowedValuesMap[k]] = k;
    }
    if (propertyErrorFunc) {
      propertyErrorFunc(propName, allowedValuesList, value);
    }
    throw null;
  }

  var frameInjected;
  function maybeInjectFrame(){
    if (!frameInjected) {
      frameInjected = true;
      var iframe = $doc_0.createElement('iframe');
      iframe.src = "javascript:''";
      iframe.id = 'agrisys';
      iframe.style.cssText = 'position:absolute;width:0;height:0;border:none';
      iframe.tabIndex = -1;
      $doc_0.body.appendChild(iframe);
      $stats && $stats({moduleName:'agrisys', sessionId:$sessionId_0, subSystem:'startup', evtGroup:'moduleStartup', millis:(new Date).getTime(), type:'moduleRequested'});
      iframe.contentWindow.location.replace(base + initialHtml);
    }
  }

  providers['locale'] = function(){
    var locale = null;
    var rtlocale = 'default';
    try {
      if (!locale) {
        var queryParam = location.search;
        var qpStart = queryParam.indexOf('locale=');
        if (qpStart >= 0) {
          var value = queryParam.substring(qpStart + 7);
          var end = queryParam.indexOf('&', qpStart);
          if (end < 0) {
            end = queryParam.length;
          }
          locale = queryParam.substring(qpStart + 7, end);
        }
      }
      if (!locale) {
        locale = __gwt_getMetaProperty('locale');
      }
      if (!locale) {
        locale = $wnd_0['__gwt_Locale'];
      }
      if (locale) {
        rtlocale = locale;
      }
      while (locale && !__gwt_isKnownPropertyValue('locale', locale)) {
        var lastIndex = locale.lastIndexOf('_');
        if (lastIndex < 0) {
          locale = null;
          break;
        }
        locale = locale.substring(0, lastIndex);
      }
    }
     catch (e) {
      alert('Unexpected exception in locale detection, using default: ' + e);
    }
    $wnd_0['__gwt_Locale'] = rtlocale;
    return locale || 'default';
  }
  ;
  values['locale'] = {de_DE:0, 'default':1};
  providers['user.agent'] = function(){
    var ua = navigator.userAgent.toLowerCase();
    var makeVersion = function(result){
      return parseInt(result[1]) * 1000 + parseInt(result[2]);
    }
    ;
    if (ua.indexOf('opera') != -1) {
      return 'opera';
    }
     else if (ua.indexOf('webkit') != -1) {
      return 'safari';
    }
     else if (ua.indexOf('msie') != -1) {
      if (document.documentMode >= 8) {
        return 'ie8';
      }
       else {
        var result_0 = /msie ([0-9]+)\.([0-9]+)/.exec(ua);
        if (result_0 && result_0.length == 3) {
          var v = makeVersion(result_0);
          if (v >= 6000) {
            return 'ie6';
          }
        }
      }
    }
     else if (ua.indexOf('gecko') != -1) {
      return 'gecko1_8';
    }
    return 'unknown';
  }
  ;
  values['user.agent'] = {gecko1_8:0, ie6:1, ie8:2, opera:3, safari:4};
  agrisys.onScriptLoad = function(){
    if (frameInjected) {
      loadDone = true;
      maybeStartModule();
    }
  }
  ;
  agrisys.onInjectionDone = function(){
    scriptsDone = true;
    $stats && $stats({moduleName:'agrisys', sessionId:$sessionId_0, subSystem:'startup', evtGroup:'loadExternalRefs', millis:(new Date).getTime(), type:'end'});
    maybeStartModule();
  }
  ;
  processMetas();
  computeScriptBase();
  var strongName;
  var initialHtml;
  if (isHostedMode()) {
    if ($wnd_0.external && ($wnd_0.external.initModule && $wnd_0.external.initModule('agrisys'))) {
      $wnd_0.location.reload();
      return;
    }
    initialHtml = 'hosted.html?agrisys';
    strongName = '';
  }
  $stats && $stats({moduleName:'agrisys', sessionId:$sessionId_0, subSystem:'startup', evtGroup:'bootstrap', millis:(new Date).getTime(), type:'selectingPermutation'});
  if (!isHostedMode()) {
    try {
      unflattenKeylistIntoAnswers(['de_DE', 'ie6'], '249158EFFFB5CA1FD4DDD687B519FF1B');
      unflattenKeylistIntoAnswers(['de_DE', 'ie8'], '249158EFFFB5CA1FD4DDD687B519FF1B');
      unflattenKeylistIntoAnswers(['de_DE', 'gecko1_8'], '4D35076D2C8B4678ECE8487092ABFF8A');
      unflattenKeylistIntoAnswers(['default', 'gecko1_8'], '55302F5C3EE4D606C5E57C4D21C0A785');
      unflattenKeylistIntoAnswers(['default', 'ie6'], '97A2772E949EC46E87BBEB45D6A6C782');
      unflattenKeylistIntoAnswers(['default', 'ie8'], '97A2772E949EC46E87BBEB45D6A6C782');
      unflattenKeylistIntoAnswers(['default', 'safari'], 'B0E24AC55E02577D6B7C645BFFB28327');
      unflattenKeylistIntoAnswers(['de_DE', 'safari'], 'DF7B59232D7CB31842F662C8CEDF48C8');
      unflattenKeylistIntoAnswers(['de_DE', 'opera'], 'E681FB1933152F0BD2A4FEF7F22B34EB');
      unflattenKeylistIntoAnswers(['default', 'opera'], 'F2B2D74CB380B46B6A0E72F8CC0C3FCE');
      strongName = answers[computePropValue('locale')][computePropValue('user.agent')];
      var idx = strongName.indexOf(':');
      if (idx != -1) {
        softPermutationId = Number(strongName.substring(idx + 1));
        strongName = strongName.substring(0, idx);
      }
      initialHtml = strongName + '.cache.html';
    }
     catch (e) {
      return;
    }
  }
  var onBodyDoneTimerId;
  function onBodyDone(){
    if (!bodyDone) {
      bodyDone = true;
      maybeStartModule();
      if ($doc_0.removeEventListener) {
        $doc_0.removeEventListener('DOMContentLoaded', onBodyDone, false);
      }
      if (onBodyDoneTimerId) {
        clearInterval(onBodyDoneTimerId);
      }
    }
  }

  if ($doc_0.addEventListener) {
    $doc_0.addEventListener('DOMContentLoaded', function(){
      maybeInjectFrame();
      onBodyDone();
    }
    , false);
  }
  var onBodyDoneTimerId = setInterval(function(){
    if (/loaded|complete/.test($doc_0.readyState)) {
      maybeInjectFrame();
      onBodyDone();
    }
  }
  , 50);
  $stats && $stats({moduleName:'agrisys', sessionId:$sessionId_0, subSystem:'startup', evtGroup:'bootstrap', millis:(new Date).getTime(), type:'end'});
  $stats && $stats({moduleName:'agrisys', sessionId:$sessionId_0, subSystem:'startup', evtGroup:'loadExternalRefs', millis:(new Date).getTime(), type:'begin'});
  if (!__gwt_scriptsLoaded['sc/modules/ISC_Core.js']) {
    __gwt_scriptsLoaded['sc/modules/ISC_Core.js'] = true;
    document.write('<script language="javascript" src="' + base + 'sc/modules/ISC_Core.js"><\/script>');
  }
  if (!__gwt_scriptsLoaded['sc/modules/ISC_Foundation.js']) {
    __gwt_scriptsLoaded['sc/modules/ISC_Foundation.js'] = true;
    document.write('<script language="javascript" src="' + base + 'sc/modules/ISC_Foundation.js"><\/script>');
  }
  if (!__gwt_scriptsLoaded['sc/modules/ISC_Containers.js']) {
    __gwt_scriptsLoaded['sc/modules/ISC_Containers.js'] = true;
    document.write('<script language="javascript" src="' + base + 'sc/modules/ISC_Containers.js"><\/script>');
  }
  if (!__gwt_scriptsLoaded['sc/modules/ISC_Grids.js']) {
    __gwt_scriptsLoaded['sc/modules/ISC_Grids.js'] = true;
    document.write('<script language="javascript" src="' + base + 'sc/modules/ISC_Grids.js"><\/script>');
  }
  if (!__gwt_scriptsLoaded['sc/modules/ISC_Forms.js']) {
    __gwt_scriptsLoaded['sc/modules/ISC_Forms.js'] = true;
    document.write('<script language="javascript" src="' + base + 'sc/modules/ISC_Forms.js"><\/script>');
  }
  if (!__gwt_scriptsLoaded['sc/modules/ISC_RichTextEditor.js']) {
    __gwt_scriptsLoaded['sc/modules/ISC_RichTextEditor.js'] = true;
    document.write('<script language="javascript" src="' + base + 'sc/modules/ISC_RichTextEditor.js"><\/script>');
  }
  if (!__gwt_scriptsLoaded['sc/modules/ISC_Calendar.js']) {
    __gwt_scriptsLoaded['sc/modules/ISC_Calendar.js'] = true;
    document.write('<script language="javascript" src="' + base + 'sc/modules/ISC_Calendar.js"><\/script>');
  }
  if (!__gwt_scriptsLoaded['sc/modules/ISC_DataBinding.js']) {
    __gwt_scriptsLoaded['sc/modules/ISC_DataBinding.js'] = true;
    document.write('<script language="javascript" src="' + base + 'sc/modules/ISC_DataBinding.js"><\/script>');
  }
  if (!__gwt_scriptsLoaded['sc/skins/Enterprise/load_skin.js']) {
    __gwt_scriptsLoaded['sc/skins/Enterprise/load_skin.js'] = true;
    document.write('<script language="javascript" src="' + base + 'sc/skins/Enterprise/load_skin.js"><\/script>');
  }
  if (!__gwt_scriptsLoaded['http://maps.google.com/maps?gwt=1&file=api&v=2&sensor=false&key=ABQIAAAAWC3arzoRHb2hqhFgPw4LFRQZV8fD85RK7KWaR-Anp3MtkiEAqBRbYStNJOA2z6Na_UvE8RfJTrvpRg']) {
    __gwt_scriptsLoaded['http://maps.google.com/maps?gwt=1&file=api&v=2&sensor=false&key=ABQIAAAAWC3arzoRHb2hqhFgPw4LFRQZV8fD85RK7KWaR-Anp3MtkiEAqBRbYStNJOA2z6Na_UvE8RfJTrvpRg'] = true;
    document.write('<script language="javascript" src="http://maps.google.com/maps?gwt=1&file=api&v=2&sensor=false&key=ABQIAAAAWC3arzoRHb2hqhFgPw4LFRQZV8fD85RK7KWaR-Anp3MtkiEAqBRbYStNJOA2z6Na_UvE8RfJTrvpRg"><\/script>');
  }
  $doc_0.write('<script defer="defer">agrisys.onInjectionDone(\'agrisys\')<\/script>');
}

agrisys();
