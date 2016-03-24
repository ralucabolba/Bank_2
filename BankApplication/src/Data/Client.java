package Data;

import com.sun.rowset.CachedRowSetImpl;

public class Client {
	private int idClient;
	private String name;
	private String cnp;
	private String address;
	private ClientGateway gateway;
	
	public Client(int idClient, String name, String cnp, String address, ClientGateway gateway){
		this.idClient = idClient;
		this.name = name;
		this.cnp = cnp;
		this.address = address;
		this.gateway = gateway;
		
		this.gateway.addClient(idClient, name, cnp, address);
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getCnp(){
		return this.cnp;
	}
	
	public String getAddress(){
		return this.address;
	}
	
	public int getIdClient(){
		return this.idClient;
	}
	
	public void setName(String newName){
		this.name = newName;
		this.gateway.updateClient(this.idClient, newName, this.cnp, this.address);
	}
	
	public void setCnp(String newCnp){
		this.cnp = newCnp;
		this.gateway.updateClient(this.idClient, this.name, newCnp, this.address);
	}
	
	public void setAddress(String newAddress){
		this.address = newAddress;
		this.gateway.updateClient(this.idClient, this.name, this.cnp, newAddress);
	}
	
//	public CachedRowSetImpl getClients(){
//		return this.gateway.getClients();
//	}
}
