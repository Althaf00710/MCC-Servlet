<%@ page import="com.example.megacitycab.models.Cab.CabBrand" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Altha
  Date: 2/27/2025
  Time: 12:04 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>MCC | Cab Brands</title>
  <script src="https://unpkg.com/@tailwindcss/browser@4"></script>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/mouseAnimation.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/scrollBar.css">
  <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/2.1.0/uicons-regular-rounded/css/uicons-regular-rounded.css">
  <script DEFER src="${pageContext.request.contextPath}/views/static/js/mouseAnimation.js"></script>
  <script src="${pageContext.request.contextPath}/views/static/js/confirmDelete.js"></script>
  <script src="${pageContext.request.contextPath}/views/static/js/modalFunction.js"></script>
  <script src="${pageContext.request.contextPath}/views/static/js/cabBrandEditModal.js"></script>
</head>
<body>
  <%@ include file="../common/navBar.jsp" %>
  <div class="mouse-circle"></div>
  <div class="ml-64 flex-1 p-8 pt-4">
    <!-- Header Section -->
    <div class="flex justify-between items-center mb-6 gap-4">
      <h1 class="text-2xl font-medium text-gray-600 whitespace-nowrap">Cab Brands</h1>

      <!-- Search Bar -->
      <div class="flex-1 max-w-md relative">
        <div class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-400 z-3">
          <i class="fi fi-rr-search text-lg"></i>
        </div>
          <input type="text"
                 id="searchInput"
                 name="searchTerm"
                 placeholder="Search Brands..."
                 class="w-full px-4 py-2 rounded-3xl border border-gray-300 focus:outline-none focus:ring-2 focus:ring-orange-500 pl-10 pr-16 transition-colors duration-200 hover:border-gray-400 bg-white">
      </div>

      <!-- Add Brand Button -->
      <button onclick="openModal()" class="bg-orange-500 hover:bg-orange-600 text-white px-4 py-2 rounded-3xl flex items-center transition-colors duration-200 whitespace-nowrap">
        <i class="fi fi-rr-plus mr-2"></i>
        Add Brand
      </button>
    </div>

    <!-- Cab Brands Table -->
    <div class="bg-white rounded-lg shadow overflow-hidden relative">
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-200">
        <tr>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Brand Name</th>
          <th class="px-6 py-3 text-xs font-medium text-gray-500 uppercase tracking-wider text-center">Actions</th>
        </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
        <%
          List<CabBrand> cabBrands = (List<CabBrand>) request.getAttribute("cabBrand");
          if (cabBrands != null && !cabBrands.isEmpty()) {
            for (CabBrand brand : cabBrands) {
        %>
        <tr class="border border-gray-300">
          <td class="px-6 py-4 whitespace-nowrap"><%= brand.getBrandName() %></td>
          <td class="px-6 py-4 whitespace-nowrap text-center">
            <button onclick="openEditModal(<%= brand.getId() %>)" class="px-3 py-1 text-sm bg-blue-100 hover:bg-blue-200 text-blue-600 rounded-lg">
              <i class="fi fi-rr-edit mr-2"></i>Edit
            </button>
            <button onclick="DeleteBrand(<%= brand.getId() %>)" class="px-3 py-1 text-sm bg-red-100 hover:bg-red-200 text-red-600 rounded-lg">
              <i class="fi fi-rr-trash mr-2"></i>Delete
            </button>
          </td>
        </tr>
        <%
          }
        } else {
        %>
        <tr>
          <td colspan="3" class="text-center py-4 text-gray-500">No cab brands available.</td>
        </tr>
        <% } %>
        </tbody>
      </table>
    </div>
  </div>

  <!-- Add Brand Modal -->
  <div id="Modal" class="hidden fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white p-6 rounded-lg shadow-lg w-96">
      <h2 class="text-xl font-semibold mb-4 text-gray-700">Add Cab Brand</h2>

      <form action="${pageContext.request.contextPath}/cabbrand/add" method="post">
        <label for="brandName" class="block text-gray-600 text-sm font-medium mb-2">Brand Name</label>
        <input type="text" id="brandName" name="brandName" required
               class="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500">

        <div class="flex justify-end mt-4 gap-2">
          <button type="button" onclick="closeModal()"
                  class="px-4 py-2 bg-gray-300 hover:bg-gray-400 text-gray-700 rounded-lg">
            Cancel
          </button>
          <button type="submit"
                  class="px-4 py-2 bg-orange-500 hover:bg-orange-600 text-white rounded-lg">
            Add Brand
          </button>
        </div>
      </form>
    </div>
  </div>

  <!-- Edit Brand Modal -->
  <div id="EditModal" class="hidden fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white p-6 rounded-lg shadow-lg w-96">
      <h2 class="text-xl font-semibold mb-4 text-gray-700">Edit Cab Brand</h2>

      <form action="${pageContext.request.contextPath}/cabbrand/update" method="post">
        <input type="hidden" id="editBrandId" name="id">
        <label for="editBrandName" class="block text-gray-600 text-sm font-medium mb-2">Brand Name</label>
        <input type="text" id="editBrandName" name="brandName" required
               class="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500">

        <div class="flex justify-end mt-4 gap-2">
          <button type="button" onclick="closeEditModal()"
                  class="px-4 py-2 bg-gray-300 hover:bg-gray-400 text-gray-700 rounded-lg">
            Cancel
          </button>
          <button type="submit"
                  class="px-4 py-2 bg-orange-500 hover:bg-orange-600 text-white rounded-lg">
            Save Changes
          </button>
        </div>
      </form>
    </div>
  </div>

</body>
</html>
