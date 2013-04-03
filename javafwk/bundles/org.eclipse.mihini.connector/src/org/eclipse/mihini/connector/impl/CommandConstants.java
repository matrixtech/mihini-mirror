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

public final class CommandConstants {
	private CommandConstants() {
	}

	public final static int SEND_DATA_COMMAND = 1;
	public final static int REGISTER_COMMAND = 2;
	public final static int UNREGISTER_COMMAND = 3;
	public final static int FORCE_CONNECT_COMMAND = 4;
	public final static int REGISTER_SMS_LISTENER_COMMAND = 7;
	public final static int NEW_SMS_COMMAND = 8;
	public static final int PDATA_COMMAND = 30;
	public static final int PFLUSH_COMMAND = 32;
	public static final int REBOOT_COMMAND = 50;
	public final static int UNREGISTER_SMS_LISTENER_COMMAND = 51;
	public static final int SEND_SMS_COMMAND = 52;

}
