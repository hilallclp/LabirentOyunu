package Proje;

public class EnemyX extends Enemy {

    private int startX, endX; // Başlangıç ve bitiş noktaları
    private boolean movingRight; // Düşmanın sağa mı solda mı hareket ettiğini tutar

    public EnemyX(int startX, int startY, int endX, double speed) {
        super(startX, startY, speed,
              new String[] { "Image/Enemy/character_robot_walk0.png", 
                             "Image/Enemy/character_robot_walk1.png", 
                             "Image/Enemy/character_robot_walk2.png", 
                             "Image/Enemy/character_robot_walk3.png", 
                             "Image/Enemy/character_robot_walk4.png", 
                             "Image/Enemy/character_robot_walk5.png", 
                             "Image/Enemy/character_robot_walk6.png", 
                             "Image/Enemy/character_robot_walk7.png" }, 1);
        this.startX = startX;
        this.endX = endX;
        this.movingRight = true; // Başlangıçta sağa harekete başla
    }

    @Override
    public void move() {
        // Düşman sağa hareket ediyorsa
        if (movingRight) {
            y += speed; // Sağa doğru hareket
            if (y >= endX) { // Sağ sınırına ulaşıldıysa yön değiştir
                movingRight = false; // Sola dön
            }
        } else {
            y -= speed; // Sola doğru hareket
            if (y <= startY) { // Sol sınırına ulaşıldıysa yön değiştir
                movingRight = true; // Sağa dön
            }
        }
    }
}
