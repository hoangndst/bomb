
package com.bom.game.utils;

import com.badlogic.gdx.math.Vector2;
import com.bom.game.BomGame;

public abstract class UnitHelper {

  // Snap coordenadas do Box2D para coordenadas num grid
  public static Vector2 coordBox2DSnapToGrid(Vector2 position) {
    return new Vector2(snapMetersToGrid(position.x), snapMetersToGrid(position.y));
  }

  public static int snapMetersToGrid(float meters) {
    return (int) (metersToPixels(meters) / BomGame.PPT);
  }


  // Converter coordenadas de tela em Box2D
  public static Vector2 coordScreenToBox2D(float pixelX, float pixelY, float radius) {
    return new Vector2(pixelX + radius, pixelY + radius);
  }

  public static Vector2 coordScreenToBox2D(float pixelX, float pixelY, float width, float height) {
    return new Vector2(screenToBox2D(pixelX, width), screenToBox2D(pixelY, height));
  }

  public static float screenToBox2D(float px, float scale) {
    return px + scale / 2;
  }


  // Converter coordenadas de Box2D em tela
  public static Vector2 coordBox2DToScreen(float mX, float mY, float radius) {
    return new Vector2(mX - radius, mY - radius);
  }

  public static Vector2 coordBox2DToScreen(float mX, float mY, float width, float height) {
    return new Vector2(box2DToScreen(mX, width), box2DToScreen(mY, height));
  }

  public static float box2DToScreen(float meters, float scale) {
    return meters - scale / 2;
  }


  // Converter de pixels para metro
  public static Vector2 coordPixelsToMeters(float pixelX, float pixelY) {
    return new Vector2(pixelX / BomGame.PPM, pixelY / BomGame.PPM);
  }

  public static float pixelsToMeters(int pixel) {
    return pixel / BomGame.PPM;
  }

  public static float pixelsToMeters(float pixel) {
    return pixelsToMeters(Math.round(pixel));
  }


  // Converter de metros para pixel
  public static Vector2 coordMetersToPixels(float mX, float mY) {
    return new Vector2(mX * BomGame.PPM, mY * BomGame.PPM);
  }

  public static float metersToPixels(float meters) {
    return meters * BomGame.PPM;
  }
}
