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
import org.osgi.service.component.ComponentContext;

public class Component {
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

		Asset assetGreenhouse = (Asset) cctx.locateService("asset-greenhouse");
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("bar", 10);
		assetGreenhouse.pushData("foo", values, "now");
	}

	protected void deactivate() {
	}

}
