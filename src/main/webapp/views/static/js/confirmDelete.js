function confirmDelete(userId) {
    if (confirm("Are you sure you want to delete this user?")) {
        window.location.href = "/users/delete?id=" + userId;
    }
}
