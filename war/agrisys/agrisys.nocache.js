function agrisys(){
  var $intern_0 = '', $intern_34 = '" for "gwt:onLoadErrorFn"', $intern_32 = '" for "gwt:onPropertyErrorFn"', $intern_15 = '"><\/script>', $intern_17 = '#', $intern_42 = '&', $intern_71 = '.cache.html', $intern_19 = '/', $intern_60 = '27418CB220DE06F28B9E4D53B31A9983', $intern_61 = '36C92B2560115B1D4B7E18662D3C09C0', $intern_62 = '397A823B412DFC877AA3DF8E46F5C416', $intern_63 = '61B64A671C4952CF9B4B7B2543D4154C', $intern_64 = '850663E2C33D9248593A1D4979256BB7', $intern_65 = '931D1724C85F85A26F8E3B7EF91E9777', $intern_66 = '96A89A80548D6D64A310429F7CE5F949', $intern_67 = '9858847F71C55C02BF1168B8590CCA2F', $intern_70 = ':', $intern_26 = '::', $intern_92 = '<script defer="defer">agrisys.onInjectionDone(\'agrisys\')<\/script>', $intern_14 = '<script id="', $intern_74 = '<script language="javascript" src="', $intern_29 = '=', $intern_18 = '?', $intern_68 = 'A53EBC166D0967BDB9685583E30D41ED', $intern_31 = 'Bad handler "', $intern_69 = 'C68D2102F83B7A2642DAECD148F00071', $intern_72 = 'DOMContentLoaded', $intern_16 = 'SCRIPT', $intern_45 = 'Unexpected exception in locale detection, using default: ', $intern_44 = '_', $intern_43 = '__gwt_Locale', $intern_13 = '__gwt_marker_agrisys', $intern_1 = 'agrisys', $intern_12 = 'agrisys.nocache.js', $intern_25 = 'agrisys::', $intern_20 = 'base', $intern_10 = 'baseUrl', $intern_4 = 'begin', $intern_3 = 'bootstrap', $intern_22 = 'clear.cache.gif', $intern_28 = 'content', $intern_59 = 'de_DE', $intern_40 = 'default', $intern_9 = 'end', $intern_53 = 'gecko', $intern_54 = 'gecko1_8', $intern_5 = 'gwt.codesvr=', $intern_6 = 'gwt.hosted=', $intern_7 = 'gwt.hybrid', $intern_33 = 'gwt:onLoadErrorFn', $intern_30 = 'gwt:onPropertyErrorFn', $intern_27 = 'gwt:property', $intern_57 = 'hosted.html?agrisys', $intern_52 = 'ie6', $intern_51 = 'ie8', $intern_35 = 'iframe', $intern_21 = 'img', $intern_36 = "javascript:''", $intern_56 = 'loadExternalRefs', $intern_39 = 'locale', $intern_41 = 'locale=', $intern_23 = 'meta', $intern_38 = 'moduleRequested', $intern_8 = 'moduleStartup', $intern_50 = 'msie', $intern_24 = 'name', $intern_47 = 'opera', $intern_37 = 'position:absolute;width:0;height:0;border:none', $intern_49 = 'safari', $intern_86 = 'sc/modules/ISC_Calendar.js', $intern_87 = 'sc/modules/ISC_Calendar.js"><\/script>', $intern_78 = 'sc/modules/ISC_Containers.js', $intern_79 = 'sc/modules/ISC_Containers.js"><\/script>', $intern_73 = 'sc/modules/ISC_Core.js', $intern_75 = 'sc/modules/ISC_Core.js"><\/script>', $intern_88 = 'sc/modules/ISC_DataBinding.js', $intern_89 = 'sc/modules/ISC_DataBinding.js"><\/script>', $intern_82 = 'sc/modules/ISC_Forms.js', $intern_83 = 'sc/modules/ISC_Forms.js"><\/script>', $intern_76 = 'sc/modules/ISC_Foundation.js', $intern_77 = 'sc/modules/ISC_Foundation.js"><\/script>', $intern_80 = 'sc/modules/ISC_Grids.js', $intern_81 = 'sc/modules/ISC_Grids.js"><\/script>', $intern_84 = 'sc/modules/ISC_RichTextEditor.js', $intern_85 = 'sc/modules/ISC_RichTextEditor.js"><\/script>', $intern_90 = 'sc/skins/Enterprise/load_skin.js', $intern_91 = 'sc/skins/Enterprise/load_skin.js"><\/script>', $intern_11 = 'script', $intern_58 = 'selectingPermutation', $intern_2 = 'startup', $intern_55 = 'unknown', $intern_46 = 'user.agent', $intern_48 = 'webkit';
  var $wnd = window, $doc = document, $stats = $wnd.__gwtStatsEvent?function(a){
    return $wnd.__gwtStatsEvent(a);
  }
  :null, $sessionId = $wnd.__gwtStatsSessionId?$wnd.__gwtStatsSessionId:null, scriptsDone, loadDone, bodyDone, base = $intern_0, metaProps = {}, values = [], providers = [], answers = [], softPermutationId = 0, onLoadErrorFunc, propertyErrorFunc;
  $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_3, millis:(new Date).getTime(), type:$intern_4});
  if (!$wnd.__gwt_stylesLoaded) {
    $wnd.__gwt_stylesLoaded = {};
  }
  if (!$wnd.__gwt_scriptsLoaded) {
    $wnd.__gwt_scriptsLoaded = {};
  }
  function isHostedMode(){
    var result = false;
    try {
      var query = $wnd.location.search;
      return (query.indexOf($intern_5) != -1 || (query.indexOf($intern_6) != -1 || $wnd.external && $wnd.external.gwtOnLoad)) && query.indexOf($intern_7) == -1;
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
      var iframe = $doc.getElementById($intern_1);
      var frameWnd = iframe.contentWindow;
      if (isHostedMode()) {
        frameWnd.__gwt_getProperty = function(name){
          return computePropValue(name);
        }
        ;
      }
      agrisys = null;
      frameWnd.gwtOnLoad(onLoadErrorFunc, $intern_1, base, softPermutationId);
      $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_8, millis:(new Date).getTime(), type:$intern_9});
    }
  }

  function computeScriptBase(){
    if (metaProps[$intern_10]) {
      base = metaProps[$intern_10];
      return base;
    }
    var thisScript;
    var scriptTags = $doc.getElementsByTagName($intern_11);
    for (var i = 0; i < scriptTags.length; ++i) {
      if (scriptTags[i].src.indexOf($intern_12) != -1) {
        thisScript = scriptTags[i];
      }
    }
    if (!thisScript) {
      var markerId = $intern_13;
      var markerScript;
      $doc.write($intern_14 + markerId + $intern_15);
      markerScript = $doc.getElementById(markerId);
      thisScript = markerScript && markerScript.previousSibling;
      while (thisScript && thisScript.tagName != $intern_16) {
        thisScript = thisScript.previousSibling;
      }
    }
    function getDirectoryOfFile(path){
      var hashIndex = path.lastIndexOf($intern_17);
      if (hashIndex == -1) {
        hashIndex = path.length;
      }
      var queryIndex = path.indexOf($intern_18);
      if (queryIndex == -1) {
        queryIndex = path.length;
      }
      var slashIndex = path.lastIndexOf($intern_19, Math.min(queryIndex, hashIndex));
      return slashIndex >= 0?path.substring(0, slashIndex + 1):$intern_0;
    }

    ;
    if (thisScript && thisScript.src) {
      base = getDirectoryOfFile(thisScript.src);
    }
    if (base == $intern_0) {
      var baseElements = $doc.getElementsByTagName($intern_20);
      if (baseElements.length > 0) {
        base = baseElements[baseElements.length - 1].href;
      }
       else {
        base = getDirectoryOfFile($doc.location.href);
      }
    }
     else if (base.match(/^\w+:\/\//)) {
    }
     else {
      var img = $doc.createElement($intern_21);
      img.src = base + $intern_22;
      base = getDirectoryOfFile(img.src);
    }
    if (markerScript) {
      markerScript.parentNode.removeChild(markerScript);
    }
    return base;
  }

  function processMetas(){
    var metas = document.getElementsByTagName($intern_23);
    for (var i = 0, n = metas.length; i < n; ++i) {
      var meta = metas[i], name = meta.getAttribute($intern_24), content;
      if (name) {
        name = name.replace($intern_25, $intern_0);
        if (name.indexOf($intern_26) >= 0) {
          continue;
        }
        if (name == $intern_27) {
          content = meta.getAttribute($intern_28);
          if (content) {
            var value, eq = content.indexOf($intern_29);
            if (eq >= 0) {
              name = content.substring(0, eq);
              value = content.substring(eq + 1);
            }
             else {
              name = content;
              value = $intern_0;
            }
            metaProps[name] = value;
          }
        }
         else if (name == $intern_30) {
          content = meta.getAttribute($intern_28);
          if (content) {
            try {
              propertyErrorFunc = eval(content);
            }
             catch (e) {
              alert($intern_31 + content + $intern_32);
            }
          }
        }
         else if (name == $intern_33) {
          content = meta.getAttribute($intern_28);
          if (content) {
            try {
              onLoadErrorFunc = eval(content);
            }
             catch (e) {
              alert($intern_31 + content + $intern_34);
            }
          }
        }
      }
    }
  }

  function __gwt_isKnownPropertyValue(propName, propValue){
    return propValue in values[propName];
  }

  function __gwt_getMetaProperty(name){
    var value = metaProps[name];
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
      var iframe = $doc.createElement($intern_35);
      iframe.src = $intern_36;
      iframe.id = $intern_1;
      iframe.style.cssText = $intern_37;
      iframe.tabIndex = -1;
      $doc.body.appendChild(iframe);
      $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_8, millis:(new Date).getTime(), type:$intern_38});
      iframe.contentWindow.location.replace(base + initialHtml);
    }
  }

  providers[$intern_39] = function(){
    try {
      var locale;
      var defaultLocale = $intern_40 || $intern_40;
      if (locale == null) {
        var args = location.search;
        var startLang = args.indexOf($intern_41);
        if (startLang >= 0) {
          var language = args.substring(startLang);
          var begin = language.indexOf($intern_29) + 1;
          var end = language.indexOf($intern_42);
          if (end == -1) {
            end = language.length;
          }
          locale = language.substring(begin, end);
        }
      }
      if (locale == null) {
        locale = __gwt_getMetaProperty($intern_39);
      }
      if (locale == null) {
        locale = $wnd[$intern_43];
      }
       else {
        $wnd[$intern_43] = locale || defaultLocale;
      }
      if (locale == null) {
        return defaultLocale;
      }
      while (!__gwt_isKnownPropertyValue($intern_39, locale)) {
        var lastIndex = locale.lastIndexOf($intern_44);
        if (lastIndex == -1) {
          locale = defaultLocale;
          break;
        }
         else {
          locale = locale.substring(0, lastIndex);
        }
      }
      return locale;
    }
     catch (e) {
      alert($intern_45 + e);
      return $intern_40;
    }
  }
  ;
  values[$intern_39] = {de_DE:0, 'default':1};
  providers[$intern_46] = function(){
    var ua = navigator.userAgent.toLowerCase();
    var makeVersion = function(result){
      return parseInt(result[1]) * 1000 + parseInt(result[2]);
    }
    ;
    if (ua.indexOf($intern_47) != -1) {
      return $intern_47;
    }
     else if (ua.indexOf($intern_48) != -1) {
      return $intern_49;
    }
     else if (ua.indexOf($intern_50) != -1) {
      if (document.documentMode >= 8) {
        return $intern_51;
      }
       else {
        var result = /msie ([0-9]+)\.([0-9]+)/.exec(ua);
        if (result && result.length == 3) {
          var v = makeVersion(result);
          if (v >= 6000) {
            return $intern_52;
          }
        }
      }
    }
     else if (ua.indexOf($intern_53) != -1) {
      var result = /rv:([0-9]+)\.([0-9]+)/.exec(ua);
      if (result && result.length == 3) {
        if (makeVersion(result) >= 1008)
          return $intern_54;
      }
      return $intern_53;
    }
    return $intern_55;
  }
  ;
  values[$intern_46] = {gecko:0, gecko1_8:1, ie6:2, ie8:3, opera:4, safari:5};
  agrisys.onScriptLoad = function(){
    if (frameInjected) {
      loadDone = true;
      maybeStartModule();
    }
  }
  ;
  agrisys.onInjectionDone = function(){
    scriptsDone = true;
    $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_56, millis:(new Date).getTime(), type:$intern_9});
    maybeStartModule();
  }
  ;
  processMetas();
  computeScriptBase();
  var strongName;
  var initialHtml;
  if (isHostedMode()) {
    if ($wnd.external && ($wnd.external.initModule && $wnd.external.initModule($intern_1))) {
      $wnd.location.reload();
      return;
    }
    initialHtml = $intern_57;
    strongName = $intern_0;
  }
  $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_3, millis:(new Date).getTime(), type:$intern_58});
  if (!isHostedMode()) {
    try {
      unflattenKeylistIntoAnswers([$intern_59, $intern_53], $intern_60);
      unflattenKeylistIntoAnswers([$intern_40, $intern_49], $intern_61);
      unflattenKeylistIntoAnswers([$intern_59, $intern_49], $intern_62);
      unflattenKeylistIntoAnswers([$intern_40, $intern_53], $intern_63);
      unflattenKeylistIntoAnswers([$intern_59, $intern_47], $intern_64);
      unflattenKeylistIntoAnswers([$intern_40, $intern_47], $intern_65);
      unflattenKeylistIntoAnswers([$intern_40, $intern_54], $intern_66);
      unflattenKeylistIntoAnswers([$intern_59, $intern_54], $intern_67);
      unflattenKeylistIntoAnswers([$intern_40, $intern_52], $intern_68);
      unflattenKeylistIntoAnswers([$intern_40, $intern_51], $intern_68);
      unflattenKeylistIntoAnswers([$intern_59, $intern_52], $intern_69);
      unflattenKeylistIntoAnswers([$intern_59, $intern_51], $intern_69);
      strongName = answers[computePropValue($intern_39)][computePropValue($intern_46)];
      var idx = strongName.indexOf($intern_70);
      if (idx != -1) {
        softPermutationId = Number(strongName.substring(idx + 1));
        strongName = strongName.substring(0, idx);
      }
      initialHtml = strongName + $intern_71;
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
      if ($doc.removeEventListener) {
        $doc.removeEventListener($intern_72, onBodyDone, false);
      }
      if (onBodyDoneTimerId) {
        clearInterval(onBodyDoneTimerId);
      }
    }
  }

  if ($doc.addEventListener) {
    $doc.addEventListener($intern_72, function(){
      maybeInjectFrame();
      onBodyDone();
    }
    , false);
  }
  var onBodyDoneTimerId = setInterval(function(){
    if (/loaded|complete/.test($doc.readyState)) {
      maybeInjectFrame();
      onBodyDone();
    }
  }
  , 50);
  $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_3, millis:(new Date).getTime(), type:$intern_9});
  $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_56, millis:(new Date).getTime(), type:$intern_4});
  if (!__gwt_scriptsLoaded[$intern_73]) {
    __gwt_scriptsLoaded[$intern_73] = true;
    document.write($intern_74 + base + $intern_75);
  }
  if (!__gwt_scriptsLoaded[$intern_76]) {
    __gwt_scriptsLoaded[$intern_76] = true;
    document.write($intern_74 + base + $intern_77);
  }
  if (!__gwt_scriptsLoaded[$intern_78]) {
    __gwt_scriptsLoaded[$intern_78] = true;
    document.write($intern_74 + base + $intern_79);
  }
  if (!__gwt_scriptsLoaded[$intern_80]) {
    __gwt_scriptsLoaded[$intern_80] = true;
    document.write($intern_74 + base + $intern_81);
  }
  if (!__gwt_scriptsLoaded[$intern_82]) {
    __gwt_scriptsLoaded[$intern_82] = true;
    document.write($intern_74 + base + $intern_83);
  }
  if (!__gwt_scriptsLoaded[$intern_84]) {
    __gwt_scriptsLoaded[$intern_84] = true;
    document.write($intern_74 + base + $intern_85);
  }
  if (!__gwt_scriptsLoaded[$intern_86]) {
    __gwt_scriptsLoaded[$intern_86] = true;
    document.write($intern_74 + base + $intern_87);
  }
  if (!__gwt_scriptsLoaded[$intern_88]) {
    __gwt_scriptsLoaded[$intern_88] = true;
    document.write($intern_74 + base + $intern_89);
  }
  if (!__gwt_scriptsLoaded[$intern_90]) {
    __gwt_scriptsLoaded[$intern_90] = true;
    document.write($intern_74 + base + $intern_91);
  }
  $doc.write($intern_92);
}

agrisys();
