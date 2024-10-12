package main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import model.Transaction;
import model.User;
import util.Connect;

public class PurchaseHistory  {
	
	User existingUser = new User();
	Connect connect = Connect.getInstance();
	Connection connection = connect.getConnection();
	Stage ss;
	
	MenuBar mbPHistory;
	Menu menuHomePHistory, menuCartPHistory, menuAccountPHistory;
	MenuItem miHomePagePHistory, miMyCartPHistory, miPurchaseHistoryPHistory, miLogoutPHistory;
	
	Scene PurchaseHistoryScene;
	
	Label PurchaseHistoryLblPH, transactionIDLblPH, usernameLblPH, phoneNumberLblPH, addressLblPH,
	welcomingMessageLblPH, totalPriceLblPH;
	Text productDetailLblHC;
	TextFlow tfdescHC;
	
	HBox hb1PH, hb2PH, hb3PH, hb4PH, hb5PH, hb6PH;
	
	BorderPane bpPH;
	GridPane gpPH;
	
	ArrayList<Transaction> ALPurchases = new ArrayList<>();
	ArrayList<Transaction> ALPurchasesDetail = new ArrayList<>();
	ListView<String> transactionIDLVPH;
	ListView<String> transactionDetailLVPH;
	ObservableList<String> transactionIDListPH;
	ObservableList<String> transactionDetailListPH;
	
	public PurchaseHistory(Stage primaryStage, User exisUser) {
		ss = primaryStage;
		existingUser = exisUser;
		
		Initiallize();
		eventListenerPH();
		eventListenerMenuPH();
		primaryStage.setScene(PurchaseHistoryScene);
		primaryStage.setTitle("Purchase History");
	}

	private void Initiallize() {
		menuBar();
		bpPH = new BorderPane();
		gpPH = new GridPane();
		
		PurchaseHistoryLblPH = new Label(existingUser.getUserName() + "'s Purchase History"); 
		transactionIDLblPH = new Label("TransactionID : "); 
		usernameLblPH = new Label("Username : ");
		phoneNumberLblPH = new Label("Phone Number : "); 
		addressLblPH = new Label("Address : ");
		welcomingMessageLblPH = new Label("Select a Transaction to View Details"); 
		totalPriceLblPH = new Label("Total : ");
		
		transactionIDListPH = FXCollections.observableArrayList();
		transactionDetailListPH = FXCollections.observableArrayList();
		getTransactionData();
		generateObservableIDPH();
		transactionIDLVPH = new ListView<String>(transactionIDListPH);
		transactionDetailLVPH = new ListView<String>();
		
		hb1PH = new HBox(welcomingMessageLblPH);
		hb2PH = new HBox();
		hb3PH = new HBox();
		hb4PH = new HBox();
		hb5PH = new HBox();
		hb6PH = new HBox();
		
//		hb1PH = new HBox(transactionIDLblPH);
//		hb2PH = new HBox(usernameLblPH);
//		hb3PH = new HBox(phoneNumberLblPH);
//		hb4PH = new HBox(addressLblPH);
//		hb5PH = new HBox(totalPriceLblPH);
		
		gpPH.add(PurchaseHistoryLblPH, 0, 0);
		gpPH.add(transactionIDLVPH, 0, 1);
		gpPH.add(hb1PH, 1, 2);
		gpPH.add(hb2PH, 1, 3);
		gpPH.add(hb3PH, 1, 4);
		gpPH.add(hb4PH, 1, 5);
		gpPH.add(hb5PH, 1, 6);
		gpPH.add(hb6PH, 1, 7);
		GridPane.setRowSpan(transactionIDLVPH, 12);
		
		bpPH.setTop(mbPHistory);
		bpPH.setCenter(gpPH);
		
		GridPane.setColumnSpan(PurchaseHistoryLblPH, 2);
		GridPane.setMargin(PurchaseHistoryLblPH, new Insets(20, 15, 10, 15));
		GridPane.setMargin(transactionIDLVPH, new Insets(0, 15, 0, 15));
		GridPane.setMargin(hb1PH, new Insets(0, 0, 5, 0));
		GridPane.setMargin(hb2PH, new Insets(5, 0, 5, 0));
		GridPane.setMargin(hb3PH, new Insets(5, 0, 5, 0));
		GridPane.setMargin(hb4PH, new Insets(5, 0, 5, 0));
		GridPane.setMargin(hb5PH, new Insets(5, 0, 5, 0));
		transactionIDLVPH.setMaxSize(150, 320);
		transactionDetailLVPH.setMaxSize(300, 180);
		
		transactionIDLVPH.setPrefSize(150, 320);
		transactionDetailLVPH.setPrefSize(300, 180);
		GridPane.setValignment(transactionIDLVPH, VPos.TOP);		
		
		// Css
		String fxbold = "-fx-font-weight:bold;";
		PurchaseHistoryLblPH.setStyle("-fx-font-size:30px;" + fxbold);
		transactionIDLblPH.setStyle(fxbold);
		usernameLblPH.setStyle(fxbold);
				
		PurchaseHistoryScene = new Scene(bpPH, 800, 600);
	}

