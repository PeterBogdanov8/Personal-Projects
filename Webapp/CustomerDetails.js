
function submitDetails() {
    var o = new Object();
    o.name = $("#txtName").val();
    o.surname = $("#txtSurname").val();
    o.paymentMethod = $("#txtPayment").val();
    o.tour = $("#txtTour").val();
    o.amount = $("#txtAmount").val();
    o.date = $("#txtDate").val();
    var jsonString = customJSONstringify(o);

    localStorage.tour = $("#txtTour");
    localStorage.amount = $("#txtAmount");
    localStorage.dateElem = $("#txtDate");

    $.post("CustomerDetails.asmx/submitDetails",
        {
            json: jsonString,
        },
        function (data) {
            alert(data);
        }).fail(() => alert("Error: Unable to book the tour", "error"));
}

function customJSONstringify(obj) {
    return JSON.stringify(obj).replace(/\/Date/g, "\\\/Date").replace(/\)\//g, "\)\\\/")
}