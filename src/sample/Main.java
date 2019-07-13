package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import sample.Udp.PacketParser;
import sample.Udp.UdpListener;
import sample.Util.Vector2d;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static sample.Udp.PacketParser.points1;

public class Main extends Application {
  public static ArrayList<Vector2d> points = new ArrayList<>();
  public static ArrayList<Vector2d> path = new ArrayList<>();
  private Label debuggingLabel;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    points1.clear();
    primaryStage.setTitle("8221 Cubix");

    Group rootGroup = new Group();
    Scene scene = new Scene(rootGroup, Screen.FIELD_SIZE * 2, Screen.FIELD_SIZE * 2);

    // Background Image
    Image image = new Image(new FileInputStream(System.getProperty("user.dir") + "\\field.png"));
    ImageView fieldBackground = new ImageView();
    fieldBackground.setImage(image);
    rootGroup.getChildren().add(fieldBackground);

    // Scale the image to fit the window
    fieldBackground.setFitWidth(Screen.SCREEN_WIDTH);
    fieldBackground.setFitHeight(Screen.SCREEN_HEIGHT);

    // Canvas
    Canvas fieldCanvas = new Canvas(Screen.SCREEN_WIDTH, Screen.SCREEN_HEIGHT);
    // the GraphicsContext is what we use to draw on the fieldCanvas
    GraphicsContext gc = fieldCanvas.getGraphicsContext2D();
    rootGroup.getChildren().add(fieldCanvas); // add the canvas

    debuggingLabel = new Label();
    debuggingLabel.setFont(new Font("Fira Code Medium", 40));
    debuggingLabel.textFillProperty().setValue(new Color(1.0, 1.0, 1.0, 1));
    debuggingLabel.setLayoutX(100);
    debuggingLabel.setLayoutY(100);
    primaryStage.setScene(scene);
    primaryStage.show();

    rootGroup.getChildren().add(debuggingLabel);

    UdpListener udpListener = new UdpListener(8221);
    Thread runner = new Thread(udpListener);
    runner.start();

    new AnimationTimer() {
      @Override
      public void handle(long currentNanoTime) {
        drawScreen(gc);
        drawPaths(gc);
        drawRobot(gc);
        drawPoints(gc);
      }
    }.start();
  }

  private void drawScreen(GraphicsContext gc) {
    // Reset the canvas
    gc.clearRect(0, 0, Screen.SCREEN_WIDTH, Screen.SCREEN_HEIGHT);

    debuggingLabel.setText(PacketParser.getRobotPos().toString());
  }

  private void drawPaths(GraphicsContext gc) {
    for (int i = 0; i < path.size(); i++) {
      int j = i;
      Vector2d test = Screen.cartesianToScreen(path.get(i));
      try {
        Vector2d test1 = Screen.cartesianToScreen(path.get(i + 1));

        gc.setStroke(new Color(1, 0.101, 0.956, 1.0));
        gc.setLineWidth(5);

        gc.strokeLine(test.x, test.y, test1.x, test1.y);
      } catch (IndexOutOfBoundsException e) {
        // ree
      }
    }
  }

  private void drawRobot(GraphicsContext gc) {
    double robotX = PacketParser.getRobotPos().x;
    double robotY = PacketParser.getRobotPos().y;
    double robotAngle = PacketParser.getRobotPos().heading;
    double robotRadius = Math.sqrt(162) * 2.54 * 2;

    try {
      Vector2d robot = Screen.cartesianToScreen(new Vector2d(robotX, robotY));
      double width = 18 * 2.54 * 2;

      gc.save(); // save the gc
      Image image = new Image(new FileInputStream(System.getProperty("user.dir") + "/robot.png"));
      gc.transform(new Affine(new Rotate(robotAngle, robot.x, robot.y)));
      gc.drawImage(image, robot.x - width / 2, robot.y - width / 2, width, width);

      gc.setStroke(new Color(0.160, 1, 0.211, 1.0));
      double radius = 19.685 * 2.54 * 2;
      gc.strokeOval(robot.x - radius, robot.y - radius, radius * 2, radius * 2);
      gc.restore();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void drawPoints(GraphicsContext gc) {

    for (Vector2d x : points) {
      Vector2d screen = Screen.cartesianToScreen(x);

      double radius = 5;
      gc.setStroke(new Color(0.2, 0.996, 1, 1.0));
      gc.strokeOval(screen.x - radius, screen.y - radius, radius * 2, radius * 2);
    }
    for(Vector2d x : points1) {
      Vector2d screen = Screen.cartesianToScreen(x);

      double radius = 5;
      gc.setStroke(new Color(0.556, 1, 0.058, 1.0));
      gc.strokeOval(screen.x - radius, screen.y - radius, radius * 2, radius * 2);
    }
  }
}
