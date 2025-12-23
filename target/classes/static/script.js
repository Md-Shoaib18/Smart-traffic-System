async function fetchTrafficStatus() {
    try {
        // Replace with your actual backend endpoint
        const response = await fetch('/status'); 
        const data = await response.json();
        updateUI(data);
    } catch (err) {
        console.error("Failed to fetch traffic status", err);
    }
}

function updateUI(data) {
    // 1. Update Traffic Lights and Timers
    data.lights.forEach((light, i) => {
        const lightEl = document.getElementById(`light-${i}`);
        const timerEl = document.getElementById(`timer-${i}`);
        
        // Handle Orange/Yellow sequence
        let color = light.color.toLowerCase();
        if (color === 'orange' || color === 'yellow') color = '#ffa500';
        
        lightEl.style.backgroundColor = color;
        timerEl.innerText = light.timeLeft || "0";
        
        // Visual feedback: dim timer if light is red
        timerEl.style.color = (color === 'red') ? '#e74c3c' : '#2ecc71';
    });

    // 2. Update Vehicle Positions (Flow Logic)
    data.roads.forEach((road, i) => {
        const container = document.getElementById(`road-${i}`);
        const currentCount = container.children.length;
        const targetCount = road.vehicleCount;

        if (targetCount < currentCount) {
            // Decrease: Remove from the "front" (closest to signal)
            const diff = currentCount - targetCount;
            for (let j = 0; j < diff; j++) {
                if (container.firstChild) {
                    container.removeChild(container.firstChild);
                }
            }
        } else if (targetCount > currentCount) {
            // Increase: Add to the "back" (away from signal)
            const diff = targetCount - currentCount;
            for (let j = 0; j < diff; j++) {
                const vehicle = document.createElement("div");
                vehicle.className = "vehicle";
                container.appendChild(vehicle);
            }
        }
    });
}

// Poll backend every 1 second
setInterval(fetchTrafficStatus, 1000);