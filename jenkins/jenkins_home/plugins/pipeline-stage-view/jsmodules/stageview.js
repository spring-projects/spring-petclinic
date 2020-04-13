(function(){function r(e,n,t){function o(i,f){if(!n[i]){if(!e[i]){var c="function"==typeof require&&require;if(!f&&c)return c(i,!0);if(u)return u(i,!0);var a=new Error("Cannot find module '"+i+"'");throw a.code="MODULE_NOT_FOUND",a}var p=n[i]={exports:{}};e[i][0].call(p.exports,function(r){var n=e[i][1][r];return o(n||r)},p,p.exports,r,e,n,t)}return n[i].exports}for(var u="function"==typeof require&&require,i=0;i<t.length;i++)o(t[i]);return o}return r})()({1:[function(require,module,exports){
var internal = require("./internal");
var promise = require("./promise");
var onRegisterTimeout;

/**
 * Asynchronously import/require a set of modules.
 *
 * <p>
 * Responsible for triggering the async loading of modules from plugins if
 * a given module is not already loaded.
 *
 * @param moduleQNames... A list of module "qualified" names, each containing the module name prefixed with the Jenkins plugin name
 * separated by a colon i.e. "<pluginName>:<moduleName>" e.g. "jquery:jquery2".
 *
 * @return A Promise, allowing async load of all modules. The promise is only fulfilled when all modules are loaded.
 */
exports.import = function() {
    if (arguments.length === 1) {
        return internal.import(arguments[0], onRegisterTimeout);        
    }
    
    var moduleQNames = [];    
    for (var i = 0; i < arguments.length; i++) {
        var argument = arguments[i];
        if (typeof argument === 'string') {
            moduleQNames.push(argument);
        }
    }
    
    if (moduleQNames.length == 0) {
        throw "No plugin module names specified.";
    }
    
    return promise.make(function (resolve, reject) {
        var fulfillments = [];
        
        function onFulfillment() {
            if (fulfillments.length === moduleQNames.length) {
                var modules = [];
                for (var i = 0; i < fulfillments.length; i++) {
                    if (fulfillments[i].value) {
                        modules.push(fulfillments[i].value);
                    } else {
                        // don't have everything yet so can't fulfill all.
                        return;
                    }
                }
                // If we make it here, then we have fulfilled all individual promises, which 
                // means we can now fulfill the top level import promise.
                resolve(modules);
            }
        }        
        
        // doRequire for each module
        for (var i = 0; i < moduleQNames.length; i++) {           
            function doRequire(moduleQName) {
                var promise = internal.import(moduleQName, onRegisterTimeout);
                var fulfillment = {
                    promise: promise,
                    value: undefined
                };
                fulfillments.push(fulfillment);
                promise
                    .onFulfilled(function(value) {
                        fulfillment.value = value;
                        onFulfillment();
                    })
                    .onRejected(function(error) {
                        reject(error);
                    });
            }
            doRequire(moduleQNames[i]);
        }
    }).applyArgsOnFulfill();    
};

/**
 * Synchronously "require" a module that it already loaded/registered.
 *
 * <p>
 * This function will throw an error if the module is not already loaded via an outer call to 'import'
 * (or 'import').
 *
 * @param moduleQName The module "qualified" name containing the module name prefixed with the Jenkins plugin name
 * separated by a colon i.e. "<pluginName>:<moduleName>" e.g. "jquery:jquery2".
 *
 * @return The module.
 */
exports.require = function(moduleQName) {
    var parsedModuleName = internal.parseModuleQName(moduleQName);
    var module = internal.getModule(parsedModuleName);    
    if (!module) {
        throw "Unable to perform synchronous 'require' for module '" + moduleQName + "'. This module is not pre-loaded. " +
            "The module needs to have been asynchronously pre-loaded via an outer call to 'import'.";
    }
    return module.exports;
}

/**
 * Export a module.
 * 
 * @param pluginName The Jenkins plugin in which the module resides, or "undefined" if the modules is in
 * the "global" module namespace e.g. a Jenkins core bundle.
 * @param moduleName The name of the module. 
 * @param module The CommonJS style module, or "undefined" if we just want to notify other modules waiting on
 * the loading of this module.
 * @param onError On error callback;
 */
exports.export = function(pluginName, moduleName, module, onError) {
    internal.onReady(function() {
        try {
            var moduleSpec = {pluginName: pluginName, moduleName: moduleName};
            var moduleNamespace = internal.getModuleNamespace(moduleSpec);
            
            if (moduleNamespace[moduleName]) {
                if (pluginName) {
                    throw "Jenkins plugin module '" + pluginName + ":" + moduleName + "' already registered.";
                } else {
                    throw "Jenkins global module '" + moduleName + "' already registered.";
                }
            }
            
            if (!module) {
                module = {
                    exports: {}
                };
            } else if (module.exports === undefined) {
                module = {
                    exports: module
                };
            }
            moduleNamespace[moduleName] = module;
            
            // Notify all that the module has been registered. See internal.loadModule also.
            internal.notifyModuleExported(moduleSpec, module.exports);
        } catch (e) {
            console.error(e);
            if (onError) {
                onError(e);
            }
        }
    });
};

/**
 * Add a module's CSS to the browser page.
 * 
 * <p>
 * The assumption is that the CSS can be accessed at
 * {@code <rootURL>/plugin/<pluginName>/jsmodules/<moduleName>/style.css}
 * 
 * @param pluginName The Jenkins plugin in which the module resides.
 * @param moduleName The name of the module. 
 * @param onError On error callback;
 */
exports.addModuleCSSToPage = function(pluginName, moduleName, onError) {
    internal.onReady(function() {
        try {
            internal.addModuleCSSToPage(pluginName, moduleName);
        } catch (e) {
            console.error(e);
            if (onError) {
                onError(e);
            }
        }
    });
};

/**
 * Set the module registration timeout i.e. the length of time to wait for a module to load before failing.
 *
 * @param timeout Millisecond duration before onRegister times out. Defaults to 10000 (10s) if not specified.
 */
exports.setRegisterTimeout = function(timeout) {
    onRegisterTimeout = timeout;
}

/**
 * Set the Jenkins root/base URL.
 * 
 * @param rootUrl The root/base URL.
 */
exports.setRootURL = function(rootUrl) {
    internal.setRootURL(rootUrl);
};

/**
 * Manually initialise the Jenkins Global.
 * <p>
 * This should only ever be called from a test environment.
 */
exports.initJenkinsGlobal = function() {
    internal.initJenkinsGlobal();
};

internal.onJenkinsGlobalInit(function(jenkinsCIGlobal) {
    // For backward compatibility, we need to make some jenkins-js-modules
    // functions globally available e.g. to allow legacy code wait for
    // certain modules to be loaded, as with legacy adjuncts.
    if (!jenkinsCIGlobal._internal) {
        // Put the functions on an object called '_internal' as a way
        // of hinting to people to not use it.
        jenkinsCIGlobal._internal = {
            import: exports.import,
            addScript: internal.addScript
        };
    }
});
},{"./internal":2,"./promise":3}],2:[function(require,module,exports){
var promise = require("./promise");
var windowHandle = require("window-handle");
var jenkinsCIGlobal;
var globalInitListeners = [];

exports.onReady = function(callback) {
    // This allows test based initialization of jenkins-js-modules when there might 
    // not yet be a global window object.
    if (jenkinsCIGlobal) {
        callback();
    } else {
        windowHandle.getWindow(function() {
            callback();
        });
    }    
};

exports.onJenkinsGlobalInit = function(callback) {
    globalInitListeners.push(callback);
};

exports.initJenkinsGlobal = function() {
    jenkinsCIGlobal = {
    };
    if (globalInitListeners) {
        for (var i = 0; i < globalInitListeners.length; i++) {
            globalInitListeners[i](jenkinsCIGlobal);
        }
    }
};

exports.clearJenkinsGlobal = function() {    
    jenkinsCIGlobal = undefined;
};

exports.getJenkins = function() {
    if (jenkinsCIGlobal) {
        return jenkinsCIGlobal;
    }
    var window = windowHandle.getWindow();
    if (window.jenkinsCIGlobal) {
        jenkinsCIGlobal = window.jenkinsCIGlobal;
    } else {
        exports.initJenkinsGlobal();
        jenkinsCIGlobal.rootURL = getRootURL();
        window.jenkinsCIGlobal = jenkinsCIGlobal;
    }   
    return jenkinsCIGlobal;
};

exports.getModuleNamespace = function(moduleSpec) {
    if (moduleSpec.pluginName) {
        return exports.getPlugin(moduleSpec.pluginName);
    } else {
        return exports.getGlobalModules();
    }
}

exports.getPlugin = function(pluginName) {
    var plugins = exports.getPlugins();
    var pluginNamespace = plugins[pluginName];
    if (!pluginNamespace) {
        pluginNamespace = {
            globalNS: false            
        };
        plugins[pluginName] = pluginNamespace;
    }
    return pluginNamespace;
};

exports.import = function(moduleQName, onRegisterTimeout) {
    return promise.make(function (resolve, reject) {
        // getPlugin etc needs to access the 'window' global. We want to make sure that
        // exists before attempting to fulfill the require operation. It may not exists
        // immediately in a test env.
        exports.onReady(function() {
            var moduleSpec = exports.parseModuleQName(moduleQName);
            var module = exports.getModule(moduleSpec);
            
            if (module) {
                // module already loaded
                resolve(module.exports);
            } else {
                if (onRegisterTimeout === 0) {
                    if (moduleSpec.pluginName) {
                        throw 'Plugin module ' + moduleSpec.pluginName + ':' + moduleSpec.moduleName + ' require failure. Async load mode disabled.';
                    } else {
                        throw 'Global module ' + moduleSpec.moduleName + ' require failure. Async load mode disabled.';
                    }
                }

                // module not loaded. Load async, fulfilling promise once registered
                exports.loadModule(moduleSpec, onRegisterTimeout)
                    .onFulfilled(function (moduleExports) {
                        resolve(moduleExports);
                    })
                    .onRejected(function (error) {
                        reject(error);
                    });
            }
        });
    });    
};

exports.loadModule = function(moduleSpec, onRegisterTimeout) {
    var moduleNamespace = exports.getModuleNamespace(moduleSpec);
    var module = moduleNamespace[moduleSpec.moduleName];
    
    if (module) {
        // Module already loaded. This prob shouldn't happen.
        console.log("Unexpected call to 'loadModule' for a module (" + moduleSpec.moduleName + ") that's already loaded.");
        return promise.make(function (resolve) {
            resolve(module.exports);
        });
    }

    function waitForRegistration(loadingModule, onRegisterTimeout) {
        return promise.make(function (resolve, reject) {
            if (typeof onRegisterTimeout !== "number") {
                onRegisterTimeout = 10000;
            }
            
            var timeoutObj = setTimeout(function () {
                // Timed out waiting on the module to load and register itself.
                if (!loadingModule.loaded) {
                    var moduleSpec = loadingModule.moduleSpec;
                    var errorDetail;
                    
                    if (moduleSpec.pluginName) {
                        errorDetail = "Please verify that the plugin '" +
                            moduleSpec.pluginName + "' is installed, and that " +
                            "it registers a module named '" + moduleSpec.moduleName + "'";
                    } else {
                        errorDetail = "Timed out waiting on global module '" + moduleSpec.moduleName + "' to load.";
                    }                    
                    console.error('Module load failure: ' + errorDetail);

                    // Call the reject function and tell it we timed out
                    reject({
                        reason: 'timeout',
                        detail: errorDetail
                    });
                }
            }, onRegisterTimeout);
            
            loadingModule.waitList.push({
                resolve: resolve,
                timeoutObj: timeoutObj
            });                    
        });
    }
    
    var loadingModule = getLoadingModule(moduleNamespace, moduleSpec.moduleName);
    if (!loadingModule.waitList) {
        loadingModule.waitList = [];
    }
    loadingModule.moduleSpec = moduleSpec; 
    loadingModule.loaded = false;

    try {
        return waitForRegistration(loadingModule, onRegisterTimeout);
    } finally {
        // We can auto/dynamic load modules in a plugin namespace. Global namespace modules
        // need to make sure they load themselves (via an adjunct, or whatever).
        if (moduleSpec.pluginName) {
            var scriptId = exports.toPluginModuleId(moduleSpec.pluginName, moduleSpec.moduleName) + ':js';
            var scriptSrc = exports.toPluginModuleSrc(moduleSpec.pluginName, moduleSpec.moduleName);
            exports.addScript(scriptId, scriptSrc);
        }
    }
};

exports.addScript = function(scriptId, scriptSrc) {
    var document = windowHandle.getWindow().document;
    var script = document.getElementById(scriptId);

    if (script) {
        var replaceable = script.getAttribute('data-replaceable');
        if (replaceable && replaceable === 'true') {
            // This <script> element is replaceable. In this case, 
            // we remove the existing script element and add a new one of the
            // same id and with the specified src attribute.
            // Adding happens below.
            script.parentNode.removeChild(script);
        } else {
            return undefined;
        }
    }

    var docHead = exports.getHeadElement();

    script = createElement('script');
    script.setAttribute('id', scriptId);
    script.setAttribute('type', 'text/javascript');
    script.setAttribute('src', scriptSrc);
    script.setAttribute('async', 'true');
    docHead.appendChild(script);
    
    return script;
};

exports.notifyModuleExported = function(moduleSpec, moduleExports) {
    var moduleNamespace = exports.getModuleNamespace(moduleSpec);
    var loadingModule = getLoadingModule(moduleNamespace, moduleSpec.moduleName);
    
    loadingModule.loaded = true;
    if (loadingModule.waitList) {
        for (var i = 0; i < loadingModule.waitList.length; i++) {
            var waiter = loadingModule.waitList[i];
            clearTimeout(waiter.timeoutObj);
            waiter.resolve(moduleExports);
        }
    }    
};

exports.addModuleCSSToPage = function(pluginName, moduleName) {
    var cssElId = exports.toPluginModuleId(pluginName, moduleName) + ':css';
    var document = windowHandle.getWindow().document;
    var cssEl = document.getElementById(cssElId);
    
    if (cssEl) {
        // already added to page
        return;
    }

    var cssPath = exports.getJSModulesDir(pluginName) + '/' + moduleName + '/style.css';
    var docHead = exports.getHeadElement();
    cssEl = createElement('link');
    cssEl.setAttribute('id', cssElId);
    cssEl.setAttribute('type', 'text/css');
    cssEl.setAttribute('rel', 'stylesheet');
    cssEl.setAttribute('href', cssPath);
    docHead.appendChild(cssEl);
};

exports.getGlobalModules = function() {
    var jenkinsCIGlobal = exports.getJenkins();
    if (!jenkinsCIGlobal.globals) {
        jenkinsCIGlobal.globals = {
            globalNS: true
        };
    }
    return jenkinsCIGlobal.globals;
};

exports.getPlugins = function() {
    var jenkinsCIGlobal = exports.getJenkins();
    if (!jenkinsCIGlobal.plugins) {
        jenkinsCIGlobal.plugins = {};
    }
    return jenkinsCIGlobal.plugins;
};

exports.toPluginModuleId = function(pluginName, moduleName) {
    return 'jenkins-plugin-module:' + pluginName + ':' + moduleName;
};

exports.toPluginModuleSrc = function(pluginName, moduleName) {
    return exports.getJSModulesDir(pluginName) + '/' + moduleName + '.js';
};

exports.getJSModulesDir = function(pluginName) {
    return getRootURL() + '/plugin/' + pluginName + '/jsmodules';
};

exports.getHeadElement = function() {
    var window = windowHandle.getWindow();
    var docHead = window.document.getElementsByTagName("head");
    if (!docHead || docHead.length == 0) {
        throw 'No head element found in document.';
    }
    return docHead[0];
};

exports.setRootURL = function(url) {    
    if (!jenkinsCIGlobal) {
        exports.initJenkinsGlobal();
    }
    jenkinsCIGlobal.rootURL = url;
};

exports.parseModuleQName = function(moduleQName) {
    var qNameTokens = moduleQName.split(":");    
    if (qNameTokens.length === 2) {
        return {
            pluginName: qNameTokens[0].trim(),
            moduleName: qNameTokens[1].trim()
        };
    } else {
        // The module/bundle is not in a plugin and doesn't
        // need to be loaded i.e. it will load itself and export.
        return {
            moduleName: qNameTokens[0].trim()
        };
    }
}

exports.getModule = function(moduleSpec) {
    if (moduleSpec.pluginName) {
        var plugin = exports.getPlugin(moduleSpec.pluginName);
        return plugin[moduleSpec.moduleName];
    } else {
        var globals = exports.getGlobalModules();
        return globals[moduleSpec.moduleName];
    }
}

function getRootURL() {
    if (jenkinsCIGlobal && jenkinsCIGlobal.rootURL) {
        return jenkinsCIGlobal.rootURL;
    }
    
    var docHead = exports.getHeadElement();
    var resURL = getAttribute(docHead, "resURL");

    if (!resURL) {
        throw "Attribute 'resURL' not defined on the document <head> element.";
    }

    if (jenkinsCIGlobal) {
        jenkinsCIGlobal.rootURL = resURL;
    }
    
    return resURL;
}

function createElement(name) {
    var document = windowHandle.getWindow().document;
    return document.createElement(name);
}

function getAttribute(element, attributeName) {
    var value = element.getAttribute(attributeName.toLowerCase());
    
    if (value) {
        return value;
    } else {
        // try without lowercasing
        return element.getAttribute(attributeName);
    }    
}

function getLoadingModule(moduleNamespace, moduleName) {
    if (!moduleNamespace.loadingModules) {
        moduleNamespace.loadingModules = {};
    }
    if (!moduleNamespace.loadingModules[moduleName]) {
        moduleNamespace.loadingModules[moduleName] = {};
    }
    return moduleNamespace.loadingModules[moduleName];
}

},{"./promise":3,"window-handle":4}],3:[function(require,module,exports){
/*
 * Very simple "Promise" impl.
 * <p>
 * Intentionally not using the "promise" module/polyfill because it will add a few Kb and we 
 * only need something very simple here. We really just want to follow the main pattern
 * and don't need some of the fancy stuff.
 * <p>
 * I think so long as we stick to same interface/interaction pattern as outlined in the link
 * below, then we can always switch to the "promise" module later without breaking anything.
 * <p>
 * See https://developer.mozilla.org/en/docs/Web/JavaScript/Reference/Global_Objects/Promise
 */

exports.make = function(executor) {
    var thePromise = new APromise();
    executor.call(thePromise, function(result) {
        thePromise.resolve(result);
    }, function(reason) {
        thePromise.reject(reason);
    });
    return thePromise;
};

function APromise() {
    this.state = 'PENDING';
    this.whenFulfilled = undefined;
    this.whenRejected = undefined;
    this.applyFulfillArgs = false;
}

APromise.prototype.applyArgsOnFulfill = function() {
    this.applyFulfillArgs = true;
    return this;
}

APromise.prototype.resolve = function (result) {
    this.state = 'FULFILLED';
    
    var thePromise = this;
    function doFulfill(whenFulfilled, result) {
        if (thePromise.applyFulfillArgs) {
            whenFulfilled.apply(whenFulfilled, result);
        } else {
            whenFulfilled(result);
        }
    }
    
    if (this.whenFulfilled) {
        doFulfill(this.whenFulfilled, result);
    }
    // redefine "onFulfilled" to call immediately
    this.onFulfilled = function (whenFulfilled) {
        if (whenFulfilled) {
            doFulfill(whenFulfilled, result);
        }
        return this;
    }
};

APromise.prototype.reject = function (reason) {
    this.state = 'REJECTED';
    if (this.whenRejected) {
        this.whenRejected(reason);
    }
    // redefine "onRejected" to call immediately
    this.onRejected = function(whenRejected) {
        if (whenRejected) {
            whenRejected(reason);
        }
        return this;
    }
};

APromise.prototype.onFulfilled = function(whenFulfilled) {
    if (!whenFulfilled) {
        throw 'Must provide an "whenFulfilled" callback.';
    }
    this.whenFulfilled = whenFulfilled;
    return this;
};

APromise.prototype.onRejected = function(whenRejected) {        
    if (whenRejected) {
        this.whenRejected = whenRejected;
    }
    return this;
};

},{}],4:[function(require,module,exports){
var theWindow;
var defaultTimeout = 10000;
var callbacks = [];
var windowSetTimeouts = [];

function execCallback(callback, theWindow) {
    if (callback) {
        try {
            callback.call(callback, theWindow);                
        } catch (e) {
            console.log("Error invoking window-handle callback.");
            console.log(e);
        }
    }
}

/**
 * Get the global "window" object.
 * @param callback An optional callback that can be used to receive the window asynchronously. Useful when
 * executing in test environment i.e. where the global window object might not exist immediately. 
 * @param timeout The timeout if waiting on the global window to be initialised.
 * @returns {*}
 */
exports.getWindow = function(callback, timeout) {
    
	if (theWindow) {
        execCallback(callback, theWindow);
        return theWindow;
	} 
	
	try {
		if (window) {
            execCallback(callback, window);
			return window;
		} 
	} catch (e) {
		// no window "yet". This should only ever be the case in a test env.
		// Fall through and use callbacks, if supplied.
	}

	if (callback) {
        function waitForWindow(callback) {
            callbacks.push(callback);
            var windowSetTimeout = setTimeout(function() {
                callback.error = "Timed out waiting on the window to be set.";
                callback.call(callback);
            }, (timeout?timeout:defaultTimeout));
            windowSetTimeouts.push(windowSetTimeout);
        }
        waitForWindow(callback);
	} else {
		throw "No 'window' available. Consider providing a 'callback' and receiving the 'window' async when available. Typically, this should only be the case in a test environment.";
	}
}

/**
 * Set the global window e.g. in a test environment.
 * <p>
 * Once called, all callbacks (registered by earlier 'getWindow' calls) will be invoked.
 * 
 * @param newWindow The window.
 */
exports.setWindow = function(newWindow) {
	for (var i = 0; i < windowSetTimeouts.length; i++) {
		clearTimeout(windowSetTimeouts[i]);
	}
    windowSetTimeouts = [];
	theWindow = newWindow;
	for (var i = 0; i < callbacks.length; i++) {
		execCallback(callbacks[i], theWindow);
	}
    callbacks = [];
}

/**
 * Set the default time to wait for the global window to be set.
 * <p>
 * Default is 10 seconds (10000 ms).
 * 
 * @param millis Milliseconds to wait for the global window to be set.
 */
exports.setDefaultTimeout = function(millis) {
    defaultTimeout = millis;
}
},{}],5:[function(require,module,exports){


var handlebars = {};

require('jenkins-js-modules').import('handlebars:handlebars3')
    .onFulfilled(function(handlebars3) {
        for (var member in handlebars3) {
            handlebars[member] = handlebars3[member];
        }
    });

module.exports = handlebars;

},{"jenkins-js-modules":1}],6:[function(require,module,exports){
/**
 * Expose the top level API type constructors.
 */

exports.ExtensionPointContainer = require('./js/ExtensionPointContainer.js');
exports.ExtensionPoint = require('./js/ExtensionPoint.js');
exports.ExtensionPointContribution = require('./js/ExtensionPointContribution.js');

exports.extensionPointContainer = new exports.ExtensionPointContainer();
},{"./js/ExtensionPoint.js":7,"./js/ExtensionPointContainer.js":8,"./js/ExtensionPointContribution.js":9}],7:[function(require,module,exports){
/**
 * Represents an actual "point" in the UI to which "contributions" can be made.
 * <p>
 * See README.md.
 */

// TODO: Refine the docs as we learn more.

module.exports = ExtensionPoint;

var ExtensionPointContribution = require('./ExtensionPointContribution.js');

/**
 * Create an ExtensionPoint instance.
 * @param type The type.
 * @param id A unique ID for the ExtensionPoint.
 * @param dock Dock element (DOM element).
 * @constructor
 */
function ExtensionPoint(type, id, dock) {
    if (!type) {
        throw 'You must define an ExtensionPoint "type".';
    }
    if (!id) { // I hope making this a requirement will not be an issue.
        throw 'You must define an ExtensionPoint "id".';
    }
    this.type = type;
    this.id = id;
    this.dock = dock;

    // private properties - do not use
    this._private = {};
    this._private.oncontributeCallback = undefined;
    this._private.contributions = [];
}

/**
 * Contribute to the Extension Point.
 * <p>
 * An instance of <code>ExtensionPointContribution</code>.
 */
ExtensionPoint.prototype.contribute = function(contribution) {
    if (contribution instanceof ExtensionPointContribution) {
        this._private.contributions.push(contribution);                
    } else {
        throw '"contribution" must be of type ExtensionPointContribution.';
    }
    
    // Fire the oncontribute callback if one was supplied.
    var extensionPoint = this;
    if (extensionPoint._private.oncontributeCallback) {
        setTimeout(function() {
            extensionPoint._private.oncontributeCallback.call(contribution);
        }, 0);
    } 
};

/**
 * Set an contribution registration listener.
 * @param listener Listener function for when a contribution is made via
 * the contribute function.
 */
ExtensionPoint.prototype.oncontribute = function(listener) {
    this._private.oncontributeCallback = listener;
};
},{"./ExtensionPointContribution.js":9}],8:[function(require,module,exports){
/**
 * Represents a UI "part" that contains one or more ExtensionPoint instances. 
 * <p>
 * I guess that, in time, this may morph into a page level construct.
 * <p>
 * See README.md.
 */

// TODO: Refine the docs as we learn more.

module.exports = ExtensionPointContainer;

var ExtensionPoint = require('./ExtensionPoint.js');

function ExtensionPointContainer() {
    // private properties - do not use
    this._private = {};
    this._private.extensionPoints = [];
    this._private.onchangeListeners = [];
}

/**
 * Add an ExtensionPoint.
 * <p>
 * Removal is done by calling <code>this.remove()</code>
 * inside <code>forEach</code>.
 * @param extensionPoint The ExtensionPoint instance.
 */
ExtensionPointContainer.prototype.add = function(extensionPoint) {
    if (extensionPoint instanceof ExtensionPoint) {
        this._private.extensionPoints.push(extensionPoint);
    } else {
        throw '"extensionPoint" must be of type ExtensionPoint.';
    }
};

/**
 * Add an onchange listener.
 * @param listener The listener.
 */
ExtensionPointContainer.prototype.onchange = function(listener) {
    this._private.onchangeListeners.push(listener);
};

/**
 * Iterate the ExtensionPoint instance in this container.
 * <p>
 * The context object (<code>this</code>) provided to the callback call
 * contains a <code>remove</code> function.
 * @param callback The callback to call for each ExtensionPoint in the container.
 * The ExtensionPoint instance is supplied as the first argument to the callback. 
 * @param ofType ExtensionPoint type filter.
 */
ExtensionPointContainer.prototype.forEach = function(callback, ofType) {
    var extensionPointsToRemove = [];
    var extensionPoint;
    
    function callCallback(extensionPoint) {
        callback.call({
            remove: function() {
                extensionPointsToRemove.push(extensionPoint);
            } 
        }, extensionPoint);
    }
    
    for (var i = 0; i < this._private.extensionPoints.length; i++) {
        extensionPoint = this._private.extensionPoints[i];
        if (!ofType || extensionPoint.type === ofType) {
            callCallback(extensionPoint);
        }
    }
    
    if (extensionPointsToRemove.length > 0) {
        var isInRemoveList = function(extensionPoint) {
            for (var ii = 0; ii < extensionPointsToRemove.length; ii++) {
                if (extensionPointsToRemove[ii] === extensionPoint) {
                    return true;
                }
            }
            return false;
        };
        
        // Create a new list, omitting these extension points.
        var newList = [];
        for (var iii = 0; iii < this._private.extensionPoints.length; iii++) {
            extensionPoint = this._private.extensionPoints[iii];
            if (!isInRemoveList(extensionPoint)) {
                newList.push(extensionPoint);
            }
        }
        this._private.extensionPoints = newList;
    }
};

/**
 * Remove all ExtensionPoint instances from this container.
 * @param ofType ExtensionPoint instance type filter (optional).
 */
ExtensionPointContainer.prototype.remove = function(ofType) {
    this.forEach(function(extensionPoint) {
        if (!ofType || extensionPoint.type === ofType) {
            this.remove();
        }
    });
};

/**
 * Notify all onchange listeners. This must be called by the
 * ExtensionPointContainer "owner". Intentionally not calling it
 * from add/remove etc because that might result in a flood of unnecessary
 * activity e.g. on batch add/remove.
 */
ExtensionPointContainer.prototype.notifyOnChange = function() {
    for (var i = 0; i < this._private.onchangeListeners.length; i++) {
        try {
            this._private.onchangeListeners[i].call(this);
        } catch (e) {
            console.warn(e);
        }
    }
};
},{"./ExtensionPoint.js":7}],9:[function(require,module,exports){
/**
 * Represents a "contribution" to an ExtensionPoint.
 * <p>
 * See README.md.
 */

// TODO: Refine the docs as we learn more.

module.exports = ExtensionPointContribution;

function ExtensionPointContribution() {
    this.activatorContent = undefined;
    this.activatorContentType = 'blank'; // or 'markup' or 'className'
    this.caption = undefined;
    this.content = undefined;    

    // private properties - do not use
    this._private = {};
    this._private.onshowCallback = undefined;
    this._private.onhideCallback = undefined;
}

/**
 * Set the activator widget content used to activate the contribution content.
 * <p>
 * If the extension point implementation uses activation widgets (e.g. an icon of some sort), then
 * this widget will be dropped in the extension point activation area and then used to trigger the rendering
 * of the extension point.
 * 
 * @param activatorContent A piece of markup, or just a CSS class name. The rules of what are required
 * here depend on the ExtensionPoint implementation i.e. it could be a button or an icon etc. See
 * 'activatorContentType' property.
 */
ExtensionPointContribution.prototype.setActivator = function(activatorContent) {
    this.activatorContent = activatorContent;
    if (this.activatorContent) {
        this.activatorContent = this.activatorContent.trim();
        if (this.activatorContent.length > 0) {
            if (this.activatorContent.charAt(0) === '<') {
                this.activatorContentType = 'markup';
            } else {
                this.activatorContentType = 'className';
            }
        }
    }
    return this;
};

/**
 * Set caption text for the contribution.
 */
ExtensionPointContribution.prototype.setCaption = function(caption) {
    this.caption = caption;
    return this;
};

/**
 * Set the content to be displayed on activation of the contribution.
 * <p>
 * This can be an "initial" piece of content that is "enriched" by the <code>onShow</code>
 * method e.g. after calling a REST API method.
 * <p>
 * Depending on the ExtensionPoint, activation of this content may be based on user interaction with the
 * "activator widget", or may be from some other mechanism.
 */
ExtensionPointContribution.prototype.setContent = function(content) {
    this.content = content;
    return this;
};

/**
 * Set the callback function to be called after the activation content has been displayed.
 * <p>
 * The on <code>onShow</code> context object will be the DOM element that contains the
 * rendered content that was supplied to the <code>setContent</code> function (or an empty element if 
 * <code>setContent</code> was not called).
 */
ExtensionPointContribution.prototype.onShow = function(onshow) {
    this._private.onshowCallback = onshow;
    return this;
};

/**
 * Trigger the onShow callback, if one was registered.
 * @param containerElement The DOM element containing the content that was shown.
 */
ExtensionPointContribution.prototype.show = function(containerElement) {
    if (!containerElement) {
        throw 'Call to ExtensionPointContribution.show() without specifying a "containerElement".';
    } 
    if (this._private.onshowCallback) {
        this._private.onshowCallback.call(containerElement);
    }
    return this;
};

/**
 * Set the callback function to be called after the contribution content is to be "undisplayed".
 */
ExtensionPointContribution.prototype.onHide = function(onhide) {
    this._private.onhideCallback = onhide;
    return this;
};

/**
 * Trigger the onHide callback, if one was registered.
 */
ExtensionPointContribution.prototype.hide = function() {
    if (this._private.onhideCallback) {
        this._private.onhideCallback.call();
    }
    return this;
};

},{}],10:[function(require,module,exports){
var internal = require("./internal");
var promise = require("./promise");
var onRegisterTimeout;
var whoami;

/**
 * What's the top level module/bundle name.
 * @param moduleQName The module QName.
 * @returns The module QName, or undefined if unknown.
 */
exports.whoami = function(moduleQName) {
    if (moduleQName) {
        whoami = moduleQName;
        internal.whoami(whoami);
    }
    return whoami;
};

/**
 * Asynchronously import/require a set of modules.
 *
 * <p>
 * Responsible for triggering the async loading of modules if a given module is not already loaded.
 *
 * @param moduleQNames... A list of module "qualified" names, each containing the module name prefixed with the namespace
 * and separated by a colon i.e. "<namespace>:<moduleName>" e.g. "jquery:jquery2".
 *
 * @return A Promise, allowing async load of all modules. The promise is only fulfilled when all modules are loaded.
 */
exports.import = function() {
    if (arguments.length === 1) {
        return internal.import(arguments[0], onRegisterTimeout);        
    }
    
    var moduleQNames = [];    
    for (var i = 0; i < arguments.length; i++) {
        var argument = arguments[i];
        if (typeof argument === 'string') {
            moduleQNames.push(argument);
        }
    }
    
    if (moduleQNames.length == 0) {
        throw "No module names specified.";
    }
    
    return promise.make(function (resolve, reject) {
        var fulfillments = [];
        
        function onFulfillment() {
            if (fulfillments.length === moduleQNames.length) {
                var modules = [];
                for (var i = 0; i < fulfillments.length; i++) {
                    if (fulfillments[i].value) {
                        modules.push(fulfillments[i].value);
                    } else {
                        // don't have everything yet so can't fulfill all.
                        return;
                    }
                }
                // If we make it here, then we have fulfilled all individual promises, which 
                // means we can now fulfill the top level import promise.
                resolve(modules);
            }
        }        
        
        // doRequire for each module
        for (var i = 0; i < moduleQNames.length; i++) {           
            function doRequire(moduleQName) {
                var promise = internal.import(moduleQName, onRegisterTimeout);
                var fulfillment = {
                    promise: promise,
                    value: undefined
                };
                fulfillments.push(fulfillment);
                promise
                    .onFulfilled(function(value) {
                        fulfillment.value = value;
                        onFulfillment();
                    })
                    .onRejected(function(error) {
                        reject(error);
                    });
            }
            doRequire(moduleQNames[i]);
        }
    }).applyArgsOnFulfill();    
};

/**
 * Synchronously "require" a module that it already loaded/registered.
 *
 * <p>
 * This function will throw an error if the module is not already loaded via an outer call to 'import'
 * (or 'import').
 *
 * @param moduleQName The module "qualified" name containing the module name prefixed with the namespace
 * separated by a colon i.e. "<namespace>:<moduleName>" e.g. "jquery:jquery2".
 *
 * @return The module.
 */
exports.require = function(moduleQName) {
    var parsedModuleName = internal.parseResourceQName(moduleQName);
    var module = internal.getModule(parsedModuleName);    
    if (!module) {
        throw "Unable to perform synchronous 'require' for module '" + moduleQName + "'. This module is not pre-loaded. " +
            "The module needs to have been asynchronously pre-loaded via an outer call to 'import'.";
    }
    return module.exports;
}

/**
 * Export a module.
 * 
 * @param namespace The namespace in which the module resides, or "undefined" if the modules is in
 * the "global" module namespace e.g. a Jenkins core bundle.
 * @param moduleName The name of the module. 
 * @param module The CommonJS style module, or "undefined" if we just want to notify other modules waiting on
 * the loading of this module.
 * @param onError On error callback;
 */
exports.export = function(namespace, moduleName, module, onError) {
    internal.onReady(function() {
        try {
            var moduleSpec = {namespace: namespace, moduleName: moduleName};
            var moduleNamespaceObj = internal.getModuleNamespaceObj(moduleSpec);
            
            if (moduleNamespaceObj[moduleName]) {
                if (namespace) {
                    throw "Jenkins plugin module '" + namespace + ":" + moduleName + "' already registered.";
                } else {
                    throw "Jenkins global module '" + moduleName + "' already registered.";
                }
            }
            
            if (!module) {
                module = {
                    exports: {}
                };
            } else if (module.exports === undefined) {
                module = {
                    exports: module
                };
            }
            moduleNamespaceObj[moduleName] = module;
            
            // Notify all that the module has been registered. See internal.loadModule also.
            internal.notifyModuleExported(moduleSpec, module.exports);
        } catch (e) {
            console.error(e);
            if (onError) {
                onError(e);
            }
        }
    });
};

/**
 * Add a module's CSS to the browser page.
 * 
 * <p>
 * The assumption is that the CSS can be accessed at e.g.
 * {@code <rootURL>/plugin/<namespace>/jsmodules/<moduleName>/style.css} i.e.
 * the pluginId acts as the namespace.
 * 
 * @param namespace The namespace in which the module resides.
 * @param moduleName The name of the module. 
 * @param onError On error callback;
 */
exports.addModuleCSSToPage = function(namespace, moduleName, onError) {
    internal.onReady(function() {
        try {
            internal.addModuleCSSToPage(namespace, moduleName);
        } catch (e) {
            console.error(e);
            if (onError) {
                onError(e);
            }
        }
    });
};

/**
 * Add a plugin CSS file to the browser page.
 * 
 * @param pluginName The Jenkins plugin in which the module resides.
 * @param cssPath The CSS path. 
 * @param onError On error callback;
 */
exports.addPluginCSSToPage = function(pluginName, cssPath, onError) {
    internal.onReady(function() {
        try {
            internal.addPluginCSSToPage(pluginName, cssPath);
        } catch (e) {
            console.error(e);
            if (onError) {
                onError(e);
            }
        }
    });
};

/**
 * Add CSS file to the browser page.
 * 
 * @param cssPath The CSS path. 
 * @param onError On error callback;
 */
exports.addCSSToPage = function(cssPath, onError) {
    internal.onReady(function() {
        try {           
            internal.addCSSToPage('global', internal.getRootURL() + '/' + cssPath);
        } catch (e) {
            console.error(e);
            if (onError) {
                onError(e);
            }
        }
    });
};

/**
 * Add a javascript &lt;script&gt; element to the document &lt;head&gt;.
 * <p/>
 * Options:
 * <ul>
 *     <li><strong>scriptId</strong>: The script Id to use for the element. If not specified, one will be generated from the scriptSrc.</li>
 *     <li><strong>async</strong>: Asynchronous loading of the script. Default is 'true'.</li>
 *     <li><strong>success</strong>: An optional onload success function for the script element.</li>
 *     <li><strong>error</strong>: An optional onload error function for the script element. This is called if the .js file exists but there's an error evaluating the script. It is NOT called if the .js file doesn't exist (ala 404).</li>
 *     <li><strong>removeElementOnLoad</strong>: Remove the script element after loading the script. Default is 'false'.</li>
 * </ul>
 * 
 * @param scriptSrc The script src.
 * @param options Optional script load options object. See above.
 */
exports.addScript = function(scriptSrc, options) {
    internal.onReady(function() {
        internal.addScript(scriptSrc, options);
    });    
};

/**
 * Set the module registration timeout i.e. the length of time to wait for a module to load before failing.
 *
 * @param timeout Millisecond duration before onRegister times out. Defaults to 10000 (10s) if not specified.
 */
exports.setRegisterTimeout = function(timeout) {
    onRegisterTimeout = timeout;
}

/**
 * Set the Jenkins root/base URL.
 * 
 * @param rootUrl The root/base URL.
 */
exports.setRootURL = function(rootUrl) {
    internal.setRootURL(rootUrl);
};

exports.getRootURL = internal.getRootURL;
exports.getAdjunctURL = internal.getAdjunctURL;

/**
 * Manually initialise the Jenkins Global.
 * <p>
 * This should only ever be called from a test environment.
 */
exports.initJenkinsGlobal = function() {
    internal.initJenkinsGlobal();
};

internal.onJenkinsGlobalInit(function(jenkinsCIGlobal) {
    // For backward compatibility, we need to make some jenkins-js-modules
    // functions globally available e.g. to allow legacy code wait for
    // certain modules to be loaded, as with legacy adjuncts.
    if (!jenkinsCIGlobal._internal) {
        // Put the functions on an object called '_internal' as a way
        // of hinting to people to not use it.
        jenkinsCIGlobal._internal = {
            import: exports.import,
            addScript: internal.addScript
        };
    }
});
},{"./internal":11,"./promise":12}],11:[function(require,module,exports){
var promise = require("./promise");
var windowHandle = require("window-handle");
var jenkinsCIGlobal;
var globalInitListeners = [];
var whoami;

exports.whoami = function(moduleQName) {
    if (moduleQName) {
        whoami = exports.parseResourceQName(moduleQName);
        whoami.nsProvider = getBundleNSProviderFromScriptElement(whoami.namespace, whoami.moduleName);
    }
    return whoami;
};

exports.onReady = function(callback) {
    // This allows test based initialization of jenkins-js-modules when there might 
    // not yet be a global window object.
    if (jenkinsCIGlobal) {
        callback();
    } else {
        windowHandle.getWindow(function() {
            callback();
        });
    }    
};

exports.onJenkinsGlobalInit = function(callback) {
    globalInitListeners.push(callback);
};

exports.initJenkinsGlobal = function() {
    jenkinsCIGlobal = {
    };
    if (globalInitListeners) {
        for (var i = 0; i < globalInitListeners.length; i++) {
            globalInitListeners[i](jenkinsCIGlobal);
        }
    }
};

exports.clearJenkinsGlobal = function() {    
    jenkinsCIGlobal = undefined;
    whoami = undefined;
};

exports.getJenkins = function() {
    if (jenkinsCIGlobal) {
        return jenkinsCIGlobal;
    }
    var window = windowHandle.getWindow();
    if (window.jenkinsCIGlobal) {
        jenkinsCIGlobal = window.jenkinsCIGlobal;
    } else {
        exports.initJenkinsGlobal();
        jenkinsCIGlobal.rootURL = getRootURL();
        window.jenkinsCIGlobal = jenkinsCIGlobal;
    }   
    return jenkinsCIGlobal;
};

exports.getModuleNamespaceObj = function(moduleSpec) {
    if (moduleSpec.namespace) {
        return exports.getNamespace(moduleSpec.namespace);
    } else {
        return exports.getGlobalModules();
    }
}

exports.getNamespace = function(namespaceName) {
    var namespaces = exports.getNamespaces();
    var namespace = namespaces[namespaceName];
    if (!namespace) {
        namespace = {
            globalNS: false            
        };
        namespaces[namespaceName] = namespace;
    }
    return namespace;
};

exports.import = function(moduleQName, onRegisterTimeout) {
    return promise.make(function (resolve, reject) {
        // Some functions here needs to access the 'window' global. We want to make sure that
        // exists before attempting to fulfill the require operation. It may not exists
        // immediately in a test env.
        exports.onReady(function() {
            var moduleSpec = exports.parseResourceQName(moduleQName);
            var module = exports.getModule(moduleSpec);
            
            if (module) {
                // module already loaded
                resolve(module.exports);
            } else {
                if (onRegisterTimeout === 0) {
                    if (moduleSpec.namespace) {
                        throw 'Module ' + moduleSpec.namespace + ':' + moduleSpec.moduleName + ' require failure. Async load mode disabled.';
                    } else {
                        throw 'Global module ' + moduleSpec.moduleName + ' require failure. Async load mode disabled.';
                    }
                }

                // module not loaded. Load async, fulfilling promise once registered
                exports.loadModule(moduleSpec, onRegisterTimeout)
                    .onFulfilled(function (moduleExports) {
                        resolve(moduleExports);
                    })
                    .onRejected(function (error) {
                        reject(error);
                    });
            }
        });
    });    
};

exports.loadModule = function(moduleSpec, onRegisterTimeout) {
    var moduleNamespaceObj = exports.getModuleNamespaceObj(moduleSpec);
    var module = moduleNamespaceObj[moduleSpec.moduleName];
    
    if (module) {
        // Module already loaded. This prob shouldn't happen.
        console.log("Unexpected call to 'loadModule' for a module (" + moduleSpec.moduleName + ") that's already loaded.");
        return promise.make(function (resolve) {
            resolve(module.exports);
        });
    }

    function waitForRegistration(loadingModule, onRegisterTimeout) {
        return promise.make(function (resolve, reject) {
            if (typeof onRegisterTimeout !== "number") {
                onRegisterTimeout = 10000;
            }
            
            var timeoutObj = setTimeout(function () {
                // Timed out waiting on the module to load and register itself.
                if (!loadingModule.loaded) {
                    var moduleSpec = loadingModule.moduleSpec;
                    var errorDetail;
                    
                    if (moduleSpec.namespace) {
                        errorDetail = "Timed out waiting on module '" + moduleSpec.namespace + ":" + moduleSpec.moduleName + "' to load.";
                    } else {
                        errorDetail = "Timed out waiting on global module '" + moduleSpec.moduleName + "' to load.";
                    }                    
                    console.error('Module load failure: ' + errorDetail);

                    // Call the reject function and tell it we timed out
                    reject({
                        reason: 'timeout',
                        detail: errorDetail
                    });
                }
            }, onRegisterTimeout);
            
            loadingModule.waitList.push({
                resolve: resolve,
                timeoutObj: timeoutObj
            });                    
        });
    }
    
    var loadingModule = getLoadingModule(moduleNamespaceObj, moduleSpec.moduleName);
    if (!loadingModule.waitList) {
        loadingModule.waitList = [];
    }
    loadingModule.moduleSpec = moduleSpec; 
    loadingModule.loaded = false;

    try {
        return waitForRegistration(loadingModule, onRegisterTimeout);
    } finally {
        // We can auto/dynamic load modules in a non-global namespace. Global namespace modules
        // need to make sure they load themselves (via an adjunct, or whatever).
        if (moduleSpec.namespace) {
            var scriptId = exports.toModuleId(moduleSpec.namespace, moduleSpec.moduleName) + ':js';
            var scriptSrc = exports.toModuleSrc(moduleSpec, 'js');
            var scriptEl = exports.addScript(scriptSrc, {
                scriptId: scriptId,
                scriptSrcBase: ''
            });

            if (scriptEl) {
                // Set the module spec info on the <script> element. This allows us to resolve the
                // nsProvider for that bundle after 'whoami' is called for it (as it loads). whoami
                // is not called with the nsProvider info on it because a given bundle can
                // potentially be loaded from multiple different ns providers, so we only resole the provider
                // at load-time i.e. just after a bundle is loaded it calls 'whoami' for itself
                // and then this module magically works out where it was loaded from (it's nsProvider)
                // by locating the <script> element and using this information. For a module/bundle, knowing
                // where it was loaded from is important because it dictates where that module/bundle
                // should load it dependencies from. For example, the Bootstrap module/bundle depends on the
                // jQuery bundle. So, if the bootstrap bundle is loaded from the 'core-assets' namespace provider,
                // then that means the jQuery bundle should also be loaded from the 'core-assets'
                // namespace provider.
                // See getBundleNSProviderFromScriptElement.
                scriptEl.setAttribute('data-jenkins-module-nsProvider', moduleSpec.nsProvider);
                scriptEl.setAttribute('data-jenkins-module-namespace', moduleSpec.namespace);
                scriptEl.setAttribute('data-jenkins-module-moduleName', moduleSpec.moduleName);
            }
        }
    }
};

exports.addScript = function(scriptSrc, options) {
    if (!scriptSrc) {
        console.warn('Call to addScript with undefined "scriptSrc" arg.');
        return undefined;
    }    
    
    var normalizedOptions;
    
    // If there's no options object, create it.
    if (typeof options === 'object') {
        normalizedOptions = options;
    } else {
        normalizedOptions = {};
    }
    
    // May want to transform/map some urls.
    if (normalizedOptions.scriptSrcMap) {
        if (typeof normalizedOptions.scriptSrcMap === 'function') {
            scriptSrc = normalizedOptions.scriptSrcMap(scriptSrc);
        } else if (Array.isArray(normalizedOptions.scriptSrcMap)) {
            // it's an array of suffix mappings
            for (var i = 0; i < normalizedOptions.scriptSrcMap.length; i++) {
                var mapping = normalizedOptions.scriptSrcMap[i];
                if (mapping.from && mapping.to) {
                    if (endsWith(scriptSrc, mapping.from)) {
                        normalizedOptions.originalScriptSrc = scriptSrc;
                        scriptSrc = scriptSrc.replace(mapping.from, mapping.to);
                        break;
                    }
                }
            }
        }
    }
    
    normalizedOptions.scriptId = getScriptId(scriptSrc, options);
    
    // set some default options
    if (normalizedOptions.async === undefined) {
        normalizedOptions.async = true;
    }
    if (normalizedOptions.scriptSrcBase === undefined) {
        normalizedOptions.scriptSrcBase = '@root';
    }
    
    if (normalizedOptions.scriptSrcBase === '@root') {
        normalizedOptions.scriptSrcBase = getRootURL() + '/';
    } else if (normalizedOptions.scriptSrcBase === '@adjunct') {
        normalizedOptions.scriptSrcBase = getAdjunctURL() + '/';
    }

    var document = windowHandle.getWindow().document;
    var head = exports.getHeadElement();
    var script = document.getElementById(normalizedOptions.scriptId);

    if (script) {
        var replaceable = script.getAttribute('data-replaceable');
        if (replaceable && replaceable === 'true') {
            // This <script> element is replaceable. In this case, 
            // we remove the existing script element and add a new one of the
            // same id and with the specified src attribute.
            // Adding happens below.
            script.parentNode.removeChild(script);
        } else {
            return undefined;
        }
    }

    script = createElement('script');

    // Parts of the following onload code were inspired by how the ACE editor does it,
    // as well as from the follow SO post: http://stackoverflow.com/a/4845802/1166986
    var onload = function (_, isAborted) {
        script.setAttribute('data-onload-complete', true);
        try {
            if (isAborted) {
                console.warn('Script load aborted: ' + scriptSrc);
            } else if (!script.readyState || script.readyState === "loaded" || script.readyState === "complete") {
                // If the options contains an onload function, call it.
                if (typeof normalizedOptions.success === 'function') {
                    normalizedOptions.success(script);
                }
                return;
            }
            if (typeof normalizedOptions.error === 'function') {
                normalizedOptions.error(script, isAborted);
            }
        } finally {
            if (normalizedOptions.removeElementOnLoad) {
                head.removeChild(script);
            }
            // Handle memory leak in IE
            script = script.onload = script.onreadystatechange = null;
        }
    };
    script.onload = onload; 
    script.onreadystatechange = onload;

    script.setAttribute('id', normalizedOptions.scriptId);
    script.setAttribute('type', 'text/javascript');
    script.setAttribute('src', normalizedOptions.scriptSrcBase + scriptSrc);
    if (normalizedOptions.originalScriptSrc) {
        script.setAttribute('data-referrer', normalizedOptions.originalScriptSrc);        
    }
    if (normalizedOptions.async) {
        script.setAttribute('async', normalizedOptions.async);
    }
    
    head.appendChild(script);
    
    return script;
};

exports.notifyModuleExported = function(moduleSpec, moduleExports) {
    var moduleNamespaceObj = exports.getModuleNamespaceObj(moduleSpec);
    var loadingModule = getLoadingModule(moduleNamespaceObj, moduleSpec.moduleName);
    
    loadingModule.loaded = true;
    if (loadingModule.waitList) {
        for (var i = 0; i < loadingModule.waitList.length; i++) {
            var waiter = loadingModule.waitList[i];
            clearTimeout(waiter.timeoutObj);
            waiter.resolve(moduleExports);
        }
    }    
};

exports.addModuleCSSToPage = function(namespace, moduleName) {
    var moduleSpec = exports.getModuleSpec(namespace + ':' + moduleName);
    var cssElId = exports.toModuleId(namespace, moduleName) + ':css';
    var cssPath = exports.toModuleSrc(moduleSpec, 'css');
    return exports.addCSSToPage(namespace, cssPath, cssElId);
};

exports.addPluginCSSToPage = function(namespace, cssPath, cssElId) {
    var cssPath = exports.getPluginPath(namespace) + '/' + cssPath;
    return exports.addCSSToPage(namespace, cssPath, cssElId);
};

exports.addCSSToPage = function(namespace, cssPath, cssElId) {
    var document = windowHandle.getWindow().document;
    
    if (cssElId === undefined) {
        cssElId = 'jenkins-js-module:' + namespace + ':css:' + cssPath;
    }
    
    var cssEl = document.getElementById(cssElId);
    
    if (cssEl) {
        // already added to page
        return;
    }

    var docHead = exports.getHeadElement();
    cssEl = createElement('link');
    cssEl.setAttribute('id', cssElId);
    cssEl.setAttribute('type', 'text/css');
    cssEl.setAttribute('rel', 'stylesheet');
    cssEl.setAttribute('href', cssPath);
    docHead.appendChild(cssEl);

    return cssEl;
};

exports.getGlobalModules = function() {
    var jenkinsCIGlobal = exports.getJenkins();
    if (!jenkinsCIGlobal.globals) {
        jenkinsCIGlobal.globals = {
            globalNS: true
        };
    }
    return jenkinsCIGlobal.globals;
};

exports.getNamespaces = function() {
    var jenkinsCIGlobal = exports.getJenkins();

    // The namespaces are stored in an object named "plugins". This is a legacy from the
    // time when all modules lived in plugins. By right we'd like to rename this, but
    // that would cause compatibility issues.

    if (!jenkinsCIGlobal.plugins) {
        jenkinsCIGlobal.plugins = {
            __README__: 'This object holds namespaced JS modules/bundles, with the property names representing the module namespace. It\'s name ("plugins") is a legacy thing. Changing it to a better name (e.g. "namespaces") would cause compatibility issues.'
        };
    }
    return jenkinsCIGlobal.plugins;
};

exports.toModuleId = function(namespace, moduleName) {
    return 'jenkins-js-module:' + namespace + ':' + moduleName;
};

exports.toModuleSrc = function(moduleSpec, srcType) {
    var nsProvider = moduleSpec.nsProvider;

    // If a moduleSpec on a module/bundle import doesn't specify a namespace provider
    // (i.e. is of the form "a:b" and not "core-assets/a:b"),
    // then check "this" bundles module spec and see if it was imported from a specific
    // namespace. If it was (e.g. 'core-assets'), then import from that namespace.
    if (nsProvider === undefined) {
        nsProvider = thisBundleNamespaceProvider();
        if (nsProvider === undefined) {
            nsProvider = 'plugin';
        }
        // Store the nsProvider back onto the moduleSpec.
        moduleSpec.nsProvider = nsProvider;
    }

    var srcPath = undefined;
    if (srcType === 'js') {
        srcPath = moduleSpec.moduleName + '.js';
    } else if (srcType === 'css') {
        srcPath = moduleSpec.moduleName + '/style.css';
    } else {
        throw 'Unsupported srcType "'+ srcType + '".';
    }

    if (nsProvider === 'plugin') {
        return exports.getPluginJSModulesPath(moduleSpec.namespace) + '/' + srcPath;
    } if (nsProvider === 'core-assets') {
        return exports.getCoreAssetsJSModulesPath(moduleSpec.namespace) + '/' + srcPath;
    } else {
        throw 'Unsupported namespace provider: ' + nsProvider;
    }
};

exports.getPluginJSModulesPath = function(pluginId) {
    return exports.getPluginPath(pluginId) + '/jsmodules';
};

exports.getCoreAssetsJSModulesPath = function(namespace) {
    return getRootURL() + '/assets/' + namespace + '/jsmodules';
};

exports.getPluginPath = function(pluginId) {
    return getRootURL() + '/plugin/' + pluginId;
};

exports.getHeadElement = function() {
    var window = windowHandle.getWindow();
    var docHead = window.document.getElementsByTagName("head");
    if (!docHead || docHead.length == 0) {
        throw 'No head element found in document.';
    }
    return docHead[0];
};

exports.setRootURL = function(url) {    
    if (!jenkinsCIGlobal) {
        exports.initJenkinsGlobal();
    }
    jenkinsCIGlobal.rootURL = url;
};

exports.parseResourceQName = function(resourceQName) {
    var qNameTokens = resourceQName.split(":");
    if (qNameTokens.length === 2) {
        var namespace = qNameTokens[0].trim();
        var nsTokens = namespace.split("/");
        var namespaceProvider = undefined;
        if (nsTokens.length === 2) {
            namespaceProvider = nsTokens[0].trim();
            namespace = nsTokens[1].trim();
            if (namespaceProvider !== 'plugin' && namespaceProvider !== 'core-assets') {
                console.error('Unsupported module namespace provider "' + namespaceProvider + '". Setting to undefined.');
                namespaceProvider = undefined;
            }
        }
        return {
            nsProvider: namespaceProvider,
            namespace: namespace,
            moduleName: qNameTokens[1].trim()
        };
    } else {
        // The module/bundle is not in a namespace and doesn't
        // need to be loaded i.e. it will load itself and export.
        return {
            moduleName: qNameTokens[0].trim()
        };
    }
};

exports.getModule = function(moduleSpec) {
    if (moduleSpec.namespace) {
        var plugin = exports.getNamespace(moduleSpec.namespace);
        return plugin[moduleSpec.moduleName];
    } else {
        var globals = exports.getGlobalModules();
        return globals[moduleSpec.moduleName];
    }
};

exports.getModuleSpec = function(moduleQName) {
    var moduleSpec = exports.parseResourceQName(moduleQName);
    var moduleNamespaceObj = exports.getModuleNamespaceObj(moduleSpec);
    if (moduleNamespaceObj) {
        var loading = getLoadingModule(moduleNamespaceObj, moduleSpec.moduleName);
        if (loading && loading.moduleSpec) {
            return loading.moduleSpec;
        }
    }
    return moduleSpec;
};

function getScriptId(scriptSrc, config) {
    if (typeof config === 'string') {
        return config;
    } else if (typeof config === 'object' && config.scriptId) {
        return config.scriptId;
    } else {
        return 'jenkins-script:' + scriptSrc;
    }    
}

exports.getRootURL = getRootURL;
function getRootURL() {
    if (jenkinsCIGlobal && jenkinsCIGlobal.rootURL) {
        return jenkinsCIGlobal.rootURL;
    }
    
    var docHead = exports.getHeadElement();
    var resURL = getAttribute(docHead, "data-resurl");

    if (!resURL) {
        var resURL = getAttribute(docHead, "resURL");
    
        if (!resURL) {
            throw "Attribute 'data-resurl' not defined on the document <head> element.";
        }
    }

    if (jenkinsCIGlobal) {
        jenkinsCIGlobal.rootURL = resURL;
    }
    
    return resURL;
}

exports.getAdjunctURL = getAdjunctURL;
function getAdjunctURL() {
    if (jenkinsCIGlobal && jenkinsCIGlobal.adjunctURL) {
        return jenkinsCIGlobal.adjunctURL;
    }
    
    var docHead = exports.getHeadElement();
    var adjunctURL = getAttribute(docHead, "data-adjuncturl");

    if (!adjunctURL) {
        throw "Attribute 'data-adjuncturl' not defined on the document <head> element.";
    }

    if (jenkinsCIGlobal) {
        jenkinsCIGlobal.adjunctURL = adjunctURL;
    }
    
    return adjunctURL;
}

function createElement(name) {
    var document = windowHandle.getWindow().document;
    return document.createElement(name);
}

function getAttribute(element, attributeName) {
    var value = element.getAttribute(attributeName.toLowerCase());
    
    if (value) {
        return value;
    } else {
        // try without lowercasing
        return element.getAttribute(attributeName);
    }    
}

function getLoadingModule(moduleNamespaceObj, moduleName) {
    if (!moduleNamespaceObj.loadingModules) {
        moduleNamespaceObj.loadingModules = {};
    }
    if (!moduleNamespaceObj.loadingModules[moduleName]) {
        moduleNamespaceObj.loadingModules[moduleName] = {};
    }
    return moduleNamespaceObj.loadingModules[moduleName];
}

function endsWith(string, suffix) {
    return (string.indexOf(suffix, string.length - suffix.length) !== -1);
}

function thisBundleNamespaceProvider() {
    if (whoami !== undefined) {
        return whoami.nsProvider;
    }
    return undefined;
}

function getBundleNSProviderFromScriptElement(namespace, moduleName) {
    var docHead = exports.getHeadElement();
    var scripts = docHead.getElementsByTagName("script");

    for (var i = 0; i < scripts.length; i++) {
        var script = scripts[i];
        var elNamespace = script.getAttribute('data-jenkins-module-namespace');
        var elModuleName = script.getAttribute('data-jenkins-module-moduleName');

        if (elNamespace === namespace && elModuleName === moduleName) {
            return script.getAttribute('data-jenkins-module-nsProvider');
        }
    }

    return undefined;
}

},{"./promise":12,"window-handle":13}],12:[function(require,module,exports){
arguments[4][3][0].apply(exports,arguments)
},{"dup":3}],13:[function(require,module,exports){
arguments[4][4][0].apply(exports,arguments)
},{"dup":4}],14:[function(require,module,exports){
var theWindow;
var defaultTimeout = 10000;
var callbacks = [];
var windowSetTimeouts = [];

function execCallback(callback, theWindow) {
    if (callback) {
        try {
            callback.call(callback, theWindow);                
        } catch (e) {
            console.log("Error invoking window-handle callback.");
            console.log(e.stack);
        }
    }
}

/**
 * Get the global "window" object.
 * @param callback An optional callback that can be used to receive the window asynchronously. Useful when
 * executing in test environment i.e. where the global window object might not exist immediately. 
 * @param timeout The timeout if waiting on the global window to be initialised.
 * @returns {*}
 */
exports.getWindow = function(callback, timeout) {
    callbacks.push(callback);
    
	if (theWindow) {
        execCallback(callback, theWindow);
        return theWindow;
	} 
	
	try {
		if (window) {
            execCallback(callback, window);
			return window;
		} 
	} catch (e) {
		// no window "yet". This should only ever be the case in a test env.
		// Fall through and use callbacks, if supplied.
	}

	if (callback) {
        function waitForWindow(callback) {
            var windowSetTimeout = setTimeout(function() {
                callback.error = "Timed out waiting on the window to be set.";
                callback.call(callback);
            }, (timeout?timeout:defaultTimeout));
            windowSetTimeouts.push(windowSetTimeout);
        }
        waitForWindow(callback);
	} else {
		throw new Error("No 'window' available. Consider providing a 'callback' and receiving the 'window' async when available. Typically, this should only be the case in a test environment.");
	}
}

/**
 * Set the global window e.g. in a test environment.
 * <p>
 * Once called, all callbacks (registered by earlier 'getWindow' calls) will be invoked.
 * 
 * @param newWindow The window.
 */
exports.setWindow = function(newWindow) {
	for (var i = 0; i < windowSetTimeouts.length; i++) {
		clearTimeout(windowSetTimeouts[i]);
	}
    windowSetTimeouts = [];
	theWindow = newWindow;
	for (var i = 0; i < callbacks.length; i++) {
		execCallback(callbacks[i], theWindow);
	}
}

/**
 * Set the default time to wait for the global window to be set.
 * <p>
 * Default is 10 seconds (10000 ms).
 * 
 * @param millis Milliseconds to wait for the global window to be set.
 */
exports.setDefaultTimeout = function(millis) {
    defaultTimeout = millis;
}

/**
 * Reset the window handle, clearing callbacks etc.
 */
exports.reset = function() {
    callbacks = [];
};
},{}],15:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var model = require('../model/lazy-load-object');
var view = require('../view/build-artifacts-popup');

exports.getName = function() {
    return 'build-artifacts-popup';
}

exports.getModel = function() {
    return model;
}

exports.getView = function() {
    return view;
}

},{"../model/lazy-load-object":28,"../view/build-artifacts-popup":44}],16:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
var model = require('../model/feature-tbd');
var view = require('../view/info-action-popover');

