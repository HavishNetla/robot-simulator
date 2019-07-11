package sample;

import sample.Util.Vector2d;

/**
 * This class is for some useful methods like converting normal coordinates to screen coordinates
 */
public class Screen {
  static final double FIELD_SIZE = 365.76; // cm

  static final double SCREEN_WIDTH = 365.76 * 2; // cm
  static final double SCREEN_HEIGHT = 365.76 * 2; // cm

  static Vector2d cartesianToScreen(Vector2d coord) {
    return new Vector2d(coord.x * 2, (SCREEN_HEIGHT - coord.y * 2));
  }

  public static double getCentimetersPerPixel() {
    // Get the conversion of centimeters per pixel. This is the size in cm of the biggest dimension
    // divided by the size in pixels of the biggest dimension
    return FIELD_SIZE / getFieldSizePixels();
  }

  public static double getFieldSizePixels() {
    // get the biggest dimension if it is the width or the height
    double biggestWindowDimensionPixels =
        SCREEN_HEIGHT > SCREEN_WIDTH ? SCREEN_HEIGHT : SCREEN_WIDTH;
    return biggestWindowDimensionPixels / 1;
  }
}
