function openEditModal(id) {
    fetch(`${window.location.origin}/megacitycab_war_exploded/cabbrand/edit?id=` + id)
        .then(response => response.json())
        .then(data => {
            if (data) {
                document.getElementById('editBrandId').value = data.id;
                document.getElementById('editBrandName').value = data.brandName;
                document.getElementById('EditModal').classList.remove('hidden');
            }
        })
        .catch(error => console.error('Error fetching brand data:', error));
}

function closeEditModal() {
    document.getElementById('EditModal').classList.add('hidden');
}
