const modalElement = document.getElementById('modal');
const overlayElement = document.getElementById('overlay');
const popupSettingsElements = document.querySelectorAll('.slider.round');

// Close popup
overlayElement.addEventListener('click', () => {
    closeModal()
    location.reload()
});

function closeModal() {
    modalElement.classList.remove('active')
    overlayElement.classList.remove('active')
}

function openModal() {
    modalElement.classList.add('active')
    overlayElement.classList.add('active')
}

function toSeconds(days) {
    return days * 24 * 60 * 60
}

// Set property values
popupSettingsElements.forEach(slider => {
    slider.addEventListener('click', () => {
        // Get element id
        const sliderId = slider.id

        // Find checkbox
        const currentSliderCheckBox = document.getElementById(sliderId + '-box')

        if (!currentSliderCheckBox.checked) {
            // Add cookie
            document.cookie = `${sliderId}=true; max-age=${toSeconds(365)}`
        } else {
            // Remove cookie
            document.cookie = `${sliderId}=false; max-age=0`
        }
    });
});

function getAllCookies() {
    const cookies = document.cookie.split(';')

    for (let cookie of cookies) {
        if (cookie.includes('hideAvg')) {
            cookieValue = cookie.split('=').at(-1)  // Get value: 'true'/'false'

            hideAvg(cookieValue)
        } else if (cookie.includes('saveSolve')) {
            cookieValue = cookie.split('=').at(-1)  // Get value: 'true'/'false'

            doNotSaveSolve(cookieValue)
        } else if (cookie.includes('timerFullScreen')) {
            cookieValue = cookie.split('=').at(-1)  // Get value: 'true'/'false'

            timerOnFullScreenCheck(cookieValue)
        }    
    }
}

function hideAvg(cookieValue) {
    const avgElement = document.querySelector('.avg'),
    avgCheckbox = document.getElementById('hideAvg-box')

    if (JSON.parse(cookieValue)) {
        avgElement.classList.add('hide')
        avgCheckbox.checked = true

    } else {
        avgElement.classList.remove('hide')
        avgCheckbox.checked = false
    }
}

function doNotSaveSolve(cookieValue) {
    const solveCheckbox = document.getElementById('saveSolve-box')

    if (JSON.parse(cookieValue)) 
        solveCheckbox.checked = true
    else 
        solveCheckbox.checked = false
}

function timerOnFullScreenCheck(cookieValue) {
    const timerCheckbox = document.getElementById('timerFullScreen-box')

    if (JSON.parse(cookieValue)) 
        timerCheckbox.checked = true
    else 
        timerCheckbox.checked = false
}

getAllCookies()