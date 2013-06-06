package mobile.android.photo;

import mobile.android.weibo.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class FrameLayoutExt extends FrameLayout
{
	public Bitmap mBitmap;

	public ImageView mivDrawing; 
	public float mScale;
	public int mRegionTop;
	public int mRegionBottom;
	public int mRegionLeft;
	public int mRegionRight;

	public int mRectLineWidth = 1;
	public int mRectLineColor = Color.WHITE;

	
	
	
	private int mResizeLength = 40;

	private Bitmap mLeftTopRegion;
	private Bitmap mLeftBottomRegion;
	private Bitmap mRightTopRegion;
	private Bitmap mRightBottomRegion;

	public int mLeftTopRegionResourceId = R.drawable.left_top_normal;
	public int mLeftBottomRegionResourceId = R.drawable.left_bottom_normal;
	public int mRightTopRegionResourceId = R.drawable.right_top_normal;
	public int mRightBottomRegionResourceId = R.drawable.right_bottom_normal;
	private int mWidth;
	private int mHeight;

	private Context mContext;   

	public FrameLayoutExt(Context context, AttributeSet attrs)
	{  
		super(context, attrs); 
		mContext = context;
	 
	} 

	public void init()
	{
		int screenWidth = mivDrawing.getWidth();
		int screenHeight = mivDrawing.getHeight();
		int sourceWidth = mBitmap.getWidth();
		int sourceHeight = mBitmap.getHeight();

		if (sourceWidth * screenHeight > sourceHeight * screenWidth)
		{

			int insideHeight = sourceHeight * screenWidth / sourceWidth;
			mRegionTop = (screenHeight - insideHeight) / 2;
			mRegionLeft = 0;
			mRegionRight = mivDrawing.getMeasuredWidth();
			mRegionBottom = mRegionTop + insideHeight + 1;

			mScale = (float) sourceWidth / (float) screenWidth;
		}
		else
		{
			int insideWidth = sourceWidth * screenHeight / sourceHeight;
			mRegionLeft = (screenWidth - insideWidth) / 2;
			mRegionTop = 0;

			mRegionRight = mRegionLeft + insideWidth + 1;
			mRegionBottom = mivDrawing.getMeasuredHeight();
			mScale = (float) sourceHeight / (float) screenHeight;
		}
		mLeftTopRegion = BitmapFactory.decodeResource(mContext.getResources(),
				mLeftTopRegionResourceId);
		mLeftBottomRegion = BitmapFactory.decodeResource(mContext
				.getResources(), mLeftBottomRegionResourceId);
		mRightTopRegion = BitmapFactory.decodeResource(mContext.getResources(),
				mRightTopRegionResourceId);
		mRightBottomRegion = BitmapFactory.decodeResource(mContext
				.getResources(), mRightBottomRegionResourceId);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		init();
		canvas.drawBitmap(mBitmap, new Rect(
				(int) ((getLeft() - mRegionLeft) * mScale),
				(int) ((getTop() - mRegionTop) * mScale)+2,
				(int) ((getRight() - mRegionLeft) * mScale),
				(int) ((getBottom() - mRegionTop) * mScale)), new Rect(0, 0,
				mWidth, mHeight), null);
		Paint paint = new Paint();
		paint.setColor(mRectLineColor);
		paint.setStrokeWidth(mRectLineWidth);
		paint.setStyle(Style.STROKE);
		canvas.drawRect(new Rect(1, 1, mWidth - 2, mHeight - 2), paint);
		canvas.drawBitmap(mLeftTopRegion, null, new Rect(0, 0, mLeftTopRegion
				.getWidth(), mLeftTopRegion.getHeight()), null);

		canvas.drawBitmap(mLeftBottomRegion, null, new Rect(0, mHeight
				- mLeftBottomRegion.getHeight(), mLeftBottomRegion.getWidth(),
				mHeight), null);

		canvas.drawBitmap(mRightTopRegion, null, new Rect(mWidth
				- mLeftBottomRegion.getWidth(), 0, mWidth, mRightTopRegion
				.getHeight()), null);
		canvas.drawBitmap(mRightBottomRegion, null, new Rect(mWidth
				- mRightBottomRegion.getWidth(), mHeight
				- mRightBottomRegion.getHeight(), mWidth, mHeight), null);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		mWidth = w;
		mHeight = h;
		super.onSizeChanged(w, h, oldw, oldh);
	}

}
