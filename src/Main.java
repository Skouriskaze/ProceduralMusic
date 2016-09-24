import Models.MusicGenerator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jm.JMC;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        MusicGenerator gen = new MusicGenerator(dummyScore());
        gen.play();
        launch(args);
    }

    private static Score dummyScore() {
        Score r = new Score();
        Part p = new Part();
        Phrase ph = new Phrase();
        //------------
        ph.addNote(JMC.C4, JMC.QUARTER_NOTE);
        ph.addNote(JMC.D4, JMC.QUARTER_NOTE);
        ph.addNote(JMC.E4, JMC.QUARTER_NOTE);
        ph.addNote(JMC.F4, JMC.QUARTER_NOTE);
        ph.addNote(JMC.G4, JMC.QUARTER_NOTE);
        ph.addNote(JMC.A4, JMC.QUARTER_NOTE);
        ph.addNote(JMC.B4, JMC.QUARTER_NOTE);
        ph.addNote(JMC.C5, JMC.QUARTER_NOTE);
        //------------
        p.add(ph);
        r.add(p);

        return r;
        // ---
    }
}
