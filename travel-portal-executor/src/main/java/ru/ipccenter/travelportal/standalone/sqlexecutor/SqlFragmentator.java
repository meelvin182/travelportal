package ru.ipccenter.travelportal.standalone.sqlexecutor;


public interface SqlFragmentator {
    public boolean hasNext();
    public Fragment next();
}
