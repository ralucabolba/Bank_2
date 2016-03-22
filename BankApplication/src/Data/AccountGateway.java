package Data;

import java.util.Date;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class AccountGateway {
	private Connection connection = this.connect("jdbc:mysql//localhost:3306/bank", "root", "admin");
	
	
	/**
	 * Method that adds a new BankAccount to database
	 * @param idAccount
	 * @param idClient
	 * @param type
	 * @param balance
	 * @param dateCreation
	 * @return
	 */
	public boolean createAccount(int idAccount, int idClient, String type, float balance, Date dateCreation){
		PreparedStatement pst;
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		try{
			pst = connection.prepareStatement("INSERT INTO BankAccount values (" + idAccount + ", " + idClient + ", '" + 
												type + "', " + balance + ", '" + dateFormat.format(dateCreation) + "');");
			pst.executeUpdate();
			
			return true;
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
			
			return false;
		}
	}
	
	/**
	 * Method that updates an account
	 * @param idAccount
	 * @param idClient
	 * @param type
	 * @param balance
	 * @param dateCreation
	 * @return
	 */
	public boolean updateAccount(int idAccount, int idClient, String type, float balance, Date dateCreation){
		PreparedStatement pst;
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		try{
			pst = connection.prepareStatement("UPDATE BankAccount SET idClient = " + idClient + 
													", typeAccount = '" + type + "', balance = " + balance + "dateCreation = '" + dateFormat.format(dateCreation) +
													"' WHERE idAccount = " + idAccount);
			
			pst.executeUpdate();
			return true;
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
			
			return false;
		}
	}
	
	
	/**
	 * Method that deletes an account
	 * @param idAccount
	 * @return
	 */
	public boolean deleteAccount(int idAccount){
		PreparedStatement pst;
		
		try{
			pst = connection.prepareStatement("DELETE FROM BankAccount WHERE idAccount = " + idAccount);
			pst.executeUpdate();
			
			return true;
		}catch(SQLException e){
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	/**
	 * Method that returns the type of an account
	 * @param idAccount
	 * @return
	 */
	public String getAccountType(int idAccount){
		String type = null;
		
		PreparedStatement pst;
		ResultSet rs;
		
		try{
			pst = connection.prepareStatement("SELECT typeAccount FROM BankAccount WHERE idAccount = " + idAccount);
			rs = pst.executeQuery();
			
			if(rs.next()){
				type = rs.getString("idAccount");
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		return type;
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
}
