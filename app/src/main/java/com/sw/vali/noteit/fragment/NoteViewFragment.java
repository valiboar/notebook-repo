package com.sw.vali.noteit.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sw.vali.noteit.model.Note;
import com.sw.vali.noteit.model.enums.NoteCategory;
import com.sw.vali.noteit.R;
import com.sw.vali.noteit.activity.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteViewFragment extends Fragment {


    public NoteViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentLayout = inflater.inflate(R.layout.fragment_note_view, container, false);

        TextView title = (TextView) fragmentLayout.findViewById(R.id.note_view_title);
        TextView message = (TextView) fragmentLayout.findViewById(R.id.note_view_message);
        ImageView icon = (ImageView) fragmentLayout.findViewById(R.id.note_view_icon);

        Intent intent = getActivity().getIntent();

        title.setText(intent.getExtras().getString(MainActivity.EXTRA_NOTE_TITLE));
        message.setText(intent.getExtras().getString(MainActivity.EXTRA_NOTE_MESSAGE));

        NoteCategory noteCategory = (NoteCategory) intent.getSerializableExtra(MainActivity.EXTRA_NOTE_CATEGORY);
        // if NoteCategory would have been defined in Note class => Note.NoteCategory noteCategory = (Note.NoteCategory)...
        icon.setImageResource(Note.categoryToDrawable(noteCategory));

        return fragmentLayout;
    }

}
