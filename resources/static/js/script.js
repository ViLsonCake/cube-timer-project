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

    let minutes = Math.floor(time / (60 * 100)),
    seconds = Math.floor((time / 100) % 60);
    millis = time % 100;

    if (minutes == 0) {
        if (seconds < 10 && millis < 10)
            element.innerHTML = `0${seconds}:0${millis}`;
        else if (seconds < 10 && millis >= 10)
            element.innerHTML = `0${seconds}:${millis}`;
        else if (seconds >= 10 && millis < 10)
            element.innerHTML = `${seconds}:0${millis}`;
        else if (seconds >= 10 && millis >= 10)
            element.innerHTML = `${seconds}:${millis}`;
    } else {
        if (minutes < 10) {
            if (seconds < 10 && millis < 10)
                element.innerHTML = `0${minutes}:0${seconds}:0${millis}`;
            else if (seconds < 10 && millis >= 10)
                element.innerHTML = `0${minutes}:0${seconds}:${millis}`;
            else if (seconds >= 10 && millis < 10)
                element.innerHTML = `0${minutes}:${seconds}:0${millis}`;
            else if (seconds >= 10 && millis >= 10)
                element.innerHTML = `0${minutes}:${seconds}:${millis}`;
        } else {
            if (seconds < 10 && millis < 10)
                element.innerHTML `${minutes}:0${seconds}:0${millis}`;
            else if (seconds < 10 && millis >= 10)
                element.innerHTML = `${minutes}:0${seconds}:${millis}`;
            else if (seconds >= 10 && millis < 10)
                element.innerHTML = `${minutes}:${seconds}:0${millis}`;
            else if (seconds >= 10 && millis >= 10)
                element.innerHTML = `${minutes}:${seconds}:${millis}`;
        }
    }


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
        element.classList.remove('ready');

        // Make string body <scramble>,<time>,<cube>
        let body = scramble.innerText + ',' + timer.innerText + 
        ',' + cube.innerText;

        sendPost('http://localhost:8080/timer', JSON.stringify(body)).then(data => console.log(data));
        setInterval(200);
        location.reload();
        location.reload();

        // scramble.innerText = getRandomScramble("3x3");
    }
    // Timer ready to start
    else if (event.code == 'Space' && (!timerStartedNow))
        element.classList.add('ready')


});


