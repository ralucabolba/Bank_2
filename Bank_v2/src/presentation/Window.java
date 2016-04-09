package presentation;
import java.sql.Date;

import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.sun.rowset.CachedRowSetImpl;

import business.*;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.*;
import javafx.geometry.Insets;
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

public class Window extends Application{

	private String newName = "";
	private String newCnp = "";
	private String newAddress = "";
	private String newTypeAc = "";
	private String newBalance = "";
	private String newEmUsername = "";
	private String newEmType = "";
	
	private static User ub;
	private static Client cm;
	private static Account am;
	private static Activity actm;
	
	
	private Button buttonLogin;
	private Button buttonAddClient;
	private Button buttonAddAccount;
	private Button buttonTransfer;
	private Button buttonReport;
	
	private Scene currentScene;
	
	private TabPane tabPaneEmployee;
	
	private Tab tabClients;
	private Tab tabAccounts;
	private Tab tabTransfer;
	private Tab tabBills;
	
	private TextField usernameField;
	private TextField addNameClient;
	private TextField addCnpClient;
	private TextField addAddressClient;
	private TextField sumTransfer;
	
	private TableView<List<String>> clientTable = new TableView<>();
	private TableView<List<String>> accountTable = new TableView<>();
	private TableView<List<String>> emTable = new TableView<>();
	private TableView<List<String>> actTable = new TableView<>();
	
	private PasswordField passwordField;
	
	private ComboBox<String> addTypeAccount;
	private ComboBox<String> addOwner;
	private ComboBox<String> ems;
	private ComboBox<String> sourceAccount;
	private ComboBox<String> targetAccount;
	
	
	private ObservableList<String> clientsIds;
	private ObservableList<String> accountsIds;
	private ObservableList<String> emIds;
	private TextField addUsernameEm;
	private PasswordField addPasswordEm;
	private Button buttonAddEm;
	
	private DatePicker from, to;
	private ComboBox<String> clientBill;
	private ComboBox<String> providerCombo;
	private TextField billField;
	private TextField sumBillField;
	private Button buttonPay;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		currentScene = loginWindow();
		
		primaryStage.setTitle("Bank Application");
		primaryStage.setScene(currentScene);
		primaryStage.show();
		primaryStage.centerOnScreen();
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
		
		usernameField = new TextField();
		passwordField = new PasswordField();
		
		buttonLogin = new Button("Log in");
		addListenerButtonLogin(new ButtonLoginListener());
		
		hboxUsername.getChildren().addAll(usernameLabel, usernameField);
		hboxPassword.getChildren().addAll(passwordLabel, passwordField);
		
		vboxLog.getChildren().addAll(hboxUsername, hboxPassword, buttonLogin);
		
		Scene scene = new Scene(vboxLog, 250, 350, Color.POWDERBLUE);
		
