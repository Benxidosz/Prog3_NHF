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
		FXMLLoader loader = new FXMLLoader(getClass().getResource(String.valueOf(new File("menu", "mainMenu.fxml"))));

		loader.setControllerFactory(c -> new MainMenu(primaryStage));

		Parent main = loader.load();
		Scene mainScene = new Scene(main);

		primaryStage.setResizable(false);
		primaryStage.setScene(mainScene);
		primaryStage.setTitle("Main Menu");

		primaryStage.show();
	}


	public static void main(String[] args) {
		launch(args);
	}
}
