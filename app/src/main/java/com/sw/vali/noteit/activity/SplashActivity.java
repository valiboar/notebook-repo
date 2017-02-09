package com.sw.vali.noteit.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sw.vali.noteit.R;

public class SplashActivity extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

        // TODO: 22-Sep-16 Leave just the next 3 lines. Explanation:
        // If you did have a layout file for your splash activity, that layout file would be visible
        // to the user only after your app has been fully initialized, which is too late. You want the
        // splash to be displayed only in that small amount of time before the app is initialized.

        // So don't worry if the splash screen doesn't appear. Maybe the app was being initialized very quickly.
        // Try with many many items in the notes list view.
        /*Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();*/
    }
}
