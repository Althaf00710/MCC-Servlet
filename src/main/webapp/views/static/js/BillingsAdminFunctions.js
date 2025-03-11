function confirmDelete(billingId, bookingId) {
    Swal.fire({
        title: "Are you sure?",
        text: "This action cannot be undone!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#d33",
        cancelButtonColor: "#3085d6",
        confirmButtonText: "Yes, delete it!"
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`${window.location.origin}/megacitycab_war_exploded/billing/delete`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: `billingId=${billingId}&bookingId=${bookingId}`
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        Swal.fire("Deleted!", "Billing has been deleted.", "success")
                            .then(() => location.reload());
                    } else {
                        Swal.fire("Error!", "Failed to delete billing.", "error");
                    }
                })
                .catch(() => Swal.fire("Error!", "An unexpected error occurred.", "error"));
        }
    });
}

