package ru.ipccenter.travelportal.standalone.sqlexecutor.impl;

import ru.ipccenter.travelportal.standalone.sqlexecutor.Fragment;

public class FragmentImpl implements Fragment {

    private Type type;
    private String content;
    private StringBuilder builder = new StringBuilder();

    public FragmentImpl(Type type) {
        this.type = type;
    }

    public FragmentImpl(Type type, String content) {
        this.type = type;
        this.content = content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void append(String fragment) {
        builder.append(fragment);
    }

    public void appendLine(String fragment) {
        builder.append(fragment).append('\n');
    }

    public boolean isEmpty() {
        return (builder.length() == 0);
    }

    public Fragment build() {
        setContent(builder.toString());
        return this;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public Type getType() {
        return type;
    }

    //    @Override
//    public void execute(String sql) {
//        try {
//            ru.ipccenter.travelportal.standalone.sqlexecutor.impl.SqlFragmentatorImpl sqlFragmentator = new ru.ipccenter.travelportal.standalone.sqlexecutor.impl.SqlFragmentatorImpl();
//            Connection connection = DriverManager.getConnection(url, user, password);
//            final Statement statement = connection.createStatement();
//            List<String> sqlFragments = sqlFragmentator.next(sql);
//            for (String toRun : sqlFragments) {
//                if (toRun.length() > 0) {
//                    statement.execute(toRun);
//                }
//            }
//        } catch (SQLException e) {
//            Logger.getLogger(ru.ipccenter.travelportal.standalone.sqlexecutor.impl.FragmentImpl.class.getName()).log(Level.SEVERE, null, e);
//        }
//    }
}
