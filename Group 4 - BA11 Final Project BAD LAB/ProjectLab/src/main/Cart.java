package main;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import model.CartItem;
import model.User;
import util.Connect;

public class Cart {
	Stage ss;
	Connect connect = Connect.getInstance();
	User existingUser = new User();
	// menubar
	MenuBar mbCSCart;
	Menu menuHomeCSCart, menuCartCSCart, menuAccountCSCart;
	MenuItem miHomePageCSCart, miMyCartCSCart, miPurchaseHistoryCSCart, miLogoutCSCart;
	// Cart
	Stage popUpStage;
	Scene popUpScene;
	Scene CartScene;
	Label areYouSureLblC, titlePopUpLblC;
	HBox hbpopupC;
	VBox vbpopupC;
	Button yesBtnC, noBtnC;
	HBox titleHBPopUp;

	BorderPane bpC, bp2C;
	GridPane gpC;

	Label titleLblC;
	Label welcomeLblC;
	Label selectLblC;
	Label priceLblC;
	Label pNameLblC;
	Label quantityLblC;
	Label totalPriceLblC;
	Label OrderInfoLblC, userNameLblC, phoneNumberLblC, addressLblC;
	Label spinnerTotalLblC;
	Label noItemLblC, considerLblC;
	Text productDescC;
	TextFlow tfdescC;
	ArrayList<CartItem> ALCartItem = new ArrayList<>();
	ListView<String> cartLV;
	ObservableList<String> cartList;

	HBox hb1C, hb2C, hb3C, hb4C, hb5C;
	Spinner<Integer> quantitySpinner;
	Button updateCartBtnC, removeCartBtnC, makePurchaseBtnC;

	public Cart(Stage s, User exisUser) {
		if(exisUser.getRole().equalsIgnoreCase("Admin")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Cart Scene is only for customer");
			alert.show();
			return;
		}
		existingUser = exisUser;
//		System.out.println("Logged In  User(1):"+existingUser.getUserName());
		ss = s;
		menu();
		cart();
		eventListenerCart();
		eventListenerMenu();

		CartScene = new Scene(bpC, 800, 600);
		s.setScene(CartScene);
		s.setTitle("Cart");
	}

	private void eventListenerMenu() {
		// CS CART
		miHomePageCSCart.setOnAction(e -> {
			new Home(ss, existingUser);
//				gantiScene(HomeSceneCustomer, "Home");
		});
		miMyCartCSCart.setOnAction(e -> {
//				gantiScene(CartScene, "Cart");
			new Cart(ss, existingUser);
		});
		miLogoutCSCart.setOnAction(e -> {
			existingUser = null;
//			System.out.println("Logout");
//				gantiScene(loginScene, "Login");
			new Login(ss, existingUser);
		});
		miPurchaseHistoryCSCart.setOnAction(e->{
			new PurchaseHistory(ss, existingUser);
		});
	}

