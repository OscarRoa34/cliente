package co.edu.uptc.views.Menu;

import javax.imageio.ImageIO;
import javax.swing.*;

import co.edu.uptc.models.UFOModel;
import co.edu.uptc.pojos.UFO;
import co.edu.uptc.utils.PropertiesService;
import co.edu.uptc.views.GlobalView;
import co.edu.uptc.views.Game.GameGUI;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class GameMenu extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private String[] ufoImages;
    private int currentImageIndex = 0;
    private String selectedUFOImage;
    public boolean drawTrajectory;
    private PropertiesService p = new PropertiesService();

    public GameMenu() {
        ufoImages = new String[] { p.getKeyValue("ufo1normal"), p.getKeyValue("ufo2normal"),
                p.getKeyValue("ufo3normal") };
        setTitle("UFO Control Traffic Game");
        initializeFrame();
        initializePanels();
        addPanelsToMainPanel();
        addMainPanelToFrame();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("UFO Control Traffic Game");
        setSize(400, 430);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initializePanels() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
    }

    private void addPanelsToMainPanel() {
        mainPanel.add(createCoverPanel(), "Cover");
        mainPanel.add(createMenuPanel(), "Menu");
        mainPanel.add(createOptionsPanel(), "Options");
        mainPanel.add(createImageViewerPanel(), "ImageViewer");
        mainPanel.add(createHowToPlayPanel(), "HowToPlay");
    }

    private void addMainPanelToFrame() {
        add(mainPanel);
    }

    private JPanel createCoverPanel() {
        JPanel coverPanel = new JPanel(new BorderLayout());
        coverPanel.setBackground(GlobalView.BUTTONS_BG_COLOR);
        coverPanel.setFocusable(true);
        addBackgroundImage(coverPanel);
        addButtonToStartGame(coverPanel);
        addKeyListenerToPanel(coverPanel);
        return coverPanel;
    }

    private void addBackgroundImage(JPanel panel) {
        try {
            Image backgroundImage = ImageIO.read(new File(p.getKeyValue("coverbackground")));
            panel.add(new JLabel(new ImageIcon(backgroundImage)), BorderLayout.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addButtonToStartGame(JPanel panel) {
        JButton startButton = createButton("empezar a jugar", GlobalView.BUTTONS_BG_COLOR, 15);
        startButton.setBorderPainted(false);
        startButton.setForeground(GlobalView.BUTTONS_FOREGROUND_COLOR);
        startButton.setFocusable(false);
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "Menu");
            }
        });
        panel.add(startButton, BorderLayout.SOUTH);
    }

    private void addKeyListenerToPanel(JPanel panel) {
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                cardLayout.show(mainPanel, "Menu");
            }
        });
    }

    private JPanel createMenuPanel() {
        JPanel backgroundPanel = createBackgroundPanel();
        addPlayButton(backgroundPanel);
        addOptionsButton(backgroundPanel);
        addHowToPlayButton(backgroundPanel);
        createExitButton(backgroundPanel);
        return backgroundPanel;
    }

    private JPanel createBackgroundPanel() {
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setBackground(GlobalView.MENU_BG_COLOR);
        backgroundPanel.setLayout(new GridBagLayout());
        return backgroundPanel;
    }

    private void createExitButton(JPanel backgroundPanel) {
        JButton exitButton = createButton("salir", GlobalView.BUTTONS_BG_COLOR, 50);
        exitButton.addActionListener(e -> System.exit(0));
        GridBagConstraints gbc = createGridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        backgroundPanel.add(exitButton, gbc);
    }

    private void addPlayButton(JPanel backgroundPanel) {
        JButton playButton = createButton("jugar", GlobalView.BUTTONS_BG_COLOR, 50);
        playButton.addActionListener(e -> startGame());
        GridBagConstraints gbc = createGridBagConstraints();
        backgroundPanel.add(playButton, gbc);
    }

    private void addOptionsButton(JPanel backgroundPanel) {
        JButton optionsButton = createButton("opciones", GlobalView.BUTTONS_BG_COLOR, 50);
        optionsButton.addActionListener(e -> cardLayout.show(mainPanel, "Options"));
        GridBagConstraints gbc = createGridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        backgroundPanel.add(optionsButton, gbc);
    }

    private void addHowToPlayButton(JPanel backgroundPanel) {
        JButton howToPlayButton = createButton("como jugar", GlobalView.BUTTONS_BG_COLOR, 50);
        howToPlayButton.addActionListener(e -> cardLayout.show(mainPanel, "HowToPlay"));
        GridBagConstraints gbc = createGridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        backgroundPanel.add(howToPlayButton, gbc);
    }

    private GridBagConstraints createGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.weightx = 1.0;
        return gbc;
    }

    private JPanel createOptionsPanel() {
        JPanel optionsPanel = new JPanel();
        optionsPanel.setBackground(GlobalView.MENU_BG_COLOR);
        optionsPanel.setLayout(null);
        addSelectImageButton(optionsPanel);
        addUfoCountControls(optionsPanel);
        addAppearanceTimeControls(optionsPanel);
        addTrajectoryCheckBox(optionsPanel);
        addSaveOptionsButton(optionsPanel);
        return optionsPanel;
    }

    private void addSelectImageButton(JPanel panel) {
        JButton selectUFOImageButton = createButton("elegir    imagen    del    ufo", GlobalView.OK_BUTTON,
                25);
        selectUFOImageButton.setBounds(0, 20, 400, 40);
        selectUFOImageButton.addActionListener(e -> cardLayout.show(mainPanel, "ImageViewer"));
        panel.add(selectUFOImageButton);
    }

    private void addUfoCountControls(JPanel panel) {
        JLabel ufoCountLabel = createLabel("Numero de ovnis:");
        ufoCountLabel.setForeground(GlobalView.BUTTONS_FOREGROUND_COLOR);
        ufoCountLabel.setBounds(150, 80, 200, 30);
        panel.add(ufoCountLabel);
        JSpinner ufoCountSpinner = createSpinner(1, 1, 100, 1);
        ufoCountSpinner.setBounds(150, 120, 100, 30);
        panel.add(ufoCountSpinner);
    }

    private void addAppearanceTimeControls(JPanel panel) {
        JLabel appearanceTimeLabel = createLabel("Tiempo de aparición de ovnis (ms):");
        appearanceTimeLabel.setForeground(GlobalView.BUTTONS_FOREGROUND_COLOR);
        appearanceTimeLabel.setBounds(100, 180, 200, 30);
        panel.add(appearanceTimeLabel);
        JSpinner appearanceTimeSpinner = createSpinner(1000, 500, 10000, 100);
        appearanceTimeSpinner.setBounds(150, 220, 100, 30);
        panel.add(appearanceTimeSpinner);
    }

    private void addTrajectoryCheckBox(JPanel panel) {
        JCheckBox trajectoryCheckBox = createCheckBox("Mostrar trayectoria");
        trajectoryCheckBox.setForeground(GlobalView.BUTTONS_FOREGROUND_COLOR);
        trajectoryCheckBox.setBackground(GlobalView.MENU_BG_COLOR);
        trajectoryCheckBox.setSelected(true);
        trajectoryCheckBox.setBorderPainted(false);
        trajectoryCheckBox.setFocusPainted(false);
        trajectoryCheckBox.setBounds(130, 280, 200, 30);
        panel.add(trajectoryCheckBox);
    }

    private void addSaveOptionsButton(JPanel panel) {
        JButton backButton = createButton("guardar", GlobalView.BACK_BUTTON, 25);
        backButton.setBounds(100, 340, 200, 40);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));
        panel.add(backButton);
    }

    public Font loadFont(int size) {
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(p.getKeyValue("menufont"))).deriveFont(
                    Font.PLAIN,
                    size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            return customFont;
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JButton createButton(String text, Color color, int size) {
        JButton button = new JButton(text);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setForeground(GlobalView.BUTTONS_FOREGROUND_COLOR);
        Font font = loadFont(size);
        button.setFont(font);
        return button;
    }

    private JLabel createLabel(String text) {
        return new JLabel(text);
    }

    private JSpinner createSpinner(int initialValue, int minValue, int maxValue, int stepSize) {
        return new JSpinner(new SpinnerNumberModel(initialValue, minValue, maxValue, stepSize));
    }

    private JCheckBox createCheckBox(String text) {
        return new JCheckBox(text);
    }

    private JPanel createImageViewerPanel() {
        JPanel viewerPanel = new JPanel(new BorderLayout());
        viewerPanel.setBackground(GlobalView.MENU_BG_COLOR);
        JLabel imageLabel = createImageLabel();
        JPanel buttonPanel = createNavigationButtonsPanel(imageLabel);
        JPanel actionPanel = createActionButtonsPanel();
        viewerPanel.add(imageLabel, BorderLayout.CENTER);
        viewerPanel.add(buttonPanel, BorderLayout.SOUTH);
        viewerPanel.add(actionPanel, BorderLayout.NORTH);
        return viewerPanel;
    }

    private JLabel createImageLabel() {
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        updateImage(imageLabel);
        return imageLabel;
    }

    private JPanel createNavigationButtonsPanel(JLabel imageLabel) {
        JButton leftButton = createNavigationButton("<", imageLabel, -1);
        JButton rightButton = createNavigationButton(">", imageLabel, 1);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(leftButton);
        buttonPanel.add(rightButton);
        return buttonPanel;
    }

    private JPanel createActionButtonsPanel() {
        JButton saveButton = createSaveButton();
        JButton backButton = createBackButton("Options");
        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.setOpaque(false);
        actionPanel.add(backButton);
        actionPanel.add(saveButton);
        return actionPanel;
    }

    private JButton createSaveButton() {
        JButton saveButton = createButton("save", GlobalView.BUTTONS_BG_COLOR, 15);
        saveButton.setBorder(BorderFactory.createLineBorder(GlobalView.OK_BUTTON, 2));
        saveButton.setPreferredSize(new Dimension(80, 30));
        saveButton.addActionListener(e -> {
            selectedUFOImage = ufoImages[currentImageIndex];
            UFOModel ufoModel = UFOModel.getInstance();
            for (UFO ufo : ufoModel.getUfos()) {
                ufo.setImage(selectedUFOImage);
            }

            cardLayout.show(mainPanel, "Options");
        });
        return saveButton;
    }

    private void updateImage(JLabel imageLabel) {
        ImageIcon icon = new ImageIcon(ufoImages[currentImageIndex]);
        Image scaledImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledImage));
    }

    private JButton createNavigationButton(String text, JLabel imageLabel, int direction) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(60, 40));
        button.setForeground(GlobalView.BUTTONS_FOREGROUND_COLOR);
        button.setBackground(GlobalView.BUTTONS_BG_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(GlobalView.OK_BUTTON, 2));
        button.addActionListener(e -> {
            currentImageIndex = (currentImageIndex + direction + ufoImages.length) % ufoImages.length;
            updateImage(imageLabel);
        });
        return button;
    }

    private JPanel createHowToPlayPanel() {
        JPanel howToPlayPanel = new JPanel(new BorderLayout());
        howToPlayPanel.setBackground(GlobalView.MENU_BG_COLOR);
        JLabel titleLabel = createTitleLabel();
        JTextArea instructionsText = createInstructionsText();
        JButton backButton = createBackButton("Menu");
        howToPlayPanel.add(titleLabel, BorderLayout.NORTH);
        howToPlayPanel.add(new JScrollPane(instructionsText), BorderLayout.CENTER);
        howToPlayPanel.add(backButton, BorderLayout.SOUTH);
        return howToPlayPanel;
    }

    private JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("¿Como jugar?\n\n", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setForeground(GlobalView.BUTTONS_FOREGROUND_COLOR);
        return titleLabel;
    }

    private JTextArea createInstructionsText() {
        JTextArea instructionsText = new JTextArea(8, 25);
        instructionsText.setEditable(false);
        instructionsText.setText("\n   Instrucciones:\n\n"
                + "\n   1. Selecciona un ovni y dirigelo a donde quieras\n       dibujando la ruta con tu mouse.\n"
                + "\n   2. Mientras tengas un ovni seleccionado podras\n       aumentar o disminuir su velocidad\n       con las flechas ↑,↓ del teclado.\n"
                + "\n   3. Evita que los ovnis se choquen con \n       otros o colisionen con los bordes.\n"
                + "\n   3. Tu meta es llevar a todos los \n       ovnis a la luna del centro.\n"
                + "\n   4. ¡Diviértete jugando y desafiando tus habilidades!\n");
        return instructionsText;
    }

    private JButton createBackButton(String name) {
        JButton backButton = createButton("back", GlobalView.BACK_BUTTON, 15);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, name));
        return backButton;
    }

    private void startGame() {
        this.dispose();
        SwingUtilities.invokeLater(() -> new GameGUI(this));
    }

}