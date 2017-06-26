package com.smokynote.alarm.timed;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.CompoundButton;

import com.smokynote.R;
import com.smokynote.playback.PlaybackFragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.Nullable;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class TimedAlarmFragment extends PlaybackFragment {

    public static final String EXTRA_NOTE = PlaybackFragment.EXTRA_NOTE;

    private static final Logger LOG = LoggerFactory.getLogger("SMOKYNOTE.ALARM");

    @Nullable
    private Vibrator vibrator;
    @Nullable
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        stopAlarm();
        super.onDestroy();
    }

    public void startAlarm(Context context) {
        startVibration(context);
        playAlarmSound(context);
    }

    public void stopAlarm() {
        stopVibration();
        stopAlarmSound();
    }

    private void startVibration(Context context) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{500, 300}, 0);
    }

    private void stopVibration() {
        if (vibrator != null) {
            vibrator.cancel();
            vibrator = null;
        }
    }

    private void playAlarmSound(Context context) {
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
            doPlayAlarmSound(context);
        }
    }

    private void doPlayAlarmSound(Context context) {
        try {
            doTryPlayAlarmSound(context);
        } catch (IOException e) {
            LOG.error("Couldn't prepare media player to play Alarm sound", e);
        }
    }

    private void doTryPlayAlarmSound(Context context) throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(true);
        mediaPlayer.setDataSource(context, getAlarmUri(context));
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        mediaPlayer.setLooping(true);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    private void stopAlarmSound() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private Uri getAlarmUri(Context context) {
        final Uri systemAlarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        if (systemAlarmUri == null) {
            return getFallbackAlertUri(context);
        }

        try {
            context.getContentResolver().openInputStream(systemAlarmUri);
        } catch (FileNotFoundException e) {
            return getFallbackAlertUri(context);
        }

        return systemAlarmUri;
    }

    private Uri getFallbackAlertUri(Context context) {
        return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.fallbackalert);
    }

    @Override
    protected void handleActionButtonClick(CompoundButton button) {
        stopAlarm();

        super.handleActionButtonClick(button);
    }
}
