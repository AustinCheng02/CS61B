import java.io.Reader;
import java.io.IOException;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author Daniel Kim
 */
public class TrReader extends Reader {
    private Reader _str;
    private String _from;
    private String _to;
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(0) to TO.charAt(0), etc., leaving other characters
     *  unchanged.  FROM and TO must have the same length. */
    public TrReader(Reader str, String from, String to) {
        _str = str;
        _from = from;
        _to = to;
        // FILL IN
    }

    /** reads the translated form of the current character from the string.
     */
    public int read() throws IOException {
        int character = _str.read();
        if (character == -1) {
            return -1;
        }

        int position = _from.indexOf((char) character);
        if (position == -1) {
            return character;
        }

        return _to.charAt(position);
    }

    /** returns -1 if no string is read. returns the number of characters read.
     */
    public int read(char[] cbuf, int off, int len) throws IOException {
        if (len == 0) {
            return -1;
        }

        int character = read();
        for (int i = 1; i < off; i++){
            character = read();
        }

        if (character == -1) {
            return character;
        }
        int num = 0;
        cbuf[off] = (char) character;
        for (int i = 0; i < len; i++) {
            if (character == -1) {
                return num;
            }
            cbuf[off + num] = (char) character;
            character = read();
            num+=1;
        }
        return num;
    }

    public void close() throws IOException {
        _str.close();
    }
    // FILL IN
    // NOTE: Until you fill in the right methods, the compiler will
    //       reject this file, saying that you must declare TrReader
    //     abstract.  Don't do that; define the right methods instead!
}


