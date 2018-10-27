package roundoid.classes.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class BackButtonDisplayHandler {

	/** How many bytes per float. */
	private final int mBytesPerFloat = 4;
	
	//Timer Buffers
	public final FloatBuffer mBackButtonPositions;	
	public final FloatBuffer mBackButtonNormals;
	public final FloatBuffer mBackButtonTextureCoordinates;	
	int screenHeight;
	int screenWidth;
	float halfx;
	
	public BackButtonDisplayHandler(int _screenHeight,int _screenWidth)
	{
		screenHeight = _screenHeight;
		screenWidth = _screenWidth;
		halfx = (float)_screenHeight/(float)_screenWidth;
		
		float f1 = 0.15f;
		float f2 = 1.37f;
		float f3 = 1.0f;
		//Timer buffers	
		final float[] BackButtonPositions =
			{		
				    0.7f, -halfx+0.3f, 0.0f,
				    0.7f, -halfx, 0.0f,	
					1.0f, -halfx+0.3f, 0.0f,		
					0.7f, -halfx, 0.0f,				
					1.0f, -halfx, 0.0f,
					1.0f, -halfx+0.3f, 0.0f,		
					
			};		
		final float[] BackButtonNormals = 
			{
				// Front face
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
			};
		final float[] BackButtonTextureCoordinates = 
			{				
				1.0f, 0.0f,			
				0.0f, 0.0f, 
				1.0f, 1.0f,
				0.0f, 0.0f, 
				0.0f, 1.0f,
				1.0f, 1.0f,
			};
		
		mBackButtonPositions  = ByteBuffer.allocateDirect(BackButtonPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mBackButtonPositions.put(BackButtonPositions).position(0);	
		
		mBackButtonNormals  = ByteBuffer.allocateDirect(BackButtonNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mBackButtonNormals.put(BackButtonNormals).position(0);	
		
		mBackButtonTextureCoordinates  = ByteBuffer.allocateDirect(BackButtonTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mBackButtonTextureCoordinates.put(BackButtonTextureCoordinates).position(0);	
	}	
}
