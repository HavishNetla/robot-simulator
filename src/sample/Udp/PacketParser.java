package sample.Udp;

import sample.Main;
import sample.Util.Pose2d;
import sample.Util.Vector2d;

import java.util.ArrayList;

public class PacketParser {
  public static ArrayList<Vector2d> points = new ArrayList<>();
  public static ArrayList<Vector2d> points1 = new ArrayList<>();
  private static Pose2d robotPos = new Pose2d(0, 0, 0);
  private static ArrayList<Vector2d> path = new ArrayList<>();

  PacketParser() {}

  public static Pose2d getRobotPos() {
    return robotPos;
  }

  public static ArrayList<Vector2d> getPath() {
    return path;
  }

  void parseMessage(String message) {
    System.out.println("Message: " + message);

    /*
       Different cases for the message

       ROBOT:x,y,c
       POINT:x,y
       LINE:x,y,label
    */

    // Split string on commas
    String[] split = message.split(":");
    String label = split[0]; // ROBOT or POINT or LINE
    switch (label) {
      case "ROBOT":
        // System.out.println("ROBOT");
        parseRobot(split[1]);
        break;
      case "POINT":
        // System.out.println("POINT");
        parsePoint(split[1]);
        break;
      case "POINT1":
        // System.out.println("POINT");
        parsePoint1(split[1]);
        break;
      case "LINE":
        // System.out.println("LINE");/
        parseLine(split[1]);
        break;
      case "CLEAR":
        clear();
        break;
      default:
        // System.out.println("default :(");
        // System.out.println("uh oh");
        break;
    }
  }

  private void parseRobot(String message) {
    // initially message is equal to something like this: "43.134,63.140,28.1456"
    String[] points = message.split(","); // Split the message by commas

    robotPos =
        new Pose2d(
            Double.parseDouble(points[0]),
            Double.parseDouble(points[1]),
            Double.parseDouble(points[2]));
  }

  private void parsePoint(String message) {
    // message will look like this (0.000,0.000)
    message = message.replace("(", "").replace(")", "").replace(" ", "");
    String[] split = message.split(","); // split the string into an array by commas
    points.add(new Vector2d(Double.parseDouble(split[0]), Double.parseDouble(split[1])));
  }

  private void parsePoint1(String message) {
    // message will look like this (0.000,0.000)
    message = message.replace("(", "").replace(")", "").replace(" ", "");
    String[] split = message.split(","); // split the string into an array by commas
    points1.add(new Vector2d(Double.parseDouble(split[0]), Double.parseDouble(split[1])));
  }

  private void parseLine(String message) {
    // message will look like this (0.000, 0.000),(10.000, 10.000),(20.000, 20.000),(40.000, 40.000)
    message = message.replace("(", "").replace(")", "").replace(" ", "");
    // message will look like this 0.000,0.000,10.000,10.000,20.000,20.000,40.000,40.000

    String[] split = message.split(","); // split the string into an array by commas

    ArrayList<String> xs = new ArrayList<>();
    ArrayList<String> ys = new ArrayList<>();

    // Get all the x terms
    for (int i = 0; i < split.length; i += 2) {
      xs.add(split[i]);
    }
    // Get all the x terms
    for (int i = 1; i < split.length; i += 2) {
      ys.add(split[i]);
    }

    // Combine them
    for (int i = 0; i < xs.size(); i++) {
      path.add(new Vector2d(Double.parseDouble(xs.get(i)), Double.parseDouble(ys.get(i))));
    }
  }

  public void clear() {
    Main.points.clear();
    Main.points.addAll(points);
    points.clear();

    Main.path.clear();
    Main.path.addAll(path);
    path.clear();
  }
}
