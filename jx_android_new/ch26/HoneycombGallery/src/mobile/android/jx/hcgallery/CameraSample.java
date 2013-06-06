package mobile.android.jx.hcgallery;


import android.app.Activity;
import android.os.Bundle;

public class CameraSample extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int themeId = this.getIntent().getExtras().getInt("theme");
        this.setTheme(themeId);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_sample);
    }

}
