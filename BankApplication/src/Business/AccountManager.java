package Business;
import Data.Account;
import Data.AccountGateway;
import java.sql.*;
import java.util.Date;
import java.util.*;

import com.sun.rowset.CachedRowSetImpl;

public class AccountManager {
	private static AccountGateway ag = new AccountGateway();
	
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
	
	public List<List<String>> getAccounts(){
		CachedRowSetImpl crs = ag.getAccounts();
		
		List<List<String>> list = new ArrayList<>();
		
		try{
			while(crs.next()){
				int idAc = crs.getInt("idAccount");
				int idC = crs.getInt("idClient");
				String type = crs.getString("typeAccount");
				float balance = crs.getFloat("balance");
				Date dateC = crs.getDate("dateCreation");
				
				String nameC = ClientManager.getClientNameById(idC);
				
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
	
	public void addNewAccount(String type, int idClient){
		int idAct = ag.getMaxAccountId();
		Date date = new Date();
		
		Account a = new Account(idAct + 1, idClient, type, (float)0.0, date, ag);
	}
	
	public int updateAccount(int idAccount, int idClient, String type, float balance, Date dateCreation){
		if(!(type.equals("Saving account")) && !(type.equals("Spending account"))){
			return -1;
			
		}
		if(balance < 0.0){
			return -2;
		}
		ag.updateAccount(idAccount, idClient, type, balance, dateCreation);
		return 0;
	}
	
	public boolean deleteAccount(int idAccount){
		return ag.deleteAccount(idAccount);
	}
	
	public List<String> getAccountsIds(){
		CachedRowSetImpl crs = ag.getAccounts();
		
		List<String> cs = new ArrayList<>();
		
		try{
			while(crs.next()){
				cs.add(String.valueOf(crs.getInt("idAccount")));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return cs;
	}
	
	public int transferMoney(int sourceId, int destId, String sum){
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
