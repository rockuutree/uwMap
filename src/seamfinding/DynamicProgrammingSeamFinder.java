package seamfinding;

import seamfinding.energy.EnergyFunction;

import java.util.List;
import java.util.ArrayList;

/**
 * Dynamic programming implementation of the {@link SeamFinder} interface.
 *
 * @see SeamFinder
 */
public class DynamicProgrammingSeamFinder implements SeamFinder {

    @Override
    public List<Integer> findHorizontal(Picture picture, EnergyFunction f) {
        List<Integer> result = new ArrayList<>(picture.height());
        Double[][] pixelsEnergies = new Double[picture.height()][picture.width()];

        generatePixels(pixelsEnergies, picture, f);
        generateShortestEnergyPath(result, pixelsEnergies, picture);

        return result;
    }

    private void generatePixels(Double[][] pixelsEnergies, Picture picture, EnergyFunction f) {
        // last column
        for (int y = 0; y < picture.height(); y++) {
            pixelsEnergies[y][picture.width() - 1] = f.apply(picture, picture.width() - 1, y);
        }

        // remaining columns
        for (int x = picture.width() - 2; x >= 0; x--) {
            for (int y = 0; y < picture.height(); y++) {
                double min = Double.POSITIVE_INFINITY;
                for (int z = y - 1; z <= y + 1; z++) {
                    if (0 <= z && z < picture.height()) {
                        double curr = pixelsEnergies[z][x + 1];
                        if (curr < min) {
                            min = curr;
                        }
                    }
                }
                pixelsEnergies[y][x] = f.apply(picture, x, y) + min;
            }
        }
    }

    private void generateShortestEnergyPath(List<Integer> result, Double[][] pixelsEnergies, Picture picture) {
        double min = Double.POSITIVE_INFINITY;
        int min_y = -1;
        for (int y = 0; y < picture.height(); y++) {
            double curr = pixelsEnergies[y][0];
            if (curr < min) {
                min = curr;
                min_y = y;
            }
        }

        result.add(min_y);

        // remaining columns, has to be one of the previous column's neighbors
        for (int x = 1; x < picture.width(); x++) {
            min = Double.POSITIVE_INFINITY;
            int y = min_y;
            for (int z = y - 1; z <= y + 1; z++) {
                if (0 <= z && z < picture.height()) {
                    double curr = pixelsEnergies[z][x];
                    if (curr < min) {
                        min = curr;
                        min_y = z;
                    }
                }
            }
            result.add(min_y);
        }
    }
}