const commentList = document.querySelector(".cards-list");

function retrieveCommentList() {
    $.ajax({
        url: commentsApiUrl + "/" + gameName,
        type: "GET",
        success: function (comments) {
            commentList.innerHTML = "";
            comments.forEach(comment => {
                appendComment(comment.player, comment.text, comment.commentedOn)
            })
        }
    })

    // requestWithAuthentication(
    //     commentsApiUrl + "/" + gameName,
    //     "GET",
    //     (response) => {
    //         commentList.innerHTML = "";
    //         response.forEach(comment => {
    //             appendComment(comment.player, comment.text, comment.commentedOn)
    //         })
    //     }
    // )
}

function postComment() {

    storeUserInfo();

    const data = {
        "text": localStorage.getItem("comment-text"),
        "commentedOn": Date.now()
    }

    requestWithAuthentication(
        commentsApiUrl,
        "POST",
        () => {
            appendComment(localStorage.getItem("username"), data.text, data.commentedOn)
            localStorage.removeItem("comment-text")
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

    document.getElementById("comment-text").value = localStorage.getItem("comment-text");

    $("#post-comment").click(function () {
        let commentText = document.getElementById("comment-text")
        console.log(commentText.value);
        localStorage.setItem("comment-text", commentText.value);
        commentText.value = "";
        postComment()
    })
});