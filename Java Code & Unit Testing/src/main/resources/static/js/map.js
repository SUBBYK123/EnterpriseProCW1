let map;
let markersByCategory = {};  // Holds markers grouped by dataset name
let uploadedFiles = new Set();
const bradfordCenter = { lat: 53.795, lng: -1.759 };

// Array of marker colors
const markerColors = ["blue", "red", "yellow", "green", "purple"];
let currentColorIndex = 0;

// Initialize the map
function initMap() {
    map = new google.maps.Map(document.getElementById("map"), {
        center: bradfordCenter,
        zoom: 12
    });
    console.log('Map initialized');
}

// Fetch API key dynamically and load the Google Maps script
fetch('/api/maps-key')
    .then(response => response.text())
    .then(apiKey => {
        const script = document.createElement("script");
        script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&callback=initMap&libraries=places`;
        script.async = true;
        script.defer = true;
        document.body.appendChild(script);
    })
    .catch(error => console.error("Error fetching API key:", error));

// Handle file upload
function handleFileUpload() {
    const fileInput = document.getElementById('fileInput');
    const file = fileInput.files[0];

    if (!file) {
        alert("Please select a file.");
        return;
    }

    const fileName = file.name;

    // Prevent duplicate file uploads
    if (uploadedFiles.has(fileName)) {
        alert("This dataset has already been uploaded.");
        return;
    }

    const reader = new FileReader();
    reader.onload = function (event) {
        const fileContent = event.target.result;
        const fileType = file.name.split('.').pop().toLowerCase();
        const categoryName = file.name.split('.')[0]; // Use filename as category

        if (fileType === "csv") {
            parseCSV(fileContent, categoryName);
        } else if (fileType === "json") {
            parseJSON(fileContent, categoryName);
        } else {
            alert("Please upload a CSV or JSON file.");
        }

        uploadedFiles.add(fileName);
        document.getElementById("fileInput").value = "";
    };

    reader.readAsText(file);
}

// Parse CSV Data
function parseCSV(csvData, categoryName) {
    const rows = csvData.split("\n");
    const headers = rows[0].split(",").map(header => header.trim());

    const latIndex = headers.findIndex(header => header.toLowerCase() === "latitude");
    const lngIndex = headers.findIndex(header => header.toLowerCase() === "longitude");

    if (latIndex === -1 || lngIndex === -1) {
        alert("No Latitude or Longitude columns found.");
        return;
    }
    var errorInDataset = 0;
    const markerColor = markerColors[currentColorIndex % markerColors.length];
    currentColorIndex++;

    const newMarkers = rows.slice(1).map(row => {
        const columns = row.split(",");
        const lat = parseFloat(columns[latIndex]);
        const lng = parseFloat(columns[lngIndex]);

        if (!isNaN(lat) && !isNaN(lng)) {
            const marker = new google.maps.Marker({
                position: { lat, lng },
                map: map,
                title: `Lat: ${lat}, Lng: ${lng}`,
                icon: { url: `http://maps.google.com/mapfiles/ms/icons/${markerColor}-dot.png` }
            });

            const infoWindow = new google.maps.InfoWindow({
                content: `<strong>Latitude:</strong> ${lat}<br><strong>Longitude:</strong> ${lng}`
            });

            marker.addListener("click", () => {
                infoWindow.open(map, marker);
            });

            return marker;
        } else {
            errorInDataset += 1
        }
    }).filter(marker => marker !== undefined);

    markersByCategory[categoryName] = newMarkers;
    createFilters();
    updateMarkersVisibility();
    alert(`Dataset successfully added with ${errorInDataset} errors.`);
}

// Parse JSON Data
function parseJSON(jsonData, categoryName) {
    const data = JSON.parse(jsonData);
    const markerColor = markerColors[currentColorIndex % markerColors.length];
    currentColorIndex++;

    const newMarkers = data.map(item => {
        const lat = parseFloat(item.Latitude);
        const lng = parseFloat(item.Longitude);

        if (!isNaN(lat) && !isNaN(lng)) {
            const marker = new google.maps.Marker({
                position: { lat, lng },
                map: map,
                title: `Lat: ${lat}, Lng: ${lng}`,
                icon: { url: `http://maps.google.com/mapfiles/ms/icons/${markerColor}-dot.png` }
            });

            const infoWindow = new google.maps.InfoWindow({
                content: `<strong>Latitude:</strong> ${lat}<br><strong>Longitude:</strong> ${lng}`
            });

            marker.addListener("click", () => {
                infoWindow.open(map, marker);
            });

            return marker;
        }
    }).filter(marker => marker !== undefined);

    markersByCategory[categoryName] = newMarkers;
    createFilters();
    updateMarkersVisibility();
}

// Create filter checkboxes
function createFilters() {
    const filtersContainer = document.getElementById("filters-container");
    filtersContainer.innerHTML = "";

    Object.keys(markersByCategory).forEach(category => {
        let label = document.createElement("label");
        let checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        checkbox.classList.add("filter-checkbox");
        checkbox.id = category.toLowerCase().replace(/\s+/g, "-");
        checkbox.checked = true;
        checkbox.addEventListener("change", updateMarkersVisibility);
        label.appendChild(checkbox);
        label.appendChild(document.createTextNode(` ${category}`));
        filtersContainer.appendChild(label);
    });
}

// Update marker visibility based on checkbox selection
function updateMarkersVisibility() {
    Object.keys(markersByCategory).forEach(category => {
        const checkbox = document.getElementById(category.toLowerCase().replace(/\s+/g, "-"));
        const isVisible = checkbox.checked;

        markersByCategory[category].forEach(marker => {
            marker.setMap(isVisible ? map : null);
        });
    });
}

// Toggle visibility of all markers
function toggleSelectAll() {
    const checkboxes = document.querySelectorAll(".filter-checkbox");
    const allChecked = [...checkboxes].every(checkbox => checkbox.checked);

    checkboxes.forEach(checkbox => {
        checkbox.checked = !allChecked;
    });

    updateMarkersVisibility();
}
