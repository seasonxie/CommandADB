package Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;

import Utils.ShellUtils.CommandResult;
import Views.Main;

public class ShellHandle {
	

	public boolean isWifiConnected() {
		String result = getProp("dhcp.wlan0.result");
		System.out.println(result);
		return ((result != null) && ("ok".equals(result.trim())));
	}

	public static String getTime() {
		return String.valueOf(System.currentTimeMillis());
	}

	public static String getCurrentActivity() {
		CommandResult result = ShellUtils.execCommand(
				"dumpsys activity top | grep ACTIVITY", false);
		return result.successMsg;
	}

	public static String getAppVersion() {
		String check = "";
		try {
			String[] commands = { "dumpsys package " + Main.app + "" };
			CommandResult result = ShellUtils.execCommand(commands, false);
			int a = result.successMsg.indexOf("versionName=");
			check = result.successMsg.substring(a + 12, a + 23).trim();
		} catch (Exception e) {
			// TODO: handle exception
			check = "无法找到应用版本信息";
		}
		return check;
	}

	public static String getIp() {
		InetAddress ia = null;
		String localip = "localhost";
		try {
			ia = ia.getLocalHost();
			// String localname=ia.getHostName();
			localip = ia.getHostAddress();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return localip;
	}

	
	public static String getProp(String item) {
		return ShellHandle.exec("adb shell getprop " + item + "");
	}

	// 4.2以下的返回true
	public static boolean pushCheck(String appVerison) {
		boolean check = false;
		try {
			appVerison = appVerison.substring(0, 3);
			if (Double.valueOf(appVerison) >= 4.2) {
				check = true;
			}
		} catch (Exception e) {
			// 报错默认是false
		}
		return check;
	}

	
	
	public static String exec(String command) {
		Process p = null;
		String result = "";
		try {
			p = Runtime.getRuntime().exec(command);
			//p.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					p.getInputStream(), "GBK"));
			String s = "";
			while ((s = br.readLine()) != null) {
				if (!s.trim().isEmpty())
					result += s/* +"\n" */;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (p != null) {
				p.destroy();
			}
		}
		return result;
	}
	
	public static String execLine(String command) {
		Process p = null;
		String result = "";
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					p.getInputStream(), "GBK"));
			String s = "";
			while ((s = br.readLine()) != null) {
				if (!s.trim().isEmpty())
					result += s +"\n" ;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (p != null) {
				p.destroy();
			}
		}
		return result;
	}
	
	 
	 public static String getProperty(String property)
	  {
	    Process p = null;

	    String result = "";
	    try {
	      p = Runtime.getRuntime().exec("getprop " + property);
	      p.waitFor();
	      BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
	      result = reader.readLine();
	      reader.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    } finally {
	      if (p != null)
	        p.destroy();
	    }

	    return result;
	  }

}
