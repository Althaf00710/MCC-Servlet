function openEditDriverModal(customerId) {
    fetch(`${window.location.origin}/megacitycab_war_exploded/customers/edit?id=${customerId}`)
        .then(response => response.json())
        .then(customer => {
            // Populate form fields
            const driverIdInputs = document.querySelectorAll('#EditCustomerModal input[name=id]');
            driverIdInputs.forEach(input => input.value = customer.id);
            document.querySelector('#EditCustomerModal input[name=name]').value = customer.name;
            document.querySelector('#EditCustomerModal input[name=address]').value = customer.address;
            document.querySelector('#EditCustomerModal input[name=nicNumber]').value = customer.nicNumber;
            document.querySelector('#EditCustomerModal input[name=phoneNumber]').value = customer.phoneNumber;
            document.querySelector('#EditCustomerModal input[name=email]').value = customer.email;

            // Show modal
            document.getElementById('EditCustomerModal').classList.remove('hidden');
        })
        .catch(error => console.error('Error:', error));
}

function closeEditDriverModal() {
    document.getElementById('EditCustomerModal').classList.add('hidden');
}

