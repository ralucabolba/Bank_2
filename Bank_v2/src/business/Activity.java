package business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sun.rowset.CachedRowSetImpl;

import data.ActivityGateway;

public class Activity {

//	private int idActivity;
//	private int idUser;
//	private String type;
//	private String description;
//	private Date date;
	
	private static ActivityGateway ag;
	
	public Activity(String DBname, String DBusername, String DBpassword){
		ag = new ActivityGateway(DBname, DBusername, DBpassword);
	}
	
	public static void addActivityForUser(int idEmployee, String type, String description){
		ag.createActivity(type, description, idEmployee, new Date());
	}
	
	public static List<List<String>> getActivitiesForUser(int idUser, Date from, Date to){
		List<List<String>> list = new ArrayList<>();
		
		CachedRowSetImpl crs = ag.findForEmployee(idUser, from, to);
		
		try{
			while(crs.next()){
				List<String> ls = new ArrayList<>();
				
				int idAct = crs.getInt("idAct");
				Date date = crs.getDate("dateAct");
				String type = crs.getString("typeAct");
				String desc = crs.getString("description");
				
				
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