	private void eventListenerCart() {
		yesBtnC.setOnAction(e -> {
			if (cartList.isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Failed to make transaction");
				alert.showAndWait();
				popUpStage.close();
				return;
			} else {
//				System.out.println("Add Transac");
				String newTransId = "";
				String lastTransactionId = "";
				ArrayList<String> ALTransactionID = new ArrayList<>();
				try {
					ALTransactionID = getTransactionId();
				} catch (Exception e2) {
					System.out.println("Fail get Trans id(1)" + e2);
				}
				if (ALTransactionID.isEmpty()) {
//					System.out.println("No data in Transaction");
					newTransId = "TR001";
				} else {
					lastTransactionId = "TR001";
					for (String s : ALTransactionID) {
						if (lastTransactionId.compareTo(s) < 0) {
							lastTransactionId = s;
//							System.out.println("Last ID"+lastTransactionId);
						}
					}
					Integer i = Integer.parseInt(lastTransactionId.substring(2)) + 1;
					String id = String.format("%03d", i);
					newTransId = "TR" + id;
				}
//				System.out.println("Last ID in db:" + lastTransactionId);
//				System.out.println("new ID: " + newTransId);

				String queryaddTH = "INSERT INTO `transaction_header` (`transactionID`, `userID`) " + "VALUES ('"
						+ newTransId + "', '" + existingUser.getUserID() + "');";
				
				boolean success =	connect.execUpdate(queryaddTH);
				if(!success) {
					Alert a = new Alert(AlertType.ERROR);
					a.setHeaderText("Failed To Make Transaction");
					a.show();
					popUpStage.close();
					return;
				}	
//					System.out.println("Add TH");
				getCartData();
				for (CartItem c : ALCartItem) {
					String q = c.getQuantity();
					String pid = c.getProductID();
					String queryaddTD = "INSERT INTO `transaction_detail` (`transactionID`, `productID`, `quantity`) VALUES "
							+ "('" + newTransId + "', '" + pid + "', " + "'" + q + "');";
					
					boolean s=connect.execUpdate(queryaddTD);
					if(!s) {
						Alert a2 = new Alert(AlertType.ERROR);
						a2.setHeaderText("Failed To Make Transaction");
						a2.show();
						popUpStage.close();
						return;
					}
					}

				}
				// clear cart
//				System.out.println("Clear Cart");
				String queryclearcart = "DELETE FROM `cart` WHERE  cart.userID ='" + existingUser.getUserID() + "'";
				connect.execUpdate(queryclearcart);
				popUpStage.close();
				getCartData();
				generateObservableList();
				totalPriceLblC.setText("Total : Rp. " + getTotalPriceCart());
				cartLV.refresh();
				Alert a = new Alert(AlertType.INFORMATION);
				a.setHeaderText("Succesfully Purchased");
				a.show();

				if (cartList.isEmpty()) {
//					System.out.println("Cart Is Empty (1)");
					hb1C.getChildren().setAll(noItemLblC);
					hb2C.getChildren().setAll(considerLblC);
					hb3C.getChildren().clear();
					hb4C.getChildren().clear();
					hb5C.getChildren().clear();
				}
			
		});
		noBtnC.setOnAction(e -> {
			popUpStage.close();
		});
		makePurchaseBtnC.setOnAction(e -> {
			try {
				if (!popUpStage.isShowing()) {
					popUpStage.show();
				} else {
					popUpStage.close();
					popUpStage.show();
				}
			} catch (Exception e1) {
				System.out.println(e1);
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Fail To Make Purchase");
				alert.showAndWait();

			}
		});
		cartLV.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			int selectedID = cartLV.getSelectionModel().getSelectedIndex();
//			try {
//				cartList.get(0);
//			} catch (Exception e) {
//				System.out.println("1111");
//			}
			if (selectedID < 0) {
//				System.out.println("Out of Bound");
			}
//			System.out.println("Selected Index: " + selectedID);
			
			if (selectedID >= 0) {
//				System.out.println(">0");
				productDescC.setText(ALCartItem.get(selectedID).getDescription());
				priceLblC.setText("Price: Rp." + ALCartItem.get(selectedID).getProductPrice());
				pNameLblC.setText(ALCartItem.get(selectedID).getProductName());
				spinnerTotalLblC.setText("Total: ");
				String q = ALCartItem.get(selectedID).getQuantity();
				Integer qty = Integer.parseInt(q);
				quantityLblC.setText("Quantity: ");
				quantitySpinner = new Spinner<>(-999, 999, 0);

				hb1C.getChildren().setAll(pNameLblC);
				hb2C.getChildren().setAll(tfdescC);
				hb3C.getChildren().setAll(priceLblC);
				hb4C.getChildren().setAll(quantityLblC, quantitySpinner, spinnerTotalLblC);
				hb5C.getChildren().setAll(updateCartBtnC, removeCartBtnC);
				
				quantitySpinner.setOnMouseClicked(e -> {
					Integer qspinner = quantitySpinner.getValue();
					Integer p = Integer.parseInt(ALCartItem.get(selectedID).getProductPrice());
					Integer subtotal = qspinner * p;
					spinnerTotalLblC.setText("Total : Rp." + subtotal);
				});
				removeCartBtnC.setOnAction(e -> {
					try {

//						System.out.println("remove");
						String queryDelete = "DELETE FROM `cart` " + "WHERE `cart`.`productID` = '"
								+ ALCartItem.get(selectedID).getProductID() + "' " + "AND `cart`.`userID` = '"
								+ existingUser.getUserID() + "'";
						hb1C.getChildren().clear();
						hb2C.getChildren().clear();
						hb3C.getChildren().clear();
						hb4C.getChildren().clear();
						hb5C.getChildren().clear();
						boolean success = connect.execUpdate(queryDelete);
						if(!success) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setContentText("Fail To Remove Cart");
							alert.showAndWait();
							generateObservableList();
							return;
						}
						getCartData();
						generateObservableList();
						totalPriceLblC.setText("Total : Rp. " + getTotalPriceCart());
						cartLV.refresh();
						if (cartList.isEmpty()) {
//							System.out.println("Cart Is Empty (1)");
							hb1C.getChildren().setAll(noItemLblC);
							hb2C.getChildren().setAll(considerLblC);
							hb3C.getChildren().clear();
							hb4C.getChildren().clear();
							hb5C.getChildren().clear();
						}
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText("Deleted From Cart");
						alert.showAndWait();

					} catch (Exception e2) {

						System.out.println(e2);
						Alert alert = new Alert(AlertType.ERROR);
						alert.setContentText("Fail To Remove Cart");
						alert.showAndWait();
						generateObservableList();
					}
				});

				updateCartBtnC.setOnAction(e -> {
					try {

						Integer qspinner = quantitySpinner.getValue();
						Integer newQty = qty + qspinner;
//			    		System.out.println(newQty);
						if (qspinner == 0) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setHeaderText("Not a Valid Amount");
							alert.showAndWait();
						} else if (newQty == 0) {
							System.out.println("remove(update)");
							hb1C.getChildren().clear();
							hb2C.getChildren().clear();
							hb3C.getChildren().clear();
							hb4C.getChildren().clear();
							hb5C.getChildren().clear();
							String queryDelete = "DELETE FROM `cart` " + "WHERE `cart`.`productID` = '"
									+ ALCartItem.get(selectedID).getProductID() + "' " + "AND `cart`.`userID` = '"
									+ existingUser.getUserID() + "'";
							boolean success = connect.execUpdate(queryDelete);
							if(!success) {
								Alert alert = new Alert(AlertType.ERROR);
								alert.setContentText("Fail To Remove Cart");
								alert.showAndWait();
								generateObservableList();
								return;
							}
							getCartData();
							generateObservableList();
							cartLV.refresh();
							totalPriceLblC.setText("Total : Rp. " + getTotalPriceCart());
							System.out.println("11aa");
							if (cartList.isEmpty()) {
//								System.out.println("Cart Is Empty (3)");
								hb1C.getChildren().setAll(noItemLblC);
								hb2C.getChildren().setAll(considerLblC);
								hb3C.getChildren().clear();
								hb4C.getChildren().clear();
								hb5C.getChildren().clear();
							}
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setHeaderText("Deleted From Cart");
							alert.showAndWait();
						}

						else if (newQty < 0) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setHeaderText("Not a Valid Amount");
							alert.showAndWait();
						} else {
//							System.out.println("update qty");
							Connect connect = Connect.getInstance();
							String query = "UPDATE `cart` " + "SET `quantity` = '" + newQty + "' "
									+ "WHERE `cart`.`productID` = '" + ALCartItem.get(selectedID).getProductID() + "' "
									+ "AND `cart`.`userID` = '" + existingUser.getUserID() + "';";
							hb1C.getChildren().clear();
							hb2C.getChildren().clear();
							hb3C.getChildren().clear();
							hb4C.getChildren().clear();
							hb5C.getChildren().clear();
							connect.execUpdate(query);
							getCartData();
							generateObservableList();
							cartLV.refresh();

							totalPriceLblC.setText("Total : Rp. " + getTotalPriceCart());

							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setHeaderText("Updated Cart");
							alert.showAndWait();
						}

					} catch (Exception e2) {
						System.out.println(e2);
						Alert alert = new Alert(AlertType.ERROR);
						alert.setContentText("Fail To Update Cart");
						alert.showAndWait();
						generateObservableList();
					}
				});
			}
