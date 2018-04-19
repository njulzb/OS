package Memory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author lzb
 * @date 2018/4/19 16:24
 */
class ConcreteMemoryAllocator2Test {

    @Test
    void requestMemoryByFirstFit() {
        ConcreteMemoryAllocator2 god = new ConcreteMemoryAllocator2(100);
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
}