import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Main {
    private static final int POPULATION_SIZE = 100;
    private static final double MUTATION_RATE = 0.01;
    private static final int MAX_GENERATIONS = 5000;

    private static final String TARGET_STRING = "Algorithmique avancée";
    public static final Color TARGET_COLOR = new Color(255, 0, 0);
    private static final Random random = new Random();

    public static void main(String[] args) {
        List<Individual> population = initializePopulation();
        List<Individual> paretoOptimalSolutions = new ArrayList<>();

        for (int generation = 1; generation <= MAX_GENERATIONS; generation++) {
            List<Individual> offspring = new ArrayList<>();

            while (offspring.size() < POPULATION_SIZE) {
                Individual parent1 = selectParent(population);
                Individual parent2 = selectParent(population);

                Individual child = crossover(parent1, parent2);

                if (random.nextDouble() < MUTATION_RATE) {
                    child = mutate(child);
                }

                offspring.add(child);
            }

            population = offspring;

            // Display the color in the console using ANSI escape codes
            System.out.println(formatOutput(generation, population.get(0)));
        }

        // Find and display Pareto optimal solutions
        paretoOptimalSolutions = filterParetoOptimal(population);
        System.out.println("\n\nPareto Optimal Solutions:");

        for (Individual ind : paretoOptimalSolutions) {
            System.out.println(formatOutput(0, ind)); // Display generation as 0 for Pareto solutions
        }
    }


    private static String formatOutput(int generation, Individual individual) {
        // Print the color as ANSI escape code
        System.out.print("\u001B[38;2;" + individual.getGeneColor(0).getRed() + ";"
                + individual.getGeneColor(0).getGreen() + ";" + individual.getGeneColor(0).getBlue() + "m");

        return String.format("Generation %d: %s, Fitness1: %d, Fitness2: %d",
                generation, individual.getGenes(), individual.getFitness1(), individual.getFitness2());
    }

    private static List<Individual> initializePopulation() {
        List<Individual> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            String genes = generateRandomGenes(TARGET_STRING.length());
            Color color = generateRandomColor();
            population.add(new Individual(genes, color, TARGET_STRING));
        }
        return population;
    }

    private static String generateRandomGenes(int length) {
        StringBuilder genes = new StringBuilder();
        for (int i = 0; i < length; i++) {
            genes.append((char) (random.nextInt(94) + 32)); // ASCII printable characters
        }
        return genes.toString();
    }

    private static Color generateRandomColor() {
        return new Color(
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256)
        );
    }
    private static Individual selectParent(List<Individual> population) {
        int tournamentSize = 5;
        List<Individual> tournament = new ArrayList<>();

        for (int i = 0; i < tournamentSize; i++) {
            tournament.add(population.get(random.nextInt(POPULATION_SIZE)));
        }

        // Filter non-dominated individuals
        List<Individual> nonDominated = filterParetoOptimal(tournament);

        // If there are non-dominated individuals, select randomly from them
        // Otherwise, select randomly from the entire tournament
        if (!nonDominated.isEmpty()) {
            return nonDominated.get(random.nextInt(nonDominated.size()));
        } else {
            return tournament.get(random.nextInt(tournamentSize));
        }
    }

    private static List<Individual> filterParetoOptimal(List<Individual> individuals) {
        List<Individual> paretoOptimal = new ArrayList<>(individuals);

        for (Individual ind1 : individuals) {
            for (Individual ind2 : individuals) {
                if (ind1 != ind2 && isParetoDominated(ind1, ind2)) {
                    paretoOptimal.remove(ind1);
                    break;
                }
            }
        }

        return paretoOptimal;
    }

    private static boolean isParetoDominated(Individual ind1, Individual ind2) {
        return ind1.getFitness1() >= ind2.getFitness1() && ind1.getFitness2() >= ind2.getFitness2()
                && (ind1.getFitness1() > ind2.getFitness1() || ind1.getFitness2() > ind2.getFitness2());
    }

    private static Individual crossover(Individual parent1, Individual parent2) {
        int crossoverPoint = random.nextInt(parent1.getGenes().length());
        String childGenes = parent1.getGenes().substring(0, crossoverPoint) +
                parent2.getGenes().substring(crossoverPoint);
        Color childColor = new Color(
                (parent1.getGeneColor(0).getRed() + parent2.getGeneColor(0).getRed()) / 2,
                (parent1.getGeneColor(0).getGreen() + parent2.getGeneColor(0).getGreen()) / 2,
                (parent1.getGeneColor(0).getBlue() + parent2.getGeneColor(0).getBlue()) / 2
        );
        return new Individual(childGenes, childColor, TARGET_STRING);
    }

    private static Individual mutate(Individual individual) {
        int mutationPoint = random.nextInt(individual.getGenes().length());
        char[] genesArray = individual.getGenes().toCharArray();
        genesArray[mutationPoint] = (char) (random.nextInt(94) + 32);
        Color mutatedColor = new Color(
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256)
        );
        return new Individual(new String(genesArray), mutatedColor, TARGET_STRING);
    }
}