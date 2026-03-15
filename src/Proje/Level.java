package Proje;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

abstract class Level {
    protected int[][] maze; // Labirent yapısı
    protected int playerX, playerY; // Oyuncunun konumları
    protected int goalX, goalY; // Hedefin konumları
    protected List<Enemy> enemies; // Düşmanların listesi
    protected List<Bullet> bullets; // Mermiler
    protected Image wallImage; // Resim dosyaları
    protected Image goalImage;
    public Image floorImage;
    boolean moved = false;

    public Level() {
        enemies = new ArrayList<>(); // Düşman listesi başlat
        bullets = new ArrayList<>(); // Mermi listesi başlat
        loadImages(); // Resimleri yükle
        initLevel(); // Her seviyeye özgü başlatma
    }

    // Resimleri yükleme
    private void loadImages() {
       
    }
    public abstract int getTimeLimit(); // Her seviyeye özgü zaman limitini döndüren metot

    public void initLevel1() {
        // Genel seviye başlatma işlemleri
    }
    // Soyut metot
    public abstract void initLevel(); // Her level için başlangıç işlemleri

    // Soyut metot
    public abstract void setWalls(); // Her level için duvarları belirle

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy); // Düşman ekleme metodu
    }

    public void addBullet(Bullet b) {
        bullets.add(b); // Mermi ekleme metodu
    }

    public List<Enemy> getEnemies() {
        return enemies; // Düşmanların listesi
    }

    public List<Bullet> getBullets() {
        return bullets; // Mermilerin listesi
    }

    public int[][] getMaze() {
        return maze; // Labirent verisini döndür
    }

    public int getPlayerX() {
        return playerX; // Oyuncunun X koordinatı
    }

    public int getPlayerY() {
        return playerY; // Oyuncunun Y koordinatı
    }

    public void setGoal(int x, int y) {
        goalX = x; // Hedefin X koordinatı
        goalY = y; // Hedefin Y koordinatı
        maze[x][y] = 2; // Labirentte hedef konumunu ayarla
    }

    public boolean isGoalReached() {
        return playerX == goalX && playerY == goalY; // Hedefe ulaşıldı mı kontrol et
    }

    public boolean isValidMove(int x, int y) {
        return x >= 0 && x < maze.length && y >= 0 && y < maze[0].length && maze[x][y] != 1; // Geçerli hareket kontrolü
    }

    public void movePlayer(char key) {
        int newX = playerX;
        int newY = playerY;
        
        char normalizedKey = Character.toLowerCase(key);

        switch (normalizedKey) {
            case 'w': // Yukarı
                newX--;
                break;
            case 's': // Aşağı
                newX++;
                break;
            case 'a': // Sola
                newY--;
                break;
            case 'd': // Sağa
                newY++;
                break;
        }

        if (isValidMove(newX, newY)) { // Geçerli bir pozisyona mı hareket etti?
            playerX = newX;
            playerY = newY;
        }
        if (moved) {
            SoundHelper.playSound("Sounds\\back_002.wav"); // Ses dosyasını burada belirtmelisiniz
        }
    }
    

    public void moveEnemies() {
        for (Enemy enemy : enemies) {
            enemy.move(); // Her düşmanın hareket etme metodunu çağır
        }
    }

    // Maze'i çizme
    public void drawMaze(Graphics g) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == 1) {
                    g.drawImage(wallImage, j * 50, i * 50, 50, 50, null); // Duvarı resimle çiz
                } else if (maze[i][j] == 2) {
                    g.drawImage(goalImage, j * 50, i * 50, 50, 50, null); // Hedefi resimle çiz
                } else {
                    g.drawImage(floorImage, j * 50, i * 50, 50, 50, null); // Geçilebilir alanı resimle çiz
                }
            }
        }
    }
    
}
