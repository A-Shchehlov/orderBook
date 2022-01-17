package com.pack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.Files.newBufferedWriter;

public class Manager {

    public static void main(String[] args) {

        String inputFile = "input.txt";
        String outputFile = "output.txt";
        try (var reader = newBufferedReader(Path.of(inputFile));
             var writer = newBufferedWriter(Path.of(outputFile))) {
            Processor.process(supply(reader), consume(writer));
        } catch (IOException e) {
            handleIoException(e);
        }


    }

    private static Supplier<String> supply(BufferedReader reader) {
        return () -> {
            try {
                return reader.readLine();
            } catch (IOException e) {
                handleIoException(e);
            }
            return null;
        };
    }

    private static Consumer<String> consume(BufferedWriter writer) {
        return s -> {
            try {
                writer.write(s);
                writer.newLine();
            } catch (IOException e) {
                handleIoException(e);
            }
        };
    }

    private static void handleIoException(IOException e) {
        e.printStackTrace();
    }
}