public class MMU {
    public static final String ANSI_RESET = "\u001B[0m", ANSI_PURPLE = "\u001B[35m", ANSI_GREEN = "\u001B[32m", ANSI_YELLOW = "\u001B[33m";
    public static Page[] virtualMemory, physicalMemory;
    public static PageTable[] realPageTable;
    public static PagesUsed pagesUsed;
    public static int eachPageIDUsed[];
    public static int numberOfPagesReplaced = 0;
    public static int numberOfThePagesPageTable = 0;

    //---------------------------------------------------
    // Constructor
    public MMU(int virtualMemoryLength, int physicalMemoryLength) {
        this.virtualMemory = new Page[virtualMemoryLength];
        this.physicalMemory = new Page[physicalMemoryLength];
        this.realPageTable = new PageTable[5000];
        this.pagesUsed = new PagesUsed();
        this.eachPageIDUsed = new int[25];
    }

    //---------------------------------------------------
    // Allocate the Process Pages in the Virtual Memory
    public static void allocateVirtualMemory(Process p1){

        PageTable aux;
        int counter = 0;

        for (int i = 0; i < p1.getNumberOfPages(); i++){

            for (int j = 0; j < virtualMemory.length; j++){
                if (virtualMemory[j] == null) {
                    virtualMemory[j] = p1.pages[i];

                    aux = new PageTable();
                    aux.setPage(p1.pages[i]);
                    aux.setPositionVirtualMemory(j);
                    aux.setPositionPhysicalMemory(-1);
                    aux.setValidateBit(false);

                    realPageTable[numberOfThePagesPageTable++] = aux;
                    counter++;
                    break;
                }

                if (counter == virtualMemory.length)
                    System.out.println("No more empty space in the Virtual Memory, cannot allocate more pages!!");
            }
        }
    }

    //---------------------------------------------------
    // Allocate the Process Pages in the Physical Memory
    public static void allocatePhysicalMemory(Process p1){

        int numberOfPagesFilled = 0;
        boolean pageAlreadyFilled = false, replaceThePage = true;

        for (int i = 0; i < p1.getNumberOfPages(); i++){

            for (int j = 0; j < physicalMemory.length; j++) {

                    // The Page is shared by more than one process and already got a space in the
                    // Physical Memory by the other process --> only update the page table of the actual "request"
                if (physicalMemory[j] != null){
                    if (p1.pages[i].getPageID().equals(physicalMemory[j].getPageID())) {
                        for (int x = 0; x < realPageTable.length; x++) {
                            if (realPageTable[x] != null) {
                                if (realPageTable[x].getPage() == p1.pages[i]) {
                                    realPageTable[x].setPositionPhysicalMemory(j);
                                    realPageTable[x].setValidateBit(true);
                                    replaceThePage = false;

                                        // Count the number of times the page was allocated in the Physical Memory
                                    pageCounter(p1.pages[i]);
                                }
                            }
                        }
                        break;
                    }
                }

                    // Is the page already in the Physical Memory by another process?
                for (int w = 0; w < physicalMemory.length; w++) {
                    if (physicalMemory[w] != null && physicalMemory[w].getPageID().equals(p1.pages[i].getPageID()))
                        replaceThePage = false;
                }

                    // There is an empty space in the Physical Memory
                    // Fill it with the new page
                if (physicalMemory[j] == null && replaceThePage) {

                    physicalMemory[j] = p1.pages[i];

                        // Update the Page Table
                    for (int x = 0; x < realPageTable.length; x++){
                            if (realPageTable[x] != null && realPageTable[x].getPage() == p1.pages[i]) {
                                realPageTable[x].setPositionPhysicalMemory(j);
                                realPageTable[x].setValidateBit(true);
                                pageAlreadyFilled = true;

                                    // Count the number of times the page was allocated in the Physical Memory
                                pageCounter(p1.pages[i]);
                            }
                    }
                    break;
                }

                    // Ensure that the page is not yet allocate in the Physical Memory by another process
                for (int w = 0; w < physicalMemory.length; w++) {
                    if (physicalMemory[w] != null)
                        numberOfPagesFilled++;
                }

                    // There is no more Empty Space in the Physical Memory
                    // Chose a page (frame) to take from the Physical Memory
                if (numberOfPagesFilled == 8 && pageAlreadyFilled == false)
                    pageReplacer();

                numberOfPagesFilled = 0;
                pageAlreadyFilled = false;

            }
            replaceThePage = true;
        }
    }

