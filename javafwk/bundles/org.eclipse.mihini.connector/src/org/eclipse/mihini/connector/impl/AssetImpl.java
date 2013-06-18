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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.mihini.connector.Asset;

public class AssetImpl implements Asset {
	private String _assetId;
	private Agent _agent;
	private Map<String, DataHandler> _dataHandlers;

	public AssetImpl(String assetId, Agent agent) {
		_assetId = assetId;
		_agent = agent;
		_dataHandlers = new HashMap<String, Asset.DataHandler>();
		_agent.registerAsset(this);
	}

	@Override
	public String getAssetId() {
		return _assetId;
	}

	@Override
	public void unregister() {
		_agent.unregisterAsset(this);
	}

	@Override
	public void pushData(String path, Map<String, Object> data, String policy) {
		_agent.pushData(this, path, data, policy);
	}

	@Override
	public void registerDataHandler(String path, DataHandler handler) {
		_dataHandlers.put(path, handler);
	}

	public Map<String, DataHandler> getDataHandlers() {
		return _dataHandlers;
	}

}
