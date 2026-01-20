package ro.pub.cs.systems.eim.practicaltest02v4

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PracticalTest02MainActivityv4 : AppCompatActivity() {
    var ss_port: EditText? = null
    var start_server_button: Button? = null

    var url_edit_text: EditText? = null
    var get_url_button: Button? = null
    var result_text_view: TextView? = null


    var serverThread: ServerThread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test02v4_main)

        ss_port = findViewById<EditText>(R.id.sv_port_text)
        start_server_button = findViewById<Button>(R.id.start_server_button)

        url_edit_text = findViewById<EditText>(R.id.url_edit_text)
        get_url_button = findViewById<Button>(R.id.get_url_button)
        result_text_view = findViewById<TextView>(R.id.result_text_view)

        start_server_button!!.setOnClickListener {
            if(serverThread != null) {
                Toast.makeText(this, "Server is already started!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var svPort: String = ss_port!!.text.toString()
            if (svPort.isEmpty()) {
                Toast.makeText(this, "Server port should be filled!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            serverThread = ServerThread(svPort.toInt())
            if (serverThread?.getServerSocket() == null) {
                Toast.makeText(this, "Could not create server thread!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            serverThread!!.start()
        }

        get_url_button!!.setOnClickListener {
            var url = url_edit_text?.text.toString()
            if (url.isEmpty()) {
                Toast.makeText(this, "All client parameters should be filled!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (serverThread == null || serverThread?.getServerSocket() == null) {
                Toast.makeText(this, "There is no server to connect to!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            result_text_view?.text = ""

            var clientThread = ClientThread(
                ss_port!!.text.toString().toInt(), url,
                result_text_view!!
            )
            clientThread.start()

        }
    }
}