package Business;
import Data.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import Data.Activity;
import Data.Client;

public class UserManager {
	private int currentUser;
	
	public int getUser(String username, String password){
		int idUser = User.getIdUserByInfo(username, password);
		return idUser;
	}
	
	public String getTypeUser(int idUser){
		if(idUser > 0){
			return User.getTypeUser(idUser);
		}
		
		return null;
	}
	
	public void setCurrentUser(int user){
		this.currentUser = user;
	}
	
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
	
	public void addActivityForUser(String type, String description){
		PreparedStatement pst;
		ResultSet rs;
		
		int id;
		
		try{
			pst = connection.prepareStatement("SELECT MAX(idAct) AS maxid FROM Activity;");
			rs = pst.executeQuery();
			
			if(rs.next()){
				id = rs.getInt("maxid");
				Activity.createActivity(id+1, type, description, currentUser, new Date());
			}else{
				Activity.createActivity(1, type, description, currentUser, new Date());
			}
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}