exports.getName = function() {
    return 'feature-tbd';
}

exports.getModel = function() {
    return model;
}

exports.getView = function() {
    return view;
}

},{"../model/feature-tbd":26,"../view/info-action-popover":45}],17:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var model = require('../model/lazy-load-object');
var view = require('../view/node-log');

exports.getName = function() {
    return 'node-log';
}

exports.getModel = function() {
    return model;
}

exports.getView = function() {
    return view;
}

},{"../model/lazy-load-object":28,"../view/node-log":46}],18:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var model = require('../model/runs-stage-grouped');
var view = require('../view/pipeline-staged');

exports.getName = function() {
    return 'pipeline-staged';
}

exports.getModel = function() {
    return model;
}

exports.getView = function() {
    return view;
}

},{"../model/runs-stage-grouped":33,"../view/pipeline-staged":47}],19:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var model = require('../model/no-model');
var view = require('../view/remove-sidepanel');

exports.getName = function() {
    return 'remove-sidepanel';
}

exports.getModel = function() {
    return model;
}

exports.getView = function() {
    return view;
}

},{"../model/no-model":29,"../view/remove-sidepanel":48}],20:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var model = require('../model/run-changesets');
var view = require('../view/run-changesets');

exports.getName = function() {
    return 'run-changesets';
}

