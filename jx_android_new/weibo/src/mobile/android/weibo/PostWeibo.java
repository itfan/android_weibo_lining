package mobile.android.weibo;

import mobile.android.weibo.adapters.FaceListAdapter;
import mobile.android.weibo.interfaces.Const;
import mobile.android.weibo.library.FaceMan;
import mobile.android.weibo.library.StorageManager;
import mobile.android.weibo.library.Tools;
import mobile.android.weibo.workqueue.task.CommentWeiboTask;
import mobile.android.weibo.workqueue.task.PostWeiboTask;
import mobile.android.weibo.workqueue.task.RepostWeiboTask;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PostWeibo extends Activity implements Const, OnClickListener,
		OnTouchListener, OnItemClickListener
{
	private EditText weiboContent;
	private ImageView minPicViewer;
	private TextView postWeiboTitle;
	private View inputBoard;
	private View insertAtButton;
	private View insertFaceButton;
	private View insertPicButton;
	private View insertTopicButton;
	private View isCommentView;
	private CheckBox isCommentCheckBox;
	private CheckBox postWeiboCheckBox; // 是否发一条微博
	private GridView faceList;
	private Bitmap bitmap;
	private static String filename;
	private int type;
	private long statusId;
	private String title;
	private String text;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.post_weibo);
		weiboContent = (EditText) findViewById(R.id.edittext_weibo_content);
		minPicViewer = (ImageView) findViewById(R.id.imageview_insert_pic_min_viewer);
		postWeiboTitle = (TextView) findViewById(R.id.textview_post_weibo_title);
		inputBoard = findViewById(R.id.framelayout_input_board);
		faceList = (GridView) findViewById(R.id.gridview_face_list);
		minPicViewer.setOnClickListener(this);
		weiboContent.setOnTouchListener(this);
		faceList.setOnItemClickListener(this);
		findViewById(R.id.button_send).setOnClickListener(this);
		insertAtButton = findViewById(R.id.linearlayout_insert_at);
		insertFaceButton = findViewById(R.id.linearlayout_insert_face);
		insertPicButton = findViewById(R.id.linearlayout_insert_pic);
		insertTopicButton = findViewById(R.id.linearlayout_insert_topic);
		isCommentView = findViewById(R.id.framelayout_is_comment);
		isCommentCheckBox = (CheckBox) findViewById(R.id.checkbox_is_comment);
		postWeiboCheckBox = (CheckBox) findViewById(R.id.checkbox_post_weibo);
		insertAtButton.setOnClickListener(this);
		insertFaceButton.setOnClickListener(this);
		insertPicButton.setOnClickListener(this);
		insertTopicButton.setOnClickListener(this);

		findViewById(R.id.imageview_back).setOnClickListener(this);

		faceList.setAdapter(new FaceListAdapter(this));
		bitmap = (Bitmap) getLastNonConfigurationInstance();
		if (bitmap != null)
		{
			minPicViewer.setVisibility(View.VISIBLE);
			minPicViewer.setImageBitmap(bitmap);
		}
		statusId = getIntent().getLongExtra("status_id", 0);
		type = getIntent().getIntExtra("type", TYPE_POST_WEIBO);
		title = getIntent().getStringExtra("title");
		text = getIntent().getStringExtra("text");
		viewSetting();

	}

	private void viewSetting()
	{

		switch (type)
		{
			case TYPE_POST_WEIBO:

				break;

			case TYPE_FORWARD:
				insertPicButton.setVisibility(View.GONE);
				isCommentView.setVisibility(View.VISIBLE);
				postWeiboCheckBox.setVisibility(View.GONE);
				isCommentCheckBox.setVisibility(View.VISIBLE);
				if (title != null)
				{
					postWeiboTitle.setText(title);
				}
				if (text != null)
				{
					weiboContent.setText(Tools.changeTextToFace(this,
							Html.fromHtml(Tools.atBlue(text))));
					weiboContent.getText().insert(0, " ");
					weiboContent.setSelection(0, 1);
					InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					imm.showSoftInput(weiboContent,
							InputMethodManager.SHOW_FORCED);
				}
				break;
			case TYPE_COMMENT:
				insertPicButton.setVisibility(View.GONE);
				isCommentView.setVisibility(View.VISIBLE);
				postWeiboCheckBox.setVisibility(View.VISIBLE);
				isCommentCheckBox.setVisibility(View.GONE);
				if (title != null)
				{
					postWeiboTitle.setText(title);
				}

				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id)
	{
		switch (parent.getId())
		{
			case R.id.gridview_face_list:

				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						FaceMan.getFaceResourceId(position));
				ImageSpan imageSpan = new ImageSpan(bitmap);
				String faceText = FaceMan.getFaceText(position);
				SpannableString spannableString = new SpannableString(faceText);
				spannableString.setSpan(imageSpan, 0, faceText.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				weiboContent.getText().insert(weiboContent.getSelectionStart(),
						spannableString);

			default:
				break;
		}

	}

	@Override
	public boolean onTouch(View view, MotionEvent event)
	{
		switch (view.getId())
		{
			case R.id.edittext_weibo_content:
				inputBoard.setVisibility(View.GONE);
				break;

			default:
				break;
		}
		return false;
	}

	@Override
	public Object onRetainNonConfigurationInstance()
	{
		return bitmap;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
			case CODE_REQUEST_CAPTURE_IMAGE:
				switch (resultCode)
				{
					case Activity.RESULT_OK:
						minPicViewer.setVisibility(View.VISIBLE);
						bitmap = (Bitmap) data.getExtras().get("data");
						minPicViewer.setImageBitmap(bitmap);
						filename = StorageManager.saveBitmap(bitmap);
						break;

					default:
						break;
				}
				break;
			case CODE_REQUEST_PICTURE_VIEWER:
				switch (resultCode)
				{
					case CODE_RESULT_REMOVE:
						filename = null;
						bitmap = null;
						minPicViewer.setImageBitmap(null);
						minPicViewer.setVisibility(View.GONE);
						break;
					case CODE_RESULT_RETURN:
						if (data != null)
						{
							filename = data.getStringExtra("filename");
							bitmap = BitmapFactory.decodeFile(filename);
							minPicViewer.setImageBitmap(bitmap);
						}
						break;
					default:
						break;
				}

				break;
			default:
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void send()
	{
		String text = weiboContent.getText().toString();
		switch (type)
		{
			case TYPE_POST_WEIBO:
				if (filename != null && "".equals(text))
				{
					text = "分享图片";
				}
				else if ("".equals(text))
				{

					Toast.makeText(this, "请输入微博内容", Toast.LENGTH_LONG).show();
					return;
				}
				PostWeiboTask postWeiboTask = new PostWeiboTask();
				postWeiboTask.text = text;
				postWeiboTask.file = filename;

				Tools.getGlobalObject(this).getWorkQueueStorage()
						.addTask(postWeiboTask);
				Toast.makeText(this, "已经提交发布微博任务到工作队列", Toast.LENGTH_LONG)
						.show();
				break;

			case TYPE_FORWARD:
				if ("".equals(text))
				{

					Toast.makeText(this, "请输入微博内容", Toast.LENGTH_LONG).show();
					return;
				}
				RepostWeiboTask repostWeiboTask = new RepostWeiboTask();
				repostWeiboTask.id = statusId;
				repostWeiboTask.text = text;
				if (isCommentCheckBox.isChecked())
					repostWeiboTask.isComment = 1;
				else
					repostWeiboTask.isComment = 0;

				Tools.getGlobalObject(this).getWorkQueueStorage()
						.addTask(repostWeiboTask);
				Toast.makeText(this, "已经提交转发微博任务到工作队列", Toast.LENGTH_LONG)
						.show();
				break;
			case TYPE_COMMENT:
				if ("".equals(text))
				{

					Toast.makeText(this, "请输入微博内容", Toast.LENGTH_LONG).show();
					return;
				}
				CommentWeiboTask commentWeiboTask = new CommentWeiboTask();
				commentWeiboTask.text = text;
				commentWeiboTask.weiboText =  text + this.text;
				commentWeiboTask.id = statusId;

				if (postWeiboCheckBox.isChecked())
					commentWeiboTask.postWeibo = true;
				else
					commentWeiboTask.postWeibo = false;

				Tools.getGlobalObject(this).getWorkQueueStorage()
						.addTask(commentWeiboTask);
				Toast.makeText(this, "已经提交评论微博任务到工作队列", Toast.LENGTH_LONG)
						.show();
				break;

		}

		finish();// 分享图片
	}

	@Override
	public void onClick(View view)
	{
		Intent intent = null;
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		switch (view.getId())
		{
			case R.id.button_send:
				send();
				break;
			case R.id.linearlayout_insert_pic:
				intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, CODE_REQUEST_CAPTURE_IMAGE);
				break;
			case R.id.linearlayout_insert_topic:
				String topicText = "请输入主题";
				weiboContent.getText().insert(weiboContent.getSelectionStart(),
						"#" + topicText + "#");
				weiboContent.setSelection(weiboContent.getSelectionStart()
						- topicText.length() - 1,
						weiboContent.getSelectionStart() - 1);

				imm.showSoftInput(weiboContent, InputMethodManager.SHOW_FORCED);
				break;
			case R.id.linearlayout_insert_at:
				String atText = "请输入用户名";
				weiboContent.getText().insert(weiboContent.getSelectionStart(),
						"@" + atText + " ");
				weiboContent
						.setSelection(weiboContent.getSelectionStart() - 1
								- atText.length(),
								weiboContent.getSelectionStart() - 1);

				imm.showSoftInput(weiboContent, InputMethodManager.SHOW_FORCED);
				break;
			case R.id.linearlayout_insert_face:

				inputBoard.setVisibility(View.VISIBLE);
				faceList.setVisibility(View.VISIBLE);
				break;
			case R.id.imageview_insert_pic_min_viewer:
				intent = new Intent(this, PictureViewer.class);
				intent.putExtra("filename", filename);
				startActivityForResult(intent, CODE_REQUEST_PICTURE_VIEWER);
				break;
			case R.id.imageview_back:
				finish();

				break;
			default:
				break;
		}
	}

	@Override
	protected void onDestroy()
	{
		filename = null;
		super.onDestroy();
	}

}
