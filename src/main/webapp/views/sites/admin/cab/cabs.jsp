<%--
  Created by IntelliJ IDEA.
  User: Altha
  Date: 2/27/2025
  Time: 12:56 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>MCC | Cabs</title>
    <script src="https://unpkg.com/@tailwindcss/browser@4"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/mouseAnimation.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/scrollBar.css">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/2.1.0/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <script DEFER src="${pageContext.request.contextPath}/views/static/js/mouseAnimation.js"></script>
    <script src="${pageContext.request.contextPath}/views/static/js/confirmDelete.js"></script>
    <script src="${pageContext.request.contextPath}/views/static/js/modalFunction.js"></script>
    <script src="${pageContext.request.contextPath}/views/static/js/cabFunctions.js"></script>
</head>
<body>
    <%@ include file="../common/navBar.jsp" %>
    <div class="mouse-circle"></div>
    <div class="ml-64 flex-1 p-8 pt-4">
        <!-- Header Section -->
        <div class="flex justify-between items-center mb-6 gap-4">
            <h1 class="text-2xl font-medium text-gray-600 whitespace-nowrap">Cabs</h1>

            <!-- Search Bar -->
            <div class="flex-1 max-w-md relative">
                <div class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-400 z-3">
                    <i class="fi fi-rr-search text-lg"></i>
                </div>
                <input type="text"
                       id="searchInput"
                       name="searchTerm"
                       placeholder="Search Cabs..."
                       class="w-full px-4 py-2 rounded-3xl border border-gray-300 focus:outline-none focus:ring-2 focus:ring-orange-500 pl-10 pr-16 transition-colors duration-200 hover:border-gray-400 bg-white">
            </div>

            <!-- Add Brand Button -->
            <button onclick="openModal()" class="bg-orange-500 hover:bg-orange-600 text-white px-4 py-2 rounded-3xl flex items-center transition-colors duration-200 whitespace-nowrap">
                <i class="fi fi-rr-plus mr-2"></i>
                Add Cabs
            </button>
        </div>

        <!-- Table Section for Cabs -->
        <div class="overflow-x-auto bg-white rounded-lg shadow-md">
            <table class="min-w-full table-auto">
                <thead>
                <tr>
                    <th class="px-4 py-2 text-left text-gray-600">Name</th>
                    <th class="px-4 py-2 text-left text-gray-600">Register No</th>
                    <th class="px-4 py-2 text-left text-gray-600">Plate No</th>
                    <th class="px-4 py-2 text-left text-gray-600">Cab Type</th>
                    <th class="px-4 py-2 text-left text-gray-600">Status</th>
                    <th class="px-4 py-2 text-left text-gray-600">Last Service</th>
                    <th class="px-4 py-2 text-left text-gray-600">Actions</th>
                </tr>
                </thead>
                <tbody>
                <!-- Loop through the list of cabs -->
                <c:forEach var="cab" items="${cabs}">
                    <tr>
                        <td class="px-4 py-2">${cab.cabBrandName} ${cab.cabName}</td>
                        <td class="px-4 py-2">${cab.registrationNumber}</td>
                        <td class="px-4 py-2">${cab.plateNumber}</td>
                        <td class="px-4 py-2">${cab.cabTypeName}</td>
                        <td class="px-4 py-2">${cab.status}</td>
                        <td class="px-4 py-2">${cab.lastService}</td>
                        <td class="px-4 py-2">
                            <button class="bg-blue-500 text-white px-4 py-1 rounded-lg">Edit</button>
                            <button class="bg-red-500 text-white px-4 py-1 rounded-lg" onclick="confirmDelete()">Delete</button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Modal for Creating Cab -->
    <div id="Modal" class="fixed inset-0 bg-gray-800 bg-opacity-50 flex justify-center items-center hidden">
        <div class="bg-white p-6 rounded-lg shadow-lg max-w-lg w-full">
            <h2 class="text-2xl font-semibold text-gray-800 mb-4">Create New Cab</h2>

            <form id="createCabForm" action="${pageContext.request.contextPath}/cabs/add" method="post">
                <div class="mb-4">
                    <label for="cabBrandId" class="block text-gray-700">Cab Brand</label>
                    <select id="cabBrandId" name="cabBrandId" class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500">
                        <option value="">Select Brand</option>
                    </select>
                </div>

                <div class="mb-4">
                    <label for="cabTypeId" class="block text-gray-700">Cab Type</label>
                    <select id="cabTypeId" name="cabTypeId" class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500">
                        <option value="">Select Type</option>
                    </select>
                </div>

                <div class="mb-4">
                    <label for="cabName" class="block text-gray-700">Cab Name</label>
                    <input type="text" id="cabName" name="cabName" placeholder="Enter Cab Name" class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500" required>
                </div>

                <div class="mb-4">
                    <label for="registrationNumber" class="block text-gray-700">Registration Number</label>
                    <input type="text" id="registrationNumber" name="registrationNumber" placeholder="Enter Registration Number" class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500" required>
                </div>

                <div class="mb-4">
                    <label for="plateNumber" class="block text-gray-700">Plate Number</label>
                    <input type="text" id="plateNumber" name="plateNumber" placeholder="Enter Plate Number" class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500" required>
                </div>

                <div class="flex justify-end">
                    <button type="button" onclick="closeModal()" class="bg-gray-400 text-white px-4 py-2 rounded-lg mr-2">Cancel</button>
                    <button type="submit" class="bg-orange-500 text-white px-6 py-2 rounded-lg">Create</button>
                </div>
            </form>
        </div>
    </div>

</body>
</html>
