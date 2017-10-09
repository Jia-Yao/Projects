//****************************************************************************
//       Example Main Program for CS480 PA4
//****************************************************************************
// Description: 
//
//     The following keys control the program:
//
//      Q,q: quit
//		T,t: show testing examples (3 different scenes)
//		F,f: use flat surface rendering
//		G,g: use Gouraud rendering
//		P,p: use Phong rendering
//		M,m: change material properties (random parameters)
//		S,s: toggle on/off specular term
//		D,d: toggle on/off diffuse term
//		A,a: toggle on/off ambient term
//		L,l: allow selecting a light source
//		O,o: allow selecting an object (O and L cannot work simultaneously)
//		R,r: toggle on/off rotation
//		E,e: toggle on/off translation 
//		W,e: toggle on/off scaling	(R E and W cannot work simultaneously)
//		X,x: rotate with respect to x axis
//		Y,y: rotate with respect to y axis
//		Z,z: rotate with respect to z axis
//		1:	 toggle on/off the infinite light source OR select the first object
//		2:	 toggle on/off the point light source OR select the second object
//		3:	 toggle on/off the spotlight source OR select the third object
//		4: 	 select the fourth object (only one object can be selected at any time)
//		>:	 increase the step number for examples
//		<:   decrease the step number for examples
//     	+,-: increase or decrease spectral exponent
//		Up/Down arrow: increase/decrease rotation angle OR translate/scale in positive/negative direction (for object)
//		Left/Right arrow: increase/decrease rotation angle OR translate in positive/negative direction (for camera)
//
//
//****************************************************************************
// History :
//	Dec 2015 Modified by Jia Yao to include three scenes and more options for keyboard interface
//
//  Nov 2014 modified to include test cases for shading example for PA4
//	code by Stan Sclaroff
//
//  Aug 2004 Created by Jianming Zhang based on the C
//  
//
//


import javax.swing.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.*; 
import java.awt.image.*;
//import java.io.File;
//import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

//import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.awt.GLCanvas;//for new version of gl
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;

import com.jogamp.opengl.util.FPSAnimator;//for new version of gl


