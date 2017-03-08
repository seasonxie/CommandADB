package Actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import Performance.performanceFacade;
import Utils.FileUtil;
import Utils.ShellHandle;
import Utils.ShellUtils;
import Utils.ShellUtils.CommandResult;
import Views.Constants;
import Views.DialogShow;
import Views.Main;



public class performanceAction {
	performanceFacade getPerform=new performanceFacade();
	static long beforeFlowRev = 0;
	static long beforeFlowSnd = 0;
	
	 public void ActionHandle(ActionEvent evt){
			if (evt.getActionCommand().equals(Constants.STARTTEST)) {
				Main.txtIng.setText("");
				JOptionPane
						.showMessageDialog(null, "启动时间测试开始，冷热启动各5次\n 需要20S，确定开始");
				int runTime = 5;
				
				Main.fill("------启动时间测试开始，冷热启动各5次--------");
				Main.fill("------冷启动--------");
				int start1 = 0;
				int startTime = 0;
				for (int i = 0; i < runTime; i++) {
					Main.sleep(1000);
					ShellUtils.execCommand("am force-stop " + Main.app, false);
					Main.sleep(2000);
					startTime = getPerform.getStartTime(Main.app, Main.activity);
					start1 += startTime;
					Main.fill(startTime + "");
				}
				Main.fill("avg: " + start1 / runTime);
				start1 = 0;
				startTime = 0;
				Main.fill("-----热启动--------");
				// 热启动时间
				for (int i = 0; i < runTime; i++) {
					Main.sleep(1000);
					// ShellUtil.clearCache(app);
					ShellUtils.execCommand("input keyevent 3", false);
					Main.sleep(2000);
					startTime = getPerform.getStartTime(Main.app, Main.activity);
					start1 += startTime;
					Main.fill(startTime + "");
				}
				Main.fill("avg: " + start1 / runTime);
				Main.newline();

			} else if (evt.getActionCommand().equals(Constants.FLOWTEST)) {
				Main.fill("-----获取耗流  单位：KB-----");
				if (!ShellHandle.getCurrentActivity().contains(Main.app))
					ShellHandle.exec("adb shell am start -n " + Main.app + "/" + Main.activity + "");
				Long[] data = getPerform.getFlow(Main.app);
				long beforeFlowSnd = data[2] / 1024;
				long beforeFlowRev = data[1] / 1024;
				Main.fill("发送: " + beforeFlowSnd + "        相差   "
						+ (beforeFlowSnd - this.beforeFlowSnd));
				Main.fill("接收: " + beforeFlowRev + "        相差   "
						+ (beforeFlowRev - this.beforeFlowRev));
				Main.fill("总: "
						+ (beforeFlowSnd + beforeFlowRev)
						+ "        相差   "
						+ (beforeFlowSnd + beforeFlowRev - this.beforeFlowSnd - this.beforeFlowRev));
				Main.newline();
				this.beforeFlowRev = beforeFlowRev;
				this.beforeFlowSnd = beforeFlowSnd;

			} else if (evt.getActionCommand().equals(Constants.GETMEMORY)) {
				
				Main.fill("-----获取内存信息-----");
				if (!ShellHandle.getCurrentActivity().contains(Main.app))
					ShellHandle.exec("adb shell am start -n " + Main.app + "/" + Main.activity + "");
				String[] data = getPerform.getMemory(Main.app);
				Main.fill("Total: " + data[0]);
				Main.fill("Dalvik_Heap: " + data[1]);
				Main.fill("Native_Heap: " + data[2]);
				Main.fill("Views: " + data[3]);
				Main.fill("Activites: " + data[4]);
				Main.newline();

			} else if (evt.getActionCommand().equals(Constants.MEMORYTEST)) {
				Main.txtIng.setText("");
				JOptionPane
						.showMessageDialog(
								null,
								"需要20S\n 【验证1】：开启应用，滑动listview，退出到桌面，查看Views和Activites是否为0\n 【验证2】：杀掉应用，查看应用是否被杀死\n确定开始");
				int runTime = 5;
				
				Main.fill("-----内存测试-----");
				Main.fill("【验证1】：开启应用，滑动listview，退出到桌面，查看Views和Activites是否为0");
				ShellUtils.execCommand("am force-stop " + Main.app, false);
				ShellHandle.exec("adb shell am start -n " + Main.app + "/" + Main.activity + "");
				for (int i = 0; i < runTime; i++) {
					Main.sleep(500);
					ShellHandle.exec("adb shell input swipe 500 1000 500 0");
				}
				for (int i = 0; i < runTime; i++) {
					ShellHandle.exec("adb shell input keyevent 4");
				}
				Main.sleep(5000);
				try {
					String[] data = getPerform.getMemory(Main.app);
					Main.fill("  Total: " + data[0] + "  Dalvik_Heap: " + data[1]
							+ "  Native_Heap: " + data[2] + "  Views: " + data[3]
							+ "  Activites: " + data[4]);
					if (Integer.valueOf(data[3]) + Integer.valueOf(data[4]) == 0) {
						Main.fill("【passed】 Views和Activites为0");
					} else {
						Main.fill("【failed】 Views或Activites不为0");
					}
				} catch (Exception e) {
					Main.fill("【failed】  Views或Activites不为0");
				}

				Main.fill("【验证2】：杀掉应用，查看应用是否被杀死");
				ShellUtils.execCommand("am force-stop " + Main.app, false);
				Main.sleep(1000);
				String result = ShellHandle.exec("adb shell dumpsys meminfo " + Main.app);
				if (result.contains("No process found for:")) {
					Main.fill("【passed】 应用被杀死");
				} else {
					Main.fill("【failed】 应用没被杀死");
				}
				Main.newline();
			} else if (evt.getActionCommand().equals(Constants.SCREENTEST)) {
				String checkData = "Disabling non-boot CPUs";
				JOptionPane.showMessageDialog(null,
						"熄屏检查只是去获取你的最新mtklog \n查看是否存在 “" + checkData + "”\n 确定开始");
				Main.fill("-----熄屏检查开始------");
				String[] commands = { "cd /sdcard/mtklog/mobilelog/", "ls -l" };
				CommandResult result = ShellUtils.execCommand(commands, false);
				List<String> data = new LinkedList<String>();
				for (String a : result.successMsg.split("\n")) {
					if (a.contains("drw")) {
						data.add(a);
					}
				}
				if (data.size() > 0) {
					String last = data.get(data.size() - 1);
					int APLog = last.indexOf("APLog");
					if (APLog > 0) {
						String lastLog = last.substring(APLog, last.length());
						Main.fill("拉最新的到电脑：  " + lastLog);
						ShellHandle.exec("adb pull /sdcard/mtklog/mobilelog/" + lastLog + " "
								+ Constants.CURRENTPATH + "\\" + lastLog + "");
						boolean getData = FileUtil.readTxtFile(Constants.CURRENTPATH + "\\"
								+ lastLog + "\\kernel_log", checkData);
						Main.fill("检查log  " + Constants.CURRENTPATH + "\\" + lastLog
								+ "\\kernel_log");
						if (getData) {
							Main.fill("【passed】 log中找到 " + checkData + "");
						} else {
							Main.fill("【faileded】 log中找不到 " + checkData + "");
						}
					} else {
						Main.fill("找不到最新的 MTK-APLog  ");
					}
				} else {
					Main.fill("没找到log /sdcard/mtklog/mobilelog/");
				}
				Main.newline();
			} else if (evt.getActionCommand().equals(Constants.RUNMONKEY)) {
				Main.fill("未实现");
				Main.newline();

			} /*else if (evt.getActionCommand().equals(Constants.GETMONKEYLOG)) {
				Main.fill("未实现");
				String operation = "123";
				String command = "adb shell monkey -s 1000 -p com.android.browser --ignore-crashes --ignore-timeouts --kill-process-after-error --ignore-security-exceptions --pct-trackball 0 --pct-nav 0 -v -v -v --throttle 500 "
						+ operation + " > /sdcard/monkeytest.log 2>&1 &";

				ThreadManger.runShell(command);
				ThreadManger.runLog();
				
				Main.newline();

			}*/ else if (evt.getActionCommand().equals(Constants.INWEBWIFI)) {
				System.out.println("---------");
				Main.fill("未实现");
				Main.fill("拉最新的到电脑：  ");
				ShellHandle.exec("adb pull /sdcard/monkeytest.log " + Constants.CURRENTPATH);
				ArrayList<String> datas = FileUtil.getTxtFile(Constants.CURRENTPATH
						+ "\\monkeytest.log");
				Main.fill("log数： " + datas.size());
				Main.newline();

			} 

	  	   
     }

}
