package Proje;

public class EnemyY extends Enemy{
    private int startY, endY, startX; // Başlangıç ve bitiş noktaları
    private boolean movingDown; // Düşmanın aşağı mı yukarı mı hareket ettiğini tutar

    public EnemyY(int startX, int startY, int endY, double speed) {
        super(startX, startY, speed, 
        		new String[] 
        		{ "Image/Enemy/character_robot_walk0.png", 
                "Image/Enemy/character_robot_walk1.png", 
                "Image/Enemy/character_robot_walk2.png", 
                "Image/Enemy/character_robot_walk3.png", 
                "Image/Enemy/character_robot_walk4.png", 
                "Image/Enemy/character_robot_walk5.png", 
                "Image/Enemy/character_robot_walk6.png", 
                "Image/Enemy/character_robot_walk7.png" }, 1);
        this.startX = startX;
        this.startY = startY; // Başlangıç Y koordinatı
        this.endY = endY; // Bitiş Y koordinatı
        this.movingDown = true; // Başlangıçta aşağıya harekete başla
    }

    @Override
    public void move() {
        if (movingDown) {
            x += speed; // Aşağı
            if (x >= endY) { // Aşağı sınırına ulaşıldıysa yön değiştir
                movingDown = false; // Yukarı dön
            }
        } else {
            x -= speed; // Yukarı
            if (x <= startX) { // Yukarı sınırına ulaşıldıysa yön değiştir
                movingDown = true; // Aşağı dön
            }
        }
        // X koordinatı sabit kaldığı için bir şey yapılmaz
    }
}