/*global cordova, module*/
module.exports = {
    startMeasure: function (win, fail) {
        cordova.exec(win, fail, "Thermodo", "start_measure", []);
    },
    stopMeasure: function (win, fail) {
        cordova.exec(win, fail, "Thermodo", "stop_measure", []);
    }
};