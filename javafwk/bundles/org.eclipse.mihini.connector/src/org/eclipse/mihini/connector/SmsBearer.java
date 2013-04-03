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
package org.eclipse.mihini.connector;

public interface SmsBearer {
	void sendSms(String recipientNumber, String payload, SMS_FORMAT smsFormat);

	void addSmsListener(String phoneNumberPattern, String messagePattern,
			SmsListener smsListener);

	void removeSmsListener(SmsListener smsListener);
}
