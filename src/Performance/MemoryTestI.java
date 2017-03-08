package Performance;

public interface MemoryTestI {
	
	/**
	 * 
	 * @description: 获取性能数据，其实就是执行adb shell dumpsys meminfo ，这样写方便获取 
	 * @date: 2016年4月13日 下午8:45:46
	 * @author: wenli
	 * @param pckName
	 */
	public void start(String pckName);
	
	/**
	 * 
	 * @description: 获取MemInfo里面对应的数据，详细可以参考代码，只是做了简单的数据提取
	 * @date: 2016年4月13日 下午8:46:59
	 * @author: wenli
	 * @return
	 */
	public String getNative_Heap();
	public String getDalvik_Heap();
	public String getTOTAL();
	public String getActivites();
	public String getViews();
}
