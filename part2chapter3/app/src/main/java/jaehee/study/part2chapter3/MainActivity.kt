package jaehee.study.part2chapter3

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Thread{
            val port = 8080
            val server = ServerSocket(port)

            val socket = server.accept()

            //스트림은 일방통행, 인풋, 아웃풋 각각 운용해야 한다. 인풋으로 들어온건 아웃풋으로 나간다.
            socket.getInputStream() //클라이언트로부터 들어오는 스트림 == 클라이언트의 socket.outputStream
            socket.getOutputStream() //클라이언트에게 데이터를 주는 스트림 == 클라이언트의 socket.inputStream

            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            val printer = PrintWriter(socket.getOutputStream())

            var input: String? = "-1"

            while(input != null && input != ""){
                input = reader.readLine()
            }

            Log.e("SERVER", "READE DATA $input")

            printer.println("HTTP/1.1 200 OK")
            printer.println("Content-Type: text/html\r\n")

            printer.println("<h1>Hello World</h1>")
            printer.println("\r\n")
            printer.flush() //잔여 버퍼링 데이터가 있을 수 있으니 flush로 마저 배출
            printer.close()

            reader.close()

            socket.close()
        }.start()

    }
}