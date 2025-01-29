
    document.addEventListener("DOMContentLoaded", function () {
    // Vanta.js initialization
    VANTA.DOTS({
        el: "#vanta-bg",
        mouseControls: true,
        touchControls: true,
        gyroControls: false,
        minHeight: 200.00,
        minWidth: 200.00,
        scale: 1.00,
        scaleMobile: 1.00,
        color: 0xff7100,
        backgroundColor: 0xf7f7f7,
        size: 5.00,
        spacing: 50.00,
        showLines: false
    });

    // Carousel functionality
    const messages = [
{
    prefix: "Access Customer Insights",
    text: "View Registrations, Preferences and Feedback in One Dashboard."
},
{
    prefix: "Secure and Streamlined",
    text: "Process Payments, Apply Discounts and Generate Bills in Seconds."
},
{
    prefix: "Stay in Control",
    text: "Monitor Driver Availability, Cab Maintenance and Daily Revenue Trends."
}
    ];

    const carousel = document.getElementById('carousel');
    let currentIndex = 0;

    function createCarouselItem(message) {
    const item = document.createElement('div');
    item.className = 'carousel-item';
    item.innerHTML = `
                <div class="text-orange-500 text-5xl font-semibold mb-2">${message.prefix}</div>
                <div class="text-gray-600 text-2xl font-normal">${message.text}</div>
            `;
    return item;
}

    function showNextMessage() {
    // Remove current active item
    while (carousel.firstChild) {
    carousel.removeChild(carousel.firstChild);
}

    // Create and add new item
    const newItem = createCarouselItem(messages[currentIndex]);
    carousel.appendChild(newItem);

    // Trigger reflow to enable transition
    void newItem.offsetWidth;
    newItem.classList.add('active');

    // Update index
    currentIndex = (currentIndex + 1) % messages.length;
}

    // Initial show
    showNextMessage();

    // Start interval
    setInterval(showNextMessage, 5000);
});
