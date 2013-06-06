package mobile.android.photo;

import java.io.FileOutputStream;

import mobile.android.weibo.R;
import mobile.android.weibo.interfaces.Const;
import mobile.android.weibo.library.Tools;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PhotoViewer extends Activity implements Const, OnClickListener,
		OnMenuItemClickListener, AllThreadEnd, OnTouchListener,
		OnSeekBarChangeListener
{

	private View mEffect;
	private View mCombination;
	private View mDrawing;
	public static ProgressBar mpbPhotoProcess;
	private Bitmap mSourceBitmap;
	private Bitmap mEffectBitmap;
	private ImageViewExt mivDrawing;
	private ImageView mivCleanRegion;
	private FrameLayout mflScreen;
	private TextView mtvUnCleanLayer;
	private FrameLayout mflCleanRegion;
	private FrameLayoutExt mflMoveRegion;

	private TextView mtvUnCleanCenterLayer;

	private ImageView mivResizeLeftTop;
	private ImageView mivResizeLeftTop1;
	private ImageView mivResizeLeftBottom;
	private ImageView mivResizeLeftBottom1;
	private ImageView mivResizeRightTop;
	private ImageView mivResizeRightTop1;
	private ImageView mivResizeRightBottom;
	private ImageView mivResizeRightBottom1;

	// 下面是屏幕下方的几个层次的按钮
	private View mMainButton;
	private View mMosaicButton;

	private TextView mtvStart;

	private MyCanvas mivLayer;

	private int mSrcRegionLeft;
	private int mSrcRegionTop;
	private int mSrcRegionWidth;
	private int mSrcRegionHeight;

	private int mRectLineWidth = 1;
	private int mRectLineColor = Color.WHITE;

	// 用于注册Context Menu，并不显示
	private View mEffectMenu;

	private SeekBar msbRotate;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.photo_viewer);
		mEffect = findViewById(R.id.llEffect);

		mpbPhotoProcess = (ProgressBar) findViewById(R.id.pbPhotoProcess);
		mtvUnCleanLayer = (TextView) findViewById(R.id.tvUnCleanLayer);

		mMainButton = findViewById(R.id.llMainButton);
		mMosaicButton = findViewById(R.id.llMosaicButton);

		View startButton = findViewById(R.id.llStart);
		View backButton = findViewById(R.id.llBack);

		View saveButton = findViewById(R.id.llSave);
		View cancelButton = findViewById(R.id.llCancel);

		saveButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);

		mtvStart = (TextView) findViewById(R.id.tvStart);
		msbRotate = (SeekBar) findViewById(R.id.sbRotate);
		msbRotate.setOnSeekBarChangeListener(this);
		startButton.setOnClickListener(this);
		backButton.setOnClickListener(this);

		mEffect.setOnClickListener(this);

		mflMoveRegion = (FrameLayoutExt) findViewById(R.id.flMoveRegion);
		mflMoveRegion.setOnTouchListener(this);

		// mDrawing.setOnClickListener(this);
		mivDrawing = (ImageViewExt) findViewById(R.id.ivDrawing);

		// 获得要编辑的图像文件名
		String filename = getIntent().getStringExtra("filename");

		if (filename == null)
		{
			mSourceBitmap = (Bitmap) getIntent().getExtras().get("bitmap");
		}
		else
		{

			mSourceBitmap = Tools.getFitBitmap(filename, BITMAP_MAX_SIZE);

		}
		// mSourceBitmap = BitmapFactory.decodeFile("/sdcard/200992800346.jpg");

		copyBitmap();
		mflMoveRegion.mBitmap = mEffectBitmap;
		mflMoveRegion.mivDrawing = mivDrawing;

		mivDrawing.setImageBitmap(mSourceBitmap);

		mEffectMenu = findViewById(R.id.tvEffectMenu);

		ProcessBitmapRegions.mAllThreadEnd = this;
		ProcessBitmapRegions.processType = "";
		registerForContextMenu(mEffectMenu);

	}

	private Bitmap mOldEffectBitmap;

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser)
	{
		Matrix matrix = new Matrix();
		matrix.setRotate(progress);
		mEffectBitmap = Bitmap.createBitmap(mOldEffectBitmap, 0, 0,
				mOldEffectBitmap.getWidth(), mOldEffectBitmap.getHeight(),
				matrix, true);
		mflMoveRegion.mBitmap = mEffectBitmap;
		mHandler.sendEmptyMessage(0);

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo)
	{
		switch (view.getId())
		{
			case R.id.tvEffectMenu:
				getMenuInflater().inflate(R.menu.effect_menu, menu);
				menu.findItem(R.id.mnuGray).setOnMenuItemClickListener(this);
				menu.findItem(R.id.mnuMosaic).setOnMenuItemClickListener(this);
				menu.findItem(R.id.mnuCrop).setOnMenuItemClickListener(this);
				menu.findItem(R.id.mnuRotate).setOnMenuItemClickListener(this);
				menu.findItem(R.id.mnuResume).setOnMenuItemClickListener(this);

				break;

		}
		super.onCreateContextMenu(menu, view, menuInfo);
	}

	private void copyBitmap()
	{
		// mEffectBitmap = Bitmap.createBitmap(mSourceBitmap);
		mEffectBitmap = Bitmap.createBitmap(mSourceBitmap.getWidth(),
				mSourceBitmap.getHeight(), Config.ARGB_4444);
		Canvas canvas = new Canvas(mEffectBitmap);
		canvas.drawBitmap(mSourceBitmap, 0, 0, null);
	}

	@Override
	public void onClick(View view)
	{

		switch (view.getId())
		{
			case R.id.llEffect:

				openContextMenu(mEffectMenu);
				break;

			case R.id.llStart:
				if (mEffectBitmap == null)
				{
					copyBitmap();
				}
				Rect rect = new Rect();

				rect.left = (int) ((mflMoveRegion.getLeft() - mflMoveRegion.mRegionLeft) * mflMoveRegion.mScale);
				rect.top = (int) ((mflMoveRegion.getTop() - mflMoveRegion.mRegionTop) * mflMoveRegion.mScale);
				rect.right = (int) ((mflMoveRegion.getRight() - mflMoveRegion.mRegionLeft) * mflMoveRegion.mScale);
				rect.bottom = (int) ((mflMoveRegion.getBottom() - mflMoveRegion.mRegionTop) * mflMoveRegion.mScale);
				if (ProcessBitmapRegions.PROCESS_TYPE_MOSAIC
						.equals(ProcessBitmapRegions.processType))
				{
					ProcessBitmapRegions.isWorking = true;
					mpbPhotoProcess.setVisibility(View.VISIBLE);
					mpbPhotoProcess.setProgress(0);
					mflMoveRegion.setVisibility(View.GONE);

					ProcessBitmapRegions processBitmapRegions = new ProcessBitmapRegions(
							new MosaicProcess(mEffectBitmap, rect));

					processBitmapRegions.work();
				}
				else if (ProcessBitmapRegions.PROCESS_TYPE_CROP
						.equals(ProcessBitmapRegions.processType))
				{
					mEffectBitmap = Bitmap.createBitmap(mEffectBitmap,
							rect.left, rect.top, rect.right - rect.left,
							rect.bottom - rect.top);
					mHandler.sendEmptyMessage(0);
				}
				break;

			case R.id.llBack:
				mtvUnCleanLayer.setVisibility(View.GONE);
				mflMoveRegion.setVisibility(View.GONE);
				mMosaicButton.setVisibility(View.GONE);
				mMainButton.setVisibility(View.VISIBLE);
				break;
			case R.id.llSave:
				try
				{
					String fn = Tools.getEffectTempImageFilename();

					FileOutputStream fos = new FileOutputStream(fn);
					
					mEffectBitmap.compress(CompressFormat.JPEG, 100, fos);
					
					fos.close();
				
					Intent intent = new Intent();
					intent.putExtra("filename", fn);
					
					
					setResult(CODE_RESULT_SAVE, intent);
					
					finish();
				}
				catch (Exception e)
				{
					Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG)
							.show();
				}

				break;
			case R.id.llCancel:
				setResult(CODE_RESULT_CANCEL);
				finish();
				break;
		}

	}

	@Override
	public boolean onMenuItemClick(MenuItem item)
	{
		if (ProcessBitmapRegions.isWorking)
		{
			Toast.makeText(this, "任务正在进行中，无法进行其他操作.", Toast.LENGTH_LONG).show();
			return false;
		}
		int screenWidth = mivDrawing.getWidth();
		int screenHeight = mivDrawing.getHeight();

		int sourceWidth = mEffectBitmap.getWidth();
		int sourceHeight = mEffectBitmap.getHeight();
		int regionTop = 0;
		int regionLeft = 0;
		int regionRight = 0;
		int regionBottom = 0;
		if (sourceWidth * screenHeight > sourceHeight * screenWidth)
		{

			int insideHeight = sourceHeight * screenWidth / sourceWidth;
			regionTop = (screenHeight - insideHeight) / 2;
			regionLeft = 0;
			regionRight = mivDrawing.getMeasuredWidth();
			regionBottom = regionTop + insideHeight + 1;

		}
		else
		{
			int insideWidth = sourceWidth * screenHeight / sourceHeight;
			regionLeft = (screenWidth - insideWidth) / 2;
			regionTop = 0;

			regionRight = regionLeft + insideWidth + 1;
			regionBottom = mivDrawing.getMeasuredHeight();

		}
		switch (item.getItemId())
		{
			case R.id.mnuGray:
				ProcessBitmapRegions.isWorking = true;
				mpbPhotoProcess.setVisibility(View.VISIBLE);
				mpbPhotoProcess.setMax(mEffectBitmap.getWidth() - 1);
				mpbPhotoProcess.setProgress(0);
				if (mEffectBitmap == null)
				{
					mEffectBitmap = Bitmap.createBitmap(
							mSourceBitmap.getWidth(),
							mSourceBitmap.getHeight(), Config.ARGB_8888);
					Canvas canvas = new Canvas(mEffectBitmap);
					canvas.drawBitmap(mSourceBitmap, 0, 0, null);
				}
				mflMoveRegion.mBitmap = mEffectBitmap;
				ProcessBitmapRegions processBitmapRegions = new ProcessBitmapRegions(
						new GrayProcess(mEffectBitmap));
				processBitmapRegions.work();
				msbRotate.setVisibility(View.GONE);
				ProcessBitmapRegions.processType = ProcessBitmapRegions.PROCESS_TYPE_GRAY;
				break;
			case R.id.mnuMosaic:

				ProcessBitmapRegions.processType = ProcessBitmapRegions.PROCESS_TYPE_MOSAIC;
				mMainButton.setVisibility(View.GONE);
				mMosaicButton.setVisibility(View.VISIBLE);
				mflMoveRegion.setVisibility(View.VISIBLE);
				mtvStart.setText("马赛克");

				mtvUnCleanLayer.setVisibility(View.VISIBLE);

				mtvUnCleanLayer.getLayoutParams().width = regionRight
						- regionLeft;
				mtvUnCleanLayer.getLayoutParams().height = regionBottom
						- regionTop;
				mtvUnCleanLayer.requestLayout();
				msbRotate.setVisibility(View.GONE);
				break;
			case R.id.mnuCrop:
				if (ProcessBitmapRegions.isWorking)
				{
					Toast.makeText(this, "任务正在进行中，无法进行其他操作.", Toast.LENGTH_LONG)
							.show();
					break;
				}
				ProcessBitmapRegions.processType = ProcessBitmapRegions.PROCESS_TYPE_MOSAIC;
				mMainButton.setVisibility(View.GONE);
				mMosaicButton.setVisibility(View.VISIBLE);
				mflMoveRegion.setVisibility(View.VISIBLE);
				mtvStart.setText("截图");
				ProcessBitmapRegions.processType = ProcessBitmapRegions.PROCESS_TYPE_CROP;
				mtvUnCleanLayer.setVisibility(View.VISIBLE);

				mtvUnCleanLayer.getLayoutParams().width = regionRight
						- regionLeft;
				mtvUnCleanLayer.getLayoutParams().height = regionBottom
						- regionTop;
				mtvUnCleanLayer.requestLayout();
				msbRotate.setVisibility(View.GONE);

				break;
			case R.id.mnuRotate:
				if (ProcessBitmapRegions.PROCESS_TYPE_ROTATE
						.equals(ProcessBitmapRegions.processType))
					break;
				ProcessBitmapRegions.processType = ProcessBitmapRegions.PROCESS_TYPE_ROTATE;
				mOldEffectBitmap = mEffectBitmap;
				msbRotate.setVisibility(View.VISIBLE);
				msbRotate.setProgress(0);

				mflMoveRegion.mBitmap = mEffectBitmap;
				mHandler.sendEmptyMessage(0);
				break;
			case R.id.mnuResume:
				msbRotate.setVisibility(View.GONE);
				copyBitmap();
				mHandler.sendEmptyMessage(0);
				break;
		}
		return false;
	}

	private void processEffect()
	{
		mivDrawing.setImageBitmap(mEffectBitmap);
		mflMoveRegion.mBitmap = mEffectBitmap;
		mflMoveRegion.setVisibility(View.GONE);
		mpbPhotoProcess.setVisibility(View.GONE);
		mtvUnCleanLayer.setVisibility(View.GONE);
		mMainButton.setVisibility(View.VISIBLE);
		mMosaicButton.setVisibility(View.GONE);
	}

	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{

			try
			{

				mivDrawing.setImageBitmap(mEffectBitmap);
				mflMoveRegion.mBitmap = mEffectBitmap;
				mflMoveRegion.setVisibility(View.GONE);
				mpbPhotoProcess.setVisibility(View.GONE);
				mtvUnCleanLayer.setVisibility(View.GONE);
				mMainButton.setVisibility(View.VISIBLE);
				mMosaicButton.setVisibility(View.GONE);
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}
			super.handleMessage(msg);
		}

	};

	@Override
	public void onFinish()
	{

		try
		{
			mHandler.sendEmptyMessage(0);

		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		ProcessBitmapRegions.isWorking = false;

	}

	private float mOldX;
	private float mOldY;
	private boolean mLeftTopResize = false;
	private boolean mLeftBottomResize = false;
	private boolean mRightTopResize = false;
	private boolean mRightBottomResize = false;

	@Override
	public boolean onTouch(View view, MotionEvent event)
	{

		Drawable drawable = null;
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				if (mEffectBitmap != null)
					mflMoveRegion.mBitmap = mEffectBitmap;

				if (event.getX() <= ProcessBitmapRegions.RESIZE_REGION_SIZE
						&& event.getY() <= ProcessBitmapRegions.RESIZE_REGION_SIZE)
				{
					mLeftTopResize = true;
					mflMoveRegion.mLeftTopRegionResourceId = R.drawable.left_top_focused;
				}
				else if (event.getX() <= ProcessBitmapRegions.RESIZE_REGION_SIZE
						&& event.getY() >= view.getHeight()
								- ProcessBitmapRegions.RESIZE_REGION_SIZE)
				{
					mLeftBottomResize = true;
					mflMoveRegion.mLeftBottomRegionResourceId = R.drawable.left_bottom_focused;
				}
				else if (event.getX() >= view.getWidth()
						- ProcessBitmapRegions.RESIZE_REGION_SIZE
						&& event.getY() <= ProcessBitmapRegions.RESIZE_REGION_SIZE)
				{
					mRightTopResize = true;
					mflMoveRegion.mRightTopRegionResourceId = R.drawable.right_top_focused;
				}
				else if (event.getX() >= view.getWidth()
						- ProcessBitmapRegions.RESIZE_REGION_SIZE
						&& event.getY() >= view.getHeight()
								- ProcessBitmapRegions.RESIZE_REGION_SIZE)
				{
					mRightBottomResize = true;
					mflMoveRegion.mRightBottomRegionResourceId = R.drawable.right_bottom_focused;
				}
				else
				{
					mflMoveRegion.mRectLineColor = ProcessBitmapRegions.RECT_LINE_COLOR_FOCUSED;
					mflMoveRegion.mRectLineWidth = ProcessBitmapRegions.RECT_LINE_WIDTH_FOCUSED;

				}

				mOldX = event.getRawX();
				mOldY = event.getRawY();
				mflMoveRegion.invalidate();
				break;
			case MotionEvent.ACTION_MOVE:

				int left = view.getLeft() + (int) (event.getRawX() - mOldX);
				int top = view.getTop() + (int) (event.getRawY() - mOldY);
				int right = view.getRight() + (int) (event.getRawX() - mOldX);
				int bottom = view.getBottom() + (int) (event.getRawY() - mOldY);

				if (left < mflMoveRegion.mRegionLeft)
				{
					left = mflMoveRegion.mRegionLeft;
					right = left + view.getWidth();
				}

				if (right > mflMoveRegion.mRegionRight)
				{
					right = mflMoveRegion.mRegionRight;
					left = right - view.getWidth();
				}

				if (top < mflMoveRegion.mRegionTop)
				{
					top = mflMoveRegion.mRegionTop;
					bottom = top + view.getHeight();
				}
				if (bottom > mflMoveRegion.mRegionBottom)
				{
					bottom = mflMoveRegion.mRegionBottom;
					top = bottom - view.getHeight();
				}

				if (mLeftTopResize)
				{
					if (view.getRight() - left < ProcessBitmapRegions.MIN_MOVE_REGION_SIZE)
						left = view.getLeft();
					if (view.getBottom() - top < ProcessBitmapRegions.MIN_MOVE_REGION_SIZE)
						top = view.getTop();
					view.layout(left, top, view.getRight(), view.getBottom());
				}
				else if (mLeftBottomResize)
				{
					if (view.getRight() - left < ProcessBitmapRegions.MIN_MOVE_REGION_SIZE)
						left = view.getLeft();

					if (bottom - view.getTop() < ProcessBitmapRegions.MIN_MOVE_REGION_SIZE)
						bottom = view.getBottom();
					view.layout(left, view.getTop(), view.getRight(), bottom);
				}
				else if (mRightTopResize)
				{

					if (view.getBottom() - top < ProcessBitmapRegions.MIN_MOVE_REGION_SIZE)
						top = view.getTop();
					if (right - view.getLeft() < ProcessBitmapRegions.MIN_MOVE_REGION_SIZE)
						right = view.getRight();

					view.layout(view.getLeft(), top, right, view.getBottom());
				}
				else if (mRightBottomResize)
				{

					if (right - view.getLeft() < ProcessBitmapRegions.MIN_MOVE_REGION_SIZE)
						right = view.getRight();
					if (bottom - view.getTop() < ProcessBitmapRegions.MIN_MOVE_REGION_SIZE)
						bottom = view.getBottom();
					view.layout(view.getLeft(), view.getTop(), right, bottom);
				}
				else
				{
					// 判断移动框是否越界
					view.layout(left, top, right, bottom);
					view.postInvalidate();
				}
				mOldX = event.getRawX();
				mOldY = event.getRawY();
				break;
			case MotionEvent.ACTION_UP:

				mflMoveRegion.mRectLineColor = ProcessBitmapRegions.RECT_LINE_COLOR_NORMAL;
				mflMoveRegion.mRectLineWidth = ProcessBitmapRegions.RECT_LINE_WIDTH_NORMAL;
				mflMoveRegion.mLeftTopRegionResourceId = R.drawable.left_top_normal;
				mflMoveRegion.mLeftBottomRegionResourceId = R.drawable.left_bottom_normal;
				mflMoveRegion.mRightTopRegionResourceId = R.drawable.right_top_normal;
				mflMoveRegion.mRightBottomRegionResourceId = R.drawable.right_bottom_normal;
				mflMoveRegion.invalidate();
				mLeftTopResize = false;
				mLeftBottomResize = false;
				mRightTopResize = false;
				mRightBottomResize = false;

				break;
		}

		// 一定要返回true，否则Move动作不会发生
		return true;
	}
}