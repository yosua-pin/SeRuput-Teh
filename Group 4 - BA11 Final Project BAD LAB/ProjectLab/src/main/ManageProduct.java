package main;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import model.ProductItem;
import model.User;
import util.Connect;

public class ManageProduct {
	Stage ss;
	Connect connect = Connect.getInstance();
	User existingUser = new User();
	
	//MENU BAR MANAGE PRODUCT
	MenuBar mbAMP;
	Menu menuHomeAMP, menuManageProductAMP, menuAccountAMP;
	MenuItem miHomePageAMP, miLogoutAMP, miManageProductAMP;
	
	//Manage Product Components
	Scene MPScene;
	Label titleLblM, welcomeLblM, selectLblM, productNameLblM, descriptionLblM, priceLblM,
	inputPriceLblM, inputDetailLblM, inputNameLblM, updateLblM, removeProductLblM;
	Button addBtnMP, addBtnMP2, updateBtnMP, updateBtnMP2, removeBtnMP, removeBtnMP2, backBtnMP;
	TextFlow descFlow;
	Text descriptionMP;
	TextField InputPNameTF, InputPPriceTF, updatePPriceTF;
	TextArea InputPDescTA;
	
	HBox hb1M, hb2M, hb3M, hb4M, hb5M;
	VBox vbAddProduct, vbUpdateProduct;
	
	ArrayList<String> ALProductName = new ArrayList<>();
	ArrayList<ProductItem> ALProduct = new ArrayList<>();
	ListView<String> productLVMP;
	ObservableList<String> productListMP;
	
	BorderPane bpMP;
	GridPane gpMP;
	
	private void menu() {
		// Cart
		mbAMP = new MenuBar();
		menuHomeAMP = new Menu("Home");
		menuManageProductAMP = new Menu("Manage Product");
		menuAccountAMP = new Menu("Account");
		miHomePageAMP = new MenuItem("Home Page");
		miLogoutAMP = new MenuItem("Log Out");
		miManageProductAMP = new MenuItem("Manage Product");
		
		menuHomeAMP.getItems().setAll(miHomePageAMP);
		menuAccountAMP.getItems().setAll(miLogoutAMP);
		menuManageProductAMP.getItems().setAll(miManageProductAMP);
		mbAMP.getMenus().addAll(menuHomeAMP, menuManageProductAMP, menuAccountAMP);
	}
	
	public ManageProduct(Stage s,User existingUser) {
		if(existingUser.getRole().equalsIgnoreCase("Customer")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Manage Product Scene is only for admin");
			alert.show();
			return;
		}
		this.existingUser = existingUser;
		ss = s;
		menu();
		manageProduct();
		eventListenermanageProduct();
		eventListenerMenu();
		
		
		MPScene = new Scene(bpMP,800,600);
		s.setScene(MPScene);
		s.setTitle("Products");
	}
	
