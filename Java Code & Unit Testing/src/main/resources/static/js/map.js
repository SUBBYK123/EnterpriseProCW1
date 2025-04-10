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

    loadManualAssets();
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


function parseCSV(csvData, categoryName) {
    const rows = csvData.split("\n").filter(row => row.trim() !== "");
    if (rows.length < 2) {
        alert("CSV has insufficient data.");
        return;
    }

    const smartCSVsplit = (row) => {
        const values = [];
        let current = '';
        let insideQuotes = false;

        for (let char of row) {
            if (char === '"') {
                insideQuotes = !insideQuotes;
            } else if (char === ',' && !insideQuotes) {
                values.push(current.trim());
                current = '';
            } else {
                current += char;
            }
        }
        values.push(current.trim());
        return values;
    };

    const headers = smartCSVsplit(rows[0]).map(h => h.replace("\uFEFF", "").trim().toLowerCase());

    const latIndex = headers.findIndex(h => ["latitude", "lat", "y"].includes(h));
    const lngIndex = headers.findIndex(h => ["longitude", "lng", "long", "x"].includes(h));
    const eastingIndex = headers.findIndex(h => ["easting", "x"].includes(h));
    const northingIndex = headers.findIndex(h => ["northing", "y"].includes(h));
    const nameIndex = headers.findIndex(h => ["location", "place", "placename", "name", "address", "site", "landmark", "description","country"].includes(h));

    let useEastingNorthing = latIndex === -1 || lngIndex === -1;
    if (useEastingNorthing && (eastingIndex === -1 || northingIndex === -1)) {
        alert("No valid Latitude/Longitude or Easting/Northing columns found.");
        return;
    }

    // Set up projection (British National Grid to WGS84)
    proj4.defs("EPSG:27700", "+proj=tmerc +lat_0=49 +lon_0=-2 " +
        "+k=0.9996012717 +x_0=400000 +y_0=-100000 " +
        "+ellps=airy +datum=OSGB36 +units=m +no_defs");
    proj4.defs("EPSG:4326", "+proj=longlat +datum=WGS84 +no_defs");

    const markerColor = markerColors[currentColorIndex % markerColors.length];
    currentColorIndex++;

    let errorInDataset = 0;

    const newMarkers = rows.slice(1).map(row => {
        const columns = smartCSVsplit(row);
        let lat, lng;

        if (!useEastingNorthing) {
            lat = parseFloat(columns[latIndex]);
            lng = parseFloat(columns[lngIndex]);
        } else {
            const easting = parseFloat(columns[eastingIndex]);
            const northing = parseFloat(columns[northingIndex]);
            if (!isNaN(easting) && !isNaN(northing)) {
                const converted = proj4("EPSG:27700", "EPSG:4326", [easting, northing]);
                lng = converted[0];
                lat = converted[1];
            }
        }

        const name = nameIndex !== -1 ? columns[nameIndex] : "";

        if (!isNaN(lat) && !isNaN(lng)) {
            const marker = new google.maps.Marker({
                position: { lat, lng },
                map: map,
                title: `Latitude: ${lat}\nLongitude: ${lng}`,
                icon: { url: `http://maps.google.com/mapfiles/ms/icons/${markerColor}-dot.png` }
            });

            const infoWindowContent = `
                ${name ? `<strong>Name:</strong> ${name}<br>` : ""}
                <strong>Latitude:</strong> ${lat}<br>
                <strong>Longitude:</strong> ${lng}
            `;

            const infoWindow = new google.maps.InfoWindow({
                content: infoWindowContent
            });

            marker.addListener("click", () => {
                infoWindow.open(map, marker);
            });

            return marker;
        } else {
            errorInDataset++;
        }
    }).filter(marker => marker !== undefined);

    markersByCategory[categoryName] = newMarkers;
    createFilters();
    updateMarkersVisibility();
    alert(`✅ ${categoryName} added to map with ${errorInDataset} skipped rows.`);
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

    // Preserve existing manual asset entries
    const existingManualAssets = Array.from(filtersContainer.querySelectorAll(".asset-sidebar-entry"));

    // Clear only filter checkboxes and buttons, not manual entries
    filtersContainer.innerHTML = "";

    Object.keys(markersByCategory).forEach(category => {
        // ✅ Remove this line: if (category === "Manual Asset") return;

        let filterDiv = document.createElement("div");
        filterDiv.classList.add("filter-item");

        let checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        checkbox.classList.add("filter-checkbox");
        checkbox.id = category.toLowerCase().replace(/\s+/g, "-");
        checkbox.checked = true;
        checkbox.addEventListener("change", updateMarkersVisibility);

        let label = document.createElement("label");
        label.appendChild(checkbox);
        label.appendChild(document.createTextNode(` ${category}`));

        let uploadBtn = document.createElement("button");
        uploadBtn.textContent = "Upload";
        uploadBtn.classList.add("upload-btn");
        uploadBtn.dataset.category = category;
        uploadBtn.addEventListener("click", function () {
            uploadDataset(category);
        });

        filterDiv.appendChild(label);
        filtersContainer.appendChild(filterDiv);
    });

    // Re-append previously created manual asset entries
    existingManualAssets.forEach(asset => filtersContainer.appendChild(asset));
}

