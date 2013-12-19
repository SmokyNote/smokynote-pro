package com.smokynote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ToggleButton;
import com.smokynote.orm.Note;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class NotesListAdapter extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private final List<Note> notes;

    public NotesListAdapter(Context context) {
        this(context, Collections.<Note>emptyList());
    }

    public NotesListAdapter(Context context, List<Note> notes) {
        this.notes = new ArrayList<Note>(notes);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setNotes(Collection<Note> newNotes) {
        notes.clear();
        notes.addAll(newNotes);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Note getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, R.layout.notes_list_row);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
        View v;
        if (convertView == null) {
            v = layoutInflater.inflate(resource, parent, false);
        } else {
            v = convertView;
        }

        final Note note = notes.get(position);
        bind(v, note);

        return v;
    }

    private void bind(View view, Note note) {
        bindToggleButton((ToggleButton) view.findViewById(R.id.toggle_alarm), note);
    }

    private void bindToggleButton(ToggleButton button, Note note) {
        button.setChecked(note.isEnabled());
    }
}
