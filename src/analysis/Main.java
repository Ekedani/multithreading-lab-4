package analysis;

import java.io.File;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        final String TEXTS_DIRECTORY = "D:\\KPI\\H1'23\\Parallel\\lab-4\\data";
        var textsDirectory = new File(TEXTS_DIRECTORY);

        StatisticAnalysisTask mainStatisticAnalysisTask;
        try (var forkJoinPool = new ForkJoinPool()) {
            mainStatisticAnalysisTask = new StatisticAnalysisTask(textsDirectory);
            forkJoinPool.invoke(mainStatisticAnalysisTask);
        }

        System.out.println("===== RANDOM VARIABLE PARAMETERS =====");
        System.out.println("Mean: " + mainStatisticAnalysisTask.getMean());
        System.out.println("Variance: " + mainStatisticAnalysisTask.getVariance());
        System.out.println("Standard Derivation: " + mainStatisticAnalysisTask.getStandardDeviation());
    }
}