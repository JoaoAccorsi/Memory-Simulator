public class PageTable {
    public Page page;
    public int positionVirtualMemory;
    public int positionPhysicalMemory;
    public boolean validateBit;         // true (it is in the physical memory), false (it is not)

    //---------------------------------------------------
    // Constructor
    public PageTable() {
        this.page = null;
        this.positionVirtualMemory = 0;
        this.positionPhysicalMemory = 0;
        this.validateBit = false;
    }

    //---------------------------------------------------
    // Getters and Setters

    public Page getPage() {
        return page;
    }
    public void setPage(Page page) {
        this.page = page;
    }
    public void setPositionVirtualMemory(int positionVirtualMemory) {
        this.positionVirtualMemory = positionVirtualMemory;
    }
    public void setPositionPhysicalMemory(int positionPhysicalMemory) {
        this.positionPhysicalMemory = positionPhysicalMemory;
    }
    public void setValidateBit(boolean validateBit) {
        this.validateBit = validateBit;
    }

    //---------------------------------------------------
    // toString

    @Override
    public String toString() {
        return "PageTable{" +
                "page=" + page +
                ", positionVirtualMemory=" + positionVirtualMemory +
                ", positionPhysicalMemory=" + positionPhysicalMemory +
                ", validateBit=" + validateBit +
                '}';
    }
}
