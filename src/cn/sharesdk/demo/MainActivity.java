package cn.sharesdk.demo;


import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


/**ShareSDK 官网地址 ： http://www.sharesdk.cn </br>
/1、这是用2.11版本的sharesdk，一定注意  </br>
/2、如果要咨询客服，请加企业QQ 4006852216 </br>
/3、咨询客服时，请把问题描述清楚，最好附带错误信息截图 </br>
/4、一般问题，集成文档中都有，请先看看集成文档；减少客服压力，多谢合作  ^_^
*/
public class MainActivity extends Activity implements OnClickListener,PlatformActionListener,Callback{

	private static final int MSG_TOAST = 1;
	private static final int MSG_ACTION_CCALLBACK = 2;
	private static final int MSG_CANCEL_NOTIFY = 3;

	private static final String FILE_NAME = "/share_pic.jpg";
	public static String TEST_IMAGE;

	private CheckedTextView ctvStWm;
	
	/**ShareSDK集成方法有两种</br>
	 * 1、第一种是引用方式，例如引用onekeyshare项目，onekeyshare项目在引用mainlibs库</br>
	 * 2、第二种是把onekeyshare和mainlibs集成到项目中，本例子就是用第二种方式</br>
	 * 请看“ShareSDK 使用说明文档”，SDK下载目录中 </br>
	 * 或者看网络集成文档 http://wiki.sharesdk.cn/Android_%E5%BF%AB%E9%80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
	 * 3、混淆时，把sample或者本例子的混淆代码copy过去，在proguard-project.txt文件中
	 * 
	 * 
	 * 平台配置信息有三种方式：
	 * 1、在我们后台配置各个微博平台的key
	 * 2、在代码中配置各个微博平台的key，http://sharesdk.cn/androidDoc/cn/sharesdk/framework/ShareSDK.html
	 * 3、在配置文件中配置，本例子里面的assets/ShareSDK.conf	 
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//初始化ShareSDK
		ShareSDK.initSDK(this);		
	
		/**微信分享要注意的问题</br>
		 * 
		 * 1、sample或者本例子测试微信，要打包
		 *    keystore在sample项目中，密码123456
		 *    
		 *    不会用keystore打包应用，可以参考
		 *    http://www.cnblogs.com/timeng/archive/2012/02/17/2355513.html
		 * 
		 * 2、微信集成有点特别，请看看这篇文章
		 * 	  http://bbs.sharesdk.cn/thread-92-1-1.html
		 * 
		 * 3、微信分享不了，有下面原因，请好好检查   
		 * 	  a、测试没有打包
         *    b、打包的keystore跟微信开放平台上面的不一致 ，导致MD5码，不一致
         *    c、分享参数错误
         *    d、应用没有审核通过
         *    e、包名跟开放平台上面的不一致
         *   
         * 4、第3点中，b小点的详细信息是：
         * 
         *    你用你的keystore去打包一个应用A
		 *	     然后用微信提供的工具获取应用A的签名
		 *	     然后把应用A的签名填写到微信注册上
         *    测试你现在的应用时，就用keystore打包就行
         *    
         *    keystore的生成可以参考http://lufengdie.iteye.com/blog/814660   
         *    不懂打包apk的可以参考：http://www.cnblogs.com/timeng/archive/2012/02/17/2355513.html
         *    
         * 5、微信监听回调
		 * 
		 *    监听接口跟原来一样，也是用PlatformActionListener
		 *    但是需要添加文件"你项目的包名.wxapi/WXEntryActivity"
		 * 	      例如本例子中的cn.sharesdk.demo.wxapi/WXEntryActivity
		 *    还要在AndroidManifest中注册这个Activity
		 * 
		 *    如果没有添加这个文件，监听不到；微信就是这么麻烦的
		 * 
		 *    注意：项目包名到AndroidManifest查看package
		 *    集成到你的项目中，要改成 ："你项目的包名.wxapi/WXEntryActivity"
		 *    然后在AndroidManifest中注册，否则找不到WXEntryActivity
         *    
         * 6、混淆时，把sample或者本例子的混淆代码copy过去，在proguard-project.txt文件中
         * 
		 * 7、一般问题，文档中都有写，请查看；也可以网络文档http://wiki.sharesdk.cn/Android_%E5%BF%AB%E9%80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
		 *   跟客服联系的话，不要磨磨唧唧的，直接说问题，最好有错误信息截图，特别讨厌：在吗？在？ 这太浪费时间啦  ^_^
		 */		
		Button button1 =(Button) findViewById(R.id.button1);
		button1.setText("微信快捷分享，直接打开微信客户端");
		button1.setOnClickListener(this);
		
