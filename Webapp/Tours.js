function getCustomerData() {
    let colNames = document.createElement("tr");
    colNames.innerHTML =
        "<th class='hidden'>ID</th>"
        + "<th>Name</th>"
        + "<th>Surname</th>"
        + "<th>Payment Method</th>"
        + "<th>Tour</th>"
        + "<th>Amount</th>"
        + "<th>Date</th>"
        + "<th>Edit Customer</th>"
        + "<th>Delete Customer</th>";
    $("#tourTable").append(colNames);
    $.post("Tours.asmx/getCustomerData", {},
        function (data) {
            var entries = data.split("|");
            for (let i = 0; i < entries.length; i++) {
                var row = document.createElement("tr");
                row.id = "Tour" + 1;
                var entry = entries[i].split(",");

                let id = entry[0];
                let name = entry[1];
                let surname = entry[2];
                let paymentMethod = entry[3];
                let tour = entry[4];;
                let amount = entry[5];
                let date = entry[6].split(" ")[0];

                row.innerHTML =
                    "<td class='hidden'>" + id + "</td>"
                    + "<td>" + name + "</td>"
                    + "<td>" + surname + "</td>"
                    + "<td>" + paymentMethod + "</td>"
                    + "<td>" + tour + "</td>"
                    + "<td>" + amount + "</td>"
                    +"<td>" +date + "</td>"
                    + "<td id='clientEditor" +i +"' class='editIcon'> </td>"
                    + "<td id='clientDeleter" + i +"' class='deleteIcon'> </td>"
                $("#tourTable").append(row);

                showEditWindow("clientEditor" + i, id, name, surname, paymentMethod, tour, amount, date);
                deleteClient("clientDeleter" + i, id);
            }   
        }
    ).fail(() => alert("Error: Unable to get the Customer data for the tours"))
}

function showEditWindow(elemId, id, name, surname, paymentMethod, tour, amount, date) {
    document.getElementById(elemId).addEventListener("click", () => {
        $("#txtID").val(id);
        $("#txtName").val(name);
        $("#txtSurname").val(surname);
        $("#txtPayment").val(paymentMethod);
        $("#txtTour").val(tour);
        $("#txtAmount").val(amount);
        $("#txtDate").val(date.replaceAll("/", "-"));
        $("#editCustomerWindow").toggleClass("hidden");
    });
}

function hideEditWindow() {
    $("#editCustomerWindow").addClass("hidden");
}

function editClient() {
    let o = new Object();
    o.id = $("#txtID").val();
    o.name = $("#txtName").val();
    o.surname = $("#txtSurname").val();
    o.paymentMethod = $("#txtPayment").val();
    o.tour = $("#txtTour").val();
    o.amount = $("#txtAmount").val();
    o.date = $("#txtDate").val();
    var jsonString = customJSONstringify(o);
    $("#editCustomerWindow").toggleClass("hidden");
    $.post("Tours.asmx/editClient",
        {
            json: jsonString
        },
        (data) => {
            hideEditWindow();
            $("#tourTable").empty();
            getCustomerData();
            alert(data);
        }).fail(() => {

            alert("Error: Unable to edit customer", "error")
        });
}

function deleteClient(elemId, id) {
    document.getElementById(elemId).addEventListener("click", () => {
        let obj = new Object();
        obj.id = id;
        let jsonString = customJSONstringify(obj);
        $.post("Tours.asmx/deleteClient",
            {
                json: jsonString
            },
            (data) => {
                $("#tourTable").empty();
                getCustomerData();
                alert(data);
            });
    });
}

function addCustomer() {
    var o = new Object();
    o.name = $("#txtNewName").val();
    o.surname = $("#txtNewSurname").val();
    o.paymentMethod = $("#txtNewPayment").val();
    o.tour = $("#txtNewTour").val();
    o.amount = $("#txtNewAmount").val();
    o.date = $("#txtNewDate").val();
    var jsonString = customJSONstringify(o);

    localStorage.tour = $("#txtTour");
    localStorage.amount = $("#txtAmount");
    localStorage.dateElem = $("#txtDate");

    $.post("Tours.asmx/addCustomer",
        {
            json: jsonString,
        },
        function (data) {
            $("#tourTable").empty();
            getCustomerData();
            hideAddCustomerWindow();
            $("#txtNewName").val("");
            $("#txtNewSurname").val("");
            $("#txtNewPayment").val("Card");
            $("#txtNewTour").val("");
            $("#txtNewAmount").val("");
            $("#txtNewDate").val("");
            o.date = $("#txtNewDate").val();
            alert(data);
        }).fail(() => alert("Error: Unable to book the tour", "error"));
}

function hideAddCustomerWindow() {
    $("#addCustomerWindow").addClass("hidden");
}

function showAddCustomerWindow() {
    $("#addCustomerWindow").toggleClass("hidden");
}

function search() {
    let o = new Object();
    o.search = $("#searchBar").val();
    let jsonString = customJSONstringify(o);

    $.post("Tours.asmx/search", {
        json: jsonString
    }, (data) => {
        $("#tourTable").empty();
        let colNames = document.createElement("tr");
        colNames.innerHTML =
            "<th class='hidden'>ID</th>"
            + "<th>Name</th>"
            + "<th>Surname</th>"
            + "<th>Payment Method</th>"
            + "<th>Tour</th>"
            + "<th>Amount</th>"
            + "<th>Date</th>"
            + "<th>Edit Customer</th>"
            + "<th>Delete Customer</th>";
        $("#tourTable").append(colNames);
        var entries = data.split("|");
        for (let i = 0; i < entries.length; i++) {
            var row = document.createElement("tr");
            row.id = "Tour" + 1;
            var entry = entries[i].split(",");

            let id = entry[0];
            let name = entry[1];
            let surname = entry[2];
            let paymentMethod = entry[3];
            let tour = entry[4];;
            let amount = entry[5];
            let date = entry[6].split(" ")[0];

            row.innerHTML =
                "<td class='hidden'>" + id + "</td>"
                + "<td>" + name + "</td>"
                + "<td>" + surname + "</td>"
                + "<td>" + paymentMethod + "</td>"
                + "<td>" + tour + "</td>"
                + "<td>" + amount + "</td>"
                + "<td>" + date + "</td>"
                + "<td id='clientEditor" + i + "' class='editIcon'> </td>"
                + "<td id='clientDeleter" + i + "' class='deleteIcon'> </td>"
            $("#tourTable").append(row);

            showEditWindow("clientEditor" + i, id, name, surname, paymentMethod, tour, amount, date);
            deleteClient("clientDeleter" + i, id);
        }
    }).fail(() => {
        alert("Unable to search","error")
    });
}

function customJSONstringify(obj) {
    return JSON.stringify(obj).replace(/\/Date/g, "\\\/Date").replace(/\)\//g, "\)\\\/")
}

getCustomerData();