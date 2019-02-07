package lanerush;

/**
 *
 * @author Wernich
 */
public abstract class Structure extends GameObject {
    
    protected int health;

    public Structure(ID id, boolean playerLeft, Handler handler, SpriteSheet ss) {
        super(id, playerLeft, handler, ss);
    }
    
    public abstract void initCoords();
    
}