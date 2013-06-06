package mobile.android.weibo.workqueue;

import java.util.ArrayList;
import java.util.List;

public class WorkQueueStorage
{
	private List<String> webFileDoingList = new ArrayList<String>();
	private List taskList = new ArrayList();

	public List<String> getDoingWebFileUrls()
	{
		synchronized (webFileDoingList)
		{

			if (webFileDoingList.size() > 0)
			{
				List<String> resultList = new ArrayList<String>();
				resultList.addAll(webFileDoingList);

				return resultList;
			}
			else
			{
				return null;
			}
		}

	}

	public void removeDoingWebFileUrl(List<String> list)
	{
		synchronized (webFileDoingList)
		{
			if (list != null)
				webFileDoingList.removeAll(list);

		}
	}

	public void addDoingWebFileUrl(String url)
	{
		synchronized (webFileDoingList)
		{
			webFileDoingList.add(url);
		}
	}

	// 目前必须只得到到一个任务，如果一下得到多个任务，需要修改WorkQueueMonitor类的代码
	public List getDoingTasks()
	{
		synchronized (taskList)
		{
			List result = new ArrayList();
			if (taskList.size() > 0)
			{
				result.add(taskList.get(0));
				return result;
			}
			else
			{
				return null;
			}

		}
	}

	public void removeTask(List tasks)
	{
		synchronized (taskList)
		{
			if (taskList.size() > 0)
			{
				taskList.removeAll(tasks);

			}
		}
	}

	public void addTask(Object task)
	{
		synchronized (taskList)
		{
			taskList.add(task);
		}
	}
}
