import Models.MusicGenerator;
import Models.NewMusicGenerator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jm.JMC;
import jm.constants.Pitches;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("RiffForm\\musicGUI.fxml"));
        primaryStage.setTitle("Procedurally Generated Music");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        //MusicGenerator gen = new MusicGenerator(dummyScore());
        //gen.play();
        dummyScore();
        launch(args);
    }

    private static Score dummyScore() {
        Score r = new Score();
        r.setTempo(120);
        Part p = new Part();
        Phrase ph = new Phrase();
        //-----------
        ph.addNote(JMC.C4, JMC.QUARTER_NOTE);
        ph.addNote(JMC.E4, JMC.HALF_NOTE);
        ph.addNote(JMC.G4, JMC.QUARTER_NOTE);
        ph.addNote(JMC.A4, JMC.QUARTER_NOTE);
        ph.addNote(JMC.G4, JMC.QUARTER_NOTE);
        ph.addNote(JMC.E4, JMC.QUARTER_NOTE);
        ph.addNote(JMC.D4, JMC.QUARTER_NOTE);
        ph.addNote(JMC.C4, JMC.WHOLE_NOTE);
        //------------
        p.add(ph);
        r.add(p);

        NewMusicGenerator gen = new NewMusicGenerator(r, Pitches.C4);
        //r.add(b);

        return r;
    }
}
