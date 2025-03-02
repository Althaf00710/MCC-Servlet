document.addEventListener("DOMContentLoaded", async function () {
    const cabTypeSelect = document.getElementById("cabTypeSelect");
    const cabSelect = document.getElementById("cabSelect");
    const addStopBtn = document.getElementById("addStop");
    const stopsContainer = document.getElementById("stopsContainer");
    const pickupLocation = document.getElementById("pickupLocation");
    const dropLocation = document.getElementById("dropLocation");
    const customerSelect = document.getElementById("customerSelect");

    let map, directionsService, directionsRenderer;
    let customers = []; // Store fetched customer data

    // **Fetch customers from the endpoint**
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

    // **Populate the customer dropdown**
    function populateCustomerDropdown(customerList) {
        customerSelect.innerHTML = '<option value="">Select a Customer</option>';
        //customerList.classList.add("p-4");
        customerList.forEach(customer => {
            const option = document.createElement("option");
            option.value = customer.id; // Assuming customer has an 'id' field
            option.textContent = `${customer.registerNumber} | ${customer.name}`;
            // Add searchable data to a custom attribute
            option.dataset.search = `${customer.registerNumber} ${customer.name} ${customer.countryCode}${customer.phoneNumber} ${customer.nicNumber}`.toLowerCase();
            customerSelect.appendChild(option);
        });

        // Initialize select2 with custom filtering
        $('#customerSelect').select2({
            placeholder: "Select a Customer",
            allowClear: true,
            matcher: function(params, data) {
                // If no search term, return all options
                if ($.trim(params.term) === '') return data;
                // If no searchable data, skip this option
                if (typeof data.element.dataset.search === 'undefined') return null;
                // Filter based on search term
                if (data.element.dataset.search.indexOf(params.term.toLowerCase()) > -1) return data;
                return null;
            }
        });
    }

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

    // Display cab types as selectable boxes
    function displayCabTypes(cabTypes) {
        const container = document.getElementById("cabTypeContainer");
        container.innerHTML = ""; // Clear any existing content
        cabTypes.forEach(cabType => {
            const box = document.createElement("div");
            box.classList.add("cab-type-box", "p-4", "border", "rounded", "cursor-pointer", "hover:border-blue-500", "w-64");
            box.dataset.id = cabType.id;
            box.innerHTML = `
            <img src="${cabType.imageUrl}" alt="${cabType.typeName}" class="w-full h-32 object-cover mb-2">
            <h3 class="text-lg font-semibold">${cabType.typeName}</h3>
            <div class="details flex justify-around mt-2">
                <span><i class="fi-rr-users"></i> ${cabType.capacity}</span>
                <span><i class="fi-rr-dollar"></i> ${cabType.baseFare}</span>
                <span><i class="fi-rr-clock"></i> ${cabType.baseWaitTimeFare}</span>
            </div>
        `;
            box.addEventListener("click", function() {
                // Remove selection from all boxes
                document.querySelectorAll(".cab-type-box").forEach(b => {
                    b.classList.remove("border-blue-500", "bg-blue-100");
                });
                // Highlight the clicked box and update the hidden input
                this.classList.add("border-blue-500", "bg-blue-100");
                document.getElementById("selectedCabType").value = this.dataset.id;
            });
            container.appendChild(box);
        });
    }

    // **Initialize Google Maps**
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

    // **Setup Autocomplete for location inputs**
    function setupAutocomplete(input) {
        const autocomplete = new google.maps.places.Autocomplete(input);
        autocomplete.addListener("place_changed", updateRoute);
    }

    // **Update the route on the map**
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

    // **Display route information**
    function displayRouteInfo(response) {
        const route = response.routes[0];
        const legs = route.legs;
        let totalDistance = 0;
        let infoHtml = "<h3>Route Information</h3><ul>";

        legs.forEach((leg, index) => {
            const distance = leg.distance.text;
            const duration = leg.duration.text;
            totalDistance += leg.distance.value; // in meters
            const from = index === 0 ? "Pickup" : `Stop ${index}`;
            const to = index === legs.length - 1 ? "Drop" : `Stop ${index + 1}`;
            infoHtml += `<li><strong>${from} to ${to}:</strong> ${distance}, ${duration}</li>`;
        });

        const totalKm = (totalDistance / 1000).toFixed(2);
        infoHtml += `</ul><p><strong>Total Distance:</strong> ${totalKm} km</p>`;
        document.getElementById("routeInfo").innerHTML = infoHtml;
    }

    // **Add stop functionality**
    addStopBtn.addEventListener("click", function () {
        const stopDiv = document.createElement("div");
        stopDiv.classList.add("flex", "items-center", "mb-4");

        const input = document.createElement("input");
        input.type = "text";
        input.classList.add("w-full", "p-2", "border", "rounded", "location-input");
        input.placeholder = "Enter Stop Location";

        const removeBtn = document.createElement("button");
        removeBtn.type = "button";
        removeBtn.classList.add("ml-2", "bg-white", "text-red-600", "p-2", "rounded");
        removeBtn.innerHTML = '<i class="fi-rr-trash"></i>';
        removeBtn.title = "Remove Stop";
        removeBtn.addEventListener("click", () => {
            stopDiv.remove();
            updateRoute();
        });

        stopDiv.appendChild(input);
        stopDiv.appendChild(removeBtn);
        stopsContainer.appendChild(stopDiv);

        setupAutocomplete(input);
        input.addEventListener("change", updateRoute);
    });

    // **Event listeners**
    pickupLocation.addEventListener("change", updateRoute);
    dropLocation.addEventListener("change", updateRoute);

    // **Initialize everything**
    fetchCustomers(); // Load customers and initialize dropdown
    fetchCabTypes();
    initMap();        // Initialize the map
});