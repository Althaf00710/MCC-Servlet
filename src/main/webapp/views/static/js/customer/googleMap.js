let map, directionsService, directionsRenderer, pickupAutocomplete, dropAutocomplete;
let stops = [];

function initMap() {
    map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: 7.8731, lng: 80.7718 },
        zoom: 8,
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

    const locationInput = document.createElement("input");
    locationInput.type = "text";
    locationInput.classList.add("w-full", "p-2", "border", "rounded", "bg-white/90", "text-gray-900");
    locationInput.placeholder = "Enter Stop Location";

    const removeBtn = document.createElement("button");
    removeBtn.classList.add("bg-white", "text-red-600", "p-2", "rounded", "hover:bg-red-50");
    removeBtn.innerHTML =
        '<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/></svg>';
    
    removeBtn.addEventListener("click", () => {
        stopDiv.remove();
        stops = stops.filter((s) => s !== locationInput);
        updateRoute();
    });

    stopDiv.appendChild(locationInput);
    stopDiv.appendChild(removeBtn);
    stopsContainer.appendChild(stopDiv);

    const stopAutocomplete = new google.maps.places.Autocomplete(locationInput);
    stopAutocomplete.addListener("place_changed", () => {
        stops.push(locationInput);
        updateRoute();
    });
}

function updateRoute() {
    const origin = pickupAutocomplete.getPlace();
    const destination = dropAutocomplete.getPlace();
    if (!origin || !destination) return;

    const waypoints = stops
        .map((stop) => stop.value)
        .filter((place) => place.trim() !== "")
        .map((place) => ({ location: place, stopover: true }));

    const request = {
        origin: origin.geometry.location,
        destination: destination.geometry.location,
        waypoints,
        travelMode: google.maps.TravelMode.DRIVING,
    };

    directionsService.route(request, (result, status) => {
        if (status === google.maps.DirectionsStatus.OK) {
            directionsRenderer.setDirections(result);
        }
    });
}
