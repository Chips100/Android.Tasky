package com.tasky.android.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.tasky.android.R;
import com.tasky.android.utilities.ParameterCheck;

import org.joda.time.DateTime;

/**
 * Dialog with inputs required to create a new task.
 */
public class CreateTaskDialog extends Dialog implements View.OnClickListener {
    private final Context context;
    private final OnTaskCreateListener listener;
    private EditText titleEditText;
    private EditText dueDateEditText;
    private DateTime dueDate;

    /**
     * Creates a CreateTaskDialog.
     * @param context Context in which to create the dialog.
     * @param listener Listener that should be called when the creation is requested.
     */
    public CreateTaskDialog(Context context, OnTaskCreateListener listener) {
        super(context);
        ParameterCheck.NotNull(listener, "listener");

        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_create);

        findViews();
        setUiHandlers();
    }

    /**
     * Finds views in the content view and stores them in fields.
     */
    private void findViews() {
        titleEditText = (EditText)findViewById(R.id.createTaskTitle);
        dueDateEditText = (EditText)findViewById(R.id.createTaskDueDateEditText);
    }

    /**
     * Configures all handlers that should react to UI events.
     */
    private void setUiHandlers() {
        // Button handlers handled by the onClick method.
        Button confirmButton = (Button)findViewById(R.id.createTaskConfirmButton);
        Button cancelButton = (Button)findViewById(R.id.createTaskCancelButton);
        confirmButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        // Show a DatePicker when the DueDate input is focused.
        // Unfocus it immediately to allow focusing again after closing the DatePicker.
        dueDateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                dueDateEditText.clearFocus();
                if (hasFocus) showDueDatePicker();
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
            case R.id.createTaskConfirmButton: {
                // Creation has been requested.
                if (listener.handleTaskCreate(titleEditText.getText().toString(), dueDate)) {
                    hide();
                }
                break;
            }
            case R.id.createTaskCancelButton: {
                // Cancellation has been requested.
                hide();
                break;
            }
        }
    }

    /**
     * Shows a DatePicker to select the due date of the task.
     */
    private void showDueDatePicker() {
        // If no due date has been previously selected, the default is tomorrow.
        // Today would make less sense as that would be equal to not specifying a due date at all.
        DateTime current = dueDate != null ? dueDate : DateTime.now().plusDays(1);

        // Show DatePicker.
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dueDate = new DateTime(year, month, dayOfMonth, 0, 0);
            dueDateEditText.setText(dueDate.toString(context.getString(R.string.format_shortdate)));
            }
        }, current.getYear(), current.getMonthOfYear(), current.getDayOfMonth()).show();
    }

    /**
     * Reacts to requests for creating a new task.
     */
    public interface OnTaskCreateListener {
        boolean handleTaskCreate(String title, DateTime dueDate);
    }
}