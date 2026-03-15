package Proje;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundHelper {
    // Çalan seslerin tutulacağı liste
    private static List<Clip> activeClips = new ArrayList<>();

    // Ses dosyasını çalmak için yardımcı metot
    public static void playSound(String soundFile) {
        try {
            // Ses dosyasını yükle
            File file = new File(soundFile);
            if (!file.exists()) {
                System.out.println("Ses dosyası bulunamadı: " + soundFile);
                return; // Dosya bulunmazsa işlem yapma
            }

            // Ses dosyasını aç
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Eğer GameofThrones.wav çalınıyorsa döngüye al
            if (soundFile.contains("GameofThrones.wav")) {
                clip.loop(Clip.LOOP_CONTINUOUSLY); // Sonsuz döngü
            }

            clip.start(); // Ses dosyasını çalmaya başla

            // Aktif ses klibini listeye ekle
            activeClips.add(clip);

        } catch (UnsupportedAudioFileException e) {
            System.out.println("Desteklenmeyen ses dosyası formatı: " + soundFile);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Dosya okuma hatası: " + soundFile);
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.out.println("Ses çalma hattı kullanılabilir değil: " + soundFile);
            e.printStackTrace();
        }
    }

    // Tüm çalan sesleri durdurmak için metot
    public static void stopAllSounds() {
        // Tüm aktif ses kliplerini durdur
        for (Clip clip : activeClips) {
            if (clip.isRunning()) {
                clip.stop();  // Ses çalıyorsa durdur
            }
            clip.close();  // Ses kaynağını kapat
        }
        activeClips.clear();  // Listeyi temizle
    }
}