package Models;

import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Play;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jesse on 9/24/2016.
 */
public class MusicGenerator implements JMC {

    private Score sRoot;
    private List<Score> aIterations;

    public MusicGenerator(Score s) {
        sRoot = s;
        aIterations = new ArrayList<>();
        aIterations.add(s);
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

        Part pBass = new Part();
        Phrase phBass = new Phrase();

        double dDuration = 0;
        double dStandard = (new Note(C4, QUARTER_NOTE).getDuration());
        for (Note n : aOldNotes) {
            int[] third = {n.getPitch(), n.getPitch() + 4, n.getPitch() + 7};
            phNew.addChord(third, n.getDuration() / dStandard);
            if (dDuration == 0) {
                //Random rand = new Random(10);
                Random rand = new Random();
                int nHarmonic = rand.nextBoolean() ? 1 : 0;
                int[] bass = {n.getPitch() - 12 + (nHarmonic * 4), n.getPitch() - 24 +(nHarmonic * 4)};
                phBass.addChord(bass, WHOLE_NOTE);
            }
            dDuration = (dDuration + (n.getDuration() / dStandard)) % 4;
        }
        pNew.add(phNew);
        pBass.add(phBass);
        s.add(pNew);
        s.add(pBass);

        aIterations.add(s);
    }

    public void play() {
        Runnable p = () -> { for (Score s : aIterations) Play.midi(s);};
        Thread thread = new Thread(p);
        thread.start();
    }
}
