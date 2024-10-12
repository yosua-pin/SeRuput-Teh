package main;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.User;

public class Template {
	Stage ss;
	User existingUser = new User();
	
	Scene templateScene;
	
	public Template(Stage s,User existingUser) {
		this.existingUser = existingUser;
		ss = s;
		
		
		templateScene = new Scene(new Label(),800,600);
		s.setScene(templateScene);
		s.setTitle("template title");
	}

}
