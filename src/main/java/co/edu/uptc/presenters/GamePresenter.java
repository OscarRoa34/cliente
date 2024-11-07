package co.edu.uptc.presenters;

import java.awt.Point;
import java.util.List;

import co.edu.uptc.interfaces.GameInterface;
import co.edu.uptc.pojos.UFO;

public class GamePresenter implements GameInterface.Presenter {
    @SuppressWarnings("unused")
    private GameInterface.View view;
    private GameInterface.Model model;

    public void setView(GameInterface.View view) {
        this.view = view;
    }

    public void setModel(GameInterface.Model model) {
        this.model = model;
    }

    @Override
    public void spawnUFO(int width, int height) {
        model.spawnUFO(width, height);
    }

    @Override
    public void moveUfos(int width, int height) {
        model.moveUfos(width, height);
    }

    @Override
    public void fadeRoutes() {
        model.fadeRoutes();
    }

    @Override
    public List<UFO> getUfos() {
        return model.getUfos();
    }

    @Override
    public boolean checkCollisions() {
        return model.checkCollisions();
    }

    @Override
    public UFO selectUFO(Point point, UFO currentSelected) {
        return model.selectUFO(point, currentSelected);
    }

    @Override
    public void addRouteToUFO(Point point, UFO ufo) {
        model.addRouteToUFO(point, ufo);
    }

    @Override
    public void addPointToRoute(Point point, UFO ufo) {
        model.addPointToRoute(point, ufo);
    }

    @Override
    public void adjustSpeed(UFO ufo, int delta) {
        model.adjustSpeed(ufo, delta);
    }

}
