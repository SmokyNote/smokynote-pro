package com.smokynote.playback;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.actionbarsherlock.app.SherlockFragment;
import com.cocosw.undobar.UndoBarController;
import com.smokynote.R;
import com.smokynote.note.Note;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nullable;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class PlaybackFragment extends SherlockFragment {

    private static final Logger LOG = LoggerFactory.getLogger("SMOKYNOTE.PLAY");

    public static final String EXTRA_NOTE = "targetNote";

    @Nullable
    private MediaPlayer mediaPlayer;
    @Nullable
    private CompoundButton playStopButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Important: retain fragment while playback in progress.
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.playback_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playStopButton = (CompoundButton) view.findViewById(R.id.play_stop_button);
        assert playStopButton != null;
        playStopButton.setOnClickListener(new CompoundButton.OnClickListener() {
            @Override
            public void onClick(View buttonView) {
                handleActionButtonClick((CompoundButton) buttonView);
            }
        });

        if (mediaPlayer != null) {
            setActionButtonState(mediaPlayer.isPlaying() ? State.PLAYING : State.STOPPED);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        startPlayback();
    }

    @Override
    public void onDestroy() {
        releaseMediaPlayer();

        super.onDestroy();
    }

    private void startPlayback() {
        LOG.info("Start playback");

        initMediaPlayer();
        doStartPlayback();
    }

    public void stopPlayback() {
        LOG.info("Stop playback");

        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    private void initMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.setLooping(false);
        } else {
            mediaPlayer.reset();
        }
    }

    private void doStartPlayback() {
        assert mediaPlayer != null;

        final String filename = getFilename();
        if (!new File(filename).exists()) {
            LOG.error("Requested file '{}' doesn't exist", filename);
            // TODO: check if external SD is mounted and not available. It is not the same that file deletion.
            setActionButtonState(State.STOPPED);
            showFileError();
            return;
        }

        try {
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    LOG.info("Playback finished");
                    handlePlaybackFinished();
                }
            });
            mediaPlayer.setDataSource(filename);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mediaPlayer.prepare();
            mediaPlayer.start();

            LOG.debug("Playback started");

            setActionButtonState(State.PLAYING);
        } catch (IOException e) {
            LOG.error("Unable to play stored media.", e);
            // TODO: show friendly message with retry option
        }
    }

    private String getFilename() {
        final Note note = (Note) getArguments().getSerializable(EXTRA_NOTE);
        return note.getFilename();
    }

    private void handlePlaybackFinished() {
        // Reset play/stop button state
        setActionButtonState(State.STOPPED);
    }

    private void handleActionButtonClick(CompoundButton button) {
        if (button.isChecked()) {
            startPlayback();
        } else {
            stopPlayback();
        }
    }

    private void releaseMediaPlayer() {
        LOG.debug("Release MediaPlayer");

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void setActionButtonState(State state) {
        if (playStopButton != null) {
            playStopButton.setChecked(state == State.PLAYING);
        }
    }

    private void showFileError() {
        new UndoBarController.UndoBar(getActivity())
                .message(R.string.playback_error_file)
                .style(UndoBarController.MESSAGESTYLE)
                .show();
    }

    private static enum State {
        PLAYING, STOPPED
    }
}
