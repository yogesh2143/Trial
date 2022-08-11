package com.cybage.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cybage.bean.Restaurant;
import com.cybage.bean.User;
import com.cybage.service.RestaurantService;
import com.cybage.service.RestaurantServiceImpl;
import com.cybage.service.UserService;
import com.cybage.service.UserServiceImpl;

@WebServlet(urlPatterns= {"/DisplayAllRestaurants","/Login","/AdminController/EditRestaurant","/AdminController/DeleteRestaurant","/Logout","/AddRestaurantOwner","/UpdateRestaurant"})
public class AdminController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private RestaurantService restaurantService = new RestaurantServiceImpl(); 
	private UserService userService = new UserServiceImpl();
	RequestDispatcher requestDispatcher ;
	
    public AdminController() {
        super();
    }

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	String path = request.getServletPath();
		System.out.println(path);
		switch (path) {
			case "/DisplayAllRestaurants":
				List<Restaurant> restaurantList = restaurantService.allRestaurant();
				request.setAttribute("restaurantList", restaurantList);
				requestDispatcher = request.getRequestDispatcher("/jsp/RestaurantOwner/displayAllRestaurant.jsp");
				requestDispatcher.forward(request, response);
				break;
				
			case "/AdminController/EditRestaurant":
				requestDispatcher = request.getRequestDispatcher("/jsp/RestaurantOwner/editRestaurant.jsp");
				requestDispatcher.forward(request, response);
				break;
			
			case "/AdminController/DeleteRestaurant":
				restaurantService.deleteResaturant(Integer.parseInt(request.getParameter("id")));
				response.sendRedirect("http://localhost:8080/OnlineFoodDeliveryApp/DisplayAllRestaurants");
				break;
					
			case "/Logout":
				request.getSession().invalidate();
				requestDispatcher = request.getRequestDispatcher("Login.jsp");
				requestDispatcher.forward(request, response);
			break;

		}

	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		PrintWriter out = response.getWriter();
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		User user = userService.findByEmail(email);
		HttpSession session = request.getSession();
		String path = request.getServletPath();
		System.out.println(path);
		switch (path) {
			case "/Login":
				session.setAttribute("favoriteList", new ArrayList<>());
				session.setAttribute("user", user);
				if(email.equals("admin@gmail.com") && password.equals("admin@123")) {
					response.sendRedirect("http://localhost:8080/OnlineFoodDeliveryApp/jsp/RestaurantOwner/admin.jsp");
				}
				else if(email.equals(user.getEmail()) && password.equals(user.getPassword())) {
					response.sendRedirect("HomeController");
				}
				else {
					out.print("<center><h4 style='color:red'>Enter Valid Credentials</h4><br></center>");
					request.getRequestDispatcher("Login.jsp").include(request, response);
				}
			break;
			
			case "/AddRestaurantOwner":
				restaurantService.addRestaurant(new Restaurant(request.getParameter("name"), request.getParameter("userName"), request.getParameter("password"),Integer.parseInt(request.getParameter("pincode")), request.getParameter("email"), request.getParameter("address")));
				response.sendRedirect("http://localhost:8080/OnlineFoodDeliveryApp/DisplayAllRestaurants");
				break;
				
			case "/UpdateRestaurant":
				Restaurant restaurant = new Restaurant();
				restaurant.setRestaurantId(Integer.parseInt(request.getParameter("restId")));
				restaurant.setRestaurantUserName(request.getParameter("userName"));
				restaurant.setPincode(Integer.parseInt(request.getParameter("pincode")));
				restaurant.setAddress(request.getParameter("address"));
				restaurantService.updateResaturant(restaurant);
				response.sendRedirect("http://localhost:8080/OnlineFoodDeliveryApp/DisplayAllRestaurants");
				break;
		}
			
	}

}
