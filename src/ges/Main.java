package ges;

import ges.menu.MainMenu;
import ges.menu.MyScene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setResizable(false);
		primaryStage.setTitle("Main Menu");

		new MainMenu(primaryStage);
	}


	public static void main(String[] args) {
		launch(args);
	}
}
