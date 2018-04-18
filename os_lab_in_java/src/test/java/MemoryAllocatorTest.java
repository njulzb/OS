import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

/**
 * @author lzb
 * @date 2018/4/18 21:53
 */
class MemoryAllocatorTest {

    @Test
    /**
     * 基本的测试，只对基本情况做了测试，边界值还没有测试
     */
    void requestMemoryByFirstFit() {

        MemoryAllocator god = new MemoryAllocator(100);
        int begin = god.requestMemoryByFirstFit("p1",60);
        assertEquals(0,begin);
        int begin2 = god.requestMemoryByFirstFit("p2",30);
        assertEquals(60,begin2);
        int begin3 = god.requestMemoryByFirstFit("p3",10);
        assertEquals(90,begin3);

        boolean result = god.returnMemory("p1");
        assertTrue(result);

        int begin4 = god.requestMemoryByFirstFit("p4",30);
        assertEquals(0,begin4);



    }

    @Test
    void adjustMemory() {
    }

    @Test
    void returnMemory() {
    }
}