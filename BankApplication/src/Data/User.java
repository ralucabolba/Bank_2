package Data;

import java.sql.*;

public class User {

	private int idUser;
	private String username;
	private String password;
	private Type type;
	
	public enum Type{employee, administrator};
	
	private Connection connection = this.connect("jdbc:mysql//localhost:3306/bank", "root", "admin");
	
	public User(int idUser, String username, String password, Type type){
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
	private Connection connect(String name, String username, String password){
		Connection connection = null;
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(name, username, password);
			
		}catch(Exception e){
			System.out.println(e.getMessage());
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
	public void createUser(int idUser, String username, String password, Type type){
		String t = type.toString();
		
		PreparedStatement pst;
		
		try{
			pst = connection.prepareStatement("INSERT INTO BankUser VALUES (" + idUser + ", '" + username + "', '" + password + "', '" + t + "');");
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
	public void updateUser(int idUser, String username, String password, Type type){
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
	public ResultSet getEmployees(){
		PreparedStatement pst;
		ResultSet rs = null;
		
		try{
			pst = connection.prepareStatement("SELECT * FROM BankUser WHERE typeUser = 'employee';");
			rs = pst.executeQuery();
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
		}
		return rs;
	}
	
	/**
	 * Method that returns an employee
	 * @param idUser
	 * @return
	 */
	public ResultSet getEmployee(int idUser){
		PreparedStatement pst;
		ResultSet rs = null;
		
		try{
			pst = connection.prepareStatement("SELECT * FROM BankUser WHERE idUser = " + idUser + " AND typeUser = 'employee';");
			rs = pst.executeQuery();
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
		}
		return rs;
	}
	
	public int getIdUserByInfo(String username, String password){
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
}
