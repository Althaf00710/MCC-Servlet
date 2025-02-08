<%@ page import="com.example.megacitycab.models.Driver" %>
<%@ page import="com.example.megacitycab.models.user.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
  <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/mouseAnimation.css">
  <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/2.1.0/uicons-regular-rounded/css/uicons-regular-rounded.css">
  <script src="${pageContext.request.contextPath}/views/static/js/userSearch.js"></script>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/avatarInitials.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/modalAnimation.css">
  <script src="${pageContext.request.contextPath}/views/static/js/mouseAnimation.js"></script>
  <script src="${pageContext.request.contextPath}/views/static/js/modalFunction.js"></script>
  <script src="${pageContext.request.contextPath}/views/static/js/confirmDelete.js"></script>
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
      <button onclick="openModal()" class="bg-orange-500 hover:bg-orange-600 text-white px-4 py-2 rounded-3xl flex items-center transition-colors duration-200 whitespace-nowrap">
        <i class="fi fi-rr-plus mr-2"></i>
        Add User
      </button>
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
                  <c:choose>
                    <c:when test="${not empty user.avatarUrl}">
                      <img class="h-10 w-10 rounded-full" src="C:/Program Files/Apache Software Foundation/Tomcat 11.0/bin/src/main/webapp/${user.avatarUrl}" alt="User avatar">
                    </c:when>
                    <c:otherwise>
                      <img class="h-10 w-10 rounded-full" src="${pageContext.request.contextPath}/views/static/images/defaultAvatar.png" alt="User avatar">
                    </c:otherwise>
                  </c:choose>
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
                <!-- Delete Form -->
                <form action="${pageContext.request.contextPath}/users/delete" method="post" class="inline">
                  <input type="hidden" name="id" value="${user.id}">
                  <button type="submit"
                          class="text-red-600 hover:text-red-900 px-3 py-1 rounded-3xl hover:bg-red-200 flex items-center">
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


  <!-- Add User Modal -->
  <div id="Modal" class="hidden fixed inset-0 flex items-center justify-center bg-opacity-50 h-full w-full
                               bg-gray-200/32 border border-gray-50/48 rounded-2xl
                               shadow-lg backdrop-blur-[9px]">
    <div class="modal-animation relative p-5 border w-1/3 shadow-lg rounded-2xl bg-white">
      <!-- Modal Header -->
      <div class="flex justify-between items-center mb-4">
        <h3 class="text-lg font-semibold text-gray-700">Create New User</h3>
        <button onclick="closeModal()" class="text-gray-400 hover:text-gray-600">
          <i class="fi fi-rr-cross-small text-xl"></i>
        </button>
      </div>

      <!-- Add User Form -->
      <form action="${pageContext.request.contextPath}/users/add" method="post" enctype="multipart/form-data">
        <div class="space-y-4">
          <!-- Name Row -->
          <div class="flex gap-4">
            <!-- First Name -->
            <div class="flex-1 relative">
              <label class="block text-sm font-medium text-gray-700">First Name</label>
                <input type="text" name="firstName" required
                       class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
                >
            </div>

            <!-- Last Name -->
            <div class="flex-1 relative">
              <label class="block text-sm font-medium text-gray-700">Last Name</label>
                <input type="text" name="lastName" required
                       class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
                >
            </div>
          </div>

          <!-- Username -->
          <div class="relative">
            <label class="block text-sm font-medium text-gray-700">Username</label>
            <div class="relative">
              <i class="fi fi-rr-user absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
              <input type="text" name="username" required
                     class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 pl-10 focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
              >
            </div>
          </div>

          <!-- Password -->
          <div class="relative">
            <label class="block text-sm font-medium text-gray-700">Password</label>
            <div class="relative">
              <i class="fi fi-rr-lock absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
              <input type="text" name="password" required
                     class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 pl-10 focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
              >
            </div>
          </div>

          <!-- Email -->
          <div class="relative">
            <label class="block text-sm font-medium text-gray-700">Email</label>
            <div class="relative">
              <i class="fi fi-rr-envelope absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
              <input type="email" name="email" required
                     class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 pl-10 focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
              >
            </div>
          </div>

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

          <!-- Image Upload -->
          <div class="relative">
            <label class="block text-sm font-medium text-gray-700">Avatar</label>
            <div class="relative">
              <i class="fi fi-rr-upload absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
              <input type="file" name="avatar" accept="image/*"
                     class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 pl-10 focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
            </div>
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
</html>
