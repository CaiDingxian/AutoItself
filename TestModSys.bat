@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  TestModSys startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..
set JAVA_HOME=../p

@rem Add default JVM options here. You can also use JAVA_OPTS and TEST_MOD_SYS_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS="-Dfile.encoding=UTF-8"

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\TestModSys-1.0.jar;%APP_HOME%\lib\js-19.0.2.jar;%APP_HOME%\lib\regex-19.0.2.jar;%APP_HOME%\lib\truffle-api-19.0.2.jar;%APP_HOME%\lib\js-scriptengine-19.0.2.jar;%APP_HOME%\lib\graal-sdk-19.0.2.jar;%APP_HOME%\lib\spring-webmvc-5.0.10.RELEASE.jar;%APP_HOME%\lib\kotlin-reflect-1.3.10.jar;%APP_HOME%\lib\kotlin-stdlib-jdk8-1.3.10.jar;%APP_HOME%\lib\jna-platform-5.4.0.jar;%APP_HOME%\lib\jna-5.4.0.jar;%APP_HOME%\lib\jetty-annotations-9.4.14.v20181114.jar;%APP_HOME%\lib\jetty-plus-9.4.14.v20181114.jar;%APP_HOME%\lib\jetty-webapp-9.4.14.v20181114.jar;%APP_HOME%\lib\jetty-servlet-9.4.14.v20181114.jar;%APP_HOME%\lib\jetty-security-9.4.14.v20181114.jar;%APP_HOME%\lib\jetty-server-9.4.14.v20181114.jar;%APP_HOME%\lib\javax.servlet-api-3.1.0.jar;%APP_HOME%\lib\spring-context-5.0.10.RELEASE.jar;%APP_HOME%\lib\spring-aop-5.0.10.RELEASE.jar;%APP_HOME%\lib\spring-web-5.0.10.RELEASE.jar;%APP_HOME%\lib\spring-beans-5.0.10.RELEASE.jar;%APP_HOME%\lib\spring-expression-5.0.10.RELEASE.jar;%APP_HOME%\lib\spring-core-5.0.10.RELEASE.jar;%APP_HOME%\lib\kotlin-stdlib-jdk7-1.3.10.jar;%APP_HOME%\lib\kotlin-stdlib-1.3.10.jar;%APP_HOME%\lib\jetty-xml-9.4.14.v20181114.jar;%APP_HOME%\lib\javax.annotation-api-1.2.jar;%APP_HOME%\lib\asm-commons-7.0.jar;%APP_HOME%\lib\asm-util-6.2.1.jar;%APP_HOME%\lib\asm-analysis-7.0.jar;%APP_HOME%\lib\asm-tree-7.0.jar;%APP_HOME%\lib\asm-7.0.jar;%APP_HOME%\lib\icu4j-62.1.jar;%APP_HOME%\lib\spring-jcl-5.0.10.RELEASE.jar;%APP_HOME%\lib\kotlin-stdlib-common-1.3.10.jar;%APP_HOME%\lib\annotations-13.0.jar;%APP_HOME%\lib\jetty-jndi-9.4.14.v20181114.jar;%APP_HOME%\lib\jetty-http-9.4.14.v20181114.jar;%APP_HOME%\lib\jetty-io-9.4.14.v20181114.jar;%APP_HOME%\lib\jetty-util-9.4.14.v20181114.jar

@rem Execute TestModSys
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %TEST_MOD_SYS_OPTS%  -classpath "%CLASSPATH%" blockman.server.js.JsEngine %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable TEST_MOD_SYS_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%TEST_MOD_SYS_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
