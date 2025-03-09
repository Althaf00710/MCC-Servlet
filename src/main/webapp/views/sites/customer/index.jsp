<%--
  Created by IntelliJ IDEA.
  User: Altha
  Date: 3/9/2025
  Time: 1:18 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>MCC | Welcome</title>
    <script src="https://unpkg.com/@tailwindcss/browser@4"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/three.js/r128/three.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/three@0.128.0/examples/js/loaders/GLTFLoader.js"></script>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/static/css/customer/style.css">
    <script defer src="${pageContext.request.contextPath}/views/static/js/customer/googleMap.js"></script>
    <script defer src="${pageContext.request.contextPath}/views/static/js/customer/script.js"></script>
    <script defer src="${pageContext.request.contextPath}/views/static/js/customer/addStop.js"></script>
    <script defer src="${pageContext.request.contextPath}/views/static/js/customer/formModal.js"></script>
    <script defer src="${pageContext.request.contextPath}/views/static/js/customer/Addbooking.js"></script>
    <script defer src="https://maps.googleapis.com/maps/api/js?key=&libraries=places&callback=initMap"></script>
</head>
<body class="bg-gray-900 text-white font-inter">
<!-- Header with Glassmorphism -->
<header class="fixed top-0 left-0 w-full flex items-center justify-between px-8 py-2 bg-white/20 backdrop-blur-sm border border-white/10 shadow-lg z-50">
    <a href="#"><img src="${pageContext.request.contextPath}/views/static/images/logoDark.png" alt="Mega City" class="h-10 cursor-pointer"></a>
    <button id="loginBtn" class="px-8 py-1.5 bg-orange-500/90 backdrop-blur-lg border border-orange-500/50 text-white font-semibold rounded-full hover:bg-orange-500 hover:text-white transition-all duration-300 hover:cursor-pointer">Login</button>
</header>

<!-- Map Section -->
<div class="h-screen relative">
    <div id="map" class="absolute w-full h-full"></div>
    <div class="absolute top-0 left-0 w-full h-full pointer-events-none" style="background: linear-gradient(to bottom, rgba(255, 165, 0, 0.2), transparent);">
        <div class="absolute top-1/3 left-1/2 transform -translate-x-1/2 -translate-y-1/4 flex flex-col justify-center w-full items-center animate-slide-down">
            <span class="text-center text-4xl font-medium text-orange-500/50">Your Journey starts here with</span>
            <img src="${pageContext.request.contextPath}/views/static/images/logoDark.png" alt="MegaCity Logo" class="h-35 center w-100">
        </div>
    </div>

    <!-- Booking Form on Map -->
    <div id="bookingForm" class="absolute left-1/2 top-3/4 -translate-x-1/2 -translate-y-1/2 w-4/5 max-w-4xl bg-white/80 backdrop-blur-md border border-white/50 shadow-lg z-10 px-6 py-4 rounded-2xl group">
        <form class="space-y-4">
            <div class="flex gap-4 items-center">
                <div class="flex-1 relative">
                    <input id="pickup" type="text" placeholder="Pickup Location" class="w-full px-4 py-2 bg-white/90 border border-gray-500 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500 text-gray-900 transition-all duration-300">
                    <svg class="w-5 h-5 absolute right-3 top-1/2 -translate-y-1/2 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/>
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/>
                    </svg>
                </div>
                <svg class="w-6 h-6 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3"/>
                </svg>
                <div class="flex-1 relative">
                    <input id="drop" type="text" placeholder="Drop Location" class="w-full px-4 py-2 bg-white/90 border border-gray-500 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500 text-gray-900 transition-all duration-300">
                    <svg class="w-5 h-5 absolute right-3 top-1/2 -translate-y-1/2 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/>
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/>
                    </svg>
                </div>
                <input id="date" type="date" class="w-1/3 px-4 py-2 bg-white/90 border border-gray-500 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500 text-gray-900 transition-all duration-300">
            </div>

            <div id="stopsContainer" class="space-y-2"></div>
            <button type="button" id="addStop" class="text-blue-500 hover:text-blue-700 hover:cursor-pointer transition-colors mb-1"><i class="fi fi-rr-plus"></i> + Add Stop</button>

            <div id="cabTypeContainer" class="flex flex-nowrap gap-4 overflow-x-auto scroll-smooth whitespace-nowrap px-4 max-h-0 overflow-hidden transition-all duration-600 ease-in-out group-hover:max-h-60 group-hover:py-2"></div>
            <input type="hidden" id="selectedCabType" name="cabTypeId" required>
        </form>
    </div>
</div>

<!-- Bottom Content -->
<div class="h-1/4 flex items-center justify-center p-10 bg-white text-gray-500">
    <div class="grid grid-cols-1 md:grid-cols-4 gap-8 w-full max-w-6xl">
        <div class="text-center group">
            <div class="mb-4 flex justify-center">
                <svg class="w-12 h-12 text-orange-500 group-hover:text-orange-400 transition-colors" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"/>
                </svg>
            </div>
            <h3 class="text-xl font-semibold mb-2">Easy Online Booking</h3>
        </div>
        <div class="text-center group">
            <div class="mb-4 flex justify-center">
                <svg class="w-12 h-12 text-orange-500 group-hover:text-orange-400 transition-colors" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"/>
                </svg>
            </div>
            <h3 class="text-xl font-semibold mb-2">Professional Drivers</h3>
        </div>
        <div class="text-center group">
            <div class="mb-4 flex justify-center">
                <svg class="w-12 h-12 text-orange-500 group-hover:text-orange-400 transition-colors" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4m0 13V4m0 0L9 7"/>
                </svg>
            </div>
            <h3 class="text-xl font-semibold mb-2">Big Fleet Of Vehicles</h3>
        </div>
        <div class="text-center group">
            <div class="mb-4 flex justify-center">
                <svg class="w-12 h-12 text-orange-500 group-hover:text-orange-400 transition-colors" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M18.364 5.636l-3.536 3.536m0 5.656l3.536 3.536M9.172 9.172L5.636 5.636m3.536 9.192l-3.536 3.536M21 12a9 9 0 11-18 0 9 9 0 0118 0zm-5 0a4 4 0 11-8 0 4 4 0 018 0z"/>
                </svg>
            </div>
            <h3 class="text-xl font-semibold mb-2">24/7 Support</h3>
        </div>
    </div>
