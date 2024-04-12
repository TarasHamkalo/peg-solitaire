const apiUrl = "http://localhost/pegsolitaire/api/game/";
const movesUrl = new URL(apiUrl + "moves?x=?&y=?");

window.addEventListener('DOMContentLoaded', event => {
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            document.body.classList.toggle('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
        });
    }
});


function getBoardDimensions() {
    const boardRows = document.querySelectorAll('.board-row');
    let maxCellsCount = 0;
    for (const row of boardRows) {
        maxCellsCount = Math.max(
            maxCellsCount, row.querySelectorAll('.board-cell').length
        );
    }

    return [
        boardRows.length,
        maxCellsCount
    ];
}


function getMaxBoardSize() {
    const pageWrapper = document.querySelector('#page-content-wrapper');
    const textContainer = document.querySelector('#text-container');
    let availableHeight = window.innerHeight;
    if (textContainer != null) {
        availableHeight -= textContainer.offsetHeight;
    }

    console.log('board size by height is ' + availableHeight);

    return Math.min(window.innerWidth, availableHeight);
}

function resizeCells() {
    const boardSize = getMaxBoardSize() * 0.7;
    console.log(boardSize);
    const dimensions = getBoardDimensions();
    console.log(dimensions);
    const largestDimension = Math.max(dimensions[0], dimensions[1]);
    console.log('setting size to ', Math.ceil(boardSize / largestDimension));
    // const pegs = document.querySelectorAll('.peg');
    const boardCells = document.querySelectorAll('.board-cell');
    boardCells.forEach(peg => {
        peg.style.width = Math.ceil(boardSize / largestDimension) + 'px';
        peg.style.height = Math.ceil(boardSize / largestDimension) + 'px';
    });
}

window.addEventListener('DOMContentLoaded', event => resizeCells());
window.addEventListener('resize', resizeCells);

$(".peg").mouseenter(function () {
    $(".peg:hover").css("animation-name", "in");
})

$(".peg").mouseleave(function () {
    $(".peg").css("animation-name", "out");
})

$(".peg").click(function () {
    const x = this.parentNode.getAttribute("data-x");
    const y = this.parentNode.getAttribute("data-y");
    movesUrl.searchParams.set("x", x);
    movesUrl.searchParams.set("y", y);
    console.log(movesUrl)
    $.ajax({
        url: movesUrl,
        type: "GET",
        success: function (moves) {
            renderMoves(moves);
        }
    })
})

function renderMoves(moves) {
    const targets = Array.from(
        document.querySelectorAll(".possible-move")
    );

    moves.forEach(move => {
        targets.push(
            document.querySelector(`[data-x="${move[0]}"][data-y="${move[1]}"]`)
        );
    });

    targets.forEach(target => target.classList.toggle("possible-move"));
}

