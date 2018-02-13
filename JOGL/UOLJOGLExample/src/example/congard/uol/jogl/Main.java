package example.congard.uol.jogl;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

public class Main {
	int width = 1024, height = 1024;
	
	public static void main(String[] args) {
		new Main().mkUI();
	}
	
	private void mkUI() {
		Renderer r = new Renderer();
		JFrame window = new JFrame();
		window.getContentPane().add(r);
		window.setSize(width, height);
		window.setTitle("UOLJOGLExample by Congard");
		window.setVisible(true);
		window.setFocusable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					r.angleAroundX += 4;
					break;
				case KeyEvent.VK_DOWN:
					r.angleAroundX -= 4;
					break;
				case KeyEvent.VK_RIGHT:
					r.angleAroundY += 4;
					break;
				case KeyEvent.VK_LEFT:
					r.angleAroundY -= 4;
					break;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				
			}
		});
	}
}