		return scene;
		
	}
	@SuppressWarnings("unchecked")
	
	public Scene AdminWindow(){
		TabPane tabPaneAdmin = new TabPane();
		
		Tab tabEm = new Tab("Employees");
		Tab tabAct = new Tab("Activities");
		
		VBox vboxEm = new VBox(20);
		VBox vboxAct = new VBox(20);
		
		/***********Employees' table******************/
		emTable.setEditable(true);
		emTable.setMinWidth(600);
		emTable.setMaxHeight(600);
		
		Label label = new Label("Employees");
		populateEmployees(emTable);
		
		TableColumn<List<String>, String> idCol = 
				new TableColumn<List<String>, String>("Id employee");
		idCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(0));
			}
		});
		
		idCol.setMinWidth(50);
		
		TableColumn<List<String>, String> usernameCol = 
				new TableColumn<List<String>, String>("Username");
		usernameCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(1));
			}
		});
		
		usernameCol.setMinWidth(100);
		usernameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		usernameCol.setOnEditCommit(
			    new EventHandler<CellEditEvent<List<String>, String>>() {
			        @Override
			        public void handle(CellEditEvent<List<String>, String> t) {
			        	newEmUsername = t.getNewValue();
			        }
			    }
			);
		
		TableColumn<List<String>, String> typeCol = 
				new TableColumn<List<String>, String>("Type user");
		typeCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(2));
			}
		});
		
		typeCol.setMinWidth(100);
		typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
		typeCol.setOnEditCommit(
			    new EventHandler<CellEditEvent<List<String>, String>>() {
			        @Override
			        public void handle(CellEditEvent<List<String>, String> t) {
			        	newEmType = t.getNewValue();
			        }
			    }
			);
		
		TableColumn<List<String>, String> updateCol = 
				new TableColumn<List<String>, String>("Update");
		Callback<TableColumn<List<String>, String>, TableCell<List<String>, String>> cell3Factory = //
                new Callback<TableColumn<List<String>, String>, TableCell<List<String>, String>>()
                {
                    @Override
                    public TableCell call( final TableColumn<List<String>, String> param )
                    {
                        TableCell<List<String>, String> cell = new TableCell<List<String>, String>()
                        {

                        	//Button buttonDeleteClient = new Button( "Delete" );
                        	Button buttonUpdateAccount = new Button("Update");
                        	
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
                                	buttonUpdateAccount.setOnAction(event -> getUpdatedEmRow((List<String>) getTableRow().getItem()));
                                    setGraphic(buttonUpdateAccount);
                                    setText( null );
                                }
                            }
                        };
                        return cell;
                    }
                };

        updateCol.setCellFactory( cell3Factory );
        
        
        TableColumn<List<String>, String> deleteCol = 
				new TableColumn<List<String>, String>("Delete");
		Callback<TableColumn<List<String>, String>, TableCell<List<String>, String>> cell4Factory = //
                new Callback<TableColumn<List<String>, String>, TableCell<List<String>, String>>()
                {
                    @Override
                    public TableCell call( final TableColumn<List<String>, String> param )
                    {
                        TableCell<List<String>, String> cell = new TableCell<List<String>, String>()
                        {
                        	Button buttonDeleteAccount = new Button("Delete");
                        	
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
                                	buttonDeleteAccount.setOnAction(event -> getDeletedEmRow((List<String>) getTableRow().getItem()));
                                	
                                    setGraphic(buttonDeleteAccount);
                                    setText( null );
                                }
                            }
                        };
                        return cell;
                    }
                };

        deleteCol.setCellFactory( cell4Factory );
        
		
		emTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		emTable.getColumns().addAll(idCol, usernameCol, typeCol, updateCol, deleteCol);
		
		HBox addEmBox = new HBox(20);
		
		addUsernameEm = new TextField();
		addUsernameEm.setPromptText("Username");
		
		addPasswordEm = new PasswordField();
		addPasswordEm.setPromptText("Password");
		
		
		buttonAddEm = new Button("Add employee");
		addListenerAddEmployee(new AddEmployeeListener());
		
		addEmBox.getChildren().addAll(addUsernameEm, addPasswordEm,  buttonAddEm);
		
		Button buttonLogout1 = new Button("Log out");
		addListenerButtonLogout(new LogoutListener(), buttonLogout1);
		
		StackPane stackEm = new StackPane();
		stackEm.getChildren().add(emTable);
		
		vboxEm.getChildren().addAll(label, stackEm, addEmBox, buttonLogout1);
		
		vboxEm.setAlignment(Pos.CENTER);
		vboxEm.setPrefSize(300, 300);
		
		/**************************Activity panel*****************************/
		
		Label labelDate = new Label("Select period for report :");
		from = new DatePicker();
		to = new DatePicker();
		
		HBox selectDateBox = new HBox(20);
		selectDateBox.setAlignment(Pos.TOP_LEFT);
		selectDateBox.getChildren().addAll(labelDate, from, to);
		
		
		Label labelIdEm = new Label("Select employee's id :");
		
		emIds = FXCollections.observableArrayList();
		getEmployees(emIds);
		
		ems = new ComboBox<String>(emIds);
		ems.setPromptText("Select employee");
	
		
		buttonReport = new Button("Generate report");
		addListenerGenerateReport(new GenerateReportListener());
		
		HBox selectEmBox = new HBox(20);
		selectEmBox.setAlignment(Pos.TOP_LEFT);
		selectEmBox.getChildren().addAll(labelIdEm, ems);
		
		actTable.setEditable(true);
		actTable.setMinWidth(600);
		actTable.setMaxHeight(600);
		
		
		TableColumn<List<String>, String> idActCol = 
				new TableColumn<List<String>, String>("Id activity");
		idActCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(0));
			}
		});
		
		idActCol.setMinWidth(50);
		
		TableColumn<List<String>, String> idUserCol = 
				new TableColumn<List<String>, String>("Id employee");
		idUserCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(1));
			}
		});
		idUserCol.setMinWidth(50);
		
		TableColumn<List<String>, String> dateCol = 
				new TableColumn<List<String>, String>("Date");
		dateCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(2));
			}
		});
		
		dateCol.setMinWidth(50);
		
		TableColumn<List<String>, String> typeActCol = 
				new TableColumn<List<String>, String>("Type");
		typeActCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(3));
			}
		});
		
		typeActCol.setMinWidth(50);
		
		TableColumn<List<String>, String> descrCol = 
				new TableColumn<List<String>, String>("Description");
		descrCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(4));
			}
		});
		
		
		descrCol.setMinWidth(50);

		actTable.setFixedCellSize(60.0);
		actTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		actTable.getColumns().addAll(idActCol, idUserCol, dateCol, typeActCol, descrCol);
		
		Button buttonLogout2 = new Button("Log out");
		addListenerButtonLogout(new LogoutListener(), buttonLogout2);
		
		StackPane stackAct = new StackPane();
		stackAct.getChildren().add(actTable);
		
		vboxAct.getChildren().addAll(selectDateBox, selectEmBox, buttonReport, stackAct, buttonLogout2);
		vboxAct.setPrefSize(300, 300);
		vboxAct.setPadding(new Insets(50));
		
		vboxEm.setPadding(new Insets(50));
		
		tabEm.setContent(vboxEm);
		tabAct.setContent(vboxAct);

		
		tabPaneAdmin.getTabs().addAll(tabEm, tabAct);
		
		Group root = new Group();
		Scene scene = new Scene(root, 800, 600, Color.POWDERBLUE);
		
		BorderPane borderPane = new BorderPane();
		borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());
        
        borderPane.setCenter(tabPaneAdmin);
        root.getChildren().add(borderPane);
        
        return scene;
	}




	public Scene EmployeeWindow(){
		tabPaneEmployee = new TabPane();
		
		tabClients = new Tab("Clients");
		tabAccounts = new Tab("Accounts");
		tabBills = new Tab("Bills");
		tabTransfer = new Tab("Transfer money");
		
		VBox hboxClients = new VBox(20);
		VBox hboxAccounts = new VBox(20);
		VBox hboxBills = new VBox(20);
		VBox hboxTransfer = new VBox(20);
		
		/*Account table*/
		
		accountTable.setEditable(true);
		accountTable.setMinWidth(600);
		accountTable.setMaxHeight(600);
		
		Label labelA = new Label("Accounts");
		populateAccount(accountTable);
		
		TableColumn<List<String>, String> idAcCol = 
				new TableColumn<List<String>, String>("Id account");
		idAcCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(0));
			}
		});
		
		idAcCol.setMinWidth(50);
		
		TableColumn<List<String>, String> idClientCol = 
				new TableColumn<List<String>, String>("Id client");
		idClientCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(1));
			}
		});
		
		idClientCol.setMinWidth(50);
		
		TableColumn<List<String>, String> ownerCol = 
				new TableColumn<List<String>, String>("Name owner");
		ownerCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(2));
			}
		});
		
		ownerCol.setMinWidth(100);
		
		TableColumn<List<String>, String> typeAcCol = 
				new TableColumn<List<String>, String>("Type");
		typeAcCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(3));
			}
		});
		
		typeAcCol.setMinWidth(100);
		typeAcCol.setCellFactory(TextFieldTableCell.forTableColumn());
		typeAcCol.setOnEditCommit(
			    new EventHandler<CellEditEvent<List<String>, String>>() {
			        @Override
			        public void handle(CellEditEvent<List<String>, String> t) {
			        	newTypeAc = t.getNewValue();
			        }
			    }
			);
		
		TableColumn<List<String>, String> balanceCol = 
				new TableColumn<List<String>, String>("Balance");
		balanceCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(4));
			}
		});
		
		balanceCol.setMinWidth(100);
		balanceCol.setCellFactory(TextFieldTableCell.forTableColumn());
		balanceCol.setOnEditCommit(
			    new EventHandler<CellEditEvent<List<String>, String>>() {
			        @Override
			        public void handle(CellEditEvent<List<String>, String> t) {
			        	newBalance = t.getNewValue();
			        }
			    }
			);
		
		TableColumn<List<String>, String> dateAcCol = 
				new TableColumn<List<String>, String>("Date creation");
		dateAcCol.setCellValueFactory(new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<List<String>, String> data){
				return new ReadOnlyStringWrapper(data.getValue().get(5));
			}
		});
		
		dateAcCol.setMinWidth(100);
		
		TableColumn<List<String>, String> updateACol = 
				new TableColumn<List<String>, String>("Update");
		Callback<TableColumn<List<String>, String>, TableCell<List<String>, String>> cell3Factory = //
                new Callback<TableColumn<List<String>, String>, TableCell<List<String>, String>>()
                {
                    @Override
                    public TableCell call( final TableColumn<List<String>, String> param )
                    {
                        TableCell<List<String>, String> cell = new TableCell<List<String>, String>()
                        {
                        	Button buttonUpdateAccount = new Button("Update");
                        	
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
                                	buttonUpdateAccount.setOnAction(event -> getUpdatedAccountRow((List<String>) getTableRow().getItem()));
                                    setGraphic(buttonUpdateAccount);
                                    setText( null );
                                }
                            }
                        };
                        return cell;
                    }
                };

        updateACol.setCellFactory( cell3Factory );
        
        
        TableColumn<List<String>, String> deleteACol = 
				new TableColumn<List<String>, String>("Delete");
		Callback<TableColumn<List<String>, String>, TableCell<List<String>, String>> cell4Factory = //
                new Callback<TableColumn<List<String>, String>, TableCell<List<String>, String>>()
                {
                    @Override
                    public TableCell call( final TableColumn<List<String>, String> param )
                    {
                        TableCell<List<String>, String> cell = new TableCell<List<String>, String>()
                        {
                        	Button buttonDeleteAccount = new Button("Delete");
                        	
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
                                	buttonDeleteAccount.setOnAction(event -> getDeletedAccountRow((List<String>) getTableRow().getItem()));
                                	
                                    setGraphic(buttonDeleteAccount);
                                    setText( null );
                                }
                            }
                        };
                        return cell;
                    }
                };

        deleteACol.setCellFactory( cell4Factory );
        
		accountTable.getColumns().addAll(idAcCol, idClientCol, ownerCol, typeAcCol, balanceCol, dateAcCol, updateACol, deleteACol);
		accountTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		
		
		/****************************************************Client table********************************************/
		
		clientTable.setEditable(true);
		clientTable.setMinWidth(600);
		clientTable.setMaxHeight(600);
		
		
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
                                	buttonUpdateClient.setOnAction(event -> getUpdatedRow((List<String>) getTableRow().getItem()));
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
		HBox addAccountBox = new HBox(20);
		
		addNameClient = new TextField();
		addNameClient.setPromptText("Name");
		
		addCnpClient = new TextField();
		addCnpClient.setPromptText("Cnp");
		
		addAddressClient = new TextField();
		addAddressClient.setPromptText("Address");
		
		buttonAddClient = new Button("Add client");
		addListenerAddClient(new AddClientListener());
		
		addClientBox.getChildren().addAll(addNameClient, addCnpClient, addAddressClient, buttonAddClient);
		
		
		addTypeAccount = new ComboBox(FXCollections.observableArrayList("Saving account", "Spending account"));
		addTypeAccount.setPromptText("Select type");
		
		clientsIds = FXCollections.observableArrayList();
		getClients(clientsIds);
		
		addOwner = new ComboBox(clientsIds);
		addOwner.setPromptText("Select client");
		
	
		buttonAddAccount = new Button("Add account");
		addListenerAddAccount(new AddAccountListener());
		
		addAccountBox.getChildren().addAll(addTypeAccount, addOwner, buttonAddAccount);
		/**********************Pay bills************************************************/
		Label blabel = new Label("Pay bill");
		
		clientBill = new ComboBox(clientsIds);
		clientBill.setPromptText("Select client");
		
		providerCombo = new ComboBox(FXCollections.observableArrayList("Telekom", "Orange", "Vodafone", "RCS&RDS", "UPC", "E-ON Energie"));
		providerCombo.setPromptText("Select provider");
		
		billField = new TextField();
		
		sumBillField = new TextField();
		
		buttonPay = new Button("Pay");
		
		addListenerButtonPay(new ButtonPayListener());
		
		/**********************Transfer money panel**************************************/
		
		Label sLabel = new Label("Transfer money");
		
		accountsIds = FXCollections.observableArrayList();
		
		getAccounts(accountsIds);
		
		sourceAccount = new ComboBox(accountsIds);
		targetAccount = new ComboBox(accountsIds);
		
		sourceAccount.setPromptText("Select source account");
		targetAccount.setPromptText("Select destination account");
		
		sourceAccount.setMinWidth(100);
		targetAccount.setMinWidth(100);
		
		sumTransfer = new TextField();
		sumTransfer.setPromptText("Introduce amount");
		sumTransfer.setMaxWidth(150);
		
		buttonTransfer = new Button("Transfer money");
		addListenerButtonTransfer(new ButtonTransferListener());
		
		Button buttonLogout1 = new Button("Log out");
		addListenerButtonLogout(new LogoutListener(), buttonLogout1);
		
		Button buttonLogout2 = new Button("Log out");
		addListenerButtonLogout(new LogoutListener(), buttonLogout2);
		
		Button buttonLogout3 = new Button("Log out");
		addListenerButtonLogout(new LogoutListener(), buttonLogout3);
		
		Button buttonLogout4 = new Button("Log out");
		addListenerButtonLogout(new LogoutListener(), buttonLogout4);
		
		StackPane stackClient = new StackPane();
		stackClient.getChildren().add(clientTable);
		
		StackPane stackAccount = new StackPane();
		stackAccount.getChildren().add(accountTable);
		
		hboxClients.getChildren().addAll(labelC, stackClient, addClientBox, buttonLogout1);
		
		hboxClients.setMinWidth(400);
		hboxClients.setAlignment(Pos.CENTER);
		
		hboxAccounts.setMinWidth(400);
		hboxAccounts.getChildren().addAll(labelA, stackAccount, addAccountBox, buttonLogout2);
		hboxAccounts.setAlignment(Pos.CENTER);
		
		hboxBills.setMinWidth(400);
		
		VBox vleft = new VBox(20);
		vleft.getChildren().addAll(new Label("Select client:"),
								   new Label("Select bill provider:"),
								   new Label("Introduce bill number:"),
								   new Label("Introduce sum:"));
		
		VBox vright = new VBox(20);
		vright.getChildren().addAll(clientBill, providerCombo, billField, sumBillField);
		
		vleft.setAlignment(Pos.CENTER_LEFT);
		vright.setAlignment(Pos.CENTER_LEFT);
		
		HBox h1 = new HBox(20);
		h1.getChildren().addAll(vleft, vright);

		hboxBills.getChildren().addAll(blabel, h1, buttonPay, buttonLogout3);
		hboxBills.setAlignment(Pos.CENTER);
		
		hboxTransfer.getChildren().addAll(sLabel, sourceAccount, targetAccount, sumTransfer, buttonTransfer, buttonLogout4);
		hboxTransfer.setAlignment(Pos.CENTER_LEFT);
		hboxTransfer.setPrefSize(300, 300);
		
		hboxClients.setPadding(new Insets(50));
		hboxAccounts.setPadding(new Insets(50));
		hboxBills.setPadding(new Insets(50));
		hboxTransfer.setPadding(new Insets(50));
		
		tabClients.setContent(hboxClients);
		tabAccounts.setContent(hboxAccounts);
		tabBills.setContent(hboxBills);
		tabTransfer.setContent(hboxTransfer);
		
		tabPaneEmployee.getTabs().addAll(tabClients, tabAccounts, tabBills, tabTransfer);
		
		Group root = new Group();
		Scene scene = new Scene(root, 800, 600, Color.POWDERBLUE);
		
		BorderPane borderPane = new BorderPane();
		borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());
        
        borderPane.setCenter(tabPaneEmployee);
        root.getChildren().add(borderPane);
        
        return scene;
	}
	
	public void getEmployees(ObservableList<String> ems){
		List<String> ids = ub.getEmployeesIds();
		ems.setAll(ids);
	}
	public void getAccounts(ObservableList<String> accounts){
		List<String> ids = am.getAccountsIds();
		accounts.setAll(ids);
	}
	public void getClients(ObservableList<String> clients) {
		List<String> ids = cm.getClientsIds();
		clients.setAll(ids);
	}


	public void populateEmployees(TableView emTable){
		emTable.getItems().clear();
		
		List<List<String>> ems = ub.getEmployees();
		ObservableList<List<String>> data = FXCollections.observableArrayList(ems);
		emTable.setItems(data);
	}
	
	public void populateActivities(TableView actTable, int idUser, java.util.Date from, java.util.Date to){
		actTable.getItems().clear();
		
		List<List<String>> acts = actm.getActivitiesForUser(idUser, from, to);
		ObservableList<List<String>> data = FXCollections.observableArrayList(acts);
		actTable.setItems(data);
	}

	public void populateAccount(TableView accountTable){
		accountTable.getItems().clear();
		
		List<List<String>> accs = am.getAccounts();
		ObservableList<List<String>> data = FXCollections.observableArrayList(accs);
		accountTable.setItems(data);
	}
	public void populateClient(TableView clientTable){
		clientTable.getItems().clear();
		
		List<List<String>> cs = cm.getClients();
		ObservableList<List<String>> data = FXCollections.observableArrayList(cs);
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
		ub = new User("jdbc:mysql://localhost:3306/bank?autoReconnect=true&useSSL=false", "root", "admin");
		cm = new Client("jdbc:mysql://localhost:3306/bank?autoReconnect=true&useSSL=false", "root", "admin");
		am = new Account("jdbc:mysql://localhost:3306/bank?autoReconnect=true&useSSL=false", "root", "admin");
		actm = new Activity("jdbc:mysql://localhost:3306/bank?autoReconnect=true&useSSL=false", "root", "admin");
		launch(args);
	}
	
	

	public void addListenerButtonLogin(EventHandler<ActionEvent> act){
		buttonLogin.setOnAction(act);
	}
	
	public void addListenerButtonLogout(EventHandler<ActionEvent> act, Button button){
		button.setOnAction(act);
	}
	
	public void addListenerAddClient(EventHandler<ActionEvent> act){
		buttonAddClient.setOnAction(act);
	}
	
	public void addListenerAddAccount(EventHandler<ActionEvent> act){
		buttonAddAccount.setOnAction(act);
	}
	public void addListenerButtonTransfer(EventHandler<ActionEvent> act){
		buttonTransfer.setOnAction(act);
	}
	
	public void addListenerAddEmployee(EventHandler<ActionEvent> act){
		buttonAddEm.setOnAction(act);
	}
	
	public void addListenerGenerateReport(EventHandler<ActionEvent> act){
		buttonReport.setOnAction(act);
	}
	
	public void addListenerButtonPay(EventHandler<ActionEvent> act){
		buttonPay.setOnAction(act);
	}
	
	class ButtonPayListener implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			if(clientBill.getValue() == null || providerCombo.getValue() == null || billField.getText().equals("") || sumBillField.getText().equals("")){
				ErrorMessage("Error", "Error paying bills", "You must fill in all fields in order to pay a bill.");
			}
			else{
				try{
					int op = ub.payBill(Integer.valueOf(clientBill.getValue()), providerCombo.getValue().toString(), billField.getText().toString(), Float.valueOf(sumBillField.getText()));
					if(op == -1){
						ErrorMessage("Error", "Error paying bills", "Client with id = " + clientBill.getValue() + " has no accounts");
					}
					else if(op == -2){
						ErrorMessage("Error", "Error paying bills", "Sum too large. Please try again.");
					}
					else if(op == -3){
						ErrorMessage("Error", "Error paying bills", "There are no accounts.");
					}
					else if(op == -4){
						ErrorMessage("Error", "Error paying bills", "Invalid sum. Sum must be positive.");
					}
					else{
						SuccesMessage("Succes", "Bill paid", "Bill has been paid successfully.");
						actm.addActivityForUser(ub.getId(), "Bill payment", "Bill no " + billField.getText() + " from \n " + 
											providerCombo.getValue().toString() + "\nfor client " + 
											cm.getClientNameById(Integer.valueOf(clientBill.getValue().toString())) +
											" with sum " + sumBillField.getText() + " LEI has been paid");
						populateAccount(accountTable);
						getAccounts(accountsIds);
					}
				}
				catch(NumberFormatException e){
					e.printStackTrace();
					ErrorMessage("Error", "Error paying bills", "Invalid sum. Please try again.");
				}
				
			}
		}
		
	}
	class LogoutListener implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			ub.logout();
			
			emTable.getColumns().clear();
			actTable.getColumns().clear();
			accountTable.getColumns().clear();
			clientTable.getColumns().clear();
			
			Stage emStage = (Stage)((Node)event.getSource()).getScene().getWindow();
			emStage.setScene(loginWindow());
			emStage.centerOnScreen();
		}
		
	}
	class GenerateReportListener implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			
			if(from.getValue() != null && to.getValue() != null && ems.getValue() != null && to.getValue().compareTo(from.getValue()) > 0 ){
				java.util.Date fromDate =  Date.from(from.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
				java.util.Date toDate =  Date.from(to.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
				populateActivities(actTable, Integer.valueOf(ems.getValue().toString()), fromDate, toDate);
			}
			else{
				ErrorMessage("Error", "Error reporting activities", "You must select the period and the employee's id for which you want to generate the report.");
			}
		}
		
	}
	class ButtonLoginListener implements EventHandler<ActionEvent>{
		public void handle(ActionEvent event){
			
			if (!ub.authentificate(usernameField.getText(), passwordField.getText())){
				ErrorMessage("Error", "Authentification failed", "Incorrect username or password. Please try again.");
			}
			else{
				String type = ub.getType();
				
				if(type.equals("employee")){
					Stage emStage = (Stage)((Node)event.getSource()).getScene().getWindow();
					emStage.setScene(EmployeeWindow());
					emStage.centerOnScreen();
				}
				else if(type.equals("administrator")){
					Stage adminStage = (Stage)((Node)event.getSource()).getScene().getWindow();
					adminStage.setScene(AdminWindow());
					adminStage.centerOnScreen();
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
			else if(op == -4){
				ErrorMessage("Error", "Failed creating a new client", "Incorrect cnp. A client with this cnp already exists. Please try again.");
			}
			else{
				actm.addActivityForUser(ub.getId(), "Add client", "Client (" + addNameClient.getText() + ", " + addCnpClient.getText() + ", " + addAddressClient.getText() + ") was added.");
				SuccesMessage("Success", null, "Client have been added successfully");
				populateClient(clientTable);
				getClients(clientsIds);
			}
			
			addNameClient.clear();
			addCnpClient.clear();
			addAddressClient.clear();
		}
	}
	
	class AddAccountListener implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			if(addTypeAccount.getValue() != null && addOwner.getValue() != null){
				String type = addTypeAccount.getValue().toString();
				String owner = addOwner.getValue().toString();
				
				am.addNewAccount(type, Integer.valueOf(owner));
				
				populateAccount(accountTable);
				getAccounts(accountsIds);
				actm.addActivityForUser(ub.getId(), "Add account", "Account [" + addTypeAccount.getValue().toString() + ", " + addOwner.getValue().toString() + ", 0.0] was added.");
			}else{
				ErrorMessage("Error", "Failed adding account", "Failed adding a new account. Make sure you select both the type and the client of the account.");
			}
			
		}
		
	}
	
	class ButtonTransferListener implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			if(sourceAccount.getValue() == null || targetAccount.getValue() == null || sumTransfer == null){
				ErrorMessage("Error", "Failed transfering money", "You must provide all information in order to transfer money.");
			}
			else{
				int op = am.transferMoney(Integer.valueOf(sourceAccount.getValue().toString()), Integer.valueOf(targetAccount.getValue().toString()), sumTransfer.getText());
				if(op == -4){
					ErrorMessage("Error", "Failed transfering money", "Source account and destination account are the same.");
				}
				else if(op == -3){
					ErrorMessage("Error", "Failed transfering money", "Please try again later.");
				}
				else if(op == -2){
					ErrorMessage("Error", "Failed transfering money", "Invalid amount of money.");
				}
				else if(op == -1){
					ErrorMessage("Error", "Failed transfering money", "Amount of money too large.");
				}
				else{
					SuccesMessage("Succes", "Money transfered", "Money have been transfered successfully.");
					actm.addActivityForUser(ub.getId(), "Transfer money", sumTransfer.getText() + "LEI were \n transfered from account with id = " + sourceAccount.getValue().toString() + " to account with id = " + targetAccount.getValue().toString());
					populateAccount(accountTable);
					getAccounts(accountsIds);
				}
			}
		}
		
	}
	
	class AddEmployeeListener implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			int op = ub.addEmployee(addUsernameEm.getText(), addPasswordEm.getText());
			if(op == -1){
				ErrorMessage("Error", "Failed adding employee", "Invalid username. Username can contain : alphanumeric characters, ., _ and -.");
			}
			else if(op == -2){
				ErrorMessage("Error", "Failed adding employee", "Invalid password. It must contains at least 6 characters. It can contain : alphanumeric characters, ., _ and -.");
			}
			else if(op == -3){
				ErrorMessage("Error", "Failed adding employee", "Incorrect username. This username already exists. Please try again.");
			}
			else{
				SuccesMessage("Succes", "Employee added", "The employee has been added successfully.");
				populateEmployees(emTable);
				getEmployees(emIds);
			}
			addUsernameEm.clear();
			addPasswordEm.clear();
		}
		
	}
	public void getDeletedAccountRow(List<String> ls){
		am.deleteAccount(Integer.valueOf(ls.get(0)));
		actm.addActivityForUser(ub.getId(), "Delete account", "Account " + ls.remove(1).toString() + " was deleted.");
		populateAccount(accountTable);
		getAccounts(accountsIds);
	}
	
	public void getUpdatedAccountRow(List<String> ls){
		boolean changed = false;
		
		if(newTypeAc.equals("")){
			newTypeAc = ls.get(3);
		}else{
			changed = true;
		}
		if(newBalance.equals("")){
			newBalance = ls.get(4);
		}
		else{
			changed = true;
		}
		
		
		if(changed){
			int op = am.updateAccount(Integer.valueOf(ls.get(0)), Integer.valueOf(ls.get(1)), newTypeAc, Float.valueOf(newBalance));
			
			if(op == -1){
				ErrorMessage("Error", "Failed updating the account", "Incorrect type. Please try again.");
			}
			else if(op == -2){
				ErrorMessage("Error", "Failed updating the account", "Incorrect balance. Please try again.");
			}
			else{
				actm.addActivityForUser(ub.getId(), "Update account", "Account " + ls.toString() + " was \n changed to [" + ls.get(0) + ", " + ls.get(0) + ", " + newTypeAc + ", " + newBalance + ",  " + ls.get(4) + "].");
				SuccesMessage("Success", null, "Account has been updated successfully");
				populateAccount(accountTable);
			}
			
			newTypeAc = "";
			newBalance = "";
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
			else if(op == -4){
				ErrorMessage("Error", "Failed creating a new client", "Incorrect cnp. A client with this cnp already exists. Please try again.");
			}
			else{
				actm.addActivityForUser(ub.getId(), "Update client", "Client " + ls.toString() + " was \n changed to [" + ls.get(0) + ", " + newName + ", " + newCnp + ", " + newAddress + "].");
				SuccesMessage("Success", null, "Client have been updated successfully");
				populateClient(clientTable);
			}
			
			newName = "";
			newCnp = "";
			newAddress = "";
		}
	}

	public void getDeletedEmRow(List<String> ls){

		ub.deleteUser(Integer.valueOf(ls.get(0)));
		populateEmployees(emTable);
		getEmployees(emIds);
	}
	
	public void getUpdatedEmRow(List<String> ls){
		boolean changed = false;
		
		if(newEmUsername.equals("")){
			newEmUsername = ls.get(1);
		}else{
			changed = true;
		}
		if(newEmType.equals("")){
			newEmType = ls.get(2);
		}
		else{
			changed = true;
		}
		
		
		if(changed){
			int op = ub.updateUser(Integer.valueOf(ls.get(0)), newEmUsername, newEmType);
			
			if(op == -1){
				ErrorMessage("Error", "Failed updating the employee", "Incorrect username. Please try again.");
			}
			else if(op == -2){
				ErrorMessage("Error", "Failed updating the employee", "Incorrect type. Please try again.");
			}
			else if(op == -3){
				ErrorMessage("Error", "Failed updating the employee", "Incorrect username. This username already exists. Please try again.");
			}
			else{
				SuccesMessage("Success", null, "Employee have been updated successfully");
				populateEmployees(emTable);
				getEmployees(emIds);
			}
			
			newEmUsername = "";
			newEmType = "";
		}
	}
}


