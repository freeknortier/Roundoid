package roundoid.classes.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import roundoid.classes.game.common.TextureHelper;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class SelectWorldDisplayHandler {

private final int mBytesPerFloat = 4;
	
	int screenHeight;
	int screenWidth;
	float halfx;
	int iPlayButtonHandle;
	int iSettingsButtonHandle;
	int iBackButtonHandle;
	int iBackHandle;
	int iForwardHandle;
	
	int i1;
	int i2;
	int i3;
	int i4;
	int i5;
		
	public int iSelectedWorld = 1;
	
	BackGroundDisplayHandler bgdh; 
	
	//MainMenu Background Buffers
	public FloatBuffer mMainMenuBackGroundPositions;	
	public FloatBuffer mMainMenuBackGroundNormals;
	public FloatBuffer mMainMenuBackGroundTextureCoordinates;
	
	//MainMenu HeaderBuffers
	public FloatBuffer mMainMenuHeaderPositions;	
	public FloatBuffer mMainMenuHeaderNormals;
	public FloatBuffer mMainMenuHeaderTextureCoordinates;	
	
	//Select World Back Button Buffers
	public  FloatBuffer mSelectWorldBackButtonPositions;	
	public  FloatBuffer mSelectWorldBackButtonNormals;
	public  FloatBuffer mSelectWorldBackButtonTextureCoordinates;

	//Select World Back Buffers
	public  FloatBuffer mSelectWorldBackPositions;	
	public  FloatBuffer mSelectWorldBackNormals;
	public  FloatBuffer mSelectWorldBackTextureCoordinates;
	
	//Select World Forward Buffers
	public  FloatBuffer mSelectWorldForwardPositions;	
	public  FloatBuffer mSelectWorldForwardNormals;
	public  FloatBuffer mSelectWorldForwardTextureCoordinates;
	
	//Select World Forward Buffers
	public  FloatBuffer mSelectWorldPositions;	
	public  FloatBuffer mSelectWorldNormals;
	public  FloatBuffer mSelectWorldTextureCoordinates;
	
	public static int iSelectedBackGround = 1;
	private int mBackGroundTextureHandle1;
	private int mBackGroundTextureHandle2;
	private int mBackGroundTextureHandle3;
	private int mBackGroundTextureHandle4;
	private int mBackGroundTextureHandle5;
	
	
	public SelectWorldDisplayHandler(int _screenHeight, int _screenWidth, Context _mActivityContext)
	{
		screenHeight = _screenHeight;
		screenWidth = _screenWidth;
		halfx = (float)_screenHeight/(float)_screenWidth;		
		
		bgdh = new BackGroundDisplayHandler(screenHeight,screenWidth);		
		
		iPlayButtonHandle = TextureHelper.loadTexture(_mActivityContext, R.drawable.playbutton);
		iSettingsButtonHandle = TextureHelper.loadTexture(_mActivityContext, R.drawable.settingsbutton);
		iBackButtonHandle = TextureHelper.loadTexture(_mActivityContext, R.drawable.backbutton);
		iBackHandle = TextureHelper.loadTexture(_mActivityContext, R.drawable.number0);
		iForwardHandle = TextureHelper.loadTexture(_mActivityContext, R.drawable.number9);
		i1 = TextureHelper.loadTexture(_mActivityContext, R.drawable.number1);
		i2 = TextureHelper.loadTexture(_mActivityContext, R.drawable.number2);
		i3 = TextureHelper.loadTexture(_mActivityContext, R.drawable.number3);
		i4 = TextureHelper.loadTexture(_mActivityContext, R.drawable.number4);
		i5 = TextureHelper.loadTexture(_mActivityContext, R.drawable.number5);
		
		InitializeBackGroundBuffers();
		InitializeHeaderBuffers();
		InitializeBackButtonBuffers();
		InitializeBackBuffers();	
		InitializeForwardBuffers();
		InitializeSelectWorldBuffers();
		
		 mBackGroundTextureHandle1 = TextureHelper.loadTexture(_mActivityContext, R.drawable.backgroundsnow);
	        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
		  mBackGroundTextureHandle2 = TextureHelper.loadTexture(_mActivityContext, R.drawable.junglebackground);
	        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);	        
		  mBackGroundTextureHandle3 = TextureHelper.loadTexture(_mActivityContext, R.drawable.desertbackground);
	        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
		  mBackGroundTextureHandle4 = TextureHelper.loadTexture(_mActivityContext, R.drawable.citybackground);
	        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
		  mBackGroundTextureHandle5 = TextureHelper.loadTexture(_mActivityContext, R.drawable.spacebackground);
	        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
	        
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
        
        
        //Draw Main Menu Leader Board Button
      	////////////////////////////////////////////////////////////////////////////////////////////
        Matrix.setIdentityM(_mModelMatrix, 0);       
        
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iBackButtonHandle);
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(_mTextureUniformHandle, 0);
        
        mSelectWorldBackButtonTextureCoordinates.position(0);
        
        // Pass in the texture coordinate information
        mSelectWorldBackButtonTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(_mTextureCoordinateHandle, _mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectWorldBackButtonTextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(_mTextureCoordinateHandle);        
      
        		// Pass in the position information
        mSelectWorldBackButtonPositions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mSelectWorldBackButtonPositions);        
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);                       
                
        // Pass in the normal information
        mSelectWorldBackButtonNormals.position(0);
        GLES20.glVertexAttribPointer(_mNormalHandle, _mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectWorldBackButtonNormals);
        
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
        
        
        //Draw Select World Back
      	////////////////////////////////////////////////////////////////////////////////////////////
        if(iSelectedWorld > 1)
        {
        Matrix.setIdentityM(_mModelMatrix, 0);       
        
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iBackHandle);
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(_mTextureUniformHandle, 0);
        
        mSelectWorldBackTextureCoordinates.position(0);
        
        // Pass in the texture coordinate information
        mSelectWorldBackTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(_mTextureCoordinateHandle, _mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectWorldBackTextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(_mTextureCoordinateHandle);        
      
        		// Pass in the position information
        mSelectWorldBackPositions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mSelectWorldBackPositions);        
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);                       
                
        // Pass in the normal information
        mSelectWorldBackNormals.position(0);
        GLES20.glVertexAttribPointer(_mNormalHandle, _mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectWorldBackNormals);
        
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
        }
        ////////////////////////////////////////////////////////////////////////////////////////////   
        
        //Draw Select World Forward
      	////////////////////////////////////////////////////////////////////////////////////////////
        if(iSelectedWorld < 5)
        {
        Matrix.setIdentityM(_mModelMatrix, 0);       
        
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iForwardHandle);
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(_mTextureUniformHandle, 0);
        
        mSelectWorldForwardTextureCoordinates.position(0);
        
        // Pass in the texture coordinate information
        mSelectWorldForwardTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(_mTextureCoordinateHandle, _mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectWorldForwardTextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(_mTextureCoordinateHandle);        
      
        		// Pass in the position information
        mSelectWorldForwardPositions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mSelectWorldForwardPositions);        
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);                       
                
        // Pass in the normal information
        mSelectWorldForwardNormals.position(0);
        GLES20.glVertexAttribPointer(_mNormalHandle, _mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectWorldForwardNormals);
        
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
        }
        ////////////////////////////////////////////////////////////////////////////////////////////   
        
        //Draw Select World 
      	////////////////////////////////////////////////////////////////////////////////////////////
        Matrix.setIdentityM(_mModelMatrix, 0);       
        
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
              
        // Bind the texture to this unit.
        switch(iSelectedWorld)
        {
	        case 1:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, i1);
	        break;
	        case 2:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, i2);
		    break;
	        case 3:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, i3);
		    break;
	        case 4:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, i4);
		    break;
	        case 5:
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, i5);
		    break;
        }       
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(_mTextureUniformHandle, 0);
        
        mSelectWorldTextureCoordinates.position(0);
        
        // Pass in the texture coordinate information
        mSelectWorldTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(_mTextureCoordinateHandle, _mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectWorldTextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(_mTextureCoordinateHandle);        
      
        		// Pass in the position information
        mSelectWorldPositions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mSelectWorldPositions);        
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);                       
                
        // Pass in the normal information
        mSelectWorldNormals.position(0);
        GLES20.glVertexAttribPointer(_mNormalHandle, _mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectWorldNormals);
        
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

	public void InitializeBackGroundBuffers()
	{
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
	}

	public void InitializeHeaderBuffers()
	{
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
	}

	public void InitializeBackButtonBuffers()
	{
		//Back Button Buffers
				//////////////////////////////////////////////////////////////////////////////////////////////////////
				
				float fSelectWorldBackButtonWidth = 0.5f;
				
				final float[] SelectWorldBackButtonPositions =
					{		
						    0.8f, -halfx+fSelectWorldBackButtonWidth, 0.0f,
						    0.8f, -halfx, 0.0f,	
							1.0f, -halfx+fSelectWorldBackButtonWidth, 0.0f,		
							0.8f, -halfx, 0.0f,				
							1.0f, -halfx, 0.0f,
							1.0f, -halfx+fSelectWorldBackButtonWidth, 0.0f,		
							
					};		
				final float[] SelectWorldBackButtonNormals = 
					{
						// Front face
						0.0f, 0.0f, 1.0f,				
						0.0f, 0.0f, 1.0f,
						0.0f, 0.0f, 1.0f,
						0.0f, 0.0f, 1.0f,				
						0.0f, 0.0f, 1.0f,
						0.0f, 0.0f, 1.0f,
					};
				final float[] SelectWorldBackButtonTextureCoordinates = 
					{		
						1.0f, 0.0f,			
						0.0f, 0.0f, 
						1.0f, 1.0f,
						0.0f, 0.0f, 
						0.0f, 1.0f,
						1.0f, 1.0f,
					};
				
				mSelectWorldBackButtonPositions  = ByteBuffer.allocateDirect(SelectWorldBackButtonPositions.length * mBytesPerFloat)
		        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
				mSelectWorldBackButtonPositions.put(SelectWorldBackButtonPositions).position(0);	
				
				mSelectWorldBackButtonNormals  = ByteBuffer.allocateDirect(SelectWorldBackButtonNormals.length * mBytesPerFloat)
		        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
				mSelectWorldBackButtonNormals.put(SelectWorldBackButtonNormals).position(0);	
				
				mSelectWorldBackButtonTextureCoordinates  = ByteBuffer.allocateDirect(SelectWorldBackButtonTextureCoordinates.length * mBytesPerFloat)
		        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
				mSelectWorldBackButtonTextureCoordinates.put(SelectWorldBackButtonTextureCoordinates).position(0);
				
				//////////////////////////////////////////////////////////////////////////////////////////////////////
				
	}

	public void InitializeBackBuffers()
	{
		//Select World Back Buffers
    	////////////////////////////////////////////////////////////////////////////////////////////////////
		
		float fBack = 0.15f;
		
		final float[] SelectWorldBackPositions =
			{		
				    -fBack, -halfx+0.3f, 0.0f,
				    -fBack, -halfx, 0.0f,	
				    fBack, -halfx+0.3f, 0.0f,		
					-fBack, -halfx, 0.0f,				
					fBack, -halfx, 0.0f,
					fBack, -halfx+0.3f, 0.0f,		
					
			};		
		final float[] SelectWorldBackNormals = 
			{
				// Front face
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
			};
		final float[] SelectWorldBackTextureCoordinates = 
			{		
				0.0f, 0.0f,			
				0.0f, 1.0f, 
				1.0f, 0.0f,
				0.0f, 1.0f, 
				1.0f, 1.0f,
				1.0f, 0.0f,
			};
		
		mSelectWorldBackPositions  = ByteBuffer.allocateDirect(SelectWorldBackPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectWorldBackPositions.put(SelectWorldBackPositions).position(0);	
		
		mSelectWorldBackNormals  = ByteBuffer.allocateDirect(SelectWorldBackNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectWorldBackNormals.put(SelectWorldBackNormals).position(0);	
		
		mSelectWorldBackTextureCoordinates  = ByteBuffer.allocateDirect(SelectWorldBackTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectWorldBackTextureCoordinates.put(SelectWorldBackTextureCoordinates).position(0);
	}

	public void InitializeForwardBuffers()
	{
		//Select World Forward Buffers
    	////////////////////////////////////////////////////////////////////////////////////////////////////
		
		float fBack = 0.15f;
		
		final float[] SelectWorldForwardPositions =
			{		
				    -fBack, halfx, 0.0f,
				    -fBack, halfx-0.3f, 0.0f,	
				    fBack, halfx, 0.0f,		
					-fBack, halfx-0.3f, 0.0f,				
					fBack, halfx-0.3f, 0.0f,
					fBack, halfx, 0.0f,		
					
			};		
		final float[] SelectWorldForwardNormals = 
			{
				// Front face
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
			};
		final float[] SelectWorldForwardTextureCoordinates = 
			{		
				0.0f, 0.0f,			
				0.0f, 1.0f, 
				1.0f, 0.0f,
				0.0f, 1.0f, 
				1.0f, 1.0f,
				1.0f, 0.0f,
			};
		
		mSelectWorldForwardPositions  = ByteBuffer.allocateDirect(SelectWorldForwardPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectWorldForwardPositions.put(SelectWorldForwardPositions).position(0);	
		
		mSelectWorldForwardNormals  = ByteBuffer.allocateDirect(SelectWorldForwardNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectWorldForwardNormals.put(SelectWorldForwardNormals).position(0);	
		
		mSelectWorldForwardTextureCoordinates  = ByteBuffer.allocateDirect(SelectWorldForwardTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectWorldForwardTextureCoordinates.put(SelectWorldForwardTextureCoordinates).position(0);
	}

	public void InitializeSelectWorldBuffers()
	{
		//Select World Buffers
    	////////////////////////////////////////////////////////////////////////////////////////////////////
		
		float fSelectWorldHeightWidth = 0.5f;
		
		final float[] SelectWorldPositions =
			{		
				    -fSelectWorldHeightWidth, fSelectWorldHeightWidth, 0.0f,
				    -fSelectWorldHeightWidth, -fSelectWorldHeightWidth, 0.0f,	
				    fSelectWorldHeightWidth, fSelectWorldHeightWidth, 0.0f,		
					-fSelectWorldHeightWidth, -fSelectWorldHeightWidth, 0.0f,				
					fSelectWorldHeightWidth, -fSelectWorldHeightWidth, 0.0f,
					fSelectWorldHeightWidth, fSelectWorldHeightWidth, 0.0f,		
					
			};		
		final float[] SelectWorldNormals = 
			{
				// Front face
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
			};
		final float[] SelectWorldTextureCoordinates = 
			{		
				0.0f, 0.0f,			
				0.0f, 1.0f, 
				1.0f, 0.0f,
				0.0f, 1.0f, 
				1.0f, 1.0f,
				1.0f, 0.0f,
			};
		
		mSelectWorldPositions  = ByteBuffer.allocateDirect(SelectWorldPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectWorldPositions.put(SelectWorldPositions).position(0);	
		
		mSelectWorldNormals  = ByteBuffer.allocateDirect(SelectWorldNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectWorldNormals.put(SelectWorldNormals).position(0);	
		
		mSelectWorldTextureCoordinates  = ByteBuffer.allocateDirect(SelectWorldTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectWorldTextureCoordinates.put(SelectWorldTextureCoordinates).position(0);
	}

	public int HandleUserInput(int _iDisplayIndex, float _fX, float _fY)
	{
		if(_fX > 0.8f && _fX < 1.0f && -_fY < -halfx+0.5f && -_fY > -halfx)
		{			    
				return 0;					
		}	
		if(_fX > -0.15f && _fX < 0.15f && -_fY < -halfx+0.3f && -_fY > -halfx && iSelectedWorld>1)
		{			   			
			    iSelectedWorld -= 1;
				return 2;					
		}
		if(_fX > -0.15f && _fX < 0.15f && -_fY < halfx && -_fY > halfx-0.3f && iSelectedWorld<5 )
		{			  	
			    iSelectedWorld += 1;
				return 2;					
		}
		if(_fX > -0.5f && _fX < 0.5f && -_fY < 0.5f && -_fY > -0.5f)
		{			
				return 3;					
		}			
		return _iDisplayIndex;
	}	
}
