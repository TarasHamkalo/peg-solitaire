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
    const textContainer = document.querySelector('#text-container');
    let availableHeight = window.innerHeight;
    if (textContainer != null) {
        availableHeight -= textContainer.offsetHeight;
    }

    console.log('board size by height is ' + availableHeight);

    return Math.min(window.innerWidth, availableHeight);
}


const boardDimensions = getBoardDimensions();
const largestDimension = Math.max(boardDimensions[0], boardDimensions[1]);

function resizeCells() {
    const boardSize = getMaxBoardSize() * 0.7;
    console.log('Setting size to ', Math.ceil(boardSize / largestDimension));

    const boardCells = document.querySelectorAll('.board-cell');
    boardCells.forEach(peg => {
        peg.style.width = Math.ceil(boardSize / largestDimension) + 'px';
        peg.style.height = Math.ceil(boardSize / largestDimension) + 'px';
    });
}


window.addEventListener('DOMContentLoaded', event => resizeCells());
window.addEventListener('resize', resizeCells);

