package com.sw.vali.noteit.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.sw.vali.noteit.model.enums.FragmentToLaunch;
import com.sw.vali.noteit.R;

public class MainActivity extends AppCompatActivity {

    public static String PACKAGE_NAME;

    public static final String EXTRA_NOTE_ID = "com.sw.vali.noteit.Identifier";
    public static final String EXTRA_NOTE_TITLE = "com.sw.vali.noteit.Title";
    public static final String EXTRA_NOTE_MESSAGE = "com.sw.vali.noteit.Message";
    public static final String EXTRA_NOTE_CATEGORY = "com.sw.vali.noteit.Category";
    public static final String EXTRA_NOTE_FRAGMENT_TO_LOAD = "com.sw.vali.noteit.Fragment_To_Load";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setMainLayoutBackgroundColor(Color.parseColor("#c1d7d7"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PACKAGE_NAME = getApplicationContext().getPackageName();

//       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        loadPreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, AppPreferencesActivity.class);

            startActivity(intent);

            return true;
        } else if (id == R.id.action_add_note) {

            Intent intent = new Intent(this, NoteDetailsActivity.class);

            intent.putExtra(MainActivity.EXTRA_NOTE_FRAGMENT_TO_LOAD, FragmentToLaunch.CREATE);

            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean isBackgroundDark = sharedPreferences.getBoolean("background_color", false);

        if(isBackgroundDark) {
            setMainLayoutBackgroundColor(Color.parseColor("#3c3f41"));
        }

        String notebookTitle = sharedPreferences.getString("title", "Notebook");

        setTitle(notebookTitle);
    }

    private void setMainLayoutBackgroundColor(int color) {
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_activity_layout);
        mainLayout.setBackgroundColor(color);
    }
}
