package com.sw.vali.noteit.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sw.vali.noteit.NoteItDbAdapter;
import com.sw.vali.noteit.model.enums.FragmentToLaunch;
import com.sw.vali.noteit.model.Note;
import com.sw.vali.noteit.NoteAdapter;
import com.sw.vali.noteit.R;
import com.sw.vali.noteit.activity.MainActivity;
import com.sw.vali.noteit.activity.NoteDetailsActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityListFragment extends ListFragment {

    public static final String TAG = MainActivityListFragment.class.getName();

    private ArrayList<Note> notes;
    private NoteAdapter noteAdapter;

    private TextView noOfNotesTextView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        String[] values = new String[] { "Android", "iOS", "WindowsMobile", "Blackberry",
//                "Web0S", "Ubuntu", "Windows 7", "Mac OS", "Linux", "OS/2" };
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
//                android.R.layout.simple_list_item_1, values);
//
//        setListAdapter(adapter);

        /*notes = new ArrayList<>();
        notes.add(new Note("This is a new note title", "This is the body of our note!", NoteCategory.PERSONAL));
        notes.add(new Note("FINANCE", "The stock price is higher", NoteCategory.FINANCE));
        notes.add(new Note("Nice quote", "This is written by Shakespeare", NoteCategory.QUOTE));
        notes.add(new Note("Technical things", "Body of the note", NoteCategory.TECHNICAL));*/

        NoteItDbAdapter dbAdapter = new NoteItDbAdapter(getActivity().getBaseContext());
        dbAdapter.open();
        notes = dbAdapter.getAllNotes();
        dbAdapter.close();

        noteAdapter = new NoteAdapter(getActivity(), notes);

        setListAdapter(noteAdapter);

        getListView().setDivider(ContextCompat.getDrawable(getActivity(), R.color.colorMainBackground));
        getListView().setDividerHeight(20);

        View footerView =  ((LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_footer_notes_listview, null, false);
        noOfNotesTextView = (TextView) footerView.findViewById(R.id.no_of_notes_textview);

        refreshNoOfNotes();

        getListView().addFooterView(footerView, null, false);

        registerForContextMenu(getListView());  // sets a "long-click" listener for the ListView;
                                                // triggers the onCreateContextMenu (below), which eventually
                                                // inflate the desired context menu
    }

    private void refreshNoOfNotes() {
        noOfNotesTextView.setText(noteAdapter.getCount() + " notes");

        if(noteAdapter.getCount() == 1) {
            noOfNotesTextView.setText("1 note");
        }

        if(noteAdapter.getCount() == 0) {
            noOfNotesTextView.setText("You don't have any notes yet!");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        launchNoteDetailsActivity(FragmentToLaunch.VIEW, position);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.note_long_press_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // give me the position of whatever note I long pressed on
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int rowPosition = info.position;

        final Note note = (Note) getListAdapter().getItem(rowPosition);

        // returns to us the id of whatever item was selected
        switch (item.getItemId()) {
            // if we press "Edit"
            case R.id.context_menu_edit_item:

                Log.d(TAG, "Pressed EDIT");
                launchNoteDetailsActivity(FragmentToLaunch.EDIT, rowPosition);
                return true;
            case R.id.context_menu_delete_item:
                new AlertDialog.Builder(getActivity())
                        .setTitle("Warning")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NoteItDbAdapter dbAdapter = new NoteItDbAdapter(getActivity().getBaseContext());
                                dbAdapter.open();
                                dbAdapter.deleteNote(note.getId());

                                // refresh notes view
                                notes.clear();
                                notes.addAll(dbAdapter.getAllNotes());
                                noteAdapter.notifyDataSetChanged();

                                refreshNoOfNotes();

                                dbAdapter.close();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setIcon(android.R.drawable.ic_dialog_info)
                        .show();

                return true;
        }

        return super.onContextItemSelected(item);
    }

    private void launchNoteDetailsActivity(FragmentToLaunch fragmentToLaunch, int position) {

        // grab the note information associated with whatever note item we clicked on
        Note note = (Note) getListAdapter().getItem(position);

        // create a new intent that launches our NoteDetailsActivity
        Intent intent = new Intent(getActivity(), NoteDetailsActivity.class);

        // pass along the information of the note we clicked on to our NoteDetailsActivity
        intent.putExtra(MainActivity.EXTRA_NOTE_TITLE, note.getTitle());
        intent.putExtra(MainActivity.EXTRA_NOTE_MESSAGE, note.getMessage());
        intent.putExtra(MainActivity.EXTRA_NOTE_CATEGORY, note.getNoteCategory());
        intent.putExtra(MainActivity.EXTRA_NOTE_ID, note.getId());

        switch (fragmentToLaunch) {
            case VIEW:
                intent.putExtra(MainActivity.EXTRA_NOTE_FRAGMENT_TO_LOAD, FragmentToLaunch.VIEW);
                break;
            case EDIT:
                intent.putExtra(MainActivity.EXTRA_NOTE_FRAGMENT_TO_LOAD, FragmentToLaunch.EDIT);
                break;
        }

        startActivity(intent);
    }
}
