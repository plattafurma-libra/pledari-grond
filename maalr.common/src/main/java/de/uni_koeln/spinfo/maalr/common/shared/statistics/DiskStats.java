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

public class DiskStats implements Serializable {
	
	private static final long serialVersionUID = -4122923204175840997L;

	private String devName, dirName;
	
	private long used, available, reads, writes, total, readBytes, writeBytes;
	
	private double diskQueue;

	/**
	 * Returns the disk queue
	 */
	public double getDiskQueue() {
		return diskQueue;
	}

	public void setDiskQueue(double diskQueue) {
		this.diskQueue = diskQueue;
	}

	/**
	 * Returns the device name
	 */
	public String getDevName() {
		return devName;
	}

	
	public void setDevName(String devName) {
		this.devName = devName;
	}

	/**
	 * Returns the mount point
	 * @return
	 */
	public String getDirName() {
		return dirName;
	}

	public void setDirName(String dirName) {
		this.dirName = dirName;
	}

	public long getUsed() {
		return used;
	}

	/**
	 * Returns the total used Kbytes of the file system.
	 * @param used
	 */
	public void setUsed(long used) {
		this.used = used;
	}
	
	public double getUsedPercent() {
		long total = getTotal();
		if(total == 0) return -1;
		return getUsed()/(double)total;
	}
	
	public double getAvailablePercent() {
		long total = getTotal();
		if(total == 0) return -1;
		return getAvailable()/(double)total;
	}

	/**
	 * Returns the total free Kbytes of the file system.
	 * @param used
	 */
	public long getAvailable() {
		return available;
	}

	public void setAvailable(long available) {
		this.available = available;
	}

	/**
	 * Returns the number of physical disk reads
	 * @param used
	 */
	public long getReads() {
		return reads;
	}

	public void setReads(long reads) {
		this.reads = reads;
	}

	/**
	 * Returns the number of physical disk writes
	 * @param used
	 */
	public long getWrites() {
		return writes;
	}

	public void setWrites(long writes) {
		this.writes = writes;
	}

	/**
	 * Returns the total number of Kbytes of the file system
	 * @return
	 */
	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	/**
	 * Returns the number of physical disk bytes read
	 * @return
	 */
	public long getReadBytes() {
		return readBytes;
	}

	public void setReadBytes(long readBytes) {
		this.readBytes = readBytes;
	}

	/**
	 * Returns the number of physical disk bytes written.
	 * @return
	 */
	public long getWriteBytes() {
		return writeBytes;
	}

	public void setWriteBytes(long writeBytes) {
		this.writeBytes = writeBytes;
	}

}
