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

import org.eclipse.mihini.connector.Asset;
import org.eclipse.mihini.connector.AssetFactory;
import org.osgi.service.component.ComponentContext;

public class AssetFactoryImpl implements AssetFactory {

	private Agent _agent;

	protected void activate(ComponentContext cctx) {
		this._agent = (Agent) cctx.locateService("agent");
	}

	protected void deactivate() {
	}

	@Override
	public Asset createAsset(String assetId) {
		return new AssetImpl(assetId, _agent);
	}
}
