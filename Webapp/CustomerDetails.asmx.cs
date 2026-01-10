using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Web;
using System.Web.Script.Serialization;
using System.Web.Script.Services;
using System.Web.Services;

namespace TourismWebApp
{
    /// <summary>
    /// Summary description for CustomerDetails1
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]
    public class CustomerDetails1 : System.Web.Services.WebService
    {

        [WebMethod]
        [ScriptMethod(UseHttpGet = false, ResponseFormat = ResponseFormat.Json)]
        public void submitDetails(String json)
        {
            var javaSriptSerializer = new JavaScriptSerializer();
            javaSriptSerializer.MaxJsonLength = Int32.MaxValue;
            Dictionary<String, Object> obj = javaSriptSerializer.Deserialize<Dictionary<String, Object>>(json);
            string name = Convert.ToString(obj["name"]);
            string surname = Convert.ToString(obj["surname"]);
            string paymentMethod = Convert.ToString(obj["paymentMethod"]);
            string tour = Convert.ToString(obj["tour"]);
            long amount = Convert.ToInt64(obj["amount"]);
            DateTime date = Convert.ToDateTime(Convert.ToString(obj["date"]));
            addTour(name, surname, paymentMethod, tour, amount, date);
            HttpContext.Current.Response.Write("Tour has been successfully booked");
        }

        public void addTour(string name, string surname, string paymentMethod, string tour, long amount, DateTime date)
        {
            SqlConnection connection = new SqlConnection(@"Data Source=(LocalDb)\WebAPIDatabase;Initial Catalog=TourismWebApp;Integrated Security=True;");
            SqlCommand command;
            connection.Open();
            command = new SqlCommand($"insert into TourismWebApp.dbo.customerDetails (firstName, lastName, paymentMethod, tour, amount, tourDate) values('{name}', '{surname}', '{paymentMethod}', '{tour}', {amount}, '{date}')", connection);
            command.ExecuteNonQuery();
        }
    }
}
