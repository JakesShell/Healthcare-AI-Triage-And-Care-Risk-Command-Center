let sharedLocation = { latitude: null, longitude: null };

const $ = (selector) => document.querySelector(selector);
const kpiGrid = $("#kpiGrid");
const caseRows = $("#caseRows");
const teamRows = $("#teamRows");
const auditRows = $("#auditRows");
const businessImpact = $("#businessImpact");
const resultCard = $("#resultCard");

function badgeClass(level) {
    if (!level) return "";
    if (level.toLowerCase().includes("critical")) return "critical";
    if (level.toLowerCase().includes("high")) return "high";
    return "";
}

function money(value) {
    return new Intl.NumberFormat("en-US", { style: "currency", currency: "USD", maximumFractionDigits: 0 }).format(value);
}

async function loadDashboard() {
    const response = await fetch("/api/dashboard");
    const data = await response.json();
    const summary = data.summary;

    kpiGrid.innerHTML = `
        <div class="kpi"><span>Total Cases</span><strong>${summary.totalCases}</strong></div>
        <div class="kpi"><span>High Risk</span><strong>${summary.highRiskCases}</strong></div>
        <div class="kpi"><span>Overdue Reviews</span><strong>${summary.overdueReviews}</strong></div>
        <div class="kpi"><span>Average Risk</span><strong>${summary.averageRiskScore}</strong></div>
        <div class="kpi"><span>Monthly Exposure</span><strong>${money(summary.estimatedMonthlyRiskExposure)}</strong></div>
    `;

    caseRows.innerHTML = data.cases.map(item => `
        <tr>
            <td><strong>${item.caseId}</strong><br><span class="muted">${item.patientAlias}, Age ${item.age}</span></td>
            <td>${item.conditionCategory}</td>
            <td><span class="badge ${badgeClass(item.triageLevel)}">${item.riskScore} - ${item.triageLevel}</span></td>
            <td>${item.readmissionProbability}%</td>
            <td>${item.assignedCareTeam}</td>
            <td>${item.escalationStatus}</td>
        </tr>
    `).join("");

    teamRows.innerHTML = data.teams.map(team => `
        <div class="team-item">
            <div class="team-header">
                <strong>${team.teamName}</strong>
                <span class="badge ${team.capacityPressure >= 85 ? "critical" : team.capacityPressure >= 70 ? "high" : ""}">${team.capacityPressure}% Capacity</span>
            </div>
            <div class="muted">${team.assignedCases} cases | ${team.highRiskCases} high-risk | ${team.overdueReviews} overdue</div>
            <div class="progress"><div style="width:${team.capacityPressure}%"></div></div>
            <p>${team.recommendation}</p>
        </div>
    `).join("");

    businessImpact.innerHTML = Object.entries(data.businessImpact).map(([key, value]) => `
        <div class="impact-item">
            <strong>${value}</strong>
            <span class="muted">${key.replace(/([A-Z])/g, " $1").replace(/^./, c => c.toUpperCase())}</span>
        </div>
    `).join("");

    auditRows.innerHTML = data.audit.map(event => `
        <div class="audit-item">
            <div class="audit-header">
                <strong>${event.eventId} | ${event.caseId}</strong>
                <span class="muted">${event.timestamp}</span>
            </div>
            <div>${event.action}</div>
            <div class="muted">${event.reviewer} - ${event.decision}</div>
        </div>
    `).join("");
}

function setupSymptomChips() {
    document.querySelectorAll("#symptomChips button").forEach(button => {
        button.addEventListener("click", () => {
            button.classList.toggle("active");
            const textArea = $("#symptoms");
            const symptom = button.dataset.symptom;
            const current = textArea.value.trim();
            if (button.classList.contains("active")) {
                textArea.value = current ? `${current}, ${symptom}` : symptom;
            }
        });
    });
}

