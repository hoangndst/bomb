package com.bom.game.modules;

public class Bit {
    public static final short NULL = 0;
    public static final short PLAYER = 1;
    public static final short WALL = 2;
    public static final short CHEST = 4;
    public static final short DESTROYED_CHEST = 8;
    public static final short MAGNET = 16;
    public static final short BOMB = 32;
    public static final short FIRE = 64;

    public static short orOperation(short... bits) {
        short result = 0;
        for (short bit : bits)
            result |= bit;
        return result;
    }
}
