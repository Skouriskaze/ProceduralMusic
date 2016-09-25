package Models;

import jm.music.data.Note;
import jm.music.data.Score;
import jm.util.Play;

/**
 * Created by Jesse on 9/25/2016.
 */
public class AsyncPlayer {
    public static void play(Score s) {
        Score ss = s.copy();
        Thread play = new Thread(() -> Play.midi(s));
        play.start();
    }
    public static void play(Note s) {
        Note ss = s.copy();
        Thread play = new Thread(() -> Play.midi(s));
        play.start();
    }
}
