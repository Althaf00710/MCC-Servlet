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
                        <td colspan="9" class="px-2 py-2">
                            <div class="grid grid-cols-2 gap-6">
                                <!-- Customer & Driver Info -->
                                <div class="space-y-4">
                                    <div id="map" class="w-full h-full rounded-xl shadow-md"></div>
                                </div>

                                <!-- Trip Details -->
                                <div class="space-y-4">
                                    <!-- Heading -->
                                    <h3 class="text-md text-center font-semibold text-gray-700">Trip Details</h3>
                            
                                    <!-- Driver and Customer Side by Side -->
                                    <div class="grid grid-cols-2 gap-8">
                                        <!-- Driver -->
                                        <div class="bg-white p-1 rounded-full shadow-lg">
                                            <div class="flex items-center space-x-3">
                                                <img src="${window.location.origin}/megacitycab_war_exploded/${data.driver.avatarUrl || '/views/static/images/defaultAvatar.png'}" 
                                                     class="w-14 h-14 rounded-full object-cover">
                                                <div>
                                                    <h3 class="text-sm font-semibold text-gray-700">Driver</h3>
                                                    <p class="text-sm">${data.driver.name}</p>
                                                    <p class="text-sm text-gray-600">${data.driver.phoneNumber}</p>
                                                </div>
                                            </div>
                                        </div>
                            
                                        <!-- Customer -->
                                        <div class="bg-white p-1 rounded-full shadow-lg">
                                            <div class="flex items-center space-x-3">
                                                <img src="${window.location.origin}/megacitycab_war_exploded/views/static/images/defaultAvatar.png" 
                                                     class="w-14 h-14 rounded-full object-cover">
                                                <div class="bg-white rounded-lg">
                                                    <h3 class="text-sm font-semibold text-gray-700">Customer</h3>
                                                    <p class="text-sm">${data.customer.name}</p>
                                                    <p class="text-sm text-gray-600">${data.customer.phone}</p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- Pickup and Stops -->
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
                                                <div class="ml-8 pl-4 border-l-2 border-gray-300">
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
                                        <div class="mt-4 p-2 bg-gray-200 rounded-2xl text-center">
                                            <span class="font-semibold">Total Distance:</span> 
                                            <span class="font-medium text-gray-600">${data.stops.reduce((sum, stop) => sum + stop.distanceFromLastStop, 0).toFixed(2)} km</span>
                                            
                                            ${data.stops.some(stop => stop.waitMinutes > 0) ? `
                                                <span class="font-semibold">Total Wait Time:</span> 
                                                ${data.stops.reduce((sum, stop) => sum + stop.waitMinutes, 0)} Minutes
                                            ` : ''}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </td>
                    `;

                    row.insertAdjacentElement('afterend', newRow);
                    initializeMap(data);
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

async function initializeMap(data) {
    try {
        // Fetch the API key from the server
        const response = await fetch(`${window.location.origin}/megacitycab_war_exploded/getKey`);
        const { apiKey } = await response.json();

        // Load Google Maps script dynamically
        await loadGoogleMaps(apiKey);

        // Initialize the map with data
        initMap(data);
    } catch (error) {
        console.error('Error initializing map:', error);
    }
}

function loadGoogleMaps(apiKey) {
    return new Promise((resolve, reject) => {
        if (window.google && window.google.maps) {
            resolve();
            return;
        }

        const script = document.createElement('script');
        script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&libraries=places`;
        script.async = true;
        script.defer = true;
        script.onload = resolve;
        script.onerror = reject;
        document.head.appendChild(script);
    });
}

