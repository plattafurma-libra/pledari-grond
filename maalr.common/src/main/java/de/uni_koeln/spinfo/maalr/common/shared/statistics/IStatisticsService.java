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

/**
 * This interface is an extension point to provide information about the server status.
 * During startup, the system tries to find a class implementing this interface. If an 
 * implementation was found, the details will be displayed in the admin backend.
 * 
 * @author sschwieb
 *
 */
public interface IStatisticsService {

	/**
	 * Returns the current {@link SystemSummary}, providing information
	 * about system load, disc capacities, etc.
	 * @return
	 */
	public SystemSummary getCurrent();

}
