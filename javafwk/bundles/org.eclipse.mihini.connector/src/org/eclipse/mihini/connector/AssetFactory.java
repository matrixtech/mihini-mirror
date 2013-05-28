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

/**
 * An {@link AssetFactory} allows to retrieve instances of {@link Asset} that
 * can be used to push data, listen to commands...
 */
public interface AssetFactory {
	Asset createAsset(String assetId);
}
