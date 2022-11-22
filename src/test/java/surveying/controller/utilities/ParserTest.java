package surveying.controller.utilities;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    Parser parser = new Parser("src/main/resources/route1.txt");

    @Test
    void parseFile() throws IOException {
        List<EntryData> entryList = new ArrayList<>();
        File inputData = new File("src/main/resources/route1.txt");
        BufferedReader br = new BufferedReader(new FileReader(inputData));
        String st;
        while ((st = br.readLine()) != null) {
            String[] split = st.split(" ");
            int[] entryArray = new int[3];
            for (int i = 0; i < entryArray.length; i++) {
                entryArray[i] = Integer.parseInt(split[i]);
            }
            EntryData entryData = new EntryData(entryArray[0], entryArray[1], entryArray[2]);
            entryList.add(entryData);
        }

        assertEquals(entryList, parser.parseFile());
    }
}