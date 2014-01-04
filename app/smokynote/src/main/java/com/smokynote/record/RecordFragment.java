package com.smokynote.record;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import com.smokynote.R;
import com.smokynote.inject.Injector;
import com.smokynote.widget.ImageIndicatorView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class RecordFragment extends SherlockFragment {

    private static final Logger LOG = LoggerFactory.getLogger("SMOKYNOTE.RECORD");

    /**
     * Indicator refresh interval in ms.
     */
    private static final int REFRESH_INTERVAL = 100;

    @Inject
    /* private */ ScheduledExecutorService scheduledExecutorService;

    private MediaRecorder recorder;
    private Future<?> indicatorUpdateFuture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Important: retain fragment while record in progress.
        setRetainInstance(true);

        ((Injector) getActivity().getApplication()).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.record_fragment, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        final RecordListener listener = (RecordListener) activity;

        try {
            startRecording();
        } catch (StorageUnavailableException e) {
            listener.onStorageUnavailable();
        } catch (RecordException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onPause() {
        if (indicatorUpdateFuture != null) {
            indicatorUpdateFuture.cancel(true);
        }

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        startIndicatorUpdater();
    }

    private void startIndicatorUpdater() {
        final ImageIndicatorView indicatorView = (ImageIndicatorView) getView().findViewById(R.id.volume_indicator);

        final LevelIndicatorUpdater levelIndicatorUpdater = new LevelIndicatorUpdater(indicatorView, recorder);
        indicatorUpdateFuture = scheduledExecutorService.scheduleAtFixedRate(levelIndicatorUpdater, 0, REFRESH_INTERVAL, TimeUnit.MILLISECONDS);
    }

    private void startRecording() throws RecordException {
        createRecorder();

        final File recordFile = createRecordFile();
        final String recordFileName = recordFile.getAbsolutePath();

        LOG.info("Writing to file {}", recordFileName);
        recorder.setOutputFile(recordFileName);

        try {
            recorder.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e); // XXX mb show user-friendly message?
        }

        recorder.start();   // Recording is now started
    }

    private File createRecordFile() throws StorageUnavailableException {
        final File externalFilesDir = getExternalFilesDir();
        return new File(externalFilesDir, ".recording.3gp");
    }

    private File getExternalFilesDir() throws StorageUnavailableException {
        final File externalFilesDir = ContextCompat.getExternalFilesDirs(getActivity(), "Notifications")[0];
        if (externalFilesDir == null) {
            throw new StorageUnavailableException();
        }

        if (!externalFilesDir.exists()) {
            if (!externalFilesDir.mkdirs()) {
                // TODO: return error
                throw new RuntimeException();
            }
        }

        return externalFilesDir;
    }

    private void createRecorder() {
        recorder = new MediaRecorder();

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    }

    @Override
    public void onDestroy() {
        stopRecording();

        super.onDestroy();
    }

    public void stopRecording() {
        LOG.info("Stop recording");

        indicatorUpdateFuture.cancel(true);
        recorder.stop();
    }
}
