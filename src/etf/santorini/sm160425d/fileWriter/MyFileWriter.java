package etf.santorini.sm160425d.fileWriter;

import etf.santorini.sm160425d.Logic.Board;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFileWriter {

    PrintWriter printWriter;

    public MyFileWriter(String filename){
        try (FileWriter fileWriter = new FileWriter(filename)) {
            printWriter = new PrintWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printToFile(int row, int col, boolean isBuild){
        try{
        if(row < 0 || row >= Board.rows || col < 0 || col >= Board.rows){
            throw new ErrorOutOfBounds(row, col);
        }
        char rowChar = 'F';
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

        printWriter.print(rowChar + col + " ");
        if(isBuild == true)
            printWriter.println();

        }catch(ErrorOutOfBounds e){
            System.err.println(e.toString());
        }
    }



}
