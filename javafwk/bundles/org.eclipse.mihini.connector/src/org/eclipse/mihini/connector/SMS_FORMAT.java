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

public enum SMS_FORMAT {
	_8_BITS("8bits"), _7_BITS("7bits"), _UCS2("ucs2");

	private String enc;

	private SMS_FORMAT(String enc) {
		this.enc = enc;
	}

	@Override
	public String toString() {
		return this.enc;
	}

}
