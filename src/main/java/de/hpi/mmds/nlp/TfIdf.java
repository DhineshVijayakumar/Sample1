package de.hpi.mmds.nlp;

import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.WordTokenFactory;
import edu.stanford.nlp.stats.ClassicCounter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by axel on 07.05.16.
 */
public class TfIdf {
    private ClassicCounter<String> dfCounter;
    private int docCounter;
    private List<Word> stopwords;
    private final static String stopwordPath = "resources/stopwords.txt";

    public TfIdf() {
        dfCounter = new ClassicCounter<String>();
        docCounter = 0;
        try {
            PTBTokenizer tokenizer = new PTBTokenizer<>(new FileReader(stopwordPath), new WordTokenFactory(), "");
            stopwords = new ArrayList<Word>(new HashSet<Word>(tokenizer.tokenize()));
        }
        catch (FileNotFoundException i){
            System.out.println("Did not find stopword list");
            stopwords = new ArrayList<Word>();
        }

    }

    public List<Word> removeStopwords(List<Word> input){
        input.removeAll(stopwords);
        return input;
    }

    /**
    * Takes a text and adds its tokens to the index. Treats one input as one document.
     **/
    public void addReviewText(String reviewText) {
        //TODO: lowercase everything
        PTBTokenizer tokenizer = new PTBTokenizer<>(new StringReader(reviewText),
                new WordTokenFactory(), "");
        List<Word> uniqueWordList = new ArrayList<Word>(new HashSet<Word>(tokenizer.tokenize()));
        uniqueWordList = removeStopwords(uniqueWordList);
        for (Word word : uniqueWordList) {
            dfCounter.incrementCount(word.toString());
        }
        docCounter += 1;
    }

    /**
     *
     * Takes a review as input and computes the tf-idf score for each word in the input
     * Returns a Hashmap <Word, TfIdf>
     **/
    public HashMap<String, Double> getTfIdf(String reviewText) {
        //TODO: lowercase everything
        HashMap<String, Double> tfidfMap = new HashMap<>();
        PTBTokenizer tokenizer = new PTBTokenizer<>(new StringReader(reviewText),
                new WordTokenFactory(), "");
        List<Word> wordList = tokenizer.tokenize();
        wordList = removeStopwords(wordList);
        ClassicCounter<String> wordCounter = new ClassicCounter<String>();
        for (Word word : wordList) {
            wordCounter.incrementCount(word.toString());
        }
        for (Word word : wordList) {
            double tf = wordCounter.getCount(word.toString());
            double df = dfCounter.getCount(word.toString()) / Math.max(docCounter, 1);
            double tfidf = tf / Math.max(df, 1.0 / Math.max(docCounter, 1)); // add Laplace Smoothing for unseen words
            tfidfMap.put(word.toString(), tfidf);
        }
        return tfidfMap;

    }

}