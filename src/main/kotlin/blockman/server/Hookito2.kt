package blockman.server


import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.platform.win32.BaseTSD.ULONG_PTR
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef.*
import com.sun.jna.platform.win32.WinNT
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.platform.win32.WinUser.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.schedule

fun zero() = 0
/**
 * Demonstration of windows message api of complex structures like WM_COPYDATA.
 */
@Structure.FieldOrder("number", "message")
class MsgStruct : Structure {


    @JvmField
    var number: Int = 0

    @JvmField
    var message: String? = null

    constructor() : super() {
        // reads memory effectively.
        read()
    }

    constructor(p: Pointer) : super(p) {
        // reads memory effectively.
        read()
    }

//    override fun getFieldOrder(): List<String> {
//        return arrayOf("number", "message").asList()
//    }
}

class Hookito2 {

    /**
     * Gets the last error.
     *
     * @return the last error
     */
    val lastError: Int
        get() {
            val rc = Kernel32.INSTANCE.GetLastError()

            if (rc != 0) {
                log("error: $rc")
            }

            return rc
        }

    /**
     * Instantiates 2 windows and make them communicate through windows
     * messages, even complex ones throught WM_COPYDATA.
     */
    fun testWindowMesssages() {

        // note : check the asserts that are present in the callback implementations here after.
        // Create window 1 named "ping"
        createWindow("ping")

        // let windows create the window before searching its handle.
        sleepCurrThread(4000)
        // Retrieves the created window's handle.
        val hwndPing = determineHWNDFromWindowClass("ping")

        var hook: HHOOK? = null
        var h: WinNT.HANDLE? = null
        try {

            // DEMO 1 : sends a simple message to ping window with code MSG_CODE and value 123456.
            var result = User32.INSTANCE.SendMessage(
                hwndPing, WinUser.WM_USER, WPARAM(MSG_SIMPLE_CODE.toLong()),
                LPARAM(MSG_SIMPLE_VAL.toLong())
            )
            log("User Message sent to $hwndPing, result = $result")

            // DEMO 2 : send of structured message.
            // copyDataStruct must be held strongly on the java side.
            // cf : https://github.com/java-native-access/jna/pull/774 comments.
            val copyDataStruct = createStructuredMessage()
            result = User32.INSTANCE.SendMessage(
                hwndPing, WinUser.WM_COPYDATA, null,
                LPARAM(Pointer.nativeValue(copyDataStruct.pointer))
            )/* No current hwnd for this demo */
            log(
                "COPYDATASTRUCT sent message to " + hwndPing + "(size=" + copyDataStruct.size() + ") code ="
                        + copyDataStruct.dwData
            )


            // DEMO 3 : hook winproc then send a message to the hooked proc.
            //hook = hookwinProc(hwndPing)
            h = Hookito().hookwinProc()
            result = User32.INSTANCE.SendMessage(
                hwndPing, WinUser.WM_USER, WPARAM(MSG_HOOKED_CODE.toLong()),
                LPARAM(MSG_HOOKED_VAL.toLong())
            )
            log("User Message sent to hooked proc $hwndPing, result = $result")


            // Waits e few moment before shutdown message.
            sleepCurrThread(3000)

        } finally {

            try {
                // checks that there has been no exception in the created thread for windows messages receival.
                //assert done in a try block in order not to block the WM_CLOSE message sending.

            } finally {

                Timer().schedule(18000L) {
                    User32.INSTANCE.PostMessage(hwndPing, WinUser.WM_CLOSE, null, null)
                    if (hook != null) {
                        User32.INSTANCE.UnhookWindowsHookEx(hook)
                        User32.INSTANCE.UnhookWinEvent(h)
                    }
                }
                //User32.INSTANCE.PostMessage(hwndPing, WinUser.WM_CLOSE, null, null)
                //log("WM_CLOSE posted to " + hwndPing)

                // Remember to unhook the win proc.

            }

        }

    }

    private fun createStructuredMessage(): COPYDATASTRUCT {
        val myData = MsgStruct()
        myData.number = MSG_STRUCT_NUMBER
        myData.message = MSG_STRUCT_VAL
        myData.write() // writes to native memory the data structure otherwise nothing is sent...

        // log("Prepared structured content to send : " + myData.toString(true));
        val copyDataStruct = COPYDATASTRUCT()
        copyDataStruct.dwData = ULONG_PTR(DATA_STRUCT_CODE.toLong())
        copyDataStruct.cbData = myData.size()
        copyDataStruct.lpData = myData.pointer
        copyDataStruct.write() // writes to native memory the data structure otherwise nothing is sent...
        return copyDataStruct
    }

