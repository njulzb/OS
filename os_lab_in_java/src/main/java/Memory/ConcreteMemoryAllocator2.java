package Memory;

/**
 * @author lzb
 * @date 2018/4/19 14:52
 */
public class ConcreteMemoryAllocator2 implements MemoryAllocator{

    private MemoryList usedList;
    private MemoryList freeList;

    int totalSize;

    public ConcreteMemoryAllocator2(int totalSize) {
        this.totalSize = totalSize;

        this.usedList = new MemoryList();
        this.freeList = new MemoryList();

        this.freeList.add(new MemoryNode(0,this.totalSize,null));
    }


    @Override
    public int requestMemoryByFirstFit(String processId, int requestSize) {
        if (requestSize > this.totalSize){
            return -1;
        }

        if (requestSize > this.freeList.getSize()){
            return -2; // this process needs to wait
        }

        /**
         * find out the place to insert new node
         */
        MemoryNode firstFit = this.freeList.findFirstFit(requestSize);

        if (firstFit == null){
            this.adjustMemory();
            return this.requestMemoryByFirstFit(processId,requestSize);
        }


        MemoryNode newUsedNode = new MemoryNode(firstFit.getBeginAddress(),requestSize,processId);
        this.usedList.add(newUsedNode);



        if (firstFit.getLength() == requestSize){
            //delete the node
            this.freeList.remove(firstFit);
        }
        else {
            //update the node
            firstFit.setBeginAddress(firstFit.getBeginAddress() + requestSize);
            firstFit.setLength(firstFit.getLength() - requestSize);
        }


        return newUsedNode.getBeginAddress();

    }

    @Override
    public boolean returnMemory(String processId) {

        MemoryNode usedNode = this.usedList.searchNodeByProcessId(processId);
        if (usedNode == null) {
            return false;//not found
        }

        this.usedList.remove(usedNode);

        MemoryNode newFreeNode = new MemoryNode(usedNode.getBeginAddress(),usedNode.getLength(),null);
        this.freeList.add(newFreeNode);
        return true;

    }

    public boolean adjustMemory(){

        if (this.usedList == null || this.freeList == null){
            return false;
        }

        boolean mergeResult = this.freeList.mergeNeighbour();


        if (this.usedList.getHead().getBeginAddress() != 0){
            MemoryNode usedHead = this.usedList.getHead();
            this.usedList.remove(usedHead);
            usedHead.setBeginAddress(0);
            this.usedList.add(usedHead);

            MemoryNode freeHead = this.freeList.getHead();
            this.freeList.remove(freeHead);
            freeHead.setBeginAddress(usedHead.getLength());
            this.freeList.add(freeHead);
            this.freeList.mergeNeighbour();
        }

        MemoryNode usedBefore = this.usedList.getHead();
        MemoryNode usedAfter = usedBefore.getNext();
        while (usedAfter != null){

            if (usedBefore.getBeginAddress() + usedBefore.getLength() != usedAfter.getBeginAddress()){
                this.usedList.remove(usedAfter);
                usedAfter.setBeginAddress(usedBefore.getBeginAddress() + usedBefore.getLength());
                this.usedList.add(usedAfter);

                MemoryNode freeNode = this.freeList.searchNodeByBeginAddress(usedAfter.getBeginAddress());
                this.freeList.remove(freeNode);
                freeNode.setBeginAddress(usedAfter.getBeginAddress() + usedAfter.getLength());
                this.freeList.add(freeNode);
                this.freeList.mergeNeighbour();
            }

            usedBefore = usedAfter;
            usedAfter = usedAfter.getNext();

        }


        return true;

    }


}
