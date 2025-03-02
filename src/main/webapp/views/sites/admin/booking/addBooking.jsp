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

    <div class="max-w-4xl mx-auto bg-white p-6 rounded-lg shadow-md">
        <!-- Booking Form -->
        <form id="bookingForm" class="bg-white p-6 rounded-lg shadow-md">
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
    <div id="map" style="height: 400px; width: 100%; margin-top: 20px;"></div>
    <div id="routeInfo" class="mt-4"></div>
</div>
</body>
</html>