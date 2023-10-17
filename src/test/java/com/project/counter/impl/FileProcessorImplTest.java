package com.project.counter.impl;

import com.project.counter.api.FileProcessor;
import com.project.counter.api.ProcessorFactory;
import com.project.counter.exception.FileProcessException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class FileProcessorImplTest {

    private static Stream<Arguments> testProcessInputProvider() {
        return Stream.of(
                Arguments.of("input_1.txt",  "output_1.csv")
        );
    }
    @ParameterizedTest
    @MethodSource("testProcessInputProvider")
    void testProcess(String file, String expectedOutputResource) throws IOException, FileProcessException {
        StringWriter sink = new StringWriter();
        ProcessorFactory factory = new ProcessorFactoryImpl();
        FileProcessor processor = factory.createProcessor(file);
        processor.process(file, sink);

        Reader readerExpected = loadResource(expectedOutputResource);
        Reader readerActual = new StringReader(sink.getBuffer().toString());
        long lineNumber = filesCompareByLine(readerExpected, readerActual);
        assertEquals(-1, lineNumber);
    }

    private Reader loadResource(String fileName) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(fileName);
        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        return new BufferedReader(streamReader);
    }

    public static long filesCompareByLine(Reader readerExpected, Reader readerActual) throws IOException {
        try (BufferedReader bf1 = new BufferedReader(readerExpected);
             BufferedReader bf2 = new BufferedReader(readerActual)) {

            long lineNumber = 1;
            String line1, line2;
            while ((line1 = bf1.readLine()) != null) {
                line2 = bf2.readLine();
                if (!line1.equals(line2)) {
                    return lineNumber;
                }
                lineNumber++;
            }
            if (bf2.readLine() == null) {
                System.out.println("-1");
                return -1;
            } else {
                return lineNumber;
            }
        }
    }
}