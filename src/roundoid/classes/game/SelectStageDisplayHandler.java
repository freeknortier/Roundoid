package roundoid.classes.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import roundoid.classes.game.common.TextureHelper;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class SelectStageDisplayHandler {

private final int mBytesPerFloat = 4;
	
	int screenHeight;
	int screenWidth;
	float halfx;
	float halfy;
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
	
	//Select World Back Button Buffers
	public  FloatBuffer mSelectWorldBackButtonPositions;	
	public  FloatBuffer mSelectWorldBackButtonNormals;
	public  FloatBuffer mSelectWorldBackButtonTextureCoordinates;
	
	//Select Stage1 Buffers
	public FloatBuffer mSelectStage1Positions;	
	public FloatBuffer mSelectStage1Normals;
	public FloatBuffer mSelectStage1TextureCoordinates;
	public float[] SelectStage1ButtonLocations = new float[4];
	
	//Select Stage2 Buffers
	public  FloatBuffer mSelectStage2Positions;	
	public  FloatBuffer mSelectStage2Normals;
	public  FloatBuffer mSelectStage2TextureCoordinates;
	public float[] SelectStage2ButtonLocations = new float[4];
	
	//Select Stage3 Buffers
	public  FloatBuffer mSelectStage3Positions;	
	public  FloatBuffer mSelectStage3Normals;
	public  FloatBuffer mSelectStage3TextureCoordinates;
	public float[] SelectStage3ButtonLocations = new float[4];
	
	//Select Stage4 Buffers
	public  FloatBuffer mSelectStage4Positions;	
	public  FloatBuffer mSelectStage4Normals;
	public  FloatBuffer mSelectStage4TextureCoordinates;
	public float[] SelectStage4ButtonLocations = new float[4];
	
	//Select Stage5 Buffers
	public  FloatBuffer mSelectStage5Positions;	
	public  FloatBuffer mSelectStage5Normals;
	public  FloatBuffer mSelectStage5TextureCoordinates;
	public float[] SelectStage5ButtonLocations = new float[4];
	
	//Select Stage6 Buffers
	public  FloatBuffer mSelectStage6Positions;	
	public  FloatBuffer mSelectStage6Normals;
	public  FloatBuffer mSelectStage6TextureCoordinates;
	public float[] SelectStage6ButtonLocations = new float[4];
	
	//Select Stage7 Buffers
	public  FloatBuffer mSelectStage7Positions;	
	public  FloatBuffer mSelectStage7Normals;
	public  FloatBuffer mSelectStage7TextureCoordinates;
	public float[] SelectStage7ButtonLocations = new float[4];
	
	//Select Stage8 Buffers
	public  FloatBuffer mSelectStage8Positions;	
	public  FloatBuffer mSelectStage8Normals;
	public  FloatBuffer mSelectStage8TextureCoordinates;
	public float[] SelectStage8ButtonLocations = new float[4];
	
	//Select Stage9 Buffers
	public  FloatBuffer mSelectStage9Positions;	
	public  FloatBuffer mSelectStage9Normals;
	public  FloatBuffer mSelectStage9TextureCoordinates;
	public float[] SelectStage9ButtonLocations = new float[4];	
	
	public static int iSelectedBackGround = 1;
	private int mBackGroundTextureHandle1;
	private int mBackGroundTextureHandle2;
	private int mBackGroundTextureHandle3;
	private int mBackGroundTextureHandle4;
	private int mBackGroundTextureHandle5;
	
	
	public SelectStageDisplayHandler(int _screenHeight, int _screenWidth, Context _mActivityContext)
	{
		screenHeight = _screenHeight;
		screenWidth = _screenWidth;
		halfx = (float)_screenHeight/(float)_screenWidth;		
		halfy = (float)_screenWidth/(float)_screenHeight;		
				
		Speed.fXCoordinate3 = 0.7f;
		Speed.fYCoordinate3 = 0.0f;
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
		InitializeBackButtonBuffers();
		InitializeStage1Buffers();
		InitializeStage2Buffers();
		InitializeStage3Buffers();
		InitializeStage4Buffers();
		InitializeStage5Buffers();
		InitializeStage6Buffers();
		InitializeStage7Buffers();
		InitializeStage8Buffers();
		InitializeStage9Buffers();
		
		 mBackGroundTextureHandle1 = TextureHelper.loadTexture(_mActivityContext, R.drawable.backgroundsnow);	     
		 mBackGroundTextureHandle2 = TextureHelper.loadTexture(_mActivityContext, R.drawable.junglebackground);	          
		 mBackGroundTextureHandle3 = TextureHelper.loadTexture(_mActivityContext, R.drawable.desertbackground);	       
		 mBackGroundTextureHandle4 = TextureHelper.loadTexture(_mActivityContext, R.drawable.citybackground);	       
		 mBackGroundTextureHandle5 = TextureHelper.loadTexture(_mActivityContext, R.drawable.spacebackground);
	      	        
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
                    
        DrawSelectStage1(_iProgramHandle,_mMVPMatrixHandle,_mMVMatrixHandle,_mTextureUniformHandle,_mPositionHandle,_mNormalHandle,_mTextureCoordinateHandle,_mModelMatrix,_mBackGroundTextureHandle,_mTextureCoordinateDataSize,_mPositionDataSize,_mNormalDataSize,_mMVPMatrix, _mViewMatrix,_mTemporaryMatrix,_mProjectionMatrix,_iNumberZeroTextureHandle);
        DrawSelectStage2(_iProgramHandle,_mMVPMatrixHandle,_mMVMatrixHandle,_mTextureUniformHandle,_mPositionHandle,_mNormalHandle,_mTextureCoordinateHandle,_mModelMatrix,_mBackGroundTextureHandle,_mTextureCoordinateDataSize,_mPositionDataSize,_mNormalDataSize,_mMVPMatrix, _mViewMatrix,_mTemporaryMatrix,_mProjectionMatrix,_iNumberZeroTextureHandle);
        DrawSelectStage3(_iProgramHandle,_mMVPMatrixHandle,_mMVMatrixHandle,_mTextureUniformHandle,_mPositionHandle,_mNormalHandle,_mTextureCoordinateHandle,_mModelMatrix,_mBackGroundTextureHandle,_mTextureCoordinateDataSize,_mPositionDataSize,_mNormalDataSize,_mMVPMatrix, _mViewMatrix,_mTemporaryMatrix,_mProjectionMatrix,_iNumberZeroTextureHandle);
        DrawSelectStage4(_iProgramHandle,_mMVPMatrixHandle,_mMVMatrixHandle,_mTextureUniformHandle,_mPositionHandle,_mNormalHandle,_mTextureCoordinateHandle,_mModelMatrix,_mBackGroundTextureHandle,_mTextureCoordinateDataSize,_mPositionDataSize,_mNormalDataSize,_mMVPMatrix, _mViewMatrix,_mTemporaryMatrix,_mProjectionMatrix,_iNumberZeroTextureHandle);
        DrawSelectStage5(_iProgramHandle,_mMVPMatrixHandle,_mMVMatrixHandle,_mTextureUniformHandle,_mPositionHandle,_mNormalHandle,_mTextureCoordinateHandle,_mModelMatrix,_mBackGroundTextureHandle,_mTextureCoordinateDataSize,_mPositionDataSize,_mNormalDataSize,_mMVPMatrix, _mViewMatrix,_mTemporaryMatrix,_mProjectionMatrix,_iNumberZeroTextureHandle);
        DrawSelectStage6(_iProgramHandle,_mMVPMatrixHandle,_mMVMatrixHandle,_mTextureUniformHandle,_mPositionHandle,_mNormalHandle,_mTextureCoordinateHandle,_mModelMatrix,_mBackGroundTextureHandle,_mTextureCoordinateDataSize,_mPositionDataSize,_mNormalDataSize,_mMVPMatrix, _mViewMatrix,_mTemporaryMatrix,_mProjectionMatrix,_iNumberZeroTextureHandle);
        DrawSelectStage7(_iProgramHandle,_mMVPMatrixHandle,_mMVMatrixHandle,_mTextureUniformHandle,_mPositionHandle,_mNormalHandle,_mTextureCoordinateHandle,_mModelMatrix,_mBackGroundTextureHandle,_mTextureCoordinateDataSize,_mPositionDataSize,_mNormalDataSize,_mMVPMatrix, _mViewMatrix,_mTemporaryMatrix,_mProjectionMatrix,_iNumberZeroTextureHandle);
        DrawSelectStage8(_iProgramHandle,_mMVPMatrixHandle,_mMVMatrixHandle,_mTextureUniformHandle,_mPositionHandle,_mNormalHandle,_mTextureCoordinateHandle,_mModelMatrix,_mBackGroundTextureHandle,_mTextureCoordinateDataSize,_mPositionDataSize,_mNormalDataSize,_mMVPMatrix, _mViewMatrix,_mTemporaryMatrix,_mProjectionMatrix,_iNumberZeroTextureHandle);
        DrawSelectStage9(_iProgramHandle,_mMVPMatrixHandle,_mMVMatrixHandle,_mTextureUniformHandle,_mPositionHandle,_mNormalHandle,_mTextureCoordinateHandle,_mModelMatrix,_mBackGroundTextureHandle,_mTextureCoordinateDataSize,_mPositionDataSize,_mNormalDataSize,_mMVPMatrix, _mViewMatrix,_mTemporaryMatrix,_mProjectionMatrix,_iNumberZeroTextureHandle);
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
	
	public void InitializeStage1Buffers()
	{
		//Select Stage 1 Buffers
    	////////////////////////////////////////////////////////////
		
		float fHeightWidth = 0.15f;
		float xOffSet = ((halfy*2.0f)/6.0f)*2.0f;
		float yOffSet = ((halfx*2.0f)/6.0f)*2.0f;
				
		final float[] SelectWorldForwardPositions =
			{		
			    -fHeightWidth-xOffSet, fHeightWidth+yOffSet, 0.0f,
			    -fHeightWidth-xOffSet, -fHeightWidth+yOffSet, 0.0f,	
			    fHeightWidth-xOffSet, fHeightWidth+yOffSet, 0.0f,		
				-fHeightWidth-xOffSet, -fHeightWidth+yOffSet, 0.0f,				
				fHeightWidth-xOffSet, -fHeightWidth+yOffSet, 0.0f,
				fHeightWidth-xOffSet, fHeightWidth+yOffSet, 0.0f,	
			};		
		
		SelectStage1ButtonLocations[0] =  SelectWorldForwardPositions[0];
		SelectStage1ButtonLocations[1] =  SelectWorldForwardPositions[6];
		SelectStage1ButtonLocations[2] =  SelectWorldForwardPositions[1];
		SelectStage1ButtonLocations[3] =  SelectWorldForwardPositions[4];
		
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
		
		mSelectStage1Positions  = ByteBuffer.allocateDirect(SelectWorldForwardPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage1Positions.put(SelectWorldForwardPositions).position(0);	
		
		mSelectStage1Normals  = ByteBuffer.allocateDirect(SelectWorldForwardNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage1Normals.put(SelectWorldForwardNormals).position(0);	
		
		mSelectStage1TextureCoordinates  = ByteBuffer.allocateDirect(SelectWorldForwardTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage1TextureCoordinates.put(SelectWorldForwardTextureCoordinates).position(0);
	}
	
	public void InitializeStage2Buffers()
	{
		//Select Stage 2 Buffers
    	////////////////////////////////////////////////////////////
		
		float fHeightWidth = 0.15f;
		float xOffSet = ((halfy*2.0f)/6.0f)*2.0f;		
		
		final float[] SelectWorldForwardPositions =
			{		
				    -fHeightWidth-xOffSet, fHeightWidth, 0.0f,
				    -fHeightWidth-xOffSet, -fHeightWidth, 0.0f,	
				    fHeightWidth-xOffSet, fHeightWidth, 0.0f,		
					-fHeightWidth-xOffSet, -fHeightWidth, 0.0f,				
					fHeightWidth-xOffSet, -fHeightWidth, 0.0f,
					fHeightWidth-xOffSet, fHeightWidth, 0.0f,	
			};		
		
		SelectStage2ButtonLocations[0] =  SelectWorldForwardPositions[0];
		SelectStage2ButtonLocations[1] =  SelectWorldForwardPositions[6];
		SelectStage2ButtonLocations[2] =  SelectWorldForwardPositions[1];
		SelectStage2ButtonLocations[3] =  SelectWorldForwardPositions[4];
		
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
		
		mSelectStage2Positions  = ByteBuffer.allocateDirect(SelectWorldForwardPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage2Positions.put(SelectWorldForwardPositions).position(0);	
		
		mSelectStage2Normals  = ByteBuffer.allocateDirect(SelectWorldForwardNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage2Normals.put(SelectWorldForwardNormals).position(0);	
		
		mSelectStage2TextureCoordinates  = ByteBuffer.allocateDirect(SelectWorldForwardTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage2TextureCoordinates.put(SelectWorldForwardTextureCoordinates).position(0);
	}
	
	public void InitializeStage3Buffers()
	{
		//Select Stage 3 Buffers
    	////////////////////////////////////////////////////////////
		
		float fHeightWidth = 0.15f;
		float xOffSet = ((halfy*2.0f)/6.0f)*2.0f;
		float yOffSet = ((halfx*2.0f)/6.0f)*2.0f;
		
		final float[] SelectWorldForwardPositions =
			{		
				    -fHeightWidth-xOffSet, fHeightWidth-yOffSet, 0.0f,
				    -fHeightWidth-xOffSet, -fHeightWidth-yOffSet, 0.0f,	
				    fHeightWidth-xOffSet, fHeightWidth-yOffSet, 0.0f,		
					-fHeightWidth-xOffSet, -fHeightWidth-yOffSet, 0.0f,				
					fHeightWidth-xOffSet, -fHeightWidth-yOffSet, 0.0f,
					fHeightWidth-xOffSet, fHeightWidth-yOffSet, 0.0f,	
			};		
		
		SelectStage3ButtonLocations[0] =  SelectWorldForwardPositions[0];
		SelectStage3ButtonLocations[1] =  SelectWorldForwardPositions[6];
		SelectStage3ButtonLocations[2] =  SelectWorldForwardPositions[1];
		SelectStage3ButtonLocations[3] =  SelectWorldForwardPositions[4];
		
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
		
		mSelectStage3Positions  = ByteBuffer.allocateDirect(SelectWorldForwardPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage3Positions.put(SelectWorldForwardPositions).position(0);	
		
		mSelectStage3Normals  = ByteBuffer.allocateDirect(SelectWorldForwardNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage3Normals.put(SelectWorldForwardNormals).position(0);	
		
		mSelectStage3TextureCoordinates  = ByteBuffer.allocateDirect(SelectWorldForwardTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage3TextureCoordinates.put(SelectWorldForwardTextureCoordinates).position(0);
	}
	
	public void InitializeStage4Buffers()
	{
		//Select Stage 4 Buffers
    	////////////////////////////////////////////////////////////
		
		float fHeightWidth = 0.15f;		
		float yOffSet = ((halfx*2.0f)/6.0f)*2.0f;
		
		final float[] SelectWorldForwardPositions =
			{		
				    -fHeightWidth, fHeightWidth+yOffSet, 0.0f,
				    -fHeightWidth, -fHeightWidth+yOffSet, 0.0f,	
				    fHeightWidth, fHeightWidth+yOffSet, 0.0f,		
					-fHeightWidth, -fHeightWidth+yOffSet, 0.0f,				
					fHeightWidth, -fHeightWidth+yOffSet, 0.0f,
					fHeightWidth, fHeightWidth+yOffSet, 0.0f,	
			};	
		
		SelectStage4ButtonLocations[0] =  SelectWorldForwardPositions[0];
		SelectStage4ButtonLocations[1] =  SelectWorldForwardPositions[6];
		SelectStage4ButtonLocations[2] =  SelectWorldForwardPositions[1];
		SelectStage4ButtonLocations[3] =  SelectWorldForwardPositions[4];
		
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
		
		mSelectStage4Positions  = ByteBuffer.allocateDirect(SelectWorldForwardPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage4Positions.put(SelectWorldForwardPositions).position(0);	
		
		mSelectStage4Normals  = ByteBuffer.allocateDirect(SelectWorldForwardNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage4Normals.put(SelectWorldForwardNormals).position(0);	
		
		mSelectStage4TextureCoordinates  = ByteBuffer.allocateDirect(SelectWorldForwardTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage4TextureCoordinates.put(SelectWorldForwardTextureCoordinates).position(0);
	}
	
	public void InitializeStage5Buffers()
	{
		//Select Stage 5 Buffers
    	////////////////////////////////////////////////////////////
		
		float fHeightWidth = 0.15f;	
		
		final float[] SelectWorldForwardPositions =
			{		
				    -fHeightWidth, fHeightWidth, 0.0f,
				    -fHeightWidth, -fHeightWidth, 0.0f,	
				    fHeightWidth, fHeightWidth, 0.0f,		
					-fHeightWidth, -fHeightWidth, 0.0f,				
					fHeightWidth, -fHeightWidth, 0.0f,
					fHeightWidth, fHeightWidth, 0.0f,	
			};	
		
		SelectStage5ButtonLocations[0] =  SelectWorldForwardPositions[0];
		SelectStage5ButtonLocations[1] =  SelectWorldForwardPositions[6];
		SelectStage5ButtonLocations[2] =  SelectWorldForwardPositions[1];
		SelectStage5ButtonLocations[3] =  SelectWorldForwardPositions[4];
		
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
		
		mSelectStage5Positions  = ByteBuffer.allocateDirect(SelectWorldForwardPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage5Positions.put(SelectWorldForwardPositions).position(0);	
		
		mSelectStage5Normals  = ByteBuffer.allocateDirect(SelectWorldForwardNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage5Normals.put(SelectWorldForwardNormals).position(0);	
		
		mSelectStage5TextureCoordinates  = ByteBuffer.allocateDirect(SelectWorldForwardTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage5TextureCoordinates.put(SelectWorldForwardTextureCoordinates).position(0);
	}

	public void InitializeStage6Buffers()
	{
		//Select Stage 6 Buffers
    	////////////////////////////////////////////////////////////
		
		float fHeightWidth = 0.15f;		
		float yOffSet = ((halfx*2.0f)/6.0f)*2.0f;
		
		final float[] SelectWorldForwardPositions =
			{		
				    -fHeightWidth, fHeightWidth-yOffSet, 0.0f,
				    -fHeightWidth, -fHeightWidth-yOffSet, 0.0f,	
				    fHeightWidth, fHeightWidth-yOffSet, 0.0f,		
					-fHeightWidth, -fHeightWidth-yOffSet, 0.0f,				
					fHeightWidth, -fHeightWidth-yOffSet, 0.0f,
					fHeightWidth, fHeightWidth-yOffSet, 0.0f,	
			};	
		
		SelectStage6ButtonLocations[0] =  SelectWorldForwardPositions[0];
		SelectStage6ButtonLocations[1] =  SelectWorldForwardPositions[6];
		SelectStage6ButtonLocations[2] =  SelectWorldForwardPositions[1];
		SelectStage6ButtonLocations[3] =  SelectWorldForwardPositions[4];
		
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
		
		mSelectStage6Positions  = ByteBuffer.allocateDirect(SelectWorldForwardPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage6Positions.put(SelectWorldForwardPositions).position(0);	
		
		mSelectStage6Normals  = ByteBuffer.allocateDirect(SelectWorldForwardNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage6Normals.put(SelectWorldForwardNormals).position(0);	
		
		mSelectStage6TextureCoordinates  = ByteBuffer.allocateDirect(SelectWorldForwardTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage6TextureCoordinates.put(SelectWorldForwardTextureCoordinates).position(0);		
	}

	public void InitializeStage7Buffers()
	{
		//Select Stage 7 Buffers
    	////////////////////////////////////////////////////////////
		
		float fHeightWidth = 0.15f;
		float xOffSet = ((halfy*2.0f)/6.0f)*2.0f;
		float yOffSet = ((halfx*2.0f)/6.0f)*2.0f;
		
		final float[] SelectWorldForwardPositions =
			{		
				    -fHeightWidth+xOffSet, fHeightWidth+yOffSet, 0.0f,
				    -fHeightWidth+xOffSet, -fHeightWidth+yOffSet, 0.0f,	
				    fHeightWidth+xOffSet, fHeightWidth+yOffSet, 0.0f,		
					-fHeightWidth+xOffSet, -fHeightWidth+yOffSet, 0.0f,				
					fHeightWidth+xOffSet, -fHeightWidth+yOffSet, 0.0f,
					fHeightWidth+xOffSet, fHeightWidth+yOffSet, 0.0f,	
			};	
		
		SelectStage7ButtonLocations[0] =  SelectWorldForwardPositions[0];
		SelectStage7ButtonLocations[1] =  SelectWorldForwardPositions[6];
		SelectStage7ButtonLocations[2] =  SelectWorldForwardPositions[1];
		SelectStage7ButtonLocations[3] =  SelectWorldForwardPositions[4];
		
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
		
		mSelectStage7Positions  = ByteBuffer.allocateDirect(SelectWorldForwardPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage7Positions.put(SelectWorldForwardPositions).position(0);	
		
		mSelectStage7Normals  = ByteBuffer.allocateDirect(SelectWorldForwardNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage7Normals.put(SelectWorldForwardNormals).position(0);	
		
		mSelectStage7TextureCoordinates  = ByteBuffer.allocateDirect(SelectWorldForwardTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage7TextureCoordinates.put(SelectWorldForwardTextureCoordinates).position(0);
	}
	
	public void InitializeStage8Buffers()
	{
		//Select Stage 8 Buffers
    	////////////////////////////////////////////////////////////
		
		float fHeightWidth = 0.15f;
		float xOffSet = ((halfy*2.0f)/6.0f)*2.0f;		
		
		final float[] SelectWorldForwardPositions =
			{		
				    -fHeightWidth+xOffSet, fHeightWidth, 0.0f,
				    -fHeightWidth+xOffSet, -fHeightWidth, 0.0f,	
				    fHeightWidth+xOffSet, fHeightWidth, 0.0f,		
					-fHeightWidth+xOffSet, -fHeightWidth, 0.0f,				
					fHeightWidth+xOffSet, -fHeightWidth, 0.0f,
					fHeightWidth+xOffSet, fHeightWidth, 0.0f,	
			};	
		
		SelectStage8ButtonLocations[0] =  SelectWorldForwardPositions[0];
		SelectStage8ButtonLocations[1] =  SelectWorldForwardPositions[6];
		SelectStage8ButtonLocations[2] =  SelectWorldForwardPositions[1];
		SelectStage8ButtonLocations[3] =  SelectWorldForwardPositions[4];
		
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
		
		mSelectStage8Positions  = ByteBuffer.allocateDirect(SelectWorldForwardPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage8Positions.put(SelectWorldForwardPositions).position(0);	
		
		mSelectStage8Normals  = ByteBuffer.allocateDirect(SelectWorldForwardNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage8Normals.put(SelectWorldForwardNormals).position(0);	
		
		mSelectStage8TextureCoordinates  = ByteBuffer.allocateDirect(SelectWorldForwardTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage8TextureCoordinates.put(SelectWorldForwardTextureCoordinates).position(0);
	}
	
	public void InitializeStage9Buffers()
	{
		//Select Stage 9 Buffers
    	////////////////////////////////////////////////////////////
		
		float fHeightWidth = 0.15f;
		float xOffSet = ((halfy*2.0f)/6.0f)*2.0f;
		float yOffSet = ((halfx*2.0f)/6.0f)*2.0f;
		
		final float[] SelectWorldForwardPositions =
			{		
				    -fHeightWidth+xOffSet, fHeightWidth-yOffSet, 0.0f,
				    -fHeightWidth+xOffSet, -fHeightWidth-yOffSet, 0.0f,	
				    fHeightWidth+xOffSet, fHeightWidth-yOffSet, 0.0f,		
					-fHeightWidth+xOffSet, -fHeightWidth-yOffSet, 0.0f,				
					fHeightWidth+xOffSet, -fHeightWidth-yOffSet, 0.0f,
					fHeightWidth+xOffSet, fHeightWidth-yOffSet, 0.0f,	
			};	
		
		SelectStage9ButtonLocations[0] =  SelectWorldForwardPositions[0];
		SelectStage9ButtonLocations[1] =  SelectWorldForwardPositions[6];
		SelectStage9ButtonLocations[2] =  SelectWorldForwardPositions[1];
		SelectStage9ButtonLocations[3] =  SelectWorldForwardPositions[4];
		
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
		
		mSelectStage9Positions  = ByteBuffer.allocateDirect(SelectWorldForwardPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage9Positions.put(SelectWorldForwardPositions).position(0);	
		
		mSelectStage9Normals  = ByteBuffer.allocateDirect(SelectWorldForwardNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage9Normals.put(SelectWorldForwardNormals).position(0);	
		
		mSelectStage9TextureCoordinates  = ByteBuffer.allocateDirect(SelectWorldForwardTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSelectStage9TextureCoordinates.put(SelectWorldForwardTextureCoordinates).position(0);
	}
	
	public void DrawSelectStage1(int _iProgramHandle, int _mMVPMatrixHandle, int _mMVMatrixHandle,int _mTextureUniformHandle,int _mPositionHandle,int _mNormalHandle,int _mTextureCoordinateHandle, float[] _mModelMatrix,int _mBackGroundTextureHandle,int _mTextureCoordinateDataSize,int _mPositionDataSize,int _mNormalDataSize,float[] _mMVPMatrix, float[] _mViewMatrix,float[] _mTemporaryMatrix,float[] _mProjectionMatrix,int _iNumberZeroTextureHandle)
	{
		//Draw Select Stage 1
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
        
        mSelectStage1TextureCoordinates.position(0);
        
        // Pass in the texture coordinate information
        mSelectStage1TextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(_mTextureCoordinateHandle, _mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectStage1TextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(_mTextureCoordinateHandle);        
      
        		// Pass in the position information
        mSelectStage1Positions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mSelectStage1Positions);        
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);                       
                
        // Pass in the normal information
        mSelectStage1Normals.position(0);
        GLES20.glVertexAttribPointer(_mNormalHandle, _mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectStage1Normals);
        
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
	
	public void DrawSelectStage2(int _iProgramHandle, int _mMVPMatrixHandle, int _mMVMatrixHandle,int _mTextureUniformHandle,int _mPositionHandle,int _mNormalHandle,int _mTextureCoordinateHandle, float[] _mModelMatrix,int _mBackGroundTextureHandle,int _mTextureCoordinateDataSize,int _mPositionDataSize,int _mNormalDataSize,float[] _mMVPMatrix, float[] _mViewMatrix,float[] _mTemporaryMatrix,float[] _mProjectionMatrix,int _iNumberZeroTextureHandle)
	{
		//Draw Select Stage 2
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
        
        mSelectStage2TextureCoordinates.position(0);
        
        // Pass in the texture coordinate information
        mSelectStage2TextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(_mTextureCoordinateHandle, _mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectStage2TextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(_mTextureCoordinateHandle);        
      
        		// Pass in the position information
        mSelectStage2Positions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mSelectStage2Positions);        
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);                       
                
        // Pass in the normal information
        mSelectStage2Normals.position(0);
        GLES20.glVertexAttribPointer(_mNormalHandle, _mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectStage2Normals);
        
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

	public void DrawSelectStage3(int _iProgramHandle, int _mMVPMatrixHandle, int _mMVMatrixHandle,int _mTextureUniformHandle,int _mPositionHandle,int _mNormalHandle,int _mTextureCoordinateHandle, float[] _mModelMatrix,int _mBackGroundTextureHandle,int _mTextureCoordinateDataSize,int _mPositionDataSize,int _mNormalDataSize,float[] _mMVPMatrix, float[] _mViewMatrix,float[] _mTemporaryMatrix,float[] _mProjectionMatrix,int _iNumberZeroTextureHandle)
	{
		//Draw Select Stage 3
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
        
        mSelectStage3TextureCoordinates.position(0);
        
        // Pass in the texture coordinate information
        mSelectStage3TextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(_mTextureCoordinateHandle, _mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectStage3TextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(_mTextureCoordinateHandle);        
      
        		// Pass in the position information
        mSelectStage3Positions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mSelectStage3Positions);        
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);                       
                
        // Pass in the normal information
        mSelectStage3Normals.position(0);
        GLES20.glVertexAttribPointer(_mNormalHandle, _mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectStage3Normals);
        
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

	public void DrawSelectStage4(int _iProgramHandle, int _mMVPMatrixHandle, int _mMVMatrixHandle,int _mTextureUniformHandle,int _mPositionHandle,int _mNormalHandle,int _mTextureCoordinateHandle, float[] _mModelMatrix,int _mBackGroundTextureHandle,int _mTextureCoordinateDataSize,int _mPositionDataSize,int _mNormalDataSize,float[] _mMVPMatrix, float[] _mViewMatrix,float[] _mTemporaryMatrix,float[] _mProjectionMatrix,int _iNumberZeroTextureHandle)
	{
		//Draw Select Stage 4
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
        
        mSelectStage4TextureCoordinates.position(0);
        
        // Pass in the texture coordinate information
        mSelectStage4TextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(_mTextureCoordinateHandle, _mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectStage4TextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(_mTextureCoordinateHandle);        
      
        		// Pass in the position information
        mSelectStage4Positions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mSelectStage4Positions);        
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);                       
                
        // Pass in the normal information
        mSelectStage4Normals.position(0);
        GLES20.glVertexAttribPointer(_mNormalHandle, _mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectStage4Normals);
        
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

	public void DrawSelectStage5(int _iProgramHandle, int _mMVPMatrixHandle, int _mMVMatrixHandle,int _mTextureUniformHandle,int _mPositionHandle,int _mNormalHandle,int _mTextureCoordinateHandle, float[] _mModelMatrix,int _mBackGroundTextureHandle,int _mTextureCoordinateDataSize,int _mPositionDataSize,int _mNormalDataSize,float[] _mMVPMatrix, float[] _mViewMatrix,float[] _mTemporaryMatrix,float[] _mProjectionMatrix,int _iNumberZeroTextureHandle)
	{
		//Draw Select Stage 5
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
        
        mSelectStage5TextureCoordinates.position(0);
        
        // Pass in the texture coordinate information
        mSelectStage5TextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(_mTextureCoordinateHandle, _mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectStage5TextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(_mTextureCoordinateHandle);        
      
        		// Pass in the position information
        mSelectStage5Positions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mSelectStage5Positions);        
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);                       
                
        // Pass in the normal information
        mSelectStage5Normals.position(0);
        GLES20.glVertexAttribPointer(_mNormalHandle, _mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectStage5Normals);
        
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

	public void DrawSelectStage6(int _iProgramHandle, int _mMVPMatrixHandle, int _mMVMatrixHandle,int _mTextureUniformHandle,int _mPositionHandle,int _mNormalHandle,int _mTextureCoordinateHandle, float[] _mModelMatrix,int _mBackGroundTextureHandle,int _mTextureCoordinateDataSize,int _mPositionDataSize,int _mNormalDataSize,float[] _mMVPMatrix, float[] _mViewMatrix,float[] _mTemporaryMatrix,float[] _mProjectionMatrix,int _iNumberZeroTextureHandle)
	{
		//Draw Select Stage 6
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
        
        mSelectStage6TextureCoordinates.position(0);
        
        // Pass in the texture coordinate information
        mSelectStage6TextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(_mTextureCoordinateHandle, _mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectStage6TextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(_mTextureCoordinateHandle);        
      
        		// Pass in the position information
        mSelectStage6Positions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mSelectStage6Positions);        
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);                       
                
        // Pass in the normal information
        mSelectStage6Normals.position(0);
        GLES20.glVertexAttribPointer(_mNormalHandle, _mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectStage6Normals);
        
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
	
	public void DrawSelectStage7(int _iProgramHandle, int _mMVPMatrixHandle, int _mMVMatrixHandle,int _mTextureUniformHandle,int _mPositionHandle,int _mNormalHandle,int _mTextureCoordinateHandle, float[] _mModelMatrix,int _mBackGroundTextureHandle,int _mTextureCoordinateDataSize,int _mPositionDataSize,int _mNormalDataSize,float[] _mMVPMatrix, float[] _mViewMatrix,float[] _mTemporaryMatrix,float[] _mProjectionMatrix,int _iNumberZeroTextureHandle)
	{
		//Draw Select Stage 7
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
        
        mSelectStage7TextureCoordinates.position(0);
        
        // Pass in the texture coordinate information
        mSelectStage7TextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(_mTextureCoordinateHandle, _mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectStage7TextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(_mTextureCoordinateHandle);        
      
        		// Pass in the position information
        mSelectStage7Positions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mSelectStage7Positions);        
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);                       
                
        // Pass in the normal information
        mSelectStage7Normals.position(0);
        GLES20.glVertexAttribPointer(_mNormalHandle, _mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectStage7Normals);
        
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
	
	public void DrawSelectStage8(int _iProgramHandle, int _mMVPMatrixHandle, int _mMVMatrixHandle,int _mTextureUniformHandle,int _mPositionHandle,int _mNormalHandle,int _mTextureCoordinateHandle, float[] _mModelMatrix,int _mBackGroundTextureHandle,int _mTextureCoordinateDataSize,int _mPositionDataSize,int _mNormalDataSize,float[] _mMVPMatrix, float[] _mViewMatrix,float[] _mTemporaryMatrix,float[] _mProjectionMatrix,int _iNumberZeroTextureHandle)
	{
		//Draw Select Stage 8
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
        
        mSelectStage8TextureCoordinates.position(0);
        
        // Pass in the texture coordinate information
        mSelectStage8TextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(_mTextureCoordinateHandle, _mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectStage8TextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(_mTextureCoordinateHandle);        
      
        		// Pass in the position information
        mSelectStage8Positions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mSelectStage8Positions);        
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);                       
                
        // Pass in the normal information
        mSelectStage8Normals.position(0);
        GLES20.glVertexAttribPointer(_mNormalHandle, _mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectStage8Normals);
        
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
	
	public void DrawSelectStage9(int _iProgramHandle, int _mMVPMatrixHandle, int _mMVMatrixHandle,int _mTextureUniformHandle,int _mPositionHandle,int _mNormalHandle,int _mTextureCoordinateHandle, float[] _mModelMatrix,int _mBackGroundTextureHandle,int _mTextureCoordinateDataSize,int _mPositionDataSize,int _mNormalDataSize,float[] _mMVPMatrix, float[] _mViewMatrix,float[] _mTemporaryMatrix,float[] _mProjectionMatrix,int _iNumberZeroTextureHandle)
	{
		//Draw Select Stage 9
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
        
        mSelectStage9TextureCoordinates.position(0);
        
        // Pass in the texture coordinate information
        mSelectStage9TextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(_mTextureCoordinateHandle, _mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectStage9TextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(_mTextureCoordinateHandle);        
      
       	// Pass in the position information
        mSelectStage9Positions.position(0);		
        GLES20.glVertexAttribPointer(_mPositionHandle, _mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mSelectStage9Positions);        
                
        GLES20.glEnableVertexAttribArray(_mPositionHandle);                       
                
        // Pass in the normal information
        mSelectStage9Normals.position(0);
        GLES20.glVertexAttribPointer(_mNormalHandle, _mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, mSelectStage9Normals);
        
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
		if(_fX > SelectStage1ButtonLocations[0] && _fX < SelectStage1ButtonLocations[1] && -_fY < SelectStage1ButtonLocations[2] && -_fY > SelectStage1ButtonLocations[3])
		{			
			//Stage 1 Selected
			return 1;					
		}
		if(_fX > SelectStage2ButtonLocations[0] && _fX < SelectStage2ButtonLocations[1] && -_fY < SelectStage2ButtonLocations[2] && -_fY > SelectStage2ButtonLocations[3])
		{			
			//Stage 2 Selected
			return 1;					
		}
		if(_fX > SelectStage3ButtonLocations[0] && _fX < SelectStage3ButtonLocations[1] && -_fY < SelectStage3ButtonLocations[2] && -_fY > SelectStage3ButtonLocations[3])
		{			
			//Stage 3 Selected
			return 1;					
		}
		if(_fX > SelectStage4ButtonLocations[0] && _fX < SelectStage4ButtonLocations[1] && -_fY < SelectStage4ButtonLocations[2] && -_fY > SelectStage4ButtonLocations[3])
		{			
			//Stage 4 Selected
			return 1;					
		}		
		if(_fX > SelectStage5ButtonLocations[0] && _fX < SelectStage5ButtonLocations[1] && -_fY < SelectStage5ButtonLocations[2] && -_fY > SelectStage5ButtonLocations[3])
		{			
			//Stage 5 Selected
			return 1;					
		}
		if(_fX > SelectStage6ButtonLocations[0] && _fX < SelectStage6ButtonLocations[1] && -_fY < SelectStage6ButtonLocations[2] && -_fY > SelectStage6ButtonLocations[3])
		{			
			//Stage 6 Selected
			return 1;					
		}
		if(_fX > SelectStage7ButtonLocations[0] && _fX < SelectStage7ButtonLocations[1] && -_fY < SelectStage7ButtonLocations[2] && -_fY > SelectStage7ButtonLocations[3])
		{			
			//Stage 7 Selected
			return 1;					
		}
		if(_fX > SelectStage8ButtonLocations[0] && _fX < SelectStage8ButtonLocations[1] && -_fY < SelectStage8ButtonLocations[2] && -_fY > SelectStage8ButtonLocations[3])
		{			
			//Stage 8 Selected
			return 1;					
		}
		if(_fX > SelectStage9ButtonLocations[0] && _fX < SelectStage9ButtonLocations[1] && -_fY < SelectStage9ButtonLocations[2] && -_fY > SelectStage9ButtonLocations[3])
		{			
			//Stage 9 Selected
			return 1;					
		}
		if(_fX > 0.8f && _fX < 1.0f && -_fY < -halfx+0.5f && -_fY > -halfx)
		{
			//BackButton
			return 2;							
		}			
		return _iDisplayIndex;
	}	
}
