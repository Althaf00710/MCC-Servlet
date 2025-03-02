document.addEventListener('DOMContentLoaded', () => {
    loadCustomers();
    loadCabTypes();
});

let stopCount = 0;

function addStop() {
    stopCount++;
    const stopsContainer = document.getElementById('stopsContainer');
    const stopDiv = document.createElement('div');
    stopDiv.className = 'flex gap-3 items-center';
    stopDiv.innerHTML = `
        <input type="text" name="stop${stopCount}" 
            class="flex-1 px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="Stop location ${stopCount + 1}">
        <button type="button" onclick="this.parentElement.remove()" 
            class="text-red-500 hover:text-red-700">
            <i class="fi fi-rr-trash"></i>
        </button>
    `;
    stopsContainer.appendChild(stopDiv);
}

async function loadCabTypes() {
    try {
        const response = await fetch(`${window.location.origin}/megacitycab_war_exploded/cabtypes/get`);
        const types = await response.json();
        const select = document.getElementById('cabTypeSelect');

        types.forEach(type => {
            const option = document.createElement('option');
            option.value = type.id;
            option.textContent = type.typeName;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Error loading cab types:', error);
    }
}

async function loadCabsByType(typeId) {
    const cabSelect = document.getElementById('cabSelect');
    cabSelect.disabled = !typeId;

    if (!typeId) return;

    try {
        const response = await fetch(`${window.location.origin}/megacitycab_war_exploded/cabs/getByType?typeId=${typeId}`);
        const cabs = await response.json();
        cabSelect.innerHTML = '<option value="">Select Cab</option>';

        cabs.forEach(cab => {
            const option = document.createElement('option');
            option.value = cab.id;
            option.textContent = `${cab.cabBrandName} ${cab.cabName} (${cab.plateNumber})`;
            cabSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error loading cabs:', error);
    }
}

async function loadCustomers() {
    try {
        const response = await fetch(`${window.location.origin}/megacitycab_war_exploded/customers/getCustomers`);
        const customers = await response.json();
        const select = document.getElementById('customerSelect');

        customers.forEach(customer => {
            const option = document.createElement('option');
            option.value = customer.id;
            option.textContent = `${customer.registerNumber} | ${customer.name}`;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Error loading customers:', error);
    }
}

document.getElementById('bookingForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const bookingData = {
        bookingDateTime: document.getElementById('bookingDate').value,
        customerId: document.getElementById('customerSelect').value,
        cabId: document.getElementById('cabSelect').value,
        pickupLocation: document.getElementById('pickupLocation').value,
        dropLocation: document.getElementById('dropLocation').value,
        stops: Array.from(document.querySelectorAll('[name^="stop"]')).map(input => input.value)
    };

    try {
        const response = await fetch(`${window.location.origin}/megacitycab_war_exploded/bookings/add`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(bookingData)
        });

        if (response.ok) {
            alert('Booking created successfully!');
            window.location.href = `${window.location.origin}/megacitycab_war_exploded/bookings/list`;
        } else {
            alert('Error creating booking');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred while saving the booking');
    }
});