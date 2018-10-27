package roundoid.classes.game;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class RoundoidGLSurfaceView extends GLSurfaceView 
{	
	private RoundoidRenderer mRenderer;
	
	//Offsets for touch events	 
    private float mPreviousX;
    private float mPreviousY;    
    private float mDensity;    
    private double angle;    	
    
	public RoundoidGLSurfaceView(Context context) 
	{
		super(context);				
	}
	
	public RoundoidGLSurfaceView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		
		if (event != null)
		{			
			float x = event.getX();
			float y = event.getY();			
			
			if (event.getAction() == MotionEvent.ACTION_MOVE)
			{
				if (mRenderer != null)
				{
					float deltaX = (x - mPreviousX) / mDensity / 2f;
					float deltaY = (y - mPreviousY) / mDensity / 2f;
					
					mRenderer.mDeltaX += deltaX;
					mRenderer.mDeltaY += deltaY;	
					LeftPaddleLocation.DeltaX += deltaX;
					LeftPaddleLocation.DeltaY += deltaY;
					//Log.d("x y ",x + " - " + y);	
					mRenderer.GetOGLPos((int)x,(int)y);
				}
			}	
			
			mPreviousX = x;
			mPreviousY = y;
			CheckifBackButtonIsPressed();
			//gluUnProject
			return true;
		}
		else
		{
			return super.onTouchEvent(event);
		}		
		
		
	}

	// Hides superclass method.
	public void setRenderer(RoundoidRenderer renderer, float density) 
	{		
		
		this.setZOrderOnTop(true);
		this.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
		this.getHolder().setFormat(PixelFormat.RGBA_8888);
					    
		mRenderer = renderer;
		mDensity = density;
		super.setRenderer(renderer);
		
	}
	
	public void CheckifBackButtonIsPressed()
	{
		
		if(mPreviousX > 0.85f && mPreviousY < -1.22f)
		{
			System.exit(0);
		}
		
	}
	
}
