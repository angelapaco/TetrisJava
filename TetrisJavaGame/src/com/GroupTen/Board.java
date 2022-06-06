package com.GroupTen;

import com.GroupTen.Shape.Tetrominoe;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;



public class Board extends JPanel { // this is where the game logic is located


    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 22; // The BOARD_WIDTH and BOARD_HEIGHT define the size of the board
    private Timer timer;
    private int timerDelay; // speed of falling pieces
    private boolean isFallingFinished = false; // determines if the Tetris shape has finished falling, and we then need to create a new shape
    private boolean isPaused = false; // checks if game is paused
    private int numLinesRemoved = 0; // counts the number of lines that we have removed so far
    private int curX = 0;
    private int curY = 0; // curX and curY determines the actual position of the falling Tetris shape
    private JLabel statusbar;
    private Shape curPiece;
    private Tetrominoe[] board;
    private BufferedImage image;



    public Board(Tetris parent) {
        initBoard(parent);
    }



    private void initBoard(Tetris parent) {

        setFocusable(true);
        statusbar = parent.getStatusBar();
        addKeyListener(new TAdapter());
    }

    private int squareWidth() {

        return (int) getSize().getWidth() / BOARD_WIDTH;
    } // determines the width and height of a single Tetrominoe square

    private int squareHeight() {

        return (int) getSize().getHeight() / BOARD_HEIGHT;
    }

    private Tetrominoe shapeAt(int x, int y) {

        return board[(y * BOARD_WIDTH) + x];
    } // determines the shape at the given coordinates. the shapes are stored in the board array.

    void start() {

        curPiece = new Shape();
        board = new Tetrominoe[BOARD_WIDTH * BOARD_HEIGHT]; // creates a new current shape and a new board

        clearBoard();
        newPiece(); // when the board is cleared, a new falling piece is initialized

        timerDelay = 1000; // 1 second speed of falling piece
        timer = new Timer(timerDelay, new GameCycle());
        timer.start(); // the timer is executed at timerDelay intervals, creating a game cycle
    }

