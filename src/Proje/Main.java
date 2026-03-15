package Proje;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainMenu();  // Ana menü ekranı başlatılır
            }
        });
	}
}
