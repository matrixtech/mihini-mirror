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

import org.eclipse.mihini.connector.NewSmsListener;
import org.eclipse.mihini.connector.SMS_FORMAT;
import org.eclipse.mihini.connector.SmsBearer;
import org.eclipse.mihini.connector.SmsListener;
import org.json.JSONArray;

public class SmsBearerImpl implements SmsBearer {

	private Agent _agent;
	private Map<SmsListener, Integer> _smsListeners = new HashMap<SmsListener, Integer>();

	@Override
	public void sendSms(String recipientNumber, String payload,
			SMS_FORMAT smsFormat) {
		JSONArray params = new JSONArray();
		params.put(recipientNumber);
		params.put(payload);
		params.put(smsFormat.toString());
		_agent.writeCommand(CommandConstants.SEND_SMS_COMMAND,
				params.toString());
	}

	@Override
	public void addSmsListener(String phoneNumberPattern,
			String messagePattern, final SmsListener smsListener) {
		JSONArray params = new JSONArray();
		params.put(phoneNumberPattern);
		params.put(messagePattern);
		_agent.writeCommand(CommandConstants.REGISTER_SMS_LISTENER_COMMAND,
				params.toString());

		// TODO must retrieve real registration ID
		final int registrationId = 20;

		_smsListeners.put(smsListener, registrationId);
		_agent.addNewSmsListener(new NewSmsListener() {
			@Override
			public void newSms(String senderNumber, String payload, Integer id) {
				if (id == registrationId) {
					smsListener.smsReceived(senderNumber, payload);
				}
			}
		});
	}

	@Override
	public void removeSmsListener(SmsListener smsListener) {
		_agent.writeCommand(CommandConstants.UNREGISTER_SMS_LISTENER_COMMAND,
				new Integer(_smsListeners.get(smsListener)).toString());
		_smsListeners.remove(smsListener);
	}

	public void setAgent(Agent agent) {
		_agent = agent;
	}

}
