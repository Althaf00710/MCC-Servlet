const circle = document.querySelector('.mouse-circle');
const body = document.querySelector('body');
let mouseX = 0;
let mouseY = 0;
let circleX = 0;
let circleY = 0;
let scale = 1;

// Create trail dots
const trailDots = [];
for(let i = 0; i < 5; i++) {
    const dot = document.createElement('div');
    dot.className = 'mouse-trail';
    document.body.appendChild(dot);
    trailDots.push({
        element: dot,
        x: 0,
        y: 0,
        opacity: 0
    });
}

// Mouse move listener
document.addEventListener('mousemove', (e) => {
    mouseX = e.clientX;
    mouseY = e.clientY;
    scale = 1;
});

// Click effect
document.addEventListener('mousedown', () => {
    scale = 1.5;
});

document.addEventListener('mouseup', () => {
    scale = 1;
});

// Animation loop
function animate() {
    // Move main circle
    circleX += (mouseX - circleX) * 0.2;
    circleY += (mouseY - circleY) * 0.2;

    circle.style.left = `${circleX}px`;
    circle.style.top = `${circleY}px`;
    circle.style.transform = `translate(-50%, -50%) scale(${scale})`;

    // Update trail dots
    trailDots.forEach((dot, index) => {
        setTimeout(() => {
            dot.x += (mouseX - dot.x) * 0.2;
            dot.y += (mouseY - dot.y) * 0.2;
            dot.element.style.left = `${dot.x}px`;
            dot.element.style.top = `${dot.y}px`;
            dot.element.style.opacity = 1 - (index * 0.2);
            dot.element.style.transform = `translate(-50%, -50%) scale(${1 - (index * 0.1)})`;
        }, index * 30);
    });

    requestAnimationFrame(animate);
}

animate();