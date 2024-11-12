package jaehee.study.part2chapter3

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .url("http://10.0.2.2:8080") //emulator
            .build()

        val callback = object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Client", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {

                if(response.isSuccessful) {
                    Log.e("Client", "${response.body?.string()}")
                }
            }
        }

        client.newCall(request).enqueue(callback)  //execute는 동기함수. 블락

//        val httpURLConnection = HttpURLConnection(URL("https://naver.com"))
//        httpURLConnection.connect()

        /*Thread{
            try{
                val socket = Socket("10.0.2.2", 8080) //emulator 한정
                val printer = PrintWriter(socket.getOutputStream())
                val reader = BufferedReader(InputStreamReader(socket.getInputStream()))

                printer.println("GET / HTTP/1.1")
                printer.println("Host: 127.0.0.1:8080")
                printer.println("User-Agent: android")
                printer.println("\r\n")
                printer.flush()

                var input: String? = "-1"
                while(input != null) {
                    input = reader.readLine()
                    Log.e("Client", input)
                }
                reader.close()
                printer.close()
                socket.close()
            }catch (e: Exception) {
                Log.e("Cliente", e.toString())
            }
        }.start()*/

    }
}