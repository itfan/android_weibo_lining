package mobile.android.weibo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import mobile.android.photo.PhotoViewer;
import mobile.android.weibo.interfaces.Const;
import mobile.android.weibo.library.StorageManager;
import mobile.android.weibo.library.Tools;
import mobile.android.weibo.library.WeiboManager;
import mobile.android.weibo.workqueue.DoneAndProcess;
import mobile.android.weibo.workqueue.task.ParentTask;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class PictureViewer extends Activity implements Const, DoneAndProcess,
		OnClickListener
{
	private ImageView pictureViewer;
	private Button back;
	private Button effect;
	private Button remove;
	private Button save;
	private String filename;
	private String fileUrl;
	private String imageUrl;
	private int type;
	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{

			imageUrl = WeiboManager.getImageurl(PictureViewer.this, fileUrl,
					PictureViewer.this);
			if (imageUrl != null)
			{
				pictureViewer.setImageURI(Uri.fromFile(new File(imageUrl)));
			}

			super.handleMessage(msg);
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.picture_viewer);
		pictureViewer = (ImageView) findViewById(R.id.imageview_picture_viewer);
		back = (Button) findViewById(R.id.button_back);
		effect = (Button) findViewById(R.id.button_effect);
		remove = (Button) findViewById(R.id.button_remove);
		save = (Button) findViewById(R.id.button_save);
		back.setOnClickListener(this);
		effect.setOnClickListener(this);
		remove.setOnClickListener(this);
		save.setOnClickListener(this);
		filename = getIntent().getStringExtra("filename");
		fileUrl = getIntent().getStringExtra("file_url");
		type = getIntent().getExtras()
				.getInt("type", PICTURE_VIEWER_POST_WEIBO);

		switch (type)
		{
			case PICTURE_VIEWER_POST_WEIBO:
				if (filename != null)
				{
					Bitmap bitmap = BitmapFactory.decodeFile(filename);
					pictureViewer.setImageBitmap(bitmap);
				}
				save.setVisibility(View.GONE);
				break;

			case PICTURE_VIEWER_WEIBO_BROWSER:
				effect.setVisibility(View.GONE);
				remove.setVisibility(View.GONE);
				imageUrl = WeiboManager.getImageurl(this, fileUrl, this);
				if (imageUrl != null)
				{
					pictureViewer.setImageURI(Uri.fromFile(new File(imageUrl)));
				}
				break;
		}
	}

	@Override
	public void doneProcess(ParentTask task)
	{
		handler.sendEmptyMessage(0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (resultCode)
		{
			case CODE_RESULT_SAVE:
				if (data != null)
				{
					filename = data.getStringExtra("filename");
					Bitmap bitmap = BitmapFactory.decodeFile(filename);
					pictureViewer.setImageBitmap(bitmap);
				}
				break;

			default:
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View view)
	{
		Intent intent = null;
		switch (view.getId())
		{
			case R.id.button_back:
				intent = new Intent();
				intent.putExtra("filename", filename);
				setResult(CODE_RESULT_RETURN, intent);
				finish();
				break;
			case R.id.button_remove:
				setResult(CODE_RESULT_REMOVE);
				finish();
				break;
			case R.id.button_effect:
				intent = new Intent(this, PhotoViewer.class);
				intent.putExtra("filename", filename);
				startActivityForResult(intent, 0);
				break;
			case R.id.button_save:
				
				if (imageUrl != null && fileUrl != null)
				{
					String fn = PATH_STORAGE + "/"
							+ String.valueOf(fileUrl.hashCode()) + ".jpg";
					try
					{
						FileInputStream fis = new FileInputStream(imageUrl);
						FileOutputStream fos = new FileOutputStream(fn);
						Tools.dataTransfer(fis, fos);
						fis.close();
						fos.close();
						Toast.makeText(this, "保存成功，图像路径：" + fn,
								Toast.LENGTH_LONG).show();
					}
					catch (Exception e)
					{
						Toast.makeText(this, "保存图像失败", Toast.LENGTH_LONG)
								.show();
					}
				}
				break;
			default:
				break;
		}
	}
}
