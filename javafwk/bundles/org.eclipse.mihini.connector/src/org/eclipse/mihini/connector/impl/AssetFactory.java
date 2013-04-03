package org.eclipse.mihini.connector.impl;

import java.util.Map;

import org.eclipse.mihini.connector.Asset;
import org.osgi.service.component.ComponentContext;

public class AssetFactory implements Asset {

	private Agent _agent;
	private Asset _asset;

	protected void activate(ComponentContext cctx) {
		this._agent = (Agent) cctx.locateService("agent");
		System.out.println(cctx.getProperties());
		_asset = _agent.registerAsset("test");
	}

	protected void deactivate() {
	}

	@Override
	public String getAssetId() {
		return _asset.getAssetId();
	}

	@Override
	public void unregister() {
		_asset.unregister();
	}

	@Override
	public void pushData(String path, Map<String, Object> data, String queue) {
		_asset.pushData(path, data, queue);
	}

}
