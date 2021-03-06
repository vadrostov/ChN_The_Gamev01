package com.vrostov.chronon.envirmoment.beans;

import com.vrostov.chronon.envirmoment.ChNMainCity;
import com.vrostov.chronon.envirmoment.ValuesBean;
import playn.core.Tile;
import pythagoras.f.IDimension;

/**
 * Created by vrostov on 18.10.2017.
 */
public class MainCityValuesBean implements ValuesBean{


    private  final int TILE_HEIGHT=100;
    private  final int TILE_WIDTH=80;
    private  final int TILE_DEPTH=40;
    private  final int TILE_BASE = 90;
    private  final int TILE_IMAGE_HEIGHT = 170;

    private   final int MAX_STACK_HEIGHT = 35;

    private  final int OBJECT_BASE=30;

    private  final double GRAVITY = -10.0;
    private  final double RESTITUTION = 0.4;
    private  final double FRICTION = 10.0;

    private final int width=16;
    private final int height=16;

    public  final String[] tilesNames=new String[]{"block_wood", "block_wood"};
    private final Tile[] tiles=new Tile[tilesNames.length];

    public MainCityValuesBean() {

    }

    public Tile[] getTiles() {
        return tiles;
    }

    public String[] getTilesNames() {
        return tilesNames;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMAX_STACK_HEIGHT() {
        return MAX_STACK_HEIGHT;
    }

    public  int getTileHeight() {
        return TILE_HEIGHT;
    }

    public  int getTileWidth() {
        return TILE_WIDTH;
    }

    public  int getTileDepth() {
        return TILE_DEPTH;
    }

    public  int getTileBase() {
        return TILE_BASE;
    }

    public  int getTileImageHeight() {
        return TILE_IMAGE_HEIGHT;
    }

    public  int getObjectBase() {
        return OBJECT_BASE;
    }

    public  double getGRAVITY() {
        return GRAVITY;
    }

    public  double getRESTITUTION() {
        return RESTITUTION;
    }

    public  double getFRICTION() {
        return FRICTION;
    }
}
