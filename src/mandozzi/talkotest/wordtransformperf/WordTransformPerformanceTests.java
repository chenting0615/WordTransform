package mandozzi.talkotest.wordtransformperf;

import mandozzi.talkotest.wordtransform.FiveLetterDictionary;
import mandozzi.talkotest.wordtransform.WordTransform;
import mandozzi.talkotest.wordtransformsinglethread.WordTransformSingleThread;

public class WordTransformPerformanceTests {

    private static int num_trials = 100;
    public static void main(String [] args) {
        WordTransform.initDictionary();
        System.out.println(num_trials + " transformation with graph: ");
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < num_trials; i++) {
            WordTransform.findShortestTransform("smart", "brain", true);
        }
        System.out.println((System.currentTimeMillis() - startTime)/num_trials + " per transform");
        System.out.println(num_trials + " transformation without graph: ");
        startTime = System.currentTimeMillis();
        for (int i = 0; i < num_trials; i++) {
            WordTransform.findShortestTransform("smart", "brain", false);
        }
        System.out.println((System.currentTimeMillis() - startTime)/num_trials + " per transform");
        WordTransformSingleThread.initDictionary();
        System.out.println(num_trials + " single-thread transformation with graph: ");
        startTime = System.currentTimeMillis();
        for (int i = 0; i < num_trials; i++) {
            WordTransformSingleThread.findShortestTransform("smart", "brain", true);
        }
        System.out.println((System.currentTimeMillis() - startTime)/num_trials + " per transform");
        System.out.println(num_trials + " single-thread transformation without graph: ");
        startTime = System.currentTimeMillis();
        for (int i = 0; i < num_trials; i++) {
            WordTransformSingleThread.findShortestTransform("smart", "brain", false);
        }
        System.out.println((System.currentTimeMillis() - startTime)/num_trials + " per transform");
        startTime = System.currentTimeMillis();
        System.out.println(num_trials + " dictionary inits: ");
        for (int i = 0; i < num_trials; i++) {
            new FiveLetterDictionary(false);
        }
        System.out.println((System.currentTimeMillis() - startTime)/num_trials + " per init");

        startTime = System.currentTimeMillis();
        System.out.println(num_trials + " dictionary inits with graph construction: ");
        for (int i = 0; i < num_trials; i++) {
            new FiveLetterDictionary(true);
        }
        System.out.println((System.currentTimeMillis() - startTime)/num_trials + " per init");

    }
}
