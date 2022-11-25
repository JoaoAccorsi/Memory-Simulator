import java.util.Random;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m", ANSI_PURPLE = "\u001B[35m", ANSI_GREEN = "\u001B[32m";
    static Random gerador = new Random();
    static int numberOfTheProcess = 0;

    public static void main(String[] args){
        MMU mmu = new MMU(128, 8);
        Process[] generatedProcess = new Process[20];

        for (int i = 0; i < 20; i ++) {
                // Create the Process
            Process p1 = generateProcess();
            generatedProcess[i] = p1;
            generatePages(p1);

                // Allocation
            mmu.allocateVirtualMemory(p1);
            mmu.allocatePhysicalMemory(p1);
            mmu.toStringPhysicalMemory();

        }

            // Prints
        System.out.println(ANSI_GREEN + "\n\n\n\n+++-----------------------------------------------------------------------------------------------+++");
        System.out.println("                                       +---Summary---+            ");
        System.out.println("+++-----------------------------------------------------------------------------------------------+++" + ANSI_RESET);

        System.out.println(ANSI_PURPLE + "\n+++-------------------------+++");
        System.out.println("      Generated Processes            ");
        System.out.println("+++-------------------------+++" + ANSI_RESET);

        for (int i = 0; i < generatedProcess.length; i ++){
            if (generatedProcess != null)
                if (i < 10)
                    System.out.printf("(0%d) - %s\n", i, generatedProcess[i]);
                else
                    System.out.printf("(%d) - %s\n", i, generatedProcess[i]);
        }

        mmu.toStringPhysicalMemory();
        mmu.toStringVirtualMemory();
        mmu.toStringPageTable();

        System.out.println(ANSI_PURPLE + "\n+++-------------------------+++");
        System.out.println("   Number of Replaced Pages            ");
        System.out.println("+++-------------------------+++" + ANSI_RESET);
        System.out.printf("%d", mmu.getNumberOfPagesReplaced());

        System.out.println("\n\n");
    }

    //---------------------------------------------------
    // Generates the Process
    public static Process generateProcess(){
        Process p1 = new Process();

        p1.setPID(numberOfTheProcess++);
        p1.setProcessSize((gerador.nextInt(3) + 1) * 8); // from 8kB to 32kB
        p1.setNumberOfPages(p1.getProcessSize()/8);
        p1.setPagesQuantity(p1.getNumberOfPages());

        return p1;
    }

    //---------------------------------------------------
    // Generates the Process Pages
    public static void generatePages(Process p1){

        String alphabet[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "W", "Z", "Y", "Z"};
        Page createdPage;

        for (int i = 0; i < p1.getNumberOfPages(); i ++){
            createdPage = new Page((alphabet[gerador.nextInt(25)]), p1.getPID()); //25
            p1.setPages(createdPage, i);
        }
    }
}
