package main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import model.CartItem;
import model.ProductItem;
import model.User;
import util.Connect;

public class Home {
	//	User  //
	User existingUser = new User();
	Connect connect = Connect.getInstance();
	Connection connection = connect.getConnection();
	Stage ss;
	
	//  List  //
	ArrayList<ProductItem> ALProductItem = new ArrayList<>();
	ArrayList<CartItem> ALCartItem = new ArrayList<>();
	ListView<String> ProductLV;
	ObservableList<String> productList;

	//  Pop-up  //
	Stage popUpStage;
	Scene popUpScene;
	
	// HomeScene-Customer(Dwi)//
	MenuBar mbCSHome;
	Menu menuHomeCSHome, menuCartCSHome, menuAccountCSHome;
	MenuItem miHomePageCSHome, miMyCartCSHome, miPurchaseHistoryCSHome, miLogoutCSHome;
	
	Scene HomeSceneCustomer;

	Label seRuputTehLblHC, productNameLblHC, productPriceLblHC, totalPriceLblHC, quantityLblHC,
			welcomingMessageLblHC, welcomingDetailLblHC, spinnerTotalLblHC, noItemLblHC, considerLblHC;
	Text productDetailLblHC;
	TextFlow tfdescHC;
	
	ListView<String> productLVHC;
	ObservableList<String> productNameListHC;
	Spinner<Integer> quantitySpnHC;
	Button addProductBtnHC;

	BorderPane bpHC;
	GridPane gpHC;
	HBox hb1HC, hb2HC, hb3HC, hb4HC, hb5HC;
	
	// HomeScene-Admin(Dwi)//
	MenuBar mbAHome;
	Menu menuHomeAHome, menuManageProductAHome, menuAccountAHome;
	MenuItem miHomePageAHome, miManageProductAHome, miPurchaseHistoryAHome, miLogoutAHome;
	
	Scene HomeSceneAdmin;

	Label seRuputTehLblHA, productNameLblHA, productPriceLblHA, totalPriceLblHA, quantityLblHA,
			welcomingMessageLblHA, welcomingDetailLblHA, noItemLblHA, considerLblHA;
	Text productDetailLblHA;
	TextFlow tfdescHA;
	
	ListView<String> productLVHA;
	ObservableList<String> productNameListHA;

	BorderPane bpHA;
	GridPane gpHA;
	HBox hb1HA, hb2HA, hb3HA;
	
	
	public Home(Stage primaryStage, User exisUser) {
		ss = primaryStage;
		existingUser = exisUser;
		if(exisUser.getRole().equalsIgnoreCase("Admin")) {
			initiallizeA();
			eventListenerMenuA();
			eventListenerHA();
			primaryStage.setScene(HomeSceneAdmin);
			primaryStage.setTitle("HOME");
			
		}else {
			initiallizeC();
			eventListenerMenuCS();
			eventListenerHC();
			primaryStage.setScene(HomeSceneCustomer);
			primaryStage.setTitle("Home");
		}
		
	}
	
