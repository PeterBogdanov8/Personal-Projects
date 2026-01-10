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
    /// Summary description for AddCustomerTours
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]
    public class AddCustomerTours : System.Web.Services.WebService
    {

        [WebMethod]
        [ScriptMethod(UseHttpGet = false, ResponseFormat = ResponseFormat.Json)]
        public void submitDetails(String json)
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
    }
}
