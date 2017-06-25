package com.tasky.android.dialogs;

import android.content.Context;
import android.os.Bundle;

import com.tasky.android.R;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Dialog with inputs required to postpone an existing task.
 */
public class PostponeTaskDialog extends ActionDialogBase<DateTime> {
    public PostponeTaskDialog(Context context, OnActionDialogCompleteListener<DateTime> completeListener) {
        super(context, completeListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Preset tomorrow for postpone date (minimum value that would have an effect).
        setDateValue(R.id.action_dialog_postpone_task_postpone_date, DateTime.now().plusDays(1));
    }

    /**
     * Gets the Id of the view that should be used for the content of the dialog.
     * @return The Id of the content view for the dialog.
     */
    @Override
    protected int getContentViewId() { return R.layout.action_dialog_postpone_task; }

    /**
     * Gets a result that represents the user input in the dialog.
     * @return A result with the user input in the dialog.
     */
    @Override
    protected DateTime getDialogResult() {
        return getDateValue(R.id.action_dialog_postpone_task_postpone_date);
    }

    /**
     * Can be overridden to indicate which controls in the dialog represent date inputs.
     * @return List of Ids of the controls that represent date inputs.
     */
    @Override
    protected List<Integer> getDateInputIds() {
        List<Integer> result = new ArrayList();
        result.add(R.id.action_dialog_postpone_task_postpone_date);
        return result;
    }

    /**
     * Can be overridden to define a custom text for the button that completes the dialog.
     * @return A String with the custom text for the button; or null to keep the default.
     */
    @Override
    protected String getCompleteButtonText() {
        return getContext().getResources().getString(R.string.action_dialog_postpone_task_ok);
    }
}