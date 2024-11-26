import java.awt.*;

class Individual {
    private String genes;
    private int fitness1; // Objective 1: Number of characters that didn't match the target string
    private int fitness2; // Objective 2: Color difference

    private final Color targetColor;

    public Individual(String genes, Color color, String target) {
        this.genes = genes;
        this.targetColor = color;
        calculateFitness(target);
    }

    private void calculateFitness(String target) {
        int nonMatchingChars = 0;
        double colorDifference = calculateColorDifference(targetColor, Main.TARGET_COLOR);

        for (int i = 0; i < target.length(); i++) {
            if (genes.charAt(i) != target.charAt(i)) {
                nonMatchingChars++;
            }
        }

        this.fitness1 = nonMatchingChars;
        this.fitness2 = (int) -colorDifference;
    }



    public Color getGeneColor(int index) {
        return new Color((int) genes.charAt(index), 0, 0);
    }

    private double calculateColorDifference(Color color1, Color color2) {
        int redDiff = color1.getRed() - color2.getRed();
        int greenDiff = color1.getGreen() - color2.getGreen();
        int blueDiff = color1.getBlue() - color2.getBlue();

        
        return Math.sqrt(redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
    }

    public String getGenes() {
        return genes;
    }

    public void setGenes(String genes) {
        this.genes = genes;
    }

    public int getFitness1() {
        return fitness1;
    }

    public void setFitness1(int fitness1) {
        this.fitness1 = fitness1;
    }

    public int getFitness2() {
        return fitness2;
    }

    public void setFitness2(int fitness2) {
        this.fitness2 = fitness2;
    }

    public Color getTargetColor() {
        return targetColor;
    }
}