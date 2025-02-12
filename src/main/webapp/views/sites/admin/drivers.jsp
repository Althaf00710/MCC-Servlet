<%--
  Created by IntelliJ IDEA.
  User: Altha
  Date: 2/12/2025
  Time: 11:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.example.megacitycab.models.Driver" %>
<%@ page import="com.example.megacitycab.models.user.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>MCC | Driver</title>
  <script src="https://unpkg.com/@tailwindcss/browser@4"></script>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/mouseAnimation.css">
  <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/2.1.0/uicons-regular-rounded/css/uicons-regular-rounded.css">
  <script src="${pageContext.request.contextPath}/views/static/js/mouseAnimation.js"></script>
  <script src="${pageContext.request.contextPath}/views/static/js/confirmDelete.js"></script>
  <script src="${pageContext.request.contextPath}/views/static/js/modalFunction.js"></script>
  <script src="${pageContext.request.contextPath}/views/static/js/driverEditModal.js"></script>
</head>
<body class="bg-gray-50">
<%@ include file="common/navBar.jsp" %>
<div class="mouse-circle"></div>
<div class="ml-64 flex-1 p-8 pt-4">
  <!-- Header Section -->
  <div class="flex justify-between items-center mb-6 gap-4">
    <h1 class="text-2xl font-medium text-gray-600 whitespace-nowrap">Drivers</h1>

    <!-- Search Bar -->
    <div class="flex-1 max-w-md relative">
      <div class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-400 z-3">
        <i class="fi fi-rr-search text-lg"></i>
      </div>
      <form action="${pageContext.request.contextPath}/drivers/search" method="GET" class="relative">
        <input type="text"
               id="searchInput"
               name="searchTerm"
               placeholder="Search drivers..."
               class="w-full px-4 py-2 rounded-3xl border border-gray-300 focus:outline-none focus:ring-2 focus:ring-orange-500 pl-10 pr-16 transition-colors duration-200 hover:border-gray-400 bg-white">
        <button type="submit"
                class="absolute right-2 top-1/2 transform -translate-y-1/2 px-8 py-1 rounded-3xl bg-gray-100 text-orange-600 font-semibold hover:bg-gray-200 hover:text-orange-400 transition-colors duration-350 z-3 cursor-pointer">
          GO
        </button>
      </form>
    </div>


    <!-- Add Driver Button -->
    <button onclick="openModal()" class="bg-orange-500 hover:bg-orange-600 text-white px-4 py-2 rounded-3xl flex items-center transition-colors duration-200 whitespace-nowrap">
      <i class="fi fi-rr-plus mr-2"></i>
      Add Driver
    </button>
  </div>

  <!-- Drivers Table -->
  <div class="bg-white rounded-lg shadow overflow-hidden relative">
    <table class="min-w-full divide-y divide-gray-200" id="driversTable">
      <thead class="bg-gray-200">
      <tr>
        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Driver</th>
        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">NIC Number</th>
        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">License</th>
        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Phone</th>
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
                <div class="flex items-center px-2 py-1 rounded-md hover:bg-gray-100 cursor-pointer filter-option" data-filter="on_trip">
                  <span class="w-2 h-2 rounded-full bg-blue-500 mr-2"></span>
                  On Trip
                </div>
              </div>
            </div>
          </div>
        </th>
        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
      </tr>
      </thead>
      <tbody class="bg-white divide-y divide-gray-200">
      <c:forEach items="${drivers}" var="driver">
        <tr>
          <!-- Driver Info -->
          <td class="px-6 py-4 whitespace-nowrap">
            <div class="flex items-center">
              <div class="flex-shrink-0 h-10 w-10">
                <c:choose>
                  <c:when test="${not empty driver.avatarUrl}">
                    <img class="h-10 w-10 rounded-full" src="${pageContext.request.contextPath}/${driver.avatarUrl}" alt="Driver avatar">
                  </c:when>
                  <c:otherwise>
                    <img class="h-10 w-10 rounded-full" src="${pageContext.request.contextPath}/views/static/images/defaultAvatar.png" alt="Driver avatar">
                  </c:otherwise>
                </c:choose>
              </div>
              <div class="ml-4">
                <div class="text-sm font-medium text-gray-900">
                    ${driver.name}
                </div>
                <div class="text-sm text-gray-500">${driver.email}</div>
              </div>
            </div>
          </td>

          <!-- NIC Number -->
          <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${driver.nicNumber}</td>

          <!-- License Number -->
          <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${driver.licenceNumber}</td>

          <!-- Phone -->
          <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
            <a href="tel:${driver.phoneNumber}" class="hover:text-gray-800">
                ${driver.phoneNumber}
            </a>
          </td>

          <!-- Status -->
          <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500" data-status="${driver.status}">
            <div class="flex items-center">
              <c:choose>
                <c:when test="${driver.status == 'ACTIVE'}">
                  <span class="inline-block h-2 w-2 rounded-full bg-green-500 mr-2"></span>
                  Active
                </c:when>
                <c:when test="${driver.status == 'ON_TRIP'}">
                  <span class="inline-block h-2 w-2 rounded-full bg-blue-500 mr-2"></span>
                  On Trip
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
              <a href="#" class="text-orange-600 hover:text-orange-900 px-3 py-1 rounded-3xl hover:bg-orange-200 flex items-center" onclick="openEditDriverModal(${driver.id})">
                <i class="fi fi-rr-pencil mr-2"></i>
                Edit
              </a>
              <form action="${pageContext.request.contextPath}/drivers/delete" method="post" class="inline">
                <input type="hidden" name="id" value="${driver.id}">
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

