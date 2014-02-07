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
package de.uni_koeln.spinfo.maalr.lucene.core;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexCommandQueue {

	private ExecutorService executor;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static IndexCommandQueue instance;
	
	public static synchronized IndexCommandQueue getInstance() {
		if(instance == null) {
			instance = new IndexCommandQueue();
		}
		return instance;
	}
	
	private IndexCommandQueue() {
		ThreadFactory factory = new ThreadFactory() {
			
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setName("Index-Queue");
				return t;
			}
		};
		executor = Executors.newSingleThreadExecutor(factory);
		logger.info("Index Command Queue initialized");
	}
	
	public void pushMulti(final List<IndexOperation> operation) throws Exception {
		logger.info("Received " + operation.size() + " operations");
		Callable<Void> callable = new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				try {
					for (IndexOperation op : operation) {
						op.execute();
					}
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			}
		};
		Future<Void> future = executor.submit(callable);
		try {
			future.get();
			return;
		} catch (ExecutionException e) {
			if(e.getCause() != null && (e.getCause() instanceof Exception)) {
				throw (Exception) e.getCause();
			} else {
				throw e;
			}
		}
	}
	
	public void push(final IndexOperation operation) throws Exception {
		Callable<Void> callable = new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				try {
					operation.execute();
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			}
		};
		Future<Void> future = executor.submit(callable);
		try {
			future.get();
			return;
		} catch (ExecutionException e) {
			if(e.getCause() != null && (e.getCause() instanceof Exception)) {
				throw (Exception) e.getCause();
			} else {
				throw e;
			}
		}
	}

}
