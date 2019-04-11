package no.erikgustafsson.tiospill.pingpong;

import java.awt.Color;
import java.awt.Point;
import java.util.LinkedList;

import no.erikgustafsson.tiospill.Button;
import no.erikgustafsson.tiospill.ButtonState;
import no.erikgustafsson.tiospill.Game;
import no.erikgustafsson.tiospill.GameDescription;
import no.erikgustafsson.tiospill.HorizontalAxisState;
import no.erikgustafsson.tiospill.VerticalAxisState;
import no.erikgustafsson.tiospill.ormfeldt.OrmfeldtBoard;
import no.erikgustafsson.tiospill.template.GameTemplate;

public class PingPong extends Game {

	
	private boolean gameover;
	
	private Color backgroundColor = Color.BLACK;
	private Color frameColor = Color.GREEN;
	private Color orange = new Color(209, 74, 2);
	private Color racketColor = Color.WHITE;
	private Color playAreaColor = Color.BLACK;
	
	private Color[][] gameScreen = new Color[60][30];
	private Color[][] ballGraphics = new Color[][] { // O-post
		{Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, orange},
		{Color.WHITE, Color.WHITE, Color.WHITE, orange, orange},
		{Color.WHITE, Color.WHITE, orange, orange, orange},
		{Color.WHITE, orange, orange, orange, orange},
		{orange, orange, orange, orange, orange}
	};
	private int racketHeight = 10;
	private Color[][] racketGraphics = new Color[][] { 
		{racketColor},
		{racketColor},
		{racketColor},
		{racketColor},
		{racketColor}
	};
	
	private GameObject ball = new GameObject(new Point(30, 15), ballGraphics, 1, 58, 1, 28, this);
	private GameObject racketL = new GameObject(new Point(0, 1), racketGraphics, 0, 0, 1, 29, this);
	private GameObject racketR = new GameObject(new Point(59, 1), racketGraphics, 59, 59, 1, 29, this);
	
	
	
	
	
	public static final GameDescription GAME_DESCRIPTION = new GameDescription("OPONG", PingPong.class);

	private final Thread runThread = new Thread(new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(1000);
				while (true) {
					drawPlayArea();
					calculateBallDirection();
					drawBall();
					drawRackets();
					graphicsChanged();
					Thread.sleep(75);	
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	});
	

	// Metodene start(), stop(), buttonPressed() og getGraphics() blir alle kjørt
	// ifra spillrammeverket. Disse kan bli kjørt på ulike tråder men vil aldrig bli
	// kjørt samtidlig. Trådsikring trengs derfor kun hvis spillet starter egne
	// tråder.

	@Override
	public void start(Object option) {
		// Denne metoden blir kjørt når spillet starter.
		// option vil være satt til det alternativ som er valgt i menyen ved oppstart.
		// Hvis ingen alternativer er spesifisert i GAME_DESCRIPTION vil option være
		// null.
		drawBackground();
		drawFrame();
		drawPlayArea();
		ball.setMovingRight(true);			// initial moving direction
		ball.setMovingDown(true);
		racketHeight = (int) option;
		graphicsChanged();
		runThread.setDaemon(true);
		runThread.start();
	}

	@Override
	public void stop() {
		// Denne metoden blir kjørt når spillet avsluttes (f. eks. når man går til
		// menyen).
		// OBS!!! Denne metoden MÅ sørge for å avslutte alle tråder som eventuellt er
		// startet i spillet. Ingen andre tråder må kjøre etter at denne metoden
		// returnerer.
		runThread.interrupt();
	}

	// @Override
	// public void buttonPressed(int controllerId, Button button) {
	// // TODO Denne metoden blir kjørt hver gang en knapp på en spillkontroll blir
	// // trykket.
	// // controllerId er en unik id for hver spillkontroll (0, 1, 2, osv.)
	// // controllerId kan brukes for å avgjøre hvilken spillere som har trykket på
	// en
	// // knapp.
	// // Denne metoden vil ikke bli kjørt hvis SELECT-knappen blir trykket da den
	// // brukes for å gå til menyen.
	// // Denne metoden vil aldrig bli kjørt før start() og aldrig etter stop().
	// }

	@Override
	public void buttonStateChanged(int controllerId, Button button, ButtonState state) {
	}

	@Override
	public void horizontalAxisStateChanged(int controllerId, HorizontalAxisState state) {
		// TODO Auto-generated method stub
		super.horizontalAxisStateChanged(controllerId, state);
	}

	@Override
	public void verticalAxisStateChanged(int controllerId, VerticalAxisState state) {
		if (!(state == VerticalAxisState.NEUTRAL)) {
			GameObject racket = null;
			switch (controllerId) {
				case 0:
					racket = racketL;
					break;
				case 1:
					racket = racketR;
					break;
				default:
					break;
			}
			if (racket != null) {
				switch (state) {
					case UP:
						racket.up();
						break;
					case DOWN:		
						racket.down();
						break;
					default:
						break;
				}
			}
		}
	}

