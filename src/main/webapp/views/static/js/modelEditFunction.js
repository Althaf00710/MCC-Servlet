function openEditModal(userId) {
    fetch(`${window.location.origin}/megacitycab_war_exploded/users/edit?id=${userId}`)
        .then(response => response.json())
        .then(user => {
            // Populate form fields
            document.querySelector('#editModal input[name=id]').value = user.id;
            document.querySelector('#editModal input[name=firstName]').value = user.firstName;
            document.querySelector('#editModal input[name=lastName]').value = user.lastName;
            document.querySelector('#editModal input[name=username]').value = user.username;
            document.querySelector('#editModal input[name=email]').value = user.email;
            document.querySelector('#editModal select[name=countryCode]').value = user.countryCode;
            // Remove country code from the phone number and set it
            document.querySelector('#editModal input[name=phoneNumber]').value = user.phoneNumber ? user.phoneNumber.replace(user.countryCode, '') : '';
            // Handle avatar image
            const avatarImg = document.querySelector('#editModal #currentAvatar');
            avatarImg.src = user.avatarUrl
                ? `${window.location.origin}/megacitycab_war_exploded/${user.avatarUrl}`
                : `${window.location.origin}/megacitycab_war_exploded/views/static/images/defaultAvatar.png`;

            // Show modal
            document.getElementById('editModal').classList.remove('hidden');
        })
        .catch(error => console.error('Error:', error));
}

function closeEditModal() {
    document.getElementById('editModal').classList.add('hidden');
}

// Add image preview functionality for Edit Modal
document.querySelector('#editModal input[name=avatar]').addEventListener('change', function(e) {
    const reader = new FileReader();
    reader.onload = function() {
        document.querySelector('#editModal #currentAvatar').src = reader.result;
    }
    if(this.files[0]) reader.readAsDataURL(this.files[0]);
});

// Image Upload Handling
const avatarInput = document.getElementById('avatarInput');
const submitImageBtn = document.getElementById('submitImageBtn');

// When file is selected
avatarInput.addEventListener('change', function(e) {
    if (this.files && this.files[0]) {
        // Show preview
        const reader = new FileReader();
        reader.onload = function(e) {
            document.getElementById('currentAvatar').src = e.target.result;
        }
        reader.readAsDataURL(this.files[0]);

        // Show submit button
        submitImageBtn.classList.remove('hidden');
    }
});

// When submit button is clicked
submitImageBtn.addEventListener('click', function() {
    const formData = new FormData();
    const userId = document.querySelector('#editModal input[name=id]').value;

    formData.append('id', userId);
    formData.append('avatar', avatarInput.files[0]);

    fetch('${pageContext.request.contextPath}/users/updateImage', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Update avatar URL permanently
                document.getElementById('currentAvatar').src =
                    '${pageContext.request.contextPath}/' + data.avatarUrl;
                // Reset input and hide button
                avatarInput.value = '';
                submitImageBtn.classList.add('hidden');
            }
        })
        .catch(error => console.error('Error:', error));
});

window.onclick = function(event) {
    const modal = document.getElementById('editModal');
    if (event.target === modal) {
        closeEditModal();
    }
}