</div>

<!-- 3D Car Canvas -->
<div id="canvas-container" class="fixed top-3/4 left-1/2 transform -translate-x-1/2 -translate-y-1/2 w-[500px] h-[500px]"></div>

<!-- Login Modal -->
<div id="loginModal" class="fixed inset-0 bg-black/50 backdrop-blur-md hidden z-50">
    <div class="fixed inset-0 flex items-center justify-center p-4">
        <div class="relative bg-gray-900/80 backdrop-blur-xl rounded-2xl p-8 w-full max-w-md border border-white/10 shadow-xl">
            <!-- Email Form -->
            <form id="emailForm" class="space-y-6">
                <h2 class="text-2xl font-bold text-center mb-8">Welcome to Mega City</h2>
                <input type="email" name="email" placeholder="Enter your email" class="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-lg focus:outline-none focus:border-orange-500 focus:ring-2 focus:ring-orange-500/50 transition-all duration-300">
                <button type="button" id="sendOTP" class="w-full py-3 bg-orange-500 hover:bg-orange-600 rounded-lg font-semibold transition-all duration-300">Send OTP</button>
            </form>
            <!-- OTP Form -->
            <form id="otpForm" class="space-y-6 hidden">
                <h2 class="text-2xl font-bold text-center mb-8">Verify OTP</h2>
                <div class="flex gap-2 justify-center">
                    <input type="text" maxlength="1" class="w-12 h-12 text-center bg-white/5 border border-white/10 rounded-lg focus:outline-none focus:border-orange-500 focus:ring-2 focus:ring-orange-500/50 transition-all duration-300">
                    <input type="text" maxlength="1" class="w-12 h-12 text-center bg-white/5 border border-white/10 rounded-lg focus:outline-none focus:border-orange-500 focus:ring-2 focus:ring-orange-500/50 transition-all duration-300">
                    <input type="text" maxlength="1" class="w-12 h-12 text-center bg-white/5 border border-white/10 rounded-lg focus:outline-none focus:border-orange-500 focus:ring-2 focus:ring-orange-500/50 transition-all duration-300">
                    <input type="text" maxlength="1" class="w-12 h-12 text-center bg-white/5 border border-white/10 rounded-lg focus:outline-none focus:border-orange-500 focus:ring-2 focus:ring-orange-500/50 transition-all duration-300">
                </div>
                <button type="submit" class="w-full py-3 bg-orange-500 hover:bg-orange-600 rounded-lg font-semibold transition-all duration-300">Verify OTP</button>
            </form>
            <!-- Register Form -->
            <form id="registerForm" class="space-y-6 hidden">
                <h2 class="text-2xl font-bold text-center mb-8">Create Account</h2>
                <div class="grid grid-cols-2 gap-4">
                    <input type="text" name="firstname" placeholder="First Name" required class="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-lg focus:outline-none focus:border-orange-500 focus:ring-2 focus:ring-orange-500/50 transition-all duration-300">
                    <input type="text" name="lastname" placeholder="Last Name" required class="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-lg focus:outline-none focus:border-orange-500 focus:ring-2 focus:ring-orange-500/50 transition-all duration-300">
                </div>
                <input type="tel" placeholder="Phone Number" required class="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-lg focus:outline-none focus:border-orange-500 focus:ring-2 focus:ring-orange-500/50 transition-all duration-300">
                <input type="email" placeholder="Email Address" required class="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-lg focus:outline-none focus:border-orange-500 focus:ring-2 focus:ring-orange-500/50 transition-all duration-300">
                <div class="relative">
                    <input type="password" placeholder="Create Password" required class="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-lg focus:outline-none focus:border-orange-500 focus:ring-2 focus:ring-orange-500/50 transition-all duration-300">
                    <button type="button" class="absolute right-3 top-1/2 -translate-y-1/2 text-white/50 hover:text-white transition-colors">
                        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7z"/>
                        </svg>
                    </button>
                </div>
                <button type="submit" class="w-full py-3 bg-orange-500 hover:bg-orange-600 rounded-lg font-semibold transition-all duration-300">Create Account</button>
            </form>
            <div class="relative my-6">
                <div class="absolute inset-0 flex items-center"><div class="w-full border-t border-white/10"></div></div>
                <div class="relative flex justify-center text-sm"><span class="px-2 bg-gray-900/80 text-white/50">OR</span></div>
            </div>
            <button id="registerBtn" class="w-full py-3 bg-white/5 hover:bg-white/10 rounded-lg font-semibold transition-all duration-300">Register New Account</button>
            <button id="backToLogin" class="hidden w-full mt-4 text-sm text-white/50 hover:text-white transition-colors">← Back to Login</button>
        </div>
    </div>
</div>
</body>
</html>
