
function retrieveAvgRating() {
    $.ajax({
        url: ratingsApiUrl + "/" + gameName,
        type: "GET",
        success: function (rating) {
            emojiRain(rating)
        }
    })

    // requestWithAuthentication(
    //     ratingsApiUrl + "/" + gameName,
    //     "GET",
    //     (response) => {
    //         emojiRain(response)
    //     }
    // )
}

function retrieveRatingForPlayer(player) {
    $.ajax({
        url: ratingsApiUrl + "/" + gameName + "/" + player,
        type: "GET",
        success: function (rating) {
            stars.forEach((star, i) => {
                if (i < rating) {
                    star.classList.add("active")
                } else {
                    star.classList.remove("active")
                }
            })
            emojiRain(rating)
        }
    })

    // requestWithAuthentication(
    //     ratingsApiUrl + "/" + gameName + "/" + player,
    //     "GET",
    //     (response) => {
    //         emojiRain(response)
    //     }
    // )
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

function showCurrentPlayerRating() {
    if (localStorage.getItem("username") != null) {
        retrieveRatingForPlayer(localStorage.getItem("username"));
    }
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

