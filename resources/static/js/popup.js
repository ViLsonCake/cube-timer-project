const modalElement = document.getElementById('modal');
const overlayElement = document.getElementById('overlay');

// Close popup
overlayElement.addEventListener('click', () => {
    closeModal()
});

function openModal() {
    modalElement.classList.add('active')
    overlayElement.classList.add('active')
}

function closeModal() {
    modalElement.classList.remove('active')
    overlayElement.classList.remove('active')
}