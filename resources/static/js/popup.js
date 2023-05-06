const modalElement = document.getElementById('modal');
const overlayElement = document.getElementById('overlay');
const popupSettingsElements = document.querySelectorAll('slider.round');

// Close popup
overlayElement.addEventListener('click', () => {
    closeModal()
});

function closeModal() {
    modalElement.classList.remove('active')
    overlayElement.classList.remove('active')
}

function openModal() {
    modalElement.classList.add('active')
    overlayElement.classList.add('active')
}

function setDateExpires(days) {
    let date = new Date;
    date.setDate(date.getDate() + days)
    return date
}

// Set property values
popupSettingsElements.forEach(slider => {
    slider.addEventListener('click', () => {
        // Get element id
        let sliderId = slider.id

        if (slider.checked) {
            // Add cookie
            document.cookie = `${sliderId}=true; expires=${setDateExpires(365)}`
        } else {
            // Delete cookie
            document.cookie = `${sliderId}=false; expires=-1`            
        }
    });
});