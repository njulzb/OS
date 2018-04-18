import javafx.scene.input.Mnemonic;

/**
 * @author lzb
 * @date 2018/4/18 19:29
 */
public class MemoryNode {

    private int beginAddress;
    private int length;
    private String processId;
    private MemoryNode next;

    public MemoryNode(int beginAddress, int length, String processId) {
        this.beginAddress = beginAddress;
        this.length = length;
        this.processId = processId;
    }

    public MemoryNode getLastNode(){
        MemoryNode node = this;
        while (node.getNext() != null){
            node = node.getNext();
        }
        return node;
    }

    public MemoryNode getNext() {
        return next;
    }

    public void setNext(MemoryNode next) {
        this.next = next;
    }

    public boolean hasNext(){
        return this.next!=null;
    }

    public int getBeginAddress() {
        return beginAddress;
    }

    public void setBeginAddress(int beginAddress) {
        this.beginAddress = beginAddress;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }
}
