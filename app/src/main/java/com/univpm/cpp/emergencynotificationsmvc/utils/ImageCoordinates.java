package com.univpm.cpp.emergencynotificationsmvc.utils;

import com.univpm.cpp.emergencynotificationsmvc.models.map.Map;

/**
 * Classe che effutua la conversione da pixel in metri in base alla mappa specificata
 */
public class ImageCoordinates {

    public static int getPixelsXFromMetres(int x, Map map) {

        double scale = map.getScale();
        int xRef = map.getxRef();
        int xRefpx = map.getxRefpx();

        return (int) (xRefpx-((xRef - x)*scale));
    }

    public static int getPixelsYFromMetres(int y, Map map) {

        double scale = map.getScale();
        int yRef = map.getyRef();
        int yRefpx = map.getyRefpx();

        return (int) (yRefpx-((yRef - y)*scale));
    }
}
