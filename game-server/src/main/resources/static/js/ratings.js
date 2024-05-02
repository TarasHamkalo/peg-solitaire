function retrieveAvgRating() {
    requestWithAuthentication(
        ratingsApiUrl + "/" + gameName,
        "GET",
        (response) => {
            response.forEach(avg => {
                console.log(avg);
            })
        }
    )
}

function retrieveAvgRating() {
    requestWithAuthentication(
        ratingsApiUrl + "/" + gameName,
        "GET",
        (response) => {
            response.forEach(avg => {
                console.log(avg);
            })
        }
    )
}

function postRating(points) {
    requestWithAuthentication(
        ratingsApiUrl + "/" + gameName,
        "POST",
        (response) => {
            response.forEach(avg => {
                console.log(avg);
            })
        }
    )
}

const stars = document.querySelectorAll(".stars");
stars.forEach((star, i) => {
    star.addEventListener("click", () => {
        postRating(i);
        stars.forEach((other, j) => {
            if (i >= j) {
                other.classList.add("active");
            } else {
                other.classList.remove("active");
            }
        });
    });
});
