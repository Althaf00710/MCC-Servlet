<%--
  Created by IntelliJ IDEA.
  User: Altha
  Date: 2/4/2025
  Time: 1:30 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>MCC | User</title>
  <script src="https://unpkg.com/@tailwindcss/browser@4"></script>
  <link rel="stylesheet" href="../../static/css/mouseAnimation.css">
  <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/2.1.0/uicons-regular-rounded/css/uicons-regular-rounded.css">
  <script src="../../static/js/userSearch.js"></script>
  <link rel="stylesheet" href="../../static/css/avatarInitials.css">
  <link rel="stylesheet" href="../../static/css/modalAnimation.css">
</head>
<body class="bg-gray-50">
  <%@ include file="../../common/navBar.jsp" %>
  <div class="mouse-circle"></div>
  <div class="ml-64 flex-1 p-8 pt-4">
    <!-- Header Section -->
    <div class="flex justify-between items-center mb-6 gap-4">
      <!-- Left Title -->
      <h1 class="text-2xl font-medium text-gray-600 whitespace-nowrap">Users</h1>

      <!-- Search Bar -->
      <div class="flex-1 max-w-md relative">
        <div class="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none text-gray-400">
          <i class="fi fi-rr-search text-lg"></i>
        </div>
        <input type="text"
               id="searchInput"
               onkeyup="filterUsers()"
               placeholder="Search users..."
               class="w-full px-4 py-2 rounded-3xl border border-gray-300 focus:outline-none focus:ring-2 focus:ring-orange-500 pl-10 transition-colors duration-200 hover:border-gray-400 bg-white">
      </div>

      <!-- Right Add Button -->
      <a href="javascript:void(0)" onclick="openModal()" class="bg-orange-500 hover:bg-orange-600 text-white px-4 py-2 rounded-3xl flex items-center transition-colors duration-200 whitespace-nowrap">
        <i class="fi fi-rr-plus mr-2"></i>
        Add User
      </a>
    </div>

    <!-- Users Table -->
    <div class="bg-white rounded-lg shadow overflow-hidden">
      <table class="min-w-full divide-y divide-gray-200" id="usersTable">
        <thead class="bg-gray-200">
        <tr>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Username</th>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Email</th>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Phone</th>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
        </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
        <c:forEach items="${users}" var="user">
          <tr>
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="flex items-center">
                <div class="flex-shrink-0 h-10 w-10">
                  <img class="h-10 w-10 rounded-full"
                       src="${not empty user.avatarUrl ? user.avatarUrl : pageContext.request.contextPath}/views/static/images/defaultAvatar.png"
                       alt="User avatar">
                </div>
                <div class="ml-4">
                  <div class="text-sm font-medium text-gray-900">
                      ${user.firstName} ${user.lastName}
                  </div>
                </div>
              </div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${user.username}</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
              <a href="mailto:${user.email}" class="hover:text-gray-800">
                  ${user.email}
              </a>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
              <a href="tel:${user.phoneNumber}" class="hover:text-gray-800">
                  ${user.phoneNumber}
              </a>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
              <div class="flex items-center space-x-3">
                <a href="#" class="text-orange-600 hover:text-orange-900 px-3 py-1 rounded-3xl hover:bg-orange-200 flex items-center">
                  <i class="fi fi-rr-pencil mr-2"></i>
                  Edit
                </a>
                <a href="#" class="text-red-600 hover:text-red-900 px-3 py-1 rounded-3xl hover:bg-red-200 flex items-center">
                  <i class="fi fi-rr-trash mr-2"></i>
                  Delete
                </a>
              </div>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
  </div>





  <!-- Add User Modal -->
  <div id="Modal" class="hidden fixed inset-0 bg-opacity-50 overflow-y-auto h-full w-full
                                 bg-gray-200/32 border border-gray-50/48 rounded-2xl
                                 shadow-lg backdrop-blur-[9px]">
    <div class="modal-animation relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-2xl bg-white">
      <!-- Modal Header -->
      <div class="flex justify-between items-center mb-4">
        <h3 class="text-lg font-semibold text-gray-700">Create New User</h3>
        <button onclick="closeModal()" class="text-gray-400 hover:text-gray-600">
          <i class="fi fi-rr-cross-small text-xl"></i>
        </button>
      </div>

      <!-- Add User Form -->
      <form action="${pageContext.request.contextPath}/users/add" method="post">
        <div class="space-y-4">
          <!-- First Name -->
          <div>
            <label class="block text-sm font-medium text-gray-700">First Name</label>
            <input type="text" name="firstName" required
                   class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
          </div>

          <!-- Last Name -->
          <div>
            <label class="block text-sm font-medium text-gray-700">Last Name</label>
            <input type="text" name="lastName" required
                   class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
          </div>

          <!-- Username -->
          <div>
            <label class="block text-sm font-medium text-gray-700">Username</label>
            <input type="text" name="username" required
                   class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
          </div>

          <!-- Email -->
          <div>
            <label class="block text-sm font-medium text-gray-700">Email</label>
            <input type="email" name="email" required
                   class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
          </div>

          <!-- Phone -->
          <div>
            <label class="block text-sm font-medium text-gray-700">Phone</label>
            <input type="tel" name="phoneNumber"
                   class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
          </div>

          <!-- Submit Button -->
          <div class="pt-4">
            <button type="submit"
                    class="w-full bg-orange-500 hover:bg-orange-600 text-white font-medium py-2 px-4 rounded-lg transition-colors duration-200">
              Create User
            </button>
          </div>
        </div>
      </form>
    </div>
  </div>

</body>
  <script src="../../static/js/mouseAnimation.js"></script>
  <script src="../../static/js/modalFunction.js"></script>
</html>
