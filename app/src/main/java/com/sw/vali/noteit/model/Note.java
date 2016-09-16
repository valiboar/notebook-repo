package com.sw.vali.noteit.model;

import com.sw.vali.noteit.R;
import com.sw.vali.noteit.model.enums.NoteCategory;

/**
 * The note model - includes an id, a title, a message, a category and the creation date (in millis).
 *
 */
public class Note {
    private long id;
    private String title;
    private String message;
    private NoteCategory noteCategory;
    private long creationDateMillis;

    // CONSTRUCTORS
    public Note(String title, String message, NoteCategory noteCategory) {
        this.title = title;
        this.message = message;
        this.noteCategory = noteCategory;
        this.id = 0;
        this.creationDateMillis = 0;
    }

    public Note(long id, String title, String message, NoteCategory noteCategory, long creationDateMillis) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.noteCategory = noteCategory;
        this.creationDateMillis = creationDateMillis;
    }


    // GETTERS
    public long getId() {
        return id;
    }

    public long getCreationDateMillis() {
        return creationDateMillis;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public NoteCategory getNoteCategory() {
        return noteCategory;
    }


    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", iconId=" + noteCategory.name() +
                '}';
    }


    public int getAssociatedDrawable() {
        return categoryToDrawable(noteCategory);
    }

    public static int categoryToDrawable(NoteCategory noteCategory) {

        switch (noteCategory) {
            case PERSONAL:
                return R.drawable.p;
            case TECHNICAL:
                return R.drawable.t;
            case FINANCE:
                return R.drawable.f;
            case QUOTE:
                return R.drawable.q;
        }

        return R.drawable.p;
    }
}
