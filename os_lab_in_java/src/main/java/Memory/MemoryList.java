package Memory;

/**
 * 考虑是否将内存分配表封装起来，但是操作的接口还没有考虑清楚，已分配的和未分配的操作接口也很不相同
 * @author lzb
 * @date 2018/4/18 22:11
 */
public class MemoryList {

    private MemoryNode head;
    private int size;

    public MemoryList(MemoryNode head) {
        this.head = head;
    }

    public MemoryList() {
    }

    public boolean add(MemoryNode newNode){

        if (this.head == null){
            this.head = newNode;
            this.size += newNode.getLength();
            return true;
        }

        if (this.head.getBeginAddress() > newNode.getBeginAddress()){
            newNode.setNext(this.head);
            this.head = newNode;
            this.size += newNode.getLength();
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
        this.size += newNode.getLength();

        return true;
    }

    public MemoryNode findFirstFit(int size){
        MemoryNode node = this.head;

        while (node != null && node.getLength() < size){
            node = node.getNext();
        }
        return node;
    }

    public boolean remove(MemoryNode dead){

        if (this.head == null) {
            return false;
        }
        if (this.head.equals(dead)){
            this.head = this.head.getNext();
        }

        MemoryNode node = this.head;
        MemoryNode before = null;
        while (node != null && !node.equals(dead)){
            before = node;
            node = node.getNext();
        }


        if (node == null) {
            return false;
        }


        before.setNext(node.getNext());
        this.size -= dead.getLength();

        return true;
    }

    public boolean mergeNeighbour(){

        if (this.head == null) {
            return false;
        }

        MemoryNode before = this.head;
        MemoryNode after = before.getNext();

        while (after != null){

            if (before.getBeginAddress() + before.getLength() == after.getBeginAddress()){
                before.setLength(before.getLength() + after.getLength());
                before.setNext(after.getNext());

                continue;
            }

            before = after;
            after = after.getNext();
        }

        return true;

    }

    public MemoryNode searchNodeByProcessId(String processId){
        MemoryNode node = this.head;
        while (node != null && !node.getProcessId().equals(processId)){
            node = node.getNext();
        }
        return node;
    }

    public MemoryNode searchNodeByBeginAddress(int begin){
        if (this.head == null) {
            return null;
        }
        MemoryNode node = this.head;
        while (node != null && node.getBeginAddress() != begin){
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

    public int getSize() {
        return size;
    }
}
