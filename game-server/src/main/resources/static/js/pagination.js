const pagesCount = document.querySelectorAll('.social-container').length;
let page = 1;


function changePageFromTo(from, to) {
    document.querySelector(`[data-page="${from}"]`)
        .classList.toggle('visually-hidden');
    document.querySelector(`[data-page="${to}"]`)
        .classList.toggle('visually-hidden');
}

$(document).ready(function () {
    $('#pg-next').on('click', function () {
        console.log(page)
        if (page < pagesCount) {
            changePageFromTo(page, page + 1);
            page++;
        }
    })

    $('#pg-prev').on('click', function () {
        console.log(page)
        if (page > 1) {
            changePageFromTo(page, page - 1);
            page--;
        }
    })

    $('#refresh').on('click', function () {
        console.log("refresh " + page)
        switch (page) {
            case 1:
                retrieveCommentList();
        }
    })
})
