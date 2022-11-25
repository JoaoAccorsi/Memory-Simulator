import java.util.HashMap;
import java.util.Map;
public class PagesUsed {
    private static Map<String,Integer> pagesUsed = new HashMap<String,Integer>();

    //---------------------------------------------------
    // Constructor
    public PagesUsed() {
        pagesUsed.put( "A", 0);
        pagesUsed.put( "B", 0);
        pagesUsed.put( "C", 0);
        pagesUsed.put( "D", 0);
        pagesUsed.put( "E", 0);
        pagesUsed.put( "F", 0);
        pagesUsed.put( "G", 0);
        pagesUsed.put( "H", 0);
        pagesUsed.put( "I", 0);
        pagesUsed.put( "J", 0);
        pagesUsed.put( "K", 0);
        pagesUsed.put( "L", 0);
        pagesUsed.put( "M", 0);
        pagesUsed.put( "N", 0);
        pagesUsed.put( "O", 0);
        pagesUsed.put( "P", 0);
        pagesUsed.put( "Q", 0);
        pagesUsed.put( "R", 0);
        pagesUsed.put( "S", 0);
        pagesUsed.put( "T", 0);
        pagesUsed.put( "U", 0);
        pagesUsed.put( "V", 0);
        pagesUsed.put( "W", 0);
        pagesUsed.put( "X", 0);
        pagesUsed.put( "Y", 0);
        pagesUsed.put( "Z", 0);
    }

    //---------------------------------------------------
    // The page was used, increment 1 in the counter
    public void incrementPage(String pageID) {
        Integer number = pagesUsed.get(pageID);
        pagesUsed.replace(pageID, number + 1);
    }

    //---------------------------------------------------
    // How many times the page was used?
    public int numberOfTimesThePageWasUsed(String pageID) {
        return pagesUsed.get(pageID);
    }

    //---------------------------------------------------
    // toString
    public void toStringPagesUsed() {
        System.out.println(pagesUsed);
    }
}
