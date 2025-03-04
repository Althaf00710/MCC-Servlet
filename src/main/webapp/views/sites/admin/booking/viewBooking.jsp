<%@ page import="com.example.megacitycab.models.booking.Booking" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Altha
  Date: 3/2/2025
  Time: 6:15 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>MCC | View Bookings</title>
    <script src="https://unpkg.com/@tailwindcss/browser@4"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/mouseAnimation.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/scrollBar.css">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/2.1.0/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <script DEFER src="${pageContext.request.contextPath}/views/static/js/mouseAnimation.js"></script>
</head>
<body>
    <%@ include file="../common/navBar.jsp" %>
    <div class="mouse-circle"></div>
    <div class="ml-64 flex-1 p-8 pt-4">
        <!-- Header Section -->
        <div class="flex justify-between items-center mb-6 gap-4">
            <h1 class="text-2xl font-medium text-gray-600 whitespace-nowrap">Bookings</h1>

            <!-- Search Bar -->
            <div class="flex-1 max-w-md relative">
                <div class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-400 z-3">
                    <i class="fi fi-rr-search text-lg"></i>
                </div>
                <input type="text"
                       id="searchInput"
                       name="searchTerm"
                       placeholder="Enter Booking Number..."
                       class="w-full px-4 py-2 rounded-3xl border border-gray-300 focus:outline-none focus:ring-2 focus:ring-orange-500 pl-10 pr-16 transition-colors duration-200 hover:border-gray-400 bg-white">
            </div>

            <!-- Add Brand Button -->
            <a href="${pageContext.request.contextPath}/booking/add-booking" class="bg-orange-500 hover:bg-orange-600 text-white px-4 py-2 rounded-3xl flex items-center transition-colors duration-200 whitespace-nowrap">
                <i class="fi fi-rr-plus mr-2"></i>
                Add Booking
            </a>
        </div>

        <table class="min-w-full bg-white border border-gray-300">
            <thead>
            <tr>
                <th>Booking Number</th>
                <th>Cab</th>
                <th>Customer</th>
                <th>Booking Date</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <%
                List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
                if (bookings != null && !bookings.isEmpty()) {
                    for (Booking booking : bookings) {
            %>
            <tr>
                <td><%= booking.getBookingNumber() %></td>
                <td><%= booking.getCabName() %></td>
                <td><%= booking.getCustomerName() %></td>
                <td><%= booking.getBookingDateTime() %></td>
                <td><%= booking.getStatus() %></td>
                <td>
                    <a href="editBooking?id=<%= booking.getId() %>" class="text-blue-500">Edit</a> |
                    <a href="deleteBooking?id=<%= booking.getId() %>" class="text-red-500" onclick="return confirm('Are you sure?');">Delete</a>
                </td>
            </tr>
            <%
                }
            } else {
            %>
            <tr>
                <td colspan="9" class="text-center text-gray-500">No bookings found</td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>
    </div>
</body>
</html>
