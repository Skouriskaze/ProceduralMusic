package Models;

import RiffForm.InputListener;
import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Play;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Jesse on 9/25/2016.
 */
public class NewMusicGenerator implements JMC {
    private Score root;
    private Score song;
    private int base;

    private double dNoteSimilarity;
    private double dRhythmSimilarity;

    private Phrase currentPhrase;

    private Random rand;

    public NewMusicGenerator(Score s, int base, double noteSim, double rhythmSim) {
        root = s;
        song = root.copy();
        this.base = base;
        currentPhrase = root.getPart(0).getPhrase(0);

        dNoteSimilarity = noteSim;
        dRhythmSimilarity = rhythmSim;

        rand = new Random();
    }

    public NewMusicGenerator(Score s, int base) {
        this(s, base, 0.5, 0.5);
    }

    private Map<Integer, Double> countPhraseNotes() {
        Map<Integer, Double> freq = new HashMap<>();
        int[] pitches = currentPhrase.getPitchArray();

        for (int n : pitches) {
            if (freq.keySet().contains(n)) {
                freq.put(n, freq.get(n) + 1);
            } else {
                freq.put(n, 1.0);
            }
        }

        for (Integer n : freq.keySet()) {
            freq.put(n, freq.get(n) / pitches.length);
        }

        return freq;
    }

    private Map<Double, Double> countPhraseRhythm() {
        Map<Double, Double> freq = new HashMap<>();
        double[] rhythmArray = currentPhrase.getRhythmArray();

        for (double d : rhythmArray) {
            if (freq.keySet().contains(d)) {
                freq.put(d, freq.get(d) + 1);
            } else {
                freq.put(d, 1.0);
            }
        }

        for (double d : freq.keySet()) {
            freq.put(d, freq.get(d) / rhythmArray.length);
        }

        return freq;
    }

    private void appendCurrentPhrase(Phrase ph) {
        double[] freq = currentPhrase.getRhythmArray();
        currentPhrase.addNoteList(ph.getNoteArray());

        int nNotes = 0;
        int nBeats = 0;
        do {
            nBeats += freq[nNotes++];
        } while (nBeats <= 4);

        for (int i = 0; i < nNotes; i++) {
            currentPhrase.removeNote(0);
        }
    }

    private Phrase makeNewPhrase() {
        Phrase ph = new Phrase();
        List<Integer> pitches = new ArrayList<>();
        List<Double> rhythms = new ArrayList<>();

        Map<Integer, Double> noteFreq = countPhraseNotes();
        Map<Double, Double> rhythmFreq = countPhraseRhythm();

        double totalRhy = 0;
        double rhy = 0;
        boolean bRhySim = rand.nextDouble() < dRhythmSimilarity;
        do {
            if (bRhySim) {
                boolean bEqualChance = rand.nextDouble() > dRhythmSimilarity;
                if (bEqualChance) {
                    rhy = new double[]{1, 2, 4}[rand.nextInt(3)];
                    while (rhy + totalRhy > 4) {
                        rhy /= 2;
                    }
                } else {
                    double prob = rand.nextDouble();
                    for (Double n : rhythmFreq.keySet()) {
                        if (prob < rhythmFreq.get(n)) {
                            rhy = n;
                            break;
                        } else {
                            prob -= rhythmFreq.get(n);
                        }
                    }
                    while (rhy + totalRhy > 4) {
                        rhy /= 2;
                    }
                }
                rhythms.add(rhy);
                totalRhy += rhy;
            } else {
                rhy = new double[]{1, 2, 4}[rand.nextInt(3)];
                while (rhy + totalRhy > 4) {
                    rhy /= 2;
                }
                rhythms.add(rhy);
                totalRhy += rhy;
            }
        } while (totalRhy < 4);

        boolean bNoteSim = rand.nextDouble() < dNoteSimilarity;
        for (int i = 0; i < rhythms.size(); i++) {
            int pitch = 0;
            if (bNoteSim) {
                // Find similar notes
                boolean bEqualChance = rand.nextDouble() > dNoteSimilarity;
                if (bEqualChance) {
                    pitch = rand.nextInt(14) + base;
                } else {
                    double prob = rand.nextDouble();
                    for (Integer n : noteFreq.keySet()) {
                        if (prob < noteFreq.get(n)) {
                            pitch = n;
                            break;
                        } else {
                            prob -= noteFreq.get(n);
                        }
                    }
                }
                pitches.add(pitch);
            } else {
                // Find dissimilar notes
                pitch = rand.nextInt(14) + base;
                pitches.add(pitch);
            }
        }

        int[] aPitches = new int[pitches.size()];
        double[] aRhythms = new double[rhythms.size()];
        for (int i = 0; i < pitches.size(); i++) {
            aPitches[i] = pitches.get(i);
            aRhythms[i] = rhythms.get(i);
        }

        ph.addNoteList(aPitches, aRhythms);

        return ph;
    }
}
