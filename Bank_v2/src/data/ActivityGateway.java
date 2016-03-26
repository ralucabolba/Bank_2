package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sun.rowset.CachedRowSetImpl;

public class ActivityGateway {
	private static Connection connection;
	
	public ActivityGateway(String name, String username, String password){
		connection = connect(name, username, password);
	}
	
	
	private static Connection connect(String name, String username, String password){
		Connection connection = null;
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(name, username, password);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return connection;
	}
	
	private int getMaxActivityId(){
		PreparedStatement pst;
		ResultSet rs;
		
		int id = -1;
		try{
			pst = connection.prepareStatement("SELECT MAX(idAct) AS maxid FROM Activity;");
			rs = pst.executeQuery();
			
			if(rs.next()){
				id = rs.getInt("maxid");
			}
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return id;
	}
	
	public int createActivity(String type, String description, int idEmployee, Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		PreparedStatement pst;
		
		int idActivity = getMaxActivityId() + 1;
		
		try{
			pst = connection.prepareStatement("INSERT INTO Activity VALUES (" + idActivity + ", " + idEmployee + ", '" + dateFormat.format(date) + "', '" + type + "', '" + description + "');");
			pst.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		return idActivity;
	}
	
	public CachedRowSetImpl findForEmployee(int idEmployee, Date from, Date to){
		PreparedStatement pst;
		ResultSet rs;
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		try{
			pst  = connection.prepareStatement("SELECT * FROM Activity WHERE idUser = " + idEmployee + " AND dateAct >= '" + dateFormat.format(from) + "' AND dateAct <= '" + dateFormat.format(to) + "';");
			rs = pst.executeQuery();
			
			CachedRowSetImpl crs = new CachedRowSetImpl();
			crs.populate(rs);
			
			return crs;
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
		}
		
		return null;
	}
	
}
