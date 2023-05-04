package keywords;

import common.CommonWordsSearchTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class SearchByKeywordsTask extends RecursiveTask<ArrayList<String>> {
    final File textFile;
    final ArrayList<String> keywords;
    final ArrayList<String> foundFiles = new ArrayList<>();

    public SearchByKeywordsTask(File textFile, ArrayList<String> keywords) {
        this.textFile = textFile;
        this.keywords = keywords;
    }

    @Override
    protected ArrayList<String> compute() {
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
                this.foundFiles.addAll(subTask.join());
            }
        } else {
            try {
                processTextFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return this.foundFiles;
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
        var words = this.getWords();
        for (var word : words) {
            if (keywords.contains(word.toLowerCase())) {
                foundFiles.add(this.textFile.getAbsolutePath());
                return;
            }
        }
    }
}
