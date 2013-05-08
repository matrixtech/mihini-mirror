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
package org.eclipse.mihini.connector.impl;

import java.util.Arrays;

public class RequestIdGenerator {

	public class Request {
		public int id;

		public Request(int id) {
			this.id = id;
		}
	}

	/**
	 * an array of boolean; <code>true</code> means a request is pending for the
	 * associated index/ID. The boolean objects can also be used to lock for
	 * synchronous command execution
	 */
	private Request[] _pendingRequests;

	public RequestIdGenerator(int maxRequestsInParallel) {
		_pendingRequests = new Request[maxRequestsInParallel];
		Arrays.fill(_pendingRequests, null);
	}

	public Request getRequest() throws Exception {
		synchronized (_pendingRequests) {
			for (int i = 0; i < _pendingRequests.length; i++) {
				if (_pendingRequests[i] == null) {
					_pendingRequests[i] = new Request(i);
					return _pendingRequests[i];
				}
			}
			throw new Exception(
					"Max number of pending requests exhausted. Try again later.");
		}
	}

	public void freeRequest(int requestId) {
		synchronized (_pendingRequests[requestId]) {
			_pendingRequests[requestId].notify();
			_pendingRequests[requestId] = null;
		}
	}
}
