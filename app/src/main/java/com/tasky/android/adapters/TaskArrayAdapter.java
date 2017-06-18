package com.tasky.android.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tasky.android.R;
import com.tasky.android.entities.Task;
import com.tasky.android.utilities.ParameterCheck;

/**
 * Adapter for displaying Task items in the UI.
 */
public class TaskArrayAdapter extends ArrayAdapter<Task> {
    private final OnTaskDoneListener listener;

    public TaskArrayAdapter(Context context, OnTaskDoneListener listener) {
        super(context, R.layout.task_item, R.id.taskItemTitleText);
        ParameterCheck.NotNull(listener, "listener");

        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        final Task item = getItem(position);

        TextView titleText = (TextView)view.findViewById(R.id.taskItemTitleText);
        CheckBox doneState = (CheckBox)view.findViewById(R.id.taskItemDoneState);

        // Attach listener to checkbox for handling the event
        // when the user sets the task to done.
        doneState.setTag(position);
        doneState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) listener.handleTaskDone(getItem((Integer)buttonView.getTag()));
            }
        });

        titleText.setText(item.getTitle());
        doneState.setChecked(item.isDone());
        return view;
    }

    /**
     * Handles when a task is marked as done by the user.
     */
    public interface OnTaskDoneListener {
        void handleTaskDone(Task task);
    }
}