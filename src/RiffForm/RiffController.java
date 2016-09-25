package RiffForm;


import Models.AsyncPlayer;
import com.leapmotion.leap.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Play;

import java.util.ArrayList;
import java.util.List;

public class RiffController implements RiffModel {
    @FXML VBox root;

    @FXML private Canvas staff;
    @FXML private Canvas notes;
    @FXML private Pane start;
    private Image[][] images;
    private GraphicsContext notesGC;
    private GraphicsContext staffGC;
    private int currentNote;
    private int currentScale;

    private double noteCount;
    private double noteValue;
    private int measureNum;

    private Score score;
    private Part p;
    private Phrase ph;

    Controller controller;
    List<InputListener> listeners;

    @FXML public void initialize() {
        currentNote = 0;
        currentScale = 0;
        noteValue = 1;
        noteCount = 0;
        measureNum = 0;
        Image clef = null;
        images = new Image[3][2];
        start.setStyle("-fx-background-color: white;");
        try {
            clef = new Image("res\\TrebleClef.png", 0, 118, true, true);
            images[0][0] = new Image("res\\QuarterNote.png", 0, 64, true, true);
            images[0][1] = new Image("res\\QuarterNoteTransp.png", 0, 64, true, true);
            images[1][0] = new Image("res\\HalfNote.png", 0, 64, true, true);
            images[1][1] = new Image("res\\HalfNoteTransp.png", 0, 64, true, true);
            images[2][0] = new Image("res\\WholeNote.png", 0, 64, true, true);
            images[2][1] = new Image("res\\WholeNoteTransp.png", 0, 64, true, true);

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

        score = new Score();
        score.setTempo(100);
        p = new Part();
        ph = new Phrase();

        //Leap
        listeners = new ArrayList<>();
        controller = new Controller();
    }

    public void onPress(ActionEvent e) {
        InputListener il = new KeyboardListener(root.getScene());
        listeners.add(il);

        LeapListener il2 = new LeapListener();
        listeners.add(il2);
        controller.addListener(il2);

        for (InputListener ill : listeners) {
            ill.addModel(this);
        }

        System.out.println("Button pressed");
        start.setVisible(false);
    }

    public void closeAction() {
        System.exit(0);
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
            currentNote--;
            notesGC.clearRect(95 + measureNum * 140 + noteCount * 30, 0, 30, 300);
            notesGC.drawImage(images[currentNote][1], 100 + measureNum * 140 + noteCount * 30, currentScale * 8);
        } else {
            System.out.println("Unable to reduce note value.");
        }
    }

    @Override
    public void eventRightPressed() {
        System.out.println("Right Pressed!");
        if (noteValue < 4 && noteValue * 2 <= 4 - noteCount) {
            noteValue *= 2;
            currentNote++;
            notesGC.clearRect(95 + measureNum * 140 + noteCount * 30, 0, 30, 300);
            notesGC.drawImage(images[currentNote][1], 100 + measureNum * 140 + noteCount * 30, currentScale * 8);
        } else {
            System.out.println("Unable to increase note value.");
        }
    }

    @Override
    public void eventEnterPressed() {
        if (measureNum < 3){
            System.out.println("Enter Pressed!");
            notesGC.clearRect(95 + measureNum * 140 + noteCount * 30, 0, 30, 300);
            notesGC.drawImage(images[currentNote][0], 100 + measureNum * 140 + noteCount * 30, currentScale * 8);
            if (currentScale == 7) {
                notesGC.fillRect(97 + measureNum * 140 + noteCount * 30, 111, 26, 2);
            }
            noteCount += noteValue;
            Note n = new Note(JMC.MAJOR_SCALE[(7 - currentScale)%7] + 12 * (7 - currentScale >= 7 ? 1 : 0) + JMC.C4, noteValue);
            ph.addNote(n);
            // Todo: Make async
//            AsyncPlayer.play(n);
//            Play.midi(n);start();
            noteValue = 1;
            currentNote = 0;
            if (noteCount == 4) {
                noteCount = 0;
                measureNum += 1;
                staffGC.fillRect(84 + measureNum * 140, 30, 3, 64);
            }
            if (measureNum < 3) {
                notesGC.drawImage(images[currentNote][1], 100 + measureNum * 140 + noteCount * 30, currentScale * 8);
            } else {
                p.addPhrase(ph);
                score.addPart(p);
                System.out.println("Reached the end of allotted space. added to parts");

                AsyncPlayer.play(score);
            }
        } else {
            System.out.println("Stop pressing enter you ho!");
        }
    }

    @Override
    public Score getScore() {
        return null;
    }
}


