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

ADD_LUA_LIBRARY(agent_asscon DESTINATION agent/asscon
    init.lua datamanager.lua sms.lua)

ADD_DEPENDENCIES(agent_asscon
    sched pack stagedb persist m3da_bysant messaging mime returncodes)
INSTALL(FILES init.lua datamanager.lua sms.lua DESTINATION lua/agent/asscon)
