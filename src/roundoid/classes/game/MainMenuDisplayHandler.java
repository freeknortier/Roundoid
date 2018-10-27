package roundoid.classes.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import roundoid.classes.game.common.TextureHelper;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class MainMenuDisplayHandler {
	
	private final int mBytesPerFloat = 4;
	
	int screenHeight;
	int screenWidth;
	float halfx;
	int iPlayButtonHandle;
	int iSettingsButtonHandle;
	int iScoresButtonHandle;
	
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
	
	//MainMenu SettingsButtonBuffers
	public final FloatBuffer mMainMenuSettingsButtonPositions;	
	public final FloatBuffer mMainMenuSettingsButtonNormals;
	public final FloatBuffer mMainMenuSettingsButtonTextureCoordinates;	
	
	//MainMenu LeaderBoardButtonBuffers
	public final FloatBuffer mMainMenuLeaderBoardButtonPositions;	
	public final FloatBuffer mMainMenuLeaderBoardButtonNormals;
	public final FloatBuffer mMainMenuLeaderBoardButtonTextureCoordinates;	
	
		
	public MainMenuDisplayHandler(int _screenHeight, int _screenWidth, Context _mActivityContext)
	{
		
		screenHeight = _screenHeight;
		screenWidth = _screenWidth;
		halfx = (float)_screenHeight/(float)_screenWidth;		
		
		bgdh = new BackGroundDisplayHandler(screenHeight,screenWidth);		
		
		iPlayButtonHandle = TextureHelper.loadTexture(_mActivityContext, R.drawable.playbutton);
		iSettingsButtonHandle = TextureHelper.loadTexture(_mActivityContext, R.drawable.settingsbutton);
		iScoresButtonHandle = TextureHelper.loadTexture(_mActivityContext, R.drawable.scoressutton);
		
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
		
		mMainMenuPlayButtonTextureCoordinates  = ByteBuffer.allocateDirect(MainMenuPlayButtonTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMainMenuPlayButtonTextureCoordinates.put(MainMenuPlayButtonTextureCoordinates).position(0);
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Settings Button Buffers
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		float fSettingsButtonMiddleLine = -halfx+((halfx*2.0f)/5.0f)*3.1f;
		float fSettingsButtonHeight = 0.15f;
		float fSettingsButtonWidth = 0.5f;
		
		final float[] MainMenuSettingsButtonPositions =
			{		
				fSettingsButtonMiddleLine-fSettingsButtonHeight, -fSettingsButtonWidth, 0.0f,
				fSettingsButtonMiddleLine+fSettingsButtonHeight, -fSettingsButtonWidth, 0.0f,	
				fSettingsButtonMiddleLine-fSettingsButtonHeight, fSettingsButtonWidth, 0.0f,		
				fSettingsButtonMiddleLine+fSettingsButtonHeight, -fSettingsButtonWidth, 0.0f,				
				fSettingsButtonMiddleLine+fSettingsButtonHeight, fSettingsButtonWidth, 0.0f,
				fSettingsButtonMiddleLine-fSettingsButtonHeight, fSettingsButtonWidth, 0.0f,		
					
			};		
		final float[] MainMenuSettingsButtonNormals = 
			{
				// Front face
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
			};
		final float[] MainMenuSettingsButtonTextureCoordinates = 
			{				
				0.0f, 0.0f,			
				0.0f, 1.0f, 
				1.0f, 0.0f,
				0.0f, 1.0f, 
				1.0f, 1.0f,
				1.0f, 0.0f,
			};
		
		mMainMenuSettingsButtonPositions  = ByteBuffer.allocateDirect(MainMenuSettingsButtonPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMainMenuSettingsButtonPositions.put(MainMenuSettingsButtonPositions).position(0);	
		
		mMainMenuSettingsButtonNormals  = ByteBuffer.allocateDirect(MainMenuSettingsButtonNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMainMenuSettingsButtonNormals.put(MainMenuSettingsButtonNormals).position(0);	
		
		mMainMenuSettingsButtonTextureCoordinates  = ByteBuffer.allocateDirect(MainMenuSettingsButtonTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMainMenuSettingsButtonTextureCoordinates.put(MainMenuSettingsButtonTextureCoordinates).position(0);
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Leader Board Button Buffers
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		float fLeaderBoardButtonMiddleLine = -halfx+((halfx*2.0f)/5.0f)*3.9f;
		float fLeaderBoardButtonHeight = 0.15f;
		float fLeaderBoardButtonWidth = 0.5f;
		
		final float[] MainMenuLeaderBoardButtonPositions =
			{		
				fLeaderBoardButtonMiddleLine-fLeaderBoardButtonHeight, -fLeaderBoardButtonWidth, 0.0f,
				fLeaderBoardButtonMiddleLine+fLeaderBoardButtonHeight, -fLeaderBoardButtonWidth, 0.0f,	
				fLeaderBoardButtonMiddleLine-fLeaderBoardButtonHeight, fLeaderBoardButtonWidth, 0.0f,		
				fLeaderBoardButtonMiddleLine+fLeaderBoardButtonHeight, -fLeaderBoardButtonWidth, 0.0f,				
				fLeaderBoardButtonMiddleLine+fLeaderBoardButtonHeight, fLeaderBoardButtonWidth, 0.0f,
				fLeaderBoardButtonMiddleLine-fLeaderBoardButtonHeight, fLeaderBoardButtonWidth, 0.0f,		
					
			};		
		final float[] MainMenuLeaderBoardButtonNormals = 
			{
				// Front face
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
			};
		final float[] MainMenuLeaderBoardButtonTextureCoordinates = 
			{		
				0.0f, 0.0f,			
				0.0f, 1.0f, 
				1.0f, 0.0f,
				0.0f, 1.0f, 
				1.0f, 1.0f,
				1.0f, 0.0f,
			};
		
		mMainMenuLeaderBoardButtonPositions  = ByteBuffer.allocateDirect(MainMenuLeaderBoardButtonPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMainMenuLeaderBoardButtonPositions.put(MainMenuLeaderBoardButtonPositions).position(0);	
		
		mMainMenuLeaderBoardButtonNormals  = ByteBuffer.allocateDirect(MainMenuLeaderBoardButtonNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMainMenuLeaderBoardButtonNormals.put(MainMenuLeaderBoardButtonNormals).position(0);	
		
		mMainMenuLeaderBoardButtonTextureCoordinates  = ByteBuffer.allocateDirect(MainMenuLeaderBoardButtonTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMainMenuLeaderBoardButtonTextureCoordinates.put(MainMenuLeaderBoardButtonTextureCoordinates).position(0);
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////
				
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
        
    	// Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, _mBackGroundTextureHandle);
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(_mTextureUniformHandle, 0);
		
		// Pass in the position information
        bgdh.mBackGroundPositions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, bgdh.mBackGroundPositions);        
                
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
        ////////////////////////////////////////////////////////////////////////////////////////////  
        
        
        //Draw Main Menu Play Button
      	////////////////////////////////////////////////////////////////////////////////////////////
        Matrix.setIdentityM(_mModelMatrix, 0);       
        
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iPlayButtonHandle);
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(_mTextureUniformHandle, 0);
        
        mMainMenuPlayButtonTextureCoordinates.position(0);
        
        // Pass in the texture coordinate information
        mMainMenuPlayButtonTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(_mTextureCoordinateHandle, _mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mMainMenuPlayButtonTextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(_mTextureCoordinateHandle);        
      
        		// Pass in the position information
        mMainMenuPlayButtonPositions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mMainMenuPlayButtonPositions);        
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);                       
                
        // Pass in the normal information
        mMainMenuPlayButtonNormals.position(0);
        GLES20.glVertexAttribPointer(_mNormalHandle, _mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mMainMenuPlayButtonNormals);
        
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
        
        // Draw the ball.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 8); 
        
        ////////////////////////////////////////////////////////////////////////////////////////////        
        
        //Draw Main Menu Settings Button
      	////////////////////////////////////////////////////////////////////////////////////////////
        Matrix.setIdentityM(_mModelMatrix, 0);       
        
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iSettingsButtonHandle);
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(_mTextureUniformHandle, 0);
        
        mMainMenuSettingsButtonTextureCoordinates.position(0);
        
        // Pass in the texture coordinate information
        mMainMenuSettingsButtonTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(_mTextureCoordinateHandle, _mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mMainMenuSettingsButtonTextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(_mTextureCoordinateHandle);        
      
        		// Pass in the position information
        mMainMenuSettingsButtonPositions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mMainMenuSettingsButtonPositions);        
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);                       
                
        // Pass in the normal information
        mMainMenuSettingsButtonNormals.position(0);
        GLES20.glVertexAttribPointer(_mNormalHandle, _mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mMainMenuSettingsButtonNormals);
        
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
        
        // Draw the ball.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 8); 
        
        ////////////////////////////////////////////////////////////////////////////////////////////   
        
        //Draw Main Menu Leader Board Button
      	////////////////////////////////////////////////////////////////////////////////////////////
        Matrix.setIdentityM(_mModelMatrix, 0);       
        
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iScoresButtonHandle);
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(_mTextureUniformHandle, 0);
        
        mMainMenuLeaderBoardButtonTextureCoordinates.position(0);
        
        // Pass in the texture coordinate information
        mMainMenuLeaderBoardButtonTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(_mTextureCoordinateHandle, _mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mMainMenuLeaderBoardButtonTextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(_mTextureCoordinateHandle);        
      
        		// Pass in the position information
        mMainMenuLeaderBoardButtonPositions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mMainMenuLeaderBoardButtonPositions);        
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);                       
                
        // Pass in the normal information
        mMainMenuLeaderBoardButtonNormals.position(0);
        GLES20.glVertexAttribPointer(_mNormalHandle, _mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mMainMenuLeaderBoardButtonNormals);
        
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
        
        // Draw the ball.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 8); 
        
        ////////////////////////////////////////////////////////////////////////////////////////////          
             
	}

}
