<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/scrollBar.css">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/2.1.0/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <script src="${pageContext.request.contextPath}/views/static/js/mouseAnimation.js"></script>
    <script src="${pageContext.request.contextPath}/views/static/js/confirmDelete.js"></script>
    <script src="${pageContext.request.contextPath}/views/static/js/modalFunction.js"></script>
    <script src="${pageContext.request.contextPath}/views/sites/admin/customer/editModal.js"></script>
</head>
<body class="bg-gray-50">
<%@ include file="../common/navBar.jsp" %>
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
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Reg No</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Contact No</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">NIC</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Email</th>
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
                                        ${customer.name}
                                </div>
                                <div class="text-sm text-gray-500">Since
                                    <fmt:parseDate value="${customer.joinedDate}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDateTime" type="both" />
                                    <jsp:useBean id="now" class="java.util.Date" />
                                    <fmt:formatDate pattern="dd MMM yyyy" value="${parsedDateTime}" />
                                </div>
                            </div>
                        </div>
                    </td>

                    <!-- Register Number -->
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        ${customer.registerNumber}
                    </td>

                    <!-- Contact Number -->
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        <a href="tel:${customer.countryCode}${customer.phoneNumber}" class="hover:text-gray-800">
                                ${customer.countryCode}${customer.phoneNumber}
                        </a>
                    </td>

                    <!-- NIC -->
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        ${customer.nicNumber}
                    </td>

                    <!-- Email -->
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        <a href="mailto:${customer.email}" class="hover:text-gray-800">
                                ${customer.email}
                        </a>
                    </td>

                    <!-- Actions -->
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                        <div class="flex items-center space-x-3">
                            <a href="#" class="text-orange-600 hover:text-orange-900 px-3 py-1 rounded-3xl hover:bg-orange-200 flex items-center" onclick="openEditDriverModal(${customer.id})">
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

<!-- Add Customer Modal -->
<div id="Modal" class="hidden fixed inset-0 flex items-center justify-center bg-opacity-50 h-full w-full
                               bg-gray-200/32 border border-gray-50/48 rounded-2xl
                               shadow-lg backdrop-blur-[9px]">
    <div class="modal-animation relative p-5 border w-1/3 shadow-lg rounded-2xl bg-white">
        <!-- Modal Header -->
        <div class="flex justify-between items-center mb-4">
            <h3 class="text-lg font-semibold text-gray-700">Add New Customer</h3>
            <button onclick="closeModal()" class="text-gray-400 hover:text-gray-600">
                <i class="fi fi-rr-cross-small text-xl"></i>
            </button>
        </div>

        <!-- Add Customer Form -->
        <form action="${pageContext.request.contextPath}/customers/add" method="post">
            <div class="space-y-4">
                <!-- Customer Name -->
                <div class="relative">
                    <label class="block text-sm font-medium text-gray-700">Full Name</label>
                    <input type="text" name="name" required
                           class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
                </div>

                <!-- Address -->
                <div class="relative">
                    <label class="block text-sm font-medium text-gray-700">Address</label>
                    <input type="text" name="address" required
                           class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
                </div>

                <!-- Contact Information -->
                <!-- Phone -->
                <div class="relative">
                    <label class="block text-sm font-medium text-gray-700">Phone</label>
                    <div class="flex mt-1 rounded-lg border border-gray-300 focus-within:ring-2 focus-within:ring-orange-500 focus-within:border-orange-500">

                        <!-- Country Code Dropdown -->
                        <select name="countryCode" class="bg-gray-100 text-gray-700 border-r border-gray-300 px-3 py-2 rounded-l-lg focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
                            <option value="+94" selected>🇱🇰 +94</option>
                            <option value="+1">🇺🇸 +1</option>
                            <option value="+44">🇬🇧 +44</option>
                            <option value="+91">🇮🇳 +91</option>
                            <option value="+61">🇦🇺 +61</option>
                            <option value="+971">🇦🇪 +971</option>
                            <option value="+33">🇫🇷 +33</option>
                            <option value="+49">🇩🇪 +49</option>
                        </select>

                        <!-- Input Field -->
                        <div class="relative flex-1">
                            <i class="fi fi-rr-phone-call absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
                            <input type="tel" name="phoneNumber" placeholder="Enter phone number"
                                   class="block w-full px-3 py-2 pl-10 rounded-r-lg focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
                        </div>
                    </div>
                </div>

                <div class="relative">
                    <label class="block text-sm font-medium text-gray-700">Email</label>
                    <input type="email" name="email" required
                           class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
                </div>

                <!-- NIC Number -->
                <div class="relative">
                    <label class="block text-sm font-medium text-gray-700">NIC Number</label>
                    <input type="text" name="nicNumber" required
                           class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
                </div>

                <!-- Submit Button -->
                <div class="pt-4">
                    <button type="submit"
                            class="w-full bg-orange-500 hover:bg-orange-600 text-white font-medium py-2 px-4 rounded-lg transition-colors duration-200">
                        Add Customer
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>

