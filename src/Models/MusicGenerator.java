package Models;

import jm.JMC;
import jm.audio.Instrument;
import jm.constants.Durations;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.music.tools.Mod;
import jm.util.Play;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Jesse on 9/24/2016.
 */
public class MusicGenerator implements JMC {

    private Score sRoot;
    private List<Score> aIterations;
    private int nBase;

    public MusicGenerator(Score s, int nBase) {
        this.sRoot = s;
        aIterations = new ArrayList<>();
        this.nBase = nBase;
        //aIterations.add(s);

        Score bass = sRoot.copy();
        Phrase phOld = sRoot.getPart(0).getPhrase(0);
        phOld.setTitle("root");
//        Phrase phBass = addBassLine(bass, phOld, 2).getPhrase(0);
//        addOctaveBaseLine(bass, phBass);
        addRandomBass(bass, 2);
        addHighHarmony(bass, phOld);
//        addDrumLine(bass);
        aIterations.add(bass);
    }

    private Part addRandomBass(Score s, int nOctave) {
        Part pBass = new Part("randombass");
        Phrase phBass = new Phrase("randombass");
        Random rand = new Random();
        int[] aBass = new int[2 * (int) lengthPhraseBeats(s.getPart(0).getPhrase(0))];
        Arrays.fill(aBass, Integer.MIN_VALUE);
        for (int i = 0; i < aBass.length / 2; i++) {
            aBass[2 * i] = MAJOR_SCALE[rand.nextInt(MAJOR_SCALE.length)] - 12 * nOctave;
        }

        arrayToPhrase(phBass, aBass);
        pBass.add(phBass);
        s.add(pBass);

        return pBass;
    }

    /**
     * Adds the higher harmony to a phrase
     * @param s Score to add a phrase to
     * @param phOld the original phrase
     */
    private Part addHighHarmony(Score s, Phrase phOld) {
        Part pHarmony = new Part("highharmony");
        Phrase phHarmony = new Phrase("highharmony");

        int[] aOld = arrayPhrase(phOld);
        int[] aHarm = new int[aOld.length];

        int i = 0;
        while (i < aOld.length) {
            aHarm[i] = findHarmonic(aOld[i]);
            while (++i < aOld.length && aOld[i] == Integer.MIN_VALUE) {
                aHarm[i] = aOld[i];
            }
        }

        arrayToPhrase(phHarmony, aHarm);
        pHarmony.add(phHarmony);
        s.add(pHarmony);

        return pHarmony;
    }

    /**
     * Adds a bass line based on a phrase
     * @param s Score to add the phrase to
     * @param phOld Original phrase
     * @param nOctaves The number of octaves to drop (0 is the same)
     */
    private Part addBassLine(Score s, Phrase phOld, int nOctaves) {

        Part pBass = new Part("bassline");
        Phrase phBass = new Phrase("bassline");

        int[] aOld = arrayPhrase(phOld);
        int[] aBass = new int[aOld.length];

        // Copying bass an octave down
        for (int i = 0; i < aOld.length; i++) {
            if (aOld[i] > Integer.MIN_VALUE) {
                aBass[i] = aOld[i] - nOctaves * 12;
            } else {
                aBass[i] = Integer.MIN_VALUE;
            }
        }

        arrayToPhrase(phBass, aBass);
        pBass.add(phBass);
        s.add(pBass);

        return pBass;
    }

