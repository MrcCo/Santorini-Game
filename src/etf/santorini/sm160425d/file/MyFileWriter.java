package etf.santorini.sm160425d.file;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFileWriter {

    private static MyFileWriter instance;

    PrintWriter printWriter;
    private String filename;
    public MyFileWriter(String filename) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            printWriter = new PrintWriter(fileWriter);
            this.filename = filename;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MyFileWriter getInstance() {
        if (instance == null)
            instance = new MyFileWriter("Nebitno.txt");

        return instance;
    }

    public void printToFile(int row, int col) {
        char rowChar = 'f';
        if(row == 0)
            rowChar = 'A';
        if(row == 1)
            rowChar = 'B';
        if(row == 2)
            rowChar = 'C';
        if(row == 3)
            rowChar = 'D';
        if(row == 4)
            rowChar = 'E';
        instance.printWriter.print(rowChar + "" + col + " ");
        System.out.print(rowChar + "" + col + " ");
    }
    public void printNLToFile(){
        instance.printWriter.println();
        System.out.println();
    }

    public void save(){
        instance.printWriter.close();
    }
    public void startAgain(String name) throws FileNotFoundException {
        instance.printWriter = new PrintWriter(name);
    }
}
