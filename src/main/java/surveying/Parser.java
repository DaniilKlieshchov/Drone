package surveying;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    File inputData;

    public Parser(String path) {
        this.inputData = new File(path);
    }

    public List<EntryData> parseFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(inputData));
        String st;
        List<EntryData> entryList = new ArrayList<>();
        while ((st = br.readLine()) != null) {
            String[] split = st.split(" ");
            int[] entryArray = new int[3];
            for (int i = 0; i < entryArray.length; i++) {
                entryArray[i] = Integer.parseInt(split[i]);
            }
            EntryData entryData = new EntryData(entryArray[0], entryArray[1], entryArray[2]);
            entryList.add(entryData);
        }
        return entryList;
    }
}
