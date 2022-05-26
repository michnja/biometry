package pl.edu.pb.wi.viewer;

import pl.edu.pb.wi.filtration.KuwaharaFilter;
import pl.edu.pb.wi.filtration.MedianFilter;
import pl.edu.pb.wi.filtration.SimpleFilter;
import pl.edu.pb.wi.histogram.HistogramOperations;
import pl.edu.pb.wi.shared.ImageSharedOperations;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class Viewer extends JFrame {

    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu files = new JMenu("File");
    private final JMenu histogram = new JMenu("Histograms");
    private final JMenu filter = new JMenu("Filter");
    private final JMenuItem loadImage = new JMenuItem("Load image");
    private final JMenuItem saveImage = new JMenuItem("Save image");
    private final JMenuItem calculateHistograms = new JMenuItem("Calculate histograms");
    private final JMenuItem filterImage = new JMenuItem("Filter image");
    private final JMenuItem medianFilter = new JMenuItem("Median filter");
    private final JMenuItem kuwaharaFilter = new JMenuItem("Kuwahara filter");
    private final JLabel imageLabel = new JLabel();

    public Viewer() {
        this.setLayout(new BorderLayout());
        this.setTitle("Podstawy Biometrii");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

        this.menuBar.add(this.files);
        this.menuBar.add(this.histogram);
        this.menuBar.add(this.filter);
        this.files.add(this.loadImage);
        this.files.add(this.saveImage);
        this.histogram.add(this.calculateHistograms);
        this.filter.add(this.filterImage);
        this.filter.add(this.medianFilter);
        this.filter.add(this.kuwaharaFilter);

        this.add(this.menuBar, BorderLayout.NORTH);
        this.add(this.imageLabel, BorderLayout.CENTER);
        this.imageLabel.setHorizontalAlignment(JLabel.CENTER);
        this.imageLabel.setVerticalAlignment(JLabel.CENTER);

        this.loadImage.addActionListener((ActionEvent e) -> {
            JFileChooser imageOpener = new JFileChooser();
            imageOpener.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    String fileName = f.getName().toLowerCase();
                    if (fileName.endsWith(".jpg") || fileName.endsWith(".png")
                            || fileName.endsWith(".tiff") || fileName.endsWith(".jpeg")) {
                        return true;
                    } else return false;
                }

                @Override
                public String getDescription() {
                    return "Image files (.jpg, .png, .tiff)";
                }
            });

            int returnValue = imageOpener.showDialog(null, "Select image");
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                BufferedImage img = ImageSharedOperations.loadImage(imageOpener.getSelectedFile().getPath());
                this.imageLabel.setIcon(new ImageIcon(img));
            }
        });

        this.saveImage.addActionListener((ActionEvent e) -> {
            String path = "./image.jpg";
            BufferedImage img = ImageSharedOperations.convertIconToImage((ImageIcon) this.imageLabel.getIcon());
            ImageSharedOperations.saveImage(img, path);
        });

        this.calculateHistograms.addActionListener((ActionEvent e) -> {
            BufferedImage img = ImageSharedOperations.convertIconToImage((ImageIcon) this.imageLabel.getIcon());
            HistogramOperations.calculateHistograms(img);
        });

        this.filterImage.addActionListener((ActionEvent e) -> {
            BufferedImage img = ImageSharedOperations.convertIconToImage((ImageIcon) this.imageLabel.getIcon());
            int size = Integer.parseInt(JOptionPane.showInputDialog(this,
                    "Enter size of filter (odd values)"));
            BufferedImage result = SimpleFilter.filterImage(img, size);
            this.imageLabel.setIcon(new ImageIcon(result));
        });

        this.medianFilter.addActionListener(e -> {
            BufferedImage img = ImageSharedOperations.convertIconToImage((ImageIcon) this.imageLabel.getIcon());
            int size = Integer.parseInt(JOptionPane.showInputDialog(this,
                    "Enter size of filter (odd values)"));
            BufferedImage result = MedianFilter.filterImage(img, size);
            this.imageLabel.setIcon(new ImageIcon(result));
        });

        this.kuwaharaFilter.addActionListener(e -> {
            BufferedImage img = ImageSharedOperations.convertIconToImage((ImageIcon) this.imageLabel.getIcon());
            int size = Integer.parseInt(JOptionPane.showInputDialog(this,
                    "Enter size of filter (odd values)"));
            BufferedImage result = KuwaharaFilter.filterImage(img, size);
            this.imageLabel.setIcon(new ImageIcon(result));
        });

    }

}
