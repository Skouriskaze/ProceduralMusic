package sample;

import jm.JMC;
import jm.music.data.Score;
import jm.util.Play;

/**
 * Created by Jesse on 9/24/2016.
 */
public class MusicGenerator implements JMC {

    private Score sRoot;

    public MusicGenerator(Score s) {
        sRoot = s;
    }

    public void play() {
        Runnable p = () -> Play.midi(sRoot);
        Thread thread = new Thread(p);
        thread.start();
    }
}
