package mobile.android.jx.clip;

import android.app.Activity;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.widget.ImageView;

public class Clip extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);  
        ImageView imageview = (ImageView) findViewById(R.id.image);
        ClipDrawable drawable = (ClipDrawable) imageview.getBackground();
        drawable.setLevel(3000);
    }

}


