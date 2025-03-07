<%@ page import="com.example.megacitycab.models.booking.Booking" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%--
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
        <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/tableScrollBar.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/billingPopup.css">
        <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/2.1.0/uicons-regular-rounded/css/uicons-regular-rounded.css">
        <script DEFER src="${pageContext.request.contextPath}/views/static/js/mouseAnimation.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
        <script src="${pageContext.request.contextPath}/views/static/js/bookingView.js"></script>
    </head>

    <body class="bg-gray-100">
        <%@ include file="../common/navBar.jsp" %>
        <div class="mouse-circle"></div>
        <div class="ml-64 flex-1 p-8 pt-4 bg-gray-100">
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

            <div class="overflow-x-auto bg-white rounded-lg shadow-md">
                <table class="min-w-full divide-y divide-gray-200 min-h-60">
                    <thead class="bg-gray-200 sticky">
                        <tr>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Booking Number</th>
                            <th class="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">Cab</th>
                            <th class="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">Customer</th>
                            <th class="px-6 py-3 text-center group text-xs font-medium text-gray-500 uppercase tracking-wider">
                                <div class="flex items-center justify-center group relative cursor-pointer">
                                    Booking Date
                                    <i class="fi fi-rr-angle-small-down ml-1 text-gray-400 group-hover:text-gray-600 transition-colors"></i>
                                    <!-- Date Filter Dropdown -->
                                    <div class="hidden group-hover:block absolute top-full left-0 mt-1 w-64 bg-white border border-gray-200 rounded-lg shadow-lg z-10 p-2">
                                        <input type="date"
                                               id="dateFilter"
                                               class="w-full px-2 py-1 border border-gray-600 rounded-lg text-sm"
                                               onchange="applyDateFilter(this.value)">
                                        <div class="bg-gray-50 text-center px-2 py-1 rounded-md hover:bg-gray-100 cursor-pointer filter-option mt-2"
                                             data-filter=""
                                             onclick="applyDateFilter('')">
                                            Show All
                                        </div>
                                    </div>
                                </div>
                            </th>

                            <!-- Status Header with Filter -->
                            <th class="px-6 py-3 text-center text-xs group font-medium text-gray-500 uppercase tracking-wider">
                                <div class="flex items-center justify-center group relative cursor-pointer">
                                    Status
                                    <i class="fi fi-rr-angle-small-down ml-1 text-gray-400 group-hover:text-gray-600 transition-colors"></i>
                                    <!-- Status Filter Dropdown -->
                                    <div class="hidden group-hover:block absolute top-full left-0 mt-1 w-40 bg-white border border-gray-200 rounded-lg shadow-lg z-10">
                                        <div class="p-2 space-y-2">
                                            <div class="flex items-center px-2 py-1 rounded-md hover:bg-gray-100 cursor-pointer filter-option"
                                                 data-filter="PENDING"
                                                 onclick="applyStatusFilter('PENDING')">
                                                <span class="w-2 h-2 rounded-full bg-gray-500 mr-2"></span>
                                                Pending
                                            </div>
                                            <div class="flex items-center px-2 py-1 rounded-md hover:bg-gray-100 cursor-pointer filter-option"
                                                 data-filter="CONFIRMED"
                                                 onclick="applyStatusFilter('CONFIRMED')">
                                                <span class="w-2 h-2 rounded-full bg-blue-400 mr-2"></span>
                                                Confirmed
                                            </div>
                                            <div class="flex items-center px-2 py-1 rounded-md hover:bg-gray-100 cursor-pointer filter-option"
                                                 data-filter="ON_TRIP"
                                                 onclick="applyStatusFilter('ON TRIP')">
                                                <span class="w-2 h-2 rounded-full bg-orange-400 mr-2"></span>
                                                On Trip
                                            </div>
                                            <div class="flex items-center px-2 py-1 rounded-md hover:bg-gray-100 cursor-pointer filter-option"
                                                 data-filter="COMPLETED"
                                                 onclick="applyStatusFilter('COMPLETED')">
                                                <span class="w-2 h-2 rounded-full bg-green-400 mr-2"></span>
                                                Completed
                                            </div>
                                            <div class="flex items-center px-2 py-1 rounded-md hover:bg-gray-100 cursor-pointer filter-option"
                                                 data-filter="CANCELLED"
                                                 onclick="applyStatusFilter('CANCELLED')">
                                                <span class="w-2 h-2 rounded-full bg-red-500 mr-2"></span>
                                                Cancelled
                                            </div>
                                            <div class="bg-gray-50 text-center px-2 py-1 rounded-md hover:bg-gray-100 cursor-pointer filter-option"
                                                 data-filter=""
                                                 onclick="applyStatusFilter('')">
                                                Show All
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </th>

                            <th class="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                        </tr>
                    </thead>

                    <tbody class="bg-white divide-y divide-gray-200">
                    <%
                        List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
                        if (bookings != null && !bookings.isEmpty()) {
                            for (Booking booking : bookings) {
                    %>
                    <tr data-booking-id="<%= booking.getId() %>">
                        <td class="px-4 py-4 text-left whitespace-nowrap font-medium text-sm text-gray-700"><%= booking.getBookingNumber() %></td>
                        <td class="px-6 py-4 text-center whitespace-nowrap text-sm text-gray-500"><%= booking.getCabName() %></td>
                        <td class="px-6 py-4 text-center whitespace-nowrap text-sm text-gray-500"><%= booking.getCustomerName() %></td>
                        <td class="px-6 py-4 text-center whitespace-nowrap text-sm text-gray-500">
                            <%
                                Date bookingDate = booking.getBookingDateTime();
                                if (bookingDate != null) {
                                    // Create calendars for comparison
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(bookingDate);

                                    // Get current date
                                    Calendar today = Calendar.getInstance();
                                    today.set(Calendar.HOUR_OF_DAY, 0);
                                    today.set(Calendar.MINUTE, 0);
                                    today.set(Calendar.SECOND, 0);
                                    today.set(Calendar.MILLISECOND, 0);

                                    // Create yesterday and tomorrow
                                    Calendar yesterday = (Calendar) today.clone();
                                    yesterday.add(Calendar.DATE, -1);

                                    Calendar tomorrow = (Calendar) today.clone();
                                    tomorrow.add(Calendar.DATE, 1);

                                    // Format time
                                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                                    String time = timeFormat.format(bookingDate);

                                    // Check dates
                                    cal.set(Calendar.HOUR_OF_DAY, 0);
                                    cal.set(Calendar.MINUTE, 0);
                                    cal.set(Calendar.SECOND, 0);
                                    cal.set(Calendar.MILLISECOND, 0);

                                    String dateText;
                                    if (cal.equals(today)) {
                                        dateText = "Today";
                                    } else if (cal.equals(yesterday)) {
                                        dateText = "Yesterday";
                                    } else if (cal.equals(tomorrow)) {
                                        dateText = "Tomorrow";
                                    } else {
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                                        dateText = dateFormat.format(bookingDate);
                                    }

                                    out.print(dateText + " " + time);
                                } else {
                                    out.print("N/A");
                                }
                            %>
                        </td>
                        <td class="px-6 py-4 text-center whitespace-nowrap text-sm text-gray-500">
                            <%
                                String status = booking.getStatus();
                                String color = ""; // Default color
                                switch (status) {
                                    case "PENDING":
                                        color = "bg-gray-500";
                                        break;
                                    case "CONFIRMED":
                                        color = "bg-blue-400";
                                        break;
                                    case "ON TRIP":
                                        color = "bg-orange-400";
                                        break;
                                    case "COMPLETED":
                                        color = "bg-green-400";
                                        break;
                                    case "CANCELLED":
                                        color = "bg-red-500";
                                        break;
                                    default:
                                        color = "bg-gray-500";
                                        break;
                                }
                            %>
                            <div class="w-2 h-2 rounded-full inline-block <%= color %> mr-1"></div> <%= status %>
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-center text-gray-500">
                            <button class="dropdown-button text-gray-500 hover:cursor-pointer bg-gray-100 px-4 py-1 hover:bg-gray-200 rounded-lg">
                                <i id="caret-icon" class="fi fi-rr-caret-down transition-transform duration-300"></i>
                            </button>

                            <% if (booking.getStatus().equalsIgnoreCase("PENDING")) { %>
                            <form action="${pageContext.request.contextPath}/booking/updateStatus" method="POST" style="display: inline;">
                                <input type="hidden" name="id" value="<%= booking.getId() %>">
                                <input type="hidden" name="status" value="CONFIRMED">
                                <button type="submit" class="text-blue-500 hover:cursor-pointer bg-blue-100 hover:bg-blue-200 px-6 py-1 rounded-full font-semibold">Confirm</button>
                            </form>
                            <% } else if (booking.getStatus().equalsIgnoreCase("CONFIRMED")) { %>
                            <form action="${pageContext.request.contextPath}/booking/updateStatus" method="POST" style="display: inline;">
                                <input type="hidden" name="id" value="<%= booking.getId() %>">
                                <input type="hidden" name="status" value="ON TRIP">
                                <button type="submit" class="text-orange-500 hover:cursor-pointer bg-orange-100 hover:bg-orange-200 px-8 py-1 rounded-full font-semibold">Start</button>
                            </form>
                            <% } else if (booking.getStatus().equalsIgnoreCase("ON TRIP")) { %>
                            <button type="button" class="completeButton text-green-500 hover:cursor-pointer bg-green-100 hover:bg-green-200 px-5 py-1 rounded-full font-semibold"
                                    onclick="openCompleteModal('<%= booking.getId() %>', '<%= booking.getBookingNumber() %>')">
                                Complete
                            </button>
                            <% } %>

                            <% if (!((booking.getStatus().equalsIgnoreCase("CANCELLED")) || (booking.getStatus().equalsIgnoreCase("COMPLETED")))) { %>
                            <form id="cancelForm-<%= booking.getId() %>" action="${pageContext.request.contextPath}/booking/updateStatus" method="POST" style="display: inline;">
                                <input type="hidden" name="id" value="<%= booking.getId() %>">
                                <input type="hidden" name="status" value="CANCELLED">
                                <button type="button" class="text-white bg-red-500 hover:bg-red-600 p-1.5 rounded-full text-sm ml-4" onclick="confirmCancel('<%= booking.getId() %>')">
                                    <i class="fi fi-rr-cross text-xs font-bold"></i>
                                </button>
                            </form>
                            <% } %>
                        </td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="9" class="text-center text-gray-500 p-18">No bookings found</td>
                    </tr>
                    <%
                        }
                    %>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Modal -->
        <div id="modalOverlay" class="fixed inset-0 bg-black/50 bg-opacity-50 hidden transition-opacity duration-300" onclick="closeModal()"></div>
        <div id="completeModal" class="fixed inset-y-0 right-0 my-4 mr-4 w-108 h-[calc(100vh-40px)] rounded-3xl bg-white shadow-lg transform translate-x-full transition-transform duration-300 ease-in-out hidden z-101">
            <div id="modalContent" class="p-6 overflow-y-auto h-full">

            </div>
        </div>

        <div id="PaymentModal" class="fixed bottom-0 right-0 my-6 mr-118 w-108 h-[40vh] rounded-3xl bg-white shadow-lg transform translate-x-full transition-transform duration-300 ease-in-out hidden z-100">
            <div id="paymentModalContent" class="p-6 overflow-y-auto h-full">

            </div>
        </div>

    </body>
</html>