    /**
     * Adds an alternating octave to the score
     * @param s Score to add
     * @param phOld The bass line
     * @return Returns the alternating pattern
     */
    private Part addOctaveBaseLine(Score s, Phrase phOld) {
        Part pBass = new Part("octavebaseline");
        Phrase phBass = new Phrase("octavebaseline");

        int[] aOld = arrayPhrase(phOld);
        int[] aBass = new int[aOld.length];


        int i = 0;
        while (i < aOld.length) {
            int nNote = aOld[i];
            aBass[i] = nNote + 12;
            boolean bFlipped = false;
            while (++i < aOld.length && aOld[i] == Integer.MIN_VALUE) {
                aBass[i] = nNote + 12 * (bFlipped ? 1 : 0);
                bFlipped = !bFlipped;
            }
        }

        //Shifting
        for (int j = aBass.length - 1; j > 0; j--) {
            aBass[j] = aBass[j - 1];
        }
        aBass[0] = aOld[0];

        arrayToPhrase(phBass, aBass);
        pBass.add(phBass);
        s.add(pBass);

        return pBass;
    }

    private Part addDrumLine(Score s) {
        Part pDrum = new Part("Drum Kit", 0, 9);
        Phrase phBass = new Phrase(0.0);
        Phrase phHighHat = new Phrase(0.0);
        Phrase phSnare = new Phrase(0.0);

        int nLength = (int) lengthPhraseBeats(s.getPart(0).getPhrase(0));
        for (int i = 0; i < nLength / 4; i++) {
            phBass.addNote(36, HALF_NOTE);
            phBass.addNote(36, HALF_NOTE);
            phSnare.addNote(REST, QUARTER_NOTE);
            phSnare.addNote(38, HALF_NOTE);
            phSnare.addNote(38, QUARTER_NOTE);
            phHighHat.addNote(42, QUARTER_NOTE);
            phHighHat.addNote(42, QUARTER_NOTE);
            phHighHat.addNote(42, QUARTER_NOTE);
            phHighHat.addNote(42, QUARTER_NOTE);
        }

        pDrum.add(phBass);
        pDrum.add(phHighHat);
        pDrum.add(phSnare);
        s.add(pDrum);
        return pDrum;
    }

    /**
     * Converts and array to a phrase.
     * @param ph Phrase to be edited
     * @param notes The corresponding notes.
     */
    private void arrayToPhrase(Phrase ph, int[] notes) {
        int i = 0;
        while (i < notes.length) {
            double nLength = 1;
            int nNote = i;

            i++;
            while (i < notes.length && notes[i] == Integer.MIN_VALUE) {
                i++;
                nLength++;
            }

            ph.addNote(notes[nNote] + nBase, nLength / 2);
        }
    }

    private void printArray(int[] array) {
        for (int n : array) System.out.print(n + " ");
        System.out.println();
    }

    /**
     * Finds the harmonic in the major scale
     * @param nNote Note to harmonize
     * @return Pitch of the harmonic
     */
    private int findHarmonic(int nNote) {
        int nHarm = Arrays.binarySearch(MAJOR_SCALE, nNote);
        return MAJOR_SCALE[(nHarm + 2) % MAJOR_SCALE.length] + 12 * (nHarm + 2 >= MAJOR_SCALE.length ? 1 : 0);
    }

    /**
     * Converts a phrase to an array
     * @param ph Phrase to convert
     * @return the phrase array
     */
    private int[] arrayPhrase(Phrase ph) {
        int[] ret = new int[(int) lengthPhraseBeats(ph) * 2];
        int i = 0;
        for (Note n : ph.getNoteArray()) {
            double dStandard = (new Note(nBase, QUARTER_NOTE).getDuration());
            double dLength = n.getDuration() / dStandard * 2;

            ret[i++] = n.getPitch() - nBase;
            for (int j = 0; j < dLength - 1; j++) {
                ret[i++] = Integer.MIN_VALUE;
            }
        }

        return ret;
    }

    private double lengthPhraseBeats(Phrase ph) {
        double dLength = 0;
        double dStandard = (new Note(nBase, QUARTER_NOTE).getDuration());
        for (Note n : ph.getNoteArray()) {
            dLength += n.getDuration();
        }

        return dLength / dStandard;
    }

    public void play() {
        Runnable p = () -> { for (Score s : aIterations) Play.midi(s);};
        Thread thread = new Thread(p);
        thread.start();
    }
}
