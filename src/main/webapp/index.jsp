<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <script src="https://unpkg.com/@tailwindcss/browser@4"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/three.js/110/three.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/vanta/0.5.21/vanta.dots.min.js"></script>
    <style>
        body, html {
            margin: 0;
            padding: 0;
            height: 100%;
            overflow: hidden;
        }
        #vanta-bg {
            width: 100%;
            height: 100%;
        }
    </style>
</head>
<body>
<div id="vanta-bg" class="flex h-screen">
    <div class="w-3/5 flex items-center justify-center hidden sm:flex">

    </div>

    <div class="w-full sm:w-2/5 flex flex-col items-center justify-center p-6">
        <p class="text-2xl font-semibold text-white mb-4">Welcome Back,</p>

        <!-- Login Form -->
        <form action="get" class="w-full max-w-sm bg-white p-6 rounded-lg shadow-md">
            <!-- Email Input -->
            <div class="mb-4">
                <label for="email" class="block text-gray-700 font-bold mb-2">Email</label>
                <input
                        type="email"
                        id="email"
                        name="email"
                        class="w-full border border-gray-300 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-orange-400"
                        placeholder="Enter your email"
                        required>
            </div>

            <!-- Password Input -->
            <div class="mb-4">
                <label for="password" class="block text-gray-700 font-bold mb-2">Password</label>
                <input
                        type="password"
                        id="password"
                        name="password"
                        class="w-full border border-gray-300 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-orange-400"
                        placeholder="Enter your password"
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

<script>
    document.addEventListener("DOMContentLoaded", function () {
        VANTA.DOTS({
            el: "#vanta-bg",
            mouseControls: true,
            touchControls: true,
            gyroControls: false,
            minHeight: 200.00,
            minWidth: 200.00,
            scale: 1.00,
            scaleMobile: 1.00,
            color: 0xff7100,
            backgroundColor: 0xf7f7f7,
            size: 5.00,
            spacing: 50.00,
            showLines: false
        });
    });
</script>
</body>
</html>
