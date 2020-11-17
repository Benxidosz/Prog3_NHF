package sample;

import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;

public class Editor {
	Parent editor;
	ButtonListener buttonListener;

	Editor(Parent p){
		editor = p;
		buttonListener = new ButtonListener();
		editor.lookup("#editorMove").addEventHandler(MouseEvent.MOUSE_CLICKED, buttonListener);
		editor.lookup("#editorNewNode").addEventHandler(MouseEvent.MOUSE_CLICKED, buttonListener);
		editor.lookup("#editorRmNode").addEventHandler(MouseEvent.MOUSE_CLICKED, buttonListener);
		editor.lookup("#editorNewEdge").addEventHandler(MouseEvent.MOUSE_CLICKED, buttonListener);
		editor.lookup("#editorRmEdge").addEventHandler(MouseEvent.MOUSE_CLICKED, buttonListener);
	}
}
