package test;


public class Main {
    static int TESTCOUNT = 49;
    static String VERSIONNAME = "# druk-0.2";
    static String DRUKFILENAME = "druk.jar"; //jar should be named accordingly

    public static void main(String[] args) {
        Test test;
        if(args.length == 0 || args.length == 1) {
            test = new Test(TESTCOUNT, VERSIONNAME, DRUKFILENAME, args);
            test.run();
        }
        else
            System.out.println("Invalid number of arguments");

    }
}