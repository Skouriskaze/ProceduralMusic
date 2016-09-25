package Models;

import jm.JMC;
import jm.music.data.Score;

/**
 * Created by Jesse on 9/25/2016.
 */
public class NewMusicGenerator implements JMC {
    private Score root;
    private int base;

    public NewMusicGenerator(Score s, int base) {
        root = s;
        this.base = base;
    }
}
