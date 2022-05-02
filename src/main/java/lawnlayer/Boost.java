package lawnlayer;

import lawnlayer.Info.Name;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * A singleton subclass of GameObject and implementation of PowerUp.
 * This power up increases the Player's speed for a brief
 * period of time.
 */
public class Boost extends GameObject implements PowerUp {

    private static Boost boost = null;

    private static final int EFFECTDURATION  = 3;
    private static final int SPAWNDURATION = 10;
    private static final int[] SPAWNDELAY = new int[] { 5, 12 };
    private static final int[] RGB = new int[] { 120, 190, 33 };
    
    private boolean isVisible;
    private boolean inEffect;
    private int delay;
    private int stateChangeFrameCount;
    
    /**
     * Initialises the Boost power up with the specified sprite and name.
     * 
     * @param sprite the PImage sprite of the boost
     * @param name the name of the boost
     */
    private Boost(PImage sprite, Name name) {

        super(sprite, name);

        delay = SPAWNDELAY[0] + rand.nextInt(SPAWNDELAY[1] - SPAWNDELAY[0]);
        isVisible = false;
        inEffect = false;
        stateChangeFrameCount = 0;
    }

    /**
     * A 'static constructor' for Player. If multiple instantiation of Player
     * is attempted, an exception is thrown.
     * 
     * @param sprite the PImage sprite of the Boost
     * @param name the name of the boost
     * @return a single instance of Player
     * 
     * @see #Boost(PImage, Name)
     * @throws IllegalStateException if an instance already exists
     */
    public static Boost createBoost(PImage sprite, Name name) {

        if (boost == null) {
            boost = new Boost(sprite, name);
            return boost;
        }
        else throw
            new IllegalStateException("Boost has already been created");
    }

    /**
     * Removes the single instance of Boost.
     */
    public static void removeBoost() {

        if (boost != null) {
            boost = null;
        }
    }

    /**
     * Draws the power up.
     * 
     * @param app the PApplet instance to be drawn in
     * 
     * @see GameObject#draw(PApplet)
     */
    @Override
    public void draw(PApplet app) {

        if (isVisible)
            super.draw(app);
    }

    /**
     * @see PowerUp#getProgressBarWidth(int, int)
     */
    @Override
    public double getProgressBarWidth(int barWidth, int frameCount) {

        int frameDuration = Info.FPS * EFFECTDURATION;
        int frameRemaining =
            frameDuration - (frameCount - stateChangeFrameCount);
        
        return (barWidth * ((double) frameRemaining / frameDuration));
    }

    /**
     * @see PowerUp#getRGB()
     */
    @Override
    public int[] getRGB() {

        return RGB;
    }

    /**
     * @see PowerUp#getStateChangeFrameCount()
     */
    @Override
    public int getStateChangeFrameCount() {

        return stateChangeFrameCount;
    }

    /**
     * @see PowerUp#isInEffect()
     */
    @Override
    public boolean isInEffect() {

        return inEffect;
    }

    /**
     * @see PowerUp#setStartingFrameCount(int)
     */
    @Override
    public void setStartingFrameCount(int frameCount) {

        stateChangeFrameCount = frameCount;
    }

    /**
     * @see PowerUp#isTimeToDeactivate(int)
     */
    @Override
    public boolean isTimeToDeactivate(int frameCount) {

        int frameDuration = Info.FPS * EFFECTDURATION;
        return (!isVisible && inEffect &&
                frameCount - stateChangeFrameCount >= frameDuration);
    }

    /**
     * @see PowerUp#isTimeToDespawn(int)
     */
    @Override
    public boolean isTimeToDespawn(int frameCount) {

        int frameDuration = Info.FPS * SPAWNDURATION;
        return (isVisible && !inEffect &&
                frameCount - stateChangeFrameCount >= frameDuration);
    }

    /**
     * @see PowerUp#isTimeToSpawn(int)
     */
    @Override
    public boolean isTimeToSpawn(int frameCount) {
        
        int frameDelay = Info.FPS * delay;
        return (!isVisible && !inEffect &&
                frameCount - stateChangeFrameCount >= frameDelay);
    }

    /**
     * @see PowerUp#spawn(TileList, int)
     */
    @Override
    public void spawn(TileList unfilledTiles, int frameCount) {
        
        int tileIndex = rand.nextInt(unfilledTiles.size());
        Tile spawnTile = unfilledTiles.get(tileIndex);
        
        x = spawnTile.getX();
        y = spawnTile.getY();

        isVisible = true;
        stateChangeFrameCount = frameCount;
    }

    /**
     * @see PowerUp#despawn(int)
     */
    @Override
    public void despawn(int frameCount) {

        x = -1;
        y = -1;

        isVisible = false;
        stateChangeFrameCount = frameCount;
        delay = SPAWNDELAY[0] + rand.nextInt(SPAWNDELAY[1] - SPAWNDELAY[0]);
    }

    /**
     * @see PowerUp#activateOn(Entity, Tile, int)
     */
    @Override
    public void activateOn(Entity entity, Tile overlappedTile,
        int frameCount) {

        if (!(entity instanceof Player))
            throw new IllegalArgumentException
                ("Argument needs to be an instance of Player");
        
        Player player = (Player) entity;

        if (overlappedTile == null ||
            overlappedTile.getName() != Name.GRASS) {
            
            player.setSpeed(Entity.DEFAULTSPEED + 3);
            inEffect = true;
        }
        despawn(frameCount);
    }

    /**
     * @see PowerUp#deactivateOn(Entity)
     */
    @Override
    public void deactivateOn(Entity entity) {

        if (!(entity instanceof Player))
            throw new IllegalArgumentException
                ("Argument needs to be an instance of Player");

        Player player = (Player) entity;
        player.setSpeed(Entity.DEFAULTSPEED);

        inEffect = false;
    }

}