package Memory;

/**
 * @author lzb
 * @date 2018/4/19 14:50
 */
public interface MemoryAllocator {

    /**
     *
     * @param processId
     * @param requestSize
     * @return begin address 申请到的内存起始地址，长度为传入的长度，-1表示无法分配内存，需要等待
     */
    int requestMemoryByFirstFit(String processId , int requestSize);


    /**
     *
     * @param processId
     * @return 返回归还是否成功的标志
     */
    boolean returnMemory(String processId);

}
