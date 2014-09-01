package com.smokynote.playback;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.smokynote.R;
import com.smokynote.inject.Injector;
import com.smokynote.note.NotesRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class PlaybackFragment extends SherlockFragment {

    private static final Logger LOG = LoggerFactory.getLogger("SMOKYNOTE.PLAY");

    @Inject
    /* private */ NotesRepository notesRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Important: retain fragment while playback in progress.
        setRetainInstance(true);

        ((Injector) getActivity().getApplication()).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.playback_fragment, container, false);
    }

    public void startPlayback() {
        LOG.info("Start playback");
    }

    public void stopPlayback() {
        LOG.info("Stop playback");
    }
}
