package com.step.cpu;


/**
 * @author : Sun
 * @date : 2022/8/16  18:54
 */
public class MonitorInfoVO {

	/**该进程占用CPU*/
	private Double processCpuLoad;
	/**系统CPU占用率*/
	private Double systemCpuLoad;
	/**系统总内存*/
	private Double totalMemory;
	/**系统剩余内存*/
	private Double freeMemory;
	/**内存占用率*/
	private Double memoryUseRatio;

	public Double getProcessCpuLoad() {
		return processCpuLoad;
	}

	public void setProcessCpuLoad(Double processCpuLoad) {
		this.processCpuLoad = processCpuLoad;
	}

	public Double getSystemCpuLoad() {
		return systemCpuLoad;
	}

	public void setSystemCpuLoad(Double systemCpuLoad) {
		this.systemCpuLoad = systemCpuLoad;
	}

	public Double getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(Double totalMemory) {
		this.totalMemory = totalMemory;
	}

	public Double getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(Double freeMemory) {
		this.freeMemory = freeMemory;
	}

	public Double getMemoryUseRatio() {
		return memoryUseRatio;
	}

	public void setMemoryUseRatio(Double memoryUseRatio) {
		this.memoryUseRatio = memoryUseRatio;
	}

}
