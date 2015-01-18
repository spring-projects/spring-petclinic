/*!
 angularAMD v0.2.1
 (c) 2013-2014 Marcos Lin https://github.com/marcoslin/
 License: MIT
*/
define({
    load: function (name, req, onload) {
        'use strict';
        //console.log("ngamd loaded: ", req.toUrl(name));
        req(['angularAMD', name], function (angularAMD, value) {
            //console.log("Processing queues.");
            angularAMD.processQueue();
            onload(value);
        });
    }
});


