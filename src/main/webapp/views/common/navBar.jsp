<%--
  Created by IntelliJ IDEA.
  User: Altha
  Date: 1/30/2025
  Time: 10:50 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="w-64 h-[calc(100vh-1rem)] text-white fixed left-2 top-2 bottom-2 flex flex-col rounded-xl shadow-2xl" style="background-color: #0f0f0f">
    <!-- Company Logo -->
    <div class="p-4 border-b border-gray-700">
        <img src="${pageContext.request.contextPath}/views/static/images/logoLight.png"
             alt="Company Logo"
             class="w-32 mx-auto">
    </div>

    <!-- Navigation Links -->
    <nav class="flex-1 overflow-y-auto p-4">
        <ul class="space-y-2">
            <li>
                <a href="${pageContext.request.contextPath}/views/sites/admin/dashboard.jsp"
                   class="flex items-center p-2 rounded-3xl hover:bg-gray-700 pl-3 ${param.activePage eq 'dashboard' ? 'bg-gray-700' : ''}">
                    <i class="fi fi-rr-dashboard mr-3"></i>
                    Dashboard
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/bookings"
                   class="flex items-center p-2 rounded-3xl hover:bg-gray-700 pl-3 ${param.activePage eq 'bookings' ? 'bg-gray-700' : ''}">
                    <i class="fi fi-rr-calendar mr-3"></i>
                    Bookings
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/users/list"
                   class="flex items-center p-2 rounded-3xl hover:bg-gray-700 pl-3 ${param.activePage eq 'users' ? 'bg-gray-700' : ''}">
                    <i class="fi fi-rr-users mr-3"></i>
                    Users
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/drivers"
                   class="flex items-center p-2 rounded-3xl hover:bg-gray-700 pl-3 ${param.activePage eq 'drivers' ? 'bg-gray-700' : ''}">
                    <i class="fi fi-rr-steering-wheel mr-3"></i>
                    Drivers
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/reports"
                   class="flex items-center p-2 rounded-3xl hover:bg-gray-700 pl-3 ${param.activePage eq 'reports' ? 'bg-gray-700' : ''}">
                    <i class="fi fi-rr-document mr-3"></i>
                    Reports
                </a>
            </li>
        </ul>
    </nav>

    <!-- User Profile Section -->
    <div class="p-4 pl-6 border-b border-gray-700">
        <div class="flex items-center gap-3">
            <!-- Avatar -->
            <div class="flex-shrink-0">
                <img src="${not empty user.avatarUrl ? user.avatarUrl : pageContext.request.contextPath}/views/static/images/defaultAvatar.png"
                     alt="User Avatar"
                     class="w-10 h-10 rounded-full">
            </div>

            <!-- Name -->
            <div class="font-semibold text-sm text-white truncate uppercase">
                ${user.firstName} ${user.lastName}
            </div>
        </div>
    </div>

    <!-- Logout Button -->
    <div class="mt-auto p-4 border-t border-gray-700">
        <form action="${pageContext.request.contextPath}/logout" method="post">
            <button type="submit"
                    class="w-full flex items-center p-2 pl-3 rounded-3xl hover:bg-orange-400 text-orange-400 hover:text-white">
                <i class="fi fi-rr-exit mr-3"></i>
                Logout
            </button>
        </form>
    </div>
</div>