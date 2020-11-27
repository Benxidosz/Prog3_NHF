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
		LinkedList<Integer> test = new LinkedList<>();
		test.addFirst(1);
		test.addFirst(2);
		test.addFirst(3);
		test.addFirst(4);
		test.addFirst(5);
		test.forEach(System.out::print);

		/*var iterTest = test.listIterator(2);
		System.out.println("\n" + (iterTest.previousIndex()));
		//iterTest.previous();
		System.out.println(iterTest.nextIndex());

		for (var iter = test.listIterator(2); iter.hasPrevious();) {
			iter.previous();
			iter.remove();
		}

		test.forEach(System.out::print);*/

		launch(args);
	}
}
