package analysis;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static boolean TESTING_SERIAL = false;
    public static int WORKER_THREADS = 12;

    public static void main(String[] args) {
        final String TEXTS_DIRECTORY = "D:\\KPI\\H1'23\\Parallel\\lab-4\\data\\16";
        var textsDirectory = new File(TEXTS_DIRECTORY);

        if (TESTING_SERIAL) {
            SerialAnalysisTask serialAnalysisTask = new SerialAnalysisTask(textsDirectory);
            long start = System.nanoTime();
            serialAnalysisTask.compute();
            long finish = System.nanoTime();
            System.out.println("Execution time: " + (finish - start) + " ns");
            System.out.println("===== STATISTIC ANALYSIS RESULT =====");
            System.out.println("Distribution law:");
            printDistributionLaw(serialAnalysisTask.getDistributionLaw());
            System.out.println();
            System.out.println("Mean: " + serialAnalysisTask.getMean() + " ");
            System.out.println("Variance: " + serialAnalysisTask.getVariance());
            System.out.println("Standard Derivation: " + serialAnalysisTask.getStandardDeviation());
        } else {
            StatisticAnalysisTask statisticAnalysisTask;
            try (var forkJoinPool = new ForkJoinPool(WORKER_THREADS)) {
                statisticAnalysisTask = new StatisticAnalysisTask(textsDirectory);
                long start = System.nanoTime();
                forkJoinPool.invoke(statisticAnalysisTask);
                long finish = System.nanoTime();
                System.out.println("Execution time: " + (finish - start) + " ns");
            }

            System.out.println("===== STATISTIC ANALYSIS RESULT =====");
            System.out.println("Distribution law:");
            printDistributionLaw(statisticAnalysisTask.getDistributionLaw());
            System.out.println();
            System.out.println("Mean: " + statisticAnalysisTask.getMean() + " ");
            System.out.println("Variance: " + statisticAnalysisTask.getVariance());
            System.out.println("Standard Derivation: " + statisticAnalysisTask.getStandardDeviation());
        }
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