function initMap(data) {
    const mapDiv = document.getElementById('map');
    if (!mapDiv) {
        console.error('Map div not found');
        return;
    }

    const map = new google.maps.Map(mapDiv, {
        zoom: 10,
        center: { lat: 7.8731, lng: 80.7718 }
    });

    const locations = [
        { placeId: data.pickupPlaceId, label: 'P', title: `Pickup: ${data.pickupLocation}` },
        ...data.stops.map((stop, index) => ({
            placeId: stop.placeId,
            label: index === data.stops.length - 1 ? 'D' : `${index + 1}`,
            title: index === data.stops.length - 1 ? `Drop: ${stop.location}` : `Stop ${index + 1}: ${stop.location}`
        }))
    ];

    const bounds = new google.maps.LatLngBounds();
    const markers = [];
    const placesService = new google.maps.places.PlacesService(map);

    // Track processed markers
    let processedCount = 0;

    locations.forEach((location, index) => {
        placesService.getDetails({ placeId: location.placeId }, (place, status) => {
            processedCount++; // Increment counter regardless of status

            if (status === google.maps.places.PlacesServiceStatus.OK) {
                const marker = new google.maps.Marker({
                    position: place.geometry.location,
                    map: map,
                    label: {
                        text: location.label,
                        color: 'gray',
                        fontSize: '12px',
                        fontWeight: 'bold'
                    },
                    title: location.title,
                    icon: {
                        url: index === 0
                            ? `${window.location.origin}/megacitycab_war_exploded/views/static/images/pickup.png`
                            : (index === locations.length - 1
                                ? `${window.location.origin}/megacitycab_war_exploded/views/static/images/drop.png`
                                : `${window.location.origin}/megacitycab_war_exploded/views/static/images/stop.png`),
                        scaledSize: new google.maps.Size(32, 32)
                    }
                });
                markers.push(marker);
                bounds.extend(place.geometry.location);
            } else {
                console.error(`Failed to get details for ${location.title}: ${status}`);
            }

            // Fit bounds only after all markers are processed
            if (processedCount === locations.length) {
                map.fitBounds(bounds);
            }
        });
    });

    const directionsService = new google.maps.DirectionsService();
    const directionsRenderer = new google.maps.DirectionsRenderer({
        map: map,
        suppressMarkers: true
    });

    const waypoints = data.stops.slice(0, -1).map(stop => ({
        location: { placeId: stop.placeId },
        stopover: true
    }));

    const origin = { placeId: data.pickupPlaceId };
    const destination = { placeId: data.stops[data.stops.length - 1].placeId };

    directionsService.route(
        {
            origin: origin,
            destination: destination,
            waypoints: waypoints,
            travelMode: google.maps.TravelMode.DRIVING
        },
        (response, status) => {
            if (status === google.maps.DirectionsStatus.OK) {
                directionsRenderer.setDirections(response);
            } else {
                console.error('Directions request failed due to ' + status);
            }
        }
    );
}

function confirmCancel(bookingId) {
    Swal.fire({
        title: "Are you sure?",
        text: "You won't be able to revert this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#ff6c2e",
        cancelButtonColor: "#707070",
        confirmButtonText: "Yes, cancel it!"
    }).then((result) => {
        if (result.isConfirmed) {
            document.getElementById(`cancelForm-${bookingId}`).submit();
        }
    });
}

