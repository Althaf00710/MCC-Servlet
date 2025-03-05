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
                                            <img src="${window.location.origin}/megacitycab_war_exploded/${data.driver.avatarUrl || '/views/static/images/defaultAvatar.png'}" 
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
                                    <!-- Pickup -->
                                    <div class="step">
                                        <div class="flex items-center gap-2">
                                            <div class="w-8 h-8 bg-orange-500 text-white rounded-full flex items-center justify-center">
                                                P
                                            </div>
                                            <div>
                                                <span class="font-semibold">Pickup</span>
                                                <span class="text-sm text-gray-500 ml-2 font-semibold">${data.pickupLocation}</span>
                                            </div>
                                        </div>
                                    </div>
                        
                                    <!-- Stops -->
                                    ${data.stops.map((stop, index) => `
                                        <div class="step">
                                            <div class="ml-8 pl-4 border-l-2 border-gray-200">
                                                <div class="my-2 text-sm">
                                                    <div class="flex gap-4 text-gray-600">
                                                        <span>Distance: ${stop.distanceFromLastStop} km</span>
                                                    
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="flex items-center gap-2">
                                                <div class="w-8 h-8 ${index === data.stops.length - 1 ? 'bg-blue-500' : 'bg-gray-500'} text-white rounded-full flex items-center justify-center">
                                                    ${index === data.stops.length - 1 ? 'D' : index + 1}
                                                </div>
                                                <div>
                                                    <span class="font-semibold">${index === data.stops.length - 1 ? 'Drop' : `Stop ${index + 1}`}</span>
                                                    <span class="text-sm text-gray-500 ml-2 font-semibold">${stop.location}</span> 
                                                    ${stop.waitMinutes > 0 ?
                                                    `<span class="text-gray-500 text-sm ml-2">Wait Time: ${stop.waitMinutes} mins</span>` : ''}
                                                </div>
                                            </div>                                       
                                        </div>
                                    `).join('')}
                        
                                    <!-- Total Distance -->
                                    <div class="mt-4 p-2 bg-gray-100 rounded-lg">
                                        <span class="font-semibold">Total Distance:</span> 
                                        ${data.stops.reduce((sum, stop) => sum + stop.distanceFromLastStop, 0).toFixed(2)} km
                                        
                                        ${data.stops.some(stop => stop.waitMinutes > 0) ? `
                                            <span class="font-semibold">Total Wait Time:</span> 
                                            ${data.stops.reduce((sum, stop) => sum + stop.waitMinutes, 0)} Minutes
                                        ` : ''}
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