package sample;

import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Write;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jesse on 9/24/2016.
 */
public class MusicGenTest implements JMC {
    private static String FILE_NAME = "Random.midi";
    private static Sequencer sq;
    public static void create() {

        Phrase aMelody = new Phrase();
        for (int i = 0; i < 8; i++) {
            aMelody.add(new Note(C4, QUARTER_NOTE));
        }

        Part p1 = new Part();
        p1.add(aMelody);

        Score s = new Score();
        s.add(p1);

        Write.midi(s, FILE_NAME);

        try {
            sq = MidiSystem.getSequencer();
            sq.open();

            InputStream is = new BufferedInputStream(new FileInputStream(new File(FILE_NAME)));

            sq.setSequence(is);
            sq.start();

        } catch (MidiUnavailableException | InvalidMidiDataException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        if (sq != null) sq.close();
    }
}