<!-- Add Driver Modal -->
<div id="Modal" class="hidden fixed inset-0 flex items-center justify-center bg-opacity-50 h-full w-full
                               bg-gray-200/32 border border-gray-50/48 rounded-2xl
                               shadow-lg backdrop-blur-[9px]">
  <div class="modal-animation relative p-5 border w-1/3 shadow-lg rounded-2xl bg-white">
    <!-- Modal Header -->
    <div class="flex justify-between items-center mb-4">
      <h3 class="text-lg font-semibold text-gray-700">Add New Driver</h3>
      <button onclick="closeModal()" class="text-gray-400 hover:text-gray-600">
        <i class="fi fi-rr-cross-small text-xl"></i>
      </button>
    </div>

    <!-- Add Driver Form -->
    <form action="${pageContext.request.contextPath}/drivers/add" method="post" enctype="multipart/form-data">
      <div class="space-y-4">
        <!-- Driver Name -->
        <div class="relative">
          <label class="block text-sm font-medium text-gray-700">Full Name</label>
          <input type="text" name="name" required
                 class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
        </div>

        <!-- NIC Number -->
        <div class="relative">
          <label class="block text-sm font-medium text-gray-700">NIC Number</label>
          <input type="text" name="nicNumber" required
                 class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
        </div>

        <!-- License Number -->
        <div class="relative">
          <label class="block text-sm font-medium text-gray-700">License Number</label>
          <input type="text" name="licenceNumber" required
                 class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
        </div>

        <!-- Contact Information -->
        <div class="grid grid-cols-2 gap-4">
          <div class="relative">
            <label class="block text-sm font-medium text-gray-700">Phone Number</label>
            <input type="tel" name="phoneNumber" required
                   class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
          </div>

          <div class="relative">
            <label class="block text-sm font-medium text-gray-700">Email</label>
            <input type="email" name="email" required
                   class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
          </div>
        </div>

        <!-- Image Upload -->
        <div class="relative">
          <label class="block text-sm font-medium text-gray-700">Driver Photo</label>
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
            Add Driver
          </button>
        </div>
      </div>
    </form>
  </div>
</div>

