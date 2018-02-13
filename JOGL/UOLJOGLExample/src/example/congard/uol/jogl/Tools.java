package example.congard.uol.jogl;

import com.jogamp.opengl.GL2;

public class Tools {
	
	public static class GL {
		public static void rotate(GL2 gl, float angleAroundX, float angleAroundY, float angleAroundZ) {
			gl.glRotatef(angleAroundX, 1, 0, 0);
			gl.glRotatef(angleAroundY, 0, 1, 0);
			gl.glRotatef(angleAroundZ, 0, 0, 1);
		}
		
		public static void move(GL2 gl, float x, float y, float z) {
			gl.glTranslatef(x, y, z);
		}
	}
}
