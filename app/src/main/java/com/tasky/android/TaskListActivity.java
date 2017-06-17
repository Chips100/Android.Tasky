package com.tasky.android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.tasky.android.entities.Task;
import com.tasky.android.storage.SqliteTaskyDataProvider;
import com.tasky.android.storage.TaskyContract;
import com.tasky.android.storage.TaskyDataProvider;
import com.tasky.android.storage.queries.EmptyQueryFilter;
import com.tasky.android.storage.queries.ValueQueryFilter;

import org.joda.time.DateTime;

import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        /* Temporary real environment test for inserting a new task. */
        Task task = new Task();
        task.setTitle("Test Task!");
        task.setDone(true);
        task.setPostponedUntil(DateTime.now());
        task.setCreatedFromRecurringTaskId(null);
        task.setDueDate(DateTime.now().plusDays(5));

        TaskyDataProvider dataprovider = new SqliteTaskyDataProvider(this);
        dataprovider.insertTask(task);

        TextView txtExample = (TextView)findViewById(R.id.txtExample);
        txtExample.setText("Erzeugter Task hat ID: " + String.valueOf(task.getId()));


        /* Temporary real environment test for querying tasks. */
        List<Task> tasks = dataprovider.queryTasks(new ValueQueryFilter(TaskyContract.Task._ID, ValueQueryFilter.Type.Equals, 2).Or(
            new ValueQueryFilter(TaskyContract.Task._ID, ValueQueryFilter.Type.Equals, 3)
        ));
        String message = txtExample.getText().toString();
        message = message + " Gefunden: ";

        for(Task t : tasks) {
            message += String.valueOf(t.getId());
        }
        txtExample.setText(message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
