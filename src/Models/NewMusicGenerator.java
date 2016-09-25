package Models;

import jm.JMC;
import jm.music.data.Phrase;
import jm.music.data.Score;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jesse on 9/25/2016.
 */
public class NewMusicGenerator implements JMC {
    private Score root;
    private int base;

    private Phrase currentPhrase;

    public NewMusicGenerator(Score s, int base) {
        root = s;
        this.base = base;
        currentPhrase = root.getPart(0).getPhrase(0);
    }

    private Map<Integer, Integer> countPhraseNotes() {
        Map<Integer, Integer> freq = new HashMap<>();
        int[] pitches = currentPhrase.getPitchArray();

        for (int n : pitches) {
            if (freq.keySet().contains(n)) {
                freq.put(n, freq.get(n) + 1);
            } else {
                freq.put(n, 1);
            }
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
}
