package Data;

import java.util.*;

public class Account {
	private int idAccount;
	private int idClient;
	private String type;
	private float balance;
	private Date dateCreation;
	private AccountGateway gateway;
	
	public Account(int idAccount, String type, float balance, Date dateCreation, AccountGateway gateway){
		this.idAccount = idAccount;
		this.type = type;
		this.balance = balance;
		this.dateCreation = dateCreation;
		this.gateway = gateway;
		
		this.gateway.createAccount(idAccount, idClient, type, balance, dateCreation);
	}
	
	public int getIdNumber(){
		return this.idAccount;
	}
	
	public String getType(){
		return this.type;
	}
	
	public float getAmountMoney(){
		return this.balance;
	}
	
	public Date getCreationDate(){
		return this.dateCreation;
	}
	
	public void setType(String newType){
		this.type = newType;
		this.gateway.updateAccount(idAccount, idClient, newType, balance, dateCreation);
	}
	
	public void setBalance(float newBalance){
		this.balance = newBalance;
		this.gateway.updateAccount(idAccount, idClient, type, newBalance, dateCreation);
	}
	
	public void setIdAccount(int newIdAccount){
		this.idAccount = newIdAccount;
		this.gateway.updateAccount(newIdAccount, newIdAccount, type, newIdAccount, dateCreation);
	}
	
}
