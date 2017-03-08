package Performance;

import Utils.ShellUtils;
import Utils.ShellUtils.CommandResult;






public class StartTimeTestImpl implements StartTimeTestI{

	@Override
	public String getStartTime(String PackageActivity) {
		 CommandResult result=  ShellUtils.execCommand(
					"am start -W "+PackageActivity+"", false);		  
		 String response=result.successMsg;
		 //adb格式打印是固定的
		 String time=response.substring(response.indexOf("WaitTime")+10,response.indexOf("WaitTime")+14).trim();
		 time.replace("C", "").trim();
		return time;
	}

}
