package mobile.android.jx.ftp.client;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Templates;

import org.apache.commons.logging.Log;
import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class FtpClient extends Activity
{
	private FTPClient ftpClient;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public void onClick_ConnectFTP(View view)
	{
		try
		{
			ftpClient = new FTPClient();

		
			ftpClient.connect("192.168.17.100", 21);

			if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
			{
				
				boolean status = ftpClient.login("account", "password");
				if (status)
				{
					Toast.makeText(this, "登录成功.", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(this, "登录失败.", Toast.LENGTH_LONG).show();
				}
			}
		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}

	}

	public void onClick_CloseConnection(View view)
	{

		try
		{
			if (ftpClient != null)
			{
				ftpClient.logout();
				ftpClient.disconnect();
				ftpClient = null;
				Toast.makeText(this, "成功关闭FTP连接.", Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(this, "还没进行FTP连接呢.", Toast.LENGTH_LONG).show();
			}

		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}

	}

	public void onClick_CurrentWorkingDirectory(View view)
	{
		try
		{
			if (ftpClient == null)
			{
				Toast.makeText(this, "还没进行FTP连接呢.", Toast.LENGTH_LONG).show();
				return;
			}
			String workingDir = ftpClient.printWorkingDirectory();
			Toast.makeText(this, "当前工作目录：" + workingDir, Toast.LENGTH_LONG)
					.show();

		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}

	}

	// 显示当前目录中所有目录与文件
	public void onClick_ListAll(View view)
	{
		try
		{
			if (ftpClient == null)
			{
				Toast.makeText(this, "还没进行FTP连接呢.", Toast.LENGTH_LONG).show();
				return;
			}
			FTPFile[] ftpFiles = ftpClient.listFiles();
			int length = ftpFiles.length;
			String result = "";
			for (int i = 0; i < length; i++)
			{
				String name = new String(ftpFiles[i].getName().getBytes(
						"ISO-8859-1"), "utf-8");

				boolean isFile = ftpFiles[i].isFile();

				if (isFile)
				{
					result += "<" + name + ">(文件）   ";
				}
				else
				{
					result += "<" + name + ">(目录）   ";
				}
			}
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}

	}

	public void onClick_ChangeCurrentWorkingDirectory(View view)
	{
		try
		{

			if (ftpClient == null)
			{
				Toast.makeText(this, "还没进行FTP连接呢.", Toast.LENGTH_LONG).show();
				return;
			}
			ftpClient.changeWorkingDirectory("/test");
			Toast.makeText(this, "成功改变了当前工作目录.", Toast.LENGTH_LONG).show();
		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}

	}

	public void onClick_MakeDirectory(View view)
	{
		try
		{

			if (ftpClient == null)
			{
				Toast.makeText(this, "还没进行FTP连接呢.", Toast.LENGTH_LONG).show();
				return;
			}
			boolean status = ftpClient.makeDirectory("new_dir");
			if (status)
				Toast.makeText(this, "成功建立了目录.", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(this, "建立目录失败.", Toast.LENGTH_LONG).show();
		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		 
		 
	}

	public void onClick_RenameDirectory(View view)
	{
		try
		{

			if (ftpClient == null)
			{
				Toast.makeText(this, "还没进行FTP连接呢.", Toast.LENGTH_LONG).show();
				return;
			}
			boolean status = ftpClient.rename("new_dir", "rename_dir");
			Toast.makeText(this, "成功重命名了目录.", Toast.LENGTH_LONG).show();
		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}

	}

	public void onClick_RemoveDirectory(View view)
	{
		try
		{

			if (ftpClient == null)
			{
				Toast.makeText(this, "还没进行FTP连接呢.", Toast.LENGTH_LONG).show();
				return;
			}
			boolean status = ftpClient.removeDirectory("rename_dir");
			Toast.makeText(this, "成功删除了目录.", Toast.LENGTH_LONG).show();
		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}

	}

	public void onClick_UploadFile(View view)
	{
		try
		{

			if (ftpClient == null)
			{
				Toast.makeText(this, "还没进行FTP连接呢.", Toast.LENGTH_LONG).show();
				return;
			}
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			FileInputStream fis = new FileInputStream("/sdcard/obm.jpg");

			boolean status = ftpClient.storeFile("obm.jpg", fis);

			if (status)
			{
				Toast.makeText(this, "文件上传成功.", Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(this, "文件上传失败.", Toast.LENGTH_LONG).show();
			}
			fis.close();

		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}

	}

	public void onClick_RenameFile(View view)
	{
		try
		{

			if (ftpClient == null)
			{
				Toast.makeText(this, "还没进行FTP连接呢.", Toast.LENGTH_LONG).show();
				return;
			}

			boolean status = ftpClient.rename("obm.jpg",
					new String("奥巴马.jpg".getBytes("utf-8"), "ISO-8859-1"));

			if (status)
			{
				Toast.makeText(this, "重命名文件成功.", Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(this, "重命名文件失败.", Toast.LENGTH_LONG).show();
			}

		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}

	}

	public void onClick_DownloadFile(View view)
	{
		try
		{

			if (ftpClient == null)
			{
				Toast.makeText(this, "还没进行FTP连接呢.", Toast.LENGTH_LONG).show();
				return;
			}
			FileOutputStream fos = new FileOutputStream("/sdcard/奥巴马.jpg");
			boolean status = ftpClient.retrieveFile(
					new String("奥巴马.jpg".getBytes("utf-8"), "ISO-8859-1"), fos);

			if (status)
			{
				Toast.makeText(this, "下载文件成功.", Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(this, "下载文件失败.", Toast.LENGTH_LONG).show();
			}
			fos.close();
		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}

	}

	public void onClick_DeleteFile(View view)
	{
		try
		{

			if (ftpClient == null)
			{
				Toast.makeText(this, "还没进行FTP连接呢.", Toast.LENGTH_LONG).show();
				return;
			}
			boolean status = ftpClient.deleteFile(new String("奥巴马.jpg"
					.getBytes("utf-8"), "ISO-8859-1"));

			if (status)
			{
				Toast.makeText(this, "删除文件成功.", Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(this, "删除文件失败.", Toast.LENGTH_LONG).show();
			}

		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}

	}
}