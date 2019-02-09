package lanerush.gatherers;

import lanerush.Cursor;
import lanerush.GameObject;
import lanerush.Gatherer;
import lanerush.Handler;
import lanerush.ID;
import lanerush.SpriteSheet;
import lanerush.structures.Castle;
import lanerush.structures.Mine;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;
import lanerush.Animation;

/**
 *
 * @author Wernich
 */
public class Miner extends Gatherer {

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

    public Miner(ID id, boolean playerLeft, int lane, Handler handler, SpriteSheet ss) {
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
            stillImg = ss.grabImage(1, 11, 64, 64);
            walkImgLeft[0] = ss.grabImage(3, 13, 64, 64);
            walkImgLeft[1] = ss.grabImage(5, 13, 64, 64);
            walkImgLeft[2] = ss.grabImage(7, 13, 64, 64);
            walkImgLeft[3] = ss.grabImage(5, 13, 64, 64);
            walkImgRight[0] = ss.grabImage(3, 11, 64, 64);
            walkImgRight[1] = ss.grabImage(5, 11, 64, 64);
            walkImgRight[2] = ss.grabImage(7, 11, 64, 64);
            walkImgRight[3] = ss.grabImage(5, 11, 64, 64);
            gatherImg[0] = stillImg;
            gatherImg[1] = ss.grabImage(1, 15, 64, 64);
            gatherImg[2] = ss.grabImage(3, 15, 64, 64);
            gatherImg[3] = ss.grabImage(1, 15, 64, 64);
            
        } else {
            stillImg = ss.grabImage(9, 11, 64, 64);
            walkImgLeft[0] = ss.grabImage(11, 11, 64, 64);
            walkImgLeft[1] = ss.grabImage(13, 11, 64, 64);
            walkImgLeft[2] = ss.grabImage(15, 11, 64, 64);
            walkImgLeft[3] = ss.grabImage(13, 11, 64, 64);
            walkImgRight[0] = ss.grabImage(11, 13, 64, 64);
            walkImgRight[1] = ss.grabImage(13, 13, 64, 64);
            walkImgRight[2] = ss.grabImage(15, 13, 64, 64);
            walkImgRight[3] = ss.grabImage(13, 13, 64, 64);
            gatherImg[0] = stillImg;
            gatherImg[1] = ss.grabImage(9, 15, 64, 64);
            gatherImg[2] = ss.grabImage(11, 15, 64, 64);
            gatherImg[3] = ss.grabImage(9, 15, 64, 64);
        }
        walkAnimLeft = new Animation(10, walkImgLeft);
        walkAnimRight = new Animation(10, walkImgRight);
        gatherAnim = new Animation(10, gatherImg);
    }

    @Override
    public void initCoords() {
        for (GameObject object : handler.object) {
            if (object.getId() == ID.Mine && object.isPlayerLeft() == isPlayerLeft()) {
                destX = object.getX();
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
            randSpot = rand.nextInt(((destX + Mine.WIDTH - WIDTH) - destX) + 1)
                    + destX;
            state = TOGATHER;
        } else if (state == TOGATHER) {
            if (randSpot > x && y < castleY - (HEIGHT * 1.5)) {
                velX = speed;
                x += speed;
            } else if (randSpot < x && y < castleY - (HEIGHT * 1.5)) {
                velX = -1 * speed;
                x -= speed;
            }
            if (destY + 32 >= y) {
                gatherTimer = System.nanoTime();
                state = GATHERING;
            } else if (destY < y) {
                velY = -1 * speed;
                y -= speed;
            }
        } else if (state == GATHERING) {
            velX = 0;
            velY = 0;
            long currTime = System.nanoTime();
            if (currTime - gatherTimer > gatherTime) {
                state = TOBASE;
            }
        } else if (state == TOBASE) {
            if (castleX > x) {
                velX = speed;
                x += speed;
            } else if (castleX < x) {
                velX = -1 * speed;
                x -= speed;
            }
            if (castleY <= y) {
                state = ATBASE;
                for (GameObject object : handler.object) {
                    if (object.getId() == ID.Cursor && object.isPlayerLeft() == isPlayerLeft()) {
                        object.resourceInflux(Cursor.GOLD);
                        return;
                    }
                }
            } else if (castleY > y) {
                velY = speed;
                y += speed;
            }
        }
        walkAnimLeft.runAnimation();
        walkAnimRight.runAnimation();
        gatherAnim.runAnimation();
    }

    @Override
    public void render(Graphics g) {
        if (velX == 0) {
            if (isPlayerLeft()) {
                gatherAnim.drawAnimation(g, x, y - 32, 0);
            } else {
                gatherAnim.drawAnimation(g, x - 32, y - 32, 0);
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
