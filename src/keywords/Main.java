package keywords;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        final String TEXTS_DIRECTORY = "D:\\KPI\\H1'23\\Parallel\\lab-4\\data\\16";
        var textsDirectory = new File(TEXTS_DIRECTORY);
        ArrayList<String> keywords = new ArrayList<>(Arrays.asList(
                "programming", "computer", "programmer", "algorithm", "http", "tcp"
        ));

        List<String> filesWithKeywords;
        SearchByKeywordsTask searchByKeywordsTask;
        try (var forkJoinPool = new ForkJoinPool()) {
            searchByKeywordsTask = new SearchByKeywordsTask(textsDirectory, keywords);
            filesWithKeywords = forkJoinPool.invoke(searchByKeywordsTask);
        }

        System.out.println("===== FILES WITH KEYWORDS SEARCH RESULT =====");
        System.out.println("Keywords: " + keywords);
        for (var file : filesWithKeywords) {
            System.out.println(file);
        }
        System.out.println("Total files with keywords found: " + filesWithKeywords.size());
    }
}