package roundoid.classes.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import roundoid.classes.game.common.TextureHelper;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class SettingsDisplayHandler {

	private final int mBytesPerFloat = 4;
	
	int screenHeight;
	int screenWidth;
	float halfx;
	float halfy;
	int iBackButtonHandle;
	boolean bToggleSound = false;
	boolean bToggleMusic = false;
	int SelectedDifficulty = 0;
	float fToggleSoundMusicPositionOffSet = 12.0f;
	float fDifficultyPositionOffSet = 8.0f;	
	
	int i1;
	int i0;	
	
	BackGroundDisplayHandler bgdh; 
	
	//Select Stage Background Buffers
	public FloatBuffer mSettingsBackGroundPositions;	
	public FloatBuffer mSettingsBackGroundNormals;
	public FloatBuffer mSettingsBackGroundTextureCoordinates;
	
	//Select Stage Back Button Buffers
	public  FloatBuffer mSettingsBackButtonPositions;	
	public  FloatBuffer mSettingsBackButtonNormals;
	public  FloatBuffer mSettingsBackButtonTextureCoordinates;	
	
	//Toggle Sound Buffers
	public  FloatBuffer mToggleSoundButtonPositions;	
	public  FloatBuffer mToggleSoundButtonNormals;
	public  FloatBuffer mToggleSoundButtonTextureCoordinates;	
	public float[] ToggleSoundButtonLocations = new float[4];
	
	//Toggle Music Buffers
	public  FloatBuffer mToggleMusicButtonPositions;	
	public  FloatBuffer mToggleMusicButtonNormals;
	public  FloatBuffer mToggleMusicButtonTextureCoordinates;	
	public float[] ToggleMusicButtonLocations = new float[4];
	
	//Easy Difficulty Buffers
	public  FloatBuffer mEasyDifficultyButtonPositions;	
	public  FloatBuffer mEasyDifficultyButtonNormals;
	public  FloatBuffer mEasyDifficultyButtonTextureCoordinates;	
	public float[] EasyDifficultyButtonLocations = new float[4];
	
	//Medium Difficulty Buffers
	public  FloatBuffer mMediumDifficultyButtonPositions;	
	public  FloatBuffer mMediumDifficultyButtonNormals;
	public  FloatBuffer mMediumDifficultyButtonTextureCoordinates;	
	public float[] MediumDifficultyButtonLocations = new float[4];
	
	//Hard Difficulty Buffers
	public  FloatBuffer mHardDifficultyButtonPositions;	
	public  FloatBuffer mHardDifficultyButtonNormals;
	public  FloatBuffer mHardDifficultyButtonTextureCoordinates;	
	public float[] HardDifficultyButtonLocations = new float[4];
	
	public SettingsDisplayHandler(int _screenHeight, int _screenWidth, Context _mActivityContext)
	{
		screenHeight = _screenHeight;
		screenWidth = _screenWidth;
		halfx = (float)_screenHeight/(float)_screenWidth;		
		halfy = (float)_screenWidth/(float)_screenHeight;
		
		bgdh = new BackGroundDisplayHandler(screenHeight,screenWidth);	
		
		iBackButtonHandle = TextureHelper.loadTexture(_mActivityContext, R.drawable.backbutton);		
		i1 = TextureHelper.loadTexture(_mActivityContext, R.drawable.number1);
		i0 = TextureHelper.loadTexture(_mActivityContext, R.drawable.number0);
				
		InitializeBackGroundBuffers();		
		InitializeBackButtonBuffers();	
		InitializeToggleSoundBuffers();
		InitializeToggleMusicBuffers();
		InitializeEasyDifficultyBuffers();
		InitializeMediumDifficultyBuffers();
		InitializeHardDifficultyBuffers();	
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
       
       // public void Draw(int _iProgramHandle, int _mMVPMatrixHandle, int _mMVMatrixHandle,int _mTextureUniformHandle,int _mPositionHandle,int _mNormalHandle,int _mTextureCoordinateHandle, float[] _mModelMatrix,int _mBackGroundTextureHandle,int _mTextureCoordinateDataSize,int _mPositionDataSize,int _mNormalDataSize,float[] _mMVPMatrix, float[] _mViewMatrix,float[] _mTemporaryMatrix,float[] _mProjectionMatrix,int _iNumberZeroTextureHandle)      GLES20.glEnableVertexAttribArray(_mNormalHandle);                
        
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
        
        mSettingsBackButtonTextureCoordinates.position(0);
        
        // Pass in the texture coordinate information
        mSettingsBackButtonTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(_mTextureCoordinateHandle, _mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mSettingsBackButtonTextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(_mTextureCoordinateHandle);        
      
        		// Pass in the position information
        mSettingsBackButtonPositions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mSettingsBackButtonPositions);        
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);                       
                
        // Pass in the normal information
        mSettingsBackButtonNormals.position(0);
        GLES20.glVertexAttribPointer(_mNormalHandle, _mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mSettingsBackButtonNormals);
        
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
                    
        DrawToggleSound(_iProgramHandle,_mMVPMatrixHandle,_mMVMatrixHandle,_mTextureUniformHandle,_mPositionHandle,_mNormalHandle,_mTextureCoordinateHandle,_mModelMatrix,_mBackGroundTextureHandle,_mTextureCoordinateDataSize,_mPositionDataSize,_mNormalDataSize,_mMVPMatrix, _mViewMatrix,_mTemporaryMatrix,_mProjectionMatrix,_iNumberZeroTextureHandle);
        DrawToggleMusic(_iProgramHandle,_mMVPMatrixHandle,_mMVMatrixHandle,_mTextureUniformHandle,_mPositionHandle,_mNormalHandle,_mTextureCoordinateHandle,_mModelMatrix,_mBackGroundTextureHandle,_mTextureCoordinateDataSize,_mPositionDataSize,_mNormalDataSize,_mMVPMatrix, _mViewMatrix,_mTemporaryMatrix,_mProjectionMatrix,_iNumberZeroTextureHandle);
        DrawEasyDifficulty(_iProgramHandle,_mMVPMatrixHandle,_mMVMatrixHandle,_mTextureUniformHandle,_mPositionHandle,_mNormalHandle,_mTextureCoordinateHandle,_mModelMatrix,_mBackGroundTextureHandle,_mTextureCoordinateDataSize,_mPositionDataSize,_mNormalDataSize,_mMVPMatrix, _mViewMatrix,_mTemporaryMatrix,_mProjectionMatrix,_iNumberZeroTextureHandle);
        DrawMediumDifficulty(_iProgramHandle,_mMVPMatrixHandle,_mMVMatrixHandle,_mTextureUniformHandle,_mPositionHandle,_mNormalHandle,_mTextureCoordinateHandle,_mModelMatrix,_mBackGroundTextureHandle,_mTextureCoordinateDataSize,_mPositionDataSize,_mNormalDataSize,_mMVPMatrix, _mViewMatrix,_mTemporaryMatrix,_mProjectionMatrix,_iNumberZeroTextureHandle);
        DrawHardDifficulty(_iProgramHandle,_mMVPMatrixHandle,_mMVMatrixHandle,_mTextureUniformHandle,_mPositionHandle,_mNormalHandle,_mTextureCoordinateHandle,_mModelMatrix,_mBackGroundTextureHandle,_mTextureCoordinateDataSize,_mPositionDataSize,_mNormalDataSize,_mMVPMatrix, _mViewMatrix,_mTemporaryMatrix,_mProjectionMatrix,_iNumberZeroTextureHandle);
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
				
				mSettingsBackButtonPositions  = ByteBuffer.allocateDirect(SelectWorldBackButtonPositions.length * mBytesPerFloat)
		        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
				mSettingsBackButtonPositions.put(SelectWorldBackButtonPositions).position(0);	
				
				mSettingsBackButtonNormals  = ByteBuffer.allocateDirect(SelectWorldBackButtonNormals.length * mBytesPerFloat)
		        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
				mSettingsBackButtonNormals.put(SelectWorldBackButtonNormals).position(0);	
				
				mSettingsBackButtonTextureCoordinates  = ByteBuffer.allocateDirect(SelectWorldBackButtonTextureCoordinates.length * mBytesPerFloat)
		        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
				mSettingsBackButtonTextureCoordinates.put(SelectWorldBackButtonTextureCoordinates).position(0);
				
				//////////////////////////////////////////////////////////////////////////////////////////////////////
				
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
		
		mSettingsBackGroundPositions  = ByteBuffer.allocateDirect(MainMenuBackGroundPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSettingsBackGroundPositions.put(MainMenuBackGroundPositions).position(0);	
		
		mSettingsBackGroundNormals  = ByteBuffer.allocateDirect(MainMenuBackGroundNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSettingsBackGroundNormals.put(MainMenuBackGroundNormals).position(0);	
		
		mSettingsBackGroundTextureCoordinates  = ByteBuffer.allocateDirect(MainMenuBackGroundTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSettingsBackGroundTextureCoordinates.put(MainMenuBackGroundTextureCoordinates).position(0);
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////
	}

	public void InitializeToggleSoundBuffers()
	{
		//Toggle Sound Buffers
    	////////////////////////////////////////////////////////////
		
		float fHeightWidth = 0.15f;
		float xOffSet = ((halfy*2.0f)/6.0f)*2.0f;
		float yOffSet = ((halfx*2.0f)/fToggleSoundMusicPositionOffSet)*2.0f;
				
		final float[] ToggleSoundPositions =
			{		
			    -fHeightWidth-xOffSet, fHeightWidth-yOffSet, 0.0f,
			    -fHeightWidth-xOffSet, -fHeightWidth-yOffSet, 0.0f,	
			    fHeightWidth-xOffSet, fHeightWidth-yOffSet, 0.0f,		
				-fHeightWidth-xOffSet, -fHeightWidth-yOffSet, 0.0f,				
				fHeightWidth-xOffSet, -fHeightWidth-yOffSet, 0.0f,
				fHeightWidth-xOffSet, fHeightWidth-yOffSet, 0.0f,	
			};		
		
		ToggleSoundButtonLocations[0] =  ToggleSoundPositions[0];
		ToggleSoundButtonLocations[1] =  ToggleSoundPositions[6];
		ToggleSoundButtonLocations[2] =  ToggleSoundPositions[1];
		ToggleSoundButtonLocations[3] =  ToggleSoundPositions[4];
		
		final float[] ToggleSoundNormals = 
			{
				// Front face
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
			};
		final float[] ToggleSoundTextureCoordinates = 
			{		
				0.0f, 0.0f,			
				0.0f, 1.0f, 
				1.0f, 0.0f,
				0.0f, 1.0f, 
				1.0f, 1.0f,
				1.0f, 0.0f,
			};
		
		mToggleSoundButtonPositions  = ByteBuffer.allocateDirect(ToggleSoundPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mToggleSoundButtonPositions.put(ToggleSoundPositions).position(0);	
		
		mToggleSoundButtonNormals  = ByteBuffer.allocateDirect(ToggleSoundNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mToggleSoundButtonNormals.put(ToggleSoundNormals).position(0);	
		
		mToggleSoundButtonTextureCoordinates  = ByteBuffer.allocateDirect(ToggleSoundTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mToggleSoundButtonTextureCoordinates.put(ToggleSoundTextureCoordinates).position(0);
	}

	public void InitializeToggleMusicBuffers()
	{
		//Toggle Music Buffers
    	////////////////////////////////////////////////////////////
		
		float fHeightWidth = 0.15f;
		float xOffSet = ((halfy*2.0f)/6.0f)*2.0f;
		float yOffSet = ((halfx*2.0f)/fToggleSoundMusicPositionOffSet)*2.0f;
				
		final float[] ToggleMusicPositions =
			{		
			    -fHeightWidth-xOffSet, fHeightWidth+yOffSet, 0.0f,
			    -fHeightWidth-xOffSet, -fHeightWidth+yOffSet, 0.0f,	
			    fHeightWidth-xOffSet, fHeightWidth+yOffSet, 0.0f,		
				-fHeightWidth-xOffSet, -fHeightWidth+yOffSet, 0.0f,				
				fHeightWidth-xOffSet, -fHeightWidth+yOffSet, 0.0f,
				fHeightWidth-xOffSet, fHeightWidth+yOffSet, 0.0f,	
			};		
		
		ToggleMusicButtonLocations[0] =  ToggleMusicPositions[0];
		ToggleMusicButtonLocations[1] =  ToggleMusicPositions[6];
		ToggleMusicButtonLocations[2] =  ToggleMusicPositions[1];
		ToggleMusicButtonLocations[3] =  ToggleMusicPositions[4];
		
		final float[] ToggleMusicNormals = 
			{
				// Front face
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
			};
		final float[] ToggleMusicTextureCoordinates = 
			{		
				0.0f, 0.0f,			
				0.0f, 1.0f, 
				1.0f, 0.0f,
				0.0f, 1.0f, 
				1.0f, 1.0f,
				1.0f, 0.0f,
			};
		
		mToggleMusicButtonPositions  = ByteBuffer.allocateDirect(ToggleMusicPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mToggleMusicButtonPositions.put(ToggleMusicPositions).position(0);	
		
		mToggleMusicButtonNormals  = ByteBuffer.allocateDirect(ToggleMusicNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mToggleMusicButtonNormals.put(ToggleMusicNormals).position(0);	
		
		mToggleMusicButtonTextureCoordinates  = ByteBuffer.allocateDirect(ToggleMusicTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mToggleMusicButtonTextureCoordinates.put(ToggleMusicTextureCoordinates).position(0);
	}
	
	public void InitializeEasyDifficultyBuffers()
	{
		//Toggle Easy Difficulty Buffers
    	////////////////////////////////////////////////////////////
		
		float fHeightWidth = 0.15f;		
		float yOffSet = ((halfx*2.0f)/fDifficultyPositionOffSet)*2.0f;
				
		final float[] EasyDifficultyPositions =
			{		
			    -fHeightWidth, fHeightWidth-yOffSet, 0.0f,
			    -fHeightWidth, -fHeightWidth-yOffSet, 0.0f,	
			    fHeightWidth, fHeightWidth-yOffSet, 0.0f,		
				-fHeightWidth, -fHeightWidth-yOffSet, 0.0f,				
				fHeightWidth, -fHeightWidth-yOffSet, 0.0f,
				fHeightWidth, fHeightWidth-yOffSet, 0.0f,	
			};		
		
		EasyDifficultyButtonLocations[0] =  EasyDifficultyPositions[0];
		EasyDifficultyButtonLocations[1] =  EasyDifficultyPositions[6];
		EasyDifficultyButtonLocations[2] =  EasyDifficultyPositions[1];
		EasyDifficultyButtonLocations[3] =  EasyDifficultyPositions[4];
		
		final float[] EasyDifficultyNormals = 
			{
				// Front face
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
			};
		final float[] EasyDifficultyTextureCoordinates = 
			{		
				0.0f, 0.0f,			
				0.0f, 1.0f, 
				1.0f, 0.0f,
				0.0f, 1.0f, 
				1.0f, 1.0f,
				1.0f, 0.0f,
			};
		
		mEasyDifficultyButtonPositions  = ByteBuffer.allocateDirect(EasyDifficultyPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mEasyDifficultyButtonPositions.put(EasyDifficultyPositions).position(0);	
		
		mEasyDifficultyButtonNormals  = ByteBuffer.allocateDirect(EasyDifficultyNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mEasyDifficultyButtonNormals.put(EasyDifficultyNormals).position(0);	
		
		mEasyDifficultyButtonTextureCoordinates  = ByteBuffer.allocateDirect(EasyDifficultyTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mEasyDifficultyButtonTextureCoordinates.put(EasyDifficultyTextureCoordinates).position(0);
	}
	
	public void InitializeMediumDifficultyBuffers()
	{
		//Toggle Medium Difficulty Buffers
    	////////////////////////////////////////////////////////////
		
		float fHeightWidth = 0.15f;				
				
		final float[] MediumDifficultyPositions =
			{		
			    -fHeightWidth, fHeightWidth, 0.0f,
			    -fHeightWidth, -fHeightWidth, 0.0f,	
			    fHeightWidth, fHeightWidth, 0.0f,		
				-fHeightWidth, -fHeightWidth, 0.0f,				
				fHeightWidth, -fHeightWidth, 0.0f,
				fHeightWidth, fHeightWidth, 0.0f,	
			};		
		
		MediumDifficultyButtonLocations[0] =  MediumDifficultyPositions[0];
		MediumDifficultyButtonLocations[1] =  MediumDifficultyPositions[6];
		MediumDifficultyButtonLocations[2] =  MediumDifficultyPositions[1];
		MediumDifficultyButtonLocations[3] =  MediumDifficultyPositions[4];
		
		final float[] MediumDifficultyNormals = 
			{
				// Front face
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
			};
		final float[] MediumDifficultyTextureCoordinates = 
			{		
				0.0f, 0.0f,			
				0.0f, 1.0f, 
				1.0f, 0.0f,
				0.0f, 1.0f, 
				1.0f, 1.0f,
				1.0f, 0.0f,
			};
		
		mMediumDifficultyButtonPositions  = ByteBuffer.allocateDirect(MediumDifficultyPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMediumDifficultyButtonPositions.put(MediumDifficultyPositions).position(0);	
		
		mMediumDifficultyButtonNormals  = ByteBuffer.allocateDirect(MediumDifficultyNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMediumDifficultyButtonNormals.put(MediumDifficultyNormals).position(0);	
		
		mMediumDifficultyButtonTextureCoordinates  = ByteBuffer.allocateDirect(MediumDifficultyTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMediumDifficultyButtonTextureCoordinates.put(MediumDifficultyTextureCoordinates).position(0);
	}	

	public void InitializeHardDifficultyBuffers()
	{
		//Toggle Hard Difficulty Buffers
    	////////////////////////////////////////////////////////////
		
		float fHeightWidth = 0.15f;		
		float yOffSet = ((halfx*2.0f)/fDifficultyPositionOffSet)*2.0f;
				
		final float[] HardDifficultyPositions =
			{		
			    -fHeightWidth, fHeightWidth+yOffSet, 0.0f,
			    -fHeightWidth, -fHeightWidth+yOffSet, 0.0f,	
			    fHeightWidth, fHeightWidth+yOffSet, 0.0f,		
				-fHeightWidth, -fHeightWidth+yOffSet, 0.0f,				
				fHeightWidth, -fHeightWidth+yOffSet, 0.0f,
				fHeightWidth, fHeightWidth+yOffSet, 0.0f,	
			};		
		
		HardDifficultyButtonLocations[0] =  HardDifficultyPositions[0];
		HardDifficultyButtonLocations[1] =  HardDifficultyPositions[6];
		HardDifficultyButtonLocations[2] =  HardDifficultyPositions[1];
		HardDifficultyButtonLocations[3] =  HardDifficultyPositions[4];
		
		final float[] HardDifficultyNormals = 
			{
				// Front face
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
			};
		final float[] HardDifficultyTextureCoordinates = 
			{		
				0.0f, 0.0f,			
				0.0f, 1.0f, 
				1.0f, 0.0f,
				0.0f, 1.0f, 
				1.0f, 1.0f,
				1.0f, 0.0f,
			};
		
		mHardDifficultyButtonPositions  = ByteBuffer.allocateDirect(HardDifficultyPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mHardDifficultyButtonPositions.put(HardDifficultyPositions).position(0);	
		
		mHardDifficultyButtonNormals  = ByteBuffer.allocateDirect(HardDifficultyNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mHardDifficultyButtonNormals.put(HardDifficultyNormals).position(0);	
		
		mHardDifficultyButtonTextureCoordinates  = ByteBuffer.allocateDirect(HardDifficultyTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mHardDifficultyButtonTextureCoordinates.put(HardDifficultyTextureCoordinates).position(0);
	}
	
	public void DrawToggleSound(int _iProgramHandle, int _mMVPMatrixHandle, int _mMVMatrixHandle,int _mTextureUniformHandle,int _mPositionHandle,int _mNormalHandle,int _mTextureCoordinateHandle, float[] _mModelMatrix,int _mBackGroundTextureHandle,int _mTextureCoordinateDataSize,int _mPositionDataSize,int _mNormalDataSize,float[] _mMVPMatrix, float[] _mViewMatrix,float[] _mTemporaryMatrix,float[] _mProjectionMatrix,int _iNumberZeroTextureHandle)
	{
		//Draw Toggle Sound
      	////////////////////////////////////////////////////////////////////////////////////////////
        Matrix.setIdentityM(_mModelMatrix, 0);       
        
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
       
        // Bind the texture to this unit.
        if(bToggleSound)
        	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, i1);
        else
        	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, i0);        
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(_mTextureUniformHandle, 0);
        
        mToggleSoundButtonTextureCoordinates.position(0);
        
        // Pass in the texture coordinate information
        mToggleSoundButtonTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(_mTextureCoordinateHandle, _mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mToggleSoundButtonTextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(_mTextureCoordinateHandle);        
      
        // Pass in the position information
        mToggleSoundButtonPositions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mToggleSoundButtonPositions);        
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);                       
                
        // Pass in the normal information
        mToggleSoundButtonNormals.position(0);
        GLES20.glVertexAttribPointer(_mNormalHandle, _mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mToggleSoundButtonNormals);
        
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
	
	public void DrawToggleMusic(int _iProgramHandle, int _mMVPMatrixHandle, int _mMVMatrixHandle,int _mTextureUniformHandle,int _mPositionHandle,int _mNormalHandle,int _mTextureCoordinateHandle, float[] _mModelMatrix,int _mBackGroundTextureHandle,int _mTextureCoordinateDataSize,int _mPositionDataSize,int _mNormalDataSize,float[] _mMVPMatrix, float[] _mViewMatrix,float[] _mTemporaryMatrix,float[] _mProjectionMatrix,int _iNumberZeroTextureHandle)
	{
		//Draw Toggle Music
      	////////////////////////////////////////////////////////////////////////////////////////////
        Matrix.setIdentityM(_mModelMatrix, 0);       
        
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
       
        // Bind the texture to this unit.
        if(bToggleMusic)
        	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, i1);
        else
        	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, i0);        
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(_mTextureUniformHandle, 0);
        
        mToggleMusicButtonTextureCoordinates.position(0);
        
        // Pass in the texture coordinate information
        mToggleMusicButtonTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(_mTextureCoordinateHandle, _mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mToggleMusicButtonTextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(_mTextureCoordinateHandle);        
      
        // Pass in the position information
        mToggleMusicButtonPositions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mToggleMusicButtonPositions);        
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);                       
                
        // Pass in the normal information
        mToggleMusicButtonNormals.position(0);
        GLES20.glVertexAttribPointer(_mNormalHandle, _mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mToggleMusicButtonNormals);
        
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
	
	public void DrawEasyDifficulty(int _iProgramHandle, int _mMVPMatrixHandle, int _mMVMatrixHandle,int _mTextureUniformHandle,int _mPositionHandle,int _mNormalHandle,int _mTextureCoordinateHandle, float[] _mModelMatrix,int _mBackGroundTextureHandle,int _mTextureCoordinateDataSize,int _mPositionDataSize,int _mNormalDataSize,float[] _mMVPMatrix, float[] _mViewMatrix,float[] _mTemporaryMatrix,float[] _mProjectionMatrix,int _iNumberZeroTextureHandle)
	{
		//Draw EasyDifficulty
      	////////////////////////////////////////////////////////////////////////////////////////////
        Matrix.setIdentityM(_mModelMatrix, 0);       
        
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
       
        // Bind the texture to this unit.
        if(SelectedDifficulty == 0)
        	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, i1);
        else
        	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, i0);    
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(_mTextureUniformHandle, 0);
        
        mEasyDifficultyButtonTextureCoordinates.position(0);
        
        // Pass in the texture coordinate information
        mEasyDifficultyButtonTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(_mTextureCoordinateHandle, _mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mEasyDifficultyButtonTextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(_mTextureCoordinateHandle);        
      
        // Pass in the position information
        mEasyDifficultyButtonPositions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mEasyDifficultyButtonPositions);        
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);                       
                
        // Pass in the normal information
        mEasyDifficultyButtonNormals.position(0);
        GLES20.glVertexAttribPointer(_mNormalHandle, _mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mEasyDifficultyButtonNormals);
        
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

	public void DrawMediumDifficulty(int _iProgramHandle, int _mMVPMatrixHandle, int _mMVMatrixHandle,int _mTextureUniformHandle,int _mPositionHandle,int _mNormalHandle,int _mTextureCoordinateHandle, float[] _mModelMatrix,int _mBackGroundTextureHandle,int _mTextureCoordinateDataSize,int _mPositionDataSize,int _mNormalDataSize,float[] _mMVPMatrix, float[] _mViewMatrix,float[] _mTemporaryMatrix,float[] _mProjectionMatrix,int _iNumberZeroTextureHandle)
	{
		//Draw MediumDifficulty
      	////////////////////////////////////////////////////////////////////////////////////////////
        Matrix.setIdentityM(_mModelMatrix, 0);       
        
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
       
        // Bind the texture to this unit.
        if(SelectedDifficulty == 1)
        	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, i1);
        else
        	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, i0);    
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(_mTextureUniformHandle, 0);
        
        mMediumDifficultyButtonTextureCoordinates.position(0);
        
        // Pass in the texture coordinate information
        mMediumDifficultyButtonTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(_mTextureCoordinateHandle, _mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mMediumDifficultyButtonTextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(_mTextureCoordinateHandle);        
      
        // Pass in the position information
        mMediumDifficultyButtonPositions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mMediumDifficultyButtonPositions);        
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);                       
                
        // Pass in the normal information
        mMediumDifficultyButtonNormals.position(0);
        GLES20.glVertexAttribPointer(_mNormalHandle, _mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mMediumDifficultyButtonNormals);
        
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
	
	public void DrawHardDifficulty(int _iProgramHandle, int _mMVPMatrixHandle, int _mMVMatrixHandle,int _mTextureUniformHandle,int _mPositionHandle,int _mNormalHandle,int _mTextureCoordinateHandle, float[] _mModelMatrix,int _mBackGroundTextureHandle,int _mTextureCoordinateDataSize,int _mPositionDataSize,int _mNormalDataSize,float[] _mMVPMatrix, float[] _mViewMatrix,float[] _mTemporaryMatrix,float[] _mProjectionMatrix,int _iNumberZeroTextureHandle)
	{
		//Draw HardDifficulty
      	////////////////////////////////////////////////////////////////////////////////////////////
        Matrix.setIdentityM(_mModelMatrix, 0);       
        
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
       
        // Bind the texture to this unit.
        if(SelectedDifficulty == 2)
        	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, i1);
        else
        	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, i0);    
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(_mTextureUniformHandle, 0);
        
        mHardDifficultyButtonTextureCoordinates.position(0);
        
        // Pass in the texture coordinate information
        mHardDifficultyButtonTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(_mTextureCoordinateHandle, _mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mHardDifficultyButtonTextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(_mTextureCoordinateHandle);        
      
        // Pass in the position information
        mHardDifficultyButtonPositions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mHardDifficultyButtonPositions);        
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);                       
                
        // Pass in the normal information
        mHardDifficultyButtonNormals.position(0);
        GLES20.glVertexAttribPointer(_mNormalHandle, _mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mHardDifficultyButtonNormals);
        
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

	public int HandleUserInput(int _iDisplayIndex, float _fX, float _fY)
	{
		if(_fX > 0.8f && _fX < 1.0f && -_fY < -halfx+0.5f && -_fY > -halfx)
		{
			//BackButton
			return 0;							
		}			
		if(_fX > ToggleSoundButtonLocations[0] && _fX < ToggleSoundButtonLocations[1] && -_fY < ToggleSoundButtonLocations[2] && -_fY > ToggleSoundButtonLocations[3])
		{			
			bToggleSound = !bToggleSound;							
		}
		if(_fX > ToggleMusicButtonLocations[0] && _fX < ToggleMusicButtonLocations[1] && -_fY < ToggleMusicButtonLocations[2] && -_fY > ToggleMusicButtonLocations[3])
		{			
			bToggleMusic = !bToggleMusic;							
		}
		if(_fX > EasyDifficultyButtonLocations[0] && _fX < EasyDifficultyButtonLocations[1] && -_fY < EasyDifficultyButtonLocations[2] && -_fY > EasyDifficultyButtonLocations[3])
		{			
			SelectedDifficulty = 0;							
		}
		if(_fX > MediumDifficultyButtonLocations[0] && _fX < MediumDifficultyButtonLocations[1] && -_fY < MediumDifficultyButtonLocations[2] && -_fY > MediumDifficultyButtonLocations[3])
		{			
			SelectedDifficulty = 1;							
		}
		if(_fX > HardDifficultyButtonLocations[0] && _fX < HardDifficultyButtonLocations[1] && -_fY < HardDifficultyButtonLocations[2] && -_fY > HardDifficultyButtonLocations[3])
		{			
			SelectedDifficulty = 2;							
		}		
		return _iDisplayIndex;		
	}	
}
