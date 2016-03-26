package business;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.rowset.CachedRowSetImpl;

import data.ClientGateway;

public class Client {

//	private int idClient;
//	private String name;
//	private String cnp;
//	private String address;
	
	private static ClientGateway cg;
	
	public Client(String DBname, String DBusername, String DBpassword){
		cg = new ClientGateway(DBname, DBusername, DBpassword);
	}
	
	public static List<List<String>> getClients(){
		CachedRowSetImpl crs = cg.getClients();
		List<List<String>> data = new ArrayList<>();
		
		try {
			while(crs.next()){
				int idC = crs.getInt("idClient");
				String name = crs.getString("nameClient");
				String cnp = crs.getString("cnp");
				String address = crs.getString("address");
				
				List<String> unit = new ArrayList<>();
				
				unit.add(String.valueOf(idC));
				unit.add(name);
				unit.add(cnp);
				unit.add(address);
				
				
				data.add(unit);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	public static List<String> getClientsIds(){
		List<String> list = new ArrayList<>();
		
		List<List<String>> cs = getClients();
		for(List<String> cl : cs){
			list.add(cl.get(0));
		}
		
		return list;
	}
	
	public static String getClientNameById(int idC){
		return cg.getClientNameById(idC);
	}
	
	public static int addClient(String name, String cnp, String address){
		int check = checkInfo(name, cnp, address);
		
		if(cg.existsClientCnp(cnp)){
			return -4;
		}
		if(check == 0){
			cg.addClient(name, cnp, address);
		}
		
		return check;
	}
	
	public static int checkInfo(String name, String cnp, String address){
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
	
	public static int updateClient(int idClient, String name, String cnp, String address){
		int check = checkInfo(name, cnp, address);
		
		if(check == 0){
			cg.updateClient(idClient, name, cnp, address);
		}
		return check;
	}
}
