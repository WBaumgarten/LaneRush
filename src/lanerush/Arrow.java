package lanerush;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Wernich
 */
public class Arrow extends GameObject{
    
    private GameObject archer, target;
    private int damage;

    public Arrow(ID id, boolean playerLeft, int damage, GameObject archer, GameObject target, Handler handler, SpriteSheet ss) {
        super(id, playerLeft, handler, ss);
        this.setLane(0);
        x = archer.getX();
        y = archer.getY();
        this.archer = archer;
        this.target = target;
        this.damage = damage;

    }

    @Override
    public void tick() {
        if (atDestination()) {
            target.loseHealth(damage);
            handler.removeObject(this);
        } else {
            int fX = target.getX() + 16;
            int fY = target.getY() + 16;
            double velX = (fX - x) / 10;
            double velY = (fY - y) / 10;
            x += velX;
            y += velY;
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillOval(x, y, 16, 16);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x - 8, y - 8, 16, 16);
    }

    @Override
    public void loseHealth(int damage) {
    }

    @Override
    public int getHealth() {
        return 0;
    }

    @Override
    public void moveUp() {
    }

    @Override
    public void moveDown() {
    }

    private boolean atDestination() {
        return getBounds().intersects(target.getBounds());
    }

    @Override
    public void resourceInflux(int type) {
    }
}