	private void menuBar() {
		mbPHistory = new MenuBar();
		menuHomePHistory = new Menu("Home"); 
		menuCartPHistory = new Menu("Cart"); 
		menuAccountPHistory = new Menu("Account");
		miHomePagePHistory = new MenuItem("Home Page");
		miMyCartPHistory = new MenuItem("Cart");
		miPurchaseHistoryPHistory = new MenuItem("Purchase History");
		miLogoutPHistory = new MenuItem("Log Out");
		
		menuHomePHistory.getItems().setAll(miHomePagePHistory);
		menuCartPHistory.getItems().setAll(miMyCartPHistory);
		menuAccountPHistory.getItems().setAll(miPurchaseHistoryPHistory, miLogoutPHistory);
		mbPHistory.getMenus().addAll(menuHomePHistory, menuCartPHistory, menuAccountPHistory);
	}
	
	private void eventListenerMenuPH() {
		//CS Home
		miHomePagePHistory.setOnAction(e->{
			new Home(ss, existingUser);
		});
		miMyCartPHistory.setOnAction(e->{
			new Cart(ss, existingUser);
		});
		miLogoutPHistory.setOnAction(e->{
			existingUser=null;
			new Login(ss, existingUser);
		});
		
		miPurchaseHistoryPHistory.setOnAction(e->{
			new PurchaseHistory(ss, existingUser);
		});
				
	}
	
	private void generateObservableIDPH() {
		transactionIDListPH.clear();
		try {
			String tid ="";
			String listContent = "";
			for (Transaction t : ALPurchases) {
				if(tid.contentEquals(t.getTransactionID())) { //check duplikat
//					System.out.println("skip");
					continue;
				}
				tid = t.getTransactionID();
				listContent = tid;
				transactionIDListPH.add(listContent);
			}
		} catch (Exception e) {
			System.out.println("generate observable list failed:" + e);
		}
	}
	
	private void generateObservableDetailPH(String transactionID) {
		transactionDetailListPH.clear();
		try {
			String listContent = "";
			for (Transaction t : ALPurchases) {
				if(!t.getTransactionID().contentEquals(transactionID)) {
//					System.out.println(t.getTransactionID() + "Skipped");
					continue;
				}
				int q = t.getQuantity();
				String n = t.getProductName();
				int p = t.getPrice();
				listContent = q + "x " + n + " (Rp." + p + ")";
				transactionDetailListPH.add(listContent);
			}
		} catch (Exception e) {
			System.out.println("generate observable list failed:" + e);
		}
	}
	
	private Integer getTotalPriceCart(String transactionID) {
		Integer total = 0;
		for (Transaction t : ALPurchases) {
			if(!t.getTransactionID().contentEquals(transactionID)) {
				continue;
			}
			Integer price = t.getPrice();
			Integer quantity = t.getQuantity();
			Integer subtotal = price * quantity;
			total += subtotal;
		}
		return total;
	}
	
	private void getTransactionData() {
		String queryGetTransaction ="SELECT * \r\n" + 
									"FROM transaction_header th \r\n" + 
									"JOIN transaction_detail td ON th.transactionID = td.transactionID \r\n" + 
									"JOIN user u ON th.userID = u.userID \r\n" + 
									"JOIN product p ON td.productID = p.productID \r\n" +
									"WHERE th.userID='" + existingUser.getUserID() + "';";
		connect.rs = connect.execQuery(queryGetTransaction);
		try {
			ALPurchases.clear();
			while (connect.rs.next()) {
				String transactionID = connect.rs.getString("transactionID");
				String userID = connect.rs.getString("userID");
				String productID = connect.rs.getString("productID");
				int quantity = connect.rs.getInt("quantity");
			    String username = connect.rs.getString("username");
			    String address = connect.rs.getString("address");
			    String phoneNumber = connect.rs.getString("phone_num");
			    int price = connect.rs.getInt("product_price");
			    String productName = connect.rs.getString("product_name");
				ALPurchases.add(new Transaction(transactionID, userID, productID, quantity, username, 
						address, phoneNumber, price, productName));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void eventListenerPH() {
		transactionIDLVPH.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			String selectedID = transactionIDLVPH.getSelectionModel().getSelectedItem();
				
			transactionIDLblPH.setText("Transaction ID : " + selectedID);
			usernameLblPH.setText("Username : " + existingUser.getUserName());
			phoneNumberLblPH.setText("Phone Number : " + existingUser.getPhone_num());
			addressLblPH.setText("Address : " + existingUser.getAddress());
			totalPriceLblPH.setText("Total : Rp." + getTotalPriceCart(selectedID));
			
			generateObservableDetailPH(selectedID);
			transactionDetailLVPH = new ListView<String>(transactionDetailListPH);
			
			hb1PH.getChildren().setAll(transactionIDLblPH);
			hb2PH.getChildren().setAll(usernameLblPH);
			hb3PH.getChildren().setAll(phoneNumberLblPH);
			hb4PH.getChildren().setAll(addressLblPH);
			hb5PH.getChildren().setAll(totalPriceLblPH);
			hb6PH.getChildren().setAll(transactionDetailLVPH);
			GridPane.setRowSpan(hb6PH, 6);

		});
	}
	
	

}
