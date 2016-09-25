package RiffForm;

import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.SwipeGesture;

/**
 * Created by Jesse on 9/24/2016.
 */
public class LeapListener extends Listener implements InputListener {
    Frame last;

    @Override
    public void onInit(Controller controller) {
        last = new Frame();
        System.out.println("Initialized");
    }

    @Override
    public void onConnect(Controller controller) {
        System.out.println("Connected");
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
    }

    @Override
    public void onDisconnect(Controller controller) {
        System.out.println("Disconnected");
    }

    @Override
    public void onExit(Controller controller) {
        System.out.println("Exited");
    }

    @Override
    public void onFrame(Controller controller) {
        Frame frame = controller.frame();
        GestureList gestures = frame.gestures();
        for (int i = 0; i < gestures.count(); i++) {
            Gesture ges = gestures.get(i);
            switch (ges.type()) {
                case TYPE_SWIPE:
                    SwipeGesture swipe = new SwipeGesture(ges);
                    System.out.println(swipe.direction());
                    break;
                case TYPE_CIRCLE:
                    CircleGesture circle = new CircleGesture(ges);
                    boolean clockwise = circle.pointable().direction().angleTo(circle.normal()) <= Math.PI/2;
                    break;
                default:
                    break;
            }
        }

        last = frame;
    }

    @Override
    public void addModel(RiffModel model) {

    }
}
