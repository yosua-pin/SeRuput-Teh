package main;

import java.util.ArrayList;

import util.Connect;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.User;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Register {
	Stage ss;
	User existingUser = new User();
	 GridPane gridContainer;
	 Label nameLbl, emailLbl, passLbl, confirmLbl,phoneLbl, addressLbl, genderLbl, registerLbl, registerInvite;
	 RadioButton maleRadio, femaleRadio;
	 TextField nameField, emailField, phoneField;
	 TextArea addressTF;
	 FlowPane genderFlow, loginHook;
	 PasswordField passwordFd, confirmFd;
	 ToggleGroup genderGroup;
	 Button registerBtn;
	 Label loginLink;
	 Alert errorAlert = new Alert(AlertType.ERROR);
	 Stage primaryStage;
	 ArrayList<Character> userList = new ArrayList<Character>();
	 Connect connect = Connect.getInstance();
	Scene registerScene;
	
	public Register(Stage s,User existingUser) {
		this.existingUser = existingUser;
		ss = s;
		initialize();
		
		registerScene = new Scene(gridContainer,800,600);
		s.setScene(registerScene);
		s.setTitle("Register");
	}
	public void initialize() { 
		  gridContainer = new GridPane();
		  
		  registerLbl= new Label("Register");
		  registerLbl.setFont(Font.font("Arial", FontWeight.BOLD, 30));
		  nameLbl = new Label("Username: ");
		  emailLbl = new Label("Email: ");
		  passLbl = new Label("Password: ");
		  confirmLbl = new Label("Confirm Password: ");
		  phoneLbl = new Label("Phone Number: ");
		  addressLbl = new Label("Address:  ");
		  genderLbl = new Label("Gender:  ");
		  
		  
		  nameField = new TextField();
		  nameField.setPromptText("Input Username.");
		  emailField = new TextField();
		  emailField.setPromptText("Input Email Address.");
		  phoneField = new TextField();
		  phoneField.setPromptText("Input Phone Number.");
		  
		  
		  
		  passwordFd = new PasswordField();
		  passwordFd.setPromptText("Input Password.");
		  confirmFd = new PasswordField();
		  confirmFd.setPromptText("Confirm Password.");
		  
		  addressTF = new TextArea();
		  
		  maleRadio = new RadioButton("Male");
		  femaleRadio = new RadioButton("Female");
		  genderGroup = new ToggleGroup();
		  maleRadio.setToggleGroup(genderGroup);
		  femaleRadio.setToggleGroup(genderGroup);
		  
		  genderFlow = new FlowPane();
		  genderFlow.getChildren().addAll(maleRadio, femaleRadio);
		  genderFlow.setHgap(10);
		  
		  nameField.setPrefWidth(100);
		  emailField.setPrefWidth(100);
		  phoneField.setPrefWidth(100);
		  passwordFd.setPrefWidth(100);
		  confirmFd.setPrefWidth(100);
		  addressTF.setPrefWidth(100);
		  
		  
		  loginLink = new Label("Login here");
		  registerInvite = new Label("Have an account?");
		  registerBtn = new Button("Register");
		  registerBtn.setPrefWidth(150);
		  
		  loginLink.setOnMouseClicked(event ->{
		   new Login(ss, existingUser);
		   });
		  
		  loginHook = new FlowPane();
		  loginHook.getChildren().addAll(registerInvite, loginLink);
		  loginHook.setHgap(5);
		  
		  gridComponent();
		  setValidation();
		  loginLink.setStyle("-fx-text-fill:blue");
		  


		  
		 }
		 
		 public void gridComponent() {
		  gridContainer.add(registerLbl, 1 , 0);
		  gridContainer.add(nameLbl, 0, 1);
		  gridContainer.add(nameField, 1, 1);
		  gridContainer.add(emailLbl, 0, 2);
		  gridContainer.add(emailField, 1, 2);
		  gridContainer.add(passLbl, 0, 3);
		  gridContainer.add(passwordFd, 1, 3);
		  gridContainer.add(confirmLbl, 0, 4);
		  gridContainer.add(confirmFd, 1, 4);
		  gridContainer.add(phoneLbl, 0, 5);
		  gridContainer.add(phoneField, 1, 5);
		  gridContainer.add(addressLbl, 0, 6);
		  gridContainer.add(addressTF, 1, 6);
		  gridContainer.add(genderLbl, 0, 7);
		  gridContainer.add(genderFlow, 1, 7);
		  gridContainer.add(loginHook, 1, 8);
		  gridContainer.add(registerBtn, 1, 9);
		  gridContainer.setAlignment(Pos.CENTER);
		  gridContainer.setVgap(10);
		  gridContainer.setHgap(5);
		 }
		 


		 
		 public boolean usernameValidation() {
		     String nameTemp = nameField.getText();
		     boolean validate = connect.isUsernameUnique(nameTemp);

		     if (!validate) {
		         errorAlert.setHeaderText("Failed to register");
		         errorAlert.setContentText("Username has to be unique!");
		         errorAlert.show();
		         return false;
		     }
		     return true;
		 }

		 public void setValidation(){
		  registerBtn.setOnMouseClicked(event->{
		   if(!usernameValidation()) {
		    return;
		   }
		    char[] charArray = passwordFd.getText().toCharArray();
		    for (char ch : charArray) {
		     if(! (Character.isDigit(ch) | Character.isAlphabetic (ch))) {
		      errorAlert.setHeaderText("Failed to register");
		     errorAlert.setContentText("Password must be alphanumeric!");
		     errorAlert.show();
		      break;
		          }
		     }
		    
		   
		  
		   
		   if(nameField.getText().isEmpty()||passwordFd.getText().isEmpty()||confirmFd.getText().isEmpty()
		     ||phoneField.getText().isEmpty()) {
		    errorAlert.setHeaderText("Failed to register");
		    errorAlert.setContentText("All field must be filled!");
		    errorAlert.show();
		    return;
		   }
		   else if(nameField.getText().length() < 5 || nameField.getText().length() > 20) {
		    errorAlert.setHeaderText("Failed to register");
		    errorAlert.setContentText("Username must be 5-20 characters!");
		    errorAlert.show();
		    return;
		   }
		   
		   else if(!emailField.getText().endsWith("@gmail.com")){
		    errorAlert.setHeaderText("Failed to register");
		    errorAlert.setContentText("Email must end with ‘@gmail.com’.");
		    errorAlert.show();
		    return;
		   }
		   
		   
		   else if(passwordFd.getText().length()< 5) {
		    errorAlert.setHeaderText("Failed to register");
		    errorAlert.setContentText("Password must be at least 5 characters");
		    errorAlert.show();
		    return;
		   }
		   
		   else if(!confirmFd.getText().equals(passwordFd.getText())) {
		    errorAlert.setHeaderText("Failed to register");
		    errorAlert.setContentText("Confirm password must equals to password.");
		    errorAlert.show();
		    return;
		   }
		   
		   else if(!phoneField.getText().startsWith("+62")) {
		    errorAlert.setHeaderText("Failed to register");
		    errorAlert.setContentText("Phone Number must start with '+62' ");
		    errorAlert.show();
		    return;
		   }
		   
		   else if(phoneField.getText().startsWith("+62")) {
		    String phoneTemp = phoneField.getText().substring(2);
		     char[] charNumber = phoneTemp.toCharArray();
		     for (char c : charNumber) {
		     if(!(Character.isDigit(c))) {
		      errorAlert.setHeaderText("Failed to register");
		      errorAlert.setContentText("Phone Number must be Numeric!");
		      errorAlert.show();
		     }
		    }
		   }
		   
		 
		  
		    
		             connect.insertData(
		               connect.createID(),
		                     nameField.getText(),
		                     passwordFd.getText(),
		                     "Customer",
		                     phoneField.getText(),
		                     addressTF.getText(),
		                     maleRadio.isSelected() ? "Male" : "Female"
		             );

		             Alert successAlert = new Alert(AlertType.INFORMATION);
		             successAlert.setHeaderText("Registared Successfully!");
		             successAlert.setContentText("You have been successfully registered.");
		             successAlert.showAndWait();
		             new Login(ss, existingUser);
		         
		     });
		  
		  
		 }
}
