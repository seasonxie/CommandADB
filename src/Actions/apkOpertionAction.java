package Actions;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;

import Utils.HttpUtil;
import Utils.ShellHandle;
import Utils.ShellUtils;
import Utils.deviceCheck;
import Views.Constants;
import Views.Main;

public class apkOpertionAction {
	
	private static String getSDKLocation() {
        String pathValue = System.getenv("Path");
        if (pathValue == null) {
            return null;
        }
        String[] vals = pathValue.split(";");
        for (int i = 0; i < vals.length; i++) {
            File f1 = new File(vals[i] + File.separator + "adb");
            File f2 = new File(vals[i] + File.separator + "adb.exe");
            if (f1.exists() || f2.exists()) {
                return vals[i].replaceAll("platform-tools", "");
            }
        }
        return null;
    }
	
	
	public static String KillPid(String command) {
		String pid = "";
		String ss = ShellUtils.execCommand(command, false).successMsg;
		for (String s : ss.split("\\n")) {
			System.out.println(s);
			if (s.startsWith("shell")&&s.contains(command.replace("ps | grep ", ""))) {
				
				pid = s.substring(9, 15).trim();
				System.out.println("--------  "+pid);
				//ShellUtils.execCommand("kill "+pid,false);
			}
		}
		return pid;
	}

	
	public static void ActionHandle(ActionEvent evt) {
		try {
			if (evt.getActionCommand().equals(Constants.CLEAR)) {
				/*ShellHandle.exec("adb shell pm clear " + Main.app + "");
				Main.fill("adb shell am start -W " + Main.app + "/" + Main.activity + "");
				ShellHandle.exec("adb shell am start -W " + Main.app + "/" + Main.activity + "");
				Main.fill("重启浏览器完成");
				Main.newline();*/
				
				KillPid("ps | grep com.android.commands.monkey");
				KillPid("ps | grep logcat");
				KillPid("ps | grep screen");
				
			/*	Long t=System.currentTimeMillis();
				ShellHandle.exec("adb shell am dumpheap com.android.browser /sdcard/"+t+".hprof");
				Thread.sleep(10000);
				ShellHandle.exec("adb pull /sdcard/"+t+".hprof d://");
				String a= getSDKLocation()  + "platform-tools" + File.separator + "hprof-conv";
				String replaceName="replace"+t;
                ShellHandle.exec(a+" d://"+t+".hprof"+" d://"+replaceName+".hprof");   */
                 

			} else if (evt.getActionCommand().equals(Constants.INSTALL)) {
				JOptionPane.showMessageDialog(null, "直接把应用拖进来就可以了亲，给好评哦");

			} else if (evt.getActionCommand().equals(Constants.UNINSTALL)) {
				// showP();
				String commands = "pm uninstall -k " + Main.app + "";
				ShellUtils.execCommand(commands, false);
				Main.fill("当前版本：  " + ShellHandle.getAppVersion());
				// dialog.setVisible(false);
				Main.newline();

			} else if (evt.getActionCommand().equals(Constants.PUSHLOCAL)) {
				String appVersion = ShellHandle.getAppVersion();
				String url;
				String commands;
				if (Main.app.contains("browser")) {
					url = JOptionPane.showInputDialog("请输入推送的url:");
					if(!url.isEmpty()){
					if (ShellHandle.pushCheck(appVersion.trim())) {// 4.2以上版本
						commands = "am broadcast -a com.meizu.flyme.push.intent.MESSAGE -p \"" + Main.app
								+ "\" --es message \"{'tp':1,'fb': {'c': 'test','u': '" + url + "','t': 'test'}}\"";
					} else {
						commands = "am broadcast -a com.meizu.flyme.push.intent.MESSAGE -p \""
								+ Main.app
								+ "\" --es message \"{'notice':{'content':'content','id':174,'layout':'ACTIVITY_DETAIL','subject':'subject','url':'"
								+ url + "'}}\"";
					}
					ShellUtils.execCommand(commands, false);
					ShellHandle.exec("adb shell input swipe 500 0 500 1600");
					}
				} else {
					String json = JOptionPane.showInputDialog("请输入推送的json:");
					if(!json.isEmpty()){
					String commandOtherApp = "am broadcast -a com.meizu.flyme.push.intent.MESSAGE -p \"" + Main.app
							+ "\" --es message \"" + json + "\"";
					ShellUtils.execCommand(commandOtherApp, false);
					ShellHandle.exec("adb shell input swipe 500 0 500 1600");
					}
				}
			}else if (evt.getActionCommand().equals(Constants.RUNUIAUTO)) {
				Main.fill("-----  执行ui测试开始  -----");
				String command="adb shell uiautomator runtest browser.jar --nohup -c com.meizu.browser.test.SanityTestRun";
				HttpUtil.downLoadFile("http://tm.meizu.com/tms/TestResource/browser.jar",Constants.CURRENTPATH+"\\browser.jar");
				ShellHandle.execLine("adb push "+Constants.CURRENTPATH+"\\browser.jar /data/local/tmp");
				Main.fill("注意：  必须连接外网wifi");
				Main.fill("Log日志、截图存放   / sdcard / ${R_当前时间戳) /  ");
				Main.fill("开始执行,请等待3s  "+command);				
				Thread.sleep(1000);
		
				Main.newline();
			}
		} catch (Exception e) {
			// TODO: handle exception
			Main.fill("----------  有异常,记下log  ----------");
			Main.fill(e + "");
		}

	}

	public static void drag()// 定义的拖拽方法
	{
		
		
		// panel表示要接受拖拽的控件
		new DropTarget(Main.txtIng, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
			@Override
			public void drop(DropTargetDropEvent dtde)// 重写适配器的drop方法
			{
				try {
					if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))// 如果拖入的文件格式受支持
					{
						dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);// 接收拖拽来的数据
						if (deviceCheck.conect == false) {
							Main.fill("adb未连接");
							return;
						}
						List<File> list = (List<File>) (dtde.getTransferable()
								.getTransferData(DataFlavor.javaFileListFlavor));
						String temp = "";
						for (File file : list)
							temp += file.getAbsolutePath() + ";\n";
						if (temp.contains(".apk")) {
							JOptionPane.showMessageDialog(null, "\n将会安装  " + temp + "  请稍等 【20S】");							
							dtde.dropComplete(true);// 指示拖拽操作已完成							
							Thread.sleep(100);
							System.out.println("adb install -r " + temp.replace(";", "") + "");						
							//ThreadManger.runShell("adb install -r " + temp.replace(";", "") + "");							 
							ShellHandle.exec("adb install -r " + temp.replace(";", "") + "");
							Main.fill("已安装完毕");
							Main.fill("【当前版本】：  " + ShellHandle.getAppVersion());
						} else if (temp.contains(".jar")) {
							JOptionPane.showMessageDialog(null, "\n运行jar  " + temp + "  请稍等");
							ShellHandle.exec("adb push " + temp.replace(";", "") + " /sdcard/");

							dtde.dropComplete(true);// 指示拖拽操作已完成

						} else {
							JOptionPane.showMessageDialog(null, "\n无操作指令for  " + temp);
							dtde.dropComplete(true);// 指示拖拽操作已完成
						}
					} else {
						dtde.rejectDrop();// 否则拒绝拖拽来的数据
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
