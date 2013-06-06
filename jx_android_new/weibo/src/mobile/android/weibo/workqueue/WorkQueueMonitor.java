package mobile.android.weibo.workqueue;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mobile.android.weibo.interfaces.Const;
import mobile.android.weibo.workqueue.task.ParentTask;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class WorkQueueMonitor implements Runnable, Const
{
	private WorkQueueStorage storage;
	private Map<Integer, DoneAndProcess> doneAndProcessMap = new HashMap<Integer, DoneAndProcess>();
	private DoingAndProcess doingAndProcess;
	private Activity activity;
	private boolean stopFlag = false;
	private Thread thread;
	private int monitorType = MONITOR_TYPE_IMAGE; // MONITOR_TYPE_IMAGE = 1501
													// MONITOR_TYPE_TASK = 1502
	private Handler hander = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			ParentTask parentTask = (ParentTask) msg.obj;

			if (parentTask != null && parentTask.doneAndProcess != null)
			{
				parentTask.doneAndProcess.doneProcess(parentTask);
			}
			else
			{
				Collection<DoneAndProcess> doneAndProcesses = doneAndProcessMap
						.values();


				for (DoneAndProcess doneAndProcess : doneAndProcesses)
				{
					doneAndProcess.doneProcess(parentTask);
					
				}
			}

			super.handleMessage(msg);
		}

	};

	public WorkQueueMonitor(Activity activity, WorkQueueStorage storage,
			DoingAndProcess doingAndProcess, int monitorType)
	{
		this.activity = activity;
		this.storage = storage;

		this.doingAndProcess = doingAndProcess;
		this.monitorType = monitorType;

	}

	public void start()
	{
		if (thread == null)
		{
			thread = new Thread(this);
			thread.start();
		}
	}

	public void stop()
	{
		stopFlag = true;
	}

	private void imageScan()
	{
		List<String> webFileDoingList = storage.getDoingWebFileUrls();

		while (webFileDoingList != null)
		{
			try
			{
				doingAndProcess.doingProcess(webFileDoingList);

				hander.sendEmptyMessage(0);
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}
			finally
			{
				storage.removeDoingWebFileUrl(webFileDoingList);
			}
			webFileDoingList = storage.getDoingWebFileUrls();
		}

	}

	private void taskScan()
	{
		List taskList = storage.getDoingTasks();
		while (taskList != null)
		{
			try
			{
				doingAndProcess.doingProcess(taskList);

				Message msg = new Message();

				if (taskList.size() > 0)
					msg.obj = taskList.get(0);
				hander.sendMessage(msg);
			}
			catch (Exception e)
			{

			}
			finally
			{
				storage.removeTask(taskList);
			}
			taskList = storage.getDoingTasks();
		}
	}

	@Override
	public void run()
	{

		while (!stopFlag)
		{

			if (monitorType == MONITOR_TYPE_IMAGE)
			{
				imageScan();
			}
			else if (monitorType == MONITOR_TYPE_TASK)
			{
				taskScan();
			}
			try
			{
				Thread.sleep(200);
			}
			catch (Exception e)
			{

			}

		}

	}

	public void addDoneAndProcess(int type, DoneAndProcess doneAndProcess)
	{

		if (doneAndProcess != null)
		{
			doneAndProcessMap.put(type, doneAndProcess);
		}
		
	}
}