	private void initiallizeC() {
		menuBarCS();
		bpHC = new BorderPane();
		gpHC = new GridPane();

		seRuputTehLblHC = new Label("SeRuput Teh");
		welcomingMessageLblHC = new Label("Welcome, " + existingUser.getUserName());
		welcomingDetailLblHC = new Label("Select a product to view");
		productNameLblHC = new Label();
		productDetailLblHC = new Text();
		tfdescHC = new TextFlow(productDetailLblHC);
		productPriceLblHC = new Label();
		totalPriceLblHC = new Label();
		quantityLblHC = new Label();
		spinnerTotalLblHC = new Label();
		noItemLblHC = new Label();
		considerLblHC = new Label();
		
		productNameListHC = FXCollections.observableArrayList();
		getProductData();
		generateObservableListHC();
		productLVHC = new ListView<String>(productNameListHC);

		quantitySpnHC = new Spinner<>();
		addProductBtnHC = new Button("Add to Cart");

		hb1HC = new HBox(welcomingMessageLblHC);
		hb2HC = new HBox(welcomingDetailLblHC);
		hb3HC = new HBox();
		hb4HC = new HBox();
		hb5HC = new HBox();
/*		if (productList.isEmpty()) {
			hb1HC.getChildren().setAll(noItemLblHC);
			hb2HC.getChildren().setAll(considerLblHC);
			hb3HC.getChildren().clear();
			hb4HC.getChildren().clear();
			hb5HC.getChildren().clear();
		}*/
		eventListenerHC();
		
		gpHC.add(seRuputTehLblHC, 0, 0);
		gpHC.add(productLVHC, 0, 1);
		gpHC.add(hb1HC, 1, 2);
		gpHC.add(hb2HC, 1, 3);
		gpHC.add(hb3HC, 1, 4);
		gpHC.add(hb4HC, 1, 5);
		gpHC.add(hb5HC, 1, 6);
		GridPane.setRowSpan(productLVHC, 12);

		bpHC.setTop(mbCSHome);
		bpHC.setCenter(gpHC);
		
		GridPane.setColumnSpan(seRuputTehLblHC, 2);
		GridPane.setMargin(seRuputTehLblHC, new Insets(20, 15, 10, 15));
		GridPane.setMargin(productLVHC, new Insets(0, 15, 0, 15));
		GridPane.setMargin(hb1HC, new Insets(0, 0, 5, 0));
		GridPane.setMargin(hb2HC, new Insets(5, 0, 5, 0));
		GridPane.setMargin(hb3HC, new Insets(5, 0, 5, 0));
		GridPane.setMargin(hb4HC, new Insets(5, 0, 5, 0));
		GridPane.setMargin(hb5HC, new Insets(5, 0, 5, 0));
		HBox.setMargin(addProductBtnHC, new Insets(0, 5, 0, 0));
		HBox.setMargin(spinnerTotalLblHC, new Insets(0, 0, 0, 5));
		HBox.setMargin(quantityLblHC, new Insets(0, 5, 0, 0));
		addProductBtnHC.setPrefSize(150, 20);
		productLVHC.setMaxSize(300, 320);
		
		tfdescHC.setPrefWidth(300);
		productLVHC.setPrefSize(300, 320);
		tfdescHC.setPrefHeight(65);
		GridPane.setValignment(productLVHC, VPos.TOP);		
		
		// Css
		String fxbold = "-fx-font-weight:bold;";
		seRuputTehLblHC.setStyle("-fx-font-size:30px;" + fxbold);
		noItemLblHC.setStyle(fxbold);
		welcomingMessageLblHC.setStyle(fxbold);
		productNameLblHC.setStyle("-fx-font-size:12px;" + fxbold);

		HomeSceneCustomer = new Scene(bpHC, 800, 600);
		/* pop up
		*/
	}

	private void menuBarCS() {
		// HomeCustomer(CSHome)
		mbCSHome = new MenuBar();
		menuHomeCSHome = new Menu("Home");
		menuCartCSHome = new Menu("Cart");
		menuAccountCSHome = new Menu("Account");
		miHomePageCSHome = new MenuItem("Home Page");
		miMyCartCSHome = new MenuItem("My Cart");
		miPurchaseHistoryCSHome = new MenuItem("Purchase History");
		miLogoutCSHome = new MenuItem("Log Out");
		
		menuHomeCSHome.getItems().setAll(miHomePageCSHome);
		menuCartCSHome.getItems().setAll(miMyCartCSHome);
		menuAccountCSHome.getItems().setAll(miLogoutCSHome, miPurchaseHistoryCSHome);
		mbCSHome.getMenus().addAll(menuHomeCSHome, menuCartCSHome, menuAccountCSHome);
		
	}
	

//	private void homeA(Stage primaryStage) {
//		initiallizeA();
//		menuBarA();
//		primaryStage.setScene(HomeSceneAdmin);
//		primaryStage.setTitle("HOME");
//	}

