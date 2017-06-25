package com.tasky.android.dialogs;

import android.content.Context;
import android.widget.EditText;

import com.tasky.android.R;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Dialog with inputs required to create a new task.
 */
public class CreateTaskDialog extends ActionDialogBase<CreateTaskDialog.TaskCreateResult> {
    public CreateTaskDialog(Context context, OnActionDialogCompleteListener<TaskCreateResult> completeListener) {
        super(context, completeListener);
    }

    /**
     * Gets the Id of the view that should be used for the content of the dialog.
     * @return The Id of the content view for the dialog.
     */
    @Override
    protected int getContentViewId() { return R.layout.action_dialog_create_task; }

    /**
     * Gets a result that represents the user input in the dialog.
     * @return A result with the user input in the dialog.
     */
    @Override
    protected TaskCreateResult getDialogResult() {
        return new TaskCreateResult(
            ((EditText)findViewById(R.id.createTaskTitle)).getText().toString(),
            getDateValue(R.id.createTaskDueDateEditText)
        );
    }

    /**
     * Can be overridden to indicate which controls in the dialog represent date inputs.
     * @return List of Ids of the controls that represent date inputs.
     */
    @Override
    protected List<Integer> getDateInputIds() {
        List<Integer> result = new ArrayList();
        result.add(R.id.createTaskDueDateEditText);
        return result;
    }

    /**
     * Can be overridden to suggest a Date value when the user first opens a date picker.
     * If not overridden, the current Date will be suggested.
     * @param dateInputId Id of the control for which to suggest a value.
     * @return The value that should be suggested when the user opens the date picker for the first time.
     */
    @Override
    protected DateTime suggestDateValue(int dateInputId) {
        // Suggest tomorrow for due date, as today would have the same effect as selecting no value.
        if (dateInputId == R.id.createTaskDueDateEditText) {
            return DateTime.now().plusDays(1);
        }

        return super.suggestDateValue(dateInputId);
    }

    /**
     * Can be overridden to define a custom text for the button that completes the dialog.
     * @return A String with the custom text for the button; or null to keep the default.
     */
    @Override
    protected String getCompleteButtonText() {
        return getContext().getResources().getString(R.string.action_dialog_create_task_ok);
    }

    /**
     * Can be overridden to automatically show the keyboard when the dialog is opened
     * to allow immediate input by the user without any extra clicks.
     * @return True, if the keyboard should be shown when the dialog is opened; otherwise false.
     */
    @Override
    protected boolean showKeyboardOnOpen() { return true; }


    /**
     * Represents the user input from a CreateTaskDialog.
     */
    public final class TaskCreateResult {
        private final String title;
        private final DateTime dueDate;

        public TaskCreateResult(String title, DateTime dueDate) {
            this.title = title;
            this.dueDate = dueDate;
        }

        /**
         * Gets the title entered in the CreateTaskDialog.
         * @return The title entered in the CreateTaskDialog.
         */
        public String getTitle() { return title; }

        /**
         * Gets the due date entered in the CreateTaskDialog.
         * @return The due date entered in the CreateTaskDialog.
         */
        public DateTime getDueDate() { return dueDate; }
    }
}