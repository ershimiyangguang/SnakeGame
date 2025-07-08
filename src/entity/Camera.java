package entity;

import java.awt.*;

public class Camera {
    Point p;

    public Camera() {
        p = new Point();
    }

    public void move(int x,int y) {
        p.setLocation(x-10,y-10);
    }

    public Point getP() {
        return p;
    }
}
