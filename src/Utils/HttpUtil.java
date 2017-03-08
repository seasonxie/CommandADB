package Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import Views.Constants;
import Views.Main;

public class HttpUtil {
	
	public static void main(String[] args) {
		//System.out.println(sendGet("http://tm.meizu.com/tms/testResource/getCustomButtons"));
		//http://tm.meizu.com/tms/TestResource/browser.jar
		
		downLoadFile("http://tm.meizu.com/tms/TestResource/browser.jar",Constants.CURRENTPATH+"\\browser.jar");
	}
	
	public static String sendGet(String url) {
		String result = "";
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.connect();

			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}

		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	
	 public static boolean downLoadFile(String urlStr, String localName){
	        BufferedOutputStream bufferedOutputStream = null;
	        BufferedInputStream bufferedInputStream = null;
	        try{
	            URL url = new URL(urlStr);
	            HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
	            bufferedInputStream = new BufferedInputStream(httpConn.getInputStream());
	            File file = new File(localName);
	            if(file.exists()){
	            	file.delete();
	                System.out.println("exits");
	                Main.fill("当前目录已存在browser.jar  删除重新拉取");
	            }
	           /* File filePath = new File(localName.substring(1, localName.lastIndexOf("/")));
	            if(!filePath.exists()){
	                filePath.mkdirs();
	            }
	            file.createNewFile();   // 新建文件
*/	            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
	            byte[] buffer = new byte[1024 * 100];
	            int byteCount = 0;
	            while ((byteCount = bufferedInputStream.read(buffer, 0, buffer.length)) != -1){
	                bufferedOutputStream.write(buffer, 0, byteCount);
	            }

	            bufferedOutputStream.flush();
	            bufferedOutputStream.close();
	            bufferedInputStream.close();
	            return true;

	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	            return false;
	        } catch (IOException e) {
	            e.printStackTrace();
	            return false;
	        }finally {
	            try {
	                if(bufferedInputStream != null){
	                    bufferedInputStream.close();
	                }
	                if(bufferedOutputStream != null){
	                    bufferedOutputStream.close();
	                }
	            }catch (IOException e){
	                e.printStackTrace();
	            }
	        }
	    }

}
