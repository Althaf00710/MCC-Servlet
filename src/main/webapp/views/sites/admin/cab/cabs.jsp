<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <script src="${pageContext.request.contextPath}/views/static/js/cabAssignFunctions.js"></script>
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
            <button onclick="openCModal()" class="bg-orange-500 hover:bg-orange-600 text-white px-4 py-2 rounded-3xl flex items-center transition-colors duration-200 whitespace-nowrap">
                <i class="fi fi-rr-plus mr-2"></i>
                Add Cabs
            </button>
        </div>

        <!-- Table Section for Cabs -->
        <div class="overflow-x-auto bg-white rounded-lg shadow-md">
            <table class="min-w-full divide-y divide-gray-200">
                <thead class="bg-gray-200">
                    <tr>
                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Driver</th>
                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Register No</th>
                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Plate No</th>
                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Cab Type</th>
                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Last Service</th>
                        <th class="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                    </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                <!-- Loop through the list of cabs -->
                <c:forEach var="cab" items="${cabs}">
                    <tr>
                        <td class="px-4 py-4 whitespace-nowrap text-sm text-gray-700">${cab.cabBrandName} ${cab.cabName}</td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500 assignedDriverCell" id="cabRow_${cab.id}" data-cab-id="${cab.id}">
                            <!-- JavaScript will insert either the assigned driver or dropdown -->
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${cab.registrationNumber}</td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${cab.plateNumber}</td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${cab.cabTypeName}</td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${cab.status}</td>
                        <fmt:formatDate value="${cab.lastService}" pattern="yyyy-MM-dd" var="Date"/>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${Date}</td>
                        <td class="px-4 py-4 whitespace-nowrap text-sm text-gray-500">
                            <div class="flex items-center space-x-1">
                                <button class="text-orange-600 hover:text-orange-900 px-3 py-1 rounded-3xl hover:bg-orange-200 flex items-center" onclick="openEditModal(${cab.id})">
                                    <i class="fi fi-rr-pencil mr-2"></i>
                                    Edit
                                </button>
                                <button class="text-red-600 hover:text-red-900 px-3 py-1 rounded-3xl hover:bg-red-200 flex items-center" onclick="confirmDelete()">
                                    <i class="fi fi-rr-trash mr-2"></i>
                                    Delete
                                </button>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Modal for Creating Cab -->
    <div id="Modal" class="fixed inset-0 bg-opacity-50 h-full w-full
                               bg-gray-200/32 border border-gray-50/48 rounded-2xl
                               shadow-lg backdrop-blur-[9px] flex justify-center items-center hidden">
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

    <!-- Edit Cab Modal -->
    <div id="editModal" class="fixed inset-0 bg-opacity-50 h-full w-full
                               bg-gray-200/32 border border-gray-50/48 rounded-2xl
                               shadow-lg backdrop-blur-[9px] flex justify-center items-center hidden">
        <div class="bg-white p-6 rounded-lg shadow-lg max-w-lg w-full">
            <h2 class="text-2xl font-semibold text-gray-800 mb-4">Edit Cab</h2>

            <form id="editCabForm" action="${pageContext.request.contextPath}/cabs/update" method="post">
                <input type="hidden" id="editCabId" name="id">

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
                    <label for="editCabName" class="block text-gray-700">Cab Name</label>
                    <input type="text" id="editCabName" name="cabName" class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500" required>
                </div>

                <div class="mb-4">
                    <label for="editRegistrationNumber" class="block text-gray-700">Registration Number</label>
                    <input type="text" id="editRegistrationNumber" name="registrationNumber" class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500" required>
                </div>

                <div class="mb-4">
                    <label for="editPlateNumber" class="block text-gray-700">Plate Number</label>
                    <input type="text" id="editPlateNumber" name="plateNumber" class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500" required>
                </div>

                <div class="flex justify-end">
                    <button type="button" onclick="closeEditModal()" class="bg-gray-400 text-white px-4 py-2 rounded-lg mr-2">Cancel</button>
                    <button type="submit" class="bg-orange-500 text-white px-6 py-2 rounded-lg">Update</button>
                </div>
            </form>
        </div>
    </div>

</body>

</html>
