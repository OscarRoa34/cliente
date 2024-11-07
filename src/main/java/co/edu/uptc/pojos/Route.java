package co.edu.uptc.pojos;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Route {
    private List<Point> points;
    private float opacity;

    public Route(List<Point> points, float opacity) {
        this.points = new ArrayList<>(points);
        this.opacity = opacity;
    }

    public void fade() {
        opacity -= 0.007f;
        if (opacity < 0)
            opacity = 0;
    }

    public boolean isVisible() {
        return opacity > 0;
    }

    public List<Point> getPoints() {
        return points;
    }

    public float getOpacity() {
        return opacity;
    }
}
