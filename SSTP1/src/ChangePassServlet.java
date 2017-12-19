import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Exceptions.SessionExpiredException;
import Exceptions.UndefinedAccountException;

@WebServlet("/changePass")
public class ChangePassServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			HttpSession session = request.getSession(false);
			if (session == null || !session.getAttributeNames().hasMoreElements()) {
				throw new SessionExpiredException();
			}
			request.getRequestDispatcher("/changePass.jsp").forward(request, response);
		}catch (Exception e){
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
			Account authUser;
			authUser = auth.loginSessionParameters(request, response);

			String password1;
			String password2;
			String name = request.getParameter("username").trim();

			password1 = request.getParameter("password1").trim();
			password2 = request.getParameter("password2").trim();
			if (!password1.equals(password2) || name.equals("") || password1.equals("") || password2.equals("")) {
				PrintWriter out = response.getWriter();
                out.println("<script type=\"text/javascript\">");
                out.println("alert('User fields are incorrect or blank');");
                out.println("location='changePass.jsp';");
                out.println("</script>");
			} else if (!authUser.getName().equals(name)) {
				System.out.println(authUser.getName() + "   " + name);
				PrintWriter out = response.getWriter();
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Username does not match');");
				out.println("location='changePass.jsp';");
				out.println("</script>");
			} else {
				auth.constFile();
				auth.change_pwd(name, password1);
				request.getSession().setAttribute("PWD",auth.encrypt(password1));
				Logger.authenticated("Change Password",authUser.getName());
				response.sendRedirect("home");
				
			}
		} catch (Exception e) {
			PrintWriter out = response.getWriter();
			if (e instanceof UndefinedAccountException){
                out.println("<script type=\"text/javascript\">");
                out.println("alert('User not found');");
                out.println("location='changePass.jsp';");
                out.println("</script>");
            }
            if(e instanceof SessionExpiredException){
				out.println("<script type=\"text/javascript\">");
				out.println("alert('Session Expired');");
				out.println("location='login.jsp';");
				out.println("</script>");
			}
		}
	}

}
