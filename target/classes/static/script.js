async function fetchTrafficStatus() {
    try {
        const response = await fetch('/status');
        const data = await response.json();
        updateUI(data);
    } catch (err) {
        console.error("Connection Error:", err);
    }
}

async function triggerEmergency(roadId) {
    try {
        await fetch(`/status/emergency/${roadId}`, { method: 'POST' });
    } catch (e) { console.error(e); }
}

function updateUI(data) {
    // 1. UPDATE LIGHTS
    data.lights.forEach((light, i) => {
        const lightEl = document.getElementById(`light-${i}`);
        const timerEl = document.getElementById(`timer-${i}`);
        
        // Color mapping
        let color = light.color.toLowerCase(); 
        let displayColor = '#e74c3c'; 
        
        if (color === 'green') displayColor = '#2ecc71';
        else if (color === 'yellow' || color === 'orange') displayColor = '#f39c12';
        
        lightEl.style.backgroundColor = displayColor;
        timerEl.innerText = light.timeLeft;
        timerEl.style.color = displayColor;

        // Emergency Pulse Effect
        if (displayColor === '#2ecc71' && light.timeLeft < 5) {
            lightEl.style.boxShadow = `0 0 20px ${displayColor}`;
        } else {
            lightEl.style.boxShadow = 'none';
        }
    });

    // 2. UPDATE VEHICLES (The Movement Logic)
    data.roads.forEach((road, i) => {
        const lane = document.getElementById(`road-${i}`);
        const currentCars = lane.children.length;
        const backendCount = road.vehicleCount;

        if (backendCount < currentCars) {
            const carsToRemove = currentCars - backendCount;
            for (let k = 0; k < carsToRemove; k++) {
                if (lane.firstElementChild) {
                    lane.removeChild(lane.firstElementChild);
                }
            }
        } 
        else if (backendCount > currentCars) {
            const carsToAdd = backendCount - currentCars;
            for (let k = 0; k < carsToAdd; k++) {
                const car = document.createElement('div');
                car.className = 'vehicle';
                lane.appendChild(car); 
            }
        }
    });
}
// Run every 500ms for smoother updates
setInterval(fetchTrafficStatus, 500);