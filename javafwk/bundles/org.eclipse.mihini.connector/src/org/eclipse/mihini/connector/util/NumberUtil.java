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
package org.eclipse.mihini.connector.util;

public final class NumberUtil {
	private NumberUtil() {
	}

	// return the int value of the byte array given as parameter
	// bytes must have a size of 2 or 4
	public static int bytesToInt(byte[] bytes) {
		if (bytes.length == 4) {
			return ((bytes[0] & 0xFF) << 24) + ((bytes[1] & 0xFF) << 16)
					+ ((bytes[2] & 0xFF) << 8) + (bytes[3] & 0xFF);
		} else if (bytes.length == 2) {
			return ((bytes[0] & 0xFF) << 8) + (bytes[1] & 0xFF);
		} else {
			return -1;
		}
	}

	/*
	 * converts the value given as parameter to a 'size' bytes array
	 */
	public static byte[] intToBytes(int size, int value) {
		byte[] buff = null;
		if (size == 2) {
			buff = new byte[size];
			buff[0] = (byte) (value >> 8);
			buff[1] = (byte) value;
		} else if (size == 4) {
			buff = new byte[size];
			buff[0] = (byte) (value >> 24);
			buff[1] = (byte) (value >> 16);
			buff[2] = (byte) (value >> 8);
			buff[3] = (byte) value;
		}
		return buff;
	}
}
