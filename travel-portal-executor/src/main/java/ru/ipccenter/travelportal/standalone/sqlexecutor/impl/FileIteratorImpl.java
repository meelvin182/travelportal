package ru.ipccenter.travelportal.standalone.sqlexecutor.impl;

import ru.ipccenter.travelportal.standalone.sqlexecutor.FileIterator;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FileIteratorImpl implements FileIterator {

    private Iterator<File> iterator;


    public FileIteratorImpl(String directory) {
        File top = new File(directory);
        if(top.exists()) {
            if (top.isDirectory()) {
                this.iterator = buildFileList(top).iterator();
            } else {
                throw new IllegalArgumentException("...");
            }
        } else {
            throw new IllegalArgumentException("...");
        }
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }


    @Override
    public File next() {
        return iterator.next();
    }

    private static List<File> buildFileList(File top) {
        List<File> files = new LinkedList<>();
        buildFileListRecursive(top, files);
        return new ArrayList<File>(files);
    }

    private static void buildFileListRecursive(File current, List<File> list) {
        for (File directory: current.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        })) {
            buildFileListRecursive(directory, list);
        }


        for (File file: current.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return (name.toLowerCase().endsWith(".sql"));
            }
        })) {
            list.add(file);
        }
    }
}