	private void initiallizeA() {
		menuBarA();
		bpHA = new BorderPane();
		gpHA = new GridPane();

		seRuputTehLblHA = new Label("SeRuput Teh");
		welcomingMessageLblHA = new Label("Welcome, " + existingUser.getUserName());
		welcomingDetailLblHA = new Label("Select a product to view");
		productNameLblHA = new Label();
		productDetailLblHA = new Text();
		tfdescHA = new TextFlow(productDetailLblHA);
		productPriceLblHA = new Label();
		noItemLblHA = new Label();
		considerLblHA = new Label();
		
		productNameListHA = FXCollections.observableArrayList();
		getProductData();
		generateObservableListHA();
		productLVHA = new ListView<String>(productNameListHA);

		hb1HA = new HBox(welcomingMessageLblHA);
		hb2HA = new HBox(welcomingDetailLblHA);
		hb3HA = new HBox();
//		if (productList.isEmpty()) {
//			hb1HA.getChildren().setAll(noItemLblHA);
//			hb2HA.getChildren().setAll(considerLblHA);
//			hb3HA.getChildren().clear();
//		}
		
		gpHA.add(seRuputTehLblHA, 0, 0);
		gpHA.add(productLVHA, 0, 1);
		gpHA.add(hb1HA, 1, 2);
		gpHA.add(hb2HA, 1, 3);
		gpHA.add(hb3HA, 1, 4);
		GridPane.setRowSpan(productLVHA, 12);

		bpHA.setTop(mbAHome);
		bpHA.setCenter(gpHA);
		
		GridPane.setColumnSpan(seRuputTehLblHA, 2);
		GridPane.setMargin(seRuputTehLblHA, new Insets(20, 15, 10, 15));
		GridPane.setMargin(productLVHA, new Insets(0, 15, 0, 15));
		GridPane.setMargin(hb1HA, new Insets(0, 0, 5, 0));
		GridPane.setMargin(hb2HA, new Insets(5, 0, 5, 0));
		GridPane.setMargin(hb3HA, new Insets(5, 0, 5, 0));
		productLVHA.setMaxSize(300, 320);
		
		tfdescHA.setPrefWidth(300);
		productLVHA.setPrefSize(300, 320);
		tfdescHA.setPrefHeight(65);
		GridPane.setValignment(productLVHA, VPos.TOP);		
		
		// Css
		String fxbold = "-fx-font-weight:bold;";
		seRuputTehLblHA.setStyle("-fx-font-size:30px;" + fxbold);
		noItemLblHA.setStyle(fxbold);
		welcomingMessageLblHA.setStyle(fxbold);
		productNameLblHA.setStyle("-fx-font-size:12px;" + fxbold);
		
		HomeSceneAdmin = new Scene(bpHA, 800, 600);
	}

	private void menuBarA() {
		// HomeCustomer(CSHome)
		mbAHome = new MenuBar();
		menuHomeAHome = new Menu("Home");
		menuManageProductAHome = new Menu("Manage Product");
		menuAccountAHome = new Menu("Account");
		miHomePageAHome = new MenuItem("Home Page");
		miPurchaseHistoryAHome = new MenuItem("Purchase History");
		miLogoutAHome = new MenuItem("Log Out");
		miManageProductAHome = new MenuItem("Manage Product");
		
		menuHomeAHome.getItems().setAll(miHomePageAHome);
		menuAccountAHome.getItems().setAll(miLogoutAHome);
		menuManageProductAHome.getItems().setAll(miManageProductAHome);
		mbAHome.getMenus().addAll(menuHomeAHome, menuManageProductAHome, menuAccountAHome);
	}
	


	private void generateObservableListHC() {
		productNameListHC.clear();
		try {
			for (ProductItem c : ALProductItem) {
				String pname = c.getProductName();
				String listContent = pname;
				productNameListHC.add(listContent);
			}
		} catch (Exception e) {
			System.out.println("generate observable list failed:" + e);
		}
	}
	
