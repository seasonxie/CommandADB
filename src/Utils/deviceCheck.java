package Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import Views.Main;

public class deviceCheck {
	
	public static boolean conect = false;// adb是否有连接
	public static String check = "";
	
	public static void deviceChecker() throws InterruptedException {
		Thread.sleep(500);
		String checkAgain = "";
		// 检查adb
		while (true) {
			try {
				check = checkadb();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!checkAgain.equals(check)) {
				Main.txtIng.append("\n 检查ADB过程中.....\n");
				Main.txtIng.append(check);
				Main.txtIng.setCaretPosition(Main.txtIng.getText().length());
				Main.txtIng.paintImmediately(Main.txtIng.getBounds());
				checkAgain = check;
			}

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// 检查adb
		public static String checkadb() throws IOException {
			String check = "";
			BufferedReader br = null;
			Process p = Runtime.getRuntime().exec("cmd /c adb devices");
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}

			if (!sb.toString().trim().equals("List of devices attached")
					&& !sb.toString().trim().contains("offline")
					&& !sb.toString().trim().contains("adb server is out of date")) {
				conect = true;
				check = "【adb已连接，为所欲为吧】  \n" + sb.toString().trim();
			} else {
				conect = false;
				check = "【无法连接adb,什么都不能操作！悲剧】\n" + sb.toString().trim();
			}
			return check;
		}

		public static int getDevices(String check) {
			ArrayList<String> devices = new ArrayList<String>();
			for (String aa : check.split("\n")) {
				if (aa.length() == 19 && aa.contains("device")) {
					devices.add(aa.substring(0, 12));
				}
			}
			return devices.size();
		}


}
