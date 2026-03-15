package Proje;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class MainMenu extends JFrame {
    private JButton startButton;
    private JButton levelsButton;
    private JButton exitButton;
    private Image backgroundImage;
    private Image buttonBackground;
    private List<Image> adamFrames; // Adam animasyonu için kareler
    private int adamX = -100;
    private int adamY;
    private int currentFrame = 0;
    private Font customFont; // Özel font

    public MainMenu() {
        setTitle("Labirent Oyunu - Ana Menü");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Özel fontu yükle
        loadCustomFont();

        // Adam animasyonu için kareleri yükleme
        loadAdamFrames();

        // Buton arka planını yükle
        loadButtonBackground();

        // Background panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Arka plan resmini yükle
                if (backgroundImage == null) {
                    try {
                        backgroundImage = ImageIO.read(new File("Image/background/obrin-7.jpg"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // Arka planı çiz
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }

                // Başlık çizme
                g.setColor(Color.WHITE); // Başlık için beyaz renk
                g.setFont(customFont.deriveFont(36f)); // Özel fontu kullanarak başlığı yaz
                String title = "GOLGELER LABIRENTI";
                FontMetrics fm = g.getFontMetrics();
                int titleWidth = fm.stringWidth(title);
                g.drawString(title, (getWidth() - titleWidth) / 2, 170); // Ortada başlık

                // Adam animasyonu karelerini çiz
                if (adamFrames != null && !adamFrames.isEmpty()) {
                    g.drawImage(adamFrames.get(currentFrame), adamX, adamY, 100, 100, this);
                }
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        // Buttons
        startButton = createCustomButton("Oyuna Basla");
        levelsButton = createCustomButton("Bolumler");
        exitButton = createCustomButton("Cikis");

        startButton.addActionListener(e -> startNewGame());
        levelsButton.addActionListener(e -> showLevels());
        exitButton.addActionListener(e -> System.exit(0));

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(startButton);
        buttonPanel.add(levelsButton);
        buttonPanel.add(exitButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(50, 0, 0, 0);
        backgroundPanel.add(buttonPanel, gbc);

        add(backgroundPanel);
        setVisible(true);

        // Timer ile animasyonu güncelleme
        Timer timer = new Timer(80, e -> updateAnimation());
        timer.start();

        adamY = getHeight() - 150;
    }

    private void loadCustomFont() {
        try {
            // Fontu yükle
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonttype/Alan Den.ttf")).deriveFont(16f); // 16pt olarak ayarladık
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAdamFrames() {
        adamFrames = new ArrayList<>();
        String[] framePaths = {
            "Image/karekter/character_malePerson_walk0.png",
            "Image/karekter/character_malePerson_walk1.png",
            "Image/karekter/character_malePerson_walk2.png",
            "Image/karekter/character_malePerson_walk3.png",
            "Image/karekter/character_malePerson_walk4.png",
            "Image/karekter/character_malePerson_walk5.png",
            "Image/karekter/character_malePerson_walk6.png",
            "Image/karekter/character_malePerson_walk7.png"
        };

        try {
            for (String path : framePaths) {
                File file = new File(path);
                if (file.exists()) {
                    Image frame = ImageIO.read(file);
                    adamFrames.add(frame);
                } else {
                    System.out.println("Resim bulunamadı: " + path);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadButtonBackground() {
        try {
            buttonBackground = ImageIO.read(new File("Image/background/rock_no_background_cleaned.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateAnimation() {
        currentFrame++;
        if (currentFrame >= adamFrames.size()) {
            currentFrame = 0;
        }
        adamX += 5;
        if (adamX >= getWidth()) {
            adamX = -100;
        }
        repaint(); // Paneli yeniden çiz
    }

    private JButton createCustomButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Buton arka planını çiz
                if (buttonBackground != null) {
                    g.drawImage(buttonBackground, 0, 0, getWidth(), getHeight(), this);
                }

                // Yazıyı çiz
                g.setFont(customFont);
                g.setColor(Color.WHITE);
                FontMetrics fm = g.getFontMetrics();
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getHeight();
                g.drawString(text, (getWidth() - textWidth) / 2, (getHeight() + textHeight / 2) / 2 + 6);
            }
        };
        button.setPreferredSize(new Dimension(150, 75)); // Buton boyutunu küçült
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }

    private void startNewGame() {
        dispose();
        MazeGame game = new MazeGame();
        JFrame gameFrame = new JFrame("Labirent Oyunu");
        gameFrame.setSize(1000, 1000);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.add(game);
        gameFrame.setVisible(true);
        gameFrame.setResizable(false);
    }

    private void showLevels() {
        String[] levels = {"1. Seviye", "2. Seviye", "3. Seviye", "4. Seviye", "5. Seviye"};
        String level = (String) JOptionPane.showInputDialog(
                this,
                "Bir seviye seçin:",
                "Bölümler",
                JOptionPane.QUESTION_MESSAGE,
                null,
                levels,
                levels[0]
        );

        if (level != null) {
            int levelNumber = Integer.parseInt(level.split("\\.")[0]);
            startLevel(levelNumber);
        }
    }

    private void startLevel(int levelNumber) {
        JOptionPane.showMessageDialog(this, "Seviye " + levelNumber + " başlatılıyor...");
        
        dispose();
        MazeGame game = new MazeGame();
        game.setLevel(levelNumber);
        JFrame gameFrame = new JFrame("Labirent Oyunu");
        gameFrame.setSize(1000, 1000);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.add(game);
        gameFrame.setVisible(true);
        gameFrame.setResizable(false);
    }

}


