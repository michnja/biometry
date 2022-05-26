package pl.edu.pb.wi.binarization;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Binarization {

    public static BufferedImage manualBinarization(BufferedImage img, int threshold) {
        for(int w = 0; w < img.getWidth(); w++) {
            for(int h = 0; h < img.getHeight(); h++) {
                Color c = new Color(img.getRGB(w, h));
                img.setRGB(w, h,
                        c.getRed() >= threshold ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }
        return img;
    }

}
