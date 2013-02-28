package edu.vu.augmented.reality;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Camera mCamera;
    private CameraPreview mPreview;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	if (mCamera != null){
    		mCamera.release();
    	}
    }
    
    @Override
    public void onResume(){
    	super.onResume();
        if (checkCameraHardware(this)){
            setContentView(R.layout.activity_main);
            
            // Create an instance of Camera
            mCamera = getCameraInstance();
            if (mCamera == null){
            	Toast.makeText(getApplicationContext(), "Camera Unavailable", Toast.LENGTH_SHORT).show();
            	showErrorView();
            	return;
            }
            Camera.Parameters cp = mCamera.getParameters();
            List<Camera.Size> sizes = cp.getSupportedPreviewSizes();
            cp.setPreviewSize(sizes.get(sizes.size()-1).width, sizes.get(sizes.size()-1).height);
            mCamera.setParameters(cp);
            
            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview,0);
        } else {
            showErrorView();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        } else {
            return false;
        }
    }
    
    private void showErrorView(){
    	TextView errorView;
        errorView = new TextView(getApplicationContext());
        errorView.setBackgroundResource(android.R.drawable.dark_header);
        errorView.setGravity(Gravity.CENTER_VERTICAL);
        errorView.setFocusable(false);
        errorView.setClickable(false);
        errorView.setLongClickable(false);
        errorView.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
            }
        });
        errorView.setTextSize((float) 14.0);
        errorView.setTypeface(Typeface.DEFAULT_BOLD);
        errorView.setText("Sorry, your device either does not have a camera or its camera is not compatible with this application.");
        setContentView(errorView);
    }
    
    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
}