public class PA4 extends JFrame
	implements GLEventListener, KeyListener, MouseListener, MouseMotionListener
{
	
	private static final long serialVersionUID = 1L;
	private final int DEFAULT_WINDOW_WIDTH=512;
	private final int DEFAULT_WINDOW_HEIGHT=512;
	private final float DEFAULT_LINE_WIDTH=1.0f;

	private GLCapabilities capabilities;
	private GLCanvas canvas;
	private FPSAnimator animator;

	final private int numTestCase;
	private int testCase;
	private BufferedImage buff;
	private DepthBuffer depth;
	@SuppressWarnings("unused")
	private Random rng;
	
	// specular exponent for materials
	private Material[] mats;
	private int ns=5; 
	private int Nsteps;
	
	// flags corresponding to key interface toggles
	private boolean doFlat, doGouraud, doPhong, useAmbient, useSpecular, useDiffuse, toggleLights;
	private ArrayList<Integer> onLights;
	private Integer light_index;
	private LightCollection scene1Lights;
	private LightCollection scene2Lights;
	private LightCollection scene3Lights;
	// flags corresponding to rotation
	private boolean selectObjects, moveObj1, moveObj2, moveObj3, moveObj4, doRotation, doTranslation, doScaling, selectX, selectY, selectZ;
	
	// objects in scene 1
	Sphere3D scene1_sphere;
	Box3D scene1_box;
    Torus3D scene1_torus;
    CappedCylinder3D scene1_cylinder;
    
    // objects in scene 2
    Ellipsoid3D scene2_ellipsoid;
    Torus3D scene2_torus;
    CappedCylinder3D scene2_cylinder;
    
    // objects in scene 3
    Ellipsoid3D scene3_ellipsoid1;
    Ellipsoid3D scene3_ellipsoid2;
    Ellipsoid3D scene3_ellipsoid3;
    Torus3D scene3_torus1;
    Torus3D scene3_torus2;
    Torus3D scene3_torus3;
    CappedCylinder3D scene3_cylinder1;
    CappedCylinder3D scene3_cylinder2;
    CappedCylinder3D scene3_cylinder3;
	
	/** The quaternion which controls the rotation of the world. */
    private Quaternion viewing_quaternion = new Quaternion();
    private Vector3D viewing_center = new Vector3D((float)(DEFAULT_WINDOW_WIDTH/2),(float)(DEFAULT_WINDOW_HEIGHT/2),(float)0.0);
    /** The last x and y coordinates of the mouse press. */
    private int last_x = 0, last_y = 0;
    /** Whether the world is being rotated. */
    private boolean rotate_world = false;
    
	public PA4()
	{
	    capabilities = new GLCapabilities(null);
	    capabilities.setDoubleBuffered(true);  // Enable Double buffering

	    canvas  = new GLCanvas(capabilities);
	    canvas.addGLEventListener(this);
	    canvas.addMouseListener(this);
	    canvas.addMouseMotionListener(this);
	    canvas.addKeyListener(this);
	    canvas.setAutoSwapBufferMode(true); // true by default. Just to be explicit
	    canvas.setFocusable(true);
	    getContentPane().add(canvas);

	    animator = new FPSAnimator(canvas, 60); // drive the display loop @ 60 FPS

	    numTestCase = 3;
	    testCase = 0;
	    Nsteps = 12;

	    setTitle("CS480/680 PA4");
	    setSize( DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setVisible(true);
	    setResizable(false);
	    
	    rng = new Random();
	    mats = new Material[4];
	    ColorType ka = new ColorType(0.3f,0.1f,0.13f);
	    mats[0] = new Material(ka, ka, new ColorType(1f,0.8f,0.85f), ns);	// ka=kd
	    ka = new ColorType(0.1f,0.10f,0.26f);
	    mats[1] = new Material(ka, ka, new ColorType(0.85f,0.85f,1f), ns);
	    ka = new ColorType(0.1f, 0.50f, 0.31f);
	    mats[2] = new Material(ka, ka, new ColorType(0.6f,1f,0.82f), ns);
	    ka = new ColorType(0.47f,0.62f,0.39f);
	    mats[3] = new Material(ka, ka, new ColorType(0.85f,1f,0.77f), ns);
	    
	    doFlat = true;
	    doGouraud = false;
	    doPhong = false;
	    useAmbient = true;
	    useSpecular = true;
	    useDiffuse = true;
	    toggleLights = false;
	    onLights = new ArrayList<Integer>();
	    onLights.add(new Integer(1));
	    onLights.add(new Integer(2));
	    onLights.add(new Integer(3));
	    
	    // define one infinite light source, with color = yellow
        ColorType light_color = new ColorType(0.7f,0.7f,0.0f);
        Vector3D light_direction = new Vector3D((float)0.0,(float)(-1.0/Math.sqrt(2.0)),(float)(1.0/Math.sqrt(2.0)));
        InfiniteLight in_light = new InfiniteLight(light_color,light_direction);
        // define one point light source, with color = cyan
        light_color = new ColorType(0.0f,1.0f,1.0f);
        Vector3D light_origin = new Vector3D((float)256.0,(float)256.0,(float)250.0);
        PointLight po_light = new PointLight(light_color, light_origin, (float)0.01, (float)0.001, (float)0.0001);		// the last three are constant coefficients a0 a1 a2
        // define one spot light source, with color = red
        light_color = new ColorType(1.0f,0.0f,0.0f);
        light_direction = new Vector3D((float)0.0,(float)0.0,(float)1.0);
        light_origin = new Vector3D((float)400.0,(float)128.0,(float)120.0);
        SpotLight sp_light = new SpotLight(light_color, light_direction, light_origin, (float)2.0, (float)Math.PI/10);	// the last two are constant parameters al theta
        // define the ambient coefficient
        ColorType Ia = new ColorType(0.3f,0.3f,0.3f);
        scene1Lights = new LightCollection(in_light, po_light, sp_light, Ia);
        
        // change colors of the lights for the other scenes
        light_color = new ColorType(1.0f,0.6f,0.6f);	// pink
        in_light = new InfiniteLight(light_color,light_direction);
        light_color = new ColorType(0.5f,1.0f,0.5f);	// green
        light_origin = new Vector3D((float)256.0,(float)256.0,(float)200.0);
        po_light = new PointLight(light_color, light_origin, (float)0.01, (float)0.001, (float)0.0001);
        scene2Lights = new LightCollection(in_light, po_light, sp_light, Ia);
        
        light_color = new ColorType(0.5f,0.6f,1.0f);	// blue
        in_light = new InfiniteLight(light_color,light_direction);
        light_color = new ColorType(1.0f,0.0f,0.0f);	// red
        po_light = new PointLight(light_color, light_origin, (float)0.01, (float)0.001, (float)0.0001);
        scene3Lights = new LightCollection(in_light, po_light, sp_light, Ia);
        
        // initialize flag for transforming objects in the scene
        selectObjects = false;
        moveObj1 = false;
        moveObj2 = false;
        moveObj3 = false;
        moveObj4 = false; 
        doRotation = false;
        doTranslation = false;
        doScaling = false;
        selectX = false;
        selectY = false;
        selectZ = false;
        
        // initialize objects in scene 1
        float radius = (float)50.0;
		scene1_sphere = new Sphere3D((float)128.0, (float)256.0, (float)128.0, (float)1.25*radius, Nsteps, Nsteps);
		scene1_box = new Box3D((float)200.0, (float)100.0, (float)50.0, (float)radius, Nsteps);
        scene1_torus = new Torus3D((float)350.0, (float)350.0, (float)75.0, (float)0.5*radius, (float)radius, Nsteps, Nsteps);
        scene1_cylinder = new CappedCylinder3D((float)400.0, (float)128.0, (float)75.0, (float)radius, (float)3*radius, Nsteps, Nsteps);
        
        // initialize objects in scene 2
        radius = (float)70.0;
		scene2_ellipsoid = new Ellipsoid3D((float)360.0, (float)100.0, (float)0.0, (float)1.5*radius, (float)0.75*radius, (float)2*radius, Nsteps, Nsteps);
        scene2_torus = new Torus3D((float)350.0, (float)320.0, (float)75.0, (float)0.5*radius, (float)radius, Nsteps, Nsteps);
        scene2_cylinder = new CappedCylinder3D((float)100.0, (float)388.0, (float)50.0, (float)radius, (float)3*radius, Nsteps, Nsteps);
        
        // initialize objects in scene 3
        radius = (float)30.0;
		scene3_ellipsoid1 = new Ellipsoid3D((float)80.0, (float)80.0, (float)100.0, radius, (float)1.5*radius, radius, Nsteps, Nsteps);
		scene3_ellipsoid2 = new Ellipsoid3D((float)220.0, (float)80.0, (float)100.0, (float)1.5*radius, (float)2*radius, radius, Nsteps, Nsteps);
		scene3_ellipsoid3 = new Ellipsoid3D((float)370.0, (float)80.0, (float)100.0, (float)2*radius, radius, (float)1.5*radius, Nsteps, Nsteps);
		scene3_torus1 = new Torus3D((float)80.0, (float)220.0, (float)45.0, (float)0.5*radius, (float)0.75*radius, Nsteps, Nsteps);
		scene3_torus2 = new Torus3D((float)220.0, (float)220.0, (float)45.0, (float)0.5*radius, (float)radius, Nsteps, Nsteps);
		scene3_torus3 = new Torus3D((float)370.0, (float)220.0, (float)45.0, (float)0.5*radius, (float)1.25*radius, Nsteps, Nsteps);
		scene3_cylinder1 = new CappedCylinder3D((float)80.0, (float)378.0, (float)75.0, (float)radius, (float)3*radius, Nsteps, Nsteps);
		scene3_cylinder2 = new CappedCylinder3D((float)220.0, (float)378.0, (float)75.0, (float)1.5*radius, (float)2*radius, Nsteps, Nsteps);
		scene3_cylinder3 = new CappedCylinder3D((float)370.0, (float)378.0, (float)75.0, (float)2*radius, (float)radius, Nsteps, Nsteps);
	}

	public void run()
	{
		animator.start();
	}

	public static void main( String[] args )
	{
	    PA4 P = new PA4();
	    P.run();
	}

	//*********************************************** 
	//  GLEventListener Interfaces
	//*********************************************** 
	public void init( GLAutoDrawable drawable) 
	{
	    GL gl = drawable.getGL();
	    gl.glClearColor( 0.0f, 0.0f, 0.0f, 0.0f);
	    gl.glLineWidth( DEFAULT_LINE_WIDTH );
	    Dimension sz = this.getContentPane().getSize();
	    buff = new BufferedImage(sz.width,sz.height,BufferedImage.TYPE_3BYTE_BGR);
	    depth = new DepthBuffer(sz.width, sz.height);
	    clearPixelBuffer();
	}

	// Redisplaying graphics
	public void display(GLAutoDrawable drawable)
	{
	    GL2 gl = drawable.getGL().getGL2();
	    WritableRaster wr = buff.getRaster();
	    DataBufferByte dbb = (DataBufferByte) wr.getDataBuffer();
	    byte[] data = dbb.getData();

	    gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
	    gl.glDrawPixels (buff.getWidth(), buff.getHeight(),
                GL2.GL_BGR, GL2.GL_UNSIGNED_BYTE,
                ByteBuffer.wrap(data));
        drawTestCase();
	}

	// Window size change
	public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
	{
		// deliberately left blank
	}
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
	      boolean deviceChanged)
	{
		// deliberately left blank
	}
	
	void clearPixelBuffer()
	{
		Graphics2D g = buff.createGraphics();
	    g.setColor(Color.BLACK);
	    g.fillRect(0, 0, buff.getWidth(), buff.getHeight());
	    g.dispose();
	    
	    for (int i = 0; i < depth.width; i++){
	    	for (int j = 0; j < depth.height; j++){
	    		depth.mat[i][j] = -999;
	    	}
	    }
	}
	
	// drawTest
	void drawTestCase()
	{  
		/* clear the window and vertex state */
		clearPixelBuffer();
	  
		//System.out.printf("Test case = %d\n",testCase);

		switch (testCase){
		case 0:
			scene1();
			break;
		case 1:
			scene2();
			break;
		case 2:
			scene3();
			break;
		}
	}


	//*********************************************** 
	//          KeyListener Interfaces
	//*********************************************** 
	public void keyTyped(KeyEvent key)
	{
	//      Q,q: quit
	//		T,t: show testing examples (3 different scenes)
	//		F,f: use flat surface rendering
	//		G,g: use Gouraud rendering
	//		P,p: use Phong rendering
	//		M,m: change material properties (random parameters)
	//		S,s: toggle on/off specular term
	//		D,d: toggle on/off diffuse term
	//		A,a: toggle on/off ambient term
	//		L,l: allow selecting a light source
	//		O,o: allow selecting an object (O and L cannot work simultaneously)
	//		R,r: toggle on/off rotation
	//		E,e: toggle on/off translation
	//		W,e: toggle on/off scaling	(R E and W cannot work simultaneously)
	//		X,x: rotate with respect to x axis
	//		Y,y: rotate with respect to y axis
	//		Z,z: rotate with respect to z axis
	//		1:	 toggle on/off the infinite light source OR select the first object
	//		2:	 toggle on/off the point light source OR select the second object
	//		3:	 toggle on/off the spotlight source OR select the third object
	//		4: 	 select the fourth object (only one object can be selected at any time)
	//		>:	 increase the step number for examples
	//		<:   decrease the step number for examples
	//     	+,-: increase or decrease spectral exponent
	    switch ( key.getKeyChar() ) 
	    {
	    case 'Q' :
	    case 'q' : 
	    	new Thread()
	    	{
	          	public void run() { animator.stop(); }
	        }.start();
	        System.exit(0);
	        break;
	    case 'T' :
	    case 't' : 
	    	testCase = (testCase+1)%numTestCase;
	    	drawTestCase();
	        break; 
	    case 'F' :
	    case 'f' : 
	    	doFlat = true;
	    	doGouraud = false;
	    	doPhong = false;
	    	drawTestCase();
	        break; 
	    case 'G' :
	    case 'g' : 
	    	doFlat = false;
	    	doGouraud = true;
	    	doPhong = false;
	    	drawTestCase();
	        break; 
	    case 'P' :
	    case 'p' : 
	    	doFlat = false;
	    	doGouraud = false;
	    	doPhong = true;
	    	drawTestCase();
	        break; 
	    case 'M' :
	    case 'm' : 
	    	mats[0].ka = new ColorType(rng.nextFloat(),rng.nextFloat(),rng.nextFloat());
	    	mats[0].kd = new ColorType(mats[0].ka);
	    	mats[0].ks = new ColorType(rng.nextFloat(),rng.nextFloat(),rng.nextFloat());
	    	mats[1].ka = new ColorType(rng.nextFloat(),rng.nextFloat(),rng.nextFloat());
	    	mats[1].kd = new ColorType(mats[1].ka);
	    	mats[1].ks = new ColorType(rng.nextFloat(),rng.nextFloat(),rng.nextFloat());
	    	mats[2].ka = new ColorType(rng.nextFloat(),rng.nextFloat(),rng.nextFloat());
	    	mats[2].kd = new ColorType(mats[2].ka);
	    	mats[2].ks = new ColorType(rng.nextFloat(),rng.nextFloat(),rng.nextFloat());
	    	mats[3].ka = new ColorType(rng.nextFloat(),rng.nextFloat(),rng.nextFloat());
	    	mats[3].kd = new ColorType(mats[3].ka);
	    	mats[3].ks = new ColorType(rng.nextFloat(),rng.nextFloat(),rng.nextFloat());
	    	drawTestCase();
	        break; 
	    case 'S' :
	    case 's' :
	    	useSpecular = !useSpecular;
	    	drawTestCase();
	    	break;
	    case 'D' :
	    case 'd' :
	    	useDiffuse = !useDiffuse;
	    	drawTestCase();
	    	break;
	    case 'A' :
	    case 'a' :
	    	useAmbient = !useAmbient;
	    	drawTestCase();
	    	break;
	    case 'L' :
	    case 'l' :
	    	toggleLights = true;
	    	selectObjects = false;
	    	break;
	    case 'O' :
	    case 'o' :
	    	selectObjects = true;
	    	toggleLights = false;
	    	break;
	    case 'R' :
	    case 'r' :
	    	doRotation= true;
	    	doTranslation = false;
	    	doScaling = false;
	    	break;
	    case 'E' :
	    case 'e' :
	    	doTranslation = true;
	    	doRotation = false;
	    	doScaling = false;
	    	break;
	    case 'W' :
	    case 'w' :
	    	doScaling = true;
	    	doRotation = false;
	    	doTranslation = false;
	    	break;
	    case 'X' :
	    case 'x' :
	    	selectX= true;
	    	selectY = false;
	    	selectZ = false;
	    	break;
	    case 'Y' :
	    case 'y' :
	    	selectY= true;
	    	selectX = false;
	    	selectZ = false;
	    	break;
	    case 'Z' :
	    case 'z' :
	    	selectZ= true;
	    	selectX = false;
	    	selectY = false;
	    	break;
	    case '1' :
	    	if (toggleLights){
	    		light_index = new Integer(1);
		    	if (this.onLights.contains(light_index)){
		    		onLights.remove(light_index);
			    } else{
			    	onLights.add(light_index);
			    }
		    	drawTestCase();
	    	}
	    	if (selectObjects){
	    		moveObj1 = true;
	    		moveObj2 = false;
	    		moveObj3 = false;
	    		moveObj4 = false;
	    	}
	    	break;
	    case '2' :
	    	if (toggleLights){
	    		light_index = new Integer(2);
		    	if (this.onLights.contains(light_index)){
		    		onLights.remove(light_index);
			    } else{
			    	onLights.add(light_index);
			    }
		    	drawTestCase();
	    	}
	    	if (selectObjects){
	    		moveObj2 = true;
	    		moveObj1 = false;
	    		moveObj3 = false;
	    		moveObj4 = false;
	    	}
	    	break;
	    case '3' :
	    	if (toggleLights){
	    		light_index = new Integer(3);
		    	if (this.onLights.contains(light_index)){
		    		onLights.remove(light_index);
			    } else{
			    	onLights.add(light_index);
			    }
		    	drawTestCase();
	    	}
	    	if (selectObjects){
	    		moveObj3 = true;
	    		moveObj1 = false;
	    		moveObj2 = false;
	    		moveObj4 = false;
	    	}
	    	break;	
	    case '4' :
	    	if (selectObjects){
	    		moveObj4 = true;
	    		moveObj1 = false;
	    		moveObj2 = false;
	    		moveObj3 = false;
	    	}
	    	break;
	    case '<':  
	        Nsteps = Nsteps < 4 ? Nsteps: Nsteps / 2;
	        System.out.printf( "Nsteps = %d \n", Nsteps);
	        drawTestCase();
	        break;
	    case '>':
	        Nsteps = Nsteps > 190 ? Nsteps: Nsteps * 2;
	        System.out.printf( "Nsteps = %d \n", Nsteps);
	        drawTestCase();
	        break;
	    case '+':
	    	ns++;
	    	mats[0].ns = ns;
	        mats[1].ns = ns;
	        mats[2].ns = ns;
	        mats[3].ns = ns;
	        drawTestCase();
	    	break;
	    case '-':
	    	if(ns>1){
	    		ns--;
	    		mats[0].ns = ns;
		        mats[1].ns = ns;
		        mats[2].ns = ns;
		        mats[3].ns = ns;
		        drawTestCase();
	    	}
	    	break;
	    default :
	        break;
	    }
	}

	//		Up/Down arrow: increase/decrease rotation angle OR translate/scale in positive/negative direction (for object)
	//		Left/Right arrow: increase/decrease rotation angle OR translate in positive/negative direction (for camera)
	public void keyPressed(KeyEvent key)
	{
	    switch (key.getKeyCode()) 
	    {
	    case KeyEvent.VK_ESCAPE:
	    	new Thread()
	        {
	    		public void run()
	    		{
	    			animator.stop();
	    		}
	        }.start();
	        System.exit(0);
	        break;
	    case KeyEvent.VK_KP_UP:		// transform individual objects
	    case KeyEvent.VK_UP:
	    	if (doRotation){
	    		Quaternion Q = new Quaternion();
	    		if (selectX){
	    			Q = new Quaternion((float)Math.cos(0.035), (float)Math.sin(0.035), 0f, 0f);	// roughly 2 degrees
	    		} else if (selectY){
	    			Q = new Quaternion((float)Math.cos(0.035), 0f, (float)Math.sin(0.035), 0f);	// roughly 2 degrees
	    		} else if (selectZ){
	    			Q = new Quaternion((float)Math.cos(0.035), 0f, 0f, (float)Math.sin(0.035));	// roughly 2 degrees
	    		}
	    		if (moveObj1){
    				switch (testCase){
    				case 0:
    					scene1_sphere.rotate(Q);
    					break;
    				case 1:
    					scene2_ellipsoid.rotate(Q);
    					break;
    				case 2:
    					scene3_ellipsoid1.rotate(Q);
    					scene3_torus1.rotate(Q);
    					scene3_cylinder1.rotate(Q);
    					break;
    				}
    			} else if (moveObj2){
    				switch (testCase){
    				case 0:
    					scene1_box.rotate(Q);
    					break;
    				case 1:
    					scene2_torus.rotate(Q);
    					break;
    				case 2:
    					scene3_ellipsoid2.rotate(Q);
    					scene3_torus2.rotate(Q);
    					scene3_cylinder2.rotate(Q);
    					break;
    				}
    			} else if (moveObj3){
    				switch (testCase){
    				case 0:
    					scene1_torus.rotate(Q);
    					break;
    				case 1:
    					scene2_cylinder.rotate(Q);
    					break;
    				case 2:
    					scene3_ellipsoid3.rotate(Q);
    					scene3_torus3.rotate(Q);
    					scene3_cylinder3.rotate(Q);
    					break;
    				}
    			} else if (moveObj4){
    				switch (testCase){
    				case 0:
    					scene1_cylinder.rotate(Q);
    					break;
    				case 1:
    					// left blank
    					break;
    				case 2:
    					// left blank
    					break;
    				}
    			}
	    	} 
	    	else if (doTranslation){
	    		if (selectX){
	    			if (moveObj1){
	    				switch (testCase){
	    				case 0:
	    					scene1_sphere.translate(2,0,0);
	    					break;
	    				case 1:
	    					scene2_ellipsoid.translate(2,0,0);
	    					break;
	    				case 2:
	    					scene3_ellipsoid1.translate(2,0,0);
	    					scene3_torus1.translate(2,0,0);
	    					scene3_cylinder1.translate(2,0,0);
	    					break;
	    				}
	    			} else if (moveObj2){
	    				switch (testCase){
	    				case 0:
	    					scene1_box.translate(2,0,0);
	    					break;
	    				case 1:
	    					scene2_torus.translate(2,0,0);
	    					break;
	    				case 2:
	    					scene3_ellipsoid2.translate(2,0,0);
	    					scene3_torus2.translate(2,0,0);
	    					scene3_cylinder2.translate(2,0,0);
	    					break;
	    				}
	    			} else if (moveObj3){
	    				switch (testCase){
	    				case 0:
	    					scene1_torus.translate(2,0,0);
	    					break;
	    				case 1:
	    					scene2_cylinder.translate(2,0,0);
	    					break;
	    				case 2:
	    					scene3_ellipsoid3.translate(2,0,0);
	    					scene3_torus3.translate(2,0,0);
	    					scene3_cylinder3.translate(2,0,0);
	    					break;
	    				}
	    			} else if (moveObj4){
	    				switch (testCase){
	    				case 0:
	    					scene1_cylinder.translate(2,0,0);
	    					break;
	    				case 1:
	    					// left blank
	    					break;
	    				case 2:
	    					// left blank
	    					break;
	    				}
	    			}
	    		} else if (selectY){
	    			if (moveObj1){
	    				switch (testCase){
	    				case 0:
	    					scene1_sphere.translate(0,-2,0);
	    					break;
	    				case 1:
	    					scene2_ellipsoid.translate(0,-2,0);
	    					break;
	    				case 2:
	    					scene3_ellipsoid1.translate(0,-2,0);
	    					scene3_torus1.translate(0,-2,0);
	    					scene3_cylinder1.translate(0,-2,0);
	    					break;
	    				}
	    			} else if (moveObj2){
	    				switch (testCase){
	    				case 0:
	    					scene1_box.translate(0,-2,0);
	    					break;
	    				case 1:
	    					scene2_torus.translate(0,-2,0);
	    					break;
	    				case 2:
	    					scene3_ellipsoid2.translate(0,-2,0);
	    					scene3_torus2.translate(0,-2,0);
	    					scene3_cylinder2.translate(0,-2,0);
	    					break;
	    				}
	    			} else if (moveObj3){
	    				switch (testCase){
	    				case 0:
	    					scene1_torus.translate(0,-2,0);
	    					break;
	    				case 1:
	    					scene2_cylinder.translate(0,-2,0);
	    					break;
	    				case 2:
	    					scene3_ellipsoid3.translate(0,-2,0);
	    					scene3_torus3.translate(0,-2,0);
	    					scene3_cylinder3.translate(0,-2,0);
	    					break;
	    				}
	    			} else if (moveObj4){
	    				switch (testCase){
	    				case 0:
	    					scene1_cylinder.translate(0,-2,0);
	    					break;
	    				case 1:
	    					// left blank
	    					break;
	    				case 2:
	    					// left blank
	    					break;
	    				}
	    			}
	    		} else if (selectZ){
	    			if (moveObj1){
	    				switch (testCase){
	    				case 0:
	    					scene1_sphere.translate(0,0,2);
	    					break;
	    				case 1:
	    					scene2_ellipsoid.translate(0,0,2);
	    					break;
	    				case 2:
	    					scene3_ellipsoid1.translate(0,0,2);
	    					scene3_torus1.translate(0,0,2);
	    					scene3_cylinder1.translate(0,0,2);
	    					break;
	    				}
	    			} else if (moveObj2){
	    				switch (testCase){
	    				case 0:
	    					scene1_box.translate(0,0,2);
	    					break;
	    				case 1:
	    					scene2_torus.translate(0,0,2);
	    					break;
	    				case 2:
	    					scene3_ellipsoid2.translate(0,0,2);
	    					scene3_torus2.translate(0,0,2);
	    					scene3_cylinder2.translate(0,0,2);
	    					break;
	    				}
	    			} else if (moveObj3){
	    				switch (testCase){
	    				case 0:
	    					scene1_torus.translate(0,0,2);
	    					break;
	    				case 1:
	    					scene2_cylinder.translate(0,0,2);
	    					break;
	    				case 2:
	    					scene3_ellipsoid3.translate(0,0,2);
	    					scene3_torus3.translate(0,0,2);
	    					scene3_cylinder3.translate(0,0,2);
	    					break;
	    				}
	    			} else if (moveObj4){
	    				switch (testCase){
	    				case 0:
	    					scene1_cylinder.translate(0,0,2);
	    					break;
	    				case 1:
	    					// left blank
	    					break;
	    				case 2:
	    					// left blank
	    					break;
	    				}
	    			}
	    		}
	    	}
	    	else if (doScaling){
	    		if (moveObj1){
    				switch (testCase){
    				case 0:
    					scene1_sphere.r += 1;
    					break;
    				case 1:
    					if (selectX){
    						scene2_ellipsoid.rx += 1;
    					} else if (selectY){
    						scene2_ellipsoid.ry += 1;
    					}
    					else if (selectY){
    						scene2_ellipsoid.rz += 1;
    					}
    					break;
    				case 2:
    					if (selectX){
    						scene3_ellipsoid1.rx += 1;
    					} else if (selectY){
    						scene3_ellipsoid1.ry += 1;
    					}
    					else if (selectZ){
    						scene3_ellipsoid1.rz += 1;
    					}
    					scene3_torus1.r_axial += 1;
    					scene3_torus1.r = 0.5f*scene3_torus1.r_axial;
    					scene3_cylinder1.r += 1;
    					scene3_cylinder1.height = 3*scene3_cylinder1.r;
    					break;
    				}
    			} else if (moveObj2){
    				switch (testCase){
    				case 0:
    					scene1_box.height += 1;
    					break;
    				case 1:
    					scene2_torus.r_axial += 1;
    					scene2_torus.r = 0.5f*scene2_torus.r_axial;
    					break;
    				case 2:
    					if (selectX){
    						scene3_ellipsoid2.rx += 1;
    					} else if (selectY){
    						scene3_ellipsoid2.ry += 1;
    					}
    					else if (selectZ){
    						scene3_ellipsoid2.rz += 1;
    					}
    					scene3_torus2.r_axial += 1;
    					scene3_torus2.r = 0.5f*scene3_torus2.r_axial;
    					scene3_cylinder2.r += 1;
    					scene3_cylinder2.height = 3*scene3_cylinder2.r;
    					break;
    				}
    			} else if (moveObj3){
    				switch (testCase){
    				case 0:
    					scene1_torus.r_axial += 1;
    					scene1_torus.r = 0.5f*scene1_torus.r_axial;
    					break;
    				case 1:
    					scene2_cylinder.r += 1;
    					scene2_cylinder.height = 3*scene2_cylinder.r;
    					break;
    				case 2:
    					if (selectX){
    						scene3_ellipsoid3.rx += 1;
    					} else if (selectY){
    						scene3_ellipsoid3.ry += 1;
    					}
    					else if (selectZ){
    						scene3_ellipsoid3.rz += 1;
    					}
    					scene3_torus3.r_axial += 1;
    					scene3_torus3.r = 0.5f*scene3_torus3.r_axial;
    					scene3_cylinder3.r += 1;
    					scene3_cylinder3.height = 3*scene3_cylinder3.r;
    					break;
    				}
    			} else if (moveObj4){
    				switch (testCase){
    				case 0:
    					scene1_cylinder.r += 1;
    					scene1_cylinder.height = 3*scene1_cylinder.r;
    					break;
    				case 1:
    					// left blank
    					break;
    				case 2:
    					// left blank
    					break;
    				}
    			}
	    	}
	    	drawTestCase();
	    	break;
	    case KeyEvent.VK_KP_DOWN:	// transform individual objects
	    case KeyEvent.VK_DOWN:
	    	if (doRotation){
	    		Quaternion Q = new Quaternion();
	    		if (selectX){
	    			Q = new Quaternion((float)Math.cos(-0.035), (float)Math.sin(-0.035), 0f, 0f);	// roughly -2 degrees
	    		} else if (selectY){
	    			Q = new Quaternion((float)Math.cos(-0.035), 0f, (float)Math.sin(-0.035), 0f);	// roughly -2 degrees
	    		} else if (selectZ){
	    			Q = new Quaternion((float)Math.cos(-0.035), 0f, 0f, (float)Math.sin(-0.035));	// roughly -2 degrees
	    		}
	    		if (moveObj1){
    				switch (testCase){
    				case 0:
    					scene1_sphere.rotate(Q);
    					break;
    				case 1:
    					scene2_ellipsoid.rotate(Q);
    					break;
    				case 2:
    					scene3_ellipsoid1.rotate(Q);
    					scene3_torus1.rotate(Q);
    					scene3_cylinder1.rotate(Q);
    					break;
    				}
    			} else if (moveObj2){
    				switch (testCase){
    				case 0:
    					scene1_box.rotate(Q);
    					break;
    				case 1:
    					scene2_torus.rotate(Q);
    					break;
    				case 2:
    					scene3_ellipsoid2.rotate(Q);
    					scene3_torus2.rotate(Q);
    					scene3_cylinder2.rotate(Q);
    					break;
    				}
    			} else if (moveObj3){
    				switch (testCase){
    				case 0:
    					scene1_torus.rotate(Q);
    					break;
    				case 1:
    					scene2_cylinder.rotate(Q);
    					break;
    				case 2:
    					scene3_ellipsoid3.rotate(Q);
    					scene3_torus3.rotate(Q);
    					scene3_cylinder3.rotate(Q);
    					break;
    				}
    			} else if (moveObj4){
    				switch (testCase){
    				case 0:
    					scene1_cylinder.rotate(Q);
    					break;
    				case 1:
    					// left blank
    					break;
    				case 2:
    					// left blank
    					break;
    				}
    			}
	    	} 
	    	else if (doTranslation){
	    		if (selectX){
	    			if (moveObj1){
	    				switch (testCase){
	    				case 0:
	    					scene1_sphere.translate(-2,0,0);
	    					break;
	    				case 1:
	    					scene2_ellipsoid.translate(-2,0,0);
	    					break;
	    				case 2:
	    					scene3_ellipsoid1.translate(-2,0,0);
	    					scene3_torus1.translate(-2,0,0);
	    					scene3_cylinder1.translate(-2,0,0);
	    					break;
	    				}
	    			} else if (moveObj2){
	    				switch (testCase){
	    				case 0:
	    					scene1_box.translate(-2,0,0);
	    					break;
	    				case 1:
	    					scene2_torus.translate(-2,0,0);
	    					break;
	    				case 2:
	    					scene3_ellipsoid2.translate(-2,0,0);
	    					scene3_torus2.translate(-2,0,0);
	    					scene3_cylinder2.translate(-2,0,0);
	    					break;
	    				}
	    			} else if (moveObj3){
	    				switch (testCase){
	    				case 0:
	    					scene1_torus.translate(-2,0,0);
	    					break;
	    				case 1:
	    					scene2_cylinder.translate(-2,0,0);
	    					break;
	    				case 2:
	    					scene3_ellipsoid3.translate(-2,0,0);
	    					scene3_torus3.translate(-2,0,0);
	    					scene3_cylinder3.translate(-2,0,0);
	    					break;
	    				}
	    			} else if (moveObj4){
	    				switch (testCase){
	    				case 0:
	    					scene1_cylinder.translate(-2,0,0);
	    					break;
	    				case 1:
	    					// left blank
	    					break;
	    				case 2:
	    					// left blank
	    					break;
	    				}
	    			}
	    		} else if (selectY){
	    			if (moveObj1){
	    				switch (testCase){
	    				case 0:
	    					scene1_sphere.translate(0,2,0);
	    					break;
	    				case 1:
	    					scene2_ellipsoid.translate(0,2,0);
	    					break;
	    				case 2:
	    					scene3_ellipsoid1.translate(0,2,0);
	    					scene3_torus1.translate(0,2,0);
	    					scene3_cylinder1.translate(0,2,0);
	    					break;
	    				}
	    			} else if (moveObj2){
	    				switch (testCase){
	    				case 0:
	    					scene1_box.translate(0,2,0);
	    					break;
	    				case 1:
	    					scene2_torus.translate(0,2,0);
	    					break;
	    				case 2:
	    					scene3_ellipsoid2.translate(0,2,0);
	    					scene3_torus2.translate(0,2,0);
	    					scene3_cylinder2.translate(0,2,0);
	    					break;
	    				}
	    			} else if (moveObj3){
	    				switch (testCase){
	    				case 0:
	    					scene1_torus.translate(0,2,0);
	    					break;
	    				case 1:
	    					scene2_cylinder.translate(0,2,0);
	    					break;
	    				case 2:
	    					scene3_ellipsoid3.translate(0,2,0);
	    					scene3_torus3.translate(0,2,0);
	    					scene3_cylinder3.translate(0,2,0);
	    					break;
	    				}
	    			} else if (moveObj4){
	    				switch (testCase){
	    				case 0:
	    					scene1_cylinder.translate(0,2,0);
	    					break;
	    				case 1:
	    					// left blank
	    					break;
	    				case 2:
	    					// left blank
	    					break;
	    				}
	    			}
	    		} else if (selectZ){
	    			if (moveObj1){
	    				switch (testCase){
	    				case 0:
	    					scene1_sphere.translate(0,0,-2);
	    					break;
	    				case 1:
	    					scene2_ellipsoid.translate(0,0,-2);
	    					break;
	    				case 2:
	    					scene3_ellipsoid1.translate(0,0,-2);
	    					scene3_torus1.translate(0,0,-2);
	    					scene3_cylinder1.translate(0,0,-2);
	    					break;
	    				}
	    			} else if (moveObj2){
	    				switch (testCase){
	    				case 0:
	    					scene1_box.translate(0,0,-2);
	    					break;
	    				case 1:
	    					scene2_torus.translate(0,0,-2);
	    					break;
	    				case 2:
	    					scene3_ellipsoid2.translate(0,0,-2);
	    					scene3_torus2.translate(0,0,-2);
	    					scene3_cylinder2.translate(0,0,-2);
	    					break;
	    				}
	    			} else if (moveObj3){
	    				switch (testCase){
	    				case 0:
	    					scene1_torus.translate(0,0,-2);
	    					break;
	    				case 1:
	    					scene2_cylinder.translate(0,0,-2);
	    					break;
	    				case 2:
	    					scene3_ellipsoid3.translate(0,0,-2);
	    					scene3_torus3.translate(0,0,-2);
	    					scene3_cylinder3.translate(0,0,-2);
	    					break;
	    				}
	    			} else if (moveObj4){
	    				switch (testCase){
	    				case 0:
	    					scene1_cylinder.translate(0,0,-2);
	    					break;
	    				case 1:
	    					// left blank
	    					break;
	    				case 2:
	    					// left blank
	    					break;
	    				}
	    			}
	    		}
	    	}
	    	else if (doScaling){
	    		if (moveObj1){
    				switch (testCase){
    				case 0:
    					scene1_sphere.r -= 1;
    					break;
    				case 1:
    					if (selectX){
    						scene2_ellipsoid.rx -= 1;
    					} else if (selectY){
    						scene2_ellipsoid.ry -= 1;
    					}
    					else if (selectY){
    						scene2_ellipsoid.rz -= 1;
    					}
    					break;
    				case 2:
    					if (selectX){
    						scene3_ellipsoid1.rx -= 1;
    					} else if (selectY){
    						scene3_ellipsoid1.ry -= 1;
    					}
    					else if (selectZ){
    						scene3_ellipsoid1.rz -= 1;
    					}
    					scene3_torus1.r_axial -= 1;
    					scene3_torus1.r = 0.5f*scene3_torus1.r_axial;
    					scene3_cylinder1.r -= 1;
    					scene3_cylinder1.height = 3*scene3_cylinder1.r;
    					break;
    				}
    			} else if (moveObj2){
    				switch (testCase){
    				case 0:
    					scene1_box.height -= 1;
    					break;
    				case 1:
    					scene2_torus.r_axial -= 1;
    					scene2_torus.r = 0.5f*scene2_torus.r_axial;
    					break;
    				case 2:
    					if (selectX){
    						scene3_ellipsoid2.rx -= 1;
    					} else if (selectY){
    						scene3_ellipsoid2.ry -= 1;
    					}
    					else if (selectZ){
    						scene3_ellipsoid2.rz -= 1;
    					}
    					scene3_torus2.r_axial -= 1;
    					scene3_torus2.r = 0.5f*scene3_torus2.r_axial;
    					scene3_cylinder2.r -= 1;
    					scene3_cylinder2.height = 3*scene3_cylinder2.r;
    					break;
    				}
    			} else if (moveObj3){
    				switch (testCase){
    				case 0:
    					scene1_torus.r_axial -= 1;
    					scene1_torus.r = 0.5f*scene1_torus.r_axial;
    					break;
    				case 1:
    					scene2_cylinder.r -= 1;
    					scene2_cylinder.height = 3*scene2_cylinder.r;
    					break;
    				case 2:
    					if (selectX){
    						scene3_ellipsoid3.rx -= 1;
    					} else if (selectY){
    						scene3_ellipsoid3.ry -= 1;
    					}
    					else if (selectZ){
    						scene3_ellipsoid3.rz -= 1;
    					}
    					scene3_torus3.r_axial -= 1;
    					scene3_torus3.r = 0.5f*scene3_torus3.r_axial;
    					scene3_cylinder3.r -= 1;
    					scene3_cylinder3.height = 3*scene3_cylinder3.r;
    					break;
    				}
    			} else if (moveObj4){
    				switch (testCase){
    				case 0:
    					scene1_cylinder.r -= 1;
    					scene1_cylinder.height = 3*scene1_cylinder.r;
    					break;
    				case 1:
    					// left blank
    					break;
    				case 2:
    					// left blank
    					break;
    				}
    			}
	    	}
	    	drawTestCase();
	    	break;
	    case KeyEvent.VK_KP_LEFT:	// transform camera
	    case KeyEvent.VK_LEFT:
	    	if (doRotation){
	    		Quaternion Q = new Quaternion();
	    		if (selectX){
	    			Q = new Quaternion((float)Math.cos(0.035), (float)Math.sin(0.035), 0f, 0f);	// roughly 2 degrees
	    		} else if (selectY){
	    			Q = new Quaternion((float)Math.cos(0.035), 0f, (float)Math.sin(0.035), 0f);	// roughly 2 degrees
	    		} else if (selectZ){
	    			Q = new Quaternion((float)Math.cos(0.035), 0f, 0f, (float)Math.sin(0.035));	// roughly 2 degrees
	    		}
	    		switch (testCase){
				case 0:
					scene1_sphere.rotate(Q);
					scene1_box.rotate(Q);
					scene1_torus.rotate(Q);
					scene1_cylinder.rotate(Q);
					scene1Lights.in_light.rotate(Q);
					scene1Lights.sp_light.rotate(Q);
					break;
				case 1:
					scene2_ellipsoid.rotate(Q);
					scene2_torus.rotate(Q);
					scene2_cylinder.rotate(Q);
					scene2Lights.in_light.rotate(Q);
					scene2Lights.sp_light.rotate(Q);
					break;
				case 2:
					scene3_ellipsoid1.rotate(Q);
					scene3_torus1.rotate(Q);
					scene3_cylinder1.rotate(Q);
					scene3_ellipsoid2.rotate(Q);
					scene3_torus2.rotate(Q);
					scene3_cylinder2.rotate(Q);
					scene3_ellipsoid3.rotate(Q);
					scene3_torus3.rotate(Q);
					scene3_cylinder3.rotate(Q);
					scene3Lights.in_light.rotate(Q);
					scene3Lights.sp_light.rotate(Q);
					break;
				}
	    	}
	    	else if (doTranslation){
	    		if (selectX){
	    			switch (testCase){
					case 0:
						scene1_sphere.translate(2,0,0);
						scene1_box.translate(2,0,0);
						scene1_torus.translate(2,0,0);
						scene1_cylinder.translate(2,0,0);
						scene1Lights.po_light.translate(2,0,0);
						scene1Lights.sp_light.translate(2,0,0);
						break;
					case 1:
						scene2_ellipsoid.translate(2,0,0);
						scene2_torus.translate(2,0,0);
						scene2_cylinder.translate(2,0,0);
						scene2Lights.po_light.translate(2,0,0);
						scene2Lights.sp_light.translate(2,0,0);
						break;
					case 2:
						scene3_ellipsoid1.translate(2,0,0);
						scene3_torus1.translate(2,0,0);
						scene3_cylinder1.translate(2,0,0);
						scene3_ellipsoid2.translate(2,0,0);
						scene3_torus2.translate(2,0,0);
						scene3_cylinder2.translate(2,0,0);
						scene3_ellipsoid3.translate(2,0,0);
						scene3_torus3.translate(2,0,0);
						scene3_cylinder3.translate(2,0,0);
						scene3Lights.po_light.translate(2,0,0);
						scene3Lights.sp_light.translate(2,0,0);
						break;
					}
	    		} else if (selectY){
	    			switch (testCase){
					case 0:
						scene1_sphere.translate(0,2,0);
						scene1_box.translate(0,2,0);
						scene1_torus.translate(0,2,0);
						scene1_cylinder.translate(0,2,0);
						scene1Lights.po_light.translate(0,2,0);
						scene1Lights.sp_light.translate(0,2,0);
						break;
					case 1:
						scene2_ellipsoid.translate(0,2,0);
						scene2_torus.translate(0,2,0);
						scene2_cylinder.translate(0,2,0);
						scene2Lights.po_light.translate(0,2,0);
						scene2Lights.sp_light.translate(0,2,0);
						break;
					case 2:
						scene3_ellipsoid1.translate(0,2,0);
						scene3_torus1.translate(0,2,0);
						scene3_cylinder1.translate(0,2,0);
						scene3_ellipsoid2.translate(0,2,0);
						scene3_torus2.translate(0,2,0);
						scene3_cylinder2.translate(0,2,0);
						scene3_ellipsoid3.translate(0,2,0);
						scene3_torus3.translate(0,2,0);
						scene3_cylinder3.translate(0,2,0);
						scene3Lights.po_light.translate(0,2,0);
						scene3Lights.sp_light.translate(0,2,0);
						break;
					}
	    		} else if (selectZ){
	    			switch (testCase){
					case 0:
						scene1_sphere.translate(0,0,2);
						scene1_box.translate(0,0,2);
						scene1_torus.translate(0,0,2);
						scene1_cylinder.translate(0,0,2);
						scene1Lights.po_light.translate(0,0,2);
						scene1Lights.sp_light.translate(0,0,2);
						break;
					case 1:
						scene2_ellipsoid.translate(0,0,2);
						scene2_torus.translate(0,0,2);
						scene2_cylinder.translate(0,0,2);
						scene2Lights.po_light.translate(0,0,2);
						scene2Lights.sp_light.translate(0,0,2);
						break;
					case 2:
						scene3_ellipsoid1.translate(0,0,2);
						scene3_torus1.translate(0,0,2);
						scene3_cylinder1.translate(0,0,2);
						scene3_ellipsoid2.translate(0,0,2);
						scene3_torus2.translate(0,0,2);
						scene3_cylinder2.translate(0,0,2);
						scene3_ellipsoid3.translate(0,0,2);
						scene3_torus3.translate(0,0,2);
						scene3_cylinder3.translate(0,0,2);
						scene3Lights.po_light.translate(0,0,2);
						scene3Lights.sp_light.translate(0,0,2);
						break;
					}
	    		}
	    	}
	    	drawTestCase();
	    	break;
	    case KeyEvent.VK_KP_RIGHT:	// transform camera
	    case KeyEvent.VK_RIGHT:
	    	if (doRotation){
	    		Quaternion Q = new Quaternion();
	    		if (selectX){
	    			Q = new Quaternion((float)Math.cos(-0.035), (float)Math.sin(-0.035), 0f, 0f);	// roughly 2 degrees
	    		} else if (selectY){
	    			Q = new Quaternion((float)Math.cos(-0.035), 0f, (float)Math.sin(-0.035), 0f);	// roughly 2 degrees
	    		} else if (selectZ){
	    			Q = new Quaternion((float)Math.cos(-0.035), 0f, 0f, (float)Math.sin(-0.035));	// roughly 2 degrees
	    		}
	    		switch (testCase){
				case 0:
					scene1_sphere.rotate(Q);
					scene1_box.rotate(Q);
					scene1_torus.rotate(Q);
					scene1_cylinder.rotate(Q);
					scene1Lights.in_light.rotate(Q);
					scene1Lights.sp_light.rotate(Q);
					break;
				case 1:
					scene2_ellipsoid.rotate(Q);
					scene2_torus.rotate(Q);
					scene2_cylinder.rotate(Q);
					scene2Lights.in_light.rotate(Q);
					scene2Lights.sp_light.rotate(Q);
					break;
				case 2:
					scene3_ellipsoid1.rotate(Q);
					scene3_torus1.rotate(Q);
					scene3_cylinder1.rotate(Q);
					scene3_ellipsoid2.rotate(Q);
					scene3_torus2.rotate(Q);
					scene3_cylinder2.rotate(Q);
					scene3_ellipsoid3.rotate(Q);
					scene3_torus3.rotate(Q);
					scene3_cylinder3.rotate(Q);
					scene3Lights.in_light.rotate(Q);
					scene3Lights.sp_light.rotate(Q);
					break;
				}
	    	}
	    	else if (doTranslation){
	    		if (selectX){
	    			switch (testCase){
					case 0:
						scene1_sphere.translate(-2,0,0);
						scene1_box.translate(-2,0,0);
						scene1_torus.translate(-2,0,0);
						scene1_cylinder.translate(-2,0,0);
						scene1Lights.po_light.translate(-2,0,0);
						scene1Lights.sp_light.translate(-2,0,0);
						break;
					case 1:
						scene2_ellipsoid.translate(-2,0,0);
						scene2_torus.translate(-2,0,0);
						scene2_cylinder.translate(-2,0,0);
						scene2Lights.po_light.translate(-2,0,0);
						scene2Lights.sp_light.translate(-2,0,0);
						break;
					case 2:
						scene3_ellipsoid1.translate(-2,0,0);
						scene3_torus1.translate(-2,0,0);
						scene3_cylinder1.translate(-2,0,0);
						scene3_ellipsoid2.translate(-2,0,0);
						scene3_torus2.translate(-2,0,0);
						scene3_cylinder2.translate(-2,0,0);
						scene3_ellipsoid3.translate(-2,0,0);
						scene3_torus3.translate(-2,0,0);
						scene3_cylinder3.translate(-2,0,0);
						scene3Lights.po_light.translate(-2,0,0);
						scene3Lights.sp_light.translate(-2,0,0);
						break;
					}
	    		} else if (selectY){
	    			switch (testCase){
					case 0:
						scene1_sphere.translate(0,-2,0);
						scene1_box.translate(0,-2,0);
						scene1_torus.translate(0,-2,0);
						scene1_cylinder.translate(0,-2,0);
						scene1Lights.po_light.translate(0,-2,0);
						scene1Lights.sp_light.translate(0,-2,0);
						break;
					case 1:
						scene2_ellipsoid.translate(0,-2,0);
						scene2_torus.translate(0,-2,0);
						scene2_cylinder.translate(0,-2,0);
						scene2Lights.po_light.translate(0,-2,0);
						scene2Lights.sp_light.translate(0,-2,0);
						break;
					case 2:
						scene3_ellipsoid1.translate(0,-2,0);
						scene3_torus1.translate(0,-2,0);
						scene3_cylinder1.translate(0,-2,0);
						scene3_ellipsoid2.translate(0,-2,0);
						scene3_torus2.translate(0,-2,0);
						scene3_cylinder2.translate(0,-2,0);
						scene3_ellipsoid3.translate(0,-2,0);
						scene3_torus3.translate(0,-2,0);
						scene3_cylinder3.translate(0,-2,0);
						scene3Lights.po_light.translate(0,-2,0);
						scene3Lights.sp_light.translate(0,-2,0);
						break;
					}
	    		} else if (selectZ){
	    			switch (testCase){
					case 0:
						scene1_sphere.translate(0,0,-2);
						scene1_box.translate(0,0,-2);
						scene1_torus.translate(0,0,-2);
						scene1_cylinder.translate(0,0,-2);
						scene1Lights.po_light.translate(0,0,-2);
						scene1Lights.sp_light.translate(0,0,-2);
						break;
					case 1:
						scene2_ellipsoid.translate(0,0,-2);
						scene2_torus.translate(0,0,-2);
						scene2_cylinder.translate(0,0,-2);
						scene2Lights.po_light.translate(0,0,-2);
						scene2Lights.sp_light.translate(0,0,-2);
						break;
					case 2:
						scene3_ellipsoid1.translate(0,0,-2);
						scene3_torus1.translate(0,0,-2);
						scene3_cylinder1.translate(0,0,-2);
						scene3_ellipsoid2.translate(0,0,-2);
						scene3_torus2.translate(0,0,-2);
						scene3_cylinder2.translate(0,0,-2);
						scene3_ellipsoid3.translate(0,0,-2);
						scene3_torus3.translate(0,0,-2);
						scene3_cylinder3.translate(0,0,-2);
						scene3Lights.po_light.translate(0,0,-2);
						scene3Lights.sp_light.translate(0,0,-2);
						break;
					}
	    		}
	    	}
	    	drawTestCase();
	    	break;
	    default:
	        break;
	    }
	}

	public void keyReleased(KeyEvent key)
	{
		// deliberately left blank
	}

	//************************************************** 
	// MouseListener and MouseMotionListener Interfaces
	//************************************************** 
	public void mouseClicked(MouseEvent mouse)
	{
		// deliberately left blank
	}
	  public void mousePressed(MouseEvent mouse)
	  {
	    int button = mouse.getButton();
	    if ( button == MouseEvent.BUTTON1 )
	    {
	      last_x = mouse.getX();
	      last_y = mouse.getY();
	      rotate_world = true;
	    }
	  }

	  public void mouseReleased(MouseEvent mouse)
	  {
	    int button = mouse.getButton();
	    if ( button == MouseEvent.BUTTON1 )
	    {
	      rotate_world = false;
	    }
	  }

	public void mouseMoved( MouseEvent mouse)
	{
		// Deliberately left blank
	}

	/**
	   * Updates the rotation quaternion as the mouse is dragged.
	   * 
	   * @param mouse
	   *          The mouse drag event object.
	   */
	  public void mouseDragged(final MouseEvent mouse) {
	    if (this.rotate_world) {
	      // get the current position of the mouse
	      final int x = mouse.getX();
	      final int y = mouse.getY();

	      // get the change in position from the previous one
	      final int dx = x - this.last_x;
	      final int dy = y - this.last_y;

	      // create a unit vector in the direction of the vector (dy, dx, 0)
	      final float magnitude = (float)Math.sqrt(dx * dx + dy * dy);
	      if(magnitude > 0.0001)
	      {
	    	  // define axis perpendicular to (dx,-dy,0)
	    	  // use -y because origin is in upper lefthand corner of the window
	    	  final float[] axis = new float[] { -(float) (dy / magnitude),
	    			  (float) (dx / magnitude), 0 };

	    	  // calculate appropriate quaternion
	    	  final float viewing_delta = 3.1415927f / 180.0f;
	    	  final float s = (float) Math.sin(0.5f * viewing_delta);
	    	  final float c = (float) Math.cos(0.5f * viewing_delta);
	    	  final Quaternion Q = new Quaternion(c, s * axis[0], s * axis[1], s
	    			  * axis[2]);
	    	  this.viewing_quaternion = Q.multiply(this.viewing_quaternion);

	    	  // normalize to counteract acccumulating round-off error
	    	  this.viewing_quaternion.normalize();

	    	  // save x, y as last x, y
	    	  this.last_x = x;
	    	  this.last_y = y;
	          drawTestCase();
	      }
	    }

	  }
	  
	public void mouseEntered( MouseEvent mouse)
	{
		// Deliberately left blank
	}

	public void mouseExited( MouseEvent mouse)
	{
		// Deliberately left blank
	} 


	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}
	
	//************************************************** 
	// Test Cases
	// Dec 6, 2015 Jia Yao : three different scenes are included
	//************************************************** 
	
	private void scene1(){
		// A scene with a sphere, a box, a torus, and a cylinder
		Sphere3D sphere = scene1_sphere;
		Box3D box = scene1_box;
        Torus3D torus = scene1_torus;
        CappedCylinder3D cylinder = scene1_cylinder;
		
        sphere.reset();
        box.reset();
        torus.reset();
        cylinder.reset();
        
        //System.out.println(sphere.xOffset + " " + sphere.yOffset + " " + sphere.zOffset + " ");
        
		// view vector is defined along z axis
        // this example assumes simple othorgraphic projection
        // view vector is used in 
        //   (a) calculating specular lighting contribution
        //   (b) backface culling / backface rejection
        Vector3D view_vector = new Vector3D((float)0.0,(float)0.0,(float)1.0);
        
        // update flags for lights in the scene
        scene1Lights.updateFlags(useAmbient, useDiffuse, useSpecular, onLights);
        
        // call helper functions to draw the objects
        drawSphere(sphere, view_vector, scene1Lights);
        drawTorus(torus, view_vector, scene1Lights);
        drawBox(box, view_vector, scene1Lights);
        drawCylinder(cylinder, view_vector, scene1Lights);
	}
	
	private void scene2(){
		// A scene with an ellipsoid, a torus, and a cylinder
		Ellipsoid3D ellipsoid = scene2_ellipsoid;
        Torus3D torus = scene2_torus;
        CappedCylinder3D cylinder = scene2_cylinder;
        
        ellipsoid.reset();
        torus.reset();
        cylinder.reset();
		
		// view vector is defined along z axis
        // this example assumes simple othorgraphic projection
        // view vector is used in 
        //   (a) calculating specular lighting contribution
        //   (b) backface culling / backface rejection
        Vector3D view_vector = new Vector3D((float)0.0,(float)0.0,(float)1.0);
        
        // update flags for lights in the scene
        scene2Lights.updateFlags(useAmbient, useDiffuse, useSpecular, onLights);
        
        // call helper functions to draw the objects
        drawEllipsoid(ellipsoid, view_vector, scene2Lights);
        drawTorus(torus, view_vector, scene2Lights);
        drawCylinder(cylinder, view_vector, scene2Lights);
	}
	
	private void scene3(){
		// A scene with a row of ellipsoids, a row of torus, and a row of cylinders
		Ellipsoid3D ellipsoid1 = scene3_ellipsoid1;
		Ellipsoid3D ellipsoid2 = scene3_ellipsoid2;
		Ellipsoid3D ellipsoid3 = scene3_ellipsoid3;
		Torus3D torus1 = scene3_torus1;
		Torus3D torus2 = scene3_torus2;
		Torus3D torus3 = scene3_torus3;
		CappedCylinder3D cylinder1 = scene3_cylinder1;
		CappedCylinder3D cylinder2 = scene3_cylinder2;
		CappedCylinder3D cylinder3 = scene3_cylinder3;
		
		ellipsoid1.reset();
		ellipsoid2.reset();
		ellipsoid3.reset();
		torus1.reset();
		torus2.reset();
		torus3.reset();
		cylinder1.reset();
		cylinder2.reset();
		cylinder3.reset();
		
		// view vector is defined along z axis
        // this example assumes simple othorgraphic projection
        // view vector is used in 
        //   (a) calculating specular lighting contribution
        //   (b) backface culling / backface rejection
        Vector3D view_vector = new Vector3D((float)0.0,(float)0.0,(float)1.0);
        
        // update flags for lights in the scene
        scene3Lights.updateFlags(useAmbient, useDiffuse, useSpecular, onLights);
        
        // call helper functions to draw the objects
        drawEllipsoid(ellipsoid1, view_vector, scene3Lights);
        drawEllipsoid(ellipsoid2, view_vector, scene3Lights);
        drawEllipsoid(ellipsoid3, view_vector, scene3Lights);
        drawTorus(torus1, view_vector, scene3Lights);
        drawTorus(torus2, view_vector, scene3Lights);
        drawTorus(torus3, view_vector, scene3Lights);
        drawCylinder(cylinder1, view_vector, scene3Lights);
        drawCylinder(cylinder2, view_vector, scene3Lights);
        drawCylinder(cylinder3, view_vector, scene3Lights);
        
	}

	private void drawSphere(Sphere3D sphere, Vector3D view_vector, LightCollection light){
        
        // normal to the plane of a triangle
        // to be used in backface culling / backface rejection
        Vector3D triangle_normal = new Vector3D();
        
        // triangle mesh
        Mesh3D mesh = sphere.mesh;
        // rotate the surface's 3D mesh using quaternion
     	mesh.rotateMesh(viewing_quaternion, viewing_center);
     	
        int i, j;
        // temporary variables for triangle 3D vertices and 3D normals
     	Vector3D v0,v1, v2, n0, n1, n2;
     		
     	// projected triangle, with vertex colors
     	Point3D[] tri = {new Point3D(), new Point3D(), new Point3D()};
     	
     	for(i=0; i < Nsteps-1; ++i)
	    {
			for(j=0; j < Nsteps-1; ++j)
			{
				v0 = mesh.v[i][j];
				v1 = mesh.v[i][j+1];
				v2 = mesh.v[i+1][j+1];
				triangle_normal = computeTriangleNormal(v0,v1,v2);
				
				if(view_vector.dotProduct(triangle_normal) > 0.0)  // front-facing triangle?
				{	
					tri[0].x = (int)v0.x;
					tri[0].y = (int)v0.y;
					tri[0].z = (int)v0.z;
					tri[1].x = (int)v1.x;
					tri[1].y = (int)v1.y;
					tri[1].z = (int)v1.z;
					tri[2].x = (int)v2.x;
					tri[2].y = (int)v2.y;
					tri[2].z = (int)v2.z;
					
					if(doGouraud)  // Gouraud shading
					{
						// vertex colors
						n0 = mesh.n[i][j];
						n1 = mesh.n[i][j+1];
						n2 = mesh.n[i+1][j+1];
						tri[0].c = light.applyLight(mats[0], view_vector, n0, v0);
						tri[1].c = light.applyLight(mats[0], view_vector, n1, v1);
						tri[2].c = light.applyLight(mats[0], view_vector, n2, v2);
						SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
					}
					else if (doPhong)	// Phong shading
					{
						// need the vertices and the normals
						n0 = mesh.n[i][j];
						n1 = mesh.n[i][j+1];
						n2 = mesh.n[i+1][j+1];
						SketchBase.drawTrianglePhong(buff, depth, light, mats[0], view_vector, tri[0], tri[1], tri[2], n0, n1, n2);
					}
					else // Flat shading: use the normal to the triangle itself
					{
						n2 = n1 = n0 =  triangle_normal;
						tri[2].c = tri[1].c = tri[0].c = light.applyLight(mats[0], view_vector, triangle_normal, v0);
						SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
					}
     
				}
				
				v0 = mesh.v[i][j];
				v1 = mesh.v[i+1][j+1];
				v2 = mesh.v[i+1][j];
				triangle_normal = computeTriangleNormal(v0,v1,v2);
				
				if(view_vector.dotProduct(triangle_normal) > 0.0)  // front-facing triangle?
				{	
					tri[0].x = (int)v0.x;
					tri[0].y = (int)v0.y;
					tri[0].z = (int)v0.z;
					tri[1].x = (int)v1.x;
					tri[1].y = (int)v1.y;
					tri[1].z = (int)v1.z;
					tri[2].x = (int)v2.x;
					tri[2].y = (int)v2.y;
					tri[2].z = (int)v2.z;
					
					if(doGouraud)  // Gouraud shading
					{
						// vertex colors
						n0 = mesh.n[i][j];
						n1 = mesh.n[i+1][j+1];
						n2 = mesh.n[i+1][j];
						tri[0].c = light.applyLight(mats[0], view_vector, n0, v0);
						tri[1].c = light.applyLight(mats[0], view_vector, n1, v1);
						tri[2].c = light.applyLight(mats[0], view_vector, n2, v2);
						SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
					}
					else if (doPhong)	// Phong shading
					{
						// need the vertices and the normals
						n0 = mesh.n[i][j];
						n1 = mesh.n[i+1][j+1];
						n2 = mesh.n[i+1][j];
						SketchBase.drawTrianglePhong(buff, depth, light, mats[0], view_vector, tri[0], tri[1], tri[2], n0, n1, n2);
					}
					else // Flat shading: use the normal to the triangle itself
					{
						n2 = n1 = n0 =  triangle_normal;
						tri[2].c = tri[1].c = tri[0].c = light.applyLight(mats[0], view_vector, triangle_normal, v0);
						SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
					}      
				}
			}
	    }
     	
	}

	private void drawEllipsoid(Ellipsoid3D ellipsoid, Vector3D view_vector, LightCollection light){
        
        // normal to the plane of a triangle
        // to be used in backface culling / backface rejection
        Vector3D triangle_normal = new Vector3D();
        
        // triangle mesh
        Mesh3D mesh = ellipsoid.mesh;
        // rotate the surface's 3D mesh using quaternion
     	mesh.rotateMesh(viewing_quaternion, viewing_center);
     	
        int i, j;
        // temporary variables for triangle 3D vertices and 3D normals
     	Vector3D v0,v1, v2, n0, n1, n2;
     		
     	// projected triangle, with vertex colors
     	Point3D[] tri = {new Point3D(), new Point3D(), new Point3D()};
     	
     	for(i=0; i < Nsteps-1; ++i)
	    {
			for(j=0; j < Nsteps-1; ++j)
			{
				v0 = mesh.v[i][j];
				v1 = mesh.v[i][j+1];
				v2 = mesh.v[i+1][j+1];
				triangle_normal = computeTriangleNormal(v0,v1,v2);
				
				if(view_vector.dotProduct(triangle_normal) > 0.0)  // front-facing triangle?
				{	
					tri[0].x = (int)v0.x;
					tri[0].y = (int)v0.y;
					tri[0].z = (int)v0.z;
					tri[1].x = (int)v1.x;
					tri[1].y = (int)v1.y;
					tri[1].z = (int)v1.z;
					tri[2].x = (int)v2.x;
					tri[2].y = (int)v2.y;
					tri[2].z = (int)v2.z;
					
					if(doGouraud)  // Gouraud shading
					{
						// vertex colors
						n0 = mesh.n[i][j];
						n1 = mesh.n[i][j+1];
						n2 = mesh.n[i+1][j+1];
						tri[0].c = light.applyLight(mats[1], view_vector, n0, v0);
						tri[1].c = light.applyLight(mats[1], view_vector, n1, v1);
						tri[2].c = light.applyLight(mats[1], view_vector, n2, v2);
						SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
					}
					else if (doPhong)	// Phong shading
					{
						// need the vertices and the normals
						n0 = mesh.n[i][j];
						n1 = mesh.n[i][j+1];
						n2 = mesh.n[i+1][j+1];
						tri[0].c = light.applyLight(mats[1], view_vector, n0, v0);
						tri[1].c = light.applyLight(mats[1], view_vector, n1, v1);
						tri[2].c = light.applyLight(mats[1], view_vector, n2, v2);
						SketchBase.drawTrianglePhong(buff, depth, light, mats[1], view_vector, tri[0], tri[1], tri[2], n0, n1, n2);
					}
					else // Flat shading: use the normal to the triangle itself
					{
						n2 = n1 = n0 =  triangle_normal;
						tri[2].c = tri[1].c = tri[0].c = light.applyLight(mats[1], view_vector, triangle_normal, v0);
						SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
					}
     
				}
				
				v0 = mesh.v[i][j];
				v1 = mesh.v[i+1][j+1];
				v2 = mesh.v[i+1][j];
				triangle_normal = computeTriangleNormal(v0,v1,v2);
				
				if(view_vector.dotProduct(triangle_normal) > 0.0)  // front-facing triangle?
				{	
					tri[0].x = (int)v0.x;
					tri[0].y = (int)v0.y;
					tri[0].z = (int)v0.z;
					tri[1].x = (int)v1.x;
					tri[1].y = (int)v1.y;
					tri[1].z = (int)v1.z;
					tri[2].x = (int)v2.x;
					tri[2].y = (int)v2.y;
					tri[2].z = (int)v2.z;
					
					if(doGouraud)  // Gouraud shading
					{
						// vertex colors
						n0 = mesh.n[i][j];
						n1 = mesh.n[i+1][j+1];
						n2 = mesh.n[i+1][j];
						tri[0].c = light.applyLight(mats[1], view_vector, n0, v0);
						tri[1].c = light.applyLight(mats[1], view_vector, n1, v1);
						tri[2].c = light.applyLight(mats[1], view_vector, n2, v2);
						SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
					}
					else if (doPhong)	// Phong shading
					{
						// need the vertices and the normals
						n0 = mesh.n[i][j];
						n1 = mesh.n[i+1][j+1];
						n2 = mesh.n[i+1][j];
						tri[0].c = light.applyLight(mats[1], view_vector, n0, v0);
						tri[1].c = light.applyLight(mats[1], view_vector, n1, v1);
						tri[2].c = light.applyLight(mats[1], view_vector, n2, v2);
						SketchBase.drawTrianglePhong(buff, depth, light, mats[1], view_vector, tri[0], tri[1], tri[2], n0, n1, n2);
					}
					else // Flat shading: use the normal to the triangle itself
					{
						n2 = n1 = n0 =  triangle_normal;
						tri[2].c = tri[1].c = tri[0].c = light.applyLight(mats[1], view_vector, triangle_normal, v0);
						SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
					}      
				}
			}
	    }
     	
	}
	
	private void drawTorus(Torus3D torus, Vector3D view_vector, LightCollection light){
        
        // normal to the plane of a triangle
        // to be used in backface culling / backface rejection
        Vector3D triangle_normal = new Vector3D();
        
        // triangle mesh
        Mesh3D mesh = torus.mesh;
        // rotate the surface's 3D mesh using quaternion
     	mesh.rotateMesh(viewing_quaternion, viewing_center);
     	
        int i, j;
        // temporary variables for triangle 3D vertices and 3D normals
     	Vector3D v0,v1, v2, n0, n1, n2;
     		
     	// projected triangle, with vertex colors
     	Point3D[] tri = {new Point3D(), new Point3D(), new Point3D()};
     	
     	for(i=0; i < Nsteps-1; ++i)
	    {
			for(j=0; j < Nsteps-1; ++j)
			{
				v0 = mesh.v[i][j];
				v1 = mesh.v[i][j+1];
				v2 = mesh.v[i+1][j+1];
				triangle_normal = computeTriangleNormal(v0,v1,v2);
				
				if(view_vector.dotProduct(triangle_normal) > 0.0)  // front-facing triangle?
				{	
					tri[0].x = (int)v0.x;
					tri[0].y = (int)v0.y;
					tri[0].z = (int)v0.z;
					tri[1].x = (int)v1.x;
					tri[1].y = (int)v1.y;
					tri[1].z = (int)v1.z;
					tri[2].x = (int)v2.x;
					tri[2].y = (int)v2.y;
					tri[2].z = (int)v2.z;
					
					if(doGouraud)  // Gouraud shading
					{
						// vertex colors
						n0 = mesh.n[i][j];
						n1 = mesh.n[i][j+1];
						n2 = mesh.n[i+1][j+1];
						tri[0].c = light.applyLight(mats[2], view_vector, n0, v0);
						tri[1].c = light.applyLight(mats[2], view_vector, n1, v1);
						tri[2].c = light.applyLight(mats[2], view_vector, n2, v2);
						SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
					}
					else if (doPhong)	// Phong shading
					{
						// need the vertices and the normals
						n0 = mesh.n[i][j];
						n1 = mesh.n[i][j+1];
						n2 = mesh.n[i+1][j+1];
						tri[0].c = light.applyLight(mats[2], view_vector, n0, v0);
						tri[1].c = light.applyLight(mats[2], view_vector, n1, v1);
						tri[2].c = light.applyLight(mats[2], view_vector, n2, v2);
						SketchBase.drawTrianglePhong(buff, depth, light, mats[2], view_vector, tri[0], tri[1], tri[2], n0, n1, n2);
					}
					else // Flat shading: use the normal to the triangle itself
					{
						n2 = n1 = n0 =  triangle_normal;
						tri[2].c = tri[1].c = tri[0].c = light.applyLight(mats[2], view_vector, triangle_normal, v0);
						SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
					}
     
				}
				
				v0 = mesh.v[i][j];
				v1 = mesh.v[i+1][j+1];
				v2 = mesh.v[i+1][j];
				triangle_normal = computeTriangleNormal(v0,v1,v2);
				
				if(view_vector.dotProduct(triangle_normal) > 0.0)  // front-facing triangle?
				{	
					tri[0].x = (int)v0.x;
					tri[0].y = (int)v0.y;
					tri[0].z = (int)v0.z;
					tri[1].x = (int)v1.x;
					tri[1].y = (int)v1.y;
					tri[1].z = (int)v1.z;
					tri[2].x = (int)v2.x;
					tri[2].y = (int)v2.y;
					tri[2].z = (int)v2.z;
					
					if(doGouraud)  // Gouraud shading
					{
						// vertex colors
						n0 = mesh.n[i][j];
						n1 = mesh.n[i+1][j+1];
						n2 = mesh.n[i+1][j];
						tri[0].c = light.applyLight(mats[2], view_vector, n0, v0);
						tri[1].c = light.applyLight(mats[2], view_vector, n1, v1);
						tri[2].c = light.applyLight(mats[2], view_vector, n2, v2);
						SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
					}
					else if (doPhong)	// Phong shading
					{
						// need the vertices and the normals
						n0 = mesh.n[i][j];
						n1 = mesh.n[i+1][j+1];
						n2 = mesh.n[i+1][j];
						tri[0].c = light.applyLight(mats[2], view_vector, n0, v0);
						tri[1].c = light.applyLight(mats[2], view_vector, n1, v1);
						tri[2].c = light.applyLight(mats[2], view_vector, n2, v2);
						SketchBase.drawTrianglePhong(buff, depth, light, mats[2], view_vector, tri[0], tri[1], tri[2], n0, n1, n2);
					}
					else // Flat shading: use the normal to the triangle itself
					{
						n2 = n1 = n0 =  triangle_normal;
						tri[2].c = tri[1].c = tri[0].c = light.applyLight(mats[2], view_vector, triangle_normal, v0);
						SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
					}      
				}
			}
	    }
     	
	}

	private void drawBox(Box3D box, Vector3D view_vector, LightCollection light){
        
        // normal to the plane of a triangle
        // to be used in backface culling / backface rejection
        Vector3D triangle_normal = new Vector3D();
        
        // triangle mesh
        Mesh3D mesh = box.mesh;
        // rotate the surface's 3D mesh using quaternion
     	mesh.rotateMesh(viewing_quaternion, viewing_center);
     	
        int i, j, k, face_i;
        boolean reverse_normal = false;
        // temporary variables for triangle 3D vertices and 3D normals
     	Vector3D v0,v1, v2, n0, n1, n2;
     		
     	// projected triangle, with vertex colors
     	Point3D[] tri = {new Point3D(), new Point3D(), new Point3D()};
     	
     	for(k=0; k < 6; k++){
 			if (k == 0 || k == 2 || k == 5){
				reverse_normal = true;
			} else {
				reverse_normal = false;
			}
			for(i=0; i < Nsteps-1; ++i)
			{
				face_i = i + k*Nsteps;
				for(j=0; j < Nsteps-1; ++j)
				{
					
					v0 = mesh.v[face_i][j];
					v1 = mesh.v[face_i][j+1];
					v2 = mesh.v[face_i+1][j+1];
					triangle_normal = computeTriangleNormal(v0,v1,v2);
					if (reverse_normal){
						triangle_normal.x*=-1;
						triangle_normal.y*=-1;
						triangle_normal.z*=-1;
					}
					if(view_vector.dotProduct(triangle_normal) > 0.0)  // front-facing triangle?
					{	
						tri[0].x = (int)v0.x;
						tri[0].y = (int)v0.y;
						tri[0].z = (int)v0.z;
						tri[1].x = (int)v1.x;
						tri[1].y = (int)v1.y;
						tri[1].z = (int)v1.z;
						tri[2].x = (int)v2.x;
						tri[2].y = (int)v2.y;
						tri[2].z = (int)v2.z;
						
						if(doGouraud)  // Gouraud shading
						{
							// vertex colors
							n0 = mesh.n[face_i][j];
							n1 = mesh.n[face_i][j+1];
							n2 = mesh.n[face_i+1][j+1];
							tri[0].c = light.applyLight(mats[1], view_vector, n0, v0);
							tri[1].c = light.applyLight(mats[1], view_vector, n1, v1);
							tri[2].c = light.applyLight(mats[1], view_vector, n2, v2);
							SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
						}
						else if (doPhong)	// Phong shading
						{
							// need the vertices and the normals
							n0 = mesh.n[face_i][j];
							n1 = mesh.n[face_i][j+1];
							n2 = mesh.n[face_i+1][j+1];
							tri[0].c = light.applyLight(mats[1], view_vector, n0, v0);
							tri[1].c = light.applyLight(mats[1], view_vector, n1, v1);
							tri[2].c = light.applyLight(mats[1], view_vector, n2, v2);
							SketchBase.drawTrianglePhong(buff, depth, light, mats[1], view_vector, tri[0], tri[1], tri[2], n0, n1, n2);
						}
						else // Flat shading: use the normal to the triangle itself
						{
							n2 = n1 = n0 =  triangle_normal;
							tri[2].c = tri[1].c = tri[0].c = light.applyLight(mats[1], view_vector, triangle_normal, v0);
							SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
						}      
					}
					
					v0 = mesh.v[face_i][j];
					v1 = mesh.v[face_i+1][j+1];
					v2 = mesh.v[face_i+1][j];
					triangle_normal = computeTriangleNormal(v0,v1,v2);
					if (reverse_normal){
						triangle_normal.x*=-1;
						triangle_normal.y*=-1;
						triangle_normal.z*=-1;
					}
					if(view_vector.dotProduct(triangle_normal) > 0.0)  // front-facing triangle?
					{
						tri[0].x = (int)v0.x;
						tri[0].y = (int)v0.y;
						tri[0].z = (int)v0.z;
						tri[1].x = (int)v1.x;
						tri[1].y = (int)v1.y;
						tri[1].z = (int)v1.z;
						tri[2].x = (int)v2.x;
						tri[2].y = (int)v2.y;
						tri[2].z = (int)v2.z;
						
						if(doGouraud)  // Gouraud shading
						{
							// vertex colors
							n0 = mesh.n[face_i][j];
							n1 = mesh.n[face_i+1][j+1];
							n2 = mesh.n[face_i+1][j];
							tri[0].c = light.applyLight(mats[1], view_vector, n0, v0);
							tri[1].c = light.applyLight(mats[1], view_vector, n1, v1);
							tri[2].c = light.applyLight(mats[1], view_vector, n2, v2);
							SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
						}
						else if (doPhong)	// Phong shading
						{
							// need the vertices and the normals
							n0 = mesh.n[face_i][j];
							n1 = mesh.n[face_i+1][j+1];
							n2 = mesh.n[face_i+1][j];
							tri[0].c = light.applyLight(mats[1], view_vector, n0, v0);
							tri[1].c = light.applyLight(mats[1], view_vector, n1, v1);
							tri[2].c = light.applyLight(mats[1], view_vector, n2, v2);
							SketchBase.drawTrianglePhong(buff, depth, light, mats[1], view_vector, tri[0], tri[1], tri[2], n0, n1, n2);
						}
						else // Flat shading: use the normal to the triangle itself
						{
							n2 = n1 = n0 =  triangle_normal;
							tri[2].c = tri[1].c = tri[0].c = light.applyLight(mats[1], view_vector, triangle_normal, v0);
							SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
						}       
					}
				}
			}
 		}
     	
	}
	
	private void drawCylinder(CappedCylinder3D cylinder, Vector3D view_vector, LightCollection light){
        
        // normal to the plane of a triangle
        // to be used in backface culling / backface rejection
        Vector3D triangle_normal = new Vector3D();
        
        // triangle mesh
        Mesh3D mesh = cylinder.mesh, end_mesh = cylinder.end_mesh;
        // rotate the surface's 3D mesh using quaternion
     	mesh.rotateMesh(viewing_quaternion, viewing_center);
     	end_mesh.rotateMesh(viewing_quaternion, viewing_center);
     	
        int i, j;
        // temporary variables for triangle 3D vertices and 3D normals
     	Vector3D v0,v1, v2, n0, n1, n2;
     		
     	// projected triangle, with vertex colors
     	Point3D[] tri = {new Point3D(), new Point3D(), new Point3D()};
     	
     	for(i=0; i < Nsteps-1; ++i)
		{
			for(j=0; j < Nsteps-1; ++j)
			{
				v0 = mesh.v[i][j];
				v1 = mesh.v[i][j+1];
				v2 = mesh.v[i+1][j+1];
				triangle_normal = computeTriangleNormal(v0,v1,v2);
				
				if(view_vector.dotProduct(triangle_normal) > 0.0)  // front-facing triangle?
				{	
					tri[0].x = (int)v0.x;
					tri[0].y = (int)v0.y;
					tri[0].z = (int)v0.z;
					tri[1].x = (int)v1.x;
					tri[1].y = (int)v1.y;
					tri[1].z = (int)v1.z;
					tri[2].x = (int)v2.x;
					tri[2].y = (int)v2.y;
					tri[2].z = (int)v2.z;
					
					if(doGouraud)  // Gouraud shading
					{
						// vertex colors
						n0 = mesh.n[i][j];
						n1 = mesh.n[i][j+1];
						n2 = mesh.n[i+1][j+1];
						tri[0].c = light.applyLight(mats[3], view_vector, n0, v0);
						tri[1].c = light.applyLight(mats[3], view_vector, n1, v1);
						tri[2].c = light.applyLight(mats[3], view_vector, n2, v2);
						SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
					}
					else if (doPhong)	// Phong shading
					{
						// need the vertices and the normals
						n0 = mesh.n[i][j];
						n1 = mesh.n[i][j+1];
						n2 = mesh.n[i+1][j+1];
						tri[0].c = light.applyLight(mats[3], view_vector, n0, v0);
						tri[1].c = light.applyLight(mats[3], view_vector, n1, v1);
						tri[2].c = light.applyLight(mats[3], view_vector, n2, v2);
						SketchBase.drawTrianglePhong(buff, depth, light, mats[3], view_vector, tri[0], tri[1], tri[2], n0, n1, n2);
					}
					else // Flat shading: use the normal to the triangle itself
					{
						n2 = n1 = n0 =  triangle_normal;
						tri[2].c = tri[1].c = tri[0].c = light.applyLight(mats[3], view_vector, triangle_normal, v0);
						SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
					}
				}
				
				v0 = mesh.v[i][j];
				v1 = mesh.v[i+1][j+1];
				v2 = mesh.v[i+1][j];
				triangle_normal = computeTriangleNormal(v0,v1,v2);
				
				if(view_vector.dotProduct(triangle_normal) > 0.0)  // front-facing triangle?
				{	
					tri[0].x = (int)v0.x;
					tri[0].y = (int)v0.y;
					tri[0].z = (int)v0.z;
					tri[1].x = (int)v1.x;
					tri[1].y = (int)v1.y;
					tri[1].z = (int)v1.z;
					tri[2].x = (int)v2.x;
					tri[2].y = (int)v2.y;
					tri[2].z = (int)v2.z;
					
					if(doGouraud)  // Gouraud shading
					{
						// vertex colors
						n0 = mesh.n[i][j];
						n1 = mesh.n[i+1][j+1];
						n2 = mesh.n[i+1][j];
						tri[0].c = light.applyLight(mats[3], view_vector, n0, v0);
						tri[1].c = light.applyLight(mats[3], view_vector, n1, v1);
						tri[2].c = light.applyLight(mats[3], view_vector, n2, v2);
						SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
					}
					else if (doPhong)	// Phong shading
					{
						// need the vertices and the normals
						n0 = mesh.n[i][j];
						n1 = mesh.n[i+1][j+1];
						n2 = mesh.n[i+1][j];
						tri[0].c = light.applyLight(mats[3], view_vector, n0, v0);
						tri[1].c = light.applyLight(mats[3], view_vector, n1, v1);
						tri[2].c = light.applyLight(mats[3], view_vector, n2, v2);
						SketchBase.drawTrianglePhong(buff, depth, light, mats[3], view_vector, tri[0], tri[1], tri[2], n0, n1, n2);
					}
					else // Flat shading: use the normal to the triangle itself
					{
						n2 = n1 = n0 =  triangle_normal;
						tri[2].c = tri[1].c = tri[0].c = light.applyLight(mats[3], view_vector, triangle_normal, v0);
						SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
					}
				}
			}

			// draw end caps
			v0 = end_mesh.v[0][0];
			v1 = end_mesh.v[i+1][0];
			v2 = end_mesh.v[i+2][0];
			triangle_normal = computeTriangleNormal(v0,v1,v2);
			
			if(view_vector.dotProduct(triangle_normal) > 0.0)  // front-facing triangle?
			{	
				tri[0].x = (int)v0.x;
				tri[0].y = (int)v0.y;
				tri[0].z = (int)v0.z;
				tri[1].x = (int)v1.x;
				tri[1].y = (int)v1.y;
				tri[1].z = (int)v1.z;
				tri[2].x = (int)v2.x;
				tri[2].y = (int)v2.y;
				tri[2].z = (int)v2.z;
				
				if(doGouraud)  // Gouraud shading
				{
					// vertex colors
					n0 = end_mesh.n[0][0];
					n1 = end_mesh.n[i+1][0];
					n2 = end_mesh.n[i+2][0];
					tri[0].c = light.applyLight(mats[3], view_vector, n0, v0);
					tri[1].c = light.applyLight(mats[3], view_vector, n1, v1);
					tri[2].c = light.applyLight(mats[3], view_vector, n2, v2);
					SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
				}
				else if (doPhong)	// Phong shading
				{
					// need the vertices and the normals
					n0 = end_mesh.n[0][0];
					n1 = end_mesh.n[i+1][0];
					n2 = end_mesh.n[i+1][0];
					tri[0].c = light.applyLight(mats[3], view_vector, n0, v0);
					tri[1].c = light.applyLight(mats[3], view_vector, n1, v1);
					tri[2].c = light.applyLight(mats[3], view_vector, n2, v2);
					SketchBase.drawTrianglePhong(buff, depth, light, mats[3], view_vector, tri[0], tri[1], tri[2], n0, n1, n2);
				}
				else // Flat shading: use the normal to the triangle itself
				{
					n2 = n1 = n0 =  triangle_normal;
					tri[2].c = tri[1].c = tri[0].c = light.applyLight(mats[3], view_vector, triangle_normal, v0);
					SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
				} 
			}
			
			v0 = end_mesh.v[0][1];
			v1 = end_mesh.v[i+1][1];
			v2 = end_mesh.v[i+2][1];
			triangle_normal = computeTriangleNormal(v0,v1,v2);
			triangle_normal.x*=-1;
			triangle_normal.y*=-1;
			triangle_normal.z*=-1;
			
			if(view_vector.dotProduct(triangle_normal) > 0.0)  // front-facing triangle?
			{	
				tri[0].x = (int)v0.x;
				tri[0].y = (int)v0.y;
				tri[0].z = (int)v0.z;
				tri[1].x = (int)v1.x;
				tri[1].y = (int)v1.y;
				tri[1].z = (int)v1.z;
				tri[2].x = (int)v2.x;
				tri[2].y = (int)v2.y;
				tri[2].z = (int)v2.z;
				
				if(doGouraud)  // Gouraud shading
				{
					// vertex colors
					n0 = end_mesh.n[0][1];
					n1 = end_mesh.n[i+1][1];
					n2 = end_mesh.n[i+2][1];
					tri[0].c = light.applyLight(mats[3], view_vector, n0, v0);
					tri[1].c = light.applyLight(mats[3], view_vector, n1, v1);
					tri[2].c = light.applyLight(mats[3], view_vector, n2, v2);
					SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
				}
				else if (doPhong)	// Phong shading
				{
					// need the vertices and the normals
					n0 = end_mesh.n[0][1];
					n1 = end_mesh.n[i+1][1];
					n2 = end_mesh.n[i+1][1];
					tri[0].c = light.applyLight(mats[3], view_vector, n0, v0);
					tri[1].c = light.applyLight(mats[3], view_vector, n1, v1);
					tri[2].c = light.applyLight(mats[3], view_vector, n2, v2);
					SketchBase.drawTrianglePhong(buff, depth, light, mats[3], view_vector, tri[0], tri[1], tri[2], n0, n1, n2);
				}
				else // Flat shading: use the normal to the triangle itself
				{
					n2 = n1 = n0 =  triangle_normal;
					tri[2].c = tri[1].c = tri[0].c = light.applyLight(mats[3], view_vector, triangle_normal, v0);
					SketchBase.drawTriangle(buff, depth, tri[0],tri[1],tri[2],doGouraud); 
					
				} 
				
				//buff.setRGB((int)v0.x, (int)(buff.getHeight()-v0.y-1), light_color.getRGB_int());
				//buff.setRGB((int)v1.x, (int)(buff.getHeight()-v1.y-1), light_color.getRGB_int());
				//buff.setRGB((int)v2.x, (int)(buff.getHeight()-v2.y-1), light_color.getRGB_int());
			}
		}
     	
	}

	// helper method that computes the unit normal to the plane of the triangle
	// degenerate triangles yield normal that is numerically zero
	private Vector3D computeTriangleNormal(Vector3D v0, Vector3D v1, Vector3D v2)
	{
		Vector3D e0 = v1.minus(v2);
		Vector3D e1 = v0.minus(v2);
		Vector3D norm = e0.crossProduct(e1);
		
		if(norm.magnitude()>0.000001)
			norm.normalize();
		else 	// detect degenerate triangle and set its normal to zero
			norm.set((float)0.0,(float)0.0,(float)0.0);

		return norm;
	}

}