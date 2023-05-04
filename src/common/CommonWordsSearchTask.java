package common;

import analysis.StatisticAnalysisTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class CommonWordsSearchTask extends RecursiveTask<HashSet<String>> {
    final File textFile;
    final HashSet<String> commonWords = new HashSet<>();

    public CommonWordsSearchTask(File textFile) {
        this.textFile = textFile;
    }

    @Override
    protected HashSet<String> compute() {
        if (textFile.isDirectory()) {
            List<CommonWordsSearchTask> subTasks = new ArrayList<>();
            var subFiles = textFile.listFiles();
            assert subFiles != null;
            for (var subFile : subFiles) {
                var subTask = new CommonWordsSearchTask(subFile);
                subTasks.add(subTask);
                subTask.fork();
            }
            for (var subTask : subTasks) {
                this.intersectWordSets(subTask.join());
            }
        } else {
            try {
                processTextFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return this.commonWords;
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
            commonWords.add(word.toLowerCase());
        }
    }

    private void intersectWordSets(HashSet<String> intersectedSet) {
        if (this.commonWords.isEmpty()) {
            this.commonWords.addAll(intersectedSet);
        } else {
            this.commonWords.retainAll(intersectedSet);
        }
    }
}