	private void generateObservableListHA() {
		productNameListHA.clear();
		try {
			for (ProductItem p : ALProductItem) {
				String pname = p.getProductName();
				String listContent = pname;
				productNameListHA.add(listContent);
			}
		} catch (Exception e) {
			System.out.println("generate observable list failed:" + e);
		}
	}

	private void gantiScene(Scene target, String title) {
		ss.setScene(target);
		ss.setTitle(title);
	}

	private void eventListenerMenuCS() {
		//CS Home
		miHomePageCSHome.setOnAction(e->{
			//gantiScene(HomeSceneCustomer, "Home");
		});
		miMyCartCSHome.setOnAction(e->{
			new Cart(ss, existingUser);
		});
		miLogoutCSHome.setOnAction(e->{
			existingUser=null;
//			gantiScene(loginScene, "Login");
//			System.out.println("Logout");
			new Login(ss, existingUser);
		});
		
		miPurchaseHistoryCSHome.setOnAction(e->{
			new PurchaseHistory(ss, existingUser);
		});
				
	}
	
	private void eventListenerMenuA() {
		//CS Home
		miHomePageAHome.setOnAction(e->{
			gantiScene(HomeSceneAdmin, "Home");
		});
		miManageProductAHome.setOnAction(e->{
			new ManageProduct(ss, existingUser);
//			System.out.println("ManageProductMenu");
		});
		miLogoutAHome.setOnAction(e->{
			existingUser=null;
//			gantiScene(loginScene, "Login");
//			System.out.println("Logout");
			new Login(ss, existingUser);
		});
				
	}
	
