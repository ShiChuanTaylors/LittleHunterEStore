package servlets;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class BannerServlet extends HttpServlet {
    
    public static String htmlHeader = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n" +
" \"http://www.w3.org/TR/html4/loose.dtd\"><html lang=\"en\">\n" +
"<head>\n" +
"  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no\"/>\n" +
"  <title>Little Hunter eStore</title>\n" +
"\n" +
"  <!-- CSS  -->\n" +
"  <link href=\"https://fonts.googleapis.com/icon?family=Material+Icons\" rel=\"stylesheet\">\n" +
"  <link href=\"css/materialize.css\" type=\"text/css\" rel=\"stylesheet\" media=\"screen,projection\"/>\n" +
"  <link href=\"css/style.css\" type=\"text/css\" rel=\"stylesheet\" media=\"screen,projection\"/>\n" +
"  \n" +
"  \n" +
"  <!--  Scripts-->\n" +
"  <script src=\"https://code.jquery.com/jquery-2.1.1.min.js\"></script>\n" +
"  <script src=\"js/materialize.js\"></script>\n" +
                "<script src=\"js/init.js\"></script>" +
"\n" +
"</head>\n" +
"<body>";
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        output(request, response);
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException { 
        output(request, response);
    }
    private void output(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String path= request.getContextPath();
        PrintWriter out = response.getWriter();
        out.println(
                
            "<nav class=\"white\" role=\"navigation\">\n" +
"    <div class=\"nav-wrapper container\">\n" +
"        <a href='"+response.encodeURL(path)+"'><img src=\"images/logo_lh.png\" style=\"width:18%;\" alt=\"Little Hunter\"/></a>\n" +
"      <ul class=\"right hide-on-med-and-down\">\n" +
"        <li><a href='"+response.encodeURL(path +"/Admin")+"'>Admin</a></li>\n" +
"      </ul>\n" +
"\n" +
"      <ul id=\"nav-mobile\" class=\"side-nav\">\n" +
"        <li><a href='"+response.encodeURL(path +"/Admin")+"'>Admin</a></li>\n" +
"      </ul>\n" +
"      <a href=\"#\" data-activates=\"nav-mobile\" class=\"button-collapse\"><i class=\"material-icons\">menu</i></a>\n" +
"    </div>\n" + "  </nav>");
    }
}
