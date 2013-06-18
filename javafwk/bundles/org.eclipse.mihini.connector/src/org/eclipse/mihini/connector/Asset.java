/*******************************************************************************
 * Copyright (c) 2013 Sierra Wireless and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/
package org.eclipse.mihini.connector;

import java.util.Map;

public interface Asset {
	public interface DataHandler {
		Object value();
	}

	String getAssetId();

	void unregister();

	/**
	 * Push some unstructured data to the data manager
	 * 
	 * @param path
	 *            the datastore path under which data will be stored relative to
	 *            the asset node (can be <code>null</code>)
	 * @param a
	 *            {@link Map} of key/value pairs
	 * @param queue
	 *            name of the policy controlling when the data must be sent to
	 *            the server, if <code>null</code> then the default policy is
	 *            used
	 */
	void pushData(String path, Map<String, Object> data, String queue);

	/**
	 * This method allows to register a handler in order to manage requests
	 * coming from a server to read the value of node
	 * 
	 * @param path
	 *            the path of the value to read
	 * @param handler
	 *            a {@link DataHandler} responsible for providing the value of
	 *            the requested data. {@link DataHandler#value()} will be called
	 *            upon server request
	 */
	void registerDataHandler(String path, DataHandler handler);

	// addDataListener(String prefix, DataListener dataListener);
}
