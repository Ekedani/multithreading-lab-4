package analysis;

import java.io.*;
import java.util.*;
import java.util.concurrent.RecursiveTask;

public class StatisticAnalysisTask extends RecursiveTask<HashMap<Integer, Long>> {
    final File textFile;

    private final HashMap<Integer, Long> sample = new HashMap<>();

    public StatisticAnalysisTask(File textFile) {
        this.textFile = textFile;
    }

    @Override
    protected HashMap<Integer, Long> compute() {
        if (textFile.isDirectory()) {
            List<StatisticAnalysisTask> subTasks = new ArrayList<>();
            var subFiles = textFile.listFiles();
            assert subFiles != null;
            for (var subFile : subFiles) {
                var subTask = new StatisticAnalysisTask(subFile);
                subTasks.add(subTask);
                subTask.fork();
            }
            for (var subTask : subTasks) {
                extendSample(subTask.join());
            }
        } else {
            try {
                processTextFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return this.sample;
    }

    private HashMap<Integer, Double> getDistributionLaw() {
        HashMap<Integer, Double> distributionLaw = new HashMap<>();
        double sum = 0;
        for (var entry : sample.entrySet()) {
            sum += entry.getValue();
        }
        for (var entry : sample.entrySet()) {
            double probability = entry.getValue() / sum;
            distributionLaw.put(entry.getKey(), probability);
        }
        return distributionLaw;
    }

    public double getMean() {
        var distributionLaw = this.getDistributionLaw();
        double mean = 0;
        for (var entry : distributionLaw.entrySet()) {
            mean += entry.getKey() * entry.getValue();
        }
        return mean;
    }

    public double getVariance() {
        var distributionLaw = this.getDistributionLaw();
        double variance = 0;
        for (var entry : distributionLaw.entrySet()) {
            variance += Math.pow(entry.getKey(), 2) * entry.getValue();
        }
        variance -= Math.pow(this.getMean(), 2);
        return variance;
    }

    public double getStandardDeviation() {
        return Math.sqrt(this.getVariance());
    }

    private void extendSample(HashMap<Integer, Long> extensionSample) {
        for (var entry : extensionSample.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            if (sample.containsKey(key)) {
                sample.put(key, sample.get(key) + value);
            } else {
                sample.put(key, value);
            }
        }
    }

    private List<String> getWords() throws IOException {
        List<String> words = new ArrayList<>();
        String currentLine;
        var bufferedReader = new BufferedReader(new FileReader(textFile));
        while ((currentLine = bufferedReader.readLine()) != null) {
            var tokens = currentLine.split("[\\s,:;.]+");
            for (var token : tokens) {
                if (token.matches("[-'\\p{L}]+")) {
                    words.add(token);
                }
            }
        }
        return words;
    }

    private void processTextFile() throws IOException {
        var words = this.getWords();
        for (var word : words) {
            if (sample.containsKey(word.length())) {
                sample.put(word.length(), sample.get(word.length()) + 1);
            } else {
                sample.put(word.length(), 1L);
            }
        }
    }
}
