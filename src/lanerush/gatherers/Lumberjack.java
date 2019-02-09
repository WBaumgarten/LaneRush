package lanerush.gatherers;

import lanerush.Cursor;
import lanerush.GameObject;
import lanerush.Gatherer;
import lanerush.Handler;
import lanerush.ID;
import lanerush.SpriteSheet;
import lanerush.structures.Castle;
import lanerush.structures.Woods;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;
import lanerush.Animation;

/**
 *
 * @author Wernich
 */
public class Lumberjack extends Gatherer {

    public static final int GOLDCOST = 0, TIMBERCOST = 0;
    public static final int WIDTH = 32;
    public static final int HEIGHT = 32;
    private int randSpot;
    
    private final BufferedImage stillImg;
    private final BufferedImage[] walkImgLeft = new BufferedImage[4];
    private final Animation walkAnimLeft;
    private final BufferedImage[] walkImgRight = new BufferedImage[4];
    private final Animation walkAnimRight;
    private final BufferedImage[] gatherImg = new BufferedImage[4];
    private final Animation gatherAnim;

    public Lumberjack(ID id, boolean playerLeft, int lane, Handler handler, SpriteSheet ss) {
        super(id, playerLeft, handler, ss);
        this.setLane(lane);
        state = ATBASE;
        speed = 1;
        gatherTime = 2000000000;
        initCoords();
        if (isPlayerLeft()) {
            velX = 1;
        } else {
            velX = -1;
        }
        
        if (isPlayerLeft()) {
            stillImg = ss.grabImage(1, 17, 64, 64);
            walkImgLeft[0] = ss.grabImage(3, 19, 64, 64);
            walkImgLeft[1] = ss.grabImage(5, 19, 64, 64);
            walkImgLeft[2] = ss.grabImage(7, 19, 64, 64);
            walkImgLeft[3] = ss.grabImage(5, 19, 64, 64);
            walkImgRight[0] = ss.grabImage(3, 17, 64, 64);
            walkImgRight[1] = ss.grabImage(5, 17, 64, 64);
            walkImgRight[2] = ss.grabImage(7, 17, 64, 64);
            walkImgRight[3] = ss.grabImage(5, 17, 64, 64);
            gatherImg[0] = ss.grabImage(1, 21, 64, 64);;
            gatherImg[1] = ss.grabImage(3, 21, 64, 64);
            gatherImg[2] = ss.grabImage(5, 21, 64, 64);
            gatherImg[3] = ss.grabImage(3, 21, 64, 64);
            
        } else {
            stillImg = ss.grabImage(9, 17, 64, 64);
            walkImgLeft[0] = ss.grabImage(11, 17, 64, 64);
            walkImgLeft[1] = ss.grabImage(13, 17, 64, 64);
            walkImgLeft[2] = ss.grabImage(15, 17, 64, 64);
            walkImgLeft[3] = ss.grabImage(13, 17, 64, 64);
            walkImgRight[0] = ss.grabImage(11, 19, 64, 64);
            walkImgRight[1] = ss.grabImage(13, 19, 64, 64);
            walkImgRight[2] = ss.grabImage(15, 19, 64, 64);
            walkImgRight[3] = ss.grabImage(13, 19, 64, 64);
            gatherImg[0] = ss.grabImage(9, 21, 64, 64);
            gatherImg[1] = ss.grabImage(11, 21, 64, 64);
            gatherImg[2] = ss.grabImage(13, 21, 64, 64);
            gatherImg[3] = ss.grabImage(11, 21, 64, 64);
        }
        walkAnimLeft = new Animation(10, walkImgLeft);
        walkAnimRight = new Animation(10, walkImgRight);
        gatherAnim = new Animation(10, gatherImg);
    }

    @Override
    public void initCoords() {
        for (GameObject object : handler.object) {
            if (object.getId() == ID.Woods && object.isPlayerLeft() == isPlayerLeft()) {
                if (isPlayerLeft()) {
                    destX = object.getX() + Woods.WIDTH + (WIDTH / 4);
                } else {
                    destX = object.getX() - ((int) (WIDTH * 1.25));
                }
                destY = object.getY();
            } else if (object.getId() == ID.Castle && object.isPlayerLeft() == isPlayerLeft()) {
                castleX = object.getX() + (Castle.WIDTH / 2) - (WIDTH / 2);
                castleY = object.getY() + (Castle.HEIGHT / 2) - (HEIGHT / 2);
            }
        }
        x = castleX;
        y = castleY;
    }

    @Override
    public void tick() {
        if (state == ATBASE) {
            Random rand = new Random();
            randSpot = rand.nextInt(((destY + Woods.HEIGHT - HEIGHT) - destY) + 1)
                    + destY;
            state = TOGATHER;
        } else if (state == TOGATHER) {
            if (destX > x && y > castleY + Castle.HEIGHT + (HEIGHT * 1.5)) {
                velX = speed;
                x += speed;
            } else if (destX < x && y > castleY + Castle.HEIGHT + (HEIGHT * 1.5)) {
                velX = -1 * speed;
                x -= speed;
            }
            if (randSpot <= y) {
                gatherTimer = System.nanoTime();
                state = GATHERING;
            } else if (randSpot > y) {
                velY = speed;
                y += speed;
            }
        } else if (state == GATHERING) {
            velX = 0;
            velY = 0;
            long currTime = System.nanoTime();
            if (currTime - gatherTimer > gatherTime) {
                state = TOBASE;
            }
        } else if (state == TOBASE) {
            if (castleX > x && y < destY - (HEIGHT * 1.5)) {
                velX = speed;
                x += speed;
            } else if (castleX < x && y < destY - (HEIGHT * 1.5)) {
                velX = -1 * speed;
                x -= speed;
            }
            if (castleY >= y) {
                state = ATBASE;
                for (GameObject object : handler.object) {
                    if (object.getId() == ID.Cursor && object.isPlayerLeft() == isPlayerLeft()) {
                        object.resourceInflux(Cursor.TIMBER);
                        return;
                    }
                }
            } else if (castleY < y) {
                velY = -1 * speed;
                y -= speed;
            }
        }
        walkAnimLeft.runAnimation();
        walkAnimRight.runAnimation();
        gatherAnim.runAnimation();
    }

    @Override
    public void render(Graphics g) {
        if (velX == 0 && velY == 0) {
            if (isPlayerLeft()) {
                gatherAnim.drawAnimation(g, x - 32, y - 32, 0);
            } else {
                gatherAnim.drawAnimation(g, x, y - 32, 0);
            }
        } else if (velX > 0) {
            if (isPlayerLeft()) {
                walkAnimRight.drawAnimation(g, x, y - 32, 0);
            } else {
                walkAnimRight.drawAnimation(g, x, y - 32, 0);
            }
        } else if (velX < 0) {
            if (isPlayerLeft()) {
                walkAnimLeft.drawAnimation(g, x - 32, y - 32, 0);
            } else {
                walkAnimLeft.drawAnimation(g, x - 32, y - 32, 0);
            }
        } else if (velY > 0) {
            if (isPlayerLeft()) {
                walkAnimRight.drawAnimation(g, x, y - 32, 0);
            } else {
                walkAnimLeft.drawAnimation(g, x - 32, y - 32, 0);
            }          
        } else if (velY < 0) {
            if (isPlayerLeft()) {
                walkAnimLeft.drawAnimation(g, x - 32, y - 32, 0);
            } else {
                walkAnimRight.drawAnimation(g, x, y - 32, 0);
            }
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 32);
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

    @Override
    public void resourceInflux(int type) {
    }

}
