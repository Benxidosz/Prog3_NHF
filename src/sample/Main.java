package sample;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Arrays;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Menu menu = new Menu(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