    private void pause() {

        isPaused = !isPaused;

        if (isPaused) {
            statusbar.setText("Paused");
        } else {
            var scoreMsg = String.format("Score: %d", numLinesRemoved);
            statusbar.setText(scoreMsg);
        }

        repaint();
    } // this method pauses or resumes the game



    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        stars(g);
        doDrawing(g);
    }

    private void stars(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(50,50,9,3); //-
        g.fillRect(53,47,3,9);

        g.fillRect(155,90,9,3); //-
        g.fillRect(158,87,3,9);

        g.fillRect(40,110,9,3); //-
        g.fillRect(43,107,3,9);

        g.fillRect(90,120,9,3); //-
        g.fillRect(93,117,3,9);

        g.fillRect(0,140,6,3); //-
        g.fillRect(0,137,3,9);

        g.fillRect(65,180,9,3); //-
        g.fillRect(68,177,3,9);

        g.fillRect(136,220,9,3); //-
        g.fillRect(139,217,3,9);

        g.fillRect(3,280,9,3); //-
        g.fillRect(6,277,3,9);

        g.fillRect(50,343,9,3); //-
        g.fillRect(53,339,3,6);

        g.fillRect(20,5,3,3);
        g.fillRect(110,20,3,3);
        g.fillRect(40,35,3,3);
        g.fillRect(150,50,3,3);
        g.fillRect(90,65,3,3);
        g.fillRect(20,80,3,3);
        g.fillRect(120,95,3,3);
        g.fillRect(70,110,3,3);
        g.fillRect(160,125,3,3);
        g.fillRect(50,140,3,3);
        g.fillRect(100,155,3,3);
        g.fillRect(130,170,3,3);
        g.fillRect(30,185,3,3);
        g.fillRect(90,200,3,3);
        g.fillRect(170,215,3,3);
        g.fillRect(150,230,3,3);
        g.fillRect(40,245,3,3);
        g.fillRect(100,260,3,3);
        g.fillRect(130,275,3,3);
        g.fillRect(20,290,3,3);
        g.fillRect(70,305,3,3);
        g.fillRect(150,320,3,3);
        g.fillRect(70,335,3,3);
    }


    private void doDrawing(Graphics g) { // we draw all objects on the board

        var size = getSize();
        int boardTop = (int) size.getHeight() - BOARD_HEIGHT * squareHeight();

        for (int i = 0; i < BOARD_HEIGHT; i++) {

            for (int j = 0; j < BOARD_WIDTH; j++) {

                Tetrominoe shape = shapeAt(j, BOARD_HEIGHT - i - 1);

                if (shape != Tetrominoe.NoShape) {

                    drawSquare(g, j * squareWidth(),
                            boardTop + i * squareHeight(), shape);
                }
            }
        } // paints all shapes or remains of the shapes that have been dropped to the bottom of the board. All squares are remembered in the board array. We access it using the shapeAt() method

        if (curPiece.getShape() != Tetrominoe.NoShape) {

            for (int i = 0; i < 4; i++) {

                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);

                drawSquare(g, x * squareWidth(),
                        boardTop + (BOARD_HEIGHT - y - 1) * squareHeight(),
                        curPiece.getShape());
            }
        } // we paint the actual falling piece
    }

    private void dropDown() {

        int newY = curY;

        while (newY > 0) {

            if (!tryMove(curPiece, curX, newY - 1)) {
                break;
            }
            newY--;
        }
        pieceDropped();
    } // pressing the space key drops the piece to the bottom

    private void oneLineDown() {

        if (!tryMove(curPiece, curX, curY - 1)) {
            pieceDropped();
        }
    } // this is soft drop, one line drop only

    private void clearBoard() {

        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {

            board[i] = Tetrominoe.NoShape;
        }
    }

    private void pieceDropped() {

        for (int i = 0; i < 4; i++) {

            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            board[(y * BOARD_WIDTH) + x] = curPiece.getShape();
        }

        removeFullLines(); // removes a complete line

        if (!isFallingFinished) {

            newPiece();
        } // creates new piece after removal of complete line
    } // puts all the falling piece into the board array

    private void newPiece() {

        curPiece.setRandomShape();
        curX = BOARD_WIDTH / 2 + 1;
        curY = BOARD_HEIGHT - 1 + curPiece.minY();

        if (!tryMove(curPiece, curX, curY)) {

            curPiece.setShape(Tetrominoe.NoShape);
            timer.stop();

            var msg = String.format("Game Over. Score: %d", numLinesRemoved);
            statusbar.setText(msg);
            Tetris.playGameoverSound();
        }
    } // creates a new piece. The piece is in random shape. Then we compute the initial curX and curY values. If we cannot move to the initial positions, the game is over—we top out. The timer is stopped, and we display Game over string containing the score on the statusbar.

    private boolean tryMove(Shape newPiece, int newX, int newY) {

        for (int i = 0; i < 4; i++) {

            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);

            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
                return false;
            }

            if (shapeAt(x, y) != Tetrominoe.NoShape) {
                return false;
            }
        }

        curPiece = newPiece;
        curX = newX;
        curY = newY;

        repaint();
        return true;
    } // the tryMove() method tries to move the tetris piece. The method returns false if it has reached the board boundaries, or it is adjacent to the already fallen piece.

    private void removeFullLines() {

        int numFullLines = 0;

        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {

            boolean lineIsFull = true;

            for (int j = 0; j < BOARD_WIDTH; j++) {

                if (shapeAt(j, i) == Tetrominoe.NoShape) {

                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {

                numFullLines++;

                for (int k = i; k < BOARD_HEIGHT - 1; k++) {
                    for (int j = 0; j < BOARD_WIDTH; j++) {
                        board[(k * BOARD_WIDTH) + j] = shapeAt(j, k + 1);
                    }
                }
            }
        }

        if (numFullLines > 0) {

            numLinesRemoved += numFullLines;
            numLinesRemoved++;
            timerDelay /= 1.3;
            timer.setDelay(timerDelay);


            var newScoreMsg = String.format("Score: %d", numLinesRemoved);
            statusbar.setText(newScoreMsg);
            isFallingFinished = true;
            curPiece.setShape(Tetrominoe.NoShape);
            Tetris.playClearLineSound();
        }

    } // we check if there is any full row among all rows in the board and remove it.



    private void drawSquare(Graphics g, int x, int y, Tetrominoe shape) {

        Color colors[] = {new Color(0, 0, 0), new Color(210, 39, 48),
                new Color(68, 214, 44), new Color(77, 77, 255),
                new Color(224, 231, 34), new Color(219, 62, 177),
                new Color(102, 204, 204), new Color(255, 173, 0)
        };

        var color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + 1);
    } // every piece has four squares. each square are drawn with the drawSquare() method.

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    } // in here, we call the doGameCycle() method, creating a game cycle

    private void doGameCycle() {
        update();
        repaint();
    } // the game is divided into game cycles. each cycle updates the game and redraws the board

    private void update() {
        if (isPaused) {

            return;
        }
        if (isFallingFinished) {

            isFallingFinished = false;
            newPiece();
        } else {
            oneLineDown();
        }
    } // the update() represents one step of the game. the falling piece goes one line down or a new piece is created if the previous one has finished falling

    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            if (curPiece.getShape() == Tetrominoe.NoShape) {

                return;
            }

            int keycode = e.getKeyCode(); // we get the code with getKeyCode() method

            // Java 12 switch expressions
            switch (keycode) {

                case KeyEvent.VK_P -> pause();
                case KeyEvent.VK_LEFT -> {
                    tryMove(curPiece, curX - 1, curY);
                    Tetris.playMoveLRSound();
                }
                case KeyEvent.VK_RIGHT -> {
                    tryMove(curPiece, curX + 1, curY);
                    Tetris.playMoveLRSound();
                }
                case KeyEvent.VK_DOWN -> {
                    tryMove(curPiece.rotateRight(), curX, curY);
                    Tetris.playRotateLRSound();
                }
                case KeyEvent.VK_UP -> {
                    tryMove(curPiece.rotateLeft(), curX, curY);
                    Tetris.playRotateLRSound();
                }
                case KeyEvent.VK_SPACE -> {
                    dropDown();
                    Tetris.playHardDropSound();
                }
                case KeyEvent.VK_D -> {
                    oneLineDown();
                    Tetris.playSoftDropSound();
                }
            } // these are Java 12 switch expressions, we bind key events to methods
        }
    } // the game is controlled with cursor keys. we check for key events in the KeyAdapter
}