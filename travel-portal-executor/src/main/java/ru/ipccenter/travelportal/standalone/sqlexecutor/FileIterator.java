package ru.ipccenter.travelportal.standalone.sqlexecutor;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileIterator {

    public boolean hasNext();

    public File next();

}