    /**
     * Example of message sent in the copydatastruct.
     */

    fun createWindow(windowClass: String) {
        // Runs it in a specific thread because the main thread is blocked in infinite loop otherwise.
        Thread(Runnable {
            try {
                createWindowAndLoop(windowClass)
            } catch (t: Throwable) {
                //will fail the test in case of exception in created thread.
                exceptionInCreatedThread = t
            }
        }).start()
        log("Window $windowClass created.")
    }

    fun createWindowAndLoop(windowClass: String) {
        // define new window class
        val hInst = Kernel32.INSTANCE.GetModuleHandle("")

        val wClass = WNDCLASSEX()
        wClass.hInstance = hInst
        wClass.lpfnWndProc = WindowProc { hwnd, uMsg, wParam, lParam ->
            // log(hwnd + " - received a message : " + uMsg);
            when (uMsg) {
                WinUser.WM_CREATE -> {
                    log((hwnd).toString() + " - onCreate: WM_CREATE")
                    LRESULT(0)
                }
                WinUser.WM_CLOSE -> {
                    log((hwnd).toString() + " WM_CLOSE")
                    User32.INSTANCE.DestroyWindow(hwnd)
                    LRESULT(0)
                }
                WinUser.WM_DESTROY -> {
                    log((hwnd).toString() + " - on Destroy.")
                    User32.INSTANCE.PostQuitMessage(0)
                    LRESULT(0)
                }
                WinUser.WM_USER -> {
                    log(
                        ((hwnd).toString() + " - received a WM_USER message with code : '" + wParam + "' and value : '" + lParam
                                + "'")
                    )

                    if (wParam.toInt() == MSG_SIMPLE_CODE) {
                        assertEqualsForCallbackExecution(
                            MSG_SIMPLE_VAL,
                            lParam.toInt()
                        )
                    }

                    if (wParam.toInt() == MSG_HOOKED_CODE) {
                        assertEqualsForCallbackExecution(
                            MSG_HOOKED_VAL,
                            lParam.toInt()
                        )
                    }

                    LRESULT(0)
                }
                WinUser.WM_COPYDATA -> {

                    val copyDataStruct = COPYDATASTRUCT(Pointer(lParam.toLong()))
                    val uMsg1 = copyDataStruct.dwData
                    val lParam1 = copyDataStruct.lpData
                    val wParam1 = copyDataStruct.cbData
                    log(
                        ((hwnd).toString() + " - received a WM_COPYDATA message with code : '" + uMsg1 + "' of size : '" + wParam1
                                + "'")
                    )

                    when (uMsg1.toInt()) {
                        DATA_STRUCT_CODE -> {
                            val msg = MsgStruct(lParam1)
                            // log(hwnd + " - received structured content : " + msg.toString(true));
                            log(
                                ((hwnd).toString() + " - message is of type MsgStruct with number = " + msg.number + " and message = '"
                                        + msg.message + "'")
                            )
                            assertEqualsForCallbackExecution(
                                MSG_STRUCT_NUMBER,
                                msg.number
                            )
                            assertEqualsForCallbackExecution(
                                MSG_STRUCT_VAL,
                                msg.message
                            )
                        }
                    }
                    LRESULT(0)
                }
                else -> User32.INSTANCE.DefWindowProc(hwnd, uMsg, wParam, lParam)
            }
        }
        wClass.lpszClassName = windowClass

        // register window class
        User32.INSTANCE.RegisterClassEx(wClass)
        lastError

        // create new window
        val hWnd = User32.INSTANCE.CreateWindowEx(
            User32.WS_EX_TOPMOST, windowClass,
            "My hidden helper window, used only to catch the windows events", 0, 0, 0, 0, 0, null, null, hInst, null
        )

        lastError
        log("window sucessfully created! window hwnd: " + hWnd.pointer.toString())

        val msg = MSG()
        while (User32.INSTANCE.GetMessage(msg, hWnd, 0, 0) > 0) {
            User32.INSTANCE.TranslateMessage(msg)
            User32.INSTANCE.DispatchMessage(msg)

        }

        User32.INSTANCE.UnregisterClass(windowClass, hInst)
        User32.INSTANCE.DestroyWindow(hWnd)

        log("program exit!")
    }

    fun determineHWNDFromWindowClass(windowClass: String): HWND? {
        val cb = CallBackFindWindowHandleByWindowclass(windowClass)
        User32.INSTANCE.EnumWindows(cb, null)
        return cb.foundHwnd

    }

    private class CallBackFindWindowHandleByWindowclass(private val windowClass: String) : WNDENUMPROC {

        var foundHwnd: HWND? = null
            private set

