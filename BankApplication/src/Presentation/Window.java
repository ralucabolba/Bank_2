package Presentation;

import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

public class Window extends Application{

	private Button buttonEmployee;
	private Button buttonAdmin;
	private Button buttonLogin;
	
	private Label labelType;
	
	private Scene currentScene;
	
	private TabPane tabPaneEmployee;
	private TabPane tabPaneAdmin;
	
	private Tab tabClients;
	private Tab tabAccounts;
	private Tab tabTransfer;
	private Tab tabBills;
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		startWindow();
		
		primaryStage.setTitle("Bank Application");
		primaryStage.setScene(currentScene);
		primaryStage.show();
	}
	
	public void startWindow(){
		
		HBox hboxType = new HBox(20);
		VBox vboxType = new VBox(50);
		
		hboxType.setAlignment(Pos.CENTER);
		vboxType.setAlignment(Pos.CENTER);
		
		/*
		 * Buttons
		 */
		buttonEmployee = new Button("Employee");
		buttonAdmin = new Button("Administrator");
		
		addListenerButtonEmployee(new ButtonEmployeeListener());
		addListenerButtonAdmin(new ButtonAdminListener());
		/*
		 * Labels
		 */
		
		labelType = new Label("I am an ...");
		
		
		hboxType.getChildren().addAll(buttonEmployee, buttonAdmin);
		vboxType.getChildren().addAll(labelType, hboxType);
		
		Scene scene = new Scene(vboxType, 250, 400);
		
		currentScene = scene;
		
	}
	
	public Scene loginWindow(){
		VBox vboxLog = new VBox(20);
		HBox hboxUsername = new HBox(10);
		HBox hboxPassword = new HBox(10);
		
		vboxLog.setAlignment(Pos.CENTER);
		hboxUsername.setAlignment(Pos.CENTER);
		hboxPassword.setAlignment(Pos.CENTER);
		
		Label usernameLabel = new Label("Username");
		Label passwordLabel = new Label("Password");
		
		TextField usernameField = new TextField();
		PasswordField passwordField = new PasswordField();
		
		buttonLogin = new Button("Log in");
		addListenerButtonLogin(new ButtonLoginListener());
		
		hboxUsername.getChildren().addAll(usernameLabel, usernameField);
		hboxPassword.getChildren().addAll(passwordLabel, passwordField);
		
		vboxLog.getChildren().addAll(hboxUsername, hboxPassword, buttonLogin);
		
		Scene scene = new Scene(vboxLog, 250, 350);
		
		return scene;
		//currentScene = scene;
		
	}
	public Scene EmployeeWindow(){
		tabPaneEmployee = new TabPane();
		
		tabClients = new Tab("Clients");
		tabAccounts = new Tab("Accounts");
		tabBills = new Tab("Bills");
		tabTransfer = new Tab("Transfer money");
		
		HBox hboxClients = new HBox();
		HBox hboxAccounts = new HBox();
		HBox hboxBills = new HBox();
		HBox hboxTransfer = new HBox();
		
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
		Scene scene = new Scene(root, 400, 250, Color.CRIMSON);
		
		BorderPane borderPane = new BorderPane();
		borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());
        
        borderPane.setCenter(tabPaneEmployee);
        root.getChildren().add(borderPane);
        
        return scene;
	}
	public static void main(String[] args){
		//System.out.println("Hello");
		launch(args);
	}
	
	public void addListenerButtonEmployee(EventHandler<ActionEvent> act){
		buttonEmployee.setOnAction(act);
	}
	
	public void addListenerButtonAdmin(EventHandler<ActionEvent> act){
		buttonAdmin.setOnAction(act);
	}

	public void addListenerButtonLogin(EventHandler<ActionEvent> act){
		buttonLogin.setOnAction(act);
	}
	
	class ButtonEmployeeListener implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			loginStage.setScene(loginWindow());

		}
		
	}
	
	class ButtonAdminListener implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			loginStage.setScene(loginWindow());

		}
		
	}
	
	class ButtonLoginListener implements EventHandler<ActionEvent>{
		public void handle(ActionEvent event){
			Stage emStage = (Stage)((Node)event.getSource()).getScene().getWindow();
			emStage.setScene(EmployeeWindow());
		}
	}
}