async function openCompleteModal(bookingId, bookingNumber) {
    const modal = document.getElementById("completeModal");
    const overlay = document.getElementById("modalOverlay");
    const modalContent = document.getElementById("modalContent");

    try {
        const response = await fetch(`${window.location.origin}/megacitycab_war_exploded/booking/booking-details?id=${bookingId}`);
        const booking = await response.json();

        window.currentBookingId = bookingId;
        window.currentTax = booking.companyData.tax;
        // Populate Modal Content
        modalContent.innerHTML = `
            <div class="flex justify-between items-center pb-2 mb-1">
                <h2 class="text-lg font-bold text-gray-800 flex-1 text-center">Billing <span class="font-medium text-gray-500">${bookingNumber}</span></h2>
                <button onclick="closeModal()" class="px-2 py-2 bg-gray-300 rounded-full text-sm hover:bg-gray-400 text-gray-500"><i class="fi fi-rr-cross"></i></button>
            </div>
            
            <div class="flex justify-between">
                <!-- Left Side: Customer Details -->
                <div class="w-1/2">
                    <span class="block font-semibold text-xs text-gray-700">Bill To:</span>
                    <span class="block text-gray-600 text-sm">${booking.customer.name}</span>
                    <span class="block text-gray-600 text-sm">${booking.customer.phone}</span>
                </div>
            
                <!-- Right Side: Driver & Cab Type -->
                <div class="w-1/2 text-right">
                    <span class="block font-semibold text-gray-700 text-xs">Driver:</span>
                    <span class="block text-gray-600 text-sm">${booking.driver.name}</span>
                    <p class="text-gray-600 text-sm">${booking.cabType.typeName}</p>
                </div>
            </div>
            <hr class="w-3/4 border-t border-gray-200 my-4 mx-auto">
        
            <div class="step">
                <div class="ml-8 pl-4 border-l-2 border-gray-300"></div>
                <div class="flex items-center gap-2">
                    <div class="w-8 h-8 bg-orange-500 text-white rounded-full flex items-center justify-center">P</div>
                    <div>
                        <span class="text-sm text-gray-500 ml-2 font-semibold">${booking.pickupLocation}</span>
                    </div>
                </div>
            </div>
        
            <div id="stops-container">
                ${booking.stops.map((stop, index) => `
                    <div class="step">
                        <div class="ml-8 pl-4 border-l-2 border-gray-300">
                            <div class="my-2 text-md">
                                <div class="flex gap-4 text-gray-600">
                                    <span>Distance: <input type="number" class="distance-input w-14 border-2 border-gray-200 rounded-lg" value="${stop.distanceFromLastStop}" data-index="${index}" step="0.1" min="0"> km</span>
                                </div>
                            </div>
                        </div>
                        <div class="flex items-center gap-2">
                            <div class="w-8 h-8 ${index === booking.stops.length - 1 ? 'bg-blue-500' : 'bg-gray-500'} text-white rounded-full flex items-center justify-center">
                                ${index === booking.stops.length - 1 ? 'D' : index + 1}
                            </div>
                            <div>
                                <span class="text-sm text-gray-500 ml-2 font-semibold">
                                    ${index !== booking.stops.length - 1 ?
            `${stop.location.slice(0, 20)}${stop.location.length > 20 ? '...' : ''}` : stop.location}
                                </span> 
                                ${index !== booking.stops.length - 1 ? `
                                    <span class="text-gray-500 text-sm ml-2">
                                        Wait Time: <input type="number" class="wait-input w-12 border-2 border-gray-200 rounded-lg" value="${stop.waitMinutes}" data-index="${index}" step="1" min="0"> mins
                                    </span>
                                ` : ''}
                            </div>
                        </div>                                       
                    </div>
                `).join('')}
            </div>
            
            <div class="mt-4 flex justify-evenly bg-gray-100 rounded-lg py-1">
              <span class="text-gray-600">
                <strong>Distance:</strong> 
                <span id="total-distance">${booking.stops.reduce((total, stop) => total + stop.distanceFromLastStop, 0).toFixed(2)}</span> km
              </span>
              <span class="text-gray-600">
                <strong>Wait Time:</strong> 
                <span id="total-wait">${booking.stops.reduce((total, stop) => total + stop.waitMinutes, 0)}</span> mins
              </span>
            </div>
            <hr class="w-3/4 border-t border-gray-200 my-4 mx-auto"> 
            <table class="min-w-full divide-y divide-gray-300 border-1 border-gray-300 rounded-lg overflow-hidden">
                <thead class="bg-gray-200 sticky">
                    <tr>
                        <th class="px-6 py-2 text-left text-xs font-medium text-gray-600 uppercase tracking-wider">Bill Name</th>
                        <th class="px-6 py-2 text-xs font-medium text-gray-600 uppercase tracking-wider">Amount</th>
                    </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-300">
                    <tr class="border-b border-gray-300">
                        <td class="px-4 py-2 text-left whitespace-nowrap font-normal text-sm text-gray-700">Distance Cost: <span class="text-xs font-semibold">${booking.cabType.baseFare}/= per km</span></td>
                        <td class="px-4 py-2 text-right whitespace-nowrap font-normal text-sm text-gray-700">Rs.<span id="distance-cost">0.00</span></td>
                    </tr>
                    <tr class="border-b border-gray-300">
                        <td class="px-4 py-2 text-left whitespace-nowrap font-normal text-sm text-gray-700">Wait Time Cost: <span class="text-xs font-semibold">${booking.cabType.baseWaitTimeFare}/= per min</span></td>
                        <td class="px-4 py-2 text-right whitespace-nowrap font-normal text-sm text-gray-700">Rs.<span id="wait-cost">0.00</span></td>
                    </tr>
                    <tr class="border-b border-gray-300">
                        <td class="px-4 py-2 text-left whitespace-nowrap font-medium text-sm text-gray-700">Subtotal</td>
                        <td class="px-4 py-2 text-right whitespace-nowrap font-medium text-sm text-gray-700">Rs.<span id="subtotal">0.00</span></td>
                    </tr>
                </tbody> 
            </table>
        
            <div class="m-2 bg-gray-100 border border-orange-400 px-4 py-2 flex justify-evenly rounded-lg">
                <label for="discount-percentage" class="text-gray-600">Discount:</label>
                <div>
                    <input type="number" id="discount-percentage" value="${booking.companyData.discount}" step="0.1" min="0" max="100" class="w-12 border-2 bg-white border-gray-200 rounded-lg">
                    <span class="text-gray-600">%</span>
                </div>
                <div>
                    <input type="number" id="discount-amount" value="0" step="0.01" min="0" class="w-20 border-2 bg-white border-gray-200 rounded-lg">
                    <span class="text-gray-600">Rs.</span>
                </div>
            </div>
            
            <table class="min-w-full divide-y divide-gray-300 border-1 border-gray-300 rounded-lg overflow-hidden">
                <tbody class="bg-white">
                    <tr>
                        <td class="px-4 py-1 text-left whitespace-nowrap font-normal text-sm text-gray-700">Discount</td>
                        <td class="px-4 py-1 text-right whitespace-nowrap font-normal text-sm text-gray-700">Rs.<span id="discount-display">0.00</span></td>
                    </tr>
                    <tr>
                        <td class="px-4 py-1 text-left whitespace-nowrap font-normal text-sm text-gray-700">Tax Amount ${booking.companyData.tax}%</td>
                        <td class="px-4 py-1 text-right whitespace-nowrap font-normal text-sm text-gray-700">Rs.<span id="tax-amount">0.00</span></td>
                    </tr>
                    <tr>
                        <td class="px-4 py-1 text-left whitespace-nowrap font-medium text-sm text-gray-700">Total Amount</td>
                        <td class="px-4 py-1 text-right whitespace-nowrap font-medium text-sm text-gray-700">Rs.<span id="total-amount">0.00</span></td>
                    </tr>
                </tbody> 
            </table>
            <div class="mt-6 flex justify-center w-full rounded-2xl border border-white">
                <button onclick="openPaymentModal()" class="px-4 py-2 bg-orange-400 hover:bg-orange-500 text-sm w-full text-gray-100 rounded-2xl font-semibold flex justify-center items-center">
                    <i class="fi fi-rr-angle-left text-white mr-2"></i> <span>Select Payment</span>
                </button>
            </div>
        `;

        // Show Modal
        overlay.classList.remove("hidden");
        modal.classList.remove("hidden");
        setTimeout(() => {
            modal.classList.remove("translate-x-full"); // Slide-in effect
            overlay.classList.add("opacity-100"); // Fade-in effect
        }, 10);

        // Attach event listeners for real-time updates
        attachEventListeners(booking);

    } catch (error) {
        console.error("Error fetching booking details:", error);
    }
}

