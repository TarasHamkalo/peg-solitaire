const commentList = document.querySelector(".cards-list");

function retrieveCommentList() {
    requestWithAuthentication(
        commentsApiUrl + "/" + gameName,
        "GET",
        (response) => {
            commentList.innerHTML = "";
            response.forEach(comment => {
                appendComment(comment.player, comment.text, comment.commentedOn)
            })
        }
    )
}

function postComment(text) {
    const data = {
        "text": text,
        "commentedOn": Date.now()
    }

    requestWithAuthentication(
        commentsApiUrl,
        "POST",
        () => {
            appendComment(localStorage.getItem("username"), data.text, data.commentedOn)
        },
        data
    )
}

function appendComment(player, text, date) {
    let newCard = $("#empty-comment").clone();
    newCard.removeAttr("id")

    newCard.find(".card-header").text(player);
    newCard.find(".card-body").text(text);
    newCard.find(".card-footer").text("Posted on " + new Date(date).getFullYear());

    newCard.appendTo($("#comment-list"));
    newCard.show();
}

$(document).ready(function () {
    $("#empty-comment").hide();
    $("#post-comment").click(function () {
        let commentText = document.getElementById("comment-text")
        postComment(commentText.value)
        commentText.value = "";
    })
});