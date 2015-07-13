package ru.ipccenter.travelportal.standalone;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplateRemote;
import ru.ipccenter.travelportal.standalone.sqlexecutor.FileIterator;
import ru.ipccenter.travelportal.standalone.sqlexecutor.Fragment;
import ru.ipccenter.travelportal.standalone.sqlexecutor.SqlFragmentator;
import ru.ipccenter.travelportal.standalone.sqlexecutor.impl.FileIteratorImpl;
import ru.ipccenter.travelportal.standalone.sqlexecutor.impl.SqlFragmentatorImpl;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

public class ScriptExecutor {

    private static final Logger Log = Logger.getLogger(ScriptExecutor.class);
    private static final String JDBC_TEMPLATE_BEAN =
            "ejb:travel-portal-ear-0.1/travel-portal-ejb-0.1/JdbcTemplateBean!ru.ipccenter.travelportal.jdbc.template.JdbcTemplateRemote";

    private final String directory;
    private JdbcTemplateRemote jdbcTemplate;
    private InitialContext initialContext;


    private ScriptExecutor(String directory) {
        this.directory = directory;
        final Hashtable jndiProperties = new Hashtable();
    }

    private void execute() throws NamingException  {
        FileIterator fileIterator = new FileIteratorImpl(directory);
        try {
            this.initialContext = new InitialContext();
            this.jdbcTemplate = (JdbcTemplateRemote) initialContext.lookup(JDBC_TEMPLATE_BEAN);

            while (fileIterator.hasNext()) {
                File sql = fileIterator.next();

                try {
                    SqlFragmentator fragmentator = new SqlFragmentatorImpl(sql);
                    while (fragmentator.hasNext()) {
                        Fragment fragment = fragmentator.next();
                        switch (fragment.getType()) {
                            case COMMENT:
                                //Log.info("=== COMMENT: " + fragment.getContent());
                                //Log.info("=== END COMMENT ===");
                                Log.info(fragment.getContent());
                                break;
                            case SQL:
                                try {
                                    jdbcTemplate.executeUpdate(fragment.getContent());
                                } catch (Exception e) {
                                    Log.error(e.getMessage());
                                }
                                break;
                        }
                    }
                } catch (IOException e) {
                    Log.error(e);
                }
            }
        } finally {
            if (initialContext != null)
                initialContext.close();
        }
    }


    public static void main(String[] args) throws Exception {
        try {
            if (args.length < 1) {
                throw new IllegalArgumentException("No directory parameter");
            }

            new ScriptExecutor(args[0]).execute();
        } catch (Throwable t) {
            Log.error(t.getMessage(), t);
            System.err.println(t.getMessage());
        }
    }
}
