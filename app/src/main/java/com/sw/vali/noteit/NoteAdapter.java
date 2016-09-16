package com.sw.vali.noteit;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sw.vali.noteit.model.Note;

import java.util.ArrayList;

/**
 * Created by Vali on 13-Sep-16.
 */
public class NoteAdapter extends ArrayAdapter<Note> {

    // TODO: 14-Sep-16 Why should the ViewHolder class be static?
    // 1) It shouldn't; if it's static you can use it in other classes (adapters) - NOT RECOMMENDED (better
    //      to create a new separated class and use it from multiple places - one class for one purpose)
    // 2) If it's static, it will take less memory and also it avoids memory leaks if its instance
    //      is taken out of the class.
    // *) In the case of view holders, they will be used only inside the adapter; their instances should
    //      not go to the fragment or activity or elsewhere. This means having it static or non-static,
    //      is the same.
    public static class ViewHolder {
        TextView noteTitle;
        TextView noteText;
        ImageView noteIcon;
    }

    public NoteAdapter(Context context, ArrayList<Note> notes) {
        super(context, 0, notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Create a new view holder
        ViewHolder viewHolder;

        // Check if an existing view is being reused, otherwise inflate a new view from custom row layout
        if (convertView == null) {

            // if we don't have a view that is being used, create one and make sure you create a view holder
            // along with it to save our view references to
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);

            // set our views to our view holder so that we no longer have to go back and use findViewById every
            // time we have a new row
            viewHolder.noteTitle = (TextView) convertView.findViewById(R.id.list_item_note_title);
            viewHolder.noteText = (TextView) convertView.findViewById(R.id.list_item_note_body);
            viewHolder.noteIcon = (ImageView) convertView.findViewById(R.id.list_item_note_icon);

            // use setTag to remember our view holder which is holding our references to our widgets
            convertView.setTag(viewHolder);
        } else {

            // we already have a view so just go to our view holder and grab the widgets from it
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        // Grab references of views so we can populate them with specific note row data
//        TextView noteTitle = (TextView) convertView.findViewById(R.id.list_item_note_title);
//        TextView noteText = (TextView) convertView.findViewById(R.id.list_item_note_body);
//        ImageView noteIcon = (ImageView) convertView.findViewById(R.id.list_item_note_icon);

        Note note = getItem(position); // Get the data item for this position

        if (note != null) {

            // Fill each new referenced view with data associated with note it's referencing
            viewHolder.noteTitle.setText(note.getTitle());
            viewHolder.noteText.setText(note.getMessage());
            viewHolder.noteIcon.setImageResource(note.getAssociatedDrawable());

            switch (note.getNoteCategory()) {
                case PERSONAL:
                    convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPersonalNoteBackground));

                    viewHolder.noteTitle.setTextColor(Color.parseColor("#ff5050"));
                    viewHolder.noteText.setTextColor(Color.parseColor("#ff5050"));

                    break;
                case TECHNICAL:
                    convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTechnicalNoteBackground));

                    viewHolder.noteTitle.setTextColor(Color.parseColor("#3399ff"));
                    viewHolder.noteText.setTextColor(Color.parseColor("#3399ff"));

                    break;
                case QUOTE:
                    convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorQuoteNoteBackground));

                    viewHolder.noteTitle.setTextColor(Color.parseColor("#e6b800"));
                    viewHolder.noteText.setTextColor(Color.parseColor("#e6b800"));

                    break;
                case FINANCE:
                    convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorFinanceNoteBackground));

                    viewHolder.noteTitle.setTextColor(Color.parseColor("#00b300"));
                    viewHolder.noteText.setTextColor(Color.parseColor("#00b300"));

                    break;
            }
        }

        // now that we modified the view to display appropriate data, return it so it will be displayed
        return convertView;
    }
}
