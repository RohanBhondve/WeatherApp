package WeatherPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class WeatherServlet
 */
public class WeatherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WeatherServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//API Setup
		String inputCity = request.getParameter("city");
		String apiKey = "****";
		String apiurl = "****";
		
		//API Integration
		URL url = new URL(apiurl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		//Reading data from network
		InputStream inputstream = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(inputstream);
		
		//storing data
		StringBuilder responseContent = new StringBuilder();
		Scanner scanner = new Scanner(reader);
		while(scanner.hasNext()) {
			responseContent.append(scanner.nextLine());
		}
		scanner.close();
		
		
		//parsing data into json format
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(responseContent.toString(),JsonObject.class);
		
		
		//retrieving date and time
		String localTime = jsonObject.getAsJsonObject("location").get("localtime").getAsString();
		String[] dateTime = localTime.split(" ");
		String date = dateTime[0];
		String time = dateTime[1];
		
		//retrieving temperature
		double temperature = jsonObject.getAsJsonObject("current").get("temp_c").getAsDouble();
		
		//retrieving humidity
		int humidity = jsonObject.getAsJsonObject("current").get("humidity").getAsInt();
		
		//retrieving windSpeed
		double windSpeed = jsonObject.getAsJsonObject("current").get("wind_kph").getAsDouble();
		
		//retrieving weather condition
//		String weatherCondition = jsonObject.getAsJsonObject("current").get("weather_descriptions").getAsString();
		
		//retrieving pressure
		double pressure = jsonObject.getAsJsonObject("current").get("pressure_mb").getAsDouble();
		
		String icon = jsonObject.getAsJsonObject("current").getAsJsonObject("condition").get("icon").getAsString();
		
		
		//set data as request attribute to send to jsp 
		request.setAttribute("date", date);
		request.setAttribute("time", time);
		request.setAttribute("city", inputCity);
		request.setAttribute("temperature", temperature);
		request.setAttribute("humidity", humidity);
		request.setAttribute("windSpeed", windSpeed);
		request.setAttribute("icon",icon);
//		request.setAttribute("weatherCondition", weatherCondition);
		request.setAttribute("pressure", pressure);
		request.setAttribute("weatherData", responseContent.toString());
		connection.disconnect();
		
		//forward request to index.jsp
		request.getRequestDispatcher("index.jsp").forward(request, response);
		
		
		
		
	}
	

}