function attachEventListeners(booking) {
    const distanceInputs = document.querySelectorAll('.distance-input');
    const waitInputs = document.querySelectorAll('.wait-input');
    const discountPercentageInput = document.getElementById('discount-percentage');
    const discountAmountInput = document.getElementById('discount-amount');

    function updateBilling() {
        let totalDistance = 0;
        let totalWait = 0;

        // Calculate totals from inputs
        distanceInputs.forEach(input => {
            totalDistance += parseFloat(input.value) || 0;
        });

        waitInputs.forEach(input => {
            totalWait += parseInt(input.value) || 0;
        });

        // Billing parameters
        const baseFare = parseFloat(booking.cabType.baseFare);
        const waitMinuteFare = parseFloat(booking.cabType.baseWaitTimeFare);
        const taxRate = parseFloat(booking.companyData.tax) / 100;

        // Calculate costs
        const distanceCost = totalDistance * baseFare;
        const waitCost = totalWait * waitMinuteFare;
        const subtotal = distanceCost + waitCost;

        // Handle discount
        let discountPercentage = parseFloat(discountPercentageInput.value) || 0;
        let discountAmount = parseFloat(discountAmountInput.value) || 0;

        if (discountAmount > 0) {
            // If amount is edited, update percentage
            discountPercentage = (discountAmount / subtotal) * 100;
            discountPercentageInput.value = discountPercentage.toFixed(2);
        } else {
            // If percentage is edited, update amount
            discountAmount = (discountPercentage / 100) * subtotal;
            discountAmountInput.value = discountAmount.toFixed(2);
        }

        // Calculate tax and total
        const taxAmount = (subtotal - discountAmount) * taxRate;
        const totalAmount = subtotal - discountAmount + taxAmount;

        // Update UI
        document.getElementById('total-distance').textContent = totalDistance.toFixed(2);
        document.getElementById('total-wait').textContent = totalWait;
        document.getElementById('distance-cost').textContent = distanceCost.toFixed(2);
        document.getElementById('wait-cost').textContent = waitCost.toFixed(2);
        document.getElementById('subtotal').textContent = subtotal.toFixed(2);
        document.getElementById('discount-display').textContent = discountAmount.toFixed(2);
        document.getElementById('tax-amount').textContent = taxAmount.toFixed(2);
        document.getElementById('total-amount').textContent = totalAmount.toFixed(2);
    }

    // Attach event listeners to inputs
    distanceInputs.forEach(input => {
        input.addEventListener('input', updateBilling);
    });
    waitInputs.forEach(input => {
        input.addEventListener('input', updateBilling);
    });
    discountPercentageInput.addEventListener('input', () => {
        discountAmountInput.value = 0; // Reset amount when percentage changes
        updateBilling();
    });
    discountAmountInput.addEventListener('input', () => {
        discountPercentageInput.value = 0; // Reset percentage when amount changes
        updateBilling();
    });

    // Initial calculation
    updateBilling();
}

