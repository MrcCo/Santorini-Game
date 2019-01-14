package etf.santorini.sm160425d.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MyFileReader {

    private static MyFileReader instance;

    public static MyFileReader getInstance() {
        if(instance == null)
            instance = new MyFileReader();
        return instance;
    }

    public String readFile(String filename) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return  sb.toString();
        }
    }

}
