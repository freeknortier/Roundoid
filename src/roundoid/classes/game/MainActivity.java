package roundoid.classes.game;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLES20;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import roundoid.classes.game.R;

public class MainActivity extends Activity {

	private RoundoidGLSurfaceView mGLSurfaceView;
	private RoundoidRenderer mRenderer;
		
	private int mMinSetting = -1;
	private int mMagSetting = -1;
	
	private static final String MIN_SETTING = "min_setting";
	private static final String MAG_SETTING = "mag_setting";
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		mGLSurfaceView = (RoundoidGLSurfaceView)findViewById(R.id.gl_surface_view);

		// Check if the system supports OpenGL ES 2.0.
		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

		//Log.d("Support?", String.valueOf(supportsEs2));
		
		// Request an OpenGL ES 2.0 compatible context.
		mGLSurfaceView.setEGLContextClientVersion(2);
		
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		// Set the renderer to our demo renderer, defined below.
		mRenderer = new RoundoidRenderer(this);
		mGLSurfaceView.setRenderer(mRenderer, displayMetrics.density);					
		
		setMinSetting(); 
		setMagSetting(); 		
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
        
	@Override
	protected void onResume() 
	{
		// The activity must call the GL surface view's onResume() on activity
		// onResume().
		super.onResume();
		mGLSurfaceView.onResume();
	}

	@Override
	protected void onPause() 
	{
		// The activity must call the GL surface view's onPause() on activity
		// onPause().
		super.onPause();
		mGLSurfaceView.onPause();
	}
	
	@Override
	protected void onSaveInstanceState (Bundle outState)
	{
		outState.putInt(MIN_SETTING, mMinSetting);
		outState.putInt(MAG_SETTING, mMagSetting);
	}

	private void setMinSetting()
	{	
		mGLSurfaceView.queueEvent(new Runnable()
		{
			//@Override
			public void run()
			{				
				//mRenderer.igdh.setMinFilter(GLES20.GL_LINEAR_MIPMAP_LINEAR);
			}
		});
	}

	private void setMagSetting()
	{		
		mGLSurfaceView.queueEvent(new Runnable()
		{
			//@Override
			public void run()
			{									
				
				//mRenderer.igdh.setMagFilter(GLES20.GL_LINEAR);
			}
		});
	}	
}
