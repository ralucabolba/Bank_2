package data;

import java.util.Date;

import com.sun.rowset.CachedRowSetImpl;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class AccountGateway {
	private static Connection connection;// = connect("jdbc:mysql://localhost:3306/bank?autoReconnect=true&useSSL=false", "root", "admin");
	
	public AccountGateway(String name, String username, String password){
		connection = connect(name, username, password);
	}

	public int createAccount(int idClient, String type, float balance, Date dateCreation){
		PreparedStatement pst;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		int idAccount = getMaxAccountId() + 1;
		
		try{
			pst = connection.prepareStatement("INSERT INTO BankAccount values (" + idAccount + ", " + idClient + ", '" + 
												type + "', " + balance + ", '" + dateFormat.format(dateCreation) + "');");
			pst.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		return idAccount;
	}
	

	public void updateAccount(int idAccount, int idClient, String type, float balance, Date dateCreation){
		PreparedStatement pst;
		
		try{
			pst = connection.prepareStatement("UPDATE BankAccount SET idClient = " + idClient + 
													", typeAccount = '" + type + "', balance = " + balance + ", dateCreation = ?" + 
													" WHERE idAccount = " + idAccount);
			
			pst.setDate(1, new java.sql.Date(dateCreation.getTime()));
			pst.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	

	public void deleteAccount(int idAccount){
		PreparedStatement pst;
		
		try{
			pst = connection.prepareStatement("DELETE FROM BankAccount WHERE idAccount = " + idAccount);
			pst.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	
//	public String getAccountType(int idAccount){
//		String type = null;
//		
//		PreparedStatement pst;
//		ResultSet rs;
//		
//		try{
//			pst = connection.prepareStatement("SELECT typeAccount FROM BankAccount WHERE idAccount = " + idAccount);
//			rs = pst.executeQuery();
//			
//			if(rs.next()){
//				type = rs.getString("idAccount");
//			}
//		}
//		catch(Exception e){
//			System.out.println(e.getMessage());
//		}
//		
//		return type;
//	}
	
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
	
	public CachedRowSetImpl getAccounts(){
		PreparedStatement pst;
		ResultSet rs;
		
		try{
			pst = connection.prepareStatement("SELECT * FROM BankAccount;");
			rs = pst.executeQuery();
			
			CachedRowSetImpl crs = new CachedRowSetImpl();
			crs.populate(rs);
			crs.setMatchColumn(new String[]{"Id account", "Name owner", "Type", "Balance", "Date creation"});
			return crs;
		}catch(SQLException e){
			System.out.println(e.getMessage());
		}
		
		return null;
	}
	
	private int getMaxAccountId(){
		PreparedStatement pst;
		ResultSet rs;
		
		int id = -1;
		try{
			pst = connection.prepareStatement("SELECT MAX(idAccount) AS maxid FROM BankAccount;");
			rs = pst.executeQuery();
			
			if(rs.next()){
				id = rs.getInt("maxid");
			}
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return id;
	}
	
	public CachedRowSetImpl getAccountForId(int id){
		PreparedStatement pst;
		ResultSet rs;
		
		try{
			pst = connection.prepareStatement("SELECT * FROM BankAccount WHERE idAccount = " + id);
			rs = pst.executeQuery();
			
			CachedRowSetImpl crs = new CachedRowSetImpl();
			crs.populate(rs);
			
			return crs;
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		return null;
	}
}