exports.getModel = function() {
    return model;
}

exports.getView = function() {
    return view;
}

},{"../model/run-changesets":31,"../view/run-changesets":49}],21:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var model = require('../model/run-input-required');
var view = require('../view/run-input-required');

exports.getName = function() {
    return 'run-input-required';
}

exports.getModel = function() {
    return model;
}

exports.getView = function() {
    return view;
}

},{"../model/run-input-required":32,"../view/run-input-required":50}],22:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var model = require('../model/stage-actions');
var view = require('../view/info-action-popover');

exports.getName = function() {
    return 'stage-actions-popover';
}

exports.getModel = function() {
    return model;
}

exports.getView = function() {
    return view;
}

},{"../model/stage-actions":34,"../view/info-action-popover":45}],23:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var model = require('../model/stage-errors-message');
var view = require('../view/info-action-popover');

exports.getName = function() {
    return 'stage-failed-popover';
}

exports.getModel = function() {
    return model;
}

exports.getView = function() {
    return view;
}

},{"../model/stage-errors-message":36,"../view/info-action-popover":45}],24:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var model = require('../model/stage-description');
var view = require('../view/stage-logs');

exports.getName = function() {
    return 'stage-logs';
}

exports.getModel = function() {
    return model;
}

exports.getView = function() {
    return view;
}

},{"../model/stage-description":35,"../view/stage-logs":51}],25:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var jQD = require('jenkins-js-modules').require('jquery-detached:jquery2');

/**
 * jQuery wrapper module.  Other modules should access jQuery via this module (i.e. never access the 'jquery'
 * module directly).  That allows us to inject the jQuery function (aka dollar) to be used by modules during
 * testing etc.
 */

/**
 * Get the jQuery function (aka dollar).
 * @returns The jQuery function (aka dollar).
 */
exports.getJQuery = jQD.getJQuery;

/**
 * Get all widgets in the specified element, or full document if inElement is not specified.
 * @param inElement The element to search for.  If undefined, the whole document is searched.
 */
exports.getWidgets = function (inElement) {
    if (inElement) {
        return exports.getJQuery()("[cbwf-controller]", inElement);
    } else {
        return exports.getJQuery()("[cbwf-controller]");
    }
}

/**
 * jQuery .each() wrapper.
 * @param elementList The element list to iterate.
 * @param func The function to apply to the elements.
 */
exports.forEachElement = function (elementList, func) {
    elementList.each(function() {
        func(exports.getJQuery()(this));
    });
}

function roundUp(number) {
    // only round up if there's a decimal part, otherwise return the number
    if (Math.floor(number) === number) {
        return number;
    }
    return Math.floor(number + 1);
}
function roundDown(number) {
    return Math.floor(number);
}

/**
 * Is the specified x and y coordinate "over" the supplied box coordinates.
 * <p/>
 * Useful for testing if the mouse pointer is inside a region on the page.
 * Use getElementsEnclosingBoxCoords to get the enclosing box.
 *
 * @param x The X coordinate.
 * @param y The Y coordinate.
 * @param boxCoords The box coordinates to check against.
 */
exports.isCoordInBox = function (x, y, boxCoords) {
    if (x < boxCoords.topLeft.x) {
        // to the left
        return false;
    }
    if (y < boxCoords.topLeft.y) {
        // above
        return false;
    }

    if (x > boxCoords.topRight.x) {
        // to the right
        return false;
    }
    if (y > boxCoords.bottomLeft.y) {
        // below
        return false;
    }

    return true;
}

/**
 * Get the box coordinates for an element.
 * <p/>
 * X and Y coordinates for each of the four corners, rounded up and down so as to get
 * the "outside" of the box.
 * @param element The element.
 * @returns The element's box coordinates.
 */
exports.getElementBoxCoords = function (element) {
    var elementOffset = element.offset();

    var topLeft = {
        x: roundDown(elementOffset.left),
        y: roundDown(elementOffset.top)
    };
    var bottomLeft = {
        x: topLeft.x,
        y: roundUp(elementOffset.top + element.height())
    };
    var topRight = {
        x: roundUp(elementOffset.left + element.width()),
        y: topLeft.y
    };
    var bottomRight = {
        x: topRight.x,
        y: bottomLeft.y
    };

    return {
        topLeft: topLeft,
        bottomLeft: bottomLeft,
        topRight: topRight,
        bottomRight: bottomRight
    };
}

/**
 * Get the coordinates of a box that encloses all the supplied elements
 * @param elements The elements.
 */
exports.getElementsEnclosingBoxCoords = function(elements) {
    var boxCoords = exports.getElementBoxCoords(elements[0]);

    for (var i = 1; i < elements.length; i++) {
        var nextElCoords = exports.getElementBoxCoords(elements[i]);

        boxCoords.topLeft.x = Math.min(boxCoords.topLeft.x, nextElCoords.topLeft.x);
        boxCoords.topLeft.y = Math.min(boxCoords.topLeft.y, nextElCoords.topLeft.y);
        boxCoords.bottomLeft.x = boxCoords.topLeft.x;
        boxCoords.bottomLeft.y = Math.max(boxCoords.bottomLeft.y, nextElCoords.bottomLeft.y);
        boxCoords.topRight.x = Math.max(boxCoords.topRight.x, nextElCoords.topRight.x);
        boxCoords.topRight.y = boxCoords.topLeft.y;
        boxCoords.bottomRight.x = boxCoords.topRight.x;
        boxCoords.bottomRight.y = boxCoords.bottomLeft.y;
    }

    return boxCoords;
}

/**
 * Stretch the supplied boxCoords in the specified direction.
 * @param boxCoords The box coordinates to stretch.
 * @param direction The direction in which to stretch.  One of 'left', 'right',
 * 'up' or 'down'.
 * @param to The x or y coordinate to which the box is to be stretched.  The interpretation
 * of this depends on the 'direction' parameter.
 */
exports.stretchBoxCoords = function (boxCoords, direction, to) {
    if (direction === 'left') {
        // 'to' is a x coordinate
        boxCoords.topLeft.x = to;
        boxCoords.bottomLeft.x = to;
    } else if (direction === 'right') {
        // 'to' is a x coordinate
        boxCoords.topRight.x = to;
        boxCoords.bottomRight.x = to;
    } else if (direction === 'up') {
        // 'to' is a y coordinate
        boxCoords.topLeft.y = to;
        boxCoords.topRight.y = to;
    } else if (direction === 'down') {
        // 'to' is a y coordinate
        boxCoords.bottomLeft.y = to;
        boxCoords.bottomRight.y = to;
    }
}

/**
 * Extract the element attributes as a JSON object.
 * @param element The element from which to extract an object.
 * @param requiredAttrs The element attributes to be mapped, or a function to be
 * called to get the list of attributes.
 */
exports.toObject = function (element, requiredAttrs) {
    var object = {};
    if (requiredAttrs !== undefined) {
        if (typeof requiredAttrs === 'function') {
            requiredAttrs = requiredAttrs();
        }

        for (var i = 0; i < requiredAttrs.length; i++) {
            if (!exports.attrToObject(element, requiredAttrs[i], object)) {
                console.error("Required attribute '" + requiredAttrs[i] + "' not defined on element.");
            }
        }
    }
    return object;
}

/**
 * Map a single attribute value to the supplied object
 * @param element The element from which to extract the attribute value.
 * @param attrName The name of the attribute whose value is to be mapped.
 * @param object The target object.
 * @return True if a value is mapped to the target object, otherwise false.
 */
exports.attrToObject = function (element, attrName, object) {
    var attrVal = element.attr(attrName);

    if (!attrVal) {
        return false;
    }

    object[attrName] = attrVal;
    return true;
}

/**
 * Provide the ability to find out where the mouse is.
 */
var lastKnownXY = {
    x: undefined,
    y: undefined
};
exports.getMouseTracker = function() {
    if (lastKnownXY.x === undefined) {

        exports.getJQuery()('body').mousemove(function(event) {
            lastKnownXY.x = event.pageX;
            lastKnownXY.y = event.pageY;
        });
    }
    return lastKnownXY;
}

},{"jenkins-js-modules":10}],26:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

exports.getModelData = function (callback) {
    callback({
        alert: 'info',
        dismissible: true,
        caption: 'Not yet implemented',
        footer: 'Please check back later',
        'alert-classes': 'feature-tbd-alert',
        'activate-on': 'click'
    });
}

},{}],27:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var restApi = require('./rest-api');

