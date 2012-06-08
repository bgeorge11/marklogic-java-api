/*
 * Copyright 2012 MarkLogic Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.marklogic.client.io;

import com.marklogic.client.ContentDescriptor;
import com.marklogic.client.Format;

public abstract class BaseHandle<R,W>
    implements ContentDescriptor
{
	private Format format = Format.UNKNOWN;
	private String mimetype;
	private long length = UNKNOWN_LENGTH;

	public BaseHandle() {
		super();
	}

	@Override
	public Format getFormat() {
		return format;
	}
	@Override
	public void setFormat(Format format) {
		this.format = format;
	}

	@Override
	public String getMimetype() {
		if (mimetype == null && format != null)
			return format.getDefaultMimetype();

		return mimetype;
	}
	@Override
	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	@Override
	public long getByteLength() {
		return length;
	}
	@Override
	public void setByteLength(long length) {
		this.length = length;
	}

	/**
	 * As part of the contract between a read handle and the API, 
	 * declares the class of the content received from the database.
	 * You should rarely if ever need to call this method directly when using the handle.
	 * @return
	 */
	protected Class<R> receiveAs() {
		throw new UnsupportedOperationException(this.getClass().getName()+" cannot receive content");
	}
	/**
	 * As part of the contract between a read handle and the API, 
	 * receives content from the database.  You should rarely
	 * if ever need to call this method directly when using the handle.
	 * @return
	 */
	protected void receiveContent(R content) {
		throw new UnsupportedOperationException(this.getClass().getName()+" cannot receive content");
	}

	/**
	 * As part of the contract between a write handle and the API, 
	 * sends content to the database.  You should rarely
	 * if ever need to call this method directly when using the handle.
	 * @return
	 */
	protected W sendContent() {
		throw new UnsupportedOperationException(this.getClass().getName()+" cannot send content");
	}

}