<%@ page import="javax.naming.*" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.PrintWriter" %>
<html>
<head>
    <title></title>
</head>
<body>
<%
    class JndiPrinter {
        private void printContext(PrintWriter out, Context ctx, int indent) throws ServletException, IOException, NamingException {
            NamingEnumeration en = ctx.listBindings("");
            while (en.hasMore()) {
                Binding b = (Binding) en.next();
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < indent; i++)
                    builder.append("&nbsp;");
                out.println(builder.toString() + b.getName() + " = " + b.getClassName() + "<br />");
                try {
                    if (b.getObject() instanceof Context) {
                        printContext(out, (Context) b.getObject(), indent + 1);
                    }
                } catch (Exception exc) {
                    out.println("Error <br />");
                    exc.printStackTrace(new PrintWriter(out));
                }
            }
        }
    }

    InitialContext ictx = new InitialContext();
    Context ctx = (Context) ictx.lookup("java:global");
    new JndiPrinter().printContext(new PrintWriter(out), ctx, 1);
%>
</body>
</html>
