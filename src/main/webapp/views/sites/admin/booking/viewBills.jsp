<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: Altha
  Date: 3/7/2025
  Time: 10:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>MCC | Bills</title>
    <script src="https://unpkg.com/@tailwindcss/browser@4"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/mouseAnimation.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/scrollBar.css">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/2.6.0/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="${pageContext.request.contextPath}/views/static/js/BillingsAdminFunctions.js"></script>
</head>
<body>
    <%@ include file="../common/navBar.jsp" %>
    <div class="mouse-circle"></div>
    <div class="ml-64 flex-1 p-8 pt-4">
        <!-- Header Section -->
        <div class="flex justify-between items-center mb-6 gap-4">
            <h1 class="text-2xl font-medium text-gray-600 whitespace-nowrap">Bills</h1>

            <!-- Search Bar -->
            <div class="flex-1 max-w-md relative">
                <div class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-400 z-3">
                    <i class="fi fi-rr-search text-lg"></i>
                </div>
                <input type="text"
                       id="searchInput"
                       name="searchTerm"
                       placeholder="Enter Booking No..."
                       class="w-full px-4 py-2 rounded-3xl border border-gray-300 focus:outline-none focus:ring-2 focus:ring-orange-500 pl-10 pr-16 transition-colors duration-200 hover:border-gray-400 bg-white">
            </div>

            <!-- Add Brand Button -->
            <a href="" class="bg-orange-500 hover:bg-orange-600 text-white px-4 py-2 rounded-3xl flex items-center transition-colors duration-200 whitespace-nowrap">
                <i class="fi fi-rr-plus mr-2"></i>
                Add Bills
            </a>
        </div>

        <div class="bg-white rounded-lg shadow overflow-hidden relative">
            <table class="min-w-full divide-y divide-gray-200" id="billsTable">
                <thead class="bg-gray-200">
                <tr>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Booking No</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Bill Date</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Customer</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Cab Type</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Total Distance</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Total Wait Time</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Total Amount</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Action</th>
                </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                <c:forEach items="${bills}" var="bill">
                    <tr>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${bill.bookingNo}</td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${bill.billDate}</td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                            <div class="font-medium text-gray-600">${bill.customerName}</div>
                            <div class="text-gray-400 font-light text-xs">${bill.customerRegisterNo}</div>
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500 uppercase">${bill.cabType}</td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${bill.totalDistance} km</td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${bill.totalWaitTime} mins</td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">Rs. ${bill.totalAmount}</td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                            <form method="post" action="${pageContext.request.contextPath}/billing/delete">
                                <input type="hidden" name="bookingId" value="${bill.bookingId}">
                                <input type="hidden" name="billingId" value="${bill.billingId}">
                                <button type="button" onclick="confirmDelete(${bill.billingId}, ${bill.bookingId})" class="text-red-500 hover:text-red-600 hover:cursor-pointer py-2 px-4"> <i class="fi fi-rr-trash"></i></button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</body>

<script>
    document.getElementById('searchInput').addEventListener('keyup', function() {
        var searchTerm = this.value.toLowerCase();
        var rows = document.querySelectorAll('#billsTable tbody tr');

        rows.forEach(function(row) {
            var bookingNo = row.cells[0].textContent.toLowerCase();

            if (bookingNo.includes(searchTerm)) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    });
</script>
</html>
