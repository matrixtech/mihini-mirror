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

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.mihini.connector.util.NumberUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AgentSocketReader implements Runnable {
	private RequestIdGenerator _requestIdGenerator;
	private InputStream _inputStream;
	private Agent _agent;

	private boolean _mustDie = false;

	public AgentSocketReader(RequestIdGenerator requestIdGenerator,
			InputStream is, Agent agent) {
		_requestIdGenerator = requestIdGenerator;
		_inputStream = is;
		_agent = agent;
	}

	@Override
	public void run() {
		while (true && !_mustDie) {
			try {
				// EMP header fields
				byte[] cmdId = new byte[2];
				byte[] type = new byte[1];
				byte[] requestId = new byte[1];
				byte[] payloadSize = new byte[4];

				// read command id
				int read = _inputStream.read(cmdId, 0, 2);
				if (read != 2 && read != -1) {
					throw new IOException(
							"Incorrect message from agent, incorrect command id");
				}

				// read type
				read = _inputStream.read(type, 0, 1);
				if (read != 1 && read != -1) {
					throw new IOException(
							"Incorrect message from agent, incorrect type size");
				}

				// read request ID
				read = _inputStream.read(requestId, 0, 1);
				if (read != 1 && read != -1) {
					throw new IOException(
							"Incorrect message from agent, incorrect request ID size");
				}

				// read the payload size
				read = _inputStream.read(payloadSize, 0, 4);
				if (read != 4 && read != -1) {
					throw new IOException(
							"Incorrect message from agent, incorrect payload size");
				}

				// end of the stream encountered, throw IO exception
				if (read == -1) {
					throw new IOException("Connection to agent lost");
				}

				int size = NumberUtil.bytesToInt(payloadSize);
				byte[] payload = null;
				if (size > 0) {
					payload = new byte[size];
					read = _inputStream.read(payload, 0, size);
					while (read != size) {
						read += _inputStream.read(payload, read, size - read);
					}
				}

				int commandId = NumberUtil.bytesToInt(cmdId);

				switch (commandId) {
				case CommandConstants.NEW_SMS_COMMAND:
					try {
						JSONArray array = new JSONArray(new String(payload));
						_agent.notifySms(array.getString(0),
								array.getString(1), array.getInt(2));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case CommandConstants.SEND_DATA_COMMAND:
					try {
						JSONObject o = new JSONObject(new String(payload));
						String path = o.getString("path");
						JSONObject body = (JSONObject) o.get("body");

						_agent.notifyAssetData(path, body);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
				}

				// if we received a response, free the corresponding request ID
				if (type[0] == 1)
					_requestIdGenerator.freeRequest(requestId[0]);

			} catch (IOException e) {
				// TODO handle exception
			}

		}
	}

	protected void kill() {
		_mustDie = true;
		try {
			_inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
