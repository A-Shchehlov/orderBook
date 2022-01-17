package com.pack;

import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Processor {
    private Processor(){}
    public static TreeMap<Integer, Integer> bidMap = new TreeMap<>();
    public static TreeMap<Integer, Integer> askMap = new TreeMap<>();


    public static void process(Supplier<String> source, Consumer<String> drain) {
        while (true) {
            String s = source.get();
            if (Objects.isNull(s)) {
                break;
            }
            var command = s.charAt(0);
            s = s.substring(2);
            switch (command) {
                case 'q' -> drain.accept(queryProcess(s));
                case 'u' -> updateProcess(s);
                case 'o' -> orderProcess(s);
            }
        }
    }

    public static void updateProcess(String s) {//9,1,bid/ask
        var q = s.split(",");
        var price = Integer.parseInt(q[0]);
        var size = Integer.parseInt(q[1]);
        if ("bid".equals(q[2])) {
            if (size == 0) {
                bidMap.remove(price);
            } else {
                bidMap.put(price, size);
            }
        } else {
            if (size == 0) {
                askMap.remove(price);
            } else {
                askMap.put(price, size);
            }
        }
    }

    private static String bestBid() {
        var map = bidMap.lastEntry();
        if (Objects.nonNull(map)) {
            return map.getKey().toString() + "," + map.getValue().toString();
        }
        return "";
    }

    private static String bestAsk() {
        var map = askMap.firstEntry();
        if (Objects.nonNull(map)) {
            return map.getKey().toString() + "," + map.getValue().toString();
        }
        return "";
    }

    private static String sizeInPrice(int price) {
        var size = bidMap.containsKey(price) ? bidMap.get(price) : askMap.get(price);
        if (Objects.nonNull(size)) {
            return Integer.toString(size);
        }
        return "0";
    }


    public static void orderProcess(String s) {
        var strings = s.split(",");
        var command = strings[0];
        switch (command) {
            case "buy" -> buy(Integer.parseInt(strings[1]));
            case "sell" -> sell(Integer.parseInt(strings[1]));
        }
    }

    private static void buy(int size) {
        while (size > 0) {
            var strings = bestAsk().split(",");
            var price = Integer.parseInt(strings[0]);
            var value = Integer.parseInt(strings[1]);
            size -= value;
            if (size == 0 || size > 0) {
                askMap.remove(price);
            } else {
                askMap.put(price, (size * -1));
            }
        }
    }

    private static void sell(int size) {
        while (size > 0) {
            var strings = bestBid().split(",");
            var price = Integer.parseInt(strings[0]);
            var value = Integer.parseInt(strings[1]);
            size -= value;
            if (size == 0 || size > 0) {
                bidMap.remove(price);
            } else {
                bidMap.put(price, (size * -1));
            }
        }
    }

    public static String queryProcess(String s) {
        String result = "";
        var strings = s.split(",");
        var command = strings[0];
        switch (command) {
            case "best_bid" -> result = bestBid();
            case "best_ask" -> result = bestAsk();
            case "size" -> result = sizeInPrice(Integer.parseInt(strings[1]));
        }
        return result;
    }
}