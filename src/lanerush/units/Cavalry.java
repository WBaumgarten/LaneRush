package lanerush.units;

import lanerush.GameObject;
import lanerush.Handler;
import lanerush.ID;
import lanerush.SpriteSheet;
import lanerush.Unit;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author Wernich
 */
public class Cavalry extends Unit {
    
    public static final int GOLDCOST = 30, TIMBERCOST = 30;

    public Cavalry(ID id, boolean playerLeft, int xCoord, int yCoord, int lane, Handler handler, SpriteSheet ss) {
        super(id, playerLeft, handler, ss);
        this.setLane(lane);
        initCoords(xCoord, yCoord);
        health = 40;
        damage = 30;
        cooldown = 1000000000;
        cooldownTimer = System.nanoTime();
        target = null;
        speed = 4;
        velX = 1;
    }

    @Override
    public Rectangle getAttackBounds() {
        if (playerLeft) {
            return new Rectangle(x + 32, y + 8, 48, 16);
        } else {
            return new Rectangle(x - 48, y + 8, 48, 16);
        }
    }

    @Override
    public Rectangle getMovementBounds() {
        if (playerLeft) {
            return new Rectangle(x + 32, y + 8, 16, 16);
        } else {
            return new Rectangle(x - 16, y + 8, 16, 16);
        }
    }

    @Override
    public void tick() {
        if (target == this) {
            velX = 0;
            findTarget();
            return;
        }
        if (target != null) {
            velX = 0;
            attack();
        } else {
            findTarget();
            if (playerLeft) {
                velX = speed;
            } else {
                velX = -speed;
            }

            x += velX;
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(playerColor);
        if (isPlayerLeft()) {
            g.fillRect(x - 32, y, 64, 32);
        } else {
            g.fillRect(x, y, 64, 32);
        }

        g.setColor(Color.PINK);
        g.fillRect(x + 12, y + 12, 8, 8);
        g.setColor(Color.WHITE);
        g.drawString(health + "", x, y);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.draw(getAttackBounds());
        g2d.setColor(Color.GREEN);
        g2d.draw(getMovementBounds());
    }

    @Override
    public Rectangle getBounds() {
        if (isPlayerLeft()) return new Rectangle(x - 32, y, 64, 32);
        else return new Rectangle(x, y, 64, 32);
    }

    @Override
    public void moveUp() {
    }

    @Override
    public void moveDown() {
    }

    @Override
    public void findTarget() {
        double distance;
        double nearestDistance = -1;
        GameObject nearestEnemy = null;
        for (GameObject object : handler.object) {
            if (object == this
                    || object.getId() == ID.Cursor
                    || object.getId() == ID.Castle
                    && object.isPlayerLeft() == isPlayerLeft()) {
                continue; //if on own team, continue
            }
            if (object.getId() == ID.Castle
                    && getBounds().intersects(object.getBounds())) {
                target = object;
                return;
            }
            if (getAttackBounds().intersects(object.getBounds())
                    && object.isPlayerLeft() != playerLeft) {
                distance = Math.sqrt((object.getX() - x) * (object.getX() - x)
                        + (object.getY() - y) * (object.getY() - y));
                if (distance < nearestDistance || nearestDistance == -1) {
                    nearestDistance = distance;
                    nearestEnemy = object;
                }
            } else if (getMovementBounds().intersects(object.getBounds())
                    && object.isPlayerLeft() == playerLeft) {
                target = this; //collision
                return;
            }
        }
        if (nearestDistance != -1) {
            target = nearestEnemy;
        } else {
            target = null;
        }
    }

    @Override
    public void attack() {
        long currTime = System.nanoTime();
        if (target.getHealth() <= 0) {
            target = null;
            return;
        }
        if (currTime - cooldownTimer > cooldown) {
            if (target != null) {
                target.loseHealth(damage);
                cooldownTimer = currTime;
            }
        }
    }

    @Override
    public void resourceInflux(int type) {
    }

}
