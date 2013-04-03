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
import org.eclipse.mihini.connector.NewSmsListener;
import org.eclipse.mihini.connector.SmsBearer;
import org.eclipse.mihini.connector.SmsListener;
import org.eclipse.mihini.connector.impl.RequestIdGenerator.Request;
import org.eclipse.mihini.connector.util.NumberUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class Agent {

	protected Socket _socket;

	private RequestIdGenerator _requestIdGenerator;

	private List<NewSmsListener> _newSmsListeners = new ArrayList<NewSmsListener>();

	/* lock for socket's OutputStream access */
	private static final Object LOCK_STREAM = new Object();

	public Agent() {
		try {
			_requestIdGenerator = new RequestIdGenerator(256);
			_socket = new Socket("localhost", 9999);
			AgentSocketReader agentSocketReader = new AgentSocketReader(
					_requestIdGenerator, _socket.getInputStream(), this);
			new Thread(agentSocketReader).start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Asset registerAsset(String assetId) {
		writeCommand(CommandConstants.REGISTER_COMMAND, "\"" + assetId + "\"");
		return new AssetImpl(assetId, this);
	}

	public void unregisterAsset(Asset asset) {
		writeCommand(CommandConstants.UNREGISTER_COMMAND,
				"\"" + asset.getAssetId() + "\"");
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

	protected void notifySms(String senderNumber, String payload,
			int registrationId) {
		synchronized (_newSmsListeners) {
			for (NewSmsListener newSmsListener : _newSmsListeners) {
				newSmsListener.newSms(senderNumber, payload, registrationId);
			}
		}
	}

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
				System.out.println("ANSWERED!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		final Agent agent = new Agent();
		// agent.connectToServer(10);

		agent.reboot("ca va?");
		Asset asset = agent.registerAsset("theasset");

		// ((AgentImpl) agent).sendSms("+33619196101", "sdklfsf",
		// SMS_FORMAT._8_BITS);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("one", 20.0);
		data.put("two", 40.0);
		asset.pushData("", data, "now");

		asset.unregister();

		agent.flushPolicy("daily");

		SmsBearer smsBearer = new SmsBearerImpl();
		((SmsBearerImpl) smsBearer).setAgent(agent);

		Runnable r = new Runnable() {
			@Override
			public void run() {
				agent.reboot("xxx");
				agent.reboot("xxx");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				agent.reboot("xxx");
				agent.reboot("xxx");
			}
		};

		// for (int i = 0; i < 20; i++) {
		// Thread t = new Thread(r);
		// t.start();
		// }

		SmsListener smsListener = new SmsListener() {
			@Override
			public void smsReceived(String senderNumber, String payload) {
				System.out.println("Received an SMS from:" + senderNumber);
			}
		};
		smsBearer.addSmsListener("+33.*", ".*", smsListener);

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		smsBearer.removeSmsListener(smsListener);

	}
}
