/**
 * 考虑是否将内存分配表封装起来，但是操作的接口还没有考虑清楚，已分配的和未分配的操作接口也很不相同
 * @author lzb
 * @date 2018/4/18 22:11
 */
public class MemoryList {

    private MemoryNode head;

    public MemoryList(MemoryNode head) {
        this.head = head;
    }

    public MemoryList() {
    }

    public boolean add(MemoryNode newNode){

        if (this.head == null){
            this.head = newNode;
            return true;
        }

        MemoryNode node = this.head;
        while (node != null) {
            if (node.getBeginAddress() <= newNode.getBeginAddress()){
                if ( !node.hasNext() ||
                        node.getNext().getBeginAddress() > newNode.getBeginAddress()){
                    break;
                }
            }

            node = node.getNext();

        }

        newNode.setNext(node.getNext());
        node.setNext(newNode);

        return true;
    }

    public MemoryNode findFirstFit(int size){
        MemoryNode node = this.head;

        while (node != null && node.getLength() < size){
            node = node.getNext();
        }
        return node;
    }

    public MemoryNode getHead() {
        return head;
    }

    public void setHead(MemoryNode head) {
        this.head = head;
    }
}
