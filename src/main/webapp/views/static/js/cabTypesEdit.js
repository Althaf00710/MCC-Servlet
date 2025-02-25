// Edit CabType Modal Functions
function openEditCabTypeModal(cabTypeId) {
    fetch(`${window.location.origin}/megacitycab_war_exploded/cabtypes/edit?id=${cabTypeId}`)
        .then(response => {
            if (!response.ok) throw new Error('Network response was not ok');
            return response.json();
        })
        .then(cabType => {
            const form = document.forms.cabTypeDetailsForm;
            form.id.value = cabType.id;
            form.typeName.value = cabType.typeName;
            form.capacity.value = cabType.capacity;
            form.baseFare.value = cabType.baseFare;
            form.baseWaitFare.value = cabType.baseWaitFare;

            document.forms.cabTypeImageForm.id.value = cabType.id;
            // Update image
            const imageUrl = cabType.imageUrl
                ? `${window.location.origin}/megacitycab_war_exploded/${cabType.imageUrl}`
                : `${window.location.origin}/megacitycab_war_exploded/views/static/images/defaultCab.png`;

            document.getElementById('currentCabTypeImage').src = imageUrl;

            // Show modal
            document.getElementById('editCabTypeModal').classList.remove('hidden');
        })
        .catch(error => {
            console.error("Image Upload error: ", error);
        });
}

function closeEditCabTypeModal() {
    document.getElementById('editCabTypeModal').classList.add('hidden');
}

// Image Preview Handling
document.querySelector('input[name=image]').addEventListener('change', function(e) {
    const reader = new FileReader();
    reader.onload = () => {
        document.getElementById('currentCabTypeImage').src = reader.result;
    }
    if(this.files[0]) reader.readAsDataURL(this.files[0]);
});

// Close modal on outside click
document.getElementById('editCabTypeModal').addEventListener('click', (e) => {
    if (e.target === document.getElementById('editCabTypeModal')) {
        closeEditCabTypeModal();
    }
});

document.addEventListener('DOMContentLoaded', () => {
    // Image Upload Form
    document.getElementById('cabTypeImageForm')?.addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);

        try {
            const response = await fetch(`${window.location.origin}/megacitycab_war_exploded/cabtypes/updateImageUrl`, {
                method: 'POST',
                body: formData
            });

            if (response.ok) {
                const data = await response.json();
                document.getElementById('currentCabTypeImage').src =
                    `${window.location.origin}/megacitycab_war_exploded/${data.imageUrl}?t=${Date.now()}`;
                showFeedback('Image updated successfully!');
            } else {
                throw new Error('Image update failed');
            }
        } catch (error) {
            console.error('Error:', error);
            alert(`Error updating image: ${error.message}`);
        }
    });

    // Details Update Form
    // document.getElementById('cabTypeDetailsForm')?.addEventListener('submit', async (e) => {
    //     e.preventDefault();
    //     const formData = {
    //         id: e.target.id.value,
    //         typeName: e.target.typeName.value,
    //         capacity: e.target.capacity.value,
    //         baseFare: e.target.baseFare.value,
    //         baseWaitFare: e.target.baseWaitFare.value
    //     };
    //
    //     try {
    //         const response = await fetch(`${window.location.origin}/megacitycab_war_exploded/cabtypes/update`, {
    //             method: 'POST',
    //             headers: {
    //                 'Content-Type': 'application/json',
    //             },
    //             body: JSON.stringify(formData)
    //         });
    //
    //         if (response.ok) {
    //             window.location.reload();
    //         } else {
    //             throw new Error('Details update failed');
    //         }
    //     } catch (error) {
    //         console.error('Error:', error);
    //         alert(`Error updating details: ${error.message}`);
    //     }
    // });

    // Image Preview
    document.querySelector('input[name=image]')?.addEventListener('change', function(e) {
        const reader = new FileReader();
        reader.onload = () => {
            document.getElementById('currentCabTypeImage').src = reader.result;
        };
        if (this.files[0]) reader.readAsDataURL(this.files[0]);
    });
});

// Helper function for feedback messages
function showFeedback(message) {
    const feedback = document.createElement('div');
    feedback.className = 'text-green-500 text-sm mt-2 text-center';
    feedback.textContent = message;
    document.querySelector('#cabTypeImageForm').appendChild(feedback);
    setTimeout(() => feedback.remove(), 3000);
}