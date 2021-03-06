package com.example.chesskotlinfollowalong

import android.util.Log

private const val TAG = "ChessModel"

object ChessGame {
    private var piecesBox = mutableSetOf<ChessPiece>()

    init {
        reset()

        //Log.d(TAG, "${piecesBox.size}")
    }

    fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int) {
        if (fromCol == toCol && fromRow == toRow) return
        val movingPiece = pieceAt(fromCol, fromRow) ?: return

        pieceAt(toCol, toRow)?.let {
            if (it.player == movingPiece.player) {
                return
            }
            piecesBox.remove(it)
        }

        piecesBox.remove(movingPiece)
        //val newPiece = movingPiece.copy(col = toCol, row = toRow)
        //piecesBox.add(ChessPiece(toCol, toRow, movingPiece.player, movingPiece.rank, movingPiece.resID))
        piecesBox.add(movingPiece.copy(col = toCol, row = toRow))
    }

    fun reset() {
        piecesBox.removeAll(piecesBox)
        for (i in 0 until 2) {
            piecesBox.add(ChessPiece(0 + i * 7, 0, ChessPlayer.WHITE, ChessRank.ROOK, R.drawable.rook_white))
            piecesBox.add(ChessPiece(0 + i * 7, 7, ChessPlayer.BLACK, ChessRank.ROOK, R.drawable.rook_black))

            piecesBox.add(ChessPiece(1 + i * 5, 0, ChessPlayer.WHITE, ChessRank.KNIGHT, R.drawable.knight_white))
            piecesBox.add(ChessPiece(1 + i * 5, 7, ChessPlayer.BLACK, ChessRank.KNIGHT, R.drawable.knight_black))

            piecesBox.add(ChessPiece(2 + i * 3, 0, ChessPlayer.WHITE, ChessRank.BISHOP, R.drawable.bishop_white))
            piecesBox.add(ChessPiece(2 + i * 3, 7, ChessPlayer.BLACK, ChessRank.BISHOP, R.drawable.bishop_black))
        }

        for (i in 0 until 8) {
            piecesBox.add(ChessPiece(i, 1, ChessPlayer.WHITE, ChessRank.PAWN, R.drawable.pawn_white))
            piecesBox.add(ChessPiece(i, 6, ChessPlayer.BLACK, ChessRank.PAWN, R.drawable.pawn_black))
        }

        piecesBox.add(ChessPiece(3, 0, ChessPlayer.WHITE, ChessRank.QUEEN, R.drawable.queen_white))
        piecesBox.add(ChessPiece(3, 7, ChessPlayer.BLACK, ChessRank.QUEEN, R.drawable.queen_black))
        piecesBox.add(ChessPiece(4, 0, ChessPlayer.WHITE, ChessRank.KING, R.drawable.king_white))
        piecesBox.add(ChessPiece(4, 7, ChessPlayer.BLACK, ChessRank.KING, R.drawable.king_black))
    }

    fun pieceAt(col: Int, row: Int) : ChessPiece? {
        for (piece in piecesBox) {
            if (col == piece.col && row == piece.row) {
                return piece
            }
        }
        return null
    }

    override fun toString(): String {
        var desc = " \n"
        for (row in 7 downTo 0) {
            desc += "$row"

            for (col in 0 until 8) {
                desc += " "
                desc += pieceAt(col, row)?.let {
                    val white = it.player == ChessPlayer.WHITE
                    when (it.rank) {
                        ChessRank.KING -> if (white) "k" else "K"
                        ChessRank.QUEEN -> if (white) "q" else "Q"
                        ChessRank.BISHOP -> if (white) "b" else "B"
                        ChessRank.ROOK -> if (white) "r" else "R"
                        ChessRank.KNIGHT -> if (white) "n" else "N"
                        ChessRank.PAWN -> if (white) "p" else "P"
                    }
                } ?: "."
            }
            desc += "\n"
        }
        desc += " 0 1 2 3 4 5 6 7"

        return desc
    }
}