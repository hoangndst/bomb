package com.bom.game.manager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

public class GameManager implements Disposable {

    private static final GameManager instance = new GameManager();

    private AssetManager assetManager;

    private final String soundPath = "sounds/";
    private final String musicPath = "music/";

    public static int bombermanLive = 3;
    public static boolean bombermanHasKey = false;
    public static boolean bombermanInPortal = false;

    private String currentMusic = "";

    private GameManager() {
        assetManager = new AssetManager();
        assetManager.load("img/actors.pack", TextureAtlas.class);
        assetManager.load("sounds/Pickup.ogg", Sound.class);
        assetManager.load("sounds/PlaceBomb.ogg", Sound.class);
        assetManager.load("sounds/KickBomb.ogg", Sound.class);
        assetManager.load("sounds/Powerup.ogg", Sound.class);
        assetManager.load("sounds/Explosion.ogg", Sound.class);
        assetManager.load("sounds/Die.ogg", Sound.class);
        assetManager.load("sounds/EnemyDie.ogg", Sound.class);
        assetManager.load("sounds/EnemyDie1.ogg", Sound.class);
        assetManager.load("sounds/EnemyDie2.ogg", Sound.class);
        assetManager.load("sounds/Boss1Hammer.ogg", Sound.class);
        assetManager.load("sounds/PortalAppears.ogg", Sound.class);
        assetManager.load("sounds/Teleport.ogg", Sound.class);
        assetManager.load("sounds/Pause.ogg", Sound.class);

        assetManager.load("music/SuperBomberman-Title.ogg", Music.class);
        assetManager.load("music/SuperBomberman-Area1.ogg", Music.class);
        assetManager.load("music/SuperBomberman-Area2.ogg", Music.class);
        assetManager.load("music/SuperBomberman-Boss.ogg", Music.class);
        assetManager.load("music/GameOver.ogg", Music.class);
        assetManager.load("music/Victory.ogg", Music.class);
        assetManager.load("music/Oops.ogg", Music.class);
        assetManager.load("music/StageCleared.ogg", Music.class);


        assetManager.finishLoading();
    }



    public static GameManager getInstance() {
        return instance;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public void playSound(String soundName) {
        playSound(soundName, 1.0f, 1.0f, 0f);
    }

    public void playSound(String soundName, float volume, float pitch, float pan) {
        Sound sound = assetManager.get(soundPath + soundName, Sound.class);
        sound.play(volume, pitch, pan);
    }

    public void playMusic(String musicName, boolean isLooping) {
        Music music = assetManager.get(musicPath + musicName);
        music.setVolume(0.6f);
        if (currentMusic.equals(musicName)) {
            music.setLooping(isLooping);
        if (!music.isPlaying()) {
            music.play();
        }
            return;
        }

        stopMusic();
        music.setLooping(isLooping);
        music.play();
        currentMusic = musicName;
    }

    public void playMusic() {
        if (currentMusic.isEmpty()) {
            return;
        }
        Music music = assetManager.get(musicPath + currentMusic, Music.class);
        music.play();
    }

    public void stopMusic() {
        if (currentMusic.isEmpty()) {
        return;
        }
        Music music = assetManager.get(musicPath + currentMusic, Music.class);
        if (music.isPlaying()) {
        music.stop();
        }
    }

    public void pauseMusic() {
        if (currentMusic.isEmpty()) {
        return;
        }
        Music music = assetManager.get(musicPath + currentMusic, Music.class);
        if (music.isPlaying()) {
        music.pause();
        }
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}