exports.listen = function (jobUrl, callback) {
    restApi.getJobRuns(jobUrl, function (jobRunsData) {
        callback(jobRunsData);
        setupJobPoll(jobUrl, callback, jobRunsData)
    }, {fullStages: 'true'});
}

exports.schedulePoll = function (callback) {
    // Tests can mock this function for testing.
    setTimeout(callback, 5000);
}

function setupJobPoll(jobUrl, callback, jobRunsData) {
    function findSinceRunParam() {
        // console.log('findSinceRunParam');
        // Find the name of the oldest build that has an in progress type status.
        // If there's none that fit that, use the latest build.
        var i = jobRunsData.length - 1;
        for (; i >= 0; i--) {
            var runStatus = jobRunsData[i].status;
            if (runStatus === 'IN_PROGRESS' || runStatus === 'PAUSED_PENDING_INPUT') {
                return jobRunsData[i].name;
            }
        }
        if (jobRunsData.length > 0) {
            return jobRunsData[0].name;
        } else {
            return undefined;
        }
    }

    function findRunIndex(id) {
        for (var i = 0; i < jobRunsData.length; i++) {
            if (jobRunsData[i].id === id) {
                return i;
            }
        }
        return -1;
    }

    function addRun(run) {
        jobRunsData = [run].concat(jobRunsData);
    }

    function pollJobRuns () {
        restApi.getJobRuns(jobUrl, function (sinceJobRunsData) {
        // console.log('job-progress......');
            try {
                var notifyListeners = false;

                // reverse iterate the returned set and see if there's anything new
                // or potentially changed...
                for (var i = sinceJobRunsData.length - 1; i >= 0; i--) {
                    var aSinceRun = sinceJobRunsData[i];
                    var knownRunIndex = findRunIndex(aSinceRun.id);

                    if (knownRunIndex === -1) {
                        // We don't know this run... it's a new one.  Add it to the start.
                        notifyListeners = true;
                        addRun(aSinceRun);
                    } else {
                        // We know this run... has it changed?
                        var knownRun = jobRunsData[knownRunIndex];
                        if (aSinceRun.status !== knownRun.status) {
                            // status has changed
                            notifyListeners = true;
                        } else if (aSinceRun.status === 'IN_PROGRESS' || aSinceRun.status === 'PAUSED_PENDING_INPUT') {
                            // it's in an in progress state of some sort...
                            notifyListeners = true;
                        }
                        jobRunsData[knownRunIndex] = aSinceRun;
                    }
                }

                // TODO: what about jobs that have been deleted?

                if (notifyListeners) {
                    callback(jobRunsData);
                }
            } finally {
                exports.schedulePoll(pollJobRuns);
            }
        }, {since: findSinceRunParam(), fullStages: 'true'});
    }

    // Kick it ...
    exports.schedulePoll(pollJobRuns);
}

},{"./rest-api":30}],28:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var restApi = require('./rest-api');

exports.getModelData = function (callback) {
    var objectUrl = this.requiredAttr('objectUrl');

    // Lazy loading of an object.  The view code calls the getObject function on the model
    // it receives.  It allows the view to load the real model lazily e.g. only after
    // the user clicks on something.
    var lazyLoadModel = {
        getObject: function (loadCallback) {
            if (lazyLoadModel.cachedObject) {
                loadCallback(lazyLoadModel.cachedObject);
            } else {
                restApi.getObject(objectUrl, function (object) {
                    if (lazyLoadModel.cacheFunc) {
                        lazyLoadModel.cacheFunc(object);
                    }
                    loadCallback(object);
                });
            }
        }
    };

    return callback(lazyLoadModel);
}

},{"./rest-api":30}],29:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/**
 * Empty model used for views that do not require a model.
 */

exports.getModelData = function (callback) {
    callback({});
}

},{}],30:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var ajax = require('../util/ajax');

/**
 * Workflow REST API
 */

var cache = {};

function addCacheEntry (key, ob) {
    if (typeof ob === 'object') {
        if (ob.status && (ob.status === 'SUCCESS' || ob.status === 'ABORTED' || ob.status === 'FAILED' || ob.status === 'UNSTABLE')) {
            cache[key] = ob;
        }
    }
};

// Cache the stage data if it's eligible for caching
function cacheRunStages(run) {
    if (run && run.stages) {
        for (var j=0; j<run.stages.length; j++) {
            var stage = run.stages[j];
            try {
                addCacheEntry(stage._links.self.href, stage);
            } catch (e) {
                console.error("Stage with no link to it");
            }
        }
    }
}

exports.getJobRuns = function(jobUrl, success, params) {
    ajax.execAsyncGET([jobUrl, 'wfapi', 'runs'], function(obj) {
        // Cache the stages for the run
        for (var i=0; i < obj.length; i++) {
            cacheRunStages(obj[i]);
        }
        success(obj);
    }, params);
}

exports.getDescription = function(of, success) {
    var url;
    if (typeof of === 'string') {
        url = of;
    } else if (typeof of === 'object') {
        if (!of._links) {
            url = of._links.self.href;
        }
    } else {
        console.error("Request to get description using a type other than 'string' or 'object'.");
        return;
    }

    // Use existing cache if possible, otherwise make a request
    var cacheEntry = cache[url];
    if (cacheEntry) {
        success(cacheEntry);
    } else {
        ajax.execAsyncGET([url], function(ob) {
            // Cache the details for the object: this can greatly reduce the number of REST calls to the Jenkins backend.
            addCacheEntry(url, ob);
            success(ob);
        });
    }
}

exports.getPendingActions = function(url, success) {
    ajax.execAsyncGET([url], success);
}

exports.getObject = function(url, success) {
    ajax.execAsyncGET([url], success);
}

},{"../util/ajax":39}],31:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var restApi = require('./rest-api');

exports.getModelData = function (callback) {
    var objectUrl = this.requiredAttr('objectUrl');

    restApi.getObject(objectUrl, function (changesets) {

        var sumCommits = {
           commitCount:0,
           commits:[],
           contributorCount:0,
           contributors:[],
           kind:'multiple'
        };

        if (changesets.length > 0) {
            sumCommits.consoleUrl = changesets[0].consoleUrl;
        }

        // Go through each changeset and mine extra info
        for (var i = 0; i < changesets.length; i++) {
            var changeset = changesets[i];
            var commits = changeset.commits;
            var contributorName = {};

            changeset.contributors = [];
            for (var ii = 0; ii < commits.length; ii++) {
                var commit = commits[ii];

                // tag each changeset with the names of
                // the contributors to the commit list
                if (!contributorName[commit.authorJenkinsId]) {
                    contributorName[commit.authorJenkinsId] = 'yes';
                    changeset.contributors.push({
                        authorJenkinsId: commit.authorJenkinsId
                    });
                }

                // Parse out the first line of the commit message
                commit.messageLine1 = commit.message.split(/\r?\n/)[0];

                if (changeset.kind === 'git') {
                    commit.commitIdDisplay = commit.commitId.slice(0, 7);
                } else {
                    commit.commitIdDisplay = commit.commitId;
                }
                commit.kind = changeset.kind;
                sumCommits.commits.push(commit);
            }

            sumCommits.commitCount += changeset.commitCount;
            sumCommits.contributors = sumCommits.contributors.concat(changeset.contributors);
            sumCommits.contributorCount = sumCommits.contributors.length;
            if(changesets.length === 1)
                sumCommits.kind = changeset.kind;

            changeset.contributorCount = changeset.contributors.length;
        }


        return callback(sumCommits);
    });
}

},{"./rest-api":30}],32:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var restApi = require('./rest-api');

exports.getModelData = function (callback) {
    var pendingInputActions = this.requiredAttr('objectUrl');
    restApi.getPendingActions(pendingInputActions, function (pendingActions) {
        callback(pendingActions);
    });
}

},{"./rest-api":30}],33:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var time = require('../util/time');
var jqProxy = require('../jQuery');
var jobHistoryListener = require('./job-history-listener');

/**
 * Code for producing a model containing Workflow Job runs grouped by matching stage names.
 * <p/>
 * Multiple groups will occur where stages have been reordered, renamed or inserted.
 */

var maxRunsPerRunGroup = 5;

exports.getModelData = function (callback) {
    var mvcContext = this;
    var jobUrl = mvcContext.requiredAttr('objectUrl');

    jobHistoryListener.listen(jobUrl, function (jobRunsData) {
        if (!jobRunsData) {
            // TODO: come up with an error passing scheme
            return callback(undefined);
        }

        var jobRunGroups = mashupRunData(jobRunsData);

        // This callback can get called multiple times.  The first time it is called is for the initial data load.
        // Subsequent calls are for when the model changes. We only want to call the getModelData callback once
        // (the initial data load).  After that (for model changes), we fire a 'modelChange' event in the context
        // and anything that's listening can handle it.
        if (callback) {
            callback(jobRunGroups);
            callback = undefined; // blank it so we don't call it again
        } else {
            mvcContext.modelChange(jobRunGroups);
        }
    });
}

exports.setMaxRunsPerRunGroup = function (max) {
    maxRunsPerRunGroup = max;
}

function mashupRunData(jobRunsData) {
    var $ = jqProxy.getJQuery();
    var jobRunGroups = {
        runs: [].concat(jobRunsData)
    };

    // Total up timings etc
    addStageTotals(jobRunGroups);

    // Add end times before applying the template....
    addEndTimes(jobRunGroups);

    // Sort the runs into groups based on the stage names
    // and the number of runs
    createRunGroups(jobRunGroups);

    return jobRunGroups;
}

function getStageData(stageName, runGroup) {
    if (runGroup && runGroup.stageData) {
        for (var i = 0; i < runGroup.stageData.length; i++) {
            var stageData = runGroup.stageData[i];
            if (stageData.name === stageName) {
                return stageData;
            }
        }
    }
    return undefined;
}

function addStageTotals(runGroup) {
    if (runGroup.stageData === undefined) {
        runGroup.stageData = [];
    }

    if (runGroup.runs === undefined || runGroup.runs.length === 0) {
        return;
    }

    runGroup.numRuns = 0;
    runGroup.numStages = 0;
    runGroup.durationMillis = 0;
    runGroup.durationMillisNoPause = 0;
    runGroup.avgDurationMillis = 0;
    runGroup.avgDurationMillisNoPause = 0;

    // Maybe add this to the model?
    var endToEndRuns = 0;

    // Calc top level run and stage timings...
    for (var i = 0; i < runGroup.runs.length; i++) {
        var run = runGroup.runs[i];

        run.durationMillisNoPause = run.durationMillis - run.pauseDurationMillis;

        // For the top level run timings, only use successfully completed runs.
        if (run.status === 'SUCCESS') {
            runGroup.numRuns++;

            // When calculating run time averages, only include runs where every stage has been executed,
            // otherwise the times get skewed. We could get fancier here in time.
            if (run.stages && run.stages.length > 0 && run.stages[0].status !== 'NOT_EXECUTED') {
                endToEndRuns++;
                runGroup.durationMillis += run.durationMillis;
                runGroup.durationMillisNoPause += run.durationMillisNoPause;
                if (endToEndRuns > 0) {
                    runGroup.avgDurationMillis = Math.floor(runGroup.durationMillis / endToEndRuns);
                    runGroup.avgDurationMillisNoPause = Math.floor(runGroup.durationMillisNoPause / endToEndRuns);
                } else {
                    runGroup.avgDurationMillis = 0;
                    runGroup.avgDurationMillisNoPause = 0;
                }
            }
        }

        if (run.stages) {
            for (var ii = 0; ii < run.stages.length; ii++) {
                var stage = run.stages[ii];

                runGroup.numStages++;

                if (stage.status !== 'NOT_EXECUTED') {
                    var stageData = getStageData(stage.name, runGroup);

                    if (!stageData) {
                        stageData = {name: stage.name};
                        runGroup.stageData.push(stageData);
                    }

                    if (stageData.runs === undefined) {
                        stageData.runs = 0;
                        stageData.durationMillis = 0;
                        stageData.durationMillisNoPause = 0;
                        stageData.avgDurationMillis = 0;
                    }

                    stage.durationMillisNoPause = stage.durationMillis - stage.pauseDurationMillis;

                    stageData.runs++;
                    stageData.durationMillis += stage.durationMillis;
                    stageData.durationMillisNoPause += stage.durationMillisNoPause;
                    stageData.avgDurationMillis = Math.floor(stageData.durationMillis / stageData.runs);
                    stageData.avgDurationMillisNoPause = Math.floor(stageData.durationMillisNoPause / stageData.runs);
                    // console.log(stage.durationMillis);
                    // this will give us a number between 1 and 1.5
                    stage.emphasise = (stage.durationMillisNoPause / run.durationMillisNoPause / 2) + 1;
                    stage.emphasise = Math.min(stage.emphasise, 1.5);
                }
            }
        }
    }

    // Run per stage calc's that require data from top level calc's (above)...
    for (var i = 0; i < runGroup.runs.length; i++) {
        var run = runGroup.runs[i];

        if (run.stages && (run.status === 'IN_PROGRESS' || run.status === 'PAUSED_PENDING_INPUT')) {
            addCompletionEstimates(run, runGroup.avgDurationMillisNoPause, runGroup.runs.length);
            for (var ii = 0; ii < run.stages.length; ii++) {
                var stage = run.stages[ii];
                var stageData = getStageData(stage.name, runGroup);

                if (stage.percentCompleteEstimate === undefined) {
                    if (stage.status === 'IN_PROGRESS' || stage.status === 'PAUSED_PENDING_INPUT') {
                        addCompletionEstimates(stage, stageData.avgDurationMillisNoPause, runGroup.runs.length);
                    }
                }
            }
        }
    }
}

function createRunGroups(jobRunsData) {
    jobRunsData.runGroups = [];

    function addRunGroup(runGroupGenerator) {
        addStageTotals(runGroupGenerator.getModelData());
        runGroupGenerator.addPendingStages()
        jobRunsData.runGroups.push(runGroupGenerator.getModelData());
    }

    var curRunGroup = new RunGroupGenerator(maxRunsPerRunGroup);
    for (var i = 0; i < jobRunsData.runs.length; i++) {
        var run = jobRunsData.runs[i];
        if (!curRunGroup.addRun(run)) {
            // current rungroup is full... create a new one...
            addRunGroup(curRunGroup);
            curRunGroup = new RunGroupGenerator();
            curRunGroup.addRun(run);
        }
    }
    addRunGroup(curRunGroup);
    delete jobRunsData.runs;
}

function addEndTimes(jobRunsData) {
    for (var i = 0; i < jobRunsData.runs.length; i++) {
        var run = jobRunsData.runs[i];
        time.setEndTime(run);
        if (run.stages) {
            for (var ii = 0; ii < run.stages.length; ii++) {
		time.setEndTime(run.stages[ii]);
            }
        }
    }
}

/**
 * Add completion estimates to a timedObject.
 * <p/>
 * Basically anything with 'durationMillis', 'avgDurationMillis' etc
 * (i.e. a run, a runGroup or a stage).
 */
function addCompletionEstimates(timedObject, avgDurationMillis, averagedOver) {
    if (averagedOver === 0) {
        // If no runs have completed yet, then we can't make an estimate, so just mark it at 50%.
        timedObject.percentCompleteEstimate = 50;
    } else {
        timedObject.percentCompleteEstimate = (timedObject.durationMillisNoPause / avgDurationMillis * 100);
        // Don't show an estimate of more than 98%, otherwise it will look as though
        // things should be completed when they might not be.
        timedObject.percentCompleteEstimate = Math.min(98, timedObject.percentCompleteEstimate);
        timedObject.percentCompleteEstimate = Math.max(1, timedObject.percentCompleteEstimate);
        timedObject.percentCompleteEstimate = Math.floor(timedObject.percentCompleteEstimate);

        var timeRemainingEstimate = Math.max(0, (avgDurationMillis - timedObject.durationMillisNoPause));
        if (timeRemainingEstimate > 0) {
            timedObject.timeRemainingEstimate = timeRemainingEstimate;
        }
    }
}

function RunGroupGenerator(maxRuns) {
    this.stageData = [];
    this.runs = [];
}
RunGroupGenerator.prototype.addRun = function(run) {
    // Make sure the stage name ordering matches
    for (var i = 0; i < run.stages.length; i++) {
        var stage = run.stages[i];
        if (this.stageData.length > i) {
            if (stage.name !== this.stageData[i].name) {
                // Stages have been changed - reordered or
                // new ones inserted.  Can't add to this group.
                return false;
            }
        } else {
            // Add the stage name to the list for the group
            this.stageData.push({name: stage.name});
        }
    }

    this.runs.push(run);
    return true;
}
RunGroupGenerator.prototype.addPendingStages = function() {
    for (var i = 0; i < this.runs.length; i++) {
        var run  = this.runs[i];
        // fill out the stages in the run if the stages are not all started yet
        if (run.stages.length < this.stageData.length) {
            for (var ii = run.stages.length; ii < this.stageData.length; ii++) {
                run.stages.push({
                    name: this.stageData[ii].name,
                    status: "NOT_EXECUTED",
                    startTimeMillis: 0,
                    endTimeMillis: 0,
                    durationMillis: 0,
                    emphasise: 1
                });
            }
        }
    }
}
RunGroupGenerator.prototype.getModelData = function() {
    if (this.modelData) {
        return this.modelData;
    }
    this.modelData = {};
    this.modelData.stageData = this.stageData;
    this.modelData.runs = this.runs;
    return this.modelData;
}

},{"../jQuery":25,"../util/time":41,"./job-history-listener":27}],34:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

exports.getModelData = function (callback) {
    var caption = this.targetEl.attr('caption');

    var alertType = "success";
    if (!caption) {
        caption = "Success";
    } else {
        if (caption == 'Unstable Build') {
            alertType = 'warning';
        }
    }

    var descUrl = this.requiredAttr('descUrl');
    callback({
	alert: alertType,
	caption: caption,
	errors:[],
        options: [
            {
                label: 'Logs',
                icon: 'stats',
                controller: 'stage-logs'
            }
        ],
        descUrl: descUrl,
        'click-widget-placement': 'right-inside',
        'menu-items-placement': 'left'
    });
}

},{}],35:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var restApi = require('./rest-api');

exports.getModelData = function (callback) {
    var stageDescribeUrl = this.requiredAttr('descUrl');
    restApi.getDescription(stageDescribeUrl, function (stageDetails) {
            callback(stageDetails);
    });
}

},{"./rest-api":30}],36:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var restApi = require('./rest-api');

exports.getModelData = function (callback) {
    var stageDescUrl = this.requiredAttr('objectUrl');
    restApi.getDescription(stageDescUrl, function (stageDesc) {
        var aggregatedErrorMessage = {
            alert: 'danger',
            caption: 'Failed with the following error(s)',
            errors: [],
            footer: 'See stage logs for more detail.',
            options:[
				{
				    label: 'Logs',
				    icon: 'stats',
				    controller: 'stage-logs'
				}
            ],
            descUrl: stageDescUrl
        };

        var stageFlowNodes = stageDesc.stageFlowNodes;
        for (var i = 0; i < stageFlowNodes.length; i++) {
            var stageFlowNode = stageFlowNodes[i];
            if (stageFlowNode.status === 'FAILED') {
                aggregatedErrorMessage.errors.push({
                    label: stageFlowNode.name,
                    message: stageFlowNode.error.message
                });
            }
        }

        callback(aggregatedErrorMessage);
    });
}

},{"./rest-api":30}],37:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var jqProxy = require('../jQuery');

/**
 * Lightweight MVC.
 */

var controllers = {};

/**
 * Register a controller.
 * @param controller The controller to be registered.
 */
exports.register = function (controller) {
    var controllerName = controller.getName();

    if (exports.isRegistered(controllerName)) {
        throw "A controller named '" + controllerName + "' is already registered.";
    }
    controllers[controllerName] = controller;
}

/**
 * Is the named controller already registered.
 * @param controllerName The name of the controller.
 * @return true if the controller is registered, otherwise false.
 */
exports.isRegistered = function (controllerName) {
    return controllers.hasOwnProperty(controllerName);
}

/**
 * Apply controllers.
 */
exports.applyControllers = function (onElement, allowReapply) {
    var targetEls = jqProxy.getWidgets(onElement);

    jqProxy.forEachElement(targetEls, function(targetEl) {
        if (allowReapply || !targetEl.hasClass('cbwf-controller-applied')) {
            var controllerName = targetEl.attr('cbwf-controller');

            if (controllerName) {
                var controller = controllers[controllerName];

                if (controller) {
                    var model = controller.getModel();
                    var view = controller.getView();
                    var mvcContext = new MVCContext(controllerName, targetEl);

                    targetEl.addClass('cbwf-widget');
                    targetEl.addClass('cbwf-controller-applied');
                    targetEl.addClass(controllerName);

                    model.getModelData.call(mvcContext, function (modelData) {
                        view.render.call(mvcContext, modelData, targetEl);
                    });
                } else {
                    console.error("No controller named '" + controllerName + "'.");
                }
            } else {
                console.error("'widget-element' must define 'controller'.");
            }
        }
    });
}

exports.newContext = function (controllerName, targetEl) {
    return new MVCContext(controllerName, targetEl);
}

function MVCContext(controllerName, targetEl) {
    if (!controllerName) {
        throw "No 'controllerName' supplied to MVCContext.";
    }
    if (!targetEl || targetEl.size() === 0) {
        throw "No 'targetEl' name supplied to MVCContext.";
    }

    this.targetEl = targetEl;
    this.controllerName = controllerName;
}

MVCContext.prototype.getControllerName = function() {
    return this.controllerName;
}

MVCContext.prototype.getTargetElement = function() {
    return this.targetEl;
}

MVCContext.prototype.attr = function(attributeName, defaultVal) {
    var attrVal = this.targetEl.attr(attributeName);
    if (attrVal) {
        return attrVal;
    }  else {
        return defaultVal;
    }
}

MVCContext.prototype.requiredAttr = function(attributeName) {
    var attrVal = this.targetEl.attr(attributeName);
    if (!attrVal) {
        throw "Required attribute '" +  attributeName + "' not defined on MVC controller '" + this.controllerName + "' element.";
    }
    return attrVal;
}

MVCContext.prototype.onModelChange = function(callback) {
    this.targetEl.on("cbwfModelChange", callback);
}

MVCContext.prototype.modelChange = function(modelData) {
    this.targetEl.trigger({type: "cbwfModelChange", modelData: modelData});
}

},{"../jQuery":25}],38:[function(require,module,exports){
require('jenkins-js-modules').whoami('pipeline-stage-view:stageview');

require('jenkins-js-modules')
    .import('jquery-detached:jquery2', 'handlebars:handlebars3', 'momentjs:momentjs2')
    .onFulfilled(function() {

/*
 * Copyright (C) 2013 CloudBees Inc.
 *
 * All rights reserved.
 */

var mvc = require('./mvc');

// Register all controllers...
mvc.register(require('./controller/pipeline-staged'));
mvc.register(require('./controller/feature-tbd'));
mvc.register(require('./controller/stage-failed-popover'));
mvc.register(require('./controller/stage-actions-popover'));
mvc.register(require('./controller/stage-logs'));
mvc.register(require('./controller/node-log'));
mvc.register(require('./controller/run-input-required'));
mvc.register(require('./controller/build-artifacts-popup'));
mvc.register(require('./controller/run-changesets'));
mvc.register(require('./controller/remove-sidepanel'));

// Apply controllers to the whole document.
var jqProxy = require('./jQuery');
var $ = jqProxy.getJQuery();
$(function() {
    if (isTestEnv()) {
        // In a test env, we do not want async rendering delays... just makes the
        // tests more complicated. Doing this for now and we'll see
        // if it's a good or bad idea :)
        require('./util/timeout').setMaxDelay(0);
    }
    mvc.applyControllers();
});

function isTestEnv() {
    if (window === undefined) {
        return true;
    } else if (window.navigator === undefined) {
        return true;
    } else if (window.navigator.userAgent === undefined) {
        return true;
    } else if (window.navigator.userAgent.toLowerCase().indexOf("phantomjs") !== -1) {
        return true;
    }
}

var stageView = require('./view/pipeline-staged.js');
var extpAPI = require('jenkins-js-extp/API');

// Expose an API that will allow other plugins to contribute to the
// stage view. We make this exposed API "look" like that of 'jenkins-js-extp/API'
// so that we can mock an ExtensionPointContainer API on other plugins during dev,
// and then wire in the external/real Stage View ExtensionPointContainer instance
// at runtime.
module.exports = extpAPI;
// and overwrite the extensionPointContainer with the Stage View specific
// instance. This may eventually be a page level object, containing all
// extension points on the page.
module.exports.extensionPointContainer = stageView.extensionPointContainer;

		require('jenkins-js-modules').export('pipeline-stage-view', 'stageview', module);
    });


},{"./controller/build-artifacts-popup":15,"./controller/feature-tbd":16,"./controller/node-log":17,"./controller/pipeline-staged":18,"./controller/remove-sidepanel":19,"./controller/run-changesets":20,"./controller/run-input-required":21,"./controller/stage-actions-popover":22,"./controller/stage-failed-popover":23,"./controller/stage-logs":24,"./jQuery":25,"./mvc":37,"./util/timeout":42,"./view/pipeline-staged.js":47,"jenkins-js-extp/API":6,"jenkins-js-modules":10}],39:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var url = require('./url');
var jqProxy = require('../jQuery');

