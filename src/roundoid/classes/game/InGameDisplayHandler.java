package roundoid.classes.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;

import roundoid.classes.game.common.TextureHelper;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class InGameDisplayHandler {
		
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
	
	private float[] mModelMatrix = new float[16];
	private final float[] mCurrentRotation = new float[16];
	public volatile float mDeltaX;					
	public volatile float mDeltaY;	
	private float[] mTemporaryMatrix = new float[16];
	private final float[] mAccumulatedRotation = new float[16];
	private int mPaddleTextureHandle;
	private int mTextureUniformHandle;	
	private int mTextureCoordinateHandle;
	private final int mTextureCoordinateDataSize = 2;
	private final FloatBuffer mPaddlePositionsLeft;
	private final FloatBuffer mPaddlePositionsRight;
	private final FloatBuffer mPaddlePositionsBottom;
	private final FloatBuffer mPaddlePositionsTop;
	private final FloatBuffer mBallPositions;	
	private final FloatBuffer mBallNormals;
	private final FloatBuffer mBallTextureCoordinates;
	private final int mPositionDataSize = 3;
	private int mNormalHandle;
	private final int mNormalDataSize = 3;	
	private int mPositionHandle;
	private float[] mMVPMatrix = new float[16];
	private float[] mViewMatrix = new float[16];
	private int mMVMatrixHandle;
	private float[] mProjectionMatrix = new float[16];
	//private int mMVPMatrixHandle;
	private int mProgramHandle;
	Brick[] brickarray = new Brick[64];
	Paddle leftPaddle = new Paddle();
	Paddle rightPaddle = new Paddle();
	Paddle topPaddle = new Paddle();
	Paddle bottomPaddle = new Paddle();
	//Brick model data in a float buffer
	private final FloatBuffer mBrickPositions;	
	private final FloatBuffer mBrickNormals;
	private final FloatBuffer mBrickTextureCoordinates;
	float fOffSet = 0.95f;
	float fPaddleHeight = 0.16f;
	float fPaddleWidth = 0.026f;
	float fBallWidthHeight = 0.05f;	
	final float fBrickHeigtWidth = 0.05f;	
	private final int mBytesPerFloat = 4;	
	private int mBrickTextureHandle;
	private int mQueuedMinFilter;
	private int mQueuedMagFilter;	
	private int mBallTextureHandle;
	BrickHandler bricks = new BrickHandler(fBrickHeigtWidth);
	private final Context mActivityContext;
	BackGroundDisplayHandler bgdh; 	
	private int iProgramHandle;
	private int mMVPMatrixHandle;		
	BackButtonDisplayHandler bbdh;
	TimerDisplayHandler tdh;
	LifeCountDisplayHandler lcdh = new LifeCountDisplayHandler(); 
	BoosterBarDisplayHandler bbdh1 = new BoosterBarDisplayHandler();
	float[] outVector1 = new float[4];   
	float[] outVector2 = new float[4]; 
	float[] outVector3 = new float[4];  
	float[] outVectortestTop = new float[4];	
	float[] outVectortestBottom = new float[4];
	long ballpaddlecollisiondetectbuffer;
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
	
	public long millis;
	public int seconds;
	public int minutes;

	private int actualFps = 0;
	
	public static int iSelectedBackGround;
	private int mBackGroundTextureHandle1;
	private int mBackGroundTextureHandle2;
	private int mBackGroundTextureHandle3;
	private int mBackGroundTextureHandle4;
	private int mBackGroundTextureHandle5;
	
	InGameDisplayHandler(int _screenHeight, int _screenWidth, Context _mActivityContext)
	{
		mActivityContext = _mActivityContext;
		bgdh = new BackGroundDisplayHandler(_screenHeight,_screenWidth);		
		
		InitializeBricks();
		
		bbdh = new BackButtonDisplayHandler(_screenHeight,_screenWidth);
		tdh = new TimerDisplayHandler();
		
		Speed.fXCoordinate3 = 0.7f;
		Speed.fYCoordinate3 = 0.0f;		
				
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
				
			    mBallTextureHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.ball);
		        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);        
		        mPaddleTextureHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.paddle);
		        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
		        mBrickTextureHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.brick);
		        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
		        
		        OnSurfaceCreated();
	}
	
	public void SetVariables(int _iProgramHandle, int _mMVPMatrixHandle, int _mMVMatrixHandle,int _mTextureUniformHandle,int _mPositionHandle,int _mNormalHandle,int _mTextureCoordinateHandle, float[] _mModelMatrix,int _mBackGroundTextureHandle,int _mTextureCoordinateDataSize,int _mPositionDataSize,int _mNormalDataSize,float[] _mMVPMatrix, float[] _mViewMatrix,float[] _mTemporaryMatrix,float[] _mProjectionMatrix,int _iNumberZeroTextureHandle)
	{
		iProgramHandle = _iProgramHandle;
		mMVPMatrixHandle = _mMVPMatrixHandle;
		mMVMatrixHandle = _mMVMatrixHandle;
		mTextureUniformHandle = _mTextureUniformHandle;
		mPositionHandle = _mPositionHandle;
		mNormalHandle = _mNormalHandle;
		mTextureCoordinateHandle = _mTextureCoordinateHandle;
		mModelMatrix = _mModelMatrix;
		//mBackGroundTextureHandle = _mBackGroundTextureHandle;
		//mTextureCoordinateDataSize = _mTextureCoordinateDataSize;
		//mPositionDataSize = _mPositionDataSize;
		//mNormalDataSize = _mNormalDataSize;
		mMVPMatrix = _mMVPMatrix;
		mViewMatrix = _mViewMatrix;
		mTemporaryMatrix = _mTemporaryMatrix;
		mProjectionMatrix = _mProjectionMatrix;
		//_iNumberZeroTextureHandle = _iNumberZeroTextureHandle;
	}
	
	public void Draw()
	{						
        DrawBackGround();     
        DrawPaddleTop();
        DrawPaddleLeft();        
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
        //DrawBrickArray();            
        DrawTimer();
        DrawLifeCounter();      
        DrawBoosterBar(); 
        DrawBackButton();    
	}	
	private void DrawBackGround()
	{
		  //Draw Main Menu BackGround
      	////////////////////////////////////////////////////////////////////////////////////////////
        // Draw a paddle.
        // Translate the cube into the screen.
        Matrix.setIdentityM(mModelMatrix, 0);   
        
    	// Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        // Calculate texture
        // Bind the texture to this unit.
        switch(iSelectedBackGround)
        {
	        case 1:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBackGroundTextureHandle1);
	        break;	
	        case 2:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBackGroundTextureHandle2);
	        break;
	        case 3:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBackGroundTextureHandle3);
	        break;
	        case 4:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBackGroundTextureHandle4);
	        break;
	        case 5:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBackGroundTextureHandle5);
	        break;
	        
        }
        
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
        ////////////////////////////////////////////////////////////////////////////////////////////   		
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
              
        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 8); 
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
	public void OnSurfaceCreated()
	{
		  mBackGroundTextureHandle1 = TextureHelper.loadTexture(mActivityContext, R.drawable.backgroundsnow);
	        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
		  mBackGroundTextureHandle2 = TextureHelper.loadTexture(mActivityContext, R.drawable.junglebackground);
	        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);	        
		  mBackGroundTextureHandle3 = TextureHelper.loadTexture(mActivityContext, R.drawable.desertbackground);
	        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
		  mBackGroundTextureHandle4 = TextureHelper.loadTexture(mActivityContext, R.drawable.citybackground);
	        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
		  mBackGroundTextureHandle5 = TextureHelper.loadTexture(mActivityContext, R.drawable.spacebackground);
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
	        
		 //beter graphics hier       
        if (mQueuedMinFilter != 0)
        {
        	setMinFilter(mQueuedMinFilter);
        }
        
        if (mQueuedMagFilter != 0)
        {
        	setMagFilter(mQueuedMagFilter);
        }
	}
	public void CalculateBrickArrayIntersections()
	{/*
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
		}*/
	}
	
}
