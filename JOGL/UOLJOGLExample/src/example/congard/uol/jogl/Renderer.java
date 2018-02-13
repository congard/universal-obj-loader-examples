package example.congard.uol.jogl;

import java.io.File;
import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import free.lib.congard.objloader.LoaderConstants;
import free.lib.congard.objloader.Model;

@SuppressWarnings("serial")
public class Renderer extends GLJPanel implements GLEventListener {

	int width, height, tex;
	public float angleAroundX, angleAroundY, angleAroundZ;
	private FloatBuffer vertices, texCoords, normals;
	private FPSAnimator animator;
	private Model model;
	
	// OBJ model files
	private final static String PATH_TO_OBJ = "/home/congard/Загрузки/OBJ/optimized2.obj", 
								PATH_TO_TEXTURE_ATLAS = "/home/congard/Загрузки/OBJ/textures/Trident_UV_Dekol_Color.jpg";
	//

	public Renderer() {
		setFocusable(true);
		addGLEventListener(this);
		animator = new FPSAnimator(this, 900, false);
		animator.start();
		width = height = 1024;
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glLoadIdentity();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		// [RU] Отдаляем и поворачиваем
		// [EN] Move and rotate
		Tools.GL.move(gl, 0, 0, -16);
		Tools.GL.rotate(gl, angleAroundX, angleAroundY, angleAroundZ);
		// 
		
		// [RU] Начинаем отрисовку
		// [EN] Start rendering
		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY); // for vertices
        gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY); // for textures
        gl.glEnableClientState(GL2.GL_NORMAL_ARRAY); // for normals
        gl.glVertexPointer(3, GL2.GL_FLOAT, 0, vertices); // vertices xyz
        gl.glTexCoordPointer(2, GL2.GL_FLOAT, 0, texCoords); // textures xy
        gl.glNormalPointer(GL2.GL_FLOAT, 0, normals); // normals xyz
        gl.glBindTexture(GL2.GL_TEXTURE_2D, tex); // bind texture
        for (Model.VerticesDescriptor vd : model.vd) gl.glDrawArrays(vd.POLYTYPE, vd.START, vd.END); // drawing
        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        //
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glEnable(GL2.GL_NORMALIZE);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU glu = GLU.createGLU(gl);
		
		loadModels(gl);
		setLight(gl);

		glu.gluPerspective(100, (double) getWidth() / (double) getHeight(), 0.1, 100);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		
		model.convertToFloatArrays(true, true);
		model.cleanup();
		long st = System.currentTimeMillis();
		vertices = Buffers.newDirectFloatBuffer(model.vertices.length);
		vertices.put(model.vertices).position(0);

		texCoords = Buffers.newDirectFloatBuffer(model.texCoords.length);
		texCoords.put(model.texCoords).position(0);
         
		normals = Buffers.newDirectFloatBuffer(model.normals.length);
		normals.put(model.normals).position(0);
		System.out.println("Buffers created in " + (System.currentTimeMillis()-st)+"ms");
		
		// loading texture
		tex = loadTexture(gl, new File(PATH_TO_TEXTURE_ATLAS));
	}

	private void setLight(GL2 gl) {
		
		gl.glEnable(GL2.GL_LIGHTING);
		
		float SHINE_ALL_DIRECTIONS = 1;
		float[] lightPos = { 0, 0, 30, SHINE_ALL_DIRECTIONS };
		float[] lightColorAmbient = { 0.02f, 0.02f, 0.02f, 1f };
		float[] lightColorSpecular = { 0.9f, 0.9f, 0.9f, 1f };

		// Set light parameters.
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightColorAmbient, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightColorSpecular, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightColorSpecular, 0);
		gl.glEnable(GL2.GL_LIGHT1);
		
	}

	private void loadModels(GL2 gl) {
		model = new Model(new File(PATH_TO_OBJ)); // path to obj model
		model.enable(LoaderConstants.TEX_VERTEX_2D); // xy texture coords
		model.setDefaultPolyTypes(GL2.GL_TRIANGLES, GL2.GL_QUADS, GL2.GL_POLYGON); // for easy drawing
		model.load(); // loading model
	}
	
	private int loadTexture(GL2 gl, File path) {
		Texture t;
		try {
			t = TextureIO.newTexture(path, true);
			return t.getTextureObject(gl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU glu = new GLU();

		glu.gluPerspective(100, (double) getWidth() / (double) getHeight(), 0.1, 100);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}
}