package business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.rowset.CachedRowSetImpl;
import data.UserGateway;

public class User {

	private int idUser;
	private String username;
	private String password;
	private String type;
	
	private static UserGateway ug;
	
	public User(String DBname, String DBusername, String DBpassword){
		ug = new UserGateway(DBname, DBusername, DBpassword);
	}
	
	public boolean authentificate(String username, String password){
		CachedRowSetImpl crs = ug.getUserByInfo(username, password);
		
		try{
			while(crs.next()){
				this.idUser = crs.getInt("idUser");
				this.username = crs.getString("username");
				this.password = crs.getString("passwrd");
				this.type = crs.getString("typeUser");
			}
			
			//System.out.println(this.idUser);
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	public String getType(){
		return this.type;
	}
	
	public int getId(){
		return this.idUser;
	}
	
	
	public static List<List<String>> getEmployees(){
		CachedRowSetImpl crs = ug.getEmployees();
		
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
	
	public static int addEmployee(String username, String password){
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
		if(ug.existsUsername(username)){
			return -3;
		}
		ug.createUser(username, password, "employee");
		
		return 0;
	}
	
	public static void deleteUser(int idUser){
		ug.deleteUser(idUser);
	}
	
	public static int updateUser(int idUser, String username, String type){
		String unPattern = "[a-zA-Z.-_0-9]+";
		Pattern pUn = Pattern.compile(unPattern);
		Matcher mUn = pUn.matcher(username);
		
		if(!mUn.find()){
			return -1;
		}
		if(!type.equals("administrator") && !type.equals("employee")){
			return -2;
		}
		if(!username.equals(getUsernameForId(idUser)) && ug.existsUsername(username)){
			return -3;
		}
		
		ug.updateUser(idUser, username, type);
		
		return 0;
	}
	
	public static List<String> getEmployeesIds(){
		List<String> list = new ArrayList<>();
		
		List<List<String>> ems = getEmployees();
		for(List<String> em : ems){
			list.add(em.get(0));
		}
		return list;
	}
	
	private static String getUsernameForId(int idUser){
		CachedRowSetImpl crs = ug.getEmployee(idUser);
		
		try{
			String username;
			while(crs.next()){
				username = crs.getString("username");
				return username;
			}
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
}
