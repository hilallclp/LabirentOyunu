package Proje;

import java.util.ArrayList;
import java.util.List;


public class EnemyFire extends Enemy {
	private int fireRate;  // Ateş etme sıklığı
    private int nextFireTime; // Bir sonraki ateş etme zamanı
    private Level level;

    public EnemyFire(Level level, int startX, int startY, double speed, int fireRate) {
        super(startX, startY, speed ,new String[] 
        		{ "Image/Enemy/character_robot_walk0.png", 
            "Image/Enemy/character_robot_walk1.png", 
            "Image/Enemy/character_robot_walk2.png", 
            "Image/Enemy/character_robot_walk3.png", 
            "Image/Enemy/character_robot_walk4.png", 
            "Image/Enemy/character_robot_walk5.png", 
            "Image/Enemy/character_robot_walk6.png", 
            "Image/Enemy/character_robot_walk7.png" }, 1);
        this.level = level;
        this.fireRate = fireRate;
        this.nextFireTime = fireRate; // İlk ateş etme zamanı
    }

    @Override
    public void move() {
        if (nextFireTime <= 0) {
            fireBullet();
            nextFireTime = fireRate;
        } else {
            nextFireTime--;
        }
    }

    private void fireBullet() {
        // Düşman sağa doğru mermi ateş ediyor
        Bullet bullet = new Bullet(level, x, y, 1);  // Sağ yöne doğru
        level.addBullet(bullet);
    }}