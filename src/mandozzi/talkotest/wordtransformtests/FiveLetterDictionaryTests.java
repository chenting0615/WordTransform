package mandozzi.talkotest.wordtransformtests;

import static org.junit.Assert.*;

import mandozzi.talkotest.wordtransform.FiveLetterDictionary;

import org.junit.Test;

public class FiveLetterDictionaryTests {

    @Test
    public void testInitDictionaryNoGraph() {
        FiveLetterDictionary testDict = new FiveLetterDictionary(false);
        assertEquals(testDict.getWordMap().keySet().size(), 5757);
        assertFalse(testDict.getHasGraph());
        assertFalse(testDict.getUseGraph());
    }

    @Test
    public void testConstructMapUseGraph() {
        FiveLetterDictionary testDict = new FiveLetterDictionary(true);
        assertEquals(testDict.getWordMap().keySet().size(), 5757);
        assertTrue(testDict.getHasGraph());
        assertTrue(testDict.getUseGraph());
        assertEquals(testDict.getWordMap().get("brain").size(), 6);
    }

    @Test
    public void testSetUseGraph() {
        FiveLetterDictionary testDict = new FiveLetterDictionary(false);
        assertFalse(testDict.getHasGraph());
        assertFalse(testDict.getUseGraph());
        testDict.setUseGraph(true);
        assertTrue(testDict.getHasGraph());
        assertTrue(testDict.getUseGraph());

        testDict = new FiveLetterDictionary(true);
        assertTrue(testDict.getHasGraph());
        assertTrue(testDict.getUseGraph());
        testDict.setUseGraph(false);
        assertTrue(testDict.getHasGraph());
        assertFalse(testDict.getUseGraph());
        
    }

    @Test
    public void testIsValidWord() {
        FiveLetterDictionary testDict = new FiveLetterDictionary(false);
        assertFalse(testDict.isValidWord("FAILX"));
        assertTrue(testDict.isValidWord("words"));
    }

    @Test
    public void testGetNeighbors() {
        FiveLetterDictionary testDict = new FiveLetterDictionary(false);
        assertEquals(testDict.getNeighbors("brain").size(), 6);

        testDict.setUseGraph(true);
        assertEquals(testDict.getNeighbors("brain").size(), 6);
    }
}
