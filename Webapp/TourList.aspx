<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="TourList.aspx.cs" Inherits="TourismWebApp.TourList" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>Tours</title>
    <link rel="shortcut icon" type="image/jpg" href="Images/icon.jpg"/>
    <link href="Header.css" rel="stylesheet" />
    <link href="TourList.css" rel="stylesheet" />
    <link href="Icons.css" rel="stylesheet" />
    <link href="Buttons.css" rel="stylesheet" />
    <link href="Window.css" rel="stylesheet" />
</head>
<body>
    <form id="form1" runat="server">
        <header class="main-header">
            <h1>Tour SA</h1>
            <nav>
                <ul class="main-nav nav">
                    <li><a href="Tours.aspx">Tours Schedule</a></li>
                    <li><a href="TourList.aspx">Tours</a></li>
                </ul>
            </nav>
        </header>
        <div id="tourListGroup">
            <button id="btnAddTour" class="button" onclick="showAddToursWindow(); return false;">Add Tour</button>
            <input id="searchBar" placeholder="Search" />
            <button class="searchIcon" onclick="search(); return false;"></button>
            <table id="tourListTable"></table>
        </div>

        <div id="customerDetailsDiv" class="window hidden">
            <label>Customer Details</label>
            <span id="closeCustomerDetailsDiv" class="closeButton" onclick="closeCustomerDetailsWindow()">x</span>
            <table id="customerTable">
                <tr>
                    <td>Name</td>
                    <td><input type="text" id="txtName"/></td>
                </tr>
                <tr>
                    <td>Surname</td>
                    <td><input type="text" id="txtSurname"/></td>
                </tr>
                <tr>
                    <td>Payment Method</td>
                    <td>
                        <select name="paymentMethod" id="txtPayment">
                            <option value="Card">Card</option>
                            <option value="Cash">Cash</option>
                            <option value="Other">Other</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td class="hidden">Tour</td>
                    <td><input type="text" id="txtTour" class="hidden"/></td>
                </tr>
                <tr>
                    <td class="hidden">Amount</td>
                    <td><input type="number" id="txtAmount" class="hidden"/></td>
                </tr>
                <tr>
                    <td class="hidden">Date</td>
                    <td><input type="date" id="txtDate" class="hidden"/></td>
                </tr>
                <tr>
                    <td class="hidden">Tour ID</td>
                    <td><input type="text" id="txtTourID" class="hidden"/></td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <input type="button" id="btnSubmit" class="button" value="Submit" onclick="submitCustomerDetails();"/>
                        <input type="button" id="btnCancel" class="button" value="Cancel" onclick="closeCustomerDetailsWindow()"/>
                    </td>
                </tr>
            </table>  
        </div>

        <div id="toursDiv" class="window hidden">
            <label id="labelTourDetails">Tour Details</label>
            <span id= "closeToursDiv" class="closeButton" onclick="closeToursWindow()">x</span>
            <table>
                <tr class="hidden">
                    <td>ID</td>
                    <td><input type="text" id="txtID"/></td>
                </tr>
                <tr>
                    <td>Tour</td>
                    <td><input type="text" id="txtTourName"/></td>
                </tr>
                <tr>
                    <td>Amount</td>
                    <td><input type="number" id="txtTourAmount"/></td>
                </tr>
                <tr>
                    <td>Date</td>
                    <td><input type="date" id="txtTourDate"/></td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <input type="button" id="btnEdit" class="button" value="Edit" onclick="editTour();"/>
                        <input type="button" id="btnCancelEdit" class="button" value="Cancel" onclick="closeToursWindow()"/>
                    </td>
                </tr>
            </table>
        </div>
        
        <div id="addToursWindow" class="window hidden">
            <label id="addTourDetailsLabel">Tour Details</label>
            <span id= "closeAddToursWindow" class="closeButton" onclick="closeAddTourWindow()">x</span>
            <table>
                <tr>
                    <td>Tour</td>
                    <td><input type="text" id="txtNewTour"/></td>
                </tr>
                <tr>
                    <td>Amount</td>
                    <td><input type="number" id="txtNewAmount"/></td>
                </tr>
                <tr>
                    <td>Date</td>
                    <td><input type="date" id="txtNewDate"/></td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <input type="button" id="btnAddNewTour" value="Add Tour" class="button" onclick="addTour()"/>
                        <input type="button" id="btnCancelAdd" class="button" value="Cancel" onclick="closeAddTourWindow()"/>
                    </td>
                </tr>
            </table>
        </div>
    </form>
</body>
</html>
<script src="JQuery/jquery.js"></script>
<script src="TourList.js"></script>
