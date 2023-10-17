package com.project.counter.impl;

import com.project.counter.api.FileProcessor;
import com.project.counter.exception.FileProcessException;
import com.project.counter.model.Word;

import java.io.*;
import java.util.*;

public class FileProcessorImpl implements FileProcessor {

    private static final String OUTPUT_FILE_HEADER = "Слово,Частота,Частота (в %)";

    public FileProcessorImpl(String file) {
    }

    @Override
    public void process(String file, StringWriter output) throws FileProcessException {
        List<String> listOfWords = convertFileToListOfWords(file);
        List<Word> listOfObjects = convertToListOfObjects(listOfWords);
        writeToOutput(listOfObjects, output);
    }

    private List<Word> convertToListOfObjects(List<String> inputWords) {
        int numberOfWords = inputWords.size();
        Set<String> setOfWords = new HashSet<>(inputWords);
        List<Word> listOfObjects = new ArrayList<>();
        for (String word : setOfWords) {
            long count = inputWords.stream().filter(word::equals).count();
            double freq = (double) count * 100 / numberOfWords;
            Word wordObject = Word.builder().word(word).count(count).frequency(freq).build();
            listOfObjects.add(wordObject);
        }
        listOfObjects.sort(getWordComparator());
        return listOfObjects;
    }

    private void writeToOutput(List<Word> outputData, StringWriter output) {
        output.write(OUTPUT_FILE_HEADER + System.lineSeparator());
        for (Word entry : outputData) {
            output.write(entry.word() + ',' + entry.count() + ',' + entry.frequency() + System.lineSeparator());
        }
    }

    private List<String> convertFileToListOfWords(String fileName) throws FileProcessException {
        ClassLoader classloader = getClass().getClassLoader();
        List<String> listOfWords = new ArrayList<>();
        try (InputStream is = classloader.getResourceAsStream(fileName)) {
            assert is != null;
            try (Reader reader = new InputStreamReader(is)) {
                int data = reader.read();
                StringBuilder word = new StringBuilder();
                char theChar;
                while (data != -1) {
                    theChar = (char) data;
                    if (Character.isLetter(theChar)) {
                        word.append(theChar);
                    } else if (!word.isEmpty()) {
                        listOfWords.add(word.toString());
                        word = new StringBuilder();
                    }
                    data = reader.read();
                }
            }
        } catch (IOException e) {
            throw new FileProcessException("File not found");
        }
        return listOfWords;

    }

    private static Comparator<Word> getWordComparator() {
        return Comparator.comparing(Word::count).reversed().thenComparing(Word::word);
    }

}
