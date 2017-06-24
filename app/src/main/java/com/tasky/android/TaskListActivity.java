package com.tasky.android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.tasky.android.adapters.TaskArrayAdapter;
import com.tasky.android.dialogs.CreateTaskDialog;
import com.tasky.android.entities.Task;
import com.tasky.android.logic.PersistentTaskManager;
import com.tasky.android.logic.TaskManager;
import com.tasky.android.storage.SqliteTaskyDataProvider;

import org.joda.time.DateTime;

public class TaskListActivity extends AppCompatActivity {
    private TaskManager taskManager;
    private Snackbar revertTaskDoneSnackbar;
    private Task lastTaskDone;

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

        revertTaskDoneSnackbar = setupRevertTaskDoneSnackbar(fab);

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
        hideSnackbar();

        new CreateTaskDialog(this, new CreateTaskDialog.OnTaskCreateListener() {
            @Override
            public boolean handleTaskCreate(String title, DateTime dueDate) {
            // Create task via manager and reload the relevant task list accordingly.
            taskManager.createTask(title, dueDate);
            renderRelevantTasks();
            return true;
            }
        }).show();
    }

    /**
     * Loads all relevant tasks and displays them in the list.
     */
    public void renderRelevantTasks() {
        TaskArrayAdapter adapter = new TaskArrayAdapter(this, new TaskArrayAdapter.OnTaskDoneListener() {
            @Override
            public void handleTaskDone(Task task) {
                setTaskDone(task);
            }
        });
        adapter.addAll(taskManager.getRelevantTasks());

        ListView relevantTaskList = (ListView)findViewById(R.id.relevantTaskList);
        relevantTaskList.setAdapter(adapter);
    }

    /**
     * Sets the done-state of the specified task.
     * @param task Task that should be done.
     */
    public void setTaskDone(Task task) {
        // Set task to done via manager and reload the relevant task list accordingly.
        taskManager.setTaskDone(task.getId());
        renderRelevantTasks();

        // Present an option to revert the action.
        lastTaskDone = task;
        revertTaskDoneSnackbar.show();
    }

    /**
     * Creates a Snackbar that allows to revert setting the last task to done.
     * @param view View to find a parent from.
     * @return The created Snackbar.
     */
    private Snackbar setupRevertTaskDoneSnackbar(View view) {
        return Snackbar.make(view, R.string.task_done_message, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.undo, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lastTaskDone == null) return;

                    taskManager.revertTaskDone(lastTaskDone.getId());
                    lastTaskDone = null;
                    renderRelevantTasks();
                }
            });
    }

    /**
     * Hides any Snackbar that might currently be displayed.
     */
    public void hideSnackbar() {
        revertTaskDoneSnackbar.dismiss();
    }
}