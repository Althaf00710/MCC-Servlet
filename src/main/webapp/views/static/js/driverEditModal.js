// Add this JavaScript code
function openEditDriverModal(driverId) {
    fetch(`${window.location.origin}/megacitycab_war_exploded/drivers/edit?id=${driverId}`)
        .then(response => response.json())
        .then(driver => {
            // Populate form fields
            const driverIdInputs = document.querySelectorAll('#editDriverModal input[name=id]');
            driverIdInputs.forEach(input => input.value = driver.id);
            document.querySelector('#editDriverModal input[name=name]').value = driver.name;
            document.querySelector('#editDriverModal input[name=nicNumber]').value = driver.nicNumber;
            document.querySelector('#editDriverModal input[name=licenceNumber]').value = driver.licenceNumber;
            document.querySelector('#editDriverModal input[name=phoneNumber]').value = driver.phoneNumber;
            document.querySelector('#editDriverModal input[name=email]').value = driver.email;

            // Handle avatar image
            const avatarImg = document.querySelector('#editDriverModal #currentDriverAvatar');
            avatarImg.src = driver.avatarUrl
                ? `${window.location.origin}/megacitycab_war_exploded/${driver.avatarUrl}`
                : `${window.location.origin}/megacitycab_war_exploded/views/static/images/defaultAvatar.png`;

            // Show modal
            document.getElementById('editDriverModal').classList.remove('hidden');
        })
        .catch(error => console.error('Error:', error));
}

function closeEditDriverModal() {
    document.getElementById('editDriverModal').classList.add('hidden');
}

//Image Preview Handling
document.querySelector('#editDriverModal input[name=avatar]').addEventListener('change', function(e) {
    const reader = new FileReader();
    reader.onload = function() {
        document.querySelector('#editDriverModal #currentDriverAvatar').src = reader.result;
    }
    if(this.files[0]) {
        reader.readAsDataURL(this.files[0]);
        document.getElementById('driverSubmitImageBtn').classList.remove('hidden');
    }
});

// Image Upload Handling
document.getElementById('driverSubmitImageBtn').addEventListener('click', function() {
    const formData = new FormData();
    const driverId = document.querySelector('#editDriverModal input[name=id]').value;
    const fileInput = document.querySelector('#editDriverModal input[name=avatar]');

    if (!fileInput.files[0]) {
        alert('Please select an image first');
        return;
    }

    formData.append('id', driverId);
    formData.append('avatar', fileInput.files[0]);

    fetch(`${window.location.origin}/megacitycab_war_exploded/drivers/updateImage`, {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Update avatar with cache-busting timestamp
                document.getElementById('currentDriverAvatar').src =
                    `${window.location.origin}/megacitycab_war_exploded/${data.avatarUrl}?t=${Date.now()}`;

                // Reset upload elements
                fileInput.value = '';
                this.classList.add('hidden');

                // Show success feedback
                const feedback = document.createElement('div');
                feedback.className = 'text-green-500 text-sm mt-2 text-center';
                feedback.textContent = 'Avatar updated successfully!';
                document.querySelector('#editDriverModal .relative.group').appendChild(feedback);
                setTimeout(() => feedback.remove(), 3000);
            } else {
                throw new Error(data.message || 'Failed to update avatar');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert(`Error updating avatar: ${error.message}`);
        });
});


// Close modal when clicking outside
window.onclick = function(event) {
    const modal = document.getElementById('editDriverModal');
    if (event.target === modal) {
        closeEditDriverModal();
    }
}