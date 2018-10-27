package roundoid.classes.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.graphics.PointF;
import java.util.Vector;

import roundoid.classes.game.R;
import roundoid.classes.game.common.RawResourceReader;
import roundoid.classes.game.common.ShaderHelper;
import roundoid.classes.game.common.TextureHelper;

/**
 * This class implements our custom renderer. Note that the GL10 parameter passed in is unused for OpenGL ES 2.0
 * renderers -- the static class GLES20 is used instead.
 */
public class RoundoidRenderer implements GLSurfaceView.Renderer 
{	
	
	///Game cycle management
	/////////////////////////////////////////////////////////////////////////
	// desired fps
	private final static int    MAX_FPS = 50;
	// maximum number of frames to be skipped
	private final static int    MAX_FRAME_SKIPS = 5;
	// the frame period
	private final static int    FRAME_PERIOD = 1000 / MAX_FPS;  
	long beginTime;     // the time when the cycle begun
	long timeDiff;      // the time it took for the cycle to execute
	int sleepTime;      // ms to sleep (<0 if we're behind)
	int framesSkipped;  // number of frames being skipped 
	/////////////////////////////////////////////////////////////////////////
	
	//fps calculation variables
	/////////////////////////////////////////////////////////////////////////
	// Stuff for stats */
	private DecimalFormat df = new DecimalFormat("0.##");  // 2 dp
	// we'll be reading the stats every second
	private final static int    STAT_INTERVAL = 1000; //ms
	// the average will be calculated by storing
	// the last n FPSs
	private final static int    FPS_HISTORY_NR = 10;
	// last time the status was stored
	private long lastStatusStore = 0;
	// the status time counter
	private long statusIntervalTimer    = 0l;
	// number of frames skipped since the game started
	private long totalFramesSkipped         = 0l;
	// number of frames skipped in a store cycle (1 sec)
	private long framesSkippedPerStatCycle  = 0l;
	
	// number of rendered frames in an interval
	private int frameCountPerStatCycle = 0;
	private long totalFrameCount = 0l;
	// the last FPS values
	private double  fpsStore[];
	// the number of times the stat has been read
	private long    statsCount = 0;
	// the average FPS since the game started
	private double  averageFps = 0.0;
	//Actual Fps
	private int actualFps = 0;
	/////////////////////////////////////////////////////////////////////////
	
	//Collision Detection Variables
	/////////////////////////////////////////////////////////////////////////
	//Paddles
	
	double d1;
	double d2;	
	double d3;
	double d4;	
	double u;		
	double a;
	double b; 			   
	double c; 
	float deltax;
	float deltay;
	float length;
	
	
	float fNx; 			
	float fNy; 			
	float VdotN;
	
	//Bricks
	float circleDistancex;
	float circleDistancey;
	float cornerDistance_sq;
	/////////////////////////////////////////////////////////////////////////
	
	
	int screenWidth;
	int screenHeight;
	int[] viewport = new int[4];
	boolean rendermainmenu = false;
	float halfx;
	long backbuttontimer;
	long ballpaddlecollisiondetectbuffer;
	int iDisplayIndex = 0;
	
		
	/** Used for debug logs. */
	private static final String TAG = "Roundoid";
		
	TimerDisplayHandler tdh;
	LifeCountDisplayHandler lcdh = new LifeCountDisplayHandler();  
	BackButtonDisplayHandler bbdh;
	BackGroundDisplayHandler bgdh; 
	BoosterBarDisplayHandler bbdh1 = new BoosterBarDisplayHandler();
	MainMenuDisplayHandler mmdh; 
	SelectWorldDisplayHandler swdh; 
	
	long millis;
	int seconds;
	int minutes;
	
	boolean bShouldCollDetect = true;
	
	////////////////////////////////
	float[] fLeftPaddleTop = new float[4];
	float[] fLeftPaddleBottom = new float[4];
	float fOffSet = 0.95f;
	boolean qwer = false;
	final float fBrickHeigtWidth = 0.05f;
	
	Brick[] brickarray = new Brick[64];
	
	float[] outVector1 = new float[4];   
	float[] outVector2 = new float[4]; 
	float[] outVector3 = new float[4];  
	float[] outVectortestTop = new float[4];	
	float[] outVectortestBottom = new float[4];
	
	float fPaddleHeight = 0.16f;
	float fPaddleWidth = 0.026f;
	float fBallWidthHeight = 0.05f;	
	
	long starttime = 0;
	
	PointF pointF;
	
	BrickHandler bricks = new BrickHandler(fBrickHeigtWidth);
	
	boolean bIntersects;	
	
	Paddle leftPaddle = new Paddle();
	Paddle rightPaddle = new Paddle();
	Paddle topPaddle = new Paddle();
	Paddle bottomPaddle = new Paddle();
	
	private final Context mActivityContext;
	
	/**
	 * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
	 * of being located at the center of the universe) to world space.
	 */
	private float[] mModelMatrix = new float[16];

	/**
	 * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
	 * it positions things relative to our eye.
	 */
	private float[] mViewMatrix = new float[16];

	/** Store the projection matrix. This is used to project the scene onto a 2D viewport. */
	private float[] mProjectionMatrix = new float[16];
	
	/** Allocate storage for the final combined matrix. This will be passed into the shader program. */
	private float[] mMVPMatrix = new float[16];
	
	/** Store the accumulated rotation. */
	private final float[] mAccumulatedRotation = new float[16];
	
	/** Store the current rotation. */
	private final float[] mCurrentRotation = new float[16];
	
	/** A temporary matrix. */
	private float[] mTemporaryMatrix = new float[16];
	
	/** Store our model data in a float buffer. */
	
	//Ball model data in a float buffer	
	private final FloatBuffer mBallPositions;	
	private final FloatBuffer mBallNormals;
	private final FloatBuffer mBallTextureCoordinates;
	
	//Paddle model data in a float buffer	
	private final FloatBuffer mPaddlePositionsLeft;
	private final FloatBuffer mPaddlePositionsRight;
	//private final FloatBuffer mPaddlePositionsTopBottom;
	private final FloatBuffer mPaddlePositionsBottom;
	private final FloatBuffer mPaddlePositionsTop;
	
	//Brick model data in a float buffer
	private final FloatBuffer mBrickPositions;	
	private final FloatBuffer mBrickNormals;
	private final FloatBuffer mBrickTextureCoordinates;
		
	/** This will be used to pass in the transformation matrix. */
	private int mMVPMatrixHandle;
	
	/** This will be used to pass in the modelview matrix. */
	private int mMVMatrixHandle;
	
	/** This will be used to pass in the light position. */
	private int mLightPosHandle;
	
	/** This will be used to pass in the texture. */
	private int mTextureUniformHandle;
	
	/** This will be used to pass in model position information. */
	private int mPositionHandle;
	
	/** This will be used to pass in model normal information. */
	private int mNormalHandle;
	
	/** This will be used to pass in model texture coordinate information. */
	private int mTextureCoordinateHandle;
 
	/** How many bytes per float. */
	private final int mBytesPerFloat = 4;	
	
	/** Size of the position data in elements. */
	private final int mPositionDataSize = 3;	
	
	/** Size of the normal data in elements. */
	private final int mNormalDataSize = 3;
	
	/** Size of the texture coordinate data in elements. */
	private final int mTextureCoordinateDataSize = 2;
	
	/** Used to hold a light centered on the origin in model space. We need a 4th coordinate so we can get translations to work when
	 *  we multiply this by our transformation matrices. */
	private final float[] mLightPosInModelSpace = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
	
	/** Used to hold the current position of the light in world space (after transformation via model matrix). */
	private final float[] mLightPosInWorldSpace = new float[4];
	
	/** Used to hold the transformed position of the light in eye space (after transformation via modelview matrix) */
	private final float[] mLightPosInEyeSpace = new float[4];
	
	/** This is a handle to our cube shading program. */
	private int mProgramHandle;
		
	/** This is a handle to our light point program. */
	private int mPointProgramHandle;
	