function closeModal() {
    const modal = document.getElementById("completeModal");
    const overlay = document.getElementById("modalOverlay");
    const paymentModal = document.getElementById("PaymentModal");

    modal.classList.add("translate-x-full");
    paymentModal.classList.add("translate-x-full");
    overlay.classList.remove("opacity-100");

    setTimeout(() => {
        overlay.classList.add("hidden");
        modal.classList.add("hidden");
        paymentModal.classList.add("hidden");
    }, 300); // Hide after animation
}

function openPaymentModal() {
    const paymentModal = document.getElementById("PaymentModal");
    const totalAmount = parseFloat(document.getElementById('total-amount').textContent);

    // Populate payment modal content
    document.getElementById("paymentModalContent").innerHTML = `
        <div class="flex justify-between items-center mb-1">
            <div>
                <label class="block text-gray-700 font-semibold text-xl">Total Amount</label>
            </div>
            <div class="flex justify-around items-center">
                <div id="payment-total-amount" class="block text-gray-700 font-semibold text-xl">Rs. ${totalAmount.toFixed(2)}</div>
                <button onclick="closePaymentModal()" class="text-gray-500 hover:text-gray-700 text-sm bg-gray-300 hover:bg-gray-400 rounded-full p-2 ml-4">
                    <i class="fi fi-rr-angle-right"></i>
                </button>
            </div>
        </div>
        
        <div>
            <div class="flex justify-evenly border border-gray-200 rounded-lg py-3">
                <div class="mb-3">
                    <label for="cash" class="block text-gray-400 text-sm font-semibold uppercase">Cash</label>
                    <input type="number" id="cash" class="w-24 border-2 border-gray-200 rounded-lg p-2" step="0.01" min="0" placeholder="0.00" value="${totalAmount.toFixed(2)}">
                </div>
                <div class="mb-3">
                    <label for="card" class="block text-gray-400 text-sm font-semibold uppercase">Card</label>
                    <input type="number" id="card" class="w-24 border-2 border-gray-200 rounded-lg p-2" step="0.01" min="0" placeholder="0.00">
                </div>
                <div class="mb-3">
                    <label for="deposit" class="block text-gray-400 text-sm font-semibold uppercase">Deposit</label>
                    <input type="number" id="deposit" class="w-24 border-2 border-gray-200 rounded-lg p-2" step="0.01" min="0" placeholder="0.00">
                </div>
            </div>
            <div class="mt-2">
                <span class="text-gray-500 font-medium">Remaining to Pay: Rs.<span id="balance" class="font-semibold text-gray-700">0.00</span></span>
            </div>
        </div>
        <div class="mt-6 flex justify-center">
            <button onclick="saveBill()" class="px-4 py-2 bg-orange-400 hover:bg-orange-500 text-sm w-full text-gray-100 rounded-2xl font-semibold flex justify-center items-center">
                <i class="fi fi-rr-check text-white mr-2"></i>
                <span>Save Bill</span>
            </button>
        </div>
    `;

    paymentModal.classList.remove("hidden");
    setTimeout(() => {
        paymentModal.classList.remove("translate-x-full");
    }, 10);

    // Attach event listeners for payment inputs
    const cashInput = document.getElementById('cash');
    const cardInput = document.getElementById('card');
    const depositInput = document.getElementById('deposit');
    const balanceSpan = document.getElementById('balance');
    const paymentTotalSpan = document.getElementById('payment-total-amount');

    function updateBalance() {
        const cash = parseFloat(cashInput.value) || 0;
        const card = parseFloat(cardInput.value) || 0;
        const deposit = parseFloat(depositInput.value) || 0;
        const totalPayment = cash + card + deposit;
        const totalText = paymentTotalSpan.textContent.replace('Rs. ', '');
        const currentTotal = parseFloat(totalText) || 0;
        const balance = currentTotal - totalPayment;
        balanceSpan.textContent = balance.toFixed(2);
    }

    cashInput.addEventListener('input', updateBalance);
    cardInput.addEventListener('input', updateBalance);
    depositInput.addEventListener('input', updateBalance);

    // Observe changes to total-amount in main modal
    const totalAmountSpan = document.getElementById('total-amount');
    const observer = new MutationObserver(() => {
        const newTotal = parseFloat(totalAmountSpan.textContent);
        paymentTotalSpan.textContent = `Rs. ${newTotal.toFixed(2)}`;
        updateBalance(); // Recalculate balance with new total
    });

    observer.observe(totalAmountSpan, { childList: true, subtree: true });

    // Cleanup observer when modal closes
    paymentModal.closeObserver = () => observer.disconnect();
    updateBalance(); // Initial balance calculation
}

