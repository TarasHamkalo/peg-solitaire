const selection = document.createElement("div");
selection.classList.add("selection");

function renderMove(targetBoardCell) {
    const selectedBoardCell = document.querySelector(".selection").parentNode;
    const peg = selectedBoardCell.querySelector(".peg")

    const pegX = Number(selectedBoardCell.getAttribute("data-x"));
    const pegY = Number(selectedBoardCell.getAttribute("data-y"));

    const mx = (pegX +  Number(targetBoardCell.getAttribute("data-x"))) / 2;
    const my = (pegY +  Number(targetBoardCell.getAttribute("data-y"))) / 2;

    console.log(pegX, pegY, mx, my)
    const mCell = document.querySelector(`[data-x="${mx}"][data-y="${my}"]`);

    selectedBoardCell.removeChild(selection);
    selectedBoardCell.removeChild(peg);

    mCell.removeChild(mCell.querySelector(".peg"));

    targetBoardCell.appendChild(peg);

    renderMoves([])
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
