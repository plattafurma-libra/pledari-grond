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
package de.uni_koeln.spinfo.maalr.sigar.info;

import java.io.Serializable;

public class NetStats implements Serializable {

	private static final long serialVersionUID = -3087104959159712525L;
	
	private long averageRxInByte, averageTxInByte;
	
	private String name;
	
	private long txDropped, txErrors, rxDropped, rxErrors;
	
	private long rxInByte, txInByte;
	
	public long getRxInByte() {
		return rxInByte;
	}

	public void setRxInByte(long rx) {
		this.rxInByte = rx;
	}

	public long getTxInByte() {
		return txInByte;
	}

	public void setTxInByte(long tx) {
		this.txInByte = tx;
	}

	private long speedInBit;

	public long getAverageRxInByte() {
		return averageRxInByte;
	}

	public void setAverageRxInByte(long averageRx) {
		this.averageRxInByte = averageRx;
	}

	public long getAverageTxInByte() {
		return averageTxInByte;
	}

	public void setAverageTxInByte(long averageTx) {
		this.averageTxInByte = averageTx;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTxDropped() {
		return txDropped;
	}

	public void setTxDropped(long txDropped) {
		this.txDropped = txDropped;
	}

	public long getTxErrors() {
		return txErrors;
	}

	public void setTxErrors(long txErrors) {
		this.txErrors = txErrors;
	}

	public long getRxDropped() {
		return rxDropped;
	}

	public void setRxDropped(long rxDropped) {
		this.rxDropped = rxDropped;
	}

	public long getRxErrors() {
		return rxErrors;
	}

	public void setRxErrors(long rxErrors) {
		this.rxErrors = rxErrors;
	}

	public long getSpeedInBit() {
		return speedInBit;
	}

	public void setSpeedInBit(long speed) {
		this.speedInBit = speed;
	}



}
