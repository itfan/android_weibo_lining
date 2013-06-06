package mobile.android.jx.gtalk;

import java.util.Collection;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.Contacts;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class GTalk extends Activity
{
	private XMPPConnection xmppConnection;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			Toast.makeText(GTalk.this, String.valueOf(msg.obj),
					Toast.LENGTH_LONG).show();
			super.handleMessage(msg);
		}

	};

	public void onClick_Login(View view)
	{
		try
		{
			ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration(
					"talk.google.com", 5222, "gmail.com");
			xmppConnection = new XMPPConnection(connectionConfiguration);
			xmppConnection.connect();
			xmppConnection.login("account", "password");
			Presence presence = new Presence(Presence.Type.available);
			xmppConnection.sendPacket(presence);

			xmppConnection.addPacketListener(new PacketListener()
			{

				@Override
				public void processPacket(Packet packet)
				{
					String account = packet.getFrom().substring(0,
							packet.getFrom().indexOf("/"));
					Message message = new Message();
					if (packet.toString().startsWith("available"))
					{
						message.obj = account + "在线.";
						// handler.sendMessage(message);
					}
					else if (packet.toString().startsWith("unavailable"))
					{
						message.obj = account + "离线.";
						// handler.sendMessage(message);

					}

				}
			}, new PacketFilter()
			{

				@Override
				public boolean accept(Packet packet)
				{
					// TODO Auto-generated method stub
					return true;
				}
			});

			Toast.makeText(this, "登录成功.", Toast.LENGTH_LONG).show();
		}
		catch (Exception e)
		{
			Toast.makeText(this, "登录失败.", Toast.LENGTH_LONG).show();
		}

	}

	public void onClick_ContactList(View view)
	{
		if (xmppConnection == null)
		{
			Toast.makeText(this, "请先登录GTalk.", Toast.LENGTH_LONG).show();
			return;
		}
		Collection<RosterEntry> rosterEntries = xmppConnection.getRoster()
				.getEntries();
		StringBuilder contacts = new StringBuilder();
		for (RosterEntry rosterEntry : rosterEntries)
		{
			contacts.append(rosterEntry.getName() + ":" + rosterEntry.getUser()
					+ "\n");
		}
		new AlertDialog.Builder(this).setMessage(contacts.toString())
				.setPositiveButton("关闭", null).show();

	}

	public void onClick_SendChatMessage(View view)
	{
		if (xmppConnection == null)
		{
			Toast.makeText(this, "请先登录GTalk.", Toast.LENGTH_LONG).show();
			return;
		}

		try
		{
			
			
			ChatManager chatManager = xmppConnection.getChatManager();
			Chat chat = chatManager.createChat("account", null);
			chat.sendMessage("你好吗，我是Bill Gates.");
			Toast.makeText(this, "已发送聊天消息.", Toast.LENGTH_LONG).show();
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}

	public void onClick_ChatMessageListener(View view)
	{
		
		

		PacketListener packetListener = new PacketListener()
		{
			public void processPacket(Packet packet)
			{
				Message message = new Message();
				
				message.obj = packet.getFrom() + ":" + ((org.jivesoftware.smack.packet.Message)packet).getBody();
				handler.sendMessage(message);
				
			}
		};
		
		xmppConnection.addPacketListener(packetListener,new PacketFilter()
		{
			
			@Override
			public boolean accept(Packet packet)
			{
				// TODO Auto-generated method stub
				return true;
			}
		});

	}
}