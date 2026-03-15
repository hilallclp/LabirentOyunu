package Proje;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;

public class MazeGame extends JPanel {
    private Level currentLevel;
    private static int currentLevelIndex = 1; // İlk önce Level1
    private Timer timer;
    private Timer levelTimer; // Seviye zamanlayıcısı
    private Animation playerAnimation;
    private int levelTimeLeft; // Kalan süre
    private JPanel endPanel;
    private boolean isGameOver = false;
    
    public MazeGame() {
    	 SoundHelper.playSound("Sounds/GameofThrones.wav");
        setLevel(currentLevelIndex);
        playerAnimation = new Animation(new String[] 
                {"Image/karekter/character_malePerson_walk0.png"
                ,"Image/karekter/character_malePerson_walk1.png",
                 "Image/karekter/character_malePerson_walk3.png",
                 "Image/karekter/character_malePerson_walk4.png",
                 "Image/karekter/character_malePerson_walk5.png",
                 "Image/karekter/character_malePerson_walk6.png",
                 "Image/karekter/character_malePerson_walk7.png"}, 1);

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            	if (isGameOver) {
                    return;  // Hareket etmeyi engelle
                }
            	
            	if (currentLevel.isGoalReached()) {
            		levelTimer.stop();  // Zamanlayıcıyı durdur
                    SoundHelper.playSound("Sounds/confirmation_004.wav"); // Hedefe ulaşma sesini çal

                    // Oyunu bitir ve game over panelini göster
                    showGameEndPanel(true);
                    return;
                }
                currentLevel.movePlayer(e.getKeyChar());
               
            }
        });

        // Oyun içindeki zamanlayıcı (düşman hareketi, mermi hareketi vs.)
        timer = new Timer(100, e -> {
            currentLevel.moveEnemies(); // Düşmanların hareket etmesini sağla
            checkCollisions(); // Çarpışmaları kontrol et
            removeBullet(); // Mermilerin durumunu kontrol et
            repaint(); // Ekranı güncelle
        });
        timer.start(); // Zamanlayıcıyı başlat
    }
    
    public void setLevel(int levelIndex) {
    	isGameOver = false;

        // Önceki seviyedeki zamanlayıcıyı durdur
        if (levelTimer != null && levelTimer.isRunning()) {
            levelTimer.stop();
        }

        if (levelIndex == 1) {
            currentLevel = new Level1();
        } else if (levelIndex == 2) {
            currentLevel = new Level2();
            
        } else if (levelIndex == 3) {
            currentLevel = new Level3();
            
        } else if (levelIndex == 4) {
            currentLevel = new Level4();
           
        } else if (levelIndex == 5) {
            currentLevel = new Level5();
           
        } else {
            JOptionPane.showMessageDialog(this, "Tüm düzeyler tamamlandı!");
            System.exit(0); // Oyun kapatılır
        }
        
        if (currentLevel instanceof Level) {
            // currentLevel bir Level sınıfı (veya alt sınıfı) ise bu blok çalışacak
            currentLevel.initLevel();
            currentLevel.setWalls(); // Her seviyede duvarları ayarla

            // Her seviye için zaman sınırını ayarla
            levelTimeLeft = currentLevel.getTimeLimit(); // Seviye bitiş süresi

            // Yeni seviye için zamanlayıcıyı başlat
            startLevelTimer();
        } else {
            // Eğer currentLevel doğru türde değilse (bu durum gerçekleşmemeli)
            System.err.println("Geçersiz seviye türü!");
            
        }
    }
   
    private void styleButton(JButton button) {
        // Butonun boyutunu, fontunu ayarlıyoruz
        button.setPreferredSize(new Dimension(180, 45));  // Daha küçük boyut
        button.setMaximumSize(new Dimension(200, 50));    // Maksimum genişliği ayarlıyoruz
        button.setFont(new Font("Segoe UI", Font.PLAIN, 18));  // Daha yumuşak font
        button.setForeground(new Color(255, 255, 255));  // Beyaz metin rengi

        // Butonun metnine gölge ekleyelim
        button.setText("<html><span style='text-shadow: 2px 2px 5px rgba(0, 0, 0, 0.5);'>" + button.getText() + "</span></html>");

        // Gradyan arka plan ekleyelim (mavi ve mor tonları)
        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradyan renkler: mavi ve mor geçişi
                Color color1 = new Color(0, 191, 255);  // Açık mavi (DeepSkyBlue)
                Color color2 = new Color(138, 43, 226); // Mor (BlueViolet)
                GradientPaint gradient = new GradientPaint(0, 0, color1, 0, button.getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), 20, 20);  // Yuvarlak köşeler

                super.paint(g, c);  // Butonun metnini çizmek için
            }
        });

        // Fare ile üzerine gelindiğinde buton büyüsün
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Timer timer = new Timer(10, new ActionListener() {
                    float scale = 1.0f;
                    final float maxScale = 1.05f;  // Maksimum büyüme oranı

                    public void actionPerformed(ActionEvent e) {
                        if (scale < maxScale) {
                            scale += 0.02f; // Buton büyür (daha küçük adımlarla)
                            int newWidth = (int) (180 * scale);
                            int newHeight = (int) (45 * scale);
                            button.setPreferredSize(new Dimension(newWidth, newHeight));  // Yeni boyut

                            button.setFont(button.getFont().deriveFont(18f * scale));  // Font büyür
                            button.revalidate();
                        } else {
                            ((Timer) e.getSource()).stop();
                        }
                    }
                });
                timer.start();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                Timer timer = new Timer(10, new ActionListener() {
                    float scale = 1.05f;  // Başlangıçta buton büyüktür
                    final float minScale = 1.0f;  // Minimum boyut

                    public void actionPerformed(ActionEvent e) {
                        if (scale > minScale) {
                            scale -= 0.02f; // Buton küçülür (daha küçük adımlarla)
                            int newWidth = (int) (180 * scale);
                            int newHeight = (int) (45 * scale);
                            button.setPreferredSize(new Dimension(newWidth, newHeight));  // Yeni boyut

                            button.setFont(button.getFont().deriveFont(18f * scale));  // Font küçülür
                            button.revalidate();
                        } else {
                            ((Timer) e.getSource()).stop();
                        }
                    }
                });
                timer.start();
            }
        });
    }
    private void showGameEndPanel(boolean isWin) {
        if (endPanel != null) {
            this.remove(endPanel); // Mevcut endPanel'i kaldırıyoruz
            endPanel = null;
        }

        endPanel = new JPanel();
        endPanel.setBounds(250, 20, 400, 200); // Panel boyutunu genişletiyoruz
        endPanel.setLayout(new BoxLayout(endPanel, BoxLayout.Y_AXIS));
        endPanel.setBackground(new Color(200, 225, 255)); // Arka plan rengi

        JLabel congratulationsLabel = new JLabel();
        if (isWin) {
            congratulationsLabel.setText("Tebrikler! Bölümü Geçtiniz!");
        } else {
            congratulationsLabel.setText("Oyun Bitti! Kaybettiniz!");
        }
        congratulationsLabel.setFont(new Font("Roboto", Font.PLAIN, 30));
        congratulationsLabel.setForeground(new Color(50, 50, 50));
        congratulationsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        endPanel.add(congratulationsLabel);

        JButton mainMenuButton = new JButton("Ana Menü");
        JButton nextLevelButton = new JButton("Sonraki Bölüm");
        JButton restartButton = new JButton("Tekrar Oyna");

        // Buton stillerini ayarlıyoruz
        styleButton(mainMenuButton);
        styleButton(nextLevelButton);
        styleButton(restartButton);

        // Butonların boyutlarını sabitliyoruz
        Dimension buttonSize = new Dimension(200, 50);
        mainMenuButton.setPreferredSize(buttonSize);
        nextLevelButton.setPreferredSize(buttonSize);
        restartButton.setPreferredSize(buttonSize);

        // Butonları hizalayalım
        mainMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextLevelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (isWin) {
            endPanel.add(Box.createVerticalStrut(5));  // Butonlar arasına daha az mesafe ekledik
            endPanel.add(nextLevelButton);
        }

        endPanel.add(Box.createVerticalStrut(5));  // Butonlar arasına daha az mesafe ekledik
        endPanel.add(mainMenuButton);

        endPanel.add(Box.createVerticalStrut(5));  // Butonlar arasına daha az mesafe ekledik
        endPanel.add(restartButton);

        // Kaybetme durumunda sadece Tekrar Oyna ve Ana Menü butonları olacak
        if (!isWin) {
            endPanel.add(Box.createVerticalStrut(5));  // Butonlar arasına daha az mesafe ekledik
            endPanel.add(restartButton);  // Kaybedildiğinde "Tekrar Oyna" butonunu ekle
        }
        
        // Oyun bittiğinde hareketi engelle
        isGameOver = true;
        if (levelTimer != null && levelTimer.isRunning()) {
            levelTimer.stop(); // Zamanlayıcıyı durduruyoruz
        }
        // Ana Menü Butonuna Tıklanması Durumu
        mainMenuButton.addActionListener(e -> {
        	 SoundHelper.stopAllSounds();
        	  if (timer != null && timer.isRunning()) {
        	        timer.stop();  // Oyun zamanlayıcısını durduruyoruz
        	    }
        	    
        	    if (levelTimer != null && levelTimer.isRunning()) {
        	        levelTimer.stop();  // Seviye zamanlayıcısını durduruyoruz
        	    }

        	    // Mevcut pencereyi (MazeGame'in bulunduğu pencere) al
        	    JFrame parentFrame = (JFrame) SwingUtilities.windowForComponent(this);

        	    // Eski pencereyi kapatıyoruz
        	    if (parentFrame != null) {
        	        parentFrame.dispose(); // Oyun penceresini kapatıyoruz
        	    }

        	    // Yeni bir MainMenu oluşturuluyor
        	    MainMenu menu = new MainMenu();

        	    // Yeni ana menü penceresini oluşturuyoruz
        	    JFrame menuFrame = new JFrame("Ana Menü");
        	    menuFrame.setSize(1000, 1000);  // Yeni pencere boyutu
        	    menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	    menuFrame.setLocationRelativeTo(null);  // Pencereyi ekranda ortala
        	    menuFrame.add(menu);  // Ana menüyü pencereye ekliyoruz
        	    menuFrame.setVisible(true); 
        });

        // Sonraki Bölüm Butonuna Tıklanması Durumu
        nextLevelButton.addActionListener(e -> {
            // Sonraki seviyeye geç
            currentLevelIndex++;
            
            // Eğer geçerli seviyede sınırları aştıysanız, tekrar başlatabiliriz.
            if (currentLevelIndex > 5) {
                JOptionPane.showMessageDialog(this, "Tüm düzeyler tamamlandı!");
                System.exit(0); // Oyun kapatılır
            }

            // Yeni seviyeyi ayarla
            setLevel(currentLevelIndex); 

            // Oyuncunun hareketini yeniden etkinleştir
            isGameOver = false;  // Oyun bitti bayrağını sıfırla
            
            // End panelini kaldır
            if (endPanel != null) {
                this.remove(endPanel);  // End panelini kaldır
                endPanel = null;
            }
            
            // Seviye zamanlayıcısını başlat
            startLevelTimer();  // Zamanlayıcıyı başlatmak için metod çağrısı

            // Oyunu yeniden çizin
            revalidate();      // Layoutu yeniden düzenle
            repaint();         // Ekranı yeniden çiz
        });

        // Tekrar Oyna Butonuna Tıklanması Durumu
        restartButton.addActionListener(e -> {
            // Mevcut seviyeyi tekrar başlat
            setLevel(currentLevelIndex); // Aynı seviyeyi baştan başlatıyoruz
            
            // End panelini kaldırıyoruz
            if (endPanel != null) {
                this.remove(endPanel);  // End panelini kaldır
                endPanel = null;
            }

            // Zamanlayıcıyı yeniden başlat
            startLevelTimer();  // Seviye zamanlayıcısını yeniden başlatmak için metod çağrısı

            // Oyuncunun hareketini yeniden etkinleştir
            isGameOver = false;  // Oyun bitti bayrağını sıfırla

            // Ekranı yeniden düzenle ve çiz
            revalidate();
            repaint();
        });
        // Oyun bitiş panelini ekleyelim
        this.add(endPanel, BorderLayout.CENTER);
        revalidate(); // Layoutu yeniden düzenliyoruz
        repaint();    // Ekranı yeniden çiziyoruz
    }
    // Seviye zamanlayıcısını başlat
    private void startLevelTimer() {
    	if (levelTimer != null && levelTimer.isRunning()) {
            levelTimer.stop();
        }
        levelTimer = new Timer(1000, e -> {
            levelTimeLeft--; // Her saniye zamanı azalt
            if (levelTimeLeft == 0) {
            
                SoundHelper.playSound("Sounds/timesup.wav");//süre bitiminde çalıcak olan ses
             
                showGameEndPanel(false);
            return;
            }
        });
        levelTimer.start(); // Zamanlayıcıyı başlat
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMaze(g);
        drawEnemies(g); // Düşmanları çizme metodu
        drawTimeLeft(g); // Kalan zamanı ekrana yazdır
    }

    private void drawTimeLeft(Graphics g) {
        // Kalan süreyi ekranın üst kısmında göster
    	Font font = new Font("Impact", Font.BOLD, 30); // Daha dikkat çekici bir font ve boyut
        g.setFont(font);
        
        // Yazı rengi: Turuncu
        g.setColor(new Color(255, 165, 0)); 
        
        // Yazıya gölge ekleyelim (şimdi biraz offset ekleyerek)
        g.drawString("Kalan Süre: " + levelTimeLeft + " saniye", 12, 32); // Gölge için biraz kaydırma

        // Şimdi asıl yazıyı beyaz renkte çizebiliriz
        g.setColor(Color.WHITE);
        g.drawString("Kalan Süre: " + levelTimeLeft + " saniye", 10, 30);
    }

    private void drawMaze(Graphics g) {
        currentLevel.drawMaze(g); // Labirenti resimle çiz
        // Oyuncuyu çiz
        int playerX = currentLevel.getPlayerX();
        int playerY = currentLevel.getPlayerY();
        playerAnimation.update(); // Animasyonu güncelle
        playerAnimation.draw(g, playerY * 50, playerX * 50, 50, 50); // Animasyonu çiz
    }

    private void drawEnemies(Graphics g) {
        for (Enemy enemy : currentLevel.getEnemies()) {
            enemy.draw(g); // Düşmanları ve animasyonları çiz
        }
        for (Bullet b : currentLevel.getBullets()) {
            b.move();
            g.setColor(Color.RED); // Düşmanın rengi
            g.fillOval(b.getY() * 50, b.getX() * 50+25, 8, 8); // Düşmanı çiz
        }
    }

    private void checkCollisions() {
        for (Enemy enemy : currentLevel.getEnemies()) {
            if (enemy.checkCollision(currentLevel)) {
                SoundHelper.playSound("Sounds/error_007.wav");
                showGameEndPanel(false);
                return;
            }
        }
        for (Bullet b : currentLevel.getBullets()) {
            if (b.checkCollision(currentLevel)) {
                SoundHelper.playSound("Sounds/error_007.wav");
                showGameEndPanel(false);
                return;
            }
        }
    }

    public void removeBullet() {
        List<Bullet> bullets = currentLevel.getBullets();
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            if (b.hasHitWall(currentLevel)) {
                bullets.remove(i);
                i--; // Silinen öğe nedeniyle bir index kaybı oluyor, bu yüzden i'yi azaltıyoruz
            }
        }
    }
}