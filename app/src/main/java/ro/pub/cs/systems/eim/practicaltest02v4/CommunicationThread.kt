package ro.pub.cs.systems.eim.practicaltest02v4


import android.util.Log
import java.io.IOException
import java.net.Socket
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class CommunicationThread(private val serverThread: ServerThread, private val socket: Socket) :
    Thread() {


    override fun run() {
        try {
            Log.v(
                Constants.TAG,
                "Connection opened to " + socket.getLocalAddress() + ":" + socket.getLocalPort() + " from " + socket.getInetAddress()
            )
            var bufferedReader = Utilities.getReader(socket)
            var printWriter = Utilities.getWriter(socket)

            var url : String = bufferedReader.readLine()

            if (url.isEmpty()) {
                Log.e(Constants.TAG, "Error receiving parameters from client (city / information type)")
                socket.close()
                return
            }

            val data = serverThread.getData()
            var content: String? = null
            if(data != null) {
                if (data.containsKey(url)) {
                    content = data[url]
                }
            }
            if (content == null) {
                val httpClient = OkHttpClient()

                val getStr = url
                var request: Request = Request.Builder()
                    .url(getStr)
                    .build()


                val response = httpClient.newCall(request).execute()
                content = response.body!!.string()

                serverThread.setData(url, content)
            }

            printWriter.println(content)
            printWriter.flush()


        } catch (ioException: IOException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.message)
        } finally {
            socket.close()
            Log.v(Constants.TAG, "Connection closed")
        }
    }
}