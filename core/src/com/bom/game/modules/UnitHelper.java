
package com.bom.game.modules;

import com.badlogic.gdx.math.Vector2;
import com.bom.game.manager.GameManager;

public abstract class UnitHelper {

    public static Vector2 coordBox2DSnapToGrid(Vector2 position) {
        return new Vector2(snapMetersToGrid(position.x), snapMetersToGrid(position.y));
    }

    public static int snapMetersToGrid(float meters) {
        return (int) (metersToPixels(meters) / GameManager.PPT);
    }

    public static Vector2 coordScreenToBox2D(float pixelX, float pixelY, float radius) {
        return new Vector2(pixelX + radius, pixelY + radius);
    }

    public static Vector2 coordScreenToBox2D(float pixelX, float pixelY, float width, float height) {
        return new Vector2(screenToBox2D(pixelX, width), screenToBox2D(pixelY, height));
    }

    public static float screenToBox2D(float px, float scale) {
        return px + scale / 2;
    }

    public static Vector2 coordBox2DToScreen(float mX, float mY, float radius) {
        return new Vector2(mX - radius, mY - radius);
    }

    public static Vector2 coordBox2DToScreen(float mX, float mY, float width, float height) {
        return new Vector2(box2DToScreen(mX, width), box2DToScreen(mY, height));
    }

    public static float box2DToScreen(float meters, float scale) {
        return meters - scale / 2;
    }

    public static Vector2 coordPixelsToMeters(float pixelX, float pixelY) {
        return new Vector2(pixelX / GameManager.PPM, pixelY / GameManager.PPM);
    }

    public static float pixelsToMeters(int pixel) {
        return pixel / GameManager.PPM;
    }

    public static float pixelsToMeters(float pixel) {
        return pixelsToMeters(Math.round(pixel));
    }

    public static Vector2 coordMetersToPixels(float mX, float mY) {
        return new Vector2(mX * GameManager.PPM, mY * GameManager.PPM);
    }

    public static float metersToPixels(float meters) {
        return meters * GameManager.PPM;
    }

    public static float distance(Vector2 v1, Vector2 v2) {
        return v1.dst(v2);
    }
}
