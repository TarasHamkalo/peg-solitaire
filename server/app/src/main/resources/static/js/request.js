const apiUrl = "http://localhost/pegsolitaire";
const movesUrl = new URL(apiUrl + "/moves");
const selectUrl = new URL(apiUrl + "/select?x=0&y=0");
const moveUrl = new URL(apiUrl + "/move?x=0&y=0");
const stateUrl = new URL(apiUrl + "/state");

let selectedPeg = null;
const selection = document.createElement("div");
selection.classList.add("selection");

function requestSelect(element) {
    if (selectedPeg === element) {
        return;
    }

    if (selectedPeg != null) {
        selectedPeg.parentNode.removeChild(selection);
    }

    $.ajax({
        url: selectUrl,
        type: "POST",
        success: function () {
            selectedPeg = element;
            selectedPeg.parentNode.appendChild(selection)
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
        }
    })
}

function requestMove(element) {
    $.ajax({
        url: moveUrl,
        type: "POST",
        success: function () {
            renderMove(element);
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
