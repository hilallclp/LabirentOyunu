package Proje;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;

public class Animation {
    private Image[] frames; // Animasyon kareleri
    private int currentFrameIndex; // Şu anki kare
    private int frameCount; // Toplam kare sayısı
    private int frameDelay; // Her karede kaç milisaniye beklenmeli
    private int delayCounter; // Gecikme sayacı

    public Animation(String[] framePaths, int frameDelay) {
        try {
            frameCount = framePaths.length;
            frames = new Image[frameCount];
            for (int i = 0; i < frameCount; i++) {
                frames[i] = ImageIO.read(new File(framePaths[i])); // Kareleri yükle
            }
            this.frameDelay = frameDelay;
            currentFrameIndex = 0;
            delayCounter = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        delayCounter++;
        if (delayCounter >= frameDelay) {
            currentFrameIndex = (currentFrameIndex + 1) % frameCount; // Sıradaki kareye geç
            delayCounter = 0;
        }
    }

    public void draw(Graphics g, int x, int y, int width, int height) {
        if (frames != null && frames.length > 0) {
            g.drawImage(frames[currentFrameIndex], x, y, width, height, null);
        }
    }
}
