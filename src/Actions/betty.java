package Actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import Utils.ShellUtils;

public class betty {
	Map<String,String> b=new LinkedHashMap<String,String>();
	static String Estimated="Estimated power use";
	static String kernelwakelocks="All kernel wake locks";
	static String browserid="u0a4:";
	static String partialwakelocks="All partial wake locks";
	public static void main(String[] args) {
		System.out.println(ShellUtils.execCommand("ps | grep browser", false).successMsg);
		for (String s:ShellUtils.execCommand("ps | grep browser", false).successMsg.split("\\n")) {
			if(s.contains("com.android.browser"))
				System.out.println(browserid=s.substring(0, 5).replace("_", "")+":");
		}
		exec("adb shell dumpsys batterystats");
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
			String ss= "";
			boolean read=true;
			boolean readid=false;
			int readnum=0;
			while ((s = br.readLine()) != null) {
				if(read)
				if (!s.trim().isEmpty())
					ss=getBatterystats(s);
				
				if(s.trim().equals(browserid)||readid){
					read=false;
					readid=true;
					if (!s.trim().isEmpty())
					System.out.println("# "+s);
					readnum++;
					if(readnum>5&&s.contains("u0a")){
						read=true;
						readid=false;
						readnum=0;
						ss="";
					}
				}
				
				if(!ss.isEmpty()){
					if(ss.contains(Estimated)||ss.contains(kernelwakelocks)||ss.contains(partialwakelocks)){
						read=false;
						if (!s.trim().isEmpty())
						System.out.println("-"+s);
						readnum++;
						if(readnum>20){
							read=true;
							readnum=0;
							ss="";
						}
					}else{
						System.out.println("[  "+ss);
						ss="";
					}
					
				}
			
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
	
	
	public static String getBatterystats(String data){
	/*	System.out.println(packageName);
	
		ShellUtils.execCommand("adb shell dumpsys batterystats " + packageName+" >/sdcard/battery.log",false);
		
		ArrayList<String> datas=null;
		

		
		String[] resultStrings = result.successMsg.split("\n");
		System.out.println(datas.size());
		for (int i = 0; i < 20; i++) {
			System.out.println(data);
		}
		for (int i = 0; i < datas.size(); i++) {
			*/
/*		}
		for (String data : resultStrings) {*/
		
		
	/*	if (data.contains("Computed drain")) {
				System.out.println(data);
				for (int j = 1; j < 10; j++) {
					System.out.println(datas.get(i+j));
				}
				batteryInfo_computed = data.substring(data.indexOf("Computed drain:") + "Computed drain:".length() , data.indexOf(", actual drain:")).trim();
				batteryInfo_actual = data.substring(data.indexOf(", actual drain:") + ", actual drain:".length() ).trim();				
			}*/
		
		String totaltime="Total run time";
		String Timeonbattery="Time on battery";
		String Screenon="Screen on";
		String Timeonscreenoff="Time on battery screen off";
		String Amounton="Amount discharged while screen on";
		String Amountoff="Amount discharged while screen off";
		

		
		
	     	String s="";
			if(data.contains(totaltime)){
				return "总运行时间（包含充电） :  "+data;
			}

			if(data.contains(Timeonbattery)){
				return "电池状态下运行时间 :  "+data;
			}
			if(data.contains(Screenon)){
				return "亮屏时间 :  "+data;
			}
			if(data.contains(Timeonscreenoff)){
				return "灭屏时间    :    "+data;
			}	
			if(data.contains(Amounton)){
				return "on:亮屏耗电值     :  "+data;
			}	
			
			if(data.contains(Amountoff)){
				return "of:灭屏耗电值    :  "+data;
			}	
			
			//观察耗电排行:  
			if(data.contains(Estimated)){
				return Estimated;
			}
			//内核持锁:  
			if(data.contains(kernelwakelocks)){
				return kernelwakelocks;
			}
			//系统持锁: 
			if(data.contains(partialwakelocks)){
				return partialwakelocks;
			}	
			/*//观察应用耗电行为     
			if(data.contains(browserid)){
				return browserid;
			}*/
			
			return s;	
			
		/*	//内核持锁:  
			if(data.contains("All kernel wake locks")){
				for (int j = 0; j < 10; j++) {
					System.out.println(datas.get(i+j));
				}
			}
			
			
			//系统持锁:  
			if(data.contains("All partial wake locks")){
				for (int j = 0; j < 10; j++) {
					System.out.println(datas.get(i+j));
				}
			}	
			
			//观察应用耗电行为     
			if(data.contains("u0a2:")){
				for (int j = 0; j < 10; j++) {
					System.out.println(datas.get(i+j));
				}
			}*/	
			
		}
	}



