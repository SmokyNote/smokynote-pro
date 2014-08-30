package com.smokynote;

import android.content.Context;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.smokynote.note.Note;

import org.apache.commons.lang3.StringUtils;

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

    private final Handler handler = new Handler();
    private final Runnable updateTimerTask = new UpdateTimerTask();

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

    public void start() {
        handler.post(updateTimerTask);
    }

    public void stop() {
        handler.removeCallbacks(updateTimerTask);
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
        bindSchedule((TextView) view.findViewById(R.id.note_schedule), note);
        bindDescription((TextView) view.findViewById(R.id.note_description), note);
    }

    private void bindToggleButton(ToggleButton button, Note note) {
        if (note.getSchedule().isBeforeNow()) {
            button.setChecked(false);
            button.setEnabled(false);
        } else {
            button.setEnabled(true);
            button.setChecked(note.isEnabled());
        }
    }

    private void bindSchedule(TextView textView, Note note) {
        CharSequence timeString = DateUtils.getRelativeTimeSpanString(
                note.getSchedule().getMillis(),
                System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE);
        textView.setText(timeString);
    }

    private void bindDescription(TextView textView, Note note) {
        if (StringUtils.isEmpty(note.getDescription())) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(note.getDescription());
            textView.setVisibility(View.VISIBLE);
        }
    }

    private class UpdateTimerTask implements Runnable {

        @Override
        public void run() {
            // Invalidating data set causes whole view to redraw and aborts long taps.
            // Have no idea how to "fix" it.
            notifyDataSetChanged();
            handler.postDelayed(this, DateUtils.SECOND_IN_MILLIS);
        }
    }
}
