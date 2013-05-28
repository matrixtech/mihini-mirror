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

import org.eclipse.mihini.connector.SMS_FORMAT;
import org.eclipse.mihini.connector.SmsBearer;
import org.eclipse.mihini.connector.SmsListener;
import org.eclipse.mihini.connector.impl.Agent;
import org.eclipse.mihini.connector.impl.SmsBearerImpl;
import org.osgi.service.component.ComponentContext;

public class SmsBearerComponent implements SmsBearer {

	private SmsBearer _smsBearer;

	protected void activate(ComponentContext cctx) {
		this._smsBearer = new SmsBearerImpl((Agent) cctx.locateService("agent"));
	}

	public void sendSms(String recipientNumber, String payload,
			SMS_FORMAT smsFormat) {
		_smsBearer.sendSms(recipientNumber, payload, smsFormat);
	}

	public void addSmsListener(String phoneNumberPattern,
			String messagePattern, SmsListener smsListener) {
		_smsBearer.addSmsListener(phoneNumberPattern, messagePattern,
				smsListener);
	}

	public void removeSmsListener(SmsListener smsListener) {
		_smsBearer.removeSmsListener(smsListener);
	}
}
