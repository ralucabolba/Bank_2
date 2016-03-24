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
	
	public String getClientNameById(int id){
		PreparedStatement pst;
		ResultSet rs;
		
		try{
			pst = connection.prepareStatement("SELECT nameClient FROM BankClient WHERE idClient = " + id);
			rs = pst.executeQuery();
			
			String name;
			
			if(rs.next()){
				name = rs.getString("nameClient");
				return name;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public int getMaxClientId(){
		PreparedStatement pst;
		ResultSet rs;
		
		int id = -1;
		try{
			pst = connection.prepareStatement("SELECT MAX(idClient) AS maxid FROM BankClient;");
			rs = pst.executeQuery();
			
			if(rs.next()){
				id = rs.getInt("maxid");
			}
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return id;
	}
	
	public boolean existsClientCnp(String cnp){
		PreparedStatement pst;
		ResultSet rs;
		
		try{
			pst = connection.prepareStatement("SELECT idClient FROM BankClient WHERE cnp = '" + cnp + "'");
			rs = pst.executeQuery();
			
			if(rs.next()){
				return true;
			}
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return false;
	}
}
