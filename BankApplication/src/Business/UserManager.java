package Business;
import Data.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.rowset.CachedRowSetImpl;

import java.util.*;

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
	
	public List<List<String>> getEmployees(){
		CachedRowSetImpl crs = User.getEmployees();
		
		List<List<String>> list = new ArrayList<>();
		
		try{
			while(crs.next()){
				int id = crs.getInt("idUser");
				String un = crs.getString("username");
				String type = crs.getString("typeUser");
				
				List<String> ls = new ArrayList<>();
				
				ls.add(String.valueOf(id));
				ls.add(un);
				ls.add(type);
				
				list.add(ls);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		return list;
	}
	
	public int addEmployee(String username, String password){
		String unPattern = "[a-zA-Z.-_0-9]+";
		String pwPattern = "[a-zA-Z.-_0-9]{6}+";
		
		Pattern pUn = Pattern.compile(unPattern);
		Pattern pPw = Pattern.compile(pwPattern);
		
		Matcher mUn = pUn.matcher(username);
		Matcher mPw = pPw.matcher(password);
		
		if(!mUn.find()){
			return -1;
		}
		if(!mPw.find()){
			return -2;
		}
		if(User.existsUsername(username)){
			return -3;
		}
		User.createUser(User.getMaxUserId() + 1, username, password, "employee");
		
		return 0;
	}
	
	public boolean deleteUser(int idUser){
		return User.deleteUser(idUser);
	}
	
	public int updateUser(int idUser, String username, String type){
		String unPattern = "[a-zA-Z.-_0-9]+";
		Pattern pUn = Pattern.compile(unPattern);
		Matcher mUn = pUn.matcher(username);
		
		if(!mUn.find()){
			return -1;
		}
		if(!type.equals("administrator") && !type.equals("employee")){
			return -2;
		}
		if(User.existsUsername(username)){
			return -3;
		}
		
		User.updateUser(idUser, username, type);
		
		return 0;
	}
	
	public List<String> getEmployeesIds(){
		CachedRowSetImpl crs = User.getEmployees();
		
		List<String> list = new ArrayList<>();
		
		try{
			while(crs.next()){
				int id = crs.getInt("idUser");
				
				list.add(String.valueOf(id));
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		return list;
	}
	
	public List<List<String>> getActivitiesForUser(int idUser, Date from, Date to){
		List<List<String>> list = new ArrayList<>();
		
		CachedRowSetImpl crs = Activity.findForEmployee(idUser);
		
		try{
			while(crs.next()){
				List<String> ls = new ArrayList<>();
				
				int idAct = crs.getInt("idAct");
				Date date = crs.getDate("dateAct");
				String type = crs.getString("typeAct");
				String desc = crs.getString("description");
				
				//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				
				ls.add(String.valueOf(idAct));
				ls.add(String.valueOf(idUser));
				ls.add(String.valueOf(date));
				ls.add(type);
				ls.add(desc);
				
				list.add(ls);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return list;
	}
}
