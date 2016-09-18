package com.sw.vali.noteit.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sw.vali.noteit.NoteItDbAdapter;
import com.sw.vali.noteit.model.Note;
import com.sw.vali.noteit.model.enums.NoteCategory;
import com.sw.vali.noteit.R;
import com.sw.vali.noteit.activity.MainActivity;
import com.sw.vali.noteit.activity.NoteDetailsActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteEditFragment extends Fragment {

    public static final String TAG = NoteEditFragment.class.getName();

    private static final String MODIFIED_CATEGORY = "Modified Category";

    private EditText title;
    private EditText message;
    private ImageButton noteCategoryButton;

    private NoteCategory savedButtonCategory;

    private AlertDialog categoryDialogObject;
    private AlertDialog confirmDialogObject;

    private boolean isNewNote = false;

    private long noteId = 0;

    public NoteEditFragment() {
        // Required empty public constructor
    }

    // executes every time we change the orientation
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // grab the bundle that sends along whether or not our NoteEditFragment is creating a new note
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            isNewNote = bundle.getBoolean(NoteDetailsActivity.EXTRA_NEW_NOTE, false);
        }

        if (savedInstanceState != null) {
            savedButtonCategory = (NoteCategory) savedInstanceState.getSerializable(MODIFIED_CATEGORY);
        }

        // inflate our edit fragment layout
        View fragmentLayout = inflater.inflate(R.layout.fragment_note_edit, container, false);

        // grab widgets references from layout
        title = (EditText) fragmentLayout.findViewById(R.id.note_edit_title);
        message = (EditText) fragmentLayout.findViewById(R.id.note_edit_message);
        noteCategoryButton = (ImageButton) fragmentLayout.findViewById(R.id.note_edit_image_button);
        Button saveButton = (Button) fragmentLayout.findViewById(R.id.note_edit_save_button);

        // populate widgets with note data
        Intent intent = getActivity().getIntent();

        title.setText(intent.getExtras().getString(MainActivity.EXTRA_NOTE_TITLE, ""));
        message.setText(intent.getExtras().getString(MainActivity.EXTRA_NOTE_MESSAGE, ""));
        noteId = intent.getExtras().getLong(MainActivity.EXTRA_NOTE_ID, 0);

        // if we grabbed a category from our bundle, then we know we changed orientation and saved information,
        // so set our image button background to that category
        if (savedButtonCategory != null) {
            noteCategoryButton.setImageResource(Note.categoryToDrawable(savedButtonCategory));
        } else if (!isNewNote){ // otherwise we came from our list fragment so just do everything normally
            NoteCategory noteCategory = (NoteCategory) intent.getSerializableExtra(MainActivity.EXTRA_NOTE_CATEGORY);
            savedButtonCategory = noteCategory;
            noteCategoryButton.setImageResource(Note.categoryToDrawable(noteCategory));
        }

        buildCategoryDialog();
        buildConfirmDialog();

        noteCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryDialogObject.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                if(isNoteEmpty()) {
//                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                    Ringtone r = RingtoneManager.getRingtone(getActivity().getBaseContext(), notification);
//                    r.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build());
//                    r.play();


                    // Check if no view has focus:
//                    View v = getActivity().getCurrentFocus();
//                    if (v != null) {
//                        InputMethodManager imm = (InputMethodManager)getActivity().getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                    }


                    new Handler(Looper.getMainLooper()) { // not necessary the Handler here (we already have a UI thread

                        @Override
                        public void handleMessage(Message inputMessage) {

                            Toast.makeText(getActivity().getBaseContext(), "No content to save; create a valid note!", Toast.LENGTH_SHORT).show();

                            Vibrator vibrator = (Vibrator) getActivity().getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);

                            vibrator.vibrate(300);

                            super.handleMessage(inputMessage);
                        }
                    }.handleMessage(null);

                    return;
                }
                
                confirmDialogObject.show();
            }
        });

        return fragmentLayout;
    }

    private boolean isNoteEmpty() {
        return title.getText().toString().isEmpty() && message.getText().toString().isEmpty();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putSerializable(MODIFIED_CATEGORY, savedButtonCategory);
    }

    private void buildCategoryDialog() {
        final String[] categories = new String[]{"Personal", "Technical", "Quote", "Finance"};

        AlertDialog.Builder categoryBuilder = new AlertDialog.Builder(getActivity());

        categoryBuilder.setTitle("Choose note type");

        categoryBuilder.setSingleChoiceItems(categories, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {

                // dismisses our dialog window
                categoryDialogObject.cancel();

                switch (item) {
                    case 0:
                        savedButtonCategory = NoteCategory.PERSONAL;
                        noteCategoryButton.setImageResource(R.drawable.p);
                        break;
                    case 1:
                        savedButtonCategory = NoteCategory.TECHNICAL;
                        noteCategoryButton.setImageResource(R.drawable.t);
                        break;
                    case 2:
                        savedButtonCategory = NoteCategory.QUOTE;
                        noteCategoryButton.setImageResource(R.drawable.q);
                        break;
                    case 3:
                        savedButtonCategory = NoteCategory.FINANCE;
                        noteCategoryButton.setImageResource(R.drawable.f);
                        break;
                }
            }
        });

        categoryDialogObject = categoryBuilder.create();
    }

    private void buildConfirmDialog() {
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getActivity());

        confirmBuilder.setTitle("Are you sure?");
        confirmBuilder.setMessage("Are you sure you want to save the note?");

        confirmBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "Note title: " + title.getText() + "; Note message: " + message.getText() +
                    "; Note category: " + savedButtonCategory);

                // TODO: 17-Sep-16 Code for these situations
//                if(titleIsEmpty()) {
//                    
//                }
//                
//                if(messageIsEmpty()) {
//                    
//                }
                
                NoteItDbAdapter dbAdapter = new NoteItDbAdapter(getActivity().getBaseContext());

                dbAdapter.open();

                // if it's a new note, create it in our database
                if(isNewNote) {

                    dbAdapter.createNote(title.getText().toString(), message.getText().toString(),
                            (savedButtonCategory == null) ? NoteCategory.PERSONAL : savedButtonCategory); // if we never modified the category => put the Personal type

                } else { // otherwise it's an old note, so update it in our database

                    dbAdapter.updateNote(noteId, title.getText().toString(), message.getText().toString(), savedButtonCategory);
                }

                dbAdapter.close();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        confirmBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // do nothing here
            }
        });

        confirmDialogObject = confirmBuilder.create();
    }

}
