package lanerush;

import java.awt.Graphics;
import java.util.LinkedList;

/**
 *
 * @author Wernich
 */
public class Handler {

    public LinkedList<GameObject> object = new LinkedList<GameObject>();

    public void tick() {
        for (int i = 0; i < object.size(); i++) {
            GameObject tempObject = object.get(i);

            tempObject.tick();
        }
    }

    public void render(Graphics g) {
        for (int i = 0; i < object.size(); i++) {
            GameObject tempObject = object.get(i);

            tempObject.render(g);
        }
    }

    public void addObject(GameObject tempObject) {
        int lane = tempObject.getLane();
        for (int i = 0; i < object.size(); i++) {
            GameObject laneCheck = object.get(i);
            if (lane >= laneCheck.getLane()) {
                object.add(i, tempObject);
                return;
            }
        }
        object.add(tempObject);
    }

    public void removeObject(GameObject tempObject) {
        object.remove(tempObject);
    }

    public void removeAllObjects() {
        object.clear();
    }

}
