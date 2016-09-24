package Models;

import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
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
        sRoot = s;
        aIterations = new ArrayList<>();
        this.nBase = nBase;
        aIterations.add(s);

        Score bass = sRoot.copy();
        Phrase phOld = sRoot.getPart(0).getPhrase(0);
        addBassLine(bass, phOld, 1);
        addHighHarmony(bass, phOld);
        aIterations.add(bass);
    }


    /**
     * Adds the higher harmony to a phrase
     * @param s Score to add a phrase to
     * @param phOld the original phrase
     */
    private void addHighHarmony(Score s, Phrase phOld) {
        Part pHarmony = new Part();
        Phrase phHarmony = new Phrase();

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
    }

    /**
     * Adds a bass line based on a phrase
     * @param s Score to add the phrase to
     * @param phOld Original phrase
     * @param nOctaves The number of octaves to drop (0 is the same)
     */
    private void addBassLine(Score s, Phrase phOld, int nOctaves) {

        Part pBass = new Part();
        Phrase phBass = new Phrase();

        int[] aOld = arrayPhrase(phOld);

        System.out.print("Old: ");
        printArray(aOld);

        Random rand = new Random();
        int[] aBass = new int[aOld.length];

        for (int i = 0; i < aOld.length; i++) {
            if (aOld[i] > Integer.MIN_VALUE) {
                /*boolean bHarmonic = rand.nextBoolean();
                bHarmonic = false;
                if (bHarmonic)
                    aBass[i] = findHarmonic(aOld[i], C4) - nOctaves * 12;
                else */
                    aBass[i] = aOld[i] - nOctaves * 12;
            } else {
                aBass[i] = Integer.MIN_VALUE;
            }
        }

        System.out.print("Bass: ");
        printArray(aBass);

        arrayToPhrase(phBass, aBass);
        pBass.add(phBass);
        s.add(pBass);
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
