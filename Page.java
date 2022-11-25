public class Page {
    public String pageID;
    public int processPID;

    //---------------------------------------------------
    // Constructor
    public Page(String pageID, int processPID) {
        this.pageID = pageID;
        this.processPID = processPID;
    }

    //---------------------------------------------------
    // Getter and toString

    public String getPageID() {
        return pageID;
    }

    @Override
    public String toString() {
        return "{" +
                "pageID='" + pageID + '\'' +
                ", processPID=" + processPID +
                '}';
    }
}
