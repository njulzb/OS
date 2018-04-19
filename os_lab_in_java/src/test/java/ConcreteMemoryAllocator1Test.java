import Memory.ConcreteMemoryAllocator1;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

/**
 * @author lzb
 * @date 2018/4/18 21:53
 */
class ConcreteMemoryAllocator1Test {

    @Test
    /**
     * 基本的测试，只对基本情况做了测试，边界值还没有测试
     */
    void requestMemoryByFirstFit() {

        ConcreteMemoryAllocator1 god = new ConcreteMemoryAllocator1(100);
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
        ConcreteMemoryAllocator1 god = new ConcreteMemoryAllocator1(100);
        int p1begin = god.requestMemoryByFirstFit("p1",80);
        int p2begin = god.requestMemoryByFirstFit("p2",20);
        assertEquals(0,p1begin);
        assertEquals(80,p2begin);

        boolean p1return = god.returnMemory("p1");
        assertTrue(p1return);

        int p3begin = god.requestMemoryByFirstFit("p3",10);
        assertEquals(0,p3begin);

        int p4begin = god.requestMemoryByFirstFit("p4",10);
        assertEquals(10,p4begin);

        int p5begin = god.requestMemoryByFirstFit("p5",10);
        assertEquals(20,p5begin);

        boolean p4return = god.returnMemory("p4");
        assertTrue(p4return);

        int p6begin = god.requestMemoryByFirstFit("p6",60);
        assertEquals(40,p6begin);

        boolean p2return = god.returnMemory("p2");
        assertTrue(p2return);

        boolean p3return = god.returnMemory("p3");
        assertTrue(p3return);

        int p7begin = god.requestMemoryByFirstFit("p7",30);
        assertEquals(70,p7begin);

    }

    @Test
    void returnMemory() {
    }
}