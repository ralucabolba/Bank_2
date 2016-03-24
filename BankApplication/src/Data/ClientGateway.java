package Data;

import java.sql.*;

import com.sun.rowset.CachedRowSetImpl;

public class ClientGateway {

	private Connection connection = connect("jdbc:mysql://localhost:3306/bank?autoReconnect=true&useSSL=false", "root", "admin");
	
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
	 * Method that adds a new bank client to database
	 * @param idClient
	 * @param name
	 * @param cnp
	 * @param address
	 * @return
	 */
	public boolean addClient(int idClient, String name, String cnp, String address){
		PreparedStatement pst;
		
		try{
			pst = connection.prepareStatement("INSERT INTO BankClient VALUES (" + idClient + ", '" + name + 
					"', '" + cnp + "', '" + address + "');");
			pst.executeUpdate();
			
			return true;
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
			
			return false;
		}
	}
	
	/**
	 * Method that updates a client
	 * @param idClient
	 * @param name
	 * @param cnp
	 * @param address
	 */
	public void updateClient(int idClient, String name, String cnp, String address){
		PreparedStatement pst;
		
		try{
			pst = connection.prepareStatement("UPDATE BankClient SET nameClient = '" + name + 
					"', cnp = '" + cnp + "', address = '" + address +
					"' WHERE idClient = " + idClient);

			pst.executeUpdate();
			
			//return true;
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
			
			//return false;
		}
	}
	
	public CachedRowSetImpl getClients(){
		PreparedStatement pst;
		ResultSet rs;
		
		try{
			pst = connection.prepareStatement("SELECT * FROM BankClient;");
			rs = pst.executeQuery();
			
			CachedRowSetImpl crs = new CachedRowSetImpl();
			crs.populate(rs);
			crs.setMatchColumn(new String[]{"Id Client", "Name", "Cnp", "Address"});
			return crs;
		}catch(SQLException e){
			System.out.println(e.getMessage());
		}
		
		return null;
	}
}
