<%--
  Created by IntelliJ IDEA.
  User: Altha
  Date: 2/14/2025
  Time: 3:36 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>MCC | Customers</title>
    <script src="https://unpkg.com/@tailwindcss/browser@4"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/mouseAnimation.css">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/2.1.0/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <script src="${pageContext.request.contextPath}/views/static/js/mouseAnimation.js"></script>
    <script src="${pageContext.request.contextPath}/views/static/js/confirmDelete.js"></script>
    <script src="${pageContext.request.contextPath}/views/static/js/modalFunction.js"></script>
</head>
<body class="bg-gray-50">
<%@ include file="common/navBar.jsp" %>
<div class="mouse-circle"></div>
<div class="ml-64 flex-1 p-8 pt-4">
    <!-- Header Section -->
    <div class="flex justify-between items-center mb-6 gap-4">
        <h1 class="text-2xl font-medium text-gray-600 whitespace-nowrap">Customers</h1>

        <!-- Search Bar -->
        <div class="flex-1 max-w-md relative">
            <div class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-400 z-3">
                <i class="fi fi-rr-search text-lg"></i>
            </div>
            <form action="${pageContext.request.contextPath}/customers/search" method="GET" class="relative">
                <input type="text"
                       id="searchInput"
                       name="searchTerm"
                       placeholder="Search customers..."
                       class="w-full px-4 py-2 rounded-3xl border border-gray-300 focus:outline-none focus:ring-2 focus:ring-orange-500 pl-10 pr-16 transition-colors duration-200 hover:border-gray-400 bg-white">
                <button type="submit"
                        class="absolute right-2 top-1/2 transform -translate-y-1/2 px-8 py-1 rounded-3xl bg-gray-100 text-orange-600 font-semibold hover:bg-gray-200 hover:text-orange-400 transition-colors duration-350 z-3 cursor-pointer">
                    GO
                </button>
            </form>
        </div>

        <!-- Add Customer Button -->
        <button onclick="openModal()" class="bg-orange-500 hover:bg-orange-600 text-white px-4 py-2 rounded-3xl flex items-center transition-colors duration-200 whitespace-nowrap">
            <i class="fi fi-rr-plus mr-2"></i>
            Add Customer
        </button>
    </div>

    <!-- Customers Table -->
    <div class="bg-white rounded-lg shadow overflow-hidden relative">
        <table class="min-w-full divide-y divide-gray-200" id="customersTable">
            <thead class="bg-gray-200">
            <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Customer</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Contact Number</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Email</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    <div class="flex items-center group relative cursor-pointer">
                        Status
                        <i class="fi fi-rr-angle-small-down ml-1 text-gray-400 group-hover:text-gray-600 transition-colors"></i>
                        <div class="hidden group-hover:block absolute top-full left-0 mt-1 w-40 bg-white border border-gray-200 rounded-lg shadow-lg z-10">
                            <div class="p-2 space-y-2">
                                <div class="flex items-center px-2 py-1 rounded-md hover:bg-gray-100 cursor-pointer filter-option" data-filter="active">
                                    <span class="w-2 h-2 rounded-full bg-green-500 mr-2"></span>
                                    Active
                                </div>
                                <div class="flex items-center px-2 py-1 rounded-md hover:bg-gray-100 cursor-pointer filter-option" data-filter="inactive">
                                    <span class="w-2 h-2 rounded-full bg-red-500 mr-2"></span>
                                    Inactive
                                </div>
                            </div>
                        </div>
                    </div>
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
            </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
            <c:forEach items="${customers}" var="customer">
                <tr>
                    <!-- Customer Info -->
                    <td class="px-6 py-4 whitespace-nowrap">
                        <div class="flex items-center">
                            <div class="ml-4">
                                <div class="text-sm font-medium text-gray-900">
                                        ${customer.firstName} ${customer.lastName}
                                </div>
                                <div class="text-sm text-gray-500">Member since
                                    <fmt:formatDate value="${customer.joinDate}" pattern="MMM yyyy"/>
                                </div>
                            </div>
                        </div>
                    </td>

                    <!-- Contact Number -->
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        <a href="tel:${customer.phoneNumber}" class="hover:text-gray-800">
                                ${customer.phoneNumber}
                        </a>
                    </td>

                    <!-- Email -->
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        <a href="mailto:${customer.email}" class="hover:text-gray-800">
                                ${customer.email}
                        </a>
                    </td>

                    <!-- Status -->
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500" data-status="${customer.status}">
                        <div class="flex items-center">
                            <c:choose>
                                <c:when test="${customer.status == 'ACTIVE'}">
                                    <span class="inline-block h-2 w-2 rounded-full bg-green-500 mr-2"></span>
                                    Active
                                </c:when>
                                <c:otherwise>
                                    <span class="inline-block h-2 w-2 rounded-full bg-red-500 mr-2"></span>
                                    Inactive
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </td>

                    <!-- Actions -->
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                        <div class="flex items-center space-x-3">
                            <a href="#" class="text-orange-600 hover:text-orange-900 px-3 py-1 rounded-3xl hover:bg-orange-200 flex items-center" onclick="openEditCustomerModal(${customer.id})">
                                <i class="fi fi-rr-pencil mr-2"></i>
                                Edit
                            </a>
                            <form action="${pageContext.request.contextPath}/customers/delete" method="post" class="inline">
                                <input type="hidden" name="id" value="${customer.id}">
                                <button type="submit" class="text-red-600 hover:text-red-900 px-3 py-1 rounded-3xl hover:bg-red-200 flex items-center">
                                    <i class="fi fi-rr-trash mr-2"></i>
                                    Delete
                                </button>
                            </form>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>
