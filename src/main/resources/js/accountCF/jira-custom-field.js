let i = 1;
function createInput(){
    let a = "${field_uid}";
    i++;
    var $input = $("<tr>" + "<td>" + "<input  type=\"text\" value=\"\" name=\"" + a + "\" id=\"" + a + " new_f" + i + "\" /> "  +
        " #datePopup( " + a + " new_f" + i + " \"  $dateTimeFormat $dateFormat $timeFormat $dateTimePicker $currentMillis $currentCalendar)" +
        "<td>" + "<input  type=\"text\" value=\"\" name=\"" + a + "\" id=\"" + a + " new_s" + i + "\" /> " +
        "<td>" + "<input  type=\"text\" value=\"\" name=\"" + a + "\" id=\"" + a + " new_t" + i + "\" /> " +
        " #datePopup( " + a + " new_t" + i + "   $dateTimeFormat $dateFormat $timeFormat $dateTimePicker $currentMillis $currentCalendar)" +
        "<td>" + "<input  type=\"text\" value=\"\" name=\"" + a + "\" id=\"" + a + " new_f" + i + "\" /> "+ "</tr>");
    $input.appendTo('#mainTab');
}
