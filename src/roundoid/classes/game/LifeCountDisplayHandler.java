package roundoid.classes.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Timer;
import java.util.TimerTask;
import roundoid.classes.game.common.TextureHelper;

public class LifeCountDisplayHandler {

	public final FloatBuffer mLifeOnePositions;	
	public final FloatBuffer mLifeOneNormals;
	public final FloatBuffer mLifeOneTextureCoordinates;	
		
	public final FloatBuffer mLifeTwoPositions;	
	public final FloatBuffer mLifeTwoNormals;
	public final FloatBuffer mLifeTwoTextureCoordinates;	

	/** How many bytes per float. */
	private final int mBytesPerFloat = 4;
	
	public LifeCountDisplayHandler()
	{
		float f1 = 0.1f;
		float f2 = 1.37f;
		float f3 = -0.79f;
		//Life One buffers	
		final float[] LifeOnePositions =
			{		
				    -f1+f3, -0.1f-f2, 1.0f,
				    f1+f3, -0.1f-f2, 1.0f,	
					-f1+f3, 0.1f-f2, 1.0f,		
					f1+f3, -0.1f-f2, 1.0f,				
					f1+f3, 0.1f-f2, 1.0f,
					-f1+f3, 0.1f-f2, 1.0f,		
					
			};		
		final float[] LifeOneNormals = 
			{
				// Front face
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
			};
		final float[] LifeOneTextureCoordinates = 
			{				
				1.0f, 0.0f,			
				0.0f, 0.0f, 
				1.0f, 1.0f,
				0.0f, 0.0f, 
				0.0f, 1.0f,
				1.0f, 1.0f,
			};
		
		mLifeOnePositions  = ByteBuffer.allocateDirect(LifeOnePositions .length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mLifeOnePositions.put(LifeOnePositions ).position(0);	
		
		mLifeOneNormals  = ByteBuffer.allocateDirect(LifeOneNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mLifeOneNormals.put(LifeOneNormals).position(0);	
		
		mLifeOneTextureCoordinates  = ByteBuffer.allocateDirect(LifeOneTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mLifeOneTextureCoordinates.put(LifeOneTextureCoordinates).position(0);			
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		float f12 = 0.1f;
		float f22 = 1.15f;
		float f32 = -0.79f;
		
		//Life Two buffers	
		final float[] MinuteOnePositions =
			{		
				    -f12+f32, -0.1f-f22, 1.0f,
				    f12+f32, -0.1f-f22, 1.0f,	
					-f12+f32, 0.1f-f22, 1.0f,		
					f12+f32, -0.1f-f22, 1.0f,				
					f12+f32, 0.1f-f22, 1.0f,
					-f12+f32, 0.1f-f22, 1.0f,		
					
			};		
		final float[] MinuteOneNormals = 
			{
				// Front face
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
			};
		final float[] MinuteOneTextureCoordinates = 
			{				
				1.0f, 0.0f,			
				0.0f, 0.0f, 
				1.0f, 1.0f,
				0.0f, 0.0f, 
				0.0f, 1.0f,
				1.0f, 1.0f,
			};
		
		mLifeTwoPositions  = ByteBuffer.allocateDirect(MinuteOnePositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mLifeTwoPositions.put(MinuteOnePositions).position(0);	
		
		mLifeTwoNormals  = ByteBuffer.allocateDirect(MinuteOneNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mLifeTwoNormals.put(MinuteOneNormals).position(0);	
		
		mLifeTwoTextureCoordinates  = ByteBuffer.allocateDirect(MinuteOneTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mLifeTwoTextureCoordinates.put(MinuteOneTextureCoordinates).position(0);	
		
	}
	
}
