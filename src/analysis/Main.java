package analysis;

import java.io.File;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        final String TEXTS_DIRECTORY = "";
        var textsDirectory = new File(TEXTS_DIRECTORY);

        var forkJoinPool = new ForkJoinPool();
        var statisticAnalysisTask = new StatisticAnalysisAction(textsDirectory);
    }
}