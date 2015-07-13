package ru.ipccenter.travelportal.standalone.sqlexecutor.impl;

import ru.ipccenter.travelportal.standalone.sqlexecutor.Fragment;
import ru.ipccenter.travelportal.standalone.sqlexecutor.SqlFragmentator;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SqlFragmentatorImpl implements SqlFragmentator {

    private Iterator<Fragment> fragmentIterator;

    public SqlFragmentatorImpl(File sql) throws IOException{
        fragmentIterator = buildFragmentsList(sql).iterator();
    }

    @Override
    public boolean hasNext() {
        return fragmentIterator.hasNext();
    }

    @Override
    public Fragment next() {
        return fragmentIterator.next();
    }


    private static List<Fragment> buildFragmentsList(File sql) throws IOException {
        BufferedReader buffer = new BufferedReader(new FileReader(sql));
        List<Fragment> result = new LinkedList<>();

        String line = null;
        Fragment.Type currentLineType;

        FragmentImpl currentFragment = new FragmentImpl(Fragment.Type.COMMENT);
        currentFragment.append("file: ");
        currentFragment.appendLine(sql.getPath());

        while ((line = buffer.readLine()) != null) {
            if (line.startsWith("--")) {
                currentLineType = Fragment.Type.COMMENT;
            } else if (line.trim().equals("/")) {
                currentLineType = Fragment.Type.SEPARATOR;
            } else {
                currentLineType = Fragment.Type.SQL;
            }

            if (currentFragment == null) {
                currentFragment = new FragmentImpl(currentLineType);
            }

            if (currentLineType == Fragment.Type.SEPARATOR) {
                if (!currentFragment.isEmpty())
                    result.add(currentFragment.build());
                currentFragment = null;
            } else if (currentFragment.getType() == Fragment.Type.COMMENT
                    && (currentLineType == Fragment.Type.SQL || currentLineType == Fragment.Type.SEPARATOR)) {
                if (!currentFragment.isEmpty())
                    result.add(currentFragment.build());
                currentFragment = new FragmentImpl(currentLineType);
                currentFragment.appendLine(line);
            } else {
                currentFragment.appendLine(line);
            }
        }

        return result;
    }


    //    @Override
//    public String[] hasNext(String fileName) {
//        String line;
//        StringBuilder script = new StringBuilder();
//        BufferedReader buffer = null;
//        try {
//            buffer = new BufferedReader(new FileReader(fileName));
//            while ((line = buffer.readLine()) != null) {
//                if (line.equals("/")) {
//                    script = new StringBuilder();
//                } else if (line.trim().substring(0, 2).equals("--")) {
//                    Logger.getLogger(ru.ipccenter.travelportal.standalone.sqlexecutor.impl.SqlFragmentatorImpl.class.getName()).log(Level.ALL, line);
//                } else {
//                    script.append(line);
//                }
//            }
//            buffer.close();
//            return script.toString().split(";");
//        } catch (FileNotFoundException e) {
//            Logger.getLogger(ru.ipccenter.travelportal.standalone.sqlexecutor.impl.SqlFragmentatorImpl.class.getName()).log(Level.SEVERE, null, e);
//        } catch (IOException e) {
//            Logger.getLogger(ru.ipccenter.travelportal.standalone.sqlexecutor.impl.SqlFragmentatorImpl.class.getName()).log(Level.SEVERE, null, e);
//        }
//        return null;
//    }
//
//    @Override
//    public List<String> next(String topDirectory) {
//        BufferedReader buffer = null;
//        ru.ipccenter.travelportal.standalone.sqlexecutor.FileIterator fileIterator = new ru.ipccenter.travelportal.standalone.sqlexecutor.impl.FileIteratorImpl() ;
//        List<File> listOfFiles = null;
//        List<String> listOfStrings = new ArrayList<String>();
//        try {
//            listOfFiles = fileIterator.next(topDirectory);
//            for (File file : listOfFiles) {
//                String[] strings = hasNext(file.toString());
//                for (String str :strings){
//                    if(!str.trim().equals("")) {
//                        listOfStrings.add(str);
//                    }
//                }
//            }
//        } catch (IOException e) {
//            Logger.getLogger(ru.ipccenter.travelportal.standalone.sqlexecutor.impl.SqlFragmentatorImpl.class.getName()).log(Level.SEVERE, null, e);
//        }
//        return listOfStrings;
//    }
}
