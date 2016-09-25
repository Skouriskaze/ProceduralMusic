package Models;

import jm.music.data.Note;
import jm.music.data.Score;
import jm.util.Play;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesse on 9/25/2016.
 */
public class AsyncPlayer {

    //public static List<Score> song = new ArrayList<>();

    public static void playSong(List<Score> song) {
        Play.stopMidi();
        Play.midiCycle(song.get(0));
        Score current = song.get(0);
        for (int i = 1; i < song.size(); i++) {
            try {
                Thread.sleep((int) (current.getTempo() / 60 * current.getEndTime()));
            } catch (Exception e) {

            }

            Play.updateScore(song.get(i));
        }
    }

    public static void play(Score s) {
        Play.stopMidi();
        Score ss = s.copy();
        Thread play = new Thread(() -> Play.midi(s));
        play.start();
    }
    public static void play(Note s) {
        Note ss = s.copy();
        Play.stopMidi();
        Thread play = new Thread(() -> Play.midi(s));
        play.start();
    }
}
