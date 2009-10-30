package be.rhea.projector.controller.client.filter;

import java.awt.image.RGBImageFilter;

public class AlphaFilter extends RGBImageFilter {
    private int level;

    public AlphaFilter() {
      canFilterIndexColorModel = true;
    }

    public void setLevel(int lev) {
      level = lev;
    }

    public int filterRGB(int x, int y, int rgb) {
      int a = level * 0x01000000;
      return (rgb &   0x00ffffff) | a;
    }
 }