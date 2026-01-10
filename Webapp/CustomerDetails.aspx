<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="CustomerDetails.aspx.cs" Inherits="TourismWebApp.CustomerDetails" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>Customer Details</title>
    <link rel="shortcut icon" type="image/jpg" href="Images/icon.jpg"/>
    <link href="CustomerDetails.css" type="text/css" rel="stylesheet"/>
    <link href="Header.css" rel="stylesheet" />
</head>
<body>
    <form id="form1" runat="server">
        <header class="main-header">
            <h1>Tour SA</h1>
            <nav>
                <ul class="main-nav nav">
                    <li><a href="CustomerDetails.aspx">Customer Details</a></li>
                    <li><a href="Tours.aspx">Tours Schedule</a></li>
                    <li><a href="TourList.aspx">Tours</a></li>
                </ul>
            </nav>
        </header>
        <table id="customerTable">
            <tr>
                <td id="labelCustomerDetails">Customer Details</td>
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
                <td>Payment Method</td>
                <td><input type="text" id="txtPayment"/></td>
            </tr>
            <tr>
                <td>Tour</td>
                <td><input type="text" id="txtTour"/></td>
            </tr>
            <tr>
                <td>Amount</td>
                <td><input type="text" id="txtAmount"/></td>
            </tr>
            <tr>
                <td>Date</td>
                <td><input type="date" id="txtDate"/></td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <input type="button" id="btnSubmit" value="Submit" onclick="submitDetails();"/>
                    <input type="button" id="btnCancel" value="Cancel" onclick=""/>
                </td>
            </tr>
        </table>  
    </form>
</body>
</html>
<script src="JQuery/jquery.js"></script>
<script src="CustomerDetails.js"></script>