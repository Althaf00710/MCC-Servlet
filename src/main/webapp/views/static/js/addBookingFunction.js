document.addEventListener("DOMContentLoaded", async function () {
    // Existing variable declarations
    const cabTypeSelect = document.getElementById("cabTypeSelect");
    const cabSelect = document.getElementById("cabSelect");
    const addStopBtn = document.getElementById("addStop");
    const stopsContainer = document.getElementById("stopsContainer");
    const pickupLocation = document.getElementById("pickupLocation");
    const dropLocation = document.getElementById("dropLocation");
    const customerSelect = document.getElementById("customerSelect");
    const bookingDate = document.getElementById("bookingDate");

    let map, directionsService, directionsRenderer;
    let customers = [];
    let currentRouteResponse; // To store the directions response for distance calculations

    // **Fetch cab types** (unchanged)
    async function fetchCabTypes() {
        try {
            const response = await fetch(`${window.location.origin}/megacitycab_war_exploded/cabtypes/get`);
            if (!response.ok) throw new Error("Failed to fetch cab types");
            const cabTypes = await response.json();
            displayCabTypes(cabTypes);
        } catch (error) {
            console.error("Error fetching cab types:", error);
            const container = document.getElementById("cabTypeContainer");
            container.innerHTML = "<p class='text-red-500'>Failed to load cab types. Please try again later.</p>";
        }
    }

    // **Display cab types as selectable boxes** (unchanged)
    function displayCabTypes(cabTypes) {
        const container = document.getElementById("cabTypeContainer");
        container.innerHTML = "";
        cabTypes.forEach(cabType => {
            const box = document.createElement("div");
            box.classList.add(
                "cab-type-box",
                "relative",
                "bg-gray-100",
                "border-2",
                "border-orange-400",
                "rounded-xl",
                "shadow-lg",
                "hover:shadow-md",
                "transition-shadow",
                "duration-200",
                "p-2",
                "text-center",
                "pt-20",
                "w-64",
                "cursor-pointer",
                "mt-6",
                "flex-shrink-0"
            );
            box.dataset.id = cabType.id;
            box.innerHTML = `
                <div class="absolute top-0 left-1/2 transform -translate-x-1/2 -translate-y-1/4 w-60 h-40 z-10">
                    <img src="${window.location.origin}/megacitycab_war_exploded/${cabType.imageUrl}" 
                         alt="${cabType.typeName}" 
                         class="w-full h-full object-contain">
                </div>
                <h3 class="text-lg font-semibold text-gray-700 mt-8 mb-1">${cabType.typeName}</h3>
                <div class="flex justify-center items-center gap-3 text-gray-600 text-sm mb-3 font-semibold">
                    <div class="flex items-center gap-1">
                        <img src="${window.location.origin}/megacitycab_war_exploded/views/static/images/seat.png" class="w-4 h-4">
                        <span>${cabType.capacity}</span>
                    </div>
                    <div class="flex items-center gap-1">
                        <img src="${window.location.origin}/megacitycab_war_exploded/views/static/images/road.png" class="w-4 h-4">
                        <span>Rs. ${cabType.baseFare}</span>
                    </div>
                    <div class="flex items-center gap-1">
                        <img src="${window.location.origin}/megacitycab_war_exploded/views/static/images/clock.png" class="w-4 h-4">
                        <span>Rs. ${cabType.baseWaitTimeFare}</span>
                    </div>
                </div>
            `;
            box.addEventListener("click", function() {
                document.querySelectorAll(".cab-type-box").forEach(b => {
                    b.classList.remove("border-orange-500", "bg-orange-100");
                });
                this.classList.add("border-orange-500", "bg-orange-100");
                document.getElementById("selectedCabType").value = this.dataset.id;
                fetchCabsByType(this.dataset.id);
            });
            container.appendChild(box);
        });
    }

    // **Fetch cabs by cab type ID** (unchanged)
    async function fetchCabsByType(cabTypeId) {
        try {
            const response = await fetch(`${window.location.origin}/megacitycab_war_exploded/cabs/getByType?cabTypeId=${cabTypeId}`);
            if (!response.ok) throw new Error("Failed to fetch cabs");
            const cabs = await response.json();
            populateCabDropdown(cabs);
        } catch (error) {
            console.error("Error fetching cabs:", error);
            document.getElementById("cabSelect").innerHTML = '<option value="">No cabs available</option>';
        }
    }

    // **Populate the cab dropdown** (unchanged)
    function populateCabDropdown(cabs) {
        cabSelect.innerHTML = '<option value="">Select a Cab</option>';
        cabs.forEach(cab => {
            const option = document.createElement("option");
            option.value = cab.id;
            option.textContent = `${cab.cabName} | ${cab.plateNumber}`;
            cabSelect.appendChild(option);
        });
    }

    // **Fetch customers** (unchanged)
    async function fetchCustomers() {
        try {
            const response = await fetch(`${window.location.origin}/megacitycab_war_exploded/customers/getCustomers`);
            if (!response.ok) throw new Error("Failed to fetch customers");
            customers = await response.json();
            populateCustomerDropdown(customers);
        } catch (error) {
            console.error("Error fetching customers:", error);
        }
    }

    // **Populate customer dropdown** (unchanged)
    function populateCustomerDropdown(customerList) {
        customerSelect.innerHTML = '<option value="">Select a Customer</option>';
        customerList.forEach(customer => {
            const option = document.createElement("option");
            option.value = customer.id;
            option.textContent = `${customer.registerNumber} | ${customer.name}`;
            option.dataset.search = `${customer.registerNumber} ${customer.name} ${customer.countryCode}${customer.phoneNumber} ${customer.nicNumber}`.toLowerCase();
            customerSelect.appendChild(option);
        });

        $('#customerSelect').select2({
            placeholder: "Select a Customer",
            allowClear: true,
            matcher: function(params, data) {
                if ($.trim(params.term) === '') return data;
                if (typeof data.element.dataset.search === 'undefined') return null;
                if (data.element.dataset.search.indexOf(params.term.toLowerCase()) > -1) return data;
                return null;
            }
        });
    }

    // **Initialize map** (unchanged)
    async function initMap() {
        const keyResponse = await fetch(`${window.location.origin}/megacitycab_war_exploded/getKey`);
        const { apiKey } = await keyResponse.json();
        const script = document.createElement("script");
        script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&libraries=places`;
        script.onload = () => {
            map = new google.maps.Map(document.getElementById("map"), {
                center: { lat: 7.8731, lng: 80.7718 },
                zoom: 8,
            });
            directionsService = new google.maps.DirectionsService();
            directionsRenderer = new google.maps.DirectionsRenderer();
            directionsRenderer.setMap(map);

            document.querySelectorAll(".location-input").forEach(setupAutocomplete);
        };
        document.body.appendChild(script);
    }

    // **Setup autocomplete with place storage**
    function setupAutocomplete(input) {
        const autocomplete = new google.maps.places.Autocomplete(input);
        autocomplete.addListener("place_changed", function() {
            input.place = autocomplete.getPlace(); // Store place object
            updateRoute();
        });
    }

    // **Update route with response storage**
    function updateRoute() {
        if (!pickupLocation.value || !dropLocation.value) return;

        const waypoints = Array.from(stopsContainer.querySelectorAll(".location-input"))
            .map(input => {
                if (!input.place) return null; // Skip if no place is selected
                return { location: input.place.formatted_address, stopover: true };
            })
            .filter(w => w && w.location !== dropLocation.place?.formatted_address); // Exclude drop-off from waypoints

        directionsService.route({
            origin: pickupLocation.place?.formatted_address || pickupLocation.value,
            destination: dropLocation.place?.formatted_address || dropLocation.value,
            waypoints,
            travelMode: google.maps.TravelMode.DRIVING,
        }, (response, status) => {
            if (status === "OK") {
                currentRouteResponse = response; // Store the response
                directionsRenderer.setDirections(response);
                displayRouteInfo(response);
            } else {
                console.error("Directions request failed due to " + status);
                currentRouteResponse = null; // Clear response on failure
                document.getElementById("routeInfo").innerHTML = "<p>Unable to calculate route.</p>";
            }
        });
    }

    // **Display route info** (unchanged)
    function displayRouteInfo(response) {
        const route = response.routes[0];
        const legs = route.legs;
        const bookingDateTime = new Date(document.getElementById("bookingDate").value);
        let currentTime = new Date(bookingDateTime);

        let infoHtml = `
        <div class="route-steps">
            <h3 class="text-lg font-semibold mb-4">Journey Details</h3>
            <div class="flex flex-col gap-4">`;

        // Process Pickup and Stops
        legs.forEach((leg, index) => {
            const isFirst = index === 0;
            const isLast = index === legs.length - 1;
            const waitMinutes = !isFirst ?
                parseInt(document.querySelectorAll('#stopsContainer input[type="number"]')[index-1]?.value || 0) : 0;

            // Step Header (Pickup/Stop)
            infoHtml += `
            <div class="step">
                <div class="flex items-center gap-2">
                    <div class="w-8 h-8 bg-orange-500 text-white rounded-full flex items-center justify-center">
                        ${isFirst ? 'P' : index}
                    </div>
                    <div>
                        <span class="font-semibold">${isFirst ? 'Pickup' : `Stop ${index}`}</span>
                        <span class="text-sm text-gray-500 ml-2">${leg.start_address}</span>
                    </div>
                </div>`;

            // Travel Details
            infoHtml += `
                <div class="ml-8 pl-4 border-l-2 border-gray-200">
                    <div class="my-2 text-sm">
                        <span class="font-medium">Arrival:</span> 
                        ${currentTime.toLocaleDateString()} 
                        ${currentTime.toLocaleTimeString()}
                    </div>
                    <div class="flex gap-4 text-sm text-gray-600">
                        <div>
                            <span class="font-medium">Distance:</span> 
                            ${leg.distance.text}
                        </div>
                        <div>
                            <span class="font-medium">Travel Time:</span> 
                            ${leg.duration.text}
                        </div>
                    </div>`;

            // Add wait time for stops (except pickup)
            if (!isFirst) {
                infoHtml += `
                    <div class="mt-2 text-sm">
                        <span class="font-medium">Wait Time:</span> 
                        ${waitMinutes} mins
                        <span class="ml-4">
                            (Departure: ${new Date(currentTime.getTime() + waitMinutes * 60000).toLocaleTimeString()})
                        </span>
                    </div>`;
            }

            infoHtml += `</div></div>`;

            // Update current time: arrival time + travel time + wait time
            currentTime = new Date(currentTime.getTime() +
                leg.duration.value * 1000 +
                (waitMinutes * 60000));
        });

        // Add Drop location
        const lastLeg = legs[legs.length - 1];
        infoHtml += `
            <div class="step">
                <div class="flex items-center gap-2">
                    <div class="w-8 h-8 bg-blue-500 text-white rounded-full flex items-center justify-center">
                        D
                    </div>
                    <div>
                        <span class="font-semibold">Drop</span>
                        <span class="text-sm text-gray-500 ml-2">${lastLeg.end_address}</span>
                    </div>
                </div>
                <div class="ml-8 pl-4 border-l-2 border-gray-200">
                    <div class="my-2 text-sm">
                        <span class="font-medium">Arrival:</span> 
                        ${currentTime.toLocaleDateString()} 
                        ${currentTime.toLocaleTimeString()}
                    </div>
                </div>
            </div>`;

        // Total Distance
        infoHtml += `
            </div>
            <div class="mt-4 p-2 bg-gray-50 rounded">
                <span class="font-semibold">Total Distance:</span> 
                ${(route.legs.reduce((sum, leg) => sum + leg.distance.value, 0) / 1000).toFixed(2)} km
            </div>
        </div>`;

        document.getElementById("routeInfo").innerHTML = infoHtml;
    }

    // **Add stop with wait time input**
    addStopBtn.addEventListener("click", function () {
        const stopDiv = document.createElement("div");
        stopDiv.classList.add("flex", "items-center", "mb-4", "gap-2");

        const inputGroup = document.createElement("div");
        inputGroup.classList.add("flex", "items-center", "gap-2", "flex-1");

        const locationInput = document.createElement("input");
        locationInput.type = "text";
        locationInput.classList.add("w-full", "p-2", "border", "rounded", "location-input");
        locationInput.placeholder = "Enter Stop Location";

        const waitInput = document.createElement("input");
        waitInput.type = "number";
        waitInput.min = "0";
        waitInput.value = "0";
        waitInput.classList.add("w-24", "p-2", "border", "rounded", "bg-white");
        waitInput.placeholder = "Wait mins";

        const removeBtn = document.createElement("button");
        removeBtn.type = "button";
        removeBtn.classList.add("bg-white", "text-red-600", "p-2", "rounded", "hover:bg-red-50");
        removeBtn.innerHTML = '<i class="fi-rr-trash"></i>';
        removeBtn.title = "Remove Stop";
        removeBtn.addEventListener("click", () => {
            stopDiv.remove();
            updateRoute();
        });

        inputGroup.appendChild(locationInput);
        inputGroup.appendChild(waitInput);
        stopDiv.appendChild(inputGroup);
        stopDiv.appendChild(removeBtn);
        stopsContainer.appendChild(stopDiv);

        setupAutocomplete(locationInput);
    });

    // **Save booking function**
    async function saveBooking() {
        // Collect form data
        const customerId = customerSelect.value;
        const cabId = cabSelect.value;
        const bookingDateTime = bookingDate.value;
        const pickupPlace = pickupLocation.place;
        const dropPlace = dropLocation.place;

        // Basic validation
        if (!customerId || !cabId || !bookingDateTime || !pickupPlace || !dropPlace) {
            alert("Please fill in all required fields.");
            return;
        }

        if (!currentRouteResponse || !currentRouteResponse.routes[0].legs) {
            alert("Please calculate the route by selecting valid locations.");
            return;
        }

        const legs = currentRouteResponse.routes[0].legs;

        // Collect intermediate stops, excluding drop-off
        const intermediateStopsElements = Array.from(stopsContainer.querySelectorAll(".flex"));
        const intermediateStops = intermediateStopsElements.map((stopDiv, index) => {
            const locationInput = stopDiv.querySelector(".location-input");
            const waitInput = stopDiv.querySelector("input[type='number']");
            const place = locationInput.place;

            if (!place) {
                throw new Error("Please select a valid location for all stops.");
            }

            // Prevent drop-off from being an intermediate stop
            if (place.place_id === dropPlace.place_id) {
                throw new Error("Drop-off location cannot be added as an intermediate stop.");
            }

            const waitMinutes = parseInt(waitInput.value, 10) || 0;
            const distanceFromLastStop = legs[index]?.distance?.value
                ? (legs[index].distance.value / 1000).toFixed(2)
                : "0.00"; // Fallback if leg is missing

            return {
                stopLocation: place.formatted_address,
                longitude: place.geometry.location.lng(),
                latitude: place.geometry.location.lat(),
                placeId: place.place_id,
                distanceFromLastStop: parseFloat(distanceFromLastStop),
                waitMinutes
            };
        });

        // Add drop-off location as the last stop
        const dropStop = {
            stopLocation: dropPlace.formatted_address,
            longitude: dropPlace.geometry.location.lng(),
            latitude: dropPlace.geometry.location.lat(),
            placeId: dropPlace.place_id,
            distanceFromLastStop: legs[intermediateStops.length]?.distance?.value
                ? (legs[intermediateStops.length].distance.value / 1000).toFixed(2)
                : "0.00", // Distance from last stop to drop-off
            waitMinutes: 0
        };

        const stops = [...intermediateStops, dropStop];
        const fixedStops = stops.filter((_, index) => index % 2 === 0);
        // Construct the JSON request body
        const requestBody = {
            cabId: parseInt(cabId, 10),
            customerId: parseInt(customerId, 10),
            userId: 1, // Replace with actual logged-in user ID
            bookingDateTime: new Date(bookingDateTime).toISOString(),
            status: "CONFIRMED",
            latitude: pickupPlace.geometry.location.lat(),
            longitude: pickupPlace.geometry.location.lng(),
            pickupLocation: pickupPlace.formatted_address,
            placeId: pickupPlace.place_id,
            stops: fixedStops
        };

        // Send the POST request
        try {
            const response = await fetch(`${window.location.origin}/megacitycab_war_exploded/booking/add`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(requestBody)
            });

            if (!response.ok) {
                throw new Error("Failed to save booking: " + response.statusText);
            }

            alert("Booking saved successfully!");
        } catch (error) {
            console.error("Error saving booking:", error);
            alert("Failed to save booking. Please try again.");
        }
    }

    // **Form submission handler**
    document.getElementById("bookingForm").addEventListener("submit", function(event) {
        event.preventDefault(); // Prevent default form submission
        saveBooking();
    });

    // Event listeners for route updates
    pickupLocation.addEventListener("change", updateRoute);
    dropLocation.addEventListener("change", updateRoute);

    // Initialize everything
    fetchCustomers();
    fetchCabTypes();
    initMap();
});