<!-- Edit Driver Modal -->
<div id="editDriverModal" class="hidden fixed inset-0 flex items-center justify-center bg-opacity-50 h-full w-full
                                  bg-gray-200/32 border border-gray-50/48 rounded-2xl
                                  shadow-lg backdrop-blur-[9px]">
  <div class="modal-animation relative p-5 border w-1/3 shadow-lg rounded-2xl bg-white">
    <!-- Modal Header -->
    <div class="flex justify-between items-center mb-4">
      <h3 class="text-lg font-semibold text-gray-700">Edit Driver</h3>
      <button onclick="closeEditDriverModal()" class="text-gray-400 hover:text-gray-600">
        <i class="fi fi-rr-cross-small text-xl"></i>
      </button>
    </div>

    <!-- Avatar Update Form -->
    <form action="${pageContext.request.contextPath}/drivers/updateImage" method="post" enctype="multipart/form-data">
      <input type="hidden" name="id">

      <!-- Image Section -->
      <div class="mb-6">
        <div class="flex flex-col items-center gap-4">
          <div class="relative group">
            <div class="h-24 w-24 rounded-full overflow-hidden border-2 border-orange-200 relative">
              <img id="currentDriverAvatar"
                   class="h-full w-full object-cover"
                   src="${not empty user.avatarUrl ? pageContext.request.contextPath.concat('/').concat(user.avatarUrl) : pageContext.request.contextPath.concat('/views/static/images/defaultAvatar.png')}"
                   alt="Driver avatar">
              <div class="absolute inset-0 bg-black/40 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity">
                <label class="cursor-pointer text-white">
                  <i class="fi fi-rr-camera"></i>
                  <input type="file" name="avatar" accept="image/*" class="hidden" id="driverAvatarInput">
                </label>
              </div>
            </div>
          </div>
          <button type="button" onclick="document.querySelector('#editDriverModal input[name=avatar]').click()"
                  class="text-orange-500 hover:text-orange-600 text-sm font-medium flex items-center">
            <i class="fi fi-rr-picture mr-2"></i>
            Change Picture
          </button>
        </div>
      </div>

      <!-- Image Submit Button -->
      <div class="pt-6">
        <button type="submit" id="driverSubmitImageBtn"
                class="w-full bg-orange-500 hover:bg-orange-600 text-white font-medium py-2 px-4 rounded-lg transition-colors duration-200">
          <i class="fi fi-rr-check mr-2"></i>
          Update Profile Picture
        </button>
      </div>
    </form>

    <!-- Driver Details Form -->
    <form action="${pageContext.request.contextPath}/drivers/update" method="post">
      <div class="space-y-4 border-t border-gray-200 pt-4">
        <input type="hidden" name="id">
        <!-- Driver Name -->
        <div class="relative">
          <label class="block text-sm font-medium text-gray-700">Full Name</label>
          <input type="text" name="name" required
                 class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
                 value="${driver.name}">
        </div>

        <!-- NIC Number -->
        <div class="relative">
          <label class="block text-sm font-medium text-gray-700">NIC Number</label>
          <input type="text" name="nicNumber" required
                 class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
                 value="${driver.nicNumber}">
        </div>

        <!-- License Number -->
        <div class="relative">
          <label class="block text-sm font-medium text-gray-700">License Number</label>
          <input type="text" name="licenceNumber" required
                 class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
                 value="${driver.licenseNumber}">
        </div>

        <!-- Contact Information -->
        <div class="grid grid-cols-2 gap-4">
          <div class="relative">
            <label class="block text-sm font-medium text-gray-700">Phone Number</label>
            <input type="tel" name="phoneNumber" required
                   class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
                   value="${driver.phoneNumber}">
          </div>

          <div class="relative">
            <label class="block text-sm font-medium text-gray-700">Email</label>
            <input type="email" name="email" required
                   class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
                   value="${driver.email}">
          </div>
        </div>

        <!-- Submit Button -->
        <div class="pt-6">
          <button type="submit"
                  class="w-full bg-orange-500 hover:bg-orange-600 text-white font-medium py-2 px-4 rounded-lg transition-colors duration-200">
            Update Driver Details
          </button>
        </div>
      </div>
    </form>
  </div>
</div>

</body>
</html>
