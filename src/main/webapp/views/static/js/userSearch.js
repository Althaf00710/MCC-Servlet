let currentFilter = null;

document.addEventListener('click', function(e) {
    const filterOption = e.target.closest('.filter-option');
    if (filterOption) {
        currentFilter = currentFilter === filterOption.dataset.filter ? null : filterOption.dataset.filter;
        filterUsers();
        document.querySelectorAll('.filter-option').forEach(opt =>
            opt.classList.remove('bg-orange-100'));
        if (currentFilter) filterOption.classList.add('bg-orange-100');
    }
});

function filterUsers() {
    const search = document.getElementById('searchInput').value.toLowerCase();
    const rows = document.querySelectorAll('#usersTable tbody tr');
    let hasVisibleRows = false;

    const emptyState = document.getElementById('emptyState');
    emptyState.classList.toggle('hidden', hasVisibleRows);

    rows.forEach(row => {
        const name = row.cells[0].textContent.toLowerCase();
        const username = row.cells[1].textContent.toLowerCase();
        const email = row.cells[2].textContent.toLowerCase();
        const status = row.cells[4].dataset.status;

        const matchesSearch = name.includes(search) ||
            username.includes(search) ||
            email.includes(search);

        let matchesFilter = true;
        if(currentFilter) {
            switch(currentFilter) {
                case 'active': matchesFilter = status === 'ACTIVE'; break;
                case 'inactive': matchesFilter = status !== 'ACTIVE' && status !== 'NEVER'; break;
                case 'never': matchesFilter = status === 'NEVER'; break;
            }
        }

        const shouldShow = matchesSearch && matchesFilter;
        row.style.display = shouldShow ? '' : 'none';
        if(shouldShow) hasVisibleRows = true;
    });

    // Show/hide table header container based on results
    const tableHeader = document.querySelector('#usersTable thead');
    const tableContainer = document.querySelector('#usersTable');
    tableHeader.style.visibility = hasVisibleRows ? 'visible' : 'visible'; // Always visible
    tableContainer.style.minHeight = hasVisibleRows ? 'auto' : '200px'; // Maintain minimum height
}