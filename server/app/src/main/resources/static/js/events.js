const pegs = $(".peg");

pegs.mouseenter(function () {
    $(".peg:hover").css("animation-name", "in");
})

pegs.mouseleave(function () {
    $(".peg").css("animation-name", "out");
})

pegs.click(function (event) {
    event.stopPropagation();

    const x = this.parentNode.getAttribute("data-x");
    const y = this.parentNode.getAttribute("data-y")
    selectUrl.searchParams.set("x", x);
    selectUrl.searchParams.set("y", y);

    requestSelect(this.parentNode);
})

$(".board-cell").click(function () {
    const selected = document.querySelector(".selection");
    if (selected != null && selected.parentNode !== this) {
        const x = this.getAttribute("data-x");
        const y = this.getAttribute("data-y")
        moveUrl.searchParams.set("x", x);
        moveUrl.searchParams.set("y", y);

        console.log(this)
        requestMove(this);
    }
})

$("#sidebarToggle").click(function (event) {
    event.preventDefault();
    document.body.classList.toggle('sb-sidenav-toggled');
});
