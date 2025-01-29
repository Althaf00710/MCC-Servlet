<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <script src="https://unpkg.com/@tailwindcss/browser@4"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/three.js/110/three.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/vanta/0.5.21/vanta.dots.min.js"></script>
    <link rel="stylesheet" href="views/static/css/loginPage.s.css">
</head>
<body>
<div id="vanta-bg" class="flex h-screen">
    <div class="w-3/5 flex items-center justify-center hidden sm:flex">
        <div id="carousel" class="max-w-2xl px-4">
            <!-- Carousel items will be inserted here by JavaScript -->
        </div>
    </div>

    <div class="w-full sm:w-2/5 flex flex-col items-center justify-center p-6">
        <!-- Login Form -->
        <p class="text-2xl font-semibold text-orange mb-4">Welcome Back,</p>
        <form action="get" class="w-full max-w-sm p-6 rounded-2xl shadow-md bg-orange-50">
            <!-- Email Input -->
            <div class="mb-4">

                <input
                        type="text"
                        id="username"
                        name="username"
                        class="w-full border border-gray-300 rounded-lg px-4 py-2 focus:outline-none focus:ring-1 focus:ring-orange-400"
                        placeholder="Username"
                        required>
            </div>

            <!-- Password Input -->
            <div class="mb-4">

                <input
                        type="password"
                        id="password"
                        name="password"
                        class="w-full border border-gray-300 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-orange-400"
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
            <p class="text-center text-gray-500 text-sm">
                Forgot Password? <a href="#" class="text-orange-500 font-bold hover:underline">We Got You.</a>
            </p>
        </form>
    </div>
</div>


</body>
</html>

<script src="views/static/js/loginPage.js"></script>
