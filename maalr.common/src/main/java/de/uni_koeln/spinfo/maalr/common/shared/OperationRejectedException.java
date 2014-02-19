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


public class OperationRejectedException extends Exception {

	private static final long serialVersionUID = 3260261018146856315L;

	public OperationRejectedException() {
		super();
	}
	
	public OperationRejectedException(Throwable e) {
		super(e);
	}

	public OperationRejectedException(String message, Throwable cause) {
		super(message, cause);
	}

	public OperationRejectedException(String message) {
		super(message);
	}

}
