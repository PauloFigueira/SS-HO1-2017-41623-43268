import Exceptions.AlreadyExistsException;
import Exceptions.SessionExpiredException;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class CreateUser
 */
@WebServlet("/createUser")
public class CreateUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateUserServlet() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			HttpSession session = request.getSession(false);
			if(session ==null || !session.getAttributeNames().hasMoreElements()){
				throw new SessionExpiredException();
			}
			Authenticator auth = (Authenticator) session.getAttribute("AUTH");
			Account authUser;
			authUser = auth.loginSessionParameters(request, response);
			if (!authUser.getName().equals("root")) {
				PrintWriter out = response.getWriter();
				out.println("<script type=\"text/javascript\">");
				out.println("alert('User not allowed');");
				out.println("location='home.jsp';");
				out.println("</script>");
			} else
				request.getRequestDispatcher("/createUser.jsp").forward(request, response);
		} catch (Exception e) {
			PrintWriter out = response.getWriter();
			if(e instanceof SessionExpiredException){
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Session Expired');");
				out.println("location='login.jsp';");
				out.println("</script>");
			}
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			HttpSession session = request.getSession(false);
			if(session ==null || !session.getAttributeNames().hasMoreElements()){
				throw new SessionExpiredException();
			}
			Authenticator auth = (Authenticator) session.getAttribute("AUTH");

			String password1 = "";
			String password2 = "";
			password1 = request.getParameter("password1").trim();
			password2 = request.getParameter("password2").trim();
			String name = request.getParameter("username");

			if (!password1.equals(password2) || name.equals("") || password1.equals("") || password2.equals("")) {
				PrintWriter out = response.getWriter();
	             out.println("<script type=\"text/javascript\">");
	             out.println("alert('User fields are incorrect or blank');");
	             out.println("location='createUser.jsp';");
	             out.println("</script>");
			} else {
				auth.createAccount(name, password1);
				Logger.authenticated("Create User", "root");
				response.sendRedirect("home");
			}
		} catch (Exception e) {
			 PrintWriter out = response.getWriter();
			 if(e instanceof AlreadyExistsException)
             out.println("<script type=\"text/javascript\">");
             out.println("alert('User already exists');");
             out.println("location='home.jsp';");
             out.println("</script>");
			if(e instanceof SessionExpiredException){
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Session Expired');");
				out.println("location='login.jsp';");
				out.println("</script>");
			}
		}

	}
}
