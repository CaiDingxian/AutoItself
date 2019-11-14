package blockman.native.core.cmd

import java.io.*
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap

class Cmd : Closeable {

    private val reader: BufferedReader
    private val errorReader: BufferedReader
    private val writer: BufferedWriter
    private val runtime = Runtime.getRuntime()
    private val exe: Process

    constructor() {
        exe = runtime.exec("cmd")
        reader = exe.inputStream.reader().buffered()
        writer = exe.outputStream.writer().buffered()
        errorReader = InputStreamReader(exe.errorStream).buffered()
        //return exe.exitValue()
        writer.write("chcp 65001")
        writer.newLine()
        writer.flush()
    }

    var process: Process? = null

    fun commit(cmd: String, outputVarKeys: Array<String>): CmdResult {
        var formatedCmd = cmd.replace("\n", System.lineSeparator())
        val cmdOutput = StringBuffer()

        val uuid = UUID.randomUUID().toString()

        writer.write("echo " + uuid + "START")
        writer.newLine()
        writer.flush()

        writer.write(formatedCmd)
        writer.newLine()
        writer.flush()

        for (v in outputVarKeys) {
            writer.write("SET OUT_VAR$v=OUT_VAR_START%$v%OUT_VAR_END")
            writer.newLine()
            writer.flush()
            writer.write("echo %OUT_VAR$v%")
            writer.newLine()
            writer.flush()
        }

        writer.write("echo " + uuid + "END")
        writer.newLine()
        writer.flush()

        var doNext = true
        while (doNext) {
            val row = reader.readLine()
            if (row.matches(Regex("^" + uuid + "END"))) {
                doNext = false
            }
            cmdOutput.appendln(row)
        }
        writer.newLine()
        writer.write("echo ERR_END>&2")
        writer.newLine()
        writer.flush()

        //取得变量
        val m = Pattern.compile("\\r\\nOUT_VAR_START(.*)OUT_VAR_END\\r\\n").matcher(cmdOutput)
        val outputVars = HashMap<String, String>()
        var i = 0
        while (m.find()) {
            outputVars.put(outputVarKeys[i], m.group(1))
            i++
        }

        val errOutput = StringBuffer()
        doNext = true
        while (doNext) {
            val row = errorReader.readLine()
            if (row.matches(Regex("^" + "ERR_END"))) {
                doNext = false
            }
            errOutput.appendln(row)
        }

        val a = process?.exitValue()
        val clearCmdOutput = cmdOutput.replace(Regex("\\r\\n" + uuid + "START" + "(.*)" + uuid + "END\\r\\n"), "")
        return CmdResult(
            outputVars,
            clearCmdOutput,
            errOutput.toString().replace("ERR_END", "")
        )
    }

    override fun close() {
        closeAll(reader, writer, errorReader)
        exe.destroy()
    }

    fun isStop(): Boolean {
        return exe.isAlive
    }

    fun getExitCode(): Int? {
        return try {
            exe.exitValue()
        } catch (e: Exception) {
            null
        }
    }

    private fun closeAll(vararg target: Closeable) {
        for (c in target) {
            try {
                c.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        fun exec(cmd: String): Int {
            val runtime = Runtime.getRuntime()
            val p = runtime.exec(cmd)
            p.waitFor()
            return p.exitValue()
        }
    }


}

fun main() {

    //print(a[0]!!.javaClass)


//    val cmd=Cmd()
//    val r=cmd.commit("""
//        wmic /namespace:\\\\root\\WMI path MSAcpi_ThermalZoneTemperature get CurrentTemperature
//    """.trimIndent(), arrayOf("ABC","DEF","GHI"))
//    val a=0
//
//
//    val x=cmd.commit("""
//        START D:
//    """.trimIndent(), arrayOf())
//    val b=0
//    val runtime = Runtime.getRuntime()
//    val envp=Array<String>(10){"M=10"}
//    val exe= runtime.exec("""D:\DEV\123\1.bat cdx""".trimIndent(),envp)
//    exe.waitFor()
//    runtime
//    val x=0
}

