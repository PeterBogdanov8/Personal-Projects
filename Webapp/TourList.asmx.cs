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
    /// Summary description for TourList1
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]
    public class TourList1 : System.Web.Services.WebService
    {

        [WebMethod]
        [ScriptMethod(UseHttpGet = false, ResponseFormat = ResponseFormat.Json)]
        public void getTourData()
        {
            SqlConnection connection = new SqlConnection(@"Data Source=(LocalDb)\WebAPIDatabase;Initial Catalog=TourismWebApp;Integrated Security=True;");
            SqlCommand command = new SqlCommand();
            command.Connection = connection;
            command.CommandText = "select * from TourismWebApp.dbo.addCustomerTours";

            connection.Open();
            SqlDataReader reader = command.ExecuteReader();

            List<string> entries = new List<string>();
            if (reader.HasRows)
            {
                while (reader.Read())
                {
                    long id = Convert.ToInt64(reader["id"]);
                    string tour = Convert.ToString(reader["tour"]);
                    string amount = Convert.ToString(reader["amount"]);
                    string date = Convert.ToString(reader["date"]);
                    entries.Add($"{id},{tour},{amount},{date}");
                }
                if (entries.Count != 0)
                {
                    HttpContext.Current.Response.Write(String.Join("|", entries));
                }
            }
        }

        [WebMethod]
        [ScriptMethod(UseHttpGet = false, ResponseFormat = ResponseFormat.Json)]
        public void editTour(String json)
        {
            JavaScriptSerializer javaScriptSerializer = new JavaScriptSerializer();
            javaScriptSerializer.MaxJsonLength = Int32.MaxValue;
            Dictionary<String, Object> obj = javaScriptSerializer.Deserialize<Dictionary<String, Object>>(json);
            long id = Convert.ToInt64(obj["id"]);
            string tour = Convert.ToString(obj["tour"]);
            long amount = Convert.ToInt64(obj["amount"]);
            DateTime date = Convert.ToDateTime(obj["date"]);
            editTourDetails(id, tour, amount, date);
            HttpContext.Current.Response.Write("The tour has been successfully edited");
        }

        public void editTourDetails(long id, string tour, long amount, DateTime date)
        {
            SqlConnection connection = new SqlConnection(@"Data Source=(LocalDb)\WebAPIDatabase;Initial Catalog=TourismWebApp;Integrated Security=True;");
            SqlCommand command;
            connection.Open();
            command = new SqlCommand($"update TourismWebApp.dbo.addCustomerTours set tour='{tour}', amount={amount}, date='{date}' where id={id}", connection);
            command.ExecuteNonQuery();
        }

        [WebMethod]
        [ScriptMethod(UseHttpGet = false, ResponseFormat = ResponseFormat.Json)]
        public void deleteTour(String json)
        {
            JavaScriptSerializer javaScriptSerializer = new JavaScriptSerializer();
            javaScriptSerializer.MaxJsonLength = Int32.MaxValue;
            Dictionary<string, Object> obj = javaScriptSerializer.Deserialize<Dictionary<string, Object>>(json);
            long id = Convert.ToInt64(obj["id"]);

            SqlConnection connection = new SqlConnection(@"Data Source=(LocalDb)\WebAPIDatabase;Initial Catalog=TourismWebApp;Integrated Security=True;");
            SqlCommand command;
            connection.Open();
            command = new SqlCommand($"delete from TourismWebApp.dbo.addCustomerTours where id={id}", connection);
            command.ExecuteNonQuery();
            HttpContext.Current.Response.Write("The tour has successfully been deleted");
        }

        [WebMethod]
        [ScriptMethod(UseHttpGet = false, ResponseFormat = ResponseFormat.Json)]
        public void addTour(String json)
        {
            var javaSriptSerializer = new JavaScriptSerializer();
            javaSriptSerializer.MaxJsonLength = Int32.MaxValue;
            Dictionary<String, Object> obj = javaSriptSerializer.Deserialize<Dictionary<String, Object>>(json);
            string tour = Convert.ToString(obj["tour"]);
            long amount = Convert.ToInt64(obj["amount"]);
            DateTime date = Convert.ToDateTime(obj["date"]);
            addTourDetails(tour, amount, date);
            HttpContext.Current.Response.Write("The tour details have been added");
        }

        public void addTourDetails(string tour, long amount, DateTime date)
        {
            SqlConnection connection = new SqlConnection(@"Data Source=(LocalDb)\WebAPIDatabase;Initial Catalog=TourismWebApp;Integrated Security=True;");
            SqlCommand command;
            connection.Open();
            command = new SqlCommand($"insert into TourismWebApp.dbo.addCustomerTours (tour, amount, date) values('{tour}', {amount}, '{date}')", connection);
            command.ExecuteNonQuery();
        }

        [WebMethod]
        [ScriptMethod(UseHttpGet = false, ResponseFormat = ResponseFormat.Json)]
        public void submitCustomerDetails(String json)
        {
            var javaSriptSerializer = new JavaScriptSerializer();
            javaSriptSerializer.MaxJsonLength = Int32.MaxValue;
            Dictionary<String, Object> obj = javaSriptSerializer.Deserialize<Dictionary<String, Object>>(json);
            string name = Convert.ToString(obj["name"]);
            string surname = Convert.ToString(obj["surname"]);
            string paymentMethod = Convert.ToString(obj["paymentMethod"]);
            string tour = Convert.ToString(obj["tour"]);
            long amount = Convert.ToInt64(obj["amount"]);
            long tourId = Convert.ToInt64(obj["tourId"]);
            DateTime date = Convert.ToDateTime(Convert.ToString(obj["date"]));
            addCustomer(name, surname, paymentMethod, tour, amount, date, tourId);
            HttpContext.Current.Response.Write("Tour has been successfully booked");
        }

        public void addCustomer(string name, string surname, string paymentMethod, string tour, long amount, DateTime date, long tourId)
        {
            SqlConnection connection = new SqlConnection(@"Data Source=(LocalDb)\WebAPIDatabase;Initial Catalog=TourismWebApp;Integrated Security=True;");
            SqlCommand command;
            connection.Open();
            command = new SqlCommand($"insert into TourismWebApp.dbo.customerDetails (firstName, lastName, paymentMethod, amount, tourId) values('{name}', '{surname}', '{paymentMethod}', {amount}, {tourId})", connection);
            command.ExecuteNonQuery();
        }

        [WebMethod]
        [ScriptMethod(UseHttpGet = false, ResponseFormat = ResponseFormat.Json)]
        public void search(String json)
        {
            var javaSriptSerializer = new JavaScriptSerializer();
            javaSriptSerializer.MaxJsonLength = Int32.MaxValue;
            Dictionary<String, Object> obj = javaSriptSerializer.Deserialize<Dictionary<String, Object>>(json);
            string search = Convert.ToString(obj["search"]);
            SqlConnection connection = new SqlConnection(@"Data Source=(LocalDb)\WebAPIDatabase;Initial Catalog=TourismWebApp;Integrated Security=True;");
            SqlCommand command = new SqlCommand();
            command.Connection = connection;
            command.CommandText = $"select * from TourismWebApp.dbo.addCustomerTours";

            connection.Open();
            SqlDataReader reader = command.ExecuteReader();

            List<string> entries = new List<string>();
            if (reader.HasRows)
            {
                while (reader.Read())
                {
                    long id = Convert.ToInt64(reader["id"]);
                    string tour = Convert.ToString(reader["tour"]);
                    string amount = Convert.ToString(reader["amount"]);
                    string date = Convert.ToString(reader["date"]);
                    if(tour.Contains(search) || amount.Contains(search) || date.Contains(search))
                    {
                        entries.Add($"{id},{tour},{amount},{date}");
                    }
                }
                if (entries.Count != 0)
                {
                    HttpContext.Current.Response.Write(String.Join("|", entries));
                }
            }
        }
    }
}
