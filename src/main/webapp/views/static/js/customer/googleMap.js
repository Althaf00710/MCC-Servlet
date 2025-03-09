let map, directionsService, directionsRenderer, pickupAutocomplete, dropAutocomplete;

window.appData = window.appData || {
    companyData: null,
    selectedCabType: null,
    totalDistance: 0,
    totalWaitTime: 0,
    lastDirectionsResult: null,
    stops: []
};

fetch(`${window.location.origin}/megacitycab_war_exploded/getKey`)
    .then(response => response.json())
    .then(data => {
        const apiKey = data.apiKey;
        const script = document.createElement('script');
        script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&libraries=places&callback=initMap`;
        script.defer = true;
        document.head.appendChild(script);
    })
    .catch(error => console.error('Error fetching API Key:', error));

function initMap() {
    map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: 7.8731, lng: 80.7718 },
        zoom: 8,
        gestureHandling: "greedy",
        styles: [
            {
                featureType: "all",
                elementType: "all",
                stylers: [{ saturation: 60 }, { lightness: 50 }],
            },
        ],
    });

    directionsService = new google.maps.DirectionsService();
    directionsRenderer = new google.maps.DirectionsRenderer({ map });

    pickupAutocomplete = new google.maps.places.Autocomplete(document.getElementById("pickup"));
    dropAutocomplete = new google.maps.places.Autocomplete(document.getElementById("drop"));

    pickupAutocomplete.addListener("place_changed", updateRoute);
    dropAutocomplete.addListener("place_changed", updateRoute);

    document.getElementById("addStop").addEventListener("click", addStop);
}

function addStop() {
    const stopsContainer = document.getElementById("stopsContainer");

    const stopDiv = document.createElement("div");
    stopDiv.classList.add("flex", "items-center", "gap-2", "mb-2");

    // Location Input
    const locationInput = document.createElement("input");
    locationInput.type = "text";
    locationInput.classList.add("w-full", "p-2", "border", "rounded", "bg-white/90", "text-gray-900");
    locationInput.placeholder = "Enter Stop Location";

    // Wait Time Input
    const waitTimeInput = document.createElement("input");
    waitTimeInput.type = "number";
    waitTimeInput.min = "0";
    waitTimeInput.classList.add("w-30", "p-2", "border", "rounded", "bg-white/90", "text-gray-900", "text-center");
    waitTimeInput.placeholder = "Wait (min)";

    // Remove Button
    const removeBtn = document.createElement("button");
    removeBtn.classList.add("bg-white", "text-red-600", "p-2", "rounded", "hover:bg-red-50");
    removeBtn.innerHTML =
        '<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/></svg>';

    removeBtn.addEventListener("click", () => {
        stopDiv.remove();
        window.appData.stops = window.appData.stops.filter((s) => s.locationInput !== locationInput);
        updateRoute();
    });

    stopDiv.appendChild(locationInput);
    stopDiv.appendChild(waitTimeInput);
    stopDiv.appendChild(removeBtn);
    stopsContainer.appendChild(stopDiv);

    // Define stop data
    const stopData = {
        locationInput: locationInput,
        waitTimeInput: waitTimeInput,
        place: null
    };

    // Add stop data to window.appData.stops
    window.appData.stops.push(stopData);

    // Set up autocomplete
    const stopAutocomplete = new google.maps.places.Autocomplete(locationInput);
    stopAutocomplete.addListener("place_changed", () => {
        const place = stopAutocomplete.getPlace();
        if (place.geometry) {
            stopData.place = place; // Update the existing stopData object
            updateRoute();
        }
    });

    // Update fare on input changes
    waitTimeInput.addEventListener("input", () => {
        updateFare(window.appData.totalDistance);
    });
    locationInput.addEventListener("input", () => {
        updateFare(window.appData.totalDistance);
    });
}

function updateRoute() {
    const origin = pickupAutocomplete.getPlace();
    const destination = dropAutocomplete.getPlace();

    if (!origin && !destination) return;

    const bounds = new google.maps.LatLngBounds();

    if (origin) bounds.extend(origin.geometry.location);
    if (destination) bounds.extend(destination.geometry.location);

    // Use window.appData.stops for waypoints
    const waypoints = window.appData.stops
        .filter(stop => stop.place?.geometry)
        .map(stop => ({ location: stop.place.geometry.location, stopover: true }));

    if (origin && destination) {
        const request = {
            origin: origin.geometry.location,
            destination: destination.geometry.location,
            waypoints,
            travelMode: google.maps.TravelMode.DRIVING,
        };

        directionsService.route(request, (result, status) => {
            if (status === google.maps.DirectionsStatus.OK) {
                window.appData.lastDirectionsResult = result;
                directionsRenderer.setDirections(result);

                document.getElementById("fareText").textContent = "Calculating...";

                let totalDistance = 0;
                result.routes[0].legs.forEach(leg => {
                    totalDistance += leg.distance.value; // in meters
                });
                totalDistance = totalDistance / 1000; // convert to km
                window.appData.totalDistance = totalDistance;
                document.getElementById("distanceText").textContent = `Total Distance: ${totalDistance.toFixed(2)} km`;
                updateFare(totalDistance);

                adjustViewport(bounds);
            }
        });
    } else {
        const singleLocation = origin ? origin.geometry.location : destination.geometry.location;
        map.setCenter(singleLocation);
        map.setZoom(16);
    }
}

function updateFare(totalDistance = 0) {
    const fareText = document.getElementById("fareText");
    const confirmBtn = document.getElementById("confirmBooking");

    if (!window.appData.selectedCabType || !window.appData.companyData) {
        fareText.textContent = "Please select a cab type";
        confirmBtn.disabled = true;
        return;
    }

    const { baseFare, baseWaitTimeFare } = window.appData.selectedCabType;
    const { tax, discount, minAmountForDiscount } = window.appData.companyData;

    // Calculate total wait time from all stop inputs
    let totalWaitTime = 0;
    document.querySelectorAll('#stopsContainer input[type="number"]').forEach(input => {
        totalWaitTime += parseInt(input.value) || 0;
    });
    window.appData.totalWaitTime = totalWaitTime;

    // Update UI elements
    document.getElementById("distanceText").textContent = `Total Distance: ${totalDistance.toFixed(2)} km`;
    document.getElementById("waitTimeText").textContent = `Total Wait Time: ${totalWaitTime} mins`;

    // Calculate fare components
    const distanceFare = totalDistance * baseFare;
    const waitFare = totalWaitTime * baseWaitTimeFare;
    let subtotal = distanceFare + waitFare;

    // Apply discount if eligible
    if (subtotal > minAmountForDiscount) {
        subtotal -= discount;
    }

    // Calculate tax and total fare
    const taxAmount = subtotal * (tax / 100);
    const totalFare = subtotal + taxAmount;

    // Update fare display and enable confirm button
    fareText.textContent = `Estimated Fare: Rs. ${totalFare.toFixed(2)}`;
    confirmBtn.disabled = false;
}

function adjustViewport(bounds) {
    map.fitBounds(bounds);

    setTimeout(() => {
        const mapDiv = document.getElementById("map");
        const shiftAmount = mapDiv.clientWidth * 0.32;
        map.panBy(shiftAmount, 0);
    }, 500);
}
