function retrieveAvgRating() {
    requestWithAuthentication(
        ratingsApiUrl + "/" + gameName,
        "GET",
        (response) => {
            emojiRain(response)
        }
    )
}

function retrieveRatingForPlayer(player) {
    requestWithAuthentication(
        ratingsApiUrl + "/" + gameName + "/" + player,
        "GET",
        (response) => {
            emojiRain(response)
        }
    )
}

function postRating(stars) {
    requestWithAuthentication(
        ratingsApiUrl,
        "POST",
        () => {
            emojiRain(stars);
        },
        {"stars": stars}
    )
}

const stars = document.querySelectorAll(".stars");
stars.forEach((star, i) => {
    star.addEventListener("click", () => {
        postRating(i + 1);
        stars.forEach((other, j) => {
            if (i >= j) {
                other.classList.add("active");
            } else {
                other.classList.remove("active");
            }
        });
    });
});

$('#search-input').keypress(function (e) {
    if (e.which === 13) {
        let player = document.getElementById('search-input').value;
        if (player.toLowerCase() === "avg") {
            retrieveAvgRating()
        } else {
            retrieveRatingForPlayer(player);
        }
    }
});