package ges;

import ges.menu.MainMenu;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	/**
	 * start the application.
	 *
	 * @param primaryStage
	 * @throws Exception
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setResizable(false);
		primaryStage.setTitle("Main Menu");

		new MainMenu(primaryStage);
	}


	/**
	 * Main method
	 *
	 * @param args running args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
