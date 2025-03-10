<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>MCC | Login</title>
    <script src="https://unpkg.com/@tailwindcss/browser@4"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/three.js/110/three.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/vanta/0.5.21/vanta.dots.min.js"></script>
    <link rel="stylesheet" href="views/static/css/loginPage.s.css">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/2.1.0/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <link rel="stylesheet" href="views/static/css/mouseAnimation.css">
    <link rel="stylesheet" href="views/static/css/loginStyles.css">
</head>
<body>
<div id="vanta-bg" class="flex h-screen">
    <!-- Mouse Animation -->
    <div class="mouse-circle"></div>

    <div class="w-3/5 flex items-center justify-center hidden sm:flex">
        <div id="carousel" class="max-w-2xl px-4">
            <!-- Carousel items will be inserted here by JavaScript -->
        </div>
    </div>

    <div class="w-full sm:w-2/5 flex flex-col items-center justify-center p-6">

        <div class="w-full max-w-sm p-6 shadow-lg bg-white">
        <img
                src="views/static/images/logoDark.png"
                alt="MCC Logo"
                class="mb-4 w-3/4 mx-auto">
        <!-- Login Form -->
            <div id="form-content">
                <form method="post" class="rounded-2xl" action="${pageContext.request.contextPath}/login" id="login-form">
                    <p class="text-2xl font-semibold text-gray-700"><span class="text-orange-500">Welcome</span> Back,</p>
                    <span class="text-sm text-gray-700 mb-4 font-light">Enter Your Valid Credentials to Login.</span>
                    <!-- Username Input -->
                    <div class="my-4 relative">
                        <div class="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none text-gray-400">
                            <i class="fi fi-rr-user text-lg"></i>
                        </div>
                        <input
                                type="text"
                                id="username"
                                name="username"
                                class="w-full border border-gray-300 rounded-lg px-4 py-2 pl-10 focus:outline-none focus:ring-1 focus:ring-orange-400"
                                placeholder="Username"
                                required>
                    </div>

                    <!-- Password Input -->
                    <div class="mb-4 relative">
                        <div class="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none text-gray-400">
                            <i class="fi fi-rr-lock text-lg"></i>
                        </div>
                        <input
                                type="password"
                                id="password"
                                name="password"
                                class="w-full border border-gray-300 rounded-lg px-4 py-2 pl-10 focus:outline-none focus:ring-1 focus:ring-orange-400"
                                placeholder="Password"
                                required>
                    </div>

                    <!-- Submit Button -->
                    <div class="mb-4">
                        <button
                                type="submit"
                                class="w-full bg-orange-500 text-white font-bold py-2 px-4 rounded-lg hover:bg-orange-600 transition">
                            Login
                        </button>
                    </div>

                    <!-- Horizontal Line -->
                    <div class="flex items-center justify-center my-4">
                        <div class="w-1/3 border-t border-gray-300"></div>
                    </div>

                    <!-- Forgot Password Link -->
                    <p class="text-center text-gray-500 text-sm hover:cursor-pointer" onclick="showUsernameForm(event)">
                        Forgot Password? <span class="text-orange-500 font-bold hover:underline">We Got You.</span>
                    </p>
                </form>
            </div>
        </div>
    </div>
</div>


</body>
</html>

<script src="views/static/js/loginPage.js"></script>
<script src="views/static/js/mouseAnimation.js"></script>
