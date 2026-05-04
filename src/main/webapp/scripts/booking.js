const API_BASE = "/CarCare/api";

async function generateServiceData() {
    try {
        const res = await fetch(`${API_BASE}/services`);
        const services = await res.json();

        const servicesRow = document.querySelector(".booking-packages .row");
        const packageSelect = document.getElementById("package");

        if (!servicesRow || !packageSelect) {
            console.error("booking package area or package dropdown not found");
            return;
        }

        servicesRow.innerHTML = "";
        packageSelect.innerHTML = "";

        const defaultOption = document.createElement("option");
        defaultOption.value = "";
        defaultOption.textContent = "Select a package";
        packageSelect.appendChild(defaultOption);

        const colSize = Math.trunc(12 / services.length);

        services.forEach((service) => {
            const col = document.createElement("div");
            col.className = `col-md-${colSize}`;

            const features = service.features.split(",");
            const featureHTML = features.map(f =>
                `<li class="mb-2"><i class="bi bi-check-circle-fill text-success me-2"></i>${f.trim()}</li>`
            ).join("");

            col.innerHTML = `
                <div class="card package-card h-100">
                    <div class="card-header">${service.icon} ${service.title}</div>
                    <div class="card-body text-start p-4">
                        <span class="package-badge">${service.label}</span>
                        <div class="price-tag mb-3">AED ${service.price} <small>/ service</small></div>
                        <ul class="list-unstyled text-muted small mb-0">
                            ${featureHTML}
                            <li><i class="bi bi-clock me-2"></i>Approx. ${service.duration}</li>
                        </ul>
                    </div>
                </div>
            `;

            servicesRow.appendChild(col);

            const option = document.createElement("option");
            option.value = service.serviceId;
            option.textContent = `${service.icon} ${service.title} — AED ${service.price} (~${service.duration})`;
            packageSelect.appendChild(option);
        });

    } catch (err) {
        console.error("Failed to load services:", err);
    }
}

function addCarTypeMenu() {
    const carTypeMenu = document.getElementById("carType");

    const types = [
        { value: "Sedan", title: "Sedan" },
        { value: "SUV", title: "SUV / Crossover" },
        { value: "Truck", title: "Truck / Pickup" },
        { value: "Van", title: "Van / Minivan" },
        { value: "Coupe", title: "Coupe / Sports" },
        { value: "Other", title: "Other (Mention in notes)" }
    ];

    carTypeMenu.innerHTML = "";

    const defaultOption = document.createElement("option");
    defaultOption.value = "";
    defaultOption.textContent = "Select car type";
    carTypeMenu.appendChild(defaultOption);

    types.forEach((type) => {
        const option = document.createElement("option");
        option.value = type.value;
        option.textContent = type.title;
        carTypeMenu.appendChild(option);
    });
}

function addServiceTimesMenu() {
    const serviceTimesMenu = document.getElementById("time");

    const times = [
        { value: "8AM", title: "8:00 AM" },
        { value: "10AM", title: "10:00 AM" },
        { value: "12PM", title: "12:00 PM" },
        { value: "2PM", title: "2:00 PM" },
        { value: "4PM", title: "4:00 PM" }
    ];

    serviceTimesMenu.innerHTML = "";

    const defaultOption = document.createElement("option");
    defaultOption.value = "";
    defaultOption.textContent = "Select a time";
    serviceTimesMenu.appendChild(defaultOption);

    times.forEach((time) => {
        const option = document.createElement("option");
        option.value = time.value;
        option.textContent = time.title;
        serviceTimesMenu.appendChild(option);
    });
}

function prepareBookingForm() {
    const bookingForm = document.getElementById("bookingForm");
    const bookingSuccess = document.getElementById("bookingSuccess");
    const submitBtn = bookingForm.querySelector(".btn-submit");

    bookingForm.addEventListener("submit", async function(event) {
        event.preventDefault();

        if (!bookingForm.checkValidity()) {
            bookingForm.reportValidity();
            return;
        }

        submitBtn.disabled = true;
        submitBtn.textContent = "Submitting...";

        const formData = new URLSearchParams();
        formData.append("fullName", document.getElementById("name").value.trim());
        formData.append("phone", document.getElementById("phone").value.trim());
        formData.append("email", document.getElementById("email").value.trim());
        formData.append("address", document.getElementById("address").value.trim());
        formData.append("serviceId", document.getElementById("package").value);
        formData.append("carType", document.getElementById("carType").value);
        formData.append("carModel", document.getElementById("carModel").value.trim());
        formData.append("carColor", document.getElementById("carColor").value.trim());
        formData.append("bookingDate", document.getElementById("date").value);
        formData.append("bookingTime", document.getElementById("time").value);
        formData.append("notes", document.getElementById("notes").value.trim());

        try {
            const res = await fetch(`${API_BASE}/bookings`, {
                method: "POST",
                body: formData
            });

            const data = await res.json();

            if (data.success) {
                bookingSuccess.style.display = "flex";
                bookingSuccess.scrollIntoView({ behavior: "smooth" });
                bookingForm.reset();
            } else {
                alert("Something went wrong. Please try again.");
            }

        } catch (err) {
            console.error("Booking submission error:", err);
            alert("Could not connect to server. Please try again.");
        } finally {
            submitBtn.disabled = false;
            submitBtn.textContent = "Submit Booking";
        }
    });
}

console.log("booking.js loaded");

addCarTypeMenu();
addServiceTimesMenu();
prepareBookingForm();
generateServiceData();