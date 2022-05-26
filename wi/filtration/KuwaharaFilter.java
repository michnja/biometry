package pl.edu.pb.wi.filtration;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static pl.edu.pb.wi.filtration.SimpleFilter.deepCopy;

public abstract class KuwaharaFilter {
    private static final int RED_CHANNEL = 0;
    private static final int GREEN_CHANNEL = 1;
    private static final int BLUE_CHANNEL = 2;

    private static BufferedImage image;
    private static int size;

    public static BufferedImage filterImage(BufferedImage img, int fieldSize) {
        image = img;
        size = fieldSize;

        BufferedImage copy = deepCopy(img);
        int boundary = size / 2;

        for (int w = boundary; w < img.getWidth() - boundary; w++) {
            for (int h = boundary; h < img.getHeight() - boundary; h++) {

                List<ArrayList<Integer>> listsRed = moveThroughField(w, h, RED_CHANNEL);
                int indexOfLowestVariationRed = getIndexOfLowestVariation(listsRed);
                int lowestAverageRed = listsRed.get(0).get(indexOfLowestVariationRed);

                List<ArrayList<Integer>> listsGreen = moveThroughField(w, h, GREEN_CHANNEL);
                int indexOfLowestVariationGreen = getIndexOfLowestVariation(listsGreen);
                int lowestAverageGreen = listsGreen.get(0).get(indexOfLowestVariationGreen);

                List<ArrayList<Integer>> listsBlue = moveThroughField(w, h, BLUE_CHANNEL);
                int indexOfLowestVariationBlue = getIndexOfLowestVariation(listsBlue);
                int lowestAverageBlue = listsBlue.get(0).get(indexOfLowestVariationBlue);

                Color newColor = new Color(lowestAverageRed, lowestAverageGreen, lowestAverageBlue);
                copy.setRGB(w, h, newColor.getRGB());
            }
        }
        return copy;
    }

    private static List<ArrayList<Integer>> moveThroughField(int w, int h, int channel) {
        ArrayList<Integer> field;
        ArrayList<Integer> averages = new ArrayList<>();
        ArrayList<Integer> variations = new ArrayList<>();
        int width = w;
        int height = h;
        int boundary = size / 2;
        int average;

        for (int i = 0; i < 4; i++) {
            field = getField(width, height, channel);

            average = countAverage(field);
            averages.add(average);
            variations.add(countVariation(field, average));

            if (i == 0 || i == 2)
                width += boundary;
            else {
                height += boundary;
                width -= boundary;
            }
        }

        return List.of(averages, variations);
    }

    private static ArrayList<Integer> getField(int w, int h, int channel) {
        ArrayList<Integer> field = new ArrayList<>();
        int color;
        int boundary = size / 2;

        for (int i = boundary * -1; i <= 0; i++) {
            for (int j = boundary * -1; j <= 0; j++) {
                switch (channel) {
                    case RED_CHANNEL:
                        color = new Color(image.getRGB(w + i, h + j)).getRed();
                        break;
                    case GREEN_CHANNEL:
                        color = new Color(image.getRGB(w + i, h + j)).getGreen();
                        break;
                    case BLUE_CHANNEL:
                        color = new Color(image.getRGB(w + i, h + j)).getBlue();
                        break;
                    default:
                        color = 0;
                }

                field.add(color);
            }
        }

        return field;
    }

    private static int countAverage(ArrayList<Integer> field) {
        int sum = field
                .stream()
                .mapToInt(value -> value)
                .sum();

        return sum / field.size();
    }

    private static int countVariation(ArrayList<Integer> field, int average) {
        int sum = field
                .stream()
                .mapToInt(value -> (int) Math.pow(value - average, 2))
                .sum();

        return sum / field.size();
    }

    private static int getIndexOfLowestVariation(List<ArrayList<Integer>> lists) {
        ArrayList<Integer> variations = lists.get(1);

        int min = variations
                .stream()
                .min(Comparator.comparing(Integer::valueOf))
                .get();

        return variations.indexOf(min);
    }
}
