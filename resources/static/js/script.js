let timer,
element = document.getElementById('timer');
scrambleElement = document.getElementById('scramble');

let timerStartedNow = false,
timerStoppedNow = false,
anyKeyPressed = false;

minimizeScrambleSizeIfNeed();

function minimizeScrambleSizeIfNeed() {
    if (scrambleElement.innerText.split(' ').length > 40)
        scrambleElement.classList.add('mini');
}

function startTimer() {
    let time = 0;

    timer = setInterval(() => {

    let minutes = Math.floor(time / (60 * 100)),
    seconds = Math.floor((time / 100) % 60);
    millis = time % 100;

    element.innerHTML = (minutes < 10 ? minutes == 0 ? "" : "0" + minutes + ":" : minutes + ":") +
                (seconds < 10 ? "0" + seconds + ":" : seconds + ":") +
                (millis < 10 ? "0" + millis : millis);
    time++;
    }, 10);
}

function pause() {
    clearInterval(timer)
}

function sendPost(url, body = null) {
    const headersParam = {
        'Content-Type': 'application/json',
        "Access-Control-Allow-Origin": '*',
        "Access-Control-Allow-Credentials" : true,
    }

    return fetch(url, {
        method: 'POST',
        body: body,
        headers: headersParam,
        mode: 'no-cors'
    }).then(response => {
        return response;
    })
}

document.addEventListener('keyup', function(event) {
    if (event.code == 'Space' && (!timerStartedNow) && anyKeyPressed) {
        timerOnFullScreen()

        startTimer();

        timerStartedNow = true;
    } else
        timerStartedNow = false;

    element.classList.remove('ready');
});

document.addEventListener('keydown', function(event) {
    if (event.code == 'Space')
        anyKeyPressed = true;
    // Timer stopped
    if (event.code == 'Space' && timerStartedNow) {

        let scramble = document.getElementById('scramble'),
        timer = document.getElementById('timer'),
        cube = document.getElementById('cube');

        pause();
        element.classList.remove('ready');

        // Add last solve time to cookie
        document.cookie = `lastSolve=${timer.innerText}; max-age=${365 * 24 * 60 * 60}`

        // Make string body <scramble>,<time>,<cube>
        let body = scramble.innerText + ',' + timer.innerText + 
        ',' + cube.innerText;

        if (!findSaveSolveCookie() && scrambleElement.innerText !== "Failed to get scramble")
            sendPost('http://localhost:8080/timer', JSON.stringify(body)).then(data => console.log(data));
        
        location.reload();
    }
    // Timer ready to start
    else if (event.code == 'Space' && (!timerStartedNow))
        element.classList.add('ready')
});

function findSaveSolveCookie() {
    const cookies = document.cookie.split(';')

    for (let cookie of cookies) {
        const cookieValue = cookie.split('=').at(-1)  // Get value: 'true'/'false'

        if (cookie.includes('saveSolve')) 
            return JSON.parse(cookieValue)
    }

    return false
}

function timerOnFullScreen() {
    const cookies = document.cookie.split(';')

    for (let cookie of cookies) {
        if (cookie.includes('timerFullScreen')) {
            const cookieValue = cookie.split('=').at(-1)  // Get value: 'true'/'false'

            // Find all elements to hide
            const navBarElement = document.querySelector('.header'),
            scrambleElement = document.getElementById('scramble'),
            averageElement = document.querySelector('.avg'),
            timerElement = document.getElementById('timer')

            const timerCheckBox = document.getElementById('timerFullScreen-box')

            if (JSON.parse(cookieValue)) {
                timerCheckBox.checked = true

                // Hide all elements
                navBarElement.classList.add('hide')
                scrambleElement.classList.add('hide')
                averageElement.classList.add('hide')
                
                timerElement.classList.add('max')
            } else {
                timerCheckBox.checked = false

                // Hide all elements
                navBarElement.classList.remove('hide')
                scrambleElement.classList.remove('hide')
                averageElement.classList.remove('hide')
                
                timerElement.classList.remove('max')
            }
        }
    }
}