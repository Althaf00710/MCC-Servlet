<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--
  Created by IntelliJ IDEA.
  User: Altha
  Date: 2/16/2025
  Time: 2:56 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>MCC | Cab Types</title>
    <script src="https://unpkg.com/@tailwindcss/browser@4"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/mouseAnimation.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/scrollBar.css">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/2.1.0/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <script DEFER src="${pageContext.request.contextPath}/views/static/js/mouseAnimation.js"></script>
    <script src="${pageContext.request.contextPath}/views/static/js/confirmDelete.js"></script>
    <script src="${pageContext.request.contextPath}/views/static/js/modalFunction.js"></script>
    <script defer src="${pageContext.request.contextPath}/views/static/js/cabTypesEdit.js"></script>
</head>
<body>
    <%@ include file="../common/navBar.jsp" %>
    <div class="mouse-circle"></div>
    <div class="ml-64 flex-1 p-8 pt-4">
        <!-- Header Section -->
        <div class="flex justify-between items-center mb-6 gap-4">
            <!-- Left Title -->
            <h1 class="text-2xl font-medium text-gray-600 whitespace-nowrap">Cab Types</h1>

            <!-- Search Bar -->
            <div class="flex-1 max-w-md relative">
                <div class="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none text-gray-400">
                    <i class="fi fi-rr-search text-lg"></i>
                </div>
                <input type="text"
                       id="searchInput"
                       onkeyup="filterUsers()"
                       placeholder="Search Cab Types..."
                       class="w-full px-4 py-2 rounded-3xl border border-gray-300 focus:outline-none focus:ring-2 focus:ring-orange-500 pl-10 transition-colors duration-200 hover:border-gray-400 bg-white">
            </div>

            <!-- Right Add Button -->
            <button onclick="openModal()" class="bg-orange-500 hover:bg-orange-600 text-white px-4 py-2 rounded-3xl flex items-center transition-colors duration-200 whitespace-nowrap">
                <i class="fi fi-rr-plus mr-2"></i>
                Add Cab Type
            </button>
        </div>

        <!-- Cab Types Grid -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mt-16">
            <c:forEach items="${cabTypes}" var="cabType">
                <div class="relative bg-gray-100 border-2 border-orange-400 rounded-xl shadow-sm hover:shadow-md transition-shadow duration-200 p-2 text-center pt-20 w-68">
                    <!-- Image (Bigger & Overlapping Top Border) -->
                    <div class="absolute top-0 left-1/2 transform -translate-x-1/2 -translate-y-1/8 w-96 h-32 z-1">
                        <c:choose>
                            <c:when test="${not empty fn:trim(cabType.imageUrl)}">
                                <img src="${pageContext.request.contextPath}/${cabType.imageUrl}"
                                     alt="${cabType.typeName}"
                                     class="w-full h-full object-contain">
                            </c:when>
                            <c:otherwise>
                                <div class="w-64 h-64 bg-gray-100 rounded-lg flex items-center justify-center border-4 border-orange-400 shadow-md">
                                    <i class="fi fi-rr-car text-6xl text-gray-400"></i>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Type Name -->
                    <h3 class="text-lg font-bold text-gray-700 mt-14 mb-1">${cabType.typeName}</h3>

                    <!-- Attributes with Icons (Inline) -->
                    <div class="flex justify-center items-center gap-3 text-gray-600 text-sm mb-3 font-semibold">
                        <div class="flex items-center gap-1">
                            <img src="${pageContext.request.contextPath}/views/static/images/seat.png" class="w-4 h-4">
                            <span>${cabType.capacity} Seats</span>
                        </div>
                        <div class="flex items-center gap-1">
                            <img src="${pageContext.request.contextPath}/views/static/images/road.png" class="w-4 h-4">
                            <span>Rs. ${cabType.baseFare}</span>
                        </div>
                        <div class="flex items-center gap-1">
                            <img src="${pageContext.request.contextPath}/views/static/images/clock.png" class="w-4 h-4">
                            <span>Rs. ${cabType.baseWaitTimeFare}</span>
                        </div>
                    </div>

                    <!-- Action Buttons (Half-Half Area) -->
                    <div class="grid grid-cols-2 gap-2 z-5">
                        <button onclick="openEditCabTypeModal(${cabType.id})"
                                class="px-3 py-1.5 text-sm bg-orange-100 hover:bg-orange-200 text-orange-600 rounded-lg w-full">
                            <i class="fi fi-rr-pencil mr-2"></i>
                        </button>
                        <button onclick="deleteCabType(${cabType.id})"
                                class="px-3 py-1.5 text-sm bg-red-100 hover:bg-red-200 text-red-600 rounded-lg w-full">
                            <i class="fi fi-rr-trash mr-2"></i>
                        </button>
                    </div>
                </div>
            </c:forEach>
        </div>

    </div>

    <!-- Add Cab Type Modal -->
    <div id="Modal" class="hidden fixed inset-0 flex items-center justify-center bg-opacity-50 h-full w-full
                               bg-gray-200/32 border border-gray-50/48 rounded-2xl
                               shadow-lg backdrop-blur-[9px]">
        <div class="modal-animation relative p-5 border w-1/3 shadow-lg rounded-2xl bg-white">
            <!-- Modal Header -->
            <div class="flex justify-between items-center mb-4">
                <h3 class="text-lg font-semibold text-gray-700">Create New Cab Type</h3>
                <button onclick="closeModal()" class="text-gray-400 hover:text-gray-600">
                    <i class="fi fi-rr-cross-small text-xl"></i>
                </button>
            </div>

            <!-- Add User Form -->
            <form action="${pageContext.request.contextPath}/cabtypes/add" method="post" enctype="multipart/form-data">
                <div class="space-y-4">
                    <!-- Name Row -->
                    <div class="flex gap-4">
                        <!-- Name -->
                        <div class="flex-1 relative">
                            <label class="block text-sm font-medium text-gray-700">Cab Type Name</label>
                            <input type="text" name="typeName" required
                                   class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
                            >
                        </div>
                    </div>

                    <!-- Capacity -->
                    <div class="relative">
                        <label class="block text-sm font-medium text-gray-700">Capacity</label>
                        <div class="relative">
                            <i class="fi fi-rr-user absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
                            <input type="number" name="capacity" required
                                   class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 pl-10 focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
                            >
                        </div>
                    </div>

                    <!-- Basefare -->
                    <div class="relative">
                        <label class="block text-sm font-medium text-gray-700">Base Fare /km</label>
                        <div class="relative">
                            <i class="fi fi-rr-lock absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
                            <input type="number" name="baseFare" required
                                   class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 pl-10 focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
                            >
                        </div>
                    </div>

                    <!-- baseWaitFare -->
                    <div class="relative">
                        <label class="block text-sm font-medium text-gray-700">Base Wait Fare /Min</label>
                        <div class="relative">
                            <i class="fi fi-rr-envelope absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
                            <input type="number" name="baseWaitFare" required
                                   class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 pl-10 focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
                            >
                        </div>
                    </div>

                    <!-- Image Upload -->
                    <div class="relative">
                        <label class="block text-sm font-medium text-gray-700">Cab Type Image</label>
                        <div class="relative">
                            <i class="fi fi-rr-upload absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
                            <input type="file" name="image" accept="image/*"
                                   class="mt-1 block w-full rounded-lg border border-gray-300 px-3 py-2 pl-10 focus:ring-2 focus:ring-orange-500 focus:border-orange-500">
                        </div>
                    </div>

                    <!-- Submit Button -->
                    <div class="pt-4">
                        <button type="submit"
                                class="w-full bg-orange-500 hover:bg-orange-600 text-white font-medium py-2 px-4 rounded-lg transition-colors duration-200">
                            Create Cab Type
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <!-- Edit CabType Modal -->
    <div id="editCabTypeModal" class="hidden fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4">
        <div class="bg-white rounded-xl shadow-lg w-full max-w-md p-6">
            <!-- Modal Header -->
            <div class="flex justify-between items-center mb-4">
                <h3 class="text-lg font-semibold">Edit Cab Type</h3>
                <button onclick="closeEditCabTypeModal()" class="text-gray-500 hover:text-gray-700">
                    <i class="fi fi-rr-cross-small text-xl"></i>
                </button>
            </div>

            <!-- Image Upload Form -->
            <form id="cabTypeImageForm" enctype="multipart/form-data" class="mb-6">
                <input type="hidden" name="id">

                <div class="text-center">
                    <div class="relative group inline-block">
                        <div class="h-32 w-32 rounded-lg overflow-hidden border-2 border-orange-200">
                            <img id="currentCabTypeImage"
                                 class="h-full w-full object-cover"
                                 src="${pageContext.request.contextPath}/views/static/images/defaultCab.png"
                                 alt="Cab type image">
                            <div class="absolute inset-0 bg-black/40 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity">
                                <label class="cursor-pointer text-white">
                                    <i class="fi fi-rr-camera"></i>
                                    <input type="file" name="image" accept="image/*" class="hidden">
                                </label>
                            </div>
                        </div>
                    </div>

                    <!-- Image Form Submit Button -->
                    <button type="submit"
                            class="mt-2 px-4 py-2 bg-orange-500 hover:bg-orange-600 text-white rounded-lg">
                        Update Image
                    </button>
                </div>
            </form>

            <!-- Cab Type Details Form -->
            <form id="cabTypeDetailsForm" action="${pageContext.request.contextPath}/cabtypes/update" method="post">

                <!-- Form Fields -->
                <div class="space-y-4">
                    <input type="hidden" name="id">

                    <div>
                        <label class="block text-sm font-medium mb-1">Type Name</label>
                        <input type="text" name="typeName" required
                               class="w-full px-3 py-2 border rounded-lg">
                    </div>

                    <div>
                        <label class="block text-sm font-medium mb-1">Capacity</label>
                        <input type="number" name="capacity" required
                               class="w-full px-3 py-2 border rounded-lg">
                    </div>

                    <div>
                        <label class="block text-sm font-medium mb-1">Base Fare</label>
                        <input type="number" step="0.01" name="baseFare" required
                               class="w-full px-3 py-2 border rounded-lg">
                    </div>

                    <div>
                        <label class="block text-sm font-medium mb-1">Wait Time Fare (per min)</label>
                        <input type="number" step="0.01" name="baseWaitFare" required
                               class="w-full px-3 py-2 border rounded-lg">
                    </div>
                </div>

                <!-- Form Submit Button -->
                <div class="mt-6 flex justify-end gap-2">
                    <button type="button" onclick="closeEditCabTypeModal()"
                            class="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-lg">
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
