package mobile.android.weibo.interfaces;

public interface Const
{
	String ROOT_PATH = android.os.Environment.getExternalStorageDirectory()
			.toString() + "/~~weibo~~";
	String PATH_FILE_CACHE = ROOT_PATH + "/cache";
	String PATH_STORAGE = ROOT_PATH + "/storage";
	String PATH_IMAGE = ROOT_PATH + "/images";
	String CONFIG_NAME = "config";

	int BITMAP_MAX_SIZE = 307200; // 300K

	int FACE_HOME = 101;
	int FACE_MESSAGE_AT = 120;   	// default
	int FACE_MESSAGE_COMMENT = 121;
	int FACE_MESSAGE_FAVORITE = 122;
	int FACE_SELFINFO = 130;
	int FACE_SQUARE = 140;
	int FACE_MORE = 150;
	int FACE_WEIBO_LIST = 151;
	int FACE_HOT_STATUSES = 152;
	int FACE_HOT_FAVORITIES = 153;

	// 用于提交微博界面的图像浏览
	int PICTURE_VIEWER_POST_WEIBO = 501;
	// 用于微博浏览界面的图像浏览
	int PICTURE_VIEWER_WEIBO_BROWSER = 502;
	// 提交微博
	int TYPE_POST_WEIBO = 601;
	// 转发微博
	int TYPE_FORWARD = 602;
	//  评论微博
	int TYPE_COMMENT = 603;

	int TYPE_HOME_TIMELINE = 1001;

	int DEFAULT_STATUS_COUNT = 30;

	int MONITOR_TYPE_IMAGE = 1501;
	int MONITOR_TYPE_TASK = 1502;

	int CODE_REQUEST_CAPTURE_IMAGE = 2001;
	int CODE_REQUEST_PICTURE_VIEWER = 2002;
	int CODE_RESULT_RETURN = 2101;
	int CODE_RESULT_REMOVE = 2102;
	int CODE_RESULT_SAVE = 2103;
	int CODE_RESULT_CANCEL = 2104;
	
	int HANDLER_TYPE_LOAD_PROFILE_IMAGE = 3001;

}
