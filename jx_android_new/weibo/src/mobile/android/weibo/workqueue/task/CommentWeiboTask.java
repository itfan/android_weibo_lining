package mobile.android.weibo.workqueue.task;

public class CommentWeiboTask extends ParentTask
{
	public long id;
	public String text;
	public String weiboText;
	public int commentOri;
	
	public boolean postWeibo;  // 同时提交一条微博
	
}
