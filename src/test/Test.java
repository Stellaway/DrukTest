package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Test {

    //the number of all the available tests
    int testCount;
    //the number of the specific test to run, 0 to run all
    int toTest;
    //the name of the version to test
    String versionName;

    //gets the of all the available tests, and the arguments given to the testprogram
    public Test(int testCount, String versionName, String[] testArgs){
        this.testCount = testCount;
        this.versionName = versionName;
        if(testArgs.length == 0)
            this.toTest = 0;
        else
            try {
                this.toTest = Integer.parseInt(testArgs[0]);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Not a test number");
            }
    }

    public void run() {
        if (toTest < 0 || toTest > testCount) {
            System.out.println("Invalid test number");
            return;
        }

        //run all
        if(toTest == 0)
            for(int i = 1; i <= testCount; i++)
                runDruk(i);

        //run one
        else runDruk(toTest);


        //compare test outputs to the expected
        try {
            compareExpected();
        } catch (FileNotFoundException e) {
            System.out.println("Error: test file(s) missing!");
        } catch (IOException e) {
            System.out.println("Error: could not create result file!");
        }

    }

    //compares testfiles, and prints the result into TEST_RESULT_<DATE>.txt
    private void compareExpected() throws FileNotFoundException, IOException {
        //compare all outputs

        File result = new File("results/TEST_RESULT_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")) + ".txt");
        FileWriter fw = new FileWriter(result, true);

        fw.write("TEST RESULTS @ " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")) + "\n" +
                "Tested version: " + this.versionName + "\n\n");

        if(this.toTest == 0){
            int passedTests = 0;
            for(int i = 1; i <= this.testCount; i++){
                try {
                    fw.write("Test " + i + ": ");

                    Scanner outScan = new Scanner(new File("out/" + String.format("%02d", i) + "_out.txt"));
                    Scanner expScan = new Scanner(new File("exp/" + String.format("%02d", i) + "_exp.txt"));


                    String testResult = compareFiles(outScan, expScan);
                    if (testResult.equals("ok")) {
                        passedTests++;
                        fw.write("passed!\n");
                    } else {
                        fw.write("failed! Details below:\n\t");
                        fw.write(testResult);
                        fw.write("\n");
                    }
                }catch (IOException e) {
                    fw.write("failed! Details below:\n\t");
                    fw.write("Test file(s) missing!\n");
                }

            }

            fw.write("\nAll tests run. Passed " + passedTests + " out of " + this.testCount + " tests.\n");
            if(passedTests == this.testCount)
                fw.write("Not too shabby!\n");
            if(passedTests <= 5)
                fw.write("get better...\n");

        }
        //compare the one output
        else{
            fw.write("Running testcase " + this.toTest+ ":\n");
            Scanner outScan = new Scanner(new File("out/"+  String.format("%02d", this.toTest) + "_out.txt"));
            Scanner expScan = new Scanner(new File("exp/"+   String.format("%02d", this.toTest) + "_exp.txt"));
            String testResult = compareFiles(outScan, expScan);
            if(testResult.equals("ok"))
                fw.write("Test passed!\n");
            else{
                fw.write("Test failed! Details below:\n\t");
                fw.write(testResult);
                fw.write("\n");
            }
        }
        fw.close();
    }

    //returns the test result as a string
    private String compareFiles(Scanner outScan, Scanner expScan){

        String version = expScan.nextLine();
        if (!version.equals(this.versionName)){
            return "Invalid version";
        }

        int line = 1;
        if(!outScan.hasNextLine() || !expScan.hasNextLine())
            return "Test file(s) incomplete!";

        while(outScan.hasNextLine() && expScan.hasNextLine()){
            String outLine = outScan.nextLine();
            String expLine = expScan.nextLine();

            if(!outLine.equals(expLine))
                return "Mismatch in line " + line + ":\n\t" +
                        "Expected: "+ expLine + "\n\t" +
                        "Actual: "+ outLine;

            line++;
        }

        return "ok";
    }


    //used to run the drukmakor program with cmd, feeding it the in.txt file and saving the output to compare later
    //input from "inp" folder, output will be saved to "out" folder
    private void runDruk(int testN){
        try {
            String testInput = "inp/" + String.format("%02d", testN) + "_inp.txt";
            String testOutput = "out/"+String.format("%02d", testN) + "_out.txt";

            Runtime rt = Runtime.getRuntime();
            rt.exec(new String[]{"cmd.exe", "/c", "java", "-jar", "drukmakor-0.2.jar", "<", testInput, ">", testOutput});

        } catch (Exception e) {
            System.out.println("Error in opening to test command.");
        }
    }


}
