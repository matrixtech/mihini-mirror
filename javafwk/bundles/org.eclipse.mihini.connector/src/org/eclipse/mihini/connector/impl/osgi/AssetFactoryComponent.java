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
package org.eclipse.mihini.connector.impl.osgi;

import org.eclipse.mihini.connector.Asset;
import org.eclipse.mihini.connector.AssetFactory;
import org.eclipse.mihini.connector.impl.Agent;
import org.eclipse.mihini.connector.impl.AssetFactoryImpl;
import org.osgi.service.component.ComponentContext;

public class AssetFactoryComponent implements AssetFactory {

	private AssetFactory _assetFactory;

	protected void activate(ComponentContext cctx) {
		this._assetFactory = new AssetFactoryImpl(
				(Agent) cctx.locateService("agent"));
	}

	protected void deactivate() {
		_assetFactory = null;
	}

	public Asset createAsset(String assetId) {
		return _assetFactory.createAsset(assetId);
	}

}
