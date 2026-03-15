package Proje;

import java.awt.Graphics;

public abstract class Enemy {
    protected int x; // Düşmanın mevcut X, Y koordinatları
    protected int y;
    private int startX; // Başlangıç ve bitiş noktaları
    protected int startY;
    private int endX;
    protected int endY;
    private boolean movingRight; // Düşmanın sağa mı solda mı hareket ettiğini tutar
    private boolean movingDown; // Düşmanın aşağı mı yukarı mı hareket ettiğini tutar
    protected double speed; // Düşmanın hızını kontrol etmek için
    protected Animation animation; // Düşmanın animasyonu

    public Enemy(int startX, int startY, double speed, String[] animationFrames, int frameDelay) {
        this.x = startX; // Düşmanın başlangıç X koordinatı
        this.y = startY; // Düşmanın başlangıç Y koordinatı
        this.startX = startX; // Başlangıç X koordinatı
        this.startY = startY; // Başlangıç Y koordinatı
        this.speed = speed; // Düşmanın hızı
        this.movingRight = true; // Başlangıçta sağa harekete başla
        this.movingDown = true; // Başlangıçta aşağıya harekete başla
        
        // Düşman animasyonu için yeni bir Animation nesnesi oluştur
        this.animation = new Animation(animationFrames, frameDelay);
    }

    // Düşmanı çizme metodu
    public void draw(Graphics g) {
        animation.update(); // Animasyonu güncelle
        animation.draw(g, y * 50, x * 50, 50, 50); // Animasyonu çiz
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean checkCollision(Level level) {
        // Çarpışma kontrolü
        return level.getPlayerX() == this.x && level.getPlayerY() == this.y;
    }

    public abstract void move(); // Her düşman türü için hareket metodunu implement edin
}