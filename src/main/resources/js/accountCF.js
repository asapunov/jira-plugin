AJS.$(document).change(function() {
    AJS.$('#date').datePicker({
        'overrideBrowserDefault': true,
        'languageCode': 'ru'
    });
});
AJS.$(document).ready(function() {
    AJS.$('#demo-range-always').datePicker({'overrideBrowserDefault': true});
});