package surveying.controller.utilities;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    /* default */ private File inputData;

    public Parser(final String path) {
        this.inputData = new File(path);
    }

    public List<EntryData> parseFile() throws IOException {
        final BufferedReader br = new BufferedReader(new FileReader(inputData));
        String st;
        final List<EntryData> entryList = new ArrayList<>();

        while ((st = br.readLine()) != null) {
            final String[] split = st.split(" ");
            int[] entryArray = new int[3];
            for (int i = 0; i < entryArray.length; i++) {
                entryArray[i] = Integer.parseInt(split[i]);
            }
            final EntryData entryData = new EntryData(entryArray[0], entryArray[1], entryArray[2]);
            entryList.add(entryData);
        }
        return entryList;
    }
}
