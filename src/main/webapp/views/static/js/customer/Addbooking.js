window.appData = window.appData || {
    companyData: null,
    selectedCabType: null,
    totalDistance: 0
};

async function fetchCabTypes() {
    try {
        const response = await fetch(`${window.location.origin}/megacitycab_war_exploded/cabtypes/get`);
        if (!response.ok) throw new Error("Failed to fetch cab types");
        const cabTypes = await response.json();
        displayCabTypes(cabTypes);
    } catch (error) {
        console.error("Error fetching cab types:", error);
    }
}

async function fetchCabsByType(cabTypeId) {
    try {
        const response = await fetch(`${window.location.origin}/megacitycab_war_exploded/cabtypes?id=${cabTypeId}`);
        if (!response.ok) {
            throw new Error("Failed to fetch cabs for type " + cabTypeId);
        }
        const cabs = await response.json();
        console.log("Cabs fetched for type " + cabTypeId + ":", cabs);
        // Optionally, update UI or store the fetched data as needed
    } catch (error) {
        console.error("Error fetching cabs by type:", error);
    }
}

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
            "rounded-xl",
            "shadow-md",
            "hover:shadow-xs",
            "transition-shadow",
            "duration-200",
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

        // Add click event to select cab type
        box.addEventListener("click", function() {
            document.querySelectorAll(".cab-type-box").forEach(b => {
                b.classList.remove("border-orange-500", "bg-orange-100", "selected");
            });
            this.classList.add("border-orange-500", "selected");
            window.appData.selectedCabType = cabType;
            document.getElementById("selectedCabType").value = this.dataset.id;
            fetchCabsByType(this.dataset.id); // Assuming this function is defined elsewhere

            if (window.appData.totalDistance > 0) {
                updateFare(window.appData.totalDistance);
            }
        });

        container.appendChild(box);
    });
}

async function fetchCompanyData() {
    try {
        const response = await fetch(`${window.location.origin}/megacitycab_war_exploded/companydata/get`);
        if (!response.ok) throw new Error("Failed to fetch company data");
        window.appData.companyData = await response.json();
        console.log("Company data fetched:", window.appData.companyData);
        if (window.appData.selectedCabType && window.appData.totalDistance > 0) {
            updateFare(window.appData.totalDistance);
        }
    } catch (error) {
        console.error("Error fetching company data:", error);
    }
}

async function initializeData() {
    await Promise.all([fetchCabTypes(), fetchCompanyData()]);
}
initializeData();
