const scoresList = document.querySelector('#scores-list');

function postScore() {
    refreshTokenIfRequired();

    $.ajax({
        url: postGameScore,
        type: "POST",
        data: localStorage.getItem("id_token"),
        async: false
    })
}
function retrieveGameScores() {
    requestWithAuthentication(
        scoresApiUrl + "/" + gameName,
        "GET",
        (response) => {
            scoresList.innerHTML = "";
            response.forEach(score => {
                appendScore(score.player, score.points, score.playedOn)
            })
        }
    )
}

function appendScore(player, points, date) {
    let newCard = $("#empty-score").clone();
    newCard.removeAttr("id")

    newCard.find(".card-header").text(player);
    newCard.find(".card-body").text(points);
    newCard.find(".card-footer").text("Scored on " + new Date(date).getFullYear());

    newCard.appendTo(scoresList);
    newCard.show();
}

$(document).ready(function () {
    $("#empty-score").hide();
})

$('#save-score').click(function () {
    postScore();
    window.setTimeout(function () {
        window.location.href = "http://localhost/";
    }, 1000)
})