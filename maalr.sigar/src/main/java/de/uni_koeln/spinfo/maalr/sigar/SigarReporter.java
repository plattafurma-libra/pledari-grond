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
package de.uni_koeln.spinfo.maalr.sigar;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import de.uni_koeln.spinfo.maalr.common.shared.statistics.DiskStats;
import de.uni_koeln.spinfo.maalr.common.shared.statistics.NetStats;
import de.uni_koeln.spinfo.maalr.common.shared.statistics.SystemSummary;

public final class SigarReporter implements Runnable {
	
	private Set<SigarHandler> handlers = new HashSet<SigarHandler>();
	private Sigar sigar;
	
	private boolean load = true, uptime = true, memo = true, net = true, fs = true;
	private boolean cpu = true;
	
	private Map<String, NetAverage> rxMap = new HashMap<String, NetAverage>();
	private Map<String, NetAverage> txMap = new HashMap<String, NetAverage>();
	
	Random r = new Random();
	
	SigarReporter(Sigar sigar) {
		this.sigar = sigar;
	}

	public void run() {
		SystemSummary summary = getSigarSummary();
		for (SigarHandler handler : handlers) {
			handler.updated(summary);
		}
	}

	private SystemSummary getSigarSummary() {
		SystemSummary summary = new SystemSummary();
		if(load) {
			try {
				summary.setNumberOfCPUs(sigar.getCpuList().length);
				summary.setLoadAverage(sigar.getLoadAverage());
			} catch (SigarException e) {
				load = false;
				e.printStackTrace();
			}
		}
		if(cpu) {
			try {
				CpuPerc[] cpuPercList = sigar.getCpuPercList();
				double idle = 0, sys = 0, user = 0, nice = 0;
				for (CpuPerc cpuPerc : cpuPercList) {
					idle += cpuPerc.getIdle();
					sys += cpuPerc.getSys();
					user += cpuPerc.getUser();
					nice += cpuPerc.getNice();
				}
				summary.setCpuIdle(idle);
				summary.setCpuNice(nice);
				summary.setCpuSystem(sys);
				summary.setCpuUser(user);
			} catch (Throwable e) {
				cpu = false;
				e.printStackTrace();
			}
		}
		if(uptime) {
			try {
				summary.setUptime(sigar.getUptime().getUptime());
			} catch (SigarException e) {
				uptime =false;
				e.printStackTrace();
			}	
		}
		if(memo) {
			try {
				Mem mem = sigar.getMem();
				summary.setOsFreeMemory(mem.getActualFree());
				summary.setOsUsedMemory(mem.getActualUsed());
				summary.setOsTotalMemory(mem.getTotal());
			} catch (SigarException e) {
				memo = false;
				e.printStackTrace();
			}	
		}
		if(net) {
			try {
				summary.setNetInboundTotal(sigar.getNetStat().getAllInboundTotal());
				summary.setNetOutboundTotal(sigar.getNetStat().getAllOutboundTotal());
				String[] netIfs = sigar.getNetInterfaceList();
				int seconds = 5;
				for (String strg : netIfs) {
					NetInterfaceStat netStat = sigar.getNetInterfaceStat(strg);
					long txBytes = netStat.getTxBytes();
					NetAverage tx = txMap.get(strg);
					if(tx == null) {
						tx = new NetAverage(seconds);
						txMap.put(strg, tx);
					}
					tx.addValue(txBytes);
					long rxBytes = netStat.getRxBytes();
					NetAverage rx = rxMap.get(strg);
					if(rx == null) {
						rx = new NetAverage(seconds);
						rxMap.put(strg, rx);
					}
					rx.addValue(rxBytes);
					long txDropped = netStat.getTxDropped();
					long txErrors = netStat.getTxErrors();
					long rxErrors = netStat.getRxErrors();
					long rxDropped = netStat.getRxDropped();
					NetStats stat = new NetStats();
					stat.setAverageTxInByte(tx.getThroughput()/seconds);
					stat.setAverageRxInByte(rx.getThroughput()/seconds);
					stat.setRxInByte(rxBytes);
					stat.setTxInByte(txBytes);
					stat.setName(strg);
					stat.setRxDropped(rxDropped);
					stat.setTxDropped(txDropped);
					//System.out.println(netStat.getSpeed() + ", " + stat.getAverageTxInByte() + ", " + stat.getAverageRxInByte());
					stat.setSpeedInBit(netStat.getSpeed());
					stat.setTxErrors(txErrors);
					stat.setRxErrors(rxErrors);
					if(stat.getSpeedInBit() > 0) {
						summary.addNetStats(stat);
					}
					
					//100.000.000 
					//39800 
					//415.324
					
				}
			} catch (SigarException e) {
				net = false;
				e.printStackTrace();
			}
		}
		if(fs) {
			try {
				FileSystem[] fileSystemList = sigar.getFileSystemList();
				for (FileSystem fileSystem : fileSystemList) {
					fileSystem.getDevName();
					FileSystemUsage usage = sigar.getFileSystemUsage(fileSystem.getDirName());
					DiskStats stats = new DiskStats();
					if(usage.getTotal() < 4096) continue;
					if(usage.getUsed() < 4096 && usage.getAvail() < 4096) continue;
					stats.setTotal(usage.getTotal());
					stats.setDevName(fileSystem.getDevName());
					stats.setDirName(fileSystem.getDirName());
					stats.setUsed(usage.getUsed());
					stats.setAvailable(usage.getAvail());
					stats.setReads(usage.getDiskReads());
					stats.setWrites(usage.getDiskWrites());
					stats.setReadBytes(usage.getDiskReadBytes());
					stats.setWriteBytes(usage.getDiskWriteBytes());
					stats.setDiskQueue(usage.getDiskQueue());
					summary.addDiskStats(stats);
				}
			} catch (SigarException e) {
				fs = false;
				e.printStackTrace();
			}
		}
		
		List<MemoryPoolMXBean> memBeans = ManagementFactory.getMemoryPoolMXBeans(); 
		long used = 0;
		long committed = 0;
		long max = 0;
		for (Iterator<MemoryPoolMXBean> i = memBeans.iterator(); i.hasNext(); ) {

		    MemoryPoolMXBean mpool = (MemoryPoolMXBean)i.next();
		    MemoryUsage usage = mpool.getUsage();
		    used += usage.getUsed();
		    committed += usage.getCommitted();
		    max += usage.getMax();
		}
		summary.setJavaMemoryUsed(used);
	    summary.setJavaMemoryCommitted(committed);
	    summary.setJavaMemoryMax(max);
		return summary;
		
	}
	
	public void addHandler(SigarHandler handler) {
		handlers.add(handler);
	}
	
	public void removeHandler(SigarHandler handler) {
		handlers.remove(handler);
	}

}
