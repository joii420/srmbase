package com.step.cpu;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.management.OperatingSystemMXBean;
import com.step.tool.utils.MathUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.*;

public class CpuUtils {
	//可以设置长些，防止读到运行此次系统检查时的cpu占用率，就不准了
	private static final int CPUTIME = 5000;

	private static final int PERCENT = 100;

	private static final int FAULTLENGTH = 10;

	/**
	 * 由于String.subString对汉字处理存在问题（把一个汉字视为一个字节)，因此在
	 * 包含汉字的字符串时存在隐患，现调整如下：
	 *
	 * @param src       要截取的字符串
	 * @param start_idx 开始坐标（包括该坐标)
	 * @param end_idx   截止坐标（包括该坐标）
	 * @return
	 */
	private static String byteSubstring(String src, int start_idx, int end_idx) {
		byte[] b = src.getBytes();
		StringBuffer tgt = new StringBuffer();
		for (int i = start_idx; i <= end_idx; i++) {
			tgt.append((char) b[i]);
		}
		return tgt.toString();
	}

	//    获得当前的监控对象.
	public static MonitorInfoBean getMonitorInfoBean() throws IOException {
		int kb = 1024 * 1024;
		// 可使用内存
		long totalMemory = Runtime.getRuntime().totalMemory() / kb;
		// 剩余内存
		long freeMemory = Runtime.getRuntime().freeMemory() / kb;
		// 最大可使用内存
		long maxMemory = Runtime.getRuntime().maxMemory() / kb;
		OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		// 操作系统
		String osName = System.getProperty("os.name");
		// 总的物理内存
		long totalMemorySize = osmxb.getTotalPhysicalMemorySize() / kb;
		// 剩余的物理内存
		long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize() / kb;
		// 已使用的物理内存
		long usedMemory = (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()) / kb;
		// 获得线程总数
		ThreadGroup parentThread;
		for (parentThread = Thread.currentThread().getThreadGroup(); parentThread.getParent() != null; parentThread = parentThread.getParent())
			;
		int totalThread = parentThread.activeCount();
		double cpuRatio = 0;
		if (osName.toLowerCase().startsWith("windows")) {
			cpuRatio = getCpuRatioForWindows();
		}
		MonitorInfoBean infoBean = new MonitorInfoBean();
		infoBean.setFreeMemory(freeMemory);
		infoBean.setFreePhysicalMemorySize(freePhysicalMemorySize);
		infoBean.setMaxMemory(maxMemory);
		infoBean.setOsName(osName);
		infoBean.setTotalMemory(totalMemory);
		infoBean.setTotalMemorySize(totalMemorySize);
		infoBean.setTotalThread(totalThread);
		infoBean.setUsedMemory(usedMemory);
		infoBean.setCpuRatio(cpuRatio);
		Process process = Runtime.getRuntime().exec("cmd.exe /c tasklist");
		BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		StringBuilder sb = new StringBuilder();
		int i = -2;
		List<Map> l = new ArrayList<>();
		while ((line = input.readLine()) != null) {
			if (line.length() > 0) {
				i++;
				if (i > 0) {
					Map<String, String> m = new HashMap<>();
					m.put("name", line.substring(0, 25).trim());
					m.put("pid", line.substring(26, 34).trim());
					m.put("memUsage", line.substring(64).trim().split(" ")[0]);
					l.add(m);
				}
			}
		}
		input.close();
		infoBean.setTotalProcess(i);
		infoBean.setProcessDetail(l);
		infoBean.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		return infoBean;
	}

