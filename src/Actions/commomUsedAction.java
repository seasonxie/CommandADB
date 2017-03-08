package Actions;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import Utils.HttpUtil;
import Utils.ShellHandle;
import Utils.ShellUtils;
import Utils.ShellUtils.CommandResult;
import Utils.checkLog;
import Views.Constants;
import Views.Main;

public class commomUsedAction {
	public static void ActionHandle(ActionEvent evt) {
		try {
			// 执行对应的操作
			if (evt.getActionCommand().equals(Constants.SCREEN)) {
				String name = ShellHandle.getTime();
				ShellHandle.exec("adb shell /system/bin/screencap -p /sdcard/" + name + ".png");
				Main.sleep(300);
				ShellHandle.exec("adb pull /sdcard/" + name + ".png " + Constants.CURRENTPATH + "");
				Main.fill("图片拿出来了，在当前目录（" + Constants.CURRENTPATH + "）看看拉  " + name + ".png");
				Main.newline();
				// ShellHandle.execLine("explorer "+Constants.CURRENTPATH);

			} else if (evt.getActionCommand().equals(Constants.PROP)) {
				String imei = ShellHandle.getProp("ril.gsm.imei");
				String flyme = ShellHandle.getProp("ro.build.display.id");
				String flyme1 = ShellHandle.getProp("ro.build.inside.id");
				String device = ShellHandle.getProp("ro.meizu.product.model");
				String device1 = ShellHandle.getProp("ro.product.model");
				String appVersion = ShellHandle.getAppVersion();

				Main.fill("--------------获取手机信息---------------");
				if (imei.length() > 4 && imei != null && !imei.contains("adb")) {
					Main.fill("Imei:  " + imei);
				} else {
					Main.fill("Imei:  adb无法获取");
				}
				Main.fill("固件名:  " + flyme);
				Main.fill("固件号:  " + flyme1);
				Main.fill("对内型号:  " + device);
				Main.fill("对外型号:  " + device1);
				Main.fill("应用版本：  " + appVersion);
				Main.fill("PC--IP： " + ShellHandle.getIp());
				Main.newline();

			} else if (evt.getActionCommand().equals(Constants.RECORD)) {
				String name = ShellHandle.getTime();
				String str;
				str = JOptionPane.showInputDialog("请输入录制秒数:").trim();
				System.out.println(str);
				try {
					Integer.valueOf(str);
					JOptionPane.showMessageDialog(null, "点击确定开始录制  -- 时间：" + str + "s");
					ShellHandle.exec("adb shell screenrecord  --time-limit " + str + " /sdcard/" + name + ".mp4");
					ShellHandle.exec("adb pull /sdcard/" + name + ".mp4 " + Constants.CURRENTPATH + "");
					Main.fill("视频拿出来了，在当前目录（" + Constants.CURRENTPATH + "）看看拉  " + name + ".mp4");
					Main.newline();
				} catch (Exception e) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "已经没办法帮你录制了");
				}

			} else if (evt.getActionCommand().equals(Constants.MTKSTART)) {
				ShellHandle
						.exec("adb shell am broadcast -a com.mediatek.mtklogger.ADB_CMD -e cmd_name start --ei cmd_target 1");
				ShellHandle.exec("adb shell am start -n com.mediatek.mtklogger/.MainActivity");
				Main.sleep(200);
				CommandResult result = ShellUtils.execCommand("dumpsys activity top | grep ACTIVITY", false);
				String respon = result.successMsg;
				if (respon.contains("mtklogger")) {
					Main.fill("启动 MTL LOG 完毕");
				} else {
					Main.fill("无法启动，请手动启动 ----拨号盘输入*#*#3646633#*#*");
				}
				Main.newline();

			} else if (evt.getActionCommand().equals(Constants.GETMONKEYLOG)) {
				// String[] commands={"rm -rf /sdcard/mtklog/mobilelog/"};
				String[] commands = { "cd /sdcard/monkey_result/", "ls -l" };
				CommandResult result = ShellUtils.execCommand(commands, false);
				System.out.println(result.successMsg);
				System.out.println(result.successMsg.split("\n").length);
				Main.fill("-----Log 包------ checkPath： /sdcard/monkey_result/");
				List<String> data = new LinkedList<String>();
				for (String a : result.successMsg.split("\n")) {
					System.out.println(a);
					if (a.contains("drw")) {
						Main.fill(a);
						data.add(a);
					}
				}
				if (data.size() > 0) {
					String last = data.get(data.size() - 1);
					int APLog = last.indexOf("20160");
					if (APLog > 0) {
						String lastLog = last.substring(APLog, last.length());
						Main.fill("拉最新的到电脑（" + Constants.CURRENTPATH + "）：  " + lastLog);
						System.out.println(Constants.CURRENTPATH);

						ShellHandle.exec("adb pull /sdcard/monkey_result/" + lastLog + " " + Constants.CURRENTPATH
								+ "\\" + lastLog + "");
						ShellHandle.execLine("explorer " + Constants.CURRENTPATH);
					} else {
						Main.fill("没找到MonkeyLog 先不抓了  ");
					}
				} else {
					Main.fill("没找到log /sdcard/monkey_result/");
				}

				Main.newline();
			} else if (evt.getActionCommand().equals(Constants.GETLOG)) {
				// String[] commands={"rm -rf /sdcard/mtklog/mobilelog/"};
				String[] commands = { "cd /sdcard/mtklog/mobilelog/", "ls -l" };
				CommandResult result = ShellUtils.execCommand(commands, false);
				System.out.println(result.successMsg);
				System.out.println(result.successMsg.split("\n").length);
				Main.fill("-----Log 包------ checkPath： /sdcard/mtklog/mobilelog/");
				List<String> data = new LinkedList<String>();
				for (String a : result.successMsg.split("\n")) {
					if (a.contains("drw")) {
						Main.fill(a);
						data.add(a);
					}
				}
				if (data.size() > 0) {
					String last = data.get(data.size() - 1);
					int APLog = last.indexOf("APLog");
					if (APLog > 0) {
						String lastLog = last.substring(APLog, last.length());
						System.out.println(lastLog);
						Main.fill("拉最新的到电脑（" + Constants.CURRENTPATH + "）：  " + lastLog);
						System.out.println(Constants.CURRENTPATH);

						ShellHandle.execLine("explorer " + Constants.CURRENTPATH);
					
						  ShellHandle.exec("adb pull /sdcard/mtklog/mobilelog/"
						  + lastLog + " " + Constants.CURRENTPATH + "\\" +
						  lastLog + "");
			
					} else {
						Main.fill("没找到APLog 先不抓了  ");
					}
				} else {
					Main.fill("没找到log /sdcard/mtklog/mobilelog/");
				}

				Main.newline();
			} else if (evt.getActionCommand().equals(Constants.PROXY)) {
				ShellHandle.exec("adb push " + Constants.CURRENTPATH + "\\resource\\adb.jar /sdcard/");
				System.out.println("adb push " + Constants.CURRENTPATH + "\\src\\resource\\adb.jar /sdcard/");
				ShellHandle
						.exec("adb shell uiautomator runtest /sdcard/adb.jar --nohup -c testcase.adbSupport#proxySet");
				Main.fill("设置完了");
				Main.newline();
			} else if (evt.getActionCommand().equals(Constants.GETLOGCAT)) {
				Main.fill("-----  拉取logcat  -----");
				ShellUtils.execCommand("logcat -v threadtime -d > /sdcard/logcat.txt", false);				
				ShellHandle.execLine("adb pull /sdcard/logcat.txt "+Constants.CURRENTPATH);
				Main.fill("拉取完毕 查看 : "+Constants.CURRENTPATH+"\\logcat.txt");
				Main.openCurrentPath();
				checkLog.CheckLogFile(Constants.CURRENTPATH+"\\logcat.txt");
				Main.newline();
			}

		} catch (Exception e) {
			// TODO: handle exception
			Main.fill("----------  有异常,记下log  ----------");
			Main.fill(e + "");
		}

	}

}
