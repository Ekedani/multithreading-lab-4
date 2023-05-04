package analysis;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        final String TEXTS_DIRECTORY = "D:\\KPI\\H1'23\\Parallel\\lab-4\\data";
        var textsDirectory = new File(TEXTS_DIRECTORY);

        StatisticAnalysisTask statisticAnalysisTask;
        try (var forkJoinPool = new ForkJoinPool()) {
            statisticAnalysisTask = new StatisticAnalysisTask(textsDirectory);
            forkJoinPool.invoke(statisticAnalysisTask);
        }

        System.out.println("===== STATISTIC ANALYSIS RESULT =====");
        System.out.println("Distribution law:");
        printDistributionLaw(statisticAnalysisTask.getDistributionLaw());
        System.out.println();
        System.out.println("Mean: " + statisticAnalysisTask.getMean());
        System.out.println("Variance: " + statisticAnalysisTask.getVariance());
        System.out.println("Standard Derivation: " + statisticAnalysisTask.getStandardDeviation());
    }

    public static void printDistributionLaw(HashMap<Integer, Double> distributionLaw) {
        System.out.print("| x | ");
        for (Integer key : distributionLaw.keySet()) {
            System.out.printf("%-10d| ", key);
        }
        System.out.println();
        System.out.print("| p | ");
        for (Double value : distributionLaw.values()) {
            System.out.printf("%-10.6f| ", value);
        }
        System.out.println();
    }
}