package Business;
import java.sql.*;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.regex.*;

import com.sun.rowset.CachedRowSetImpl;

import Data.Client;
import Data.ClientGateway;

public class ClientManager {
	private static ClientGateway cg;
	
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
	
	public ClientManager(){
		cg = new ClientGateway();
	}
	
	public CachedRowSetImpl getClients(){
		return cg.getClients();
	}
	
	public List<String> getClientsIds(){
		CachedRowSetImpl crs = cg.getClients();
		
		List<String> cs = new ArrayList<>();
		
		try{
			while(crs.next()){
				cs.add(String.valueOf(crs.getInt("idClient")));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return cs;
	}
	
	public static String getClientNameById(int idC){
		return cg.getClientNameById(idC);
	}
	public int addClient(String name, String cnp, String address){
		int check = checkInfo(name, cnp, address);
		
		if(cg.existsClientCnp(cnp)){
			return -4;
		}
		if(check == 0){
			int id = cg.getMaxClientId();
			Client c = new Client(id + 1, name, cnp, address, cg);
		}
		
		return check;
	}
	
	public int checkInfo(String name, String cnp, String address){
		String cnpPattern = "[1-2]([0-9]{12})";
		String namePattern = "([A-Z][a-z]+) ([A-Z][a-z]+)";
		String addressPattern = "[A-Za-z0-9 ]+";
		
		Pattern pCnp = Pattern.compile(cnpPattern);
		Pattern pName = Pattern.compile(namePattern);
		Pattern pAddress = Pattern.compile(addressPattern);
		
		Matcher mCnp = pCnp.matcher(cnp);
		Matcher mName = pName.matcher(name);
		Matcher mAddress = pAddress.matcher(address);
		
		if(mCnp.find()){
			if(mName.find()){
				if(mAddress.find()){
					return 0;
				}
				else{
					return -3; //address invalid
				}
			}
			else
			{
				return -2; //name invalid
			}
		}else{
			return -1; //cnp invalid
		}
	}
	
	public int updateClient(int idClient, String name, String cnp, String address){
		int check = checkInfo(name, cnp, address);
		if(cg.existsClientCnp(cnp)){
			return -4;
		}
		if(check == 0){
			cg.updateClient(idClient, name, cnp, address);
		}
		return check;
	}
}
