package mobile.android.weibo.workqueue;

import mobile.android.weibo.workqueue.task.ParentTask;

public interface DoneAndProcess
{
    void doneProcess(ParentTask task);
   
}
