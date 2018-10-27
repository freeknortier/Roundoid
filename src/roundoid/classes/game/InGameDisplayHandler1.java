package roundoid.classes.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import roundoid.classes.game.common.TextureHelper;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class InGameDisplayHandler1 {
	
	private final int mBytesPerFloat = 4;
	
	int screenHeight;
	int screenWidth;
	float halfx;
	int iPlayButtonHandle;
	int iSettingsButtonHandle;
	int iScoresButtonHandle;
	private int mBallTextureHandle;
	private int mTextureUniformHandle;	
	private int mPositionHandle;
	private final int mPositionDataSize = 3;
	float fBallWidthHeight = 0.05f;	
	
	BackGroundDisplayHandler bgdh; 
	
	//MainMenu Background Buffers
	public final FloatBuffer mMainMenuBackGroundPositions;	
	public final FloatBuffer mMainMenuBackGroundNormals;
	public final FloatBuffer mMainMenuBackGroundTextureCoordinates;
	
	//MainMenu HeaderBuffers
	public final FloatBuffer mMainMenuHeaderPositions;	
	public final FloatBuffer mMainMenuHeaderNormals;
	public final FloatBuffer mMainMenuHeaderTextureCoordinates;
	
	//MainMenu PlayButtonBuffers
	public final FloatBuffer mMainMenuPlayButtonPositions;	
	public final FloatBuffer mMainMenuPlayButtonNormals;
	public final FloatBuffer mMainMenuPlayButtonTextureCoordinates;	
	
	//Ball Buffers
	private final FloatBuffer mBallPositions;	
	private final FloatBuffer mBallNormals;
	private final FloatBuffer mBallTextureCoordinates;

	public static int iSelectedBackGround = 2;
	private int mBackGroundTextureHandle1;
	private int mBackGroundTextureHandle2;
	private int mBackGroundTextureHandle3;
	private int mBackGroundTextureHandle4;
	private int mBackGroundTextureHandle5;
	
		
	public InGameDisplayHandler1(int _screenHeight, int _screenWidth, Context _mActivityContext)
	{
		
		screenHeight = _screenHeight;
		screenWidth = _screenWidth;
		halfx = (float)_screenHeight/(float)_screenWidth;		
		
		bgdh = new BackGroundDisplayHandler(screenHeight,screenWidth);		
		
		iPlayButtonHandle = TextureHelper.loadTexture(_mActivityContext, R.drawable.playbutton);
		iSettingsButtonHandle = TextureHelper.loadTexture(_mActivityContext, R.drawable.settingsbutton);
		iScoresButtonHandle = TextureHelper.loadTexture(_mActivityContext, R.drawable.scoressutton);
	    mBackGroundTextureHandle1 = TextureHelper.loadTexture(_mActivityContext, R.drawable.backgroundsnow);        
	    mBackGroundTextureHandle2 = TextureHelper.loadTexture(_mActivityContext, R.drawable.junglebackground);        	        
	    mBackGroundTextureHandle3 = TextureHelper.loadTexture(_mActivityContext, R.drawable.desertbackground);        
	    mBackGroundTextureHandle4 = TextureHelper.loadTexture(_mActivityContext, R.drawable.citybackground);        
	    mBackGroundTextureHandle5 = TextureHelper.loadTexture(_mActivityContext, R.drawable.spacebackground);
        		
		//Background buffers	
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		final float[] MainMenuBackGroundPositions =
			{		
				 -1.0f, -halfx, 0.0f,
			      1.0f, -halfx, 0.0f,	
				 -1.0f, halfx, 0.0f,		
				  1.0f, -halfx, 0.0f,				
				  1.0f, halfx, 0.0f,
				 -1.0f, halfx, 0.0f,		
					
			};		
		final float[] MainMenuBackGroundNormals = 
			{
				// Front face
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
			};
		final float[] MainMenuBackGroundTextureCoordinates = 
			{				
				0.0f, 0.0f,			
				1.0f, 0.0f, 
				0.0f, 1.0f,
				1.0f, 0.0f, 
				1.0f, 1.0f,
				0.0f, 1.0f,
			};
		
		mMainMenuBackGroundPositions  = ByteBuffer.allocateDirect(MainMenuBackGroundPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMainMenuBackGroundPositions.put(MainMenuBackGroundPositions).position(0);	
		
		mMainMenuBackGroundNormals  = ByteBuffer.allocateDirect(MainMenuBackGroundNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMainMenuBackGroundNormals.put(MainMenuBackGroundNormals).position(0);	
		
		mMainMenuBackGroundTextureCoordinates  = ByteBuffer.allocateDirect(MainMenuBackGroundTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMainMenuBackGroundTextureCoordinates.put(MainMenuBackGroundTextureCoordinates).position(0);
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Header buffers
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		
		float fHeaderMiddleLine = -halfx+(halfx*2.0f)/4.0f;
		float fHeader = 0.25f;
		
		final float[] MainMenuHeaderPositions =
			{		
				 fHeaderMiddleLine-fHeader, -1.0f, 0.0f,
				 fHeaderMiddleLine+fHeader, -1.0f, 0.0f,	
				 fHeaderMiddleLine-fHeader, 1.0f, 0.0f,		
				 fHeaderMiddleLine+fHeader, -1.0f, 0.0f,				
				 fHeaderMiddleLine+fHeader, 1.0f, 0.0f,
				 fHeaderMiddleLine-fHeader, 1.0f, 0.0f,		
					
			};		
		final float[] MainMenuHeaderNormals = 
			{
				// Front face
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
			};
		final float[] MainMenuHeaderTextureCoordinates = 
			{				
				1.0f, 0.0f,			
				0.0f, 0.0f, 
				1.0f, 1.0f,
				0.0f, 0.0f, 
				0.0f, 1.0f,
				1.0f, 1.0f,
			};
		
		mMainMenuHeaderPositions  = ByteBuffer.allocateDirect(MainMenuHeaderPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMainMenuHeaderPositions.put(MainMenuHeaderPositions).position(0);	
		
		mMainMenuHeaderNormals  = ByteBuffer.allocateDirect(MainMenuHeaderNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMainMenuHeaderNormals.put(MainMenuHeaderNormals).position(0);	
		
		mMainMenuHeaderTextureCoordinates  = ByteBuffer.allocateDirect(MainMenuHeaderTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMainMenuHeaderTextureCoordinates.put(MainMenuHeaderTextureCoordinates).position(0);
		//////////////////////////////////////////////////////////////////////////////////////////////////////
				
		//Play Button Buffers
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		float fPlayButtonMiddleLine = -halfx+((halfx*2.0f)/5.0f)*2.3f;
		float fPlayButtonHeight = 0.15f;
		float fPlayButtonWidth = 0.5f;
		
		final float[] MainMenuPlayButtonPositions =
			{		
				fPlayButtonMiddleLine-fPlayButtonHeight, -fPlayButtonWidth, 0.0f,
				fPlayButtonMiddleLine+fPlayButtonHeight, -fPlayButtonWidth, 0.0f,	
				fPlayButtonMiddleLine-fPlayButtonHeight, fPlayButtonWidth, 0.0f,		
				fPlayButtonMiddleLine+fPlayButtonHeight, -fPlayButtonWidth, 0.0f,				
				fPlayButtonMiddleLine+fPlayButtonHeight, fPlayButtonWidth, 0.0f,
				fPlayButtonMiddleLine-fPlayButtonHeight, fPlayButtonWidth, 0.0f,		
					
			};		
		final float[] MainMenuPlayButtonNormals = 
			{
				// Front face
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
			};
		final float[] MainMenuPlayButtonTextureCoordinates = 
			{				
				0.0f, 0.0f,			
				0.0f, 1.0f, 
				1.0f, 0.0f,
				0.0f, 1.0f, 
				1.0f, 1.0f,
				1.0f, 0.0f,
			};
		
		mMainMenuPlayButtonPositions  = ByteBuffer.allocateDirect(MainMenuPlayButtonPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMainMenuPlayButtonPositions.put(MainMenuPlayButtonPositions).position(0);	
		
		mMainMenuPlayButtonNormals  = ByteBuffer.allocateDirect(MainMenuPlayButtonNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMainMenuPlayButtonNormals.put(MainMenuPlayButtonNormals).position(0);	
		
		bgdh.mBackGroundNormals  = ByteBuffer.allocateDirect(MainMenuPlayButtonNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		bgdh.mBackGroundNormals.put(MainMenuPlayButtonNormals).position(0);
		 
		
		mMainMenuPlayButtonTextureCoordinates  = ByteBuffer.allocateDirect(MainMenuPlayButtonTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMainMenuPlayButtonTextureCoordinates.put(MainMenuPlayButtonTextureCoordinates).position(0);
		
		bgdh.mBackGroundTextureCoordinates  = ByteBuffer.allocateDirect(MainMenuPlayButtonTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		bgdh.mBackGroundTextureCoordinates.put(MainMenuPlayButtonTextureCoordinates).position(0);
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Ball Buffers
		//////////////////////////////////////////////////////////////////////////////////////////////////////
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
		mBallNormals = ByteBuffer.allocateDirect(ballNormalData.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();
		mBallNormals.put(ballNormalData).position(0);
		
		mBallTextureCoordinates = ByteBuffer.allocateDirect(ballTextureCoordinateData.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();
		mBallTextureCoordinates.put(ballTextureCoordinateData).position(0);
		
				
	}
			
	public void Draw(int _iProgramHandle, int _mMVPMatrixHandle, int _mMVMatrixHandle,int _mTextureUniformHandle,int _mPositionHandle,int _mNormalHandle,int _mTextureCoordinateHandle, float[] _mModelMatrix,int _mBackGroundTextureHandle,int _mTextureCoordinateDataSize,int _mPositionDataSize,int _mNormalDataSize,float[] _mMVPMatrix, float[] _mViewMatrix,float[] _mTemporaryMatrix,float[] _mProjectionMatrix,int _iNumberZeroTextureHandle)
	{		
		//Setup
		////////////////////////////////////////////////////////////////////////////////////////////
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);	       
        // Set our per-vertex lighting program.
        GLES20.glUseProgram(_iProgramHandle);        
        // Set program handles for drawing.
        _mMVPMatrixHandle = GLES20.glGetUniformLocation(_iProgramHandle, "u_MVPMatrix");
        _mMVMatrixHandle = GLES20.glGetUniformLocation(_iProgramHandle, "u_MVMatrix"); 
        _mTextureUniformHandle = GLES20.glGetUniformLocation(_iProgramHandle, "u_Texture");
        _mPositionHandle = GLES20.glGetAttribLocation(_iProgramHandle, "a_Position");        
        _mNormalHandle = GLES20.glGetAttribLocation(_iProgramHandle, "a_Normal"); 
        _mTextureCoordinateHandle = GLES20.glGetAttribLocation(_iProgramHandle, "a_TexCoordinate");     
        ////////////////////////////////////////////////////////////////////////////////////////////

        //Draw Main Menu BackGround
      	////////////////////////////////////////////////////////////////////////////////////////////
        // Draw a paddle.
        // Translate the cube into the screen.
        Matrix.setIdentityM(_mModelMatrix, 0);   
        
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
        GLES20.glUniform1i(_mTextureUniformHandle, 0);
		
		// Pass in the position information
        bgdh.mBackGroundPositions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, bgdh.mBackGroundPositions);      
        
        bgdh.mBackGroundNormals.position(0);
        GLES20.glVertexAttribPointer(_mNormalHandle, _mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, bgdh.mBackGroundNormals);
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);    
       
        GLES20.glEnableVertexAttribArray(_mNormalHandle);                
        
        bgdh.mBackGroundTextureCoordinates.position(0);
        
        // Pass in the texture coordinate information
        bgdh.mBackGroundTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(_mTextureCoordinateHandle, _mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, bgdh.mBackGroundTextureCoordinates);        
        GLES20.glEnableVertexAttribArray(_mTextureCoordinateHandle);    
        
		// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(_mMVPMatrix, 0, _mViewMatrix, 0, _mModelMatrix, 0);   
        
        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(_mMVMatrixHandle, 1, false, _mMVPMatrix, 0);                
        
        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).        
        Matrix.multiplyMM(_mTemporaryMatrix, 0, _mProjectionMatrix, 0, _mMVPMatrix, 0);
        System.arraycopy(_mTemporaryMatrix, 0, _mMVPMatrix, 0, 16);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(_mMVPMatrixHandle, 1, false, _mMVPMatrix, 0);    
        
        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 8);         
        ////////////////////////////////////////////////////////////////////////////////////////////
        
        //DrawBall
        ////////////////////////////////////////////////////////////////////////////////////////////
        Matrix.setIdentityM(_mModelMatrix, 0);
        Matrix.translateM(_mModelMatrix, 0, Speed.fXCoordinate3, Speed.fYCoordinate3, 0.0f); 
              
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
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);    
       
        GLES20.glEnableVertexAttribArray(_mNormalHandle);                
        
		// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(_mMVPMatrix, 0, _mViewMatrix, 0, _mModelMatrix, 0);   
        
        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(_mMVMatrixHandle, 1, false, _mMVPMatrix, 0);                
        
        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).        
        Matrix.multiplyMM(_mTemporaryMatrix, 0, _mProjectionMatrix, 0, _mMVPMatrix, 0);
        System.arraycopy(_mTemporaryMatrix, 0, _mMVPMatrix, 0, 16);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(_mMVPMatrixHandle, 1, false, _mMVPMatrix, 0);
              
        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 8); 
                                 
	}

}
