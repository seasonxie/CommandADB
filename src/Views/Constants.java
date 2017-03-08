package Views;

public class Constants {
	public final static String CURRENTPATH = System.getProperty("user.dir");
	//public final static String JSONPATH = CURRENTPATH + "\\config\\button.txt";
	public final static String JSONPATH = CURRENTPATH + "\\src\\config\\button.txt";
	public final static String CUSTOMFLAG = "_custom";
	public final static String MONKEYCHECK = "Fatal,CRASH,ANR,Exception,DALVIK THREADS,DexOpt";
	public final static int ANDROIDCODE = 0;
	public final static int WINDOWCODE = 1;
	public final static int LINEXCODE = 2;
	public final static int EXECTIME = 8000;
	public final static boolean ISBROWSERSPEC = true;
	
	
	// 定义顶部按钮信息
	public final static String PROP = "getprop";
	public final static String SCREEN = "takescreen";
	public final static String CLEAR = "clear";
	public final static String INSTALL = "install";
	public final static String RECORD = "record";
	public final static String UNINSTALL = "uninstall";
	public final static String PUSHLOCAL = "pushlocal";
	public final static String MTKSTART = "mtkstart";
	public final static String GETLOG = "getlog";
	public final static String RUNUIAUTO = "runuiauto";
	public final static String GETLOGCAT = "getlogcat";


	public final static String PROXY = "proxy";
	public final static String RUNMONKEY = "runmonkey";
	public final static String GETMONKEYLOG = "getmonkeylog";
	
	
	public final static String INWEBWIFI = "inwebwifi";

	public final static String GETMEMORY = "getmemory";
	public final static String MEMORYTEST = "memorytest";
	public final static String STARTTEST = "starttest";
	public final static String FLOWTEST = "flowtest";
	public final static String SCREENTEST = "screentest";

	public final static String ADDBUTTON = "addbutton";
	public final static String DELETEBUTTON = "deletebutton";
	public final static String OPENCURRENTPATH = "opencurrentpath";

	// 定义底部按钮信息
	public final static String KILLADB = "killadb";
	public final static String CLEARTEXT = "cleartext";


}
