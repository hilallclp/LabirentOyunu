package Proje;

public class Bullet {
	private int x, y;  // Merminin koordinatları
    private Level level;  // Level'a referans
    private int direction; // Merminin hareket yönü: 1 sağa, -1 sola

    public Bullet(Level level, int startX, int startY, int direction) {
        this.x = startX;
        this.y = startY;
        this.level = level;
        this.direction = direction;  // 1: sağa, -1: sola
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Mermiyi hareket ettir
    public void move() {
        // Merminin sağa veya sola hareket etmesini sağlar
        y ++;


    }

    // Merminin duvara çarpıp çarpmadığını kontrol et

    
    public boolean hasHitWall(Level level) {
        // Burada maze'deki x, y koordinatında duvar olup olmadığını kontrol ediyoruz
        return level.getMaze()[x][y] == 1;  // 1: Duvar, 0: Geçilebilir alan
    }

   
    // Merminin karaktere çarpıp çarpmadığını kontrol et
    public boolean checkCollision(Level level) {
        // Çarpışma kontrolü
        return level.getPlayerX() == this.x && level.getPlayerY() == this.y;
    }


}