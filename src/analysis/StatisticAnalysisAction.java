package analysis;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.RecursiveAction;

public class StatisticAnalysisAction extends RecursiveAction {
    final File textFile;

    private final HashMap<Integer, Long> distributionLaw = new HashMap<>();

    public StatisticAnalysisAction(File textFile) {
        this.textFile = textFile;
    }

    @Override
    protected void compute() {
        if (textFile.isDirectory()) {

        }
    }

    public double getMean() {
        return 0;
    }

    public double getVariance() {
        return 0;
    }

    public double getStandardDeviation() {
        return 0;
    }

    private void concatenateDistributionLaws() {}

    private void getWords(){}

    private void processFile(){}
}
