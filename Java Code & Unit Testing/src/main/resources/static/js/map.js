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
                title: `Latitude: ${lat}\nLongitude: ${lng}`,
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
            errorInDataset += 1;
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
                title: `Latitude: ${lat}\nLongitude: ${lng}`,
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
        let filterDiv = document.createElement("div");
        filterDiv.classList.add("filter-item");

        // Checkbox for toggling visibility
        let checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        checkbox.classList.add("filter-checkbox");
        checkbox.id = category.toLowerCase().replace(/\s+/g, "-");
        checkbox.checked = true;
        checkbox.addEventListener("change", updateMarkersVisibility);

        let label = document.createElement("label");
        label.appendChild(checkbox);
        label.appendChild(document.createTextNode(` ${category}`));

        // Upload button
        let uploadBtn = document.createElement("button");
        uploadBtn.textContent = "Upload";
        uploadBtn.classList.add("upload-btn");
        uploadBtn.dataset.category = category;
        uploadBtn.addEventListener("click", function () {
            uploadDataset(category);
        });

        filterDiv.appendChild(label);
        filterDiv.appendChild(uploadBtn);
        filtersContainer.appendChild(filterDiv);
    });
}

function uploadDataset(categoryName) {
    const markers = markersByCategory[categoryName];

    if (!markers || markers.length === 0) {
        alert("No data available for upload.");
        return;
    }

    // These can be dynamic from user session or form inputs
    const uploadedBy = "admin@bradford.gov.uk";
    const role = "Admin";
    const department = "Geography";

    const firstMarker = markers[0];
    const infoWindowContent = firstMarker.getTitle();
    let columns = [];
    let sampleData = {};

    if (infoWindowContent.includes("\n")) {
        let rows = infoWindowContent.split("\n");
        rows.forEach(row => {
            let [key, value] = row.split(":").map(item => item.trim());
            if (key && value) {
                columns.push(key.toLowerCase());
                sampleData[key.toLowerCase()] = value;
            }
        });
    }

    const datasetData = markers.map(marker => {
        let markerInfo = marker.getTitle().split("\n");
        let rowData = {};

        markerInfo.forEach(row => {
            let [key, value] = row.split(":").map(item => item.trim());
            if (key && value) {
                rowData[key.toLowerCase()] = value;
            }
        });

        return rowData;
    });

    const requestBody = {
        datasetName: categoryName,
        department: department,
        uploadedBy: uploadedBy,
        role: role,
        columns: columns,
        data: datasetData
    };

    console.log("Uploading dataset:", requestBody);

    fetch("/datasets/upload", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(requestBody)
    })
        .then(response => {
            if (!response.ok) throw new Error("Upload failed");
            return response.text();
        })
        .then(message => alert(message))
        .catch(error => {
            console.error("Error uploading dataset:", error);
            alert("Error uploading dataset: " + error.message);
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