/**
* 
*
*/
var LoginPlugin = function() {

}


/**
* 
*
* @param callback Success callback. Also receives progress messages during upload.
* @param fail Error callback
*/
LoginPlugin.prototype.loggedIn = function(callback, fail) {

    return PhoneGap.exec(
    		function(args) {
    			callback(args);
    		}, 
    		function(args) {
    			if(typeof fail == 'function') {
    				fail(args);
    			}
    		}, 
    		'LoginPlugin', 'loggedIn', []);
};

LoginPlugin.prototype.loggedOut = function(callback, fail) {

    return PhoneGap.exec(
    		function(args) {
    			callback(args);
    		}, 
    		function(args) {
    			if(typeof fail == 'function') {
    				fail(args);
    			}
    		}, 
    		'LoginPlugin', 'loggedOut', []);
};




PhoneGap.addConstructor(function() {
	PhoneGap.addPlugin('loginPlugin', new LoginPlugin());
	PluginManager.addService("LoginPlugin","com.anteboth.agrisys.mobile.LoginPlugin");
});
