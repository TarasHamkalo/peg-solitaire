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


const pegs = $(".peg");

pegs.mouseenter(function () {
    $(".peg:hover").css("animation-name", "in");
})

pegs.mouseleave(function () {
    $(".peg").css("animation-name", "out");
})

const apiUrl = "http://localhost/pegsolitaire/api/game";
const movesUrl = new URL(apiUrl + "/moves");
const selectUrl = new URL(apiUrl + "/select?x=0&y=0");
const moveUrl = new URL(apiUrl + "/move?x=0&y=0");
const stateUrl = new URL(apiUrl + "/state");

let selectedPeg = null;
const selection = document.createElement("div");
selection.classList.add("selection");

function requestSelect(element) {
    if (selectedPeg === element) {
        return;
    }

    if (selectedPeg != null) {
        selectedPeg.parentNode.removeChild(selection);
    }

    $.ajax({
        url: selectUrl,
        type: "POST",
        success: function () {
            selectedPeg = element;
            selectedPeg.parentNode.appendChild(selection)
            requestMoves();
        }
    })
}

function requestState() {
    $.ajax({
        url: stateUrl,
        type: "GET",
        success: function (data) {
            console.log(data)
        }
    })
}

function requestMove(element) {
    $.ajax({
        url: moveUrl,
        type: "POST",
        success: function () {
            renderMove(element);
            requestState();
        }
    })
}

function renderMove(target) {
    const pegX = Number(selectedPeg.parentNode.getAttribute("data-x"));
    const pegY = Number(selectedPeg.parentNode.getAttribute("data-y"));

    const mx = (pegX +  Number(target.getAttribute("data-x"))) / 2;
    const my = (pegY +  Number(target.getAttribute("data-y"))) / 2;

    const mCell = document.querySelector(`[data-x="${mx}"][data-y="${my}"]`);

    selectedPeg.parentNode.removeChild(selection);

    selectedPeg.parentNode.removeChild(selectedPeg);

    mCell.removeChild(mCell.querySelector(".peg"));

    target.appendChild(selectedPeg);

    selectedPeg = null;
    renderMoves([])
}

pegs.click(function (event) {
    event.stopPropagation();

    const x = this.parentNode.getAttribute("data-x");
    const y = this.parentNode.getAttribute("data-y")
    selectUrl.searchParams.set("x", x);
    selectUrl.searchParams.set("y", y);

    requestSelect(this);
})

$(".board-cell").click(function () {
    const x = this.getAttribute("data-x");
    const y = this.getAttribute("data-y")
    moveUrl.searchParams.set("x", x);
    moveUrl.searchParams.set("y", y);

    requestMove(this);
})

function requestMoves() {
    $.ajax({
        url: movesUrl,
        type: "GET",
        success: function (moves) {
            renderMoves(moves);
        }
    })
}

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

