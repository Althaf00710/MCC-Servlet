<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>MCC | Add Booking</title>
    <script src="https://unpkg.com/@tailwindcss/browser@4"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/mouseAnimation.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/scrollBar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/AdminCabBooking.css">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/2.1.0/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.13/css/select2.min.css" rel="stylesheet" />
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.13/js/select2.min.js"></script>
    <script DEFER src="${pageContext.request.contextPath}/views/static/js/mouseAnimation.js"></script>
    <script DEFER src="${pageContext.request.contextPath}/views/static/js/addBookingFunction.js"></script>
    <script DEFER src="https://maps.googleapis.com/maps/api/js?key=<%= request.getAttribute("apiKey") %>&libraries=places"></script>
</head>
<body>
<%@ include file="../common/navBar.jsp" %>
<div class="mouse-circle"></div>
<div class="ml-64 flex-1 p-8 pt-4 bg-gray-100">
    <!-- Header -->
    <div class="flex justify-between items-center mb-3 gap-4">
        <h1 class="text-2xl font-medium text-gray-600 whitespace-nowrap">Add Bookings</h1>
    </div>

    <!-- Flex Container for Form and Map -->
    <div class="flex flex-wrap gap-4">
        <!-- Left Side: Form Container -->
        <div class="flex-1 min-w-[300px] bg-white px-6 py-3 rounded-lg shadow-md overflow-y-auto max-h-[800px]">
            <form id="bookingForm" class="bg-white">
                <!-- Booking Date and Customer on Same Line -->
                <div class="flex gap-4 mb-4">
                    <div class="w-2/5">
                        <label class="block text-gray-600">Booking Date</label>
                        <input type="datetime-local" id="bookingDate" class="w-full p-2 border rounded" required>
                    </div>
                    <div class="w-3/5">
                        <label class="block text-gray-600">Customer</label>
                        <select id="customerSelect" class="w-full p-2 border rounded" required>
                            <option value="">Select a Customer</option>
                            <!-- Dynamic options -->
                        </select>
                    </div>
                </div>

                <!-- Pickup and Drop on Same Line with Arrow -->
                <div class="flex items-center gap-4 mb-4">
                    <div class="flex-1">
                        <label class="block text-gray-600">Pickup Location</label>
                        <input type="text" id="pickupLocation" class="w-full p-2 border rounded location-input" required>
                    </div>
                    <i class="fi fi-rr-arrow-right text-gray-600"></i>
                    <div class="flex-1">
                        <label class="block text-gray-600">Drop Location</label>
                        <input type="text" id="dropLocation" class="w-full p-2 border rounded location-input" required>
                    </div>
                </div>

                <!-- Stops Section -->
                <div id="stopsContainer"></div>
                <button type="button" id="addStop" class="text-blue-500 mb-4">
                    <i class="fi fi-rr-plus"></i> Add Stop
                </button>

                <!-- Cab Type Selection -->
                <label class="block text-gray-600">Cab Type</label>
                <div id="cabTypeContainer" class="flex flex-nowrap gap-4 mb-4 overflow-x-auto scroll-smooth whitespace-nowrap px-4 py-2"></div>
                <input type="hidden" id="selectedCabType" name="cabTypeId" required>

                <!-- Cab Selection -->
                <label class="block text-gray-600">Cab</label>
                <select id="cabSelect" class="w-full p-2 border rounded mb-4" required>
                    <option value="">Select a Cab</option>
                </select>

                <button type="submit" class="bg-green-500 text-white px-4 py-2 rounded">Save Booking</button>
            </form>
        </div>

        <!-- Right Side: Route Info & Map Container -->
        <div class="flex-1 min-w-[300px] flex flex-col gap-4">
            <!-- Route Information (Fixed 50% height) -->
            <div id="routeInfo" class="bg-white p-4 border-gray-300 border rounded-lg shadow-lg overflow-y-auto h-[40vh]"></div>

            <!-- Map Container (Fixed 50% height) -->
            <div id="map" class="rounded-lg shadow-md h-[50vh]" style="width: 100%;"></div>
        </div>

    </div>


    <!-- Route Information -->
<%--    <div id="routeInfo" class="bg-white p-4 rounded-lg shadow-md"></div>--%>
</div>
</body>
</html>