	@Override
	public Color[][] getGraphics() {
		// TODO Denne metoden blir brukt av spillrammeverket for å spillet på lystavla.
		// For å si ifra til rammeverket at bilden trenger å oppdateres kan metoden
		// notifyListeners() kjøres.
		// Denne metoden vil aldrig bli kjørt før start() og aldrig etter stop().
		return gameScreen;
	}	

	
	
	
	public synchronized void drawBackground() {
		for (int x = 0; x < 60; x++) {
			for (int y = 0; y < 30; y++) {
				gameScreen[x][y] = backgroundColor;
			}
		}
	}
	
	public synchronized void drawFrame() {
		for (int x = 0; x < 60; x++) {
			gameScreen[x][0] = frameColor;
			gameScreen[x][29] = frameColor;
		}
	}
	
	public synchronized void drawBall() {
		Color[][] ballGraphics = ball.getGraphics();
		for (int x = 0; x < ballGraphics[0].length; x++) {
			for (int y = 0; y < ballGraphics[1].length; y++) {
				gameScreen[ball.getPosition().x + x][ball.getPosition().y + y] = ballGraphics[x][y];
			}
		}
	}
	
	public synchronized void drawPlayArea() {
		for (int x = 0; x < 60; x++) {
			for (int y = 1; y < 29; y++) {
				gameScreen[x][y] = playAreaColor;
			}
		}
	}
	
	public synchronized void drawRackets() {
		Color[][] racketGraphics = racketL.getGraphics();
		for (int y = 0; y < racketGraphics.length; y++) {
			gameScreen[racketL.getPosition().x][racketL.getPosition().y + y] = racketGraphics[y][0];
			gameScreen[racketR.getPosition().x][racketR.getPosition().y + y] = racketGraphics[y][0];
			//System.out.println(racketL.getPosition().x + ", " + racketL.getPosition().y + y);
		}
	}
	
	public synchronized void calculateBallDirection() {
		String placement = null;
		if (isTopCollision()) {
			placement = "top";
		}
		if (isBottomCollision()) {
			placement = "bottom";
		}
		if (isRacketLCollision()) {
			placement = "racketL";
		}
		if (isRacketRCollision()) {
			placement = "racketR";
		}
		
		if (placement != null) { // if collision. With racket or top/bottom
			switch(placement) {
				case "top": 
					if (ball.isMovingLeft()) {
						ball.down();
						ball.left();
						break;
					} else { // moving right
						ball.down();
						ball.right();
						break;
					}
					
				case "bottom":
					if(ball.isMovingLeft()) {
						ball.up();
						ball.left();
						break;
					} else {
						ball.up();
						ball.right();
						break;
					}
					
				case "racketL":
					ball.right();
					if (ball.isMovingUp()) {
						ball.up();
						break;
					} else {
						ball.down();
						break;
					}
					
				case "racketR":
					ball.left();
					if (ball.isMovingUp()) {
						ball.up();
						break;
					} else {
						ball.down();
						break;
					}
				
				}
			
		} else { // no collision, 
			if (isGameOver()) { // 
				stop();
			}
			if (ball.isMovingLeft()) {
				ball.left();
			}
			if (ball.isMovingRight()) {
				ball.right();
			}
			if (ball.isMovingDown()) {
				ball.down();
			}
			if (ball.isMovingUp()) {
				ball.up();
			}
			
		}
			
	}
		
		
	
	
	public synchronized boolean isTopCollision() {
		if (ball.getPosition().y <= 1) {
			System.out.println("Top collision");
			return true;
		}
		else return false;
	}
	
	public synchronized boolean isGameOver() {
		if (ball.getPosition().x == racketL.getPosition().x + 1) { // left gameover
			if (!isRacketLCollision()) {
				System.out.println("gameOver");
				return true;
			}
		}
		if (ball.getPosition().x + ball.getWidth() == racketR.getPosition().x ) { // right gameover 
			System.out.println("x gameover");
			if (!isRacketRCollision()) {
				System.out.println("gameOver");
				return true;
			}
		}
		return false;
	}
	
	public synchronized boolean isBottomCollision() {
		if (ball.getPosition().y + ball.getHeight() > 28) {
			System.out.println("Bottom collision");
			return true;
		}
		else return false;
	}
	
	public synchronized boolean isRacketLCollision() {
		int ballLength = ball.getGraphics().length;
		int racketLength = racketL.getGraphics().length;
		Point racketPos = racketL.getPosition();
		Point ballPos = ball.getPosition();
		
		if (ballPos.x == racketPos.x + 1) {
			if (ballPos.y + ballLength >= racketPos.y && (ballPos.y < racketPos.y + racketLength - 1)) {
				System.out.println("racketL collision");
				return true;
			}
		}
		return false;
	}
	
	public synchronized boolean isRacketRCollision() { // checks if collision with racketR
		int racketHeight = racketR.getHeight();
		int ballHeight = ball.getHeight();
		int ballWidth = ball.getWidth();
		Point racketPos = racketR.getPosition();
		Point ballPos = ball.getPosition();
		if (ballPos.x + ballWidth == racketPos.x) {
			if ((ballPos.y + ballHeight > racketPos.y - 1) && (ballPos.y < racketPos.y + racketHeight - 1)) {
				System.out.println("racketR collision");
				return true;
			}
		}
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
