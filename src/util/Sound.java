package util;


import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


// 声音父类
public class Sound {
    Clip audioPlayer = null; // 建立一个播放接口

    public Sound(File file) {
        try{
            // 创建一个准备Player，准备好播放
            audioPlayer = AudioSystem.getClip();
            audioPlayer.open(AudioSystem.getAudioInputStream(file));
        }
        catch (Exception e) {

        }
    }

    public void play() {
        audioPlayer.setFramePosition(0);
        audioPlayer.start();
    }

    public void stop() {
        audioPlayer.stop();
        audioPlayer.close();
    }
}
