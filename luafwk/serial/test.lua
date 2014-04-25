--------------------------------------------------------------------------------
-- Copyright (c) 2012 Sierra Wireless and others.
-- All rights reserved. This program and the accompanying materials
-- are made available under the terms of the Eclipse Public License v1.0
-- and Eclipse Distribution License v1.0 which accompany this distribution.
--
-- The Eclipse Public License is available at
--   http://www.eclipse.org/legal/epl-v10.html
-- The Eclipse Distribution License is available at
--   http://www.eclipse.org/org/documents/edl-v10.php
--
-- Contributors:
--     Cuero Bugot for Sierra Wireless - initial API and implementation
-------------------------------------------------------------------------------

local sched = require 'sched'
local at = require 'at'
require 'print'
require 'shell'


local function test()

    at.init(0)
    print("Started")

end

sched.run(shell.telnet_server, 2000)


sched.run(test)

sched.loop()


function gc()
    collectgarbage 'collect'
    return collectgarbage 'count'
end