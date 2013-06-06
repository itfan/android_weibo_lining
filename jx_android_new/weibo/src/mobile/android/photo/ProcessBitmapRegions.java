package mobile.android.photo;

import android.graphics.Color;

public class ProcessBitmapRegions
{ 

	public static final String PROCESS_TYPE_GRAY = "gray";	
	public static final String PROCESS_TYPE_MOSAIC = "mosaic";
	public static final String PROCESS_TYPE_CROP = "crop";
	public static final String PROCESS_TYPE_ROTATE = "rotate";
	
	public static String processType;
	
	
	public static final int RECT_LINE_WIDTH_NORMAL = 1;
	public static final int RECT_LINE_WIDTH_FOCUSED = 2;
	public static final int RECT_LINE_COLOR_NORMAL = Color.WHITE;
	public static final int RECT_LINE_COLOR_FOCUSED = Color.BLUE;
	
	public static final int MIN_MOVE_REGION_SIZE = 70;
	public static final int RESIZE_REGION_SIZE = 30;
	
	public static final int MOSAIC_SINGLE_REGION_SIZE = 15;
	
	public static boolean isWorking = false;

	private PhotoProcess mPhotoProcess;
	public static AllThreadEnd mAllThreadEnd;

	public ProcessBitmapRegions(PhotoProcess photoProcess)
	{
		mPhotoProcess = photoProcess;
	}



	public void work()
	{
		Thread thread = new Thread(new PhotoThread(mPhotoProcess));
		thread.start();

	}
}
