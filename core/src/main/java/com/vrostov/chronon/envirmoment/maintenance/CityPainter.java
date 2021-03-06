package com.vrostov.chronon.envirmoment.maintenance;

import com.vrostov.chronon.envirmoment.ChNMainCity;
import com.vrostov.chronon.envirmoment.ValuesBean;
import com.vrostov.chronon.envirmoment.beans.MainCityValuesBean;
import playn.core.Image;
import playn.core.Platform;
import playn.core.Surface;
import pythagoras.f.IDimension;
import react.RFuture;
import react.Slot;
import react.UnitSlot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vrostov on 18.10.2017.
 */
public class CityPainter {

    ValuesBean valuesBean;

    ChNMainCity.Stack[] world;

    private final IDimension viewSize;

    Platform platform;
    boolean loaded;
    private double viewOriginX, viewOriginY, viewOriginZ;

    public CityPainter(ValuesBean valuesBean, ChNMainCity.Stack[] world, Platform platform) {
        this.valuesBean = valuesBean;
        this.world = world;
        this.platform = platform;
        this.viewSize=platform.graphics().viewSize;
        loadImg();
    }

    public void paint(Surface surface, float alpha){


        if (!loaded) return; // avoid rendering until images load

        int startX = (int) pixelToWorldX(surface, 0);
        int endX = (int) pixelToWorldX(surface, viewSize.width());
        if (startX < 0)
            startX = 0;
        if (endX < 0)
            endX = 0;
        if (startX >= valuesBean.getWidth())
            startX = valuesBean.getWidth() - 1;
        if (endX >= valuesBean.getWidth())
            endX = valuesBean.getWidth() - 1;

        int startY = (int) pixelToWorldY(surface, 0, 0);
        int endY = (int) pixelToWorldY(surface, viewSize.height(), valuesBean.getMAX_STACK_HEIGHT());
        if (startY < 0)
            startY = 0;
        if (endY < 0)
            endY = 0;
        if (startY >= valuesBean.getHeight())
            startY = valuesBean.getHeight() - 1;
        if (endY >= valuesBean.getHeight())
            endY = valuesBean.getHeight() - 1;

        // Paint all the tiles from back to front.
        for (int tz = 0; tz < valuesBean.getMAX_STACK_HEIGHT(); ++tz) {
            for (int ty = startY; ty <= endY; ++ty) {
                for (int tx = startX; tx <= endX; ++tx) {
                    ChNMainCity.Stack stack = world[ty * valuesBean.getWidth() + tx];

                    if (tz < stack.height()) {
                        // Draw the tile and its shadows.

                        // Skip obviously hidden tiles.
                        if ((tz < stack.height() - 1) && (height(tx, ty + 1) > tz)) {
                            continue;
                        }

                        // Figure out where the tile goes. If it's out of screen bounds,
                        // skip it (paintShadow() is relatively expensive).
                        int px = worldToPixelX(surface, tx);
                        int py = worldToPixelY(surface, ty, tz) - valuesBean.getTileBase();
                        if ((px > viewSize.width()) || (py > viewSize.height())
                                || (px + valuesBean.getTileWidth() < 0) || (py + valuesBean.getTileImageHeight() < 0)) {
                            continue;
                        }

                        surface.draw(valuesBean.getTiles()[stack.getTiles()[tz]], px, py);
                        //paintShadow(surf, tx, ty, px, py);
                    } else if (tz >= stack.height()) {
                        // Paint the objects in this stack.
                        paintObjects(surface, stack, tz, alpha);
                    }
                }
            }
        }

    }

    private double pixelToWorldX(Surface surf, float x) {
        double center = viewSize.width() * 0.5;
        return (int) (((viewOriginX * valuesBean.getTileWidth()) + x - center) / valuesBean.getTileWidth());
    }

    private double pixelToWorldY(Surface surf, float y, double z) {
        double center = viewSize.height() * 0.5;
        return (y + (viewOriginY * valuesBean.getTileHeight() - viewOriginZ * valuesBean.getTileDepth())
                + (z * valuesBean.getTileDepth()) - center)
                / valuesBean.getTileHeight();
    }

    private int worldToPixelX(Surface surface, double x){
        double center = viewSize.width() * 0.5;
        return (int) (center - (viewOriginX * valuesBean.getTileWidth()) + x * valuesBean.getTileWidth());
    }


    private int worldToPixelY(Surface surface, double y, double z){
        double center = viewSize.height() * 0.5;
        return (int) (center
                - (viewOriginY * valuesBean.getTileHeight() - viewOriginZ * valuesBean.getTileDepth()) + y
                * valuesBean.getTileHeight() - z * valuesBean.getTileDepth());
    }

    private void paintObjects(Surface surface, ChNMainCity.Stack stack, int tz, float alpha){

    }

    private void loadImg(){
        List<RFuture<Image>> wait=new ArrayList<RFuture<Image>>();

        for(int i=0;i<valuesBean.getTiles().length; ++i){
            final int idx=1;
            Image tile=platform.assets().getImage(imageRes(valuesBean.getTilesNames()[i]));
            tile.state.onSuccess(new Slot<Image>() {
                public void onEmit(Image image) {
                    valuesBean.getTiles()[idx]=image.texture();
                }
            });
            wait.add(tile.state);
        }

        RFuture.sequence(wait).onSuccess(new UnitSlot() {
            @Override
            public void onEmit() {
                loaded=true;
            }
        });

    }
    private String imageRes(String name) {
        return "/images/" + name + ".png";
    }

    private int height(int tx, int ty) {
        return stack(tx, ty).height();
    }


    public void setViewOrigin(double x, double y, double z) {
        viewOriginX = x;
        viewOriginY = y;
        viewOriginZ = z;
    }

    private ChNMainCity.Stack stack(int tx, int ty) {
        if ((tx < 0) || (tx >= valuesBean.getWidth()) || (ty < 0) || (ty >= valuesBean.getHeight())) {
            ChNMainCity.Stack emptyStack=new ChNMainCity.Stack();
            emptyStack.setTiles(new int[0]);
        }

        return world[ty * valuesBean.getWidth() + tx];
    }

}
