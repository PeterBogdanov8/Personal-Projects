function submitDetails() {
    var o = new Object();
    o.tour = $("#txtTour").val();
    o.amount = $("#txtAmount").val();
    o.date = $("#txtDate").val();
    var jsonString = customJSONstringify(o);
    $.post("AddCustomerTours.asmx/submitDetails",
        {
            json: jsonString,
        },
        function (data) {
            alert(data);
        }).fail(() => {
            alert("Error:Unable to add the tour", "error");
        })
}

function customJSONstringify(obj) {
    return JSON.stringify(obj).replace(/\/Date/g, "\\\/Date").replace(/\)\//g, "\)\\\/")
}

