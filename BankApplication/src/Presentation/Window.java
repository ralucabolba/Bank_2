package Presentation;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.sun.rowset.CachedRowSetImpl;

import Business.*;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Callback;
import javafx.ext.swing.*;

public class Window extends Application{

	private String newName = "", newCnp = "", newAddress = "";
	
	private static UserManager ub;
	private static ClientManager cm;
	
	private Button buttonEmployee;
	private Button buttonAdmin;
	private Button buttonLogin;
	private Button buttonAddClient;
	private Button buttonDeleteClient;
	
	private Label labelType;
	
	private Scene currentScene;
	
	private TabPane tabPaneEmployee;
	private TabPane tabPaneAdmin;
	
	private Tab tabClients;
	private Tab tabAccounts;
	private Tab tabTransfer;
	private Tab tabBills;
	
	private TextField usernameField;
	private TextField addIdClient;
	private TextField addNameClient;
	private TextField addCnpClient;
	private TextField addAddressClient;
	
	private TableView<List<String>> clientTable = new TableView<>();
	
	private PasswordField passwordField;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		loginWindow();
		
		primaryStage.setTitle("Bank Application");
		primaryStage.setScene(currentScene);
		primaryStage.show();
	}
	
	
	
	public void loginWindow(){
		VBox vboxLog = new VBox(20);
		HBox hboxUsername = new HBox(10);
		HBox hboxPassword = new HBox(10);
		
		vboxLog.setAlignment(Pos.CENTER);
		hboxUsername.setAlignment(Pos.CENTER);
		hboxPassword.setAlignment(Pos.CENTER);
		
		Label usernameLabel = new Label("Username");
		Label passwordLabel = new Label("Password");
		
		usernameField = new TextField();
		passwordField = new PasswordField();
		
		buttonLogin = new Button("Log in");
		addListenerButtonLogin(new ButtonLoginListener());
		
		hboxUsername.getChildren().addAll(usernameLabel, usernameField);
		hboxPassword.getChildren().addAll(passwordLabel, passwordField);
		
		vboxLog.getChildren().addAll(hboxUsername, hboxPassword, buttonLogin);
		
		Scene scene = new Scene(vboxLog, 250, 350);
		
		//return scene;
		currentScene = scene;
		
	}
	@SuppressWarnings("unchecked")
	public Scene EmployeeWindow(){
		tabPaneEmployee = new TabPane();
		
		tabClients = new Tab("Clients");
		tabAccounts = new Tab("Accounts");
		tabBills = new Tab("Bills");
		tabTransfer = new Tab("Transfer money");
		
		VBox hboxClients = new VBox(20);
		HBox hboxAccounts = new HBox();
		HBox hboxBills = new HBox();
		HBox hboxTransfer = new HBox();
		
		
		/*Client table*/
		
		clientTable.setEditable(true);
		clientTable.setMinWidth(600);
		clientTable.setMaxHeight(600);
		clientTable.setEditable(true);
		
		Label labelC = new Label("Clients");
		populateClient(clientTable);
		
		TableColumn<List<String>, String> idCol = 
				new TableColumn<List<String>, String>("Id client");
		idCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(0));
			}
		});
		
		idCol.setMinWidth(50);
		
		TableColumn<List<String>, String> nameCol = 
				new TableColumn<List<String>, String>("Name");
		nameCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(1));
			}
		});
		
		nameCol.setMinWidth(100);
		nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		nameCol.setOnEditCommit(
			    new EventHandler<CellEditEvent<List<String>, String>>() {
			        @Override
			        public void handle(CellEditEvent<List<String>, String> t) {
//			            ((List<String>) t.getTableView().getItems().get(
//			                t.getTablePosition().getRow())
//			                ).setFirstName(t.getNewValue());
			        	newName = t.getNewValue();
			        }
			    }
			);
		
		TableColumn<List<String>, String> cnpCol = 
				new TableColumn<List<String>, String>("Cnp");
		cnpCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(2));
			}
		});
		cnpCol.setMinWidth(100);
		cnpCol.setCellFactory(TextFieldTableCell.forTableColumn());
		cnpCol.setOnEditCommit(
			    new EventHandler<CellEditEvent<List<String>, String>>() {
			        @Override
			        public void handle(CellEditEvent<List<String>, String> t) {
//			            ((List<String>) t.getTableView().getItems().get(
//			                t.getTablePosition().getRow())
//			                ).setFirstName(t.getNewValue());
			        	newCnp = t.getNewValue();
			        }
			    }
			);
		
		TableColumn<List<String>, String> addressCol = 
				new TableColumn<List<String>, String>("Address");
		addressCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(3));
			}
		});
		addressCol.setMinWidth(100);
		addressCol.setCellFactory(TextFieldTableCell.forTableColumn());
		addressCol.setOnEditCommit(
			    new EventHandler<CellEditEvent<List<String>, String>>() {
			        @Override
			        public void handle(CellEditEvent<List<String>, String> t) {
//			            ((List<String>) t.getTableView().getItems().get(
//			                t.getTablePosition().getRow())
//			                ).setFirstName(t.getNewValue());
			        	newAddress = t.getNewValue();
			        }
			    }
			);

        TableColumn<List<String>, String> updateCol = 
				new TableColumn<List<String>, String>("Update");
		Callback<TableColumn<List<String>, String>, TableCell<List<String>, String>> cell2Factory = //
                new Callback<TableColumn<List<String>, String>, TableCell<List<String>, String>>()
                {
                    @Override
                    public TableCell call( final TableColumn<List<String>, String> param )
                    {
                        TableCell<List<String>, String> cell = new TableCell<List<String>, String>()
                        {

                        	//Button buttonDeleteClient = new Button( "Delete" );
                        	Button buttonUpdateClient = new Button("Update");
                        	
                            @Override
                            public void updateItem( String item, boolean empty )
                            {
                                super.updateItem( item, empty );
                                if ( empty )
                                {
                                    setGraphic( null );
                                    setText( null );
                                }
                                else
                                {
                                	//c = this;
                                	//List<String> ls = (List<String>) getTableRow().getItem();
                                	buttonUpdateClient.setOnAction(event -> getUpdatedRow((List<String>) getTableRow().getItem()));
                                	
                                    //setGraphic(buttonDeleteClient);
                                    setGraphic(buttonUpdateClient);
                                    setText( null );
                                }
                            }
                        };
                        return cell;
                    }
                };

        updateCol.setCellFactory( cell2Factory );
        
		clientTable.getColumns().addAll(idCol, nameCol, cnpCol, addressCol, updateCol);
		clientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		HBox addClientBox = new HBox(20);
		
		addNameClient = new TextField();
		addNameClient.setPromptText("Name");
		
		addCnpClient = new TextField();
		addCnpClient.setPromptText("Cnp");
		
		addAddressClient = new TextField();
		addAddressClient.setPromptText("Address");
		
		buttonAddClient = new Button("Add client");
		addListenerAddClient(new AddClientListener());
		
		addClientBox.getChildren().addAll(addNameClient, addCnpClient, addAddressClient, buttonAddClient);
		
		hboxClients.getChildren().addAll(labelC, clientTable, addClientBox);
		
		hboxClients.setMinWidth(400);
		hboxClients.setAlignment(Pos.CENTER);
		hboxAccounts.setAlignment(Pos.CENTER);
		hboxBills.setAlignment(Pos.CENTER);
		hboxTransfer.setAlignment(Pos.CENTER);
		
		tabClients.setContent(hboxClients);
		tabAccounts.setContent(hboxAccounts);
		tabBills.setContent(hboxBills);
		tabTransfer.setContent(hboxTransfer);
		
		tabPaneEmployee.getTabs().addAll(tabClients, tabAccounts, tabBills, tabTransfer);
		
		Group root = new Group();
		Scene scene = new Scene(root, 800, 600, Color.BISQUE);
		
		BorderPane borderPane = new BorderPane();
		borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());
        
        borderPane.setCenter(tabPaneEmployee);
        root.getChildren().add(borderPane);
        
        return scene;
	}
	
	public void populateClient(TableView clientTable){
		CachedRowSetImpl crs = cm.getClients();
		
		
		
		ObservableList<List<String>> data = FXCollections.observableArrayList();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(data);
		clientTable.setItems(data);
		
		
	}
	public void ErrorMessage(String title, String header, String content){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		
		alert.showAndWait();
	}
	
	public void SuccesMessage(String title, String header, String content){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		
		alert.showAndWait();
	}
	
	public static void main(String[] args){
		ub = new UserManager();
		cm = new ClientManager();
		launch(args);
	}
	
	

	public void addListenerButtonLogin(EventHandler<ActionEvent> act){
		buttonLogin.setOnAction(act);
	}
	
	public void addListenerAddClient(EventHandler<ActionEvent> act){
		buttonAddClient.setOnAction(act);
	}
	
	
	class ButtonLoginListener implements EventHandler<ActionEvent>{
		public void handle(ActionEvent event){
			int idUser = ub.getUser(usernameField.getText(), passwordField.getText());
			if (idUser == -1){
				ErrorMessage("Error", "Authentification failed", "Incorrect username or password. Please try again.");
			}
			else{
				ub.setCurrentUser(idUser);
				
				String type = ub.getTypeUser(idUser);
				if(type.equals("employee")){
					Stage emStage = (Stage)((Node)event.getSource()).getScene().getWindow();
					emStage.setScene(EmployeeWindow());
					emStage.centerOnScreen();
				}
			}
		}
	}
	
	class AddClientListener implements EventHandler<ActionEvent>{
		public void handle(ActionEvent event){
			
			int op = cm.addClient(addNameClient.getText(), addCnpClient.getText(), addAddressClient.getText());
			
			if(op == -1){
				ErrorMessage("Error", "Failed creating a new client", "Incorrect cnp. Please try again.");
			}
			else if(op == -2){
				ErrorMessage("Error", "Failed creating a new client", "Incorrect name. Please try again.");
			}
			else if(op == -3){
				ErrorMessage("Error", "Failed creating a new client", "Incorrect address. Please try again.");
			}
			else{
				ub.addActivityForUser("Add client", "Client (" + addNameClient.getText() + ", " + addCnpClient.getText() + ", " + addAddressClient.getText() + ") was added.");
				SuccesMessage("Success", null, "Client have been added successfully");
				populateClient(clientTable);
			}
			
			addNameClient.clear();
			addCnpClient.clear();
			addAddressClient.clear();
		}
	}
	
	
	
	public void getUpdatedRow(List<String> ls){
		boolean changed = false;
		
		if(newName.equals("")){
			newName = ls.get(1);
		}else{
			changed = true;
		}
		if(newCnp.equals("")){
			newCnp = ls.get(2);
		}
		else{
			changed = true;
		}
		if(newAddress.equals("")){
			newAddress = ls.get(3);
		}
		else{
			changed = true;
		}
		
		if(changed){
			int op = cm.updateClient(Integer.valueOf(ls.get(0)), newName, newCnp, newAddress);
			
			if(op == -1){
				ErrorMessage("Error", "Failed updating the client", "Incorrect cnp. Please try again.");
			}
			else if(op == -2){
				ErrorMessage("Error", "Failed updating the client", "Incorrect name. Please try again.");
			}
			else if(op == -3){
				ErrorMessage("Error", "Failed updating the client", "Incorrect address. Please try again.");
			}
			else{
				ub.addActivityForUser("Update client", "Client " + ls.toString() + " was changed to [" + ls.get(0) + ", " + newName + ", " + newCnp + ", " + newAddress + "].");
				SuccesMessage("Success", null, "Client have been updated successfully");
				populateClient(clientTable);
			}
			
			newName = "";
			newCnp = "";
			newAddress = "";
		}
	}
}


