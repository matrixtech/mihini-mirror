#*******************************************************************************
# Copyright (c) 2012 Sierra Wireless and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# and Eclipse Distribution License v1.0 which accompany this distribution.
#
# The Eclipse Public License is available at
#   http://www.eclipse.org/legal/epl-v10.html
# The Eclipse Distribution License is available at
#   http://www.eclipse.org/org/documents/edl-v10.php
#
# Contributors:
#     Sierra Wireless - initial API and implementation
#*******************************************************************************

ADD_LUA_LIBRARY(agent_update DESTINATION agent/update
    init.lua builtinupdaters.lua pkgcheck.lua updatemgr.lua common.lua downloader.lua status.lua
)

ADD_DEPENDENCIES(agent_update racon crypto_md5)

ADD_LUA_LIBRARY(agent_update_tools EXCLUDE_FROM_ALL DESTINATION agent/update/tools
    tools/createpkg.lua
)

INSTALL(FILES init.lua builtinupdaters.lua pkgcheck.lua updatemgr.lua common.lua downloader.lua status.lua DESTINATION lua/agent/update)
INSTALL(FILES tools/createpkg.lua DESTINATION lua/agent/update/tools)
