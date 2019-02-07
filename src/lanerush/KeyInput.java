package lanerush;

import lanerush.gatherers.Lumberjack;
import lanerush.gatherers.Miner;
import lanerush.units.Archer;
import lanerush.units.Cavalry;
import lanerush.units.Knight;
import lanerush.units.Warrior;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author Wernich
 */
public class KeyInput extends KeyAdapter {

    Handler handler;
    Game game;
    SpriteSheet ss;

    public KeyInput(Handler handler, Game game, SpriteSheet ss) {
        this.handler = handler;
        this.game = game;
        this.ss = ss;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_1) { //Warrior
            for (GameObject object : handler.object) {
                if (object.getId() == ID.Cursor && object.isPlayerLeft()
                        && object.checkCost(Warrior.GOLDCOST, Warrior.TIMBERCOST)) {
                    handler.addObject(new Warrior(ID.Warrior, true, object.getX(), object.getY(), object.lane , handler, ss));
                    return;
                }
            }            
        } else if (key == KeyEvent.VK_2) { //Archer
            for (GameObject object : handler.object) {
                if (object.getId() == ID.Cursor && object.isPlayerLeft()
                        && object.checkCost(Archer.GOLDCOST, Archer.TIMBERCOST)) {
                    handler.addObject(new Archer(ID.Archer, true, object.getX(), object.getY(), object.lane , handler, ss));
                    return;
                }
            }     
        } else if (key == KeyEvent.VK_3) { //Knight
            for (GameObject object : handler.object) {
                if (object.getId() == ID.Cursor && object.isPlayerLeft()
                        && object.checkCost(Knight.GOLDCOST, Knight.TIMBERCOST)) {
                    handler.addObject(new Knight(ID.Knight, true, object.getX(), object.getY(), object.lane , handler, ss));
                    return;
                }
            }     
        } else if (key == KeyEvent.VK_4) { //Cavalry
            for (GameObject object : handler.object) {
                if (object.getId() == ID.Cursor && object.isPlayerLeft()
                        && object.checkCost(Cavalry.GOLDCOST, Cavalry.TIMBERCOST)) {
                    handler.addObject(new Cavalry(ID.Cavalry, true, object.getX(), object.getY(), object.lane , handler, ss));
                    return;
                }
            }     
        } else if (key == KeyEvent.VK_5) { //Miner
            for (GameObject object : handler.object) {
                if (object.getId() == ID.Cursor && object.isPlayerLeft()
                        && object.checkCost(Miner.GOLDCOST, Miner.TIMBERCOST)) {
                    handler.addObject(new Miner(ID.Miner, true, object.lane, handler, ss));
                    return;
                }
            }     
        } else if (key == KeyEvent.VK_6) { //Lumberjack
            for (GameObject object : handler.object) {
                if (object.getId() == ID.Cursor && object.isPlayerLeft()
                        && object.checkCost(Lumberjack.GOLDCOST, Lumberjack.TIMBERCOST)) {
                    handler.addObject(new Lumberjack(ID.Lumberjack, true, object.lane, handler, ss));
                    return;
                }
            }     
        } else if (key == KeyEvent.VK_NUMPAD1) { //Warrior
            for (GameObject object : handler.object) {
                if (object.getId() == ID.Cursor && !object.isPlayerLeft()
                        && object.checkCost(Warrior.GOLDCOST, Warrior.TIMBERCOST)) {
                    handler.addObject(new Warrior(ID.Warrior, false, object.getX(), object.getY(), object.lane , handler, ss));
                    return;
                }
            }        
        } else if (key == KeyEvent.VK_NUMPAD2) { //Archer
            for (GameObject object : handler.object) {
                if (object.getId() == ID.Cursor && !object.isPlayerLeft()
                        && object.checkCost(Archer.GOLDCOST, Archer.TIMBERCOST)) {
                    handler.addObject(new Archer(ID.Archer, false, object.getX(), object.getY(), object.lane , handler, ss));
                    return;
                }
            }     
        } else if (key == KeyEvent.VK_NUMPAD3) { //Knight
            for (GameObject object : handler.object) {
                if (object.getId() == ID.Cursor && !object.isPlayerLeft()
                        && object.checkCost(Knight.GOLDCOST, Knight.TIMBERCOST)) {
                    handler.addObject(new Knight(ID.Knight, false, object.getX(), object.getY(), object.lane , handler, ss));
                    return;
                }
            }     
        } else if (key == KeyEvent.VK_NUMPAD4) { //Cavalry
            for (GameObject object : handler.object) {
                if (object.getId() == ID.Cursor && !object.isPlayerLeft()
                        && object.checkCost(Cavalry.GOLDCOST, Cavalry.TIMBERCOST)) {
                    handler.addObject(new Cavalry(ID.Cavalry, false, object.getX(), object.getY(), object.lane , handler, ss));
                    return;
                }
            }     
        } else if (key == KeyEvent.VK_NUMPAD5) { //Miner
            for (GameObject object : handler.object) {
                if (object.getId() == ID.Cursor && !object.isPlayerLeft()
                        && object.checkCost(Miner.GOLDCOST, Miner.TIMBERCOST)) {
                    handler.addObject(new Miner(ID.Miner, false, object.lane, handler, ss));
                    return;
                }
            }     
        } else if (key == KeyEvent.VK_NUMPAD6) { //Lumberjack
            for (GameObject object : handler.object) {
                if (object.getId() == ID.Cursor && !object.isPlayerLeft()
                        && object.checkCost(Lumberjack.GOLDCOST, Lumberjack.TIMBERCOST)) {
                    handler.addObject(new Lumberjack(ID.Lumberjack, false, object.lane, handler, ss));
                    return;
                }
            }     
        } else if (key == KeyEvent.VK_SPACE) {
            game.tick();
            game.render();
        } else if (key == KeyEvent.VK_W) {
            for (GameObject object : handler.object) {
                if (object.getId() == ID.Cursor && object.isPlayerLeft()) {
                    object.moveUp();
                }
            }
        } else if (key == KeyEvent.VK_S) {
            for (GameObject object : handler.object) {
                if (object.getId() == ID.Cursor && object.isPlayerLeft()) {
                    object.moveDown();
                }
            }
        } else if (key == KeyEvent.VK_UP) {
            for (GameObject object : handler.object) {
                if (object.getId() == ID.Cursor && !object.isPlayerLeft()) {
                    object.moveUp();
                }
            }
        } else if (key == KeyEvent.VK_DOWN) {
            for (GameObject object : handler.object) {
                if (object.getId() == ID.Cursor && !object.isPlayerLeft()) {
                    object.moveDown();
                }
            }
        }

    }

}
