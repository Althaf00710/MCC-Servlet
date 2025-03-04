document.addEventListener("DOMContentLoaded", async function () {
    // Existing variable declarations
    const cabTypeSelect = document.getElementById("cabTypeSelect");
    const cabSelect = document.getElementById("cabSelect");
    const addStopBtn = document.getElementById("addStop");
    const stopsContainer = document.getElementById("stopsContainer");
    const pickupLocation = document.getElementById("pickupLocation");
    const dropLocation = document.getElementById("dropLocation");
    const customerSelect = document.getElementById("customerSelect");

    let map, directionsService, directionsRenderer;
    let customers = []; // Store fetched customer data

    // **Fetch cab types**
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

    // **Display cab types as selectable boxes**
    function displayCabTypes(cabTypes) {
        const container = document.getElementById("cabTypeContainer");
        container.innerHTML = ""; // Clear any existing content
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
                <!-- Image Container (Bigger Size) -->
                <div class="absolute top-0 left-1/2 transform -translate-x-1/2 -translate-y-1/4 w-60 h-40 z-10">
                    <img src="${window.location.origin}/megacitycab_war_exploded/${cabType.imageUrl}" 
                         alt="${cabType.typeName}" 
                         class="w-full h-full object-contain">
                </div>
                <!-- Type Name -->
                <h3 class="text-lg font-bold text-gray-700 mt-8 mb-1">${cabType.typeName}</h3>
                <!-- Attributes with Icons -->
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
                // Remove selection from all boxes
                document.querySelectorAll(".cab-type-box").forEach(b => {
                    b.classList.remove("border-orange-500", "bg-orange-100");
                });
                // Highlight the clicked box with orange styling
                this.classList.add("border-orange-500", "bg-orange-100");
                document.getElementById("selectedCabType").value = this.dataset.id;
                // Fetch cabs for the selected cab type
                fetchCabsByType(this.dataset.id);
            });
            container.appendChild(box);
        });
    }

    // **Fetch cabs by cab type ID**
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

    // **Populate the cab dropdown**
    function populateCabDropdown(cabs) {
        const cabSelect = document.getElementById("cabSelect");
        cabSelect.innerHTML = '<option value="">Select a Cab</option>';
        cabs.forEach(cab => {
            const option = document.createElement("option");
            option.value = cab.id; // Assuming cab has an 'id' field
            option.textContent = `${cab.cabName} | ${cab.plateNumber}`;
            cabSelect.appendChild(option);
        });
    }

    // Existing fetchCustomers function (unchanged)
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

    // Existing populateCustomerDropdown function (unchanged)
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

    // Existing initMap, setupAutocomplete, updateRoute, displayRouteInfo, and addStopBtn logic (unchanged)
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

    function setupAutocomplete(input) {
        const autocomplete = new google.maps.places.Autocomplete(input);
        autocomplete.addListener("place_changed", updateRoute);
    }

    function updateRoute() {
        if (!pickupLocation.value || !dropLocation.value) return;

        const waypoints = Array.from(document.querySelectorAll("#stopsContainer input"))
            .map(input => ({ location: input.value, stopover: true }))
            .filter(w => w.location);

        directionsService.route({
            origin: pickupLocation.value,
            destination: dropLocation.value,
            waypoints,
            travelMode: google.maps.TravelMode.DRIVING,
        }, (response, status) => {
            if (status === "OK") {
                directionsRenderer.setDirections(response);
                displayRouteInfo(response);
            } else {
                console.error("Directions request failed due to " + status);
                document.getElementById("routeInfo").innerHTML = "<p>Unable to calculate route.</p>";
            }
        });
    }

    function displayRouteInfo(response) {
        const route = response.routes[0];
        const legs = route.legs;
        let totalDistance = 0;
        let infoHtml = "<h3>Route Information</h3><ul>";

        legs.forEach((leg, index) => {
            const distance = leg.distance.text;
            const duration = leg.duration.text;
            totalDistance += leg.distance.value;
            const from = index === 0 ? "Pickup" : `Stop ${index}`;
            const to = index === legs.length - 1 ? "Drop" : `Stop ${index + 1}`;
            infoHtml += `<li><strong>${from} to ${to}:</strong> ${distance}, ${duration}</li>`;
        });

        const totalKm = (totalDistance / 1000).toFixed(2);
        infoHtml += `</ul><p><strong>Total Distance:</strong> ${totalKm} km</p>`;
        document.getElementById("routeInfo").innerHTML = infoHtml;
    }

    addStopBtn.addEventListener("click", function () {
        const stopDiv = document.createElement("div");
        stopDiv.classList.add("flex", "items-center", "mb-4", "gap-2");

        // Input container for location + wait time
        const inputGroup = document.createElement("div");
        inputGroup.classList.add("flex", "items-center", "gap-2", "flex-1");

        // Location input
        const locationInput = document.createElement("input");
        locationInput.type = "text";
        locationInput.classList.add("w-full", "p-2", "border", "rounded", "location-input");
        locationInput.placeholder = "Enter Stop Location";

        // Wait time input
        const waitInput = document.createElement("input");
        waitInput.type = "number";
        waitInput.min = "0";
        waitInput.value = "0";
        waitInput.classList.add("w-24", "p-2", "border", "rounded", "bg-white");
        waitInput.placeholder = "Wait mins";
        waitInput.title = "Waiting time in minutes";

        // Remove button
        const removeBtn = document.createElement("button");
        removeBtn.type = "button";
        removeBtn.classList.add("bg-white", "text-red-600", "p-2", "rounded", "hover:bg-red-50");
        removeBtn.innerHTML = '<i class="fi-rr-trash"></i>';
        removeBtn.title = "Remove Stop";
        removeBtn.addEventListener("click", () => {
            stopDiv.remove();
            updateRoute();
        });

        // Build structure
        inputGroup.appendChild(locationInput);
        inputGroup.appendChild(waitInput);
        stopDiv.appendChild(inputGroup);
        stopDiv.appendChild(removeBtn);
        stopsContainer.appendChild(stopDiv);

        // Initialize autocomplete for location input
        setupAutocomplete(locationInput);

        // Add event listeners
        locationInput.addEventListener("change", updateRoute);
    });

    pickupLocation.addEventListener("change", updateRoute);
    dropLocation.addEventListener("change", updateRoute);

    // Initialize everything
    fetchCustomers();
    fetchCabTypes();
    initMap();
});