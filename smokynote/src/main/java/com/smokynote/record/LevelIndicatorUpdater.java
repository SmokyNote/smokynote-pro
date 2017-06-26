package com.smokynote.record;

import android.media.MediaRecorder;
import com.smokynote.widget.ImageIndicatorView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class dedicated to update {@link com.smokynote.widget.ImageIndicatorView}
 * level during note recording.
 *
* @author Maksim Zakharov
* @since 1.0
*/
class LevelIndicatorUpdater implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger("SMOKYNOTE.RECORD");

    /**
     * Max volume level ever registered during running of this task.
     */
    private int maxAmplitudeOnRecord = 2500;
    private int lastLevel;
    private int maxAmplitudeCached;

    private final ImageIndicatorView imageIndicatorView;
    private final MediaRecorder mediaRecorder;

    public LevelIndicatorUpdater(ImageIndicatorView indicatorView, MediaRecorder recorder) {
        imageIndicatorView = indicatorView;
        mediaRecorder = recorder;
    }

    @Override
    public void run() {
        updateIndicator();
    }

    private void updateIndicator() {
        final int maxAmp = getMaxAmplitude();
        maxAmplitudeOnRecord = Math.max(maxAmplitudeOnRecord, maxAmp);

        final int level = calculateLevel(maxAmp, maxAmplitudeOnRecord);
        LOG.trace("Got level {}", level);

        updateIndicator(level);
    }

    private void updateIndicator(int level) {
        if (level != lastLevel) {
            doUpdateIndicator(level);
        }
    }

    private void doUpdateIndicator(int level) {
        imageIndicatorView.setLevel(level);
        lastLevel = level;
    }

    /**
     * {@link android.media.MediaRecorder#getMaxAmplitude()} sometimes returns 0 instead of real value.
     * In this case we use cached value.
     *
     * @return maximum absolute amplitude since the last call to this method
     */
    private int getMaxAmplitude() {
        int maxAmp = mediaRecorder.getMaxAmplitude();

        if (maxAmp == 0) {
            maxAmp = maxAmplitudeCached;
        } else {
            maxAmplitudeCached = maxAmp;
        }
        return maxAmp;
    }

    /**
     * Calculate image indicator level to display.
     *
     * @param maxAmplitude maximum absolute amplitude that was sampled since the last
     * level calculation
     * @param maxAmplitudeOnRecord maximum absolute amplitude ever sampled
     * @return level to display
     */
    private int calculateLevel(int maxAmplitude, int maxAmplitudeOnRecord) {
        return (int) ((double) maxAmplitude / maxAmplitudeOnRecord * ImageIndicatorView.MAX_LEVEL);
    }
}
