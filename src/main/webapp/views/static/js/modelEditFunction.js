// Edit Modal Functions
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

            // Handle phone number split
            const phoneParts = user.phoneNumber.match(/^(\+\d+)(\d+)$/);
            if(phoneParts) {
                document.querySelector('#editModal select[name=countryCode]').value = phoneParts[1];
                document.querySelector('#editModal input[name=phoneNumber]').value = phoneParts[2];
            }

            // Handle avatar image
            const avatarImg = document.querySelector('#editModal #currentAvatar');
            avatarImg.src = user.avatarUrl
                ? `${window.location.origin}/${user.avatarUrl}`
                : `${window.location.origin}/views/static/images/defaultAvatar.png`;

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