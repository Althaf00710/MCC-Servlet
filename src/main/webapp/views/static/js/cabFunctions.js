window.onload = function() {
    // Fetch Cab Brands
    fetch(`${window.location.origin}/megacitycab_war_exploded/cabbrand/get`)
        .then(response => response.json())
        .then(data => {
            const cabBrandSelect = document.getElementById('cabBrandId');
            data.forEach(brand => {
                const option = document.createElement('option');
                option.value = brand.id;
                option.textContent = brand.brandName;
                cabBrandSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error fetching cab brands:', error));

    // Fetch Cab Types
    fetch(`${window.location.origin}/megacitycab_war_exploded/cabtypes/get`)
        .then(response => response.json())
        .then(data => {
            const cabTypeSelect = document.getElementById('cabTypeId');
            data.forEach(type => {
                const option = document.createElement('option');
                option.value = type.id;
                option.textContent = type.typeName;
                cabTypeSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error fetching cab types:', error));
};