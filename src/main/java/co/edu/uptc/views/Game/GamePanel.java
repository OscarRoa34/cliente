package co.edu.uptc.views.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.List;
import co.edu.uptc.pojos.UFO;
import co.edu.uptc.models.UFOModel;
import co.edu.uptc.utils.PropertiesService;
import co.edu.uptc.views.GlobalView;
import co.edu.uptc.views.Menu.GameMenu;

public class GamePanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    private UFOModel ufoModel;
    private Timer fadeTimer;
    private Image background, ufoImage, selectedUfoImage;
    private UFO selectedUfo;
    private GameMenu menu;
    private PropertiesService p = new PropertiesService();

    public GamePanel() {
        initializeTimers();
        initializeListeners();
        initializePanel();
        initializeUFOModel();
        initializeImages();
    }

    private void initializePanel() {
        this.setSize(1250, 700);
    }

    private void initializeUFOModel() {
        ufoModel = new UFOModel();
    }

    private void initializeImages() {
        background = new ImageIcon(p.getKeyValue("gamebackground")).getImage();
        ufoImage = new ImageIcon(p.getKeyValue("ufo1normal")).getImage();
        selectedUfoImage = new ImageIcon(p.getKeyValue("ufo1selected")).getImage();
    }

    private void initializeTimers() {
        Timer moveTimer = new Timer(5, e -> {
            ufoModel.moveUfos(getWidth(), getHeight());
            checkCollisions();
            repaint();
        });
        moveTimer.start();
        Timer spawnTimer = new Timer(2000, e -> ufoModel.spawnUFO(getWidth(), getHeight()));
        spawnTimer.start();
        fadeTimer = new Timer(50, e -> {
            ufoModel.fadeRoutes();
        });
        fadeTimer.start();
    }

    private void initializeListeners() {
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
    }

    private void createCenterCircle(Graphics g) {
        g.setColor(GlobalView.TRANSPARENT);
        g.drawOval(510, 257, 280, 230);
        Iterator<UFO> iterator = ufoModel.getUfos().iterator();
        while (iterator.hasNext()) {
            UFO ufo = iterator.next();
            int ufoX = ufo.getX();
            int ufoY = ufo.getY();
            int centerX = 650;
            int centerY = 370;
            int radius = 100;
            double distance = Math.sqrt(Math.pow(ufoX - centerX, 2) + Math.pow(ufoY - centerY, 2));
            if (distance < radius) {
                iterator.remove();
            }
        }
    }

    private void adjustSelectedUfoSpeed(int adjustment) {
        if (selectedUfo != null) {
            ufoModel.adjustSpeed(selectedUfo, adjustment);
        }
    }

    private void checkCollisions() {
        ufoModel.checkCollisions();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
        drawUFOs(g);
        drawSelectedUFOInfo(g);
        createCenterCircle(g);
    }

    private void drawBackground(Graphics g) {
        g.drawImage(background, 0, 0, 1300, 750, this);
    }

    private void drawUFOs(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        ufoModel.getUfos().forEach(ufo -> {
            int x = ufo.getX() - 30;
            int y = ufo.getY() - 30;
            boolean isSelected = selectedUfo == ufo;
            g.drawImage(isSelected ? selectedUfoImage : ufoImage, x, y, 70, 60, this);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            drawSmoothPath(g2d, ufo.getPathPoints());
        });
    }

    private void drawSelectedUFOInfo(Graphics g) {
        if (selectedUfo != null) {
            g.setColor(GlobalView.BUTTONS_FOREGROUND_COLOR);
            g.drawString("VELOCIDAD " + selectedUfo.getSpeed(), 10, 20);
        }
    }

    private void drawSmoothPath(Graphics2D g2d, List<Point> points) {
        g2d.setColor(GlobalView.BUTTONS_FOREGROUND_COLOR);
        float[] dashPattern = { 10f, 5f };
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 0, dashPattern, 0));
        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        selectedUfo = ufoModel.selectUFO(e.getPoint(), selectedUfo);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (selectedUfo != null) {
            ufoModel.addRouteToUFO(e.getPoint(), selectedUfo);
            fadeTimer.start();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (selectedUfo != null) {
            ufoModel.addPointToRoute(e.getPoint(), selectedUfo);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            adjustSelectedUfoSpeed(1);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            adjustSelectedUfoSpeed(-1);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}