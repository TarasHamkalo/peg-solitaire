const apiUrl = "http://localhost/pegsolitaire";
const movesUrl = new URL(apiUrl + "/moves");
const selectUrl = new URL(apiUrl + "/select?x=0&y=0");
const moveUrl = new URL(apiUrl + "/move?x=0&y=0");
const stateUrl = new URL(apiUrl + "/state");
const newGameUrl = new URL(apiUrl + "/new");

function requestSelect(toSelect) {
    let selected = document.querySelector(".selection");
    if (selected != null) {
        selected.parentNode.removeChild(selected);
    }

    $.ajax({
        url: selectUrl,
        type: "POST",
        success: function () {
            toSelect.appendChild(selection)
            requestMoves();
        }
    })
}

function requestState() {
    $.ajax({
        url: stateUrl,
        type: "GET",
        success: function (data) {
            console.log(data)
            if (data.reload) {
                console.log("reloading")
                location.reload();
            }
        }
    })
}

function requestMove(boardCell) {
    $.ajax({
        url: moveUrl,
        type: "POST",
        success: function () {
            renderMove(boardCell);
            requestState();
        }
    })
}

function requestMoves() {
    $.ajax({
        url: movesUrl,
        type: "GET",
        success: function (moves) {
            renderMoves(moves);
        }
    })
}

function requestNewGame() {
    $.ajax({
        url: newGameUrl,
        type: "POST",
        async: false,
        error: function (xhr, status, error) {
            console.error("Error:", error);
        }
    })
}