package lanerush;

import java.awt.Rectangle;

/**
 *
 * @author Wernich
 */
public abstract class Unit extends GameObject {

    protected double velX = 0, velY = 0;
    protected int health;
    protected GameObject target;
    protected int damage;
    protected int speed;
    protected long cooldown;
    protected long cooldownTimer;
    protected long deathTimer;

    public Unit(ID id, boolean playerLeft, Handler handler, SpriteSheet ss) {
        super(id, playerLeft, handler, ss);
    }

    public abstract Rectangle getAttackBounds();

    public abstract Rectangle getMovementBounds();

    public abstract void findTarget();

    public abstract void attack();

    public void initCoords(int xCoord, int yCoord) {
        if (playerLeft) {
            x = xCoord;//180;
            y = yCoord;
        } else {
            x = xCoord;//982;
            y = yCoord;
        }
    }

    @Override
    public void loseHealth(int damage) {
        health -= damage;
        if (health <= 0) {
            //deathTimer = System.nanoTime();
            handler.removeObject(this);
        }
    }

    @Override
    public int getHealth() {
        return health;
    }

}
