import javax.swing.*;
import java.io.IOException;

/**
 * A game loop that runs for each level of the game.
 */
public class Loop extends SwingWorker<Void,Integer> {

    private long currentTime;
    private long newTime;
    private long frameTime;
    private long accumulator = 0;
    private final long delta = 17;  // 17 milliseconds is approximately 1/60 seconds.

    private Panel panel;

    public Loop(Panel panel) {
        this.panel = panel;
    }

    public Void doInBackground() throws IOException {
        currentTime = System.currentTimeMillis();

        while(true) {
            // Calculate time passed since the last frame.
            newTime = System.currentTimeMillis();
            frameTime = newTime - currentTime;
            currentTime = newTime;

            // In case of lag, add up the actual time passed since the last frame, then go through the update loop and
            // subtract the defined target frame time (delta) from the accumulated lag time, and repeat the loop until
            // the accumulated lag time has been consumed.
            accumulator += frameTime;
            while(accumulator >= delta) {

                accumulator -= delta;
            }

            panel.peent();
        }

        //return null;
    }
}
