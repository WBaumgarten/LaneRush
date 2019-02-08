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
import java.awt.image.BufferedImage;
import lanerush.Animation;

/**
 *
 * @author Wernich
 */
public class Knight extends Unit{
    
    public static final int GOLDCOST = 30, TIMBERCOST = 30;
    private final BufferedImage stillImg;
    private final BufferedImage[] walkImg = new BufferedImage[4];
    private final Animation walkAnim;

    public Knight(ID id, boolean playerLeft, int xCoord, int yCoord, int lane, Handler handler, SpriteSheet ss) {
        super(id, playerLeft, handler, ss);
        this.setLane(lane);
        initCoords(xCoord, yCoord);
        health = 100;
        damage = 5;
        cooldown = 1000000000;
        cooldownTimer = System.nanoTime();
        target = null;
        speed = 1;
        velX = 1;
        
        if (isPlayerLeft()) {
            stillImg = ss.grabImage(1, 7, 64, 64);
            walkImg[0] = ss.grabImage(3, 7, 64, 64);
            walkImg[1] = ss.grabImage(5, 7, 64, 64);
            walkImg[2] = ss.grabImage(7, 7, 64, 64);
            walkImg[3] = ss.grabImage(5, 7, 64, 64);
        } else {
            stillImg = ss.grabImage(9, 7, 64, 64);
            walkImg[0] = ss.grabImage(11, 7, 64, 64);
            walkImg[1] = ss.grabImage(13, 7, 64, 64);
            walkImg[2] = ss.grabImage(15, 7, 64, 64);
            walkImg[3] = ss.grabImage(13, 7, 64, 64);
        }
        walkAnim = new Animation(10, walkImg);
    }

    @Override
    public Rectangle getAttackBounds() {
        if (playerLeft) {
            return new Rectangle(x + 32, y + 8, 16, 16);
        } else {
            return new Rectangle(x - 16, y + 8, 16, 16);
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
        walkAnim.runAnimation();
    }

    @Override
    public void render(Graphics g) {
        if (velX == 0) {
            if (isPlayerLeft()) {
                g.drawImage(stillImg, x, y - 32, null);
            } else {
                g.drawImage(stillImg, x - 32, y - 32, null);
            }
        } else {
            if (isPlayerLeft()) {
                walkAnim.drawAnimation(g, x, y - 32, 0);
            } else {
                walkAnim.drawAnimation(g, x - 32, y - 32, 0);
            }
        }
//        g.setColor(playerColor);
//        g.fillRect(x, y, 32, 32);
//        g.setColor(Color.GREEN);
//        g.fillRect(x + 12, y + 12, 8, 8);
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
        return new Rectangle(x, y, 32, 32);
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
