// login-modal.js
const loginModal = document.getElementById('loginModal');
const emailForm = document.getElementById('emailForm');
const otpForm = document.getElementById('otpForm');
const registerForm = document.getElementById('registerForm');
const backToLogin = document.getElementById('backToLogin');
const registerBtn = document.getElementById('registerBtn');
const sendOTP = document.getElementById('sendOTP');
const emailInput = emailForm.querySelector('input[type="email"]');
const otpInputs = otpForm.querySelectorAll('input[type="text"]');

// Create error message elements
const emailError = document.createElement('p');
emailError.className = 'text-red-500 text-sm mt-2 hidden';
emailForm.appendChild(emailError);

const otpError = document.createElement('p');
otpError.className = 'text-red-500 text-sm mt-2 text-center hidden';
otpForm.insertBefore(otpError, otpForm.querySelector('button'));

// Modal Toggle
document.getElementById('loginBtn')?.addEventListener('click', () => {
    resetForms();
    loginModal.classList.remove('hidden');
});

// OTP Input Handling
otpInputs.forEach((input, index) => {
    input.addEventListener('input', (e) => {
        if (e.target.value.length === 1) {
            if (index < otpInputs.length - 1) {
                otpInputs[index + 1].focus();
            }
        }
    });

    input.addEventListener('keydown', (e) => {
        if (e.key === 'Backspace' && e.target.value === '' && index > 0) {
            otpInputs[index - 1].focus();
        }
    });
});

// Send OTP Handler
sendOTP.addEventListener('click', async () => {
    const email = emailInput.value.trim();

    if (!validateEmail(email)) {
        showError(emailError, 'Please enter a valid email address');
        return;
    }

    try {
        const response = await fetch(`${window.location.origin}/megacitycab_war_exploded/customers/send-otp`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `email=${encodeURIComponent(email)}`
        });

        const data = await response.json();

        if (data.status === 'success') {
            hideError(emailError);
            switchForms(emailForm, otpForm);
        } else {
            showError(emailError, data.message || 'Failed to send OTP');
        }
    } catch (error) {
        showError(emailError, 'Network error - please try again');
    }
});

// Verify OTP Handler
document.querySelector('#otpForm button').addEventListener('click', async () => {
    const email = emailInput.value.trim();
    const otp = Array.from(otpInputs).map(input => input.value).join('');

    if (otp.length !== 4) {
        showError(otpError, 'Please enter a 4-digit OTP');
        return;
    }

    try {
        const response = await fetch(`${window.location.origin}/megacitycab_war_exploded/customers/verify-otp`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `email=${encodeURIComponent(email)}&otp=${encodeURIComponent(otp)}`
        });

        const data = await response.json();

        if (data.status === 'success') {
            handleSuccessfulLogin(data.customer);
        } else if (data.status === 'Customer Not Exist') {
            switchToRegistration(email);
        } else {
            showError(otpError, data.message || 'Invalid OTP');
        }
    } catch (error) {
        showError(otpError, 'Network error - please try again');
    }
});

// Registration Form Handler
document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const formData = new FormData(e.target);
    const data = Object.fromEntries(formData.entries());

    if (!validateRegistration(data)) {
        alert('Please fill all required fields');
        return;
    }

    try {
        const response = await fetch('/customers/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
        });

        const result = await response.json();

        if (result.success) {
            window.location.reload(); // Or redirect to profile
        } else {
            alert(result.message || 'Registration failed');
        }
    } catch (error) {
        alert('Registration failed - please try again');
    }
});

// Toggle Password Visibility
document.querySelectorAll('.password-toggle').forEach(button => {
    button.addEventListener('click', (e) => {
        const input = e.currentTarget.previousElementSibling;
        input.type = input.type === 'password' ? 'text' : 'password';
        e.currentTarget.querySelector('svg').classList.toggle('text-white');
    });
});

// Back to Login Handler
backToLogin.addEventListener('click', resetForms);

// Modal Click Outside Handler
loginModal.addEventListener('click', (e) => {
    if (e.target === loginModal) {
        loginModal.classList.add('hidden');
        resetForms();
    }
});

// Helper Functions
function validateEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function validateRegistration(data) {
    return data.firstName && data.lastName && data.phoneNumber && data.email && data.password;
}

function switchForms(hideForm, showForm) {
    hideForm.classList.add('hidden');
    showForm.classList.remove('hidden');
    backToLogin.classList.remove('hidden');
}

function showError(element, message) {
    element.textContent = message;
    element.classList.remove('hidden');
}

function hideError(element) {
    element.textContent = '';
    element.classList.add('hidden');
}

function resetForms() {
    emailForm.classList.remove('hidden');
    otpForm.classList.add('hidden');
    registerForm.classList.add('hidden');
    backToLogin.classList.add('hidden');
    hideError(emailError);
    hideError(otpError);
    emailInput.value = '';
    otpInputs.forEach(input => input.value = '');
}

function handleSuccessfulLogin(customer) {
    // Store customer data or redirect
    console.log('Logged in customer:', customer);
    window.location.href = '/dashboard'; // Example redirect
}

function switchToRegistration(email) {
    otpForm.classList.add('hidden');
    registerForm.classList.remove('hidden');
    registerForm.querySelector('input[type="email"]').value = email;
}