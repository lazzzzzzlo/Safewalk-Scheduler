public class Cipher {

    final String plaintextAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // Plaintext
                                                                   // Alphabet
    private String keyword; // The Secret Keyword
    private String ciphertextAlphabet; // Ciphertext Alphabet

    /**
     * Class constructor initializes the keyword and creates the Ciphertext
     * Alphabet
     * 
     * @param key
     *            the secret keyword used to create the ciphertext alphabet
     */
    public Cipher(String key) {
        this.keyword = key.toUpperCase();
        this.ciphertextAlphabet = initCiphertextAlphabet();
    }

    /**
     * removes all duplicate occurrences of characters from a String
     * 
     * @param s
     *            String with duplicate occurrences of characters
     * @return String with no duplicate characters in it
     */
    public static String removeDups(String s) {
        int i;
        String remaining = ""; // string which stores all unique characters of s
        for (i = 0; i < s.length(); i++) {
            if (remaining.indexOf(s.charAt(i)) < 0)
                remaining += s.charAt(i);
        }
        return remaining;
    }

    /**
     * generates the ciphertext alphabet from the keyword
     * 
     * @return String of ciphertext alphabet generated from the keyword
     */
    public String initCiphertextAlphabet() {
        String cAlpha = removeDups(this.keyword);

        for (int c = 0; c < plaintextAlphabet.length(); c++) {
            if (keyword.indexOf(plaintextAlphabet.charAt(c)) < 0) {
                cAlpha += plaintextAlphabet.charAt(c);
            }
        }

        return cAlpha;
    }

    /**
     * Encrypts a message in plaintext
     * 
     * @param message
     *            the message to be encrypted in ciphertext alphabet
     * @return the encrypted message in ciphertext alphabet
     */
    public String encrypt(String message) {
        String ctext = "";
        int index;
        message = message.toUpperCase();

        for (int c = 0; c < message.length(); c++) {
            index = plaintextAlphabet.indexOf(message.charAt(c));
            if (index >= 0) {
                ctext += ciphertextAlphabet.charAt(index);
            } else {
                ctext += message.charAt(c);
            }
        }
        return ctext;
    }

    /**
     * Encrypts a message in plaintext
     * 
     * @param ciphertext
     *            ciphertext in ciphertext alphabet
     * @return the decrypted message in plaintext alphabet
     */
    public String decrypt(String ciphertext) {
        String ptext = "";
        int index;
        ciphertext = ciphertext.toUpperCase();

        for (int c = 0; c < ciphertext.length(); c++) {
            index = ciphertextAlphabet.indexOf(ciphertext.charAt(c));
            if (index >= 0) {
                ptext += plaintextAlphabet.charAt(index);
            } else {
                ptext += ciphertext.charAt(c);
            }
        }

        return ptext;
    }
}
