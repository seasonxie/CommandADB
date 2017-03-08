package Performance;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Utils.ShellUtils;
import Utils.ShellUtils.CommandResult;







public class FlowTestImpl_Rx extends FlowTestI{
	
	




	@Override
	public long getFlow(String uid) {
		long flow=getFlow_Rcv(uid)+getFlow_Snd(uid);
	    return flow;
	}

	@Override
	public long getFlow_Rcv(String uid) {
		 long flow=0;
		 String[] command={"cat /proc/net/xt_qtaguid/stats | grep "+uid+""};	
	     CommandResult result =  ShellUtils.execCommand(command, false);    
	    
	    String[] results=result.successMsg.split("\\n");
	    for (int i = 0; i < results.length; i++) {	    	
	    	  flow+=filterData(results[i],true);	
		}
	  
      
	    return flow;
	}

	@Override
	public long getFlow_Snd(String uid) {
		 long flow=0;
		 String[] command={"cat /proc/net/xt_qtaguid/stats | grep "+uid+""};	
	     CommandResult result =  ShellUtils.execCommand(command, false);    
	    
	    String[] results=result.successMsg.split("\\n");
	    for (int i = 0; i < results.length; i++) {	
	    flow+=filterData(results[i],false);
	    }
	   
	    return flow;
	}
	
	



	

	

}
