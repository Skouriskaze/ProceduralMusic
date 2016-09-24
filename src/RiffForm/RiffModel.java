package RiffForm;

import jm.music.data.Score;

/**
 * Created by Jesse on 9/24/2016.
 */
public interface RiffModel {

    /**
     * Moves the current note down.
     */
    void eventDownPressed();
    /**
     * Moves the current note up.
     */
    void eventUpPressed();

    /**
     * Moves the current note left.
     */
    void eventLeftPressed();

    /**
     * Moves the current note right.
     */
    void eventRightPressed();

    /**
     * Places the current note.
     */
    void eventDneterPressed();
    /**
     * Gets the user-defined riff
     * @return the score of the user's rift
     */
    Score getScore();
}
