/**
 * @author Nadim Chibani, Youssef Mousallem
 * @version 1.1
 */
package application;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
//import java.util.Random;
import java.util.stream.Collectors;

import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;



/*
 * The game that we are running using the javafx library
 */
public class SpaceInvaderAugApp extends Application{
	
	static int score= 0;
	private Pane root = new Pane();
	
	private double time = 0;
	static boolean spawned = false;
	
	private Sprite player = new Sprite(300,750,40,40,"player",Color.BLUE);
	
	/*
	 * This function creates the visuals that we are seeing using different function to add objects to it 
	 */
	private Parent createContent() {
		root.setPrefSize(600, 800);
		
		root.getChildren().add(player);
		System.out.println(player.numberOfBullets);
		
		AnimationTimer timer = new AnimationTimer() {
			
			/**
			 * The function that is being called every second to update what us happening on the screen
			 */
			@Override
			public void handle(long now) {
				update();
			}
		};
		
		timer.start();
		
		
		return root;
	}
	
	/**
	 * This function is used to spawn target objects and add them to the GUI
	 */
	private void SpawnEnemies() {
		//System.out.println("SPAWNING");
		if (spawned == false) {
			Sprite target = new Sprite(300, 750-40, 30, 30, "target", Color.RED);
			spawned = true;
			
			root.getChildren().add(target);
		}

	}
	
	/**
	 * 
	 * @return a list of all sprite objects on the screen to use on case by case in switch statements based
	 * on what said sprite represents (a sprite is the visual representation of an object) used for bullets
	 * and objects that will be shot at
	 */
	private List<Sprite> sprites(){
		return root.getChildren().stream().map(n-> (Sprite)n).collect(Collectors.toList());
	}
	
	/**
	 * This function is used to update what is happening on the screen every moment
	 * From movements of bullets to collision and results
	 */
	private void update() {
		
		//adding enemies:
		SpawnEnemies();
		
		time +=0.016;
		sprites().forEach(s ->{
			switch(s.type) {
				case "bullet":
					s.moveUp();
					
					//Collision 
					sprites().stream().filter(e->e.type.equals("target")).forEach(target ->{
						if (s.getBoundsInParent().intersects(target.getBoundsInParent())) {
							target.dead = true;
							s.dead = true;
							score ++;
							System.out.println("Score = " + score);
							player.numberOfBullets++;
							System.out.println("Number of Bullets = " + player.numberOfBullets);
						}
					});
					break;
					
				case "target":
				try {
					s.moveUpAngular();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				break;
			}
		});
		root.getChildren().removeIf(n ->{
			Sprite s = (Sprite) n;
			return s.dead;
		});
	}
	
	
	/**
	 * self-explanatory, creates the bullet object that we are firing and reduces the number of bullets
	 */
	private void shoot(Sprite who) {
		System.out.println(who.numberOfBullets);
		if (who.numberOfBullets>0) {
			Sprite s = new Sprite((int)who.getTranslateX() +20, (int)who.getTranslateY(), 5, 20, "bullet", Color.BLACK);
			
			root.getChildren().add(s);
			player.numberOfBullets--; 
			}
	}
	
	/**
	 * The initial call that initiates the visuals, and sets the functions of each input
	 */
	@Override
	public void start(Stage stage) throws Exception{
		Scene scene = new Scene(createContent());
		
		scene.setOnKeyPressed(e->{
			switch (e.getCode()) {
				case A:
					player.moveleft();
					break;
				case D:
					player.moveRight();
					break;
				case SPACE:
						shoot(player);
					break;
//				case W:
//					player.moveUpAngular(5);
//					break;
				default:
					break;
			}
		});
		
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * The Objects that are on the screen
	 * Need to be updated and changed from rectangles to actual pixel Sprites
	 */
	private static class Sprite extends Rectangle{
		boolean moving = false;
		boolean dead = false;
		final String type;
		protected int numberOfBullets ;
		double destination_x = 0;
		double destination_y = 0;
	
		/**
		 * 
		 * @param x : x-axis position
		 * @param y : y-axis position
		 * @param w :width
		 * @param h :height
		 * @param type :what kind of object is its
		 * @param color : Color
		 * @param numberOfBullets : counter of bullets 
		 */
		Sprite (int x, int y, int w, int h, String type,Color color){
			super(w,h,color);
			numberOfBullets = 2;
			
			this.type=type;
			setTranslateX(x);
			setTranslateY(y);
		}
		
		/**
		 * moves left by updating x value
		 */
		void moveleft() {
			setTranslateX(getTranslateX() -5);
		}
		/**
		 * moves right by updating x value
		 */
		void moveRight() {
			setTranslateX(getTranslateX() +5);
		}
		/**
		 * moves up by updating y value
		 */
		void moveUp() {
			setTranslateY(getTranslateY() -15);
		}
		
		/**
		 * This function is used to create the targets movement of 5 degree angle and make it move that way
		 * @throws InterruptedException
		 */
		void moveUpAngular() throws InterruptedException {
			//System.out.println("MOVING: "+moving);
//			System.out.println("x target: " + destination_x +"y target: " + destination_y);
//			System.out.println("x :" + getTranslateX() +" y :" + getTranslateY());
			if (moving == true) {}
			else {
				//making random angle multiple of 5
				int max = 10;
				int min = 1;
				Random random = new Random();
				int angle =(random.nextInt(max - min + 1) + min)*5;
				System.out.println("ANGLE : "+ angle);

				int radius = 500;
	
				double Final_x = radius * Math.sin(Math.PI * 2 * angle / 360); //+getTranslateX();
				double Final_y = radius * Math.sin(Math.PI * 2 * angle / 360); //+getTranslateY();
				destination_x = Final_x;
				destination_y = Final_y;
//				System.out.println("Final x :" + Final_x + " Final y :" + Final_y);
	
				//creating object movement and animating it
				Line line = new Line(getTranslateX(), getTranslateY(), destination_x, destination_y);
				
				PathTransition transition=new PathTransition();
				transition.setNode(this);
				transition.setDuration(Duration.seconds(4));
				transition.setPath(line);
				transition.setCycleCount(1);
				
				transition.play();
				moving = true;
				transition.setOnFinished((event) -> {
					dead = true;
					//TimeUnit.SECONDS.sleep(1);
					spawned = false;
					System.out.println("TEST");
					moving = false;
				});
			}
		}
	}
	
	/*
	 * Main doesn't normally run for JavaFX applications but if it does due to IDE limitations it will
	 * then pass the args to the JavaFX application to run
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
