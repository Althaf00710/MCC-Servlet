let currentFilter = 'all';

function filterUsers() {
    const searchInput = document.getElementById('searchInput');
    const filter = searchInput.value.toLowerCase();
    const table = document.getElementById('usersTable');
    const tr = table.getElementsByTagName('tr');
    let visibleCount = 0;

    for (let i = 1; i < tr.length; i++) {
        const nameCell = tr[i].getElementsByTagName('td')[0];
        const usernameCell = tr[i].getElementsByTagName('td')[1];
        const emailCell = tr[i].getElementsByTagName('td')[2];
        const phoneCell = tr[i].getElementsByTagName('td')[3];
        const statusCell = tr[i].getElementsByTagName('td')[4];

        if (nameCell && usernameCell && emailCell && phoneCell) {
            const name = nameCell.textContent || nameCell.innerText;
            const username = usernameCell.textContent || usernameCell.innerText;
            const email = emailCell.textContent || emailCell.innerText;
            const phone = phoneCell.textContent || phoneCell.innerText;
            const status = statusCell.getAttribute('data-status');

            const matchesSearch = name.toLowerCase().includes(filter) ||
                username.toLowerCase().includes(filter) ||
                email.toLowerCase().includes(filter) ||
                phone.toLowerCase().includes(filter);

            const matchesFilter = currentFilter === 'all' ||
                (currentFilter === 'active' && status === 'ACTIVE') ||
                (currentFilter === 'inactive' && status !== 'ACTIVE' && status !== 'Never') ||
                (currentFilter === 'never' && status === 'Never');

            if (matchesSearch && matchesFilter) {
                tr[i].style.display = '';
                visibleCount++;
            } else {
                tr[i].style.display = 'none';
            }
        }
    }

    // Show/hide empty state
    const emptyState = document.getElementById('emptyState');
    emptyState.style.display = visibleCount === 0 ? 'flex' : 'none';
}

// Event listeners for filter options
document.querySelectorAll('.filter-option').forEach(option => {
    option.addEventListener('click', function() {
        currentFilter = this.getAttribute('data-filter');
        filterUsers();
    });
});

// Initialize search on page load
document.addEventListener('DOMContentLoaded', () => {
    filterUsers();
});