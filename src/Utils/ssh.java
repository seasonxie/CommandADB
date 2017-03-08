package Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;




import org.json.JSONException;
import org.junit.Test;

import Views.Main;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class ssh {
	static Connection conn = null;
	static Session session = null;
	static StreamGobbler stdout = null;
	static BufferedReader br = null;
	static String result="";

	public static void main(String[] args) throws IOException {
		SSHUtil("172.17.49.84","test","webtest789");
		//SSHUtil("172.17.120.22","root","123123");
		//System.out.println(execCommand("cd /data/log/jetty&&tail -n 100 bro-admin-error.log"));
		close();
	}

	public static void SSHUtil(String hostname, String username, String password) {
		try {
			conn = new Connection(hostname,22);
			conn.connect();
			System.out.println("connect success");
			boolean isAuthenticated = conn.authenticateWithPassword(username, password);
			System.out.println("user password ok");
			//Main.fill("服务器身份验证成功");
			if (isAuthenticated == false) {
				throw new IOException("Authentication failed.");
			}
			
			 SCPClient scpClient = conn.createSCPClient();   
			 scpClient.put("D:\\sypro_log", "/mnt/mfs/bro/res"); //从本地复制文件到远程目录   
		} catch (IOException e) {
			e.printStackTrace();
			Main.fill("服务器身份验证不成功，请检查ip");
		}
		
		
	}

	public static void close() {
		if (session != null) {
			session.close();
		}
		if (conn != null) {
			conn.close();
		}
	}

	public void closeSession() {
		session.close();
	}

	public static ArrayList<String> execCommand(String command) throws IOException {
		session = conn.openSession();
		session.execCommand(command);
		ArrayList<String> array_result = new ArrayList<String>();
		stdout = new StreamGobbler(session.getStdout());
		br = new BufferedReader(new InputStreamReader((stdout),"UTF-8"));	
		/* String line = br.readLine();
	        System.out.println(line);*/
		 /*   return line;*/
	
      String line;
        System.out.println("session.getInputStream info ...");
        while ((line = br.readLine()) != null) {
        	Main.fill(line);
           System.out.println(line);
            array_result.add(line);
        }
   
        close();

        return array_result;
	}

	public static String readLine() throws IOException {
		return br.readLine();
	}
	
	
	
	
	
	
	

	
}