// Update closePaymentModal to disconnect observer
function closePaymentModal() {
    const paymentModal = document.getElementById("PaymentModal");
    paymentModal.classList.add("translate-x-full");

    setTimeout(() => {
        paymentModal.classList.add("hidden");
        if (paymentModal.closeObserver) paymentModal.closeObserver();
    }, 300);
}

async function saveBill() {
    // Retrieve data from the billing modal
    const bookingId = window.currentBookingId;
    const totalDistanceFare = parseFloat(document.getElementById('distance-cost').textContent);
    const totalWaitFare = parseFloat(document.getElementById('wait-cost').textContent);
    const tax = parseFloat(window.currentTax); // Tax percentage
    const discount = parseFloat(document.getElementById('discount-percentage').value) || 0;
    const userId = 1;

    // Retrieve payment inputs
    const cash = parseFloat(document.getElementById('cash').value) || 0;
    const deposit = parseFloat(document.getElementById('deposit').value) || 0;
    const card = parseFloat(document.getElementById('card').value) || 0;
    const totalAmount = parseFloat(document.getElementById('total-amount').textContent);

    // Validate that the sum of payments equals the total amount
    const totalPayment = cash + deposit + card;
    if (totalPayment !== totalAmount) {
        alert("Error: The sum of cash, deposit, and card payments must equal the total amount due.");
        return;
    }

    const billData = {
        bookingId: bookingId,
        totalDistanceFare: totalDistanceFare,
        totalWaitFare: totalWaitFare,
        tax: tax,
        discount: discount,
        userId: userId,
        cash: cash,
        deposit: deposit,
        card: card,
        totalAmount: totalAmount
    };

    try {
        const response = await fetch(`${window.location.origin}/megacitycab_war_exploded/billing/create`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(billData)
        });

        if (response.ok) {
            closePaymentModal(); // Close payment modal
            closeModal(); // Close billing modal
            location.reload();
        } else {
            alert("Failed to save the bill. Please try again.");
        }
    } catch (error) {
        console.error("Error saving bill:", error);
        alert("An error occurred while saving the bill.");
    }
}