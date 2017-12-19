import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Exceptions.AuthenticationErrorException;
import Exceptions.LockedAccountException;
import Exceptions.UndefinedAccountException;

/**
 * Servlet implementation class test
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	
	//private static final String DIR = Authenticator.class.getProtectionDomain().getCodeSource().getLocation().toString().split(":")[1] + "database.txt";


    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */

    public LoginServlet() throws FileNotFoundException {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if(session == null || !session.getAttributeNames().hasMoreElements()){
            String dir = getServletContext().getRealPath("/")+ File.separator;
            Path path = Paths.get(dir + "database.txt");
            if(Files.exists(path)) {
                Authenticator auth = new Authenticator(dir);
                auth.constFile();
                if (!auth.getLogged().equals("none")) {
                    Account logged = auth.getAccounts().get(auth.getLogged());
                    auth.logout(logged);
                    Logger logger = new Logger(dir);
                    logger.authenticated("Log Out", logged.getName());
                }
            }
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }else {
            response.sendRedirect("home");
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String dir = getServletContext().getRealPath("/")+ File.separator;
        Path path = Paths.get(dir + "/database.txt");
        if (!Files.exists(path)) {
            List<String> lines = null;
            try {
                lines = Arrays.asList("none", "root,"+new Authenticator(dir).encrypt("pw")+",false,false,0");
                Files.write(path, lines, Charset.forName("UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
		}
        try {
        	
            Authenticator auth = new Authenticator(dir);
            auth.constFile();
            String name = request.getParameter("username");
            String password = auth.encrypt(request.getParameter("password"));
            HttpSession session = request.getSession(true);
            Account authUser = auth.login(name,password);
            session.setAttribute("USER", authUser.getName());   
            session.setAttribute("PWD", authUser.getPassword());
            session.setAttribute("AUTH",auth);
            session.setMaxInactiveInterval(600);
            response.sendRedirect("home");

            Logger logger = new Logger(dir);
            logger.authenticated("Log In", authUser.getName());

        } catch (Exception e) {
            PrintWriter out =response.getWriter();
            if(e instanceof UndefinedAccountException){
                out.println("<script type=\"text/javascript\">");
                out.println("alert('User or password are not correct');");
                out.println("location='login.jsp';");
                out.println("</script>");
            }
            if(e instanceof AuthenticationErrorException) {
                out.println("<script type=\"text/javascript\">");
                out.println("alert('User or password are not correct');");
                out.println("location='login.jsp';");
                out.println("</script>");
            }
            if(e instanceof LockedAccountException) {
                out.println("<script type=\"text/javascript\">");
                out.println("alert('User is locked');");
                out.println("location='login.jsp';");
                out.println("</script>");
            }
        }

    }

}
