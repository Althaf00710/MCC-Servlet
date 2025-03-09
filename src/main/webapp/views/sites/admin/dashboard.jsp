<%--
  Created by IntelliJ IDEA.
  User: Altha
  Date: 2/4/2025
  Time: 12:17 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>MCC | Dashboard</title>
        <script src="https://unpkg.com/@tailwindcss/browser@4"></script>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/scrollBar.css">
        <link rel="stylesheet" href="../../static/css/mouseAnimation.css">
        <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/2.1.0/uicons-regular-rounded/css/uicons-regular-rounded.css">
        <script DEFER src="${pageContext.request.contextPath}/views/static/js/mouseAnimation.js"></script>
        <script DEFER src="${pageContext.request.contextPath}/views/static/js/dashboard.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://maps.googleapis.com/maps/api/js?key=&libraries=places"></script>
    </head>
    <body class="bg-gray-50">
        <%@ include file="common/navBar.jsp" %>
        <div class="mouse-circle"></div>
        <div class="ml-64 flex-1 p-8 pt-4">
            <div class="flex justify-between items-center mb-3 gap-4">
                <h1 class="text-2xl font-medium text-gray-600 whitespace-nowrap">Dashboard</h1>
            </div>

            <!-- Main Grid Container -->
            <div class="grid grid-cols-3 gap-4 h-[calc(100vh-180px)]">
                <!-- Left Column -->
                <div class="col-span-2 flex flex-col gap-6">
                    <!-- Totals Row -->
                    <div class="grid grid-cols-3 gap-6">
                        <div class="bg-orange-400 rounded-2xl border-t-2 border-orange-400">
                            <div class="bg-white py-4 px-6 rounded-2xl shadow-md">
                                <h3 class="text-gray-500 text-sm font-medium mb-2">Today's Sales <i class="fi fi-rr-calculator-money ml-2"></i></h3>
                                <div class="text-2xl font-semibold text-gray-700">Rs. <span id="todayTotal">0.00</span></div>
                            </div>
                        </div>
                        <div class="bg-purle-400 rounded-2xl border-t-2 border-purple-400">
                            <div class="bg-white py-4 px-6 rounded-2xl shadow-md">
                                <h3 class="text-gray-500 text-sm font-medium mb-2">Today Bookings <i class="fi fi-rr-car-journey ml-2"></i></h3>
                                <div class="text-2xl font-semibold text-gray-700" id="bookingCount">0</div>
                            </div>
                        </div>
                        <div class="bg-red-400 rounded-2xl border-t-2 border-red-400">
                            <div class="bg-white py-4 px-6 rounded-2xl shadow-md">
                                <h3 class="text-gray-500 text-sm font-medium mb-2">Active Drivers <i class="fi fi-rr-driver-man ml-2"></i></h3>
                                <div class="text-2xl font-semibold text-gray-700" id="activeDriver">0</div>
                            </div>
                        </div>
                    </div>

                    <!-- Daily Sales Chart -->
                    <div class="bg-gray-200 rounded-2xl shadow-md">
                        <h2 class="text-sm font-semibold text-gray-700 mb-1 ml-3">Daily Sales</h2>
                        <div class="h-64 bg-white rounded-b-2xl p-4">
                            <canvas id="dailySalesChart" class="w-100 h-100"></canvas>
                        </div>
                    </div>

                    <!-- New Bookings Table -->
                    <div class="bg-gray-200 rounded-2xl shadow-md flex-1">
                        <div>
                            <h2 class="text-sm font-semibold text-gray-700 mb-1 ml-3">New Bookings</h2>
                            <div class="overflow-x-auto rounded-b-2xl">
                                <table id="newBookingsTable" class="min-w-full divide-y divide-gray-100">
                                    <thead class="bg-gray-100">
                                    <tr>
                                        <th class="px-6 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Booking Number</th>
                                        <th class="px-6 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Booking DateTime</th>
                                        <th class="px-6 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                                        <th class="px-6 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Customer Name</th>
                                    </tr>
                                    </thead>
                                    <tbody class="bg-white divide-y divide-gray-200">
                                    <!-- New bookings will be inserted here -->
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Right Column -->
                <div class="col-span-1 flex flex-col gap-6">
                    <!-- Cab Types Chart -->
                    <div class="bg-gray-200 rounded-2xl shadow-md">
                        <h2 class="text-sm font-semibold text-gray-700 mb-1 ml-3">Top Cab Types</h2>
                        <div class="h-64 p-2 bg-white flex justify-center rounded-b-2xl">
                            <canvas id="topCabChart"></canvas>
                        </div>
                    </div>

                    <!-- Map Container -->
                    <div class="bg-white rounded-2xl shadow-md flex-1 bg-gray-400">
                        <div class="h-full bg-gray-200 rounded-lg">
                            <h2 class="text-sm font-semibold text-gray-700 mb-1 ml-3">Next Trip</h2>
                            <div id="map" class="h-80 rounded-lg overflow-hidden"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
