package pl.edu.pb.wi.filtration;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

public abstract class SimpleFilter {

    public static BufferedImage filterImage(BufferedImage img, int size) {
        BufferedImage copy = deepCopy(img);
        int boundary = size / 2;
        for (int w = boundary; w < img.getWidth() - boundary; w++) {
            for (int h = boundary; h < img.getHeight() - boundary; h++) {
                copy.setRGB(w, h, calculateNewPixelValue(img, w, h, size));
            }
        }
        return copy;
    }

    public static int calculateNewPixelValue(BufferedImage img, int w, int h, int size) {
        double sumRed = 0, sumGreen = 0, sumBlue = 0;
        int boundary = size / 2;
        for (int i = boundary * -1; i <= boundary; i++) {
            for (int j = boundary * -1; j <= boundary; j++) {
                Color c = new Color(img.getRGB(w + i, h + j));
                sumRed += c.getRed();
                sumBlue += c.getBlue();
                sumGreen += c.getGreen();
            }
        }
        sumRed = sumRed / 9;
        sumBlue = sumBlue / 9;
        sumGreen = sumGreen / 9;

        Color c = new Color((int) sumRed, (int) sumGreen, (int) sumBlue);
        return c.getRGB();
    }

    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    static ArrayList<Integer> getField(int w, int h, BufferedImage image, int fieldSize) {
        ArrayList<Integer> field = new ArrayList<>();
        int color;
        int boundary = fieldSize / 2;

        for (int i = boundary * -1; i <= boundary; i++) {
            for (int j = boundary * -1; j <= boundary; j++) {
                color = new Color(image.getRGB(w + i, h + j)).getRGB();
                field.add(color);
            }
        }

        return field;
    }

}
