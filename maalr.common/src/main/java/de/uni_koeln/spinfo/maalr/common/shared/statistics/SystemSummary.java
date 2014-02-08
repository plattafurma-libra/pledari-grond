/*******************************************************************************
 * Copyright 2013 Sprachliche Informationsverarbeitung, University of Cologne
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uni_koeln.spinfo.maalr.common.shared.statistics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SystemSummary implements Serializable {
	
	private static final long serialVersionUID = -6634018470343905132L;
	
	private double[] loadAverage;
	private double uptime;
	
	private double cpuIdle, cpuNice, cpuSystem, cpuUser;
	
	public double getCpuIdle() {
		return cpuIdle;
	}
	public void setCpuIdle(double cpuIdle) {
		this.cpuIdle = cpuIdle;
	}
	public double getCpuNice() {
		return cpuNice;
	}
	public void setCpuNice(double cpuNice) {
		this.cpuNice = cpuNice;
	}
	public double getCpuSystem() {
		return cpuSystem;
	}
	public void setCpuSystem(double cpuSystem) {
		this.cpuSystem = cpuSystem;
	}
	public double getCpuUser() {
		return cpuUser;
	}
	public void setCpuUser(double cpuUser) {
		this.cpuUser = cpuUser;
	}
	public double getUptime() {
		return uptime;
	}
	public void setUptime(double uptime) {
		this.uptime = uptime;
	}
	private long osFreeMemory, osUsedMemory, osTotalMemory;
	private long netInboundTotal, netOutboundTotal;
	private int numberOfCPUs;
	
	public int getNumberOfCPUs() {
		return numberOfCPUs;
	}
	public void setNumberOfCPUs(int numberOfCPUs) {
		this.numberOfCPUs = numberOfCPUs;
	}
	private long javaMemoryUsed, javaMemoryCommitted, javaMemoryMax;
	
	private ArrayList<DiskStats> diskStats = new ArrayList<DiskStats>();
	
	private HashMap<String, NetStats> netStats = new HashMap<String, NetStats>();

	public ArrayList<DiskStats> getDiskStats() {
		return diskStats;
	}
	@Override
	public String toString() {
		return "SystemSummary [loadAverage=" + Arrays.toString(loadAverage)
				+ ", uptime=" + uptime + ", osFreeMemory=" + osFreeMemory
				+ ", osUsedMemory=" + osUsedMemory + ", osTotalMemory="
				+ osTotalMemory + ", netInboundTotal=" + netInboundTotal
				+ ", netOutboundTotal=" + netOutboundTotal
				+ ", javaMemoryUsed=" + javaMemoryUsed
				+ ", javaMemoryCommitted=" + javaMemoryCommitted
				+ ", javaMemoryMax=" + javaMemoryMax + "]";
	}
	public long getJavaMemoryUsed() {
		return javaMemoryUsed;
	}
	public void setJavaMemoryUsed(long javaMemoryUsed) {
		this.javaMemoryUsed = javaMemoryUsed;
	}
	public long getJavaMemoryCommitted() {
		return javaMemoryCommitted;
	}
	public void setJavaMemoryCommitted(long javaMemoryCommitted) {
		this.javaMemoryCommitted = javaMemoryCommitted;
	}
	public long getJavaMemoryMax() {
		return javaMemoryMax;
	}
	public void setJavaMemoryMax(long javaMemoryMax) {
		this.javaMemoryMax = javaMemoryMax;
	}
	public double[] getLoadAverage() {
		return loadAverage;
	}
	public void setLoadAverage(double[] loadAverage) {
		this.loadAverage = loadAverage;
	}
	public void setUptime(long uptime) {
		this.uptime = uptime;
	}
	public long getOsFreeMemory() {
		return osFreeMemory;
	}
	public void setOsFreeMemory(long freeMem) {
		this.osFreeMemory = freeMem;
	}
	public long getOsUsedMemory() {
		return osUsedMemory;
	}
	public void setOsUsedMemory(long usedMem) {
		this.osUsedMemory = usedMem;
	}
	public long getOsTotalMemory() {
		return osTotalMemory;
	}
	public void setOsTotalMemory(long totalMem) {
		this.osTotalMemory = totalMem;
	}
	public long getNetInboundTotal() {
		return netInboundTotal;
	}
	public void setNetInboundTotal(long netInboundTotal) {
		this.netInboundTotal = netInboundTotal;
	}
	public long getNetOutboundTotal() {
		return netOutboundTotal;
	}
	public void setNetOutboundTotal(long netOutboundTotal) {
		this.netOutboundTotal = netOutboundTotal;
	}
	public void addDiskStats(DiskStats stats) {
		diskStats.add(stats);
	}
	
	public double getOsUsedMemoryPercent() {
		long total = getOsTotalMemory();
		if(total == 0) return -1;
		return getOsUsedMemory()/(double) total;
	}
	
	public double getOsFreeMemoryPercent() {
		long total = getOsTotalMemory();
		if(total == 0) return -1;
		return getOsFreeMemory()/(double) total;
	}
	
	public double getJavaUsedMemoryPercent() {
		long total = getJavaMemoryMax();
		if(total == 0) return -1;
		return getJavaMemoryUsed()/(double) total;
	}
	
	public double getJavaFreeMemoryPercent() {
		long total = getJavaMemoryMax();
		if(total == 0) return -1;
		return (getJavaMemoryMax()-getJavaMemoryUsed())/(double) total;
	}
	
	public long getJavaMaxFreeMemory() {
		return getJavaMemoryMax()-getJavaMemoryUsed();
	}
	
	public long getJavaCommittedFreeMemory() {
		return getJavaMemoryCommitted()-getJavaMemoryUsed();
	}
	public void addNetStats(NetStats stat) {
		netStats.put(stat.getName(), stat);
	}
	
	public Map<String, NetStats> getNetStats() {
		return netStats;
	}
	
	

}
