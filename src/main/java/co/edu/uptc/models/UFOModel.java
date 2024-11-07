package co.edu.uptc.models;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import co.edu.uptc.interfaces.GameInterface;
import co.edu.uptc.interfaces.GameInterface.Presenter;
import co.edu.uptc.pojos.Route;
import co.edu.uptc.pojos.UFO;

public class UFOModel implements GameInterface.Model {

    private static UFOModel instance;
    @SuppressWarnings("unused")
    private Presenter presenter;
    private List<UFO> ufos;
    private List<Route> routes;
    private Random random;

    public UFOModel() {
        ufos = new ArrayList<>();
        routes = new ArrayList<>();
        random = new Random();
    }

    public UFO selectUFO(Point point, UFO currentSelected) {
        for (UFO ufo : ufos) {
            if (ufo.getBounds().contains(point)) {
                if (currentSelected != null && !currentSelected.getPathPoints().isEmpty()) {
                    routes.add(new Route(new ArrayList<>(currentSelected.getPathPoints()), 1.0f));
                }
                return ufo;
            }
        }
        return null;
    }

    public void addRouteToUFO(Point point, UFO ufo) {
        if (ufo.getPathPoints().isEmpty() || point.distance(ufo.getLastPathPoint()) > 15) {
            ufo.getPathPoints().add(point);
            routes.add(new Route(new ArrayList<>(ufo.getPathPoints()), 1.0f));
        }
    }

    public void addPointToRoute(Point point, UFO ufo) {
        if (ufo.getPathPoints().isEmpty() || point.distance(ufo.getLastPathPoint()) > 15) {
            ufo.getPathPoints().add(point);
        }
    }

    public void adjustSpeed(UFO ufo, int delta) {
        double newSpeed = ufo.getSpeed() + delta;
        ufo.setSpeed(Math.max(1, Math.min(12, newSpeed)));
    }

    public List<UFO> getUfos() {
        return ufos;
    }

    public void spawnUFO(int width, int height) {
        int posX, posY, angle;
        do {
            posX = generateRandomPosition(width - 40);
            posY = generateRandomPosition(height - 40);
            angle = random.nextInt(360);
        } while (isInSafeZone(posX, posY, 200) || isInRestrictedOval(posX, posY)
                || isAngleAtBorder(angle, width, height));

        ufos.add(new UFO(posX, posY, angle));
    }

    public void fadeRoutes() {
        routes.forEach(Route::fade);
        routes.removeIf(route -> !route.isVisible());
        if (routes.size() > 100) {
            routes = routes.subList(routes.size() - 100, routes.size());
        }
    }

    public void moveUfos(int width, int height) {
        ufos.removeIf(ufo -> {
            if (ufo.isDead())
                return true;
            moveUfo(ufo);
            return isOutOfBounds(ufo, width, height);
        });
    }

    public boolean checkCollisions() {
        boolean collisionDetected = checkUFOCollisions();
        collisionDetected |= checkBorderCollisions();
        return collisionDetected;
    }

    private int generateRandomPosition(int limit) {
        return random.nextInt(limit);
    }

    private boolean isInRestrictedOval(int x, int y) {
        int centerX = 510 + 280 / 2;
        int centerY = 257 + 230 / 2;
        double radiusX = 280 / 2.0;
        double radiusY = 230 / 2.0;
        return Math.pow(x - centerX, 2) / Math.pow(radiusX, 2) + Math.pow(y - centerY, 2) / Math.pow(radiusY, 2) <= 1;
    }

    private boolean isInSafeZone(int x, int y, int radius) {
        for (UFO ufo : ufos) {
            if (Math.hypot(ufo.getX() - x, ufo.getY() - y) < radius) {
                return true;
            }
        }
        return false;
    }

    private boolean isAngleAtBorder(int angle, int width, int height) {
        double radian = Math.toRadians(angle);
        double scaleFactor = 1.2;
        double x = Math.cos(radian) * (width / 2) * scaleFactor;
        double y = Math.sin(radian) * (height / 2) * scaleFactor;
        return Math.abs(x) >= width / 2 - 40 || Math.abs(y) >= height / 2 - 40;
    }

    private boolean checkUFOCollisions() {
        for (int i = 0; i < ufos.size(); i++) {
            for (int j = i + 1; j < ufos.size(); j++) {
                if (distanceBetween(ufos.get(i), ufos.get(j)) < 20) {
                    ufos.get(i).setDead(true);
                    ufos.get(j).setDead(true);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkBorderCollisions() {
        for (UFO ufo : new ArrayList<>(ufos)) {
            if (isOutOfBounds(ufo, 1300, 750)) {
                ufo.setDead(true);
                return true;
            }
        }
        return false;
    }

    private double distanceBetween(UFO ufo1, UFO ufo2) {
        return Point.distance(ufo1.getX(), ufo1.getY(), ufo2.getX(), ufo2.getY());
    }

    private void moveUfo(UFO ufo) {
        if (ufo.hasPath()) {
            Point nextPoint = ufo.getPathPoints().get(0);
            moveTowards(ufo, nextPoint);
            if (isAtPosition(ufo, nextPoint)) {
                ufo.removeReachedPoint();
            }
        } else {
            continueInDirection(ufo);
        }
    }

    private void moveTowards(UFO ufo, Point target) {
        double distance = Math.hypot(target.x - ufo.getX(), target.y - ufo.getY());
        if (distance < ufo.getSpeed()) {
            ufo.setX(target.x);
            ufo.setY(target.y);
        } else {
            ufo.setAngle(Math.atan2(target.y - ufo.getY(), target.x - ufo.getX()));
            ufo.setX((int) (ufo.getX() + Math.cos(ufo.getAngle()) * ufo.getSpeed()));
            ufo.setY((int) (ufo.getY() + Math.sin(ufo.getAngle()) * ufo.getSpeed()));
        }
    }

    private void continueInDirection(UFO ufo) {
        ufo.setX((int) (ufo.getX() + Math.cos(ufo.getAngle()) * ufo.getSpeed()));
        ufo.setY((int) (ufo.getY() + Math.sin(ufo.getAngle()) * ufo.getSpeed()));
    }

    private boolean isAtPosition(UFO ufo, Point p) {
        return ufo.getX() == p.x && ufo.getY() == p.y;
    }

    private boolean isOutOfBounds(UFO ufo, int width, int height) {
        return ufo.getX() < 0 || ufo.getX() > width || ufo.getY() < 0 || ufo.getY() > height;
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public static UFOModel getInstance() {
        if (instance == null) {
            instance = new UFOModel();
        }
        return instance;
    }
}
