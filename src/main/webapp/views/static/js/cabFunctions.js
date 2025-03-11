
    function fetchCabBrands(selectedBrandId = null) {
    fetch(`${window.location.origin}/megacitycab_war_exploded/cabbrand/get`)
        .then(response => response.json())
        .then(data => {
            const cabBrandSelect = document.getElementById('editCabBrandId');
            cabBrandSelect.innerHTML = '<option value="">Select Brand</option>';
            data.forEach(brand => {
                const option = document.createElement('option');
                option.value = brand.id;
                option.textContent = brand.brandName;
                if (selectedBrandId && selectedBrandId == brand.id) {
                    option.selected = true;
                }
                cabBrandSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error fetching cab brands:', error));
}

    function fetchCabTypes(selectedTypeId = null) {
    fetch(`${window.location.origin}/megacitycab_war_exploded/cabtypes/get`)
        .then(response => response.json())
        .then(data => {
            const cabTypeSelect = document.getElementById('editCabTypeId');
            cabTypeSelect.innerHTML = '<option value="">Select Type</option>';
            data.forEach(type => {
                const option = document.createElement('option');
                option.value = type.id;
                option.textContent = type.typeName;
                if (selectedTypeId && selectedTypeId == type.id) {
                    option.selected = true;
                }
                cabTypeSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error fetching cab types:', error));
}

    function openEditModal(cabId) {
    fetch(`${window.location.origin}/megacitycab_war_exploded/cabs/edit?id=${cabId}`)
        .then(response => response.json())
        .then(cab => {
            document.getElementById('editCabId').value = cabId;
            document.getElementById('editCabName').value = cab.cabName;
            document.getElementById('editRegistrationNumber').value = cab.registrationNumber;
            document.getElementById('editPlateNumber').value = cab.plateNumber;

            fetchCabBrands(cab.cabBrandId);
            fetchCabTypes(cab.cabTypeId);

            document.getElementById('editModal').classList.remove('hidden');
        })
        .catch(error => console.error('Error fetching cab details:', error));
}

    function closeEditModal() {
    document.getElementById('editModal').classList.add('hidden');
}