        override fun callback(hWnd: HWND?, data: Pointer?): Boolean {

            val windowText = CharArray(512)
            User32.INSTANCE.GetClassName(hWnd, windowText, 512)
            val className = Native.toString(windowText)

            if (windowClass.equals(className, ignoreCase = true)) {
                // Found handle. No determine root window...
                val hWndAncestor = User32.INSTANCE.GetAncestor(hWnd, User32.GA_ROOTOWNER)
                foundHwnd = hWndAncestor
                return false
            }
            return true
        }

    }

    private fun hookwinProc(hwndToHook: HWND?): HHOOK {
        val hookProc = object : HOOKPROC {

            /**
             * Callback method. cf :
             * https://msdn.microsoft.com/en-us/library/windows/desktop/ms644975(v=vs.85).aspx
             *
             *
             * nCode [in] : Specifies whether the hook procedure must process
             * the message. If nCode is HC_ACTION, the hook procedure must
             * process the message. If nCode is less than zero, the hook
             * procedure must pass the message to the CallNextHookEx function
             * without further processing and must return the value returned by
             * CallNextHookEx. wParam [in] : Specifies whether the message was
             * sent by the current thread. If the message was sent by the
             * current thread, it is nonzero; otherwise, it is zero. lParam [in]
             * : A pointer to a CWPSTRUCT structure that contains details about
             * the message.
             *
             *
             */
            // used by introspection from jna.
            fun callback(nCode: Int?, wParam: WPARAM?, lParam: LPARAM?): LRESULT? {

                if (nCode != null) {
                    if (nCode < 0) {
                        return User32.INSTANCE.CallNextHookEx(null, nCode, wParam, lParam)
                    }
                }

                try {
                    val cwp = WinUser.CWPSTRUCT(lParam?.toLong()?.let { Pointer(it) })
                    // log(" - received a message in hooked winproc : " + cwp.message.intValue());

                    when (cwp.message) {
                        WinUser.WM_USER -> {

                            val hWndSource = HWND(Pointer(cwp.wParam.toLong()))

                            log(
                                ((cwp.hwnd).toString() + " - Received a message from " + hWndSource + " hooked proc : code= " + cwp.wParam
                                        + ", value = " + cwp.lParam)
                            )
                            assertEqualsForCallbackExecution(
                                MSG_HOOKED_CODE,
                                cwp.wParam.toInt()
                            )
                            assertEqualsForCallbackExecution(
                                MSG_HOOKED_VAL,
                                cwp.lParam.toInt()
                            )

                            return LRESULT(0)
                        }
                        else -> {
                            log("-----------------CREATED !------------------")
                        }
                    }


                    // Note : in more complex cases, the message could be structured with copydatastruct containing an
                    // effective struct.
                    // Read is like so :
                    // COPYDATASTRUCT copyDataStruct = new COPYDATASTRUCT(new Pointer(cwp.lParam.longValue()));
                    // then read the copy data then the message struct as above...

                    // Send message to next hook.
                    return nCode?.let { User32.INSTANCE.CallNextHookEx(null, it, wParam, lParam) }
                } catch (t: Throwable) {
                    t.printStackTrace()
                    return LRESULT(0)
                }

            }
        }

        val hInst = Kernel32.INSTANCE.GetModuleHandle(null)
        val hwndToHook2 = User32.INSTANCE.FindWindow(null, "DEV")
        val threadtoHook = User32.INSTANCE.GetWindowThreadProcessId(hwndToHook2, null)
        // Hook of the wndProc
        return User32.INSTANCE.SetWindowsHookEx(14, hookProc, hInst, 0)
    }

    private fun sleepCurrThread(durationMs: Int) {
        try {
            Thread.sleep(durationMs.toLong())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    private fun log(message: String) {
        val currThread = Thread.currentThread().name
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        val currTime = sdf.format(Date())
        println("$currTime [$currThread] $message")

    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            Hookito2().testWindowMesssages()
        }

        private var exceptionInCreatedThread: Throwable? = null

        private val MSG_SIMPLE_CODE = 101

        private val MSG_SIMPLE_VAL = 123456

        private val DATA_STRUCT_CODE = 129

        private val MSG_HOOKED_CODE = 711

        private val MSG_HOOKED_VAL = 654321

        private val MSG_STRUCT_NUMBER = 5

        private val MSG_STRUCT_VAL = "Sending a structured message :)"

        private fun assertEqualsForCallbackExecution(expected: Any, actual: Any?) {
            //assertion method for asserts done in threads others than the main thread.
            // It will fail the test in case of exception like AssertionError is raised by junit.

        }
    }

}