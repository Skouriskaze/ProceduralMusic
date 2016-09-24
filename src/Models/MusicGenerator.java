package Models;

import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Play;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesse on 9/24/2016.
 */
public class MusicGenerator implements JMC {

    private Score sRoot;
    private List<Score> aIterations;

    public MusicGenerator(Score s) {
        sRoot = s;
        aIterations = new ArrayList<Score>();
        aIterations.add(s)
        iterate();
    }

    private void iterate() {
        Score s = sRoot.copy();
        s.empty();
        Part pOld = sRoot.getPart(0);
        Phrase phOld = pOld.getPhrase(0);
        Note[] aOldNotes = phOld.getNoteArray();
        Part pNew = new Part();
        Phrase phNew  = new Phrase();

        for (Note n : aOldNotes) {
            int[] third = {n.getPitch(), n.getPitch() + 4, n.getPitch() + 7};
            phNew.addChord(third, n.getDuration());
        }
        pNew.add(phNew);
        s.add(pNew);

        aIterations.add(s);
    }

    public void play() {
        Runnable p = () -> { for (Score s : aIterations) Play.midi(s);};
        Thread thread = new Thread(p);
        thread.start();
    }
}
