package com.sw.vali.noteit.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sw.vali.noteit.model.enums.FragmentToLaunch;
import com.sw.vali.noteit.fragment.NoteEditFragment;
import com.sw.vali.noteit.fragment.NoteViewFragment;
import com.sw.vali.noteit.R;

public class NoteDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_NEW_NOTE = "New Note";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        // grab intent and fragment to launch from our MainActivityListFragment
        Intent intent = getIntent();

        createAndAddFragment((FragmentToLaunch)intent.getSerializableExtra(MainActivity.EXTRA_NOTE_FRAGMENT_TO_LOAD));
    }

    private void createAndAddFragment(FragmentToLaunch fragmentToLaunch) {

        // grabbing our FragmentManager and our FragmentTransaction so that we can add our edit or view fragment dynamically
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // choose the correct fragment to load
        switch (fragmentToLaunch) {
            case VIEW:
                NoteViewFragment noteViewFragment = new NoteViewFragment();

                setTitle(R.string.note_view_fragment_title); // if we comment this line, the title of the activity will be the default title set in Manifest

                fragmentTransaction.add(R.id.note_container, noteViewFragment, "NOTE_VIEW_FRAGMENT");
                break;

            case EDIT:
                NoteEditFragment noteEditFragment = new NoteEditFragment();

                setTitle(R.string.note_edit_fragment_title);

                fragmentTransaction.add(R.id.note_container, noteEditFragment, "NOTE_EDIT_FRAGMENT");
                break;

            case CREATE:
                NoteEditFragment noteCreateFragment = new NoteEditFragment();

                setTitle(R.string.note_create_fragment_title);

                Bundle bundle = new Bundle();
                bundle.putBoolean(EXTRA_NEW_NOTE, true);

                noteCreateFragment.setArguments(bundle);

                fragmentTransaction.add(R.id.note_container, noteCreateFragment, "NOTE_CREATE_FRAGMENT");
                break;
        }

        // commit our changes so that everything works
        fragmentTransaction.commit();
    }
}
