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
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.mihini.connector.Asset;
import org.eclipse.mihini.connector.Asset.DataHandler;
import org.eclipse.mihini.connector.NewSmsListener;
import org.eclipse.mihini.connector.impl.RequestIdGenerator.Request;
import org.eclipse.mihini.connector.util.NumberUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class AgentImpl implements Agent {

	protected Socket _socket;

	private RequestIdGenerator _requestIdGenerator;

	private List<NewSmsListener> _newSmsListeners = new ArrayList<NewSmsListener>();

	private Map<String, AssetImpl> _assets = new HashMap<String, AssetImpl>();

	/* lock for socket's OutputStream access */
	private static final Object LOCK_STREAM = new Object();

	private AgentSocketReader _agentSocketReader;

	private Thread _agentSocketReaderThread;

	public AgentImpl() throws UnknownHostException, IOException {
		this("localhost", 9999);
	}

	public AgentImpl(String host, int port) throws UnknownHostException,
			IOException {
		reconnectToAgent(host, port);
	}

	public void reconnectToAgent(String host, int port)
			throws UnknownHostException, IOException {
		if (_agentSocketReader != null) {
			_agentSocketReader.kill();
			try {
				_agentSocketReaderThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		_requestIdGenerator = new RequestIdGenerator(256);
		_socket = new Socket(host, port);
		_agentSocketReader = new AgentSocketReader(_requestIdGenerator,
				_socket.getInputStream(), this);
		_agentSocketReaderThread = new Thread(_agentSocketReader);
		_agentSocketReaderThread.start();
	}

	@Override
	public void registerAsset(AssetImpl asset) {
		writeCommand(CommandConstants.REGISTER_COMMAND,
				"\"" + asset.getAssetId() + "\"");
		_assets.put(asset.getAssetId(), asset);
	}

	@Override
	public void unregisterAsset(Asset asset) {
		writeCommand(CommandConstants.UNREGISTER_COMMAND,
				"\"" + asset.getAssetId() + "\"");
		_assets.remove(asset.getAssetId());
	}

	public void flushPolicy(String policyName) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("policy", policyName);
			writeCommand(CommandConstants.PFLUSH_COMMAND, obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void pushData(Asset asset, String path, Map<String, Object> data,
			String policy) {
		try {
			JSONObject map = new JSONObject();
			map.put("asset", asset.getAssetId());
			map.put("policy", policy);
			map.put("path", path);
			map.put("data", data);
			writeCommand(CommandConstants.PDATA_COMMAND, map.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void connectToServer() {
		writeCommand(CommandConstants.FORCE_CONNECT_COMMAND, null);
	}

	public void connectToServer(int delay) {
		writeCommand(CommandConstants.FORCE_CONNECT_COMMAND,
				new Integer(delay).toString());
	}

	public void reboot(String reason) {
		writeCommand(CommandConstants.REBOOT_COMMAND, "\"" + reason + "\"");
	}

	@Override
	public void addNewSmsListener(NewSmsListener newSmsListener) {
		synchronized (_newSmsListeners) {
			_newSmsListeners.add(newSmsListener);
		}
	}

	public void removeNewSmsListener(NewSmsListener newSmsListener) {
		synchronized (_newSmsListeners) {
			_newSmsListeners.remove(newSmsListener);
		}
	}

	public void notifySms(String senderNumber, String payload,
			int registrationId) {
		synchronized (_newSmsListeners) {
			for (NewSmsListener newSmsListener : _newSmsListeners) {
				newSmsListener.newSms(senderNumber, payload, registrationId);
			}
		}
	}

	@Override
	public void writeCommand(int commandId, String payload) {
		synchronized (LOCK_STREAM) {
			try {
				int size = 0;
				// payload can be null
				if (payload != null) {
					size = payload.getBytes().length;
				}

				Request request = _requestIdGenerator.getRequest();
				OutputStream output = _socket.getOutputStream();

				output.write(NumberUtil.intToBytes(2, commandId));
				output.write(0); // command byte
				output.write((byte) request.id);
				byte[] pSize = NumberUtil.intToBytes(4, size);
				output.write(pSize); // payload size
				if (payload != null) {
					output.write(payload.getBytes()); // payload
				}
				output.flush();

				// request.wait();
			} catch (Exception e) {
				// TODO the commmand cannot be sent, likely because there are no
				// request# available... what should we do?
				e.printStackTrace();
			}
		}
	}

	@Override
	public void notifyAssetData(String path, JSONObject body) {
		if (path.endsWith(".commands.ReadNode")
				&& _assets.get(path.substring(0, path.indexOf('.'))) != null) {
			AssetImpl asset = _assets.get(path.substring(0, path.indexOf('.')));
			try {
				DataHandler dataHandler = asset.getDataHandlers().get(
						body.get("1"));
				if (dataHandler != null) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put((String) body.get("1"), dataHandler.value());
					asset.pushData("", map, "now");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// else {
		// ... other kind of data writings. TODO
		// }

		System.out.println("Notify asset data");
		System.out.println("path: " + path);
		System.out.println("body: " + body.toString());
	}
}
