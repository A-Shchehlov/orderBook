package com.pack;

import org.junit.jupiter.api.Test;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class ProcessorTest {

    @Test
    public void testUpdateBid(){

        Processor.bidMap.clear();
        Processor.bidMap.put(9, 3);
        String s = "9,1,bid";
        var q = s.split(",");
        Processor.updateProcess(s);

        assertEquals(1, Processor.bidMap.get(9));

    }

    @Test
    public void testUpdateRemoveFromBidIfValueIsZero(){

        Processor.bidMap.clear();
        Processor.bidMap.put(9, 0);
        String s = "9,0,bid";
        var q = s.split(",");
        Processor.updateProcess(s);

        assertFalse(Processor.bidMap.containsKey(9));

    }

    @Test
    public void testUpdateAsk(){
        Processor.askMap.clear();
        Processor.askMap.put(9, 3);
        String s = "9,1,ask";
        var q = s.split(",");
        Processor.updateProcess(s);

        assertEquals(1, Processor.askMap.get(9));

    }

    @Test
    public void testUpdateRemoveFromAskIfValueIsZero(){
        Processor.askMap.clear();
        Processor.askMap.put(9, 0);
        String s = "9,0,ask";
        var q = s.split(",");
        Processor.updateProcess(s);

        assertFalse(Processor.askMap.containsKey(9));

    }

    @Test
    public void testQueryProcessReturnBestBid(){
        Processor.bidMap.clear();
        Processor.bidMap.put(94, 30);
        Processor.bidMap.put(92, 77);
        Processor.bidMap.put(95, 40);

        var s = Processor.queryProcess("best_bid");

        assertEquals("95,40", s);

    }

    @Test
    public void testQueryProcessReturnBestAsk(){
        Processor.askMap.clear();
        Processor.askMap.put(94, 30);
        Processor.askMap.put(92, 77);
        Processor.askMap.put(95, 40);

        var s = Processor.queryProcess("best_ask");

        assertEquals( "92,77", s);

    }

    @Test
    public void testQueryProcessReturnSizeBuyPriceInBid(){
        Processor.bidMap.clear();
        Processor.askMap.clear();
        Processor.askMap.put(94, 30);
        Processor.bidMap.put(92, 77);
        Processor.askMap.put(95, 40);

        var s = Processor.queryProcess("size,92");

        assertEquals("77", s);

    }

    @Test
    public void testQueryProcessReturnSizeBuyPriceInAsk(){
        Processor.bidMap.clear();
        Processor.askMap.clear();
        Processor.askMap.put(94, 30);
        Processor.bidMap.put(92, 77);
        Processor.askMap.put(95, 40);

        var s = Processor.queryProcess("size,94");

        assertEquals("30", s);

    }

    @Test
    public void testQueryProcessNotReturnSizeBuyPriceIfKeyIsNotExist(){
        Processor.bidMap.clear();
        Processor.askMap.clear();
        Processor.askMap.put(94, 30);
        Processor.bidMap.put(92, 77);
        Processor.askMap.put(95, 40);

        var s = Processor.queryProcess("size,99");

        assertEquals("", s);

    }

    @Test
    public void testOrderProcessSell1(){
        Processor.bidMap.clear();
        Processor.askMap.clear();

        Processor.bidMap.put(9, 1);
        Processor.bidMap.put(10, 2);

        Processor.orderProcess("sell,1");

        assertEquals("10,1", Processor.queryProcess("best_bid"));

    }

    @Test
    public void testOrderProcessSell2(){
        Processor.bidMap.clear();
        Processor.askMap.clear();

        Processor.bidMap.put(9, 1);
        Processor.bidMap.put(8, 3);
        Processor.bidMap.put(10, 2);

        Processor.orderProcess("sell,4");

        assertEquals("8,2", Processor.queryProcess("best_bid"));

    }

    @Test
    public void testOrderProcessSell3(){
        Processor.bidMap.clear();

        Processor.bidMap.put(9, 1);
        Processor.bidMap.put(10, 2);

        Processor.orderProcess("sell,3");

        assertEquals("", Processor.queryProcess("best_bid"));

    }

    @Test
    public void testOrderProcessBuy1(){
        Processor.askMap.clear();

        Processor.askMap.put(9, 1);
        Processor.askMap.put(10, 2);

        Processor.orderProcess("buy,1");

        assertEquals("10,2", Processor.queryProcess("best_ask"));

    }

    @Test
    public void testOrderProcessBuy2(){
        Processor.askMap.clear();

        Processor.askMap.put(9, 1);
        Processor.askMap.put(5, 3);
        Processor.askMap.put(10, 2);
        Processor.askMap.put(15, 2);

        Processor.orderProcess("buy,7");

        assertEquals("15,1", Processor.queryProcess("best_ask"));

    }

    @Test
    public void testOrderProcessBuy3(){
        Processor.askMap.clear();

        Processor.askMap.put(9, 1);
        Processor.askMap.put(5, 3);
        Processor.askMap.put(10, 2);
        Processor.askMap.put(15, 2);

        Processor.orderProcess("buy,8");

        assertEquals("", Processor.queryProcess("best_ask"));

    }

}