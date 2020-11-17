package sample;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ButtonListener implements EventHandler {
	Button tempButton;

	ButtonListener(){
		tempButton = null;
	}

	@Override
	public void handle(Event event) {
		if (tempButton != null)
			tempButton.setDisable(false);

		tempButton = (Button) event.getSource();
		tempButton.setDisable(true);
	}
}
