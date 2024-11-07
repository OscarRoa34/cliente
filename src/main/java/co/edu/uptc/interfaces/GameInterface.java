package co.edu.uptc.interfaces;

import java.util.List;

import co.edu.uptc.pojos.UFO;

import java.awt.Point;

public interface GameInterface {

    public interface Model {
        public void setPresenter(Presenter presenter);

        public void spawnUFO(int width, int height);

        public void moveUfos(int width, int height);

        public void fadeRoutes();

        public List<UFO> getUfos();

        public boolean checkCollisions();

        public UFO selectUFO(Point point, UFO currentSelected);

        public void addRouteToUFO(Point point, UFO ufo);

        public void addPointToRoute(Point point, UFO ufo);

        public void adjustSpeed(UFO ufo, int delta);

    }

    public interface View {
        public void setPresenter(Presenter presenter);
    }

    public interface Presenter {
        public void setView(View view);

        public void setModel(Model model);

        public void spawnUFO(int width, int height);

        public void moveUfos(int width, int height);

        public void fadeRoutes();

        public List<UFO> getUfos();

        public boolean checkCollisions();

        public UFO selectUFO(Point point, UFO currentSelected);

        public void addRouteToUFO(Point point, UFO ufo);

        public void addPointToRoute(Point point, UFO ufo);

        public void adjustSpeed(UFO ufo, int delta);

    }
}
