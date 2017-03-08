package Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class HttpRequest {
	static String session = "";
	static int contentLengthAllow = -1; // 返回报文长度限制, 为-1时不限制长度
	static int defaultConnectTimeOut = 6000; // 默认连接超时,毫秒
	static int defaultReadTimeOut = 6000; // 默认读取超时,毫秒

	static String proxyHost = ""; // 代理Host
	static String proxyPort = ""; // 代理端口
	// static String proxyHost = ""; //代理Host
	// static String proxyPort = ""; //代理端口

	public static final int[] successCode = { 200, 201, 202 }; // 请求成功返回码

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * 
	 * @return Result 所代表远程资源的响应,头信息
	 */
	public static Map<String, String> get(String url) {

		Map<String, String> result = new HashMap<String, String>();
		BufferedReader in = null;

		try {
			// 打开和URL之间的连接
			URLConnection connection = new URL(url).openConnection();
			// 此处的URLConnection对象实际上是根据URL的请求协议(此处是http)生成的URLConnection类的子类HttpURLConnection
			// 故此处最好将其转化为HttpURLConnection类型的对象,以便用到HttpURLConnection更多的API.
			HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

			// 设置通用的请求属性
			httpURLConnection.setRequestProperty("accept", "*/*");
			httpURLConnection.setRequestProperty("connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			httpURLConnection.setConnectTimeout(defaultConnectTimeOut);
			httpURLConnection.setReadTimeout(defaultReadTimeOut);

			// Fidder监听请求
			if ((!proxyHost.equals("") && !proxyPort.equals(""))) {
				System.setProperty("http.proxyHost", proxyHost);
				System.setProperty("http.proxyPort", proxyPort);
			}

			if (!session.equals(""))
				httpURLConnection.setRequestProperty("Cookie", session);
			// 建立连接
			httpURLConnection.connect();
			result = getResponse(httpURLConnection, in, result);

		} catch (Exception requestException) {
			System.err.println("发送GET请求出现异常!" + requestException);
			// requestException.printStackTrace();
		}
		// 关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception closeException) {
				closeException.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param requestHeaders
	 *            请求头
	 * 
	 * @return Result 所代表远程资源的响应,头信息
	 */
	public static Map<String, String> get(String url,
			Map<String, String> requestHeaders) {

		Map<String, String> result = new HashMap<String, String>();
		BufferedReader in = null;

		try {
			// 打开和URL之间的连接
			URLConnection connection = new URL(url).openConnection();
			// 此处的URLConnection对象实际上是根据URL的请求协议(此处是http)生成的URLConnection类的子类HttpURLConnection
			// 故此处最好将其转化为HttpURLConnection类型的对象,以便用到HttpURLConnection更多的API.
			HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

			// 设置通用的请求属性
			httpURLConnection.setRequestProperty("accept", "*/*");
			httpURLConnection.setRequestProperty("connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			httpURLConnection.setConnectTimeout(defaultConnectTimeOut);
			httpURLConnection.setReadTimeout(defaultReadTimeOut);

			if (!session.equals(""))
				httpURLConnection.setRequestProperty("Cookie", session);
			// Fidder监听请求
			if (!proxyHost.equals("") && !proxyPort.equals("")) {
				System.setProperty("http.proxyHost", proxyHost);
				System.setProperty("http.proxyPort", proxyPort);
			}

			// 设置自定义请求头, 注意请求头如果跟默认的一致，会覆盖原来的设置
			for (String key : requestHeaders.keySet()) {
				httpURLConnection.setRequestProperty(key,
						requestHeaders.get(key).toString());
			}

			// 建立连接
			httpURLConnection.connect();
			getResponse(httpURLConnection, in, result);

		} catch (Exception requestException) {
			System.err.println("发送GET请求出现异常!" + requestException);
			// requestException.printStackTrace();
		}
		// 关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception closeException) {
				closeException.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定URL发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param params
	 *            请求的参数
	 * 
	 * @return Result 所代表远程资源的响应,头信息
	 */
	public static Map<String, String> post(String url, String params) {

		Map<String, String> result = new HashMap<String, String>();
		PrintWriter out = null;
		BufferedReader in = null;

		try {
			// 打开和URL之间的连接
			URLConnection connection = new URL(url).openConnection();
			// 此处的URLConnection对象实际上是根据URL的请求协议(此处是http)生成的URLConnection类的子类HttpURLConnection
			// 故此处最好将其转化为HttpURLConnection类型的对象,以便用到HttpURLConnection更多的API.
			HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

			// 设置通用的请求属性
			httpURLConnection.setRequestProperty("accept", "*/*");
			httpURLConnection.setRequestProperty("connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

			httpURLConnection.setConnectTimeout(defaultConnectTimeOut);
			httpURLConnection.setReadTimeout(defaultReadTimeOut);
			if (!session.equals(""))
				httpURLConnection.setRequestProperty("Cookie", session);
			// 发送POST请求必须设置
			// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在http正文内，
			// 需要设为true, 默认情况下是false;
			httpURLConnection.setDoOutput(true);
			// 设置是否从httpUrlConnection读入，默认情况下是true;
			httpURLConnection.setDoInput(true);
			// Post 请求不能使用缓存
			httpURLConnection.setUseCaches(false);

			// 设定传送的内容类型
			httpURLConnection.setRequestProperty("Content-type",
					"application/json");
			// 设定请求的方法为"POST"，默认是GET
			httpURLConnection.setRequestMethod("POST");
			// Fidder监听请求
			if (!proxyHost.equals("") && !proxyPort.equals("")) {
				System.setProperty("http.proxyHost", proxyHost);
				System.setProperty("http.proxyPort", proxyPort);
			}

			httpURLConnection.connect();
			// 获取URLConnection对象对应的输出流
			// 此处getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法，
			// 所以在开发中不调用上述的connect()也可以)。
			out = new PrintWriter(httpURLConnection.getOutputStream());
			// 发送请求参数
			out.print(params);
			// flush输出流的缓冲
			out.flush();
			getResponse(httpURLConnection, in, result);

		} catch (Exception requestException) {
			System.err.println("请求异常!" + requestException);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定URL发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param params
	 *            请求的参数
	 * @param content_type
	 *            post请求的content—type类型
	 * 
	 * @return Result 所代表远程资源的响应,头信息
	 */
	public static Map<String, String> post(String url, String params,
			String content_type) {

		Map<String, String> result = new HashMap<String, String>();
		PrintWriter out = null;
		BufferedReader in = null;

		try {
			// 打开和URL之间的连接
			URLConnection connection = new URL(url).openConnection();
			// 此处的URLConnection对象实际上是根据URL的请求协议(此处是http)生成的URLConnection类的子类HttpURLConnection
			// 故此处最好将其转化为HttpURLConnection类型的对象,以便用到HttpURLConnection更多的API.
			HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

			// 设置通用的请求属性
			httpURLConnection.setRequestProperty("accept", "*/*");
			httpURLConnection.setRequestProperty("connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

			httpURLConnection.setConnectTimeout(defaultConnectTimeOut);
			httpURLConnection.setReadTimeout(defaultReadTimeOut);
			if (!session.equals(""))
				httpURLConnection.setRequestProperty("Cookie", session);
			// 发送POST请求必须设置
			// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在http正文内，
			// 需要设为true, 默认情况下是false;
			httpURLConnection.setDoOutput(true);
			// 设置是否从httpUrlConnection读入，默认情况下是true;
			httpURLConnection.setDoInput(true);
			// Post 请求不能使用缓存
			httpURLConnection.setUseCaches(false);

			// 设定传送的内容类型
			httpURLConnection.setRequestProperty("Content-type", content_type);
			// 设定请求的方法为"POST"，默认是GET
			httpURLConnection.setRequestMethod("POST");
			// Fidder监听请求
			if (!proxyHost.equals("") && !proxyPort.equals("")) {
				System.setProperty("http.proxyHost", proxyHost);
				System.setProperty("http.proxyPort", proxyPort);
			}

			httpURLConnection.connect();
			// 获取URLConnection对象对应的输出流
			// 此处getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法，
			// 所以在开发中不调用上述的connect()也可以)。
			out = new PrintWriter(httpURLConnection.getOutputStream());
			// 发送请求参数
			out.print(params);
			// flush输出流的缓冲
			out.flush();
			getResponse(httpURLConnection, in, result);

		} catch (Exception requestException) {
			System.err.println("请求异常!" + requestException);
		}

		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定URL发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param params
	 *            请求的参数
	 * @param requestHeaders附带请求头
	 * 
	 * @return Result 所代表远程资源的响应,头信息
	 */
	public static Map<String, String> post(String url, String params,
			Map<String, String> requestHeaders) {

		Map<String, String> result = new HashMap<String, String>();
		PrintWriter out = null;
		BufferedReader in = null;

		try {
			// 打开和URL之间的连接
			URLConnection connection = new URL(url).openConnection();
			// 此处的URLConnection对象实际上是根据URL的请求协议(此处是http)生成的URLConnection类的子类HttpURLConnection
			// 故此处最好将其转化为HttpURLConnection类型的对象,以便用到HttpURLConnection更多的API.
			HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

			// 设置通用的请求属性
			httpURLConnection.setRequestProperty("accept", "*/*");
			httpURLConnection.setRequestProperty("connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

			httpURLConnection.setConnectTimeout(defaultConnectTimeOut);
			httpURLConnection.setReadTimeout(defaultReadTimeOut);
			if (!session.equals(""))
				httpURLConnection.setRequestProperty("Cookie", session);
			// 发送POST请求必须设置
			// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在http正文内，
			// 需要设为true, 默认情况下是false;
			httpURLConnection.setDoOutput(true);
			// 设置是否从httpUrlConnection读入，默认情况下是true;
			httpURLConnection.setDoInput(true);
			// Post 请求不能使用缓存
			httpURLConnection.setUseCaches(false);

			// 设定传送的内容类型
			// httpURLConnection.setRequestProperty("Content-type",
			// content_type);
			// 设定请求的方法为"POST"，默认是GET
			httpURLConnection.setRequestMethod("POST");
			// Fidder监听请求
			if (!proxyHost.equals("") && !proxyPort.equals("")) {
				System.setProperty("http.proxyHost", proxyHost);
				System.setProperty("http.proxyPort", proxyPort);
			}

			// 设置自定义请求头, 注意请求头如果跟默认的一致，会覆盖原来的设置
			for (String key : requestHeaders.keySet()) {
				httpURLConnection.setRequestProperty(key,
						requestHeaders.get(key).toString());
			}

			httpURLConnection.connect();
			// 获取URLConnection对象对应的输出流
			// 此处getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法，
			// 所以在开发中不调用上述的connect()也可以)。
			out = new PrintWriter(httpURLConnection.getOutputStream());
			// 发送请求参数
			out.print(params);
			// flush输出流的缓冲
			out.flush();

			getResponse(httpURLConnection, in, result);

		} catch (Exception requestException) {
			System.err.println("请求异常!" + requestException);
		}

		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	// 公用方法，根据返回码处理返回值
	public static Map<String, String> getResponse(
			HttpURLConnection httpURLConnection, BufferedReader in,
			Map<String, String> result) throws UnsupportedEncodingException,
			IOException {
		boolean flag = false;
		for (int i = 0; i < successCode.length; i++) {
			if (successCode[i] == httpURLConnection.getResponseCode()) {
				flag = true;
				break;
			}
		}

		// 返回码非“successCode”时，response为返回message
		if (flag) {
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					httpURLConnection.getInputStream(), "UTF-8"));
			String line;

			// 获取所有响应头字段
			Map<String, List<String>> Hearder = httpURLConnection
					.getHeaderFields();
			for (String key : Hearder.keySet()) {
				result.put(key, Hearder.get(key).toString());
			}

			// responseList.clear();
			String responseStr = "";
			while ((line = in.readLine()) != null) {
				responseStr += line;
			}

			// Content长度限制
			if (responseStr.length() > contentLengthAllow
					&& contentLengthAllow > 0) {
				responseStr = responseStr.substring(0, contentLengthAllow);
			}

			result.put("Message", httpURLConnection.getResponseMessage());
			result.put("Code",
					String.valueOf(httpURLConnection.getResponseCode()));
			result.put("Response", responseStr);
		} else {
			result.put("Message", httpURLConnection.getResponseMessage());
			result.put("Code",
					String.valueOf(httpURLConnection.getResponseCode()));
			//
			result.put("Response", httpURLConnection.getResponseMessage());
			// 获取所有响应头字段
			Map<String, List<String>> Hearder = httpURLConnection
					.getHeaderFields();
			for (String key : Hearder.keySet()) {
				result.put(key, Hearder.get(key).toString());
			}
		}
		return result;
	}
}