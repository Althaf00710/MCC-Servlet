document.addEventListener("DOMContentLoaded", async function () {
    const cabTypeSelect = document.getElementById("cabTypeSelect");
    const cabSelect = document.getElementById("cabSelect");
    const addStopBtn = document.getElementById("addStop");
    const stopsContainer = document.getElementById("stopsContainer");
    const pickupLocation = document.getElementById("pickupLocation");
    const dropLocation = document.getElementById("dropLocation");

    let map, directionsService, directionsRenderer;

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
            totalDistance += leg.distance.value; // in meters
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
        stopDiv.classList.add("flex", "items-center", "mb-4");

        const input = document.createElement("input");
        input.type = "text";
        input.classList.add("w-full", "p-2", "border", "rounded", "location-input");
        input.placeholder = "Enter Stop Location";

        const removeBtn = document.createElement("button");
        removeBtn.type = "button";
        removeBtn.classList.add("ml-2", "bg-red-500", "text-white", "px-2", "py-1", "rounded");
        removeBtn.textContent = "Remove";
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

    pickupLocation.addEventListener("change", updateRoute);
    dropLocation.addEventListener("change", updateRoute);

    initMap();
});