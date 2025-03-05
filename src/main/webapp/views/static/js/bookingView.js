flatpickr("#dateFilter", {
    enableTime: false,
    dateFormat: "Y-m-d",
    static: true
});

function applyDateFilter(bookingDateTime) {
    const url = new URL(window.location.href);
    url.searchParams.set('bookingDateTime', bookingDateTime);
    window.location.href = url.toString();
}

function applyStatusFilter(status) {
    const url = new URL(window.location.href);
    url.searchParams.set('status', status);
    window.location.href = url.toString();
}

document.addEventListener("DOMContentLoaded", function() {
    const searchInput = document.getElementById('searchInput');

    if (searchInput) {
        searchInput.addEventListener('keyup', function() {

            const searchTerm = this.value.toLowerCase().trim();
            const rows = document.querySelectorAll('tbody tr');

            rows.forEach(row => {
                const bookingNumber = row.querySelector('td:first-child').textContent.toLowerCase();
                row.style.display = bookingNumber.includes(searchTerm) ? '' : 'none';
            });
        });
    } else {
        console.error('Search input element not found');
    }
});

document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.dropdown-button').forEach(button => {
        button.addEventListener('click', async function() {
            const icon = this.querySelector('i');
            const row = this.closest('tr');
            const nextRow = row.nextElementSibling;
            const bookingId = row.dataset.bookingId;

            icon.classList.toggle('rotate-180');
            if (!bookingId) {
                console.error('No booking ID found on row');
                return;
            }
            try {
                if (nextRow && nextRow.classList.contains('dropdown-row')) {
                    nextRow.remove();
                } else {
                    // Add loading state
                    const tempRow = document.createElement('tr');
                    tempRow.classList.add('dropdown-row', 'bg-gray-50');
                    tempRow.innerHTML = `
                        <td colspan="9" class="px-6 py-4">
                            <div class="flex justify-center items-center">
                                <div class="animate-spin rounded-full h-6 w-6 border-b-2 border-gray-600"></div>
                            </div>
                        </td>
                    `;
                    row.insertAdjacentElement('afterend', tempRow);

                    // Fetch booking details
                    const response = await fetch(`${window.location.origin}/megacitycab_war_exploded/booking/booking-details?id=${bookingId}`);
                    const data = await response.json();

                    // Remove loading row
                    tempRow.remove();

                    // Create detailed row
                    const newRow = document.createElement('tr');
                    newRow.classList.add('dropdown-row', 'bg-gray-50');
                    newRow.innerHTML = `
                        <td colspan="9" class="px-6 py-4">
                            <div class="grid grid-cols-2 gap-6">
                                <!-- Customer & Driver Info -->
                                <div class="space-y-4">
                                    <div class="bg-white p-4 rounded-lg shadow-sm">
                                        <h3 class="font-semibold text-gray-700 mb-2">Customer Details</h3>
                                        <p class="text-sm">${data.customer.name}</p>
                                        <p class="text-sm text-gray-600">${data.customer.phone}</p>
                                        <p class="text-sm text-gray-600">${data.customer.email}</p>
                                    </div>
                                    
                                    <div class="bg-white p-4 rounded-lg shadow-sm">
                                        <h3 class="font-semibold text-gray-700 mb-2">Driver Details</h3>
                                        <div class="flex items-center space-x-3">
                                            <img src="${data.driver.avatarUrl}" 
                                                 class="w-10 h-10 rounded-full object-cover">
                                            <div>
                                                <p class="text-sm">${data.driver.name}</p>
                                                <p class="text-sm text-gray-600">${data.driver.phoneNumber}</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- Trip Details -->
                                <div class="space-y-4">
                                    <div class="bg-white p-4 rounded-lg shadow-sm">
                                        <h3 class="font-semibold text-gray-700 mb-2">Trip Summary</h3>
                                        <div class="grid grid-cols-2 gap-4 text-sm">
                                            <div>
                                                <p class="text-gray-600">Pickup Location:</p>
                                                <p>${data.pickupLocation}</p>
                                            </div>
                                            <div>
                                                <p class="text-gray-600">Booking Date:</p>
                                                <p>${new Date(data.bookingCreated).toLocaleString()}</p>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="bg-white p-4 rounded-lg shadow-sm">
                                        <h3 class="font-semibold text-gray-700 mb-2">Stops (${data.stops.length})</h3>
                                        <div class="space-y-3">
                                            ${data.stops.map((stop, index) => `
                                                <div class="border-l-4 border-blue-200 pl-3">
                                                    <p class="text-sm font-medium">Stop ${index + 1}</p>
                                                    <p class="text-sm text-gray-600">${stop.location}</p>
                                                    <div class="flex space-x-4 text-xs text-gray-500">
                                                        <span>Distance: ${stop.distanceFromLastStop} km</span>
                                                        <span>Wait Time: ${stop.waitMinutes} mins</span>
                                                    </div>
                                                </div>
                                            `).join('')}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </td>
                    `;

                    row.insertAdjacentElement('afterend', newRow);
                }
            } catch (error) {
                console.error('Error fetching details:', error);
                const errorRow = document.createElement('tr');
                errorRow.classList.add('dropdown-row', 'bg-red-50');
                errorRow.innerHTML = `
                    <td colspan="9" class="px-6 py-4 text-center text-red-600">
                        Failed to load booking details
                    </td>
                `;
                row.insertAdjacentElement('afterend', errorRow);
            }
        });
    });
});