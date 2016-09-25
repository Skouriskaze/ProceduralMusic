package RiffForm;

import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.SwipeGesture;
import com.leapmotion.leap.Vector;

/**
 * Created by Jesse on 9/24/2016.
 */
public class LeapListener extends Listener implements InputListener {
    private GestureList last;
    private RiffModel model;
    private GestureState state;

    public enum GestureState {
        SWIPING,
        CIRCLING,
        PINCHING,
        NONE;
    }

    @Override
    public void onInit(Controller controller) {
        last = new GestureList();
        state = GestureState.NONE;
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
        if (!gestures.isEmpty()) {
            for (int i = 0; i < gestures.count(); i++) {
                Gesture ges = gestures.get(i);
                switch (ges.type()) {
                    case TYPE_SWIPE:
                        if (state == GestureState.NONE || state == GestureState.SWIPING) {
                            state = GestureState.SWIPING;
                            last = gestures;
                        }
//                        SwipeGesture swipe = new SwipeGesture(ges);
//                        System.out.print(swipe.id() + ": ");
//                        System.out.println(swipe.direction());
                        break;
                    case TYPE_CIRCLE:
//                        CircleGesture circle = new CircleGesture(ges);
                        if (state == GestureState.NONE || state == GestureState.CIRCLING) {
                            state = GestureState.CIRCLING;
                            last = gestures;
                        }
                        break;
                    default:
                        state = GestureState.NONE;
                        break;
                }
            }
        } else {
            if (state == GestureState.SWIPING) {
                System.out.println("I was swiping.");
                Vector swipeDirection = new Vector(0, 0, 0);
                for (int i = 0; i < last.count(); i++) {
                    Gesture ges = last.get(i);
                    if (ges.type() == Gesture.Type.TYPE_SWIPE) {
                        SwipeGesture swipe = new SwipeGesture(ges);
                        swipeDirection = swipeDirection.plus(swipe.direction());
                    }
                }

                float yAngle = swipeDirection.angleTo(Vector.yAxis());
                if (yAngle < Math.PI / 6) {
                    model.eventUpPressed();
                } else if (yAngle > Math.PI * 5 / 6) {
                    model.eventDownPressed();
                }
            } else if (state == GestureState.CIRCLING) {
                int[] direction = new int[] {0, 0};
                for (int i = 0; i < last.count(); i++) {
                    Gesture ges = last.get(i);
                    if (ges.type() == Gesture.Type.TYPE_CIRCLE) {
                        CircleGesture circle = new CircleGesture(ges);
                        boolean clockwise = circle.pointable().direction().angleTo(circle.normal()) <= Math.PI / 2;
                        if (circle.radius() > 20) {
                            direction[clockwise ? 0 : 1]++;
                        }
                    }
                }

                if (direction[0] < direction[1]) {
                    model.eventLeftPressed();
                } else if (direction[1] < direction[0]) {
                    model.eventRightPressed();
                }
            }

            if (!frame.hands().isEmpty()) {
                HandList hands = frame.hands();
                float fStrength = hands.get(0).grabStrength();

                if (fStrength > 0.9f) {
                    if (state == GestureState.NONE) {
                        model.eventEnterPressed();
                    }
                    state = GestureState.PINCHING;
                } else {
                    state = GestureState.NONE;
                }
            }
        }

    }

    @Override
    public void addModel(RiffModel model) {
        this.model = model;
    }
}