	//    获得CPU使用率
	private static double getCpuRatioForWindows() {
		try {
			String procCmd = System.getenv("windir")
				+ "//system32//wbem//wmic.exe process get Caption,CommandLine,"
				+ "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
			// 取进程信息
			long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
			Thread.sleep(CPUTIME);
			long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
			if (c0 != null && c1 != null) {
				long idletime = c1[0] - c0[0];
				long busytime = c1[1] - c0[1];
				return (double) (PERCENT * (busytime) / (busytime + idletime));
			} else {
				return 0.0;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0.0;
		}
	}

	//    读取CPU信息
	private static long[] readCpu(final Process proc) {
		long[] retn = new long[2];
		try {
			proc.getOutputStream().close();
			InputStreamReader ir = null;
			//获取操作系统的默认编码集
			String str = System.getProperty("sun.jnu.encoding");
			if ("GBK".equals(str)) {
				ir = new InputStreamReader(proc.getInputStream(), "gbk");
			} else if ("UTF-8".equals(str)) {
				ir = new InputStreamReader(proc.getInputStream(), "utf-8");
			}

			LineNumberReader input = new LineNumberReader(ir);
			String line = input.readLine();
			if (line == null || line.length() < FAULTLENGTH) {
				return null;
			}
			int capidx = line.indexOf("Caption");
			int cmdidx = line.indexOf("CommandLine");
			int rocidx = line.indexOf("ReadOperationCount");
			int umtidx = line.indexOf("UserModeTime");
			int kmtidx = line.indexOf("KernelModeTime");
			int wocidx = line.indexOf("WriteOperationCount");
			long idletime = 0;
			long kneltime = 0;
			long usertime = 0;
			while ((line = input.readLine()) != null) {
				if (line.length() < wocidx) {
					continue;
				}
				// 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,
				// ThreadCount,UserModeTime,WriteOperation
				String caption = byteSubstring(line, capidx, cmdidx - 1).trim();
				String cmd = byteSubstring(line, cmdidx, kmtidx - 1).trim();
				if (cmd.contains("wmic.exe")) {
					continue;
				}

				if (caption.equals("System Idle Process") || caption.equals("System")) {
					idletime += Long.valueOf(byteSubstring(line, kmtidx, rocidx - 1).trim());
					idletime += Long.valueOf(byteSubstring(line, umtidx, wocidx - 1).trim());
					continue;
				}

				kneltime += Long.valueOf(byteSubstring(line, kmtidx, rocidx - 1).trim());
				usertime += Long.valueOf(byteSubstring(line, umtidx, wocidx - 1).trim());

			}
			retn[0] = idletime;
			retn[1] = kneltime + usertime;
			return retn;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				proc.getInputStream().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	static final long GB = 1024 * 1024 * 1024;

	public static MonitorInfoVO getCpuDetials() {
		OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		String osJson = JSON.toJSONString(operatingSystemMXBean);
//            System.out.println("osJson is " + osJson);
		JSONObject jsonObject = JSON.parseObject(osJson);
		//该进程占用CPU
		double processCpuLoad = jsonObject.getDouble("processCpuLoad") * 100;
		processCpuLoad = MathUtil.sacleTwo(processCpuLoad);
		//系统CPU占用率
		double systemCpuLoad = jsonObject.getDouble("systemCpuLoad") * 100;
		systemCpuLoad = MathUtil.sacleTwo(systemCpuLoad);
		Long totalPhysicalMemorySize = jsonObject.getLong("totalPhysicalMemorySize");
		Long freePhysicalMemorySize = jsonObject.getLong("freePhysicalMemorySize");
		//系统总内存
		double totalMemory = 1.0 * totalPhysicalMemorySize / GB;
		totalMemory = MathUtil.sacleTwo(totalMemory);
		//系统剩余内存
		double freeMemory = 1.0 * freePhysicalMemorySize / GB;
		freeMemory = MathUtil.sacleTwo(freeMemory);
		//内存占用率
		double memoryUseRatio = 1.0 * (totalPhysicalMemorySize - freePhysicalMemorySize) / totalPhysicalMemorySize * 100;
		memoryUseRatio = MathUtil.sacleTwo(memoryUseRatio);

		MonitorInfoVO vo = new MonitorInfoVO();
		vo.setProcessCpuLoad(processCpuLoad);
		vo.setMemoryUseRatio(memoryUseRatio);
		vo.setSystemCpuLoad(systemCpuLoad);
		vo.setTotalMemory(totalMemory);
		vo.setFreeMemory(freeMemory);

		StringBuilder result = new StringBuilder();
		result.append("系统CPU占用率: ")
			.append(systemCpuLoad)
			.append("%，内存占用率：")
			.append(memoryUseRatio)
			.append("%，系统总内存：")
			.append(totalMemory)
			.append("GB，系统剩余内存：")
			.append(freeMemory)
			.append("GB，该进程占用CPU：")
			.append(processCpuLoad)
			.append("%");
		System.out.println(result.toString());

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return vo;
//		return jsonObject;
	}


	public static void main(String... args) {
		while (true) {
			try {
				Thread.sleep(500);
//				MonitorInfoBean monitorInfo = getMonitorInfoBean();
//				System.out.println(monitorInfo.getProcessDetail());
//				System.out.println("cpu占有率=" + monitorInfo.getCpuRatio());
//				System.out.println("总的物理内存=" + monitorInfo.getTotalMemorySize() + "MB");
//				System.out.println("已使用的物理内存=" + monitorInfo.getUsedMemory() + "MB");
//				System.out.println("进程总数=" + monitorInfo.getTotalProcess());
				getCpuDetials();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