function uploadDataset(categoryName) {
    const markers = markersByCategory[categoryName];
    if (!markers || markers.length === 0) {
        alert("No data available for upload.");
        return;
    }

    const uploadedBy = "admin@bradford.gov.uk";
    const role = "Admin";
    const department = "Geography";

    // Infer columns from marker titles
    const firstMarker = markers[0];
    const infoWindowContent = firstMarker.getTitle();
    let columns = [];

    if (infoWindowContent.includes("\n")) {
        let rows = infoWindowContent.split("\n");
        rows.forEach(row => {
            let [key] = row.split(":").map(item => item.trim());
            if (key) {
                columns.push(key.toLowerCase());
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

    // Construct CSV as text
    const csvRows = [];
    csvRows.push(columns.join(","));
    datasetData.forEach(row => {
        const values = columns.map(col => row[col] || "");
        csvRows.push(values.join(","));
    });

    const csvContent = csvRows.join("\n");
    const blob = new Blob([csvContent], { type: "text/csv" });
    const file = new File([blob], categoryName + ".csv", { type: "text/csv" });

    // Use FormData to POST to /datasets/upload-stream
    const formData = new FormData();
    formData.append("file", file);
    formData.append("datasetName", categoryName);
    formData.append("department", department);
    formData.append("uploadedBy", uploadedBy);
    formData.append("role", role);

    fetch("/datasets/upload-stream", {
        method: "POST",
        body: formData
    })
        .then(response => response.text())
        .then(message => {
            console.log("✅", message);
            alert(message);
        })
        .catch(error => {
            console.error("❌ Upload error:", error);
            alert("❌ Error uploading dataset: " + error.message);
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

function loadManualAssets() {
    fetch("/api/assets/Manual Asset")
        .then(res => res.json())
        .then(data => {
            if (!markersByCategory["Manual Asset"]) {
                markersByCategory["Manual Asset"] = [];
            }

            // Clear old markers
            markersByCategory["Manual Asset"].forEach(marker => marker.setMap(null));
            markersByCategory["Manual Asset"] = [];

            data.forEach(asset => {
                const marker = new google.maps.Marker({
                    position: { lat: asset.latitude, lng: asset.longitude },
                    map: map,
                    title: `Name: ${asset.name}\nLatitude: ${asset.latitude}\nLongitude: ${asset.longitude}`,
                    icon: { url: "http://maps.google.com/mapfiles/ms/icons/orange-dot.png" }
                });

                const infoWindow = new google.maps.InfoWindow({
                    content: `<strong>${asset.name}</strong><br>Lat: ${asset.latitude}<br>Lng: ${asset.longitude}`
                });

                marker.assetId = asset.id;
                marker.addListener("click", () => infoWindow.open(map, marker));

                markersByCategory["Manual Asset"].push(marker);
                addAssetToSidebar(asset.id, asset.name, asset.latitude, asset.longitude, marker);
            });

            createFilters();
            updateMarkersVisibility();
        })
        .catch(error => {
            console.error("Error loading manual assets:", error);
        });
}


function addAssetToMap(name, lat, lng) {
    const uploadedBy = document.getElementById("uploadedByHidden")?.value || "anonymous@example.com";
    const asset = {
        datasetName: "Manual Asset",
        name: name,
        latitude: lat,
        longitude: lng,
        createdBy: uploadedBy
    };

    fetch("/api/assets/add", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "same-origin",
        body: JSON.stringify(asset)
    })
        .then(res => res.text())
        .then(msg => {
            console.log(msg);
            if (msg.includes("✅")) {
                loadManualAssets(); // Refresh map with new marker
            } else {
                alert("❌ Failed to add asset: " + msg);
            }
        })
        .catch(err => {
            console.error("API error:", err);
            alert("❌ API failed. Check console.");
        });
}

function addAssetToSidebar(id, name, lat, lng, marker) {
    const container = document.getElementById("filters-container");
    const existing = document.getElementById(id);
    if (existing) existing.remove(); // prevent duplicate entries

    const assetDiv = document.createElement("div");
    assetDiv.id = id;
    assetDiv.className = "asset-sidebar-entry";
    assetDiv.style.marginTop = "5px";

    const text = document.createElement("span");
    text.innerText = `${name}`;

    const editBtn = document.createElement("button");
    editBtn.textContent = "✎";
    editBtn.style.marginLeft = "5px";
    editBtn.onclick = () => {
        document.getElementById("assetName").value = name;
        document.getElementById("assetLat").value = lat;
        document.getElementById("assetLng").value = lng;
        document.getElementById("addAssetModal").dataset.editingId = id;
        document.getElementById("submitAssetBtn").textContent = "Update";
        showAddAssetForm();
    };

    const deleteBtn = document.createElement("button");
    deleteBtn.textContent = "🗑";
    deleteBtn.style.marginLeft = "5px";
    deleteBtn.onclick = () => {
        fetch(`/api/assets/delete/${id}`, { method: "DELETE" })
            .then(res => res.text())
            .then(() => {
                marker.setMap(null);
                assetDiv.remove();
                markersByCategory["Manual Asset"] = markersByCategory["Manual Asset"].filter(m => m.assetId !== id);
                updateMarkersVisibility();
            });
    };

    assetDiv.appendChild(text);
    assetDiv.appendChild(editBtn);
    assetDiv.appendChild(deleteBtn);
    container.appendChild(assetDiv);
}

function submitAsset() {
    const name = document.getElementById("assetName").value;
    const lat = parseFloat(document.getElementById("assetLat").value);
    const lng = parseFloat(document.getElementById("assetLng").value);
    const editingId = document.getElementById("addAssetModal").dataset.editingId;

    if (!name || isNaN(lat) || isNaN(lng)) {
        alert("Please fill in all fields with valid values.");
        return;
    }

    if (editingId) {
        const asset = {
            id: parseInt(editingId),
            name,
            latitude: lat,
            longitude: lng
        };

        fetch("/api/assets/update", {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(asset)
        })
            .then(res => res.text())
            .then(msg => {
                console.log(msg);
                loadManualAssets();
            });

        delete document.getElementById("addAssetModal").dataset.editingId;
        document.getElementById("submitAssetBtn").textContent = "Add";
    } else {
        addAssetToMap(name, lat, lng);
    }

    hideAddAssetForm();
}


function showAddAssetForm() {
    const modal = document.getElementById("addAssetModal");
    if (modal) modal.style.display = "block";
}

function hideAddAssetForm() {
    const modal = document.getElementById("addAssetModal");
    if (modal) modal.style.display = "none";
    document.getElementById("assetName").value = "";
    document.getElementById("assetLat").value = "";
    document.getElementById("assetLng").value = "";
    delete modal.dataset.editingId;
    document.getElementById("submitAssetBtn").textContent = "Add";
}


// Wait until DOM is loaded before attaching the button event
window.addEventListener("DOMContentLoaded", () => {
    const addAssetBtn = document.getElementById("addAssetButton");
    if (addAssetBtn) {
        addAssetBtn.addEventListener("click", showAddAssetForm);
    }
});

function handleBulkUpload() {
    const files = document.getElementById("fileInput").files;
    if (!files.length) {
        alert("Please select at least one file.");
        return;
    }

    const uploadedBy = document.getElementById("uploadedByHidden")?.value || "anonymous@example.com";
    const role = "Admin"; // or fetch dynamically
    const department = "Geography"; // or fetch dynamically

    Array.from(files).forEach(file => {
        const reader = new FileReader();
        reader.onload = function (event) {
            const content = event.target.result;
            const fileType = file.name.split('.').pop().toLowerCase();
            const datasetName = file.name.split('.')[0];

            let columns = [];
            let datasetData = [];

            if (fileType === "csv") {
                const rows = content.trim().split("\n");
                columns = rows[0].split(",").map(h => h.trim());
                datasetData = rows.slice(1).map(row => {
                    const values = row.split(",");
                    const obj = {};
                    columns.forEach((col, i) => obj[col] = values[i]?.trim());
                    return obj;
                });

                parseCSV(content, datasetName); // Show on map
            } else if (fileType === "json") {
                const json = JSON.parse(content);
                if (Array.isArray(json) && json.length > 0) {
                    columns = Object.keys(json[0]);
                    datasetData = json;

                    parseJSON(content, datasetName); // Show on map
                }
            } else {
                alert("Unsupported format: " + file.name);
                return;
            }

            const requestBody = {
                datasetName,
                department,
                uploadedBy,
                role,
                columns,
                data: datasetData
            };

            fetch("/datasets/upload", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(requestBody)
            })
                .then(res => res.text())
                .then(msg => {
                    console.log(`${file.name}: ${msg}`);
                    alert(`${file.name} uploaded successfully`);
                })
                .catch(err => {
                    console.error("Upload error:", err);
                    alert(`Error uploading ${file.name}`);
                });
        };

        reader.readAsText(file);
    });
}



