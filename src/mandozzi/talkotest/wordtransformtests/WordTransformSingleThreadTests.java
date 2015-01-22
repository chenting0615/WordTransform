package mandozzi.talkotest.wordtransformtests;

import static org.junit.Assert.*;

import java.util.LinkedList;


import mandozzi.talkotest.wordtransformsinglethread.WordTransformSingleThread;

import org.junit.Test;

public class WordTransformSingleThreadTests {

    @Test
    public void testIsStringYesValue() {
        assertTrue(WordTransformSingleThread.isStringYesValue("yes"));
        assertTrue(WordTransformSingleThread.isStringYesValue("YES"));
        assertTrue(WordTransformSingleThread.isStringYesValue("YeS"));
        assertTrue(WordTransformSingleThread.isStringYesValue("Y"));
        assertTrue(WordTransformSingleThread.isStringYesValue("y"));
        assertTrue(WordTransformSingleThread.isStringYesValue("TRUE"));
        assertTrue(WordTransformSingleThread.isStringYesValue("true"));
        assertTrue(WordTransformSingleThread.isStringYesValue("TrUe"));
        assertFalse(WordTransformSingleThread.isStringYesValue("NO"));
        assertFalse(WordTransformSingleThread.isStringYesValue(""));
        assertFalse(WordTransformSingleThread.isStringYesValue("GIBBERISH"));
    }

    @Test
    public void testFindShortestTransform() {
        LinkedList<String> correctList = new LinkedList<String>();
        correctList.add("brain");
        correctList.add("braid");
        correctList.add("brand");
        correctList.add("brans");
        correctList.add("beans");
        correctList.add("bears");
        correctList.add("sears");
        correctList.add("stars");
        correctList.add("start");
        correctList.add("smart");
        assertTrue(WordTransformSingleThread.findShortestTransform("brain", "smart", true).equals(correctList));
        assertTrue(WordTransformSingleThread.findShortestTransform("brain", "smart", false).equals(correctList));
        assertTrue(WordTransformSingleThread.findShortestTransform("bkkak", "salkf", false) == null);

    }
}