exports.execAsyncGET = function (resPathTokens, success, params) {
    var $ = jqProxy.getJQuery();

    $.ajax({
        url: url.concatPathTokens(resPathTokens),
        type: 'get',
        dataType: 'json',
        data: params,
        cache: false, // Force caching off for IE (and anything else)
        success: success
    });
};

exports.jenkinsAjaxGET = function (path, success) {
    new Ajax.Request(path, {
        method : 'get',
        cache: false, // Force caching off for IE (and anything else)
        onSuccess: success
    });
};

exports.jenkinsAjaxPOST = function () {
    var path = arguments[0];
    if (arguments.length === 3) {
        var data = arguments[1];
        var success = arguments[2];
        if (typeof data !== 'string') {
            data = Object.toJSON(data);
        }
        new Ajax.Request(path, {
            contentType: "application/json",
            encoding: "UTF-8",
            postBody: data,
            onSuccess: success
        });
    } else {
        var success = arguments[1];
        new Ajax.Request(path, {
            contentType: "application/json",
            method : 'post',
            onSuccess: success
        });
    }
};


exports.jenkinsAjaxPOSTURLEncoded = function (path, parameters, success) {
    new Ajax.Request(path, {
        method : 'post',
        contentType: "application/x-www-form-urlencoded",
        parameters: parameters,
        onSuccess: success
    });
}

},{"../jQuery":25,"./url":43}],40:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var KB = 1024;
var MB = KB * 1024;
var GB = MB * 1024;
var TB = GB * 1024;

var sec = 1000;
var min = sec * 60;
var hr  = min * 60;
var day = hr * 24;
var yr = day * 365;

exports.memory = function (amount) {
    if (amount > TB) {
        return (amount / TB).toFixed(2) + "TB";
    } else if (amount > GB) {
        return (amount / GB).toFixed(2) + "GB";
    } else if (amount > MB) {
        return (amount / MB).toFixed(2) + "MB";
    } else if (amount > KB) {
        return (amount / KB).toFixed(2) + "KB";
    } else {
        return amount + "B";
    }
}

exports.time = function (millis, numUnitsToShow) {
    if (millis <= 0 || isNaN(millis)) {
        return '0ms';
    }

    if (numUnitsToShow === undefined) {
        numUnitsToShow = 3;
    }

    function mod(timeUnit) {
        var numUnits = Math.floor(millis / timeUnit);
        millis = millis % timeUnit;
        return numUnits;
    }

    var years = mod(yr);
    var days = mod(day);
    var hours = mod(hr);
    var minutes = mod(min);
    var seconds = mod(sec);

    var numUnitsAdded = 0;
    var formattedTime = '';

    function addTimeUnit(value, unit) {
        if (numUnitsAdded === numUnitsToShow) {
            // don't add any more
            return;
        }
        if (value === 0 && numUnitsAdded === 0) {
            // Don't add a leading zero
            return;
        }

        // add this one.
        if (formattedTime === '') {
            formattedTime += value + unit;
        } else {
            formattedTime += ' ' + value + unit;
        }

        numUnitsAdded++;
    }

    addTimeUnit(years, 'y');
    addTimeUnit(days, 'd');
    addTimeUnit(hours, 'h');
    addTimeUnit(minutes, 'min');
    addTimeUnit(seconds, 's');
    // Only show millis if the time is below 1 second
    if (seconds === 0) {
        addTimeUnit(millis, 'ms');
    }

    return formattedTime;
}

},{}],41:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

exports.setEndTime = function (obj) {
	//console.log('setEndTime: '+ obj.durationMillis);
    if (obj.startTimeMillis && obj.durationMillis) {
        obj.endTimeMillis = obj.startTimeMillis + obj.durationMillis;
    }
}

},{}],42:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/**
 * Utility to manage timeouts, making it a bit easier to clear active timeouts. Useful for testing.
 */

var activeTimeoutHandlers = {};
var timeoutCounter = 0;
var maxDelay = Number.MAX_VALUE;

exports.newTimeoutHandler = function (handlerName) {
    if (!handlerName) {
        throw "The TimeoutHandler must be given a name.";
    }

    // Clear any timeouts on a handler by this name, if one exists.
    if (activeTimeoutHandlers.hasOwnProperty(handlerName)) {
        activeTimeoutHandlers[handlerName].clearAllTimeouts();
    }

    var timeoutHandler = new TimeoutHandler();
    activeTimeoutHandlers[handlerName] = timeoutHandler;
    return  timeoutHandler;
}

exports.clearAllTimeouts = function () {
    for (var handlerName in activeTimeoutHandlers) {
        if (activeTimeoutHandlers.hasOwnProperty(handlerName)) {
            activeTimeoutHandlers[handlerName].clearAllTimeouts();
        }
    }
}

exports.getMaxDelay = function () {
    return maxDelay;
}

exports.setMaxDelay = function (newMaxDelay) {
    maxDelay = newMaxDelay;
}

function TimeoutHandler() {
    this.timeouts = {};
    this.timeoutCount = 0;
}

TimeoutHandler.prototype.setTimeout = function(code, delay) {
    if (maxDelay === 0) {
        // There is no delay allowed (e.g. in tests). Run synchronously now.
        code();
        return undefined;
    } else {
        var thisHandler = this;

        function doTimeout(timeoutId) {
            var timeoutObj = setTimeout(function() {
                thisHandler.removeTimeout(timeoutId);
                code();
            }, Math.min(delay, maxDelay));
            thisHandler.timeouts[timeoutId] = timeoutObj;
        }

        var timeoutId = (timeoutCounter++).toString(10);
        doTimeout(timeoutId);
        this.timeoutCount++;

        return timeoutId;
    }
}

TimeoutHandler.prototype.activeTimeoutCount = function() {
    return this.timeoutCount;
}

TimeoutHandler.prototype.getTimeouts = function() {
    return this.timeouts;
}

TimeoutHandler.prototype.removeTimeout = function(timeoutId) {
    delete this.timeouts[timeoutId];
    this.timeoutCount--;
}

TimeoutHandler.prototype.clearTimeout = function(timeoutId) {
    var timeoutObj = this.timeouts[timeoutId];
    clearTimeout(timeoutObj);
    this.removeTimeout(timeoutId);
}

TimeoutHandler.prototype.clearAllTimeouts = function() {
    for (var timeoutId in this.timeouts) {
        this.clearTimeout(timeoutId);
    }
    this.timeouts = {};
    this.timeoutCount = 0;
}

},{}],43:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

exports.concatPathTokens = function (tokens) {
    if (typeof tokens === 'string') {
        return tokens;
    } else {
        var concatedString = '';
        for (var index = 0; index < tokens.length; index++) {
            if (index === 0) {
                concatedString += exports.trimTrailingSlashes(tokens[index]);
            } else if (index === tokens.length - 1) {
                concatedString += '/' + exports.trimLeadingSlashes(tokens[index]);
            } else {
                concatedString += '/' + exports.trimSlashes(tokens[index]);
            }
        }
        return concatedString;
    }
}

exports.trimLeadingSlashes = function (string) {
    return string.replace(/^\/+/g, '');
}
exports.trimTrailingSlashes = function (string) {
    return string.replace(/\/+$/g, '');
}
exports.trimSlashes = function (string) {
    return exports.trimLeadingSlashes(exports.trimTrailingSlashes(string));
}

},{}],44:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var jqProxy = require('../jQuery');
var templates = require('./templates');
var popoverWidget = require('./widgets/popover');

exports.render = function (lazyLoadConfig, onElement) {
    var $ = jqProxy.getJQuery();
    var popupEl = $('<div class="cbwf-build-artifacts"></div>');

    var popover = popoverWidget.newPopover("build-artifacts", popupEl, onElement, {
        placement: 'right',
        hoverBoth: true,
        namespace: 'build-artifacts',
        onshow: function() {
            lazyLoadConfig.getObject(function (lazyLoad) {
                var lazyLoadToDisplay = templates.apply('build-artifacts', lazyLoad);
                popupEl.empty().append(lazyLoadToDisplay);

                // need to reapply placement of the popover because:
                // 1. we have altered it's content, which possibly widened it
                // 2. it is positioned to the left and so, if widened, means it's positioning is off
                popover.applyPlacement();
            });
        }
    });

    popover.hover();
}

},{"../jQuery":25,"./templates":53,"./widgets/popover":66}],45:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var templates = require('./templates');
var popoverWidget = require('./widgets/popover');

exports.render = function (message, onElement) {
    var popoverDom = templates.apply('info-action-popover', message);
    var activateOn = message['activate-on'];
    var popoverOn = onElement.parent();
    var popoverName = this.getControllerName();
    var notIf = this.attr('notIf');
    var hide = this.attr('hide');

    if (activateOn === undefined || activateOn === 'hover') {
        var popover = popoverWidget.newPopover(popoverName, popoverDom, popoverOn, {
            notIf: notIf,
            hide: hide,
		    hoverBoth: true
        });
        popover.hover();
    } else if (activateOn === 'click') {
        var popover = popoverWidget.newPopover(popoverName, popoverDom, popoverOn);
        popover.click();
    }

    // for testing...
    return popoverDom;
}

},{"./templates":53,"./widgets/popover":66}],46:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var jqProxy = require('../jQuery');
var templates = require('./templates');

exports.render = function (logLazyLoadModel, onElement) {
    // Add a caching function to the lazy load model.  This allows us to avoid
    // reloading of log details for completed nodes.
    logLazyLoadModel.cacheFunc = function(logDetails) {
        if (logDetails.nodeStatus === 'SUCCESS') {
            logLazyLoadModel.cachedObject = logDetails;
        }
    }

    onElement.click(function() {
        logLazyLoadModel.getObject(function (lazyLoad) {
            var nodeLogDom = templates.apply('node-log', lazyLoad);
            var $ = jqProxy.getJQuery();
            var logDetailsEl = $('.log-details', onElement);

            logDetailsEl.empty();
            logDetailsEl.append(nodeLogDom);
        });
    });
}

},{"../jQuery":25,"./templates":53}],47:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var jqProxy = require('../jQuery');
var charts = require('./widgets/charts');
var dialogWidget = require('./widgets/dialog');
var templates = require('./templates');
var extpAPI = require('jenkins-js-extp/API');
var ExtensionPointContainer = extpAPI.ExtensionPointContainer;
var ExtensionPoint = extpAPI.ExtensionPoint;

var extensionPointContainer = new ExtensionPointContainer();

exports.extensionPointContainer = extensionPointContainer;

exports.render = function (jobRunsData, onElement) {
    var mvcContext = this;
    var fragCaption = mvcContext.attr('fragCaption');

    _render(jobRunsData, onElement, fragCaption);

    // listen for changes to the model...
    this.onModelChange(function(event) {
        _render(event.modelData, onElement, fragCaption);
    });
};

function _render(jobRunsData, onElement, fragCaption) {

    // If looking for an example of the model consumed by this view, see the "*_expected_*.json"
    // files in ui/src/test/resources/model/runs_stage_grouped/getModelData.
    // Of course, you can also just use console.log(jobRunsData).
    // The expected model is that produced by ui/src/main/js/model/runs-stage-grouped.js

    if (jobRunsData.runGroups && jobRunsData.runGroups.length > 0) {
        var $ = jqProxy.getJQuery();
        var runGroup = jobRunsData.runGroups[0];

        runGroup.fragCaption = fragCaption;
        runGroup.maxTableEms = runGroup.stageData.length * 10;

        var pipelineStagedDom = templates.apply('pipeline-staged', runGroup);
        addLaneCharts(pipelineStagedDom, runGroup);
        var viewPort = $('div.table-viewPort');
        if (viewPort && viewPort.size() > 0) { // First rendering does not have an existing element
            var leftScroll = viewPort[0].scrollLeft;  // This way we can jump back to the previous scroll position
            onElement.empty().append(pipelineStagedDom); // With many stages, the DOM change scrolls us back to start if we don't reset scroll.
            viewPort = $('div.table-viewPort')[0]; // Re-fetched because the DOM has changed in above.
            if (leftScroll < viewPort.scrollWidth) {
                viewPort.scrollLeft = leftScroll;
            }
        } else {
            onElement.empty().append(pipelineStagedDom);
        }

        addExtensionPoints(onElement, runGroup);
    }
}

function addLaneCharts(jQueryDom, runGroup) {
    var $ = jqProxy.getJQuery();
    var stageData = runGroup.stageData;
    var avgStageTimes = [];

    for (var i = 0; i < stageData.length; i++) {
        avgStageTimes.push(stageData[i].avgDurationMillisNoPause);
    }

    var stackedBarChartAnchorEls = $('.totals .stackedBarChart', jQueryDom);

    // Should be the same number of these as there are array elements in stageData
    if (stackedBarChartAnchorEls.size() !== avgStageTimes.length) {
        console.log('Unexpected problem.  Template failed to generate a lane header for all stages. ' + stackedBarChartAnchorEls.size() + ' != ' + avgStageTimes.length);
        return;
    }

    for (var i = 0; i < stackedBarChartAnchorEls.size(); i++) {
        var stackedBarChartAnchorEl = $(stackedBarChartAnchorEls.get(i));
        var stackedBarChart = charts.stackedBarChart(avgStageTimes, i);

        stackedBarChartAnchorEl.append(stackedBarChart);
    }
}

function addExtensionPoints(onElement, runGroupData) {
    var $ = jqProxy.getJQuery();

    // We need to clear and recreate every time here because of how the stage
    // is in a continuous re-render mode (polling). Having push notifications
    // will clean up a lot of this.

    // Clear the last set of ExtensionPoint instances.
    extensionPointContainer.remove();

    function addExtensionPoint(extensionPoint) {
        extensionPointContainer.add(extensionPoint);
        extensionPoint.oncontribute(function() {
            var theContribution = this;

            if (theContribution.activatorContentType !== 'className') {
                throw 'Jenkins Pipeline Stage View only support CSS className activation widget types.';
            }

            // Add the activator to the dock.
            var activatorDock = $(extensionPoint.dock);
            var activatorWidget = $('<span class="extension">');
            activatorWidget.addClass(theContribution.activatorContent);
            activatorWidget.attr('title', theContribution.caption);
            activatorDock.append(activatorWidget);
            activatorWidget.click(function() {
                // Display the extension content in a dialog.
                var content = $('<div>');
                content.append(theContribution.content);
                var dialog = dialogWidget.show(theContribution.caption, content);
                theContribution.onHide(function() {
                    dialog.hide();
                });
                theContribution.show(content.get());
            });
        });
    }

    // Find all runs.
    var runs = $('.job', onElement);
    runs.each(function() {
        var run = $(this);
        var runId = run.attr('data-runId');

        if (!runId) {
            console.warn('No "data-runId" on run.');
            return;
        }

        var runObj = getRun(runId, runGroupData);

        // Run level extension points...
        var stageEndIcons = $('.stage-start .stage-end-icons', run);
        var extensionPoint = new ExtensionPoint('jenkins.pipeline.run', runId, stageEndIcons.get());
        addExtensionPoint(extensionPoint);

        // Tack on the REST API links associated with the run.
        // Putting them in private for now so as discourage external use.
        extensionPoint._private._links = runObj._links;

        // Add ExtensionPoint instances for each stage in the run.
        var stages = $('.stage-cell', run);
        stages.each(function() {
            var stage = $(this);
            var stageId = stage.attr('data-stageId');

            if (!stageId) {
                console.warn('No "data-stageId" on stage.');
                return;
            }

            var stageObj = getStage(runId, stageId, runGroupData);
            var extensionPointID = runId + ':' + stageId;
            var extensionDock = $('.extension-dock', stage);
            var extensionPoint = new ExtensionPoint('jenkins.pipeline.run.stage', extensionPointID, extensionDock.get());

            // When other plugins contribute to the stage view, we add a little activation
            // widget to the stage in the ExtensionPoint dock area.
            addExtensionPoint(extensionPoint);

            // Tack on the REST API links associated with the stage.
            // Putting them in private for now so as discourage external use.
            extensionPoint._private._links = stageObj._links;
        });
    });

    // We've changed the set of ExtensionPoints, so notify anyone
    // that's interested.
    extensionPointContainer.notifyOnChange();
}

function getRun(runId, runGroupData) {
    for (var i = 0; i < runGroupData.runs.length; i++) {
        var run = runGroupData.runs[i];
        if (run.id === runId) {
            return run;
        }
    }
    return undefined;
}

function getStage(runId, stageId, runGroupData) {
    var run = getRun(runId, runGroupData);
    if (run) {
        for (var i = 0; i < run.stages.length; i++) {
            var stage = run.stages[i];
            if (stage.id === stageId) {
                return stage;
            }
        }
    }
    return undefined;
}

},{"../jQuery":25,"./templates":53,"./widgets/charts":63,"./widgets/dialog":64,"jenkins-js-extp/API":6}],48:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var jqProxy = require('../jQuery');

exports.render = function (message, onElement) {
    var $ = jqProxy.getJQuery();
    $('div#side-panel').remove();
    $('div#main-panel').addClass("fullscreen");
}

},{"../jQuery":25}],49:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var jqProxy = require('../jQuery');
var templates = require('./templates');
var popoverWidget = require('./widgets/popover');

exports.render = function (changeSets, onElement) {
    var content = templates.apply('run-changesets', changeSets);
    var $ = jqProxy.getJQuery();
    onElement.empty().append(content);

    $('.run-changeset', content).each(function() {
        var changesetWidget = $(this);
        var changeSetData = changeSets;
        var changesetPopout = templates.apply('run-changeset', changeSetData);
        var popover = popoverWidget.newPopover('run-changeset', changesetPopout, changesetWidget, {
            placement: 'right',
            hoverBoth: true
        });
        popover.hover();
    });
}

},{"../jQuery":25,"./templates":53,"./widgets/popover":66}],50:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
var jqProxy = require('../jQuery');
var templates = require('./templates');
var popoverWidget = require('./widgets/popover');
var ajax = require('../util/ajax');

var supportedInputTypes = {
    'BooleanParameterDefinition': true,
    'StringParameterDefinition': true,
    'TextParameterDefinition': true,
    'PasswordParameterDefinition': true,
    'ChoiceParameterDefinition': true
};

function allInputTypesSupported(inputs) {
    if (!inputs) {
        // no inputs.
        return true;
    }
    for (var i = 0; i < inputs.length; i++) {
        if (!supportedInputTypes[inputs[i].type]) {
            return false;
        }
    }
    return true;
}

exports.render = function (inputRequiredModel, onElement) {
    if (allInputTypesSupported(inputRequiredModel.inputs)) {
        var popoverDom = templates.apply('run-input-required', inputRequiredModel);

        var popover = popoverWidget.newPopover('run-input-required', popoverDom, onElement.parent(), {
            hoverBoth: true,
            namespace: 'runInputRequired',
            placement: 'left',
            onshow: function() {
                var $ = jqProxy.getJQuery();
                var pendingActionsTable = $('.pending-input-actions', popoverDom);

                var abortUrl = inputRequiredModel.abortUrl;
                var proceedUrl = inputRequiredModel.proceedUrl;
                var proceedBtn = $('.proceed-button', popoverDom);
                var abortBtn = $('.abort-button', popoverDom);

                function disableButtons() {
                    proceedBtn.attr("disabled", true);
                    abortBtn.attr("disabled", true);
                }

                proceedBtn.click(function() {
                    disableButtons();

                    // Get all the inputs and POST them back tot he Run API.
                    var inputs = $('.inputs :input');
                    var inputNVPs = [];
                    inputs.each(function() {
                        var input = $(this);
                        var inputName = input.attr('name');

                        if (input.is(':checkbox')) {
                            // Convert the checkbox "on"/"off" value to a boolean.
                            inputNVPs.push({
                                name: inputName,
                                value: input.is(':checked')
                            });
                        } else {
                            inputNVPs.push({
                                name: inputName,
                                value: input.val()
                            });
                        }
                    });

                    // Perform the POST. Needs to be encoded into a "json" parameter with an
                    // array object named "parameter" :)
                    var parameters = {
                        json: Object.toJSON({
                            parameter: inputNVPs
                        })
                    };
                    ajax.jenkinsAjaxPOSTURLEncoded(proceedUrl, parameters, function() {
                        popover.hide();
                    });
                });
                abortBtn.click(function() {
                    disableButtons();
                    ajax.jenkinsAjaxPOST(abortUrl, function() {
                        popover.hide();
                    });
                });
            }
        });
        popover.hover();

        // for testing...
        return popoverDom;
    } else {
        var popoverDom = templates.apply('run-input-required-redirect', inputRequiredModel);

        var popover = popoverWidget.newPopover('run-input-required', popoverDom, onElement.parent(), {
            hoverBoth: true,
            namespace: 'runInputRequired',
            placement: 'left'});

        popover.hover();

        // for testing...
        return popoverDom;
    }
};

},{"../jQuery":25,"../util/ajax":39,"./templates":53,"./widgets/popover":66}],51:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
var jqProxy = require('../jQuery');
var templates = require('./templates');
var dialog = require('./widgets/dialog');

