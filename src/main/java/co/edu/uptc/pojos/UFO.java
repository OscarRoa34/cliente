package co.edu.uptc.pojos;

import lombok.Data;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

@Data
public class UFO {
    private int x, y;
    private double angle;
    private double speed = 1.5;
    private List<Point> pathPoints;
    private boolean isDead = false;
    private String pathImage;

    public UFO(int startX, int startY, int angle) {
        this.x = startX;
        this.y = startY;
        this.angle = angle;
        this.pathPoints = new ArrayList<>();
    }

    public boolean hasPath() {
        return !pathPoints.isEmpty();
    }

    public Point getLastPathPoint() {
        return pathPoints.get(pathPoints.size() - 1);
    }

    public void removeReachedPoint() {
        while (!pathPoints.isEmpty() && isAtPosition(pathPoints.get(0))) {
            pathPoints.remove(0);
        }
    }

    public boolean isAtPosition(Point point) {
        return this.getX() == point.x && this.getY() == point.y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x - 20, y - 20, 40, 40);
    }

    public void setImage(String selectedUFOImage) {
        this.pathImage = selectedUFOImage;
    }

    public String getImagePath() {
        return pathImage;
    }
}
