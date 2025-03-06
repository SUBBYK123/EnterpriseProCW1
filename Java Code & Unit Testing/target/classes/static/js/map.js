let map;
let markersByCategory = {};  // This will hold markers grouped by dataset name
let uploadedFiles = new Set();
const bradfordCenter = { lat: 53.795, lng: -1.759 };

// Array of colors for the markers (you can add more colors if needed)
const markerColors = ["blue", "red", "yellow", "green", "purple"];
let currentColorIndex = 0;  // To cycle through the colors

// Initialize the map
function initMap() {
    map = new google.maps.Map(document.getElementById("map"), {
        center: bradfordCenter,
        zoom: 12
    });
    console.log('Map initialized');
}

// Fetch the API key dynamically and load the Google Maps script
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

    // Check if the file has already been uploaded
    if (uploadedFiles.has(fileName)) {
        alert("This dataset has already been uploaded.");
        return; // Prevent further processing of this file
    }

    const reader = new FileReader();
    reader.onload = function(event) {
        const fileContent = event.target.result;
        const fileType = file.name.split('.').pop().toLowerCase();

        // Determine the category based on the file (you can customize this logic)
        const categoryName = file.name.split('.')[0]; // Assuming the file name is the category name

        if (fileType === "csv") {
            parseCSV(fileContent, categoryName);
        } else if (fileType === "json") {
            parseJSON(fileContent, categoryName);
        } else {
            alert("Please upload a CSV or JSON file.");
        }

        // Add the file name to the uploaded files set
        uploadedFiles.add(fileName);
    };

    reader.readAsText(file);
}

// Parse CSV data
function parseCSV(csvData, categoryName) {
    const rows = csvData.split("\n");
    const headers = rows[0].split(",").map(header => header.trim());  // Clean up any unwanted whitespace or \r

    const latIndex = headers.indexOf("Latitude");
    const lngIndex = headers.indexOf("Longitude");

    if (latIndex === -1 || lngIndex === -1) {
        alert("No Latitude or Longitude columns found.");
        return;
    }

    // Get the current color for this dataset
    const markerColor = markerColors[currentColorIndex % markerColors.length];
    currentColorIndex++;  // Increment and cycle through the colors

    // Create markers based on Latitude and Longitude columns
    const newMarkers = rows.slice(1).map(row => {
        const columns = row.split(",");
        const lat = parseFloat(columns[latIndex]);
        const lng = parseFloat(columns[lngIndex]);

        if (!isNaN(lat) && !isNaN(lng)) {
            return new google.maps.Marker({
                position: { lat, lng },
                map: map, // Add marker to the map immediately
                title: `Lat: ${lat}, Lng: ${lng}`, // Just using lat and lng as title
                icon: {
                    // Use default Google Maps marker icons and set the color
                    url: `http://maps.google.com/mapfiles/ms/icons/${markerColor}-dot.png`
                }
            });
        }
    }).filter(marker => marker !== undefined);

    markersByCategory[categoryName] = newMarkers; // Store markers by category
    displayMarkersByCategory(categoryName);  // Display the markers for this category
    createFilters();  // Create the filters based on the datasets
    updateMarkersVisibility(); // Show markers immediately after upload
}

// Parse JSON data
function parseJSON(jsonData, categoryName) {
    const data = JSON.parse(jsonData);
    const markers = data.map(item => {
        const lat = item.Latitude;
        const lng = item.Longitude;

        // Get the current color for this dataset
        const markerColor = markerColors[currentColorIndex % markerColors.length];
        currentColorIndex++;  // Increment and cycle through the colors

        return {
            position: { lat, lng },
            title: `Lat: ${lat}, Lng: ${lng}`, // Using lat and lng as title
            category: categoryName,  // Store the category (dataset name)
            icon: {
                // Use default Google Maps marker icons and set the color
                url: `http://maps.google.com/mapfiles/ms/icons/${markerColor}-dot.png`
            }
        };
    });

    markersByCategory[categoryName] = markers;
    displayMarkersByCategory(categoryName);  // Display the markers for this category
    createFilters();  // Create the filters based on the datasets
    updateMarkersVisibility(); // Show markers immediately after upload
}

// Create filter checkboxes for each category
const filtersContainer = document.getElementById("filters-container");

function createFilters() {
    filtersContainer.innerHTML = "";  // Clear previous filters
    Object.keys(markersByCategory).forEach(category => {
        let label = document.createElement("label");
        let checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        checkbox.classList.add("filter-checkbox");
        checkbox.id = category.toLowerCase().replace(/\s+/g, "-");
        checkbox.checked = true;  // Make the checkbox checked by default
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
            marker.setMap(isVisible ? map : null);  // Show or hide marker based on visibility
        });
    });
}

// Toggle visibility of all markers by checking/unchecking all checkboxes
function toggleSelectAll() {
    const checkboxes = document.querySelectorAll(".filter-checkbox");
    const allChecked = [...checkboxes].every(checkbox => checkbox.checked);

    checkboxes.forEach(checkbox => {
        checkbox.checked = !allChecked; // Toggle all checkboxes
    });

    updateMarkersVisibility();
}

// Display markers for the selected category
function displayMarkersByCategory(category) {
    if (!map) {
        console.log("Map is not initialized yet.");
        return; // Ensure the map is initialized before adding markers
    }

    // Add markers to the map for the specific category
    markersByCategory[category].forEach(marker => {
        if (!marker.position || isNaN(marker.position.lat) || isNaN(marker.position.lng)) {
            console.log("Invalid marker position:", marker);
            return; // Skip markers with invalid positions
        }

        console.log("Marker data:", marker);

        let infoWindow = new google.maps.InfoWindow({
            content: `<strong>${marker.title}</strong><br>
                      📍 Lat: ${marker.position.lat}, Lng: ${marker.position.lng}`
        });

        marker.addListener("click", () => {
            infoWindow.open(map, marker);
        });
    });
}

