import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Exceptions.LockedAccountException;
import Exceptions.LoggedInAccountException;
import Exceptions.SessionExpiredException;
import Exceptions.UndefinedAccountException;

@WebServlet("/delete")
public class DeleteUserServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

			} else {
				String dir = getServletContext().getRealPath("/");
				Logger logger = new Logger(dir);
				logger.authenticated("Delete User", authUser.getName());
				request.getRequestDispatcher("/deleteUser.jsp").forward(request, response);
			}

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
			auth.constFile();
			String name = request.getParameter("deletedAcc");
			auth.deleteAccount(name);
			response.sendRedirect("home");
		} catch (Exception e) {
			PrintWriter out = response.getWriter();
			if (e instanceof UndefinedAccountException) {
				out.println("<script type=\"text/javascript\">");
				out.println("alert('User not found');");
				out.println("location='deleteUser.jsp';");
				out.println("</script>");
			}
			if (e instanceof LoggedInAccountException) {
				out.println("<script type=\"text/javascript\">");
				out.println("alert('User logged in');");
				out.println("location='deleteUser.jsp';");
				out.println("</script>");
			}
			if (e instanceof LockedAccountException) {
				out.println("<script type=\"text/javascript\">");
				out.println("alert('User not locked');");
				out.println("location='deleteUser.jsp';");
				out.println("</script>");
			}
		}
	}

}
