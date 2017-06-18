package com.tasky.android.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tasky.android.R;
import com.tasky.android.entities.Task;

/**
 * Adapter for displaying Task items in the UI.
 */
public class TaskArrayAdapter extends ArrayAdapter<Task> {
    public TaskArrayAdapter(Context context) {
        super(context, R.layout.task_item, R.id.taskItemTitleText);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        Task item = getItem(position);

        TextView titleText = (TextView)view.findViewById(R.id.taskItemTitleText);
        CheckBox doneState = (CheckBox)view.findViewById(R.id.taskItemDoneState);

        titleText.setText(item.getTitle());
        doneState.setChecked(item.isDone());
        return view;
    }
}