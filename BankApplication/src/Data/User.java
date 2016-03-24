package Data;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import com.sun.rowset.CachedRowSetImpl;

public class User {

	protected int idUser;
	protected String username;
	protected String password;
	protected String type;
	
	
	protected static Connection connection = connect("jdbc:mysql://localhost:3306/bank?autoReconnect=true&useSSL=false", "root", "admin");
	
	public User(int idUser, String username, String password, String type){
		this.idUser = idUser;
		this.username = username;
		this.password = password;
		this.type = type;
		
		this.createUser(idUser, username, password, type);
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
			//System.out.println("connect to database");
			
		}catch(Exception e){
			System.out.println("Failed to connect to database");
			//System.out.println(e.getMessage());
		}
		
		return connection;
	}
	
	/**
	 * Method that adds a user in database
	 * @param idUser
	 * @param username
	 * @param password
	 * @param type
	 */
	public void createUser(int idUser, String username, String password, String type){
		
		PreparedStatement pst;
		
		try{
			pst = connection.prepareStatement("INSERT INTO BankUser VALUES (" + idUser + ", '" + username + "', '" + password + "', '" + type + "');");
			pst.executeUpdate();
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Method that updates an user
	 * @param idUser
	 * @param username
	 * @param password
	 * @param type
	 */
	public void updateUser(int idUser, String username, String password, String type){
		String t = type.toString();
		
		PreparedStatement pst;
		
		try{
			pst = connection.prepareStatement("UPDATE BankUser SET "
					+ "								username = '" + username + "', "
					+ "								passwrd = '" + password + "', "
					+ "								typeUser = '" + t + "' WHERE idUser = " + idUser); 
			pst.executeUpdate();
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Method that deletes an user from database
	 * @param idUser
	 */
	public void deleteUser(int idUser){
		PreparedStatement pst;
		
		try{
			pst = connection.prepareStatement("DELETE FROM BankUser WHERE idUser = " + idUser);
			pst.executeUpdate();
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
		}
	}
	
	
	/**
	 * 
	 * @return employees from database
	 */
	public static CachedRowSetImpl getEmployees(){
		PreparedStatement pst;
		ResultSet rs = null;
		CachedRowSetImpl crs = null;
		try {
			crs = new CachedRowSetImpl();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//ArrayList<Employee> ems = new ArrayList<Employee>();
		
		try{
			pst = connection.prepareStatement("SELECT * FROM BankUser WHERE typeUser = 'employee';");
			rs = pst.executeQuery();
			
//			while(rs.next()){
//				int idEm = rs.getInt("idUser");
//				String username = rs.getString("username");
//				String passwrd = rs.getString("passwrd");
//				
//				ems.add(new Employee(idEm, username, passwrd));
//			}
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
		}
		//return ems;
		try {
			crs.populate(rs);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return crs;
	}
	
	
	/**
	 * Method that returns an user
	 * @param idUser
	 * @return
	 */
	public CachedRowSetImpl getEmployee(int idUser){
		PreparedStatement pst;
		ResultSet rs = null;
		
		try{
			pst = connection.prepareStatement("SELECT * FROM BankUser WHERE idUser = " + idUser + ";");
			rs = pst.executeQuery();
			
//			if(rs.next()){
//				String username = rs.getString("username");
//				String password = rs.getString("passwrd");
//			}
			
			CachedRowSetImpl crs = new CachedRowSetImpl();
			crs.populate(rs);
			
			return crs;
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
	}
	/**
	 * 
	 * @param username
	 * @param password
	 * @return the user id based on his authentification information
	 */
	public static int getIdUserByInfo(String username, String password){
		PreparedStatement pst;
		ResultSet rs;
		
		int id = -1;
		
		try{
			pst = connection.prepareStatement("SELECT idUser FROM BankUser WHERE username = '" + username + "' AND passwrd = '" + password + "'");
			rs = pst.executeQuery();
			
			if(rs.next()){
				id = rs.getInt("idUser");
			}
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
		}
		
		return id;
	}
	
	
	public static String getTypeUser(int idUser){
		String type = "";
		PreparedStatement pst;
		ResultSet rs;
		
		try{
			pst = connection.prepareStatement("SELECT typeUser FROM BankUser WHERE idUser = " + idUser);
			rs = pst.executeQuery();
			
			if(rs.next()){
				type = rs.getString("typeUser");
			}
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
		}
		
		return type;
		
	}
	
}
