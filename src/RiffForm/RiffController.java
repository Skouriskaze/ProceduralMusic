package RiffForm;


import Models.AsyncPlayer;
import com.leapmotion.leap.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Play;
import jm.util.Write;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
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

    private Deque<Note> noteList;
    private double noteCount;
    private double noteValue;
    private int measureNum;

    private Score score;
    private Part p;
    private Phrase ph;

    Controller controller;
    List<InputListener> listeners;

    @FXML public void initialize() {
        noteList = new LinkedList<>();
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

    private void drawNote(int transparent) {
        notesGC.drawImage(images[currentNote][transparent], 100 + measureNum * 140 + noteCount * 30, currentScale * 8);
    }

    private void eraseNote() {
        notesGC.clearRect(95 + measureNum * 140 + noteCount * 30, 0, 30, 300);
    }

    @Override
    public void eventDownPressed() {
        System.out.println("Down Pressed!");
        if (currentScale < 7) {
            currentScale += 1;
            eraseNote();
            drawNote(1);
        } else {
            System.out.println("At the moment we have a 1 octave range. Sorry");
        }
    }

    @Override
    public void eventUpPressed() {
        System.out.println("Up Pressed!");
        if (currentScale > 0) {
            currentScale -= 1;
            eraseNote();
            drawNote(1);
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
            eraseNote();
            drawNote(1);
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
            eraseNote();
            drawNote(1);
        } else {
            System.out.println("Unable to increase note value.");
        }
    }

    @Override
    public void eventEnterPressed() {
        System.out.println(noteCount);
        if (measureNum < 3){
            System.out.println("Enter Pressed!");
            eraseNote();
            drawNote(0);
            if (currentScale == 7) {
                notesGC.fillRect(97 + measureNum * 140 + noteCount * 30, 111, 26, 2);
            }
            noteCount += noteValue;
            Note n = new Note(JMC.MAJOR_SCALE[(7 - currentScale)%7] + 12 * (7 - currentScale >= 7 ? 1 : 0) + JMC.C4, noteValue);
            noteList.addLast(n);
            ph.addNote(n);
            // Todo: Make async
//            AsyncPlayer.play(n);
//            Play.midi(n);start();
            noteValue = 1;
            currentNote = 0;
            if (noteCount == 4) {
                noteCount = 0;
                measureNum ++;
                notesGC.fillRect(84 + measureNum * 140, 30, 3, 64);
            }
            if (measureNum < 3) {
                drawNote(1);
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
    public void eventBackspacePressed() {
        System.out.println(noteCount);
        if (measureNum > 0 || noteCount > 0){
            eraseNote();
            if (noteCount == 0) {
                noteCount = 4;
                notesGC.clearRect(84 + measureNum * 140, 30, 3, 64);
                measureNum--;
            }
            noteCount -= noteList.peekLast().getDuration()/(new Note(Note.REST, JMC.QUARTER_NOTE).getDuration());
            eraseNote();
            drawNote(1);
        } else {
            System.out.println("Cant do that ya fool");
        }
    }


    @Override
    public Score getScore() {
        return null;
    }

    @FXML
    public void setTempoAction(ActionEvent e) {
        score.setTempo(Integer.parseInt(((MenuItem) e.getSource()).getText()));
    }

    @FXML
    public void saveAction(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Phrase");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Audio Files", "*.midi", "*.mid");
        fileChooser.getExtensionFilters().addAll(filter);
        File file = fileChooser.showSaveDialog(root.getScene().getWindow());
        if (file != null) {
            Write.midi(ph, file.getPath());
        }
    }
}


