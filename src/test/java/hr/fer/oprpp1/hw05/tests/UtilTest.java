package hr.fer.oprpp1.hw05.tests;

import hr.fer.oprpp1.hw05.crypto.Util;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class UtilTest {

    @Test
    public void testLenRestriction(){
        assertThrows(NullPointerException.class, () -> Util.hextobyte(null));
        assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("01a"));
        int len = Util.hextobyte("").length;
        assertEquals(0, len);
    }

    @Test
    public void testHexToByte(){
        byte[] arr = new byte[]{1, -82, 34};
        byte[] arr2 = Util.hextobyte("01aE22");
        assertEquals(arr[0], arr2[0]);
        assertEquals(arr[1], arr2[1]);
        assertEquals(arr[2], arr2[2]);
    }

    @Test
    public void testByteToHex(){
        byte[] arr = new byte[]{1, -82, 34};
        assertEquals(Util.bytetohex(arr), "01ae22");
        assertThrows(NullPointerException.class, () -> Util.bytetohex(null));
    }
}
