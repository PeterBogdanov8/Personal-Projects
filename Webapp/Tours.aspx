<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Tours.aspx.cs" Inherits="TourismWebApp.Tours" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>Tours</title>
    <link rel="shortcut icon" type="image/jpg" href="Images/icon.jpg"/>
    <link href="Tours.css" rel="stylesheet" />
    <link href="Header.css" rel="stylesheet" />
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

        <div id="customerList">
            <button  id="btnAddCustomer" class="button" onclick="showAddCustomerWindow(); return false;">Add Customer</button>
            <input id="searchBar" placeholder="Search" />
            <button class="searchIcon" onclick="search(); return false;"></button>
            <table id="tourTable"></table>
        </div>
        
        <div id="editCustomerWindow" class="hidden window">
            <label id="labelCustomerDetails">Customer Details</label>
            <span id="closeEditWindow" class="closeButton" onclick="hideEditWindow()">x</span>
            <table>
                <tr>
                    <td class="hidden">ID</td>
                    <td class="hidden"><input type="text" id="txtID"/></td>
                </tr>
                <tr>
                    <td>Name</td>
                    <td><input type="text" id="txtName"/></td>
                </tr>
                <tr>
                    <td>Surname</td>
                    <td><input type="text" id="txtSurname"/></td>
                </tr>
                <tr>
                    <td class="hidden">Payment Method</td>
                    <td class="hidden">
                        <select name="paymentMethod" id="txtPayment">
                            <option value="Card">Card</option>
                            <option value="Cash">Cash</option>
                            <option value="Other">Other</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td class="hidden">Tour</td>
                    <td class="hidden"><input type="text" id="txtTour"/></td>
                </tr>
                <tr>
                    <td class="hidden">Amount</td>
                    <td class="hidden"><input type="number" id="txtAmount"/></td>
                </tr>
                <tr>
                    <td class="hidden">Date</td>
                    <td class="hidden"><input type="date" id="txtDate"/></td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <input type="button" id="btnSubmit" class="button" value="Edit" onclick="editClient();"/>
                        <input type="button" id="btnCancel" class="button" value="Cancel" onclick="hideEditWindow()"/>
                    </td>
                </tr>
            </table>  
        </div>

        <div id="addCustomerWindow" class="window hidden">
            <label id="addCustomerDetails">Customer Details</label>
            <span id="closeAddCustomerWindow" class="closeButton" onclick="hideAddCustomerWindow()">x</span>
            <table id="customerTable">
                <tr>
                    <td>Name</td>
                    <td><input type="text" id="txtNewName"/></td>
                </tr>
                <tr>
                    <td>Surname</td>
                    <td><input type="text" id="txtNewSurname"/></td>
                </tr>
                <tr>
                    <td>Payment Method</td>
                    <td>
                        <select name="paymentMethod" id="txtNewPayment">
                            <option value="Card">Card</option>
                            <option value="Cash">Cash</option>
                            <option value="Other">Other</option>
                        </select>
                    </td>
                </tr>
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
                        <input type="button" id="btnAddNewCustomer" class="button" value="Submit" onclick="addCustomer();"/>
                        <input type="button" id="btnCancelAdd" class="button" value="Cancel" onclick="hideAddCustomerWindow()"/>
                    </td>
                </tr>
            </table>
        </div>
    </form>
</body>
</html>
<script src="JQuery/jquery.js"></script>
<script src="Tours.js"></script>
