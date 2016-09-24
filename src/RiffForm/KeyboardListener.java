package RiffForm;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Created by Jesse on 9/24/2016.
 */
public class KeyboardListener implements InputListener {
    private Scene scene;

    public KeyboardListener(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void addModel(RiffModel model) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if (key.getCode() == KeyCode.DOWN) {
                model.eventDownPressed();
            }
        });
    }
}
