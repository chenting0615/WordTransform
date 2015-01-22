package mandozzi.talkotest.wordtransform;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class WordTransform {
    
    private static FiveLetterDictionary mDictionary;
    private static LinkedList<String> mSolution = new LinkedList<String>();
    private static ExecutorService mExecutorService;
    private static Integer mNumThreads;

    
    public static void main(String [] args) {
        Scanner in = new Scanner(System.in);
        String startWord;
        String endWord;
        boolean useGraph;
        // Loop until the user exits the program through the prompt.
        while (true) {
            System.out.println("Enter the Starting Word: ");
            startWord = in.nextLine();
            System.out.println("Enter the Ending Word: ");
            endWord = in.nextLine();
            System.out.println("Use the pre-constructed graph of the dictionary? (y/N): ");
            String response = in.nextLine();
            if (startWord != null && startWord.length() == 5 && endWord != null && endWord.length() == 5) {
                // Only continue if both words are five letters long.
                if (!isStringYesValue(response)) {
                    useGraph = false;
                } else {
                    useGraph = true;
                }
                LinkedList<String> solution = findShortestTransform(startWord, endWord, useGraph);
                if (solution != null && !solution.isEmpty()){
                    System.out.println(Arrays.toString(solution.toArray()));
                } else {
                    System.out.println("Sorry, no solution exists");
                }
            } else {
                System.out.println("Please enter two five-letter words.");
            }
            System.out.println("Run again? (y/N):");
            response = in.nextLine();
            // Default to exiting the application
            if (!isStringYesValue(response)) {
                in.close();
                return;
            }
        }
    }

    /**
     * Returns true if the input string is 'y', 'yes', 'true', case insensitive
     * 
     */
    public static boolean isStringYesValue(String response) {
        return (response != null && (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("true")));
    }

    /**
     * Initialize the dictionary and construct the graph
     */
    public static void initDictionary() {
        if (mDictionary == null) {
            mDictionary = new FiveLetterDictionary(true);
        } else {
            mDictionary.setUseGraph(true);
        }
    }
    /**
     * ALGORITHM OVERVIEW:
     * To find the shortest transform between two words, a Breadth-First-Search is used.
     * A queue (in this case, a Linked List) is created of paths (LinkedList of Strings). The first path
     * consists of only the starting word.
     * A path is dequeued, and its neighbors are fetched from the dictionary (see FiveLetterDictionary for details)
     * then if none of the neighbors are the target word, they are appended to the path and put into the queue.
     * This process is completed until the target word is reached. Because of the BFS, the first time we hit the target word
     * will be the shortest path, although there may be more than one shortest path.
     * @return 
     * 
     */
    public static  LinkedList<String> findShortestTransform(String startWord, String endWord, boolean useGraph) {
        // Create the dictionary if it doesn't exist, or configure it to the user's desired graph setting.
        if (mDictionary == null) {
            mDictionary = new FiveLetterDictionary(useGraph);
        } else {
            mDictionary.setUseGraph(useGraph);
        }
	    if (!mDictionary.isValidWord(startWord)) {
	        System.out.println(startWord + " is not a valid word.");
	        return null;
	    } else if(!mDictionary.isValidWord(endWord)) {
	        System.out.println(endWord + " is not a valid word.");
	        return null;
	    }
        LinkedList<String> startingList = new LinkedList<String>();
        startingList.add(startWord);
        // If the start word and end word match, don't continue.
        if (startWord.equals(endWord)) {
            return startingList;
        }
        mNumThreads = Runtime.getRuntime().availableProcessors(); // Launch one thread for each processor
        mExecutorService = Executors.newFixedThreadPool(mNumThreads);
        LinkedList<LinkedList<String>> wordQueue = new LinkedList<LinkedList<String>>();
        wordQueue.add(startingList);
        HashSet<String> visited = new HashSet<String>();
        while (!wordQueue.isEmpty()) {
            LinkedList<String> currentPath = wordQueue.poll();
            HashSet<String> neighbors;
            neighbors = mDictionary.getNeighbors(currentPath.getLast());
            Iterator<String> it = neighbors.iterator();
            while (it.hasNext()) {
                String nextWord = it.next();
                if (nextWord.equals(endWord)) {
                    // The matching path is found!
                    currentPath.add(nextWord);
                    return currentPath;
                } else if (!visited.contains(nextWord)) {
                    // Haven't reached the end yet, so add the current path to the queue if we haven't visited the word already.
                    LinkedList<String> newPath = new LinkedList<String>(currentPath);
                    newPath.addLast(nextWord);
                    wordQueue.add(newPath);
                    visited.add(nextWord);
                }
            }
            // Once the wordQueue is big enough to give each thread at least one queue, launch all the threads,
            // balancing the load between the threads.
            if (wordQueue.size() > mNumThreads) {
                int pathsPerThread = wordQueue.size() / mNumThreads;
                int remainder = wordQueue.size() % mNumThreads;
                int threadsStarted = mNumThreads;
                for (int i = 0; i < threadsStarted; i++) {
                    LinkedList<LinkedList<String>> threadQueue = new LinkedList<LinkedList<String>>();
                    for (int j = 0; j < pathsPerThread; j++) {
                        threadQueue.add(wordQueue.poll());
                    }
                    if (remainder > 0) {
                        threadQueue.add(wordQueue.poll());
                        remainder--;
                    }
                    mExecutorService.execute(new WordTransformRunnable(threadQueue, endWord));
                }
                break;
            }
        }
        // After all the threads are launched, wait for them to terminate.
        try {
            mExecutorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (mSolution == null) {
            System.out.println("Sorry, there is no valid transformation between " + startWord + " and " + endWord);
        }
        return mSolution;
    }
    
    private static class WordTransformRunnable implements Runnable {
        private String endWord;
        private LinkedList<LinkedList<String>> wordQueue;
        HashSet<String> visited = new HashSet<String>();

        public WordTransformRunnable (LinkedList<LinkedList<String>> queue, String target) {
            wordQueue = queue;
            endWord = target;
            for (LinkedList<String> path : queue) {
                for (String word : path) {
                    visited.add(word);
                }
            }
        }

        public void run() {
            while (!wordQueue.isEmpty()) {
                LinkedList<String> currentPath = wordQueue.poll();
                synchronized (mSolution) {
                    // If the path is the same size or bigger than the current best solution, there is no need to continue.
                    if (mSolution.size() >= currentPath.size()) {
                        break;
                    }
                }
                HashSet<String> neighbors;
                neighbors = mDictionary.getNeighbors(currentPath.getLast());
                Iterator<String> it = neighbors.iterator();
                while (it.hasNext()) {
                    String nextWord = it.next();
                    LinkedList<String> newPath = new LinkedList<String>(currentPath);
                    newPath.addLast(nextWord);
                    if (nextWord.equals(endWord)) {
                        // The matching path is found!
                        synchronized (mSolution) {
                            if (mSolution.isEmpty() || mSolution.size() > newPath.size()) {
                                mSolution = newPath;
                            }
                            mNumThreads--;
                            if (mNumThreads == 0) {
                                mExecutorService.shutdown();
                            }
                        }
                        return;
                    } else if (!visited.contains(nextWord)) {
                        // Haven't reached the end yet, so add the current path to the queue if we haven't visited the word already.
                        wordQueue.add(newPath);
                        visited.add(nextWord);
                   }
                }
            }
            synchronized (mNumThreads) {
                mNumThreads--;
                if (mNumThreads == 0) {
                    mExecutorService.shutdown();
                }
            }
        }
    }
}
