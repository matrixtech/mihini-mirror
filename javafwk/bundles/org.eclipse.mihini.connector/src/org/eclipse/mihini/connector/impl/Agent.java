package org.eclipse.mihini.connector.impl;

import java.util.Map;

import org.eclipse.mihini.connector.Asset;
import org.eclipse.mihini.connector.NewSmsListener;
import org.json.JSONObject;

/**
 * This API is not public
 */
public interface Agent {
	void writeCommand(int sendSmsCommand, String string);

	void registerAsset(AssetImpl asset);

	void unregisterAsset(Asset asset);

	void pushData(Asset asset, String path, Map<String, Object> data,
			String policy);

	void notifyAssetData(String path, JSONObject body);

	void addNewSmsListener(NewSmsListener newSmsListener);

	void notifySms(String senderNumber, String payload, int registrationId);

}
