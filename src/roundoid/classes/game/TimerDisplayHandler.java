package roundoid.classes.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Timer;
import java.util.TimerTask;
import roundoid.classes.game.common.TextureHelper;

public class TimerDisplayHandler {

	long millis = System.currentTimeMillis();
	
	//Timer Buffers
	public final FloatBuffer mMinuteOnePositions;	
	public final FloatBuffer mMinuteOneNormals;
	public final FloatBuffer mMinuteOneTextureCoordinates;	
		
	public final FloatBuffer mMinuteTwoPositions;	
	public final FloatBuffer mMinuteTwoNormals;
	public final FloatBuffer mMinuteTwoTextureCoordinates;
	
	public final FloatBuffer mSecondOnePositions;	
	public final FloatBuffer mSecondOneNormals;
	public final FloatBuffer mSecondOneTextureCoordinates;
	
	public final FloatBuffer mSecondTwoPositions;	
	public final FloatBuffer mSecondTwoNormals;
	public final FloatBuffer mSecondTwoTextureCoordinates;
	
	/** How many bytes per float. */
	private final int mBytesPerFloat = 4;
	
	
	public TimerDisplayHandler()
	{
		float f1 = 0.1f;
		float f2 = 1.37f;
		float f3 = -1.0f;
		//Timer buffers	
		final float[] MinuteOnePositions =
			{		
				    -f1+f3, -0.1f-f2, 1.0f,
				    f1+f3, -0.1f-f2, 1.0f,	
					-f1+f3, 0.1f-f2, 1.0f,		
					f1+f3, -0.1f-f2, 1.0f,				
					f1+f3, 0.1f-f2, 1.0f,
					-f1+f3, 0.1f-f2, 1.0f,		
					
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
		
		mMinuteOnePositions  = ByteBuffer.allocateDirect(MinuteOnePositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMinuteOnePositions.put(MinuteOnePositions).position(0);	
		
		mMinuteOneNormals  = ByteBuffer.allocateDirect(MinuteOneNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMinuteOneNormals.put(MinuteOneNormals).position(0);	
		
		mMinuteOneTextureCoordinates  = ByteBuffer.allocateDirect(MinuteOneTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMinuteOneTextureCoordinates.put(MinuteOneTextureCoordinates).position(0);	
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		float f12 = 0.1f;
		float f22 = 1.15f;
		float f32 = -1.0f;
		//Timer buffers	
		final float[] MinuteTwoPositions =
			{						
			    -f12+f32, -0.1f-f22, 1.0f,
			    f12+f32, -0.1f-f22, 1.0f,	
				-f12+f32, 0.1f-f22, 1.0f,		
				f12+f32, -0.1f-f22, 1.0f,				
				f12+f32, 0.1f-f22, 1.0f,
				-f12+f32, 0.1f-f22, 1.0f,		
					
			};		
		final float[] MinuteTwoNormals = 
			{
				// Front face
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
			};
		final float[] MinuteTwoTextureCoordinates = 
			{				
				1.0f, 0.0f,			
				0.0f, 0.0f, 
				1.0f, 1.0f,
				0.0f, 0.0f, 
				0.0f, 1.0f,
				1.0f, 1.0f,
			};
		
		mMinuteTwoPositions  = ByteBuffer.allocateDirect(MinuteTwoPositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMinuteTwoPositions.put(MinuteTwoPositions).position(0);	
		
		mMinuteTwoNormals  = ByteBuffer.allocateDirect(MinuteTwoNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMinuteTwoNormals.put(MinuteTwoNormals).position(0);	
		
		mMinuteTwoTextureCoordinates  = ByteBuffer.allocateDirect(MinuteTwoTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mMinuteTwoTextureCoordinates.put(MinuteTwoTextureCoordinates).position(0);	
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		

		float f123 = 0.1f;
		float f223 = 0.93f;
		float f323 = -1.0f;
		//Timer buffers	
		final float[] SecondOnePositions =
			{					
			    -f123+f323, -0.1f-f223, 1.0f,
			    f123+f323, -0.1f-f223, 1.0f,	
				-f123+f323, 0.1f-f223, 1.0f,		
				f123+f323, -0.1f-f223, 1.0f,				
				f123+f323, 0.1f-f223, 1.0f,
				-f123+f323, 0.1f-f223, 1.0f,		
					
			};		
		final float[] SecondOneNormals = 
			{
				// Front face
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
			};
		final float[] SecondOneTextureCoordinates = 
			{
				// Front face				
				1.0f, 0.0f,			
				0.0f, 0.0f, 
				1.0f, 1.0f,
				0.0f, 0.0f, 
				0.0f, 1.0f,
				1.0f, 1.0f,
			};
		
		mSecondOnePositions  = ByteBuffer.allocateDirect(SecondOnePositions.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSecondOnePositions.put(SecondOnePositions).position(0);	
		
		mSecondOneNormals  = ByteBuffer.allocateDirect(SecondOneNormals.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSecondOneNormals.put(SecondOneNormals).position(0);	
		
		mSecondOneTextureCoordinates  = ByteBuffer.allocateDirect(SecondOneTextureCoordinates.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSecondOneTextureCoordinates.put(SecondOneTextureCoordinates).position(0);	
	
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
		
		float f1234 = 0.1f;
		float f2234 = 0.71f;
		float f3234 = -1.0f;
		//Timer buffers	
		final float[] SecondTwoPositions =
		{		
		// Front face	
		
		-f1234+f3234, -0.1f-f2234, 1.0f,
		f1234+f3234, -0.1f-f2234, 1.0f,	
		-f123+f3234, 0.1f-f2234, 1.0f,		
		f1234+f3234, -0.1f-f2234, 1.0f,				
		f1234+f3234, 0.1f-f2234, 1.0f,
		-f1234+f3234, 0.1f-f2234, 1.0f,		
		
		};		
		final float[] SecondTwoNormals = 
		{
		// Front face
		0.0f, 0.0f, 1.0f,				
		0.0f, 0.0f, 1.0f,
		0.0f, 0.0f, 1.0f,
		0.0f, 0.0f, 1.0f,				
		0.0f, 0.0f, 1.0f,
		0.0f, 0.0f, 1.0f,
		};
		final float[] SecondTwoTextureCoordinates = 
		{
		// Front face
		
		1.0f, 0.0f,			
		0.0f, 0.0f, 
		1.0f, 1.0f,
		0.0f, 0.0f, 
		0.0f, 1.0f,
		1.0f, 1.0f,
		};
		
		mSecondTwoPositions  = ByteBuffer.allocateDirect(SecondTwoPositions.length * mBytesPerFloat)
		.order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSecondTwoPositions.put(SecondTwoPositions).position(0);	
		
		mSecondTwoNormals  = ByteBuffer.allocateDirect(SecondTwoNormals.length * mBytesPerFloat)
		.order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSecondTwoNormals.put(SecondTwoNormals).position(0);	
		
		mSecondTwoTextureCoordinates  = ByteBuffer.allocateDirect(SecondTwoTextureCoordinates.length * mBytesPerFloat)
		.order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mSecondTwoTextureCoordinates.put(SecondTwoTextureCoordinates).position(0);
	}	
}
