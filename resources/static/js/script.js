let timer;
let element = document.getElementById('timer');

let timerStartedNow = false,
timerStoppedNow = false;

location.onload = function() {
    document.getElementById('scramble').innerText = getRandomScramble(document.getElementById('cube'));
}

function startTimer() {
    let time = 0;

    timer = setInterval(() => {

    let seconds = Math.floor(time / 100);
    let minutes = Math.floor(seconds / 60);
    if (minutes > 0)
        seconds = time % 60;

    let milliseconds = time % 100;

    if (milliseconds < 10 && seconds < 10)
        element.innerHTML = `0${seconds}:0${milliseconds}`;
    else if (milliseconds < 10)
        element.innerHTML = `${seconds}:0${milliseconds}`;
    else if (seconds < 10)
        element.innerHTML = `0${seconds}:${milliseconds}`;
    else if (minutes > 0)
        element.innerHTML = `${minutes}:${seconds}:${milliseconds}`;
    else
        element.innerHTML = `${seconds}:${milliseconds}`;


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
        //"Access-Control-Allow-Methods": "GET, OPTIONS, POST, PUT"
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
    if (event.code == 'Space' && (!timerStartedNow)) {
        startTimer();
        timerStartedNow = true;
    } else
        timerStartedNow = false;

    element.classList.remove('ready');

});

document.addEventListener('keydown', function(event) {
    // Timer stopped
    if (event.code == 'Space' && timerStartedNow) {
        let scramble = document.getElementById('scramble'),
        timer = document.getElementById('timer'),
        cube = document.getElementById('cube');

        pause();
//        location.reload();
        element.classList.remove('ready');

        // Make string body <scramble>,<time>,<cube>
        let body = scramble.innerText + ',' + timer.innerText + 
        ',' + cube.innerText;

        sendPost('http://localhost:8080/timer', JSON.stringify(body)).then(data => console.log(data));

        // scramble.innerText = getRandomScramble("3x3");
    }
    // Timer ready to start
    else if (event.code == 'Space' && (!timerStartedNow))
        element.classList.add('ready')


});


