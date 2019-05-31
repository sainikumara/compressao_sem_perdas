package ui;

import compressao.CompressionInfo;
import compressao.IO;
import compressao.LZ77Compress;
import compressao.LZ77Decompress;
import compressao.LZ77DictionaryEntry;
import java.util.ArrayList;
import java.util.List;

public class LZ77 {

    public static void main(String[] args) {
        IO io = new IO();
        LZ77Compress compressor = new LZ77Compress();
        LZ77Decompress decompressor = new LZ77Decompress();

        String stringToCompress = io.stringFromFile("/resources/text");

        //String stringToCompress = "The Lempel-Ziv algorithm is a variable-to-fixed length code. Basically, there are two versions of the algorithm presented in the literature: the theoretical version and the practical version. Theoretically, both versions perform essentially the same. However, the proof of the asymptotic optimality of the theoretical version is easier. In practice, the practical version is easier to implement and is slightly more efficient. We explain the practical version of the algorithm as explained in the book by Gersho and Gray and in the paper by Welch. The basic idea is to parse the input sequence into non-overlapping blocks of different lengths while constructing a dictionary of blocks seen thus far. Theoretically, the size of the dictionary can grow infinitely large. In practice, the dictionary size is limited. Once the limit is reached, no more entries are added. Welch had recommended a dictionary of size 4096. This corresponds to 12 bits per index. The length of the index may vary. For example, in the n-th block, the current dictionary size is n+1 (assuming that the limit has not been reached). Therefore, we can encode the index of block n using [log2(n+1)] bits (rounded up to the nearest integer). For example, the first index can be represented by 1 bit, the 2nd and 3rd by 2 bits each, the 4th, 5th, 6th, and 7th by 3 bits each, and so on. This is the variable-to-variable length version of the Lempel-Ziv algorithm. For a maximum dictionary size of 2m, variable-length encoding of the indices saves exactly 2m-1 bits compared to fixed-length encoding. The above example, as most other illustrative examples in the literature, does not result in real compression. Actually, more bits are used to represent the indices than the original data. This is because the length of the input data in the example is too short. In practice, the Lempel-Ziv algorithm works well (lead to actual compression) only when the input data is sufficiently large and there are sufficient redundancy in the data. Decompression works in the reverse fashion. The decoder knows that the last symbol of the most recent dictionary entry is the first symbol of the next parse block. This knowledge is used to resolve possible conflict in the decoder. Many popular programs (e.g. Unix compressString and uncompress, gzip and gunzip, and Windows WinZip) are based on the Lempel-Ziv algorithm. The name Ziv-Lempel is more appropriate than Lempel-Ziv (see Gersho and Gray and the name ordering in References 3 and 4 below).";
        //String stringToCompress = "Opiskelija ratkaisee ohjaajan kanssa sovitun ohjelmointiongelman ja toteuttaa itse ratkaisualgoritmin ja sen aputietorakenteet Java-kielellä. Opiskelija testaa ja dokumentoi ratkaisunsa. Ohjaajan kanssa sovitun tehtävänannon mukaisesti työhön kuuluu lisäksi esim. itsenäistä kirjallisuuteen perustuvaa selvitystä tai algoritmien kokeellista vertailua. Oppimateriaali Harjoitustyön apuna käytetään valittuun aiheeseen liittyvää algoritmialan kirjallisuutta ja tarpeen mukaan ohjelmointikielen dokumentaatiota jne. Oppimista tukevat aktiviteetit ja opetusmenetelmät Opiskelija raportoi viikoittain työn edistymisestä ohjaajalle ja saa siitä palautetta. Ohjaaja antaa tarpeen mukaan neuvoja henkilökohtaisesti tai verkon välityksellä. Opiskelija antaa ja saa myös vertaispalautetta.";
        int searchWindowLength = 127;
        int lookAheadWindowLength = 7;

        System.out.println("Original string: " + stringToCompress);

        ArrayList<LZ77DictionaryEntry> dictionary = compressor.compressString(stringToCompress, searchWindowLength, lookAheadWindowLength);
        new CompressionInfo().LZ77Info(stringToCompress, dictionary, searchWindowLength, lookAheadWindowLength);
                
        io.writeStringBasedDictionaryToFile("/resources/text-compressed", dictionary);
        List<LZ77DictionaryEntry> reconstructedDictionary = io.compressedStringBasedDataFromFile("/resources/text-compressed");

        String decompressedString = decompressor.decompressString(reconstructedDictionary);
        System.out.println("Decompressed string: " + decompressedString);
        
        byte[] bytesToCompress = io.readBytesFromFile("/resources/text");

        byte[] byteBasedDictionary = compressor.compressBytes(bytesToCompress, searchWindowLength, lookAheadWindowLength);
        io.writeBytesToFile("/resources/text-compressedBytes", byteBasedDictionary);
        
        byte[] bytesFromFile = io.readBytesFromFile("/resources/text-compressedBytes");
        byte[] decompressedBytes = decompressor.decompressBytes(bytesFromFile);
        
        io.writeBytesToFile("/resources/text-decompressedBytes", decompressedBytes);
        
    }

}