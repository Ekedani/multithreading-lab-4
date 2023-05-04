package common;

import java.io.File;
import java.util.HashSet;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        final String TEXTS_DIRECTORY = "D:\\KPI\\H1'23\\Parallel\\lab-4\\data\\16";
        var textsDirectory = new File(TEXTS_DIRECTORY);

        HashSet<String> commonWords;
        CommonWordsSearchTask commonWordsSearchTask;
        try (var forkJoinPool = new ForkJoinPool()) {
            commonWordsSearchTask = new CommonWordsSearchTask(textsDirectory);
            commonWords = forkJoinPool.invoke(commonWordsSearchTask);
        }

        System.out.println("===== COMMON WORDS SEARCH RESULT =====");
        System.out.println("Common words found:");
        int printedWords = 0;
        for (var word : commonWords) {
            System.out.print(word);
            printedWords++;
            System.out.print(printedWords % 30 == 0 ? "\n" : " ");
        }
        System.out.println();
        System.out.println("Number of common words found: " + commonWords.size());
    }
}