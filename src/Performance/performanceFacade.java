package Performance;

import Views.Main;



public class performanceFacade {
	 public FlowTestI getFlow = new FlowTestImpl_Rx();
	 public MemoryTestI getMemory= new MemoryTestImpl();
	 public StartTimeTestI getStartTime= new StartTimeTestImpl();
	 
	public int getStartTime(String packageName,String Activity){
		int startTime=0;
		try {
			startTime=Integer.valueOf(getStartTime.getStartTime(packageName+"/"+Activity));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return startTime;
	}
	
	public Long[] getFlow(String packageName){
		getFlow.getUid(packageName);
		Main.fill("packageName= "+packageName+" ,uid= "+getFlow.getUid());
		long rev=getFlow.getFlow_Rcv(getFlow.getUid());
		long snd=getFlow.getFlow_Snd(getFlow.getUid());
		Long[] data={rev+snd,rev,snd};
		return data;		
	}
	
	public String[] getMemory(String packageName){
		getMemory.start(packageName);
		String[] data={getMemory.getTOTAL(),getMemory.getDalvik_Heap(),getMemory.getNative_Heap(),getMemory.getViews(),getMemory.getActivites()};
		return data;		
	}


}
