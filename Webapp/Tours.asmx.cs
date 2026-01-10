using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Script.Services;
using System.Web.Services;
using System.Data.SqlClient;
using System.Web.Script.Serialization;

namespace TourismWebApp
{
    /// <summary>
    /// Summary description for Tours1
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]
    public class Tours1 : System.Web.Services.WebService
    {

        [WebMethod]
        [ScriptMethod(UseHttpGet = false, ResponseFormat = ResponseFormat.Json)]
        public void getCustomerData()
        {
            SqlConnection connection = new SqlConnection(@"Data Source=(LocalDb)\WebAPIDatabase;Initial Catalog=TourismWebApp;Integrated Security=True;");
            SqlCommand command = new SqlCommand();
            command.Connection = connection;
            command.CommandText = "select a.id, a.firstName, a.lastName, a.paymentMethod, b.tour, a.amount, b.date from TourismWebApp.dbo.customerDetails a inner join TourismWebApp.dbo.addCustomerTours b on a.tourId = b.id";
            
            connection.Open();
            SqlDataReader reader = command.ExecuteReader();

            List<string> entries = new List<string>();
            if(reader.HasRows)
            {
                while(reader.Read())
                {
                    long id = Convert.ToInt64(reader["id"]);
                    string name = Convert.ToString(reader["firstName"]);
                    string surname = Convert.ToString(reader["lastName"]);
                    string paymentMethod = Convert.ToString(reader["paymentMethod"]);
                    string tour = Convert.ToString(reader["tour"]);
                    string amount = Convert.ToString(reader["amount"]);
                    string date = Convert.ToString(reader["date"]);
                    entries.Add($"{id},{name},{surname},{paymentMethod},{tour},{amount},{date}");
                }
                if(entries.Count != 0)
                {
                    // Attributes of a customer are separated by a comma (,) and customers are seperated by a pipe (|)
                    HttpContext.Current.Response.Write(String.Join("|", entries));
                }
            }
        }

        [WebMethod]
        [ScriptMethod(UseHttpGet = false, ResponseFormat = ResponseFormat.Json)]
        public void deleteClient(String json)
        {
            JavaScriptSerializer javaScriptSerializer = new JavaScriptSerializer();
            javaScriptSerializer.MaxJsonLength = Int32.MaxValue;
            Dictionary<string, Object> obj = javaScriptSerializer.Deserialize<Dictionary<string, Object>>(json);
            long id = Convert.ToInt64(obj["id"]);

            SqlConnection connection = new SqlConnection(@"Data Source=(LocalDb)\WebAPIDatabase;Initial Catalog=TourismWebApp;Integrated Security=True;");
            SqlCommand command;
            connection.Open();
            command = new SqlCommand($"delete from TourismWebApp.dbo.customerDetails where id={id}", connection);
            command.ExecuteNonQuery();
            HttpContext.Current.Response.Write("The tour has successfully been deleted");
        }

        [WebMethod]
        [ScriptMethod(UseHttpGet = false, ResponseFormat = ResponseFormat.Json)]
        public void editClient(String json)
        {
            JavaScriptSerializer javaScriptSerializer = new JavaScriptSerializer();
            javaScriptSerializer.MaxJsonLength = Int32.MaxValue;
            Dictionary<string, Object> obj = javaScriptSerializer.Deserialize<Dictionary<string, Object>>(json);
            long id = Convert.ToInt64(obj["id"]);
            string name = Convert.ToString(obj["name"]);
            string surname = Convert.ToString(obj["surname"]);
            string paymentMethod = Convert.ToString(obj["paymentMethod"]);
            string tour = Convert.ToString(obj["tour"]);
            long amount = Convert.ToInt64(obj["amount"]);
            DateTime date = Convert.ToDateTime(obj["date"]);

            editCustomerDetails(id, name, surname, paymentMethod, tour, amount, date);
            HttpContext.Current.Response.Write("Customer has been successfully edited");
        }

        public void editCustomerDetails(long id, string name, string surname, string paymentMethod, string tour, long amount, DateTime date)
        {
            SqlConnection connection = new SqlConnection(@"Data Source=(LocalDb)\WebAPIDatabase;Initial Catalog=TourismWebApp;Integrated Security=True;");
            SqlCommand command;
            connection.Open();
            command = new SqlCommand($"update TourismWebApp.dbo.customerDetails set firstName='{name}', lastName='{surname}', paymentMethod='{paymentMethod}', amount={amount} where id={id}", connection);
            command.ExecuteNonQuery();
        }

        [WebMethod]
        [ScriptMethod(UseHttpGet = false, ResponseFormat = ResponseFormat.Json)]
        public void addCustomer(String json)
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
            addCustomerDetails(name, surname, paymentMethod, tour, amount, date);
            HttpContext.Current.Response.Write("Tour has been successfully booked");
        }

        public void addCustomerDetails(string name, string surname, string paymentMethod, string tour, long amount, DateTime date)
        {
            SqlConnection connection = new SqlConnection(@"Data Source=(LocalDb)\WebAPIDatabase;Initial Catalog=TourismWebApp;Integrated Security=True;");
            SqlCommand command;
            connection.Open();
            command = new SqlCommand($"insert into TourismWebApp.dbo.addCustomerTours (tour, amount, date) values('{tour}', {amount}, '{date}')", connection);
            command.ExecuteNonQuery();

            command.Connection = connection;
            command.CommandText = $"select top 1 id from TourismWebApp.dbo.addCustomerTours where tour='{tour}' and amount={amount} and date='{date}'";
            SqlDataReader reader = command.ExecuteReader();

            long tourId = 0;
            if (reader.HasRows)
            {
                while (reader.Read())
                {
                    tourId = Convert.ToInt64(reader["id"]);

                }
            }
            reader.Close();
            SqlCommand command2 = new SqlCommand($"insert into TourismWebApp.dbo.customerDetails (firstName, lastName, paymentMethod, amount, tourId) values('{name}', '{surname}', '{paymentMethod}', {amount}, {tourId})", connection);
            command2.ExecuteNonQuery();
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
            command.CommandText = "select a.id, a.firstName, a.lastName, a.paymentMethod, b.tour, a.amount, b.date from TourismWebApp.dbo.customerDetails a inner join TourismWebApp.dbo.addCustomerTours b on a.tourId = b.id";

            connection.Open();
            SqlDataReader reader = command.ExecuteReader();

            List<string> entries = new List<string>();
            if (reader.HasRows)
            {
                while (reader.Read())
                {
                    long id = Convert.ToInt64(reader["id"]);
                    string name = Convert.ToString(reader["firstName"]);
                    string surname = Convert.ToString(reader["lastName"]);
                    string paymentMethod = Convert.ToString(reader["paymentMethod"]);
                    string tour = Convert.ToString(reader["tour"]);
                    string amount = Convert.ToString(reader["amount"]);
                    string date = Convert.ToString(reader["date"]);
                    if(name.Contains(search) || surname.Contains(search) || paymentMethod.Contains(search) || 
                        tour.Contains(search) || amount.Contains(search) || date.Contains(search))
                    {
                        entries.Add($"{id},{name},{surname},{paymentMethod},{tour},{amount},{date}");
                    }  
                }
                if (entries.Count != 0)
                {
                    // Attributes of a customer are separated by a comma (,) and customers are seperated by a pipe (|)
                    HttpContext.Current.Response.Write(String.Join("|", entries));
                }
            }
        }
    }
}
