package RiffForm;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import jm.music.data.Score;

public class RiffController implements RiffModel {
    @FXML VBox root;

    @FXML private Canvas staff;
    @FXML private Canvas notes;
    private Image[][] images;
    private GraphicsContext notesGC;
    private GraphicsContext staffGC;
    private int currentNote;
    private int currentScale;

    private double noteCount;
    private double noteValue;
    private int measureNum;

    @FXML public void initialize() {
        currentNote = Note.QUARTER_NOTE.getIndex();
        currentScale = 0;
        noteValue = 1;
        noteCount = 0;
        measureNum = 0;
        Image clef = null;
        images = new Image[3][2];
        try {
            clef = new Image("res\\TrebleClef.png", 0, 118, true, true);
            images[0][0] = new Image("res\\QuarterNote.png", 0, 64, true, true);
            images[0][1] = new Image("res\\QuarterNoteTransp.png", 0, 64, true, true);
//            images[1][0] = new Image("res\\TrebleClef.png", 0, 115.0, true, true);
//            images[1][1] = new Image("res\\TrebleClef.png", 0, 115.0, true, true);
//            images[2][0] = new Image("res\\TrebleClef.png", 0, 115.0, true, true);
//            images[2][1] = new Image("res\\TrebleClef.png", 0, 115.0, true, true);

        } catch (NullPointerException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        staffGC = staff.getGraphicsContext2D();
        staffGC.drawImage(clef, 0, 6);
        staffGC.fillRect(0, 30, 600, 3);
        staffGC.fillRect(0, 46, 600, 3);
        staffGC.fillRect(0, 62, 600, 3);
        staffGC.fillRect(0, 78, 600, 3);
        staffGC.fillRect(0, 94, 600, 3);
        notesGC = notes.getGraphicsContext2D();
        notesGC.drawImage(images[currentNote][1], 100, currentScale * 8);
    }

    public void onPress(ActionEvent e) {
        InputListener il = new KeyboardListener(root.getScene());
        il.addModel(this);
    }

    @Override
    public void eventDownPressed() {
        System.out.println("Down Pressed!");
        if (currentScale < 7) {
            currentScale += 1;
            notesGC.clearRect(95 + measureNum * 140 + noteCount * 30, 0, 30, 300);
            notesGC.drawImage(images[currentNote][1], 100 + measureNum * 140 + noteCount * 30, currentScale * 8);
        } else {
            System.out.println("At the moment we have a 1 octave range. Sorry");
        }

    }

    @Override
    public void eventUpPressed() {
        System.out.println("Up Pressed!");
        if (currentScale > 0) {
            currentScale -= 1;
            notesGC.clearRect(97 + measureNum * 140 + noteCount * 30, 0, 30, 300);
            notesGC.drawImage(images[currentNote][1], 100 + measureNum * 140 + noteCount * 30, currentScale * 8);
        } else {
            System.out.println("At the moment we have a 1 octave range. Sorry");
        }
    }

    @Override
    public void eventLeftPressed() {
        System.out.println("Left Pressed!");
        if (noteValue > 1) {
            noteValue *= 0.5;
        } else {
            System.out.println("Unable to reduce note value.");
        }
    }

    @Override
    public void eventRightPressed() {
        System.out.println("Right Pressed!");
        if (noteValue < 4 && noteValue <= 4 - noteCount) {
            noteValue *= 2;
        } else {
            System.out.println("Unable to increase note value.");
        }
    }

    @Override
    public void eventEnterPressed() {
        System.out.println("Enter Pressed!");
        notesGC.drawImage(images[currentNote][0], 100 + measureNum * 140 + noteCount * 30, currentScale * 8);
        if (currentScale == 7) {
            notesGC.fillRect(97 + measureNum * 140 + noteCount * 30, 111, 26, 2);
        }
        noteCount += noteValue;
        noteValue = 1;
        if (noteCount == 4) {
            noteCount = 0;
            measureNum += 1;
            staffGC.fillRect(84 + measureNum  * 140, 30, 3, 64);
        }
        notesGC.drawImage(images[currentNote][1], 100 + measureNum * 140 + noteCount * 30, currentScale * 8);
    }

    @Override
    public Score getScore() {
        return null;
    }

    enum Note {
        QUARTER_NOTE(0),
        HALF_NOTE(1),
        WHOLE_NOTE(2);
        private final int index;

        Note(int index) {
            this.index = index;
        }
        private int getIndex() {
            return index;
        }
    }
}


