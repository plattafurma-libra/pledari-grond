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
package de.uni_koeln.spinfo.maalr.common.shared;

import java.io.Serializable;
import java.util.Arrays;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Status;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Verification;


public class EditorQuery implements Serializable {
	
	public static final long ONE_DAY = 1000 * 60 * 60 * 24;
	
	public static final long HALF_YEAR = ONE_DAY * 31 * 6;
	
	//private static final long ONE_MONTH = ONE_DAY * 30;
	
	private static final long serialVersionUID = -5305758232516537426L;

	/**
	 * The number of items to fetch in a single query
	 */
	private int pageSize = 20;
	
	/**
	 * The current offset
	 */
	private int current = 0;
	

	private Status[] state = new Status[] {Status.NEW_ENTRY, Status.NEW_MODIFICATION};
	
	/**
	 * The current text (first name, last name, email, login) used for filtering results
	 */
	private String userOrIp;
	
	/**
	 * The current sort column
	 */
	private String sortColumn = LemmaVersion.TIMESTAMP;
	
	/**
	 * Whether or not sort ascending
	 */
	private boolean sortAscending = false;
	

	private long startTime = System.currentTimeMillis() - HALF_YEAR;
	
	private Role role;
	
	private long endTime;

	private Verification verification;

	private String verifier;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + current;
		result = prime * result + (int) (endTime ^ (endTime >>> 32));
		result = prime * result + pageSize;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + (sortAscending ? 1231 : 1237);
		result = prime * result
				+ ((sortColumn == null) ? 0 : sortColumn.hashCode());
		result = prime * result + (int) (startTime ^ (startTime >>> 32));
		result = prime * result + Arrays.hashCode(state);
		result = prime * result
				+ ((userOrIp == null) ? 0 : userOrIp.hashCode());
		result = prime * result
				+ ((verification == null) ? 0 : verification.hashCode());
		result = prime * result
				+ ((verifier == null) ? 0 : verifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EditorQuery other = (EditorQuery) obj;
		if (current != other.current)
			return false;
		if (endTime != other.endTime)
			return false;
		if (pageSize != other.pageSize)
			return false;
		if (role != other.role)
			return false;
		if (sortAscending != other.sortAscending)
			return false;
		if (sortColumn == null) {
			if (other.sortColumn != null)
				return false;
		} else if (!sortColumn.equals(other.sortColumn))
			return false;
		if (startTime != other.startTime)
			return false;
		if (!Arrays.equals(state, other.state))
			return false;
		if (userOrIp == null) {
			if (other.userOrIp != null)
				return false;
		} else if (!userOrIp.equals(other.userOrIp))
			return false;
		if (verification != other.verification)
			return false;
		if (verifier == null) {
			if (other.verifier != null)
				return false;
		} else if (!verifier.equals(other.verifier))
			return false;
		return true;
	}

	public EditorQuery getCopy() {
		EditorQuery copy = new EditorQuery();
		copy.pageSize = pageSize;
		copy.current = current;
		copy.state = state;
		copy.userOrIp = userOrIp;
		copy.sortColumn = sortColumn;
		copy.sortAscending = sortAscending;
		copy.startTime = startTime;
		copy.role = role;
		copy.endTime = endTime;
		copy.verification = verification;
		copy.verifier = verifier;
		return copy;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	public int getCurrent() {
		return current;
	}

	public Status[] getState() {
		return state;
	}

	public String getUserOrIp() {
		return userOrIp;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public boolean isSortAscending() {
		return sortAscending;
	}

	public long getStartTime() {
		return startTime;
	}

	public Role getRole() {
		return role;
	}

	public long getEndTime() {
		return endTime;
	}

	public Verification getVerification() {
		return verification;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	
	public void setUserOrIp(String text) {
		this.userOrIp = text;
	}

	public void setState(Status[] state) {
		this.state = state;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	public void setOrder(String order, boolean ascending) {
		this.sortColumn = order;
		this.sortAscending = ascending;
	}

	public void setVerification(Verification verification2) {
		this.verification = verification2;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setVerifier(String verifier) {
		this.verifier = verifier;
	}

	public String getVerifier() {
		return verifier;
	}
	
	
}
