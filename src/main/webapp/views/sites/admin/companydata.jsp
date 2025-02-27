<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.megacitycab.models.CompanyData" %>
<%@ page import="java.util.Optional" %>
<%
    CompanyData companyData = (CompanyData) request.getAttribute("companyData");
%>
<html>
<head>
    <title>MCC | Company Settings</title>
    <script src="https://unpkg.com/@tailwindcss/browser@4"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/mouseAnimation.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/scrollBar.css">
    <script defer src="${pageContext.request.contextPath}/views/static/js/mouseAnimation.js"></script>
</head>
<body>

<%@ include file="common/navBar.jsp" %>

<div class="ml-64 flex-1 p-8 pt-4">
    <h1 class="text-2xl font-medium text-gray-600 mb-6">Company Settings</h1>

    <%-- Display success or error messages --%>
    <% if (request.getAttribute("message") != null) { %>
    <div class="bg-green-100 text-green-700 p-4 rounded-md mb-4">
        <%= request.getAttribute("message") %>
    </div>
    <% } %>
    <% if (request.getAttribute("error") != null) { %>
    <div class="bg-red-100 text-red-700 p-4 rounded-md mb-4">
        <%= request.getAttribute("error") %>
    </div>
    <% } %>

    <form action="${pageContext.request.contextPath}/companydata/update" method="post" class="bg-white p-6 rounded-lg shadow-md max-w-lg">

        <div class="mb-4">
            <label for="address" class="block text-gray-700">Address</label>
            <input type="text" id="address" name="address" value="<%= companyData != null ? companyData.getAddress() : "" %>"
                   class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500" required>
        </div>

        <div class="mb-4">
            <label for="phoneNumber" class="block text-gray-700">Phone Number</label>
            <input type="text" id="phoneNumber" name="phoneNumber" value="<%= companyData != null ? companyData.getPhoneNumber() : "" %>"
                   class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500" required>
        </div>

        <div class="mb-4">
            <label for="email" class="block text-gray-700">Email</label>
            <input type="email" id="email" name="email" value="<%= companyData != null ? companyData.getEmail() : "" %>"
                   class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500" required>
        </div>

        <div class="mb-4">
            <label for="tax" class="block text-gray-700">Tax (%)</label>
            <input type="number" step="0.01" id="tax" name="tax" value="<%= companyData != null ? companyData.getTax() : "" %>"
                   class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500" required>
        </div>

        <div class="mb-4">
            <label for="discount" class="block text-gray-700">Discount (%)</label>
            <input type="number" step="0.01" id="discount" name="discount" value="<%= companyData != null ? companyData.getDiscount() : "" %>"
                   class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500" required>
        </div>

        <div class="mb-4">
            <label for="minAmountForDiscount" class="block text-gray-700">Min Amount for Discount</label>
            <input type="number" step="0.01" id="minAmountForDiscount" name="minAmountForDiscount"
                   value="<%= companyData != null ? companyData.getMinAmountForDiscount() : "" %>"
                   class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500" required>
        </div>

        <div class="flex justify-end">
            <button type="submit" class="bg-orange-500 text-white px-6 py-2 rounded-lg">
                Update Company Data
            </button>
        </div>
    </form>
</div>

</body>
</html>
