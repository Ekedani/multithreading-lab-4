package keywords;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class SearchByKeywordsTask extends RecursiveTask<HashMap<String, HashMap<String, Long>>> {
    final File textFile;
    final ArrayList<String> keywords;
    final HashMap<String, HashMap<String, Long>> foundFilesStatistics = new HashMap<>();

    public SearchByKeywordsTask(File textFile, ArrayList<String> keywords) {
        this.textFile = textFile;
        this.keywords = keywords;
    }

    @Override
    protected HashMap<String, HashMap<String, Long>> compute() {
        if (textFile.isDirectory()) {
            List<SearchByKeywordsTask> subTasks = new ArrayList<>();
            var subFiles = textFile.listFiles();
            assert subFiles != null;
            for (var subFile : subFiles) {
                var subTask = new SearchByKeywordsTask(subFile, keywords);
                subTasks.add(subTask);
                subTask.fork();
            }
            for (var subTask : subTasks) {
                this.foundFilesStatistics.putAll(subTask.join());
            }
        } else {
            try {
                processTextFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return this.foundFilesStatistics;
    }

    private List<String> getWords() throws IOException {
        List<String> words = new ArrayList<>();
        String currentLine;
        var bufferedReader = new BufferedReader(new FileReader(textFile));
        while ((currentLine = bufferedReader.readLine()) != null) {
            var tokens = currentLine.split("[\\s,:;.?!]+");
            for (var token : tokens) {
                if (token.matches("\\p{L}[\\p{L}-']*")) {
                    words.add(token);
                }
            }
        }
        return words;
    }

    private void processTextFile() throws IOException {
        HashMap<String, Long> keywordsSample = new HashMap<>();
        var words = this.getWords();
        for (var word : words) {
            var normalizedWord = word.toLowerCase();
            if (keywords.contains(normalizedWord)) {
                if (keywordsSample.containsKey(normalizedWord)) {
                    keywordsSample.put(normalizedWord, keywordsSample.get(normalizedWord) + 1);
                } else {
                    keywordsSample.put(normalizedWord, 1L);
                }
            }
        }
        if (!keywordsSample.isEmpty()) {
            foundFilesStatistics.put(textFile.getAbsolutePath(), keywordsSample);
        }
    }
}
