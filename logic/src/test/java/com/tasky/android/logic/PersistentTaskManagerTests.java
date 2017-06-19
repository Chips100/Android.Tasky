package com.tasky.android.logic;

import com.tasky.android.entities.Task;
import com.tasky.android.logic.Mocks.TaskyDataProviderMock;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for the implementation of the PersistentTaskManager.
 */
public class PersistentTaskManagerTests {
    @Test
    public void PersistentTaskManager_createsTask() {
        TaskyDataProviderMock dataproviderMock = new TaskyDataProviderMock();
        PersistentTaskManager sut = new PersistentTaskManager(dataproviderMock);

        sut.createTask("TITLE", null);

        assertEquals(1, dataproviderMock.getTasks().size());
        assertEquals("TITLE", dataproviderMock.getTasks().get(0).getTitle());
        assertNull(dataproviderMock.getTasks().get(0).getDueDate());
        assertEquals(DateTime.now().getMillis(), dataproviderMock.getTasks().get(0).getCreatedOn().getMillis(),
            Period.seconds(1).getMillis());
    }

    @Test
    public void PersistentTaskManager_createsTaskWithDueDate() {
        TaskyDataProviderMock dataproviderMock = new TaskyDataProviderMock();
        PersistentTaskManager sut = new PersistentTaskManager(dataproviderMock);

        sut.createTask("TITLE", new DateTime(2017, 6, 18, 0, 0));

        assertEquals(1, dataproviderMock.getTasks().size());
        assertEquals("TITLE", dataproviderMock.getTasks().get(0).getTitle());
        assertEquals(new DateTime(2017, 6, 18, 0, 0), dataproviderMock.getTasks().get(0).getDueDate());
        assertEquals(DateTime.now().getMillis(), dataproviderMock.getTasks().get(0).getCreatedOn().getMillis(),
            Period.seconds(1).getMillis());
    }

    @Test
    public void PersistentTaskManager_setsTaskDone() {
        Task task = new Task();
        task.setId(42);
        TaskyDataProviderMock dataproviderMock = new TaskyDataProviderMock(task);

        PersistentTaskManager sut = new PersistentTaskManager(dataproviderMock);
        sut.setTaskDone(42);

        assertEquals(1, dataproviderMock.getTasks().size());
        assertEquals(42, dataproviderMock.getTasks().get(0).getId());
        assertEquals(true, dataproviderMock.getTasks().get(0).isDone());
    }

    @Test
    public void PersistentTaskManager_yieldsRelevantTask() {
        Task task = new Task();
        task.setTitle("UNITTEST");
        TaskyDataProviderMock dataproviderMock = new TaskyDataProviderMock(task);

        PersistentTaskManager sut = new PersistentTaskManager(dataproviderMock);
        List<Task> result = sut.getRelevantTasks();

        assertEquals(1, result.size());
        assertEquals("UNITTEST", result.get(0).getTitle());
    }

    @Test
    public void PersistentTaskManager_doesNotYieldRelevantTask_IfDone() {
        Task task = new Task();
        task.setTitle("UNITTEST");
        task.setDone(true);
        TaskyDataProviderMock dataproviderMock = new TaskyDataProviderMock(task);

        PersistentTaskManager sut = new PersistentTaskManager(dataproviderMock);
        List<Task> result = sut.getRelevantTasks();

        assertEquals(0, result.size());
    }

    @Test
    public void PersistentTaskManager_doesNotYieldRelevantTask_IfNotDue() {
        Task task = new Task();
        task.setTitle("UNITTEST");
        task.setDueDate(DateTime.now().plusDays(1));
        TaskyDataProviderMock dataproviderMock = new TaskyDataProviderMock(task);

        PersistentTaskManager sut = new PersistentTaskManager(dataproviderMock);
        List<Task> result = sut.getRelevantTasks();

        assertEquals(0, result.size());
    }

    @Test
    public void PersistentTaskManager_doesNotYieldRelevantTask_IfPostponed() {
        Task task = new Task();
        task.setTitle("UNITTEST");
        task.setPostponedUntil(DateTime.now().plusDays(1));
        TaskyDataProviderMock dataproviderMock = new TaskyDataProviderMock(task);

        PersistentTaskManager sut = new PersistentTaskManager(dataproviderMock);
        List<Task> result = sut.getRelevantTasks();

        assertEquals(0, result.size());
    }
}