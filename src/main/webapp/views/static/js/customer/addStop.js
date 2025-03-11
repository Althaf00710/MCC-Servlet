// const addStopBtn = document.getElementById('addStop');
// let stopCounter = 1;
//
// function createStopRow() {
//     const stopDiv = document.createElement('div');
//     stopDiv.className = 'flex gap-3 items-center';
//     stopDiv.innerHTML = `
//         <input type="text" placeholder="Stop ${stopCounter}"
//             class="input-field flex-1">
//         <input type="number" placeholder="Wait (mins)"
//             class="input-field w-24">
//         <button class="text-red-500 hover:text-red-600 p-2 delete-stop">
//             <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
//                 <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
//                     d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
//             </svg>
//         </button>
//     `;
//
//     // Initialize autocomplete for new stop
//     const autocomplete = new google.maps.places.Autocomplete(
//         stopDiv.querySelector('input[type="text"]'),
//         { types: ['geocode'] }
//     );
//
//     stopCounter++;
//     return stopDiv;
// }
//
// addStopBtn.addEventListener('click', () => {
//     const stopsContainer = document.getElementById('stopsContainer');
//     stopsContainer.appendChild(createStopRow());
// });
//
// document.addEventListener('click', (e) => {
//     if (e.target.closest('.delete-stop')) {
//         e.target.closest('div').remove();
//     }
// });