//			System.out.println(cartLV.getSelectionModel());
//			if (cartLV.getSelectionModel().isEmpty()) {
//				System.out.println("Cart Is Empty (2)");
//				hb1C.getChildren().setAll(noItemLblC);
//				hb2C.getChildren().setAll(considerLblC);
//				hb3C.getChildren().clear();
//				hb4C.getChildren().clear();
//				hb5C.getChildren().clear();
//			}

		});

	}

	private ArrayList<String> getTransactionId() {
		Connect connect = Connect.getInstance();
		ArrayList<String> ALTransId = new ArrayList<>();
		String queryget = "Select TransactionID From  transaction_header ";

		connect.execQuery(queryget);
		try {
			while (connect.rs.next()) {
				String TransactionID = connect.rs.getString("TransactionID");
				ALTransId.add(TransactionID);
			}
		} catch (SQLException e) {
			System.out.println("GetTransaction Fail");
		}

		return ALTransId;
	}

	private Integer getTotalPriceCart() {
		Integer total = 0;
		for (CartItem c : ALCartItem) {
			Integer price = Integer.parseInt(c.getProductPrice());
			Integer quantity = Integer.parseInt(c.getQuantity());
			Integer subtotal = price * quantity;
//			System.out.println(subtotal);
			total += subtotal;
		}
		return total;
	}

	private void menu() {
		// Cart
		mbCSCart = new MenuBar();
		menuHomeCSCart = new Menu("Home");
		menuCartCSCart = new Menu("Cart");
		menuAccountCSCart = new Menu("Account");
		miHomePageCSCart = new MenuItem("Home Page");
		miMyCartCSCart = new MenuItem("My Cart");
		miPurchaseHistoryCSCart = new MenuItem("Purchase History");
		miLogoutCSCart = new MenuItem("Log Out");

		menuHomeCSCart.getItems().setAll(miHomePageCSCart);
		menuCartCSCart.getItems().setAll(miMyCartCSCart);
		menuAccountCSCart.getItems().setAll(miLogoutCSCart, miPurchaseHistoryCSCart);
		mbCSCart.getMenus().addAll(menuHomeCSCart, menuCartCSCart, menuAccountCSCart);
	}

	private void cart() {
		getCartData();
		bpC = new BorderPane();
		bp2C = new BorderPane();
		gpC = new GridPane();

		quantityLblC = new Label();
		pNameLblC = new Label();
		titleLblC = new Label(existingUser.getUserName() + "'s Cart");
		welcomeLblC = new Label("Welcome, " + existingUser.getUserName());
		productDescC = new Text();
		priceLblC = new Label();
		spinnerTotalLblC = new Label();
		selectLblC = new Label("Select a product to add and remove");
		totalPriceLblC = new Label("Total : Rp. " + getTotalPriceCart());
		OrderInfoLblC = new Label("Order Information");
		userNameLblC = new Label("Username : " + existingUser.getUserName());
		phoneNumberLblC = new Label("Phone Number : " + existingUser.getPhone_num());
		addressLblC = new Label("Address : " + existingUser.getAddress());
		noItemLblC = new Label("No Item In Cart");
		considerLblC = new Label("Consider adding one!");

		updateCartBtnC = new Button("Update Cart");
		removeCartBtnC = new Button("Remove From Cart");
		makePurchaseBtnC = new Button("Make Purchase");

		cartList = FXCollections.observableArrayList();

		generateObservableList();

		cartLV = new ListView<String>(cartList);

		hb1C = new HBox(welcomeLblC);
		hb2C = new HBox(selectLblC);
		hb3C = new HBox();
		hb4C = new HBox();
		hb5C = new HBox();
		if (cartList.isEmpty()) {
			hb1C.getChildren().setAll(noItemLblC);
			hb2C.getChildren().setAll(considerLblC);
			hb3C.getChildren().clear();
			hb4C.getChildren().clear();
			hb5C.getChildren().clear();
		}

		// Pane Layout
		gpC.add(titleLblC, 0, 0);
		gpC.add(cartLV, 0, 1);
		gpC.add(hb1C, 1, 2);
		gpC.add(hb2C, 1, 3);
		gpC.add(hb3C, 1, 4);
		gpC.add(hb4C, 1, 5);
		gpC.add(hb5C, 1, 6);
		gpC.add(totalPriceLblC, 0, 7);
		gpC.add(OrderInfoLblC, 0, 8);
		gpC.add(userNameLblC, 0, 9);
		gpC.add(phoneNumberLblC, 0, 10);
		gpC.add(addressLblC, 0, 11);
		gpC.add(makePurchaseBtnC, 0, 12);
		GridPane.setRowSpan(cartLV, 6);
		GridPane.setRowSpan(hb5C, 2);
		bpC.setTop(mbCSCart);
		bpC.setCenter(gpC);
		// non css

		GridPane.setColumnSpan(titleLblC, 2);
		GridPane.setMargin(titleLblC, new Insets(20, 15, 10, 15));
		GridPane.setMargin(cartLV, new Insets(0, 15, 0, 15));
		GridPane.setMargin(totalPriceLblC, new Insets(5, 15, 5, 15));
		GridPane.setMargin(OrderInfoLblC, new Insets(5, 15, 5, 15));
		GridPane.setMargin(userNameLblC, new Insets(5, 15, 5, 15));
		GridPane.setMargin(phoneNumberLblC, new Insets(5, 15, 5, 15));
		GridPane.setMargin(addressLblC, new Insets(5, 15, 5, 15));
		GridPane.setMargin(makePurchaseBtnC, new Insets(5, 15, 5, 15));
		GridPane.setMargin(hb1C, new Insets(0, 0, 5, 0));
		GridPane.setMargin(hb2C, new Insets(5, 0, 5, 0));
		GridPane.setMargin(hb3C, new Insets(5, 0, 5, 0));
		GridPane.setMargin(hb4C, new Insets(5, 0, 5, 0));
		GridPane.setMargin(hb5C, new Insets(5, 0, 5, 0));
		HBox.setMargin(updateCartBtnC, new Insets(0, 5, 0, 0));
		HBox.setMargin(removeCartBtnC, new Insets(0, 5, 0, 5));
		HBox.setMargin(spinnerTotalLblC, new Insets(0, 0, 0, 5));
		HBox.setMargin(quantityLblC, new Insets(0, 5, 0, 0));
		removeCartBtnC.setPrefSize(150, 20);
		updateCartBtnC.setPrefSize(150, 20);
		makePurchaseBtnC.setPrefSize(150, 20);
		cartLV.setMaxSize(280, 170);

		tfdescC = new TextFlow(productDescC);
		tfdescC.setPrefWidth(300);
		cartLV.setPrefSize(300, 170);
		tfdescC.setPrefHeight(65);
		GridPane.setValignment(cartLV, VPos.TOP);

		// Css
		String fxbold = "-fx-font-weight:bold;";
		titleLblC.setStyle("-fx-font-size:30px;" + fxbold);
		noItemLblC.setStyle(fxbold);
		welcomeLblC.setStyle(fxbold);
		OrderInfoLblC.setStyle(fxbold);
		pNameLblC.setStyle("-fx-font-size:12px;" + fxbold);

		// pop up
		titlePopUpLblC = new Label("Order Confirmation");
		areYouSureLblC = new Label("Are you sure you want to make purchase?");
		yesBtnC = new Button("Yes");
		noBtnC = new Button("No");
		hbpopupC = new HBox(yesBtnC, noBtnC);
		vbpopupC = new VBox(areYouSureLblC, hbpopupC);
		titleHBPopUp = new HBox(titlePopUpLblC);
		// pane
		bp2C.setTop(titleHBPopUp);
		bp2C.setCenter(vbpopupC);
		popUpScene = new Scene(bp2C, 400, 300);
		popUpStage = new Stage();
		popUpStage.setTitle("Order Confirmation");
		popUpStage.setScene(popUpScene);

		titleHBPopUp.setAlignment(Pos.CENTER);
		hbpopupC.setSpacing(5);
		vbpopupC.setSpacing(5);
		yesBtnC.setPrefWidth(100);
		noBtnC.setPrefWidth(100);

		// css pop up
		titleHBPopUp.setStyle("-fx-background-color: #4c4f51;");
		titlePopUpLblC.setStyle("-fx-text-fill:white;-fx-font-weight: bold;-fx-font-size:16;");
		areYouSureLblC.setStyle("-fx-font-size:14;");
		bp2C.setStyle("-fx-background-color: #8eafc4;");
		vbpopupC.setAlignment(Pos.CENTER);
		hbpopupC.setAlignment(Pos.CENTER);
//		bp2C.setAlignment(vbpopupC, Pos.CENTER);
	}

	private void generateObservableList() {
		cartList.clear();
		try {
			for (CartItem c : ALCartItem) {
				String q = c.getQuantity();
				String pname = c.getProductName();
				String p = c.getProductPrice();
				Integer TotalPrice = Integer.parseInt(q) * Integer.parseInt(p);
				String listContent = q + "x " + pname + " (RP." + TotalPrice.toString() + ")";
				cartList.add(listContent);
			}
		} catch (Exception e) {
			System.out.println("generate observable list failed:" + e);
		}
	}

	private void getCartData() {
		String querygetcart = "SELECT * FROM cart c\r\n" + "JOIN product p on c.productID=p.productID\r\n"
				+ "WHERE userID='" + existingUser.getUserID() + "';";
//		System.out.println(querygetcart);
		connect.rs = connect.execQuery(querygetcart);
		try {
			ALCartItem.clear();
			while (connect.rs.next()) {

				String productName = connect.rs.getString("product_name");
//				Integer q =  connect.rs.getInt("quantity");
//				String quantity = q.toString();
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
}