	private void getProductData() {
		String queryGetProduct = "SELECT * FROM product";
//		System.out.println(querygetcart);
		connect.rs = connect.execQuery(queryGetProduct);
		try {
			ALProductItem.clear();
			while (connect.rs.next()) {

				String productName = connect.rs.getString("product_name");
//				Integer q =  connect.rs.getInt("quantity");
//				String quantity = q.toString();
				String productID = connect.rs.getString("productID");
				int productPrice = connect.rs.getInt("product_price");
				String description = connect.rs.getString("product_des");
				ALProductItem.add(new ProductItem(productID, productName, productPrice, description));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void getCartData() {
		String querygetcart = "SELECT * FROM cart c\r\n" + "JOIN product p on c.productID=p.productID\r\n"
				+ "WHERE userID='" + existingUser.getUserID() + "';";
		connect.rs = connect.execQuery(querygetcart);
		try {
			ALCartItem.clear();
			while (connect.rs.next()) {
				String productName = connect.rs.getString("product_name");
				String quantity = connect.rs.getString("quantity");
				String productID = connect.rs.getString("productID");
				String productPrice = connect.rs.getString("product_price");
				String description = connect.rs.getString("product_des");
				ALCartItem.add(new CartItem(productID, productName, productPrice, description, quantity));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void eventListenerHC() {
		productLVHC.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			int selectedID = productLVHC.getSelectionModel().getSelectedIndex();
			if (selectedID < 0) {
				System.out.println("Out of Bound");
			}

			if (selectedID >= 0) {
//				System.out.println(">0");
				productDetailLblHC.setText(ALProductItem.get(selectedID).getDescription());
				productPriceLblHC.setText("Price: Rp." + ALProductItem.get(selectedID).getProductPrice());
				productNameLblHC.setText(ALProductItem.get(selectedID).getProductName());
				spinnerTotalLblHC.setText("Total: ");
				quantityLblHC.setText("Quantity: ");
				quantitySpnHC = new Spinner<>(-999, 999, 0);

				hb1HC.getChildren().setAll(productNameLblHC);
				hb2HC.getChildren().setAll(tfdescHC);
				hb3HC.getChildren().setAll(productPriceLblHC);
				hb4HC.getChildren().setAll(quantityLblHC, quantitySpnHC, spinnerTotalLblHC);
				hb5HC.getChildren().setAll(addProductBtnHC);

				quantitySpnHC.setOnMouseClicked(e -> {
					Integer qspinner = quantitySpnHC.getValue();
					Integer p = ALProductItem.get(selectedID).getProductPrice();
					Integer subtotal = qspinner * p;
					spinnerTotalLblHC.setText("Total : " + subtotal);
				});
				
				addProductBtnHC.setOnAction(e -> {
					getCartData();
					
					int cartSize = ALCartItem.size();
					int counter = 0;
					String productID = ALProductItem.get(selectedID).getProductID();
					String user = existingUser.getUserID();
					Integer quantity = quantitySpnHC.getValue();
					
					String queryInsert = String.format("INSERT INTO cart VALUES (\"%s\", \"%s\", \"%d\")", productID, user, quantity);
					
					if(quantity == 0) {
						Alert error = new Alert(AlertType.ERROR);
						error.setContentText("Add to Cart Failed");
						error.show();
					}else if(ALCartItem.isEmpty()) {
						if(quantity <= 0) {
							Alert error = new Alert(AlertType.ERROR);
							error.setHeaderText("Add to Cart Failed");
							error.show();
						}else {
							connect.execUpdate(queryInsert);
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setContentText("Added to Cart");
							alert.show();
						}
					}else {
						for (CartItem cartItem : ALCartItem) {
							counter++;
//							System.out.println("counter: "+ counter);
							if(cartItem.getProductID().contentEquals(productID)) {
								Integer cartQuantity = Integer.parseInt(cartItem.getQuantity());
								Integer newQuantity = cartQuantity + quantity;
								
								if(newQuantity <= 0) {
									if(cartQuantity > 0) {
										String queryDelete = "DELETE FROM `cart` " + "WHERE `cart`.`productID` = '"
												+ productID + "' " + "AND `cart`.`userID` = '"
												+ existingUser.getUserID() + "'";
										
										connect.execUpdate(queryDelete);
										
										Alert alert = new Alert(AlertType.INFORMATION);
										alert.setHeaderText("Added to Cart");
										alert.show();
										break;
									}else {
										Alert error = new Alert(AlertType.ERROR);
										error.setHeaderText("Add to Cart Failed");
										error.show();
										break;
									}
									
								}else {
									String queryUpdate = String.format("UPDATE `cart` " + "SET `quantity` = '" + (cartQuantity + quantity) + "' "
											+ "WHERE `cart`.`productID` = '" + productID + "' "
											+ "AND `cart`.`userID` = '" + existingUser.getUserID() + "';");
									
									connect.execUpdate(queryUpdate);
									
									Alert alert = new Alert(AlertType.INFORMATION);
									alert.setHeaderText("Added to Cart");
									alert.show();
									break;
								}
								
							}else if(counter == (cartSize)) {
								if(quantity <= 0) {
									Alert error = new Alert(AlertType.ERROR);
									error.setHeaderText("Add to Cart Failed");
									error.show();
									break;
								}
								
								boolean insert = connect.execUpdate(queryInsert);
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setHeaderText("Added to Cart");
								alert.show();
								
								if(!insert) {
									Alert error = new Alert(AlertType.ERROR);
									error.setHeaderText("Add to Cart Failed");
									error.show();
								}
								break;
							}else {
								continue;
							}
						}
					}
					
				});

			}

		});
	}
	
	private void eventListenerHA() {
		productLVHA.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			int selectedID = productLVHA.getSelectionModel().getSelectedIndex();
			if (selectedID < 0) {
//				System.out.println("Out of Bound");
			}

			if (selectedID >= 0) {
//				System.out.println(">0");
				productDetailLblHA.setText(ALProductItem.get(selectedID).getDescription());
				productPriceLblHA.setText("Price: Rp." + ALProductItem.get(selectedID).getProductPrice());
				productNameLblHA.setText(ALProductItem.get(selectedID).getProductName());

				hb1HA.getChildren().setAll(productNameLblHA);
				hb2HA.getChildren().setAll(tfdescHA);
				hb3HA.getChildren().setAll(productPriceLblHA);

			}

		});
	}

}