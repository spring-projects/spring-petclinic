var InlineWarning = (function () {
    'use strict';
    var exports = {};
    var options = {
        id: '',    // id of element to bind
        url: '',   // url of check method
        input: ''  // checkbox to test for checked
    };

    exports.setup = function (opts) {
        options = opts;
        return exports;
    };

    exports.start = function () {
        // Ignore when GH trigger unchecked
        if (!$$(options.input).first().checked) {
            return;
        }
        new Ajax.PeriodicalUpdater(
            options.id,
            options.url,
            {
                method: 'get',
                frequency: 10,
                decay: 2
            }
        );
    };

    return exports;
})();