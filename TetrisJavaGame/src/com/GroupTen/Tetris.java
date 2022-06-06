package com.GroupTen;

import java.awt.*;
import javax.swing.*;


public class Tetris extends JFrame { // we set up the game. we created a board on which we play the game. we create a statusbar

    private JLabel statusbar;

    private static Sounds audio = new Sounds();

    public Tetris() {
        initUI();
    }
    private void initUI() {
        statusbar = new JLabel("Score: 0");

        add(statusbar, BorderLayout.SOUTH); // the score is displayed in a label which is located at the bottom of the board

        var board = new Board(this);
        add(board);
        board.start(); // the board is created and added to the container. the start() method starts that Tetris game

        setTitle("Tetris");
        setSize(200, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        board.setBackground(Color.BLACK);
    }

    JLabel getStatusBar() {

        return statusbar;
    }

    public static void playGameoverSound() {
        audio.playGameover();
    }

    public static void playHardDropSound() {
        audio.playHardDrop();
    }

    public static void playClearLineSound() {
        audio.playClearLine();
    }

    public static void playMoveLRSound() {
        audio.playMoveLeftRight();
    }

    public static void playRotateLRSound() {
        audio.playRotateLeftRight();
    }

    public static void playSoftDropSound() {
        audio.playSoftDrop();
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            var game = new Tetris();
            game.setVisible(true);
        });
    }
}