package business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sun.rowset.CachedRowSetImpl;

import data.AccountGateway;

public class Account {
//	private int idAcc;
//	private int idClient;
//	private String type;
//	private float balance;
//	private Date dateCreation;
	
	private static AccountGateway ag;
	
	public Account(String DBname, String DBusername, String DBpassword){
		ag = new AccountGateway(DBname, DBusername, DBpassword);
	}
	
	public static List<List<String>> getAccounts(){
		CachedRowSetImpl crs = ag.getAccounts();
		
		List<List<String>> list = new ArrayList<>();
		
		try{
			while(crs.next()){
				int idAc = crs.getInt("idAccount");
				int idC = crs.getInt("idClient");
				String type = crs.getString("typeAccount");
				float balance = crs.getFloat("balance");
				Date dateC = crs.getDate("dateCreation");
				
				String nameC = Client.getClientNameById(idC);
				
				List<String> ls = new ArrayList<String>();
				ls.add(String.valueOf(idAc));
				ls.add(String.valueOf(idC));
				ls.add(nameC);
				ls.add(type);
				ls.add(String.valueOf(balance));
				ls.add(String.valueOf(dateC));
				
				list.add(ls);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static void addNewAccount(String type, int idClient){
		ag.createAccount(idClient, type, (float)0.0, new Date());
	}
	
	public static int updateAccount(int idAccount, int idClient, String type, float balance, Date dateCreation){
		if(!(type.equals("Saving account")) && !(type.equals("Spending account"))){
			return -1;
			
		}
		if(balance < 0.0){
			return -2;
		}
		ag.updateAccount(idAccount, idClient, type, balance, dateCreation);
		return 0;
	}
	
	public static void deleteAccount(int idAccount){
		ag.deleteAccount(idAccount);
	}
	
	public static List<String> getAccountsIds(){
		List<String> list = new ArrayList<>();
		
		List<List<String>> acs = getAccounts();
		for(List<String> ac : acs){
			list.add(ac.get(0));
		}
		
		return list;
	}
	
	public static int transferMoney(int sourceId, int destId, String sum){
		float amount;
		try{
			amount = Float.valueOf(sum);
			if(sourceId == destId){
				return -4;
			}
			if(amount < 0.0){
				return -2;
			}
			
			CachedRowSetImpl source = ag.getAccountForId(sourceId);
			CachedRowSetImpl dest = ag.getAccountForId(destId);
			
			if(source.next() && dest.next()){
				float sSum = source.getFloat("balance");
				float dSum = dest.getFloat("balance");
				
				if(sSum < amount){
					return -1;
				}
				
				ag.updateAccount(sourceId, source.getInt("idClient"), source.getString("typeAccount"), sSum - amount, source.getDate("dateCreation"));
				ag.updateAccount(destId, dest.getInt("idClient"), dest.getString("typeAccount"), dSum + amount, dest.getDate("dateCreation"));
				
			}
		}catch(NumberFormatException e){
			return -2;
		}
		catch(SQLException e){
			return -3;
		}
		return 0;
	}
}
