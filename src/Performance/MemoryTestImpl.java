package Performance;

import Utils.ShellUtils;



public class MemoryTestImpl implements MemoryTestI{
	
	private String Native_Heap ;
	private String Dalvik_Heap ;
	private String TOTAL ;
	private String Activites;
	private String Views;
	
	public void start(String pckName) {
		
		//symbol the location of needed information
		int location;
		
		ShellUtils.CommandResult result = ShellUtils.execCommand("dumpsys meminfo " + pckName, false);
		String[] resultStrings = result.successMsg.split("\n");
		for (String line : resultStrings) {
			
			if (line.contains("Native Heap")){
				location = line.indexOf("Native Heap") + "Native Heap".length();
				Native_Heap = line.substring(location , location + 10).trim();
			}
			if (line.contains("Dalvik Heap")) {
				location = line.indexOf("Dalvik Heap") + "Dalvik Heap".length();
				Dalvik_Heap = line.substring(location , location + 10).trim();
			}
			if (line.contains("TOTAL")) {
				location = line.indexOf("TOTAL") + "TOTAL".length();
				TOTAL = line.substring(location , location + 10).trim();
			}
			if (line.contains("Views")) {
				location = line.indexOf("Views") + "Views:".length();
				Views = line.substring(location , location + 9).trim();
			}
			if (line.contains("Activities")) {
				location = line.indexOf("Activities") + "Activities:".length();
				Activites = line.substring(location , location + 9).trim();
			}
		}
	}
	
	public String getNative_Heap() {
		return Native_Heap;
	}
	public String getDalvik_Heap() {
		return Dalvik_Heap;
	}
	public String getTOTAL() {
		return TOTAL;
	}
	public String getActivites() {
		return Activites;
	}
	public String getViews() {
		return Views;
	}
	
}
