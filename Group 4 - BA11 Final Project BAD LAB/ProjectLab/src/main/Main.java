package main;


import javafx.application.Application;
import javafx.stage.Stage;
import model.User;

public class Main extends Application {
	User existingUserMain = new User();
	Stage window;
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		new Login(window,existingUserMain);
		window.show();
		window.setResizable(false);
		
	}

	public static void main(String[] args) {
		launch(args);
	}

}