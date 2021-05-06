

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import net.javasonny.manageusers.dao.UserDAO;
import net.javasonny.manageusers.model.User;

@WebServlet("/")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;
       
   
    public UserServlet() {
    	this.userDAO = new UserDAO();
        
    }
    
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		this.doGet(request, response);
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getServletPath();
		
		try {
			
			switch(action) {
			
			case "/new":
				showNewForm(request, response);
				break;
			
			case "/insert":
				try {
					insertUser(request, response);
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
				break;
			
			case "/delete":
				try {
					deleteUser(request, response);			
				}catch(Exception e) {
					e.printStackTrace();
				}
				break;
				
			case "/edit":
				try {
					showEditForm(request, response);
				}catch(Exception e) {
					e.printStackTrace();
				}	
				break;
				
			case "/update":
				try {
					updateUser(request, response);			
				}catch(Exception e) {
					e.printStackTrace();
				}		
				break;
				
			default:
				try {
					listUser(request, response);		
				}catch(Exception e) {
					e.printStackTrace();
				}			
				break;			
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void listUser(HttpServletRequest request, HttpServletResponse response)
		    throws SQLException, IOException, ServletException {
		        List<User> listUser = userDAO.selectAllUsers();
		        request.setAttribute("listUser", listUser);
		        RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
		        dispatcher.forward(request, response);
	}
	
	private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
		dispatcher.forward(request, response);
	}
		
	private void insertUser(HttpServletRequest request, HttpServletResponse response)
		    throws SQLException, IOException {
		        String name = request.getParameter("name");
		        String email = request.getParameter("email");
		        String country = request.getParameter("country");
		        User newUser = new User(name, email, country);
		        userDAO.insertUser(newUser);
		        response.sendRedirect("list");
	}
	
	private void deleteUser(HttpServletRequest request, HttpServletResponse response)
		    throws SQLException, IOException {
		        int id = Integer.parseInt(request.getParameter("id"));
		        userDAO.deleteUser(id);
		        response.sendRedirect("list");

	}
	
	 private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			    throws SQLException, ServletException, IOException {
			        int id = Integer.parseInt(request.getParameter("id"));
			        User existingUser = userDAO.selectUser(id);
			        RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
			        request.setAttribute("user", existingUser);
			        dispatcher.forward(request, response);

	 }
	 
	 private void updateUser(HttpServletRequest request, HttpServletResponse response)
			    throws SQLException, IOException {
			        int id = Integer.parseInt(request.getParameter("id"));
			        String name = request.getParameter("name");
			        String email = request.getParameter("email");
			        String country = request.getParameter("country");

			        User book = new User(id, name, email, country);
			        userDAO.updateUser(book);
			        response.sendRedirect("list");
  }

	
}
