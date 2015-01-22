package mandozzi.talkotest.wordtransformtests;

import static org.junit.Assert.*;

import java.util.LinkedList;

import mandozzi.talkotest.wordtransform.WordTransform;

import org.junit.Test;

public class WordTransformTests {

    @Test
    public void testIsStringYesValue() {
        assertTrue(WordTransform.isStringYesValue("yes"));
        assertTrue(WordTransform.isStringYesValue("YES"));
        assertTrue(WordTransform.isStringYesValue("YeS"));
        assertTrue(WordTransform.isStringYesValue("Y"));
        assertTrue(WordTransform.isStringYesValue("y"));
        assertTrue(WordTransform.isStringYesValue("TRUE"));
        assertTrue(WordTransform.isStringYesValue("true"));
        assertTrue(WordTransform.isStringYesValue("TrUe"));
        assertFalse(WordTransform.isStringYesValue("NO"));
        assertFalse(WordTransform.isStringYesValue(""));
        assertFalse(WordTransform.isStringYesValue("GIBBERISH"));
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
        assertTrue(WordTransform.findShortestTransform("brain", "smart", true).equals(correctList));
        assertTrue(WordTransform.findShortestTransform("brain", "smart", false).equals(correctList));
        assertTrue(WordTransform.findShortestTransform("bkkak", "salkf", false) == null);

    }
}
