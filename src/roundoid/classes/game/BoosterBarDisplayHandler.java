package roundoid.classes.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class BoosterBarDisplayHandler {

	/** How many bytes per float. */
	private final int mBytesPerFloat = 4;
	
	//Timer Buffers
	public final FloatBuffer mBoosterBarPositions;	
	public final FloatBuffer mBoosterBarNormals;
	public final FloatBuffer mBoosterBarTextureCoordinates;	

	public BoosterBarDisplayHandler()
	{
		float f1 = 0.15f;
		float f2 = 1.37f;
		float f3 = 1.0f;
		//Timer buffers	
		final float[] BoosterBarPositions =
			{		
				    -1.1f, 1.15f, 1.0f,
				    1.1f, 1.15f, 1.0f,	
					-1.1f, 1.4355f, 1.0f,		
					 1.1f, 1.15f, 1.0f,				
					1.1f, 1.4355f, 1.0f,
					-1.1f, 1.4355f, 1.0f,		
					
			};		
		final float[] BoosterBarNormals = 
			{
				// Front face
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
			};
		final float[] BoosterBarTextureCoordinates = 
			{				
				1.0f, 0.0f,			
				0.0f, 0.0f, 
				1.0f, 1.0f,
				0.0f, 0.0f, 
				0.0f, 1.0f,
				1.0f, 1.0f,
			};
		
		mBoosterBarPositions  = ByteBuffer.allocateDirect(BoosterBarPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mBoosterBarPositions.put(BoosterBarPositions).position(0);	
		
		mBoosterBarNormals  = ByteBuffer.allocateDirect(BoosterBarNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mBoosterBarNormals.put(BoosterBarNormals).position(0);	
		
		mBoosterBarTextureCoordinates  = ByteBuffer.allocateDirect(BoosterBarTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mBoosterBarTextureCoordinates.put(BoosterBarTextureCoordinates).position(0);	
	}	
}
