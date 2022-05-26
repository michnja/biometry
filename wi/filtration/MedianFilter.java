package pl.edu.pb.wi.filtration;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import static pl.edu.pb.wi.filtration.SimpleFilter.deepCopy;

public abstract class MedianFilter {
    public static BufferedImage filterImage(BufferedImage img, int size) {
        BufferedImage copy = deepCopy(img);
        int boundary = size / 2;
        ArrayList<Integer> field;

        for (int w = boundary; w < img.getWidth() - boundary; w++) {
            for (int h = boundary; h < img.getHeight() - boundary; h++) {
                field = SimpleFilter.getField(w, h, img, size);
                Collections.sort(field);
                int medianValue = field.get(boundary + 1);

                copy.setRGB(w, h, medianValue);
            }
        }
        return copy;
    }
}
