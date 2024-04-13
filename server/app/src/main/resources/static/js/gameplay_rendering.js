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

