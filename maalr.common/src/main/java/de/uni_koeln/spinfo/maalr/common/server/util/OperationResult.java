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
package de.uni_koeln.spinfo.maalr.common.server.util;

public class OperationResult {
	
	private IDBOperation operation;
	
	private boolean success;
	
	private Exception failure;

	public OperationResult(IDBOperation operation, boolean success, Exception error) {
		this.operation = operation;
		this.success = success;
		this.failure = error;
	}

	public IDBOperation getOperation() {
		return operation;
	}

	public boolean isSuccess() {
		return success;
	}

	public Exception getFailure() {
		return failure;
	}

	public static OperationResult success(IDBOperation operation) {
		return new OperationResult(operation, true, null);
	}

}
