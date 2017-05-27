
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.xml.sax.*;

import com.google.gson.Gson;
/**
 * Servlet implementation class test_servlet
 */
@WebServlet("/test_servlet")
public class test_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public test_servlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		try {

			ServletInputStream sin = request.getInputStream();
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String user_name = "", pass_word = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			sin.close();
			String string_search = sb.toString().trim();


			Class.forName("com.mysql.jdbc.Driver");
			Connection test_connection = null;
			test_connection = (Connection) DriverManager
					.getConnection("jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false", "root", "root");

			if (test_connection == null) {
				System.out.println("Not successful");
				System.out.println("Connection not successfull");
			} else {
				System.out.println("Connection Successfull");
			}

			Statement stmt = test_connection.createStatement();
			String search_query = "select * from movies where title like '%" + string_search + "%' order by title;";
			ResultSet rs = stmt.executeQuery(search_query);
			ArrayList<String> list_movies = new ArrayList<String>();
			StringBuilder sb_movies = new StringBuilder();
			String send_output="";
			while (rs.next()) {
				send_output = send_output+rs.getString("title") +",";
				sb_movies.append(rs.getString("title"));
				
				sb_movies.append(",");
				list_movies.add(rs.getString("title"));
			}

				response.setStatus(HttpServletResponse.SC_OK);
				OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
				
				writer.write(send_output);
				writer.flush();
				writer.close();
//				String json = new Gson().toJson(list_movies);
//
//			    response.setContentType("application/json");
//			    response.setCharacterEncoding("UTF-8");
//			    response.getWriter().write(json);
			
		} catch (IOException e) {

			try {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().print(e.getMessage());
				response.getWriter().close();
			} catch (IOException ioe) {
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//doGet(request, response);
	}
}
