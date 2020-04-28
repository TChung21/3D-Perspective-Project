import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Camera implements KeyListener {
    public double xPos, yPos, xDir, yDir, xPlane, yPlane;
    //xpos ypos are location of player on 2D map
    //xdir and ydir are the x and y components of vector that points in the direc. the player is facing
    //xplane and yplane are components of a vector //always perpendicular to direction vector
    //these two vectors commbine to define the camera's view
    public boolean left, right, forward, back;
    public final double MOVE_SPEED = .08; //dictate how slowly the camera moves and turns based when key is pressed
    public final double ROTATION_SPEED = .045;

    public Camera(double x, double y, double xd, double yd, double xp, double yp) {
        xPos = x;
        yPos = y;
        xDir = xd;
        yDir = yd;
        xPlane = xp;
        yPlane = yp;
    }

    public void keyPressed(KeyEvent key) { //keeps track of which keys are pressed
        if ((key.getKeyCode() == KeyEvent.VK_LEFT))
            left = true;
        if ((key.getKeyCode() == KeyEvent.VK_RIGHT))
            right = true;
        if ((key.getKeyCode() == KeyEvent.VK_UP))
            forward = true;
        if ((key.getKeyCode() == KeyEvent.VK_DOWN))
            back = true;
    }

    public void keyReleased(KeyEvent key) {
        if ((key.getKeyCode() == KeyEvent.VK_LEFT))
            left = false;
        if ((key.getKeyCode() == KeyEvent.VK_RIGHT))
            right = false;
        if ((key.getKeyCode() == KeyEvent.VK_UP))
            forward = false;
        if ((key.getKeyCode() == KeyEvent.VK_DOWN))
            back = false;
    }

    public void keyTyped(KeyEvent key) {

    }

    public void update(int[][] map) {
        if (forward) {
            if (map[(int) (xPos + xDir * MOVE_SPEED)][(int) yPos] == 0) { //will only move if there is no wall
                xPos += xDir * MOVE_SPEED;
            }
            if (map[(int) xPos][(int) (yPos + yDir * MOVE_SPEED)] == 0)
                yPos += yDir * MOVE_SPEED;
        }
        if (back) {
            if (map[(int) (xPos - xDir * MOVE_SPEED)][(int) yPos] == 0)
                xPos -= xDir * MOVE_SPEED;
            if (map[(int) xPos][(int) (yPos - yDir * MOVE_SPEED)] == 0)
                yPos -= yDir * MOVE_SPEED;
        }

        /*for rotation both the firection and plane vectors are multiplied by the rotation matrix
        [ cos(ROTATION_SPEED) -sin(ROTATION_SPEED) ]
        [ sin(ROTATION_SPEED)  cos(ROTATION_SPEED) ]
        */

        if (right) {
            double oldxDir = xDir;
            xDir = xDir * Math.cos(-ROTATION_SPEED) - yDir * Math.sin(-ROTATION_SPEED);
            yDir = oldxDir * Math.sin(-ROTATION_SPEED) + yDir * Math.cos(-ROTATION_SPEED);
            double oldxPlane = xPlane;
            xPlane = xPlane * Math.cos(-ROTATION_SPEED) - yPlane * Math.sin(-ROTATION_SPEED);
            yPlane = oldxPlane * Math.sin(-ROTATION_SPEED) + yPlane * Math.cos(-ROTATION_SPEED);
        }
        if (left) {
            double oldxDir = xDir;
            xDir = xDir * Math.cos(ROTATION_SPEED) - yDir * Math.sin(ROTATION_SPEED);
            yDir = oldxDir * Math.sin(ROTATION_SPEED) + yDir * Math.cos(ROTATION_SPEED);
            double oldxPlane = xPlane;
            xPlane = xPlane * Math.cos(ROTATION_SPEED) - yPlane * Math.sin(ROTATION_SPEED);
            yPlane = oldxPlane * Math.sin(ROTATION_SPEED) + yPlane * Math.cos(ROTATION_SPEED);
        }
    }

}
