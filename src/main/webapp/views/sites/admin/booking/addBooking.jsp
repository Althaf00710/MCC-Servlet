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
    <script DEFER src="https://maps.googleapis.com/maps/api/js?key=<%= request.getAttribute("apiKey") %>&libraries=places"></script>
</head>
<body>
<%@ include file="../common/navBar.jsp" %>
<div class="mouse-circle"></div>
<div class="ml-64 flex-1 p-8 pt-4">
    <!-- Header -->
    <div class="flex justify-between items-center mb-6 gap-4">
        <h1 class="text-2xl font-medium text-gray-600 whitespace-nowrap">Add Bookings</h1>
    </div>

    <!-- Flex Container for Form and Map -->
    <div class="flex flex-wrap gap-4 mb-4">
        <!-- Form Container -->
        <div class="flex-1 min-w-[300px] bg-white p-6 rounded-lg shadow-md">
            <form id="bookingForm" class="bg-white">
                <!-- Date -->
                <label class="block text-gray-600">Booking Date</label>
                <input type="date" id="bookingDate" class="w-full p-2 border rounded mb-4" required>

                <!-- Customer Selection -->
                <label class="block text-gray-600">Customer</label>
                <select id="customerSelect" class="w-full p-2 border rounded mb-4" required>
                    <option value="">Select a Customer</option>
                    <!-- Dynamic options -->
                </select>

                <!-- Pickup & Drop Locations -->
                <label class="block text-gray-600">Pickup Location</label>
                <input type="text" id="pickupLocation" class="w-full p-2 border rounded mb-4 location-input" required>

                <label class="block text-gray-600">Drop Location</label>
                <input type="text" id="dropLocation" class="w-full p-2 border rounded mb-4 location-input" required>

                <!-- Stops Section -->
                <div id="stopsContainer"></div>
                <button type="button" id="addStop" class="bg-blue-500 text-white px-3 py-1 rounded mb-4">+ Add Stop</button>

                <!-- Cab Type Selection -->
                <label class="block text-gray-600">Cab Type</label>
                <select id="cabTypeSelect" class="w-full p-2 border rounded mb-4" required>
                    <option value="">Select a Cab Type</option>
                </select>

                <!-- Cab Selection -->
                <label class="block text-gray-600">Cab</label>
                <select id="cabSelect" class="w-full p-2 border rounded mb-4" required>
                    <option value="">Select a Cab</option>
                </select>

                <button type="submit" class="bg-green-500 text-white px-4 py-2 rounded">Save Booking</button>
            </form>
        </div>

        <!-- Map Container -->
        <div class="flex-1 min-w-[300px]">
            <div id="map" style="height: 400px; width: 100%;"></div>
        </div>
    </div>

    <!-- Route Information -->
    <div id="routeInfo" class="bg-white p-4 rounded-lg shadow-md"></div>
</div>
</body>
</html>