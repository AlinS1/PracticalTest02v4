package ro.pub.cs.systems.eim.practicaltest02v4


import android.util.Log
import android.widget.TextView
import java.io.BufferedReader
import java.net.Socket

class ClientThread(port: Int, url: String, content: TextView) :
    Thread() {

    private val port: Int = port
    private val url: String = url
    private val content: TextView = content

    override fun run() {
        // Client thread logic goes here
        var socket: Socket? = null
        try {
            socket = Socket(Constants.SERVER_ADDR, port)
            var bufferedReader = Utilities.getReader(socket)
            var printWriter = Utilities.getWriter(socket)
            printWriter.println(url)
            printWriter.flush()

            Log.v(Constants.TAG, "Client sent: $url")

            var result_recv = bufferedReader.readLine()
            content.post {
                content.text = result_recv
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            socket?.close()
        }
    }


    fun stopClient() {
    }
}