package co.edu.uptc.views.Game;

import javax.swing.*;

import co.edu.uptc.interfaces.GameInterface.Presenter;
import co.edu.uptc.interfaces.GameInterface.View;
import co.edu.uptc.utils.PropertiesService;
import co.edu.uptc.views.Menu.GameMenu;

public class GameGUI extends JFrame implements View {
    private GamePanel gamePanel;
    @SuppressWarnings("unused")
    private Presenter presenter;
    private PropertiesService p = new PropertiesService();

    public GameGUI(GameMenu airTrafficGameMenu) {
        setTitle("UFO GAME");
        setSize(1300, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        gamePanel = new GamePanel();
        add(gamePanel);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon((p.getKeyValue("tabicon"))).getImage());
        setVisible(true);
    }

    public GameGUI() {
        this(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameGUI(new GameMenu()));
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }
}