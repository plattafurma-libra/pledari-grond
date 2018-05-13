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
package de.uni_koeln.spinfo.maalr.mongo.stats;

import java.io.Serializable;

/**
 * Wrapper class for GWT module. A light version of a File representation.
 * 
 * @author Mihail Atanassov (atanassov.mihail@gmail.com)
 * 
 */
public class FileInfo implements Serializable {

	private static final long serialVersionUID = -7867093437185012413L;

	private String absolutePath;
	private String parent;
	private String name;
	private String creationDate;
	private String size;
	private String lastModified;

	public FileInfo() {
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public FileInfo absolutePath(String absolutePath) {
		this.setAbsolutePath(absolutePath);
		return this;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public String getParent() {
		return parent;
	}

	public FileInfo parent(String parent) {
		this.setParent(parent);
		return this;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public FileInfo name(String name) {
		this.setName(name);
		return this;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastModified() {
		return lastModified;
	}

	public FileInfo lastModified(String lastModified) {
		this.setLastModified(lastModified);
		return this;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public String getSize() {
		return size;
	}

	public FileInfo size(String size) {
		this.setSize(size);
		return this;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getCreationDate() {
		return creationDate;
	}
	
	public FileInfo creationDate(String creationDate) {
		this.setCreationDate(creationDate);
		return this;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((absolutePath == null) ? 0 : absolutePath.hashCode());
		result = prime * result
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result
				+ ((lastModified == null) ? 0 : lastModified.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result + ((size == null) ? 0 : size.hashCode());
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
		FileInfo other = (FileInfo) obj;
		if (absolutePath == null) {
			if (other.absolutePath != null)
				return false;
		} else if (!absolutePath.equals(other.absolutePath))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (lastModified == null) {
			if (other.lastModified != null)
				return false;
		} else if (!lastModified.equals(other.lastModified))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FileInfo [absolutePath=" + absolutePath + ", parent=" + parent
				+ ", name=" + name + ", lastModified=" + lastModified
				+ ", size=" + size + "]";
	}

}
