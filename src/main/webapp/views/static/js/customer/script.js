document.addEventListener("DOMContentLoaded", function () {
    const scene = new THREE.Scene();
    const camera = new THREE.PerspectiveCamera(75, 1, 0.1, 1000);
    const canvasContainer = document.getElementById("canvas-container");

    const renderer = new THREE.WebGLRenderer({ alpha: true });
    renderer.setSize(500, 500);
    canvasContainer.appendChild(renderer.domElement);

    // Lights
    const ambientLight = new THREE.AmbientLight(0xffffff, 1.5);
    scene.add(ambientLight);

    const directionalLight = new THREE.DirectionalLight(0xffffff, 2);
    directionalLight.position.set(2, 2, 2);
    scene.add(directionalLight);

    let car;

    // Load 3D Model
    const loader = new THREE.GLTFLoader();
    loader.load(`${window.location.origin}/megacitycab_war_exploded/views/sites/customer/model/car.glb`, function (gltf) {
        car = gltf.scene;
        car.scale.set(250, 250, 250);
        car.position.set(0, -1, 0);
        car.rotation.set(Math.PI / 1, 0, 3.15); // Start with top view

        scene.add(car);
    }, undefined, function (error) {
        console.error("Error loading model:", error);
    });

    // Initial Camera Position (Top View)
    camera.position.set(0, 10, 0);
    camera.lookAt(0, 0, 0);

    function animate() {
        requestAnimationFrame(animate);
        renderer.render(scene, camera);
    }
    animate();

    // Handle Scroll Animation
    window.addEventListener("scroll", () => {
        const scrollY = window.scrollY;
        const maxScroll = document.body.scrollHeight - window.innerHeight;
        const progress = scrollY / maxScroll; // Normalize between 0 and 1

        if (car) {
            if (progress < 0.33) {
                // From Top View to Back View
                car.rotation.set(Math.PI / 1 - progress * Math.PI / -1, 0, 3.15);
                camera.position.set(1, 10 - progress * 5, -5 * progress);
            } else if (progress < 0.66) {
                // From Back to Side View
                const phaseProgress = (progress - 0.33) / 0.33;
                car.rotation.set(0, -Math.PI / 1 * phaseProgress, 0);
                camera.position.set(-1 * phaseProgress, 1.3, -10);
            } else {
                // From Side to Semi-Front View
                const phaseProgress = (progress - 0.66) / 0.34;
                car.rotation.set(0, -Math.PI / 2 + (Math.PI / 4 * phaseProgress), 0);
                camera.position.set(3 + 3 * phaseProgress, 0, -10 + 3 * phaseProgress);
            }

            camera.lookAt(0, 0, 0);
        }
    });
});
