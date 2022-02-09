package com.example.chesskotlinfollowalong

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.*
import java.util.concurrent.Executors

private const val TAG = "MainActivity"

/*
    MVC: Model View Controller
    Model = ChessModel()
    View = ChessView
    Controller = MainActivity
 */

class MainActivity : AppCompatActivity(), ChessDelegate {

    private val PORT: Int = 50000
    private lateinit var chessView: ChessView
    private var printWriter: PrintWriter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        val chessView = findViewById<ChessView>(R.id.chess_view)
        chessView.chessDelegate = this
         */
        chessView = findViewById<ChessView>(R.id.chess_view)
        chessView.chessDelegate = this
        findViewById<Button>(R.id.reset_button).setOnClickListener {
            ChessGame.reset()
            chessView.invalidate()
        }

        /*
            on device
            Settings | Connections | Wi-Fi | (active network) | IP address
        */

        findViewById<Button>(R.id.listen_button).setOnClickListener {
            Executors.newSingleThreadExecutor().execute() {
                val serverSocket = ServerSocket(PORT)
                val socket = serverSocket.accept()
                receiveMove(socket)
            }
        }

        findViewById<Button>(R.id.connect_button).setOnClickListener {
            Executors.newSingleThreadExecutor().execute {
                val socket = Socket("localhost", PORT) // localhost can be your IP
                receiveMove(socket)
            }
        }
    }

    private fun receiveMove(socket: Socket) {
            val scanner = Scanner(socket.getInputStream())
            printWriter = PrintWriter(socket.getOutputStream(), true)
            while (scanner.hasNextLine()) {
                val move: List<Int> = scanner.nextLine().split(",").map { it.toInt() }
                runOnUiThread {
                    ChessGame.movePiece(move[0], move[1], move[2], move[3])
                    chessView.invalidate()
            }
        }
    }

    override fun pieceAt(col: Int, row: Int): ChessPiece? = ChessGame.pieceAt(col, row)

    override fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int) {
        if (fromCol != toCol && fromRow != toRow) {
            return
        }

        printWriter?.let {
            val moveStr = "$fromCol,$fromRow,$toCol,$toRow"
            Executors.newSingleThreadExecutor().execute {
                it.println(moveStr)
            }
        }
    }
}