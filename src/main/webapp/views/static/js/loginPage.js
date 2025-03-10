
    document.addEventListener("DOMContentLoaded", function () {
    // Vanta.js initialization
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

    // Carousel functionality
    const messages = [
{
    prefix: "Access Customer Insights",
    text: "View Registrations, Preferences and Feedback in One Dashboard."
},
{
    prefix: "Secure and Streamlined",
    text: "Process Payments, Apply Discounts and Generate Bills in Seconds."
},
{
    prefix: "Stay in Control",
    text: "Monitor Driver Availability, Cab Maintenance and Daily Revenue Trends."
}
    ];

    const carousel = document.getElementById('carousel');
    let currentIndex = 0;

    function createCarouselItem(message) {
    const item = document.createElement('div');
    item.className = 'carousel-item';
    item.innerHTML = `
                <div class="text-orange-500 text-5xl font-semibold mb-2">${message.prefix}</div>
                <div class="text-gray-700 text-xl font-normal">${message.text}</div>
            `;
    return item;
}

    function showNextMessage() {
    // Remove current active item
    while (carousel.firstChild) {
    carousel.removeChild(carousel.firstChild);
}

    // Create and add new item
    const newItem = createCarouselItem(messages[currentIndex]);
    carousel.appendChild(newItem);

    // Trigger reflow to enable transition
    void newItem.offsetWidth;
    newItem.classList.add('active');

    // Update index
    currentIndex = (currentIndex + 1) % messages.length;
}

    // Initial show
    showNextMessage();

    // Start interval
    setInterval(showNextMessage, 5000);
});


