function getTourData() {
    let colNames = document.createElement("tr");
    colNames.innerHTML =
        "<th class='hidden'> ID</th>"
        + "<th>Tour</th>"
        + "<th>Amount</th>"
        + "<th>Date</th>"
        + "<th>Add Client</th>"
        + "<th>Edit Tour</th>"
        //+ "<th>Delete Tour</th>";
    $("#tourListTable").append(colNames);
    $.post("TourList.asmx/getTourData", {},
        function (data) {
            var entries = data.split("|");
            for (let i = 0; i < entries.length; i++) {
                let row = document.createElement("tr");
                row.id = "Tour" + 1;
                let entry = entries[i].split(",");
                let id = entry[0];
                let tour = entry[1];
                let amount = entry[2];
                let date = entry[3].split(" ")[0];
                row.innerHTML =
                "<td class='hidden'>" +id +"</td>"
                + "<td  id='" + tour + i + "' >" + tour + "</td>"
                + "<td>" + amount + "</td>"
                + "<td>" + date + "</td>"
                + "<td id='clientAdder" + i + "' class='addIcon'></td>"
                + "<td id='tourEditor" +i +"' class='editIcon'> </td>"
                //+"<td id='tourDeleter" +i +"' class='deleteIcon'> </td>"

                $("#tourListTable").append(row);
                document.getElementById("clientAdder" + i).addEventListener("click", () => {
                    $("#customerDetailsDiv").toggleClass("hidden");
                    $("#txtTour").val(tour);
                    $("#txtAmount").val(amount);
                    $("#txtDate").val(date.replaceAll("/", "-"));
                    $('#txtTourID').val(id);
                });
                showTourEditWindow("tourEditor" + i, id, tour, amount, date);
                //deleteTour("tourDeleter" + i, id);
            }
        }
    ).fail(() => alert("Error: Unable to get the Tour data"))
}

function closeCustomerDetailsWindow() {
    $("#customerDetailsDiv").addClass("hidden");
}

function closeToursWindow() {
    $("#toursDiv").addClass("hidden");
}

function showTourEditWindow(elemId, id, tour, amount, date) {
    document.getElementById(elemId).addEventListener("click", () => {
        $("#txtID").val(id);
        $("#txtTourName").val(tour);
        $("#txtTourAmount").val(amount);
        $("#txtTourDate").val(date.replaceAll("/", "-"));
        $("#toursDiv").toggleClass("hidden");
    });
}

function editTour() {
    let o = new Object();
    o.id = $("#txtID").val();
    o.tour = $("#txtTourName").val();
    o.amount = $("#txtTourAmount").val();
    o.date = $("#txtTourDate").val();
    let jsonString = customJSONstringify(o);
    $("#toursDiv").toggleClass("hidden");

    $.post("TourList.asmx/editTour", {
        json: jsonString
    },
        function (data) {
            $("#tourListTable").empty();
            getTourData();
            alert(data);
        }).fail(() => alert("Error: Unable to edit tour", "error"));
}

function deleteTour(elemId, id) {
    document.getElementById(elemId).addEventListener("click", () => {
        let obj = new Object();
        obj.id = id;
        let jsonString = customJSONstringify(obj);
        $.post("TourList.asmx/deleteTour",
            {
                json: jsonString
            },
            (data) => {
                $("#tourListTable").empty();
                getTourData();
                alert(data);
            });
    });
}

function addTour() {
    var o = new Object();
    o.tour = $("#txtNewTour").val();
    o.amount = $("#txtNewAmount").val();
    o.date = $("#txtNewDate").val();
    var jsonString = customJSONstringify(o);
    $.post("TourList.asmx/addTour",
        {
            json: jsonString,
        },
        function (data) {
            $("#tourListTable").empty();
            getTourData();
            closeAddTourWindow();
            $("#txtNewTour").val("");
            $("#txtNewAmount").val("");
            $("#txtNewDate").val("");
            alert(data);
        }).fail(() => {
            alert("Error:Unable to add the tour", "error");
        })
}

function showAddToursWindow() {
    $("#addToursWindow").toggleClass("hidden");
}

function closeAddTourWindow() {
    $("#addToursWindow").addClass("hidden");
}

function submitCustomerDetails() {
    var o = new Object();
    o.name = $("#txtName").val();
    o.surname = $("#txtSurname").val();
    o.paymentMethod = $("#txtPayment").val();
    o.tour = $("#txtTour").val();
    o.amount = $("#txtAmount").val();
    o.date = $("#txtDate").val();
    o.tourId = $('#txtTourID').val();
    var jsonString = customJSONstringify(o);

    localStorage.tour = $("#txtTour");
    localStorage.amount = $("#txtAmount");
    localStorage.dateElem = $("#txtDate");

    closeCustomerDetailsWindow();

    $.post("TourList.asmx/submitCustomerDetails",
        {
            json: jsonString,
        },
        function (data) {
            $("#txtName").val("");
            $("#txtSurname").val("");
            $("#txtPayment").val("");
            alert(data);
        }).fail(() => alert("Error: Unable to book the tour", "error"));
}

function search() {
    let o = new Object();
    o.search = $("#searchBar").val();
    let jsonString = customJSONstringify(o);
    $.post("TourList.asmx/search", {
        json: jsonString
    }, (data) => {
        $("#tourListTable").empty();
        let colNames = document.createElement("tr");
        colNames.innerHTML =
            "<th class='hidden'> ID</th>"
            + "<th>Tour</th>"
            + "<th>Amount</th>"
            + "<th>Date</th>"
            + "<th>Add Client</th>"
            + "<th>Edit Tour</th>"
        //+ "<th>Delete Tour</th>";
        $("#tourListTable").append(colNames);
                var entries = data.split("|");
                for (let i = 0; i < entries.length; i++) {
                    let row = document.createElement("tr");
                    row.id = "Tour" + 1;
                    let entry = entries[i].split(",");
                    let id = entry[0];
                    let tour = entry[1];
                    let amount = entry[2];
                    let date = entry[3].split(" ")[0];
                    row.innerHTML =
                        "<td class='hidden'>" + id + "</td>"
                        + "<td  id='" + tour + i + "' >" + tour + "</td>"
                        + "<td>" + amount + "</td>"
                        + "<td>" + date + "</td>"
                        + "<td id='clientAdder" + i + "' class='addIcon'></td>"
                        + "<td id='tourEditor" + i + "' class='editIcon'> </td>"
                    //+"<td id='tourDeleter" +i +"' class='deleteIcon'> </td>"
                    $("#tourListTable").append(row);
                    document.getElementById("clientAdder" + i).addEventListener("click", () => {
                        $("#customerDetailsDiv").toggleClass("hidden");
                        $("#txtTour").val(tour);
                        $("#txtAmount").val(amount);
                        $("#txtDate").val(date.replaceAll("/", "-"));
                        $('#txtTourID').val(id);
                    });
                    showTourEditWindow("tourEditor" + i, id, tour, amount, date);
                    //deleteTour("tourDeleter" + i, id);
                }
            }).fail(() => {
                alert("Unable to search for result");
            });
}

function customJSONstringify(obj) {
    return JSON.stringify(obj).replace(/\/Date/g, "\\\/Date").replace(/\)\//g, "\)\\\/")
}

getTourData();