exports.render = function (stageDescription, onElement) {
    var $ = jqProxy.getJQuery();
    var theWindow = require('window-handle').getWindow();
    var stageLogsDom = templates.apply('stage-logs', stageDescription);
    var winWidth = $(theWindow).width();
    var winHeight = $(theWindow).height();
    var dialogWidth = Math.min(800, (winWidth * 0.7));
    var dialogHeight = Math.min(800, (winHeight * 0.7));
    var nodeLogFrames = $('.node-log-frame', stageLogsDom);
    var nodeNameBars = $('.node-name', stageLogsDom);

    stageLogsDom.addClass('stageLogsPopover');

    var clickNSEvent = 'click.cbwf-stage-logs';

    onElement.off(clickNSEvent);
    onElement.on(clickNSEvent, function() {
        dialog.show('Stage Logs (' + stageDescription.name + ')', stageLogsDom, {
            classes: 'cbwf-stage-logs-dialog',
            placement: 'window-visible-top',
            onElement: onElement,
            width: dialogWidth,
            height: dialogHeight,
            onshow: function() {
                var header = $('.cbwf-stage-logs-dialog .header');

                nodeNameBars.click(function() {
                    var nodeNameBar = $(this);
                    var nodeLogFrame = nodeNameBar.parent();
                    var active = nodeLogFrame.hasClass('active');

                    // add active state to the clicked log frame if was previously inactive
                    if (!active) {
                        // Hide any other log box
                        nodeLogFrames.removeClass('active');
                        // Show this one
                        nodeLogFrame.addClass('active');
                    }
                });

                if (nodeNameBars.size() === 1) {
                    nodeNameBars.click();
                }
            }
        });
    });

    // for testing...
    return stageLogsDom;
}

},{"../jQuery":25,"./templates":53,"./widgets/dialog":64,"window-handle":14}],52:[function(require,module,exports){
// hbsfy compiled Handlebars template
var HandlebarsCompiler = require('jenkins-handlebars-rt/runtimes/handlebars3_rt');
module.exports = HandlebarsCompiler.template({"1":function(depth0,helpers,partials,data) {
    var alias1=this.lambda, alias2=this.escapeExpression;

  return "                <tr class=\"artifact\">\n                    <td class=\"name\"><a href=\""
    + alias2(alias1((depth0 != null ? depth0.url : depth0), depth0))
    + "\" title=\"Download\"><span class=\"glyphicon glyphicon-file\"></span> "
    + alias2(alias1((depth0 != null ? depth0.name : depth0), depth0))
    + "</a></td>\n                    <td class=\"size\">("
    + alias2((helpers.formatMem || (depth0 && depth0.formatMem) || helpers.helperMissing).call(depth0,(depth0 != null ? depth0.size : depth0),{"name":"formatMem","hash":{},"data":data}))
    + ")</td>\n                    <td class=\"download\"><a href=\""
    + alias2(alias1((depth0 != null ? depth0.url : depth0), depth0))
    + "\" title=\"Download\"><span class=\"glyphicon glyphicon-download\"></span></a></td>\n                </tr>\n";
},"compiler":[6,">= 2.0.0-beta.1"],"main":function(depth0,helpers,partials,data) {
    var stack1;

  return "<div class=\"alert alert-info\">\n    <div class=\"header\">\n        Produced the following "
    + this.escapeExpression(this.lambda((depth0 != null ? depth0.length : depth0), depth0))
    + " artifact(s):\n    </div>\n    <div class=\"body\">\n        <table>\n"
    + ((stack1 = helpers.each.call(depth0,depth0,{"name":"each","hash":{},"fn":this.program(1, data, 0),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "        </table>\n    </div>\n</div>";
},"useData":true});

},{"jenkins-handlebars-rt/runtimes/handlebars3_rt":5}],53:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var mvc = require('../../mvc');
var handlebars = require('jenkins-handlebars-rt/runtimes/handlebars3_rt');
var jqProxy = require('../../jQuery');
var formatters = require('../../util/formatters');
var moment = require('jenkins-js-modules').require('momentjs:momentjs2');

/**
 * Templating support.
 */

// Create the template cache..
var templateCache = {
    'info-action-popover': require('./info-action-popover.hbs'),
    'dialog': require('../widgets/dialog/template.hbs'),
    'pipeline-staged': require('./pipeline-staged.hbs'),
    'menu-popout': require('./menu-popout.hbs'),
    'stage-logs': require('./stage-logs.hbs'),
    'node-log': require('./node-log.hbs'),
    'run-input-required': require('./run-input-required.hbs'),
    'run-input-required-redirect': require('./run-input-required-redirect.hbs'),
    'build-artifacts': require('./build-artifacts.hbs'),
    'run-changesets': require('./run-changesets.hbs'),
    'run-changeset': require('./run-changeset.hbs')
};

// Initialise handlebars with helpers

var dateFormattingOn = true;
var formatAliases = {
    short: 'HH:mm (MMM DD)',
    month: 'MMM',
    dom: 'DD',
    time: 'HH:mm',
    ISO_8601: 'YYYY-MM-DDTHH:mm:ss',
    long: this.ISO_8601
};

function registerHBSHelper(name, helper) {
    handlebars.registerHelper(name, helper);
}

registerHBSHelper('breaklines', function(text) {
    text = handlebars.escapeExpression(text);
    text = text.replace(/(\r\n|\n|\r)/gm, '<br>');
    return new handlebars.SafeString(text);
});

registerHBSHelper('dumpObj', function(object) {
    return JSON.stringify(object, undefined, 4);
});

registerHBSHelper('formatMem', function(amount) {
    return formatters.memory(amount);
});

registerHBSHelper('formatTime', function(millis, numUnitsDisplayed) {
    return formatters.time(millis, numUnitsDisplayed);
});

registerHBSHelper('formatDate', function (date, toFormat) {
    if (!dateFormattingOn) {
        // Just return as is...
        return date;
    }

    var aliasFormat = formatAliases[toFormat];
    if (aliasFormat) {
        return moment(date).format(aliasFormat);
    } else {
        return moment(date).format(toFormat);
    }
});

/**
 * A simple helper that converts an emphasise value (between 1.0 and 1.5) to an opacity value
 * between 0.5 and 1.0.
 */
registerHBSHelper('emphasiseToOpacity', function(emphasiseVal) {
    // first make sure the value is between 1 and 1.5
    emphasiseVal = Math.max(emphasiseVal, 1.0);
    emphasiseVal = Math.min(emphasiseVal, 1.5);

    // convert to opacity by just subtracting 0.5 :)
    return (emphasiseVal - 0.5);
});

registerHBSHelper('ifCond', function (v1, operator, v2, options) {
    switch (operator) {
        case '==':
            return (v1 === v2) ? options.fn(this) : options.inverse(this);
        case '===':
            return (v1 === v2) ? options.fn(this) : options.inverse(this);
        case '!=':
            return (v1 !== v2) ? options.fn(this) : options.inverse(this);
        case '!==':
            return (v1 !== v2) ? options.fn(this) : options.inverse(this);
        case '<':
            return (v1 < v2) ? options.fn(this) : options.inverse(this);
        case '<=':
            return (v1 <= v2) ? options.fn(this) : options.inverse(this);
        case '>':
            return (v1 > v2) ? options.fn(this) : options.inverse(this);
        case '>=':
            return (v1 >= v2) ? options.fn(this) : options.inverse(this);
        case '&&':
            return (v1 && v2) ? options.fn(this) : options.inverse(this);
        case '||':
            return (v1 || v2) ? options.fn(this) : options.inverse(this);
        default:
            return options.inverse(this);
    }
});

function getTemplate(templateName) {
    var templateInstance = templateCache[templateName];
    if (!templateInstance) {
        throw 'No template by the name "' + templateName + '".  Check ui/src/main/js/view/templates/index.js and make sure the template is registered in the templateCache.';
    }
    return  templateInstance;
}

/**
 * Get a template from the template cache.
 * @param templateName The template name.
 * @returns The template instance.
 */
exports.get = function (templateName) {
    return  getTemplate(templateName);
}

/**
 * Apply the named template to the provided data model.
 * @param templateName The name of the template.
 * @param dataModel The data model to which the template is to be applied.
 * @param divWrap Flag indicating whether the templating result is to be wrapped in a div
 * element (default true).  Needed if the html produced by the template contains text nodes
 * at the root level.
 * @returns jQuery DOM.
 */
exports.apply = function (templateName, dataModel, divWrap) {
    var templateInstance = getTemplate(templateName);
    var html = templateInstance(dataModel);
    var jQueryDom;

    if (divWrap === undefined || divWrap) {
        jQueryDom = jqProxy.getJQuery()('<div>' + html + '</div>');
    } else {
        jQueryDom = jqProxy.getJQuery()(html);
    }

    // Apply all controllers before returning...
    mvc.applyControllers(jQueryDom);

    return jQueryDom;
}

/**
 * Turn template date formatting on/off.
 * <p/>
 * Useful for testing.
 * @param on Formatting on off flag.  True to turn formatting on (default),
 * otherwise false.
 */
exports.dateFormatting = function (on) {
    dateFormattingOn = on;
}

},{"../../jQuery":25,"../../mvc":37,"../../util/formatters":40,"../widgets/dialog/template.hbs":65,"./build-artifacts.hbs":52,"./info-action-popover.hbs":54,"./menu-popout.hbs":55,"./node-log.hbs":56,"./pipeline-staged.hbs":57,"./run-changeset.hbs":58,"./run-changesets.hbs":59,"./run-input-required-redirect.hbs":60,"./run-input-required.hbs":61,"./stage-logs.hbs":62,"jenkins-handlebars-rt/runtimes/handlebars3_rt":5,"jenkins-js-modules":10}],54:[function(require,module,exports){
// hbsfy compiled Handlebars template
var HandlebarsCompiler = require('jenkins-handlebars-rt/runtimes/handlebars3_rt');
module.exports = HandlebarsCompiler.template({"1":function(depth0,helpers,partials,data) {
    return "<div class=\"remove\"><span class=\"glyphicon glyphicon-remove\"></span></div>";
},"3":function(depth0,helpers,partials,data) {
    var helper;

  return "<div class=\"caption\">"
    + this.escapeExpression(((helper = (helper = helpers.caption || (depth0 != null ? depth0.caption : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0,{"name":"caption","hash":{},"data":data}) : helper)))
    + "</div>";
},"5":function(depth0,helpers,partials,data) {
    var stack1;

  return "    <table class=\"errors\">\n"
    + ((stack1 = helpers.each.call(depth0,(depth0 != null ? depth0.errors : depth0),{"name":"each","hash":{},"fn":this.program(6, data, 0),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "    </table>\n";
},"6":function(depth0,helpers,partials,data) {
    var stack1;

  return "        <tr>\n            <td>"
    + ((stack1 = helpers['if'].call(depth0,(depth0 != null ? depth0.label : depth0),{"name":"if","hash":{},"fn":this.program(7, data, 0),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "</td>\n            <td>"
    + ((stack1 = helpers['if'].call(depth0,(depth0 != null ? depth0.message : depth0),{"name":"if","hash":{},"fn":this.program(9, data, 0),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "</td>\n        </tr>\n";
},"7":function(depth0,helpers,partials,data) {
    return "<span class=\"label label-danger\">"
    + this.escapeExpression(this.lambda((depth0 != null ? depth0.label : depth0), depth0))
    + "</span>";
},"9":function(depth0,helpers,partials,data) {
    return "<span class=\"message\">"
    + this.escapeExpression(this.lambda((depth0 != null ? depth0.message : depth0), depth0))
    + "</span>";
},"11":function(depth0,helpers,partials,data) {
    var helper;

  return "<div class=\"footer\">"
    + this.escapeExpression(((helper = (helper = helpers.footer || (depth0 != null ? depth0.footer : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0,{"name":"footer","hash":{},"data":data}) : helper)))
    + "</div>";
},"13":function(depth0,helpers,partials,data,blockParams,depths) {
    var alias1=this.lambda, alias2=this.escapeExpression;

  return "        <div class=\"btn btn-small\" cbwf-controller=\""
    + alias2(alias1((depth0 != null ? depth0.controller : depth0), depth0))
    + "\" descUrl=\""
    + alias2(alias1((depths[1] != null ? depths[1].descUrl : depths[1]), depth0))
    + "\"><span class=\"glyphicon glyphicon-"
    + alias2(alias1((depth0 != null ? depth0.icon : depth0), depth0))
    + "\"></span> "
    + alias2(alias1((depth0 != null ? depth0.label : depth0), depth0))
    + "</div>\n";
},"compiler":[6,">= 2.0.0-beta.1"],"main":function(depth0,helpers,partials,data,blockParams,depths) {
    var stack1, helper, alias1=helpers.helperMissing, alias2="function", alias3=this.escapeExpression;

  return "<div class=\"alert alert-"
    + alias3(((helper = (helper = helpers.alert || (depth0 != null ? depth0.alert : depth0)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"alert","hash":{},"data":data}) : helper)))
    + " cbwf-info-action-popover "
    + alias3(((helper = (helper = helpers['alert-classes'] || (depth0 != null ? depth0['alert-classes'] : depth0)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"alert-classes","hash":{},"data":data}) : helper)))
    + "\">\n    "
    + ((stack1 = helpers['if'].call(depth0,(depth0 != null ? depth0.dismissible : depth0),{"name":"if","hash":{},"fn":this.program(1, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "\n    "
    + ((stack1 = helpers['if'].call(depth0,(depth0 != null ? depth0.caption : depth0),{"name":"if","hash":{},"fn":this.program(3, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "\n"
    + ((stack1 = helpers['if'].call(depth0,(depth0 != null ? depth0.errors : depth0),{"name":"if","hash":{},"fn":this.program(5, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "    "
    + ((stack1 = helpers['if'].call(depth0,(depth0 != null ? depth0.footer : depth0),{"name":"if","hash":{},"fn":this.program(11, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "\n    <div class=\"cbwf-menu-item btn-toolbar\" role=\"toolbar\">\n      <div class=\"btn-group\">\n"
    + ((stack1 = helpers.each.call(depth0,(depth0 != null ? depth0.options : depth0),{"name":"each","hash":{},"fn":this.program(13, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "      </div></div>\n\n</div>";
},"useData":true,"useDepths":true});

},{"jenkins-handlebars-rt/runtimes/handlebars3_rt":5}],55:[function(require,module,exports){
// hbsfy compiled Handlebars template
var HandlebarsCompiler = require('jenkins-handlebars-rt/runtimes/handlebars3_rt');
module.exports = HandlebarsCompiler.template({"1":function(depth0,helpers,partials,data,blockParams,depths) {
    var stack1;

  return "    <div class=\"glyphicon glyphicon-align-justify\">\n        <div class=\"cbwf-menu-items\">\n"
    + ((stack1 = helpers.each.call(depth0,(depth0 != null ? depth0.items : depth0),{"name":"each","hash":{},"fn":this.program(2, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "        </div>\n    </div>\n";
},"2":function(depth0,helpers,partials,data,blockParams,depths) {
    var alias1=this.lambda, alias2=this.escapeExpression;

  return "                <div class=\"cbwf-menu-item\">\n                    <div cbwf-controller=\""
    + alias2(alias1((depth0 != null ? depth0.controller : depth0), depth0))
    + "\" descUrl=\""
    + alias2(alias1((depths[1] != null ? depths[1].descUrl : depths[1]), depth0))
    + "\"><span class=\"glyphicon glyphicon-"
    + alias2(alias1((depth0 != null ? depth0.icon : depth0), depth0))
    + "\"></span> "
    + alias2(alias1((depth0 != null ? depth0.label : depth0), depth0))
    + "</div>\n                </div>\n";
},"compiler":[6,">= 2.0.0-beta.1"],"main":function(depth0,helpers,partials,data,blockParams,depths) {
    var stack1;

  return "<div class=\"cbwf-popout-menu\">\n"
    + ((stack1 = helpers['if'].call(depth0,(depth0 != null ? depth0.items : depth0),{"name":"if","hash":{},"fn":this.program(1, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "</div>";
},"useData":true,"useDepths":true});

},{"jenkins-handlebars-rt/runtimes/handlebars3_rt":5}],56:[function(require,module,exports){
// hbsfy compiled Handlebars template
var HandlebarsCompiler = require('jenkins-handlebars-rt/runtimes/handlebars3_rt');
module.exports = HandlebarsCompiler.template({"compiler":[6,">= 2.0.0-beta.1"],"main":function(depth0,helpers,partials,data) {
    var stack1, helper;

  return "<pre class=\"console-output\">"
    + ((stack1 = ((helper = (helper = helpers.text || (depth0 != null ? depth0.text : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0,{"name":"text","hash":{},"data":data}) : helper))) != null ? stack1 : "")
    + "</pre>";
},"useData":true});

},{"jenkins-handlebars-rt/runtimes/handlebars3_rt":5}],57:[function(require,module,exports){
// hbsfy compiled Handlebars template
var HandlebarsCompiler = require('jenkins-handlebars-rt/runtimes/handlebars3_rt');
module.exports = HandlebarsCompiler.template({"1":function(depth0,helpers,partials,data,blockParams,depths) {
    var stack1, helper;

  return "<div class=\"table-box\"><div class=\"table-viewPort\">\n<table class=\"jobsTable\" style=\"max-width:"
    + this.escapeExpression(((helper = (helper = helpers.maxTableEms || (depth0 != null ? depth0.maxTableEms : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0,{"name":"maxTableEms","hash":{},"data":data}) : helper)))
    + "em\">\n  <colgroup>\n    <col class=\"start-group\" />\n"
    + ((stack1 = helpers.each.call(depth0,(depth0 != null ? depth0.stageData : depth0),{"name":"each","hash":{},"fn":this.program(2, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "  </colgroup>\n    <thead>\n    <tr class=\"header\">\n        <th class=\"stage-start\"></th>\n"
    + ((stack1 = helpers.each.call(depth0,(depth0 != null ? depth0.stageData : depth0),{"name":"each","hash":{},"fn":this.program(4, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "    </tr>\n    </thead>\n    <tbody class=\"totals-box\">\n    <tr class=\"totals\">\n        <td class=\"stage-start\"><div class=\"cell-color\">\n          Average stage times:<br />\n"
    + ((stack1 = helpers['if'].call(depth0,(depth0 != null ? depth0.avgDurationMillisNoPause : depth0),{"name":"if","hash":{},"fn":this.program(6, data, 0, blockParams, depths),"inverse":this.program(8, data, 0, blockParams, depths),"data":data})) != null ? stack1 : "")
    + "        </div></td>\n"
    + ((stack1 = helpers.each.call(depth0,(depth0 != null ? depth0.stageData : depth0),{"name":"each","hash":{},"fn":this.program(10, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "    </tr>\n  </tbody>\n    <tbody class=\"tobsTable-body\">\n"
    + ((stack1 = helpers.each.call(depth0,(depth0 != null ? depth0.runs : depth0),{"name":"each","hash":{},"fn":this.program(12, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "  </tbody>\n\n</table>\n";
},"2":function(depth0,helpers,partials,data) {
    return "      <col class=\"stage-group\" style=\"max-width:10em;\" />\n";
},"4":function(depth0,helpers,partials,data) {
    var helper, alias1=this.escapeExpression;

  return "        <th class=\"stage-header-name-"
    + alias1(((helper = (helper = helpers.index || (data && data.index)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0,{"name":"index","hash":{},"data":data}) : helper)))
    + "\">"
    + alias1(this.lambda((depth0 != null ? depth0.name : depth0), depth0))
    + "</th>\n";
},"6":function(depth0,helpers,partials,data) {
    return "          (Average <span style=\"text-decoration: underline;\" title=\"builds that run all stages\">full</span> run time: ~"
    + this.escapeExpression((helpers.formatTime || (depth0 && depth0.formatTime) || helpers.helperMissing).call(depth0,(depth0 != null ? depth0.avgDurationMillisNoPause : depth0),2,{"name":"formatTime","hash":{},"data":data}))
    + ")\n";
},"8":function(depth0,helpers,partials,data) {
    return "          &nbsp;\n";
},"10":function(depth0,helpers,partials,data) {
    var helper, alias1=helpers.helperMissing, alias2=this.escapeExpression;

  return "        <td class=\"stage-total-"
    + alias2(((helper = (helper = helpers.index || (data && data.index)) != null ? helper : alias1),(typeof helper === "function" ? helper.call(depth0,{"name":"index","hash":{},"data":data}) : helper)))
    + "\">\n          <div class=\"cell-color\">\n            <div class=\"duration\">"
    + alias2((helpers.formatTime || (depth0 && depth0.formatTime) || alias1).call(depth0,(depth0 != null ? depth0.avgDurationMillisNoPause : depth0),2,{"name":"formatTime","hash":{},"data":data}))
    + "</div>\n            <div class=\"stackedBarChart\"></div>\n          </div>\n        </td>\n";
},"12":function(depth0,helpers,partials,data,blockParams,depths) {
    var stack1, alias1=this.lambda, alias2=this.escapeExpression, alias3=helpers.helperMissing;

  return "    <tr class=\"job "
    + alias2(alias1((depth0 != null ? depth0.status : depth0), depth0))
    + "\" data-runId=\""
    + alias2(alias1((depth0 != null ? depth0.id : depth0), depth0))
    + "\">\n        <td class=\"stage-start\">\n          <div class=\"cell-color\">\n            <div class=\"cell-box\">\n              <div class=\"jobName\"><span class=\"badge\"><a href=\""
    + alias2(alias1((depth0 != null ? depth0.id : depth0), depth0))
    + "\">"
    + alias2(alias1((depth0 != null ? depth0.name : depth0), depth0))
    + "</a></span></div>\n              <div class=\"stage-start-box\">\n                <div class=\"stage-start-time\">\n                  <div class=\"date\">"
    + alias2((helpers.formatDate || (depth0 && depth0.formatDate) || alias3).call(depth0,(depth0 != null ? depth0.startTimeMillis : depth0),"month",{"name":"formatDate","hash":{},"data":data}))
    + " "
    + alias2((helpers.formatDate || (depth0 && depth0.formatDate) || alias3).call(depth0,(depth0 != null ? depth0.startTimeMillis : depth0),"dom",{"name":"formatDate","hash":{},"data":data}))
    + "</div>\n                  <div class=\"time\">"
    + alias2((helpers.formatDate || (depth0 && depth0.formatDate) || alias3).call(depth0,(depth0 != null ? depth0.startTimeMillis : depth0),"time",{"name":"formatDate","hash":{},"data":data}))
    + "</div>\n                </div>\n                "
    + ((stack1 = helpers['if'].call(depth0,((stack1 = (depth0 != null ? depth0._links : depth0)) != null ? stack1.changesets : stack1),{"name":"if","hash":{},"fn":this.program(13, data, 0, blockParams, depths),"inverse":this.program(15, data, 0, blockParams, depths),"data":data})) != null ? stack1 : "")
    + "                <div class=\"stage-end-icons extension-dock\">\n"
    + ((stack1 = helpers['if'].call(depth0,((stack1 = (depth0 != null ? depth0._links : depth0)) != null ? stack1.artifacts : stack1),{"name":"if","hash":{},"fn":this.program(17, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "                </div>\n              </div>\n\n              <div class=\"clearfix\"></div>\n\n"
    + ((stack1 = helpers.unless.call(depth0,(depth0 != null ? depth0.durationMillis : depth0),{"name":"unless","hash":{},"fn":this.program(19, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "            </div>\n          </div>\n        </td>\n"
    + ((stack1 = helpers.each.call(depth0,(depth0 != null ? depth0.stages : depth0),{"name":"each","hash":{},"fn":this.program(22, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "    </tr>\n"
    + ((stack1 = helpers['if'].call(depth0,(depth0 != null ? depth0.durationMillis : depth0),{"name":"if","hash":{},"fn":this.program(39, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "");
},"13":function(depth0,helpers,partials,data) {
    var stack1;

  return "<div class=\"changeset-box\" cbwf-controller=\"run-changesets\" objectUrl=\""
    + this.escapeExpression(this.lambda(((stack1 = ((stack1 = (depth0 != null ? depth0._links : depth0)) != null ? stack1.changesets : stack1)) != null ? stack1.href : stack1), depth0))
    + "\"></div>\n";
},"15":function(depth0,helpers,partials,data) {
    return "                <div class=\"changeset-box no-changes\">No Changes</div>\n";
},"17":function(depth0,helpers,partials,data) {
    var stack1;

  return "                  <div cbwf-controller=\"build-artifacts-popup\" objectUrl=\""
    + this.escapeExpression(this.lambda(((stack1 = ((stack1 = (depth0 != null ? depth0._links : depth0)) != null ? stack1.artifacts : stack1)) != null ? stack1.href : stack1), depth0))
    + "\"><a class=\"link\"><i class=\"glyphicon glyphicon-download\" title=\"Download\"></i></a></div>\n";
},"19":function(depth0,helpers,partials,data) {
    var stack1;

  return ((stack1 = (helpers.ifCond || (depth0 && depth0.ifCond) || helpers.helperMissing).call(depth0,(depth0 != null ? depth0.status : depth0),"==","IN_PROGRESS",{"name":"ifCond","hash":{},"fn":this.program(20, data, 0),"inverse":this.noop,"data":data})) != null ? stack1 : "");
},"20":function(depth0,helpers,partials,data) {
    return "              <div class=\"time-in-queue progress\" title=\"waiting in queue\">\n                  <div class=\"progress-bar progress-bar-warning progress-bar-striped active\" style=\"width: 100%\">"
    + this.escapeExpression((helpers.formatTime || (depth0 && depth0.formatTime) || helpers.helperMissing).call(depth0,(depth0 != null ? depth0.queueDurationMillis : depth0),2,{"name":"formatTime","hash":{},"data":data}))
    + "</div>\n              </div>\n";
},"22":function(depth0,helpers,partials,data,blockParams,depths) {
    var stack1, helper, alias1=helpers.helperMissing, alias2="function", alias3=this.escapeExpression, alias4=this.lambda;

  return "        <td class=\"stage-cell stage-cell-"
    + alias3(((helper = (helper = helpers.index || (data && data.index)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"index","hash":{},"data":data}) : helper)))
    + " "
    + alias3(alias4((depth0 != null ? depth0.status : depth0), depth0))
    + "\" data-stageId=\""
    + alias3(alias4((depth0 != null ? depth0.id : depth0), depth0))
    + "\">\n          <div class=\"cell-color\">\n            <div class=\"stage-wrapper\">\n                <div class=\"duration\" >\n                    <span style=\"font-size: "
    + alias3(((helper = (helper = helpers.emphasise || (depth0 != null ? depth0.emphasise : depth0)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"emphasise","hash":{},"data":data}) : helper)))
    + "em; opacity: "
    + alias3((helpers.emphasiseToOpacity || (depth0 && depth0.emphasiseToOpacity) || alias1).call(depth0,(depth0 != null ? depth0.emphasise : depth0),{"name":"emphasiseToOpacity","hash":{},"data":data}))
    + ";\">"
    + alias3((helpers.formatTime || (depth0 && depth0.formatTime) || alias1).call(depth0,(depth0 != null ? depth0.durationMillisNoPause : depth0),2,{"name":"formatTime","hash":{},"data":data}))
    + "</span>\n                </div>\n"
    + ((stack1 = (helpers.ifCond || (depth0 && depth0.ifCond) || alias1).call(depth0,(depth0 != null ? depth0.status : depth0),"===","ABORTED",{"name":"ifCond","hash":{},"fn":this.program(23, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + ((stack1 = (helpers.ifCond || (depth0 && depth0.ifCond) || alias1).call(depth0,(depth0 != null ? depth0.status : depth0),"===","SUCCESS",{"name":"ifCond","hash":{},"fn":this.program(25, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + ((stack1 = (helpers.ifCond || (depth0 && depth0.ifCond) || alias1).call(depth0,(depth0 != null ? depth0.status : depth0),"===","IN_PROGRESS",{"name":"ifCond","hash":{},"fn":this.program(27, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + ((stack1 = (helpers.ifCond || (depth0 && depth0.ifCond) || alias1).call(depth0,(depth0 != null ? depth0.status : depth0),"===","UNSTABLE",{"name":"ifCond","hash":{},"fn":this.program(29, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + ((stack1 = (helpers.ifCond || (depth0 && depth0.ifCond) || alias1).call(depth0,(depth0 != null ? depth0.status : depth0),"===","PAUSED_PENDING_INPUT",{"name":"ifCond","hash":{},"fn":this.program(31, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + ((stack1 = (helpers.ifCond || (depth0 && depth0.ifCond) || alias1).call(depth0,(depth0 != null ? depth0.status : depth0),"===","FAILED",{"name":"ifCond","hash":{},"fn":this.program(33, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + ((stack1 = helpers['if'].call(depth0,(depth0 != null ? depth0.percentCompleteEstimate : depth0),{"name":"if","hash":{},"fn":this.program(35, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + ((stack1 = helpers['if'].call(depth0,(depth0 != null ? depth0.pauseDurationMillis : depth0),{"name":"if","hash":{},"fn":this.program(37, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "                <div class=\"extension-dock\"></div>\n            </div>\n          </div>\n    </td>\n";
},"23":function(depth0,helpers,partials,data) {
    return "                    <div class=\"status\">aborted</div>\n";
},"25":function(depth0,helpers,partials,data) {
    var stack1;

  return "                    <div cbwf-controller=\"stage-actions-popover\" descUrl=\""
    + this.escapeExpression(this.lambda(((stack1 = ((stack1 = (depth0 != null ? depth0._links : depth0)) != null ? stack1.self : stack1)) != null ? stack1.href : stack1), depth0))
    + "\" notIf=\"stage-actions-popover,stage-failed-popover\" caption=\"Success\" />\n";
},"27":function(depth0,helpers,partials,data) {
    var stack1;

  return "                    <div cbwf-controller=\"stage-actions-popover\" descUrl=\""
    + this.escapeExpression(this.lambda(((stack1 = ((stack1 = (depth0 != null ? depth0._links : depth0)) != null ? stack1.self : stack1)) != null ? stack1.href : stack1), depth0))
    + "\" notIf=\"stage-actions-popover,stage-failed-popover\" caption=\"In Progress\" />\n";
},"29":function(depth0,helpers,partials,data) {
    var stack1;

  return "                    <div cbwf-controller=\"stage-actions-popover\" descUrl=\""
    + this.escapeExpression(this.lambda(((stack1 = ((stack1 = (depth0 != null ? depth0._links : depth0)) != null ? stack1.self : stack1)) != null ? stack1.href : stack1), depth0))
    + "\" notIf=\"stage-actions-popover,stage-failed-popover\" caption=\"Unstable Build\" />\n";
},"31":function(depth0,helpers,partials,data,blockParams,depths) {
    var stack1;

  return "                    <div cbwf-controller=\"run-input-required\" objectUrl=\""
    + this.escapeExpression(this.lambda(((stack1 = ((stack1 = (depths[2] != null ? depths[2]._links : depths[2])) != null ? stack1.nextPendingInputAction : stack1)) != null ? stack1.href : stack1), depth0))
    + "\" />\n                    <div class=\"status\">paused</div>\n";
},"33":function(depth0,helpers,partials,data) {
    var stack1, alias1=this.lambda, alias2=this.escapeExpression;

  return "                    <div cbwf-controller=\"stage-failed-popover\" descUrl=\""
    + alias2(alias1(((stack1 = ((stack1 = (depth0 != null ? depth0._links : depth0)) != null ? stack1.self : stack1)) != null ? stack1.href : stack1), depth0))
    + "\" objectUrl=\""
    + alias2(alias1(((stack1 = ((stack1 = (depth0 != null ? depth0._links : depth0)) != null ? stack1.self : stack1)) != null ? stack1.href : stack1), depth0))
    + "\" notIf=\"stage-actions-popover,stage-failed-popover\" />\n                    <div class=\"status\">failed</div>\n";
},"35":function(depth0,helpers,partials,data) {
    return "                    <div class=\"progress\">\n                        <div class=\"progress-bar progress-bar-striped\" style=\"width: "
    + this.escapeExpression(this.lambda((depth0 != null ? depth0.percentCompleteEstimate : depth0), depth0))
    + "%\"></div>\n                    </div>\n";
},"37":function(depth0,helpers,partials,data) {
    var alias1=helpers.helperMissing, alias2=this.escapeExpression;

  return "                    <div class=\"pause-duration\">\n                        <span title=\"paused for "
    + alias2((helpers.formatTime || (depth0 && depth0.formatTime) || alias1).call(depth0,(depth0 != null ? depth0.pauseDurationMillis : depth0),2,{"name":"formatTime","hash":{},"data":data}))
    + "\">(paused for "
    + alias2((helpers.formatTime || (depth0 && depth0.formatTime) || alias1).call(depth0,(depth0 != null ? depth0.pauseDurationMillis : depth0),2,{"name":"formatTime","hash":{},"data":data}))
    + ")</span>\n                    </div>\n";
},"39":function(depth0,helpers,partials,data,blockParams,depths) {
    var stack1;

  return ((stack1 = helpers['if'].call(depth0,(depth0 != null ? depth0.percentCompleteEstimate : depth0),{"name":"if","hash":{},"fn":this.program(40, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "");
},"40":function(depth0,helpers,partials,data,blockParams,depths) {
    var stack1, alias1=this.lambda, alias2=this.escapeExpression;

  return "    <tr class=\"in-progress-run\">\n        <td class=\"stage-start hide-cell\"></td>\n        <td class=\"totalComplete\" colspan=\""
    + alias2(alias1(((stack1 = (depths[3] != null ? depths[3].stageData : depths[3])) != null ? stack1.length : stack1), depth0))
    + "\">\n            <div class=\"progress\">\n                <div class=\"progress-bar progress-bar-striped active\" role=\"progressbar\" aria-valuenow=\"45\"\n                     aria-valuemin=\"0\" aria-valuemax=\"100\"\n                     style=\"width:"
    + alias2(alias1((depth0 != null ? depth0.percentCompleteEstimate : depth0), depth0))
    + "%\">\n                </div>\n"
    + ((stack1 = (helpers.ifCond || (depth0 && depth0.ifCond) || helpers.helperMissing).call(depth0,(depths[3] != null ? depths[3].numRuns : depths[3]),">",0,{"name":"ifCond","hash":{},"fn":this.program(41, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "            </div>\n        </td>\n    </tr>\n";
},"41":function(depth0,helpers,partials,data,blockParams,depths) {
    var stack1;

  return ((stack1 = helpers['if'].call(depth0,(depths[1] != null ? depths[1].timeRemainingEstimate : depths[1]),{"name":"if","hash":{},"fn":this.program(42, data, 0, blockParams, depths),"inverse":this.program(44, data, 0, blockParams, depths),"data":data})) != null ? stack1 : "");
},"42":function(depth0,helpers,partials,data,blockParams,depths) {
    return "                        <span class=\"time-remaining\">"
    + this.escapeExpression((helpers.formatTime || (depth0 && depth0.formatTime) || helpers.helperMissing).call(depth0,(depths[1] != null ? depths[1].timeRemainingEstimate : depths[1]),2,{"name":"formatTime","hash":{},"data":data}))
    + "</span>\n";
},"44":function(depth0,helpers,partials,data) {
    return "                        <span class=\"time-remaining\">almost complete</span>\n";
},"46":function(depth0,helpers,partials,data) {
    var stack1;

  return ((stack1 = helpers['if'].call(depth0,(depth0 != null ? depth0.numRuns : depth0),{"name":"if","hash":{},"fn":this.program(47, data, 0),"inverse":this.program(49, data, 0),"data":data})) != null ? stack1 : "");
},"47":function(depth0,helpers,partials,data) {
    return "        <div class=\"alert alert-warning\">This Pipeline has run successfully, but does not define any stages. Please use the <code>stage</code> step to define some stages in this Pipeline.</div>\n";
},"49":function(depth0,helpers,partials,data) {
    return "        <div class=\"alert alert-info\">No data available. This Pipeline has not yet run.</div>\n";
},"compiler":[6,">= 2.0.0-beta.1"],"main":function(depth0,helpers,partials,data,blockParams,depths) {
    var stack1, helper;

  return "<div id=\"pipeline-box\">\n<h2>"
    + this.escapeExpression(((helper = (helper = helpers.fragCaption || (depth0 != null ? depth0.fragCaption : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0,{"name":"fragCaption","hash":{},"data":data}) : helper)))
    + "</h2>\n"
    + ((stack1 = helpers['if'].call(depth0,(depth0 != null ? depth0.numStages : depth0),{"name":"if","hash":{},"fn":this.program(1, data, 0, blockParams, depths),"inverse":this.program(46, data, 0, blockParams, depths),"data":data})) != null ? stack1 : "")
    + "</div>\n";
},"useData":true,"useDepths":true});

},{"jenkins-handlebars-rt/runtimes/handlebars3_rt":5}],58:[function(require,module,exports){
// hbsfy compiled Handlebars template
var HandlebarsCompiler = require('jenkins-handlebars-rt/runtimes/handlebars3_rt');
module.exports = HandlebarsCompiler.template({"1":function(depth0,helpers,partials,data) {
    var stack1, helper, alias1=helpers.helperMissing, alias2=this.escapeExpression;

  return "    <div class=\"header\">"
    + alias2(((helper = (helper = helpers.commitCount || (depth0 != null ? depth0.commitCount : depth0)) != null ? helper : alias1),(typeof helper === "function" ? helper.call(depth0,{"name":"commitCount","hash":{},"data":data}) : helper)))
    + " commit"
    + ((stack1 = (helpers.ifCond || (depth0 && depth0.ifCond) || alias1).call(depth0,(depth0 != null ? depth0.commitCount : depth0),">",1,{"name":"ifCond","hash":{},"fn":this.program(2, data, 0),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + " by <span class=\"contributor\">"
    + alias2(this.lambda(((stack1 = ((stack1 = (depth0 != null ? depth0.contributors : depth0)) != null ? stack1['0'] : stack1)) != null ? stack1.authorJenkinsId : stack1), depth0))
    + "</span>"
    + ((stack1 = (helpers.ifCond || (depth0 && depth0.ifCond) || alias1).call(depth0,(depth0 != null ? depth0.contributorCount : depth0),">",1,{"name":"ifCond","hash":{},"fn":this.program(4, data, 0),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "</div>\n";
},"2":function(depth0,helpers,partials,data) {
    return "s";
},"4":function(depth0,helpers,partials,data) {
    return " and others";
},"6":function(depth0,helpers,partials,data) {
    var stack1, helper, alias1=helpers.helperMissing;

  return "        <div class=\"header\">"
    + this.escapeExpression(((helper = (helper = helpers.commitCount || (depth0 != null ? depth0.commitCount : depth0)) != null ? helper : alias1),(typeof helper === "function" ? helper.call(depth0,{"name":"commitCount","hash":{},"data":data}) : helper)))
    + " commit"
    + ((stack1 = (helpers.ifCond || (depth0 && depth0.ifCond) || alias1).call(depth0,(depth0 != null ? depth0.commitCount : depth0),">",1,{"name":"ifCond","hash":{},"fn":this.program(2, data, 0),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "</div>\n";
},"8":function(depth0,helpers,partials,data) {
    var stack1, helper, alias1=helpers.helperMissing, alias2=this.escapeExpression;

  return "            <tr title=\""
    + ((stack1 = helpers['if'].call(depth0,(depth0 != null ? depth0.authorJenkinsId : depth0),{"name":"if","hash":{},"fn":this.program(9, data, 0),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + alias2((helpers.formatDate || (depth0 && depth0.formatDate) || alias1).call(depth0,(depth0 != null ? depth0.timestamp : depth0),"short",{"name":"formatDate","hash":{},"data":data}))
    + "\">\n                <td class=\"commitId\">\n                    <span class=\"label label-default\">\n"
    + ((stack1 = helpers['if'].call(depth0,(depth0 != null ? depth0.commitUrl : depth0),{"name":"if","hash":{},"fn":this.program(11, data, 0),"inverse":this.program(13, data, 0),"data":data})) != null ? stack1 : "")
    + "                    </span>\n                </td>\n                <td class=\"message\">"
    + alias2(((helper = (helper = helpers.messageLine1 || (depth0 != null ? depth0.messageLine1 : depth0)) != null ? helper : alias1),(typeof helper === "function" ? helper.call(depth0,{"name":"messageLine1","hash":{},"data":data}) : helper)))
    + "</td>\n            </tr>\n";
},"9":function(depth0,helpers,partials,data) {
    var helper;

  return "by "
    + this.escapeExpression(((helper = (helper = helpers.authorJenkinsId || (depth0 != null ? depth0.authorJenkinsId : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0,{"name":"authorJenkinsId","hash":{},"data":data}) : helper)))
    + " - ";
},"11":function(depth0,helpers,partials,data) {
    var helper, alias1=helpers.helperMissing, alias2="function", alias3=this.escapeExpression;

  return "                        <a href=\""
    + alias3(((helper = (helper = helpers.commitUrl || (depth0 != null ? depth0.commitUrl : depth0)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"commitUrl","hash":{},"data":data}) : helper)))
    + "\" target=\"_blank\">"
    + alias3(((helper = (helper = helpers.commitIdDisplay || (depth0 != null ? depth0.commitIdDisplay : depth0)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"commitIdDisplay","hash":{},"data":data}) : helper)))
    + "</a>\n";
},"13":function(depth0,helpers,partials,data) {
    var helper;

  return "                        "
    + this.escapeExpression(((helper = (helper = helpers.commitIdDisplay || (depth0 != null ? depth0.commitIdDisplay : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0,{"name":"commitIdDisplay","hash":{},"data":data}) : helper)))
    + "\n";
},"compiler":[6,">= 2.0.0-beta.1"],"main":function(depth0,helpers,partials,data) {
    var stack1, helper, alias1=helpers.helperMissing, alias2="function", alias3=this.escapeExpression;

  return "<div class=\"alert alert-info run-changeset-details "
    + alias3(((helper = (helper = helpers.kind || (depth0 != null ? depth0.kind : depth0)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"kind","hash":{},"data":data}) : helper)))
    + "-repo\">\n"
    + ((stack1 = helpers['if'].call(depth0,((stack1 = ((stack1 = (depth0 != null ? depth0.contributors : depth0)) != null ? stack1['0'] : stack1)) != null ? stack1.authorJenkinsId : stack1),{"name":"if","hash":{},"fn":this.program(1, data, 0),"inverse":this.program(6, data, 0),"data":data})) != null ? stack1 : "")
    + "    <div class=\"body\">\n        <table>\n"
    + ((stack1 = helpers.each.call(depth0,(depth0 != null ? depth0.commits : depth0),{"name":"each","hash":{},"fn":this.program(8, data, 0),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "        </table>\n    </div>\n    <div class=\"footer\">\n        <a href=\""
    + alias3(((helper = (helper = helpers.consoleUrl || (depth0 != null ? depth0.consoleUrl : depth0)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"consoleUrl","hash":{},"data":data}) : helper)))
    + "\" target=\"_blank\">See detail page</a>\n    </div>\n</div>";
},"useData":true});

},{"jenkins-handlebars-rt/runtimes/handlebars3_rt":5}],59:[function(require,module,exports){
// hbsfy compiled Handlebars template
var HandlebarsCompiler = require('jenkins-handlebars-rt/runtimes/handlebars3_rt');
module.exports = HandlebarsCompiler.template({"1":function(depth0,helpers,partials,data) {
    return "s";
},"compiler":[6,">= 2.0.0-beta.1"],"main":function(depth0,helpers,partials,data) {
    var stack1;

  return "<div class=\"run-changesets\">\n    <div class=\"run-changeset\">\n        <strong class=\"num\">"
    + this.escapeExpression(this.lambda((depth0 != null ? depth0.commitCount : depth0), depth0))
    + "</strong>\n        <span class=\"changes commitLabel\">commit"
    + ((stack1 = (helpers.ifCond || (depth0 && depth0.ifCond) || helpers.helperMissing).call(depth0,(depth0 != null ? depth0.commitCount : depth0),">",1,{"name":"ifCond","hash":{},"fn":this.program(1, data, 0),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "</span>\n    </div>\n</div>";
},"useData":true});

},{"jenkins-handlebars-rt/runtimes/handlebars3_rt":5}],60:[function(require,module,exports){
// hbsfy compiled Handlebars template
var HandlebarsCompiler = require('jenkins-handlebars-rt/runtimes/handlebars3_rt');
module.exports = HandlebarsCompiler.template({"compiler":[6,">= 2.0.0-beta.1"],"main":function(depth0,helpers,partials,data) {
    var helper, alias1=helpers.helperMissing, alias2="function", alias3=this.escapeExpression;

  return "<div class=\"input cbwf-info-action-popover run-input-required\">\n    <div class=\"remove\"><span class=\"glyphicon glyphicon-remove remove\"></span></div>\n    <div class=\"caption\">"
    + alias3(((helper = (helper = helpers.message || (depth0 != null ? depth0.message : depth0)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"message","hash":{},"data":data}) : helper)))
    + "</div>\n    \n    <div class=\"has-advanced-inputs\">\n        The paused input step uses advanced input options.\n        <a href=\""
    + alias3(((helper = (helper = helpers.redirectApprovalUrl || (depth0 != null ? depth0.redirectApprovalUrl : depth0)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"redirectApprovalUrl","hash":{},"data":data}) : helper)))
    + "\">Please redirect to approve</a>.\n    </div>\n\n</div>";
},"useData":true});

},{"jenkins-handlebars-rt/runtimes/handlebars3_rt":5}],61:[function(require,module,exports){
// hbsfy compiled Handlebars template
var HandlebarsCompiler = require('jenkins-handlebars-rt/runtimes/handlebars3_rt');
module.exports = HandlebarsCompiler.template({"1":function(depth0,helpers,partials,data,blockParams,depths) {
    var stack1, alias1=helpers.helperMissing;

  return ((stack1 = (helpers.ifCond || (depth0 && depth0.ifCond) || alias1).call(depth0,(depth0 != null ? depth0.type : depth0),"===","BooleanParameterDefinition",{"name":"ifCond","hash":{},"fn":this.program(2, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + ((stack1 = (helpers.ifCond || (depth0 && depth0.ifCond) || alias1).call(depth0,(depth0 != null ? depth0.type : depth0),"===","StringParameterDefinition",{"name":"ifCond","hash":{},"fn":this.program(7, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + ((stack1 = (helpers.ifCond || (depth0 && depth0.ifCond) || alias1).call(depth0,(depth0 != null ? depth0.type : depth0),"===","TextParameterDefinition",{"name":"ifCond","hash":{},"fn":this.program(7, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + ((stack1 = (helpers.ifCond || (depth0 && depth0.ifCond) || alias1).call(depth0,(depth0 != null ? depth0.type : depth0),"===","PasswordParameterDefinition",{"name":"ifCond","hash":{},"fn":this.program(9, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + ((stack1 = (helpers.ifCond || (depth0 && depth0.ifCond) || alias1).call(depth0,(depth0 != null ? depth0.type : depth0),"===","ChoiceParameterDefinition",{"name":"ifCond","hash":{},"fn":this.program(11, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "");
},"2":function(depth0,helpers,partials,data) {
    var stack1, helper, alias1=helpers.helperMissing, alias2="function", alias3=this.escapeExpression;

  return "                <div class=\"checkbox\">\n                    <label>\n                        <input name=\""
    + alias3(((helper = (helper = helpers.name || (depth0 != null ? depth0.name : depth0)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"name","hash":{},"data":data}) : helper)))
    + "\" type=\"checkbox\" "
    + ((stack1 = helpers['if'].call(depth0,((stack1 = (depth0 != null ? depth0.definition : depth0)) != null ? stack1.defaultVal : stack1),{"name":"if","hash":{},"fn":this.program(3, data, 0),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + ">"
    + alias3(((helper = (helper = helpers.name || (depth0 != null ? depth0.name : depth0)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"name","hash":{},"data":data}) : helper)))
    + "\n                    </label>\n                    "
    + ((stack1 = helpers['if'].call(depth0,(depth0 != null ? depth0.description : depth0),{"name":"if","hash":{},"fn":this.program(5, data, 0),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "\n                </div>\n";
},"3":function(depth0,helpers,partials,data) {
    return "checked=\"checked\"";
},"5":function(depth0,helpers,partials,data) {
    var helper;

  return "<p class=\"help-block\">"
    + this.escapeExpression(((helper = (helper = helpers.description || (depth0 != null ? depth0.description : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0,{"name":"description","hash":{},"data":data}) : helper)))
    + "</p>";
},"7":function(depth0,helpers,partials,data) {
    var stack1, helper, alias1=helpers.helperMissing, alias2="function", alias3=this.escapeExpression;

  return "                <div class=\"form-group\">\n                    <label for=\""
    + alias3(((helper = (helper = helpers.id || (depth0 != null ? depth0.id : depth0)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"id","hash":{},"data":data}) : helper)))
    + "-input-"
    + alias3(((helper = (helper = helpers.index || (data && data.index)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"index","hash":{},"data":data}) : helper)))
    + "\">"
    + alias3(((helper = (helper = helpers.name || (depth0 != null ? depth0.name : depth0)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"name","hash":{},"data":data}) : helper)))
    + "</label>\n                    <input name=\""
    + alias3(((helper = (helper = helpers.name || (depth0 != null ? depth0.name : depth0)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"name","hash":{},"data":data}) : helper)))
    + "\" type=\"text\" class=\"form-control\" id=\""
    + alias3(((helper = (helper = helpers.id || (depth0 != null ? depth0.id : depth0)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"id","hash":{},"data":data}) : helper)))
    + "-input-"
    + alias3(((helper = (helper = helpers.index || (data && data.index)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"index","hash":{},"data":data}) : helper)))
    + "\" value=\""
    + alias3(this.lambda(((stack1 = (depth0 != null ? depth0.definition : depth0)) != null ? stack1.defaultVal : stack1), depth0))
    + "\">\n                    "
    + ((stack1 = helpers['if'].call(depth0,(depth0 != null ? depth0.description : depth0),{"name":"if","hash":{},"fn":this.program(5, data, 0),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "\n                </div>\n";
},"9":function(depth0,helpers,partials,data) {
    var stack1, helper, alias1=helpers.helperMissing, alias2="function", alias3=this.escapeExpression;

  return "                <div class=\"form-group\">\n                    <label for=\""
    + alias3(((helper = (helper = helpers.id || (depth0 != null ? depth0.id : depth0)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"id","hash":{},"data":data}) : helper)))
    + "-input-"
    + alias3(((helper = (helper = helpers.index || (data && data.index)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"index","hash":{},"data":data}) : helper)))
    + "\">"
    + alias3(((helper = (helper = helpers.name || (depth0 != null ? depth0.name : depth0)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"name","hash":{},"data":data}) : helper)))
    + "</label>\n                    <input name=\""
    + alias3(((helper = (helper = helpers.name || (depth0 != null ? depth0.name : depth0)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"name","hash":{},"data":data}) : helper)))
    + "\" type=\"password\" class=\"form-control\" id=\""
    + alias3(((helper = (helper = helpers.id || (depth0 != null ? depth0.id : depth0)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"id","hash":{},"data":data}) : helper)))
    + "-input-"
    + alias3(((helper = (helper = helpers.index || (data && data.index)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"index","hash":{},"data":data}) : helper)))
    + "\" value=\""
    + alias3(this.lambda(((stack1 = (depth0 != null ? depth0.definition : depth0)) != null ? stack1.defaultVal : stack1), depth0))
    + "\" placeholder=\"Password\">\n                    "
    + ((stack1 = helpers['if'].call(depth0,(depth0 != null ? depth0.description : depth0),{"name":"if","hash":{},"fn":this.program(5, data, 0),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "\n                </div>\n";
},"11":function(depth0,helpers,partials,data,blockParams,depths) {
    var stack1, helper, alias1=helpers.helperMissing, alias2="function", alias3=this.escapeExpression;

  return "                <div class=\"form-group\">\n                    <label for=\""
    + alias3(((helper = (helper = helpers.id || (depth0 != null ? depth0.id : depth0)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"id","hash":{},"data":data}) : helper)))
    + "-input-"
    + alias3(((helper = (helper = helpers.index || (data && data.index)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"index","hash":{},"data":data}) : helper)))
    + "\">"
    + alias3(((helper = (helper = helpers.name || (depth0 != null ? depth0.name : depth0)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"name","hash":{},"data":data}) : helper)))
    + "</label>\n                    <select name=\""
    + alias3(((helper = (helper = helpers.name || (depth0 != null ? depth0.name : depth0)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"name","hash":{},"data":data}) : helper)))
    + "\" class=\"form-control\" id=\""
    + alias3(((helper = (helper = helpers.id || (depth0 != null ? depth0.id : depth0)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"id","hash":{},"data":data}) : helper)))
    + "-input-"
    + alias3(((helper = (helper = helpers.index || (data && data.index)) != null ? helper : alias1),(typeof helper === alias2 ? helper.call(depth0,{"name":"index","hash":{},"data":data}) : helper)))
    + "\">\n"
    + ((stack1 = helpers.each.call(depth0,((stack1 = (depth0 != null ? depth0.definition : depth0)) != null ? stack1.choices : stack1),{"name":"each","hash":{},"fn":this.program(12, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "                    </select>\n                    "
    + ((stack1 = helpers['if'].call(depth0,(depth0 != null ? depth0.description : depth0),{"name":"if","hash":{},"fn":this.program(5, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "\n                </div>\n";
},"12":function(depth0,helpers,partials,data,blockParams,depths) {
    var stack1, alias1=helpers.helperMissing;

  return ((stack1 = (helpers.ifCond || (depth0 && depth0.ifCond) || alias1).call(depth0,depth0,"===",(depths[1] != null ? depths[1].defaultVal : depths[1]),{"name":"ifCond","hash":{},"fn":this.program(13, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + ((stack1 = (helpers.ifCond || (depth0 && depth0.ifCond) || alias1).call(depth0,depth0,"!==",(depths[1] != null ? depths[1].defaultVal : depths[1]),{"name":"ifCond","hash":{},"fn":this.program(15, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "");
},"13":function(depth0,helpers,partials,data) {
    return "                            <option selected>"
    + this.escapeExpression(this.lambda(depth0, depth0))
    + "</option>\n";
},"15":function(depth0,helpers,partials,data) {
    return "                            <option>"
    + this.escapeExpression(this.lambda(depth0, depth0))
    + "</option>\n";
},"compiler":[6,">= 2.0.0-beta.1"],"main":function(depth0,helpers,partials,data,blockParams,depths) {
    var stack1, helper, alias1=helpers.helperMissing, alias2=this.escapeExpression;

  return "<div class=\"input cbwf-info-action-popover run-input-required\">\n    <div class=\"remove\"><span class=\"glyphicon glyphicon-remove remove\"></span></div>\n    <div class=\"caption\">"
    + alias2((helpers.breaklines || (depth0 && depth0.breaklines) || alias1).call(depth0,(depth0 != null ? depth0.message : depth0),{"name":"breaklines","hash":{},"data":data}))
    + "</div>\n    <div class=\"form\">\n        <div class=\"inputs\">\n"
    + ((stack1 = helpers.each.call(depth0,(depth0 != null ? depth0.inputs : depth0),{"name":"each","hash":{},"fn":this.program(1, data, 0, blockParams, depths),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "        </div>\n        <div class=\"buttons\">\n            <button type=\"submit\" class=\"btn btn-primary btn-sm proceed-button\">"
    + alias2(((helper = (helper = helpers.proceedText || (depth0 != null ? depth0.proceedText : depth0)) != null ? helper : alias1),(typeof helper === "function" ? helper.call(depth0,{"name":"proceedText","hash":{},"data":data}) : helper)))
    + "</button>\n            <button type=\"submit\" class=\"btn btn-default btn-sm abort-button\">Abort</button>\n        </div>\n    </div>\n\n</div>";
},"useData":true,"useDepths":true});

},{"jenkins-handlebars-rt/runtimes/handlebars3_rt":5}],62:[function(require,module,exports){
// hbsfy compiled Handlebars template
var HandlebarsCompiler = require('jenkins-handlebars-rt/runtimes/handlebars3_rt');
module.exports = HandlebarsCompiler.template({"1":function(depth0,helpers,partials,data) {
    var stack1;

  return ((stack1 = (helpers.ifCond || (depth0 && depth0.ifCond) || helpers.helperMissing).call(depth0,(depth0 != null ? depth0.status : depth0),"!=","NOT_EXECUTED",{"name":"ifCond","hash":{},"fn":this.program(2, data, 0),"inverse":this.noop,"data":data})) != null ? stack1 : "");
},"2":function(depth0,helpers,partials,data) {
    var stack1;

  return ((stack1 = helpers['if'].call(depth0,((stack1 = ((stack1 = (depth0 != null ? depth0._links : depth0)) != null ? stack1.log : stack1)) != null ? stack1.href : stack1),{"name":"if","hash":{},"fn":this.program(3, data, 0),"inverse":this.program(8, data, 0),"data":data})) != null ? stack1 : "");
},"3":function(depth0,helpers,partials,data) {
    var stack1, alias1=this.lambda, alias2=this.escapeExpression;

  return "        <div class=\"node-log-frame "
    + alias2(alias1((depth0 != null ? depth0.status : depth0), depth0))
    + "\" cbwf-controller=\"node-log\" objectUrl=\""
    + alias2(alias1(((stack1 = ((stack1 = (depth0 != null ? depth0._links : depth0)) != null ? stack1.log : stack1)) != null ? stack1.href : stack1), depth0))
    + "\">\n            <div class=\"node-name\"><span class=\"glyphicon glyphicon-collapse-down\" title=\"Expand\"></span><span class=\"glyphicon glyphicon-collapse-up\" title=\"Collapse\"></span>\n                <a class=\"log-link\" href=\""
    + alias2(alias1(((stack1 = ((stack1 = (depth0 != null ? depth0._links : depth0)) != null ? stack1.console : stack1)) != null ? stack1.href : stack1), depth0))
    + "\">\n                "
    + alias2(alias1((depth0 != null ? depth0.name : depth0), depth0))
    + "\n"
    + ((stack1 = helpers['if'].call(depth0,(depth0 != null ? depth0.parameterDescription : depth0),{"name":"if","hash":{},"fn":this.program(4, data, 0),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "                </a>\n                "
    + ((stack1 = helpers['if'].call(depth0,(depth0 != null ? depth0.durationMillis : depth0),{"name":"if","hash":{},"fn":this.program(6, data, 0),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "\n\n            </div>\n            <div class=\"log-details\"></div>\n        </div>\n";
},"4":function(depth0,helpers,partials,data) {
    return "                    -- "
    + this.escapeExpression(this.lambda((depth0 != null ? depth0.parameterDescription : depth0), depth0))
    + "\n";
},"6":function(depth0,helpers,partials,data) {
    return " (self time "
    + this.escapeExpression((helpers.formatTime || (depth0 && depth0.formatTime) || helpers.helperMissing).call(depth0,(depth0 != null ? depth0.durationMillis : depth0),{"name":"formatTime","hash":{},"data":data}))
    + ")";
},"8":function(depth0,helpers,partials,data) {
    var stack1, alias1=this.lambda, alias2=this.escapeExpression;

  return "        <div class=\"node-log-frame "
    + alias2(alias1((depth0 != null ? depth0.status : depth0), depth0))
    + "\" objectUrl=\""
    + alias2(alias1(((stack1 = ((stack1 = (depth0 != null ? depth0._links : depth0)) != null ? stack1.self : stack1)) != null ? stack1.href : stack1), depth0))
    + "\">\n            <div class=\"node-name\">"
    + alias2(alias1((depth0 != null ? depth0.name : depth0), depth0))
    + "  "
    + ((stack1 = helpers['if'].call(depth0,(depth0 != null ? depth0.durationMillis : depth0),{"name":"if","hash":{},"fn":this.program(9, data, 0),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "</div>\n        </div>\n";
},"9":function(depth0,helpers,partials,data) {
    return "(self time "
    + this.escapeExpression((helpers.formatTime || (depth0 && depth0.formatTime) || helpers.helperMissing).call(depth0,(depth0 != null ? depth0.durationMillis : depth0),{"name":"formatTime","hash":{},"data":data}))
    + ")";
},"compiler":[6,">= 2.0.0-beta.1"],"main":function(depth0,helpers,partials,data) {
    var stack1;

  return "<div class=\"cbwf-stage-logs\">\n"
    + ((stack1 = helpers.each.call(depth0,(depth0 != null ? depth0.stageFlowNodes : depth0),{"name":"each","hash":{},"fn":this.program(1, data, 0),"inverse":this.noop,"data":data})) != null ? stack1 : "")
    + "</div>\n";
},"useData":true});

},{"jenkins-handlebars-rt/runtimes/handlebars3_rt":5}],63:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var jqProxy = require('../../../jQuery');

exports.stackedBarChart = function(values, highlightIdx) {
    if (highlightIdx !== undefined && highlightIdx < 0 || highlightIdx > (values.length - 1)) {
        throw '"highlightIdx" of ' + highlightIdx + ' is not valid for a value array of length ' + values.length;
    }

    var stackedBarChart = jqProxy.getJQuery()('<div class="stackedBarChart inner clearfix" />');
    var valueSum = 0;

    // Calc value sum
    for (var i = 0; i < values.length; i++) {
        valueSum += values[i];
    }

    // Calc percentages
    var percentages = [];
    for (var i = 0; i < values.length; i++) {
        percentages.push((values[i] / valueSum) * 100);
    }

    // Add cells based on percentages
    for (var i = 0; i < percentages.length; i++) {
        var cell = jqProxy.getJQuery()('<div class="bar" />');
        cell.css('width', percentages[i] + '%');
        if (highlightIdx !== undefined) {
            if (i < highlightIdx) {
                cell.addClass('prehighlight');
            } else if (i === highlightIdx) {
                cell.addClass('highlight');
            } else {
                cell.addClass('posthighlight');
            }
        }
        stackedBarChart.append(cell);

    }
    stackedBarChart.append('<div class="clearfix" />');

    return stackedBarChart;
}

},{"../../../jQuery":25}],64:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var jqProxy = require('../../../jQuery');
var templates = require('../../templates');
var popoverWidget = require('../popover');

exports.show = function(title, body, options) {
    if (!options) {
        options = {};
    }

    var dialog = templates.apply('dialog', {
        title: title,
        options: options
    });

    var $ = jqProxy.getJQuery();
    var theWindow = require('window-handle').getWindow();
    var headerEl = $('.header', dialog);
    var bodyEl = $('.body', dialog);

    if (options.width) {
        dialog.css('width', options.width);
    } else {
        var winWidth = $(theWindow).width();
        var popoverWidth = Math.min(800, (winWidth * 0.7));
        dialog.css('max-width', popoverWidth);
    }
    if (options.height) {
        dialog.css('height', options.height);
    } else {
        var winHeight = $(theWindow).height();
        var popoverHeight = Math.min(600, (winHeight * 0.7));
        dialog.css('max-height', popoverHeight);
    }

    bodyEl.append(body);

    options.modal = true;
    // Undefined for the onElement makes it in the window center, otherwise it needs it for positioning
    var popover = popoverWidget.newPopover(title, dialog, options.onElement, options);

    popover.show();
    if (options.onshow) {
        options.onshow();
    }

    return popover;
}

exports.hide = function(dialog) {
    popoverWidget.hide(dialog);
}

},{"../../../jQuery":25,"../../templates":53,"../popover":66,"window-handle":14}],65:[function(require,module,exports){
// hbsfy compiled Handlebars template
var HandlebarsCompiler = require('jenkins-handlebars-rt/runtimes/handlebars3_rt');
module.exports = HandlebarsCompiler.template({"compiler":[6,">= 2.0.0-beta.1"],"main":function(depth0,helpers,partials,data) {
    var stack1, helper, alias1=this.escapeExpression;

  return "<div class=\"cbwf-dialog "
    + alias1(this.lambda(((stack1 = (depth0 != null ? depth0.options : depth0)) != null ? stack1.classes : stack1), depth0))
    + "\">\n    <div class=\"remove\"><span class=\"glyphicon glyphicon-remove\"></span></div>\n    <div class=\"header\">"
    + alias1(((helper = (helper = helpers.title || (depth0 != null ? depth0.title : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0,{"name":"title","hash":{},"data":data}) : helper)))
    + "</div>\n    <div class=\"body\"></div>\n</div>";
},"useData":true});

},{"jenkins-handlebars-rt/runtimes/handlebars3_rt":5}],66:[function(require,module,exports){
/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var jqProxy = require('../../../jQuery');
var mvc = require('../../../mvc');
var timeout = require('../../../util/timeout');

var timeoutHandler = timeout.newTimeoutHandler("popovers");

exports.newPopover = function(name, popover, onElement, options) {
    return new Popover(name, popover, onElement, options);
}

exports.hide = function(popover) {
    if (!popover) {
        return;
    }

    var $ = jqProxy.getJQuery();

    if ($.isArray(popover)) {
        // The 'popover' arg is actually an array of popover names
        // that should be hidden.

        var activePopovers = $('.cbwf-popover');
        activePopovers.each(function () {
            var thePopover = $(this);
            var popoverName = thePopover.attr('popover-name');
            if (popover.indexOf(popoverName) !== -1) {
                exports.hide(thePopover);
            }
        });
        return;
    } else if (typeof popover === 'string') {
        exports.hide(popover.split(","));
        return;
    }

    if (popover.escPressListener) {
        $('body').unbind('keydown', popover.escPressListener);
        delete popover.escPressListener;
    }
    popover.remove();
}

function Popover(name, popover, onElement, options) {
    if (!name) {
        throw "Popover must be supplied with a 'name' param.";
    }
    if (options === undefined) {
        options = {
            allowmulti: false
        };
    }

    if (!options.placement) {
        options.placement = 'top';
    }

    popover.attr('popover-name', name);
    popover.addClass(name);

    this.name = name;
    this.popover = popover;
    this.onElement = onElement;
    this.options = options;
}

Popover.prototype.show = function() {
    var $ = jqProxy.getJQuery();
    var theWindow = require('window-handle').getWindow();
    var thisPopover = this;

    // If there's a modal popover already visible, don't show this one.
    if ($('.cbwf-popover.cbwf-modal').size() > 0) {
        // Don't show.
        return;
    }

    if (!thisPopover.options.allowmulti && isPopupVisible(thisPopover.name)) {
        // Don't show.
        return;
    }

    var notIf = thisPopover.options.notIf;
    if (notIf && isPopupVisible(notIf)) {
        // Don't show.
        return;
    }
    var hidePopups = thisPopover.options.hide;
    if (hidePopups) {
        exports.hide(hidePopups);
    }

    thisPopover.popover.addClass('cbwf-widget');
    thisPopover.popover.addClass('cbwf-popover');
    thisPopover.popover.addClass('placement-'+this.options.placement);
    if (thisPopover.options.modal) {
        thisPopover.popover.addClass('cbwf-modal');
    }

    $('body').append(thisPopover.popover);
    thisPopover.applyPlacement();

    // Reapply controllers...
    mvc.applyControllers(thisPopover.popover, true);

    // Handle removal of the popover
    if (this.popover.escPressListener) {
        $(theWindow).unbind('keydown', thisPopover.popover.escPressListener);
    }
    thisPopover.popover.escPressListener = function (keyPressEvent) {
        if(keyPressEvent.which === 27){
            thisPopover.hide();
        }
    }
    $('body').keydown(thisPopover.popover.escPressListener);
    $('.remove', thisPopover.popover).click(function() {
        thisPopover.hide();
    });

    if (thisPopover.options.onshow) {
        thisPopover.options.onshow();
        thisPopover.applyPlacement();
    }
}
Popover.prototype.hide = function() {
    exports.hide(this.popover);
}

Popover.prototype.click = function() {
    var $ = jqProxy.getJQuery();
    var docBody = $('body');
    var thisPopover = this;

    var eventName = 'click';
    if (thisPopover.options.namespace) {
        eventName += '.' + thisPopover.options.namespace;
        thisPopover.onElement.off(eventName);
    }

    thisPopover.onElement.on(eventName, function() {
        thisPopover.show();

        var boxCoords = jqProxy.getElementsEnclosingBoxCoords([thisPopover.onElement, thisPopover.popover]);
        function unbindListeners() {
            // We're done... stop listening for mouse move and keypress events and remove the popover.
            docBody.unbind('click', isOverElementListener);
            docBody.unbind('keydown', escPressListener);
            thisPopover.hide();
        }

        var isOverElementChecker = function (mouseX, mouseY) {
            if (!jqProxy.isCoordInBox(mouseX, mouseY, boxCoords)) {
                unbindListeners();
                return false;
            } else {
                return true;
            }
        };

        var isOverElementListener = function (moveEvent) {
            isOverElementChecker(moveEvent.pageX, moveEvent.pageY);
        }
        var escPressListener = function (keyPressEvent) {
            if(keyPressEvent.which === 27){
                unbindListeners();
            }
        }

        docBody.click(isOverElementListener);
        docBody.keydown(escPressListener);
        $('.remove', thisPopover.popover).click(function() {
            unbindListeners();
        });
    });
}

Popover.prototype.hover = function() {
    var $ = jqProxy.getJQuery();
    var mouseTracker = jqProxy.getMouseTracker();
    var thisPopover = this;

    var inoutDelay = thisPopover.options.inoutDelay;
    if (inoutDelay === undefined) {
        inoutDelay = 500;
    }

    // You can use the hover or mouse enter and leave events, but behavior gets interesting
    // when you have multiple popovers on top of each other, with mouse moves entering the "top"
    // one triggering movemove out events on lower ones etc.  Also, interesting things happen
    // just at the element boundaries.  Therefore, we use mouseenter and then mousemove.
    thisPopover.onElement.mouseenter(function() {
        // Don't start listening for mousemove events immediately.  This way, we "skip"
        // events that happen just at the boundary as the mouse is crossing it.
        timeoutHandler.setTimeout(function() {
            var onElementCoords = jqProxy.getElementBoxCoords(thisPopover.onElement);
            var docBody = $('body');

            // If the mouse is no longer in the box then we just return (i.e. ignore).
            if (!jqProxy.isCoordInBox(mouseTracker.x, mouseTracker.y, onElementCoords)) {
                // mouse not in the box... ignore...
                return;
            }

            // Show the popover now.
            thisPopover.show();

            var hoverBoxes = [onElementCoords];
            if (thisPopover.options.hoverBoth) {
                var popoverCoords = jqProxy.getElementBoxCoords(thisPopover.popover);

                hoverBoxes.push(popoverCoords);

                // We need to stretch the onElementCoords so the boxes touch.  Direction in which we stretch depends on the
                // placement of the popover..
                if (thisPopover.options.placement === 'left') {
                    // popover is to the left of the onElement => stretch onElement left to meet the right.x of the popover
                    jqProxy.stretchBoxCoords(onElementCoords, 'left', popoverCoords.topRight.x);
                } else if (thisPopover.options.placement === 'right') {
                    // popover is to the right of the onElement => stretch onElement right to meet the left.x of the popover
                    jqProxy.stretchBoxCoords(onElementCoords, 'right', popoverCoords.topLeft.x);
                } else if (thisPopover.options.placement === 'bottom') {
                    // popover is to the bottom of the onElement => stretch onElement down to meet the top.y of the popover
                    jqProxy.stretchBoxCoords(onElementCoords, 'down', popoverCoords.topLeft.y);
                } else {
                    // popover is to the top of the onElement => stretch onElement up to meet the bottom.y of the popover
                    jqProxy.stretchBoxCoords(onElementCoords, 'up', popoverCoords.bottomLeft.y);
                }
            }

            function remove() {
                docBody.unbind('mousemove', mouseMoveListener);
                thisPopover.hide();
            }

            function isMouseInHoverArea() {
                for (var i = 0; i < hoverBoxes.length; i++) {
                    if (jqProxy.isCoordInBox(mouseTracker.x, mouseTracker.y, hoverBoxes[i])) {
                        return true;
                    }
                }
                return false;
            }

            var removePopoverChecker = function (recheck) {
                if (!isMouseInHoverArea()) {
                    if (recheck) {
                        // Perform a recheck before removing the popover i.e. set a timeout
                        // to check are we still outside the hover area.  If at that time we still are
                        // outside, only then remove it. This allows the user to momentarily exit and
                        // re-enter the hover area without the hover disappearing.
                        timeoutHandler.setTimeout(function() {
                            removePopoverChecker(false);
                        }, Math.floor(inoutDelay * 0.8));
                        return;
                    }

                    // We're done... stop listening for mouse move events and remove the popover.
                    remove();
                }
            };

            var mouseMoveListener = function () {
                removePopoverChecker(true);
            }

            // If the mouse is still over the element (might not if the mouse just swiped over),
            // then track mouse moves as a way of deciding whether or not to hide the popover.
            if (isMouseInHoverArea()) {
                docBody.mousemove(mouseMoveListener);
                $('.remove', thisPopover.popover).click(function() {
                    remove();
                });
            }
        }, inoutDelay);
    });
}

Popover.prototype.applyPlacement = function() {
    var $ = jqProxy.getJQuery();
    var theWindow = require('window-handle').getWindow();
    var thisPopover = this;
    var placement = thisPopover.options.placement;

    // If there's no anchor element or placement specified, we default
    // the placement to the center of the window.
    if (!thisPopover.onElement) {
        placement = 'window-center';
    } else if (!placement) {
        placement = 'top';
    }

    if (placement === 'right') {
        var onElementOffset = thisPopover.onElement.offset();
        thisPopover.popover.css({
            'top': onElementOffset.top,
            'left': onElementOffset.left + thisPopover.onElement.width() + 5
        });
    } else if (placement === 'left') {
        var onElementOffset = thisPopover.onElement.offset();
        thisPopover.popover.css({
            'top': onElementOffset.top,
            'left': onElementOffset.left - (thisPopover.popover.width() + 5)
        });
    } else if (placement === 'right-inside') {
        var onElementOffset = thisPopover.onElement.offset();
        thisPopover.popover.css({
            'top': onElementOffset.top + Math.round(parseFloat(thisPopover.onElement.css("border-top-width"))),
            'left': onElementOffset.left + thisPopover.onElement.width() - (thisPopover.popover.width() - Math.round(parseFloat(thisPopover.onElement.css("border-left-width"))))
        });
    } else if (placement === 'window-center') {
        var winWidth = $(theWindow).width();
        var winHeight = $(theWindow).height();
        var popoverWidth = thisPopover.popover.width();
        var popoverHeight = thisPopover.popover.height();

        var leftPlacement = ((winWidth - popoverWidth) / 2);
        var topPlacement = ((winHeight - popoverHeight) / 2);

        // try not have the top of the dialog further down from the
        // top thn 1/4 the window height.
        topPlacement = Math.min(topPlacement, (winWidth / 4));

        thisPopover.popover.css({
            'top': topPlacement,
            'left': leftPlacement
        });
    } else if (placement === 'window-visible-top') { // Centered at top of visible window
        var winWidth = $(theWindow).width();
        var popoverWidth = thisPopover.popover.width();
        var leftPlacement = ((winWidth - popoverWidth) / 2);

        var topPlacement = 20;  // For tests, which don't have a window
        if (typeof window !== 'undefined') {
            topPlacement = window.scrollY + 20;
        }

        thisPopover.popover.css({
            'top': topPlacement,
            'left': leftPlacement
        });
    } else {
        // default is top
        var onElementOffset = thisPopover.onElement.offset();
        thisPopover.popover.css({
            'top': onElementOffset.top - (thisPopover.popover.height() + 5),
            'left': onElementOffset.left
        });
    }
}

function isPopupVisible(popupNames) {
    var $ = jqProxy.getJQuery();

    if ($.isArray(popupNames)) {
        var activePopovers = $('.cbwf-popover');
        var popoverVisible = false;
        activePopovers.each(function () {
            var thePopover = $(this);
            var popoverName = thePopover.attr('popover-name');
            if (popupNames.indexOf(popoverName) !== -1) {
                popoverVisible = true;
                return false; // terminate the loop
            }
        });
        return popoverVisible;
    } else if (typeof popupNames === 'string') {
        return isPopupVisible(popupNames.split(","));
    }

    return false;
}

},{"../../../jQuery":25,"../../../mvc":37,"../../../util/timeout":42,"window-handle":14}]},{},[38]);
