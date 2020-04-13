// We match the end of the name using $= because the beginning of the name is 
// dynamically generated to avoid name clashes.
Behaviour.specify("input[name$=_configuredByUrlRadio]", 'GitHubSCMSourceRadioConfiguration', 0, function(e) {
    if (e.gitHubSCMSourceRadioConfiguration) {
        return;
    }

    var getNthParent = function(e, n) {
        while (n > 0) {
            if (e.parentNode) {
                e = e.parentNode;
                n--;
            } else {
                return null;
            }
        }
        return e;
    }

    // Todo: Replace with a query selector?
    var findNeighboringDynamicInput = function(e) {
        var inputTbody = getNthParent(e, 4 /*tbody > tr > td > label > input*/);
        if (inputTbody) {
            // input hidden is always in the 4th position
            var hiddenBlock = inputTbody.childNodes[4].firstElementChild.firstElementChild
            return hiddenBlock
        }
    }

    var neighboringDynamicInput = findNeighboringDynamicInput(e);
    if (neighboringDynamicInput) {
        e.onclick = function() {
            neighboringDynamicInput.value = e.value;
            // When changing to true the event is triggered.
            if(e.value == "false"){
                // When changing to false a trigger has to be fired in order to fetch the repos from the backend
                if (document.createEvent) {
                    oEvent = document.createEvent("HTMLEvents");
                    oEvent.initEvent("change");
                    // Gets the first Jelly entry afte the hidden value
                    var repoOwner = getNthParent(e, 3).nextElementSibling.nextElementSibling.childNodes[2].firstElementChild
                    // if the first entry is a select for API URI, gets the following one (each Jelly entry has 3 elements)
                    if (repoOwner == null || repoOwner.tagName == "SELECT"){
                        repoOwner = getNthParent(e, 3).nextElementSibling.nextElementSibling.nextElementSibling.nextElementSibling.nextElementSibling.childNodes[2].firstElementChild
                    }
                    if( repoOwner != null) {
                        // fire a onchange event on the repoOwner input test to get the repos from backend
                        repoOwner.dispatchEvent(oEvent);
                    }
                }
            }
        };
    }
    e.gitHubSCMSourceRadioConfiguration = true;
});
