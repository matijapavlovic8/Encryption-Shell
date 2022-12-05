package hr.fer.oprpp1.hw05.crypto;

import java.util.Objects;

public class Util {

    /**
     * Takes hex-encoded String and returns appropriate byte array.
     * @param keyText hex-encoded string.
     * @return {@code byte[]} array.
     */

    public static byte[] hextobyte(String keyText){
        Objects.requireNonNull(keyText, "String can't be null!");
        if(keyText.length() == 0) return new byte[0];
        if(keyText.length() % 2 != 0) throw new IllegalArgumentException("Input array must have an even length!");

        byte[] arr = new byte[keyText.length() / 2];

        for(int i = 0; i < arr.length; i++){
            int index = i * 2;
            try {
                arr[i] = (byte) Integer.parseInt(keyText.substring(index, index + 2), 16);
            } catch (NumberFormatException e){
                throw new NumberFormatException("Input string isn't a valid hex number!");
            }
        }

        return arr;
    }

    /**
     * Takes a byte array and returns appropriate hex-encoded string.
     * @param bytearray input {@code byte[]} array.
     * @return hex-encoded string.
     */

    public static String bytetohex(byte[] bytearray){
        Objects.requireNonNull(bytearray, "Can't pass null as byte array!");
        StringBuilder sb = new StringBuilder();
        for(byte b: bytearray){
            sb.append(String.format("%02X", b));
        }
        return sb.toString().toLowerCase();
    }
}
