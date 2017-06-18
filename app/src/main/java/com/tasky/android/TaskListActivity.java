package com.tasky.android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tasky.android.adapters.TaskArrayAdapter;
import com.tasky.android.entities.Task;
import com.tasky.android.logic.PersistentTaskManager;
import com.tasky.android.logic.TaskManager;
import com.tasky.android.storage.SqliteTaskyDataProvider;
import com.tasky.android.storage.TaskyContract;
import com.tasky.android.storage.TaskyDataProvider;
import com.tasky.android.storage.queries.EmptyQueryFilter;
import com.tasky.android.storage.queries.ValueQueryFilter;

import org.joda.time.DateTime;

import java.util.List;

public class TaskListActivity extends AppCompatActivity {
    private TaskManager taskManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.newTaskButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            createNewTask();
            }
        });

        // TODO: As a starting point, we do very poor man's DI and Entourage Pattern all the way.
        // Would be cool to change it. But that needs more understanding...
        taskManager = new PersistentTaskManager(new SqliteTaskyDataProvider(this));
        renderRelevantTasks();
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

    /**
     * Displays the UI to create a new task.
     */
    public void createNewTask() {
        Toast.makeText(this, "You might create a new Task now...", Toast.LENGTH_LONG).show();
    }

    /**
     * Loads all relevant tasks and displays them in the list.
     */
    public void renderRelevantTasks() {
        TaskArrayAdapter adapter = new TaskArrayAdapter(this);
        adapter.addAll(taskManager.getRelevantTasks());

        ListView relevantTaskList = (ListView)findViewById(R.id.relevantTaskList);
        relevantTaskList.setAdapter(adapter);
    }
}