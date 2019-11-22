package.path = (package.path .. ";D:/DEV/TestModSys0/TestModSys/build/explodedWar/WEB-INF/classes/lua/?.lua")
local a = require("functions")
local function sum(a, b)
    return a + b
end

local info = debug.getinfo(sum)

for k, v in pairs(info) do
    print(k, ':', info[k])
end

print(a.test1())