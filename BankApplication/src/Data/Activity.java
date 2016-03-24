package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Activity {
	private int idActivity;
	private String type;
	private String description;
	private int idEmployee;
	private Date date;
	
	private static Connection connection = connect("jdbc:mysql://localhost:3306/bank?autoReconnect=true&useSSL=false", "root", "admin");
	
	public Activity(int idActivity, String type, String description, int idEmployee, Date date){
		this.idActivity = idActivity;
		this.type = type;
		this.description = description;
		this.idEmployee = idEmployee;
		this.date = date;
		
		this.createActivity(idActivity, type, description, idEmployee, date);
	}

	/**
	 * Method that connects to database
	 * @param name
	 * @param username
	 * @param password
	 * @return
	 */
	private static Connection connect(String name, String username, String password){
		Connection connection = null;
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(name, username, password);
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		return connection;
	}
	
	public static void createActivity(int idActivity, String type, String description, int idEmployee, Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		PreparedStatement pst;
		
		try{
			pst = connection.prepareStatement("INSERT INTO Activity VALUES (" + idActivity + ", " + idEmployee + ", '" + dateFormat.format(date) + "', '" + type + "', '" + description + "');");
			//System.out.println("INSERT INTO Activity VALUES (" + idActivity + ", " + idEmployee + ", '" + dateFormat.format(date) + "', '" + type + "', '" + description + "');");
			pst.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public ArrayList<Activity> findForEmployee(int idEmployee){
		PreparedStatement pst;
		ResultSet rs;
		
		ArrayList<Activity> acts= null;
		
		try{
			pst  = connection.prepareStatement("SELECT * FROM Activity WHERE idUser = " + idEmployee);
			rs = pst.executeQuery();
			
			while(rs.next()){
				int idAct = rs.getInt("idAct");
				String type = rs.getString("typeAct");
				String description = rs.getString("description");
				Date date = rs.getDate("dateAct");
				
				acts.add(new Activity(idAct, type, description, idEmployee, date));
			}
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
		}
		
		return acts;
	}
	
	
}
