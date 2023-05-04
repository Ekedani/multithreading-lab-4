package keywords;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;

public class SearchByKeywordsTask extends RecursiveTask<ArrayList<String>> {
    final File textFile;
    final ArrayList<String> keywords;

    public SearchByKeywordsTask(File textFile, ArrayList<String> keywords) {
        this.textFile = textFile;
        this.keywords = keywords;
    }

    @Override
    protected ArrayList<String> compute() {
        return null;
    }


}
