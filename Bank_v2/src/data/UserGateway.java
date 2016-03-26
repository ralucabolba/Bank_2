package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sun.rowset.CachedRowSetImpl;

public class UserGateway {
	private static Connection connection;
	
	public UserGateway(String name, String username, String password){
		connection = connect(name, username, password);
	}
	
	private Connection connect(String name, String username, String password){
		Connection connection = null;
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(name, username, password);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return connection;
	}
	
	private int getMaxUserId(){
		PreparedStatement pst;
		ResultSet rs;
		
		int id = -1;
		try{
			pst = connection.prepareStatement("SELECT MAX(idUser) AS maxid FROM BankUser;");
			rs = pst.executeQuery();
			
			if(rs.next()){
				id = rs.getInt("maxid");
			}
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return id;
	}
	
	public int createUser(String username, String password, String type){
		
		PreparedStatement pst;
		int idUser = getMaxUserId() + 1;
		
		try{
			pst = connection.prepareStatement("INSERT INTO BankUser VALUES (" + idUser + ", '" + username + "', '" + password + "', '" + type + "');");
			pst.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		return idUser;
	}
	
	/**
	 * Method that updates an user
	 * @param idUser
	 * @param username
	 * @param password
	 * @param type
	 */
	public void updateUser(int idUser, String username, String type){
		String t = type.toString();
		
		PreparedStatement pst;
		
		try{
			pst = connection.prepareStatement("UPDATE BankUser SET "
					+ "								username = '" + username + "', "
					//+ "								passwrd = '" + password + "', "
					+ "								typeUser = '" + t + "' WHERE idUser = " + idUser); 
			pst.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
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
			e.printStackTrace();
		}
		
	}
	
	public CachedRowSetImpl getEmployees(){
		PreparedStatement pst;
		ResultSet rs = null;
		
		
		try{
			pst = connection.prepareStatement("SELECT * FROM BankUser WHERE typeUser = 'employee';");
			rs = pst.executeQuery();
			
			CachedRowSetImpl crs = new CachedRowSetImpl();
			crs.populate(rs);
			
			return crs;
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public CachedRowSetImpl getEmployee(int idUser){
		PreparedStatement pst;
		ResultSet rs = null;
		
		try{
			pst = connection.prepareStatement("SELECT * FROM BankUser WHERE idUser = " + idUser + ";");
			rs = pst.executeQuery();
			
			CachedRowSetImpl crs = new CachedRowSetImpl();
			crs.populate(rs);
			
			return crs;
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public CachedRowSetImpl getUserByInfo(String username, String password){
		PreparedStatement pst;
		ResultSet rs;
		
		try{
			pst = connection.prepareStatement("SELECT * FROM BankUser WHERE username = '" + username + "' AND passwrd = '" + password + "'");
			rs = pst.executeQuery();
			
			CachedRowSetImpl crs = new CachedRowSetImpl();
			crs.populate(rs);
			
			return crs;
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean existsUsername(String username){
		PreparedStatement pst;
		ResultSet rs;
		
		try{
			pst = connection.prepareStatement("SELECT idUser FROM BankUser WHERE username = '" + username + "'");
			rs = pst.executeQuery();
			
			if(rs.next()){
				return true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
}
