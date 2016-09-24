package RiffForm;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import jm.music.data.Score;

public class RiffController implements RiffModel {
    @FXML
    GridPane root;

    @FXML
    Canvas canvas;

    @FXML
    public void initialize() {
    }

    public void onPress(ActionEvent e) {
        InputListener il = new KeyboardListener(root.getScene());
        il.addModel(this);
    }

    @Override
    public void eventDownPressed() {
        System.out.println("Down Pressed!");
    }

    @Override
    public void eventUpPressed() {
        System.out.println("Up Pressed!");
    }

    @Override
    public void eventLeftPressed() {
        System.out.println("Left Pressed!");
    }

    @Override
    public void eventRightPressed() {
        System.out.println("Right Pressed!");
    }

    @Override
    public void eventEnterPressed() {
        System.out.println("Enter Pressed!");
    }

    @Override
    public Score getScore() {
        return null;
    }
}
