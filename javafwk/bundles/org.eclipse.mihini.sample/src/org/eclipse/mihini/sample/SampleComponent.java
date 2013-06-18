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
package org.eclipse.mihini.sample;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.mihini.connector.Asset;
import org.eclipse.mihini.connector.AssetFactory;
import org.osgi.service.component.ComponentContext;

public class SampleComponent {
	protected void activate(ComponentContext cctx) {
		// SmsBearer smsBearer = (SmsBearer) cctx.locateService("smsBearer");
		//
		// smsBearer.addSmsListener(".*", ".*", new SmsListener() {
		// @Override
		// public void smsReceived(String senderNumber, String payload) {
		// System.out.println("Received a message from " + senderNumber
		// + ": '" + payload + "'");
		// }
		// });

		AssetFactory assetFactory = (AssetFactory) cctx
				.locateService("assetFactory");

		Asset greenhouse = assetFactory.createAsset("greenhouse");

		greenhouse.registerDataHandler("data.temperature", new Asset.DataHandler() {
			@Override
			public Object value() {
				return 20.3;
			}
		});

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("temperature", 24.4);
		values.put("luminosity", 1042);

		System.out.println("Pushing data for asset1...");
		greenhouse.pushData("data", values, "now");

		Asset asset2 = assetFactory.createAsset("asset2");
		values.clear();
		values.put("xxx", "stuff");
		values.put("yyy", "other stuff");

		System.out.println("Pushing data for asset2...");
		asset2.pushData("", values, "now");

		// System.out.println("DONE! Unregistering assets...");

		// asset1.unregister();
		// asset2.unregister();
	}

	protected void deactivate() {
	}

}
