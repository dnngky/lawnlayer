package lawnlayer;

import processing.core.PImage;

public class Worm extends Enemy {

    public Worm(PImage sprite) {

        super(sprite);
        name = "Worm";
    }

    public Worm(PImage sprite, int x, int y) {

        super(sprite, x, y);
        name = "Worm";
    }

    @Override
    public void move() {
        /*
         * If the worm collides with concrete tiles,
         * bounces the worm off in a reflected angle.
         */
        switch (collidedAt) {
            case UP:
                bouncesOffTop();
                break;
            case DOWN:
                bouncesOffBottom();
                break;
            case LEFT:
                bouncesOffLeft();
                break;
            case RIGHT:
                bouncesOffRight();
                break;
            case NONE:
                break;
        }
        switch (movement) {
            case UPLEFT:
                y--;
                x--;
                break;
            case UPRIGHT:
                y--;
                x++;
                break;
            case DOWNLEFT:
                y++;
                x--;
                break;
            case DOWNRIGHT:
                y++;
                x++;
                break;
            case STATIONARY:
                break;
        }
        checkOffMapMovement();
    }

}