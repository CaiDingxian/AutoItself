package.path = (package.path .. ";D:/DEV/TestModSys0/TestModSys/build/explodedWar/WEB-INF/classes/lua/?.lua")

w=win:getActiveWin()
c=getWinContent(w)
print(c)
win:closeWin(w)