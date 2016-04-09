package javac;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.border.Border;

import objects.Vertex;
import parsers.ParseObj;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.awt.GLJPanel;

public class MainGameloop extends GLJPanel implements GLEventListener,
		KeyListener,MouseWheelListener {

	private static float transX;
	private static float transY;
	private static float transZ;
	private float rotate_left_handb;
	private float rotate_right_handb;
	private float rotate_left_handt;
	private float rotate_right_handt;
	private float rotate_left_legb,rotate_left_legt,rotate_right_legt,rotate_right_legb;
	private static float transX1;
	private static float transY1;
	private static float transZ1;
	private float rotate_left_handb1;
	private float rotate_right_handb1;
	private float rotate_left_handt1;
	private float rotate_right_handt1;
	private float rotate_left_legb1,rotate_left_legt1,rotate_right_legt1,rotate_right_legb1,pscale;
	private static float lineofsight;
	private static float lineofsight1;
	private static JProgressBar pb1;
	private static JProgressBar pb2;
	
	private int left ,right,go,back,back1,action_hit;
	private int left1 ,right1,go1,back12,back11,action_hit1,close,healthA,healthB;
	
	
	private ArrayList<Vertex> vertex, normal, texture, color, body_vertex,
			body_normal, body_color, hand_topl_vertex, hand_topl_normal,
			hand_topl_color, hand_botl_vertex, hand_botl_normal,
			hand_botl_color,body1_vertex,body1_normal,body1_color;
	private ArrayList<Vertex> hand_topr_vertex, hand_topr_normal,
			hand_topr_color, hand_botr_vertex, hand_botr_normal,
			hand_botr_color, head_vertex, head_normal, head_color;
	private ArrayList<Vertex> footl_vertex, footl_normal, footl_color,
			footr_vertex, footr_normal, footr_color;
	private ArrayList<Vertex> leg_topl_vertex, leg_topr_vertex,
			leg_topl_normal, leg_topr_normal, leg_topl_color, leg_topr_color;
	private ArrayList<Vertex> leg_botl_vertex, leg_botr_vertex,
			leg_botl_normal, leg_botr_normal, leg_botl_color, leg_botr_color;

	public static void main(String[] args) {
		JFrame window = new JFrame("Boxing Game");
		GLCapabilities caps = new GLCapabilities(null);
		MainGameloop panel = new MainGameloop(caps);
		window.setContentPane(panel);
		window.pack();
		window.setLocation(0, 0);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		transX = Utils.MIN_X+0.1f;
		transY = Utils.MIN_Y+0.1f;
		transZ = 0.0f;
		lineofsight = 45;
		lineofsight1 = -135;
		
		Container content1 = window.getContentPane();
		Container content2 = window.getContentPane();
		pb1 = new JProgressBar();
		pb2 = new JProgressBar();
		pb1.setValue(100);
		pb2.setValue(100);
		pb1.setStringPainted(true);
		pb2.setStringPainted(true);
		Border b1 = BorderFactory.createTitledBorder("Player 1 Health");
		Border b2 = BorderFactory.createTitledBorder("Player 2 Health");
		pb1.setBorder(b1);
		pb2.setBorder(b2);
		content1.add(pb1,BorderLayout.NORTH);
		content2.add(pb2,BorderLayout.SOUTH);
		
		
		transX1 = Utils.MAX_X-0.1f;
		transY1 = Utils.MAX_Y-0.1f;
		transZ1 = 0;
		panel.requestFocusInWindow();
	}

	private float rotateX, rotateY, rotateZ; // rotation amounts about axes,
												// controlled by keyboard
	private float var1=0;

	public MainGameloop(GLCapabilities capabilities) {
		super(capabilities);
		setPreferredSize(new Dimension(1340, 750));
		addGLEventListener(this);
		addKeyListener(this);
		addMouseWheelListener(this);
		
		

		healthA = 100;
		healthB = 100;
		close  = 0;
		pscale = .5f;
		rotate_left_handb = rotate_left_handb1 = -144;
		rotate_left_handt = rotate_left_handt1 = -20;
		rotate_right_handb = rotate_right_handb1 = -144;
		rotate_right_handt = rotate_right_handt1 = -20;
		left = left1 =1;
		right = right1 = 0;
		
		rotate_left_legb = rotate_left_legb1 = 0;
		rotate_left_legt = rotate_left_legt1 = 0;
		rotate_right_legb = rotate_right_legb1 = 60;
		rotate_right_legt = rotate_right_legt1 = -60;
		
		
		// Parse ring

		vertex = new ArrayList<Vertex>();
		normal = new ArrayList<Vertex>();
		texture = new ArrayList<Vertex>();
		color = new ArrayList<Vertex>();

		ParseObj obj = new ParseObj(
				"C:/Users/RDC/workspace/BoxingGame/src/objects/boxing_ring.obj",
				1, 0.0f, 0.0f, 0.0f);
		obj.fillHmap("C:/Users/RDC/workspace/BoxingGame/src/objects/boxing_ring.mtl");
		obj.ParseIt();

		vertex = obj.getVertices();
		normal = obj.getNormals();
		texture = obj.getTextures();
		color = obj.getColor();

		// Parse body of player

		body_vertex = new ArrayList<Vertex>();
		body_normal = new ArrayList<Vertex>();
		body_color = new ArrayList<Vertex>();

		ParseObj obj1 = new ParseObj(
				"C:/Users/RDC/workspace/BoxingGame/src/objects/body.obj", 1,
				0.0f, 0.0f, 0.4016f);
		obj1.fillHmap("C:/Users/RDC/workspace/BoxingGame/src/objects/body.mtl");
		obj1.ParseIt();

		body_vertex = obj1.getVertices();
		body_normal = obj1.getNormals();
		body_color = obj1.getColor();
		
		// Parse body of player1

		body1_vertex = new ArrayList<Vertex>();
		body1_normal = new ArrayList<Vertex>();
		body1_color = new ArrayList<Vertex>();

		ParseObj obj01 = new ParseObj(
				"C:/Users/RDC/workspace/BoxingGame/src/objects/body1.obj", 1,
				0.0f, 0.0f, 0.4016f);
		obj01.fillHmap("C:/Users/RDC/workspace/BoxingGame/src/objects/body1.mtl");
		obj01.ParseIt();

		body1_vertex = obj01.getVertices();
		body1_normal = obj01.getNormals();
		body1_color = obj01.getColor();

		// Parse left hand top of player

		hand_topl_vertex = new ArrayList<Vertex>();
		hand_topl_normal = new ArrayList<Vertex>();
		hand_topl_color = new ArrayList<Vertex>();

		ParseObj obj2 = new ParseObj(
				"C:/Users/RDC/workspace/BoxingGame/src/objects/hand_top_left.obj",
				1, Utils.LEFT_ARM_TOP_X, Utils.LEFT_ARM_TOP_Y,
				Utils.LEFT_ARM_TOP_Z);
		obj2.fillHmap("C:/Users/RDC/workspace/BoxingGame/src/objects/hand_top_left.mtl");
		obj2.ParseIt();

		hand_topl_vertex = obj2.getVertices();
		hand_topl_normal = obj2.getNormals();
		hand_topl_color = obj2.getColor();

		// parse right hand top
		hand_topr_vertex = new ArrayList<Vertex>();
		hand_topr_normal = new ArrayList<Vertex>();
		hand_topr_color = new ArrayList<Vertex>();
		

		ParseObj obj3 = new ParseObj(
				"C:/Users/RDC/workspace/BoxingGame/src/objects/hand_top_left.obj",
				1, Utils.RIGHT_ARM_TOP_X, Utils.RIGHT_ARM_TOP_Y,
				Utils.RIGHT_ARM_TOP_Z);
		obj3.fillHmap("C:/Users/RDC/workspace/BoxingGame/src/objects/hand_top_left.mtl");
		obj3.ParseIt();

		hand_topr_vertex = obj3.getVertices();
		hand_topr_normal = obj3.getNormals();
		hand_topr_color = obj3.getColor();

		// Parse left hand bot of player

		hand_botl_vertex = new ArrayList<Vertex>();
		hand_botl_normal = new ArrayList<Vertex>();
		hand_botl_color = new ArrayList<Vertex>();

		ParseObj obj20 = new ParseObj(
				"C:/Users/RDC/workspace/BoxingGame/src/objects/hand_left_bot.obj",
				1, Utils.LEFT_ARM_BOT_X, Utils.LEFT_ARM_BOT_Y,
				Utils.LEFT_ARM_BOT_Z);
		obj20.fillHmap("C:/Users/RDC/workspace/BoxingGame/src/objects/hand_left_bot.mtl");
		obj20.ParseIt();

		hand_botl_vertex = obj20.getVertices();
		hand_botl_normal = obj20.getNormals();
		hand_botl_color = obj20.getColor();

		// parse right hand bot
		hand_botr_vertex = new ArrayList<Vertex>();
		hand_botr_normal = new ArrayList<Vertex>();
		hand_botr_color = new ArrayList<Vertex>();

		ParseObj obj30 = new ParseObj(
				"C:/Users/RDC/workspace/BoxingGame/src/objects/hand_right_bot.obj",
				1, Utils.RIGHT_ARM_BOT_X, Utils.RIGHT_ARM_BOT_Y,
				Utils.RIGHT_ARM_BOT_Z);
		obj30.fillHmap("C:/Users/RDC/workspace/BoxingGame/src/objects/hand_right_bot.mtl");
		obj30.ParseIt();

		hand_botr_vertex = obj30.getVertices();
		hand_botr_normal = obj30.getNormals();
		hand_botr_color = obj30.getColor();

		// Parse head

		head_vertex = new ArrayList<Vertex>();
		head_normal = new ArrayList<Vertex>();
		head_color = new ArrayList<Vertex>();

		ParseObj obj4 = new ParseObj(
				"C:/Users/RDC/workspace/BoxingGame/src/objects/head.obj", 1,
				Utils.HEAD_X, Utils.HEAD_Y, Utils.HEAD_Z);
		obj4.fillHmap("C:/Users/RDC/workspace/BoxingGame/src/objects/head.mtl");
		obj4.ParseIt();

		head_vertex = obj4.getVertices();
		head_normal = obj4.getNormals();
		head_color = obj4.getColor();

		// Parse left foot

		footl_vertex = new ArrayList<Vertex>();
		footl_normal = new ArrayList<Vertex>();
		footl_color = new ArrayList<Vertex>();

		ParseObj obj5 = new ParseObj(
				"C:/Users/RDC/workspace/BoxingGame/src/objects/foot_left.obj",
				1, Utils.LEFT_FOOT_X, Utils.LEFT_FOOT_Y, Utils.LEFT_FOOT_Z);
		obj5.fillHmap("C:/Users/RDC/workspace/BoxingGame/src/objects/foot_left.mtl");
		obj5.ParseIt();

		footl_vertex = obj5.getVertices();
		footl_normal = obj5.getNormals();
		footl_color = obj5.getColor();

		// Parse right foot

		footr_vertex = new ArrayList<Vertex>();
		footr_normal = new ArrayList<Vertex>();
		footr_color = new ArrayList<Vertex>();

		ParseObj obj6 = new ParseObj(
				"C:/Users/RDC/workspace/BoxingGame/src/objects/foot_right.obj",
				1, Utils.RIGHT_FOOT_X, Utils.RIGHT_FOOT_Y, Utils.RIGHT_FOOT_Z);
		obj6.fillHmap("C:/Users/RDC/workspace/BoxingGame/src/objects/foot_right.mtl");
		obj6.ParseIt();

		footr_vertex = obj6.getVertices();
		footr_normal = obj6.getNormals();
		footr_color = obj6.getColor();

		// parse left bottom leg

		leg_botl_vertex = new ArrayList<Vertex>();
		leg_botl_normal = new ArrayList<Vertex>();
		leg_botl_color = new ArrayList<Vertex>();

		ParseObj obj7 = new ParseObj(
				"C:/Users/RDC/workspace/BoxingGame/src/objects/leg_left_bot.obj",
				1, Utils.LEFT_LEG_BOT_X, Utils.LEFT_LEG_BOT_Y,
				Utils.LEFT_LEG_BOT_Z);
		obj7.fillHmap("C:/Users/RDC/workspace/BoxingGame/src/objects/leg_left_bot.mtl");
		obj7.ParseIt();

		leg_botl_vertex = obj7.getVertices();
		leg_botl_normal = obj7.getNormals();
		leg_botl_color = obj7.getColor();

		// parse right bottom leg

		leg_botr_vertex = new ArrayList<Vertex>();
		leg_botr_normal = new ArrayList<Vertex>();
		leg_botr_color = new ArrayList<Vertex>();

		ParseObj obj8 = new ParseObj(
				"C:/Users/RDC/workspace/BoxingGame/src/objects/leg_right_bot.obj",
				1, Utils.RIGHT_LEG_BOT_X, Utils.RIGHT_LEG_BOT_Y,
				Utils.RIGHT_LEG_BOT_Z);
		obj8.fillHmap("C:/Users/RDC/workspace/BoxingGame/src/objects/leg_right_bot.mtl");
		obj8.ParseIt();

		leg_botr_vertex = obj8.getVertices();
		leg_botr_normal = obj8.getNormals();
		leg_botr_color = obj8.getColor();

		// parse left top leg

		leg_topl_vertex = new ArrayList<Vertex>();
		leg_topl_normal = new ArrayList<Vertex>();
		leg_topl_color = new ArrayList<Vertex>();

		ParseObj obj9 = new ParseObj(
				"C:/Users/RDC/workspace/BoxingGame/src/objects/leg_left_top.obj",
				1, Utils.LEFT_LEG_TOP_X, Utils.LEFT_LEG_TOP_Y,
				Utils.LEFT_LEG_TOP_Z);
		obj9.fillHmap("C:/Users/RDC/workspace/BoxingGame/src/objects/leg_left_top.mtl");
		obj9.ParseIt();

		leg_topl_vertex = obj9.getVertices();
		leg_topl_normal = obj9.getNormals();
		leg_topl_color = obj9.getColor();

		// parse right top leg

		leg_topr_vertex = new ArrayList<Vertex>();
		leg_topr_normal = new ArrayList<Vertex>();
		leg_topr_color = new ArrayList<Vertex>();

		ParseObj obj10 = new ParseObj(
				"C:/Users/RDC/workspace/BoxingGame/src/objects/leg_right_top.obj",
				1, Utils.RIGHT_LEG_TOP_X, Utils.RIGHT_LEG_TOP_Y,
				Utils.RIGHT_LEG_TOP_Z);
		obj10.fillHmap("C:/Users/RDC/workspace/BoxingGame/src/objects/leg_right_top.mtl");
		obj10.ParseIt();

		leg_topr_vertex = obj10.getVertices();
		leg_topr_normal = obj10.getNormals();
		leg_topr_color = obj10.getColor();

		action_hit = 0;
		rotateX = 0;
		rotateY = 0;
		rotateZ = 0;
	}

	// ------------ methods of the KeyListener interface ------------

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		System.out.println(key);
		System.out.println(lineofsight);
		if(healthA>0&&healthB>0)
		{
		if(key == KeyEvent.VK_UP&&transX<Utils.MAX_X&&transY<Utils.MAX_Y)
		{
			transX += .02;
			transY += .02;
		}else if(key == KeyEvent.VK_RIGHT&&transX<Utils.MAX_X&&transY>Utils.MIN_Y)
		{	
			transX+=.02f;
			transY-=.02;
		}else if(key == KeyEvent.VK_LEFT&&transX>Utils.MIN_X&&transY<Utils.MAX_Y)
		{
			transX-=.02;
			transY+=.02;
		}else if(key == KeyEvent.VK_DOWN&&transX>Utils.MIN_X&&transY>Utils.MIN_Y)
		{
			transX -= .02;
			transY -= .02;
		}
		else if(key == KeyEvent.VK_W&&transX1>Utils.MIN_X&&transY1>Utils.MIN_Y)
		{
			transX1-=.02;
			transY1-=.02;
		}else if(key == KeyEvent.VK_D&&transX1>Utils.MIN_X&&transY1<Utils.MAX_Y)
		{
			transX1-=.02;
			transY1+=.02;
		}else if(key == KeyEvent.VK_A&&transX1<Utils.MAX_X&&transY1>Utils.MIN_Y)
		{
			transX1+=.02;
			transY1-=.02;
		}
		else if(key == KeyEvent.VK_S&&transX1<Utils.MAX_X&&transY1<Utils.MAX_Y)
		{
			transX1+=.02;
			transY1+=.02;
		}
		else if (key == KeyEvent.VK_ALT)
		{	
			
			if(action_hit == 0)
			{
				if(close == 1)
				{
					healthB -= 10;
				}
				action_hit = 1;
				go = 1;
				back = 0;
				back1 = 0;
			}
		}else if(key == KeyEvent.VK_Q)
		{
			if(action_hit1 == 0)
			{
				if(close == 1)
				{
					healthA -= 10;
				}
				action_hit1 = 1;
				go1 = 1;
				back11 = 0;
				back12 = 0;
			}
		}
		else if (key == KeyEvent.VK_HOME)
			rotateX = rotateY = rotateZ = 0;
		pb1.setValue(healthA);
		pb2.setValue(healthB);
		repaint();
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void DrawXAxis(GL2 gl) {
		for (float i = -3.0f; i <= 3.0f; i = i + .0001f) {
			gl.glBegin(GL2.GL_POINTS);
			gl.glColor3f(1.0f, 0.0f, 0.0f);
			gl.glVertex3f(i, 0.0f, 0.0f);
			gl.glEnd();
		}
	}

	public void DrawYAxis(GL2 gl) {
		for (float i = -1.8f; i <= 1.8f; i += .001f) {
			gl.glBegin(GL2.GL_POINTS);
			gl.glColor3f(0.0f, 1.0f, 0.0f);
			gl.glVertex3f(0.0f, i, 0.0f);
			gl.glEnd();
		}
	}

	public void DrawZAxis(GL2 gl) {
		for (float i = -5.0f; i <= 5.0f; i += .001f) {
			gl.glBegin(GL2.GL_POINTS);
			gl.glColor3f(0.0f, 0.0f, 1.0f);
			gl.glVertex3f(0.0f, 0.0f, i);
			gl.glEnd();
		}
	}

	public void DrawRing(GL2 gl) {
		for (int i = 0, k = 0; i < vertex.size(); i += 3, k++) {
			gl.glBegin(GL2.GL_TRIANGLES);

			// System.out.println();
			gl.glColor3f(color.get(k).getX(), color.get(k).getY(), color.get(k)
					.getZ());
			gl.glNormal3f(normal.get(i).getX(), normal.get(i).getY(), normal
					.get(i).getZ());
			gl.glVertex3f(vertex.get(i).getX(), vertex.get(i).getY(), vertex
					.get(i).getZ());
			gl.glNormal3f(normal.get(i + 1).getX(), normal.get(i + 1).getY(),
					normal.get(i + 1).getZ());
			gl.glVertex3f(vertex.get(i + 1).getX(), vertex.get(i + 1).getY(),
					vertex.get(i + 1).getZ());
			gl.glNormal3f(normal.get(i + 2).getX(), normal.get(i + 2).getY(),
					normal.get(i + 2).getZ());
			gl.glVertex3f(vertex.get(i + 2).getX(), vertex.get(i + 2).getY(),
					vertex.get(i + 2).getZ());
			gl.glEnd();
		}

	}

	public void DrawBody(GL2 gl) {
		for (int i = 0, k = 0; i < body_vertex.size(); i += 3, k++) {
			gl.glBegin(GL2.GL_TRIANGLES);
			// System.out.println();
			gl.glColor3f(body_color.get(k).getX(), body_color.get(k).getY(),
					body_color.get(k).getZ());
			gl.glNormal3f(body_normal.get(i).getX(), body_normal.get(i).getY(),
					body_normal.get(i).getZ());
			gl.glVertex3f(body_vertex.get(i).getX(), body_vertex.get(i).getY(),
					body_vertex.get(i).getZ());
			gl.glNormal3f(body_normal.get(i + 1).getX(), body_normal.get(i + 1)
					.getY(), body_normal.get(i + 1).getZ());
			gl.glVertex3f(body_vertex.get(i + 1).getX(), body_vertex.get(i + 1)
					.getY(), body_vertex.get(i + 1).getZ());
			gl.glNormal3f(body_normal.get(i + 2).getX(), body_normal.get(i + 2)
					.getY(), body_normal.get(i + 2).getZ());
			gl.glVertex3f(body_vertex.get(i + 2).getX(), body_vertex.get(i + 2)
					.getY(), body_vertex.get(i + 2).getZ());
			gl.glEnd();
		}
	}
	
	public void DrawBody1(GL2 gl) {
		for (int i = 0, k = 0; i < body1_vertex.size(); i += 3, k++) {
			gl.glBegin(GL2.GL_TRIANGLES);
			// System.out.println();
			gl.glColor3f(body1_color.get(k).getX(), body1_color.get(k).getY(),
					body1_color.get(k).getZ());
			gl.glNormal3f(body1_normal.get(i).getX(), body1_normal.get(i).getY(),
					body1_normal.get(i).getZ());
			gl.glVertex3f(body1_vertex.get(i).getX(), body1_vertex.get(i).getY(),
					body1_vertex.get(i).getZ());
			gl.glNormal3f(body1_normal.get(i + 1).getX(), body1_normal.get(i + 1)
					.getY(), body1_normal.get(i + 1).getZ());
			gl.glVertex3f(body1_vertex.get(i + 1).getX(), body1_vertex.get(i + 1)
					.getY(), body1_vertex.get(i + 1).getZ());
			gl.glNormal3f(body1_normal.get(i + 2).getX(), body1_normal.get(i + 2)
					.getY(), body1_normal.get(i + 2).getZ());
			gl.glVertex3f(body1_vertex.get(i + 2).getX(), body1_vertex.get(i + 2)
					.getY(), body1_vertex.get(i + 2).getZ());
			gl.glEnd();
		}
	}
	
	private void Player1(GL2 gl) {

		// draw axis
	//	DrawXAxis(gl);

	//	DrawYAxis(gl);

	//	DrawZAxis(gl);


		lineofsight1 = 180+(float)((Math.atan((transY1-transY)/(transX1-transX))*180)/3.14);
		// draw the body
		gl.glPushMatrix();
		gl.glTranslatef(transX1, transY1, transZ1);
		gl.glRotatef(lineofsight1,0,0,1);
		{
			gl.glPushMatrix();
			gl.glTranslatef(Utils.BODY_X, Utils.BODY_Y, Utils.BODY_Z);
			DrawBody1(gl);
			gl.glPopMatrix();

			
			
			// draw hand left
			gl.glPushMatrix();
			{
				// draw hand top left
				gl.glTranslatef(Utils.LEFT_ARM_TOP_X, Utils.LEFT_ARM_TOP_Y,
						Utils.LEFT_ARM_TOP_Z);
				gl.glRotatef(rotate_left_handt1, 0, 1, 0);
				DrawhandTopLeft(gl);
				
				// draw hand bot left
				gl.glPushMatrix();
				gl.glTranslatef(Utils.LEFT_ARM_BOT_X, Utils.LEFT_ARM_BOT_Y,
						Utils.LEFT_ARM_BOT_Z);
				gl.glRotatef(rotate_left_handb1, 0, 1, 0);
				DrawHandBotLeft(gl);
				gl.glPopMatrix();
								
			}
			gl.glPopMatrix();

			// draw hand right
			gl.glPushMatrix();
			{
				// draw hand top right
				gl.glTranslatef(Utils.RIGHT_ARM_TOP_X, Utils.RIGHT_ARM_TOP_Y,
						Utils.RIGHT_ARM_TOP_Z);
				gl.glRotatef(rotate_right_handt1, 0, 1, 0);
				DrawHandTopRight(gl);	
				
				gl.glPushMatrix();
				// draw hand bot right
				gl.glTranslatef(Utils.RIGHT_ARM_BOT_X, Utils.RIGHT_ARM_BOT_Y,
						Utils.RIGHT_ARM_BOT_Z);
				gl.glRotatef(rotate_right_handb1,0,1,0);
				DrawHandBotRight(gl);
				gl.glPopMatrix();
			}gl.glPopMatrix();

			// draw head
			gl.glPushMatrix();
			gl.glTranslatef(Utils.HEAD_X, Utils.HEAD_Y, Utils.HEAD_Z);
			DrawHead(gl);
			gl.glPopMatrix();


			// draw leg left
			gl.glPushMatrix();
			{
				//draw leg top left
				gl.glTranslatef(Utils.LEFT_LEG_TOP_X, Utils.LEFT_LEG_TOP_Y,
						Utils.LEFT_LEG_TOP_Z);
				gl.glRotatef(rotate_left_legt1,0, 1, 0);
				DrawLegTopLeft(gl);
				
				gl.glPushMatrix();
				// draw leg bottom left
				gl.glTranslatef(Utils.LEFT_LEG_BOT_X, Utils.LEFT_LEG_BOT_Y,
						Utils.LEFT_LEG_BOT_Z);
				gl.glRotatef(rotate_left_legb1,0,1,0);
				DrawLegBottomLeft(gl);
								

				// draw left foot
				gl.glPushMatrix();
				gl.glTranslatef(Utils.LEFT_FOOT_X, Utils.LEFT_FOOT_Y,
						Utils.LEFT_FOOT_Z);
				DrawLeftFoot(gl);
				gl.glPopMatrix();
				gl.glPopMatrix();
				
			}
			gl.glPopMatrix();
			
			
			
			// draw leg right
			gl.glPushMatrix();
			{
				//draw leg top right
				gl.glTranslatef(Utils.RIGHT_LEG_TOP_X, Utils.RIGHT_LEG_TOP_Y,
						Utils.RIGHT_LEG_TOP_Z);
				gl.glRotatef(rotate_right_legt, 0, 1, 0);
				DrawLegTopRight(gl);
				
				gl.glPushMatrix();
				// draw leg bottom right
				gl.glTranslatef(Utils.RIGHT_LEG_BOT_X, Utils.RIGHT_LEG_BOT_Y,
						Utils.RIGHT_LEG_BOT_Z);
				gl.glRotatef(rotate_right_legb, 0, 1, 0);
				DrawLegBottomRight(gl);
				

				// draw right foot
				gl.glPushMatrix();
				gl.glTranslatef(Utils.RIGHT_FOOT_X, Utils.RIGHT_FOOT_Y,
						Utils.RIGHT_FOOT_Z);

				DrawRightFoot(gl);
				gl.glPopMatrix();
				gl.glPopMatrix();
				
			}
			gl.glPopMatrix();
			
		}
		gl.glPopMatrix();
	}

	private void Player(GL2 gl) {

		// draw axis
	//	DrawXAxis(gl);

	//	DrawYAxis(gl);

	//	DrawZAxis(gl);
		
		lineofsight = (float)(Math.atan((transY1-transY)/(transX1-transX))*180)/3.14f;

		// draw the ring
		gl.glPushMatrix();
		DrawRing(gl);
		gl.glPopMatrix();
		// draw the body
		gl.glPushMatrix();
		gl.glTranslatef(transX, transY, transZ);
		gl.glRotatef(lineofsight, 0, 0, 1);
		{
			gl.glPushMatrix();
			gl.glTranslatef(Utils.BODY_X, Utils.BODY_Y, Utils.BODY_Z);
			DrawBody(gl);
			gl.glPopMatrix();
			
			// draw hand left
			gl.glPushMatrix();
			{
				// draw hand top left
				gl.glTranslatef(Utils.LEFT_ARM_TOP_X, Utils.LEFT_ARM_TOP_Y,
						Utils.LEFT_ARM_TOP_Z);
				gl.glRotatef(rotate_left_handt, 0, 1, 0);
				DrawhandTopLeft(gl);
				
				// draw hand bot left
				gl.glPushMatrix();
				gl.glTranslatef(Utils.LEFT_ARM_BOT_X, Utils.LEFT_ARM_BOT_Y,
						Utils.LEFT_ARM_BOT_Z);
				gl.glRotatef(rotate_left_handb, 0, 1, 0);
				DrawHandBotLeft(gl);
				gl.glPopMatrix();
								
			}
			gl.glPopMatrix();

			// draw hand right
			gl.glPushMatrix();
			{
				// draw hand top right
				gl.glTranslatef(Utils.RIGHT_ARM_TOP_X, Utils.RIGHT_ARM_TOP_Y,
						Utils.RIGHT_ARM_TOP_Z);
				gl.glRotatef(rotate_right_handt, 0, 1, 0);
				DrawHandTopRight(gl);	
				
				gl.glPushMatrix();
				// draw hand bot right
				gl.glTranslatef(Utils.RIGHT_ARM_BOT_X, Utils.RIGHT_ARM_BOT_Y,
						Utils.RIGHT_ARM_BOT_Z);
				gl.glRotatef(rotate_right_handb,0,1,0);
				DrawHandBotRight(gl);
				gl.glPopMatrix();
			}gl.glPopMatrix();

			// draw head
			gl.glPushMatrix();
			gl.glTranslatef(Utils.HEAD_X, Utils.HEAD_Y, Utils.HEAD_Z);
			DrawHead(gl);
			gl.glPopMatrix();


			// draw leg left
			gl.glPushMatrix();
			{
				//draw leg top left
				gl.glTranslatef(Utils.LEFT_LEG_TOP_X, Utils.LEFT_LEG_TOP_Y,
						Utils.LEFT_LEG_TOP_Z);
				gl.glRotatef(rotate_left_legt,0, 1, 0);
				DrawLegTopLeft(gl);
				
				gl.glPushMatrix();
				// draw leg bottom left
				gl.glTranslatef(Utils.LEFT_LEG_BOT_X, Utils.LEFT_LEG_BOT_Y,
						Utils.LEFT_LEG_BOT_Z);
				gl.glRotatef(rotate_left_legb,0,1,0);
				DrawLegBottomLeft(gl);
								

				// draw left foot
				gl.glPushMatrix();
				gl.glTranslatef(Utils.LEFT_FOOT_X, Utils.LEFT_FOOT_Y,
						Utils.LEFT_FOOT_Z);
				DrawLeftFoot(gl);
				gl.glPopMatrix();
				gl.glPopMatrix();
				
			}
			gl.glPopMatrix();
			
			
			
			// draw leg right
			gl.glPushMatrix();
			{
				//draw leg top right
				gl.glTranslatef(Utils.RIGHT_LEG_TOP_X, Utils.RIGHT_LEG_TOP_Y,
						Utils.RIGHT_LEG_TOP_Z);
				gl.glRotatef(rotate_right_legt, 0, 1, 0);
				DrawLegTopRight(gl);
				
				gl.glPushMatrix();
				// draw leg bottom right
				gl.glTranslatef(Utils.RIGHT_LEG_BOT_X, Utils.RIGHT_LEG_BOT_Y,
						Utils.RIGHT_LEG_BOT_Z);
				gl.glRotatef(rotate_right_legb, 0, 1, 0);
				DrawLegBottomRight(gl);
				

				// draw right foot
				gl.glPushMatrix();
				gl.glTranslatef(Utils.RIGHT_FOOT_X, Utils.RIGHT_FOOT_Y,
						Utils.RIGHT_FOOT_Z);

				DrawRightFoot(gl);
				gl.glPopMatrix();
				gl.glPopMatrix();
				
			}
			gl.glPopMatrix();
			
		}
		gl.glPopMatrix();
		

	}

	// --------------- Methods of the GLEventListener interface -----------

	private void DrawHandBotLeft(GL2 gl) {
		// TODO Auto-generated method stub
		for (int i = 0, k = 0; i < hand_botl_vertex.size(); i += 3, k++) {
			gl.glBegin(GL2.GL_TRIANGLES);
			// System.out.println();
			gl.glColor3f(hand_botl_color.get(k).getX(), hand_botl_color.get(k)
					.getY(), hand_botl_color.get(k).getZ());
			gl.glNormal3f(hand_botl_normal.get(i).getX(),
					hand_botl_normal.get(i).getY(), hand_botl_normal.get(i)
							.getZ());
			gl.glVertex3f(hand_botl_vertex.get(i).getX(),
					hand_botl_vertex.get(i).getY(), hand_botl_vertex.get(i)
							.getZ());
			gl.glNormal3f(hand_botl_normal.get(i + 1).getX(), hand_botl_normal
					.get(i + 1).getY(), hand_botl_normal.get(i + 1).getZ());
			gl.glVertex3f(hand_botl_vertex.get(i + 1).getX(), hand_botl_vertex
					.get(i + 1).getY(), hand_botl_vertex.get(i + 1).getZ());
			gl.glNormal3f(hand_botl_normal.get(i + 2).getX(), hand_botl_normal
					.get(i + 2).getY(), hand_botl_normal.get(i + 2).getZ());
			gl.glVertex3f(hand_botl_vertex.get(i + 2).getX(), hand_botl_vertex
					.get(i + 2).getY(), hand_botl_vertex.get(i + 2).getZ());
			gl.glEnd();
		}
	}

	private void DrawHandBotRight(GL2 gl) {
		// TODO Auto-generated method stub
		for (int i = 0, k = 0; i < hand_botr_vertex.size(); i += 3, k++) {
			gl.glBegin(GL2.GL_TRIANGLES);
			// System.out.println();
			gl.glColor3f(hand_botr_color.get(k).getX(), hand_botr_color.get(k)
					.getY(), hand_botr_color.get(k).getZ());
			gl.glNormal3f(hand_botr_normal.get(i).getX(),
					hand_botr_normal.get(i).getY(), hand_botr_normal.get(i)
							.getZ());
			gl.glVertex3f(hand_botr_vertex.get(i).getX(),
					hand_botr_vertex.get(i).getY(), hand_botr_vertex.get(i)
							.getZ());
			gl.glNormal3f(hand_botr_normal.get(i + 1).getX(), hand_botr_normal
					.get(i + 1).getY(), hand_botr_normal.get(i + 1).getZ());
			gl.glVertex3f(hand_botr_vertex.get(i + 1).getX(), hand_botr_vertex
					.get(i + 1).getY(), hand_botr_vertex.get(i + 1).getZ());
			gl.glNormal3f(hand_botr_normal.get(i + 2).getX(), hand_botr_normal
					.get(i + 2).getY(), hand_botr_normal.get(i + 2).getZ());
			gl.glVertex3f(hand_botr_vertex.get(i + 2).getX(), hand_botr_vertex
					.get(i + 2).getY(), hand_botr_vertex.get(i + 2).getZ());
			gl.glEnd();
		}
	}

	private void DrawhandTopLeft(GL2 gl) {
		// TODO Auto-generated method stub
		for (int i = 0, k = 0; i < hand_topl_vertex.size(); i += 3, k++) {
			gl.glBegin(GL2.GL_TRIANGLES);
			// System.out.println();
			gl.glColor3f(hand_topl_color.get(k).getX(), hand_topl_color.get(k)
					.getY(), hand_topl_color.get(k).getZ());
			gl.glNormal3f(hand_topl_normal.get(i).getX(),
					hand_topl_normal.get(i).getY(), hand_topl_normal.get(i)
							.getZ());
			gl.glVertex3f(hand_topl_vertex.get(i).getX(),
					hand_topl_vertex.get(i).getY(), hand_topl_vertex.get(i)
							.getZ());
			gl.glNormal3f(hand_topl_normal.get(i + 1).getX(), hand_topl_normal
					.get(i + 1).getY(), hand_topl_normal.get(i + 1).getZ());
			gl.glVertex3f(hand_topl_vertex.get(i + 1).getX(), hand_topl_vertex
					.get(i + 1).getY(), hand_topl_vertex.get(i + 1).getZ());
			gl.glNormal3f(hand_topl_normal.get(i + 2).getX(), hand_topl_normal
					.get(i + 2).getY(), hand_topl_normal.get(i + 2).getZ());
			gl.glVertex3f(hand_topl_vertex.get(i + 2).getX(), hand_topl_vertex
					.get(i + 2).getY(), hand_topl_vertex.get(i + 2).getZ());
			gl.glEnd();
		}
	}

	private void DrawHandTopRight(GL2 gl) {
		// TODO Auto-generated method stub
		for (int i = 0, k = 0; i < hand_topr_vertex.size(); i += 3, k++) {
			gl.glBegin(GL2.GL_TRIANGLES);
			// System.out.println();
			gl.glColor3f(hand_topr_color.get(k).getX(), hand_topr_color.get(k)
					.getY(), hand_topr_color.get(k).getZ());
			gl.glNormal3f(hand_topr_normal.get(i).getX(),
					hand_topr_normal.get(i).getY(), hand_topr_normal.get(i)
							.getZ());
			gl.glVertex3f(hand_topr_vertex.get(i).getX(),
					hand_topr_vertex.get(i).getY(), hand_topr_vertex.get(i)
							.getZ());
			gl.glNormal3f(hand_topr_normal.get(i + 1).getX(), hand_topr_normal
					.get(i + 1).getY(), hand_topr_normal.get(i + 1).getZ());
			gl.glVertex3f(hand_topr_vertex.get(i + 1).getX(), hand_topr_vertex
					.get(i + 1).getY(), hand_topr_vertex.get(i + 1).getZ());
			gl.glNormal3f(hand_topr_normal.get(i + 2).getX(), hand_topr_normal
					.get(i + 2).getY(), hand_topr_normal.get(i + 2).getZ());
			gl.glVertex3f(hand_topr_vertex.get(i + 2).getX(), hand_topr_vertex
					.get(i + 2).getY(), hand_topr_vertex.get(i + 2).getZ());
			gl.glEnd();
		}
	}

	private void DrawLeftFoot(GL2 gl) {
		// TODO Auto-generated method stub
		for (int i = 0, k = 0; i < footl_vertex.size(); i += 3, k++) {
			gl.glBegin(GL2.GL_TRIANGLES);
			// System.out.println();
			gl.glColor3f(footl_color.get(k).getX(), footl_color.get(k).getY(),
					footl_color.get(k).getZ());
			gl.glNormal3f(footl_normal.get(i).getX(), footl_normal.get(i)
					.getY(), footl_normal.get(i).getZ());
			gl.glVertex3f(footl_vertex.get(i).getX(), footl_vertex.get(i)
					.getY(), footl_vertex.get(i).getZ());
			gl.glNormal3f(footl_normal.get(i + 1).getX(),
					footl_normal.get(i + 1).getY(), footl_normal.get(i + 1)
							.getZ());
			gl.glVertex3f(footl_vertex.get(i + 1).getX(),
					footl_vertex.get(i + 1).getY(), footl_vertex.get(i + 1)
							.getZ());
			gl.glNormal3f(footl_normal.get(i + 2).getX(),
					footl_normal.get(i + 2).getY(), footl_normal.get(i + 2)
							.getZ());
			gl.glVertex3f(footl_vertex.get(i + 2).getX(),
					footl_vertex.get(i + 2).getY(), footl_vertex.get(i + 2)
							.getZ());
			gl.glEnd();
		}
	}

	private void DrawRightFoot(GL2 gl) {
		// TODO Auto-generated method stub
		for (int i = 0, k = 0; i < footr_vertex.size(); i += 3, k++) {
			gl.glBegin(GL2.GL_TRIANGLES);
			// System.out.println();
			gl.glColor3f(footr_color.get(k).getX(), footr_color.get(k).getY(),
					footr_color.get(k).getZ());
			gl.glNormal3f(footr_normal.get(i).getX(), footr_normal.get(i)
					.getY(), footr_normal.get(i).getZ());
			gl.glVertex3f(footr_vertex.get(i).getX(), footr_vertex.get(i)
					.getY(), footr_vertex.get(i).getZ());
			gl.glNormal3f(footr_normal.get(i + 1).getX(),
					footr_normal.get(i + 1).getY(), footr_normal.get(i + 1)
							.getZ());
			gl.glVertex3f(footr_vertex.get(i + 1).getX(),
					footr_vertex.get(i + 1).getY(), footr_vertex.get(i + 1)
							.getZ());
			gl.glNormal3f(footr_normal.get(i + 2).getX(),
					footr_normal.get(i + 2).getY(), footr_normal.get(i + 2)
							.getZ());
			gl.glVertex3f(footr_vertex.get(i + 2).getX(),
					footr_vertex.get(i + 2).getY(), footr_vertex.get(i + 2)
							.getZ());
			gl.glEnd();
		}
	}

	private void DrawLegBottomLeft(GL2 gl) {
		// TODO Auto-generated method stub
		for (int i = 0, k = 0; i < leg_botl_vertex.size(); i += 3, k++) {
			gl.glBegin(GL2.GL_TRIANGLES);
			// System.out.println();
			gl.glColor3f(leg_botl_color.get(k).getX(), leg_botl_color.get(k)
					.getY(), leg_botl_color.get(k).getZ());
			gl.glNormal3f(leg_botl_normal.get(i).getX(), leg_botl_normal.get(i)
					.getY(), leg_botl_normal.get(i).getZ());
			gl.glVertex3f(leg_botl_vertex.get(i).getX(), leg_botl_vertex.get(i)
					.getY(), leg_botl_vertex.get(i).getZ());
			gl.glNormal3f(leg_botl_normal.get(i + 1).getX(), leg_botl_normal
					.get(i + 1).getY(), leg_botl_normal.get(i + 1).getZ());
			gl.glVertex3f(leg_botl_vertex.get(i + 1).getX(), leg_botl_vertex
					.get(i + 1).getY(), leg_botl_vertex.get(i + 1).getZ());
			gl.glNormal3f(leg_botl_normal.get(i + 2).getX(), leg_botl_normal
					.get(i + 2).getY(), leg_botl_normal.get(i + 2).getZ());
			gl.glVertex3f(leg_botl_vertex.get(i + 2).getX(), leg_botl_vertex
					.get(i + 2).getY(), leg_botl_vertex.get(i + 2).getZ());
			gl.glEnd();
		}
	}

	private void DrawLegBottomRight(GL2 gl) {
		// TODO Auto-generated method stub
		for (int i = 0, k = 0; i < leg_botr_vertex.size(); i += 3, k++) {
			gl.glBegin(GL2.GL_TRIANGLES);
			// System.out.println();
			gl.glColor3f(leg_botr_color.get(k).getX(), leg_botr_color.get(k)
					.getY(), leg_botr_color.get(k).getZ());
			gl.glNormal3f(leg_botr_normal.get(i).getX(), leg_botr_normal.get(i)
					.getY(), leg_botr_normal.get(i).getZ());
			gl.glVertex3f(leg_botr_vertex.get(i).getX(), leg_botr_vertex.get(i)
					.getY(), leg_botr_vertex.get(i).getZ());
			gl.glNormal3f(leg_botr_normal.get(i + 1).getX(), leg_botr_normal
					.get(i + 1).getY(), leg_botr_normal.get(i + 1).getZ());
			gl.glVertex3f(leg_botr_vertex.get(i + 1).getX(), leg_botr_vertex
					.get(i + 1).getY(), leg_botr_vertex.get(i + 1).getZ());
			gl.glNormal3f(leg_botr_normal.get(i + 2).getX(), leg_botr_normal
					.get(i + 2).getY(), leg_botr_normal.get(i + 2).getZ());
			gl.glVertex3f(leg_botr_vertex.get(i + 2).getX(), leg_botr_vertex
					.get(i + 2).getY(), leg_botr_vertex.get(i + 2).getZ());
			gl.glEnd();
		}
	}

	private void DrawLegTopLeft(GL2 gl) {
		// TODO Auto-generated method stub
		for (int i = 0, k = 0; i < leg_topl_vertex.size(); i += 3, k++) {
			gl.glBegin(GL2.GL_TRIANGLES);
			// System.out.println();
			gl.glColor3f(leg_topl_color.get(k).getX(), leg_topl_color.get(k)
					.getY(), leg_topl_color.get(k).getZ());
			gl.glNormal3f(leg_topl_normal.get(i).getX(), leg_topl_normal.get(i)
					.getY(), leg_topl_normal.get(i).getZ());
			gl.glVertex3f(leg_topl_vertex.get(i).getX(), leg_topl_vertex.get(i)
					.getY(), leg_topl_vertex.get(i).getZ());
			gl.glNormal3f(leg_topl_normal.get(i + 1).getX(), leg_topl_normal
					.get(i + 1).getY(), leg_topl_normal.get(i + 1).getZ());
			gl.glVertex3f(leg_topl_vertex.get(i + 1).getX(), leg_topl_vertex
					.get(i + 1).getY(), leg_topl_vertex.get(i + 1).getZ());
			gl.glNormal3f(leg_topl_normal.get(i + 2).getX(), leg_topl_normal
					.get(i + 2).getY(), leg_topl_normal.get(i + 2).getZ());
			gl.glVertex3f(leg_topl_vertex.get(i + 2).getX(), leg_topl_vertex
					.get(i + 2).getY(), leg_topl_vertex.get(i + 2).getZ());
			gl.glEnd();
		}
	}

	private void DrawLegTopRight(GL2 gl) {
		// TODO Auto-generated method stub
		for (int i = 0, k = 0; i < leg_topr_vertex.size(); i += 3, k++) {
			gl.glBegin(GL2.GL_TRIANGLES);
			// System.out.println();
			gl.glColor3f(leg_topr_color.get(k).getX(), leg_topr_color.get(k)
					.getY(), leg_topr_color.get(k).getZ());
			gl.glNormal3f(leg_topr_normal.get(i).getX(), leg_topr_normal.get(i)
					.getY(), leg_topr_normal.get(i).getZ());
			gl.glVertex3f(leg_topr_vertex.get(i).getX(), leg_topr_vertex.get(i)
					.getY(), leg_topr_vertex.get(i).getZ());
			gl.glNormal3f(leg_topr_normal.get(i + 1).getX(), leg_topr_normal
					.get(i + 1).getY(), leg_topr_normal.get(i + 1).getZ());
			gl.glVertex3f(leg_topr_vertex.get(i + 1).getX(), leg_topr_vertex
					.get(i + 1).getY(), leg_topr_vertex.get(i + 1).getZ());
			gl.glNormal3f(leg_topr_normal.get(i + 2).getX(), leg_topr_normal
					.get(i + 2).getY(), leg_topr_normal.get(i + 2).getZ());
			gl.glVertex3f(leg_topr_vertex.get(i + 2).getX(), leg_topr_vertex
					.get(i + 2).getY(), leg_topr_vertex.get(i + 2).getZ());
			gl.glEnd();
		}
	}
	
	private void HitAction()
	{
		int go=1,back=0,back1=0;
		int count = 0;
		while(go==1||back==1||back1==1)
		{
			if(go == 1)
			{
				rotate_right_handb-=24;
				rotate_right_handt+=10;
				if(rotate_right_handb <= 0)
				{
					go = 0;
					back = 1;
				}
			}else if(back == 1)
			{
				rotate_right_handb+=24;
				rotate_right_handt -=10;
				rotate_left_handb-=24;
				rotate_left_handt+=10;
				if(rotate_right_handt == 50)
				{
					go = 0;
					back1  = 1;
					back = 0;
				}
			}else if(back1 == 1)
			{
				rotate_left_handb += 24;
				rotate_left_handt-=10;
				if(rotate_left_handt == 50)
				{
					back1 = 0;
				}
			}
			repaint();
		}
	}

	private void DrawHead(GL2 gl) {
		// TODO Auto-generated method stub
		for (int i = 0, k = 0; i < head_vertex.size(); i += 3, k++) {
			gl.glBegin(GL2.GL_TRIANGLES);
			// System.out.println();
			gl.glColor3f(head_color.get(k).getX(), head_color.get(k).getY(),
					head_color.get(k).getZ());
			gl.glNormal3f(head_normal.get(i).getX(), head_normal.get(i).getY(),
					head_normal.get(i).getZ());
			gl.glVertex3f(head_vertex.get(i).getX(), head_vertex.get(i).getY(),
					head_vertex.get(i).getZ());
			gl.glNormal3f(head_normal.get(i + 1).getX(), head_normal.get(i + 1)
					.getY(), head_normal.get(i + 1).getZ());
			gl.glVertex3f(head_vertex.get(i + 1).getX(), head_vertex.get(i + 1)
					.getY(), head_vertex.get(i + 1).getZ());
			gl.glNormal3f(head_normal.get(i + 2).getX(), head_normal.get(i + 2)
					.getY(), head_normal.get(i + 2).getZ());
			gl.glVertex3f(head_vertex.get(i + 2).getX(), head_vertex.get(i + 2)
					.getY(), head_vertex.get(i + 2).getZ());
			gl.glEnd();
		}
	}

	public void display(GLAutoDrawable drawable) {
		// called when the panel needs to be drawn

		GL2 gl = drawable.getGL().getGL2();
		gl.glClearColor(0, 0, 0, 0);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL2.GL_PROJECTION); // Set up the projection.
		gl.glLoadIdentity();
		gl.glOrtho(-1.8f, 1.8f, -1.1, 1.1, -5, 5);
		gl.glMatrixMode(GL2.GL_MODELVIEW);

		gl.glLoadIdentity(); // Set up modelview transform.
		gl.glScalef(pscale,pscale,pscale);
		gl.glRotatef(-60, 1, 0, 0);
		gl.glRotatef(var1, 0, 0, 1);
		var1 += .8;
		gl.glTranslatef(-.5f, -.5f, 0);
//		gl.glRotatef(rotateY,0,1,0);
//		gl.glRotatef(rotateZ,0,0,1);
/*//		gl.glRotatef(45,1,1,0);
		gl.glRotatef(-60,1,0,0);
		gl.glRotatef(1,0,1,0);
		gl.glRotatef(130, 0, 0, 1);*/

		
		Player(gl);
		Player1(gl);
		
		if(left == 1)
		{
			rotate_left_legt -= 10;
			rotate_left_legb += 10 ;
			rotate_right_legt += 10;
			rotate_right_legb -= 10;
			if(rotate_left_legb == 60)
			{left = 0;
				right = 1;
			}
		}else
		{
			rotate_left_legt += 10;
			rotate_left_legb -= 10;
			rotate_right_legt -= 10;
			rotate_right_legb += 10;
			if(rotate_right_legb==60)
			{
			left = 1;
			right = 0;
			}
		}
		
		if(left1 == 1)
		{
			rotate_left_legt1 -= 10;
			rotate_left_legb1 += 10 ;
			rotate_right_legt1 += 10;
			rotate_right_legb1 -= 10;
			if(rotate_left_legb1 == 60)
			{left1 = 0;
				right1 = 1;
			}
		}else
		{
			rotate_left_legt1 += 10;
			rotate_left_legb1 -= 10;
			rotate_right_legt1 -= 10;
			rotate_right_legb1 += 10;
			if(rotate_right_legb1==60)
			{
			left1 = 1;
			right1 = 0;
			}
		}
		
		if(action_hit == 1)
		{
			System.out.println(rotate_right_handt+" "+rotate_right_handb+" "+rotate_left_handb+" "+rotate_left_handt);
			if(go == 1)
			{
				rotate_right_handb+=48;
				rotate_right_handt-=20;
				if(rotate_right_handb >= 0)
				{
					go = 0;
					back = 1;
				}
			}else if(back == 1)
			{
				rotate_right_handb-=48;
				rotate_right_handt +=20;
				rotate_left_handb+=48;
				rotate_left_handt-=20;
				if(rotate_right_handt >= -20)
				{
					go = 0;
					back1  = 1;
					back = 0;
				}
			}else if(back1 == 1)
			{
				rotate_left_handb -= 48;
				rotate_left_handt+=20;
				if(rotate_left_handt >= -20)
				{
					back1 = 0;
					action_hit = 0;
				}
			}
		}
		
		if(action_hit1 == 1)
		{
			//System.out.println(rotate_right_handt+" "+rotate_right_handb+" "+rotate_left_handb+" "+rotate_left_handt);
			if(go1 == 1)
			{
				rotate_right_handb1+=48;
				rotate_right_handt1-=20;
				if(rotate_right_handb1 >= 0)
				{
					go1 = 0;
					back12 = 1;
				}
			}else if(back12 == 1)
			{
				rotate_right_handb1-=48;
				rotate_right_handt1 +=20;
				rotate_left_handb1+=48;
				rotate_left_handt1-=20;
				if(rotate_right_handt1 >= -20)
				{
					go1 = 0;
					back11  = 1;
					back12 = 0;
				}
			}else if(back11 == 1)
			{
				rotate_left_handb1 -= 48;
				rotate_left_handt1+=20;
				if(rotate_left_handt1 >= -20)
				{
					back11 = 0;
					action_hit1 = 0;
				}
			}
		}
		
		if(Math.abs(transX-transX1)<.3f&&Math.abs(transY-transY1)<.3f)
		{
			close  = 1;
		}else
		{
			close = 0;
		}
		
		if(healthA <=0 || healthB <= 0)
		{
			if(healthA == 0)
			pb1.setBackground(Color.RED);
			else
			pb2.setBackground(Color.RED);
			System.out.println("Stop");
		}else
		repaint();
	}

	public void init(GLAutoDrawable drawable) {
		// called when the panel is created
		GL2 gl = drawable.getGL().getGL2();
		gl.glClearColor(0.8F, 0.8F, 0.8F, 1.0F);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
	}

	public void dispose(GLAutoDrawable drawable) {
		// called when the panel is being disposed
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// called when user resizes the window
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getWheelRotation() == -1)
		{
			pscale += .04f;
		}else
		{
			pscale -= .04f;
		}
		repaint();
	}
}
