package Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Views.Constants;
import Views.Main;

public class FileUtil {
	
	private PropUtil PropUtil;
	
	// 获取配置文件
		public void loadConfig() {
			try {
				PropUtil = new PropUtil("config/app.properties");
				Main.app = PropUtil.get("app");
				Main.activity = PropUtil.get("activity");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// 本地调试直接用默认值
			}
		}

	public static void addJson(String name, String command,String type)
			throws JSONException, IOException {
		String json = FileUtil.readTxtFile(Constants.JSONPATH);
		String buttonJson = FileUtil.getValue(json, "buttons");
		String write = "{\"name\":\"" + name + "\",\"command\":\"" + command
				+ "\",\"name\":\"" + name + "\",\"command\":\"" + command
				+ "\"}";
		JSONObject add = new JSONObject();
		add.put("name", name);
		add.put("command", command);
		add.put("type", type);		
		JSONArray resultArray = new JSONArray(buttonJson);
		resultArray.put(add);
		JSONObject newResult = new JSONObject();
		newResult.put("buttons", resultArray);
		FileUtil.writeTxt(newResult.toString());
	}
	
       public static void main(String[] args) throws JSONException {
   
    	   
	}
	
	public static String getTypeByName(String key,String type) throws JSONException{
		String jsonButton = FileUtil.readTxtFile(Constants.JSONPATH);
		System.out.println(jsonButton);
		Object[] datas = FileUtil.getJsonArrayValues(jsonButton, "buttons");
		int num = datas.length;
		for (int i = 0; i < num; i++) {
			if(datas[i].toString().contains(key)){
				return getValue(datas[i].toString(), type);
			}
		}
		return null;		
	}
	
	public static String deleteJson(String key) throws JSONException, IOException{
		String jsonButton = FileUtil.readTxtFile(Constants.JSONPATH);
		Object[] datas = FileUtil.getJsonArrayValues(jsonButton, "buttons");
		int num = datas.length;
		String data="[";
		for (int i = 0; i < num; i++) {
			if(!getValue(datas[i].toString(),"name").equals(key)){
				data+=datas[i].toString()+",";
			}
		}
		data=data.subSequence(0, data.length()-1).toString();
		data+="]";
		System.out.println(data);
		JSONArray resultArray = new JSONArray(data);
		JSONObject newResult = new JSONObject();
		newResult.put("buttons", resultArray);
		FileUtil.writeTxt(newResult.toString());
		return data;		
	}

	public static void writeTxt(String txt) throws IOException {
		File file = new File(Constants.JSONPATH);
		FileWriter writer = new FileWriter(file);
		writer.write(txt);
		writer.flush();
		writer.close();
	}

	public static String getValue(String json, String key) throws JSONException {
		JSONObject resultJSON = new JSONObject(json);
		String res = resultJSON.get(key).toString();
		return res;
	}
	
	

	public static Object[] getJsonArrayValues(String response, String key)
			throws JSONException {
		String value = null;
		JSONObject resultJSON = new JSONObject(response);
		Iterator it = resultJSON.keys();

		String res = resultJSON.get(key).toString();
		JSONArray resultArray = new JSONArray(res);
		int num = resultArray.length();
		Object[] result = new String[num];
		if (num > 0) {
			for (int i = 0; i < num; i++) {
				JSONObject obj = resultArray.getJSONObject(i);
				result[i] = obj.toString();
			}
		}
		return result;
	}

	public static boolean readTxtFile(String filePath, String Iwant) {

		try {
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					if (lineTxt.contains(Iwant)) {
						return true;
					}
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}

		return false;
	}

	public static String readTxtFile(String filePath) {
		String result = "";
		try {
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					result += lineTxt;
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

	public static ArrayList<String> getTxtFile(String filePath) {
		ArrayList<String> result = new ArrayList<String>();
		try {
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					result.add(lineTxt);
					//checkMonkeyLog(lineTxt, lineNum);
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
