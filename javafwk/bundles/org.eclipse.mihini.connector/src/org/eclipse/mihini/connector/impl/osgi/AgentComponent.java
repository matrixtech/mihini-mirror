package org.eclipse.mihini.connector.impl.osgi;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

import org.eclipse.mihini.connector.Asset;
import org.eclipse.mihini.connector.NewSmsListener;
import org.eclipse.mihini.connector.impl.Agent;
import org.eclipse.mihini.connector.impl.AgentImpl;
import org.eclipse.mihini.connector.impl.AssetImpl;
import org.json.JSONObject;

public class AgentComponent implements Agent {
	private Agent _agent;

	protected void activate(Map<?, ?> config) {
		try {
			_agent = new AgentImpl();
			modified(config);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void modified(Map<?, ?> config) {
		try {
			((AgentImpl) _agent).reconnectToAgent(
					(String) config.get("agent.host"),
					(Integer) config.get("agent.port"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void pushData(Asset asset, String path, Map<String, Object> data,
			String policy) {
		_agent.pushData(asset, path, data, policy);
	}

	public void unregisterAsset(Asset asset) {
		_agent.unregisterAsset(asset);
	}

	public void registerAsset(AssetImpl asset) {
		_agent.registerAsset(asset);
	}

	public void notifyAssetData(String path, JSONObject body) {
		_agent.notifyAssetData(path, body);
	}

	public void notifySms(String senderNumber, String payload,
			int registrationId) {
		_agent.notifySms(senderNumber, payload, registrationId);
	}

	public void writeCommand(int sendSmsCommand, String string) {
		_agent.writeCommand(sendSmsCommand, string);
	}

	public void addNewSmsListener(NewSmsListener newSmsListener) {
		_agent.addNewSmsListener(newSmsListener);
	}

}
