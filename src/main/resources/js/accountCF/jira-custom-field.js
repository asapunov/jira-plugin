import datepicker from 'js-datepicker'
let i = 1;
function createInput(){
    let a = "${field_uid}";
    i++;
    var $input = $("<tr>" + "<td>" + "<input  type=\"text\" value=\"\" name=\"" + a + "\" id=\"" + a + " new_f" + i + "\" /> "  +
        "<td>" + "<input  type=\"text\" value=\"\" name=\"" + a + "\" id=\"" + a + " new_s" + i + "\" /> " +
        "<td>" + "<input  type=\"text\" value=\"\" name=\"" + a + "\" id=\"" + a + " new_t" + i + "\" /> " +
        "<td>" + "<input  type=\"text\" value=\"\" name=\"" + a + "\" id=\"" + a + " new_f" + i + "\" /> "+ "</tr>");
    $input.appendTo('#mainTab');
}
export const picker = datepicker("#datepicker")
