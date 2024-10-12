package main;
import util.Connect;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.User;

	public class Login {
	Stage ss;
	 Scene loginScene;
	 User existingUser = new User();
	// BorderPane border;
	 
	 FlowPane flow;
	 GridPane grid;
	 Label nameLbl,passLbl, title, registerlbl;
	 HBox registerHook;
	 TextField nameField;
	 PasswordField passField;
	 Button loginBtn;
	 Stage primaryStage;
	 Label registerLink;
	 Alert errorAlert = new Alert(AlertType.ERROR);
	  Alert successAlert = new Alert(AlertType.INFORMATION);
	 Connect connect = Connect.getInstance();
	 

	 public Login(Stage s,User existingUser) {
			this.existingUser = existingUser;
			ss = s;
			initialize();
			loginScene = new Scene(grid,800,600);
			s.setScene(loginScene);
			s.setTitle("Login");
		}
	  
	 public void initialize() {  
	  nameLbl = new Label("Username : ");
	  passLbl = new Label("Pasword : ");
	  title = new Label("Login");
	  registerlbl = new Label("Don't have an account yet?");
	  
	  nameField = new TextField();
	  passField = new PasswordField();
	  nameField.setPrefWidth(300);
	  passField.setPrefWidth(300);
	  
	  nameField.setPromptText("input username...");
	  passField.setPromptText("input password...");
	  
	  
	  registerLink = new Label("Register here");
	  registerLink.setStyle("-fx-text-fill:blue");
	  registerHook = new HBox(registerlbl,registerLink);
	  registerHook.setSpacing(10);
	  
	  registerLink.setOnMouseClicked(event ->{
		  new Register(ss, existingUser);
	   });
	  
	  
	  grid = new GridPane();
	  flow = new FlowPane();
	  
	  loginBtn = new Button("Login");
	  
	  addContainer();
	  setValidation();
	  
	       
	 }
	 
	 public void addContainer() {

	  
	  grid.add(title, 2, 0);
	  grid.add(nameLbl, 1, 1);
	  grid.add(passLbl, 1, 2);
	  grid.add(nameField, 2, 1);
	  grid.add(passField, 2, 2);
	  grid.add(registerHook, 2, 3);
	  grid.add(loginBtn, 2, 5);
	  grid.setAlignment(Pos.CENTER);
	//  grid.setMargin(title, new Insets(20));
	  grid.setHgap(10);
	  grid.setVgap(5);
	  loginBtn.setPrefWidth(150);
	  title.setFont(Font.font("Arial",FontWeight.BOLD,20));
	  
	 }
		private boolean validasiUser(String userN,String pass) {
			boolean valid;
			String query = "SELECT * FROM user WHERE username=? AND password=?";
			PreparedStatement ps;
			try {
				ps = connect.getConnection().prepareStatement(query);
				try {
					ps.setString(1, userN);
					ps.setString(2, pass);
					
				} catch (SQLException e) {
					System.out.println("error ps1");
				}
				
				
				try {
					connect.rs=ps.executeQuery();
				} catch (SQLException e) {
					System.out.println("error ps2");
				}
			} catch (SQLException e2) {
				System.out.println("error ps");
			}
			
			
			try {
				if(connect.rs.next()) {
					String userID= connect.rs.getString("userID");
					String userName=connect.rs.getString("username");
					String password=connect.rs.getString("password");
					String role=connect.rs.getString("role");
					String address=connect.rs.getString("address");
					String phone_num=connect.rs.getString("phone_num");
					String gender=connect.rs.getString("gender");
					existingUser = new User(userID, userName, password, role, address, phone_num, gender);
					valid =true;
				}
				else {
					valid=false;
				}
			} catch (Exception e1) {
				System.out.println("No RS");
				valid=false;
			}
			return valid;
		}
	 public void setValidation() {
	  loginBtn.setOnMouseClicked(event->{
	   if(nameField.getText().isEmpty()||passField.getText().isEmpty()) {
	    errorAlert.setHeaderText("Failed to Login");
	    errorAlert.setContentText("All field must be field!");
	    errorAlert.show();
	   }
	   
	   else {
		   boolean valid= validasiUser(nameField.getText(), passField.getText());
		   if(!valid) {
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Failed to Login");
				a.setContentText("Invalid Credential");
				a.showAndWait();
				return;
			}
			else {
				nameField.setText(null);
				passField.setText(null);
				new Home(ss, existingUser);
				
			}
	   }
	   
	  });
	 }
	 

	 

}

