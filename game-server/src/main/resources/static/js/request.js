
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
            renderMoveResult(data);

            if (data.reload) {
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


function requestWithAuthentication(url, method, handle) {
    if (userHasToBeAuthenticated()) {
        window.location.href = authorizationUrl.href;
    } else {
        refreshTokenIfRequired();
    }

    const accessToken = localStorage.getItem("access_token");
    if (accessToken) {
        const settings = {
            "url": url,
            "method": method,
            "timeout": 0,
            "headers": {
                "Authorization": "Bearer " + accessToken
            },
        };

        $.ajax(settings).done(function (response, status) {
            if (status  === "success") {
                handle(response);
            }
        });
    }
}
