package lanerush;

/**
 *
 * @author Wernich
 */
public abstract class Gatherer extends GameObject{
    
    protected final int TOGATHER = 0, TOBASE = 1, ATBASE = 2, GATHERING = 3;
    protected int state;
    protected int capacity;
    protected int maxCapacity = 100;
    protected double speed;
    protected long gatherTime;
    protected long gatherTimer;
    protected int destX;
    protected int destY;
    protected int castleX;
    protected int castleY;

    public Gatherer(ID id, boolean playerLeft, Handler handler, SpriteSheet ss) {
        super(id, playerLeft, handler, ss);
    }
    
    public abstract void initCoords();

}
