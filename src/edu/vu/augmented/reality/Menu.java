package edu.vu.augmented.reality;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Menu extends Activity implements OnClickListener  {
	Button startLink;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void init(){
		startLink = (Button) findViewById(R.id.button1);
		startLink.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.button1:
			try {
				Intent ourIntent;
				ourIntent = new Intent(Menu.this, Class.forName("edu.vu.augmented.reality.linkedin.Init"));
				startActivity(ourIntent);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		}
		
	}
	

}
