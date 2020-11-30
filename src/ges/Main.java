package ges;

import ges.menu.MainMenu;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Iterator;
import java.util.LinkedList;

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
