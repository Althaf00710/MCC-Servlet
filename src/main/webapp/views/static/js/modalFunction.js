function openModal() {
    document.getElementById('Modal').classList.remove('hidden');
}

function closeModal() {
    document.getElementById('Modal').classList.add('hidden');
}

// Close when clicking outside
window.onclick = function(event) {
    const modal = document.getElementById('Modal');
    if (event.target === modal) {
        closeModal();
    }
}