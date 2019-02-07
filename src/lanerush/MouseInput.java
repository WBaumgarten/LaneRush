package lanerush;

import lanerush.gatherers.Lumberjack;
import lanerush.gatherers.Miner;
import lanerush.units.Warrior;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Wernich
 */
public class MouseInput extends MouseAdapter {

    private Handler handler;

    public MouseInput(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mx = (int) (e.getX());
        int my = (int) (e.getY());

        //FOR TESTING ONLY
//        if (e.getButton() == MouseEvent.BUTTON2) {
//            handler.addObject(new Miner(ID.Miner, true, handler));
//            handler.addObject(new Miner(ID.Miner, false, handler));
//            handler.addObject(new Lumberjack(ID.Lumberjack, true, handler));
//            handler.addObject(new Lumberjack(ID.Lumberjack, false, handler));
//        }
    }
}
