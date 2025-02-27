// Define Bradford's coordinates
var bradfordCenter = [53.795, -1.759];

// Define the bounding box (southwest corner & northeast corner)
var bounds = L.latLngBounds(
    [53, -2], // Southwest corner (Bottom-left)
    [54, -1.5]  // Northeast corner (Top-right)
);

// Initialize the map centered on Bradford with restricted bounds
var map = L.map('map', {
    center: bradfordCenter,
    zoom: 12, // Start at an appropriate zoom level
    minZoom: 10, // Prevent zooming out too far
    maxBounds: bounds, // Restrict movement to Bradford area
    maxBoundsViscosity: 1.0 // Prevent dragging outside completely
});

// Load OpenStreetMap tiles
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap contributors',
    maxZoom: 18,
    minZoom: 10
}).addTo(map);

// Fix potential rendering issues
setTimeout(() => { map.invalidateSize(); }, 500);

function zoomIn() {
    map.zoomIn();
}

function zoomOut() {
    map.zoomOut();
}
const filterOptions = ["Schools", "Hospitals", "Parks", "Shops","Test"]; // Add/remove filters here
const filtersContainer = document.getElementById('filters-container');

function createFilters() {
    filtersContainer.innerHTML = ""; // Clear existing filters
    filterOptions.forEach(filter => {
        let label = document.createElement('label');
        let checkbox = document.createElement('input');
        checkbox.type = 'checkbox';
        checkbox.classList.add('filter-checkbox');
        checkbox.id = filter.toLowerCase().replace(/\s+/g, '-');
        label.appendChild(checkbox);
        label.appendChild(document.createTextNode(` ${filter}`));
        filtersContainer.appendChild(label);
    });
}

createFilters();

function toggleSelectAll() {
    const checkboxes = document.querySelectorAll('.filter-checkbox');
    const allChecked = [...checkboxes].every(checkbox => checkbox.checked);

    checkboxes.forEach(checkbox => {
        checkbox.checked = !allChecked; // Toggle all checkboxes
    });
}
var customIcon = L.icon({
    iconUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon.png', // Use default Leaflet icon image
    iconSize: [25, 41],  // Size of the marker
    iconAnchor: [12, 41], // Point of the icon which will correspond to marker's location
    popupAnchor: [1, -34], // Position of the popup relative to the icon
    shadowSize: [41, 41] // Shadow size
});
var universityCoordinates = [53.7951, -1.7590];
var universityMarker = L.marker(universityCoordinates, { icon: customIcon }).addTo(map);

// Add a popup to the marker
universityMarker.bindPopup("<b>University of Bradford</b><br>Location: Bradford, UK").openPopup();