		ctvStWm = (CheckedTextView) findViewById(R.id.ctvStWm);
		ViewGroup vp = (ViewGroup) ctvStWm.getParent();
		for (int i = 0, size = vp.getChildCount(); i < size; i++) {
			vp.getChildAt(i).setOnClickListener(this);
		}
		
		//初始化本地图片，把图片从drawable复制到sdcard中
		new Thread() {
			public void run() {
				initImagePath();
			}
		}.start();
	}

	//把图片从drawable复制到sdcard中
	private void initImagePath() {
		try {
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
					&& Environment.getExternalStorageDirectory().exists()) {
				TEST_IMAGE = Environment.getExternalStorageDirectory().getAbsolutePath() + FILE_NAME;
			}
			else {
				TEST_IMAGE = getApplication().getFilesDir().getAbsolutePath() + FILE_NAME;
			}
			File file = new File(TEST_IMAGE);
			if (!file.exists()) {
				file.createNewFile();
				Bitmap pic = BitmapFactory.decodeResource(getResources(), R.drawable.pic);
				FileOutputStream fos = new FileOutputStream(file);
				pic.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch(Throwable t) {
			t.printStackTrace();
			TEST_IMAGE = null;
		}
	}

	@Override
	public void onClick(View v) {
		
		if(v.getId() == R.id.button1){
			//快捷分享，没有九宫格，只有编辑页
			OnekeyShare oks = new OnekeyShare();			
			// 分享时Notification的图标和文字
			oks.setNotification(R.drawable.ic_launcher, "Gtpass");			
			//设置默认微博平台，例如腾讯微博的，可以是TencentWeibo.NAME
			oks.setPlatform(Wechat.NAME);
			//分享纯文本
			//微信分享必须要有text和title这两个参数
			//不同的分享类型，分享参数不一样,可以参考sample中wechatpage这个类
			//参数文档：http://sharesdk.cn/androidDoc/index.html?cn/sharesdk/framework/Platform.html
			oks.setText("ShareSDK测试能否分享 text");
			oks.setTitle("ShareSDK测试能否分享 title");			
			oks.setSilent(false);
			oks.show(MainActivity.this);	
			
		}else if (v.equals(ctvStWm)) {
			//微信朋友圈
			ctvStWm.setChecked(!ctvStWm.isChecked());
			findViewById(R.id.btnApp).setVisibility(ctvStWm.isChecked() ? View.GONE : View.VISIBLE);
			findViewById(R.id.btnAppExt).setVisibility(ctvStWm.isChecked() ? View.GONE : View.VISIBLE);
			findViewById(R.id.btnFile).setVisibility(ctvStWm.isChecked() ? View.GONE : View.VISIBLE);
			return;
			
		}else{
			//微信好友
			String name = ctvStWm.isChecked() ? WechatMoments.NAME : Wechat.NAME;
			Platform plat = ShareSDK.getPlatform(MainActivity.this, name);
			plat.setPlatformActionListener(this);
			ShareParams sp = ctvStWm.isChecked() ? getWechatMomentsShareParams(v) : getWechatShareParams(v);
			plat.share(sp);
			
		}
	}

	/** 
	 * 微信监听：监听接口跟原来一样，也是用PlatformActionListener
	 * 但是需要添加文件"你项目的包名.wxapi/WXEntryActivity"
	 * 例如本例子中的cn.sharesdk.demo.wxapi/WXEntryActivity
	 * 还要在AndroidManifest中注册这个Activity
	 * 
	 * 如果没有添加这个文件，监听不到；微信就是这么麻烦的
	 * 
	 * 注意：项目包名到AndroidManifest查看package
	 * 集成到你的项目中，要改成 ："你项目的包名.wxapi/WXEntryActivity"
	 * 然后在AndroidManifest中注册，否则找不到WXEntryActivity
	 * */
	//微信好友分享参数
	private ShareParams getWechatShareParams(View v) {
		Wechat.ShareParams sp = new Wechat.ShareParams();
		//任何分享类型都需要title和text
		sp.title = "ShareSDK 微信好友测试 title";
		sp.text = "ShareSDK 微信好友测试 text";
		sp.shareType = Platform.SHARE_TEXT;
		switch (v.getId()) {
			case R.id.btnUpload: {//分享sdcard中的图片
				sp.shareType = Platform.SHARE_IMAGE;
				sp.imagePath = MainActivity.TEST_IMAGE;
			}
			break;
			case R.id.btnUploadBm: {//分享drawable中的图片
				sp.shareType = Platform.SHARE_IMAGE;
				sp.imageData = BitmapFactory.decodeResource(v.getResources(), R.drawable.ic_launcher);
			}
			break;
			case R.id.btnUploadUrl: {//分享网络图片
				sp.shareType = Platform.SHARE_IMAGE;
				sp.imageUrl = "http://img.appgo.cn/imgs/sharesdk/content/2013/07/16/1373959974649.png";
			}
			break;
			case R.id.btnMusic: {//分享网络音乐
				sp.shareType = Platform.SHARE_MUSIC;
				sp.musicUrl = "http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3";
				sp.url = "http://sharesdk.cn";
				sp.imagePath = MainActivity.TEST_IMAGE;
			}
			break;
			case R.id.btnVideo: {//分享网络视频
				sp.shareType = Platform.SHARE_VIDEO;
				sp.url = "http://t.cn/zT7cZAo";
				sp.imagePath = MainActivity.TEST_IMAGE;
			}
			break;
			case R.id.btnWebpage: {//图文分享，网页形式，sdcard中本地图片
				sp.shareType = Platform.SHARE_WEBPAGE;
				sp.url = "http://t.cn/zT7cZAo";
				sp.imagePath = MainActivity.TEST_IMAGE;
			}
			break;
			case R.id.btnWebpageBm: {//图文分享，网页形式，drawable中的图片
				sp.shareType = Platform.SHARE_WEBPAGE;
				sp.url = "http://t.cn/zT7cZAo";
				sp.imageData = BitmapFactory.decodeResource(v.getResources(), R.drawable.ic_launcher);
			}
			break;
			case R.id.btnWebpageUrl: {//图文分享，网页形式，网络图片
				sp.shareType = Platform.SHARE_WEBPAGE;
				sp.url = "http://t.cn/zT7cZAo";
				sp.imageUrl = "http://img.appgo.cn/imgs/sharesdk/content/2013/07/16/1373959974649.png";
			}
			break;
			case R.id.btnApp: {//分享app
				sp.shareType = Platform.SHARE_APPS;
				// 待分享app的本地地址
				sp.filePath = MainActivity.TEST_IMAGE;
				sp.extInfo = "Share SDK received an app message from wechat client";
				sp.imagePath = MainActivity.TEST_IMAGE;
			}
			break;
			case R.id.btnAppExt: {
				//微信中点击事件，监听在cn.sharesdk.demo.wxapi/WXEntryActivity类中做监听
				sp.shareType = Platform.SHARE_APPS;
				// 供微信回调的第三方信息（或者自定义脚本）
				sp.extInfo = "Share SDK received an app message from wechat client";
				sp.imagePath = MainActivity.TEST_IMAGE;
			}
			break;
			case R.id.btnFile: {//分享本地文件，sdcard中的图片
				sp.shareType = Platform.SHARE_FILE;
				// 待分享文件的本地地址
				sp.filePath = MainActivity.TEST_IMAGE;
				sp.imagePath = MainActivity.TEST_IMAGE;
			}
		}
		return sp;
	}
	
	/**微信朋友圈分享参数*/
	private ShareParams getWechatMomentsShareParams(View v) {
		WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
		//任何分享类型都需要title和text
		sp.title = "ShareSDK 微信朋友圈测试 title";
		sp.text = "ShareSDK 微信朋友圈测试 text";
		sp.shareType = Platform.SHARE_TEXT;
		switch (v.getId()) {
			case R.id.btnUpload: {//分享sdcard中的图片
				sp.shareType = Platform.SHARE_IMAGE;
				sp.imagePath = MainActivity.TEST_IMAGE;
			}
			break;
			case R.id.btnUploadBm: {//分享drawable中的图片
				sp.shareType = Platform.SHARE_IMAGE;
				sp.imageData = BitmapFactory.decodeResource(v.getResources(), R.drawable.ic_launcher);
			}
			break;
			case R.id.btnUploadUrl: {//分享网络图片
				sp.shareType = Platform.SHARE_IMAGE;
				sp.imageUrl = "http://img.appgo.cn/imgs/sharesdk/content/2013/07/16/1373959974649.png";
			}
			break;
			case R.id.btnMusic: {//分享网络音乐
				sp.shareType = Platform.SHARE_MUSIC;
				sp.musicUrl = "http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3";
				sp.url = "http://sharesdk.cn";
				sp.imagePath = MainActivity.TEST_IMAGE;
			}
			break;
			case R.id.btnVideo: {//分享网络视频
				sp.shareType = Platform.SHARE_VIDEO;
				sp.url = "http://t.cn/zT7cZAo";
				sp.imagePath = MainActivity.TEST_IMAGE;
			}
			break;
			case R.id.btnWebpage: {//图文分享，网页形式，sdcard中的图片
				sp.shareType = Platform.SHARE_WEBPAGE;
				sp.url = "http://t.cn/zT7cZAo";
				sp.imagePath = MainActivity.TEST_IMAGE;
			}
			break;
			case R.id.btnWebpageBm: {//图文分享，网页形式，drawable中的图片
				sp.shareType = Platform.SHARE_WEBPAGE;
				sp.url = "http://t.cn/zT7cZAo";
				sp.imageData = BitmapFactory.decodeResource(v.getResources(), R.drawable.ic_launcher);
			}
			break;
			case R.id.btnWebpageUrl: {//图文分享，网页形式，网页图片
				sp.shareType = Platform.SHARE_WEBPAGE;
				sp.url = "http://t.cn/zT7cZAo";
				sp.imageUrl = "http://img.appgo.cn/imgs/sharesdk/content/2013/07/16/1373959974649.png";
			}
			break;
		}
		return sp;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ShareSDK.stopSDK(this);
	}

	//设置监听http://sharesdk.cn/androidDoc/cn/sharesdk/framework/PlatformActionListener.html
	//监听是子线程，不能Toast，要用handler处理，不要犯这么二的错误
	@Override
	public void onCancel(Platform platform, int action) {
		//取消监听
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onComplete(Platform platform, int action, HashMap<String, Object> arg2) {
		//成功监听
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onError(Platform platform, int action, Throwable t) {
		//打印错误信息
		t.printStackTrace();
		//错误监听
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = t;
		UIHandler.sendMessage(msg, this);		
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch(msg.what) {
		case MSG_TOAST: {
			String text = String.valueOf(msg.obj);
			Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
		}
		break;
		case MSG_ACTION_CCALLBACK: {
			switch (msg.arg1) {
				case 1: { // 成功提示
					showNotification(2000, getString(R.string.share_completed));
				}
				break;
				case 2: { // 失败提示
					String expName = msg.obj.getClass().getSimpleName();
					if ("WechatClientNotExistException".equals(expName)
							|| "WechatTimelineNotSupportedException".equals(expName)) {
						showNotification(2000, getString(R.string.wechat_client_inavailable));
					}
					else if ("GooglePlusClientNotExistException".equals(expName)) {
						showNotification(2000, getString(R.string.google_plus_client_inavailable));
					}
					else if ("QQClientNotExistException".equals(expName)) {
						showNotification(2000, getString(R.string.qq_client_inavailable));
					}
					else {
						showNotification(2000, getString(R.string.share_failed));
					}
				}
				break;
				case 3: { // 取消提示
					showNotification(2000, getString(R.string.share_canceled));
				}
				break;
			}
		}
		break;
		case MSG_CANCEL_NOTIFY: {
			NotificationManager nm = (NotificationManager) msg.obj;
			if (nm != null) {
				nm.cancel(msg.arg1);
			}
		}
		break;
	}
	return false;
	}

	// 在状态栏提示分享操作
	private void showNotification(long cancelTime, String text) {
		try {
			Context app = getApplicationContext();
			NotificationManager nm = (NotificationManager) app
					.getSystemService(Context.NOTIFICATION_SERVICE);
			final int id = Integer.MAX_VALUE / 13 + 1;
			nm.cancel(id);

			long when = System.currentTimeMillis();
			Notification notification = new Notification(R.drawable.ic_launcher, text, when);
			PendingIntent pi = PendingIntent.getActivity(app, 0, new Intent(), 0);
			notification.setLatestEventInfo(app, "sharesdk test", text, pi);
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			nm.notify(id, notification);

			if (cancelTime > 0) {
				Message msg = new Message();
				msg.what = MSG_CANCEL_NOTIFY;
				msg.obj = nm;
				msg.arg1 = id;
				UIHandler.sendMessageDelayed(msg, cancelTime, this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
