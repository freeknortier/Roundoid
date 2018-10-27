package roundoid.classes.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.util.Log;

public class BackGroundDisplayHandler {
	
	private final int mBytesPerFloat = 4;
	
	int screenHeight;
	int screenWidth;
	float halfx;
		
	BackGroundDisplayHandler bgdh; 
	
	//Timer Buffers
	public final FloatBuffer mBackGroundPositions;	
	public FloatBuffer mBackGroundNormals;
	public FloatBuffer mBackGroundTextureCoordinates;	

	public BackGroundDisplayHandler(int _screenHeight, int _screenWidth)
	{
		screenHeight = _screenHeight;
		screenWidth = _screenWidth;
		halfx = (float)_screenHeight/(float)_screenWidth;
		
		//Log.d("screenHeight screenWidth",screenHeight + " - " + screenWidth + " - " + halfx);
		
		float f1 = 0.15f;
		float f2 = 1.37f;
		float f3 = 1.0f;
		//Timer buffers	
		final float[] BackGroundPositions =
			{		
				    -1.0f, -halfx, 0.0f,
				    1.0f, -halfx, 0.0f,	
					-1.0f, halfx, 0.0f,		
					1.0f, -halfx, 0.0f,				
					1.0f, halfx, 0.0f,
					-1.0f, halfx, 0.0f,		
					
			};		
		final float[] BackGroundNormals = 
			{
				// Front face
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
			};
		final float[] BackGroundTextureCoordinates = 
			{				
				1.0f, 0.0f,			
				0.0f, 0.0f, 
				1.0f, 1.0f,
				0.0f, 0.0f, 
				0.0f, 1.0f,
				1.0f, 1.0f,
			};
		
		mBackGroundPositions  = ByteBuffer.allocateDirect(BackGroundPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mBackGroundPositions.put(BackGroundPositions).position(0);	
		
		mBackGroundNormals  = ByteBuffer.allocateDirect(BackGroundNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mBackGroundNormals.put(BackGroundNormals).position(0);	
		
		mBackGroundTextureCoordinates  = ByteBuffer.allocateDirect(BackGroundTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mBackGroundTextureCoordinates.put(BackGroundTextureCoordinates).position(0);	
	}
	
}