	private void manageProduct() {
		getProductData();
		bpMP = new BorderPane();
		gpMP = new GridPane();
		
		titleLblM = new Label("Manage Products");
		welcomeLblM = new Label("Welcome, " + existingUser.getUserName()); // build by ervan
		selectLblM = new Label("Select a Product to Update");
		inputDetailLblM = new Label("Input product description..");
		inputNameLblM = new Label("Input product name");
		inputPriceLblM = new Label("Input product price");
		updateLblM = new Label("Update Product");
		descriptionLblM = new Label();
		productNameLblM = new Label();
		priceLblM = new Label();
		descriptionMP = new Text();
		removeProductLblM = new Label("Are you sure, you want to remove this\n" + "product?");
		
		InputPDescTA = new TextArea();
		InputPNameTF = new TextField();
		InputPPriceTF = new TextField();
		updatePPriceTF = new TextField();
		
		addBtnMP = new Button("Add Product");
		addBtnMP2 = new Button("Add Product");
		updateBtnMP = new Button("Update Product");
		updateBtnMP2 = new Button("Update Product");
		removeBtnMP = new Button("Remove Product");
		removeBtnMP2 = new Button("Remove Product");
		backBtnMP = new Button("Back");
		
		productListMP = FXCollections.observableArrayList();
		generateProductObserableList();
		productLVMP = new ListView<>(productListMP);
		
		
		hb1M = new HBox(welcomeLblM);
		hb2M = new HBox(selectLblM);
		hb3M = new HBox(addBtnMP);
		hb4M = new HBox();
		hb5M = new HBox();
		
		vbAddProduct = new VBox(inputNameLblM, InputPNameTF, inputPriceLblM, InputPPriceTF, inputDetailLblM, InputPDescTA);
		vbUpdateProduct = new VBox(updateLblM, updatePPriceTF);
		
		gpMP.add(titleLblM, 0, 0);
		gpMP.add(productLVMP, 0, 1);
		gpMP.add(hb1M, 1, 1);
		gpMP.add(hb2M, 1, 2);
		gpMP.add(hb3M, 1, 3);
		gpMP.add(hb4M, 1, 4);
		gpMP.add(hb5M, 1, 5);
		GridPane.setRowSpan(productLVMP, 6);


		bpMP.setTop(mbAMP);
		bpMP.setCenter(gpMP);
		
		
		//non-css
		GridPane.setMargin(titleLblM, new Insets(20, 15, 10, 15));
		GridPane.setMargin(productLVMP, new Insets(0, 15, 0, 15));
		GridPane.setMargin(hb1M, new Insets(0, 0, 5, 0));
		GridPane.setMargin(hb2M, new Insets(5, 0, 5, 0));
		GridPane.setMargin(hb3M, new Insets(5, 0, 5, 0));
		GridPane.setMargin(hb4M, new Insets(5, 0, 5, 0));
		GridPane.setMargin(hb5M, new Insets(5, 0, 5, 0));
		HBox.setMargin(selectLblM, new Insets(0, 0, 12, 0));
		HBox.setMargin(updateBtnMP, new Insets(0, 8, 0, 0));
		HBox.setMargin(updateBtnMP2, new Insets(0, 8, 0, 0));
		HBox.setMargin(removeBtnMP, new Insets(0, 8, 0, 5));
		HBox.setMargin(removeBtnMP2, new Insets(0, 8, 0, 0));
		HBox.setMargin(addBtnMP, new Insets(0, 0, 8, 0));
		HBox.setMargin(addBtnMP2, new Insets(0, 8, 0, 0));
		HBox.setMargin(backBtnMP, new Insets(0, 0, 0, 0));
		productLVMP.setMaxSize(300, 320);
		GridPane.setValignment(productLVMP, VPos.TOP);
		
		VBox.setMargin(inputNameLblM, new Insets(0, 0, 8, 0));
		VBox.setMargin(inputPriceLblM, new Insets(0, 0, 8, 0));
		VBox.setMargin(inputDetailLblM, new Insets(0, 0, 8, 0));
		VBox.setMargin(InputPNameTF, new Insets(0, 0, 8, 0));
		VBox.setMargin(InputPPriceTF, new Insets(0, 0, 8, 0));
		VBox.setMargin(InputPDescTA, new Insets(0, 0, 8, 0));
		VBox.setMargin(updateLblM, new Insets(0, 0, 8, 0));
		VBox.setMargin(updatePPriceTF, new Insets(0, 0, 8, 0));
		
		descFlow = new TextFlow(descriptionMP);
		descFlow.setPrefWidth(300);
		descFlow.setPrefHeight(65);
		productLVMP.setPrefSize(300, 320);
		
		InputPNameTF.setMinWidth(350);
		InputPPriceTF.setMinWidth(350);
		InputPDescTA.setMinWidth(350);
		InputPDescTA.setMaxWidth(350);
		InputPDescTA.setMinHeight(120);
		InputPDescTA.setMaxHeight(120);
		updatePPriceTF.setMinWidth(350);
		
		InputPNameTF.setPromptText("input product name..");
		InputPPriceTF.setPromptText("input product price..");
		InputPDescTA.setPromptText("input product description..");
		updatePPriceTF.setPromptText("input new price..");
		
		selectLblM.setPadding(new Insets(0,0, 10, 0));
		addBtnMP.setMinWidth(130);
		addBtnMP2.setMinWidth(130);
		updateBtnMP.setMinWidth(130);
		updateBtnMP2.setMinWidth(130);
		removeBtnMP.setMinWidth(130);
		removeBtnMP2.setMinWidth(130);
		backBtnMP.setMinWidth(130);
		
		// Css
			String fxbold = "-fx-font-weight:bold;";
			titleLblM.setStyle("-fx-font-size:30px;" + fxbold);
			welcomeLblM.setStyle(fxbold);
			productNameLblM.setStyle("-fx-font-size:15px;" + fxbold);
			updateLblM.setStyle("-fx-font-size:15px;" + fxbold);
			inputNameLblM.setStyle("-fx-font-size:13px;" + fxbold);
			inputPriceLblM.setStyle("-fx-font-size:13px;" + fxbold);
			inputDetailLblM.setStyle("-fx-font-size:13px;" + fxbold);
			removeProductLblM.setStyle("-fx-font-size:15px;" + fxbold);
			
		
//		removeBtnMP.setMinSize(120, 30);
//		updateBtnMP.setMinSize(120, 30);
//		addBtnMP.setMinSize(120, 30);
	}

	
	private void generateProductObserableList() {
		productListMP.clear();
		try {
			for (ProductItem p : ALProduct) {
				String pname3 = p.getProductName();
				
				productListMP.add(pname3);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("generate observable list failed:" + e);
		}
	}
	

	private void getProductData() {
		String querygetproduct = "SELECT * FROM product";

		connect.rs = connect.execQuery(querygetproduct);
		try {
			ALProduct.clear();
			ALProductName.clear();
			while (connect.rs.next()) {

				String productName = connect.rs.getString("product_name");
				String productID = connect.rs.getString("productID");
				int productPrice = connect.rs.getInt("product_price");
				String description = connect.rs.getString("product_des");
				ALProduct.add(new ProductItem(productID, productName, productPrice, description));
				ALProductName.add(productName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private void eventListenermanageProduct() {

		//BELOM VALIDASI PRICE > 0 (DI FUNCTION ADD & UPDATE) 
		addBtnMP.setOnAction(e ->{
			hb1M.getChildren().setAll(welcomeLblM);
			hb2M.getChildren().setAll(selectLblM);
			hb3M.getChildren().setAll(vbAddProduct);
			hb4M.getChildren().setAll(addBtnMP2, backBtnMP);
			hb5M.getChildren().setAll();
			
			backBtnMP.setOnAction(ee ->{
					hb1M.getChildren().setAll(welcomeLblM);
					hb2M.getChildren().setAll(selectLblM);
					hb3M.getChildren().setAll(addBtnMP);
					hb4M.getChildren().setAll();
					hb5M.getChildren().setAll();
					
					InputPNameTF.clear();
					InputPDescTA.clear();
					InputPPriceTF.clear();
			});
		});
		
		productLVMP.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			int selectedID = productLVMP.getSelectionModel().getSelectedIndex();
			if (selectedID < 0) {
//				System.out.println("Out of Bound");
				productLVMP.getSelectionModel().clearSelection();
				productLVMP.getSelectionModel().clearSelection(selectedID);
				
				addBtnMP.setOnAction(e ->{
					hb1M.getChildren().setAll(welcomeLblM);
					hb2M.getChildren().setAll(selectLblM);
					hb3M.getChildren().setAll(vbAddProduct);
					hb4M.getChildren().setAll(addBtnMP2, backBtnMP);
					hb5M.getChildren().setAll();
					
				});
			}
			if (selectedID >= 0) {
				getProductData();
				productNameLblM.setText(ALProduct.get(selectedID).getProductName());
				descriptionMP.setText(ALProduct.get(selectedID).getDescription());
				String a = "Price: RP." + ALProduct.get(selectedID).getProductPrice();
				priceLblM.setText(a);
				
				hb1M.getChildren().setAll(productNameLblM);
				hb2M.getChildren().setAll(descFlow);
				hb3M.getChildren().setAll(priceLblM);
				hb4M.getChildren().setAll(addBtnMP);
				hb5M.getChildren().setAll(updateBtnMP, removeBtnMP);
				
				addBtnMP.setOnAction(e ->{
					hb1M.getChildren().setAll(productNameLblM);
					hb2M.getChildren().setAll(descFlow);
					hb3M.getChildren().setAll(priceLblM);
					hb4M.getChildren().setAll(vbAddProduct);
					hb5M.getChildren().setAll(addBtnMP2, backBtnMP);
					
				});
				
				
				backBtnMP.setOnAction(e ->{
					if (selectedID >= 0) {
						hb1M.getChildren().setAll(welcomeLblM);
						hb2M.getChildren().setAll(selectLblM);
						hb3M.getChildren().setAll(addBtnMP);
						hb4M.getChildren().setAll();
						hb5M.getChildren().setAll();
						
						productLVMP.getSelectionModel().clearSelection();
						productLVMP.getSelectionModel().clearSelection(selectedID);
						InputPNameTF.clear();
						InputPDescTA.clear();
						InputPPriceTF.clear();
					}
				});
				
				updateBtnMP.setOnAction(e -> {
						hb1M.getChildren().setAll(productNameLblM);
						hb2M.getChildren().setAll(descFlow);
						hb3M.getChildren().setAll(priceLblM);
						hb4M.getChildren().setAll(vbUpdateProduct);
						hb5M.getChildren().setAll(updateBtnMP2, backBtnMP);
			
				});
				
				updateBtnMP2.setOnAction(e -> {
					try {
						String productID =  ALProduct.get(selectedID).getProductID();
						int updatedPrice = Integer.parseInt(updatePPriceTF.getText());
						 if(updatedPrice<=0) {
						    	Alert alert = new Alert(AlertType.ERROR);
						    	alert.setContentText("Amount must be greater than 0");
						    	alert.showAndWait();
						    	return;
						    }
						 
						
						String queryUpdateProduct="UPDATE product\n" + 
								"SET product_price ="+ updatedPrice+ "\n" + 
								"WHERE productID = '" + productID + "'";
						
//						System.out.println(queryUpdateProduct);
						
						connect.execUpdate(queryUpdateProduct);
						
						
						
						boolean updateProductSuccess = connect.execUpdate(queryUpdateProduct);
						if (updateProductSuccess) {
							Alert berhasil = new Alert(AlertType.INFORMATION);
							berhasil.setTitle("Success");
							berhasil.setHeaderText("Product updated succesfully!");
							berhasil.showAndWait();
							
							hb1M.getChildren().setAll(welcomeLblM);
							hb2M.getChildren().setAll(selectLblM);
							hb3M.getChildren().setAll(addBtnMP);
							hb4M.getChildren().setAll();
							hb5M.getChildren().setAll();
						}
						
					} catch (Exception e2) {
						// TODO: handle exception
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("Fail to update product");
						alert.setContentText("Please make sure all data format is true");
						alert.showAndWait();
					}
					updatePPriceTF.clear();
					productLVMP.getSelectionModel().clearSelection();
					productLVMP.getSelectionModel().clearSelection(selectedID);
					productLVMP.refresh();
				});
				
				removeBtnMP.setOnAction(e -> {
					hb1M.getChildren().setAll(productNameLblM);
					hb2M.getChildren().setAll(descFlow);
					hb3M.getChildren().setAll(priceLblM);
					hb4M.getChildren().setAll(removeProductLblM);
					hb5M.getChildren().setAll(removeBtnMP2, backBtnMP);
				});
				
				removeBtnMP2.setOnAction(e ->{
					try {
						String productID =  ALProduct.get(selectedID).getProductID();
						
						String queryDeleteProduct="DELETE FROM product\n" + 
								"WHERE productID = '" + productID + "'";
						
//						System.out.println(queryDeleteProduct);
						
						connect.execUpdate(queryDeleteProduct);
						getProductData();
						generateProductObserableList();
						productLVMP.refresh();
						
						
						boolean deleteProductSuccess = connect.execUpdate(queryDeleteProduct);
						if (deleteProductSuccess) {
							Alert berhasil = new Alert(AlertType.INFORMATION);
							berhasil.setTitle("Success");
							berhasil.setHeaderText("Product removed succesfully!");
							berhasil.showAndWait();
							
							hb1M.getChildren().setAll(welcomeLblM);
							hb2M.getChildren().setAll(selectLblM);
							hb3M.getChildren().setAll(addBtnMP);
							hb4M.getChildren().setAll();
							hb5M.getChildren().setAll();
						} else if (!deleteProductSuccess) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Error");
							alert.setHeaderText("This Product has been bought by a customer!");
							alert.showAndWait();
							
						}
					} catch (Exception e2) {
						// TODO: handle exception
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("This Product is in Customer's Cart!");
						alert.showAndWait();
					}
					productLVMP.getSelectionModel().clearSelection();
					productLVMP.getSelectionModel().clearSelection(selectedID);
					productLVMP.refresh();
				});

				
			}
		});
		
		
		addBtnMP2.setOnAction(e ->{
			//VALIDASI ID PRODUK
			getProductId();
			String newProductId = "";
			String lastProductId = "";
			ArrayList<String> ALProductID = new ArrayList<>();
			try {
				ALProductID = getProductId();
			} catch (Exception e2) {
				System.out.println("Fail get Product id(1)" + e2);
			}if (ALProductID.isEmpty()) {
				System.out.println("No data in Product");
				newProductId = "TE001";
			} else {
				lastProductId = "TE001";
				for (String s : ALProductID) {
					if (lastProductId.compareTo(s) < 0) {
						lastProductId = s;
//						System.out.println("Last ID"+lastTransactionId);
					}
				}
				Integer i = Integer.parseInt(lastProductId.substring(2)) + 1;
				String id = String.format("%03d", i);
				newProductId = "TE" + id;
			}
			
		try {
			
			// VALIDASI NAMA PRODUK
			ArrayList<TextField> ALTF = new ArrayList<>();
			ALTF.add(InputPNameTF);
			ALTF.add(InputPPriceTF);
			
			ArrayList<TextArea> ALTA = new ArrayList<>();
			ALTA.add(InputPDescTA);
			if (!validasiProduct(ALTF, ALTA)) {
//				Alert alert = new Alert(AlertType.ERROR);
//				alert.setHeaderText("All TextField must be not blank");
//				alert.showAndWait();
			}else {
				//validasi nama produk unik
				boolean pNameExist = false;
				getProductData();
				String ProductName = InputPNameTF.getText();
				for (String p : ALProductName) {
					if (ProductName.equals(p)) {
						System.out.println("Product Name telah terdaftar");
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("Product Name telah terdaftar");
						alert.showAndWait();
						pNameExist = true;
						break;
					}
				}
			
				try {
					String product_name = InputPNameTF.getText();
					int product_price = Integer.parseInt(InputPPriceTF.getText());
					String product_des = InputPDescTA.getText();
					 if(product_price<=0) {
					    	Alert a = new Alert(AlertType.ERROR);
					    	a.setContentText("Amount must be >0");
					    	a.showAndWait();
					    	return;
					    }
					
					if (!pNameExist) {
						String queryAddProduct="INSERT INTO `product` (`productID`, `product_name`, `product_price`, `product_des`) VALUES"
								+ "('"+newProductId+"','" + product_name+"','" +product_price+"','"+product_des+"')";
						boolean addProductSuccess = connect.execUpdate(queryAddProduct);
						getProductData();
						generateProductObserableList();
						productLVMP.refresh();
						if(addProductSuccess) {

							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Information");
							alert.setHeaderText(null);
							alert.setContentText("New Product is Successfully Added");
							alert.showAndWait();
							
							hb1M.getChildren().setAll(welcomeLblM);
							hb2M.getChildren().setAll(selectLblM);
							hb3M.getChildren().setAll(addBtnMP);
							hb4M.getChildren().setAll();
							hb5M.getChildren().setAll();
						}
					}
				} catch (Exception e2) {
					// TODO: handle exception
//					System.out.println("Fail with exception"+e2);
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Fail to add product");
					alert.setContentText("Please make sure all data format is true");
					alert.showAndWait();
					
				}
				
			}
			ALTF.clear();
			ALTA.clear();
			InputPNameTF.clear();
			InputPDescTA.clear();
			InputPPriceTF.clear();
			
		} catch (Exception e3) {
			// TODO: handle exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setContentText("Please make sure all data format is true");
			alert.showAndWait();
		}	
		
		});
		

					
	}
	
	private void eventListenerMenu() {
		// 
		miHomePageAMP.setOnAction(e -> {
			new Home(ss, existingUser);
//				gantiScene(HomeSceneCustomer, "Home");
		});
		miManageProductAMP.setOnAction(e -> {
//				gantiScene(CartScene, "Cart");
			new ManageProduct(ss, existingUser);
		});
		miLogoutAMP.setOnAction(e -> {
			existingUser = null;
//			System.out.println("Logout");
//				gantiScene(loginScene, "Login");
			new Login(ss, existingUser);
		});
	}
	
	private ArrayList<String> getProductId() {
		Connect connect = Connect.getInstance();
		ArrayList<String> ALPId = new ArrayList<>();
		String queryget = "Select productID From  product ";

		connect.execQuery(queryget);
		try {
			while (connect.rs.next()) {
				String productID = connect.rs.getString("productID");
				ALPId.add(productID);
			}
		} catch (SQLException e) {
			System.out.println("GetProductID Fail");
		}

		return ALPId;
	}
	
	
	private boolean validasiProduct(ArrayList<TextField> ALValidasi, ArrayList<TextArea> ALValidasi2) {
		boolean valid = true;
		for (TextField t : ALValidasi) {
			if (t.getText().isBlank()) {
				valid = false;
			} 
		}
		for (TextArea t : ALValidasi2) {
			if (t.getText().isBlank()) {
				valid = false;
			} 
		}
		if (!valid) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("All TextField must be not blank !");
			alert.showAndWait();
		}
		
		return valid;
	}
	
}
