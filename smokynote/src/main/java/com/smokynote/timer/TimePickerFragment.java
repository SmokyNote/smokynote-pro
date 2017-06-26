package com.smokynote.timer;

import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import com.actionbarsherlock.app.SherlockFragment;
import com.smokynote.R;
import org.joda.time.DateTime;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class TimePickerFragment extends SherlockFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.date_time_pick, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final DateTime targetTime = getTargetTime();
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
        final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);

        datePicker.updateDate(targetTime.getYear(), targetTime.getMonthOfYear() - 1, targetTime.getDayOfMonth());
        if (Build.VERSION.SDK_INT >= 11) {
            try {
                datePicker.setMinDate(getMinDateInMilliseconds());
            } catch (IllegalArgumentException e) {
                // Workaround for really slow phones.
                try {
                    datePicker.setMinDate(getMinDateInMilliseconds() - 5000L);
                } catch (IllegalArgumentException again) {
                    // Okay..
                }
            }
        }
        timePicker.setIs24HourView(DateFormat.is24HourFormat(getActivity()));
        timePicker.setCurrentHour(targetTime.getHourOfDay());
        timePicker.setCurrentMinute(targetTime.getMinuteOfHour());
    }

    private DateTime getTargetTime() {
        return DateTime.now().plusMinutes(15);
    }

    private long getMinDateInMilliseconds() {
        // I've caught error few times here.
        // Android says that returned value does not precedes current time.
        return System.currentTimeMillis() - 1001L;
    }

    public DateTime getSelectedTime() {
        final DatePicker datePicker = (DatePicker) getView().findViewById(R.id.date_picker);
        final TimePicker timePicker = (TimePicker) getView().findViewById(R.id.time_picker);

        return new DateTime(
                datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth(),
                timePicker.getCurrentHour(), timePicker.getCurrentMinute());
    }
}
