<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="AddCustomerTours.aspx.cs" Inherits="TourismWebApp.CustomerTours" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>Add Customer Tours</title>
    <link rel="shortcut icon" type="image/jpg" href="Images/icon.jpg"/>
    <link href="Header.css" rel="stylesheet" />
    <link href="AddCustomerTours.css" rel="stylesheet" />
</head>
<body>
    <form id="form1" runat="server">
        <header class="main-header">
            <h1>Tour SA</h1>
            <nav>
                <ul class="main-nav nav">
                    <li><a href="CustomerDetails.aspx">Customer Details</a></li>
                    <li><a href="Tours.aspx">Tours Schedule</a></li>
                    <li><a href="addCustomerTours.aspx">Add Tours</a></li>
                    <li><a href="TourList.aspx">Tours</a></li>
                </ul>
            </nav>
        </header>
        <table id="addToursTable">
            <tr>
                <td id="labelTourDetails">Tour Details</td>
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
                </td>
            </tr>
        </table>  
    </form>
</body>
</html>
<script src="JQuery/jquery.js"></script>
<script src="AddCustomerTours.js"></script>

