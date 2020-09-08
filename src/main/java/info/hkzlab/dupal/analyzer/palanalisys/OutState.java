package info.hkzlab.dupal.analyzer.palanalisys;

public class OutState {
    public final static int IDX_O = 0;
    public final static int IDX_IO = 1;
    public final static int IDX_HIZ = 2;

    private final int[] status;
    private final OutLink[] links;

    public OutState(int o_state, int io_state, int hiz_state, int totLinks) {
        status = new int[]{o_state, io_state, hiz_state};
        links = new OutLink[totLinks];
    }

    public int[] getStatus() {
        return status.clone();
    }

    public OutLink getOutLinkAtIdx(int idx) {
        return links[idx];
    }

    public boolean setOutLinkAtIdx(OutLink link, int idx) {
        if(links[idx] != null) return false;
        links[idx] = link;

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;

        for(int s : status) hash = hash*31 + s;

        return hash;
    }

    @Override
    public String toString() {
        return "OS["+String.format("%08X", status[0])+"|"+String.format("%08X", status[1])+"|"+String.format("%08X", status[2])+"]";
    }

}
