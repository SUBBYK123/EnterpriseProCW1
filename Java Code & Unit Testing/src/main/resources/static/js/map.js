let map;
let markersByCategory = {}; // Holds markers grouped by dataset name
let uploadedFiles = new Set();
const bradfordCenter = { lat: 53.795, lng: -1.759 };

const markerColors = ["blue", "red", "yellow", "green", "purple"];
let currentColorIndex = 0;

function initMap() {
    map = new google.maps.Map(document.getElementById("map"), {
        center: bradfordCenter,
        zoom: 12
    });
}

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

function handleFileUpload() {
    const fileInput = document.getElementById('fileInput');
    const file = fileInput.files[0];

    if (!file) {
        alert("Please select a file.");
        return;
    }

    const fileName = file.name;
    if (uploadedFiles.has(fileName)) {
        alert("This dataset has already been uploaded.");
        return;
    }

    const reader = new FileReader();
    reader.onload = function (event) {
        const fileContent = event.target.result;
        const fileType = file.name.split('.').pop().toLowerCase();
        const categoryName = file.name.split('.')[0];

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

function parseCSV(csvData, categoryName) {
    const rows = csvData.split("\n").map(row => row.split(",").map(col => col.trim()));
    const headers = rows[0].map(header => header.toLowerCase());

    const latIndex = headers.findIndex(header => header === "latitude");
    const lngIndex = headers.findIndex(header => header === "longitude");
    const xIndex = headers.findIndex(header => header === "x");
    const yIndex = headers.findIndex(header => header === "y");
    const eastingsIndex = headers.findIndex(header => header === "eastings");
    const northingsIndex = headers.findIndex(header => header === "northings");

    // Function to check if at least 90% of a column is valid
    function isValidColumn(index) {
        if (index === -1) return false;
        const validCount = rows.slice(1).filter(row => !isNaN(parseFloat(row[index]))).length;
        return validCount / (rows.length - 1) >= 0.9; // 90% threshold
    }

    // Check which coordinate system is valid
    const isLatLngValid = isValidColumn(latIndex) && isValidColumn(lngIndex);
    const isXYValid = isValidColumn(xIndex) && isValidColumn(yIndex);
    const isEastNorthValid = isValidColumn(eastingsIndex) && isValidColumn(northingsIndex);

    if (!isLatLngValid && !isXYValid && !isEastNorthValid) {
        alert("Dataset upload failed: None of the coordinate columns have at least 90% valid numeric values.");
        return;
    }
    var errorInDataset = 0;
    const markerColor = markerColors[currentColorIndex % markerColors.length];
    currentColorIndex++;

    const newMarkers = rows.slice(1).map(columns => {
        let lat, lng;

        if (isLatLngValid) {
            lat = parseFloat(columns[latIndex]);
            lng = parseFloat(columns[lngIndex]);
        } else if (isXYValid) {
            lat = parseFloat(columns[yIndex]);
            lng = parseFloat(columns[xIndex]);
        } else if (isEastNorthValid) {
            const eastings = parseFloat(columns[eastingsIndex]);
            const northings = parseFloat(columns[northingsIndex]);
            console.log(eastings,northings);
            if (!isNaN(eastings) && !isNaN(northings)) {
            ({ lat, lng } = convertEastingsNorthingsToLatLng(eastings, northings));
            }
        }

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
            console.log(`Error Number ${errorInDataset}`);
        }
    }).filter(marker => marker !== undefined);

    markersByCategory[categoryName] = newMarkers;
    createFilters();
    updateMarkersVisibility();
    alert(`Dataset successfully added with ${errorInDataset} errors.`);
}

// Convert Eastings/Northings to Lat/Lng (UK OSGB36 to WGS84)
function convertEastingsNorthingsToLatLng(eastings, northings) {
    const proj4 = window.proj4;
    if (!proj4) {
        alert("Proj4 library is required for coordinate conversion.");
        return { lat: 0, lng: 0 };
    }

    // UK OSGB36 Projection (EPSG:27700) to WGS84 (EPSG:4326)
    const osgb36 = "+proj=tmerc +lat_0=49 +lon_0=-2 +k=0.9996012717 +x_0=400000 +y_0=-100000 +ellps=airy +datum=OSGB36 +units=m +no_defs";
    const wgs84 = "+proj=longlat +datum=WGS84 +no_defs";

    const [lng, lat] = proj4(osgb36, wgs84, [eastings, northings]);
    return { lat, lng };
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

function toggleSelectAll() {
    const checkboxes = document.querySelectorAll(".filter-checkbox");
    const allChecked = [...checkboxes].every(checkbox => checkbox.checked);

    checkboxes.forEach(checkbox => {
        checkbox.checked = !allChecked;
    });
    updateMarkersVisibility();
}
