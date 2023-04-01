<<<<<<< HEAD
let timer;
let element = document.getElementById('timer');

let timerStartedNow = false,
timerStopedNow = false;

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


document.addEventListener('keyup', function(event) {
    if (event.code == 'Space' && (!timerStartedNow)) {
        startTimer();
        timerStartedNow = true;
    } else 
        timerStartedNow = false;

    element.classList.remove('ready');

});

document.addEventListener('keydown', function(event) {
    if (event.code == 'Space' && timerStartedNow) {
        pause();
        location.reload();
        element.classList.remove('ready');

        let body = {
            scramble: document.getElementById('scramble').innerText,
            time: document.getElementById('timer').innerText,
            cube: document.getElementById('navbarDropdown').innerText
        }

        sendPost('http://localhost:8080/timer', JSON.stringify(body)).then(data => console.log(data));
    }
    else if (event.code == 'Space' && (!timerStartedNow))
        element.classList.add('ready')


});

=======
let timer;
let element = document.getElementById('timer');

let timerStartedNow = false,
timerStoppedNow = false;

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


document.addEventListener('keyup', function(event) {
    if (event.code == 'Space' && (!timerStartedNow)) {
        startTimer();
        timerStartedNow = true;
    } else
        timerStartedNow = false;

    element.classList.remove('ready');

});

document.addEventListener('keydown', function(event) {
    if (event.code == 'Space' && timerStartedNow) {
        pause();
        location.reload();
        element.classList.remove('ready');

        let body = document.getElementById('scramble').innerText + ',' + document.getElementById('timer').innerText + 
        ',' + document.getElementById('navbarDropdown').innerText;

        sendPost('http://localhost:8080/timer', JSON.stringify(body)).then(data => console.log(data));
    }
    else if (event.code == 'Space' && (!timerStartedNow))
        element.classList.add('ready')


});

>>>>>>> origin/master
