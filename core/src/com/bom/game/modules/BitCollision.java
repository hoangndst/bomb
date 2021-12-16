package com.bom.game.modules;

public class BitCollision {
  public static final short NULL = 0;
  public static final short BOMBERMAN = 1;
  public static final short WALL = 2;
  public static final short BRICK = 4;
  public static final short DESTROYED_BRICK = 8;
  public static final short ENEMY = 16;
  // public static final short MAGNET = 16;
  public static final short BOMB = 32;
  // public static final short BOMB_EXPLODING = 64;
  public static final short FLAME = 64;
  public static final short ITEM = 128;

  public static short orOperation(short... bits) {
    short res = 0;
    for (short bit : bits)
      res |= bit;
    return res;
  }
}