// Reset Password Functions
    function showUsernameForm(event) {
        event.preventDefault();
        document.getElementById('form-content').innerHTML = `
        <form id="username-form" onsubmit="handleUsernameSubmit(event)">
            <p class="text-2xl font-semibold text-gray-700"><span class="text-orange-500">Recover</span> Account</p>
            <span class="text-sm text-gray-700 mb-4 font-light">Enter username/email to receive OTP</span>
            
            <div class="my-4 relative">
                <div class="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none text-gray-400">
                    <i class="fi fi-rr-user text-lg"></i>
                </div>
                <input type="text" id="usernameOrEmail" class="w-full border rounded-lg px-4 py-2 pl-10 focus:ring-orange-400" placeholder="Username/Email" required>
            </div>

            <button type="submit" class="w-full bg-orange-500 text-white py-2 rounded-lg hover:bg-orange-600">
                Send OTP
            </button>
            
            <p class="text-center text-gray-500 text-sm mt-4">
                Remember password? <a href="#" onclick="showLoginForm(event)" class="text-orange-500 font-bold hover:underline">Login</a>
            </p>
        </form>
    `;
    }

    function showOTPForm(usernameOrEmail) {
        document.getElementById('form-content').innerHTML = `
    <form id="otp-form" onsubmit="handleOTPSubmit(event)">
        <input type="hidden" id="usernameOrEmail" value="${usernameOrEmail}">
        <!-- rest of the OTP form remains the same -->
        <div class="flex gap-2 justify-center mb-6">
            ${Array.from({length: 4}, (_, i) => `
                <input type="text" id="otp${i+1}" 
                    class="w-12 h-12 text-center border rounded-lg focus:ring-orange-400"
                    maxlength="1" 
                    oninput="handleOTPInput(this, ${i+1})"
                    onkeydown="handleOTPBackspace(event, ${i+1})">
            `).join('')}
        </div>
            <button type="submit" class="w-full bg-orange-500 text-white py-2 rounded-lg hover:bg-orange-600">
                Verify
            </button>
            
            <p class="text-center text-gray-500 text-sm mt-4">
                Resend OTP? <a href="#" onclick="resendOTP(event)" class="text-orange-500 font-bold hover:underline">Click here</a>
            </p>
        </form>
    `;
    }

    function showResetPasswordForm(usernameOrEmail) {
        document.getElementById('form-content').innerHTML = `
        <form id="reset-form" onsubmit="handleResetSubmit(event)">
            <p class="text-2xl font-semibold text-gray-700"><span class="text-orange-500">New</span> Password</p>
            <span class="text-sm text-gray-700 mb-4 font-light">Create your new password</span>
            <input type="hidden" id="usernameOrEmail" value="${usernameOrEmail}">
            <div class="my-4 relative">
                <div class="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none text-gray-400">
                    <i class="fi fi-rr-lock text-lg"></i>
                </div>
                <input type="password" id="newPassword" class="w-full border rounded-lg px-4 py-2 pl-10 focus:ring-orange-400" placeholder="New Password" required>
            </div>

            <div class="mb-4 relative">
                <div class="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none text-gray-400">
                    <i class="fi fi-rr-lock text-lg"></i>
                </div>
                <input type="password" id="confirmPassword" class="w-full border rounded-lg px-4 py-2 pl-10 focus:ring-orange-400" placeholder="Confirm Password" required>
            </div>

            <button type="submit" onclick="handleResetSubmit(event)" class="w-full bg-orange-500 text-white py-2 rounded-lg hover:bg-orange-600">
                Reset Password
            </button>
        </form>
    `;
    }

    let loginFormHTML = document.getElementById('login-form').outerHTML;

    function showLoginForm(event) {
        event.preventDefault();
        document.getElementById('form-content').innerHTML = loginFormHTML;
    }

    // OTP Input Handling
    function handleOTPInput(input, index) {
        input.value = input.value.replace(/\D/g, '');
        if(input.value && index < 4) {
            document.getElementById(`otp${index + 1}`).focus();
        }
    }

    function handleOTPBackspace(e, index) {
        if(e.key === 'Backspace' && !e.target.value && index > 1) {
            document.getElementById(`otp${index - 1}`).focus();
        }
    }

    async function handleUsernameSubmit(event) {
        event.preventDefault();
        const input = document.getElementById('usernameOrEmail').value;

        try {
            const response = await fetch(`${window.location.origin}/megacitycab_war_exploded/users/sendOtp?input=${encodeURIComponent(input)}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ input })
            });
            const data = await response.json();

            if (data.success) {
                showOTPForm(input);
            } else {
                alert(data.error || "Failed to send OTP.");
            }
        } catch (error) {
            alert("Network error. Please try again.");
        }
    }

    async function handleOTPSubmit(event) {
        event.preventDefault();
        const input = document.getElementById('usernameOrEmail').value;
        const otp = [...Array(4).keys()].map(i => document.getElementById(`otp${i+1}`).value).join('');

        try {
            const response = await fetch(`${window.location.origin}/megacitycab_war_exploded/users/verifyOtp?input=${encodeURIComponent(input)}&otp=${encodeURIComponent(otp)}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ input, otp })
            });
            const data = await response.json();

            if (data.success) {
                showResetPasswordForm(input);
            } else {
                alert(data.error || "Invalid OTP.");
            }
        } catch (error) {
            alert("Network error. Please try again.");
        }
    }

    async function handleResetSubmit(event) {
        event.preventDefault();
        const input = document.getElementById('usernameOrEmail').value;
        const newPassword = document.getElementById('newPassword').value;
        const confirmPassword = document.getElementById('confirmPassword').value;

        if (newPassword !== confirmPassword) {
            alert("Passwords do not match.");
            return;
        }

        try {
            const response = await fetch(`${window.location.origin}/megacitycab_war_exploded/users/resetPassword`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ input, newPassword })
            });
            const data = await response.json();

            if (data.success) {
                alert("Password reset successful!");
                showLoginForm(event);
            } else {
                alert(data.error || "Failed to reset password.");
            }
        } catch (error) {
            alert("Network error. Please try again.");
        }
    }

    async function resendOTP(event) {
        event.preventDefault();
        const input = document.getElementById('usernameOrEmail').value;

        try {
            const response = await fetch(`${window.location.origin}/megacitycab_war_exploded/users/sendOtp`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ input })
            });
            const data = await response.json();

            alert(data.success ? "New OTP sent!" : data.error || "Failed to resend OTP.");
        } catch (error) {
            alert("Network error. Please try again.");
        }
    }
