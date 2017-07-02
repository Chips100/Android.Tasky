package com.tasky.android.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.tasky.android.R;
import com.tasky.android.utilities.ParameterCheck;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base implementation of dialogs that support canceling or completing an operation
 * with addition user input in the dialog.
 * @param <T> Type of the result that contains the user input from the dialog.
 */
public abstract class ActionDialogBase<T> extends Dialog implements View.OnClickListener {
    private final Context context;
    private final OnActionDialogCompleteListener<T> completeListener;
    private final Map<Integer, DateTime> dateValues = new HashMap();

    /**
     * Creates an ActionDialog.
     * @param context Context in which the dialog is created.
     * @param completeListener Listener that handles completion of the operation represented by the dialog.
     */
    protected ActionDialogBase(Context context, OnActionDialogCompleteListener<T> completeListener) {
        super(context);
        ParameterCheck.notNull(completeListener, "completeListener");

        this.context = context;
        this.completeListener = completeListener;
    }


    /**
     * Gets the Id of the view that should be used for the content of the dialog.
     * @return The Id of the content view for the dialog.
     */
    protected abstract int getContentViewId();

    /**
     * Gets a result that represents the user input in the dialog.
     * @return A result with the user input in the dialog.
     */
    protected abstract T getDialogResult();

    /**
     * Can be overridden to indicate which controls in the dialog represent date inputs.
     * @return List of Ids of the controls that represent date inputs.
     */
    protected List<Integer> getDateInputIds() { return new ArrayList(); }

    /**
     * Can be overridden to suggest a Date value when the user first opens a date picker.
     * If not overridden, the current Date will be suggested.
     * @param dateInputId Id of the control for which to suggest a value.
     * @return The value that should be suggested when the user opens the date picker for the first time.
     */
    protected DateTime suggestDateValue(int dateInputId) { return DateTime.now(); }

    /**
     * Can be overridden to define a custom text for the button that completes the dialog.
     * @return A String with the custom text for the button; or null to keep the default.
     */
    protected String getCompleteButtonText() { return null; }

    /**
     * Can be overridden to automatically show the keyboard when the dialog is opened
     * to allow immediate input by the user without any extra clicks.
     * @return True, if the keyboard should be shown when the dialog is opened; otherwise false.
     */
    protected boolean showKeyboardOnOpen() { return false; }

    /**
     * Gets the current Date value selected for the specified control.
     * @param dateInputId Id of the control that represents a date input.
     * @return The current value selected for the specified control; or null if no value is selected.
     */
    protected DateTime getDateValue(int dateInputId) {
        return dateValues.containsKey(dateInputId) ? dateValues.get(dateInputId) : null;
    }

    /**
     * Sets the specified Date value for the specified control.
     * @param dateInputId Id of the control that represents a date input.
     * @param value Date value that should be set for the control; or null if no value should be set.
     */
    protected void setDateValue(int dateInputId, DateTime value) {
        // Store new value in local map.
        dateValues.put(dateInputId, value);

        // Display a String representation of the selected value in the input.
        String displayText = value == null ? "" : value.toString(context.getString(R.string.format_shortdate));
        ((EditText)findViewById(dateInputId)).setText(displayText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        // Button handlers handled by the onClick method.
        Button confirmButton = (Button)findViewById(R.id.action_dialog_button_complete);
        Button cancelButton = (Button)findViewById(R.id.action_dialog_button_cancel);
        confirmButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        // If available, set custom text for complete button.
        if (getCompleteButtonText() != null) {
            confirmButton.setText(getCompleteButtonText());
        }

        // Configure date inputs to open a DatePicker.
        for (Integer dateInputId : getDateInputIds()) {
            configureDateInput(dateInputId);
        }

        // Open the keyboard to allow immediate user input.
        if (showKeyboardOnOpen()) {
            Window window = getWindow();
            if (window != null) window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    /**
     * Configures a date input to automatically open a DatePicker when it is focused.
     * @param dateInputId Id of the control to configure as a date input.
     */
    private void configureDateInput(final int dateInputId) {
        final EditText dateEditText = (EditText)findViewById(dateInputId);

        // Show a DatePicker when the Date input is focused.
        // Unfocus it immediately to allow focusing again after closing the DatePicker.
        dateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Clear focus to allow refocusing after closing the DatePicker.
                dateEditText.clearFocus();
                if (hasFocus) showDatePicker(dateInputId);
            }
        });
    }

    /**
     * Handles click events on the dialog buttons.
     * @param v View that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.action_dialog_button_complete:
                // Completion has been requested.
                if (completeListener.handleComplete(getDialogResult())) {
                    hide();
                }
                break;
            case R.id.action_dialog_button_cancel:
                // Cancellation has been requested.
                hide();
                break;
        }
    }

    /**
     * Displays a DatePicker to the user that allows selecting a Date value
     * for the specified Date input.
     * @param dateInputId Id of the Date input for which a value should be selected.
     */
    public void showDatePicker(final int dateInputId) {
        // If a value has previously been selected, use it as the preselected value.
        // Otherwise, call implementation of suggestDateValue to suggest an initial value.
        DateTime current = dateValues.containsKey(dateInputId)
            ? dateValues.get(dateInputId)
            : suggestDateValue(dateInputId);

        // Show DatePicker.
        new JodaDatePickerDialog(context, new JodaDatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, DateTime date) {
                setDateValue(dateInputId, date);
            }
        }, current).show();
    }

    /**
     * Handles leaving the ActionDialog with intent to complete the operation.
     */
    public interface OnActionDialogCompleteListener<T> {
        /**
         * Executes the actual operation associated with the dialog.
         * @param result Result of the user input in the dialog.
         * @return True, if the operation could be executed and the dialog can be closed; otherwise false.
         */
        boolean handleComplete(T result);
    }
}