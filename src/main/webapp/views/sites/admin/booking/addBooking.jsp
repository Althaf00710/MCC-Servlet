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
    <title>MCC | Add Booking</title>
    <script src="https://unpkg.com/@tailwindcss/browser@4"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/mouseAnimation.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/scrollBar.css">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/2.1.0/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <script DEFER src="${pageContext.request.contextPath}/views/static/js/mouseAnimation.js"></script>
    <script DEFER src="${pageContext.request.contextPath}/views/static/js/addBookingFunction.js"></script>
</head>
<body>
    <%@ include file="../common/navBar.jsp" %>
    <div class="mouse-circle"></div>
    <div class="ml-64 flex-1 p-8 pt-4">
        <!-- Header -->
        <div class="flex justify-between items-center mb-6 gap-4">
            <h1 class="text-2xl font-medium text-gray-600 whitespace-nowrap">Add Bookings</h1>
        </div>

        <div class="max-w-4xl mx-auto bg-white p-6 rounded-lg shadow-md">
            <!-- Booking Form -->
            <form id="bookingForm" class="space-y-6">
                <!-- Date & Customer -->
                <div class="grid grid-cols-2 gap-6">
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Booking Date & Time</label>
                        <input type="datetime-local" id="bookingDate"
                               class="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" required>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Customer</label>
                        <select id="customerSelect"
                                class="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" required>
                            <option value="">Select Customer</option>
                            <!-- Populated via JS -->
                        </select>
                    </div>
                </div>

                <!-- Pickup & Drop Locations -->
                <div class="grid grid-cols-2 gap-6">
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Pickup Location</label>
                        <input type="text" id="pickupLocation"
                               class="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" required>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Drop Location</label>
                        <input type="text" id="dropLocation"
                               class="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" required>
                    </div>
                </div>

                <!-- Stops Section -->
                <div class="space-y-4">
                    <div class="flex justify-between items-center">
                        <h3 class="text-sm font-medium text-gray-700">Intermediate Stops</h3>
                        <button type="button" onclick="addStop()"
                                class="flex items-center text-blue-600 hover:text-blue-800">
                            <i class="fi fi-rr-plus text-sm mr-1"></i> Add Stop
                        </button>
                    </div>
                    <div id="stopsContainer" class="space-y-3">
                        <!-- Stops will be added here dynamically -->
                    </div>
                </div>

                <!-- Cab Selection -->
                <div class="grid grid-cols-2 gap-6">
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Cab Type</label>
                        <select id="cabTypeSelect" onchange="loadCabsByType(this.value)"
                                class="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" required>
                            <option value="">Select Cab Type</option>
                            <!-- Populated via JS -->
                        </select>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Available Cabs</label>
                        <select id="cabSelect"
                                class="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                disabled required>
                            <option value="">Select Cab</option>
                        </select>
                    </div>
                </div>

                <!-- Submit Button -->
                <div class="pt-6">
                    <button type="submit"
                            class="w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 transition-colors">
                        Create Booking
                    </button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
