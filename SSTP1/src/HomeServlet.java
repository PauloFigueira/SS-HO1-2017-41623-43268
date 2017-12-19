import Exceptions.SessionExpiredException;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
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
			request.getRequestDispatcher("/home.jsp").forward(request, response);
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

		String act = request.getParameter("action");
		switch (act) {
		case "Create User":
			// TODO
			response.sendRedirect("createUser");
			break;
		case "Delete User":
			// TODO
			response.sendRedirect("delete");
			break;
		case "Change Password":
			// TODO
			response.sendRedirect("changePass");
			break;
		case "Logout":
			// TODO
			Authenticator auth = (Authenticator)request.getSession().getAttribute("AUTH");
			try {
				Account authUser = auth.loginSessionParameters(request, response);
				auth.logout(authUser);
				HttpSession session = request.getSession(false);
				if (session != null)
					session.invalidate();
				response.sendRedirect("login");
				String dir = getServletContext().getRealPath("/");
				Logger logger =new Logger(dir);
				logger.authenticated("Log Out",authUser.getName());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			response.getWriter().println(act);
		}

	}

}
