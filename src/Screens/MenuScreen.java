package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.TitleScreenMap;
import SpriteFont.SpriteFont;
import Utils.Stopwatch;
import Sounds.AudioPlayer;

import java.awt.*;
import java.io.File;
import java.lang.ProcessHandle.Info;

import javax.security.auth.login.FailedLoginException;
import javax.swing.plaf.ProgressBarUI;

// This is the class for the main menu screen
public class MenuScreen extends Screen {
	protected ScreenCoordinator screenCoordinator;
	protected int currentMenuItemHovered = 0; // current menu item being "hovered" over
	protected int menuItemSelected = -1;
	protected SpriteFont playGame;
	protected SpriteFont credits;
	protected Map background;
	protected Stopwatch keyTimer = new Stopwatch();
	protected int pointerLocationX, pointerLocationY;
	protected KeyLocker keyLocker = new KeyLocker();
	protected static AudioPlayer menuMusic;
	protected AudioPlayer startEffect;
	protected static boolean musicAlreadyPlaying = false;

	public MenuScreen(ScreenCoordinator screenCoordinator) {
		this.screenCoordinator = screenCoordinator;
	}

	@Override
	public void initialize() {
		playGame = new SpriteFont("PLAY GAME", 310, 150, "Comic Sans", 30, new Color(49, 207, 240));
		playGame.setOutlineColor(Color.black);
		playGame.setOutlineThickness(3);
		credits = new SpriteFont("CREDITS", 310, 200, "Comic Sans", 30, new Color(49, 207, 240));
		credits.setOutlineColor(Color.black);
		credits.setOutlineThickness(3);
		background = new TitleScreenMap();
		background.setAdjustCamera(false);
		keyTimer.setWaitTime(200);
		menuItemSelected = -1;
		keyLocker.lockKey(Key.SPACE);

		// setup AudioPlayer
		try
		{
			if(musicAlreadyPlaying == false)
			{
				menuMusic = new AudioPlayer(true, "C:/Users/emili/OneDrive/Desktop/SER225_GAME/"
						+ "DreamTeam-Platformer/Resources/Zoo-Mania_Sample_Music.wav");
				menuMusic.play();
			}
		}

		catch(Exception e)
		{
			System.out.println("Error with playing sound."); 
			e.printStackTrace();
		}
	}

	public void update() {
		// update background map (to play tile animations)
		background.update(null);

		// if down or up is pressed, change menu item "hovered" over (blue square in front of text will move along with currentMenuItemHovered changing)
		if (Keyboard.isKeyDown(Key.DOWN) && keyTimer.isTimeUp()) {
			keyTimer.reset();
			currentMenuItemHovered++;
		} else if (Keyboard.isKeyDown(Key.UP) && keyTimer.isTimeUp()) {
			keyTimer.reset();
			currentMenuItemHovered--;
		}

		// if down is pressed on last menu item or up is pressed on first menu item, "loop" the selection back around to the beginning/end
		if (currentMenuItemHovered > 1) {
			currentMenuItemHovered = 0;
		} else if (currentMenuItemHovered < 0) {
			currentMenuItemHovered = 1;
		}

		// sets location for blue square in front of text (pointerLocation) and also sets color of spritefont text based on which menu item is being hovered
		if (currentMenuItemHovered == 0) {
			playGame.setColor(new Color(255, 215, 0));
			credits.setColor(new Color(49, 207, 240));
			pointerLocationX = 275;
			pointerLocationY = 125;
		} else if (currentMenuItemHovered == 1) {
			playGame.setColor(new Color(49, 207, 240));
			credits.setColor(new Color(255, 215, 0));
			pointerLocationX = 275;
			pointerLocationY = 175;
		}

		// if space is pressed on menu item, change to appropriate screen based on which menu item was chosen
		if (Keyboard.isKeyUp(Key.SPACE)) {
			keyLocker.unlockKey(Key.SPACE);
		}
		if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
			try
			{
				startEffect = new AudioPlayer (false, "C:/Users/emili/OneDrive/Desktop/SER225_GAME/"
						+ "DreamTeam-Platformer/Resources/GameStart_Sound.wav");
				startEffect.play();
			}
			
			catch(Exception e)
			{
				System.out.println("Error with sound");
			}
			
			menuItemSelected = currentMenuItemHovered;
			if (menuItemSelected == 0) {
				try
				{
					musicAlreadyPlaying = false;					
					menuMusic.stop();
				}
				
				catch(Exception e)
				{
					System.out.println("Error with music");
					e.printStackTrace();
				}
				screenCoordinator.setGameState(GameState.LEVEL);
				
			} else if (menuItemSelected == 1) {
				screenCoordinator.setGameState(GameState.CREDITS);
			}
		}
	}

	public void draw(GraphicsHandler graphicsHandler) {
		background.draw(graphicsHandler);
		playGame.draw(graphicsHandler);
		credits.draw(graphicsHandler);
		graphicsHandler.drawFilledRectangleWithBorder(pointerLocationX, pointerLocationY, 20, 20, new Color(49, 207, 240), Color.black, 2);
	}

	public int getMenuItemSelected() {
		return menuItemSelected;
	}
}