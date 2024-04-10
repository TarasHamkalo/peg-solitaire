window.addEventListener('DOMContentLoaded', event => {

    // Toggle the side navigation
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        // Uncomment Below to persist sidebar toggle between refreshes
        // if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
        //     document.body.classList.toggle('sb-sidenav-toggled');
        // }
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
    var availableHeight = window.innerHeight;
    if (textContainer != null) {
        availableHeight -= textContainer.offsetHeight;
    }

    console.log('board size by height is ' + availableHeight);

    return Math.min(window.innerWidth, availableHeight);
}

function resizeCells() {
    console.log('I AM CALLED');
    const boardSize = getMaxBoardSize() * 0.8;
    console.log(boardSize);
    const dimensions = getBoardDimensions();
    console.log(dimensions);
    const largestDimension = Math.max(dimensions[0], dimensions[1]);
    console.log('setting size to ', Math.ceil(boardSize / largestDimension));
    // const pegs = document.querySelectorAll('.peg');
    const boardCells = document.querySelectorAll('.board-cell');
    boardCells.forEach(peg => {
        peg.style.width = Math.ceil(boardSize / largestDimension)  + 'px';
        peg.style.height = Math.ceil(boardSize / largestDimension) + 'px';
    });
}

window.addEventListener('DOMContentLoaded', event => resizeCells());
window.addEventListener('resize', resizeCells);

let firstPeg = null;
function toggleSelection(element) {
    console.log(element);
    if (element.classList.contains('selected')) {
        element.classList.remove('selected');
        return;
    }

    if (firstPeg != null) {
        firstPeg.classList.remove('selected');
        firstPeg = null;
        // if move possible then not select
    } else {
        firstPeg = element;
        element.classList.add('selected');
    }
}

$(".peg").mouseenter(function () {
    $(".peg:hover").css("animation-name", "in");
})

$(".peg").mouseleave(function () {
    $(".peg").css("animation-name", "out");
})
