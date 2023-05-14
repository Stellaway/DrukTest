package test;


public class Main {
    static int TESTCOUNT = 49;
    static String VERSIONNAME = "druk-0.2";

    public static void main(String[] args) {
        Test test;
        for(String arg : args)
            System.out.println(arg);
        if(args.length == 0 || args.length == 1) {
            test = new Test(TESTCOUNT, VERSIONNAME, args);
            test.run();
        }
        else
            System.out.println("Invalid number of arguments");

    }
}