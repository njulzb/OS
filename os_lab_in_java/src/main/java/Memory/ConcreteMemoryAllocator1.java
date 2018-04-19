package Memory;

/**
 * 这是课堂上骆斌出的题目
 * 尝试实现用各种分配算法实现可变分区的内存分配算法
 * 1. 最先适应 first fit
 * 2. 邻近适应 nearest fit
 * 3. 最优适应 best fit
 * 4. 最坏适应 worst fit
 * @author lzb
 * @date 2018/4/18 17:32
 */
public class ConcreteMemoryAllocator1 {

    private MemoryNode usedMemoryList;
    private MemoryNode freeMemoryList;
    private int totalSize;
    private int freeSize;

    public ConcreteMemoryAllocator1(int size) {
        this.totalSize = size;
        this.freeSize = this.totalSize;

        this.usedMemoryList = null;
        this.freeMemoryList = new MemoryNode(0,this.totalSize,null);



    }


    /**
     *
     * @param processId
     * @param requestSize
     * @return begin address 申请到的内存起始地址，长度为传入的长度，-1表示无法分配内存，需要等待
     */
    public int requestMemoryByFirstFit(String processId , int requestSize){
        if (requestSize > this.totalSize){
            return -1;
        }

        /**
         * check the free size
         */
        if (requestSize > this.freeSize){
            return  -1;//暂时无足够空闲区可用，使进程等待
        }

        /**
         * search free list , find the first fit area to carry the process
         */
        MemoryNode freeNode = this.freeMemoryList;
        MemoryNode beforeFreeNode = null;
        while (freeNode != null){
            if (freeNode.getLength() >= requestSize){
                //length enough
                break;
            }
            beforeFreeNode = freeNode;
            freeNode = freeNode.getNext();
        }

        /**
         * if there is no enough area
         */
        if (freeNode == null){
            //adjust the memory and request again
            this.adjustMemory();
            return this.requestMemoryByFirstFit(processId,requestSize);
        }


        /**
         * update the used list
         */
        MemoryNode newInsert = new MemoryNode(freeNode.getBeginAddress(),requestSize,processId);

        if (this.usedMemoryList == null){
            this.usedMemoryList = newInsert;
        }
        else {
            //insert the new node
            if (this.usedMemoryList.getBeginAddress() > newInsert.getBeginAddress()){
                newInsert.setNext(this.usedMemoryList);
                this.usedMemoryList = newInsert;
            }
            else {
                MemoryNode node = this.usedMemoryList;
                MemoryNode before = null;
                while (node != null && node.getBeginAddress() < newInsert.getBeginAddress()){
                    before = node;
                    node = node.getNext();
                }


                if (before == null){
                    before = this.usedMemoryList;
                }
                newInsert.setNext(before.getNext());
                before.setNext(newInsert);


            }


        }



        /**
         * update the freelist
         */

        if (freeNode.getLength() == requestSize){
            //delete this node
            if (beforeFreeNode == null){
                this.freeMemoryList = null;
            }
            else {
                beforeFreeNode.setNext(freeNode.getNext());
            }

        }
        else {
            //update the node
            freeNode.setBeginAddress(freeNode.getBeginAddress() + requestSize);
            freeNode.setLength(freeNode.getLength() - requestSize);
        }


        this.freeSize -= newInsert.getLength();


        return newInsert.getBeginAddress();
    }

    /**
     * 调整内存中分区的位置，使空闲区完整
     * @return
     */
    public boolean adjustMemory(){

        if (this.freeMemoryList == null){
            return false;
        }

        MemoryNode before = this.freeMemoryList,after = before.getNext();

        while (after != null){
            if (before.getBeginAddress() + before.getLength() == after.getBeginAddress()){
                //merge
                before.setLength(before.getLength() + after.getLength());
                before.setNext(after.getNext());
                continue;
            }
            before = after;
            after = after.getNext();
        }

        /**
         * 首部归还
         */
        MemoryNode head = this.usedMemoryList;
        if ( head.getBeginAddress()!=0){
            //update free list
            //update used list
            int temp = head.getBeginAddress();
            head.setBeginAddress(0);
            this.freeMemoryList.setBeginAddress(head.getLength());
            this.adjustMemory();
        }


        /**
         * 遍历used list,将不连续的合并成连续的
         */
        MemoryNode left = this.usedMemoryList;
        MemoryNode right = left.getNext();
        while (right != null){
            if (left.getBeginAddress() + left.getLength() < right.getBeginAddress()){
                //a gap here
                right.setBeginAddress(left.getBeginAddress() + left.getLength());
                //update free list
                MemoryNode node = this.freeMemoryList;
                while (node != null && node.getBeginAddress() != right.getBeginAddress()){
                    node = node.getNext();
                }

                if (node == null) {
                    return false;//something wrong
                }

                node.setBeginAddress(right.getBeginAddress() + right.getLength());

                this.adjustMemory();
            }

            left = right;
            right = right.getNext();

        }


        return true;
    }


    public boolean returnMemory(String processId){

        /**
         * search the used node by process id
         */
        MemoryNode usedNode = this.usedMemoryList;
        MemoryNode before = null;
        while (usedNode != null && !usedNode.getProcessId().equals(processId)) {
            before = usedNode;
            usedNode = usedNode.getNext();
        }


        /**
         * update the used list
         */
        if (usedNode == null){
            return false;//there is no area with the process
        }
        //delete the node
        if (before == null){
            this.usedMemoryList = usedNode.getNext();
        }
        else {
            before.setNext(usedNode.getNext());
        }



        /**
         * update the free list
         */

        MemoryNode newFree = new MemoryNode(usedNode.getBeginAddress(),usedNode.getLength(),null);
        if (this.freeMemoryList == null){
            this.freeMemoryList = newFree;
            this.freeSize += newFree.getLength();
            return true;
        }
        if (this.freeMemoryList.getBeginAddress() > usedNode.getBeginAddress()){
            newFree.setNext(this.freeMemoryList);
            this.freeMemoryList = newFree;
            this.freeSize += newFree.getLength();
            return true;
        }

        MemoryNode freeNode = this.freeMemoryList;
        while (freeNode != null){
            if (freeNode.getBeginAddress() <= usedNode.getBeginAddress()){
                if ( !freeNode.hasNext() ||
                        freeNode.getNext().getBeginAddress() > usedNode.getBeginAddress()){
                    break;
                }
            }


            freeNode = freeNode.getNext();

        }

        //return the memory


        MemoryNode newFreeNode = new MemoryNode(usedNode.getBeginAddress(),usedNode.getLength(),null);

        if (freeNode == null){
            this.freeMemoryList =   newFreeNode;
        }
        else {
            newFreeNode.setNext(freeNode.getNext());
            freeNode.setNext(newFreeNode);
        }


        this.freeSize += newFreeNode.getLength();

        return true;
    }

}
