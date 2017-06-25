package com.tasky.android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tasky.android.adapters.TaskArrayAdapter;
import com.tasky.android.dialogs.ActionDialogBase;
import com.tasky.android.dialogs.CreateTaskDialog;
import com.tasky.android.dialogs.PostponeTaskDialog;
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
        registerForContextMenu(findViewById(R.id.relevantTaskList));

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.relevantTaskList) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_task_list_item, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        TaskArrayAdapter adapter = (TaskArrayAdapter)((ListView)findViewById(R.id.relevantTaskList)).getAdapter();

        switch(item.getItemId()) {
            case R.id.menu_task_list_item_postpone:
                postponeTask(adapter.getItem(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Displays the UI to create a new task.
     */
    public void createNewTask() {
        hideSnackbar();

        new CreateTaskDialog(this, new ActionDialogBase.OnActionDialogCompleteListener<CreateTaskDialog.TaskCreateResult>() {
            @Override
            public boolean handleComplete(CreateTaskDialog.TaskCreateResult result) {
                // Create task via manager and reload the relevant task list accordingly.
                taskManager.createTask(result.getTitle(), result.getDueDate());
                renderRelevantTasks();
                return true;
            }
        }).show();
    }

    /**
     * Displays the UI to postpone an existing task.
     * @param task Task that should be postponed.
     */
    public void postponeTask(final Task task) {
        hideSnackbar();

        new PostponeTaskDialog(this, new ActionDialogBase.OnActionDialogCompleteListener<DateTime>() {
            @Override
            public boolean handleComplete(DateTime result) {
                taskManager.postponeTask(task.getId(), result);
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
        // Set task to done via manager.
        taskManager.setTaskDone(task.getId());

        // Present an option to revert the action.
        lastTaskDone = task;
        revertTaskDoneSnackbar.show();

        // Reload task list to remove now done task.
        // Little delay to display the Checkbox-Check animation.
        ActivityTools.delay(new Runnable() {
            @Override
            public void run() {
                renderRelevantTasks();
            }
        }, getResources().getInteger(R.integer.remove_done_task_delay));
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