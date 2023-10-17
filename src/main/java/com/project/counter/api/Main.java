package com.project.counter.api;

import com.project.counter.exception.FileProcessException;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.ServiceLoader;


public class Main {

    public static void main(String[] args) throws  FileProcessException {
//        if (args.length != 1) {
//            System.err.printf("Usage:%n  java -jar  application.jar <fileName> %n");
//            System.exit(1);
//        }

        //String fileName = args[0];
        String fileName = "input.txt";
        process(fileName);

    }

    private static void process(String inputFileName) throws FileProcessException {
        Iterator<ProcessorFactory> factories =
                ServiceLoader.load(ProcessorFactory.class).iterator();
        if (!factories.hasNext()) {
            throw new IllegalStateException("No ProcessorFactory found");
        }

        ProcessorFactory factory = factories.next();
        FileProcessor processor = factory.createProcessor(inputFileName);
        StringWriter output = new StringWriter();
        processor.process(inputFileName, output);
    }
}
