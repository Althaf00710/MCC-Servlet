function loadNonAssignedDrivers(cabId) {
    fetch(`${window.location.origin}/megacitycab_war_exploded/drivers/nonassigned?cabId=${cabId}`)
        .then(response => response.json())
        .then(drivers => {
            const dropdown = document.getElementById(`assignDriver_${cabId}`);
            if (dropdown) {
                dropdown.innerHTML = '<option value="">Select Driver</option>';
                drivers.forEach(driver => {
                    dropdown.innerHTML += `<option value="${driver.id}">${driver.name}</option>`;
                });
            }
        })
        .catch(error => {
            console.error(`Error loading non-assigned drivers for cab ${cabId}:`, error);
        });
}

function assignDriver(cabId) {
    const driverId = document.getElementById(`assignDriver_${cabId}`).value;
    if (!driverId) {
        alert("Please select a driver.");
        return;
    }

    fetch('/megacitycab_war_exploded/cabassign/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `cabId=${cabId}&driverId=${driverId}`
    }).then(response => {
        if (response.ok) {
            location.reload(); // Reload to update the UI
        } else {
            alert("Failed to assign driver.");
        }
    })
        .catch(error => {
            console.error(`Error assigning driver to cab ${cabId}:`, error);
            alert("Failed to assign driver due to a network error.");
        });
}

function cancelAssign(assignId) {
    fetch('/megacitycab_war_exploded/cabassign/cancel', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `id=${assignId}`
    }).then(response => {
        if (response.ok) {
            location.reload(); // Reload to update the UI
        } else {
            alert("Failed to cancel assignment.");
        }
    })
        .catch(error => {
            console.error(`Error canceling assignment ${assignId}:`, error);
            alert("Failed to cancel assignment due to a network error.");
        });
}

async function fetchCabAssignments() {
    try {
        // Get all cab rows first to ensure we have all the cab IDs
        const cabCells = document.querySelectorAll("[data-cab-id]");
        const cabIds = Array.from(cabCells).map(cell => parseInt(cell.getAttribute("data-cab-id")));

        // Fetch assignments
        const response = await fetch(`/megacitycab_war_exploded/cabassign/list`);
        const assignments = await response.json();
        console.log("Cab Assignments:", assignments);

        // Create a map of cab ID to assignment
        const assignmentMap = {};
        assignments.forEach(assign => {
            assignmentMap[assign.cabId] = assign;
        });

        // Update each cab cell
        for (const cabCell of cabCells) {
            const cabId = parseInt(cabCell.getAttribute("data-cab-id"));
            const assignment = assignmentMap[cabId];

            if (assignment) {
                // This cab has an assigned driver
                const driver = assignment.driver;
                cabCell.innerHTML = `
                    <div class="flex items-center justify-between gap-2">
                        <div class="flex items-center gap-2 flex-1">
                            <img src="${window.location.origin}/megacitycab_war_exploded/${driver.avatarUrl || '/views/static/images/defaultAvatar.png'}" 
                                 alt="Driver Avatar" 
                                 class="w-10 h-10 rounded-full">
                            <div class="flex-1">
                                <p class="font-medium">${driver.name}</p>
                                <p class="text-sm text-gray-500">
                                    Assigned: ${new Date(assignment.assignDate).toLocaleDateString()}
                                </p>
                            </div>
                        </div>
                        <button class="bg-red-500 text-white px-1.5 py-1.5 rounded-full hover:cursor-pointer" onclick="cancelAssign(${assignment.id})">
                            <i class="fi fi-rr-cross text-xs"></i>
                        </button>
                    </div>
                `;
            } else {
                // This cab needs a driver selection dropdown
                loadDriverDropdown(cabId, cabCell);
            }
        }
    } catch (error) {
        console.error("Error fetching cab assignments:", error);
    }
}

async function loadDriverDropdown(cabId, cabCell) {
    try {
        const response = await fetch(`/megacitycab_war_exploded/drivers/nonassigned?cabId=${cabId}`);
        const drivers = await response.json();

        let options = `<option value="">Assign Driver</option>`;
        drivers.forEach(driver => {
            options += `<option value="${driver.id}">${driver.name}</option>`;
        });

        cabCell.innerHTML = `
            <div class="flex items-center justify-between gap-2">
                <select id="assignDriver_${cabId}" class="bg-gray-100 border border-gray-500 px-2 py-2 rounded-lg shadow-sm focus:outline-none focus:ring-1 focus:ring-orange-500 hover:border-orange-500">
                    ${options}
                </select>
                <button class="bg-green-500 text-white px-1.5 py-1.5 rounded-full hover:bg-green-600 transition-colors duration-200" onclick="assignDriver(${cabId})">
                    <i class="fi fi-rr-check text-xs"></i>
                </button>
            </div>
        `;
    } catch (error) {
        console.error(`Error loading drivers for cab ${cabId}:`, error);
        cabCell.innerHTML = `<div class="text-red-500">Error loading drivers</div>`;
    }
}

// Initialize when the DOM is loaded
document.addEventListener("DOMContentLoaded", fetchCabAssignments);