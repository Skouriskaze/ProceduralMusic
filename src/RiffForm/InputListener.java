package RiffForm;

/**
 * Created by Jesse on 9/24/2016.
 */
public interface InputListener {
    /**
     * Checks if the down command is activated
     * @return if down is activated
     */
    boolean isDownPressed();

    /**
     * Checks if the up command is activated
     * @return if up is activated
     */
    boolean isUpPressed();

    /**
     * Checks if the left command is activated
     * @return if left is activated
     */
    boolean isLeftPressed();

    /**
     * Checks if the right command is activated
     * @return if right is activated
     */
    boolean isRightPressed();

    /**
     * Checks if the enter command is activated
     * @return if enter is activated
     */
    boolean isEnterPressed();

}