    //---------------------------------------------------
    // Physical Memory is full --> find the less used page (frame) and takes it from the Physical Memory
    public static void pageReplacer(){

        String pageID[] = new String[8], lessUsedPage = "";
        int pageIDCounter = 0, timePageWasUsed = 1000, positionReplacement = 0;

        toStringPhysicalMemory();

            // PageID of the pages (frames) that are in the Physical Memory now
        for (int i = 0; i < physicalMemory.length; i++) {
            if (physicalMemory[i] != null) {
                pageID[pageIDCounter++] = physicalMemory[i].getPageID();
            }
        }

            // Find the less used page (frame)
        for (int i = 0; i < pageID.length; i++) {
            if (pageID[i] != null) {
                if (pagesUsed.numberOfTimesThePageWasUsed(pageID[i]) < timePageWasUsed) {
                    timePageWasUsed = pagesUsed.numberOfTimesThePageWasUsed(pageID[i]);
                    lessUsedPage = pageID[i];
                }
            }
        }

                // Takes the less used page (frame) from the Physical Memory
        for (int i = 0; i < physicalMemory.length; i++) {
            if (physicalMemory[i] != null && physicalMemory[i].getPageID().equals(lessUsedPage)) {
                physicalMemory[i] = null;

                    // Update the Page Table
                for (int j = 0; j < realPageTable.length; j++) {
                    if (realPageTable[j] != null && realPageTable[j].getPage().getPageID().equals(lessUsedPage)) {
                        realPageTable[j].setPositionPhysicalMemory(-1);
                        realPageTable[j].setValidateBit(false);
                    }
                }
                positionReplacement = i;
            }
        }
        System.out.printf( ANSI_YELLOW + "\nPage %s, which was in the Physical Memory position %d, was replaced!" + ANSI_RESET, lessUsedPage, positionReplacement);
        numberOfPagesReplaced++;
    }

    //---------------------------------------------------
    // How many pages were replaced?
    public int getNumberOfPagesReplaced() {
        return numberOfPagesReplaced;
    }

    //---------------------------------------------------
    // Page Counter --> increment each time a page is used by a process
    public static void pageCounter(Page page) {
        pagesUsed.incrementPage(page.getPageID());
    }

    //---------------------------------------------------
    // Print the Physical Memory in the screen
    public static void toStringPhysicalMemory() {

        System.out.println(ANSI_PURPLE + "\n\n+++-------------------------+++");
        System.out.println("        Physical Memory              ");
        System.out.println("+++-------------------------+++" + ANSI_RESET);

        for (int i = 0; i < physicalMemory.length; i++) {
            System.out.printf("(0%d) - %s" + ANSI_GREEN + "\t|\t" + ANSI_RESET , i, physicalMemory[i]);

            if (((i + 1) % 4 == 0) & (i != 0))
                System.out.println();
        }
    }

    //---------------------------------------------------
    // Print the Virtual Memory in the screen
    public void toStringVirtualMemory() {

        System.out.println(ANSI_PURPLE + "\n\n+++-------------------------+++");
        System.out.println("        Virtual Memory              ");
        System.out.println("+++-------------------------+++" + ANSI_RESET);

        for (int i = 0; i < virtualMemory.length; i++) {
            if (virtualMemory[i] != null) {
                if (i < 10)
                    System.out.printf("(0%d) - %s" + ANSI_GREEN + "\t|\t" + ANSI_RESET, i, virtualMemory[i]);
                else
                    System.out.printf("(%d) - %s" + ANSI_GREEN + "\t|\t" + ANSI_RESET, i, virtualMemory[i]);

                if (((i + 1) % 4 == 0) & (i != 0))
                    System.out.println();
            }
        }
    }

    //---------------------------------------------------
    // Print the Page Table in the screen
    public static void toStringPageTable() {

        System.out.println(ANSI_PURPLE + "\n\n+++-------------------------+++");
        System.out.println("         Page Table            ");
        System.out.println("+++-------------------------+++" + ANSI_RESET);

        for (int i = 0; i < realPageTable.length; i++) {
            if (realPageTable[i] != null)
                if (i < 10)
                    System.out.printf("(0%d) - %s\n", i, realPageTable[i]);
                else
                    System.out.printf("(%d) - %s\n", i, realPageTable[i]);
        }
    }
}
