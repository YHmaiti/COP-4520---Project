package TestCases;

import java.util.*;
import java.io.*;
import java.net.URL;

public class MakeTestCases{
    public static void main(String[] args) throws Exception{
        makeTestFile("SmallTC1.txt", (int) Math.pow(10, 4));
        makeTestFile("SmallTC2.txt", (int) Math.pow(10, 4) * 5);
        makeTestFile("MediumTC1.txt", (int) Math.pow(10, 5));
        makeTestFile("MediumTC2.txt", (int) Math.pow(10, 5) * 5);
        makeTestFile("LargeTC1.txt", (int) Math.pow(10, 6));
        makeTestFile("LargeTC2.txt", (int) Math.pow(10, 6) * 5);
    }

    private static void makeTestFile(String name, int size){
        try{
            Scanner reader = new Scanner(new File("../TestCaseMaker/src/input.txt"));
            FileWriter writer = new FileWriter(name);

            for(int i = 0; i < size; i++)
                writer.write(Integer.parseInt(reader.nextLine()) + "\n");

            writer.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }        
    }
}