function setupLocation() {
    $("#locationButton").addEventListener("click", () => {
        const status = $("#locationStatus");
        if (!navigator.geolocation) {
            status.textContent = "Geolocation is not available in this browser.";
            return;
        }
        status.textContent = "Requesting location permission...";
        navigator.geolocation.getCurrentPosition(position => {
            sharedLocation = {
                latitude: Number(position.coords.latitude.toFixed(6)),
                longitude: Number(position.coords.longitude.toFixed(6))
            };
            status.textContent = `Location ready: ${sharedLocation.latitude}, ${sharedLocation.longitude}`;
        }, () => {
            status.textContent = "Location permission was not granted. Maps will still open with a nearby search.";
        });
    });
}

function formValue(id) {
    return $(id).value;
}

function checked(id) {
    return $(id).checked;
}

function setupTriageForm() {
    $("#triageForm").addEventListener("submit", async (event) => {
        event.preventDefault();

        const payload = {
            symptoms: formValue("#symptoms"),
            age: Number(formValue("#age")),
            raceEthnicity: formValue("#raceEthnicity"),
            feverC: Number(formValue("#feverC")),
            symptomDurationDays: Number(formValue("#symptomDurationDays")),
            painLevel: Number(formValue("#painLevel")),
            smokingStatus: formValue("#smokingStatus"),
            medicationAdherence: Number(formValue("#medicationAdherence")),
            missedFollowUps: Number(formValue("#missedFollowUps")),
            chronicConditions: formValue("#chronicConditions"),
            multipleRecentPartners: checked("#multipleRecentPartners"),
            sexualHealthSymptoms: checked("#sexualHealthSymptoms"),
            pregnantOrPostpartum: checked("#pregnantOrPostpartum"),
            recentTravel: checked("#recentTravel"),
            notes: formValue("#notes"),
            latitude: sharedLocation.latitude,
            longitude: sharedLocation.longitude
        };

        const response = await fetch("/api/triage", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            resultCard.innerHTML = `<div class="callout danger">Please enter symptoms before running the triage simulation.</div>`;
            return;
        }

        const result = await response.json();
        renderResult(result);
    });
}

function renderResult(result) {
    resultCard.innerHTML = `
        <div class="section-heading">
            <span>02</span>
            <div>
                <h2>Triage Interpretation & Geo-Care Referral</h2>
                <p>Assessment ${result.assessmentId} | ${new Date(result.createdAt).toLocaleString()}</p>
            </div>
        </div>
        <div class="result-stack">
            <div class="result-top">
                <div class="result-metric"><span>Possible Category</span><strong>${result.possibleConditionCategory}</strong></div>
                <div class="result-metric"><span>Triage Level</span><strong>${result.triageLevel}</strong></div>
                <div class="result-metric"><span>Risk Score</span><strong>${result.riskScore}/100</strong></div>
                <div class="result-metric"><span>Data Confidence</span><strong>${result.confidenceLabel}</strong></div>
            </div>
            <div class="risk-bar"><div class="risk-fill" style="width:${result.riskScore}%"></div></div>
            <div class="callout ${result.redFlags.length ? "danger" : ""}">
                <strong>Recommended Care Type:</strong> ${result.recommendedCareType}<br>
                ${result.carePathway}
                <br><a class="map-link" href="${result.googleMapsSearchUrl}" target="_blank" rel="noopener">Open Nearby Care In Google Maps</a>
            </div>
            <div class="callout ${result.redFlags.length ? "danger" : ""}"><strong>Safety Notice:</strong> ${result.emergencyNotice}</div>
            <div class="callout"><strong>Reasoning:</strong><ul>${result.reasoning.map(reason => `<li>${reason}</li>`).join("")}</ul></div>
            <div class="callout"><strong>Red Flags:</strong> ${result.redFlags.length ? result.redFlags.join(", ") : "No red-flag symptoms were detected by this simulation."}</div>
            <div class="callout"><strong>Equity Note:</strong> ${result.equityNote}</div>
        </div>
    `;
}

setupSymptomChips();
setupLocation();
setupTriageForm();
loadDashboard();
