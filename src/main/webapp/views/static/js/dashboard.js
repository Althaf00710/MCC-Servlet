var topCabChart, dailySalesChart;

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

// Initialize charts when document is ready
$(document).ready(function() {
    // Doughnut Chart (Top Cab Types)
    var ctxDoughnut = document.getElementById('topCabChart').getContext('2d');
    topCabChart = new Chart(ctxDoughnut, {
        type: 'doughnut',
        data: {
            labels: [],
            datasets: [{
                label: 'Bookings Count',
                data: [],
                backgroundColor: ['#fe6601','#7a7979','#281e32','#ed9e67','#f25939']
            }]
        },
        options: {
            responsive: true
        }
    });

    // Line Chart (Daily Sales)
    var ctxLine = document.getElementById('dailySalesChart').getContext('2d');
    dailySalesChart = new Chart(ctxLine, {
        type: 'bar',
        data: {
            labels: [],
            datasets: [{
                label: 'Total Sales',
                data: [],
                borderColor: 'rgba(75, 192, 192, 1)',
                fill: false
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });


    fetchDashboardData();
    fetchRecentPickupAndDisplayMap();
});

// Function to fetch data for charts and new bookings table
function fetchDashboardData() {
    // 1. Top Cab Types for Doughnut Chart
    $.ajax({
        url: `${window.location.origin}/megacitycab_war_exploded/booking/get-topCabType`,
        method: 'GET',
        success: function(data) {
            var labels = [];
            var counts = [];
            data.forEach(function(item) {
                labels.push(item.cabType);
                counts.push(item.bookingsCount);
            });
            topCabChart.data.labels = labels;
            topCabChart.data.datasets[0].data = counts;
            topCabChart.update();
        },
        error: function(err) {
            console.error("Error fetching top cab types:", err);
        }
    });

    // 2. Daily Sales for Line Chart
    $.ajax({
        url: `${window.location.origin}/megacitycab_war_exploded/booking/getDailySales`,
        method: 'GET',
        success: function(data) {
            var labels = [];
            var sales = [];
            data.forEach(function(item) {
                labels.push(item.billDate);  // Format as needed
                sales.push(item.totalSales);
            });
            dailySalesChart.data.labels = labels;
            dailySalesChart.data.datasets[0].data = sales;
            dailySalesChart.update();
        },
        error: function(err) {
            console.error("Error fetching daily sales:", err);
        }
    });

    // 3. New Bookings for Table
    $.ajax({
        url: `${window.location.origin}/megacitycab_war_exploded/booking/new-booking`,
        method: 'GET',
        success: function(data) {
            var tbody = $('#newBookingsTable tbody');
            tbody.empty(); // Clear existing rows
            data.slice(0, 4).forEach(function(item) {
                var row = '<tr>' +
                    '<td class="px-6 py-2 whitespace-nowrap text-sm text-gray-500">' + item.bookingNumber + '</td>' +
                    '<td class="px-6 py-2 whitespace-nowrap text-sm text-gray-500">' + item.bookingDateTime + '</td>' +
                    '<td class="px-6 py-2 whitespace-nowrap text-sm text-gray-500">' + item.status + '</td>' +
                    '<td class="px-6 py-2 whitespace-nowrap text-sm text-gray-500">' + item.customerName + '</td>' +
                    '</tr>';
                tbody.append(row);
            });
        },
        error: function(err) {
            console.error("Error fetching new bookings:", err);
        }
    });

    $.ajax({
        url: `${window.location.origin}/megacitycab_war_exploded/booking/booking-count`,
        method: 'GET',
        success: function(data) {
            $('#bookingCount').text(data);
        },
        error: function(err) { console.error("Error fetching booking count:", err); }
    });

    // 5. Today's Total Sales (Billing)
    $.ajax({
        url: `${window.location.origin}/megacitycab_war_exploded/billing/today-total`,
        method: 'GET',
        success: function(data) {
            $('#todayTotal').text(data);
        },
        error: function(err) { console.error("Error fetching today's total sales:", err); }
    });
}

// Function to fetch recent pickup data and display directions on Google Map
function fetchRecentPickupAndDisplayMap() {
    $.ajax({
        url: `${window.location.origin}/megacitycab_war_exploded/booking/get-recent-pickup`,
        method: 'GET',
        success: function(data) {
            if (data && data.length > 0) {
                var recentPickup = data[0];
                // recentPickup contains bookingNumber, bookingPlaceId, bookingDateTime, and stopPlaceIds (array)
                displayDirectionsOnMap(recentPickup.bookingPlaceId, recentPickup.stopPlaceIds);
            }
        },
        error: function(err) {
            console.error("Error fetching recent pickup:", err);
        }
    });
}

// Function to display directions on Google Map using the origin (pickup) and stops as waypoints
function displayDirectionsOnMap(originPlaceId, stopPlaceIds) {
    // Initialize the map centered on a default location (update as needed)
    var map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 9.667793, lng: 80.009611},
        zoom: 12
    });

    var directionsService = new google.maps.DirectionsService();
    var directionsRenderer = new google.maps.DirectionsRenderer();
    directionsRenderer.setMap(map);

    // Create waypoints from stopPlaceIds (if any)
    var waypoints = [];
    if (stopPlaceIds && stopPlaceIds.length > 0) {
        // Use each stop as a waypoint
        stopPlaceIds.forEach(function(placeId) {
            waypoints.push({
                location: { placeId: placeId },
                stopover: true
            });
        });
    }

    var request = {
        origin: { placeId: originPlaceId },
        destination: { placeId: originPlaceId }, // Assuming a round-trip route (adjust as needed)
        waypoints: waypoints,
        travelMode: google.maps.TravelMode.DRIVING
    };

    directionsService.route(request, function(result, status) {
        if (status === google.maps.DirectionsStatus.OK) {
            directionsRenderer.setDirections(result);
        } else {
            console.error('Directions request failed due to ' + status);
        }
    });
}