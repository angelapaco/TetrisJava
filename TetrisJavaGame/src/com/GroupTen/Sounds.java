package com.GroupTen;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Sounds {

    private String soundsFolder = "tetrissounds" + File.separator;
    private String gameover = soundsFolder + "gameover.wav";
    private String hardDrop = soundsFolder + "harddrop.wav";
    private String clearLine = soundsFolder + "line.wav";
    private String moveLeftRight = soundsFolder + "moveLR.wav";
    private  String rotateLeftRight = soundsFolder + "rotateLR.wav";
    private String softDrop = soundsFolder + "softdrop.wav";
    //private String theme = soundsFolder + "themedownv.wav";
    private Clip gameoverSound, hardDropSound, clearLineSound, moveLeftRightSound, rotateLeftRightSound, softDropSound; //themeBGMSound;

    public Sounds() {
        try {
            gameoverSound = AudioSystem.getClip();
            hardDropSound = AudioSystem.getClip();
            clearLineSound = AudioSystem.getClip();
            moveLeftRightSound = AudioSystem.getClip();
            rotateLeftRightSound = AudioSystem.getClip();
            softDropSound = AudioSystem.getClip();
            //themeBGMSound = AudioSystem.getClip();

            gameoverSound.open(AudioSystem.getAudioInputStream(new File(gameover).getAbsoluteFile()));

            hardDropSound.open(AudioSystem.getAudioInputStream(new File(hardDrop).getAbsoluteFile()));

            clearLineSound.open(AudioSystem.getAudioInputStream(new File(clearLine).getAbsoluteFile()));

            moveLeftRightSound.open(AudioSystem.getAudioInputStream(new File(moveLeftRight).getAbsoluteFile()));

            rotateLeftRightSound.open(AudioSystem.getAudioInputStream(new File(rotateLeftRight).getAbsoluteFile()));

            softDropSound.open(AudioSystem.getAudioInputStream(new File(softDrop).getAbsoluteFile()));

            //themeBGMSound.open(AudioSystem.getAudioInputStream(new File(theme).getAbsoluteFile()));

        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void playGameover() {
        gameoverSound.setFramePosition(0);
        gameoverSound.start();
    }

    public void playHardDrop() {
        hardDropSound.setFramePosition(0);
        hardDropSound.start();
    }

    public void playClearLine() {
        clearLineSound.setFramePosition(0);
        clearLineSound.start();
    }

    public void playMoveLeftRight() {
        moveLeftRightSound.setFramePosition(0);
        moveLeftRightSound.start();
    }

    public void playRotateLeftRight() {
        rotateLeftRightSound.setFramePosition(0);
        rotateLeftRightSound.start();
    }

    public void playSoftDrop() {
        softDropSound.setFramePosition(0);
        softDropSound.start();
    }

    //public void playThemeBGM() {
        //themeBGMSound.setFramePosition(0);
        //themeBGMSound.start();
    //}

}
