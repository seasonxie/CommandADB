package Performance;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Utils.ShellUtils;
import Utils.ShellUtils.CommandResult;





public abstract class FlowTestI {

	private String uid;

	/**
	 * 
	 * @description: 获取应用耗流 -kb
	 * @date: 2016年4月13日 下午3:29:33
	 * @author: zhaotang
	 * @param uid
	 */
	public abstract long getFlow(String uid);

	/**
	 * 
	 * @description: 获取应用接收耗流 -Byte
	 * @date: 2016年4月13日 下午3:29:33
	 * @author: zhaotang
	 * @param uid
	 */
	public abstract long getFlow_Rcv(String uid);

	/**
	 * 
	 * @description: 获取应用发送耗流 -Byte-snd
	 * @date: 2016年4月13日 下午3:29:33
	 * @author: zhaotang
	 * @param uid
	 */
	public abstract long getFlow_Snd(String uid);

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * 
	 * @description: 获取应用uid
	 * @date: 2016年4月13日 下午3:29:33
	 * @author: zhaotang
	 * @param PackageName
	 */
	public void getUid(String packageName) {
		String uid = null;
		String[] command = { "ps | grep " + packageName + "" };
		CommandResult result1 = ShellUtils.execCommand(command, false);
		String[] data=result1.successMsg.split("\\n");
		String pid = data[data.length-3].substring(10, 15).trim();

		String[] command1 = { "cd /proc/" + pid + "", "cat status" };
		CommandResult result = ShellUtils.execCommand(command1, false);

		if (result != null)
			if (result.successMsg.indexOf("Uid:") != -1)
				uid = result.successMsg.substring(
						result.successMsg.indexOf("Uid:") + 5,
						result.successMsg.indexOf("Uid:") + 10);

		setUid(uid);
	}

	
	/**
	 * 
	 * @description: 解析获取的耗流数据，取第6和第8点
	 * @date: 2016年4月13日 下午3:29:33
	 * @author: zhaotang
	 * @param 获取的耗流数据
	 * true is rec  ，false is snd
	 */
	
	public static int filterData(String data, boolean RcvOrNot) {
		Pattern pattern;
		if (RcvOrNot) {
			pattern = Pattern
					.compile("\\w{1,50}\\ \\w{1,50}\\ \\w{1,50}\\ \\w{1,50}\\ \\w{1,50}\\ (\\w{1,50})\\ \\d.*");
		} else {
			pattern = Pattern
					.compile("\\w{1,50}\\ \\w{1,50}\\ \\w{1,50}\\ \\w{1,50}\\ \\w{1,50}\\ \\w{1,50}\\ \\w{1,50}\\ (\\w{1,50})\\ \\d.*");
		}
		Matcher matcher = pattern.matcher(data);
		String Flow = "0";
		while (matcher.find()) {
			Flow = matcher.group(1);
			//System.out.println(Flow);
		}
		return Integer.valueOf(Flow);
	}
	
	public static void main(String[] args) {
		String a="7 ccmni0 0x0 10002 1 459443 473 50279 489 459443 473 0 0 0 0 50279 489 0 0 0 0";
		filterData(a,true);
		filterData(a,true);
	}

}
