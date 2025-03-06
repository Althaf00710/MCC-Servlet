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
                                    <div id="map" style="height: 400px; width: 100%;"></div>
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

