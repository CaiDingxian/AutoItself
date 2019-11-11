var a = act.msgBox("执行测试", "打开记事本？")
if (a == 6) {
    act.exec("Notepad")
} else {
    act.msgBox("不打开", "点此结束进程")
}

var win = act.findWin(null, "DEV")
act.fixWinTop(win)
//act.sendClick(win,367,482);
print("OK")