<!-- Edit Customer Modal -->
<div id="EditCustomerModal" class="hidden fixed inset-0 flex items-center justify-center bg-opacity-50 h-full w-full
                               bg-gray-200/32 border border-gray-50/48 rounded-2xl
                               shadow-lg backdrop-blur-[9px]">
    <div class="modal-animation relative p-5 border w-1/3 shadow-lg rounded-2xl bg-white">
        <!-- Modal Header -->
        <div class="flex justify-between items-center mb-4">
            <h3 class="text-lg font-semibold text-gray-700">Edit Customer</h3>
            <button onclick="closeModal()" class="text-gray-400 hover:text-gray-600">
                <i class="fi fi-rr-cross-small text-xl"></i>
            </button>
        </div>

        <!-- Edit Customer Form -->
        <form action="${pageContext.request.contextPath}/customers/update" method="post">
            <div class="space-y-4">
                <input type="hidden" name="id">
                <!-- Customer Name -->
                <div class="relative">
                    <label class="block text-sm font-medium text-gray-700">Full Name</label>
                    <input type="text" name="name" required
                           class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
                </div>

                <!-- Address -->
                <div class="relative">
                    <label class="block text-sm font-medium text-gray-700">Address</label>
                    <input type="text" name="address" required
                           class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
                </div>

                <!-- Phone -->
                <div class="relative">
                    <label class="block text-sm font-medium text-gray-700">Phone</label>
                    <div class="flex mt-1 rounded-lg border border-gray-300 focus-within:ring-2 focus-within:ring-orange-500 focus-within:border-orange-500">

                        <!-- Country Code Dropdown -->
                        <select name="countryCode" class="bg-gray-100 text-gray-700 border-r border-gray-300 px-3 py-2 rounded-l-lg focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
                            <option value="+94" ${user.countryCode == "+94" ? 'selected' : ''}>🇱🇰 +94</option>
                            <option value="+1" ${user.countryCode == "+1" ? 'selected' : ''}>🇺🇸 +1</option>
                            <option value="+44" ${user.countryCode == "+44" ? 'selected' : ''}>🇬🇧 +44</option>
                            <option value="+91" ${user.countryCode == "+91" ? 'selected' : ''}>🇮🇳 +91</option>
                            <option value="+61" ${user.countryCode == "+61" ? 'selected' : ''}>🇦🇺 +61</option>
                            <option value="+971" ${user.countryCode == "+971" ? 'selected' : ''}>🇦🇪 +971</option>
                            <option value="+33" ${user.countryCode == "+33" ? 'selected' : ''}>🇫🇷 +33</option>
                            <option value="+49" ${user.countryCode == "+49" ? 'selected' : ''}>🇩🇪 +49</option>
                        </select>

                        <!-- Input Field -->
                        <div class="relative flex-1">
                            <i class="fi fi-rr-phone-call absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
                            <input type="tel" name="phoneNumber" placeholder="Enter phone number"
                                   class="block w-full px-3 py-2 pl-10 rounded-r-lg focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
                        </div>
                    </div>
                </div>

                <div class="relative">
                    <label class="block text-sm font-medium text-gray-700">Email</label>
                    <input type="email" name="email" required
                           class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
                </div>

                <!-- NIC Number -->
                <div class="relative">
                    <label class="block text-sm font-medium text-gray-700">NIC Number</label>
                    <input type="text" name="nicNumber" required
                           class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
                </div>

                <!-- Submit Button -->
                <div class="pt-4">
                    <button type="submit"
                            class="w-full bg-orange-500 hover:bg-orange-600 text-white font-medium py-2 px-4 rounded-lg transition-colors duration-200">
                        Edit Customer
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>



</body>
</html>
