function retrieveCommentList() {
    requestWithAuthentication(
        commentsApiUrl + "/" + gameName,
        "GET",
        (response) => {
            response.forEach(comment => {
                appendComment(comment.player, comment.text, comment.commentedOn)
            })
        }
    )
}

function appendComment(player, text, date) {
    let newCard = $("#empty-comment").clone();
    newCard.removeAttr("id")

    newCard.find(".card-header").text(player);
    newCard.find(".card-body").text(text);
    newCard.find(".card-footer").text("Posted on " + new Date(date).getFullYear());

    newCard.appendTo($(".comment-list"));
    newCard.show();
}

$(document).ready(function () {
    $("#empty-comment").hide();
});