package mandozzi.talkotest.wordtransform;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class FiveLetterDictionary {

    private HashMap<String, HashSet<String>> wordMap;
    private boolean useGraph = false;
    private boolean hasGraph = false;
    private char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m',
                               'n','o','p','q','r','s','t','u','v','w','x','y','z'};

    public FiveLetterDictionary(boolean useGraph) {
        initDictionary();
        setUseGraph(useGraph);
    }
    
    public HashMap<String, HashSet<String>> getWordMap() {
        return wordMap;
    }
    
    public boolean getHasGraph() {
        return hasGraph;
    }
    
    public boolean getUseGraph() {
        return useGraph;
    }

    public void setUseGraph(boolean useGraph) {
        this.useGraph = useGraph;
        if (useGraph && !hasGraph) {
            constructGraph();
        }
    }

    private void initDictionary() {
        wordMap = new HashMap<String, HashSet<String>>();
        try {
            Scanner fileScanner = new Scanner(new File("res/fiveletterdictionary.txt"));
            while (fileScanner.hasNext()) {
                String word = fileScanner.nextLine();
                // Place each word into the map with an empty set of neighbors.
                if (!wordMap.containsKey(word)) {
                    wordMap.put(word, new HashSet<String>());
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs a graph of words that differ from each other by one letter.
     * This is done using the wordMap, where the key is the word and the value is all neighbors.
     * In a visualization of the graph, each Node (key) would have an edge between all nodes in it's Neighbor set.
     * 
     * For each word, its neighbors are found by cycling through each character and modifying it, and then calling the isValidWord(String) method.
     * If a word transformation is valid, the new word is added to the neighbor set of the original, 
     * and the original is is added to the neighbor set of the new word.
     */
    public void constructGraph() {
        for (String word : wordMap.keySet()) {
            for (int i = 0; i < word.length(); i ++) {
                for (char c : alphabet) {
                    if (word.charAt(i) != c) {
                        StringBuilder newWordBuilder = new StringBuilder(word);
                        newWordBuilder.setCharAt(i, c);
                        String newWord = newWordBuilder.toString();
                        if (isValidWord(newWord) && !wordMap.get(word).contains(newWord)) {
                            wordMap.get(word).add(newWord);
                            wordMap.get(newWord).add(word);
                        }
                    }
                }
            }
        }
        hasGraph = true;
    }
    /** Returns true if the word is present in the dictionary **/
    public boolean isValidWord(String word) {
        return wordMap.containsKey(word);
    }
    
    /**
     * If the user specified the graph should be used, the pre-computed neighbor set is returned.
     * 
     * Otherwise, for each word, its neighbors are found by cycling through each character and modifying it, and then calling
     * the isValidWord(String) method. If a word transformation is valid, the new word is added to the neighbor set.
     */
    public HashSet<String> getNeighbors(String word) {
        if (useGraph) {
            return wordMap.get(word);
        } else {
            HashSet<String> neighbors = new HashSet<String>();
            for (int i = 0; i < word.length(); i ++) {
                for (char c : alphabet) {
                    if (word.charAt(i) != c) {
                        StringBuilder newWordBuilder = new StringBuilder(word);
                        newWordBuilder.setCharAt(i, c);
                        String newWord = newWordBuilder.toString();
                        if (isValidWord(newWord)) {
                            neighbors.add(newWord);
                        }
                    }
                }
            }
            return neighbors;
        }
    }

    /** For debugging purposes, prints each word and it's neighbors **/
    public void printGraph() {
        for (String word : wordMap.keySet()) {
            StringBuilder outputBuilder = new StringBuilder();
            outputBuilder.append(word).append(": {");
            if (!wordMap.get(word).isEmpty()) {
                Iterator<String> it = wordMap.get(word).iterator();
                while (it.hasNext()) {
                    outputBuilder.append(it.next()).append(", ");
                }
                outputBuilder.delete(outputBuilder.length()-2, outputBuilder.length());
            }
            outputBuilder.append('}');
            System.out.println(outputBuilder.toString());
        }
    }

    /** For debugging purposes, prints each word in the dictionary **/
    public void printMap() {
        for (String word : wordMap.keySet()) {
            System.out.println(word);
        }
    }

}
