package com.tasky.android.dialogs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import org.joda.time.DateTime;

/**
 * Wrapper for the DatePickerDialog that uses Joda DateTimes.
 */
public class JodaDatePickerDialog extends DatePickerDialog {
    public JodaDatePickerDialog(Context context, final OnDateSetListener listener, DateTime date) {
        // Shift the month accordingly (zero-based by the DatePicker, one-based by Joda).
        super(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                listener.onDateSet(view, new DateTime(year, month + 1, dayOfMonth, 0, 0));
            }
        }, date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
    }

    /**
     * Listener to handle selections of a Date with the JodaDatePickerDialog.
     */
    public interface OnDateSetListener {
        void onDateSet(DatePicker view, DateTime date);
    }
}