let timer,
element = document.getElementById('timer');
scrambleElement = document.getElementById('scramble');

let timerStartedNow = false,
timerStoppedNow = false,
anyKeyPressed = false;

document.onload = function() {
    scrambleElement.innerText = getRandomScramble(document.getElementById('cube'));
}

minimizeScrambleSizeIfNeed();


function minimizeScrambleSizeIfNeed() {
    if (scrambleElement.innerText.split(' ').length > 40)
        scrambleElement.classList.add('mini');
}


function startTimer() {
    scrambleElement.classList.add('mini');

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

function getRandomScramble(cube) {
    const url = "https://rubiks-cube-scramble-generator.onrender.com/api?puzzleType=" + cube,
    headersParam = {
        'Content-Type': 'application/json',
        "Access-Control-Allow-Origin": '*',
        "Access-Control-Allow-Credentials" : true,
    }

    return fetch(url, {
        method: 'GET',
        headers: headersParam,
        mode: 'no-cors'
    }).then(response => {
        return JSON.stringify(response);
    })
}


document.addEventListener('keyup', function(event) {
    if (event.code == 'Space' && (!timerStartedNow) && anyKeyPressed) {
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

        // Make string body <scramble>,<time>,<cube>
        let body = scramble.innerText + ',' + timer.innerText + 
        ',' + cube.innerText;

        sendPost('http://localhost:8080/timer', JSON.stringify(body)).then(data => console.log(data));
        location.reload();
    }
    // Timer ready to start
    else if (event.code == 'Space' && (!timerStartedNow))
        element.classList.add('ready')


});


