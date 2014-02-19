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
package de.uni_koeln.spinfo.maalr.mongo.core;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5OutputStream extends OutputStream {
	
	private OutputStream output;
	private MessageDigest md5;

	public MD5OutputStream(OutputStream output) throws NoSuchAlgorithmException {
		this.output = output;
		md5 = MessageDigest.getInstance("MD5");
	}

	@Override
	public void write(int i) throws IOException {
        byte[] b = { (byte) i };
        md5.update(b);
        output.write(b, 0, 1);
    }

    @Override
	public void write(byte[] b, int off, int len) throws IOException {
    	md5.update(b, off, len);
    	output.write(b, off, len);
	}

	@Override
	public void write(byte[] b) throws IOException {
		md5.update(b);
		output.write(b);
	}

	public String getHash() {
		byte[] digest = md5.digest();
		StringBuilder sb = new StringBuilder();
		for (byte b : digest) {
			sb.append(Integer.toHexString((int) (b & 0xff)));
		}
		return sb.toString();
    }

}
