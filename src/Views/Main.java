package Views;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicButtonUI;

import org.json.JSONException;

import Performance.performanceFacade;
import Utils.FileUtil;
import Utils.HttpUtil;
import Utils.ShellHandle;
import Utils.ShellUtils;
import Utils.ShellUtils.CommandResult;
import Utils.deviceCheck;
import Utils.ssh;
import Actions.apkOpertionAction;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	public static DialogShow dialog;
	public static showDeleteDiglog detedialog;
	public static Map<String, JButton> buttons = new LinkedHashMap<String, JButton>();
	public static FileUtil FileUtil = new FileUtil();
	Map<String, String> customTypes = new LinkedHashMap<String, String>();
	Map<String, String> customCommands = new LinkedHashMap<String, String>();
	apkOpertionAction apkOpertionAction = new apkOpertionAction();
	Actions.commomUsedAction commomUsedAction = new Actions.commomUsedAction();
	Actions.performanceAction performanceAction = new Actions.performanceAction();
	// 默认应用信息
	public static String app = "com.android.browser";
	public static String activity = "com.android.browser.BrowserActivity";
	public static JPanel mAdminPanel;
	public static JPanel commomUsed;
	public static JPanel apkOpertion;
	public static JPanel customOpertion;
	public static JPanel performanceOpertion;
	public static JPanel bottomPanel;
	public static JTextArea txtIng;
	// 长宽
	private final Dimension TopItemDimension = new Dimension(10, 30);
	private final Dimension bottomItemDimension = new Dimension(30, 35);

	public Main() throws FileNotFoundException {
		super("commandShortcut"); // title
		FileUtil.loadConfig();
	}

	// 顶部按钮
	public void buttonForTop(JPanel j, String buttonName, String buttonFlag) {
		JButton button = new JButton(buttonName);
		button.addActionListener(this);
		button.setActionCommand(buttonFlag);
		button.setPreferredSize(TopItemDimension);
		// button.setSize(120,120);
		j.add(button);
		buttons.put(buttonFlag, button);
	}

	// 底部按钮
	public void buttonForBottom(JPanel j, String buttonName, String buttonFlag) {
		JButton button = new JButton(buttonName);
		button.addActionListener(this);
		button.setActionCommand(buttonFlag);
		button.setPreferredSize(bottomItemDimension);
		j.add(button);
	}

	public void setjPanel(JPanel j, String title, LayoutManager mgr) {
		j.setBorder(new TitledBorder(title));
		j.setLayout(mgr);
	}

	public void run() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, JSONException {
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		mAdminPanel = new JPanel(new FlowLayout());
		mAdminPanel.setLayout(new GridLayout(3, 2));

		setjPanel(commomUsed = new JPanel(), "常用按钮", new GridLayout(2, 2));
		setjPanel(apkOpertion = new JPanel(), "应用相关", new GridLayout(2, 2));
		setjPanel(customOpertion = new JPanel(), "自定义操作", new GridLayout(2, 2));
		// setjPanel(performanceOpertion=new JPanel(),"性能相关",new
		// BoxLayout(performanceOpertion, BoxLayout.X_AXIS));

		// mAdminPanel.add(performanceOpertion,0);
		mAdminPanel.add(customOpertion, 0);
		mAdminPanel.add(apkOpertion, 0);
		mAdminPanel.add(commomUsed, 0);

		bottomPanel = new JPanel(new FlowLayout());
		bottomPanel.setLayout(new GridLayout(1, 2)); // 底部按钮

		txtIng = new JTextArea();
		txtIng.setLineWrap(true);// 激活自动换行功能
		txtIng.setWrapStyleWord(true);// 激活断行不断字功能
		apkOpertionAction.drag();// 定义可以拉进去安装方法
		// 顶层按钮
		buttonForTop(commomUsed, "获取手机信息", Constants.PROP);
		buttonForTop(commomUsed, "获取当前截图", Constants.SCREEN);
		buttonForTop(commomUsed, "录制手机屏幕", Constants.RECORD);
		buttonForTop(commomUsed, "调用本地PUSH", Constants.PUSHLOCAL);
		buttonForTop(commomUsed, "打开 MTK LOG", Constants.MTKSTART);
		buttonForTop(commomUsed, "获取最新MTK LOG", Constants.GETLOG);
		buttonForTop(commomUsed, "获取monkeyLog", Constants.GETMONKEYLOG);
		
		buttonForTop(apkOpertion, "清数据启动", Constants.CLEAR);	
		if(Constants.ISBROWSERSPEC)
		buttonForTop(apkOpertion, "RunUI测试", Constants.RUNUIAUTO);		
		
		//buttonForTop(apkOpertion, "安装应用", Constants.INSTALL);		
		buttonForTop(commomUsed, "抓取缓存Logcat", Constants.GETLOGCAT);			
		buttonForTop(apkOpertion, "删除应用", Constants.UNINSTALL);

		/*
		 * buttonForTop(commomUsed, "设置代理", Constants.PROXY);
		 * buttonForTop(commomUsed, "跑monkey", Constants.RUNMONKEY);
		 * buttonForTop(commomUsed, "检查monkey log", Constants.GETMONKEYLOG);
		 * buttonForTop(commomUsed, "连接inwebWifi", Constants.INWEBWIFI);
		 */

		buttonForTop(apkOpertion, "内存测试", Constants.MEMORYTEST);
		buttonForTop(apkOpertion, "获取内存", Constants.GETMEMORY);
		buttonForTop(apkOpertion, "启动时间", Constants.STARTTEST);
		buttonForTop(apkOpertion, "当前耗流", Constants.FLOWTEST);
		if(Constants.ISBROWSERSPEC)
		buttonForTop(apkOpertion, "熄屏检查", Constants.SCREENTEST);

		buttonForTop(customOpertion, "自定义添加", Constants.ADDBUTTON);
		buttonForTop(customOpertion, "自定义删除", Constants.DELETEBUTTON);
		buttonForTop(customOpertion, "打开当前文件夹", Constants.OPENCURRENTPATH);
		addCustomButtons();
		// 底层按钮
		buttonForBottom(bottomPanel, "清屏", Constants.CLEARTEXT);
		buttonForBottom(bottomPanel, "kill/restart adb", Constants.KILLADB);
		
	
		 
		JScrollPane sp = new JScrollPane(txtIng);
		this.setLayout(new BorderLayout());
		this.add(mAdminPanel, BorderLayout.NORTH);
		this.add(bottomPanel, BorderLayout.SOUTH);
		this.add(sp, BorderLayout.CENTER);
		
		setSize(600, 800);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	// 操作处理
	public void actionPerformed(ActionEvent evt) {
		// 底部按钮不需要连接adb就可以点击
		if (evt.getActionCommand().equals(Constants.CLEARTEXT)) {
			txtIng.setText("");

		} else if (evt.getActionCommand().equals(Constants.KILLADB)) {
			ShellHandle.exec("adb kill-server");
			ShellHandle.exec("adb start-server");
			fill("如果adb还没连上请重新插拔usb");
			newline();
		}

		if (evt.getActionCommand().equals(Constants.ADDBUTTON)) {
			if (dialog == null) {
				dialog = new DialogShow(this, "添加按钮");
			}
			dialog.setVisible(true);
			newline();
		} else if (evt.getActionCommand().equals(Constants.DELETEBUTTON)) {
			if (detedialog == null) {
				try {
					detedialog = new showDeleteDiglog(this, "删除按钮");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			detedialog.setVisible(true);
			newline();
		} else if (evt.getActionCommand().equals(Constants.OPENCURRENTPATH)) {
			openCurrentPath();
			newline();
		} else if (evt.getActionCommand().endsWith(Constants.CUSTOMFLAG)) {// 所有自定义按钮
			fill("--------------开始执行自定义命令---------------");
			String buttonName = evt.getActionCommand().replace(Constants.CUSTOMFLAG, "");

			String type;
			try {
				//type = Utils.FileUtil.getTypeByName(buttonName, "type");
				type=customTypes.get(buttonName);
				if (type == null) {
					fill("解析按钮无法找到，请重新添加");
					return;
				}
				String command=customCommands.get(buttonName);
				
				if (type.contains("window")) {
					//command = Utils.FileUtil.getTypeByName(buttonName, "command");
					fill("执行命令： " + command + "   返回结果：↓↓↓↓↓");
					String[] commands = command.split("&&");
					for (String c : commands) {
						System.out.println(c);												
							fill(ShellHandle.execLine(c));
						
					}
					
				} else if (type.contains("android")) {
					//command = Utils.FileUtil.getTypeByName(buttonName, "command");
					if (deviceCheck.conect == false) {
						return;
					}
					String[] commands = command.split("&&");
					for (String c : commands) {
						System.out.println(c);
					}
					fill("执行命令： " + Arrays.toString(commands) + "   返回结果：↓↓↓↓↓");
					CommandResult result = ShellUtils.execCommand(commands, false);
					String[] sr=result.successMsg.split("\n");
					System.out.println(sr.length);
					for (int i = 0; i < sr.length; i++) {
						if(i<=4||sr.length-i==1){
							continue;
						}
						fill(sr[i]);
					}
					
				} else if (type.contains("linux")) {
					//command = Utils.FileUtil.getTypeByName(buttonName, "command");
					String ip = buttonName.substring(buttonName.indexOf("(") + 1, buttonName.length() - 1);
					System.out.println("ip   " + ip);
					fill("服务器IP:  " + ip + "  执行命令： " + command + "   返回结果：↓↓↓↓↓");
					System.out.println(command);
					ssh.SSHUtil(ip, "test", "webtest789");
					ssh.execCommand(command);
				} else {
					fill("按钮执行平台无法识别：  " + type);
				}
				newline();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		// 操作和连接为null，不执行操作
		if (evt == null || deviceCheck.conect == false) {
			return;
		}

		Actions.apkOpertionAction.ActionHandle(evt);
		Actions.commomUsedAction.ActionHandle(evt);
		performanceAction.ActionHandle(evt);

	}
	
	public static void openCurrentPath(){
		ShellHandle.execLine("explorer " + Constants.CURRENTPATH);
	}

	public static void removeButton(String buttonName) {
		customOpertion.remove(buttons.get(buttonName));
		customOpertion.repaint();
		customOpertion.updateUI();
	}

	public void upudatUI(JPanel j) {
		j.updateUI();
	}

	public void addButton(String buttonName, String command, String type, JPanel j) {
		JButton button = new JButton(buttonName);
		button.addActionListener(this);
		button.setActionCommand(buttonName + Constants.CUSTOMFLAG);
		j.add(button);
		buttons.put(buttonName, button);
		customCommands.put(buttonName, command);
		customTypes.put(buttonName, type);
		j.repaint();
		j.updateUI();
	}

	public static void main(String[] args) throws InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, FileNotFoundException, JSONException {

		Main a = new Main();
		a.run();

		fill("Design by 应用分发测试部");
		fill("运行包名：   " + app);
		fill("activity：  " + activity);
		fill("友情提示：如要安装apk直接拖进来就就可以了");
		fill("------------开始运行----------");
		mAdminPanel.repaint();
		mAdminPanel.updateUI();
		try {
			deviceCheck.deviceChecker();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addCustomButtons() throws JSONException {
		try {
			String webButton = HttpUtil.sendGet("http://tm.meizu.com/tms/testResource/getCustomButtons");
			Object[] datas = FileUtil.getJsonArrayValues(webButton, "buttons");
			int num = datas.length;
			for (int i = 0; i < num; i++) {
				addButton(FileUtil.getValue(datas[i].toString(), "name"), FileUtil.getValue(datas[i].toString(), "command"), FileUtil.getValue(datas[i].toString(), "type"), customOpertion);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			String jsonButton ;		
		    jsonButton = FileUtil.readTxtFile(Constants.JSONPATH);
			Object[] datas1 = Utils.FileUtil.getJsonArrayValues(jsonButton, "buttons");
			int num = datas1.length;
			for (int i = 0; i < num; i++) {
				addButton(FileUtil.getValue(datas1[i].toString(), "name"), FileUtil.getValue(datas1[i].toString(), "command"), FileUtil.getValue(datas1[i].toString(), "type"), customOpertion);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 换行
	public static void newline() {
		txtIng.setCaretPosition(txtIng.getText().length());
		txtIng.paintImmediately(txtIng.getBounds());
	}

	// 显示值
	public static void fill(String value) {

		txtIng.append("\n" + value + "");
		txtIng.paintImmediately(txtIng.getBounds());
		// txtIng.paintImmediately(txtIng.getBounds());
		// txtIng.setCaretPosition(txtIng.getText().length());
		// cachedThreadPool.execute(new fill(txtIng,value));
		// newline();
	}

	public static void sleep(int num) {
		try {
			Thread.sleep(num);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