	/** These are handles to our texture data. */
	private int mBrickDataHandle;
	private int mBrickTextureHandle;
	//private int mGrassDataHandle;
	private int mBallTextureHandle;
	private int mPaddleTextureHandle;
	private int mBackGroundTextureHandle;
	//Texture Handlers for numbers
	private int iNumberZeroTextureHandle;
	private int iNumberOneTextureHandle;
	private int iNumberTwoTextureHandle;
	private int iNumberThreeTextureHandle;
	private int iNumberFourTextureHandle;
	private int iNumberFiveTextureHandle;
	private int iNumberSixTextureHandle;
	private int iNumberSevenTextureHandle;
	private int iNumberEightTextureHandle;
	private int iNumberNineTextureHandle;
	
	/** Temporary place to save the min and mag filter, in case the activity was restarted. */
	private int mQueuedMinFilter;
	private int mQueuedMagFilter;
	
	// These still work without volatile, but refreshes are not guaranteed to happen.					
	public volatile float mDeltaX;					
	public volatile float mDeltaY;	
	
	/**
	 * Initialize the model data.
	 */
	public RoundoidRenderer(final Context activityContext)
	{	
		mActivityContext = activityContext;
		
		InitializeBricks();
		
		Speed.fXCoordinate3 = 0.7f;
		Speed.fYCoordinate3 = 0.0f;
		
		starttime = System.currentTimeMillis();
		
		leftPaddle.fPaddlePoint1[0] = -fOffSet+fPaddleWidth;
		leftPaddle.fPaddlePoint1[1] = fPaddleHeight;
		leftPaddle.fPaddlePoint2[0] = -fOffSet+fPaddleWidth;
		leftPaddle.fPaddlePoint2[1] = -fPaddleHeight;
				
		rightPaddle.fPaddlePoint1[0] = fOffSet-fPaddleWidth;
		rightPaddle.fPaddlePoint1[1] = fPaddleHeight;
		rightPaddle.fPaddlePoint2[0] = fOffSet-fPaddleWidth;
		rightPaddle.fPaddlePoint2[1] = -fPaddleHeight;
		
		topPaddle.fPaddlePoint1[0] = -fPaddleHeight;
		topPaddle.fPaddlePoint1[1] = fOffSet-fPaddleWidth;
		topPaddle.fPaddlePoint2[0] = fPaddleHeight;
		topPaddle.fPaddlePoint2[1] = fOffSet-fPaddleWidth;
		
		bottomPaddle.fPaddlePoint1[0] = -fPaddleHeight;
		bottomPaddle.fPaddlePoint1[1] = -fOffSet+fPaddleWidth;
		bottomPaddle.fPaddlePoint2[0] = fPaddleHeight;
		bottomPaddle.fPaddlePoint2[1] = -fOffSet+fPaddleWidth;
		
		//define points for paddleleft surface
		//X, Y, Z
		final float[] paddlePositionDataLeft =
			{
				// Front face
				 -fPaddleWidth-fOffSet, fPaddleHeight, 0.0f,				
				 -fPaddleWidth-fOffSet, -fPaddleHeight, 0.0f,
				 fPaddleWidth-fOffSet, fPaddleHeight, 0.0f, 
				 -fPaddleWidth-fOffSet, -fPaddleHeight, 0.0f, 				
				 fPaddleWidth-fOffSet, -fPaddleHeight, 0.0f,
				 fPaddleWidth-fOffSet, fPaddleHeight, 0.0f,
			};
		
		//define points for paddleright surface
		//X, Y, Z
		final float[] paddlePositionDataRight =
			{
				// Front face
				 -fPaddleWidth+fOffSet, fPaddleHeight, 0.0f,				
				 -fPaddleWidth+fOffSet, -fPaddleHeight, 0.0f,
				 fPaddleWidth+fOffSet, fPaddleHeight, 0.0f, 
				 -fPaddleWidth+fOffSet, -fPaddleHeight, 0.0f, 				
				 fPaddleWidth+fOffSet, -fPaddleHeight, 0.0f,
				 fPaddleWidth+fOffSet, fPaddleHeight, 0.0f,
			};				
		
		//define points for paddletopbottom surface
	    //X, Y, Z
		final float[] paddlePositionDataTopBottom =
			{
				// Front face
				 -fPaddleHeight, fPaddleWidth, 0.0f,				
				 -fPaddleHeight, -fPaddleWidth, 0.0f,
				 fPaddleHeight, fPaddleWidth, 0.0f, 
				 -fPaddleHeight, -fPaddleWidth, 0.0f, 				
				 fPaddleHeight, -fPaddleWidth, 0.0f,
				 fPaddleHeight, fPaddleWidth, 0.0f,
			};
		
		final float[] paddlePositionDataTop =
			{
				// Front face
				 -fPaddleHeight, fPaddleWidth+fOffSet, 0.0f,				
				 -fPaddleHeight, -fPaddleWidth+fOffSet, 0.0f,
				 fPaddleHeight, fPaddleWidth+fOffSet, 0.0f, 
				 -fPaddleHeight, -fPaddleWidth+fOffSet, 0.0f, 				
				 fPaddleHeight, -fPaddleWidth+fOffSet, 0.0f,
				 fPaddleHeight, fPaddleWidth+fOffSet, 0.0f,
			};
		
		final float[] paddlePositionDataBottom =
			{
				// Front face
				 -fPaddleHeight, fPaddleWidth-fOffSet, 0.0f,				
				 -fPaddleHeight, -fPaddleWidth-fOffSet, 0.0f,
				 fPaddleHeight, fPaddleWidth-fOffSet, 0.0f, 
				 -fPaddleHeight, -fPaddleWidth-fOffSet, 0.0f, 				
				 fPaddleHeight, -fPaddleWidth-fOffSet, 0.0f,
				 fPaddleHeight, fPaddleWidth-fOffSet, 0.0f,
			};
		
		
		
		final float[] brickPositionData = 
			{
				// Front face
				 -fBrickHeigtWidth, fBrickHeigtWidth, 0.0f,				
				 -fBrickHeigtWidth, -fBrickHeigtWidth, 0.0f,
				 fBrickHeigtWidth, fBrickHeigtWidth, 0.0f, 
				 -fBrickHeigtWidth, -fBrickHeigtWidth, 0.0f, 				
				 fBrickHeigtWidth, -fBrickHeigtWidth, 0.0f,
				 fBrickHeigtWidth, fBrickHeigtWidth, 0.0f,
			};
		
		final float[] brickNormalData = 
			{
				// Front face
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
			};
		
		final float[] brickTextureCoordinateData = 
			{
				// Front face
				0.0f, 0.0f, 				
				0.0f, 1.0f,
				1.0f, 0.0f,
				0.0f, 1.0f,
				1.0f, 1.0f,
				1.0f, 0.0f,	
			};
		
		final float[] ballNormalData = 
			{
				// Front face
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
			};
		
		final float[] ballTextureCoordinateData = 
			{
				// Front face
				0.0f, 0.0f, 				
				0.0f, 1.0f,
				1.0f, 0.0f,
				0.0f, 1.0f,
				1.0f, 1.0f,
				1.0f, 0.0f,	
			};
		
		// Define points for a cube.			
		// X, Y, Z
		final float[] ballPositionData =
		{
				// In OpenGL counter-clockwise winding is default. This means that when we look at a triangle, 
				// if the points are counter-clockwise we are looking at the "front". If not we are looking at
				// the back. OpenGL has an optimization where all back-facing triangles are culled, since they
				// usually represent the backside of an object and aren't visible anyways.
				
				// Front face
				-fBallWidthHeight, fBallWidthHeight, 0.0f,				
				-fBallWidthHeight, -fBallWidthHeight, 0.0f,
				fBallWidthHeight, fBallWidthHeight, 0.0f, 
				-fBallWidthHeight, -fBallWidthHeight, 0.0f, 				
				fBallWidthHeight, -fBallWidthHeight, 0.0f,
				fBallWidthHeight, fBallWidthHeight, 0.0f,	
		};	
		
		// Initialize the buffers.
		mBallPositions = ByteBuffer.allocateDirect(ballPositionData.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mBallPositions.put(ballPositionData).position(0);				
				
		//Initialize paddle buffers
		mPaddlePositionsLeft = ByteBuffer.allocateDirect(paddlePositionDataLeft.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();
		mPaddlePositionsLeft.put(paddlePositionDataLeft).position(0);
		
		//Initialize paddle buffers
		mPaddlePositionsRight = ByteBuffer.allocateDirect(paddlePositionDataRight.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();
		mPaddlePositionsRight.put(paddlePositionDataRight).position(0);
		
		mPaddlePositionsTop = ByteBuffer.allocateDirect(paddlePositionDataTop.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();
		mPaddlePositionsTop.put(paddlePositionDataTop).position(0);	
		
		mPaddlePositionsBottom = ByteBuffer.allocateDirect(paddlePositionDataBottom.length * mBytesPerFloat)
		        .order(ByteOrder.nativeOrder()).asFloatBuffer();
				mPaddlePositionsBottom.put(paddlePositionDataBottom).position(0);	
		
		mBallNormals = ByteBuffer.allocateDirect(ballNormalData.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();
		mBallNormals.put(ballNormalData).position(0);
		
		mBallTextureCoordinates = ByteBuffer.allocateDirect(ballTextureCoordinateData.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();
		mBallTextureCoordinates.put(ballTextureCoordinateData).position(0);
		
		//Initialize brick buffers				
		mBrickPositions = ByteBuffer.allocateDirect(brickPositionData.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mBrickPositions.put(brickPositionData).position(0);	
		
		mBrickNormals = ByteBuffer.allocateDirect(brickNormalData.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mBrickNormals.put(brickNormalData).position(0);	
		
		mBrickTextureCoordinates = ByteBuffer.allocateDirect(brickTextureCoordinateData.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mBrickTextureCoordinates.put(brickTextureCoordinateData).position(0);	
		
		sleepTime = 0;
		
	}
		
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) 
	{
		
		// Set the background clear color to black.
		//GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		GLES20.glEnable(GLES20.GL_BLEND);	
		
	    GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_DST_ALPHA);
    
	    
		// Use culling to remove back faces.
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		
		// Enable depth testing
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		
		// Enable texture mapping
		GLES20.glEnable(GLES20.GL_TEXTURE_2D);
			
		// Position the eye in front of the origin.
		final float eyeX = 0.0f;
		final float eyeY = 0.0f;
		final float eyeZ = 3.0f;

		// We are looking toward the distance
		final float lookX = 0.0f;
		final float lookY = 0.0f;
		final float lookZ = -10.0f;

		// Set our up vector. This is where our head would be pointing were we holding the camera.
		final float upX = 0.0f;
		final float upY = 1.0f;
		final float upZ = 0.0f;

		// Set the view matrix. This matrix can be said to represent the camera position.
		// NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
		// view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
		Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

		
		final String vertexShader = RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_vertex_shader_tex_and_light);   		
 		final String fragmentShader = RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_fragment_shader_tex_and_light);			
		
		final int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);		
		final int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);		
		
		mProgramHandle = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, 
				new String[] {"a_Position",  "a_Normal", "a_TexCoordinate"});								                                							       
        
        // Define a simple shader program for our point.
        final String pointVertexShader = RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.point_vertex_shader);        	       
        final String pointFragmentShader = RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.point_fragment_shader);
        
        final int pointVertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, pointVertexShader);
        final int pointFragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, pointFragmentShader);
        mPointProgramHandle = ShaderHelper.createAndLinkProgram(pointVertexShaderHandle, pointFragmentShaderHandle, 
        		new String[] {"a_Position"}); 
        
        // Load the textures       
        mBallTextureHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.ball);
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);        
        mPaddleTextureHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.paddle);
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        mBrickTextureHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.brick);
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        mBackGroundTextureHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.mainmenubackground);
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        
        
        iNumberZeroTextureHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.number0);
        iNumberOneTextureHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.number1);
        iNumberTwoTextureHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.number2);
        iNumberThreeTextureHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.number3);
        iNumberFourTextureHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.number4);
        iNumberFiveTextureHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.number5);
        iNumberSixTextureHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.number6);
        iNumberSevenTextureHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.number7);
        iNumberEightTextureHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.number8);
        iNumberNineTextureHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.number9);
        
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);    
        
        //beter graphics hier
       
        if (mQueuedMinFilter != 0)
        {
        	setMinFilter(mQueuedMinFilter);
        }
        
        if (mQueuedMagFilter != 0)
        {
        	setMagFilter(mQueuedMagFilter);
        }
        
        // Initialize the accumulated rotation matrix
        Matrix.setIdentityM(mAccumulatedRotation, 0);      
       
	}	
		
	public void onSurfaceChanged(GL10 glUnused, int width, int height) 
	{		
		// Set the OpenGL viewport to the same size as the surface.
		GLES20.glViewport(0, 0, width, height);

		// Create a new perspective projection matrix. The height will stay the same
		// while the width will vary as per aspect ratio.
		final float ratio = (float) height/width;
		final float left = -1.0f;
		final float right = 1.0f;
		final float bottom = -ratio;
		final float top = ratio;	
		final float near = 3.0f;
		final float far = 7.0f;
		
		screenWidth = width;
		screenHeight = height;		
		
		halfx = (float)height/(float)width;
		
		viewport[0] = 0;
		viewport[1] = 0;
		viewport[2] = width;
		viewport[3] = height;		
		
		mmdh = new MainMenuDisplayHandler(screenHeight,screenWidth,mActivityContext);
		swdh = new SelectWorldDisplayHandler(screenHeight,screenWidth,mActivityContext);
		bgdh = new BackGroundDisplayHandler(screenHeight,screenWidth); 
		tdh = new TimerDisplayHandler();
		bbdh = new BackButtonDisplayHandler(screenHeight,screenWidth);
		
		Log.d("hoogte breedte",screenHeight + " - " + screenWidth);		
		Matrix.orthoM(mProjectionMatrix, 0, left, right, bottom, top, near, far);		
	}	

	private void DrawMainMenu()
	{		
		
		mmdh.Draw(mProgramHandle, mMVPMatrixHandle, mMVMatrixHandle, mTextureUniformHandle, mPositionHandle, mNormalHandle, mTextureCoordinateHandle, mModelMatrix,mBackGroundTextureHandle,mTextureCoordinateDataSize,mPositionDataSize,mNormalDataSize,mMVPMatrix,mViewMatrix,mTemporaryMatrix,mProjectionMatrix,iNumberZeroTextureHandle);              
	}	
	
	private void DrawInGame()
	{
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
       
        // Set our per-vertex lighting program.
        GLES20.glUseProgram(mProgramHandle);
        
        // Set program handles for drawing.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");
        mMVMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVMatrix"); 
        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Texture");
        mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position");        
        mNormalHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Normal"); 
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoordinate");                      
        
        DrawPaddleRight();
        DrawPaddleLeft();
        DrawPaddleTop();
        DrawPaddleBottom();
        DrawBall();         
        TranslateBallCoordinates();
        Rotate1(leftPaddle);
        CalculateLineCircleIntersection(leftPaddle);
        Rotate1(rightPaddle);
        CalculateLineCircleIntersection(rightPaddle);        
        Rotate1(topPaddle);
        CalculateLineCircleIntersection(topPaddle);
        Rotate1(bottomPaddle);
        CalculateLineCircleIntersection(bottomPaddle);   
        DrawBrickArray();            
        DrawTimer();
        DrawLifeCounter();
        DrawBackButton();
        DrawBoosterBar();
        DrawBackGround();   
        DrawBall();  
	}
	
	private void UpdateGameState()
	{
		Speed.Update();
		
		millis = System.currentTimeMillis() - starttime;
        seconds = (int) (millis / 1000);
        minutes = seconds / 60;
        seconds     = seconds % 60;
        millis     = millis % 1000;
        
        CalculateBrickArrayIntersections();
	}
	
	public void onDrawFrame(GL10 glUnused) 
	{		
		beginTime = System.currentTimeMillis();
		framesSkipped = 0;  // resetting the frames skipped
		
		// update game state
		UpdateGameState();
		// render state to the screen
		
		switch(iDisplayIndex)
        {
	        case 0:
	        DrawMainMenu();
	        break;
	        case 1:
	        DrawInGame();
		    break;
	        case 2:
	        swdh.Draw(mProgramHandle, mMVPMatrixHandle, mMVMatrixHandle, mTextureUniformHandle, mPositionHandle, mNormalHandle, mTextureCoordinateHandle, mModelMatrix,mBackGroundTextureHandle,mTextureCoordinateDataSize,mPositionDataSize,mNormalDataSize,mMVPMatrix,mViewMatrix,mTemporaryMatrix,mProjectionMatrix,iNumberZeroTextureHandle);
		    break;
        }
			
		// calculate how long did the cycle take
		timeDiff = System.currentTimeMillis() - beginTime;
		
		// calculate sleep time
		sleepTime = (int)(FRAME_PERIOD - timeDiff);
		
		if (sleepTime > 0) 
		{
			// if sleepTime > 0 we're OK
			try {
				// send the thread to sleep for a short period
				// very useful for battery saving
				Thread.sleep(sleepTime);
				} 
			catch (InterruptedException e) {}
		}
		
		while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) 
		{
			// we need to catch up
			// update without rendering
			UpdateGameState();
			// add frame period to check if in next frame
			sleepTime += FRAME_PERIOD;
			framesSkipped++;
		}
		
		// for statistics
		framesSkippedPerStatCycle += framesSkipped;
		// calling the routine to store the gathered statistics
		storeStats();
	}	
	
	public void setMinFilter(final int filter)
	{
		//if (mBrickDataHandle != 0 && mGrassDataHandle != 0)
		if(mBallTextureHandle != 0 && mBrickTextureHandle != 0 && mPaddleTextureHandle != 0)
    	{
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBallTextureHandle);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, filter);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBrickTextureHandle);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, filter);
			//GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mPaddleTextureHandle);
			//GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, filter);
		}
		else
		{
			mQueuedMinFilter = filter;
		}
	}
	
	public void setMagFilter(final int filter)
	{		
		if(mBallTextureHandle != 0 && mBrickTextureHandle != 0 && mPaddleTextureHandle != 0)
		{
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBallTextureHandle);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, filter);			
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBrickTextureHandle);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, filter);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mPaddleTextureHandle);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, filter);
		}
		else
		{
			mQueuedMagFilter = filter;
		}
	}

	private void DrawBall()
	{    
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, Speed.fXCoordinate3, Speed.fYCoordinate3, 0.0f); 
              
    	// Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBallTextureHandle);
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);
		
		// Pass in the position information
		mBallPositions.position(0);		
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mBallPositions);        
                
        GLES20.glEnableVertexAttribArray(mPositionHandle);    
       
        GLES20.glEnableVertexAttribArray(mNormalHandle);                
        
		// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
        
        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                
        
        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).        
        Matrix.multiplyMM(mTemporaryMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        System.arraycopy(mTemporaryMatrix, 0, mMVPMatrix, 0, 16);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        
        // Pass in the light position in eye space.        
        GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);
        
        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 8); 
	}	
	
	private void DrawPaddleRight()
	{	
		
		// Draw a paddle.
        // Translate the cube into the screen.
        Matrix.setIdentityM(mModelMatrix, 0);       
        
        // Set a matrix that contains the current rotation.
        Matrix.setIdentityM(mCurrentRotation, 0);        
    	Matrix.rotateM(mCurrentRotation, 0, mDeltaX, 0.0f, 0.0f, 1.0f);
    	Matrix.rotateM(mCurrentRotation, 0, mDeltaY, 0.0f, 0.0f, 1.0f);
    	mDeltaX = 0.0f;
    	mDeltaY = 0.0f;
    	
    	// Multiply the current rotation by the accumulated rotation, and then set the accumulated rotation to the result.
    	Matrix.multiplyMM(mTemporaryMatrix, 0, mCurrentRotation, 0, mAccumulatedRotation, 0);
    	System.arraycopy(mTemporaryMatrix, 0, mAccumulatedRotation, 0, 16);
    	    	
        // Rotate the cube taking the overall rotation into account.     	
    	Matrix.multiplyMM(mTemporaryMatrix, 0, mModelMatrix, 0, mAccumulatedRotation, 0);
    	System.arraycopy(mTemporaryMatrix, 0, mModelMatrix, 0, 16);
			       
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mPaddleTextureHandle);
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);
        
        // Pass in the texture coordinate information
        mBallTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mBallTextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        
      
		// Pass in the position information
        mPaddlePositionsRight.position(0);		
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mPaddlePositionsRight);        
                
        GLES20.glEnableVertexAttribArray(mPositionHandle);                       
        
        // Pass in the normal information
        mBallNormals.position(0);
        GLES20.glVertexAttribPointer(mNormalHandle, mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mBallNormals);
        
        GLES20.glEnableVertexAttribArray(mNormalHandle);                
        
		// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
        
        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                
        
        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).        
        Matrix.multiplyMM(mTemporaryMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        System.arraycopy(mTemporaryMatrix, 0, mMVPMatrix, 0, 16);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        
        // Pass in the light position in eye space.        
        //GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);
        
        // Draw the ball.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 8); 
        
	}
	
	private void DrawPaddleLeft()
	{	
	     //Matrix.setIdentityM(mModelMatrix, 0);     
	     //Matrix.multiplyMV(fLeftPaddleBottom, 0, mModelMatrix, 0, fLeftPaddleBottom, 0);
		// Draw a paddle.
        // Translate the cube into the screen.
        Matrix.setIdentityM(mModelMatrix, 0);       
        
        Matrix.setIdentityM(mCurrentRotation, 0);        
    	Matrix.rotateM(mCurrentRotation, 0, LeftPaddleLocation.DeltaX, 0.0f, 0.0f, 1.0f);
    	Matrix.rotateM(mCurrentRotation, 0, LeftPaddleLocation.DeltaY, 0.0f, 0.0f, 1.0f);
    	LeftPaddleLocation.DeltaX = 0.0f;
    	LeftPaddleLocation.DeltaY = 0.0f;
    	
    	// Multiply the current rotation by the accumulated rotation, and then set the accumulated rotation to the result.
    	Matrix.multiplyMM(mTemporaryMatrix, 0, mCurrentRotation, 0, mAccumulatedRotation, 0);
    	System.arraycopy(mTemporaryMatrix, 0, mAccumulatedRotation, 0, 16);
    	    	
        // Rotate the cube taking the overall rotation into account.     	
    	Matrix.multiplyMM(mTemporaryMatrix, 0, mModelMatrix, 0, mAccumulatedRotation, 0);
    	System.arraycopy(mTemporaryMatrix, 0, mModelMatrix, 0, 16); 
    	
		//CalculateLineCircleIntersection();
		        
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mPaddleTextureHandle);
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);
        
        // Pass in the texture coordinate information
        mBallTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mBallTextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);        
      
		// Pass in the position information
        mPaddlePositionsLeft.position(0);		
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mPaddlePositionsLeft);        
                
        GLES20.glEnableVertexAttribArray(mPositionHandle);                       
        
        // Pass in the normal information
        mBallNormals.position(0);
        GLES20.glVertexAttribPointer(mNormalHandle, mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mBallNormals);
        
        GLES20.glEnableVertexAttribArray(mNormalHandle);                
        
		// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
        
        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                
        
        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).        
        Matrix.multiplyMM(mTemporaryMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        System.arraycopy(mTemporaryMatrix, 0, mMVPMatrix, 0, 16);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        
        // Pass in the light position in eye space.        
        //GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);
        
        // Draw the ball.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 8); 
	}
	
	private void DrawPaddleBottom()
	{
		// Draw a paddle.
        // Translate the cube into the screen.
        Matrix.setIdentityM(mModelMatrix, 0);
        
        // Set a matrix that contains the current rotation.
        Matrix.setIdentityM(mCurrentRotation, 0);        
    	Matrix.rotateM(mCurrentRotation, 0, mDeltaX, 0.0f, 0.0f, 1.0f);
    	Matrix.rotateM(mCurrentRotation, 0, mDeltaY, 0.0f, 0.0f, 1.0f);    	
    	mDeltaX = 0.0f;
    	mDeltaY = 0.0f;
    	
    	// Multiply the current rotation by the accumulated rotation, and then set the accumulated rotation to the result.
    	Matrix.multiplyMM(mTemporaryMatrix, 0, mCurrentRotation, 0, mAccumulatedRotation, 0);
    	System.arraycopy(mTemporaryMatrix, 0, mAccumulatedRotation, 0, 16);
    	    	
        // Rotate the cube taking the overall rotation into account.     	
    	Matrix.multiplyMM(mTemporaryMatrix, 0, mModelMatrix, 0, mAccumulatedRotation, 0);
    	System.arraycopy(mTemporaryMatrix, 0, mModelMatrix, 0, 16);
		
		//Matrix.translateM(mModelMatrix, 0, -1.0f*(float)Math.sin(mDeltaX), -1.0f*(float)Math.cos(mDeltaY), 0.0f);
		        
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mPaddleTextureHandle);
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);
        
        // Pass in the texture coordinate information
        mBallTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mBallTextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        
      
		// Pass in the position information
		mPaddlePositionsBottom.position(0);		
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mPaddlePositionsBottom);        
                
        GLES20.glEnableVertexAttribArray(mPositionHandle);                       
        
        // Pass in the normal information
        mBallNormals.position(0);
        GLES20.glVertexAttribPointer(mNormalHandle, mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mBallNormals);
        
        GLES20.glEnableVertexAttribArray(mNormalHandle);                
        
		// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
        
        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                
        
        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).        
        Matrix.multiplyMM(mTemporaryMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        System.arraycopy(mTemporaryMatrix, 0, mMVPMatrix, 0, 16);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        
        // Pass in the light position in eye space.        
        //GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);
        
        // Draw the ball.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 8); 
	}
	
	private void DrawPaddleTop()
	{
		// Draw a paddle.
        // Translate the cube into the screen.
		    Matrix.setIdentityM(mModelMatrix, 0);        
	    	Matrix.rotateM(mCurrentRotation, 0, mDeltaX, 0.0f, 0.0f, 1.0f);
	    	Matrix.rotateM(mCurrentRotation, 0, mDeltaY, 0.0f, 0.0f, 1.0f);
	    	mDeltaX = 0.0f;
	    	mDeltaY = 0.0f;
	
    	// Multiply the current rotation by the accumulated rotation, and then set the accumulated rotation to the result.
    	Matrix.multiplyMM(mTemporaryMatrix, 0, mCurrentRotation, 0, mAccumulatedRotation, 0);
    	System.arraycopy(mTemporaryMatrix, 0, mAccumulatedRotation, 0, 16);
        
        
        // Rotate the cube taking the overall rotation into account.     	
    	Matrix.multiplyMM(mTemporaryMatrix, 0, mModelMatrix, 0, mAccumulatedRotation, 0);
    	System.arraycopy(mTemporaryMatrix, 0, mModelMatrix, 0, 16);
    	
    	
        //Matrix.translateM(mModelMatrix, 0, (float)Math.sin(mDeltaX), (float)Math.cos(mDeltaY), 0.0f);
    	
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mPaddleTextureHandle);
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);
        
        // Pass in the texture coordinate information
        mBallTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mBallTextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        
      
		// Pass in the position information
        mPaddlePositionsTop.position(0);		
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mPaddlePositionsTop);     
                
        GLES20.glEnableVertexAttribArray(mPositionHandle);                       
        
        // Pass in the normal information
        mBallNormals.position(0);
        GLES20.glVertexAttribPointer(mNormalHandle, mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mBallNormals);
        
        GLES20.glEnableVertexAttribArray(mNormalHandle);                
        
		// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
        
        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                
        
        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).        
        Matrix.multiplyMM(mTemporaryMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        System.arraycopy(mTemporaryMatrix, 0, mMVPMatrix, 0, 16);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        
        // Pass in the light position in eye space.        
        //GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);
        
        // Draw the ball.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 8);
	}
	
	public void CalculateBrickArrayIntersections()
	{
		for(int i = 0; i < brickarray.length ; i++)
		{
			if(brickarray[i].bShouldBeDrawn)
			{
				if(CalculateBallBrickIntersection(brickarray[i]))
				{
					brickarray[i].bShouldBeDrawn = false;
					Speed.toggleXDirection();
					Speed.toggleYDirection();
				}
			}
		}
	}
	
	private void DrawBrickArray()
	{
		for(int i = 0; i < brickarray.length ; i++)
		{
			if(brickarray[i].bShouldBeDrawn)
			{
				Matrix.setIdentityM(mModelMatrix, 0);
		        Matrix.translateM(mModelMatrix, 0, brickarray[i].fXOffSet, brickarray[i].fYOffSet, 0.0f); 
		              
		    	// Set the active texture unit to texture unit 0.
		        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		        
		        // Bind the texture to this unit.
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBrickTextureHandle);
		        
		        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
		        GLES20.glUniform1i(mTextureUniformHandle, 0);
				
				// Pass in the position information
				mBrickPositions.position(0);		
		        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
		        		0, mBrickPositions);        
		                
		        GLES20.glEnableVertexAttribArray(mPositionHandle);    
		       
		        GLES20.glEnableVertexAttribArray(mNormalHandle);                
		        
				// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
		        // (which currently contains model * view).
		        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
		        
		        // Pass in the modelview matrix.
		        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                
		        
		        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
		        // (which now contains model * view * projection).        
		        Matrix.multiplyMM(mTemporaryMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
		        System.arraycopy(mTemporaryMatrix, 0, mMVPMatrix, 0, 16);
	
		        // Pass in the combined matrix.
		        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
		        
		        // Pass in the light position in eye space.        
		        //GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);
		        
		        // Draw the brick.
		        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 8);     
			}
		}
	}
	
	private void CalculateLineCircleIntersection(Paddle _paddle)
	{					
		if(System.currentTimeMillis() - ballpaddlecollisiondetectbuffer > 100)
		{
			
		
			d1 = (Speed.fXCoordinate3-_paddle.outVector2[0])*(_paddle.outVector1[0]-_paddle.outVector2[0]);
			d2 =	(Speed.fYCoordinate3-_paddle.outVector2[1])*(_paddle.outVector1[1]-_paddle.outVector2[1]);	
			d3 = (_paddle.outVector1[0]-_paddle.outVector2[0])*(_paddle.outVector1[0]-_paddle.outVector2[0]);
			d4 = (_paddle.outVector1[1]-_paddle.outVector2[1])*(_paddle.outVector1[1]-_paddle.outVector2[1]);
			
			u = ( d1 + d2 ) /
				( d3 + d4 );		
			
			a = Math.pow((_paddle.outVector1[0]-_paddle.outVector2[0]), 2.0) + Math.pow((_paddle.outVector1[1]-_paddle.outVector2[1]), 2.0);
			b = 2*((_paddle.outVector1[0]-_paddle.outVector2[0])*(_paddle.outVector2[0]-Speed.fXCoordinate3) 
					   + (_paddle.outVector1[1]-_paddle.outVector2[1])*(_paddle.outVector2[1]-Speed.fYCoordinate3));
			c = Math.pow(Speed.fXCoordinate3, 2.0) + Math.pow(Speed.fYCoordinate3, 2.0) +  Math.pow(_paddle.outVector2[0], 2.0) + Math.pow(_paddle.outVector2[1], 2.0) 
					  -2*((Speed.fXCoordinate3*_paddle.outVector2[0])+(Speed.fYCoordinate3*_paddle.outVector2[1])) - Math.pow(fBallWidthHeight, 2.0);
			
			if(u>0.0 && u<1.0)
			{		
				if((b*b-4*a*c)>=0.0)
				{
					ballpaddlecollisiondetectbuffer = System.currentTimeMillis();
					
					deltax = (_paddle.outVector2[0]-_paddle.outVector1[0]);
					deltay = (_paddle.outVector2[1]-_paddle.outVector1[1]);
					length = (float)(Math.sqrt(Math.pow((deltax), 2.0f)+Math.pow((deltay), 2)));
									
					fNx = (float) (-(deltay)/length); 			
					fNy = (float) ((deltax)/length); 			
					VdotN = Speed.xDirection*fNx+Speed.yDirection*fNy;
									
					Speed.xDirection = Speed.xDirection -2*(VdotN*fNx);
					Speed.yDirection = Speed.yDirection -2*(VdotN*fNy);
										
				}
			}
		}
	}
	
	private boolean CalculateBallBrickIntersection(Brick _brick)
	{
	    circleDistancex = Math.abs(Speed.fXCoordinate3 - _brick.fXOffSet);
	    circleDistancey = Math.abs(Speed.fYCoordinate3 - _brick.fYOffSet);
	    
	    if (circleDistancex > (_brick.fHeightWidth/2 + fBallWidthHeight)) { return false; }
	    if (circleDistancey > (_brick.fHeightWidth/2 + fBallWidthHeight)) { return false; }

	    if (circleDistancex <= (_brick.fHeightWidth/2)) { return true; } 
	    if (circleDistancey <= (_brick.fHeightWidth/2)) { return true; }

	    cornerDistance_sq = (float) (Math.pow((circleDistancex - _brick.fHeightWidth/2), 2) + Math.pow((circleDistancey - _brick.fHeightWidth/2), 2));

	    return (cornerDistance_sq <= (Math.pow(fBallWidthHeight, 2)));
	}
		
	public void Rotate1(Paddle _paddle)
    {		      
		_paddle.inVector1[0] = _paddle.fPaddlePoint1[0];
		_paddle.inVector1[1] = _paddle.fPaddlePoint1[1];
		_paddle.inVector1[2] = 0;
		_paddle.inVector1[3] = 1; 
       
		_paddle.inVector2[0] = _paddle.fPaddlePoint2[0];
		_paddle.inVector2[1] = _paddle.fPaddlePoint2[1];
		_paddle.inVector2[2] = 0;
		_paddle.inVector2[3] = 1;  
     
        Matrix.multiplyMV(_paddle.outVector2, 0, mAccumulatedRotation, 0, _paddle.inVector2, 0);
        Matrix.multiplyMV(_paddle.outVector1, 0, mAccumulatedRotation, 0, _paddle.inVector1, 0);
    }	
	
	public void TranslateBallCoordinates()
	{
		    float[] inVector3 = new float[4];
	        inVector3[0] = Speed.fXCoordinate3;
	        inVector3[1] = Speed.fYCoordinate3;
	        inVector3[2] = 0;
	        inVector3[3] = 1;
			
	        Matrix.setIdentityM(mModelMatrix, 0);
	        Matrix.translateM(mModelMatrix, 0, Speed.fXCoordinate3, Speed.fYCoordinate3, 0.0f); 
	  
	        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
	        Matrix.multiplyMV(outVector3, 0, mMVPMatrix, 0, inVector3, 0);	
	}
			
	public void InitializeBricks()
	{
		//First Level
		brickarray[0] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth,fBrickHeigtWidth,true);
		brickarray[1] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth,-fBrickHeigtWidth,true);
		brickarray[2] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth,-fBrickHeigtWidth,true);
		brickarray[3] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth,fBrickHeigtWidth,true);
		
		//Second Level
		brickarray[4] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth,fBrickHeigtWidth*3,true);
		brickarray[5] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*3,fBrickHeigtWidth*3,true);
		brickarray[6] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*3,fBrickHeigtWidth,true);		
		brickarray[7] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*3,-fBrickHeigtWidth,true);
		brickarray[8] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*3,-fBrickHeigtWidth*3,true);
		brickarray[9] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth,-fBrickHeigtWidth*3,true);		
		brickarray[10] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth,-fBrickHeigtWidth*3,true);
		brickarray[11] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*3,-fBrickHeigtWidth*3,true);
		brickarray[12] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*3,-fBrickHeigtWidth,true);		
		brickarray[13] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*3,fBrickHeigtWidth,true);
		brickarray[14] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*3,fBrickHeigtWidth*3,true);
		brickarray[15] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth,fBrickHeigtWidth*3,true);
		
		//Third Level
		brickarray[16] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth,fBrickHeigtWidth*5,false);
		brickarray[17] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*3,fBrickHeigtWidth*5,true);
		brickarray[18] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*5,fBrickHeigtWidth*5,false);
		brickarray[19] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*5,fBrickHeigtWidth*3,true);
		brickarray[20] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*5,fBrickHeigtWidth,false);
		
		brickarray[21] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*5,-fBrickHeigtWidth,true);
		brickarray[22] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*5,-fBrickHeigtWidth*3,false);
		brickarray[23] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*5,-fBrickHeigtWidth*5,true);
		brickarray[24] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*3,-fBrickHeigtWidth*5,false);
		brickarray[25] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth,-fBrickHeigtWidth*5,true);
		
		brickarray[26] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth,-fBrickHeigtWidth*5,true);
		brickarray[27] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*3,-fBrickHeigtWidth*5,true);
		brickarray[28] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*5,-fBrickHeigtWidth*5,true);
		brickarray[29] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*5,-fBrickHeigtWidth*3,true);
		brickarray[30] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*5,-fBrickHeigtWidth,true);
		
		brickarray[31] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*5,fBrickHeigtWidth,true);
		brickarray[32] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*5,fBrickHeigtWidth*3,true);
		brickarray[33] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*5,fBrickHeigtWidth*5,true);
		brickarray[34] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*3,fBrickHeigtWidth*5,true);
		brickarray[35] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth,fBrickHeigtWidth*5,true);
		
		//Fourth Level
		brickarray[36] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth,fBrickHeigtWidth*7,true);
		brickarray[37] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*3,fBrickHeigtWidth*7,true);
		brickarray[38] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*5,fBrickHeigtWidth*7,true);
		brickarray[39] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*7,fBrickHeigtWidth*7,true);
		brickarray[40] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*7,fBrickHeigtWidth*5,true);
		brickarray[41] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*7,fBrickHeigtWidth*3,true);
		brickarray[42] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*7,fBrickHeigtWidth,true);
		
		brickarray[43] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*7,-fBrickHeigtWidth,true);
		brickarray[44] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*7,-fBrickHeigtWidth*3,true);
		brickarray[45] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*7,-fBrickHeigtWidth*5,true);
		brickarray[46] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*7,-fBrickHeigtWidth*7,true);
		brickarray[47] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*5,-fBrickHeigtWidth*7,true);
		brickarray[48] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth*3,-fBrickHeigtWidth*7,true);
		brickarray[49] = new Brick(fBrickHeigtWidth,0,fBrickHeigtWidth,-fBrickHeigtWidth*7,true);
		
		brickarray[50] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth,-fBrickHeigtWidth*7,true);
		brickarray[51] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*3,-fBrickHeigtWidth*7,true);
		brickarray[52] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*5,-fBrickHeigtWidth*7,true);
		brickarray[53] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*7,-fBrickHeigtWidth*7,true);
		brickarray[54] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*7,-fBrickHeigtWidth*5,true);
		brickarray[55] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*7,-fBrickHeigtWidth*3,true);
		brickarray[56] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*7,-fBrickHeigtWidth,true);
		
		brickarray[57] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*7,fBrickHeigtWidth,true);
		brickarray[58] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*7,fBrickHeigtWidth*3,true);
		brickarray[59] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*7,fBrickHeigtWidth*5,true);
		brickarray[60] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*7,fBrickHeigtWidth*7,true);
		brickarray[61] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*5,fBrickHeigtWidth*7,true);
		brickarray[62] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth*3,fBrickHeigtWidth*7,true);
		brickarray[63] = new Brick(fBrickHeigtWidth,0,-fBrickHeigtWidth,fBrickHeigtWidth*7,true);
		
	}

	private void DrawTimer()
	{		
			// Draw first minute digit
	        // Translate the cube into the screen.
	        Matrix.setIdentityM(mModelMatrix, 0);       
	        
	        // Set a matrix that contains the current rotation.
	        Matrix.setIdentityM(mCurrentRotation, 0);      
	        
	    	// Set the active texture unit to texture unit 0.
	        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	        
	        // Calculate texture
	        // Bind the texture to this unit.
	        switch(minutes/10)
	        {
		        case 0:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberZeroTextureHandle);
		        break;	
		        case 1:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberOneTextureHandle);
		        break;
		        case 2:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberTwoTextureHandle);
		        break;
		        case 3:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberThreeTextureHandle);
		        break;
		        case 4:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberFourTextureHandle);
		        break;
		        case 5:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberFiveTextureHandle);
		        break;
		        case 6:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberSixTextureHandle);
		        break;
		        case 7:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberSevenTextureHandle);
		        break;
		        case 8:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberEightTextureHandle);
		        break;
		        case 9:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberNineTextureHandle);
		        break;
	        }
	        
	        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
	        GLES20.glUniform1i(mTextureUniformHandle, 0);
			
			// Pass in the position information
	        tdh.mMinuteOnePositions.position(0);		
	        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
	        		0, tdh.mMinuteOnePositions);        
	                
	        GLES20.glEnableVertexAttribArray(mPositionHandle);    
	       
	        GLES20.glEnableVertexAttribArray(mNormalHandle);                
	        
			// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
	        // (which currently contains model * view).
	        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
	        
	        // Pass in the modelview matrix.
	        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                
	        
	        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
	        // (which now contains model * view * projection).        
	        Matrix.multiplyMM(mTemporaryMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
	        System.arraycopy(mTemporaryMatrix, 0, mMVPMatrix, 0, 16);

	        // Pass in the combined matrix.
	        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);       
	       
	        
	        // Draw second minute digit
	        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 8); 
	        /////////////////////////////////////////////////////////////////////////////////////
	        // Draw a paddle.
	        // Translate the cube into the screen.
	        Matrix.setIdentityM(mModelMatrix, 0);       
	        
	        // Set a matrix that contains the current rotation.
	        Matrix.setIdentityM(mCurrentRotation, 0);      
	        
	    	// Set the active texture unit to texture unit 0.
	        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	        
	        // Calculate texture
	        // Bind the texture to this unit.
	        switch(minutes%10)
	        {
		        case 0:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberZeroTextureHandle);
		        break;	
		        case 1:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberOneTextureHandle);
		        break;
		        case 2:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberTwoTextureHandle);
		        break;
		        case 3:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberThreeTextureHandle);
		        break;
		        case 4:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberFourTextureHandle);
		        break;
		        case 5:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberFiveTextureHandle);
		        break;
		        case 6:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberSixTextureHandle);
		        break;
		        case 7:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberSevenTextureHandle);
		        break;
		        case 8:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberEightTextureHandle);
		        break;
		        case 9:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberNineTextureHandle);
		        break;
	        }
	        
	        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
	        GLES20.glUniform1i(mTextureUniformHandle, 0);
			
			// Pass in the position information
	        tdh.mMinuteTwoPositions.position(0);		
	        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
	        		0, tdh.mMinuteTwoPositions);        
	                
	        GLES20.glEnableVertexAttribArray(mPositionHandle);    
	       
	        GLES20.glEnableVertexAttribArray(mNormalHandle);                
	        
			// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
	        // (which currently contains model * view).
	        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
	        
	        // Pass in the modelview matrix.
	        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                
	        
	        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
	        // (which now contains model * view * projection).        
	        Matrix.multiplyMM(mTemporaryMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
	        System.arraycopy(mTemporaryMatrix, 0, mMVPMatrix, 0, 16);

	        // Pass in the combined matrix.
	        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);       
	       
	        
	        // Draw the cube.
	        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 8); 
	        
	        // Draw first second digit
	        /////////////////////////////////////////////////////////////////////////////////////
	        // Draw a paddle.
	        // Translate the cube into the screen.
	        Matrix.setIdentityM(mModelMatrix, 0);       
	        
	        // Set a matrix that contains the current rotation.
	        Matrix.setIdentityM(mCurrentRotation, 0);      
	        
	    	// Set the active texture unit to texture unit 0.
	        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	        
	    	// Calculate texture
	        // Bind the texture to this unit.
	        switch(seconds/10)
	        {
		        case 0:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberZeroTextureHandle);
		        break;	
		        case 1:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberOneTextureHandle);
		        break;
		        case 2:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberTwoTextureHandle);
		        break;
		        case 3:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberThreeTextureHandle);
		        break;
		        case 4:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberFourTextureHandle);
		        break;
		        case 5:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberFiveTextureHandle);
		        break;
		        case 6:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberSixTextureHandle);
		        break;
		        case 7:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberSevenTextureHandle);
		        break;
		        case 8:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberEightTextureHandle);
		        break;
		        case 9:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberNineTextureHandle);
		        break;
	        }
	        
	        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
	        GLES20.glUniform1i(mTextureUniformHandle, 0);
			
			// Pass in the position information
	        tdh.mSecondOnePositions.position(0);		
	        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
	        		0, tdh.mSecondOnePositions);        
	                
	        GLES20.glEnableVertexAttribArray(mPositionHandle);    
	       
	        GLES20.glEnableVertexAttribArray(mNormalHandle);                
	        
			// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
	        // (which currently contains model * view).
	        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
	        
	        // Pass in the modelview matrix.
	        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                
	        
	        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
	        // (which now contains model * view * projection).        
	        Matrix.multiplyMM(mTemporaryMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
	        System.arraycopy(mTemporaryMatrix, 0, mMVPMatrix, 0, 16);

	        // Pass in the combined matrix.
	        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);       
	       
	        
	        // Draw the cube.
	        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 8); 
	        
	        // Draw second second digit
	        /////////////////////////////////////////////////////////////////////////////////////
	        // Draw a paddle.
	        // Translate the cube into the screen.
	        Matrix.setIdentityM(mModelMatrix, 0);       
	        
	        // Set a matrix that contains the current rotation.
	        Matrix.setIdentityM(mCurrentRotation, 0);      
	        
	    	// Set the active texture unit to texture unit 0.
	        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	        
	        // Calculate texture
	        // Bind the texture to this unit.
	        switch(seconds%10)
	        {
		        case 0:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberZeroTextureHandle);
		        break;	
		        case 1:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberOneTextureHandle);
		        break;
		        case 2:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberTwoTextureHandle);
		        break;
		        case 3:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberThreeTextureHandle);
		        break;
		        case 4:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberFourTextureHandle);
		        break;
		        case 5:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberFiveTextureHandle);
		        break;
		        case 6:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberSixTextureHandle);
		        break;
		        case 7:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberSevenTextureHandle);
		        break;
		        case 8:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberEightTextureHandle);
		        break;
		        case 9:
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberNineTextureHandle);
		        break;
	        }
	        
	        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
	        GLES20.glUniform1i(mTextureUniformHandle, 0);
			
			// Pass in the position information
	        tdh.mSecondTwoPositions.position(0);		
	        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
	        		0, tdh.mSecondTwoPositions);        
	                
	        GLES20.glEnableVertexAttribArray(mPositionHandle);    
	       
	        GLES20.glEnableVertexAttribArray(mNormalHandle);                
	        
			// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
	        // (which currently contains model * view).
	        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
	        
	        // Pass in the modelview matrix.
	        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                
	        
	        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
	        // (which now contains model * view * projection).        
	        Matrix.multiplyMM(mTemporaryMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
	        System.arraycopy(mTemporaryMatrix, 0, mMVPMatrix, 0, 16);

	        // Pass in the combined matrix.
	        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);       
	       
	        
	        // Draw the cube.
	        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 8); 
	}

	private void DrawLifeCounter()
	{
		
		// Draw a paddle.
        // Translate the cube into the screen.
        Matrix.setIdentityM(mModelMatrix, 0);       
        
        // Set a matrix that contains the current rotation.
        Matrix.setIdentityM(mCurrentRotation, 0);      
        
    	// Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        // Calculate texture
        // Bind the texture to this unit.
        switch(actualFps/10)
        {
	        case 0:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberZeroTextureHandle);
	        break;	
	        case 1:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberOneTextureHandle);
	        break;
	        case 2:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberTwoTextureHandle);
	        break;
	        case 3:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberThreeTextureHandle);
	        break;
	        case 4:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberFourTextureHandle);
	        break;
	        case 5:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberFiveTextureHandle);
	        break;
	        case 6:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberSixTextureHandle);
	        break;
	        case 7:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberSevenTextureHandle);
	        break;
	        case 8:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberEightTextureHandle);
	        break;
	        case 9:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberNineTextureHandle);
	        break;
        }
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);
		
		// Pass in the position information
        lcdh.mLifeOnePositions.position(0);		
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, lcdh.mLifeOnePositions);        
                
        GLES20.glEnableVertexAttribArray(mPositionHandle);    
       
        GLES20.glEnableVertexAttribArray(mNormalHandle);                
        
		// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
        
        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                
        
        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).        
        Matrix.multiplyMM(mTemporaryMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        System.arraycopy(mTemporaryMatrix, 0, mMVPMatrix, 0, 16);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);       
       
        
        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 8); 
        
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////        

		// Draw a paddle.
        // Translate the cube into the screen.
        Matrix.setIdentityM(mModelMatrix, 0);       
        
        // Set a matrix that contains the current rotation.
        Matrix.setIdentityM(mCurrentRotation, 0);      
        
    	// Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        // Calculate texture
        // Bind the texture to this unit.
        switch(actualFps%10)
        {
	        case 0:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberZeroTextureHandle);
	        break;	
	        case 1:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberOneTextureHandle);
	        break;
	        case 2:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberTwoTextureHandle);
	        break;
	        case 3:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberThreeTextureHandle);
	        break;
	        case 4:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberFourTextureHandle);
	        break;
	        case 5:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberFiveTextureHandle);
	        break;
	        case 6:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberSixTextureHandle);
	        break;
	        case 7:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberSevenTextureHandle);
	        break;
	        case 8:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberEightTextureHandle);
	        break;
	        case 9:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberNineTextureHandle);
	        break;
        }
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);
		
		// Pass in the position information
        lcdh.mLifeTwoPositions.position(0);		
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, lcdh.mLifeTwoPositions);        
                
        GLES20.glEnableVertexAttribArray(mPositionHandle);    
       
        GLES20.glEnableVertexAttribArray(mNormalHandle);                
        
		// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
        
        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                
        
        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).        
        Matrix.multiplyMM(mTemporaryMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        System.arraycopy(mTemporaryMatrix, 0, mMVPMatrix, 0, 16);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);    
        
        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 8); 
	}
	
	private void DrawBackButton()
	{
		// Draw a paddle.
        // Translate the cube into the screen.
        Matrix.setIdentityM(mModelMatrix, 0);       
        
        // Set a matrix that contains the current rotation.
        Matrix.setIdentityM(mCurrentRotation, 0);      
        
    	// Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberNineTextureHandle);
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);
		
		// Pass in the position information
        bbdh.mBackButtonPositions.position(0);		
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, bbdh.mBackButtonPositions);        
                
        GLES20.glEnableVertexAttribArray(mPositionHandle);    
       
        GLES20.glEnableVertexAttribArray(mNormalHandle);                
        
		// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
        
        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                
        
        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).        
        Matrix.multiplyMM(mTemporaryMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        System.arraycopy(mTemporaryMatrix, 0, mMVPMatrix, 0, 16);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);    
        
        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 8); 
	}
	
	private void DrawBackGround()
	{
		// Draw a paddle.
        // Translate the cube into the screen.
        Matrix.setIdentityM(mModelMatrix, 0);       
        
        // Set a matrix that contains the current rotation.
        Matrix.setIdentityM(mCurrentRotation, 0);      
        
    	// Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBackGroundTextureHandle);
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);
		
		// Pass in the position information
        bgdh.mBackGroundPositions.position(0);		
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, bgdh.mBackGroundPositions);        
                
        GLES20.glEnableVertexAttribArray(mPositionHandle);    
       
        GLES20.glEnableVertexAttribArray(mNormalHandle);                
        
		// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
        
        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                
        
        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).        
        Matrix.multiplyMM(mTemporaryMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        System.arraycopy(mTemporaryMatrix, 0, mMVPMatrix, 0, 16);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);    
        
        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 8); 
	}

	private void DrawBoosterBar()
	{			
		// Draw a paddle.
        // Translate the cube into the screen.
        Matrix.setIdentityM(mModelMatrix, 0);       
        
        // Set a matrix that contains the current rotation.
        Matrix.setIdentityM(mCurrentRotation, 0);      
        
    	// Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iNumberZeroTextureHandle);
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);
		
		// Pass in the position information
        bbdh1.mBoosterBarPositions.position(0);		
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, bbdh1.mBoosterBarPositions);        
                
        GLES20.glEnableVertexAttribArray(mPositionHandle);    
       
        GLES20.glEnableVertexAttribArray(mNormalHandle);                
        
		// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
        
        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                
        
        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).        
        Matrix.multiplyMM(mTemporaryMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        System.arraycopy(mTemporaryMatrix, 0, mMVPMatrix, 0, 16);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);    
        
        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 8); 
	}

	private void storeStats() 
	{
	        frameCountPerStatCycle++;
	        totalFrameCount++;
	 
	        // check the actual time
	        statusIntervalTimer += (System.currentTimeMillis() - statusIntervalTimer);
	 
	        if (statusIntervalTimer >= lastStatusStore + STAT_INTERVAL) 
	        {
	            // calculate the actual frames pers status check interval
	            actualFps = (frameCountPerStatCycle / (STAT_INTERVAL / 1000));
	 
	            framesSkippedPerStatCycle = 0;
	            statusIntervalTimer = 0;
	            frameCountPerStatCycle = 0;
	 
	            statusIntervalTimer = System.currentTimeMillis();
	            lastStatusStore = statusIntervalTimer;
        }
	}

	public void GetOGLPos(int _x, int _y)
	{		
		float[] modelView = new float[16];
		float[] projection = new float[16];
		int[] view = {0, 0, screenWidth, screenHeight}; // viewport
		float x = _x, y = _y, z = 0; 
		// those are the inputs
		
		float[] pos = new float[4]; 

		GLU.gluUnProject(x, y, z, mViewMatrix, 0, mProjectionMatrix, 0, view, 0, pos, 0);

		Log.d("","position (" + pos[0] + ", " + -pos[1] + ", " + pos[2] + ")");
			
		
		if(System.currentTimeMillis() - backbuttontimer > 400)
		{
			if(iDisplayIndex == 0)
			{
				if(pos[0] > 0.7f && pos[0] < 1.0f && -pos[1] < -halfx+0.3f && -pos[1] > -halfx)
				{
					backbuttontimer = System.currentTimeMillis();					
						iDisplayIndex = 1;
						return;					
				}	
				if(pos[0] < -halfx+((halfx*2.0f)/5.0f)*2.3f+0.15f && pos[0] > -halfx+((halfx*2.0f)/5.0f)*2.3f-0.15f && -pos[1] < 0.5f && -pos[1] > -0.5f)
				{
					backbuttontimer = System.currentTimeMillis();					
						iDisplayIndex = 2;
						return;					
				}
				
			}
			if(iDisplayIndex == 1)
			{
				if(pos[0] > 0.7f && pos[0] < 1.0f && -pos[1] < -halfx+0.3f && -pos[1] > -halfx)
				{
					backbuttontimer = System.currentTimeMillis();					
						iDisplayIndex = 0;
						return;					
				}	
			}	
			if(iDisplayIndex == 2)
			{
				if(pos[0] > 0.8f && pos[0] < 1.0f && -pos[1] < -halfx+0.5f && -pos[1] > -halfx)
				{
					backbuttontimer = System.currentTimeMillis();					
						iDisplayIndex = 0;
						return;					
				}	
				if(pos[0] > -0.15f && pos[0] < 0.15f && -pos[1] < -halfx+0.3f && -pos[1] > -halfx && swdh.iSelectedWorld>1)
				{
					    backbuttontimer = System.currentTimeMillis();					
					    swdh.iSelectedWorld -= 1;
						return;					
				}
				if(pos[0] > -0.15f && pos[0] < 0.15f && -pos[1] < halfx && -pos[1] > halfx-0.3f && swdh.iSelectedWorld<5 )
				{
					    backbuttontimer = System.currentTimeMillis();					
					    swdh.iSelectedWorld += 1;
						return;					
				}	
				
			}	
			
		}
	}

}