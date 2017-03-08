package Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import Views.Constants;
import Views.Main;

public class checkLog {

	public static  String[] keys ;
	static{
		keys =Constants.MONKEYCHECK.split(",");
	}
	
	
	public static void checkLogCat(String line, int lineNum) {		
		for (String k : keys) {
			if (line.contains(k)) {
				Main.fill("行数： " + lineNum + " 【" + k + "】 " + line);
			}
		}
	}
	
	

	public static ArrayList<String> CheckLogFile(String filePath) {
		
		ArrayList<String> result = new ArrayList<String>();
		int linenum=1;
		try {
			String encoding = "GBK";
			
			File file = new File(filePath);
			//System.out.println(file.isFile() && file.exists());
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					//result.add(lineTxt);
					checkLogCat(lineTxt, linenum);
					linenum++;
				}
				read.close();
				
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}

		return result;
	}

}
