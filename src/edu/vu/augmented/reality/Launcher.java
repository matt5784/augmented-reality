package edu.vu.augmented.reality;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class Launcher extends Activity {
    GridView gridView;
    private static String LOGTAG = "augmented-reality";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher);

        gridView = (GridView) findViewById(R.id.gridview_base);

        gridView.setAdapter(new ImageAdapter(this));

        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                switch (position){
                case 0: startActivity(new Intent(getApplicationContext(), CameraActivity.class)); break;
                case 1: startActivity(new Intent(getApplicationContext(), HistoryActivity.class)); break;
                case 2: startActivity(new Intent(getApplicationContext(), Options.class)); break;
                }
            }
        });
        
        loadTesseractData();

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_me, menu);
        return true;
    }*/
    
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }

        // references to our images
        private Integer[] mThumbIds = {
                R.drawable.seniordesignnewscandarkernoglare_rounded, R.drawable.seniordesignhistorydarkernoglare_rounded,
                R.drawable.seniordesignsettingsdarkernoglare_rounded
        };
    }
    
    public boolean loadTesseractData() {
        
        File sdCard = getExternalFilesDir(Environment.MEDIA_MOUNTED);
        File tessFolder = new File(sdCard.getAbsolutePath() + File.separator + "tessdata");
        
        //File base = getDir("augmented-reality", MODE_WORLD_READABLE);
        //File tessFolder = new File(base.getAbsolutePath() + "/tessdata/");
        
        if (!tessFolder.exists()) {
            
            if (!tessFolder.mkdirs()) {
                
                Log.e(LOGTAG, "Cannot create folder for tesseract training data");
                return false;
            }
        }
        
        File tessData = new File(tessFolder.getAbsolutePath() + File.separator + "eng.traineddata");
        if (!tessData.exists() || tessData.length() <= 0) {
            
            try {
                
                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open("tessdata" + File.separator + "eng_traineddata.mp3");
                OutputStream out = new FileOutputStream(tessData.getAbsoluteFile());
                
                byte buf[] = new byte[512];
                int bytesRead = in.read(buf);
                while (bytesRead > 0) {
                    
                    out.write(buf, 0, bytesRead);
                    bytesRead = in.read(buf);
                }
                
                in.close();
                out.close();
                
            } catch (Exception e) {
                
                Log.e(LOGTAG, "Unable to store tesseract training data; error below");
                Log.e(LOGTAG, e.getLocalizedMessage());
                return false;
            }
        }
        
        Log.d(LOGTAG, "Tesseract training data loaded successfully");
        return true;
    }
}
